desc 'Setup values from Keystore for pipeline'
task :'setup:vars' do
  docker = Pipeline::Docker
  persistent = Pipeline::State.persistent
  keystore = Pipeline::Keystore
  deploy = Pipeline::State::Hash.new nest: 'deploy'

  deploy['vpc'] = keystore.query?('VPC_ID')
  deploy['subnets'] = []
  [1, 2, 3].each do |subnet_number|
    deploy['subnets'].push keystore.query?("PRIVATE_SUBNET_#{subnet_number}")
  end

  docker.dockerfile_subs['DOCKER_JAVA_IMAGE'] = \
    keystore.query?('VIS_JAVA_DOCKER_TAG')

  docker.repo = keystore.query?('VIS_PRIVATE_DOCKER_REGISTRY_URL')
  ecs_cluster = keystore.query?('VIS_INTERNAL_NONPROD_ECS_CLUSTER_NAME')

  Pipeline::Templates::CloudFormation.persistent['params'].merge!(
    'Cluster' => ecs_cluster,
    'ClusterSize' => '1',
    'ElbSecurityGroup' => keystore.query?('VIS_ELB_ECS_SECURITY_GROUP_ID'),
    'PipelineInstanceId' => Pipeline.pipeline_instance_id,
    'Vpc' => persistent['deploy']['vpc']
  )
end
