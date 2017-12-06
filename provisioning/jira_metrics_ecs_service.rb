require 'vis_sparklepack'

root_pack = SparklePack.new(name: 'vis_sparklepack')
# rubocop:disable Style/Lambda

sfn = SparkleFormation.new(:vis_jira_metrics_service_ecs,
                           provider: :aws) do
  description 'VIS JIRA Metrics Service Deployment - Docker ECS'
  parameters do
    cluster do
      description 'ID of ECS cluster to deploy into'
      type 'String'
    end
    cluster_env do
      description 'Cluster environment (passed as envvar to containers)'
      type 'String'
    end
    cluster_size do
      description 'Nominal size of deployment'
      type 'String'
    end
    load_balancer do
      description 'Target ALB to use for ECS service'
      type 'String'
    end
    vis_jira_metrics_docker_image do
      description \
        'Docker image for JIRA Metrics Service containers'
      type 'String'
    end
  end

  # Create an ECS task definition
  dynamic!(
    :ecs_task, # type of dynamic
    :task, # name of resource
    cluster_env: ref!(:cluster_env),
    containers: array!(
      -> do
        name 'vis_jira_metrics_service'
        hostname 'jira-metrics'
        image ref!(:vis_jira_metrics_docker_image)
        port_mappings array!(
          -> do
            host_port 4000
            container_port 4000
          end
        )
      end
    )
  )
  # Create an IAM role for ECS use (register/deregister from ELB)
  dynamic!(:ecs_iam_role, :role)
  dynamic!(:ecs_task_iam_role, :task_role)
  # Create an ECS service
  dynamic!(:ecs_service,
           :service,
           cluster: ref!(:cluster),
           cluster_size: ref!(:cluster_size),
           elb_container: 'vis_jira_metrics_service',
           elb_cport: '4000',
           elb_id: ref!(:load_balancer),
           role: ref!(:role),
           task: ref!(:task))

  outputs do
    cluster do
      value ref!(:cluster)
    end
    service do
      value ref!(:service)
    end
    task do
      value ref!(:task)
    end
  end
end
sfn.sparkle.add_sparkle(root_pack)
sfn.load(:common)
