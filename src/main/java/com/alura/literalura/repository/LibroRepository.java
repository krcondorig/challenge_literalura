package com.alura.literalura.repository;

import com.alura.literalura.model.Libro;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByIdGutendex(Long idGutendex);

    @Query("SELECT DISTINCT l FROM Autor a JOIN a.libros l WHERE a.idAutor = :id")
    List<Libro> obtenerLibrosPorAutor(Long id);

    @Query("SELECT DISTINCT l.idioma from Libro l ORDER BY l.idioma")
    List<String> obtenerListaUnicaIdioma();

    List<Libro> findByIdioma(String idioma);

    @Query("SELECT l FROM Libro l ORDER BY l.numeroDescargas DESC")
    List<Libro> findTop10ByOrderByNumeroDescargasDesc(Pageable pageable);

    @Query("SELECT l FROM Libro l WHERE l.idioma = :idioma ORDER BY l.numeroDescargas DESC")
    List<Libro> findTop10ByIdiomaOrderByNumeroDescargasDesc(String idioma, Pageable pageable);

    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("SELECT l FROM Libro l WHERE l.idioma = :idioma")
    List<Libro> findByIdiomaCached(String idioma);

    @Query("SELECT COUNT(l) FROM Libro l WHERE l.idioma = :idioma")
    Long countByIdioma(String idioma);

    @Query("SELECT l FROM Libro l WHERE l.numeroDescargas > 0 ORDER BY l.numeroDescargas DESC")
    List<Libro> findLibrosConDescargas();
}
