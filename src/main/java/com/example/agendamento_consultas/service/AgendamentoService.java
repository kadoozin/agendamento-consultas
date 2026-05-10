package com.example.agendamento_consultas.service;

import com.example.agendamento_consultas.database.enums.AgendamentoStatus;
import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.example.agendamento_consultas.database.model.Agendamento;
import com.example.agendamento_consultas.database.model.Paciente;
import com.example.agendamento_consultas.database.repository.AgendamentoRepository;
import com.example.agendamento_consultas.database.repository.PacienteRepository;
import com.example.agendamento_consultas.dto.request.AgendamentoCreateRequest;
import com.example.agendamento_consultas.dto.request.AgendamentoUpdateRequest;
import com.example.agendamento_consultas.dto.request.ReagendamentoRequest;
import com.example.agendamento_consultas.dto.response.AgendamentoResponse;
import com.example.agendamento_consultas.exception.BusinessException;
import com.example.agendamento_consultas.exception.ResourceNotFoundException;
import com.example.agendamento_consultas.mapper.AgendamentoMapper;
import com.example.agendamento_consultas.mapper.AgendamentoUpdateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteRepository pacienteRepository;
    private final AgendamentoMapper agendamentoMapper;
    private final AgendamentoUpdateMapper agendamentoUpdateMapper;
    private final AgendamentoNotificationService agendamentoNotificationService;

    @Transactional
    public AgendamentoResponse criar(AgendamentoCreateRequest request) {
        Paciente paciente = pacienteRepository.findByIdWithContatos(request.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente nao encontrado"));

        validarAgendamento(
                request.data(),
                request.horario(),
                request.duracaoMinutos(),
                paciente.getId(),
                null
        );

        Agendamento agendamento = agendamentoMapper.toEntity(request);
        agendamento.setPaciente(paciente);

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);
        agendamentoNotificationService.enviarConfirmacao(agendamentoSalvo);

        return agendamentoMapper.toResponse(agendamentoSalvo);
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento nao encontrado"));

        return agendamentoMapper.toResponse(agendamento);
    }

    @Transactional(readOnly = true)
    public Page<AgendamentoResponse> listar(TipoConsulta tipoConsulta, Pageable pageable) {
        if (tipoConsulta != null) {
            return agendamentoMapper.toResponsePage(agendamentoRepository.findByTipoConsulta(tipoConsulta, pageable));
        }
        return agendamentoMapper.toResponsePage(agendamentoRepository.findAll(pageable));
    }

    @Transactional
    public AgendamentoResponse atualizar(Long id, AgendamentoUpdateRequest request) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento nao encontrado"));

        Paciente paciente = resolverPacienteAtualizacao(request, agendamento);
        LocalDate data = request.data() != null ? request.data() : agendamento.getData();
        LocalTime horario = request.horario() != null ? request.horario() : agendamento.getHorario();
        Integer duracaoMinutos = request.duracaoMinutos() != null
                ? request.duracaoMinutos()
                : agendamento.getDuracaoMinutos();

        validarAgendamento(data, horario, duracaoMinutos, paciente.getId(), id);

        LocalDate dataAnterior = agendamento.getData();
        LocalTime horarioAnterior = agendamento.getHorario();
        TipoConsulta tipoConsultaAnterior = agendamento.getTipoConsulta();
        AgendamentoStatus statusAnterior = agendamento.getStatus();

        agendamentoUpdateMapper.updateEntity(request, agendamento);
        agendamento.setPaciente(paciente);

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);

        if (statusAnterior != AgendamentoStatus.CONCLUIDO
                && agendamentoSalvo.getStatus() == AgendamentoStatus.CONCLUIDO) {
            agendamentoNotificationService.enviarAgradecimento(agendamentoSalvo);
        } else {
            agendamentoNotificationService.enviarAtualizacao(
                    agendamentoSalvo,
                    dataAnterior,
                    horarioAnterior,
                    tipoConsultaAnterior
            );
        }

        return agendamentoMapper.toResponse(agendamentoSalvo);
    }

    @Transactional
    public AgendamentoResponse reagendar(Long id, ReagendamentoRequest request) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento nao encontrado"));

        validarReagendamento(agendamento);
        validarAgendamento(
                request.data(),
                request.horario(),
                request.duracaoMinutos(),
                agendamento.getPaciente().getId(),
                id
        );

        LocalDate dataAnterior = agendamento.getData();
        LocalTime horarioAnterior = agendamento.getHorario();
        TipoConsulta tipoConsultaAnterior = agendamento.getTipoConsulta();

        agendamento.setData(request.data());
        agendamento.setHorario(request.horario());
        agendamento.setDuracaoMinutos(request.duracaoMinutos());
        agendamento.setStatus(AgendamentoStatus.AGENDADO);

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);
        agendamentoNotificationService.enviarAtualizacao(
                agendamentoSalvo,
                dataAnterior,
                horarioAnterior,
                tipoConsultaAnterior
        );

        return agendamentoMapper.toResponse(agendamentoSalvo);
    }

    @Transactional
    public AgendamentoResponse cancelar(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento nao encontrado"));

        validarCancelamento(agendamento);
        agendamento.setStatus(AgendamentoStatus.CANCELADO);

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);
        agendamentoNotificationService.enviarCancelamento(agendamentoSalvo);

        return agendamentoMapper.toResponse(agendamentoSalvo);
    }

    @Transactional
    public void deletar(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento nao encontrado"));

        agendamentoRepository.delete(agendamento);
    }

    private Paciente resolverPacienteAtualizacao(AgendamentoUpdateRequest request, Agendamento agendamento) {
        if (request.pacienteId() == null || request.pacienteId().equals(agendamento.getPaciente().getId())) {
            return agendamento.getPaciente();
        }

        return pacienteRepository.findByIdWithContatos(request.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente nao encontrado"));
    }

    private void validarReagendamento(Agendamento agendamento) {
        if (agendamento.getStatus() == AgendamentoStatus.CANCELADO) {
            throw new BusinessException("Nao e possivel reagendar um agendamento cancelado");
        }

        if (agendamento.getStatus() == AgendamentoStatus.CONCLUIDO) {
            throw new BusinessException("Nao e possivel reagendar um agendamento concluido");
        }
    }

    private void validarCancelamento(Agendamento agendamento) {
        if (agendamento.getStatus() == AgendamentoStatus.CANCELADO) {
            throw new BusinessException("Agendamento ja esta cancelado");
        }

        if (agendamento.getStatus() == AgendamentoStatus.CONCLUIDO) {
            throw new BusinessException("Nao e possivel cancelar um agendamento concluido");
        }
    }

    private void validarAgendamento(
            LocalDate data,
            LocalTime horario,
            Integer duracaoMinutos,
            Long pacienteId,
            Long agendamentoIgnoradoId
    ) {
        if (data.isBefore(LocalDate.now())) {
            throw new BusinessException("Nao e possivel agendar para datas passadas");
        }

        LocalTime horarioFim = horario.plusMinutes(duracaoMinutos);
        if (!horarioFim.isAfter(horario)) {
            throw new BusinessException("O horario final da consulta deve ser no mesmo dia e posterior ao inicio");
        }

        List<Agendamento> agendamentosNoDia = buscarAgendamentosNoDia(data, agendamentoIgnoradoId).stream()
                .filter(agendamento -> agendamento.getStatus() != AgendamentoStatus.CANCELADO)
                .toList();

        boolean horarioIndisponivel = agendamentosNoDia.stream()
                .anyMatch(agendamentoExistente -> horariosSeSobrepoem(
                        horario,
                        horarioFim,
                        agendamentoExistente.getHorario(),
                        agendamentoExistente.getHorarioFim()
                ));

        if (horarioIndisponivel) {
            throw new BusinessException("Ja existe outro agendamento que sobrepoe este intervalo");
        }

        boolean pacienteJaAgendado = agendamentosNoDia.stream()
                .filter(agendamento -> agendamento.getPaciente().getId().equals(pacienteId))
                .anyMatch(agendamentoExistente -> horariosSeSobrepoem(
                        horario,
                        horarioFim,
                        agendamentoExistente.getHorario(),
                        agendamentoExistente.getHorarioFim()
                ));

        if (pacienteJaAgendado) {
            throw new BusinessException("Paciente ja possui outro agendamento que sobrepoe este intervalo");
        }
    }

    private List<Agendamento> buscarAgendamentosNoDia(LocalDate data, Long agendamentoIgnoradoId) {
        if (agendamentoIgnoradoId == null) {
            return agendamentoRepository.findByData(data);
        }

        return agendamentoRepository.findByDataAndIdNot(data, agendamentoIgnoradoId);
    }

    private boolean horariosSeSobrepoem(
            LocalTime inicioSolicitado,
            LocalTime fimSolicitado,
            LocalTime inicioExistente,
            LocalTime fimExistente
    ) {
        return inicioSolicitado.isBefore(fimExistente) && fimSolicitado.isAfter(inicioExistente);
    }
}
