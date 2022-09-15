package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.custom.LossDocs;
import com.example.spgtu.dao.entities.custom.RequestDeductions;
import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.dao.enums.TypeDocs;
import com.example.spgtu.dao.enums.TypeReferenceStudent;
import com.example.spgtu.service.FileGenerator;
import com.example.spgtu.service.KeyboardService;
import com.example.spgtu.util.Const;
import com.example.spgtu.util.type.WaitingType;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class id011_LossDocs extends Command {
    private int del;
    LossDocs reg = new LossDocs();

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        if (!isRegistered() || !userRepo.findByChatId(chatId).isStudent()) {
            deleteUpdateMessage();
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }


        switch (waitingType){
            case START: // 37 38 39 40
                if (isButton(37))
                    reg.setTypeDocs(TypeDocs.STUDENT_CARD);
                else if (isButton(38))
                    reg.setTypeDocs(TypeDocs.CREDIT_BOOK);
                else if (isButton(39))
                    reg.setTypeDocs(TypeDocs.BADGE);
                else if (isButton(40))
                    reg.setTypeDocs(TypeDocs.PROXY_CARD_NEW);
                else if (isButton(116))
                    reg.setTypeDocs(TypeDocs.PROXY_CARD_RESTORE);
                else return EXIT;
                sendFIO();
                return COMEBACK;
            case GET_FIO:
                deleteUpdateMessage();
                if (hasMessageText() && updateMessageText.split(" ").length == 3) {
                    reg.setFio(updateMessageText);
                    sendCourse();
                }
                else {
                    deleteMessages(del);
                    del = sendMessage(134);
                }
                return COMEBACK;

            case SET_COURSE:
                if (hasCallbackQuery() && (isButton(41) || isButton(42) || isButton(43) || isButton(44))) {
                    reg.setCourse(updateMessageText);
                    sendDirection();
                }
                else if(hasCallbackQuery() && isButton(16)) {
                    sendFIO();
                }
                return COMEBACK;

            case TRAINING_DIRECTION:
                if (hasCallbackQuery() && (isButton(45) || isButton(46) || isButton(47) || isButton(48) || isButton(49))) {
                    reg.setEduDirection(updateMessageText);
                    sendEduForm();
                }
                else if(hasCallbackQuery() && isButton(16)) {
                    sendCourse();

                }
                return COMEBACK;

            case EDUCATION_FORM:
                if (hasCallbackQuery() && (isButton(50) || isButton(51) || isButton(52))) {
                    reg.setEducationForm(updateMessageText);
                    sendAddress();
                }
                else if(hasCallbackQuery() && isButton(16)) {
                    sendDirection();
                }
                return COMEBACK;

            case ADDRESS:
                if (hasMessageText()) {

                    reg.setAddress(updateMessageText);

                    sendRequestPhone();
                }
                else if(hasCallbackQuery() && isButton(16)) {
                    sendEduForm();
                }
                return COMEBACK;

            case GET_PHONE_NUMBER:
                if (hasContact()) {

                    String phone = updateMessagePhone;
                    reg.setSender(userRepo.findByChatId(chatId));
                    reg.setDate(new Date());
                    reg.setPhone(phone);

                    sendDocuments();
                    del = sendMessageWithKeyboard(23, 17);
                }
                else if(hasCallbackQuery() && isButton(16)) {
                    sendAddress();
                }
        }


        return false;
    }

    private void sendFIO() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessage(36);
        waitingType = WaitingType.GET_FIO;
    }

    private void sendDocuments() throws TelegramApiException {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        try {
//            if (reg.getTypeDocs().equals(TypeDocs.STUDENT_CARD))// my docs
                sendDocument.setDocument(FileGenerator.statementLossDocs(reg));


//            else if (reg.getTypeDocs().equals(TypeDocs.CREDIT_BOOK)) // period learning
//                sendDocument.setDocument(FileGenerator.statementPeriodLearning(reg));
//            else if (reg.getTypeDocs().equals(TypeDocs.BADGE)) // period learning
//                sendDocument.setDocument(FileGenerator.statementPeriodLearning(reg));
//            else if (reg.getTypeDocs().equals(TypeDocs.PROXY_CARD_NEW)) // period learning
//                sendDocument.setDocument(FileGenerator.statementPeriodLearning(reg));
//            else if (reg.getTypeDocs().equals(TypeDocs.PROXY_CARD_RESTORE)) // period learning
//                sendDocument.setDocument(FileGenerator.statementPeriodLearning(reg));


            String fileId =  bot.execute(sendDocument).getDocument().getFileId();
            reg.setFileId(fileId);
            lossDocsRepo.save(reg);

            deleteMessages(del);
            sendMessage(136); // аявление готово

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequestPhone() throws TelegramApiException {
        deleteMessages(del);

        KeyboardService keyboardService = new KeyboardService();
        del = sendMessageWithKeyboard(getText(Const.SET_MOBILE_PHONE_NUMBER),
                keyboardService.getReplyKeyboardRequestContact(getLanguage()));
        waitingType = WaitingType.GET_PHONE_NUMBER;
    }

    private void sendAddress() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessageWithKeyboard(27, 29);      // адрес
        waitingType = WaitingType.ADDRESS;
    }

    private void sendEduForm() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessageWithKeyboard(26, 20); // форма обучения
        waitingType = WaitingType.EDUCATION_FORM;
    }

    private void sendDirection() throws TelegramApiException {
        deleteMessage(del);
        del = sendMessageWithKeyboard(25, 19); // направление подготовки
        waitingType = WaitingType.TRAINING_DIRECTION;
    }

    private void sendCourse() throws TelegramApiException {
        deleteMessage(del);

        del = sendMessageWithKeyboard(24,18); // укажите курс
        waitingType = WaitingType.SET_COURSE;
    }

}
