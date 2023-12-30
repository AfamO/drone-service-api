FROM openjdk:17-oracle
MAINTAINER afamo
COPY target/demo-0.0.1-SNAPSHOT.jar drone-project-1.0.0.jar
ENTRYPOINT ["java","-jar","/drone-project-1.0.0.jar"]