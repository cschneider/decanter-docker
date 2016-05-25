# decanter-docker
Show a full logging pipeline from spring boot -> log4j socket appender -> decanter-agent -> kafka -> decanter-server -> es -> kibana

# Set your host IP in docker-compose.yml
KAFKA_ADVERTISED_HOST_NAME: 192.168.0.108

# Build
	mvn clean install
	docker-compose rm
	docker-compose build

# Start kafka, decanter-agent and decanter-server, es and kibana
	docker-compose up

# restart taskservice to create some logs
	docker-compose stop taskservice
	docker-compose start taskservice
This should write some data into ES.

# Start kibana
http://localhost:5601

Set the index name to "karaf-*" and discover the logs.


# Debugging

For debugging you can use a command line kafka consumer. This allows to check the pipeline up to kafka is working:

	bin/kafka-console-consumer.sh --zookeeper localhost:2181 --bootstrap-server localhost:9092  --topic decanter
