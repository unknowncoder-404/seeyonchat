package com.seeyon.chat.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.seeyon.chat.common.ChatConstants;
import com.seeyon.chat.ui.ChatPanel;
import com.seeyon.chat.ui.ChatCell;

import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;

/**
 * @author Shaozz
 */
public class ChatMessageSubscriber implements Flow.Subscriber<List<ByteBuffer>> {

    private Flow.Subscription subscription;

    private final ChatPanel chatPanel;

    private final ChatCell answer;

    private final CompletableFuture<Void> completion;

    public ChatMessageSubscriber(ChatPanel chatPanel, CompletableFuture<Void> completion) {
        this.chatPanel = chatPanel;
        this.completion = completion;

        this.answer = ChatCell.ofAnswer();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        SwingUtilities.invokeLater(() -> {
            chatPanel.removeLoadingLabel();
            chatPanel.addChatCell(answer);
        });
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
                    JsonNode node = ChatConstants.OBJECT_MAPPER.readTree(s.substring(index));
                    JsonNode contentNode = node.get("content");
                    if (!(contentNode == null || contentNode.isNull())) {
                        sb.append(contentNode.asText());
                    }
                    // 更新索引位置，跳过当前解析的JSON对象
                    // 注意：这里假设每个JSON对象之间没有分隔符，如果有分隔符，需要相应调整
                    index += node.toString().length();
                } catch (Exception e) {
                    break; // 如果解析出错，终止循环
                }
            }
        }
        if (!sb.isEmpty()) {
            SwingUtilities.invokeLater(() -> answer.append(sb.toString()));
        }
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof IOException) {
            return;
        }
        completion.completeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        completion.complete(null);
    }
}
