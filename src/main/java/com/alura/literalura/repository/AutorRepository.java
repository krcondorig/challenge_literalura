package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombre(String nombre);

    List<Autor> findAllByOrderByNombreAsc();

    @Query("SELECT a FROM Autor a WHERE a.anioNacimiento < :anio AND a.anioFallecimiento > :anio ORDER BY a.nombre ASC")
    List<Autor> obtenerAutorVivoAnio(int anio);
}
