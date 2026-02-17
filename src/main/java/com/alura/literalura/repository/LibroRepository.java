package com.alura.literalura.repository;

import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    //Optional<Libro> findByIdLibro(Long idLibro);
    Optional<Libro> findByIdGutendex(Long idGutendex);

    @Query("SELECT l FROM Autor a JOIN a.libros l WHERE a.idAutor = :id")
    List<Libro> obtenerLibrosPorAutor(Long id);

    @Query("SELECT DISTINCT l.idioma from Libro l ORDER BY l.idioma")
    List<String> obtenerListaUnicaIdioma();

    List<Libro> findByIdioma(String idioma);

    List<Libro> findTop10ByOrderByNumeroDescargasDesc();
}
