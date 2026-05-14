package com.example.agendamento_consultas.service;

import org.junit.jupiter.api.Test;
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
    }

    @Test
    void bootstrapAdminDeveUsarSerializable() throws Exception {
        assertSerializable(AuthService.class, "bootstrapAdmin");
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
}

