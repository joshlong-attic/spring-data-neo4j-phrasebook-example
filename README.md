# How to Setup the Spring Data Neo4j Phrasebook

* install Java 8  (`sudo apt install openjdk-8-jdk`)
* install [Neo4J](https://neo4j.com/download/). Unzip the archive. Go to the `bin` directory and then run `neo4j start` and then open up the console at `http://localhost:7474`.
* install Apache Maven  (`sudo apt install maven`)
* then go to the root of this project's directory and run `mvn spring-boot:run`. You should now be able to open up a simple REST API at `http://localhost:8080`
