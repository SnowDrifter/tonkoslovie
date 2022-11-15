FROM confluentinc/cp-kafka-connect:7.2.2

RUN confluent-hub install --no-prompt debezium/debezium-connector-postgresql:1.9.7
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-elasticsearch:14.0.2

WORKDIR /connect

COPY bin/start.sh .
COPY bin/init_connectors.sh .
COPY config/debezium-connector.json .
COPY config/elastic-sink-connector.json .

EXPOSE 8083

ENTRYPOINT ["/connect/start.sh"]