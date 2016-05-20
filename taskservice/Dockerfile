FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD target/taskservice-0.0.1-SNAPSHOT.jar taskservice.jar
ENTRYPOINT ["java","-jar","/taskservice.jar"]
