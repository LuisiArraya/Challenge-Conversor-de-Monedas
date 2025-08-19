import com.google.gson.JsonSyntaxException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        try (Scanner lectura = new Scanner(System.in)) { // Manejo automático de recursos
            int opcionElegida = 0;

            ConsultaConversion consulta = new ConsultaConversion();
            Calculos calculos = new Calculos(consulta);
            GeneradorDeArchivos generador = new GeneradorDeArchivos();

            List<String> respuestas = new ArrayList<>();

            while (opcionElegida != 8) {
                try {
                    imprimirMenu();
                    opcionElegida = Integer.parseInt(lectura.nextLine());

                    if (opcionElegida < 1 || opcionElegida > 8) {
                        System.out.println("Ingrese una opción válida");
                        continue;
                    }

                    String formattedDate = obtenerFechaActual();

                    if (opcionElegida >= 1 && opcionElegida <= 6) {
                        String[] monedas = obtenerMonedas(opcionElegida);
                        calculos.almacenarValores(monedas[0], monedas[1]);
                    } else if (opcionElegida == 7) {
                        calculos.almacenarValoresPersonalizados();
                    }

                    if (opcionElegida != 8) {
                        respuestas.add(formattedDate + " - " + calculos.obtenerMensajeRespuesta());
                    }
                } catch (JsonSyntaxException | NullPointerException e) {
                    System.out.println("Error. Ingrese solo códigos de moneda válidos.");
                } catch (NumberFormatException | InputMismatchException e) {
                    System.out.println("Error. Ingrese un valor numérico válido.");
                }
            }

            try {
                generador.guardarJson(respuestas);
            } catch (Exception e) {
                System.out.println("Error al guardar el archivo JSON: " + e.getMessage());
            }

            System.out.println("Finalizando programa");
        }
    }

    private static void imprimirMenu() {
        System.out.println("""
                \n***************************************************
                *** Sea bienvenido al Conversor de Monedas ***

                1) Peso Argentino => Dólar Estadounidense
                2) Peso Argentino => Euro
                3) Peso Argentino => Peso Chileno
                4) Dólar Estadounidense => Peso Argentino
                5) Euro => Peso Argentino
                6) Peso Chileno => Peso Argentino

                7) Otra opción de conversión

                8) Salir
                ***************************************************
                """);
    }

    private static String obtenerFechaActual() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return myDateObj.format(myFormatObj);
    }

    private static String[] obtenerMonedas(int opcion) {
        return switch (opcion) {
            case 1 -> new String[] { "ARS", "USD" };
            case 2 -> new String[] { "ARS", "EUR" };
            case 3 -> new String[] { "ARS", "CLP" };
            case 4 -> new String[] { "USD", "ARS" };
            case 5 -> new String[] { "EUR", "ARS" };
            case 6 -> new String[] { "CLP", "ARS" };
            default -> throw new IllegalArgumentException("Opción inválida");
        };
    }
}