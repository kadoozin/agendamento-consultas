package com.example.agendamento_consultas.exception.handler;

import com.example.agendamento_consultas.dto.response.ApiErrorResponse;
import com.example.agendamento_consultas.exception.BusinessException;
import com.example.agendamento_consultas.exception.CepNotFoundException;
import com.example.agendamento_consultas.exception.InvalidCepFormatException;
import com.example.agendamento_consultas.exception.ResourceAlreadyExistsException;
import com.example.agendamento_consultas.exception.ResourceNotFoundException;
import com.example.agendamento_consultas.exception.ServiceUnavailableException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        log.warn("Recurso nao encontrado: {}", ex.getMessage());
        return error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException ex) {
        log.warn("Erro de regra de negocio: {}", ex.getMessage());
        return error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleAlreadyExists(ResourceAlreadyExistsException ex) {
        log.warn("Recurso ja existe: {}", ex.getMessage());
        return error(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InvalidCepFormatException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCepFormat(InvalidCepFormatException ex) {
        log.warn("CEP invalido: {}", ex.getMessage());
        return error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CepNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCepNotFound(CepNotFoundException ex) {
        log.warn("CEP nao encontrado: {}", ex.getMessage());
        return error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ApiErrorResponse> handleServiceUnavailable(ServiceUnavailableException ex) {
        log.error("Servico externo indisponivel", ex);
        return error(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    }

    @ExceptionHandler(ConcurrencyFailureException.class)
    public ResponseEntity<ApiErrorResponse> handleConcurrencyFailure(ConcurrencyFailureException ex) {
        log.warn("Conflito de concorrencia no banco: {}", ex.getMessage());
        return error(HttpStatus.CONFLICT, "Conflito de concorrencia. Tente novamente.");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthentication(AuthenticationException ex) {
        log.warn("Falha de autenticacao: {}", ex.getMessage());
        return error(HttpStatus.UNAUTHORIZED, "Credenciais invalidas");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Dados invalidos");
        return error(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler({ConstraintViolationException.class, HttpMessageNotReadableException.class, MissingRequestHeaderException.class})
    public ResponseEntity<ApiErrorResponse> handleRequestExceptions(Exception ex) {
        log.warn("Falha na requisicao: {}", ex.getMessage());
        return error(HttpStatus.BAD_REQUEST, "Dados invalidos");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        log.error("Erro interno no servidor", ex);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor");
    }

    private ResponseEntity<ApiErrorResponse> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiErrorResponse(message));
    }
}
