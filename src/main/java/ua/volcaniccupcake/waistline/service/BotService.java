package ua.volcaniccupcake.waistline.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.volcaniccupcake.waistline.Bot;
import ua.volcaniccupcake.waistline.model.Item;
import ua.volcaniccupcake.waistline.repository.ItemRepository;
import ua.volcaniccupcake.waistline.mapper.ItemMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BotService {
    private final Bot bot;
    private final ItemRepository itemRepository;

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
    public String getHelpMessage() {
        return bot.getHelpMessage();
    }
    public List<String> listItems() {
        List<Item> itemList = (List<Item>) itemRepository.findAll();
        return ItemMapper.itemListToMessageList(itemList);
    }
}
