package br.com.adriano.integrationtests.testcontainers;

import java.util.Map;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

@ContextConfiguration(initializers = { AbstractIntegrationTest.Initializer.class })
public class AbstractIntegrationTest {

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("pgvector/pgvector:pg16");
		
		private static void startContainers() {
			postgresContainer.start();
		}
		
		private static Map<String, String> createConnectionConfiguration() {
			return Map.of(
					"spring.datasource.url", postgresContainer.getJdbcUrl(),
					"spring.datasource.username", postgresContainer.getUsername(),
					"spring.datasource.password", postgresContainer.getPassword()
			);
		}
		
		@Override
		@SuppressWarnings({"unchecked", "rawtypes"})
		public void initialize(ConfigurableApplicationContext applicationContext) {
			startContainers();
			ConfigurableEnvironment environment = applicationContext.getEnvironment();
			MapPropertySource testContainer = new MapPropertySource("testcontainers", (Map)createConnectionConfiguration());
			environment.getPropertySources().addFirst(testContainer);
		}
	}
}
