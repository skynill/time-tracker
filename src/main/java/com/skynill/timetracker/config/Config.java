package com.skynill.timetracker.config;

import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Slf4j
@EnableJpaRepositories("com.skynill.timetracker.domain")
@EnableTransactionManagement
@Configuration
public class Config {

    private final Environment environment;

    public Config(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
        liquibase.setContexts(getContexts());
        liquibase.setShouldRun(true);
        liquibase.setDropFirst(false);
        liquibase.setClearCheckSums(false);

        log.info("Liquibase configuration loaded with contexts: {}", getContexts());
        return liquibase;
    }

    private String getContexts() {
        return String.join(",", environment.getActiveProfiles());
    }

}
