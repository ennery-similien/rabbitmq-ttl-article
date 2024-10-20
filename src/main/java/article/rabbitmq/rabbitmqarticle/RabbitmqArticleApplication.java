package article.rabbitmq.rabbitmqarticle;

import article.rabbitmq.rabbitmqarticle.processor.MessageProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class RabbitmqArticleApplication implements CommandLineRunner {
    private final MessageProducer producer;
    private final Logger LOGGER = Logger.getGlobal();

    record Message(String message, int messageNumber) { }

    public RabbitmqArticleApplication(MessageProducer producer) {
        this.producer = producer;
    }

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqArticleApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        //Está em milisegundos
        int delay20Segundos = 20000;

        LOGGER.log(Level.INFO, "");

        for (int i = 1; i <= 3; i++) {
            Message message = new Message(String.format("Mesagem fila TTL #%s", i), i);
            LOGGER.log(Level.INFO, "#### ENVIANDO TTL >> " + message);
            LOGGER.log(Level.INFO, "");

            producer.sendMessage(message);
            Thread.sleep(2000);
        }
    }
}
