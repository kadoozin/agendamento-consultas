package com.example.agendamento_consultas.service;

import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.example.agendamento_consultas.database.model.Agendamento;
import com.example.agendamento_consultas.database.model.Paciente;
import com.example.agendamento_consultas.database.repository.AgendamentoRepository;
import com.example.agendamento_consultas.database.repository.PacienteRepository;
import com.example.agendamento_consultas.dto.request.AgendamentoRequest;
import com.example.agendamento_consultas.dto.request.AtualizarStatusRequest;
import com.example.agendamento_consultas.dto.request.AtualizarTipoConsultaRequest;
import com.example.agendamento_consultas.dto.response.AgendamentoResponse;
import com.example.agendamento_consultas.exception.BusinessException;
import com.example.agendamento_consultas.exception.ResourceNotFoundException;
import com.example.agendamento_consultas.mapper.AgendamentoMapper;
import com.example.agendamento_consultas.mapper.AgendamentoUpdateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteRepository pacienteRepository;
    private final AgendamentoMapper agendamentoMapper;
    private final AgendamentoUpdateMapper agendamentoUpdateMapper;

    @Transactional
    public AgendamentoResponse criar(AgendamentoRequest request) {
        Paciente paciente = pacienteRepository.findByIdWithContatos(request.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        validarAgendamento(request, null);

        Agendamento agendamento = agendamentoMapper.toEntity(request);
        agendamento.setPaciente(paciente);

        return agendamentoMapper.toResponse(agendamentoRepository.save(agendamento));
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        return agendamentoMapper.toResponse(agendamento);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarTodos() {
        return agendamentoMapper.toResponseList(agendamentoRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorTipo(TipoConsulta tipoConsulta) {
        return agendamentoMapper.toResponseList(agendamentoRepository.findByTipoConsulta(tipoConsulta));
    }

    @Transactional
    public AgendamentoResponse atualizar(Long id, AgendamentoRequest request) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        validarAgendamento(request, id);

        agendamentoUpdateMapper.updateEntity(request, agendamento);

        return agendamentoMapper.toResponse(agendamentoRepository.save(agendamento));
    }

    @Transactional
    public AgendamentoResponse atualizarStatus(Long id, AtualizarStatusRequest request) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        agendamento.setStatus(request.atualizarStatus());

        return agendamentoMapper.toResponse(agendamentoRepository.save(agendamento));
    }

    @Transactional
    public AgendamentoResponse atualizarTipoConsulta(Long id, AtualizarTipoConsultaRequest request){
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        agendamento.setTipoConsulta(request.atualizarTipoConsulta());

        return agendamentoMapper.toResponse(agendamentoRepository.save(agendamento));
    }

    @Transactional
    public void deletar(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        agendamentoRepository.delete(agendamento);
    }

    private void validarAgendamento(AgendamentoRequest request, Long id) {
        boolean horarioIndisponivel = id == null
                ? agendamentoRepository.existsByDataAndHorario(request.data(), request.horario())
                : agendamentoRepository.existsByDataAndHorarioAndIdNot(request.data(), request.horario(), id);

        boolean pacienteJaAgendado = id == null
                ? agendamentoRepository.existsByPacienteIdAndDataAndHorario(request.pacienteId(), request.data(), request.horario())
                : agendamentoRepository.existsByPacienteIdAndDataAndHorarioAndIdNot(request.pacienteId(), request.data(), request.horario(), id);

        if (horarioIndisponivel) throw new BusinessException("Horário já ocupado");
        if (pacienteJaAgendado) throw new BusinessException("Paciente já possui agendamento neste horário");
    }
}
