package com.seeyon.chat.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static String createChatbot(@NotNull String model) throws IOException, InterruptedException {
        Map<String, String> map = new HashMap<>();
        map.put("model", model);
        map.put("name", ChatBundle.message("chatbot.name", model));
        map.put("prompt", ChatBundle.message("chatbot.prompt"));
        map.put("description", ChatBundle.message("chatbot.description"));
        String body = ChatConstants.OBJECT_MAPPER.writeValueAsString(map);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ChatConstants.BASE_URL + "/chatbots"))
                .headers(buildHeaders())
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .timeout(Duration.ofSeconds(10))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Map result = ChatConstants.OBJECT_MAPPER.readValue(response.body(), Map.class);
        String message = (String) result.get("message");
        if ("Chatbot created successfully.".equals(message)) {
            Map data = (Map) result.get("data");
            return (String) data.get("_id");
        } else {
            throw new RuntimeException(message);
        }
    }

    public static String createChat(@NotNull String chatbotId) throws IOException, InterruptedException {
        Map<String, String> map = Collections.singletonMap("chatbotId", chatbotId);
        String body = ChatConstants.OBJECT_MAPPER.writeValueAsString(map);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ChatConstants.BASE_URL + "/chats"))
                .headers(buildHeaders())
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .timeout(Duration.ofSeconds(10))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Map result = ChatConstants.OBJECT_MAPPER.readValue(response.body(), Map.class);
        Object id = result.get("_id");
        if (id == null) {
            throw new RuntimeException((String) result.get("message"));
        }
        return (String) id;
    }

    public static CompletableFuture<HttpResponse<Void>> sendMessage(@NotNull String chatId, String content, Flow.Subscriber<? super List<ByteBuffer>> subscriber) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("role", "user");
        map.put("content", content);
        String body = ChatConstants.OBJECT_MAPPER.writeValueAsString(map);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ChatConstants.BASE_URL + "/chats/" + chatId + "/messages"))
                .headers(buildHeaders())
                .header("Accept", "text/event-stream")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .timeout(Duration.ofSeconds(30))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.fromSubscriber(subscriber));
    }

    private static String[] buildHeaders() {
        return new String[]{"Content-Type", "application/json", "Authorization", "Apikey " + AppSettingsState.getInstance().getApiKey()};
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
            Map result = ChatConstants.OBJECT_MAPPER.readValue(response.body(), Map.class);
            Object message = result.get("message");
            if (message != null) {
                throw new RuntimeException((String) message);
            }
            throw e;
        }
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
