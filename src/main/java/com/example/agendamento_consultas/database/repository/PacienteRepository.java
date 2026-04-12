package com.example.agendamento_consultas.database.repository;

import com.example.agendamento_consultas.database.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    boolean existsByDocumentoIdentificacao(String documentoIdentificacao);

    boolean existsByDocumentoIdentificacaoAndIdNot(String documentoIdenficacao, Long id);
}
