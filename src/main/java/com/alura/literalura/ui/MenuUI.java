package com.alura.literalura.ui;

import com.alura.literalura.model.*;
import com.alura.literalura.service.AutorService;
import com.alura.literalura.service.LibroService;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuUI {
    
    private final Scanner teclado = new Scanner(System.in);
    private final LibroService libroService;
    private final AutorService autorService;
    private final ConsumoAPI consumoAPI;
    private final ConvierteDatos convierteDatos;
    
    private static final String URL_CODIGOS_IDIOMAS = "https://krcondorig.github.io/lenguajes-iso639-1-espaniol-json/lenguajes-iso636-1.json";
    
    public MenuUI(LibroService libroService, AutorService autorService, 
                  ConsumoAPI consumoAPI, ConvierteDatos convierteDatos) {
        this.libroService = libroService;
        this.autorService = autorService;
        this.consumoAPI = consumoAPI;
        this.convierteDatos = convierteDatos;
    }
    
    public void mostrarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            mostrarMenuPrincipal();
            
            try {
                System.out.print("Elija una opcion: ");
                opcion = teclado.nextInt();
                teclado.nextLine();
                
                procesarOpcion(opcion);
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número válido");
                teclado.nextLine();
            }
        }
    }
    
    private void mostrarMenuPrincipal() {
        var menu = """
                -------------------------------
                        Menu Principal
                -------------------------------
                 1 - Buscar Libro por titulo
                 2 - Listar libros registrados
                 3 - Listar autores registrados
                 4 - Listar autores vivos por X año
                 5 - Listar libros por idioma
                 6 - Algunas estadisticas
                 7 - Top 10 libros mas descargados
                 8 - Buscar autor por nombre
                 9 - Autores por rango de nacimiento
                 10 - Autores mas longevos
                
                 0 - Salir
                """;
        System.out.println(menu);
    }
    
    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                buscarLibro();
                break;
            case 2:
                listarLibros();
                break;
            case 3:
                listarAutoresRegistrados();
                break;
            case 4:
                listarAutoresVivosPorAnio();
                break;
            case 5:
                listarLibrosPorIdioma();
                break;
            case 6:
                mostrarEstadisticas();
                break;
            case 7:
                listarTop10LibrosMasDescargados();
                break;
            case 8:
                buscarAutorPorNombre();
                break;
            case 9:
                autoresPorRangoNacimiento();
                break;
            case 10:
                autoresMasLongevos();
                break;
            case 0:
                System.out.println("Cerrando la aplicacion");
                break;
            default:
                System.out.println("Opcion invalida");
                break;
        }
    }
    
    private void buscarLibro() {
        try {
            System.out.println("Ingresar nombre del libro: ");
            String nombreLibro = teclado.nextLine();
            
            var libro = libroService.buscarYGuardarLibroPorTitulo(nombreLibro);
            
            if (libro.isPresent()) {
                mostrarDatosLibro(libro.get());
                System.out.println("\nLibro ya registrado en la base de datos");
            } else {
                System.out.println("Libro no encontrado");
            }
        } catch (Exception e) {
            System.out.println("Error al buscar el libro: " + e.getMessage());
        }
    }
    
    private void listarLibros() {
        List<Libro> libros = libroService.listarTodosLosLibros();
        
        System.out.println("\n****************************************************");
        System.out.println("            Lista de libros en Literalura");
        System.out.println("****************************************************\n");
        
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados");
        } else {
            mostrarListaLibros(libros);
            System.out.println("Total de libros registrados: " + libros.size());
        }
    }
    
    private void listarAutoresRegistrados() {
        List<Autor> autores = autorService.listarTodosLosAutores();
        
        System.out.println("\n**************************************");
        System.out.println("    Lista de autores en Literalura");
        System.out.println("**************************************\n");
        
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados");
        } else {
            mostrarListaAutores(autores);
            System.out.println("Total de autores registrados: " + autores.size());
        }
    }
    
    private void listarAutoresVivosPorAnio() {
        try {
            System.out.println("Ingresa el año: ");
            int anio = teclado.nextInt();
            teclado.nextLine();
            
            List<Autor> autores = autorService.listarAutoresVivosEnAnio(anio);
            
            System.out.printf("\n*************************************************************\n");
            System.out.printf("    Autores vivos para el año %d en Literalura\n", anio);
            System.out.printf("*************************************************************\n\n");
            
            if (autores.isEmpty()) {
                System.out.println("No hay autores vivos para el año registrado");
            } else {
                mostrarListaAutores(autores);
                System.out.println("Total de autores registrados: " + autores.size());
            }
        } catch (InputMismatchException e) {
            System.out.println("El año ingresado no es válido");
            teclado.nextLine();
        }
    }
    
    private void listarLibrosPorIdioma() {
        System.out.println("\n**************************************");
        System.out.println("    Lista de idiomas disponibles");
        System.out.println("**************************************\n");
        
        List<String> idiomasLibro = libroService.obtenerIdiomasDisponibles();
        
        if (idiomasLibro.isEmpty()) {
            System.out.println("No hay libros registrados");
            return;
        }
        
        try {
            var idiomasJson = consumoAPI.consumoAPI(URL_CODIGOS_IDIOMAS);
            var datosIdiomas = convierteDatos.obtenerDatos(idiomasJson, DatosIdioma.class);
            
            System.out.println("Idiomas disponibles:");
            for (String codigoIdioma : idiomasLibro) {
                var idiomaEncontrado = datosIdiomas.idiomas().stream()
                        .filter(i -> i.codigoIdioma().contains(codigoIdioma))
                        .findFirst();
                
                idiomaEncontrado.ifPresent(idioma -> 
                    System.out.println(idioma.codigoIdioma() + " - " + idioma.idioma()));
            }
            
            System.out.println("\nIngrese el código del idioma del libro a buscar: ");
            String inputCodigoIdioma = teclado.nextLine();
            
            if (inputCodigoIdioma.trim().isEmpty()) {
                System.out.println("Debe ingresar un código de idioma");
                return;
            }
            
            List<Libro> libros = libroService.listarLibrosPorIdioma(inputCodigoIdioma);
            
            if (libros.isEmpty()) {
                System.out.println("No hay libros con ese idioma registrado");
            } else {
                mostrarListaLibros(libros);
                System.out.println("Total de libros registrados: " + libros.size());
            }
            
        } catch (Exception e) {
            System.out.println("Error al procesar idiomas: " + e.getMessage());
        }
    }
    
    private void mostrarEstadisticas() {
        var estadisticas = libroService.obtenerEstadisticas();
        
        var muestraEstadisticas = """
                ***************************************
                    Datos estadisticos de Literalura
                ***************************************
                
                Total de libros: %d
                Libro mas descargado: %d
                Libro menos descargado: %d
                Promedio de descargas: %.2f
                
                """;
        
        System.out.printf(muestraEstadisticas,
                estadisticas.total(),
                estadisticas.maxDescargas(),
                estadisticas.minDescargas(),
                estadisticas.promedio());
    }
    
    private void listarTop10LibrosMasDescargados() {
        List<Libro> libros = libroService.obtenerTop10MasDescargados();
        
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados");
        } else {
            System.out.println("\n********************************************************");
            System.out.println("                Top 10 libros más descargados");
            System.out.println("********************************************************\n");
            mostrarListaLibros(libros);
            System.out.println("Total de libros registrados: " + libros.size());
        }
    }
    
    private void buscarAutorPorNombre() {
        System.out.println("Ingrese el nombre del autor: ");
        String nombreAutor = teclado.nextLine();
        
        var autor = autorService.buscarAutorPorNombre(nombreAutor);
        
        if (autor.isPresent()) {
            mostrarDatosAutor(autor.get());
            List<Libro> libros = autorService.obtenerLibrosPorAutor(autor.get().getIdAutor());
            System.out.println("Libros registrados del autor: " + libros.size());
            mostrarListaLibros(libros);
        } else {
            System.out.println("No se encontró el autor");
        }
    }
    
    private void autoresPorRangoNacimiento() {
        try {
            System.out.println("Ingrese el año de inicio: ");
            int anioInicio = teclado.nextInt();
            System.out.println("Ingrese el año de fin: ");
            int anioFin = teclado.nextInt();
            teclado.nextLine();
            
            List<Autor> autores = autorService.listarAutoresPorRangoNacimiento(anioInicio, anioFin);
            
            System.out.printf("\n*************************************************************\n");
            System.out.printf("    Autores nacidos entre %d y %d en Literalura\n", anioInicio, anioFin);
            System.out.printf("*************************************************************\n\n");
            
            if (autores.isEmpty()) {
                System.out.println("No hay autores registrados en ese rango de años");
            } else {
                mostrarListaAutores(autores);
                System.out.println("Total de autores encontrados: " + autores.size());
            }
        } catch (InputMismatchException e) {
            System.out.println("Debe ingresar años válidos (números enteros)");
            teclado.nextLine();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void autoresMasLongevos() {
        List<Autor> autores = autorService.listarAutoresMasLongevos();
        
        System.out.println("\n*************************************************************");
        System.out.println("            Autores más longevos en Literalura");
        System.out.println("*************************************************************\n");
        
        if (autores.isEmpty()) {
            System.out.println("No hay autores con datos completos de nacimiento y fallecimiento");
        } else {
            autores.forEach(a -> {
                int edad = a.getAnioFallecimiento() - a.getAnioNacimiento();
                System.out.printf("----------------------------------------\n");
                System.out.printf("Nombre: %s\n", a.getNombre());
                System.out.printf("Año de nacimiento: %d\n", a.getAnioNacimiento());
                System.out.printf("Año de fallecimiento: %d\n", a.getAnioFallecimiento());
                System.out.printf("Edad: %d años\n\n", edad);
            });
            System.out.println("Total de autores con edad calculada: " + autores.size());
        }
    }
    
    // Métodos de utilidad para mostrar datos
    private void mostrarDatosLibro(Libro libro) {
        var muestraLibro = """
                **************************************
                            Datos del Libro
                **************************************
                
                Titulo: %s
                Autor: %s
                Idiomas: %s
                Descargas: %s
                
                """;
        System.out.printf(muestraLibro,
                libro.getTitulo(),
                libro.getAutor() != null ? libro.getAutor().getNombre() : "Sin autor",
                libro.getIdioma(),
                libro.getNumeroDescargas());
    }
    
    private void mostrarDatosAutor(Autor autor) {
        var muestraAutor = """
                **************************************
                            Datos del autor
                **************************************
                
                Nombre: %s
                Año de nacimiento: %s
                Año de fallecimiento: %s
                """;
        System.out.printf(muestraAutor + "%n",
                autor.getNombre(),
                autor.getAnioNacimiento() == null ? "Sin datos" : autor.getAnioNacimiento(),
                autor.getAnioFallecimiento() == null ? "Sin datos" : autor.getAnioFallecimiento());
    }
    
    private void mostrarListaLibros(List<Libro> libros) {
        libros.forEach(this::mostrarDatosLibro);
    }
    
    private void mostrarListaAutores(List<Autor> autores) {
        autores.forEach(autor -> {
            mostrarDatosAutor(autor);
            List<Libro> librosAutor = autorService.obtenerLibrosPorAutor(autor.getIdAutor());
            System.out.println("Libros: " + librosAutor.stream()
                    .map(Libro::getTitulo)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Sin libros"));
            System.out.println();
        });
    }
}
