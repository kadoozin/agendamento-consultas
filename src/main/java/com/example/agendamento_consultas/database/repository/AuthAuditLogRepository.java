package com.example.agendamento_consultas.database.repository;

import com.example.agendamento_consultas.database.model.AuthAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthAuditLogRepository extends JpaRepository<AuthAuditLog, Long> {
}
