FROM openjdk:15
VOLUME /tmp
EXPOSE 9090
ADD /etracker-service/target/etracker-service-0.0.1-SNAPSHOT.jar etracker-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","etracker-service-0.0.1-SNAPSHOT.jar"]