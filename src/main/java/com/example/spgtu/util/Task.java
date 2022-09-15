package com.example.spgtu.util;

import com.example.spgtu.dao.entities.standart.Message;
import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.dao.repositories.*;
import com.example.spgtu.service.LanguageService;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class Task extends TimerTask {
    protected UserRepo userRepo = TelegramBotRepositoryProvider.getUserRepo();
    protected ButtonRepo buttonRepo = TelegramBotRepositoryProvider.getButtonRepo();
    protected MessageRepo messageRepo = TelegramBotRepositoryProvider.getMessageRepo();
    protected KeyboardMarkUpRepo keyboardMarkUpRepo = TelegramBotRepositoryProvider.getKeyboardMarkUpRepo();

    private long chatId;
    private DefaultAbsSender bot;

    public Task(long chatId, DefaultAbsSender bot) {
        this.chatId = chatId;
        this.bot = bot;
    }

    @Override
    public void run() {
        List<User> users = userRepo.findAll();
        List<String> usersIin = new ArrayList<>();
        List<String> freeUsersIin = new ArrayList<>();
        List<String>  unRegisteredUsersNames = new ArrayList<>();
        List<String>  unregisteredUsersIin = new ArrayList<>();

//        users.forEach(user -> {
//            usersIin.add(user.getIIN());
//        });

        // ------------------------------- //

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getText(1560) + "\n");
        for (String userName : unRegisteredUsersNames) {
//            stringBuilder.append("ФИО: ").append(userName).append("    ИИН: ").append(freeUserRepo.findByFullName(userName).getIin()).append("\n");
        }
        sendMessage(stringBuilder.toString());
//        cancel();
    }

    protected String getText(int messageIdFromBD) {
        int lang = LanguageService.getLanguage(chatId).getId();
        Message mes;
        try {
            mes = messageRepo.findByIdAndLangId(messageIdFromBD, lang);
            return mes.getName();
        }catch (Exception e){
            e.printStackTrace();
        }
        return messageRepo.findByIdAndLangId(messageIdFromBD, LanguageService.getLanguage(chatId).getId()).getName();
    }

    private void sendMessage(String str){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(str).setParseMode("html").setChatId(chatId);
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
