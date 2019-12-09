#!/usr/bin/bash

curl -X GET http://localhost:8080/hello

curl -X GET http://localhost:8080/messages/test

curl -X POST -d "{\"toUser\":\"test\", \"fromUser\":\"Marcel\",\"message\":\"Hallo\"}" http://localhost:8080/messages/test

curl -X GET http://localhost:8080/messages/test/count

curl -X GET http://localhost:8080/messages/test

curl -X GET http://localhost:8080/messages/test/count
