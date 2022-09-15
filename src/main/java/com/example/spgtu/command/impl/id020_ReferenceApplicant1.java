package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.custom.*;
import com.example.spgtu.dao.entities.standart.Role;
import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.dao.enums.TypeReference;
import com.example.spgtu.service.FileGenerator;
import com.example.spgtu.util.ButtonsLeaf;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.*;

public class id020_ReferenceApplicant1 extends Command {
    int bid  = 0;


    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        deleteUpdateMessage();
        if (!isRegistered() || !userRepo.findByChatId(chatId).isApplicant()) {
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }


        if (isButton(90)){//Справка о поступлении в АФ СПбГУП
            List<PreparationCourses> preparationCourses = usersRegistrCardRepo.findAllBySenderChatIdOrderById(chatId);
            if (preparationCourses.size() > 0) {
                RequestApplicant requestApplicantOld = requestApplicantRepo.findAllByPreparationCoursesSenderAndTypeReference(userRepo.findByChatId(chatId), TypeReference.ABOUT_ADMISSION);
                if (requestApplicantOld != null){
                    if (requestApplicantOld.getAccepted() == null){
                        sendMessage(107);
                    }
                    else if (requestApplicantOld.getAccepted()!= null && requestApplicantOld.getAccepted()){
                        SendDocument ref = new SendDocument();
                        ref.setDocument(FileGenerator.referenceApplicant2(preparationCourses.get(preparationCourses.size()-1))).setChatId(chatId);
                        bot.execute(ref);
                    }
                    else {
                        sendMessageWithKeyboard(108, 32);
                        bid = 90;
                    }
                }
                else {
                    sendMessageWithKeyboard(100, 32);
                    bid = 90;
                }

            }
            else {
                sendMessage(106);
                return EXIT;
            }
           return COMEBACK;
        }

        else if (hasCallbackQuery() && isButton(94)){ //da // yes
            if (bid == 90) {
                ////////////////////////////
                RequestApplicant requestApplicantOld = requestApplicantRepo.findAllByPreparationCoursesSenderAndTypeReference(userRepo.findByChatId(chatId), TypeReference.ABOUT_ADMISSION);
                if (requestApplicantOld != null)
                    requestApplicantRepo.delete(requestApplicantOld);
                ////////////////////////////

                sendMessage(101);
                List<PreparationCourses> preparationCourses = usersRegistrCardRepo.findAllBySenderChatIdOrderById(chatId);

                RequestApplicant requestApplicant = new RequestApplicant();
                requestApplicant.setTypeReference(TypeReference.ABOUT_ADMISSION);
                requestApplicant.setPreparationCourses(preparationCourses.get(preparationCourses.size() - 1));
                requestApplicant.setDate(new Date());
                requestApplicant = requestApplicantRepo.save(requestApplicant);

                sendReferecnse2(requestApplicant);
            }
            else if (bid == 88){
                List<PreparationCourses> preparationCourses = usersRegistrCardRepo.findAllBySenderChatIdOrderById(chatId);
                if (preparationCourses.size() != 0) {
                    sendMessage(91);
                }
                else sendMessage(92);
                return COMEBACK;
            }
            return EXIT;
        }
        else if (hasCallbackQuery() && isButton(95)){ //no
            return EXIT;
        }
        else if (isButton(88)) { // Справка об обучении на подгото-ных курсах
            List<PreparationCourses> preparationCourses = usersRegistrCardRepo.findAllBySenderChatIdOrderById(chatId);
            if (preparationCourses.size() > 0) {
                RequestApplicant requestApplicantOld = requestApplicantRepo.findAllByPreparationCoursesSenderAndTypeReference(userRepo.findByChatId(chatId), TypeReference.ABOUT_LEARNING);
                if (requestApplicantOld != null){
                    if (requestApplicantOld.getAccepted() == null){
                        sendMessage(107);
                    }
                    else if (requestApplicantOld.getAccepted()!= null && requestApplicantOld.getAccepted()){
                        SendDocument ref = new SendDocument();
                        ref.setDocument(FileGenerator.referenceApplicant1(preparationCourses.get(preparationCourses.size()-1), requestApplicantOld.getDays())).setChatId(chatId);
                        bot.execute(ref);
                    }
                    else {
                        sendMessageWithKeyboard(108, 32);
                        bid = 88;
                    }
                }else {
                    bid = 88;
                    sendMessageWithKeyboard(102, 32);
                }
                return COMEBACK;

            }
            else {
                sendMessage(106);
                return EXIT;
            }


        }
        else if (hasMessageText()){
            sendMessage(101);

            List<PreparationCourses> preparationCourses = usersRegistrCardRepo.findAllBySenderChatIdOrderById(chatId);


            RequestApplicant requestApplicantOld = requestApplicantRepo.findAllByPreparationCoursesSenderAndTypeReference(userRepo.findByChatId(chatId), TypeReference.ABOUT_LEARNING);
            if (requestApplicantOld != null) {
                requestApplicantRepo.delete(requestApplicantOld);
            }

            RequestApplicant requestApplicant = new RequestApplicant();
            requestApplicant.setDays(updateMessageText);
            requestApplicant.setTypeReference(TypeReference.ABOUT_LEARNING);
            requestApplicant.setPreparationCourses(preparationCourses.get(preparationCourses.size() - 1));
            requestApplicant.setDate(new Date());
            requestApplicantRepo.save(requestApplicant);

            sendDocument(requestApplicant);
        }
        return EXIT;
    }

    private void sendReferecnse2(RequestApplicant requestApplicant) {
        try {
            List<User> opds = userRepo.findAllByRolesContains(new Role(2, "ROLE_ODP"));
            ButtonsLeaf buttonsLeaf = new ButtonsLeaf(Arrays.asList(getButtonText(92),getButtonText(93)), Arrays.asList("22;"+ requestApplicant.getId() + ";92","22;"+ requestApplicant.getId() + ";93"));
            for (User odp : opds){
                SendDocument ref = new SendDocument();
                ref.setDocument(FileGenerator.referenceApplicant2(requestApplicant.getPreparationCourses())).setChatId(odp.getChatId()).setCaption(getInfo(requestApplicant.getPreparationCourses())).setReplyMarkup(buttonsLeaf.getListButtonWithIds());
                bot.execute(ref);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getInfo(PreparationCourses reg) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getText(93)).append(reg.getId()).append(next)
                .append(getText(94));
        return stringBuilder.toString();
    }


    private void sendDocument(RequestApplicant requestApplicant ) {
        try {
            List<User> opds = userRepo.findAllByRolesContains(new Role(2, "ROLE_ODP"));
            ButtonsLeaf buttonsLeaf = new ButtonsLeaf(Arrays.asList(getButtonText(92),getButtonText(93)), Arrays.asList("22;"+ requestApplicant.getId() + ";92","22;"+ requestApplicant.getId() + ";93"));
            for (User odp : opds){
                SendDocument ref = new SendDocument();
                ref.setDocument(FileGenerator.referenceApplicant1(requestApplicant.getPreparationCourses(),requestApplicant.getDays())).setChatId(odp.getChatId()).setCaption(getText(94)).setReplyMarkup(buttonsLeaf.getListButtonWithIds());
                bot.execute(ref);
            }

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}
