package ua.volcaniccupcake.waistline;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.volcaniccupcake.waistline.service.BotService;

@Component
@RequiredArgsConstructor
public class UpdateProcessor {
    private final BotService botService;

    @Autowired
    TelegramBot telegramBot;

    public void processUpdate(Update update) {
        /*temp*/telegramBot.execute(new SendMessage(update.message().chat().id(), "ahah"));
    }
}
