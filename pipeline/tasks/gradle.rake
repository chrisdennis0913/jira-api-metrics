# Rule to interpret rake gradle:* calls to invoke
# gradlew with a target
rule(/^gradle:/) do |task|
  Pipeline.logger.debug "called #{task}"
  gradle_task = task.to_s.sub(/^gradle:/, '')
  raise unless system('./gradlew', gradle_task)
end
