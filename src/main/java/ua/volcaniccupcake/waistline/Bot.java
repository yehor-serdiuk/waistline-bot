package ua.volcaniccupcake.waistline;

import com.pengrad.telegrambot.TelegramBot;
import lombok.Getter;

public class Bot extends TelegramBot {
    @Getter
    private final String helpMessage;
    public Bot(String botToken, String helpMessage) {
        super(botToken);
        this.helpMessage = helpMessage;
    }
}
