package com.alura.literalura;

import model.DatosLibro;
import model.DatosResultado;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import principal.Principal;
import service.ConsumoAPI;
import service.ConvierteDatos;

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
        System.out.println("consumo inicial: " + json);
        System.out.println("entro");

        ConvierteDatos conversor = new ConvierteDatos();
        var datos = conversor.obtenerDatos(json, DatosResultado.class);
        System.out.println(datos);
    }
}
