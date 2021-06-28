package br.com.matheushramos.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	
	@BeforeClass
	public static void init() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend";
	}
	
	@Test
	public void deveRetornarTarefas() {
		RestAssured.given()
		.when()
			.get("/todo")
		.then()
			.statusCode(200);
	}
	
	@Test
	public void deveAdicionarTarefaComSucesso() {
		RestAssured.given()
			.body("{\n"
					+ "	\"task\": \"TaskAPITest\",\n"
					+ "	\"dueDate\": \"2030-06-23\"\n"
					+ "}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201);
	}
	
	@Test
	public void naoDeveAdicionarTarefaInvalida() {
		RestAssured.given()
			.body("{\n"
					+ "	\"task\": \"TaskAPITestInvalida\",\n"
					+ "	\"dueDate\": \"2010-06-23\"\n"
					+ "}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(400)
			.body("message", CoreMatchers.is("Due date must not be in past"));
	}
	
	@Test
	public void deveRemoverTarefaComSucesso() {
		// inserir
		Integer id = RestAssured.given()
			.body("{\n"
					+ "	\"task\": \"Tarefa para remoção\",\n"
					+ "	\"dueDate\": \"2010-06-28\"\n"
					+ "}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
//			.log().all()
			.statusCode(201)
			.extract().path("id");
			
		// remover
		RestAssured.given()
			.when()
				.delete("/todo/" + id)
			.then()
				.statusCode(204);
	}

}