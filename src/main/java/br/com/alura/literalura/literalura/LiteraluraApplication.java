package br.com.alura.literalura.literalura;

import br.com.alura.literalura.literalura.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Optional; // <-- IMPORTE O OPTIONAL

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private Optional<Principal> principal;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.ifPresent(Principal::exibirMenu);
	}
}