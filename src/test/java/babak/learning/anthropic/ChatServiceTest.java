package babak.learning.anthropic;

import babak.learning.anthropic.chat.ChatMessageModel;
import babak.learning.anthropic.chat.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ChatService.class)
public class ChatServiceTest {

    @Autowired
    ChatService chatService;

    @Test
    public void send_message() {
        String res = chatService.sendMessage(new ChatMessageModel("Hi, Claude!"));
        System.out.println(res);
    }
}
