package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.service.KeyboardService;
import com.example.spgtu.service.LanguageService;
import com.example.spgtu.util.Const;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class id009_Dekanat extends Command {

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        User user = userRepo.findByChatId(chatId);
        deleteUpdateMessage();
        if (!isRegistered() || !userRepo.findByChatId(chatId).isStudent()) {
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }




        if (user.isStudent() &&isButton(33)) {     // Заявление на отчисление
            sendMessageWithKeyboard(23, 16);
        }
        else if (user.isStudent() &&isButton(34)) {     // Утеря документов
            sendMessageWithKeyboard(23, 17);
        }
        else if (user.isStudent() &&isButton(105)) {     // назад
            sendMessageWithKeyboard(23, 15);
        }
        else if (user.isStudent() && isButton(106)) {     // назад
            sendMessageWithKeyboard(4, 2); // главное меню
        }
        else if (user.isStudent() && isButton(5)){ // деканат
            sendMessageWithKeyboard(23, 15);
        }


        return EXIT;
    }


}
