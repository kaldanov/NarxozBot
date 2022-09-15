package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.custom.*;
import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.service.FileGenerator;
import com.example.spgtu.service.LanguageService;
import com.example.spgtu.util.ButtonsLeaf;
import com.example.spgtu.util.DateKeyboard;
import com.example.spgtu.util.DateUtil;
import com.example.spgtu.util.type.WaitingType;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class id005_RequestWithoutCourses extends Command {
    private int del;

    private WithoutPreparationCourses preparationCourses;
    private User user;
    DateKeyboard dateKeyboard;
    Date dateIssue;
    Direction direction;
    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        if (!isRegistered() || !userRepo.findByChatId(chatId).isApplicant()) {
            deleteUpdateMessage();
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }


        switch (waitingType) {

            case START:

                user = userRepo.findByChatId(chatId);
                preparationCourses = new WithoutPreparationCourses();

                ///////////////////////////////
//                preparationCourses = withoutPreparationCoursesRepo.findById(3L);
//                sendDocuments(preparationCourses);
                ///////////////////////////////

                sendFIO();
                return COMEBACK;

            case FIO:
                if (hasMessageText() && updateMessageText.split(" ").length == 3) {
                    preparationCourses.setSender(user);
                    preparationCourses.setFio(updateMessageText);
                    sendIIN();
                }
                else {
                    deleteMessages(del);
                    sendFio2();
//                    del = getQuestion(20);
                }
                return COMEBACK;
            case SET_IIN:
                deleteUpdateMessage();
                if (hasCallbackQuery() && isButton(16)){
                    sendFIO();
                }
                else if (hasMessageText() && isIIN(updateMessageText)) {
                    preparationCourses.setIin(updateMessageText);

                    sendAddress();
                }
                else {
                    sendMessage(50);
                }
                return COMEBACK;
            case ADDRESS:
                deleteUpdateMessage();
                if (hasCallbackQuery() && isButton(16)){
                    sendIIN();
                }
                else if (hasMessageText()) {
                    preparationCourses.setAddress(updateMessageText);

                    sendNumberCard();
                }
                return COMEBACK;
            case SET_NUMBER_CARD:
                deleteUpdateMessage();
                if (hasCallbackQuery() && isButton(16)){
                    sendAddress();
                }
                else if (hasMessageText()){
                    deleteMessage(del);
                    preparationCourses.setNumberCard(updateMessageText);
                    dateKeyboard = new DateKeyboard();
                    dateKeyboard.setKeyboard(29, chatId);
                    sendWhoGiveCard();
                }
                return COMEBACK;


            case WHO_GIVE:
                deleteUpdateMessage();
                if (hasCallbackQuery() && isButton(16)){
                    sendNumberCard();
                }
                else if (hasCallbackQuery()){
                    deleteMessage(del);
                    preparationCourses.setCardGive(updateMessageText);
                    sendSetDate();
                }
            return COMEBACK;
            case SET_DATE:
                if (hasCallbackQuery() && isButton(16)){
                    sendWhoGiveCard();
                }
                else if (hasCallbackQuery()){
                    if (dateKeyboard.isNext(updateMessageText)){
                        editMessageWithKeyboard(getText(57), updateMessageId,dateKeyboard.getCalendarKeyboard());
                    }
                    else if (dateKeyboard.getDateDate(updateMessageText) != null){
                        deleteUpdateMessage();
                        dateIssue = dateKeyboard.getDateDate(updateMessageText);
                        if (dateIssue != null) {
                            sendConfirm();
                        }
                    }
                }
                return COMEBACK;
            case CONFIRM:
                deleteUpdateMessage();
                if (hasCallbackQuery()){

                    if (isButton(507)){ // confirm
                        preparationCourses.setDateIssueCard(dateIssue);
                        deleteMessage(del);
                        sendFaculty();

                    }
                    else if (hasCallbackQuery() && isButton(16)){
                        sendSetDate();
                    }
                    else if (isButton(508)){ // re enter
                        sendSetDate();
                    }
                }
                return COMEBACK;
            case FACULTY:
                deleteUpdateMessage();
                if (hasCallbackQuery() && isButton(16)){
                    sendExparionDate();
                }
                if (hasCallbackQuery() && directionRepo.findById(getLong(updateMessageText)) != null) {
                    direction = directionRepo.findById(getLong(updateMessageText));

                    sendGetChoosenLesson();
                }
                return COMEBACK;

            case GET_CHOOSEN_LESSON:
                deleteUpdateMessage();
                if (hasCallbackQuery() && isButton(16)){
                    sendFaculty();
                }
                else if (hasCallbackQuery() && subjectRepo.findById(getLong(updateMessageText))!= null && direction.isSubjectForChose(subjectRepo.findById(getLong(updateMessageText)))) {


                    deleteMessages(del);
                    ChosenDirection chosenDirection = new ChosenDirection();
                    chosenDirection.setDirection(direction);
                    chosenDirection.setChosenSubject(subjectRepo.findById(getLong(updateMessageText)));
                    chosenDirectionRepo.save(chosenDirection);

                    preparationCourses.setChosenDirection(chosenDirection);
//                    usersRegistrCard.set(faculty + ", предмет: " + updateMessageText);

                    sendCountHours();
                }
                return COMEBACK;
            case COUNT_HOURS:
                deleteUpdateMessage();
                if (hasCallbackQuery() && isButton(16)){
                    sendConfirm();
                }
                else if (hasCallbackQuery()){
                    deleteMessages(del);
                    preparationCourses.setCountHours(updateMessageText);
                    sendEducationForm();
                }
                return COMEBACK;

            case EDUCATION_FORM:
//                deleteUpdateMessage();

                if (hasCallbackQuery() && isButton(16)){
                    sendCountHours();
                }
                else if (hasCallbackQuery()) {
                    preparationCourses.setEducationForm(updateMessageText);
                    preparationCourses.setDate(new Date());


                    AnswerCallbackQuery answer = new AnswerCallbackQuery();
                    answer.setText(getText(86)).setCallbackQueryId(update.getCallbackQuery().getId()).setShowAlert(false);

                    bot.execute(answer);
                    sendDocuments(preparationCourses);
                    deleteUpdateMessage();
                }
                return EXIT;

        }
        return false;
    }

    private void sendFaculty() throws TelegramApiException {
        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(getNamesDir(), getIdsDir());
        del = sendMessageWithKeyboard(getText(83), buttonsLeaf.getListButtonWithIds());

        waitingType = WaitingType.FACULTY;
    }

    private List<String> getIdsDir() {
        List<String> ids = new ArrayList<>();
        for (Direction direction: directionRepo.findAllByOrderById()){
            ids.add(String.valueOf(direction.getId()));
        }
        ids.add(getButtonText(16));
        return ids;
    }

    private List<String> getNamesDir() {
        List<String> names = new ArrayList<>();
        for (Direction direction: directionRepo.findAllByOrderById()){
            names.add(direction.getName(getLangId()));
        }
        names.add(getButtonText(16));
        return names;
    }

    private void sendExparionDate() throws TelegramApiException {
        deleteMessages(del);
        del = getQuestion(9);

        waitingType = WaitingType.EXPIRATION_DATE;
    }

    private void sendWhoGiveCard() throws TelegramApiException {
        deleteMessage(del);

        del = sendMessageWithKeyboard(getText(20), 14);
        waitingType = WaitingType.WHO_GIVE;
    }




    private boolean isAnswer(int i) {
        return registrCardAnswersRepo.findById(i).getId() == i;
    }

    private void sendDocuments(WithoutPreparationCourses preparationCourses) {
        SendDocument contract1 = new SendDocument();  //********
//        SendDocument registrCard = new SendDocument();
//        SendDocument statement1 =  new SendDocument();
        SendDocument statementToHandle =  new SendDocument();  //**********
        SendDocument autoBiography = new SendDocument().setDocument(new File("C:/Users/Admin/АВТОБИОГРАФИЯ.pdf")).setChatId(chatId);  //********
        SendDocument examSheet = new SendDocument().setDocument(new File("C:/Users/Admin/ЭКЗАМЕНАЦИОННЫЙ ЛИСТ.docx.")).setChatId(chatId); //*********
        try {
            contract1.setDocument(FileGenerator.getContractWithout(preparationCourses)).setChatId(chatId);
            //todo change this generator
            statementToHandle.setDocument(FileGenerator.statementToHandleWithoutCourses(preparationCourses)).setChatId(chatId);
            String con =  bot.execute(contract1).getDocument().getFileId();
            String stat = bot.execute(statementToHandle).getDocument().getFileId();
            bot.execute(autoBiography);
            bot.execute(examSheet);

            preparationCourses.setContractFileId(con);
            preparationCourses.setStatementToHandleFileId(stat);
            withoutPreparationCoursesRepo.save(preparationCourses);


        } catch (IOException | TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendFio2() throws TelegramApiException {
        deleteMessages(del);
        RegistrCardQuests registrCardQuests = registrCardQuestsRepo.findById(20);
        String  question;
        if (getLangId() == 1) {
            question = registrCardQuests.getRuQuest();
        } else {
            question = registrCardQuests.getKzQuest();
        }
        del = sendMessage(question);
    }

    private void sendConfirm() throws TelegramApiException {
        sendMessageWithKeyboard(getText(82) + " " + DateUtil.getDayDate1(dateIssue), 229);
        waitingType = WaitingType.CONFIRM;
    }

    private void sendSetDate() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessageWithKeyboard(57, dateKeyboard.getCalendarKeyboard());
        waitingType = WaitingType.SET_DATE;
    }

    private void sendNumberCard() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessageWithKeyboard(getText(54), 29);
        waitingType = WaitingType.SET_NUMBER_CARD;
    }

    private void sendCountHours() throws TelegramApiException {
        deleteMessages(del);
        del = sendMessageWithKeyboard(18, 13);
        waitingType = WaitingType.COUNT_HOURS;
    }

    private void sendMothersContact() throws TelegramApiException {
        deleteMessages(del);
        del = getQuestion(19);
        waitingType = WaitingType.MOTHERS_CONTACTS;
    }

    private void sendEducationForm() throws TelegramApiException {
        deleteMessages(del);
        del = getQuestion(11);

        waitingType = WaitingType.EDUCATION_FORM;
    }

    private void sendGetChoosenLesson() throws TelegramApiException {
        deleteMessages(del);

        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(getNamesSub(direction), getIdsSub(direction));

        del = sendMessageWithKeyboard(getReq(direction) + getText(84), buttonsLeaf.getListButtonWithIds());

        waitingType = WaitingType.GET_CHOOSEN_LESSON;
    }


    private void sendAddress() throws TelegramApiException {
        deleteMessages(del);
        del = getQuestion(15);

        waitingType = WaitingType.ADDRESS;
    }

    private void sendGender() throws TelegramApiException {
        deleteMessages(del);
        del = getQuestion(3);
        waitingType = WaitingType.GENDER;
    }

    private void sendIIN() throws TelegramApiException {
        deleteMessages(del);
        del = getQuestion(21);
        waitingType = WaitingType.SET_IIN;
    }

    private void sendFIO() throws TelegramApiException {
        deleteMessages(del);
        RegistrCardQuests registrCardQuests = registrCardQuestsRepo.findById(1);
        String  question;
        if (getLangId() == 1) {
            question = registrCardQuests.getRuQuest();
        } else {
            question = registrCardQuests.getKzQuest();
        }
        del = sendMessage(question);

        waitingType = WaitingType.FIO;
    }

    private String getReq(Direction direction) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getText(85)).append(next);
        for (Subject subject : direction.getRequired()){
            stringBuilder.append("<b>").append(subject.getName(getLangId())).append("</b>").append(next);
        }
        return stringBuilder.toString();
    }

    private List<String> getIdsSub(Direction direction) {
        List<String> ids = new ArrayList<>();
        for (Subject subject: direction.getForChoose()){
            ids.add(String.valueOf(subject.getId()));
        }
        ids.add(getButtonText(16));
        return ids;
    }

    private List<String> getNamesSub(Direction direction) {
        List<String> names = new ArrayList<>();
        for (Subject subject: direction.getForChoose()){
            names.add(subject.getName(getLangId()));
        }
        names.add(getButtonText(16));

        return names;
    }

    private int getLangId() {
        return  LanguageService.getLanguage(chatId).getId();
    }

    private int getQuestion(long id) throws TelegramApiException {
        int del;
        List<RegistrCardAnswers> answersList = new ArrayList<>();
        List<String> strAnswersList = new ArrayList<>();
        List<String> answersIdList = new ArrayList<>();
        ButtonsLeaf buttonsLeafForQuests = null;
        String question;

        RegistrCardQuests registrCardQuests = registrCardQuestsRepo.findById(id);
        if (getLangId() == 1) {
            question = registrCardQuests.getRuQuest();
        } else {
            question = registrCardQuests.getKzQuest();
        }

        answersList = registrCardAnswersRepo.findByRegistrCardQuestsOrderById(registrCardQuests);

        if (answersList.size() != 0){
            answersList.forEach(answer -> {
                if (getLangId() == 1) {
                    strAnswersList.add(answer.getRuAnswer());
                    answersIdList.add(String.valueOf(answer.getId()));
                } else {
                    strAnswersList.add(answer.getKzAnswer());
                    answersIdList.add(String.valueOf(answer.getId()));
                }
            });

        }

        strAnswersList.add(getButtonText(16));
        answersIdList.add(getButtonText(16));
        buttonsLeafForQuests = new ButtonsLeaf(strAnswersList,answersIdList);
        del = sendMessageWithKeyboard(question, buttonsLeafForQuests.getListButtonWithIds());
        return del;
    }
}
