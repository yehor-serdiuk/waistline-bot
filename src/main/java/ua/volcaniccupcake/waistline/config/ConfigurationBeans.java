package ua.volcaniccupcake.waistline.config;

import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.volcaniccupcake.waistline.Bot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@RequiredArgsConstructor
public class ConfigurationBeans {
    private final BotProperties botProperties;

    @Bean
    public Bot telegramBot() throws IOException {
        String helpMessage = Files.readString(Path.of("file/help-message.txt"));
        return new Bot(botProperties.getToken(), helpMessage);
    }
}
