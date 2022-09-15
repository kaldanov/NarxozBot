package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.service.MailingThread;
import com.example.spgtu.util.type.WaitingType;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class id016_Mailing extends Command {

    private int deleteMessageId;
    private int acceptMessId;
    private String text;

    private List<InputMedia> photoVideos;
    private List<String> documents;
    User user;


    @Override
    public boolean execute() throws TelegramApiException {
        user = userRepo.findByChatId(chatId);
        if (!isRegistered() || !isAdmin()) {
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }
        switch (waitingType) {
            case START:
                deleteMessage(updateMessageId);
                deleteMessageId = sendMessage(getText(61));
                waitingType = WaitingType.SET_TEXT;
                return COMEBACK;
            case SET_TEXT:
                deleteMessage(updateMessageId);
                deleteMessage(deleteMessageId);
//                if (hasMessageText() && isButton(11)) { // cancel
//                    deleteMessage(updateMessageId);
//                    deleteMessage(deleteMessageId);
//                    sendMessageWithKeyboard(getText(35), 12);
//                    return EXIT;
//                }

//                else
                    if (hasMessageText()) {
                    text = updateMessageText;
                    deleteMessageId = sendMessageWithKeyboard(getText(62), 27);
                    waitingType = WaitingType.CHOOSE_OPTION;
                    photoVideos = new ArrayList<>();
                    documents = new ArrayList<>();
                }
                return COMEBACK;
            case CHOOSE_OPTION:
                deleteMessage(updateMessageId);
                if (hasDocument()) {
                    documents.add(update.getMessage().getDocument().getFileId());
                    deleteMessage(acceptMessId);
                    acceptMessId = sendMessage(64);
                } else if (hasPhoto()) {
                    InputMediaPhoto photo = new InputMediaPhoto()
                            .setMedia(update.getMessage().getPhoto().get(0).getFileId());
                    photoVideos.add(photo);
                    deleteMessage(acceptMessId);
                    acceptMessId = sendMessage(64);

                } else if (hasVideo()) {
                    InputMediaVideo video = new InputMediaVideo()
                            .setMedia(update.getMessage().getVideo().getFileId());
                    photoVideos.add(video);
                    deleteMessage(acceptMessId);
                    acceptMessId = sendMessage(64);

                } else if (isButton(76)) { // back
                    deleteMessage(updateMessageId);
                    deleteMessage(deleteMessageId);
                    deleteMessageId = sendMessage(getText(61));
                    waitingType = WaitingType.SET_TEXT;
                } else if (isButton(65)) { // next
                    deleteMessage(acceptMessId);
                    deleteMessage(deleteMessageId);
                    if (documents != null && documents.size() > 0) {
                        for (String document : documents) {
                            sendDocument(document, chatId);
                        }
                    }
                    if (photoVideos != null && photoVideos.size() > 0) {
                        sendMediaGroup(photoVideos, chatId);
                    }

                    sendMessage(68);
                    deleteMessageId = sendMessageWithKeyboard(text, 28);
                    waitingType = WaitingType.CONFIRM;

                }
//                else if (isButton(11)) { // cancel
//                    deleteMessage(updateMessageId);
//                    deleteMessage(deleteMessageId);
//                    deleteMessage(acceptMessId);
//                    sendMessageWithKeyboard(getText(35), 12);
//                    return EXIT;
//                }
                return COMEBACK;
            case CONFIRM:
                deleteMessage(updateMessageId);
//                deleteMessage(deleteMessageId);
                if (isButton(507)) {
                    sendMessage(getText(63));
                    List<User> users = userRepo.findAll();

                    MailingThread mailingThread = new MailingThread(bot, users, documents, photoVideos, text);
                    mailingThread.start();

                    return EXIT;
                } else if (isButton(76)) {
                    deleteMessageId = sendMessageWithKeyboard(getText(62), 27);
                    waitingType = WaitingType.CHOOSE_OPTION;
                }
//                else if (isButton(11)) {
//                    deleteMessage(updateMessageId);
//                    sendMessageWithKeyboard(getText(35), 12);
//                    return EXIT;
//                }
                else {
                    deleteMessage(deleteMessageId);
                    deleteMessage(updateMessageId);
                    deleteMessageId = sendMessageWithKeyboard(text, 28);
                    waitingType = WaitingType.CONFIRM;
                }
                return COMEBACK;
        }
        return EXIT;
    }
}
