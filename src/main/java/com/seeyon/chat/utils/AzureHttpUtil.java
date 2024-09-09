package com.seeyon.chat.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.seeyon.chat.common.ChatConstants;
import com.seeyon.chat.core.model.Chat;
import com.seeyon.chat.settings.AppSettingsState;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

/**
 * @author Shaozz
 */
public class AzureHttpUtil {

    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofMinutes(600))
            .executor(Executors.newSingleThreadExecutor())
            .build();

    public static String sendMessageUriTemplate = "{uri}/openai/deployments/{model}/chat/completions?api-version={apiVersion}";
//    public static String uri = "https://seeyonai.openai.azure.com/openai";
//    public static String model = "GPT-4-Preview";
//    public static String apiVersion = "2024-06-01";



    public static String sendMessage(String systemTemplate, String content) throws IOException, InterruptedException {

        Map llmBodyMap = getLLMBodyMap(systemTemplate,content);


        String body = ChatConstants.OBJECT_MAPPER.writeValueAsString(llmBodyMap);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(sendMessageUriTemplate.replace("{uri}", AppSettingsState.getInstance().getGptUri()).replace("{model}", AppSettingsState.getInstance().getGptModel()).replace("{apiVersion}", AppSettingsState.getInstance().getGptApiVersion())))
                .headers(buildHeaders())
                .header("Accept","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .timeout(Duration.ofSeconds(600))
                .build();
        HttpResponse<String> send = client.send(request, HttpResponse.BodyHandlers.ofString());

        return send.body();
    }

    private static @NotNull Map getLLMBodyMap(String systemTemplate, String content) {
        Map<String,String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", systemTemplate);
        Map<String,String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", content);

        Map map = new HashMap<>();
        List messagesList = new ArrayList();
        messagesList.add(systemMessage);
        messagesList.add(userMessage);
        map.put("messages", messagesList);
        map.put("temperature", 0.00000001);
        return map;
    }


    private static String[] buildHeaders() {
        //TODO: 需要将api-key替换成页面配置
        return new String[]{"Content-Type", "application/json", "api-key", AppSettingsState.getInstance().getGptApiKey()};
    }


}
