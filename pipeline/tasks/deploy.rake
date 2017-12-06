# The deploy:ecs task sets necessary parameters for
# CFN ECS stacks (based on environment being deployed)
# then invokes the deploy:cfn task to do the deployment
desc 'Deploy ECS service'
task :"deploy:ecs" do
  # Do not attempt to deploy outside of the CI system
  next unless Pipeline.in_ci
  docker = Pipeline::Docker
  cfn = Pipeline::Templates::CloudFormation
  # :dev -> 'DEV'
  deploy_environment = Pipeline::Tasks.current_task.to_s.upcase
  cfn.persistent['deploy_environment'] = deploy_environment
  cfn.persistent['params']['ClusterEnv'] = deploy_environment

  # Handle prod/nonprod
  if ENV['docker_image'].nil?
    # Nonprod docker image is pulled from pipeline
    puts docker.persistent.inspect
    image = docker.persistent['pushed_images'] \
                             ['vis_jira_metrics']
    cfn.persistent['params']['VisJiraMetricsDockerImage'] = \
      docker.repo + '/' + image
  else
    # Production docker_image is supplied from jenkins
    cfn.persistent['params']['VisJiraMetricsDockerImage'] = \
      ENV['docker_image']
  end

  load_balancer = cfn.get_stack_outputs(
    deploy_environment + '-' + 'VIS-JIRA-METRICS-ELB'
  )
  cfn.persistent['params']['LoadBalancer'] = load_balancer['LoadBalancerName']
  cfn.stack_name = deploy_environment + '-VIS-JIRA-METRICS'
  # Necessary preqs for an ECS CFN deployment set, invoke
  # tasks
  Rake::Task['deploy:cfn:jira_metrics_ecs_service'].reenable
  Rake::Task['deploy:cfn:jira_metrics_ecs_service'].invoke
end

desc 'Deploy ELB for service'
task :"deploy:elb" do
  # Do not attempt to deploy outside of the CI system
  next unless Pipeline.in_ci
  # :dev -> 'DEV'
  deploy_environment = Pipeline::Tasks.current_task.to_s.upcase
  Pipeline::Templates::CloudFormation.persistent['deploy_environment'] = \
    deploy_environment
  Pipeline::Templates::CloudFormation.stack_name = \
    deploy_environment + '-' + 'VIS-JIRA-METRICS-ELB'

  Pipeline::Templates::CloudFormation.persistent['params'].merge!(
    'AsgSubnets' => Pipeline::State.persistent['deploy']['subnets']
                                   .join(','),
    'CertName' => Pipeline::Keystore.query?(
      'VIS_EVERIFY_ENROLLMENT_DATA_SERVICE_SSL_CERT'
    )
  )

  Pipeline.logger.debug \
    "Pipeline::State.persistent = #{Pipeline::State.persistent.inspect}"
  Rake::Task['deploy:cfn:jira_metrics_elb'].reenable
  Rake::Task['deploy:cfn:jira_metrics_elb'].invoke
end

# Create the deploy:ecs:dev and deploy:ecs:unstable subtasks
Pipeline::Tasks.subtask(:'deploy:ecs', %i[dev unstable prod])

# Create the deploy:elb:dev and deploy:elb:unstable subtasks
Pipeline::Tasks.subtask(:'deploy:elb', %i[dev unstable prod])
