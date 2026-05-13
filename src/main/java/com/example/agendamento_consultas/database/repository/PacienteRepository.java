package com.example.agendamento_consultas.database.repository;

import com.example.agendamento_consultas.database.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    boolean existsByDocumentoIdentificacao(String documentoIdentificacao);

    boolean existsByDocumentoIdentificacaoAndIdNot(String documentoIdentificacao, Long id);

    Optional<Paciente> findByDocumentoIdentificacao(String documentoIdentificacao);

    Page<Paciente> findByNomeCompletoContainingIgnoreCase(String nomeCompleto, Pageable pageable);

    Page<Paciente> findByNomeCompletoContainingIgnoreCaseAndDocumentoIdentificacao(String nomeCompleto, String documentoIdenficacao, Pageable pageable);

    @Query("SELECT p FROM Paciente p LEFT JOIN FETCH p.contatos WHERE p.id = :id")
    Optional<Paciente> findByIdWithContatos(@Param("id") Long id);
}
