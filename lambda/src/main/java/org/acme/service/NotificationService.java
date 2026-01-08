package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class NotificationService {

    @Inject
    SnsClient snsClient;

    @ConfigProperty(name = "sns.topic.arn")
    String topicArn;

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void publicar(String assunto, String mensagem) {

        logger.info("gerando request de publicacao da mensagem");

        PublishRequest request =
                PublishRequest.builder()
                        .topicArn(topicArn)
                        .subject(assunto)
                        .message(mensagem)
                        .build();

        snsClient.publish(request);

        logger.info("publicacao realizada com sucesso");
    }
}
