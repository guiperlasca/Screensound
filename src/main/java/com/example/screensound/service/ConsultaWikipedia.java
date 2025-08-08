package com.example.screensound.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ConsultaWikipedia {
    public static String obterInformacao(String nomeArtista) {
        String nomeCodificado = URLEncoder.encode(nomeArtista, StandardCharsets.UTF_8);

        String url = "https://pt.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&exintro=true&explaintext=true&titles=" + nomeCodificado;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return "Não foi possível obter informações sobre o artista. Código de erro: " + response.statusCode();
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            JsonNode pages = root.path("query").path("pages");
            if (pages.isMissingNode() || pages.isEmpty()) {
                return "Nenhuma informação encontrada para o artista: " + nomeArtista;
            }

            JsonNode pageContent = pages.elements().next();
            String extract = pageContent.path("extract").asText();

            if (extract.isEmpty() || extract.contains("pode referir-se a")) {
                return "Nenhuma informação encontrada para o artista: " + nomeArtista;
            }

            return extract;

        } catch (IOException | InterruptedException e) {
            return "Erro ao consultar a Wikipedia: " + e.getMessage();
        }
    }
}