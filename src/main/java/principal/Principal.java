package principal;

import java.util.Scanner;


public class Principal {
    private final Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/";

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
                     6 - Top 10 libros con más descargas
                     7 - Algunas estadísticas
                    
                     0 - Salir
                    """;
            System.out.println(menu);
            System.out.print("Elija una opcion: ");
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    System.out.println("caso 1 ingresando");
                case 0:
                    System.out.println("Cerrando la aplicacion");
                default:
                    System.out.println("Opcion invalida");
            }
        }



    }
}
