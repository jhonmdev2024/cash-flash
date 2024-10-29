package principal;

import calculos.Conversor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CashFlash {

    public static void main(String[] args) {

        System.out.println("Inicio de ejecución del programa");

        String bienvenida = "Bienvenido a \'Cash Flash\' la plataforma donde puedes cambiar dinero fácilmente !!!";
        System.out.println(bienvenida);

        Conversor conversor = new Conversor();
        Scanner lecturaDatos = new Scanner(System.in);

        String codigoMonedaOrigen;
        String codigoMonedaDestino;
        double montoMonedaOrigen;
        double montoMonedaDestino;
        double tasaDeCambio;

        while (true) {
            String menu = """
                    Actualmente trabajamos con las siguientes monedas:
                     ARS - Peso argentino
                     BOB - Peso boliviano
                     BRL - Real brasileño
                     CLP - Peso chileno
                     COP - Peso colombiano
                     EUR - Euro (Union Europea)
                     USD - Dólar estadounidense
                     PEN - Sol peruano
                    """;

            System.out.print("Ingresa el código de la moneda origen (Ejem: PEN, ARG, etc): ");
            codigoMonedaOrigen = lecturaDatos.next();

            System.out.print("Ingresa el código de la moneda destino (Ejem: EUR, USD, etc): ");
            codigoMonedaDestino = lecturaDatos.next();

            System.out.print("Ingresa el monto en (" + codigoMonedaOrigen + ") que desea cambiar: ");
            montoMonedaOrigen = lecturaDatos.nextDouble();


            // Preparando la URL que se va utilizar para consultar al API Estandar de Exchange-Rate API
            String apiKey = "7824e76534b98978d975177d";
            String apiStandard = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" +codigoMonedaOrigen;

            try {
                // Realizando la consulta al API Estandar de Exchange-Rate API
                URL url = new URL(apiStandard);
                HttpURLConnection request = (HttpURLConnection) url.openConnection();
                request.connect();

                // Convirtiendo la respuesta a objeto JSON
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                // Tratamiento del objeto JSON
                tasaDeCambio = jsonObject.get("conversion_rates").getAsJsonObject()
                        .get(codigoMonedaDestino).getAsDouble();

                // Obteniendo el monto de la moneda Destino
                montoMonedaDestino = conversor.convertirMoneda(montoMonedaOrigen, tasaDeCambio);

                System.out.println("Monto equivalente en (" + codigoMonedaDestino + ") es: " +  montoMonedaDestino);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("¿Deseas realizar una nueva operación (S/N) ?");
            char rptaUsuario = lecturaDatos.next().charAt(0);

            if(rptaUsuario == 'n' || rptaUsuario == 'N') {
                break;
            }

        }

        System.out.println("Fin de ejecución del programa");

    }

}
