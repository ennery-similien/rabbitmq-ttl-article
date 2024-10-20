package article.rabbitmq.rabbitmqarticle.config;

import article.rabbitmq.rabbitmqarticle.processor.AppMessageListener;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.support.GenericMessage;

import java.nio.charset.StandardCharsets;

@Configuration
public class MessageListenerConfig {

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RabbitmqConfig.QUEUE_NAME, RabbitmqConfig.QUEUE_TTL_PROCESSOR_NAME);
        container.setMessageListener(listenerAdapter);

        return container;
    }

    @Bean
    @DependsOn({"AppMessageConverter"})
    public MessageListenerAdapter listenerAdapter(AppMessageListener listener, @Qualifier("AppMessageConverter") MessageConverter messageConverter) {
        return new MessageListenerAdapter(listener, messageConverter);

        //return new MessageListenerAdapter(listener, "receiveMessage");
    }

    @Bean(name = "AppMessageConverter")
    @Primary
    public MessageConverter messageConverter() {
        return new MessageConverter() {
            @Override
            public Message toMessage(Object message, MessageProperties messageProperties) throws MessageConversionException {
                return new Message(message.toString().getBytes(StandardCharsets.UTF_8), messageProperties);
            }

            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
                return new GenericMessage<Object>(new String(message.getBody(), StandardCharsets.UTF_8), message.getMessageProperties().getHeaders());
            }
        };
    }
}
