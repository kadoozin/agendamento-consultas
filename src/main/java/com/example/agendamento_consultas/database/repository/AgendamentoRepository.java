package com.example.agendamento_consultas.database.repository;

import com.example.agendamento_consultas.database.enums.AgendamentoStatus;
import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.example.agendamento_consultas.database.model.Agendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByData(LocalDate data);

    List<Agendamento> findByDataAndIdNot(LocalDate data, Long id);

    Page<Agendamento> findByTipoConsulta(TipoConsulta tipoConsulta, Pageable pageable);

    List<Agendamento> findByDataAndStatus(LocalDate data, AgendamentoStatus status);
}
