package com.compassuol.sp.challenge.challenge3msuser.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserNotificationSend {

    private String email;
    private NotificationMessage event;
    private LocalDateTime sendDate;


}
