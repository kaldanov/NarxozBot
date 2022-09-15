package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.dao.enums.Status;
import com.example.spgtu.service.AuthorizationService;
import com.example.spgtu.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class id0667_LoginStudent extends Command {

    String login;
    String password;

    int del;
    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        deleteUpdateMessage();
        if (!isRegistered()) {
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }


        switch (waitingType){
            case START:
                deleteUpdateMessage();
                del = sendMessage(173);
                waitingType = WaitingType.SET_LOGIN;
                return COMEBACK;
            case SET_LOGIN:
                deleteUpdateMessage();
                if (hasMessageText()){
                    deleteMessage(del);
                    login = updateMessageText;
                    del = sendMessage(174);
                    waitingType = WaitingType.SET_PASSWORD;
                }
                return COMEBACK;
            case SET_PASSWORD:
                deleteUpdateMessage();
                if (hasMessageText()){
                    deleteMessage(del);
                    password = updateMessageText;
                    if (!AuthorizationService.login(login, password)){
                        sendMessage(175);
                        del = sendMessage(173);
                        waitingType = WaitingType.SET_LOGIN;
                    }
                    else {
                        User user = userRepo.findByChatId(chatId);
                        user.setStatus(Status.STUDENT);
                        user.setEmail(login);
                        userRepo.save(user);
                        sendMessageWithKeyboard(4, 2); // главное меню студента
                        return EXIT;
                    }
                }
                return COMEBACK;

        }

        return EXIT;
    }
}
