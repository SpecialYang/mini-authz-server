FROM adoptopenjdk/openjdk8:alpine

WORKDIR /work

ADD target/mini-authz-server-1.0-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "mini-authz-server-1.0-SNAPSHOT.jar"]