package ua.volcaniccupcake.waistline.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.volcaniccupcake.waistline.dto.ItemDTO;
import ua.volcaniccupcake.waistline.exception.EnergyTypeNotFoundException;
import ua.volcaniccupcake.waistline.model.EnergyType;
import ua.volcaniccupcake.waistline.model.Item;
import ua.volcaniccupcake.waistline.repository.EnergyTypeRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private static final int MESSAGE_CHARACTER_LIMIT = 4096;
    private final EnergyTypeRepository energyTypeRepository;
    public static List<String> itemListToMessageList(List<Item> itemList) {
        List<String> messageList = new ArrayList<>();
        StringBuilder messageBuilder = new StringBuilder();
        for (Item item : itemList) {
            String itemString = itemToString(item);
            if (messageBuilder.length() + itemString.length() > MESSAGE_CHARACTER_LIMIT) {
                messageList.add(messageBuilder.toString());
                messageBuilder = new StringBuilder();
            }

            messageBuilder.append(itemString);
        }

        if (!messageBuilder.toString().isBlank()) {
            messageList.add(messageBuilder.toString());
        }
        return messageList;
    }
    private static String itemToString(Item item) {
        return "==============================" + "\n"
                + "\n"
                + "Id:" + "\n"
                + item.getId() + "\n"
                + "\n"
                + "Name:" + "\n"
                + item.getName() + "\n"
                + "\n"
                + "Energy type:" + "\n"
                + item.getEnergyType().getName() + "\n"
                + "\n"
                + "Calories:" + "\n"
                + item.getCalories() + " kcal" + "\n"
                + "\n"
                + "Size:" + "\n"
                + item.getSize() + " g" + "\n"
                + "\n"
                + "Proteins:" + "\n"
                + item.getProteins() + " g" + "\n"
                + "\n"
                + "Fat:" + "\n"
                + item.getFat() + " g" + "\n"
                + "\n"
                + "Carbs:" + "\n"
                + item.getCarbs() + " g" + "\n"
                + "\n";
    }
    public static List<String> itemListToMessageListRemove(List<Item> itemList) {
        List<String> messageList = new ArrayList<>();
        StringBuilder messageBuilder = new StringBuilder();
        for (Item item : itemList) {
            String itemString = itemToStringRemove(item);
            if (messageBuilder.length() + itemString.length() > MESSAGE_CHARACTER_LIMIT) {
                messageList.add(messageBuilder.toString());
                messageBuilder = new StringBuilder();
            }

            messageBuilder.append(itemString);
        }

        if (!messageBuilder.toString().isBlank()) {
            messageList.add(messageBuilder.toString());
        }
        return messageList;
    }

    private static String itemToStringRemove(Item item) {
        return "==============================" + "\n"
                + "/removeitem_" + item.getId() + "\n"
                + "\n"
                + "Id:" + "\n"
                + item.getId() + "\n"
                + "\n"
                + "Name:" + "\n"
                + item.getName() + "\n"
                + "\n"
                + "Energy type:" + "\n"
                + item.getEnergyType().getName() + "\n"
                + "\n"
                + "Calories:" + "\n"
                + item.getCalories() + " kcal" + "\n"
                + "\n"
                + "Size:" + "\n"
                + item.getSize() + " g" + "\n"
                + "\n"
                + "Proteins:" + "\n"
                + item.getProteins() + " g" + "\n"
                + "\n"
                + "Fat:" + "\n"
                + item.getFat() + " g" + "\n"
                + "\n"
                + "Carbs:" + "\n"
                + item.getCarbs() + " g" + "\n"
                + "\n";
    }
    public Item itemDTOToItem(ItemDTO itemDTO) {
        EnergyType energyType = energyTypeRepository.findById(itemDTO.getEnergyTypeId())
                .orElseThrow(() -> new EnergyTypeNotFoundException("Energy type with id " + itemDTO.getEnergyTypeId() + " not found"));
        return Item.builder()
                .name(itemDTO.getName())
                .calories(itemDTO.getCalories())
                .size(itemDTO.getSize())
                .fat(itemDTO.getFat())
                .carbs(itemDTO.getCarbs())
                .proteins(itemDTO.getProteins())
                .energyType(energyType)
                .build();
    }
}
