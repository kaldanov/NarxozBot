package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.dao.enums.Language;
import com.example.spgtu.dao.enums.Status;
import com.example.spgtu.service.LanguageService;
import com.example.spgtu.util.Const;
import com.example.spgtu.util.UpdateUtil;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class id001_Registration extends Command {

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        User user = userRepo.findByChatId(chatId);
        deleteUpdateMessage();

        if (isButton(1)) {      //button: /start
            sendMessageWithKeyboard(1, 1); // выберите язык интерфейса
            return COMEBACK;
        }
        else if (isButton(9)) {  // button:  🔁 Сменить язык
            deleteUpdateMessage();
            sendMessageWithKeyboard(11, 1); // выберите язык интерфейса
            return COMEBACK;
        }
        else if (isButton(Const.RU_LANGUAGE)) {
            LanguageService.setLanguage(chatId, Language.ru);
            if (user == null)
                getPhone();
            else getInterface();
        }
        else if (isButton(Const.KZ_LANGUAGE)) {
            LanguageService.setLanguage(chatId, Language.kz);
            if (user == null)
                getPhone();
            else getInterface();
        }
        else if (isButton(20)) {        // Сменить статус
            if (user.isApplicant()) {
                getUserStatus();
            }
            else {
                sendMessageWithKeyboard(1, 1);
            }
        }
        else if (isButton(76) || isButton(102)) {
            getInterface();
        }

        else if (isButton(19)) {   // abiturient
            if (user != null) {  // changing user status
                if (user.isApplicant()){
                    sendMessageWithKeyboard(5, 7);  // главное меню
                }
                else if (user.isStudent())
                    sendMessageWithKeyboard(4, 2); // главное меню
                else{
                    user.setStatus(Status.APPLICANT);
                    userRepo.save(user);
                    sendMessageWithKeyboard(5, 7);  // главное меню
                }
            }
            else {
                getPhone();
            }
            return EXIT;
        } 
//        else if (isButton(18)) {    // student
//            if (user != null) {     // changing user status
//                user.setStatus(Status.STUDENT);
//                userRepo.save(user);
//                sendMessageWithKeyboard(4, 2); // главное меню
//                return EXIT;
//            }
//            else {
//                getPhone();
//            }
//        }
        else if (hasContact()){
            String phone = getFormatPhone(update.getMessage().getContact().getPhoneNumber());
            if (user == null) {
                user = new User();
                user.setChatId(chatId);
                user.setPhone(phone);
                user.setUserName(UpdateUtil.getFullName(update));
                userRepo.save(user);
            }
                getInterface();
        }

        updateMessageText = "";
        return EXIT;
    }

    private String getFormatPhone(String phone) {

        if (phone.charAt(0) == '8') {
            phone = phone.replaceFirst("8", "+7");
        } else if (phone.charAt(0) == '7') {
            phone = phone.replaceFirst("7", "+7");
        }

        return phone;
    }

    private void getInterface() throws TelegramApiException {
        if (userRepo.findByChatId(chatId) == null || userRepo.findByChatId(chatId).getStatus() == null ) {
            getUserStatus();
        }
        else {
            User userForCheck = userRepo.findByChatId(chatId);
            if (userForCheck.getStatus().equals(Status.STUDENT)) {
                sendMessageWithKeyboard(4, 2); // главное меню студента
            } else if (userForCheck.getStatus().equals(Status.APPLICANT)) {
                sendMessageWithKeyboard(4, 7); // главное меню абитуриента
            }
        }
    }




    private void getUserStatus() throws TelegramApiException {
        sendMessageWithKeyboard(9, 6);
    }
    private int getPhone() throws TelegramApiException {
        return sendMessageWithKeyboard(getText(Const.SET_MOBILE_PHONE_NUMBER),
                keyboardService.getReplyKeyboardRequestContact(getLanguage()));
    }
}
