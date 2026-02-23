package com.alura.literalura.service;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.repository.AutorRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroService {
    
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final ConsumoAPI consumoAPI;
    private final ConvierteDatos convierteDatos;
    
    private static final String URL_BASE = "https://gutendex.com/books";
    
    public LibroService(LibroRepository libroRepository, AutorRepository autorRepository, 
                        ConsumoAPI consumoAPI, ConvierteDatos convierteDatos) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.consumoAPI = consumoAPI;
        this.convierteDatos = convierteDatos;
    }
    
    public Optional<Libro> buscarYGuardarLibroPorTitulo(String titulo) {
        try {
            if (titulo == null || titulo.trim().isEmpty()) {
                return Optional.empty();
            }
            
            String json = consumoAPI.consumoAPI(URL_BASE + "?search=" + titulo.replace(" ", "+"));
            
            if (json == null || json.trim().isEmpty()) {
                return Optional.empty();
            }
            
            DatosResultado datosBusqueda = convierteDatos.obtenerDatos(json, DatosResultado.class);
            
            if (datosBusqueda == null || datosBusqueda.datosResultados() == null || 
                datosBusqueda.datosResultados().isEmpty()) {
                return Optional.empty();
            }
            
            Optional<DatosLibro> libroBuscado = datosBusqueda.datosResultados().stream()
                    .filter(l -> l.titulo() != null && 
                            l.titulo().toUpperCase().contains(titulo.toUpperCase()))
                    .findFirst();
            
            if (libroBuscado.isEmpty()) {
                return Optional.empty();
            }
            
            DatosLibro datosLibro = libroBuscado.get();
            
            // Verificar si ya existe
            Optional<Libro> libroExistente = libroRepository.findByIdGutendex(datosLibro.idLibro());
            if (libroExistente.isPresent()) {
                return libroExistente;
            }
            
            // Validar que tenga autor
            if (datosLibro.autores() == null || datosLibro.autores().isEmpty()) {
                throw new IllegalArgumentException("El libro no tiene información de autor");
            }
            
            // Crear y guardar el libro
            return Optional.ofNullable(guardarLibro(datosLibro));
            
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar libro: " + e.getMessage(), e);
        }
    }
    
    private Libro guardarLibro(DatosLibro datosLibro) {
        DatosAutor datosAutor = datosLibro.autores().get(0);
        
        // Buscar o crear autor
        Optional<Autor> autorExistente = autorRepository.findByNombre(datosAutor.nombre());
        Autor autor;
        
        if (autorExistente.isPresent()) {
            autor = autorExistente.get();
        } else {
            autor = new Autor(datosAutor);
            autor = autorRepository.save(autor);
        }
        
        // Crear libro
        Libro libro = new Libro(datosLibro);
        libro.setAutor(autor);
        
        return libroRepository.save(libro);
    }
    
    public List<Libro> listarTodosLosLibros() {
        return libroRepository.findAll();
    }
    
    public List<Libro> listarLibrosPorIdioma(String idioma) {
        // Usar consulta cacheada para idiomas frecuentes
        if (isIdiomaFrecuente(idioma)) {
            return libroRepository.findByIdiomaCached(idioma);
        }
        return libroRepository.findByIdioma(idioma);
    }
    
    private boolean isIdiomaFrecuente(String idioma) {
        return "en".equals(idioma) || "es".equals(idioma) || "fr".equals(idioma);
    }
    
    public List<String> obtenerIdiomasDisponibles() {
        return libroRepository.obtenerListaUnicaIdioma();
    }
    
    public List<Libro> obtenerTop10MasDescargados() {
        Pageable pageable = PageRequest.of(0, 10);
        return libroRepository.findTop10ByOrderByNumeroDescargasDesc(pageable);
    }
    
    public EstadisticasLibros obtenerEstadisticas() {
        try {
            List<Libro> libros = libroRepository.findAll();
            
            if (libros.isEmpty()) {
                return new EstadisticasLibros(0L, 0L, 0L, 0.0);
            }
            
            var estadisticas = libros.stream()
                    .filter(l -> l.getNumeroDescargas() != null && l.getNumeroDescargas() > 0)
                    .collect(Collectors.summarizingLong(l -> l.getNumeroDescargas().longValue()));
            
            return new EstadisticasLibros(
                    estadisticas.getCount(),
                    estadisticas.getMax(),
                    estadisticas.getMin(),
                    estadisticas.getAverage()
            );
        } catch (Exception e) {
            return new EstadisticasLibros(0L, 0L, 0L, 0.0);
        }
    }
    
    public Long contarLibrosPorIdioma(String idioma) {
        return libroRepository.countByIdioma(idioma);
    }
    
    public List<Libro> obtenerTop10PorIdioma(String idioma) {
        Pageable pageable = PageRequest.of(0, 10);
        return libroRepository.findTop10ByIdiomaOrderByNumeroDescargasDesc(idioma, pageable);
    }
    
    public record EstadisticasLibros(Long total, Long maxDescargas, Long minDescargas, Double promedio) {}
}
