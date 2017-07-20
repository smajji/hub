# hub

**A proof-of-concept application which demonstrates microservice architecture, with Auth and config server**

This is a proof-of-concept application, which demonstrates [Microservice Architecture Pattern](http://martinfowler.com/microservices/) using Spring Boot, Spring Cloud and Docker(Docker, integration TBD).

### Config service
[Spring Cloud Config](http://cloud.spring.io/spring-cloud-config/spring-cloud-config.html) is horizontally scalable centralized configuration service for distributed systems. It uses a pluggable repository layer that currently supports local storage, Git, and Subversion. 

In this project, I use `native profile`, which simply loads config files from the local classpath. You can see `shared` directory in [Config service resources](https://github.com/smajji/hub/tree/master/hub-config/src/main/resources)

##### Client side usage
Just build Spring Boot application with `spring-cloud-starter-config` dependency, autoconfiguration will do the rest.

Now you don't need any embedded properties in your application. Just provide `bootstrap.yml` with application name and Config service url:
```yml
spring:
  application:
    name: hub-auth
  cloud:
    config:
      uri: http://localhost:8888
      fail-fast: true
      
```      
      
### API Gateway
Currently there is onle one API, which expose external API to client. In a real-world systems, this number can grow very quickly as well as whole system complexity. Actualy, hundreds of services might be involved in rendering one complex webpage.

In theory, a client could make requests to each of the microservices directly. But obviously, there are challenges and limitations with this option, like necessity to know all endpoints addresses, perform http request for each peace of information separately, merge the result on a client side. Another problem is non web-friendly protocols, which might be used on the backend.

Usually a much better approach is to use API Gateway. It is a single entry point into the system, used to handle requests by routing them to the appropriate backend service or by invoking multiple backend services and [aggregating the results](http://techblog.netflix.com/2013/01/optimizing-netflix-api.html). Also, it can be used for authentication, insights, stress and canary testing, service migration, static response handling, active traffic management.

Netflix opensourced [such an edge service](http://techblog.netflix.com/2013/06/announcing-zuul-edge-service-in-cloud.html), and now with Spring Cloud we can enable it with one `@EnableZuulProxy` annotation. In this project, I use Zuul to store static content (ui application) and to route requests to appropriate microservices. Here's a simple prefix-based routing configuration for Auth service:      

```yml
zuul:
  routes:
    auth-service:
        path: /uaa/**
        url: http://localhost:5000
        stripPrefix: false

```
### Service discovery

Another commonly known architecture pattern is Service discovery. It allows automatic detection of network locations for service instances, which could have dynamically assigned addresses because of auto-scaling, failures and upgrades.

The key part of Service discovery is Registry. I use Netflix Eureka in this project. Eureka is a good example of the client-side discovery pattern, when client is responsible for determining locations of available service instances (using Registry server) and load balancing requests across them.

With Spring Boot, you can easily build Eureka Registry with `spring-cloud-starter-eureka-server` dependency, `@EnableEurekaServer` annotation and simple configuration properties.

Client support enabled with `@EnableDiscoveryClient` annotation an `bootstrap.yml` with application name:
``` yml
spring:
  application:
    name: auth-service
```

Now, on application startup, it will register with Eureka Server and provide meta-data, such as host and port, health indicator URL, home page etc. Eureka receives heartbeat messages from each instance belonging to a service. If the heartbeat fails over a configurable timetable, the instance will be removed from the registry.

Also, Eureka provides a simple interface, where you can track running services and number of available instances: `http://localhost:8761`

##### Notes
 - Need to implement Docker set up, one additional service with Mongo DB access, Load balancer, Circuit breaker and Http client
 - Also, implement centralized logging, follow Ready-to-go Docker configuration described [in sample project](http://github.com/sqshq/ELK-docker).
 - finally implement continous delivery approach
   1. The ability to release software anytime
   2. Any build could end up being a release
   3. Build artifacts once - deploy as needed