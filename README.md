# decanter-docker
Create docker images for decanter agents and servers as well as for kafka brokers to connect them

# Build
mvn clean install

# Create docker image, start kafka server and decanter-agent
docker-compose up

# To Do
Decanter is currently missing a kafka collector. So it is not yet possible to run a full demo

