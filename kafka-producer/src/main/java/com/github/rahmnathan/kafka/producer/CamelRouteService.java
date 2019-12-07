package com.github.rahmnathan.kafka.producer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;

@Slf4j
@Service
@AllArgsConstructor
public class CamelRouteService {
    private static final String KAFKA_PRODUCER_ROUTE = "direct:send-kafka-message";
    private final ProducerTemplate template;
    private final CamelContext context;

    @EventListener(ApplicationReadyEvent.class)
    public void initCamelRoutes() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(KAFKA_PRODUCER_ROUTE)
                        .to("kafka:test-topic")
                        .end();
            }
        });

        // Used for large message testing
//        String image = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get("/home/nathan/development/workspaces/localmovies/amqp-sample/amqp-producer/src/main/resources/20190421_140748.jpg")));

        int numberOfMessages = 5000;
        var start = LocalDateTime.now();

        IntStream.range(0, numberOfMessages).parallel().forEach(num -> {
            log.info("Sending message: {}", num);
            template.sendBody(KAFKA_PRODUCER_ROUTE, "Message number: " + num);
        });

        var durationSeconds = ChronoUnit.SECONDS.between(start, LocalDateTime.now());
        log.info("Sent {} messages in {} seconds", numberOfMessages, durationSeconds);
    }
}
