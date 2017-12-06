require 'vis_sparklepack'
root_pack = SparklePack.new(name: 'vis_sparklepack')
# rubocop:disable Style/Lambda

sfn = SparkleFormation.new(:vis_jira_metrics_elb,
                           provider: :aws) do
  description 'VIS JIRA Metrics Deployment - Static ELB'
  parameters do
    vpc do
      description 'VPC to deploy the ELB to'
      type 'String'
    end
    asg_subnets do
      description 'The subnet ids the ELB should attach to'
      type 'CommaDelimitedList'
    end
    cert_name do
      description 'The name of the SSL cert to use'
      type 'String'
    end
    elb_security_group do
      description 'The ECS Cluster ELB Security Group used for Ingress'
      type 'String'
    end
    elb_logging_bucket do
      description 'The S3 bucket that the ELB should log to'
      type 'String'
      default 'vdm-enr-api-elb-logs' # Ali J: are we using the same bucket?
    end
  end # parameters

  resources do
    load_balancer_listener do
      type 'AWS::ElasticLoadBalancingV2::Listener'
      properties do
        certificates array!(
          -> do
            certificate_arn join!('', 'arn:aws:iam::', ref!('AWS::AccountId'),
                                  ':server-certificate/', ref!('certName'))
          end
        )
        default_actions array!(
          -> do
            target_group_arn ref!(:load_balancer_target_group)
            type 'forward'
          end
        )
        load_balancer_arn ref!(:load_balancer)
        port 443
        protocol 'HTTPS'
      end
    end # load_balancer_listener

    load_balancer do
      type 'AWS::ElasticLoadBalancingV2::LoadBalancer'
      properties do
        scheme 'internal'
        security_groups [ref!(:elb_security_group)]
        subnets ref!(:asg_subnets)
      end
    end # load_balancer

    load_balancer_target_group do
      Type 'AWS::ElasticLoadBalancingV2::TargetGroup'
      properties do
        health_check_interval_seconds 30
        health_check_protocol 'HTTP'
        health_check_path '/'
        health_check_timeout_seconds 7
        healthy_threshold_count 2
        port 4000
        protocol 'HTTP'
        unhealthy_threshold_count 10
        vpc_id ref!('vpc')
      end
    end # load_balancer_target_group

    alarm_sns_topic do
      type 'AWS::SNS::Topic'
      properties do
        display_name 'VIS-JIRA-METRICS-SERVICE-ALARM-LOAD-BALANCER'
        subscription array!(
          -> do
            endpoint 'jesse.adams@stelligent.com'
            protocol 'email'
          end
        )
      end
    end # alarm_sns_topic

    load_balancer_alarm do
      type 'AWS::CloudWatch::Alarm'
      properties do
        alarm_description 'No Healthy Hosts in LoadBalancer'
        metric_name 'HealthyHostCount'
        statistic 'Average'
        namespace 'AWS/ApplicationELB'
        period '300'
        evaluation_periods '1'
        threshold 0
        actions_enabled true
        alarm_actions array!(
          ref!(:alarm_sns_topic)
        )
        dimensions array!(
          -> do
            name 'LoadBalancer'
            value attr!(:load_balancer, 'LoadBalancerFullName')
          end,
          -> do
            name 'TargetGroup'
            value attr!(:load_balancer_target_group, 'TargetGroupFullName')
          end
        )
        comparison_operator 'LessThanOrEqualToThreshold'
      end # properties
    end # load_balancer_alarm
  end # resources

  outputs do
    load_balancer_name do
      value ref!(:load_balancer_target_group)
    end
    load_balancer_url do
      value attr!(:load_balancer, 'DNSName')
    end
  end # outputs
end
sfn.sparkle.add_sparkle(root_pack)
sfn.load(:common)
