#!/bin/bash

cd /home/ec2-user/waffle-rookies-19.5-springboot/seminar

echo "> Git Pull"

git pull origin deploy2

echo "> Build"

./gradlew bootJar

echo "> Execute jar file"

nohup java -jar -Dspring.profiles.active=prod build/libs/seminar-0.0.1-SNAPSHOT.jar
