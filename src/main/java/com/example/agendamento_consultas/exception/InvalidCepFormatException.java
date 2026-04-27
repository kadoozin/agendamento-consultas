package com.example.agendamento_consultas.exception;

public class InvalidCepFormatException extends RuntimeException {
    public InvalidCepFormatException(String message) {
        super(message);
    }
}
