#!/usr/bin/env bash
set -e

mvn clean install -pl tonkoslovie-model
mvn spring-boot:build-image -pl !tonkoslovie-model

docker compose up -d