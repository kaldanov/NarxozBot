package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.custom.Dorm;
import com.example.spgtu.dao.entities.custom.DormRegistration;
import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.dao.enums.Gender;
import com.example.spgtu.service.FileGenerator;
import com.example.spgtu.util.ButtonsLeaf;
import com.example.spgtu.util.DateKeyboard;
import com.example.spgtu.util.DateUtil;
import com.example.spgtu.util.type.WaitingType;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class id015_BookingInAdmin extends Command {
    private int del;
    User user;
    Dorm dorm;
    DormRegistration dormRegistrationForDelete;
    DormRegistration dormRegistrationForAdd;
    DateKeyboard dateIssue = new DateKeyboard();


    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        deleteUpdateMessage();


        user = userRepo.findByChatId(chatId);
        if (!isRegistered() || !isAdmin()) {
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }

        switch (waitingType){
            case START:
                sendAllDorms();
                return COMEBACK;
            case CHOOSE_DORM:
                if (hasCallbackQuery() && dormRepo.findById(getLong(updateMessageText)) != null){
                    dorm = dormRepo.findById(getLong(updateMessageText));
                    sendDormInfo(); //->CHOOSE_OPTION
                }
                return COMEBACK;
            case CHOOSE_OPTION:
                if (hasCallbackQuery() && isButton(16)){
                    sendAllDorms();
                }
                else if (hasCallbackQuery() && isButton(119)){
                    sendDormUsers(); //-> CHOOSE_WHO
                }
                else if (hasCallbackQuery() && isButton(120)){
                    sendMessageWithKeyboard(152, 32);
                    waitingType = WaitingType.CONFIRM2;
                }
                return COMEBACK;

            case CHOOSE_WHO:
                if(hasCallbackQuery() && dormRegistrationRepo.findById(getLong(updateMessageText)) != null){
                    dormRegistrationForDelete = dormRegistrationRepo.findById(getLong(updateMessageText));

                    sendUserInfo(); //-> CONFIRM
                }
                else if (hasCallbackQuery() && isButton(16)){
                    sendDormInfo();
                }
                return COMEBACK;
            case CONFIRM:
                if (hasCallbackQuery() && isButton(121)){
                    dormRegistrationForDelete.setArchive(true);
                    Dorm dorm667 = dormRegistrationForDelete.getDorm();
                    dorm667.setFullRoom(false);
                    dormRepo.save(dorm667);
                    dormRegistrationRepo.save(dormRegistrationForDelete);

                    sendDormUsers();
                }
                else if (hasCallbackQuery() && isButton(16)){
                    sendDormUsers();
                }
                return COMEBACK;

            case CONFIRM2:
                if (hasCallbackQuery() && isButton(94)){ // da
                    dormRegistrationForAdd = new DormRegistration();
                    dormRegistrationForAdd.setDateStart(new Date());
                    dormRegistrationForAdd.setDorm(dorm);
                    dormRegistrationForAdd.setGender(dorm.getGender());
                    dormRegistrationForAdd.setSender(userRepo.findByChatId(chatId));

                    sendFIO();

                }
                else if (hasCallbackQuery() && isButton(95)){ // no
                    sendDormInfo();
                }
                return COMEBACK;

            case GET_FIO:
                deleteUpdateMessage();
                if (hasMessageText() && updateMessageText.split(" ").length == 3) {
                    dormRegistrationForAdd.setFio(updateMessageText);
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
                    dormRegistrationForAdd.setIIN(updateMessageText);
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
                    dormRegistrationForAdd.setAddress(updateMessageText);
                    sendCardId();
                    return COMEBACK;
                }
                else if(hasCallbackQuery() && isButton(16)){
                    sendIIN();
                }
                return COMEBACK;
            case SET_CARD_ID:
                deleteUpdateMessage();
                if (isLong(updateMessageText) && updateMessageText.length() == 9){
                    dormRegistrationForAdd.setCardId(updateMessageText);
                    sendDateIssue();
                }
                else if(hasCallbackQuery() && isButton(16)){
                    sendAddress();
                }
                else{
                    deleteMessages(del);
                    del = sendMessage(45);
                    del = sendMessage(41);
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
                    dormRegistrationForAdd.setDateIssueCard(dateIssue.getDateDate(updateMessageText));
                    sendConfirm();
                }
                return COMEBACK;
            case CONFIRM_DATE:
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
                    dormRegistrationForAdd.setWhoGiveCard(updateMessageText);
                    sendPeriod();
                }
                else if(hasCallbackQuery() && isButton(16)) {
                    sendConfirm();
                }
                return COMEBACK;
            case SET_PERIOD:
                if (hasCallbackQuery() && (isButton(133) || isButton(134) || isButton(135) || isButton(136))){
                    if (isButton(133)){
                        dormRegistrationForAdd.setCountMonth(1);
                    }
                    else if (isButton(134)){
                        dormRegistrationForAdd.setCountMonth(2);

                    }
                    else if (isButton(135)){
                        dormRegistrationForAdd.setCountMonth(5);
                    }
                    else if (isButton(136)){
                        dormRegistrationForAdd.setCountMonth(10);
                    }

                    dormRegistrationRepo.save(dormRegistrationForAdd);
                    if (dorm.getCountPlaces() == dormRegistrationRepo.findAllByDormAndArchiveFalse(dorm).size()){
                        dorm.setFullRoom(true);
                        dormRepo.save(dorm);
                    }
                    sendMessage(172);
                    sendDormInfo();
                }
                else if (hasCallbackQuery() && isButton(16)){
                    sendWhoGive();
                }
                return COMEBACK;

        }

        return COMEBACK;

    }

    private void sendUserInfo() throws TelegramApiException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getText(147)).append(" ").append(dormRegistrationForDelete.getFio()).append(next)
                .append(getText(148)).append(" ").append(dormRegistrationForDelete.getIIN()).append(next)
                .append(getText(149)).append(" ").append(dormRegistrationForDelete.getAddress()).append(next)
                .append(getText(150)).append(" ").append(dormRegistrationForDelete.getCourse()).append(next)
                .append(getText(151)).append(" ").append(DateUtil.getDayDate1(dormRegistrationForDelete.getDateStart()));

        sendMessageWithKeyboard(stringBuilder.toString(), 44);
        waitingType = WaitingType.CONFIRM;
    }

    private void sendDormInfo() throws TelegramApiException {
        StringBuilder dormInfo = new StringBuilder();
        dormInfo.append(getText(142)).append(dorm.getId()).append(next)
                .append(getText(143)).append(" ").append(dorm.getCountPlaces()).append(next)
                .append(getText(144)).append(" ").append(dormRegistrationRepo.findAllByDormAndArchiveFalse(dorm).size()).append(next)
                .append(getText(145)).append(" ").append(dorm.getGender() != null? dorm.getGender().getVal(): "Не выбрано");

        if (dormRegistrationRepo.findAllByDormAndArchiveFalse(dorm).size() == dorm.getCountPlaces()){
            sendMessageWithKeyboard(dormInfo.toString(), 41);
        }
        else if (dormRegistrationRepo.findAllByDormAndArchiveFalse(dorm).size() == 0){
            sendMessageWithKeyboard(dormInfo.toString(), 42);
        }
        else{
            sendMessageWithKeyboard(dormInfo.toString(), 43);
        }
        waitingType = WaitingType.CHOOSE_OPTION;
    }

    private void sendDormUsers() throws TelegramApiException {
        List<DormRegistration> forToFrees = dormRegistrationRepo.findAllByDormAndArchiveFalse(dorm);
        List<String> names = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        forToFrees.forEach(forToFree->{
            names.add(forToFree.getFio());
            ids.add(String.valueOf(forToFree.getId()));
        });
        names.add(getButtonText(16));
        ids.add(getButtonText(16));

        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(names, ids);
        sendMessageWithKeyboard(146, buttonsLeaf.getListButtonWithIds());
        waitingType = WaitingType.CHOOSE_WHO;
    }

    private void sendAllDorms() throws TelegramApiException {
        List<Dorm> dorms = dormRepo.findAllByOrderById();
        List<String> names = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        dorms.forEach(dorm -> {
            names.add( "№ " + dorm.getId());
            ids.add(String.valueOf(dorm.getId()));
        });
        ButtonsLeaf dormLeaf = new ButtonsLeaf(names, ids);
        sendMessageWithKeyboard(141, dormLeaf.getListButtonWithIds());
        waitingType = WaitingType.CHOOSE_DORM;
    }

    private void sendPeriod() throws TelegramApiException {
        deleteMessage(del);
        deleteUpdateMessage();
        del = sendMessageWithKeyboard(171,50 );
        waitingType = WaitingType.SET_PERIOD;

    }

    private void sendWhoGive() throws TelegramApiException {
        deleteMessage(del);
        del = sendMessageWithKeyboard(118, 14);
        waitingType = WaitingType.WHO_GIVE;
    }

    private void sendConfirm() throws TelegramApiException {
        deleteMessage(del);
        del = sendMessageWithKeyboard(getText(82) + " " + DateUtil.getDayDate1(dormRegistrationForAdd.getDateIssueCard()), 36);
        waitingType = WaitingType.CONFIRM_DATE;
    }

    private void sendDateIssue() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessageWithKeyboard(57, dateIssue.getWeekCalendarKeyboard());
        waitingType = WaitingType.DATE_ISSUE;
    }



    private void sendGender() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessageWithKeyboard(29, 22);
        waitingType = WaitingType.GENDER;
    }



    private void sendCardId() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessageWithKeyboard(41, 29);
        waitingType = WaitingType.SET_CARD_ID;
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
