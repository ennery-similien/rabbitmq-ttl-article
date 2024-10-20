package article.rabbitmq.rabbitmqarticle.processor;

public interface MessageProducer {
    void sendMessage(Object message, String expiration);
    void sendMessage(Object message, int delay);
}
