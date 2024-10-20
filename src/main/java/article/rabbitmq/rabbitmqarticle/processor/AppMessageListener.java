package article.rabbitmq.rabbitmqarticle.processor;

import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class AppMessageListener {
    private static final Logger LOGGER = Logger.getGlobal();

    public void handleMessage(GenericMessage<Object> message) {
        LOGGER.log(Level.INFO, String.format("Recebida com headers: %s", message.getHeaders().toString()));
        LOGGER.log(Level.INFO, String.format("Corpo recebido: %s", message.getPayload().toString()));
        LOGGER.log(Level.INFO, "");
    }
}
