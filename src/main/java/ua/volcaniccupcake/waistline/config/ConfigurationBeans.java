package ua.volcaniccupcake.waistline.config;

import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ConfigurationBeans {
    private final BotProperties botProperties;

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(botProperties.getToken());
    }
}
