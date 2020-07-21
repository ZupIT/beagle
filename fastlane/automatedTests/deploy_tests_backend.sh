#!/bin/bash
cd ../../backend/automated-tests
#not working using the gradle wrapper
#./gradlew assemble 
IMAGE_ID=$(docker build . 2>/dev/null | awk '/Successfully built/{print $NF}')
echo $IMAGE_ID
#important! -> a ordem dos parâmetros importa, o -p 8080:8080 deve ser antes do IMAGE_ID
docker run -p 8080:8080 $IMAGE_ID
#returns the mac machine ip -> is it necessary?
#SERVER_IP=$(ipconfig getifaddr en0)
#echo $SERVER_IP