package com.compassuol.sp.challenge.challenge3msuser.web.controller;

import com.compassuol.sp.challenge.challenge3msuser.repository.UserRepository;
import com.compassuol.sp.challenge.challenge3msuser.exception.ExceptionErrorMessage;
import com.compassuol.sp.challenge.challenge3msuser.jwt.JwtToken;
import com.compassuol.sp.challenge.challenge3msuser.jwt.JwtUserDetailsService;
import com.compassuol.sp.challenge.challenge3msuser.service.UserService;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserLoginDto;
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



@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class AuthenticationController {

    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@RequestBody @Valid UserLoginDto dto, HttpServletRequest request) {
        log.info("Processo de autenticação pelo login {}", dto.getEmail());
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());

            Authentication authenticate = authenticationManager.authenticate(authenticationToken);

            if (authenticate.isAuthenticated()) {

                JwtToken token = detailsService.getTokenAuthenticated(dto.getEmail());
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
