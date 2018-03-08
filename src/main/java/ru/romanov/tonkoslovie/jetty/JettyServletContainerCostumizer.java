//package ru.romanov.tonkoslovie.jetty;
//
//
//import com.codahale.metrics.MetricRegistry;
//import com.codahale.metrics.jetty9.InstrumentedHandler;
//import org.eclipse.jetty.server.Server;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
//import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
//import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
//import org.springframework.stereotype.Component;
//
//TODO: fix imports or find alternative
//@Component
//public class JettyServletContainerCostumizer implements EmbeddedServletContainerCustomizer {
//
//    private final MetricRegistry metricRegistry;
//
//    @Autowired
//    public JettyServletContainerCostumizer(MetricRegistry metricRegistry) {
//        this.metricRegistry = metricRegistry;
//    }
//
//    @Override
//    public void customize(ConfigurableEmbeddedServletContainer container) {
//        JettyEmbeddedServletContainerFactory factory = (JettyEmbeddedServletContainerFactory) container;
//        factory.addServerCustomizers(this::customize);
//    }
//
//    private void customize(Server server) {
//        InstrumentedHandler handler = new InstrumentedHandler(metricRegistry, "jetty");
//        handler.setHandler(server.getHandler());
//        server.setHandler(handler);
//    }
//}
