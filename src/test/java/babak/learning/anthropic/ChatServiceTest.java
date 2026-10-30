package babak.learning.anthropic;

import babak.learning.anthropic.chat.ChatMessageModel;
import babak.learning.anthropic.chat.ChatMessageType;
import babak.learning.anthropic.chat.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatServiceTest {

    @Autowired
    ChatService chatService;

    @Test
    public void talk() {
        String res = chatService.sendMessage(
                new ChatMessageModel("Hi, Claude! This is Babak.", ChatMessageType.TALK));
        System.out.println(res);
    }

    @Test
    void summarize() {

    }
}
