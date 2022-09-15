package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.standart.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class id002_ApplicantPart extends Command {

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        User user = userRepo.findByChatId(chatId);
        deleteUpdateMessage();
        if (!isRegistered() || !user.isApplicant()) {
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }


        if(isButton(91)){ //◀ Назад
            sendMessageWithKeyboard(4,7);
        }
        if(isButton(87)){ //Получить справку
                sendMessageWithKeyboard(getText(90), 30);
        }
        else if (isButton(4)) //Подготовительные курсы
            sendMessageWithKeyboard(6, 4);

        return EXIT;
    }
}
