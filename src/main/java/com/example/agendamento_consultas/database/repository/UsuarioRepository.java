package com.example.agendamento_consultas.database.repository;

import com.example.agendamento_consultas.database.enums.Role;
import com.example.agendamento_consultas.database.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByRolesContaining(Role role);
}
