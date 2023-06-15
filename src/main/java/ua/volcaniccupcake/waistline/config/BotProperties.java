package ua.volcaniccupcake.waistline.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="bot")
@Getter
@Setter
public class BotProperties {
    private String username;
    private String token;
    private Long adminId;
    private Integer messageCharacterLimit;
}
