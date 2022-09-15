package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.custom.RequestApplicant;
import com.example.spgtu.dao.enums.TypeReference;
import com.example.spgtu.service.FileGenerator;
import com.example.spgtu.util.ButtonsLeaf;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class id021_ODP extends Command {

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        deleteUpdateMessage();
        if (!isRegistered() || !userRepo.findByChatId(chatId).isOdp()) {
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }


        if (isButton(99) || isButton(118)){
            sendMessageWithKeyboard(109,34);
        }
        else if(isButton(100)){//Запросы об обучении
            List<RequestApplicant> requests = requestApplicantRepo.findAllByAcceptedIsNullAndTypeReference(TypeReference.ABOUT_LEARNING);
            if (requests.size() > 0) {
                ButtonsLeaf buttonsLeaf = getBL(requests);
                sendMessageWithKeyboard(110, buttonsLeaf.getListButtonWithIds());
            }
            else sendMessage(112);
        }
        else if (isButton(101)){ //Запросы о поступлении
            List<RequestApplicant> requests = requestApplicantRepo.findAllByAcceptedIsNullAndTypeReference(TypeReference.ABOUT_ADMISSION);
            if (requests.size() > 0) {
                ButtonsLeaf buttonsLeaf = getBL(requests);
                sendMessageWithKeyboard(111, buttonsLeaf.getListButtonWithIds());
            }
            else sendMessage(112);
        }
        else if (isButton(117)){ //Запросы о поступлении
            sendMessageWithKeyboard(137,40);
        }

        if (hasCallbackQuery() && isRequest()) {
            RequestApplicant requestApplicant = requestApplicantRepo.findById(getLong(updateMessageText));

            ButtonsLeaf buttonsLeaf = new ButtonsLeaf(Arrays.asList(getButtonText(92), getButtonText(93)), Arrays.asList("22;" + requestApplicant.getId() + ";92", "22;" + requestApplicant.getId() + ";93"));
            SendDocument ref = new SendDocument();
            if (requestApplicant.getTypeReference() == TypeReference.ABOUT_LEARNING)
                ref.setDocument(FileGenerator.referenceApplicant1(requestApplicant.getPreparationCourses(), requestApplicant.getDays())).setChatId(chatId).setCaption(getText(94)).setReplyMarkup(buttonsLeaf.getListButtonWithIds());
            else ref.setDocument(FileGenerator.referenceApplicant2(requestApplicant.getPreparationCourses())).setChatId(chatId).setCaption(getText(94)).setReplyMarkup(buttonsLeaf.getListButtonWithIds());


            bot.execute(ref);

        }

        return EXIT;
    }

    private boolean isRequest() {
        return requestApplicantRepo.findById(getLong(updateMessageText)) != null;
    }

    private ButtonsLeaf getBL(List<RequestApplicant> requests) {
//        List<RequestApplicant> requests = requestApplicantRepo.findAllByAcceptedIsNullAndTypeReference(TypeReference.ABOUT_LEARNING);
        List<String> names = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        for (RequestApplicant request : requests){
            names.add(request.getPreparationCourses().getSender().getUserName());
            ids.add(String.valueOf(request.getId()));
        }
        return new ButtonsLeaf(names ,ids);
    }

}
