package babak.learning.anthropic.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.Generation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;

    private String sendMessage(ChatMessageModel messageModel, Supplier<String> system) {
        List<Generation> results = Objects.requireNonNull(chatClient
                .prompt()
                .system(system.get())
                .user(messageModel.message())
                .call()
                .chatResponse()).getResults();
        //results(0) : thinking block (using sonnet makes thinking block which cannot be disabled)
        //results(1) : response
        return results.get(1).getOutput().getText();
    }

    public String sendMessage(ChatMessageModel messageModel) {
        return sendMessage(messageModel, () -> switch (messageModel.type()) {
            case TALK -> "As a friend talk to me.";
            case SUMMARIZE -> "As a professional editor summerize the text in maximum 100 words.";
        });
    }
}
