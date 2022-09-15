package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.custom.Dorm;
import com.example.spgtu.dao.entities.custom.DormRegistration;
import com.example.spgtu.dao.enums.Gender;
import com.example.spgtu.service.FileGenerator;
import com.example.spgtu.util.DateKeyboard;
import com.example.spgtu.util.DateUtil;
import com.example.spgtu.util.type.WaitingType;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class id013_Booking extends Command {
    private int del;
    private Map<Long, Boolean> selectedIds = new HashMap<>();
    private DormRegistration dormRegistration;
    private int del1;
    DateKeyboard dateIssue = new DateKeyboard();


    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        if (!isRegistered() || !userRepo.findByChatId(chatId).isStudent()) {
            deleteUpdateMessage();
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }


        switch (waitingType) {
            case START:
                List<Dorm> dorms = dormRepo.findByFullRoomIsFalse();
                if (dorms.size() == 0) {
                    deleteMessages(del);
                    sendMessage(43);
                    return EXIT;
                }
                if (dormRegistrationRepo.findBySenderChatId(chatId) != null){
                    dormRegistration = dormRegistrationRepo.findBySenderChatId(chatId);
                    sendDocuments();
                    return EXIT;
                }

                dormRegistration = new DormRegistration();
                dormRegistration.setSender(userRepo.findByChatId(chatId));
                dormRegistration.setDateStart(new Date());
                sendGender();
                return COMEBACK;

            case GENDER:
                deleteUpdateMessage();
                if (hasCallbackQuery() && ( isButton(55) || isButton(56))) {
                    if (isButton( 55))
                        dormRegistration.setGender(Gender.MALE);
                    else dormRegistration.setGender(Gender.FEMALE);

                    sendFIO();

                }
                return COMEBACK;

            case GET_FIO:
                deleteUpdateMessage();
                if (hasMessageText() && updateMessageText.split(" ").length == 3) {
                    dormRegistration.setFio(updateMessageText);
                    sendIIN();
                }
                else if(hasCallbackQuery() && isButton(16)){
                    sendGender();
                }
                else {
                    deleteMessages(del);
                    del = sendMessageWithKeyboard(134, 29);
                }
                return COMEBACK;
            case GET_IIN:
                deleteUpdateMessage();
                if (hasMessageText() && isIIN(updateMessageText)) {
                    dormRegistration.setIIN(updateMessageText);
                    sendAddress();
                }
                else if(hasCallbackQuery() && isButton(16)){
                    sendFIO();
                }
                else  sendMessage(50);
                return COMEBACK;
            case ADDRESS:
                deleteUpdateMessage();
                if (hasMessageText()) {
                    dormRegistration.setAddress(updateMessageText);
                    sendDirection();
                    return COMEBACK;
                }
                else if(hasCallbackQuery() && isButton(16)){
                    sendIIN();
                }
                return COMEBACK;
            case EDU_DIRECTION:
                deleteUpdateMessage();
                if (hasCallbackQuery() &&(isButton(45) || isButton(46) || isButton(47) || isButton(48) || isButton(49))) {
                    dormRegistration.setEduDirection(updateMessageText);
                    deleteMessages(del);
                    sendSelectCourse();
                }
                else if(hasCallbackQuery() && isButton(16)){
                    sendAddress();
                }
                return COMEBACK;
            case SELECT_COURSE:
                deleteUpdateMessage();
                if (hasCallbackQuery() && (isButton(41) || isButton(42) || isButton(43) || isButton(44))) {
                    dormRegistration.setCourse(updateMessageText);
                    sendEducationForm();
                    return COMEBACK;
                }
                else if(hasCallbackQuery() && isButton(16)){
                    sendDirection();
                }
                return COMEBACK;
            case EDUCATION_FORM:
                deleteUpdateMessage();
                if (hasCallbackQuery() && (isButton(50) || isButton(51) || isButton(52))) {
                    dormRegistration.setEducationForm(updateMessageText);
                    sendCardId();
                }
                else if(hasCallbackQuery() && isButton(16)){
                    sendSelectCourse();
                }
                else{
                    deleteMessages(del);
                    del = sendMessage(45);
                    del1 = sendMessageWithKeyboard(40, 20);
                    waitingType = WaitingType.EDUCATION_FORM;
                }
                return COMEBACK;
            case SET_CARD_ID:
                deleteUpdateMessage();
                if (isLong(updateMessageText) && updateMessageText.length() == 9){
                    dormRegistration.setCardId(updateMessageText);
                    sendDateIssue();
                }
                else if(hasCallbackQuery() && isButton(16)){
                    sendEducationForm();
                }
                else{
                    deleteMessages(del);
                    deleteMessages(del1);
                    del = sendMessage(45);
                    del1 = sendMessage(41);
                    waitingType = WaitingType.SET_CARD_ID;
                }
                return COMEBACK;
            case DATE_ISSUE:
                if (hasCallbackQuery() && dateIssue.isNext(updateMessageText)){
                    editMessageWithKeyboard(getText(57), updateMessageId , dateIssue.getWeekCalendarKeyboard());
                    return COMEBACK;
                }
                else if(hasCallbackQuery() && isButton(16)) {
                    deleteUpdateMessage();
                    sendCardId();
                }
                else if (hasCallbackQuery() && dateIssue.getDateDate(updateMessageText)!= null){
                    deleteUpdateMessage();
                    dormRegistration.setDateIssueCard(dateIssue.getDateDate(updateMessageText));
                    sendConfirm();
                }
                return COMEBACK;
            case CONFIRM:
                deleteUpdateMessage();
                if (hasCallbackQuery() && isButton(507)){
                    sendWhoGive();
                }
                else if(hasCallbackQuery() && isButton(16)) {
                    sendDateIssue();
                }
                return COMEBACK;
            case WHO_GIVE:
                deleteUpdateMessage();
                if (hasCallbackQuery() && (isButton(30) ||isButton(31) ||isButton(77) ||isButton(78))){
                    dormRegistration.setWhoGiveCard(updateMessageText);
                    sendPeriod();
                }
                else if(hasCallbackQuery() && isButton(16)) {
                    sendConfirm();
                }
                return COMEBACK;
            case SET_PERIOD:
                if (hasCallbackQuery() && (isButton(133) || isButton(134) || isButton(135) || isButton(136))){
                    if (isButton(133)){
                        dormRegistration.setCountMonth(1);
                    }
                    else if (isButton(134)){
                        dormRegistration.setCountMonth(2);

                    }
                    else if (isButton(135)){
                        dormRegistration.setCountMonth(5);
                    }
                    else if (isButton(136)){
                        dormRegistration.setCountMonth(10);
                    }

                    sendSelectTypeRoom();
                }
                else if (hasCallbackQuery() && isButton(16)){
                    sendWhoGive();
                }
                return COMEBACK;
            case SELECT_ROOM:
                deleteUpdateMessage();
                if (hasCallbackQuery() && (isButton(57) || isButton(58))){
                    if (isButton(57)){
                        if (isFreeDorm(2)){
                            sendDocuments();
                            return EXIT;
                        }
                        else{
                            deleteMessage(del);
                            del = sendMessageWithKeyboard(135,23);
                        }
                    }
                    else if(isButton(58)){
                        if (isFreeDorm(3)){
                            sendDocuments();
                            return EXIT;
                        }
                        else{
                            deleteMessage(del);
                            del = sendMessageWithKeyboard(135,23);
                        }
                    }
                }
                else if(hasCallbackQuery() && isButton(16)) {
                    sendPeriod();
                }
                return COMEBACK;

        }
        return false;
    }

    private void sendPeriod() throws TelegramApiException {
        deleteMessage(del);
        deleteUpdateMessage();
        del = sendMessageWithKeyboard(171,50 );
        waitingType = WaitingType.SET_PERIOD;

    }

    private void sendSelectTypeRoom() throws TelegramApiException {
        deleteMessage(del);
        deleteUpdateMessage();
        del = sendMessageWithKeyboard(30, 23);
        waitingType = WaitingType.SELECT_ROOM;
    }

    private void sendWhoGive() throws TelegramApiException {
        deleteMessage(del);
        del = sendMessageWithKeyboard(118, 14);
        waitingType = WaitingType.WHO_GIVE;
    }

    private void sendConfirm() throws TelegramApiException {
        deleteMessage(del);
        del = sendMessageWithKeyboard(getText(82) + " " + DateUtil.getDayDate1(dormRegistration.getDateIssueCard()), 36);
        waitingType = WaitingType.CONFIRM;
    }

    private void sendDateIssue() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessageWithKeyboard(57, dateIssue.getWeekCalendarKeyboard());
        waitingType = WaitingType.DATE_ISSUE;
    }

    private void sendEducationForm() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessageWithKeyboard(40, 20);
        waitingType = WaitingType.EDUCATION_FORM;
    }

    private void sendSelectCourse() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessageWithKeyboard(39, 18);
        waitingType = WaitingType.SELECT_COURSE;
    }

    private void sendGender() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessageWithKeyboard(29, 22);
        waitingType = WaitingType.GENDER;
    }




    private void sendDocuments() throws TelegramApiException {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId).setDocument(FileGenerator.requestDorm(dormRegistration));
        String statement =  bot.execute(sendDocument).getDocument().getFileId();

        SendDocument sendDocument2 = new SendDocument();
        try {
             sendDocument2.setChatId(chatId).setDocument(FileGenerator.getContractDorm(dormRegistration));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String contract =  bot.execute(sendDocument2).getDocument().getFileId();


        dormRegistration.setStatementFileId(statement);
        dormRegistration.setContractFileId(contract);
        dormRegistrationRepo.save(dormRegistration);
        sendMessage(32);
    }

    private boolean isFreeDorm(int i) {
        List<Dorm> freeDorms = dormRepo.findByFullRoomIsFalseAndCountPlacesOrderById(i);
        for (Dorm oneDorm : freeDorms){
            if (oneDorm.getGender() == null || oneDorm.getGender().equals(dormRegistration.getGender())) {
                dormRegistration.setDorm(oneDorm);
                if (oneDorm.getCountPlaces() == dormRegistrationRepo.findAllByDormAndArchiveFalse(oneDorm).size()){
                    oneDorm.setFullRoom(true);
                }
                oneDorm.setGender(dormRegistration.getGender());
                dormRepo.save(oneDorm);
                dormRegistration = dormRegistrationRepo.save(dormRegistration);
                return true;
            }
        }
        return false;
    }

    private void sendCardId() throws TelegramApiException {
        deleteMessages(del);
        deleteMessages(del1);
        del = sendMessageWithKeyboard(41, 29);
        waitingType = WaitingType.SET_CARD_ID;
    }

    private void sendDirection() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessageWithKeyboard(38, 19);
        waitingType = WaitingType.EDU_DIRECTION;
    }

    private void sendAddress() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessageWithKeyboard(37, 29);
        waitingType = WaitingType.ADDRESS;
    }

    private void sendIIN() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessageWithKeyboard(46, 29);
        waitingType = WaitingType.GET_IIN;
    }

    private void sendFIO() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessageWithKeyboard(36, 29);
        waitingType = WaitingType.GET_FIO;
    }




}
