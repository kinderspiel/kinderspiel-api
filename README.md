# kinderspiel

Generated project with [generator-spring-rest-commons](https://github.com/rocketbase-io/generator-spring-rest-commons)

## project-structure

* kinderspiel-api
  * containing DTOs and REST-resources
  * could be used in other projects to consume REST endpoints of server
* kinderspiel-model
  * containg entity-model, converter, spring-data repositories
  * used to work with the database layer
* kinderspiel-server
  * containg controller and SpringApplication
  * used to provide REST-endpoints via spring boot


## dependencies

* [spring-boot](https://projects.spring.io/spring-boot/):*2.2.4.RELEASE*
* [mapstruct](http://mapstruct.org/):*1.3.1.Final*
* [commons-rest](https://github.com/rocketbase-io/commons-rest):*2.0.1*
* [commons-auth](https://github.com/rocketbase-io/commons-auth):*3.2.0*
* [commons-asset](https://github.com/rocketbase-io/commons-asset):*3.1.0*

## usage

```bash
# install project
mvn install

# run spring-boot app
cd kinderspiel-server
mvn spring-boot:run 
```