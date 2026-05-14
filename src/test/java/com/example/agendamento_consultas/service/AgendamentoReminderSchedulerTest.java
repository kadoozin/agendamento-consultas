package com.example.agendamento_consultas.service;

import com.example.agendamento_consultas.database.enums.AgendamentoStatus;
import com.example.agendamento_consultas.database.repository.AgendamentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendamentoReminderSchedulerTest {

    private static final ZoneId SAO_PAULO = ZoneId.of("America/Sao_Paulo");

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private AgendamentoNotificationService agendamentoNotificationService;

    @InjectMocks
    private AgendamentoReminderScheduler scheduler;

    @Test
    void deveBuscarLembretesComDataBaseadaNoTimezoneDeSaoPaulo() {
        when(agendamentoRepository.findByDataAndStatus(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(AgendamentoStatus.AGENDADO)))
                .thenReturn(List.of());

        scheduler.enviarLembretesDoDiaSeguinte();

        ArgumentCaptor<LocalDate> dataCaptor = ArgumentCaptor.forClass(LocalDate.class);
        verify(agendamentoRepository).findByDataAndStatus(dataCaptor.capture(), org.mockito.ArgumentMatchers.eq(AgendamentoStatus.AGENDADO));
        assertEquals(LocalDate.now(SAO_PAULO).plusDays(1), dataCaptor.getValue());
    }
}

