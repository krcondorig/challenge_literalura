package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    
    Optional<Autor> findByNombre(String nombre);

    @Query("SELECT a FROM Autor a ORDER BY a.nombre ASC")
    List<Autor> findAllByOrderByNombreAsc();

    @Query("SELECT a FROM Autor a WHERE a.anioNacimiento < :anio AND a.anioFallecimiento > :anio ORDER BY a.nombre ASC")
    List<Autor> obtenerAutorVivoAnio(int anio);

    @Query("SELECT a FROM Autor a WHERE a.anioNacimiento BETWEEN :anioInicio AND :anioFin ORDER BY a.anioNacimiento ASC")
    List<Autor> findByAnioNacimientoBetween(int anioInicio, int anioFin);

    @Query("SELECT a FROM Autor a WHERE a.anioNacimiento IS NOT NULL AND a.anioFallecimiento IS NOT NULL ORDER BY (a.anioFallecimiento - a.anioNacimiento) DESC")
    List<Autor> findAutoresMasLongevos();

}
