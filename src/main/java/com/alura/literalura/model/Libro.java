package com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLibro;
    @Column(unique = true)
    private String titulo;
    private String idioma;
    private Long numeroDescargas;
    private Long idGutendex;

    @ManyToOne
    private Autor autor;

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.idioma = (datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty()) 
            ? datosLibro.idiomas().get(0) 
            : "desconocido";
        this.numeroDescargas = datosLibro.descargas();
        this.idGutendex = datosLibro.idLibro();
    }

    public Libro() {

    }

    public Long getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Long idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Long getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Long numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Long getIdGutendex() {
        return idGutendex;
    }

    public void setIdGutendex(Long idGutendex) {
        this.idGutendex = idGutendex;
    }
}