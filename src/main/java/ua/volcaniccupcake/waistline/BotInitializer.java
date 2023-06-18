package ua.volcaniccupcake.waistline;

import com.pengrad.telegrambot.UpdatesListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ua.volcaniccupcake.waistline.service.BotService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BotInitializer {
    private final Bot bot;
    private final BotService botService;
    private final UpdateProcessor updateProcessor;

    @Order(0)
    @EventListener(ContextRefreshedEvent.class)
    public void confirmAllUpdates() {
       botService.confirmAllUpdates();
    }

    @Order(1)
    @EventListener(ContextRefreshedEvent.class)
    public void updatesListener() {
        bot.setUpdatesListener(updateList -> {
            updateList.forEach(updateProcessor::processUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
