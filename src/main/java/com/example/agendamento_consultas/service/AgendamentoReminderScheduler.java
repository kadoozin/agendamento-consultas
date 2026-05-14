package com.example.agendamento_consultas.service;

import com.example.agendamento_consultas.database.enums.AgendamentoStatus;
import com.example.agendamento_consultas.database.model.Agendamento;
import com.example.agendamento_consultas.database.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendamentoReminderScheduler {
    private static final ZoneId SCHEDULER_ZONE = ZoneId.of("America/Sao_Paulo");

    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoNotificationService agendamentoNotificationService;

    @Scheduled(cron = "${app.reminders.cron:0 0 8 * * *}", zone = "America/Sao_Paulo")
    @Transactional(readOnly = true)
    public void enviarLembretesDoDiaSeguinte() {
        LocalDate dataConsulta = LocalDate.now(SCHEDULER_ZONE).plusDays(1);
        List<Agendamento> agendamentos = agendamentoRepository.findByDataAndStatus(
                dataConsulta,
                AgendamentoStatus.AGENDADO
        );

        log.info("Enviando {} lembrete(s) de consulta para {}", agendamentos.size(), dataConsulta);
        agendamentos.forEach(agendamentoNotificationService::enviarLembrete);
    }
}
