package br.com.adriano.integrationtests.swagger;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.adriano.config.TestConfig;
import br.com.adriano.integrationtests.testcontainers.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

	@Test
	@DisplayName("JUnit test for should display Swagger UI page")
	void testShouldDisplaySwaggerUiPage() {
		var content = given()
			.basePath("/swagger-ui/index.html")
			.port(TestConfig.SERVER_PORT)
			.when()
				.get()
			.then()
				.statusCode(200)
			.extract()
				.body()
					.asString();
		
		assertTrue(content.contains("Swagger UI"));
	}

}
