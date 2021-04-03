package com.example.library.server.testconfig;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.config.EnableWebFlux;

@ComponentScan(basePackages = {
    "com.example.library.server.api",
    "com.example.library.server.business",
    "com.example.library.server.config"
})
@EnableWebFlux
@EnableWebFluxSecurity
@EnableAutoConfiguration(exclude = {
    MongoReactiveAutoConfiguration.class,
    MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class,
    EmbeddedMongoAutoConfiguration.class,
    MongoReactiveRepositoriesAutoConfiguration.class,
    MongoRepositoriesAutoConfiguration.class
})
public class ApiAuthenticationTestConfig {
    //Not applicable
}
