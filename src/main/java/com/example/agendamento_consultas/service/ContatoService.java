package com.example.agendamento_consultas.service;

import com.example.agendamento_consultas.database.model.Contato;
import com.example.agendamento_consultas.database.repository.ContatoRepository;
import com.example.agendamento_consultas.database.repository.PacienteRepository;
import com.example.agendamento_consultas.dto.request.ContatoRequest;
import com.example.agendamento_consultas.dto.response.ContatoResponse;
import com.example.agendamento_consultas.exception.ResourceAlreadyExistsException;
import com.example.agendamento_consultas.exception.ResourceNotFoundException;
import com.example.agendamento_consultas.mapper.ContatoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContatoService {

    private final ContatoRepository contatoRepository;
    private final ContatoMapper contatoMapper;
    private final PacienteRepository pacienteRepository;

    @Transactional
    public ContatoResponse criar(ContatoRequest request) {
        validarContato(request, null);

        Contato contato = contatoMapper.toEntity(request);

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
    public List<ContatoResponse> listarTodos() {
        return contatoMapper.toResponseList(contatoRepository.findAll());
    }

    @Transactional
    public ContatoResponse atualizar(Long id, ContatoRequest request) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado"));

        validarContato(request, id);

        contatoMapper.updateEntity(request, contato);

        return contatoMapper.toResponse(contatoRepository.save(contato));
    }

    @Transactional
    public void deletar(Long id) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado"));

        contatoRepository.delete(contato);
    }

    private void validarContato(ContatoRequest request, Long id) {
        boolean pacienteExiste = pacienteRepository.existsById(request.pacienteId());
        boolean emailExiste = id == null
                ? contatoRepository.existsByEmail(request.email())
                : contatoRepository.existsByEmailAndIdNot(request.email(), id);
        boolean numeroExiste = id == null
                ? contatoRepository.existsByNumero(request.numero())
                : contatoRepository.existsByNumeroAndIdNot(request.numero(), id);

        if (!pacienteExiste) throw new ResourceNotFoundException("Paciente não encontrado");
        if (emailExiste) throw new ResourceAlreadyExistsException("Email já cadastrado");
        if (numeroExiste) throw new ResourceAlreadyExistsException("Número já cadastrado");
    }
}
