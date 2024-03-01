package com.compassuol.sp.challenge.challenge3msuser.web.mqueue;


import com.compassuol.sp.challenge.challenge3msuser.entity.UserNotificationSend;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationQueue {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueNotification;


    public void sendUserCreateQueue(UserNotificationSend user) throws Exception {
        try {
            var json = convertIntoJson(user);
            rabbitTemplate.convertAndSend(queueNotification.getActualName(), json);
        } catch (JsonProcessingException e) {
            throw new Exception("Erro ao enviar mensagem para fila: " + e.getMessage());
        }
    }
    private String convertIntoJson(UserNotificationSend data) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        return mapper.writeValueAsString(data);
    }


}