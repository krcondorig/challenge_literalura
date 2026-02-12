package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class Principal {
    private final Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private List<DatosLibro> datosLibros = new ArrayList<>();
    private String nombreLibro;
    private LibroRepository repositorioLibro;
    private AutorRepository repositorioAutor;

    public Principal(LibroRepository repositorioLibro, AutorRepository repositorioAutor){
        this.repositorioLibro = repositorioLibro;
        this.repositorioAutor = repositorioAutor;
    }

    public void mostrarMenu(){
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    -------------------------------
                            Menu Principal
                    -------------------------------
                     1 - Buscar Libro por titulo
                     2 - Listar libros registrados
                     3 - Listar autores registrados
                     4 - Listar autores vivos por X año
                     5 - Listar libros por idioma
                     6 - Top 10 libros con más descargas
                     7 - Algunas estadísticas
                    
                     0 - Salir
                    """;
            System.out.println(menu);
            System.out.print("Elija una opcion: ");
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    System.out.println("caso 1 ingresando");
                    buscarLibro();
                case 2:
                    System.out.println("caso 2 ingresado");
                    //mostrarLibrosBuscados();

                case 0:
                    System.out.println("Cerrando la aplicacion");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        }

    }

    private DatosResultado getDatosResultados(){
        System.out.println("Ingresar nombre del libro: ");
        nombreLibro = teclado.nextLine();
        var json = consumoAPI.consumoAPI(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        System.out.println(json);
        var datos = convierteDatos.obtenerDatos(json, DatosResultado.class);
        System.out.println(datos);
        return datos;
    }

    private void buscarLibro(){
        var datosBusqueda = getDatosResultados();
        Optional<DatosLibro> libroBuscado = datosBusqueda.datosResultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst();
        if(libroBuscado.isPresent()){
            DatosLibro datosLibro = libroBuscado.get();
            System.out.println("Libro encontrado ");
            System.out.println(datosLibro);

            //crear autor
            DatosAutor datosAutor = datosLibro.autores().get(0);
            Autor autor = new Autor(datosAutor);
            repositorioAutor.save(autor);

            //crear y guardar libro
            Libro libro = new Libro(datosLibro);
            libro.setAutor(autor);
            repositorioLibro.save(libro);

            System.out.println("Libro guardado en la base de datos");
        }else {
            System.out.println("Libro no encontrado");
        }
    }
    private void mostrarLibrosBuscados(){
        datosLibros.forEach(System.out::println);
    }
}
