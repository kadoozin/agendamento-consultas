package com.example.agendamento_consultas.controller;

import com.example.agendamento_consultas.dto.request.LoginRequest;
import com.example.agendamento_consultas.dto.request.RefreshTokenRequest;
import com.example.agendamento_consultas.dto.request.RegisterRequest;
import com.example.agendamento_consultas.dto.response.LoginResponse;
import com.example.agendamento_consultas.dto.response.RegisterResponse;
import com.example.agendamento_consultas.controller.docs.AuthControllerDocs;
import com.example.agendamento_consultas.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticacao", description = "Endpoints de autenticacao e gestao de sessao.")
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<RegisterResponse> register(
            @RequestBody @Valid RegisterRequest request,
            HttpServletRequest servletRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request, servletRequest));
    }

    @PostMapping("/bootstrap-admin")
    @Override
    public ResponseEntity<RegisterResponse> bootstrapAdmin(
            @RequestHeader("X-Bootstrap-Key") String bootstrapKey,
            @RequestBody @Valid RegisterRequest request,
            HttpServletRequest servletRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.bootstrapAdmin(request, bootstrapKey, servletRequest));
    }

    @PostMapping("/login")
    @Override
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletRequest servletRequest) {
        return ResponseEntity.ok(authService.login(request, servletRequest));
    }

    @PostMapping("/refresh")
    @Override
    public ResponseEntity<LoginResponse> refresh(
            @RequestBody @Valid RefreshTokenRequest request,
            HttpServletRequest servletRequest) {
        return ResponseEntity.ok(authService.refresh(request, servletRequest));
    }

    @PostMapping("/logout")
    @Override
    public ResponseEntity<Void> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            Authentication authentication,
            HttpServletRequest servletRequest) {
        authService.logout(authorizationHeader, authentication.getName(), servletRequest);
        return ResponseEntity.noContent().build();
    }
}
