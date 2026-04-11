package com.example.agendamento_consultas.service;

import com.example.agendamento_consultas.database.model.Paciente;
import com.example.agendamento_consultas.database.repository.PacienteRepository;
import com.example.agendamento_consultas.dto.request.PacienteRequest;
import com.example.agendamento_consultas.dto.response.PacienteResponse;
import com.example.agendamento_consultas.exception.BusinessException;
import com.example.agendamento_consultas.exception.ResourceAlreadyExistsException;
import com.example.agendamento_consultas.exception.ResourceNotFoundException;
import com.example.agendamento_consultas.mapper.PacienteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    @Transactional
    public PacienteResponse criar(PacienteRequest request) {
        if (pacienteRepository.existsByDocumentoIdentificacao(request.documentoIdentificacao())) {
            throw new ResourceAlreadyExistsException("Documento de Idenficação já cadastrado");
        }

        Paciente paciente = pacienteMapper.toEntity(request);

        return pacienteMapper.toResponse(pacienteRepository.save(paciente));
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        return pacienteMapper.toResponse(paciente);
    }

    @Transactional(readOnly = true)
    public List<PacienteResponse> listarTodos() {
        return pacienteMapper.toResponseList(pacienteRepository.findAll());
    }

    @Transactional
    public PacienteResponse atualizar(Long id, PacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        if (pacienteRepository.existsByDocumentoIdentificacaoAndIdNot(request.documentoIdentificacao(), id)){
            throw  new ResourceAlreadyExistsException("Documento de Identificação já cadastrado");
        }

        pacienteMapper.updateEntity(request, paciente);

        return pacienteMapper.toResponse(pacienteRepository.save(paciente));
    }

    @Transactional
    public void deletar(Long id){
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        pacienteRepository.delete(paciente);
    }
}
