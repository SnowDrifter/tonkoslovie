#!/usr/bin/env bash
set -e

mvn clean package
mvn spring-boot:build-image -pl tonkoslovie-application,tonkoslovie-mail

docker-compose up -d