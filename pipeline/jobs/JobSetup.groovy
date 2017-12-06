import groovy.transform.Field
import javaposse.jobdsl.dsl.DslFactory

class JobSetup {
  // name of pipeline
  static pipelineName
  // git URL and branch to poll/pull
  static githubUrl
  static gitBranch
  // ruby version to install for rvm
  static rubyVersion
  // email address for notifications
  static email
  // stage of current job
  static stage

  public static final common(jobContext) {
    common(jobContext, [:])
  }

  public static final pipelineView(dslContext, String pipeline, String triggerJob) {
    dslContext.deliveryPipelineView(pipeline) {
      allowPipelineStart(true)
      allowRebuild(true)
      columns(1)
      enableManualTriggers(true)
      enablePaging(true)
      pipelineInstances(10)
      showAggregatedPipeline(false)
      showAvatars(false)
      showChangeLog(true)
      showDescription(true)
      showTotalBuildTime(true)
      updateInterval(2)
      pipelines {
        component(pipeline, triggerJob)
      }
    }
  }

  public static final name(String jobShortName) {
    "${pipelineName}-${jobShortName}"
  }

  // generate a rake task name from the job name
  // a pipeline named 'foo' with the job
  // 'foo-bar-baz' will result in a rake task name
  // bar:baz
  private static final rakeTaskName(String jobName) {
    jobName.replaceAll(/^$pipelineName-/, '').replaceAll(/-/, ':')
  }

  public static final common_scm(jobContext) {
    def scmClosure = {
      // SCM is a git repo defined by githubUrl and gitBranch
      // Wipe out workspace between builds
      scm {
        git {
          remote { url(githubUrl) }
          branch(gitBranch)

          extensions {
            submoduleOptions {
              recursive(true)
            }
          }
        }
      }
    }
    scmClosure.delegate = jobContext
    scmClosure()
  }

  public static final common_publisher(jobContext, Map opts) {
    def publisherClosure = {
      publishers {
        // If one or more downstream jobs were passed in, create a
        // downstreamParameterized publisher for them
        if (opts.containsKey('downstreamJobs')) {
          downstreamParameterized {
            trigger(opts['downstreamJobs'].join(', ')) {
              parameters {
                currentBuild()
                predefinedProp('PIPELINE_WORKSPACE', '$PIPELINE_WORKSPACE')
                propertiesFile('pipeline_state.env')
              }
              triggerWithNoParameters(true)
            }
          }
        }

        // Email status of build
        extendedEmail {
          recipientList(email)
          defaultSubject("\$PROJECT_NAME - Build # \$BUILD_NUMBER - \$BUILD_STATUS!")
          defaultContent("""\$PROJECT_NAME - Build # \$BUILD_NUMBER - \$BUILD_STATUS:

        Check console output at \$BUILD_URL to view the results.""")
          triggers {
            failure()
            fixed()
          }
        }
      }
    }
    publisherClosure.delegate = jobContext
    publisherClosure()
  }

/*
  public static final common_wrapper(jobContext) {
    def wrapperClosure = {
    }
    wrapperClosure.delegate = jobContext
    wrapperClosure()
  }
*/

  public static final common_wrapper(jobContext) {
    def wrapperClosure = {
      // Install RVM for all builds
      wrappers {
        rvm(rubyVersion)
        colorizeOutput()
      }
    }
    wrapperClosure.delegate = jobContext
    wrapperClosure()
  }

  public static final common_parameters(jobContext) {
    def parameterClosure = {
      parameters {
        stringParam('PIPELINE_WORKSPACE','workspace/' + jobContext.name + '-commit-stage=\${BUILD_NUMBER}')
      }
    }
    parameterClosure.delegate = jobContext
    parameterClosure()
  }

  public static final common_description(jobContext, opts) {
    def descriptionClosure = {
      if (opts['description']) {
        description(opts['description'])
      }

      deliveryPipelineConfiguration(JobSetup.stage, opts['description'])
    }
    descriptionClosure.delegate = jobContext
    descriptionClosure()
  }

  public static final common_log_rotator(jobContext) {
    def rotatorClosure = {
      logRotator {
          numToKeep(20)
          artifactNumToKeep(20)
      }
    }
    rotatorClosure.delegate = jobContext
    rotatorClosure()
  }


  public static final common_start(jobContext, Map opts) {
    common_description(jobContext, opts)
    common_parameters(jobContext)
    common_scm(jobContext)
    common_publisher(jobContext, opts)
    common_wrapper(jobContext)
    common_log_rotator(jobContext)

    def startClosure = {
      steps {
        customWorkspace('${PIPELINE_WORKSPACE}')
        shell('. rvm.env; bundle install;')
      }
    }
    startClosure.delegate = jobContext
    startClosure()
  }

  public static final common(jobContext, Map opts) {
    common_description(jobContext, opts)
    common_parameters(jobContext)
    common_scm(jobContext)
    common_publisher(jobContext, opts)
    common_wrapper(jobContext)

    def commonClosure = {
      // Only step is to run Rake with the generated
      // task name (based off job name)
      def rakeTask = rakeTaskName(jobContext.name)
      // Override of the generated task name
      // Also allows for multiple tasks to be run, etc
      if (opts['tasks']) {
        rakeTask = opts['tasks']
      }
      steps {
        customWorkspace('${PIPELINE_WORKSPACE}')
        shell(". rvm.env; bundle exec rake ${rakeTask}")
      }

    }
    // Delegate closure to our calling context (jobDsl job block)
    commonClosure.delegate = jobContext
    commonClosure()
  }
}
