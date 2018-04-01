package ru.romanov.tonkoslovie.config;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "hikari.datasource")
public class DatabaseConfig extends HikariConfig {

    private final MetricRegistry metricRegistry;

    @Autowired
    public DatabaseConfig(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource(this);
        dataSource.setMetricRegistry(metricRegistry);
        return dataSource;
    }

}
