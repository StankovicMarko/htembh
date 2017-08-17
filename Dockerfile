FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/htembh.jar /htembh/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/htembh/app.jar"]
