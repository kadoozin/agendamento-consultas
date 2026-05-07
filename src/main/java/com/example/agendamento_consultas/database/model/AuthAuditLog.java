package com.example.agendamento_consultas.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "tb_auth_audit_log")
public class AuthAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;

    private String email;

    @Column(nullable = false)
    private boolean success;

    private String ipAddress;

    private String userAgent;

    private String reason;

    @Column(nullable = false)
    private Instant createdAt;
}
