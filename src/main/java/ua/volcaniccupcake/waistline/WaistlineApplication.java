package ua.volcaniccupcake.waistline;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ua.volcaniccupcake.waistline.config.BotProperties;

@SpringBootApplication
@EnableConfigurationProperties(BotProperties.class)
public class WaistlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaistlineApplication.class, args);
	}

}
