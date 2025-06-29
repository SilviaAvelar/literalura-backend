package br.com.alura.literalura.literalura.service;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class ConsumoApi {

    public String obterDados(String endereco) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            System.err.println("Erro de I/O ao consultar API: " + endereco + " - " + e.getMessage());
            return null;
        } catch (InterruptedException e) {
            System.err.println("Requisição à API interrompida: " + endereco + " - " + e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("URI inválida para a API: " + endereco + " - " + e.getMessage());
            return null;
        }

        if (response == null || response.statusCode() != 200) {
            System.err.println("Falha na requisição API para: " + endereco +
                    ". Status Code: " + (response != null ? response.statusCode() : "N/A (resposta nula)") +
                    (response != null && response.body() != null && !response.body().isEmpty() ? ". Corpo: " + response.body() : ""));
            return null;
        }

        return response.body();
    }
}