package com.recherche.offre.ti.configuration;

import lombok.Getter;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationInitializer;
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Getter
public class OffreEmploiApiTestConfiguration {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${offre-emploi.api.base-url}")
    private String baseUrl;

    @Value("${api.offredemploi.client_id}")
    private String clientId;

    @Value("${api.offredemploi.client_secret}")
    private String clientSecret;

    @Value("${offre-emploi.api.scope}")
    private String scope;

    @Value("${offre-emploi.api.auth-cache-ttl-seconds:240}")
    private long authCacheTtlSeconds;

    @Value("${offre-emploi.api.rome-cache-ttl-hours:24}")
    private long romeCacheTtlHours;

    @Bean(name="testDataSource")
    public DataSource testDataSource() {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName("org.h2.Driver")
                .build();
    }

    @Bean(name="testFlyway")
    public Flyway testFlyway(@Qualifier("testDataSource") DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:flyway/versions")
                .baselineOnMigrate(true)
                .load();
    }

    @Bean(name="flywayInitializer")
    public FlywayMigrationInitializer flywayInitializer(@Qualifier("testFlyway") Flyway flyway,
                                                        ObjectProvider<FlywayMigrationStrategy> migrationStrategy) {
        return new FlywayMigrationInitializer(flyway, migrationStrategy.getIfAvailable());
    }

}
