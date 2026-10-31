package babak.learning.anthropic.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;

    private String sendMessage(ChatMessageModel messageModel, Supplier<String> system) {
        return chatClient
                .prompt()
                .options(
                        AnthropicChatOptions
                                .builder()
                                .thinkingDisabled()
                )
                .user(messageModel.message())
                .system(system.get())
                .call()
                .content();
    }

    public String sendMessage(ChatMessageModel messageModel) {
        return sendMessage(messageModel, () -> switch (messageModel.type()) {
            case TALK -> "As a friend talk to me.";
            case SUMMARIZE -> "As a professional editor summerize the text in maximum 100 words.";
        });
    }
}
