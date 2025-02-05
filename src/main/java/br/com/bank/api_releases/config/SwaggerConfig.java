package br.com.bank.api_releases.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("API de Lançamentos Bancários")
				.version("1.0")
				.description("API RESTful para gerenciar lançamentos de débito e crédito em contas bancárias."));
	}
}
