pipelineName = JobSetup.pipelineName = 'vis_jira_metrics'
githubUrl = JobSetup.githubUrl = 'git@git.uscis.dhs.gov:USCIS/vis-jira-metrics'
gitBranch = JobSetup.gitBranch = 'master'
rubyVersion = JobSetup.rubyVersion = "2.3.3@${pipelineName}"
email = JobSetup.email = 'jesse.r.adams@uscis.dhs.gov'

// Setup pipeline view (context, name, trigger job)
JobSetup.pipelineView(this, 'VIS Jira Metrics', "${pipelineName}-version-control")

// common_start method runs bundle install to ensure all Ruby
// components are present before pipeline goes
job("${pipelineName}-version-control") { jobContext ->
  JobSetup.stage = 'Commit'
  JobSetup.common_start(jobContext, ['description': 'Trigger Pipeline on VCS Update',
                                     'downstreamJobs': ["${pipelineName}-setup-vars"]])

  // Trigger pipeline whenever there is a change to git repo, or
  // once a day at a random time
  triggers {
    scm('* * * * *')
    cron('H H * * *')
  }
}

// Commit Stage

// Job name generally transform to rake tasks. The following job
// would invoke "rake setup:vars"
job("${pipelineName}-setup-vars") { jobContext ->
  JobSetup.common(jobContext, ['description': 'Setup pipeline state',
                               'downstreamJobs': ["${pipelineName}-rubocop"]])
}

job("${pipelineName}-rubocop") { jobContext ->
  JobSetup.common(jobContext, ['description': 'Run Rubocop on repo',
                               'downstreamJobs': ["${pipelineName}-assemble",
                               "${pipelineName}-build-sparkle-jira_metrics_elb"]])
}

job("${pipelineName}-assemble") { jobContext ->
    JobSetup.common(jobContext, ['description': 'Assemble jira-metrics jar',
                                 'downstreamJobs': ["${pipelineName}-build-container-vis_jira_metrics"]])
}

job("${pipelineName}-build-container-vis_jira_metrics") { jobContext ->
    JobSetup.common(jobContext, ['description': 'Build jira-metrics container',
                                 'downstreamJobs': ["${pipelineName}-test-container-vis_jira_metrics"]])
}

job("${pipelineName}-test-container-vis_jira_metrics") { jobContext ->
    JobSetup.common(jobContext, ['description': 'Test jira-metrics container',
                                 'downstreamJobs': ["${pipelineName}-push-image-vis_jira_metrics"]])
}

job("${pipelineName}-push-image-vis_jira_metrics") { jobContext ->
    JobSetup.common(jobContext, ['description': 'Push jira-metrics container',
                                 'downstreamJobs': ["${pipelineName}-clean-image-vis_jira_metrics",
                                                    "${pipelineName}-deploy-ecs-unstable"]])
}

job("${pipelineName}-clean-image-vis_jira_metrics") { jobContext ->
    JobSetup.common(jobContext, ['description': 'Clean jira-metrics container image from Jenkins'])
}

job("${pipelineName}-build-sparkle-jira_metrics_elb") { jobContext ->
  JobSetup.common(jobContext, ['description': 'Compile ELB CloudFormation Templates',
                               'downstreamJobs': ["${pipelineName}-deploy-elb-unstable"]])
}

job("${pipelineName}-deploy-ecs-unstable") { jobContext ->
    JobSetup.stage = 'Test / Unstable'
    JobSetup.common(jobContext, ['description': 'Deploy ECS Service to Test/Unstable',
                                 'downstreamJobs': [] ])
}

job("${pipelineName}-deploy-elb-unstable") { jobContext ->
  JobSetup.stage = 'Test / Unstable'
  JobSetup.common(jobContext, ['description': 'Deploy ECS ELB to Test/Unstable',
                               'downstreamJobs': []])
}
