package com.alura.literalura;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import principal.Principal;
import service.ConsumoAPI;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        //Principal principal = new Principal();
        //principal.mostrarMenu();
        ConsumoAPI consumoAPI = new ConsumoAPI();
        String json = consumoAPI.consumoAPI("https://gutendex.com/books");
        System.out.println(json);
    }
}
