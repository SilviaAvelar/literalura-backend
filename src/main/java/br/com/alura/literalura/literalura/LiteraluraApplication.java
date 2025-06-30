package br.com.alura.literalura.literalura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication {

	public static void main(String[] args) {
		System.out.println("DB URL: " + System.getenv("SPRING_DATASOURCE_URL"));
		SpringApplication.run(LiteraluraApplication.class, args);
	}

}