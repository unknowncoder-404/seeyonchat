package com.seeyon.chat.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.seeyon.chat.core.model.Chat;
import com.seeyon.chat.settings.AppSettingsState;
import com.seeyon.chat.common.ChatConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

/**
 * @author Shaozz
 */
public class ChatHttpUtil {

    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofMinutes(30))
            .executor(Executors.newSingleThreadExecutor())
            .build();

    private static String[] buildHeaders() {
        return new String[]{"Content-Type", "application/json", "Authorization", "Apikey " + AppSettingsState.getInstance().getApiKey()};
    }

    public static String createChatbot(@NotNull String model) throws IOException, InterruptedException {
        ObjectNode bodyJson = ChatConstants.OBJECT_MAPPER.createObjectNode();
        bodyJson.put("model", model);
        bodyJson.put("name", ChatBundle.message("chatbot.name", model));
        bodyJson.put("prompt", ChatBundle.message("chatbot.prompt"));
        bodyJson.put("description", ChatBundle.message("chatbot.description"));
        String body = ChatConstants.OBJECT_MAPPER.writeValueAsString(bodyJson);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ChatConstants.BASE_URL + "/chatbots"))
                .headers(buildHeaders())
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .timeout(Duration.ofSeconds(10))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode jsonNode = ChatConstants.OBJECT_MAPPER.readTree(response.body());
        String message = jsonNode.get("message").asText();
        if ("Chatbot created successfully.".equals(message)) {
            return jsonNode.get("data").get("id").asText();
        } else {
            throw new RuntimeException(message);
        }
    }

    public static String createChat(@NotNull String chatbotId) throws IOException, InterruptedException {
        ObjectNode bodyJson = ChatConstants.OBJECT_MAPPER.createObjectNode();
        bodyJson.put("chatbotId", chatbotId);
        String body = ChatConstants.OBJECT_MAPPER.writeValueAsString(bodyJson);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ChatConstants.BASE_URL + "/chats"))
                .headers(buildHeaders())
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .timeout(Duration.ofSeconds(10))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode jsonNode = ChatConstants.OBJECT_MAPPER.readTree(response.body());
        if (jsonNode.has("_id")) {
            return jsonNode.get("_id").asText();
        }
        throw new RuntimeException(jsonNode.get("message").asText());
    }

    public static CompletableFuture<HttpResponse<Void>> sendMessage(@NotNull String chatId, String content, Flow.Subscriber<? super List<ByteBuffer>> subscriber) throws JsonProcessingException {
        ObjectNode bodyJson = ChatConstants.OBJECT_MAPPER.createObjectNode();
        bodyJson.put("role", "user");
        bodyJson.put("content", content);
        String body = ChatConstants.OBJECT_MAPPER.writeValueAsString(bodyJson);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ChatConstants.BASE_URL + "/chats/" + chatId + "/messages"))
                .headers(buildHeaders())
                .header("Accept", "text/event-stream")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .timeout(Duration.ofSeconds(30))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.fromSubscriber(subscriber));
    }

    public static List<Chat> getChats(@NotNull String chatbotId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ChatConstants.BASE_URL + "/chats"))
                .headers(buildHeaders())
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Chat> chats = ChatConstants.OBJECT_MAPPER.readValue(response.body(), new TypeReference<>() {
        });
        return chats.stream().filter(chat -> chatbotId.equals(chat.getChatbotId())).toList();
    }

    public static Chat getChat(@NotNull String chatId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ChatConstants.BASE_URL + "/chats/" + chatId))
                .headers(buildHeaders())
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        try {
            return ChatConstants.OBJECT_MAPPER.readValue(response.body(), Chat.class);
        } catch (JsonProcessingException e) {
            JsonNode jsonNode = ChatConstants.OBJECT_MAPPER.readTree(response.body());
            if (jsonNode.has("message")) {
                throw new RuntimeException(jsonNode.get("message").asText());
            }
            throw e;
        }
    }

    public static CompletableFuture<HttpResponse<String>> getSummarizeAsync(@NotNull String chatId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ChatConstants.BASE_URL + "/chats/" + chatId + "/summarize"))
                .headers(buildHeaders())
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public static CompletableFuture<HttpResponse<String>> updateChatAsync(@NotNull String chatId, @NotNull String body) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ChatConstants.BASE_URL + "/chats/" + chatId))
                .headers(buildHeaders())
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .timeout(Duration.ofSeconds(10))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void deleteChat(@NotNull String chatId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ChatConstants.BASE_URL + "/chats/" + chatId))
                .headers(buildHeaders())
                .DELETE()
                .timeout(Duration.ofSeconds(10))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
