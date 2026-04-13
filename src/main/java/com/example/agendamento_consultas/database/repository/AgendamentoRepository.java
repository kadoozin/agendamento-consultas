package com.example.agendamento_consultas.database.repository;

import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.example.agendamento_consultas.database.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    boolean existsByDataAndHora(LocalDate data, LocalTime hora);

    boolean existsByDataAndHoraAndIdNot(LocalDate data, LocalTime hora, Long id);

    boolean existsByPacienteIdAndDataAndHora(Long pacienteId, LocalDate data, LocalTime hora);

    boolean existsByPacienteIdAndDataAndHoraAndIdNot(Long pacienteId, LocalDate data, LocalTime hora, Long id);

    List<Agendamento> findByTipoConsulta(TipoConsulta tipoConsulta);
}
