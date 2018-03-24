package ru.romanov.tonkoslovie.jetty;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jetty9.InstrumentedHandler;
import org.eclipse.jetty.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class JettyServletContainerCostumizer implements WebServerFactoryCustomizer<JettyServletWebServerFactory> {

    private final MetricRegistry metricRegistry;

    @Autowired
    public JettyServletContainerCostumizer(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    public void customize(JettyServletWebServerFactory server) {
        JettyServerCustomizer customizer = new JettyServerCustomizer() {
            @Override
            public void customize(Server server) {
                InstrumentedHandler handler = new InstrumentedHandler(metricRegistry, "jetty");
                handler.setHandler(server.getHandler());
                server.setHandler(handler);
            }
        };

        server.addServerCustomizers(customizer);
    }

}