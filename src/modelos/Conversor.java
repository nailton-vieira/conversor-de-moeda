package modelos;

import excecao.ErroDeDisponibilidadeException;
import java.io.IOException;
import java.net.URI;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class Conversor {

  private static final String api_key = "50135c357236c482ea5c9788"; // edite aqui com sua chave da API.

  private static final String url = "https://v6.exchangerate-api.com/v6/" + api_key + "/latest/USD";

  @SuppressWarnings("unchecked")
public static Map<String, Double> obterCotacao() throws IOException, InterruptedException {

            
            HttpClient client = HttpClient.newHttpClient();

            
            HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create(url))
             .build();

            
            HttpResponse<String> response = client
             .send(request, HttpResponse.BodyHandlers.ofString());


            String json = response.body();

            
            Gson gson = new Gson();

            
            JsonObject conversionRates  = gson.fromJson(json, JsonObject.class);

            JsonObject jsonObject = conversionRates.getAsJsonObject("conversion_rates");

            return gson.fromJson(jsonObject, Map.class);
  }

  public static Double paresMoedas(double valor, Moeda moeda) throws IOException, InterruptedException {
            Map<String, Double> conversaoDeCotacao = obterCotacao();

            if (!conversaoDeCotacao.containsKey(moeda.getMoedaOrigem()) || !conversaoDeCotacao.containsKey(moeda.getMoedaDestino())) {
                throw new ErroDeDisponibilidadeException("Currency Code não encontrado!");
            }

            double taxaMoedaOrigem = conversaoDeCotacao.get(moeda.getMoedaOrigem());

            double taxaMoedaDestino = conversaoDeCotacao.get(moeda.getMoedaDestino());

            
            double valorConvertido = valor * taxaMoedaDestino / taxaMoedaOrigem;

            
            return valorConvertido;
  }
}
