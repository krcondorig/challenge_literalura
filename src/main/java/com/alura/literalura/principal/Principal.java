package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class Principal {
    private final Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private List<DatosLibro> datosLibros = new ArrayList<>();
    private String nombreLibro;
    private LibroRepository repositorioLibro;
    private AutorRepository repositorioAutor;
    private List<Libro> libros;
    private List<Autor> autores;
    private final String URL_CODIGOS_IDIOMAS = "https://krcondorig.github.io/lenguajes-iso639-1-espaniol-json/lenguajes-iso636-1.json";



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
                     6 - Algunas estadisticas
                     7 - Top 10 libros mas descargados
                     8 - Buscar autor por nombre
                     9 - Listar autores por X idioma
                    
                     0 - Salir
                    """;
            System.out.println(menu);
            System.out.print("Elija una opcion: ");
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
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
                    System.out.println("caso 9");;
                    break;
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
        //System.out.println(json);
        var datos = convierteDatos.obtenerDatos(json, DatosResultado.class);
        //System.out.println(datos);
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
            //System.out.println(datosLibro);
            DatosAutor datosAutor = libroBuscado.get().autores().get(0);
            System.out.println("------------" + libroBuscado.get().idLibro());

            //validar si el libro ya esta en la base de datos
            if(repositorioLibro.findByIdGutendex(libroBuscado.get().idLibro()).isPresent()){
                System.out.println("Libro ya registrado en la base de datos");
                System.out.println("------------" + libroBuscado.get().idLibro());
            } else {
                var libroEncontrado = """
                        **************************************
                                    Datos del Libro
                        **************************************
                        
                        Titulo: %s
                        Autor: %s
                        Idiomas: %s
                        Descargas: %s
                        """;
                System.out.printf(libroEncontrado + "%n",
                        libroBuscado.get().titulo(),
                        libroBuscado.get().autores().get(0).nombre(),
                        libroBuscado.get().idiomas(),
                        libroBuscado.get().descargas());

                Optional<Autor> autor = repositorioAutor.findByNombre(datosAutor.nombre());
                if(autor.isPresent()){
                    System.out.println("Autor encontrado");
                    Libro libro = new Libro(libroBuscado.get());
                    libro.setAutor(autor.get());
                    repositorioLibro.save(libro); // Guardar el libro en la BD

                } else {
                    System.out.println("Autor no encontrado");
                    libros = libroBuscado.stream()
                            .map(Libro::new)
                            .collect(Collectors.toList());
                    Autor autorClase = new Autor(datosAutor);
                    autorClase.setLibros(libros);
                    repositorioAutor.save(autorClase);
                }
                System.out.println("Libro registradp exitosamente");

            }

            /*
            //crear autor
            DatosAutor datosAutor = datosLibro.autores().get(0);
            Autor autor = new Autor(datosAutor);
            repositorioAutor.save(autor);

            //crear y guardar libro
            Libro libro = new Libro(datosLibro);
            libro.setAutor(autor);
            repositorioLibro.save(libro);

            System.out.println("Libro guardado en la base de datos");*/
        }else {
            System.out.println("Libro no encontrado");
        }
    }

    private void datosAutor(List<Autor> listaAutor){
        var muestraAutor = """
                **************************************
                            Datos del autor
                **************************************
                
                Nombre: %s
                Año de nacimiento: %s
                Año de fallecimiento: %s
                """;
        listaAutor.forEach(a -> System.out.printf(
                (muestraAutor) + "%n", a.getNombre(),
                (a.getAnioNacimiento() == null) ? "Sin datos" : a.getAnioNacimiento(),
                (a.getAnioFallecimiento() == null) ? "Sin datos" : a.getAnioFallecimiento(),
                repositorioLibro.obtenerLibrosPorAutor(a.getIdAutor()).stream()
                        .map(Libro::getTitulo)
                        .collect(Collectors.joining(", "))
        ));
    }

    private void datosLibro(List<Libro> listaLibro) {
        var muestraLibro = """
                **************************************
                            Datos del Libro
                **************************************
                
                Titulo: %s
                Autor: %s
                Idiomas: %s
                Descargas: %s
                
                """;
        listaLibro.forEach(l -> System.out.println(
                muestraLibro.formatted(
                        l.getTitulo(),
                        l.getAutor().getNombre(),
                        l.getIdioma(),
                        l.getNumeroDescargas()
                )
        ));
    }

    private void listarLibros() {
        libros = repositorioLibro.findAll();
        var muestraListaLibros = """
                ****************************************************
                            Lista de libros en Literalura
                ****************************************************       
                """;
        System.out.print("\n" + muestraListaLibros + "\n");
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados");
        } else {
            var cuentaLibros = libros.size();
            datosLibro(libros);
            System.out.println("Total de libros registrados: " + cuentaLibros);
        }
    }

    private void listarAutoresRegistrados() {
        var muestraListaAutores = """
                **************************************
                    Lista de autores en Literalura
                **************************************
                """;
        System.out.print("\n" + muestraListaAutores + "\n");
        autores = repositorioAutor.findAllByOrderByNombreAsc();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados");
        } else {
            var cuentaAutores = autores.size();
            datosAutor(autores);
            System.out.println("Total de autores registrados: " + cuentaAutores);
        }
    }

    private void listarAutoresVivosPorAnio(){
        try {
            System.out.println("Ingresa el año");
            var anio = teclado.nextInt();
            teclado.nextLine();
            autores = repositorioAutor.obtenerAutorVivoAnio(anio);
            var muestraAnioAutor = """
                *************************************************************
                    Lista de autores vivos para el año %s en Literalura
                *************************************************************
                """;
            System.out.printf((muestraAnioAutor) + "%n", anio);
            //System.out.println("\n" + muestraAnioAutor + anio + "\n");
            if (autores.isEmpty()) {
                System.out.println("No hay autores vivos para el año registrado");
            } else {
                datosAutor(autores);
                System.out.println("Total de autores registrados: " + autores.size());
            }
        }catch (InputMismatchException e){
            System.out.println("El año ingresado no es valido");
            teclado.nextLine();
        }
    }

    private void listarLibrosPorIdioma() {
        var muestraIdioma = """
                **************************************
                    Lista de idiomas disponibles
                **************************************
                """;
        var idiomasLibro = repositorioLibro.obtenerListaUnicaIdioma();
        var idiomasJson = consumoAPI.consumoAPI(URL_CODIGOS_IDIOMAS);
        var datosIdiomas = convierteDatos.obtenerDatos(idiomasJson, DatosIdioma.class);
        //System.out.println(datosIdiomas);
        List<Idioma> idiomaDisponibles = new ArrayList<>();
        if (idiomasLibro.isEmpty()) {
            System.out.println("No hay libros con ese idioma registrado");
        } else {
            for (String codigoIdioma : idiomasLibro) {
                var idiomaEncontrado = datosIdiomas.idiomas().stream()
                        .filter(i -> i.codigoIdioma().contains(codigoIdioma))
                        .collect(Collectors.toList());
                idiomaDisponibles.add(idiomaEncontrado.get(0));
            }
            System.out.println(muestraIdioma);
            idiomaDisponibles.forEach(i -> System.out.println(i.codigoIdioma()
                    + " - " + i.idioma()));
            System.out.println("Ingrese el codigo del idioma del libro a buscar");
            String inputCodigoIdioma = teclado.nextLine();
            if (inputCodigoIdioma.isEmpty()) {
                System.out.println("Ingrese un codigo de idioma");
            } else {
                libros = repositorioLibro.findByIdioma(inputCodigoIdioma);
                if (libros.isEmpty()) {
                    System.out.println("No hay libros con ese idioma registrado");
                } else {
                    var cuentaLibros = libros.size();
                    datosLibro(libros);
                    System.out.println("Total de libros registrados: " + cuentaLibros);
                }

            }
        }
    }

    private void mostrarEstadisticas() {
        libros = repositorioLibro.findAll();

        var muestraEstadisticas = """
                ***************************************
                    Datos estadisticos de Literalura
                ***************************************
                
                Total de librps: %s
                Libro mas descargado: %s
                Libro menos descargado: %s
                Promedio de descargas: %s
                
                """;

        LongSummaryStatistics estadisticas = libros.stream()
                .filter(l -> l.getNumeroDescargas() > 0)
                .collect(Collectors.summarizingLong(l -> l.getNumeroDescargas().longValue()));

        System.out.println(muestraEstadisticas.formatted(
                estadisticas.getCount(),
                estadisticas.getMax(),
                estadisticas.getMin(),
                Math.round(estadisticas.getAverage())
        ));
    }

    private void listarTop10LibrosMasDescargados() {
        libros = repositorioLibro.findTop10ByOrderByNumeroDescargasDesc();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados");
        } else {
            var muestraTop10Libros = """
                            ********************************************************
                                        Top 10 libros más descargados
                            ********************************************************
                            
                            """;
            System.out.print(muestraTop10Libros);
            var cuentaLibros = libros.size();
            datosLibro(libros);
            System.out.println("Total de libros registrados: " + cuentaLibros);
        }
    }

    private void buscarAutorPorNombre(){
        System.out.println("Ingrese el nombre del autor: ");
        String nombreAutor = teclado.nextLine();

        Optional<Autor> autor = repositorioAutor.findByNombre(nombreAutor);
        
        // Si no encuentra con búsqueda exacta, intentar búsqueda parcial
        if (autor.isEmpty()) {
            List<Autor> autores = repositorioAutor.findAll();
            autor = autores.stream()
                .filter(a -> a.getNombre().toLowerCase().contains(nombreAutor.toLowerCase()))
                .findFirst();
        }
        
        if (autor.isPresent()){
            Autor autorEncontrado = autor.get();

            var muestraAutor = """
                    **************************************
                                  Datos del autor
                    **************************************
                    
                    Nombre: %s
                    Año de nacimiento: %s
                    Año de fallecimiento: %s
                    """;
            System.out.printf(muestraAutor + "%n",
                autorEncontrado.getNombre(),
                autorEncontrado.getAnioNacimiento() == null ? "Sin datos" : autorEncontrado.getAnioNacimiento(),
                autorEncontrado.getAnioFallecimiento() == null ? "Sin datos" : autorEncontrado.getAnioFallecimiento());
            libros = repositorioLibro.obtenerLibrosPorAutor(autorEncontrado.getIdAutor());
            System.out.println("Libros registrados del autor: " + libros.size());
            datosLibro(libros);
        } else {
            System.out.println("No se encontro el autor");
        }

    }

}
