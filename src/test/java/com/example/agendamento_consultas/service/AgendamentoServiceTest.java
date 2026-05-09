package com.example.agendamento_consultas.service;

import com.example.agendamento_consultas.database.enums.AgendamentoStatus;
import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.example.agendamento_consultas.database.model.Agendamento;
import com.example.agendamento_consultas.database.model.Contato;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
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
        doAnswer(invocation -> {
            AgendamentoUpdateRequest updateRequest = invocation.getArgument(0);
            Agendamento entity = invocation.getArgument(1);
            entity.setDuracaoMinutos(updateRequest.duracaoMinutos());
            entity.setStatus(updateRequest.status());
            entity.setTipoConsulta(updateRequest.tipoConsulta());
            return null;
        }).when(agendamentoUpdateMapper).updateEntity(request, agendamento);

        agendamentoService.atualizar(10L, request);

        verify(agendamentoUpdateMapper).updateEntity(request, agendamento);
        verify(agendamentoRepository).save(agendamento);
        Assertions.assertEquals(3L, agendamento.getPaciente().getId());
    }

    @Test
    void deveImpedirCriacaoEmDataPassada() {
        LocalDate dataPassada = LocalDate.now().minusDays(1);
        Paciente paciente = paciente(1L);
        AgendamentoCreateRequest request = new AgendamentoCreateRequest(
                1L,
                dataPassada,
                LocalTime.of(9, 0),
                30,
                TipoConsulta.PRESENCIAL
        );

        when(pacienteRepository.findByIdWithContatos(1L)).thenReturn(Optional.of(paciente));

        assertThrows(BusinessException.class, () -> agendamentoService.criar(request));

        verify(agendamentoRepository, never()).save(any());
        verify(agendamentoNotificationService, never()).enviarConfirmacao(any());
    }

    @Test
    void deveImpedirCriacaoQuandoPacienteJaPossuiOutroAgendamentoSobreposto() {
        LocalDate data = LocalDate.now().plusDays(1);
        Paciente paciente = paciente(1L);
        Agendamento agendamentoExistente = agendamento(2L, paciente, data, LocalTime.of(9, 0), 60);
        AgendamentoCreateRequest request = new AgendamentoCreateRequest(
                1L,
                data,
                LocalTime.of(9, 15),
                30,
                TipoConsulta.ONLINE
        );

        when(pacienteRepository.findByIdWithContatos(1L)).thenReturn(Optional.of(paciente));
        when(agendamentoRepository.findByData(data)).thenReturn(List.of(agendamentoExistente));

        assertThrows(BusinessException.class, () -> agendamentoService.criar(request));

        verify(agendamentoRepository, never()).save(any());
    }

    @Test
    void deveEnviarAgradecimentoQuandoStatusForAtualizadoParaConcluido() {
        LocalDate data = LocalDate.now().plusDays(2);
        Agendamento agendamento = agendamento(10L, paciente(1L), data, LocalTime.of(10, 0), 60);
        AgendamentoUpdateRequest request = new AgendamentoUpdateRequest(
                null,
                null,
                null,
                null,
                AgendamentoStatus.CONCLUIDO,
                null
        );

        when(agendamentoRepository.findById(10L)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.findByDataAndIdNot(data, 10L)).thenReturn(List.of());
        when(agendamentoRepository.save(agendamento)).thenReturn(agendamento);
        when(agendamentoMapper.toResponse(agendamento)).thenReturn(new AgendamentoResponse(
                agendamento.getId(),
                null,
                agendamento.getData(),
                agendamento.getHorario(),
                agendamento.getHorarioFim(),
                agendamento.getDuracaoMinutos(),
                AgendamentoStatus.CONCLUIDO,
                agendamento.getTipoConsulta()
        ));
        doAnswer(invocation -> {
            AgendamentoUpdateRequest updateRequest = invocation.getArgument(0);
            Agendamento entity = invocation.getArgument(1);
            entity.setStatus(updateRequest.status());
            return null;
        }).when(agendamentoUpdateMapper).updateEntity(request, agendamento);

        agendamento.setStatus(AgendamentoStatus.AGENDADO);
        agendamentoService.atualizar(10L, request);

        verify(agendamentoNotificationService).enviarAgradecimento(agendamento);
    }

    @Test
    void deveEnviarAtualizacaoQuandoNaoConcluirAgendamento() {
        LocalDate data = LocalDate.now().plusDays(2);
        Agendamento agendamento = agendamento(10L, paciente(1L), data, LocalTime.of(10, 0), 60);
        AgendamentoUpdateRequest request = new AgendamentoUpdateRequest(
                null,
                data.plusDays(1),
                LocalTime.of(11, 0),
                45,
                AgendamentoStatus.AGENDADO,
                TipoConsulta.ONLINE
        );

        when(agendamentoRepository.findById(10L)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.findByDataAndIdNot(data.plusDays(1), 10L)).thenReturn(List.of());
        when(agendamentoRepository.save(agendamento)).thenAnswer(invocation -> {
            Agendamento salvo = invocation.getArgument(0);
            return salvo;
        });
        when(agendamentoMapper.toResponse(agendamento)).thenReturn(new AgendamentoResponse(
            agendamento.getId(),
            null,
                request.data(),
                request.horario(),
                request.horario().plusMinutes(request.duracaoMinutos()),
                request.duracaoMinutos(),
                AgendamentoStatus.AGENDADO,
                request.tipoConsulta()
        ));
        doAnswer(invocation -> {
            AgendamentoUpdateRequest updateRequest = invocation.getArgument(0);
            Agendamento entity = invocation.getArgument(1);
            entity.setData(updateRequest.data());
            entity.setHorario(updateRequest.horario());
            entity.setDuracaoMinutos(updateRequest.duracaoMinutos());
            entity.setStatus(updateRequest.status());
            entity.setTipoConsulta(updateRequest.tipoConsulta());
            return null;
        }).when(agendamentoUpdateMapper).updateEntity(request, agendamento);

        agendamentoService.atualizar(10L, request);

        ArgumentCaptor<Agendamento> agendamentoCaptor = ArgumentCaptor.forClass(Agendamento.class);
        verify(agendamentoNotificationService).enviarAtualizacao(
                agendamentoCaptor.capture(),
                eq(data),
                eq(LocalTime.of(10, 0)),
                eq(TipoConsulta.PRESENCIAL)
        );
        assertEquals(request.data(), agendamentoCaptor.getValue().getData());
        assertEquals(request.horario(), agendamentoCaptor.getValue().getHorario());
    }

    private Paciente paciente(Long id) {
        Paciente paciente = new Paciente();
        paciente.setId(id);
        paciente.setNomeCompleto("Paciente " + id);
        paciente.setContatos(contatos(id));
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

    private Set<Contato> contatos(Long pacienteId) {
        Contato contato = new Contato();
        contato.setId(pacienteId);
        contato.setNumero("551199999999" + pacienteId);
        contato.setEmail("paciente" + pacienteId + "@email.com");

        Set<Contato> contatos = new HashSet<>();
        contatos.add(contato);
        return contatos;
    }
}
