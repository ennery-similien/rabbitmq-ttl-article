package article.rabbitmq.rabbitmqarticle.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitmqConfig {

    public static final String EXCHANGE_NAME = "article_exchange";
    public static final String QUEUE_NAME = "article_queue";
    public static final String ROUTING_KEY = "article_routing_key";

    public static final String EXCHANGE_TTL_NAME = "article_exchange_ttl";
    public static final String QUEUE_TTL_NAME = "article_queue_ttl";
    public static final String QUEUE_TTL_PROCESSOR_NAME = "article_queue_ttl_processor";
    public static final String ROUTING_TTL_KEY = "article_routing_key_ttl";

    @Bean
    public DirectExchange exchangeTTL() {
        return new DirectExchange(EXCHANGE_TTL_NAME);
    }

    @Bean
    public Queue queueTTL() {
        return QueueBuilder.durable(QUEUE_TTL_NAME)
                .deadLetterExchange(EXCHANGE_TTL_NAME)
                .deadLetterRoutingKey(ROUTING_TTL_KEY)
                .build();
    }

    @Bean
    public Queue queueTTLProcessor() {
        return new Queue(QUEUE_TTL_PROCESSOR_NAME, Boolean.TRUE);
    }

    @Bean
    public Binding bindingTTLProcessor() {
        return BindingBuilder.bind(queueTTLProcessor()).to(exchangeTTL()).with(ROUTING_TTL_KEY);
    }


    /**
     *  Configuration for RabbitMQ, using Exchange Delayed Message
     *  Default Exchange: article_exchange
     *  Default Queue: article_queue
     *  Default Routing Key: article_routing_key
     */

    @Bean
    public CustomExchange exchangeDelayedPlugin() {
        return new CustomExchange(EXCHANGE_NAME, "x-delayed-message", Boolean.TRUE, Boolean.FALSE, Map.of("x-delayed-type", "direct"));
    }

    @Bean
    public Queue queueDelayedPlugin() {
        return new Queue(QUEUE_NAME, Boolean.TRUE);
    }

    @Bean
    public Binding bindingDelayedPlugin() {
        return BindingBuilder.bind(queueDelayedPlugin()).to(exchangeDelayedPlugin()).with(ROUTING_KEY).noargs();
    }

}
