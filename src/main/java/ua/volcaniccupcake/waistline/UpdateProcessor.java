package ua.volcaniccupcake.waistline;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;
import ua.volcaniccupcake.waistline.exception.ItemNotFoundException;
import ua.volcaniccupcake.waistline.repository.ConfigRepository;
import ua.volcaniccupcake.waistline.session.SessionManager;
import ua.volcaniccupcake.waistline.config.BotProperties;
import ua.volcaniccupcake.waistline.exception.EmptyItemListException;
import ua.volcaniccupcake.waistline.exception.EnergyTypeNotFoundException;
import ua.volcaniccupcake.waistline.exception.MessageCharacterLimitExceededException;
import ua.volcaniccupcake.waistline.service.BotService;
import ua.volcaniccupcake.waistline.session.SessionType;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateProcessor {
    private final int MESSAGE_CHARACTER_LIMIT = 4096;
    private final BotProperties botProperties;
    private final BotService botService;
    private final Bot bot;
    private final SessionManager sessionManager;
    private final ConfigRepository configRepository;
    private final ObjectMapper objectMapper =  new ObjectMapper();


    @SneakyThrows
    public void processUpdate(Update update) {
        if (update.message() == null) {
            return;
        }
        log.debug("Message received from userId " + update.message().from().id());
        if (update.message().text() == null) {
            return;
        }
        if (!isFromAdmin(update.message())) {
            return;
        }

        Long messageChatId = update.message().chat().id();
        String messageText = update.message().text();
        if (messageText.equals("/cancel")) {
            sessionManager.setSessionType(null);
            sendMessage(messageChatId, "Operation cancelled.");
        } else if (sessionManager.sessionExists()) {
            if (sessionManager.getSessionType() == SessionType.SET_ENERGY_RATIO) {
                try {
                    botService.setEnergyRatio(messageText);
                    sendMessage(messageChatId, "Energy ratio has been updated.");
                } catch (NumberFormatException e) {
                    sendMessage(messageChatId, "Invalid energy ratio: not a number");
                }
            } else if (sessionManager.getSessionType() == SessionType.SET_MUSCLE_RATIO) {
                try {
                    botService.setMuscleRatio(messageText);
                    sendMessage(messageChatId, "Muscle ratio has been updated.");
                } catch (NumberFormatException e) {
                    sendMessage(messageChatId, "Invalid muscle ratio: not a number");
                }
            } else if (sessionManager.getSessionType() == SessionType.SET_MEAL_SIZE) {
                try {
                    botService.setMealSize(messageText);
                    sendMessage(messageChatId, "Meal size has been updated.");
                } catch (NumberFormatException e) {
                    sendMessage(messageChatId, "Invalid meal size: not a number");
                }
            } else if (sessionManager.getSessionType() == SessionType.ADD_ITEM) {
                try {
                    botService.addItem(messageText);
                    sendMessage(messageChatId, "Item successfully created.");
                } catch (IOException e) {
                    sendMessage(messageChatId, "Invalid syntax");
                } catch (EnergyTypeNotFoundException e) {
                    sendMessage(messageChatId, e.getMessage());
                }
            } else if (sessionManager.getSessionType() == SessionType.SEARCH) {
                try {
                    sendMessage(messageChatId, botService.findItems(messageText));
                } catch (ItemNotFoundException e) {
                    sendMessage(messageChatId, e.getMessage());
                }
            } else if (sessionManager.getSessionType() == SessionType.CALCULATE) {
                try {
                    sendMessage(messageChatId, botService.calculate(messageText));
                } catch (NumberFormatException e) {
                    sendMessage(messageChatId, "Invalid syntax: not a number");
                } catch (ItemNotFoundException e) {
                    sendMessage(messageChatId, e.getMessage());
                }
            }
        } else if (messageText.equals("/setenergyratio")) {
            sessionManager.setSessionType(SessionType.SET_ENERGY_RATIO);
            sendMessage(messageChatId, "Send me the new energy ratio.\n\n/cancel - cancel operation");
        } else if (messageText.equals("/setmuscleratio")) {
            sessionManager.setSessionType(SessionType.SET_MUSCLE_RATIO);
            sendMessage(messageChatId, "Send me the new muscle ratio.\n\n/cancel - cancel operation");
        } else if (messageText.equals("/setmealsize")) {
            sessionManager.setSessionType(SessionType.SET_MEAL_SIZE);
            sendMessage(messageChatId, "Send me the new meal size.\n\n/cancel - cancel operation");
        } else if (messageText.equals("/additem")) {
            sessionManager.setSessionType(SessionType.ADD_ITEM);
            String sampleItemJson = "{\n" +
                    "   \"name\":\"name\",\n" +
                    "   \"calories\":300,\n" +
                    "   \"size\":100,\n" +
                    "   \"fat\":5,\n" +
                    "   \"carbs\":5,\n" +
                    "   \"proteins\":5,\n" +
                    "   \"energyTypeId\":1\n" +
                    "}";
            sendMessage(messageChatId, sampleItemJson);
        } else if (messageText.equals("/search")) {
            sessionManager.setSessionType(SessionType.SEARCH);
            sendMessage(messageChatId, "Send a search request");
        } else if (messageText.equals("/calculate")) {
            sessionManager.setSessionType(SessionType.CALCULATE);
            sendMessage(messageChatId, "Send item IDs for the meal.\n\n/cancel - cancel");
        } else if (messageText.equals("/listitems")) {
            log.debug("Command /listitems triggered");

            try {
                sendMessage(messageChatId, botService.listItems());
            } catch (EmptyItemListException e) {
                sendMessage(messageChatId, e.getMessage());
            }

        } else if (messageText.equals("/getconfig")) {
            log.debug("Command /getconfig triggered");
            sendMessage(messageChatId, botService.getConfig());
        } else if (messageText.equals("/listtypes")) {
            log.debug("Command /listtypes triggered");
            sendMessage(messageChatId, botService.listEnergyTypes());
        } else if (messageText.equals("/removeitem")) {
            try {
                sendMessage(messageChatId, botService.listItemsRemove());
            } catch (EmptyItemListException e) {
                sendMessage(messageChatId, e.getMessage());
            }
        } else if (messageText.startsWith("/type_")) {
            try {
                int energyTypeId = Integer.parseInt(messageText.replace("/type_", ""));
                sendMessage(messageChatId, botService.listItemsByEnergyTypeId(energyTypeId));
            } catch (NumberFormatException e) {
                sendMessage(messageChatId, "Invalid type ID: not a number");
            } catch (EnergyTypeNotFoundException e) {
                sendMessage(messageChatId, e.getMessage());
            }
        } else if (messageText.startsWith("/removeitem_")) {
            try {
                int itemId = Integer.parseInt(messageText.replace("/removeitem_", ""));
                botService.removeItemById(itemId);
                sendMessage(messageChatId, "Item with ID " + itemId + " is removed.");
            } catch (NumberFormatException e) {
                sendMessage(messageChatId, "Invalid item ID: not a number");
            }
        } else {
            sendMessage(messageChatId, botService.getHelpMessage());
        }

    }
    private boolean isFromAdmin(Message message) {
        Long adminId = botProperties.getAdminId();
        Long fromId = message.from().id();
        return adminId.equals(fromId);
    }
    private void sendMessage(Long chatId, String text) {
        if (text.length() > MESSAGE_CHARACTER_LIMIT) {
            throw new MessageCharacterLimitExceededException("Message limit exceeded: " + text.length());
        }
        bot.execute(new SendMessage(chatId, text));
    }
    private void sendMessage(Long chatId, List<String> messageList) {
        Thread t = new Thread(() -> {
            for (String message : messageList) {
                sendMessage(chatId, message);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException ignored) {}
            }
        });
        t.start();
    }
}
