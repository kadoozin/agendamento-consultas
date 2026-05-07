package com.example.agendamento_consultas.database.repository;

import com.example.agendamento_consultas.database.model.RevokedAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevokedAccessTokenRepository extends JpaRepository<RevokedAccessToken, Long> {
    boolean existsByTokenHash(String tokenHash);
}
