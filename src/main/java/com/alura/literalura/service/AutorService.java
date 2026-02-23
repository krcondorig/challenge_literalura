package com.alura.literalura.service;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {
    
    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;
    
    public AutorService(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }
    
    public List<Autor> listarTodosLosAutores() {
        return autorRepository.findAllByOrderByNombreAsc();
    }
    
    public List<Autor> listarAutoresVivosEnAnio(int anio) {
        return autorRepository.obtenerAutorVivoAnio(anio);
    }
    
    public Optional<Autor> buscarAutorPorNombre(String nombre) {
        // Búsqueda exacta primero
        Optional<Autor> autor = autorRepository.findByNombre(nombre);
        
        if (autor.isPresent()) {
            return autor;
        }
        
        // Búsqueda parcial si no encuentra exacto
        return autorRepository.findAll().stream()
                .filter(a -> a.getNombre() != null && 
                        a.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .findFirst();
    }
    
    public List<Autor> listarAutoresPorRangoNacimiento(int anioInicio, int anioFin) {
        if (anioInicio > anioFin) {
            throw new IllegalArgumentException("El año de inicio no puede ser mayor que el año de fin");
        }
        return autorRepository.findByAnioNacimientoBetween(anioInicio, anioFin);
    }
    
    public List<Autor> listarAutoresMasLongevos() {
        return autorRepository.findAutoresMasLongevos();
    }
    
    public List<Libro> obtenerLibrosPorAutor(Long idAutor) {
        return libroRepository.obtenerLibrosPorAutor(idAutor);
    }
    
}
