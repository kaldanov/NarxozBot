package com.example.spgtu.util;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Date;

public class UpdateUtil {

    public static long getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId();
        }
        if (update.hasEditedMessage()) {
            return update.getEditedMessage().getChatId();
        }
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        }
        if (update.hasInlineQuery()) {
            return update.getInlineQuery().getFrom().getId();
        }
        if (update.hasChosenInlineQuery()) {
            return update.getChosenInlineQuery().getFrom().getId();
        }
        if (update.hasChannelPost()) {
            return update.getChannelPost().getChatId();
        }
        if (update.hasEditedChannelPost()) {
            return update.getEditedChannelPost().getChatId();
        }
        if (update.hasPreCheckoutQuery()) {
            return update.getPreCheckoutQuery().getFrom().getId();
        }
        if (update.hasShippingQuery()) {
            return update.getShippingQuery().getFrom().getId();
        }
        return update.getMessage().getChatId();
    }

    public static  int getMessageId(Update update){
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getMessageId();
        }
            return update.getMessage().getMessageId();
    }

    public static String toString(Update update) {
        String text = update.toString();

        String[] split = text.split(",");
        StringBuilder result = new StringBuilder();
        result.append("\n");
        String concat = ",";
        for (String s : split) {
            if (s.contains("date=") && !s.contains("Update"))
                s = "date=" + getDate(update);
            if (!(s.contains("=null") || s.contains("='null'"))) {
                result.append(s).append(concat);
            }
        }
        return result.toString();
    }

    private static String getDate(Update update) {
        try {
            return DateUtil.getDbMmYyyyHhMmSs(new Date((long) update.getMessage().getDate() * 1000));
        }catch (Exception e){
             return DateUtil.getDbMmYyyyHhMmSs(new Date());
        }
    }


    public static User getUser(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom();
        }
        if (update.hasEditedMessage()) {
            return update.getEditedMessage().getFrom();
        }
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom();
        }
        if (update.hasInlineQuery()) {
            return update.getInlineQuery().getFrom();
        }
        if (update.hasShippingQuery()) {
            return update.getShippingQuery().getFrom();
        }
        if (update.hasPreCheckoutQuery()) {
            return update.getPreCheckoutQuery().getFrom();
        }
        if (update.hasChannelPost()) {
            return update.getChannelPost().getFrom();
        }
        if ((update.hasEditedChannelPost())) {
            return update.getEditedChannelPost().getFrom();
        }
        if (update.hasChosenInlineQuery()) {
            return update.getChosenInlineQuery().getFrom();
        }
        return null;
    }

    public static String getFullName(Update update){
        User user = getUser(update);
        String name = "";
        if (user!= null && user.getFirstName() != null)
            name += user.getFirstName();
        if (user!= null && user.getLastName() != null)
            name += user.getLastName();

        if (name.equals("") && user!= null && user.getUserName() != null)
            name = user.getUserName();

        return name;
    }
}
