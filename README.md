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

# Start Spring boot app
	cd taskservice
	mvn spring-boot:run

This should write some data into ES.

# Start kibana
http://localhost:5601

Set the index name to "karaf-*" and discover the logs.
