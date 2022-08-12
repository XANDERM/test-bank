# test-bank-base by Alexandro Marcos Fajardo Rodríguez

# Introduction
Base project to take how referent for new projects. Please, see the Readme.md

# Installation and run

We use Contract First approach, so you need to generate an openapi-rest.yaml file in src/main/resources of module test-bank-base-api-rest 
with the definition of the API in OpenAPI format (Swagger file).

In the package phase of the Maven lifecycle, the DTOs and driver interfaces will be auto-generated as specified in src/main/resources/openapi-rest.yaml of module test-bank-base-api-rest. 
During the clean phase, all self-generated DTOs and driver interfaces will be removed

To start the microservice with the local configuration you can run the following command:

```
mvn spring-boot:run
```


- In order to check the data in the database you can use the h2 browse:

```
http://localhost:8082/h2-console

datasource:
  platform: H2
  url: 'jdbc:h2:mem:testdb'
  username: sa
  password:
  driverClassName: org.h2.Driver
  schema: TEST
```

Once installed and started, you can take a look to the service's API with the swagger interface:

```
http://localhost:8082/swagger-ui.html
```

# Functionality

This microservice is a complete api-rest with drools and comment kafka configuration.

It exposes methods via REST API for navigate through the transactions stored in TEST_BANK.

# Do you interested on to create and to generate an archetype from this project? 

The file "test_archetype.properties" contains the basic data for generating the archetype:

```
archetype.groupId=es.test.bank
archetype.artifactId=test-bank-base
archetype.package=es.test.bank
archetype.version=1.0.0
groupId=es.test.bank
artifactId=test-bank-example
```

In the console, run the following command:

```
mvn clean archetype:create-from-project -Darchetype.properties="test_archetype.properties"
```

At this point we already have the archetype project generated with all the resources present in the initially cloned and subsequently modified project. 
Let's install it in the local repository:

```
cd ~/target/generated-sources/archetype/
mvn install
```

To create the project generated from the previously created archetype, we enter the following statements:

```
mkdir /tmp/project-test
cd /tmp/project-test/
mvn archetype:generate -Dfilter=es.test.bank:test-bank-base
```

The console will go into interactive mode, it will enumerate the list of archetypes found in the configured repositories giving them an ordinal number.

In our case, we select number 1 or the one that corresponds to es.test.bank:test-bank-base. Now, it will continue to ask for the properties for the new project. These should be our answers:

```
groupId: es.test.bank
artifactId: example
version: 0.0.1-SNAPSHOT
package: es.test.bank (or just press enter)
At this point we will be asked for confirmation. We say yes (Y).
```