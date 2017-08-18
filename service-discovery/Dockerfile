FROM java:8-alpine
MAINTAINER Kshitij Choudha
ADD target/service-discovery-0.0.1-SNAPSHOT.jar /
EXPOSE 8761
ENTRYPOINT ["java","-jar", "service-discovery-0.0.1-SNAPSHOT.jar"]
