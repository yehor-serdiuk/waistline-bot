package ua.volcaniccupcake.waistline.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BotService {
    private final TelegramBot bot;

    public void confirmAllUpdates() {
        GetUpdatesResponse getUpdatesResponse = bot.execute(new GetUpdates());
        List<Update> updateList = getUpdatesResponse.updates();
        if (updateList == null || updateList.isEmpty()) {
            return;
        }
        Update latestUpdate = updateList.get(updateList.size() - 1);

        GetUpdates confirmAllUpdates = new GetUpdates()
                .offset(latestUpdate.updateId() + 1);
        bot.execute(confirmAllUpdates);
    }
}
