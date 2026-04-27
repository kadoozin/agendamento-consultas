package com.example.agendamento_consultas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AgendamentoConsultasApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendamentoConsultasApplication.class, args);
	}

}
