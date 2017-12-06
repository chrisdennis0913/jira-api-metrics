desc 'Assemble binary and copy for docker build'
task assemble: ['gradle:build'] do
  sh 'cp ./build/libs/jira-metrics-*.jar ' \
     'containers/vis_jira_metrics/' || raise('task failed')
end
