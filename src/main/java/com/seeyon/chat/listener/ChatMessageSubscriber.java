package com.seeyon.chat.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seeyon.chat.toolWindow.ChatToolWindowService;
import com.seeyon.chat.ui.ChatBoxComponent;
import com.seeyon.chat.ui.ChatComponent;
import com.seeyon.chat.utils.NotificationUtil;

import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Flow;

/**
 * @author Shaozz
 */
public class ChatMessageSubscriber implements Flow.Subscriber<List<ByteBuffer>> {

    private static final ObjectMapper mapper = new ObjectMapper();

    private Flow.Subscription subscription;

    private final ChatBoxComponent chatBoxComponent;

    private final ChatComponent answer;

    public ChatMessageSubscriber(ChatBoxComponent chatBoxComponent) {
        this.chatBoxComponent = chatBoxComponent;
        answer = ChatComponent.ofAnswer();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        SwingUtilities.invokeLater(() -> {
            chatBoxComponent.removeLoader();
            chatBoxComponent.addChat(answer.getComponent());
        });

        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(List<ByteBuffer> item) {
        StringBuilder sb = new StringBuilder();
        for (ByteBuffer i : item) {
            byte[] buf = new byte[i.remaining()];
            i.get(buf);
            String s = new String(buf, StandardCharsets.UTF_8);

            // 逐个解析JSON对象
            int index = 0;
            while (index < s.length()) {
                try {
                    // 从当前索引开始解析JSON对象
                    JsonNode node = mapper.readTree(s.substring(index));
                    if (node.has("content")) {
                        sb.append(node.get("content").asText());
                    }
                    // 更新索引位置，跳过当前解析的JSON对象
                    // 注意：这里假设每个JSON对象之间没有分隔符，如果有分隔符，需要相应调整
                    index += node.toString().length();
                } catch (Exception e) {
                    break; // 如果解析出错，终止循环
                }
            }
        }
        SwingUtilities.invokeLater(() -> answer.append(sb.toString()));
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof IOException) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            ChatToolWindowService.getInstance().stopGenerating();
            // notify
            NotificationUtil.error(throwable.getMessage());
        });
    }

    @Override
    public void onComplete() {
        SwingUtilities.invokeLater(() -> ChatToolWindowService.getInstance().stopGenerating());
    }
}
