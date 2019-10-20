FROM openjdk:8u201-jdk-alpine
MAINTAINER ethan Won <sumusb34@gmail.com>
ADD /build/libs/wepet.jar /app/wepet.jar
EXPOSE 80
CMD ["java","-jar","app/wepet.jar"]