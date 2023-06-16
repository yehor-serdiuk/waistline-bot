package ua.volcaniccupcake.waistline.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import ua.volcaniccupcake.waistline.Bot;
import ua.volcaniccupcake.waistline.dto.ItemDTO;
import ua.volcaniccupcake.waistline.exception.EmptyItemListException;
import ua.volcaniccupcake.waistline.exception.EnergyTypeNotFoundException;
import ua.volcaniccupcake.waistline.exception.ItemNotFoundException;
import ua.volcaniccupcake.waistline.mapper.ConfigMapper;
import ua.volcaniccupcake.waistline.mapper.EnergyTypeMapper;
import ua.volcaniccupcake.waistline.model.Config;
import ua.volcaniccupcake.waistline.model.EnergyType;
import ua.volcaniccupcake.waistline.model.Item;
import ua.volcaniccupcake.waistline.repository.ConfigRepository;
import ua.volcaniccupcake.waistline.repository.EnergyTypeRepository;
import ua.volcaniccupcake.waistline.repository.ItemRepository;
import ua.volcaniccupcake.waistline.mapper.ItemMapper;
import ua.volcaniccupcake.waistline.session.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
@Slf4j
public class BotService {
    private final Bot bot;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final EnergyTypeRepository energyTypeRepository;
    private final ConfigRepository configRepository;
    private final SessionManager sessionManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        List<Item> itemList = itemRepository.findAll();
        if (itemList.isEmpty()) {
            throw new EmptyItemListException();
        }
        return ItemMapper.itemListToMessageList(itemList);
    }
    public List<String> listItemsRemove() {
        List<Item> itemList = itemRepository.findAll();
        if (itemList.isEmpty()) {
            throw new EmptyItemListException();
        }
        return ItemMapper.itemListToMessageListRemove(itemList);
    }
    public List<String> listEnergyTypes() {
        List<EnergyType> energyTypeList = (List<EnergyType>) energyTypeRepository.findAll();
        return EnergyTypeMapper.energyTypeListToMessageList(energyTypeList);
    }

    public String getConfig() {
        Config config = configRepository.findAll().iterator().next();
        return ConfigMapper.configToMessage(config);
    }

    public List<String> listItemsByEnergyTypeId(int energyTypeId) {
        EnergyType energyType = energyTypeRepository.findById(energyTypeId)
                .orElseThrow(() -> new EnergyTypeNotFoundException("Energy type with ID " + energyTypeId + " not found"));
        List<Item> itemList = energyType.getItemList();
        if (itemList.isEmpty()) {
            throw new EmptyItemListException();
        }
        return ItemMapper.itemListToMessageList(itemList);
    }
    public void setEnergyRatio(String energyRatioString) {
        int energyRatio = Integer.parseInt(energyRatioString);
        Config config = configRepository.findAll().iterator().next();
        config.setEnergyRatio(energyRatio);
        configRepository.save(config);
        sessionManager.setSessionType(null);
    }

    public void setMuscleRatio(String muscleRatioString) {
        int muscleRatio = Integer.parseInt(muscleRatioString);
        Config config = configRepository.findAll().iterator().next();
        config.setMuscleRatio(muscleRatio);
        configRepository.save(config);
        sessionManager.setSessionType(null);
    }

    public void setMealSize(String mealSizeString) {
        int mealSize = Integer.parseInt(mealSizeString);
        Config config = configRepository.findAll().iterator().next();
        config.setMealCalories(mealSize);
        configRepository.save(config);
        sessionManager.setSessionType(null);
    }

    public void addItem(String messageText) throws IOException {
        log.debug("Received json string: " + messageText);
        ItemDTO itemDTO = objectMapper.readValue(messageText, ItemDTO.class);
        Item item = itemMapper.itemDTOToItem(itemDTO);
        itemRepository.save(item);
        sessionManager.setSessionType(null);
    }
    public List<String> findItems(String name) {
        List<Item> itemList = itemRepository.findAllByNameContainingIgnoreCase(name);
        sessionManager.setSessionType(null);
        return ItemMapper.itemListToMessageList(itemList);
    }

    public void removeItemById(int itemId) {
        itemRepository.deleteById(itemId);
    }

    public List<String> calculate(String input) {
        List<Item> itemList = mapItems(input);
        List<Item> energyItemList = new ArrayList<>();
        List<Item> muscleItemList = new ArrayList<>();
        for (Item item : itemList) {
            item.setSize(item.getSize() / item.getCalories());
            item.setFat(item.getFat() / item.getCalories());
            item.setCarbs(item.getCarbs() / item.getCalories());
            item.setProteins(item.getProteins() / item.getCalories());
            item.setCalories(1.0);

            if (item.getEnergyType().getName().equals("energy")) {
                energyItemList.add(item);
            } else if (item.getEnergyType().getName().equals("muscle")) {
                muscleItemList.add(item);
            }
        }

        Config config = configRepository.findAll().iterator().next();
        double coefficient = (double) config.getMealCalories() / (config.getEnergyRatio() + config.getMuscleRatio());
        double energyCalories = coefficient * config.getEnergyRatio();
        double muscleCalories = coefficient * config.getMuscleRatio();
        if (energyItemList.isEmpty()) {
            muscleCalories = (double) config.getMealCalories() / muscleItemList.size();
        }
        if (muscleItemList.isEmpty()) {
            energyCalories = (double) config.getMealCalories() / energyItemList.size();
        }

        double caloriesPerEnergyItem = 0;
        if (!energyItemList.isEmpty()) {
            caloriesPerEnergyItem = energyCalories / energyItemList.size();
        }

        double caloriesPerMuscleItem = 0;
        if (!muscleItemList.isEmpty()) {
            caloriesPerMuscleItem = muscleCalories / muscleItemList.size();
        }

        List<Item> transformedItems = new ArrayList<>();
        for (Item energyItem : energyItemList) {
            energyItem.setCalories(energyItem.getCalories() * caloriesPerEnergyItem);
            energyItem.setSize(energyItem.getSize() * caloriesPerEnergyItem);
            energyItem.setFat(energyItem.getFat() * caloriesPerEnergyItem);
            energyItem.setCarbs(energyItem.getCarbs() * caloriesPerEnergyItem);
            energyItem.setProteins(energyItem.getProteins() * caloriesPerEnergyItem);
            transformedItems.add(energyItem);
        }
        for (Item muscleItem : muscleItemList) {
            muscleItem.setCalories(muscleItem.getCalories() * caloriesPerMuscleItem);
            muscleItem.setSize(muscleItem.getSize() * caloriesPerMuscleItem);
            muscleItem.setFat(muscleItem.getFat() * caloriesPerMuscleItem);
            muscleItem.setCarbs(muscleItem.getCarbs() * caloriesPerMuscleItem);
            muscleItem.setProteins(muscleItem.getProteins() * caloriesPerMuscleItem);
            transformedItems.add(muscleItem);
        }
        sessionManager.setSessionType(null);
        return ItemMapper.itemListToMessageList(transformedItems);
    }
    private List<Item> mapItems(String input) throws NumberFormatException, ItemNotFoundException{
        List<Integer> idList = new ArrayList<>();
        Scanner scanner = new Scanner(input);
        while (scanner.hasNextLine()) {
            idList.add(Integer.parseInt(scanner.nextLine()));
        }

        List<Item> itemList = new ArrayList<>();
        for (Integer id : idList) {
            Item item = itemRepository.findById(id)
                    .orElseThrow(() -> new ItemNotFoundException("Item with ID " + id + " not found"));
            itemList.add(item);
        }
        return itemList;
    }
}
