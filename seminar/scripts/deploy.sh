#!/bin/bash

REPOSITORY=/home/ec2-user/

cd $REPOSITORY/waffle-rookies-19.5-springboot/

echo "> Git Pull"

git pull origin deploy2

echo "> Build"

./gradlew bootJar

echo "> Build file copy"

cp ./seminar/build/libs/*.jar $REPOSITORY/

echo "> Application pid check"

CURRENT_PID=$(pgrep -f seminar)

echo "$CURRENT_PID"

if [ -z $CURRENT_PID ]; then
	echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
	echo "> kill -2 $CURRENT_PID"
	kill -2 $CURRENT_PID
	sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls $REPOSITORY | grep 'seminar' | tail -n 1)

echo "> JAR Name: $JAR_NAME"

nohup java -jar -Dspring.profiles.active=prod $REPOSITORY/$JAR_NAME &
