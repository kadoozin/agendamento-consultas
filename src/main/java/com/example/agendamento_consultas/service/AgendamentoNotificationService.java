package com.example.agendamento_consultas.service;

import com.example.agendamento_consultas.database.enums.AgendamentoStatus;
import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.example.agendamento_consultas.database.model.Agendamento;
import com.example.agendamento_consultas.database.model.Contato;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgendamentoNotificationService {

    private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORARIO_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final NotificationService notificationService;

    public void enviarConfirmacao(Agendamento agendamento) {
        enviarMensagem(agendamento, "Consulta agendada");
    }

    public void enviarAtualizacao(Agendamento agendamento) {
        enviarMensagem(agendamento, "Consulta atualizada");
    }

    public void enviarAtualizacao(
            Agendamento agendamento,
            LocalDate dataAnterior,
            LocalTime horarioAnterior,
            TipoConsulta tipoConsultaAnterior
    ) {
        buscarTelefonePrincipal(agendamento)
                .ifPresent(numero -> notificationService.enviarMensagem(
                        numero,
                        montarMensagemAtualizacao(agendamento, dataAnterior, horarioAnterior, tipoConsultaAnterior)
                ));
    }

    public void enviarLembrete(Agendamento agendamento) {
        enviarMensagem(agendamento, "Lembrete de consulta");
    }

    public void enviarAgradecimento(Agendamento agendamento) {
        if (agendamento.getStatus() != AgendamentoStatus.CONCLUIDO) {
            return;
        }

        buscarTelefonePrincipal(agendamento)
                .ifPresent(numero -> notificationService.enviarMensagem(numero, montarMensagemAgradecimento(agendamento)));
    }

    private void enviarMensagem(Agendamento agendamento, String titulo) {
        buscarTelefonePrincipal(agendamento)
                .ifPresent(numero -> notificationService.enviarMensagem(numero, montarMensagem(agendamento, titulo)));
    }

    private Optional<String> buscarTelefonePrincipal(Agendamento agendamento) {
        return agendamento.getPaciente().getContatos().stream()
                .map(Contato::getNumero)
                .filter(numero -> numero != null && !numero.isBlank())
                .min(Comparator.naturalOrder());
    }

    private String montarMensagem(Agendamento agendamento, String titulo) {
        return "%s: %s, sua consulta %s esta marcada para %s as %s."
                .formatted(
                        titulo,
                        agendamento.getPaciente().getNomeCompleto(),
                        agendamento.getTipoConsulta().name().toLowerCase(),
                        agendamento.getData().format(DATA_FORMATTER),
                        agendamento.getHorario().format(HORARIO_FORMATTER)
                );
    }

    private String montarMensagemAtualizacao(
            Agendamento agendamento,
            LocalDate dataAnterior,
            LocalTime horarioAnterior,
            TipoConsulta tipoConsultaAnterior
    ) {
        List<String> alteracoes = new ArrayList<>();

        if (!Objects.equals(dataAnterior, agendamento.getData())) {
            alteracoes.add("data de %s para %s".formatted(
                    dataAnterior.format(DATA_FORMATTER),
                    agendamento.getData().format(DATA_FORMATTER)
            ));
        }

        if (!Objects.equals(horarioAnterior, agendamento.getHorario())) {
            alteracoes.add("horario de %s para %s".formatted(
                    horarioAnterior.format(HORARIO_FORMATTER),
                    agendamento.getHorario().format(HORARIO_FORMATTER)
            ));
        }

        if (!Objects.equals(tipoConsultaAnterior, agendamento.getTipoConsulta())) {
            alteracoes.add("modalidade de %s para %s".formatted(
                    formatarTipoConsulta(tipoConsultaAnterior),
                    formatarTipoConsulta(agendamento.getTipoConsulta())
            ));
        }

        if (alteracoes.isEmpty()) {
            return montarMensagem(agendamento, "Consulta atualizada");
        }

        return "Consulta atualizada: %s, sua consulta foi alterada: %s. Novo agendamento: %s em %s as %s."
                .formatted(
                        agendamento.getPaciente().getNomeCompleto(),
                        String.join(", ", alteracoes),
                        formatarTipoConsulta(agendamento.getTipoConsulta()),
                        agendamento.getData().format(DATA_FORMATTER),
                        agendamento.getHorario().format(HORARIO_FORMATTER)
                );
    }

    private String formatarTipoConsulta(TipoConsulta tipoConsulta) {
        return tipoConsulta.name().toLowerCase();
    }

    private String montarMensagemAgradecimento(Agendamento agendamento) {
        return "%s, sua consulta foi concluida. Obrigado por escolher nossos servicos!"
                .formatted(agendamento.getPaciente().getNomeCompleto());
    }
}
