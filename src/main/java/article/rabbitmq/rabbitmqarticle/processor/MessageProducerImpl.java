package article.rabbitmq.rabbitmqarticle.processor;

import article.rabbitmq.rabbitmqarticle.config.RabbitmqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
public class MessageProducerImpl implements MessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public MessageProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @Override
    public void sendMessage(Object message, String expiration) {
        rabbitTemplate.convertAndSend(RabbitmqConfig.QUEUE_TTL_NAME, new GenericMessage<Object>(message), msg -> {
            msg.getMessageProperties().setHeader("ORIGIN", "sendMessage(Object message)");
            msg.getMessageProperties().setExpiration(expiration);
            msg.getMessageProperties().setContentType("application/json");
            return msg;
        });
    }

    @Override
    public void sendMessage(Object message, int delay) {
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_NAME, RabbitmqConfig.ROUTING_KEY, new GenericMessage<Object>(message), msg -> {
            msg.getMessageProperties().setHeader("x-delay", delay);
            msg.getMessageProperties().setHeader("ORIGIN", "sendMessage(Object message, int delay)");
            msg.getMessageProperties().setContentType("application/json");
            return msg;
        });
    }
}
