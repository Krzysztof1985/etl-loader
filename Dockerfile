FROM openjdk:11
COPY ./target/*.jar /usr/src/server.jar
RUN apt-get update && apt-get install -y telnet iputils-ping dnsutils vim wget curl
WORKDIR /usr/src
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","server.jar"]
