package com.example.agendamento_consultas.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TransactionalIsolationTest {

    @Test
    void agendamentoServiceOperacoesCriticasDevemUsarSerializable() throws Exception {
        assertSerializable(AgendamentoService.class, "criar");
        assertSerializable(AgendamentoService.class, "atualizar");
        assertSerializable(AgendamentoService.class, "reagendar");
        assertRetryableForConcurrencyFailure(AgendamentoService.class, "criar");
        assertRetryableForConcurrencyFailure(AgendamentoService.class, "atualizar");
        assertRetryableForConcurrencyFailure(AgendamentoService.class, "reagendar");
    }

    @Test
    void bootstrapAdminDeveUsarSerializable() throws Exception {
        assertSerializable(AuthService.class, "bootstrapAdmin");
        assertRetryableForConcurrencyFailure(AuthService.class, "bootstrapAdmin");
    }

    private void assertSerializable(Class<?> type, String methodName) throws Exception {
        Method method = findMethodByName(type, methodName);
        Transactional transactional = method.getAnnotation(Transactional.class);
        assertNotNull(transactional, "Metodo " + type.getSimpleName() + "." + methodName + " precisa de @Transactional");
        assertEquals(Isolation.SERIALIZABLE, transactional.isolation());
    }

    private Method findMethodByName(Class<?> type, String methodName) {
        for (Method method : type.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Metodo nao encontrado: " + type.getSimpleName() + "." + methodName);
    }

    private void assertRetryableForConcurrencyFailure(Class<?> type, String methodName) throws Exception {
        Method method = findMethodByName(type, methodName);
        Retryable retryable = method.getAnnotation(Retryable.class);
        Assertions.assertNotNull(retryable, "Metodo " + type.getSimpleName() + "." + methodName + " precisa de @Retryable");
        boolean handlesConcurrencyFailure = false;
        for (Class<? extends Throwable> retryClass : retryable.retryFor()) {
            if (retryClass.equals(ConcurrencyFailureException.class)) {
                handlesConcurrencyFailure = true;
                break;
            }
        }
        Assertions.assertTrue(handlesConcurrencyFailure, "Metodo " + type.getSimpleName() + "." + methodName + " deve retentar ConcurrencyFailureException");
    }
}
