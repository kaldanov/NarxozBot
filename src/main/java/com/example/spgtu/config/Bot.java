package com.example.spgtu.config;

import com.example.spgtu.util.UpdateUtil;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

//@Component
@Slf4j

public class Bot extends TelegramLongPollingBot {

    private Map<Long, Conversation> conversations = new HashMap<>();

//    @PostConstruct
//    public void initIt() throws TelegramApiRequestException {
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
//        telegramBotsApi.registerBot(this);
//        log.info("Bot was registered : " + getBotUsername());
//
//    }

    //    private ButtonRepository buttonRepository  = TelegramBotRepositoryProvider.getButtonRepository();
    @Override
    public void onUpdateReceived(Update update) {

        Conversation conversation = getConversation(update);

        try {

                conversation.handleUpdate(update, this);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in conversation handleUpdate" + e);
        }
    }

    private Conversation getConversation(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        Conversation conversation = conversations.get(chatId);
        if (conversation == null) {
            log.info("InitNormal new conversation for '{}'", chatId);
            conversation = new Conversation();
            conversations.put(chatId, conversation);
        }
        return conversation;
    }

    @Override
    public String getBotUsername() {
        return "almaty_gup_bot";
    }

    @Override
    public String getBotToken() {return "637859693:AAF00NMMeSD9wB4cgeQk4TqvIcviPxD0ckI"; }
}
