package com.example.agendamento_consultas.database.repository;

import com.example.agendamento_consultas.database.model.RefreshToken;
import com.example.agendamento_consultas.database.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findByUsuarioAndRevokedAtIsNull(Usuario usuario);
}
