package com.example.agendamento_consultas.database.repository;

import com.example.agendamento_consultas.database.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository extends JpaRepository<Long, Agendamento> {
}
