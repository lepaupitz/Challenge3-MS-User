package com.compassuol.sp.challenge.challenge3msuser.web.controller;

import com.compassuol.sp.challenge.challenge3msuser.entity.NotificationMessage;
import com.compassuol.sp.challenge.challenge3msuser.entity.UserNotificationSend;
import com.compassuol.sp.challenge.challenge3msuser.repository.UserRepository;
import com.compassuol.sp.challenge.challenge3msuser.exception.ExceptionErrorMessage;
import com.compassuol.sp.challenge.challenge3msuser.jwt.JwtToken;
import com.compassuol.sp.challenge.challenge3msuser.jwt.JwtUserDetailsService;
import com.compassuol.sp.challenge.challenge3msuser.service.UserService;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserLoginDto;
import com.compassuol.sp.challenge.challenge3msuser.web.mqueue.NotificationQueue;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class AuthenticationController {

    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;
    private final NotificationQueue notificationQueue;


    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@RequestBody @Valid UserLoginDto dto, HttpServletRequest request) {
        log.info("Processo de autenticação pelo login {}", dto.getEmail());
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());

            Authentication authenticate = authenticationManager.authenticate(authenticationToken);

            if (authenticate.isAuthenticated()) {

                JwtToken token = detailsService.getTokenAuthenticated(dto.getEmail());

                UserNotificationSend userNotificationSend = new UserNotificationSend();
                userNotificationSend.setEmail(dto.getEmail());
                userNotificationSend.setEvent(NotificationMessage.LOGIN);
                userNotificationSend.setSendDate(new Date());
                try {
                    notificationQueue.sendUserCreateQueue(userNotificationSend);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return ResponseEntity.ok(token);
            } else {
                return null;
            }

        } catch (AuthenticationException ex) {
            log.warn("Bad Credentials from username '{}'", dto.getEmail());
        }
        return ResponseEntity
                .badRequest()
                .body(new ExceptionErrorMessage(request, HttpStatus.BAD_REQUEST, "Credenciais Inválidas"));

    }
}
