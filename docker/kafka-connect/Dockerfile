FROM confluentinc/cp-kafka-connect:7.5.0

RUN confluent-hub install --no-prompt debezium/debezium-connector-postgresql:2.2.1
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-elasticsearch:14.0.10

WORKDIR /connect

COPY bin/start.sh .
COPY bin/init_connectors.sh .
COPY config/debezium-connector.json .
COPY config/elastic-sink-connector.json .

EXPOSE 8083

ENTRYPOINT ["/connect/start.sh"]