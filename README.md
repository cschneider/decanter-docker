# decanter-docker
Create docker images for decanter agents and servers as well as for kafka brokers to connect them

# Build
mvn clean install
docker-compose build

# Start kafka server, decanter-agent and decanter-server
docker-compose up --force-recreate

# To Do
Decanter is currently missing a kafka collector. So it is not yet possible to run a full demo

