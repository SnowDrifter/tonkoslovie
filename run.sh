#!/usr/bin/env bash
set -e

mvn clean spring-boot:build-image
docker-compose up