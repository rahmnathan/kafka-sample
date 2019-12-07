package com.github.rahmnathan.kafka.consumer;

import lombok.AllArgsConstructor;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CamelRouteService {
    private final CamelContext context;

    @EventListener(ApplicationReadyEvent.class)
    public void initCamelRoutes() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("kafka:test-topic")
                        .to("log:com.github.rahmnathan?level=info")
                        .end();
            }
        });
    }
}
