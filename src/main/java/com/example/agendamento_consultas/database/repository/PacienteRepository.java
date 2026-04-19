package com.example.agendamento_consultas.database.repository;

import com.example.agendamento_consultas.database.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    boolean existsByDocumentoIdentificacao(String documentoIdentificacao);

    boolean existsByDocumentoIdentificacaoAndIdNot(String documentoIdenficacao, Long id);

    Optional<Paciente> findByDocumentoIdentificacao(String documentoIdentificacao);

    List<Paciente> findByNomeCompletoContainingIgnoreCase(String nomeCompleto);

    List<Paciente> findByNomeCompletoContainingIgnoreCaseAndDocumentoIdentificacao(String nomeCompleto, String documentoIdenficacao);

    @Query("SELECT p FROM Paciente p LEFT JOIN FETCH p.contatos WHERE p.id = :id")
    Optional<Paciente> findByIdWithContatos(Long id);
}
