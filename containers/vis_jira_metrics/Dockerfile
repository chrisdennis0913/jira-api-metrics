# FROM_LOCAL: $DOCKER_JAVA_IMAGE
FROM java:8

RUN apt-get update \
    && apt-get -y upgrade

# Copy up the jar
COPY jira-metrics-*.jar /opt/

# Poke a hole for spring boot
EXPOSE 4000

USER nobody

RUN ls -al /opt

# Start the java application
CMD ["sh", "-c", "java -jar /opt/jira-metrics-*.jar"]
