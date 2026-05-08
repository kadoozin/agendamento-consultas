package com.example.agendamento_consultas.service;

import com.example.agendamento_consultas.database.enums.AgendamentoStatus;
import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.example.agendamento_consultas.database.model.Agendamento;
import com.example.agendamento_consultas.database.model.Paciente;
import com.example.agendamento_consultas.database.repository.AgendamentoRepository;
import com.example.agendamento_consultas.database.repository.PacienteRepository;
import com.example.agendamento_consultas.dto.request.AgendamentoCreateRequest;
import com.example.agendamento_consultas.dto.request.AgendamentoUpdateRequest;
import com.example.agendamento_consultas.dto.response.AgendamentoResponse;
import com.example.agendamento_consultas.exception.BusinessException;
import com.example.agendamento_consultas.mapper.AgendamentoMapper;
import com.example.agendamento_consultas.mapper.AgendamentoUpdateMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private AgendamentoMapper agendamentoMapper;

    @Mock
    private AgendamentoUpdateMapper agendamentoUpdateMapper;

    @Mock
    private AgendamentoNotificationService agendamentoNotificationService;

    @InjectMocks
    private AgendamentoService agendamentoService;

    @Test
    void deveImpedirCriacaoQuandoHorarioSobrepoeOutroAgendamento() {
        LocalDate data = LocalDate.now().plusDays(1);
        Paciente paciente = paciente(1L);
        Agendamento agendamentoExistente = agendamento(2L, paciente(2L), data, LocalTime.of(9, 0), 60);
        AgendamentoCreateRequest request = new AgendamentoCreateRequest(
                1L,
                data,
                LocalTime.of(9, 30),
                30,
                TipoConsulta.PRESENCIAL
        );

        when(pacienteRepository.findByIdWithContatos(1L)).thenReturn(Optional.of(paciente));
        when(agendamentoRepository.findByData(data)).thenReturn(List.of(agendamentoExistente));

        assertThrows(BusinessException.class, () -> agendamentoService.criar(request));

        verify(agendamentoRepository, never()).save(any());
    }

    @Test
    void deveAtualizarPacienteQuandoPacienteIdForInformado() {
        LocalDate data = LocalDate.now().plusDays(2);
        Paciente pacienteAtual = paciente(1L);
        Paciente novoPaciente = paciente(3L);
        Agendamento agendamento = agendamento(10L, pacienteAtual, data, LocalTime.of(10, 0), 60);
        AgendamentoUpdateRequest request = new AgendamentoUpdateRequest(
                3L,
                null,
                null,
                90,
                AgendamentoStatus.AGENDADO,
                TipoConsulta.ONLINE
        );

        when(agendamentoRepository.findById(10L)).thenReturn(Optional.of(agendamento));
        when(pacienteRepository.findByIdWithContatos(3L)).thenReturn(Optional.of(novoPaciente));
        when(agendamentoRepository.findByDataAndIdNot(data, 10L)).thenReturn(List.of());
        when(agendamentoRepository.save(agendamento)).thenReturn(agendamento);
        when(agendamentoMapper.toResponse(agendamento)).thenReturn(new AgendamentoResponse(
                agendamento.getId(),
                null,
                agendamento.getData(),
                agendamento.getHorario(),
                agendamento.getHorarioFim(),
                agendamento.getDuracaoMinutos(),
                agendamento.getStatus(),
                agendamento.getTipoConsulta()
        ));

        agendamentoService.atualizar(10L, request);

        verify(agendamentoUpdateMapper).updateEntity(request, agendamento);
        verify(agendamentoRepository).save(agendamento);
        Assertions.assertEquals(3L, agendamento.getPaciente().getId());
    }

    private Paciente paciente(Long id) {
        Paciente paciente = new Paciente();
        paciente.setId(id);
        paciente.setNomeCompleto("Paciente " + id);
        return paciente;
    }

    private Agendamento agendamento(Long id, Paciente paciente, LocalDate data, LocalTime horario, int duracaoMinutos) {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(id);
        agendamento.setPaciente(paciente);
        agendamento.setData(data);
        agendamento.setHorario(horario);
        agendamento.setDuracaoMinutos(duracaoMinutos);
        agendamento.setStatus(AgendamentoStatus.AGENDADO);
        agendamento.setTipoConsulta(TipoConsulta.PRESENCIAL);
        return agendamento;
    }
}
