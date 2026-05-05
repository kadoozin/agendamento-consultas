package com.example.agendamento_consultas.service;

import com.example.agendamento_consultas.config.TwilioProperties;
import com.example.agendamento_consultas.exception.ServiceUnavailableException;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwilioNotificationService implements NotificationService {

    private final TwilioProperties properties;

    @PostConstruct
    void init() {
        if (!properties.isEnabled()) {
            log.info("Twilio desativado. Defina TWILIO_ENABLED=true para enviar mensagens reais.");
            return;
        }

        validarConfiguracao();
        Twilio.init(properties.getAccountSid(), properties.getAuthToken());
    }

    @Override
    public void enviarMensagem(String numeroDestino, String mensagem) {
        String destino = formatarNumero(numeroDestino);
        String remetente = formatarNumero(properties.getFrom());

        if (!properties.isEnabled()) {
            log.info("Mensagem simulada para {}: {}", destino, mensagem);
            return;
        }

        try {
            Message message = Message.creator(
                    new PhoneNumber(destino),
                    new PhoneNumber(remetente),
                    mensagem
            ).create();

            log.info("Mensagem enviada pela Twilio. sid={}", message.getSid());
        } catch (ApiException ex) {
            log.error("Erro ao enviar mensagem pela Twilio", ex);
            throw new ServiceUnavailableException("Erro ao enviar mensagem pela Twilio");
        }
    }

    private void validarConfiguracao() {
        if (!StringUtils.hasText(properties.getAccountSid())
                || !StringUtils.hasText(properties.getAuthToken())
                || !StringUtils.hasText(properties.getFrom())) {
            throw new IllegalStateException("Configure TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN e TWILIO_FROM");
        }
    }

    private String formatarNumero(String numero) {
        if (!StringUtils.hasText(numero)) {
            return numero;
        }

        String numeroLimpo = numero.replaceAll("[^0-9+]", "");
        String numeroE164 = numeroLimpo.startsWith("+") ? numeroLimpo : "+55" + numeroLimpo;

        if (properties.isWhatsapp()) {
            return "whatsapp:" + numeroE164;
        }

        return numeroE164;
    }
}
