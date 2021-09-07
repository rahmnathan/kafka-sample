#!/bin/bash

export KAFKA_OPTS="-Djava.security.auth.login.config=/opt/bitnami/kafka/config/kafka_jaas.conf"

sleep 5
kafka-server-start.sh -daemon /opt/bitnami/kafka/config/server.properties
sleep 5

kafka-topics.sh --create --bootstrap-server kafka-localhost:9092 --topic test-topic --partitions 1 --replication-factor 1

kafka-configs.sh --bootstrap-server kafka-localhost:9092 --alter --add-config 'SCRAM-SHA-512=[password=pw123]' --entity-type users --entity-name kafka-consumer
kafka-acls.sh --add  --allow-principal User:kafka-consumer --operation Read  --topic test-topic --command-config /opt/bitnami/kafka/config/consumer.properties --bootstrap-server kafka-localhost:9092 --group test

kafka-configs.sh --bootstrap-server kafka-localhost:9092 --alter --add-config 'SCRAM-SHA-512=[password=pw123]' --entity-type users --entity-name kafka-producer
kafka-acls.sh --add  --allow-principal User:kafka-producer --operation Write  --topic test-topic --command-config /opt/bitnami/kafka/config/consumer.properties --bootstrap-server kafka-localhost:9092

kafka-server-stop.sh /opt/bitnami/kafka/config/server.properties
sleep 5