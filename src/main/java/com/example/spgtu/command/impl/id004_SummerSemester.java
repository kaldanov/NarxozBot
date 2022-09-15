package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.custom.Vedomost;
import com.example.spgtu.service.JDBC;
import com.example.spgtu.util.ButtonsLeaf;
import com.example.spgtu.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class id004_SummerSemester extends Command {

    List<Vedomost> allVedomosts;
    Vedomost currentVedomost;
    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        if (!isRegistered() || !userRepo.findByChatId(chatId).isStudent()) {
            deleteUpdateMessage();
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }


        switch (waitingType){
            case START:
                deleteUpdateMessage();
                allVedomosts = JDBC.getAllVedomost(userRepo.findByChatId(chatId).getEmail());
                if (allVedomosts.size() == 0){
                    sendMessage(180);
                    return EXIT;
                }
                else {
                    sendAllVed();
                }
                return COMEBACK;
            case SET_VEDOMOST:
                deleteUpdateMessage();
                if (hasCallbackQuery() && getVed(updateMessageText) != null){
                    currentVedomost = getVed(updateMessageText);
                    sendVedomostInfo();
                }
                return COMEBACK;
            case PAY_VEDOMOST:
                deleteUpdateMessage();
                if (hasCallbackQuery() && isButton(16)){
                    sendAllVed();
                }
                else if (hasCallbackQuery() && isButton(137)){
                    sendMessage("По оплате ждем ответа от банков");
                }
                return COMEBACK;

        }


        return EXIT;
    }

    private void sendVedomostInfo() throws TelegramApiException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getText(177)).append(" ").append(currentVedomost.getLearnYear()).append(next)
                .append(getText(179)).append(" ").append(currentVedomost.getSemester()).append(next)
                .append(getText(178)).append(" ").append(currentVedomost.getDis_Name());

        sendMessageWithKeyboard(stringBuilder.toString(), 51);
        waitingType = WaitingType.PAY_VEDOMOST;
    }

    private Vedomost getVed(String updateMessageText) {
        try {
            return allVedomosts.get(getInt(updateMessageText));
        }catch (Exception e){
            return null;
        }
    }

    private void sendAllVed() throws TelegramApiException {
        List<String> names = new ArrayList<>();
        allVedomosts.forEach(vedomost -> {names.add(vedomost.getDis_Name());});
        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(names);
        sendMessageWithKeyboard(176, buttonsLeaf.getListButton());
        waitingType = WaitingType.SET_VEDOMOST;
    }
}
