package com.example.agendamento_consultas.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "twilio")
public class TwilioProperties {
    private boolean enabled;
    private String accountSid;
    private String authToken;
    private String from;
    private String channel = "whatsapp";

    public boolean isWhatsapp() {
        return "whatsapp".equalsIgnoreCase(channel);
    }
}
