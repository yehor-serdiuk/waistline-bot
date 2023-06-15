package ua.volcaniccupcake.waistline.mapper;

import ua.volcaniccupcake.waistline.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {
    private static final int MESSAGE_CHARACTER_LIMIT = 4096;
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
                + item.getSize() + " gram" + "\n"
                + "\n"
                + "Proteins:" + "\n"
                + item.getProteins() + "g" + "\n"
                + "\n"
                + "Fat:" + "\n"
                + item.getFat() + "g" + "\n"
                + "\n"
                + "Carbs:" + "\n"
                + item.getCarbs() + "g" + "\n"
                + "\n";
    }
}
