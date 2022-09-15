package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.custom.Onay;
import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.service.KeyboardService;
import com.example.spgtu.util.Const;
import com.example.spgtu.util.DateKeyboard;
import com.example.spgtu.util.DateUtil;
import com.example.spgtu.util.type.WaitingType;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class id012_RequestOnay extends Command {
    User user;
    Onay onay;

    private int deleteId;
    private int deleteIdWrong;
    List<Message> delMesss;
    private DateKeyboard dateIssue;
    private DateKeyboard dateEnd = new DateKeyboard();

    @Override
    public boolean execute() throws TelegramApiException {
        if (!isRegistered() || !userRepo.findByChatId(chatId).isStudent()) {
            deleteUpdateMessage();
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }
        switch (waitingType) {
            case START:
                user = userRepo.findByChatId(chatId);
                deleteAllMessage();
                Onay onay777 = onayRepository.findBySenderChatId(chatId);
                if(onay777 != null){
                    sendOnayInfo(onay777);
                    return EXIT;
                }
                onay = new Onay();
                sendFIO();
                return COMEBACK;
            case GET_FIO:
                deleteUpdateMessage();
                if (hasMessageText() && updateMessageText.split(" ").length == 3) {
                    onay.setFullName(updateMessageText);
                    sendYesOrNo();
                }
                else {
                    deleteMessages(deleteId);
                    deleteId = sendMessage(134);
                }
                return COMEBACK;
            case YES_OR_NO:
                onay.setSender(user);
                if (isButton(94)) {   //  получал
                    deleteAllMessage();
                    deleteId = sendMessageWithKeyboard(getText(114), 35);
                    waitingType = WaitingType.SELECT_EDUCATION;
                } else if (isButton(95)) {  // не получал
                    onay.setGotOnay("Не получал");
                    deleteAllMessage();
                    sendCardId();
                }
                else {
                    sendWrongData();
                }
                return COMEBACK;
            case SELECT_EDUCATION:
                if (isButton(103)) {
                    onay.setGotOnay("Колледж/ВУЗ");
                    deleteAllMessage();
                    sendRequestPhone();
                } else if (isButton(104)) {
                    onay.setGotOnay("Школа");
                    deleteAllMessage();
                    sendRequestPhone();
                } else if (hasCallbackQuery() && isButton(16)) { // todo back
                    deleteAllMessage();
                    sendYesOrNo();
                }  else {
                    sendWrongData();
                }
                return COMEBACK;
            case SET_PHONE_NUMBER:
                if (hasContact()) {
                    sendMessageWithKeyboard(4, 2); // главное меню студента

                    String phone = updateMessagePhone;
                    onay.setDate(new Date());
                    onay.setPhone(phone);

                    deleteAllMessage();
                    sendCardId();
                }
                else if(hasCallbackQuery() && isButton(16)) {
                    deleteId = sendMessageWithKeyboard(getText(114), 35);
                    waitingType = WaitingType.SELECT_EDUCATION;
                }
                return COMEBACK;
            case SET_CARD_ID:
                if (hasCallbackQuery() && isButton(16)) {
                    deleteAllMessage();
                    sendRequestPhone();
                }
                else if (hasMessageText()) {
                    if (updateMessageText.length() != 9) {
                        sendWrongData();
                    } else if (isDigit(updateMessageText)) {
                        sendWrongData();
                    } else {
                        deleteAllMessage();
                        onay.setCardId(updateMessageText);
                        sendIIN();
                    }
                } else {
                    sendWrongData();
                }
                return COMEBACK;
            case SET_IIN:
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, -4);
                dateIssue = new DateKeyboard(calendar);

                if (hasCallbackQuery() && isButton(16)) { // todo back
                    deleteAllMessage();
                    sendCardId();
                }
                else if (hasMessageText()) {
                    if (updateMessageText.length() != 12) {
                        sendWrongData();
                    } else if (isDigit(updateMessageText)) {
                        sendWrongData();
                    } else {
                        deleteAllMessage();
                        onay.setIin(updateMessageText);
                        sendDateIssue();
                    }
                } else {
                    sendWrongData();
                }
                return COMEBACK;
            case DATE_ISSUE:
                if (hasCallbackQuery() && isButton(16)) {
                    deleteAllMessage();
                    sendIIN();
                }
                else if (hasCallbackQuery()) {
                    try {
                        if (dateIssue.isNext(updateMessageText)){
                            editMessageWithKeyboard(getText(116), deleteId, dateIssue.getWeekCalendarKeyboard());
                            return COMEBACK;
                        }
                        Date date = dateIssue.getDateDate(updateMessageText);
                        onay.setDateIssue(date);
                        deleteAllMessage();
                        sendDateEnd();
                    } catch (Exception e) {
                        sendWrongData();
                    }
                } else {
                    sendWrongData();
                }
                return COMEBACK;
            case DATE_END:
                if (hasCallbackQuery() && isButton(16)) {
                    deleteAllMessage();
                    sendDateIssue();
                }
                else if (hasCallbackQuery()) {
                    Date date = null;
                    try {
                        if (dateEnd.isNext(updateMessageText)){
                            editMessageWithKeyboard(getText(117), deleteId, dateEnd.getWeekCalendarKeyboard());
                            return COMEBACK;
                        }

                        date = dateEnd.getDateDate(updateMessageText);
                        deleteAllMessage();
                        sendIssuedBy();
                        onay.setDateEnd(date);
                    } catch (Exception e) {
                        sendWrongData();
                    }
                } else {
                    sendWrongData();
                }
                return COMEBACK;
            case ISSUED_BY:
                if (hasCallbackQuery() && isButton(16)) {
                    deleteAllMessage();
                    sendDateEnd();
                }
                else if (hasCallbackQuery() &&  isButton(30) || isButton(31) || isButton(77)|| isButton(78) ) {
                    onay.setIssuedBy(updateMessageText);
                    deleteAllMessage();
                    sendPhotoFrontCard();
                } else {
                    sendWrongData();
                }
                return COMEBACK;
            case PHOTO_CARD:
                if (hasCallbackQuery() && isButton(16)) {
                    deleteAllMessage();
                    sendIssuedBy();
                    updateMessageText = "";
                }
                else if (hasPhoto()) {
                    deleteAllMessage();
                    onay.setCardUrlFront(update.getMessage().getPhoto().get(0).getFileId());
                    sendCardPhotoBack();
                }
//                else if (hasDocument()) {
//                    deleteAllMessage();
//                    onay.setCardUrlFront(update.getMessage().getDocument().getFileId());
//                    sendCardPhotoBack();
//
//                }
                else {
                    sendWrongData();
                }
                return COMEBACK;
            case PHOTO_CARD_BACK:
                if (hasCallbackQuery() && isButton(16)) {
                    deleteAllMessage();
                    sendPhotoFrontCard();
                    updateMessageText = "";
                }
                else if (hasPhoto()) {
                    deleteAllMessage();
                    onay.setCardUrlBack(update.getMessage().getPhoto().get(0).getFileId());
                    send3x4Photo();
                }
                else {
                    sendWrongData();
                }
                return COMEBACK;
            case PHOTO:
                if (hasCallbackQuery() && isButton(16)) {
                    deleteAllMessage();
                    sendCardPhotoBack();
                    updateMessageText = "";
                }
                else if (hasPhoto()) {
                    deleteAllMessage();
                    onay.setPhotoUrl(update.getMessage().getPhoto().get(0).getFileId());
                    onay.setDate(new Date());
//                    onay.setFullName(user.getFullName());
//                    onay.setPhone(user.getPhone());

                    InputMediaPhoto frontCard = new InputMediaPhoto();
                    frontCard.setCaption(getText(123)).setParseMode("html").setMedia(onay.getCardUrlFront());

                    InputMediaPhoto backCard = new InputMediaPhoto();
                    backCard.setCaption(getText(124)).setParseMode("html").setMedia(onay.getCardUrlBack());

                    InputMediaPhoto photo_3_4 = new InputMediaPhoto();
                    photo_3_4.setCaption(getText(125)).setParseMode("html").setMedia(onay.getPhotoUrl());
//todo im stopped in here
                    List<InputMedia> photos = new ArrayList<>();
                    photos.add(frontCard);
                    photos.add(backCard);
                    photos.add(photo_3_4);

                    SendMediaGroup sendMediaGroup = new SendMediaGroup();
                    sendMediaGroup.setChatId(chatId).setMedia(photos);

                    delMesss = bot.execute(sendMediaGroup);

                    sendConfirm();
                } else {
                    sendWrongData();
                }
                return COMEBACK;
            case CONFIRM:
                if (isButton(16)){
                    deleteAllMessage();
                    for (Message message74 : delMesss){
                        deleteMessage(message74.getMessageId());
                    }
                    send3x4Photo();
                    updateMessageText = "";
                    return COMEBACK;
                }
                if (hasCallbackQuery()){
                    if (isButton(507)){
                        deleteAllMessage();
                        for (Message message74 : delMesss){
                            deleteMessage(message74.getMessageId());
                        }
                        String str = getText(126) + " " + user.getUserName() + " " + getText(127);
                        sendMessage(str);
                        onayRepository.save(onay);
                        return EXIT;
                    }
                    else {
                        sendWrongData();
                    }
                }
                else {
                    sendWrongData();
                }
                return COMEBACK;
        }
        return EXIT;
    }

    private void sendFIO() throws TelegramApiException {
        deleteMessages(deleteId);
        deleteId = sendMessage(36);
        waitingType = WaitingType.GET_FIO;
    }

    private void sendRequestPhone() throws TelegramApiException {
        deleteMessages(deleteId);

        KeyboardService keyboardService = new KeyboardService();
        deleteId = sendMessageWithKeyboard(getText(Const.SET_MOBILE_PHONE_NUMBER),
                keyboardService.getReplyKeyboardRequestContact(getLanguage()));
        waitingType = WaitingType.SET_PHONE_NUMBER;
    }

    private void sendConfirm() throws TelegramApiException {
        deleteId = sendMessageWithKeyboard(getInfoOnay(onay), 36);
        waitingType = WaitingType.CONFIRM;
    }

    private void send3x4Photo() throws TelegramApiException {
        deleteId = sendMessageWithKeyboard(getText(120), 29);
        waitingType = WaitingType.PHOTO;
    }

    private void sendCardPhotoBack() throws TelegramApiException {
        deleteId = sendMessageWithKeyboard(getText(122), 29);
        waitingType = WaitingType.PHOTO_CARD_BACK;
    }

    private void sendIssuedBy() throws TelegramApiException {
        deleteId = sendMessageWithKeyboard(getText(118), 14);
        waitingType = WaitingType.ISSUED_BY;
    }

    private void sendPhotoFrontCard() throws TelegramApiException {
        deleteId = sendMessageWithKeyboard(getText(119),29);
        waitingType = WaitingType.PHOTO_CARD;
    }

    private void sendDateEnd() throws TelegramApiException {
        deleteId = sendMessageWithKeyboard(getText(117),dateEnd.getWeekCalendarKeyboard());
        waitingType = WaitingType.DATE_END;
    }

    private void sendDateIssue() throws TelegramApiException {
        deleteId = sendMessageWithKeyboard(116,dateIssue.getWeekCalendarKeyboard());
        waitingType = WaitingType.DATE_ISSUE;
    }

    private void sendIIN() throws TelegramApiException {
        deleteId = sendMessageWithKeyboard(getText(121), 29);
        waitingType = WaitingType.SET_IIN;
    }

    private void sendCardId() throws TelegramApiException {
        deleteId = sendMessageWithKeyboard(getText(115),29);
        waitingType = WaitingType.SET_CARD_ID;

    }

    private void sendYesOrNo() throws TelegramApiException {
        deleteId = sendMessageWithKeyboard(getText(113), 32);
        waitingType = WaitingType.YES_OR_NO;
    }

    private void sendOnayInfo(Onay onay777) throws TelegramApiException {
        InputMediaPhoto frontCard = new InputMediaPhoto();
        frontCard.setCaption(getText(123)).setParseMode("html").setMedia(onay777.getCardUrlFront());

        InputMediaPhoto backCard = new InputMediaPhoto();
        backCard.setCaption(getText(124)).setParseMode("html").setMedia(onay777.getCardUrlBack());

        InputMediaPhoto photo_3_4 = new InputMediaPhoto();
        photo_3_4.setCaption(getText(125)).setParseMode("html").setMedia(onay777.getPhotoUrl());

        List<InputMedia> photos = new ArrayList<>();
        photos.add(frontCard);
        photos.add(backCard);
        photos.add(photo_3_4);

        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(chatId).setMedia(photos);

        delMesss = bot.execute(sendMediaGroup);
        sendMessage(getInfoOnay(onay777));

    }

    private String getInfoOnay(Onay onay777) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getText(128)).append(" ").append(onay777.getCardId()).append(next)
                .append(getText(129)).append(" ").append(onay777.getIin()).append(next)
                .append(getText(130)).append(" ").append(onay777.getGotOnay()).append(next)
                .append(getText(131)).append(" ").append(onay777.getIssuedBy()).append(next)
                .append(getText(132)).append(" ").append(DateUtil.getDayDate(onay777.getDateIssue())).append(next)
                .append(getText(133)).append(" ").append(DateUtil.getDayDate(onay777.getDateEnd())).append(next);

        return stringBuilder.toString();
    }

//    private String getAllInfo() {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(getText(167)).append(" ").append(onay.getCardId()).append(next)
//                .append(getText(168)).append(" ").append(onay.getIin()).append(next)
//                .append(getText(169)).append(" ").append(onay.getGotOnay()).append(next)
//                .append(getText(170)).append(" ").append(onay.getIssuedBy()).append(next)
//                .append(getText(171)).append(" ").append(DateUtil.getDayDate(onay.getDateIssue())).append(next)
//                .append(getText(172)).append(" ").append(DateUtil.getDayDate(onay.getDateEnd())).append(next);
//
//        return stringBuilder.toString();
//    }

    private boolean isDigit(String s) {
        try {
            Long.parseLong(s);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private void deleteAllMessageWithoutKeyboard() {
        deleteMessage(updateMessageId);
        deleteMessage(deleteIdWrong);
    }

    private void deleteAllMessage() {
        deleteMessage(updateMessageId);
        deleteMessage(deleteId);
        deleteMessage(deleteIdWrong);
    }

    private void sendWrongData() throws TelegramApiException {
        deleteMessage(updateMessageId);
        if (deleteIdWrong != 0)
            deleteMessage(deleteIdWrong);
        deleteIdWrong = sendMessage(4, chatId);

    }
}
