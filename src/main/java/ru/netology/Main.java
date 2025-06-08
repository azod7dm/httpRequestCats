package ru.netology;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            getRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getRequest() throws IOException {
        // Создаем HTTP клиент
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Создаем GET запрос
            HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");

            // Выполняем запрос
            HttpResponse response = httpClient.execute(request);

            // Получаем и выводим тело ответа
            String responseBody = EntityUtils.toString(response.getEntity());

            ObjectMapper om = new ObjectMapper();
            List<factCats> catFacts = om.readValue(responseBody,
                    new TypeReference<List<factCats>>() {});

            List<factCats> sortFactsCats = catFacts.stream()
                    .filter(catFact -> catFact.getUpvotes() != null && catFact.getUpvotes() > 0)
                    .collect(Collectors.toList());

            // Выводим отфильтрованные факты
            sortFactsCats.forEach(fact -> {
                System.out.println("ID: " + fact.getId());
                System.out.println("Text: " + fact.getText());
                System.out.println("Upvotes: " + fact.getUpvotes());
                System.out.println("--------------");
            });
        }
    }
}
