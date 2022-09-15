package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.custom.RequestApplicant;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class id022_ODPHandleRequest extends Command {

    RequestApplicant requestApplicant;
    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        if (!isRegistered() || !userRepo.findByChatId(chatId).isOdp()) {
            deleteUpdateMessage();
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }
        if (hasCallbackQuery() && isButton2("92")){ //accept
            requestApplicant = getRequest();
            if (requestApplicant!= null) {
                requestApplicant.setAccepted(true);
                requestApplicantRepo.save(requestApplicant);
            }
            sendRef();
        }

        else if (hasCallbackQuery() && isButton2("93")){ //reject
            deleteUpdateMessage();
            sendMessage(97);
            requestApplicant = getRequest();
            return COMEBACK;
        }
        else if (hasMessageText()){
            deleteUpdateMessage();

            if (requestApplicant!= null) {
                sendMessage(98);
                sendMessage(getText(99) +" "+ updateMessageText, requestApplicant.getPreparationCourses().getSender().getChatId());
                requestApplicant.setAccepted(false);
                requestApplicantRepo.save(requestApplicant);
            }
        }


        return EXIT;
    }

    private RequestApplicant getRequest() {
        String data = update.getCallbackQuery().getData();
        return requestApplicantRepo.findById(Long.parseLong(data.split(";")[1]));
    }

    private boolean isButton2(String s) {
        String data = update.getCallbackQuery().getData();
        if (data.split(";").length ==3) {
            return data.split(";")[2].equals(s);
        }
        return false;
    }

    private void sendRef() {
        String mess = "";
        mess += getText(95) + next;
        try {
            if (requestApplicant != null){

            SendDocument sendDocument = new SendDocument();
            sendDocument.setCaption(mess).setDocument(update.getCallbackQuery().getMessage().getDocument().getFileId()).setChatId(requestApplicant.getPreparationCourses().getSender().getChatId());
            requestApplicant.setFileId(update.getCallbackQuery().getMessage().getDocument().getFileId());
            requestApplicantRepo.save(requestApplicant);
            EditMessageMedia editMessageMedia = new EditMessageMedia();
            InputMediaDocument document = new InputMediaDocument();
            document.setMedia(update.getCallbackQuery().getMessage().getDocument().getFileId()).setParseMode("html").setCaption(getText(96));
            editMessageMedia.setMessageId(updateMessageId).setChatId(chatId).setMedia(document);
            bot.execute(editMessageMedia);
            bot.execute(sendDocument);
            }
            else {
                deleteUpdateMessage();
            }

        }catch (Exception e){e.printStackTrace();}
    }


}
