package ua.volcaniccupcake.waistline;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.volcaniccupcake.waistline.config.BotProperties;
import ua.volcaniccupcake.waistline.exception.MessageCharacterLimitExceededException;
import ua.volcaniccupcake.waistline.service.BotService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateProcessor {
    private final int MESSAGE_CHARACTER_LIMIT = 4096;
    private final BotProperties botProperties;
    private final BotService botService;
    private final Bot bot;



    public void processUpdate(Update update) {
        if (update.message() == null) {
            return;
        }
        log.debug("Message received from userId " + update.message().from().id());
        if (update.message().text() == null) {
            return;
        }
        if (!isFromAdmin(update.message())) {
            return;
        }

        Long messageChatId = update.message().chat().id();
        String messageText = update.message().text();

        if (messageText.equals("/listitems")) {
            log.debug("Command /listitems triggered");
            botService.listItems().forEach(message -> sendMessage(messageChatId, message));
        }
        else {
            sendMessage(messageChatId, botService.getHelpMessage());
        }

    }
    private boolean isFromAdmin(Message message) {
        Long adminId = botProperties.getAdminId();
        Long fromId = message.from().id();
        return adminId.equals(fromId);
    }
    private void sendMessage(Long chatId, String text) {
        if (text.length() > MESSAGE_CHARACTER_LIMIT) {
            throw new MessageCharacterLimitExceededException("Message limit exceeded: " + text.length());
        }
        bot.execute(new SendMessage(chatId, text));
    }
    private void sendMessage(Long chatId, List<String> messageList) {
        Thread t = new Thread(() -> {
            for (String message : messageList) {
                sendMessage(chatId, message);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException ignored) {}
            }
        });
        t.start();
    }
}
