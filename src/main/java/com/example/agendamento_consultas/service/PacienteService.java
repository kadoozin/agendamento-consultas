package com.example.agendamento_consultas.service;

import com.example.agendamento_consultas.database.model.Paciente;
import com.example.agendamento_consultas.database.repository.PacienteRepository;
import com.example.agendamento_consultas.dto.request.PacienteCreateRequest;
import com.example.agendamento_consultas.dto.request.PacienteUpdateRequest;
import com.example.agendamento_consultas.dto.response.PacienteResponse;
import com.example.agendamento_consultas.exception.ResourceAlreadyExistsException;
import com.example.agendamento_consultas.exception.ResourceNotFoundException;
import com.example.agendamento_consultas.mapper.PacienteMapper;
import com.example.agendamento_consultas.mapper.PacienteUpdateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final PacienteRepository pacienteRepository;
    private final PacienteUpdateMapper pacienteUpdateMapper;
    private final PacienteMapper pacienteMapper;

    @Transactional
    public PacienteResponse criar(PacienteCreateRequest request) {
        validarDocumento(request.documentoIdentificacao(), null);

        Paciente paciente = pacienteMapper.toEntity(request);

        return pacienteMapper.toResponse(pacienteRepository.save(paciente));
    }

    @Transactional(readOnly = true)
    public List<PacienteResponse> buscar(String nome, String documento) {

        if (nome != null && documento != null) {
            return pacienteMapper.toResponseList(
                    pacienteRepository.findByNomeContainingIgnoreCaseAndDocumentoIdentificacao(nome, documento)
            );
        }

        if (nome != null) {
            return pacienteMapper.toResponseList(
                    pacienteRepository.findByNomeContainingIgnoreCase(nome)
            );
        }

        if (documento != null) {
            Paciente paciente = pacienteRepository.findByDocumentoIdentificacao(documento)
                    .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

            return List.of(pacienteMapper.toResponse(paciente));
        }

        return listar();
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        return pacienteMapper.toResponse(paciente);
    }

    @Transactional(readOnly = true)
    public List<PacienteResponse> listar() {
        return pacienteMapper.toResponseList(pacienteRepository.findAll());
    }

    @Transactional
    public PacienteResponse atualizar(Long id, PacienteUpdateRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        if (request.documentoIdentificacao() != null){
            validarDocumento(request.documentoIdentificacao(), id);
        }

        pacienteUpdateMapper.updateEntity(request, paciente);

        return pacienteMapper.toResponse(pacienteRepository.save(paciente));
    }

    @Transactional
    public void deletar(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        pacienteRepository.delete(paciente);
    }

    private void validarDocumento(String documento, Long id) {
        boolean existe = (id == null)
                ? pacienteRepository.existsByDocumentoIdentificacao(documento)
                : pacienteRepository.existsByDocumentoIdentificacaoAndIdNot(documento, id);

        if (existe) {
            throw new ResourceAlreadyExistsException("Documento de identificação já cadastrado");
        }
    }
}
