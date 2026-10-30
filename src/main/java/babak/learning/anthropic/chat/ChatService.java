package babak.learning.anthropic.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;

    public String sendMessage(ChatMessageModel messageModel) {
        return chatClient
                .prompt()
                .user(messageModel.message())
                .call()
                .content();
    }
}
