package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.dao.enums.Status;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class id014_Admin extends Command {
    private int del;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        deleteUpdateMessage();
        if (!isRegistered() || !isAdmin()) {
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }


        if (isButton(66) || isButton(67)) {
            sendMessageWithKeyboard(47, 25);
        }
        else if (isButton(68)){
            sendMessageWithKeyboard(60, 26);
        }
        else if (isButton(71)){
            User user = userRepo.findByChatId(chatId);
            user.setStatus(Status.STUDENT);
            userRepo.save(user);
            sendMessageWithKeyboard(49, 2);
        }
        else if (isButton(72)){
            User user = userRepo.findByChatId(chatId);
            user.setStatus(Status.APPLICANT);
            userRepo.save(user);
            sendMessageWithKeyboard(51, 7);
        }
        else if (isButton(84)){
            sendMessageWithKeyboard(137,37);
        }
        else if (isButton(107)){
            sendMessageWithKeyboard(139,38);
        }
        else if (isButton(108)){
            sendMessageWithKeyboard(140,39);
        }
        else if (isButton(123)){
            sendMessageWithKeyboard(166,45);
        }
        return EXIT;
    }
}
