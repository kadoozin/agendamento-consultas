package com.example.agendamento_consultas.service;

import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.example.agendamento_consultas.database.model.Agendamento;
import com.example.agendamento_consultas.database.model.Paciente;
import com.example.agendamento_consultas.database.repository.AgendamentoRepository;
import com.example.agendamento_consultas.database.repository.PacienteRepository;
import com.example.agendamento_consultas.dto.request.AgendamentoCreateRequest;
import com.example.agendamento_consultas.dto.request.AgendamentoUpdateRequest;
import com.example.agendamento_consultas.dto.response.AgendamentoResponse;
import com.example.agendamento_consultas.exception.BusinessException;
import com.example.agendamento_consultas.exception.ResourceNotFoundException;
import com.example.agendamento_consultas.mapper.AgendamentoMapper;
import com.example.agendamento_consultas.mapper.AgendamentoUpdateMapper;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public AgendamentoResponse criar(AgendamentoCreateRequest request) {
        Paciente paciente = pacienteRepository.findByIdWithContatos(request.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        validarAgendamentoCreate(request, null);

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
    public List<AgendamentoResponse> listar(TipoConsulta tipoConsulta) {
        if (tipoConsulta != null){
            return agendamentoMapper.toResponseList(agendamentoRepository.findByTipoConsulta(tipoConsulta));
        }
        return agendamentoMapper.toResponseList(agendamentoRepository.findAll());
    }

    @Transactional
    public AgendamentoResponse atualizar(Long id, AgendamentoUpdateRequest request) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        validarAgendamentoUpdate(request, id, agendamento);

        agendamentoUpdateMapper.updateEntity(request, agendamento);

        return agendamentoMapper.toResponse(agendamentoRepository.save(agendamento));
    }

    @Transactional
    public void deletar(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        agendamentoRepository.delete(agendamento);
    }

    private void validarAgendamentoCreate(AgendamentoCreateRequest request, Long id) {

        boolean horarioIndisponivel =
                agendamentoRepository.existsByDataAndHorario(request.data(), request.horario());

        boolean pacienteJaAgendado =
                agendamentoRepository.existsByPacienteIdAndDataAndHorario(
                        request.pacienteId(),
                        request.data(),
                        request.horario()
                );

        if (horarioIndisponivel) {
            throw new BusinessException("Horário já ocupado");
        }

        if (pacienteJaAgendado) {
            throw new BusinessException("Paciente já possui agendamento neste horário");
        }
    }

    private void validarAgendamentoUpdate(AgendamentoUpdateRequest request, Long id, Agendamento agendamentoAtual) {

        LocalDate data = request.data() != null ? request.data() : agendamentoAtual.getData();
        LocalTime horario = request.horario() != null ? request.horario() : agendamentoAtual.getHorario();
        Long pacienteId = request.pacienteId() != null ? request.pacienteId() : agendamentoAtual.getPaciente().getId();

        boolean horarioIndisponivel =
                agendamentoRepository.existsByDataAndHorarioAndIdNot(
                        data,
                        horario,
                        id
                );

        if (horarioIndisponivel) {
            throw new BusinessException("Horário já ocupado");
        }

        boolean pacienteJaAgendado =
                agendamentoRepository.existsByPacienteIdAndDataAndHorarioAndIdNot(
                        pacienteId,
                        data,
                        horario,
                        id
                );

        if (pacienteJaAgendado) {
            throw new BusinessException("Paciente já possui agendamento neste horário");
        }
    }
}
