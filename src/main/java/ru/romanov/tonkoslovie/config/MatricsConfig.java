package ru.romanov.tonkoslovie.config;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.izettle.metrics.influxdb.InfluxDbHttpSender;
import com.izettle.metrics.influxdb.InfluxDbReporter;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableMetrics
public class MatricsConfig extends MetricsConfigurerAdapter {

    @Value("${metrics.influxdb.active:false}")
    private boolean influxdbActive;
    @Value("${metrics.influxdb.host:@null}")
    private String host;
    @Value("${metrics.influxdb.port:0}")
    private int port;
    @Value("${metrics.influxdb.database:@null}")
    private String database;
    @Value("${metrics.influxdb.auth:@null}")
    private String auth;
    @Value("${metrics.console:false}")
    private boolean console;

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        if (influxdbActive) {
            metricRegistry.register("jvm.memory", new MemoryUsageGaugeSet());

            try {
                InfluxDbReporter reporter = InfluxDbReporter
                        .forRegistry(metricRegistry)
                        .build(new InfluxDbHttpSender("http", host, port, database, auth, TimeUnit.SECONDS, 1000, 1000));
                reporter.start(10, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (console) {
            registerReporter(ConsoleReporter
                    .forRegistry(metricRegistry)
                    .build())
                    .start(10, TimeUnit.SECONDS);
        }
    }
}
