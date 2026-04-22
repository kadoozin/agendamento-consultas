package com.example.agendamento_consultas.service;

import com.example.agendamento_consultas.database.model.Contato;
import com.example.agendamento_consultas.database.model.Paciente;
import com.example.agendamento_consultas.database.repository.ContatoRepository;
import com.example.agendamento_consultas.database.repository.PacienteRepository;
import com.example.agendamento_consultas.dto.request.ContatoCreateRequest;
import com.example.agendamento_consultas.dto.request.ContatoUpdateRequest;
import com.example.agendamento_consultas.dto.response.ContatoResponse;
import com.example.agendamento_consultas.exception.ResourceAlreadyExistsException;
import com.example.agendamento_consultas.exception.ResourceNotFoundException;
import com.example.agendamento_consultas.mapper.ContatoMapper;
import com.example.agendamento_consultas.mapper.ContatoUpdateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContatoService {
    private final ContatoRepository contatoRepository;
    private final ContatoMapper contatoMapper;
    private final ContatoUpdateMapper contatoUpdateMapper;
    private final PacienteRepository pacienteRepository;

    @Transactional
    public ContatoResponse criar(ContatoCreateRequest request) {

        validarContatoCreate(request);

        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        Contato contato = contatoMapper.toEntity(request);
        contato.setPaciente(paciente);

        return contatoMapper.toResponse(contatoRepository.save(contato));
    }

    @Transactional(readOnly = true)
    public ContatoResponse buscarPorId(Long id) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado"));

        return contatoMapper.toResponse(contato);
    }

    @Transactional(readOnly = true)
    public ContatoResponse buscarPorEmail(String email) {
        Contato contato = contatoRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado"));

        return contatoMapper.toResponse(contato);
    }

    @Transactional(readOnly = true)
    public ContatoResponse buscarPorNumero(String numero) {
        Contato contato = contatoRepository.findByNumero(numero)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado"));

        return contatoMapper.toResponse(contato);
    }

    @Transactional(readOnly = true)
    public Page<ContatoResponse> listarTodos(Pageable pageable) {
        return contatoMapper.toResponsePage(contatoRepository.findAll(pageable));
    }

    @Transactional
    public ContatoResponse atualizar(Long id, ContatoUpdateRequest request) {

        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado"));

        validarContatoUpdate(request, id);

        contatoUpdateMapper.updateEntity(request, contato);

        if (request.pacienteId() != null) {
            Paciente paciente = pacienteRepository.findById(request.pacienteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

            contato.setPaciente(paciente);
        }

        return contatoMapper.toResponse(contatoRepository.save(contato));
    }

    @Transactional
    public void deletar(Long id) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado"));

        contatoRepository.delete(contato);
    }

    private void validarContatoCreate(ContatoCreateRequest request) {

        boolean pacienteExiste =
                pacienteRepository.existsById(request.pacienteId());

        boolean emailExiste =
                contatoRepository.existsByEmail(request.email());

        boolean numeroExiste =
                contatoRepository.existsByNumero(request.numero());

        if (!pacienteExiste) {
            throw new ResourceNotFoundException("Paciente não encontrado");
        }

        if (emailExiste) {
            throw new ResourceAlreadyExistsException("Email já cadastrado");
        }

        if (numeroExiste) {
            throw new ResourceAlreadyExistsException("Número já cadastrado");
        }
    }

    private void validarContatoUpdate(ContatoUpdateRequest request, Long id) {

        if (request.pacienteId() != null) {

            boolean pacienteExiste =
                    pacienteRepository.existsById(request.pacienteId());

            if (!pacienteExiste) {
                throw new ResourceNotFoundException("Paciente não encontrado");
            }
        }

        if (request.email() != null) {

            boolean emailExiste =
                    contatoRepository.existsByEmailAndIdNot(request.email(), id);

            if (emailExiste) {
                throw new ResourceAlreadyExistsException("Email já cadastrado");
            }
        }

        if (request.numero() != null) {

            boolean numeroExiste =
                    contatoRepository.existsByNumeroAndIdNot(request.numero(), id);

            if (numeroExiste) {
                throw new ResourceAlreadyExistsException("Número já cadastrado");
            }
        }
    }
}
