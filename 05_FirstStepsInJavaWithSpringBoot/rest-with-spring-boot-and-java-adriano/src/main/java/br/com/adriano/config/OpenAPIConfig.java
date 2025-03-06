package br.com.adriano.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {
	// Configuração do OpenAPI
	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Hello Swagger API")
						.version("v1")
						.description("API de Exemplo para demonstração de uso do Swagger")
						.termsOfService("http://swagger.io/terms/")
						.license(
								new License()
								.name("Apache 2.0")
								.url("http://springdoc.org"))
				);
	}
}
