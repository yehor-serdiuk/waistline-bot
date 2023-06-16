package ua.volcaniccupcake.waistline.mapper;

import ua.volcaniccupcake.waistline.model.EnergyType;
import ua.volcaniccupcake.waistline.model.Item;

import java.util.ArrayList;
import java.util.List;


public class EnergyTypeMapper {
    private static final int MESSAGE_CHARACTER_LIMIT = 4096;

    public static List<String> energyTypeListToMessageList(List<EnergyType> energyTypeList) {
        List<String> messageList = new ArrayList<>();
        StringBuilder messageBuilder = new StringBuilder();
        for (EnergyType energyType : energyTypeList) {
            String energyTypeString = energyTypeToString(energyType);
            if (messageBuilder.length() + energyTypeString.length() > MESSAGE_CHARACTER_LIMIT) {
                messageList.add(messageBuilder.toString());
                messageBuilder = new StringBuilder();
            }

            messageBuilder.append(energyTypeString);
        }

        if (!messageBuilder.toString().isBlank()) {
            messageList.add(messageBuilder.toString());
        }
        return messageList;
    }
    private static String energyTypeToString(EnergyType energyType) {
        return "==============================" + "\n"
                + "/type_" + energyType.getId() + "\n"
                + "\n"
                + "Id:" + "\n"
                + energyType.getId() + "\n"
                + "\n"
                + "Name:" + "\n"
                + energyType.getName() + "\n"
                + "\n";
    }
}
