package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.standart.Role;
import com.example.spgtu.dao.entities.standart.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


public class id029_RolesHandler extends Command {
    @Override
    public boolean execute() throws TelegramApiException {

        if (!isRegistered()) {
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }
        User user = userRepo.findByChatId(chatId);

        if (isButton(129) && user.isDekanat()){ //dek
            sendMessageWithKeyboard(167,46);
        }
        else if (isButton(130) && user.isOnay()){//onay
            sendMessageWithKeyboard(168,47);
        }
        else if (isButton(131) && user.isBron()){//bron
            sendMessageWithKeyboard(169,48);
        }
        else if (isButton(132) && user.isBuh()){//buh
            sendMessageWithKeyboard(170,49);
        }

        return EXIT;
    }

}

