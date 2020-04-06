# Springboot-Angular-Example

This example shall show how to setup a client server application with a springboot server and 
an angular application as client

## Layout 

The folder ``ngclient`` contains the angular client, the folder ``server``contains the springboot server.
The ngclient is automatially copied to ``src/main/resources/static`` of server to be hosted directly in the 
spring boot server.

The server api, which is used from client is generated from swagger definition from ``server/src/main/resources/example.yaml``.
 

## Tasks

``gradlew bootRun``   starts the server application including the client 

``gradlew dockerPublish`` creates a docker container and publishes it to vsa docker  

## Future
This simple example can be extended if needed, so feel free to contribute.


