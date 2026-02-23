package com.alura.literalura;

import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import com.alura.literalura.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    @Autowired
    private LibroRepository repositoryLibro;
    @Autowired
    private AutorRepository repositoryAutor;
    @Autowired
    private ConsumoAPI consumoAPI;
    @Autowired
    private ConvierteDatos convierteDatos;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(repositoryLibro, repositoryAutor, consumoAPI, convierteDatos);
        principal.mostrarMenu();
    }
}
