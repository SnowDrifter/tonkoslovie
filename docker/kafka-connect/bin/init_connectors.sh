#!/bin/bash
set -e

echo -e "Start init connectors\c"

while :; do
  if [[ $(curl -o /dev/null -s -w "%{http_code}\n" http://kafka-connect:8083/connectors) == "200" ]]; then
    echo -e "\Kafka-connect is up"

    if [[ $(curl -sb -H "Accept:application/json" http://kafka-connect:8083/connectors) == "[]" ]]; then

      echo "Prepare debezium connector config"
      sed -i "s/{DB_HOSTNAME}/${DB_HOSTNAME}/" debezium-connector-config.json
      sed -i "s/{DB_DATABASE}/${DB_DATABASE}/" debezium-connector-config.json
      sed -i "s/{DB_USER}/${DB_USER}/" debezium-connector-config.json
      sed -i "s/{DB_PASSWORD}/${DB_PASSWORD}/" debezium-connector-config.json

      curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://kafka-connect:8083/connectors/ -d @debezium-connector-config.json
      echo -e "\nDebezium connector initialized successfully"

      echo -e "\nPrepare elastic connector config"
      sed -i "s/{ELASTIC_URL}/${ELASTIC_URL}/" elastic-sink-connector.json

      curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://kafka-connect:8083/connectors/ -d @elastic-sink-connector.json
      echo -e "\nElastic connector initialized successfully"
    else
      echo "Connectors has already been initialized"
    fi

    break
  else
    echo -e ".\c"
    sleep 1
  fi
done

