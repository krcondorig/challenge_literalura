package com.alura.literalura.principal;

import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.AutorService;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import com.alura.literalura.service.LibroService;
import com.alura.literalura.ui.MenuUI;
import org.springframework.stereotype.Component;

@Component
public class Principal {
    
    private final MenuUI menuUI;
    
    public Principal(LibroRepository libroRepository, 
                     AutorRepository autorRepository,
                     ConsumoAPI consumoAPI,
                     ConvierteDatos convierteDatos) {
        
        // Crea servicios
        LibroService libroService = new LibroService(libroRepository, autorRepository, 
                                                     consumoAPI, convierteDatos);
        AutorService autorService = new AutorService(autorRepository, libroRepository);
        
        // Crea el UI con los servicios
        this.menuUI = new MenuUI(libroService, autorService, consumoAPI, convierteDatos);
    }
    
    public void mostrarMenu() {
        menuUI.mostrarMenu();
    }
}
