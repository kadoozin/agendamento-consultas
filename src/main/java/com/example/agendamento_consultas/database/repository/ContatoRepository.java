package com.example.agendamento_consultas.database.repository;

import com.example.agendamento_consultas.database.model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByNumero(String numero);

    boolean existsByNumeroAndIdNot(String numero, Long id);

    Optional<Contato> findByEmail(String email);

    Optional<Contato> findByNumero(String numero);
}
