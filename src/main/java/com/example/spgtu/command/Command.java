package com.example.spgtu.command;

import com.example.spgtu.dao.entities.standart.Button;
import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.dao.enums.Language;
import com.example.spgtu.dao.repositories.*;
import com.example.spgtu.service.KeyboardService;
import com.example.spgtu.service.LanguageService;
import com.example.spgtu.util.BotUtil;
import com.example.spgtu.util.ButtonsLeaf;
import com.example.spgtu.util.UpdateUtil;
import com.example.spgtu.util.type.WaitingType;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.List;

public abstract class Command {

    protected int updateMessageId;
    protected int lastSentMessageID;


    @Getter
    @Setter
    protected long id;
    protected long chatId;
    @Getter
    @Setter
    protected long messageId;

    protected Update update;


    protected Message updateMessage;
    protected DefaultAbsSender bot;
    protected String updateMessageText;
    protected String updateMessagePhoto;
    protected String updateMessagePhone;
    protected WaitingType waitingType = WaitingType.START;
    protected String editableTextOfMessage;
    protected Button currentButton;
    protected com.example.spgtu.dao.entities.standart.Message currentMessage;

    protected static BotUtil botUtils;

    protected static final String next = "\n";
    protected static final String space = " ";
    protected final static boolean EXIT = true;
    protected final static boolean COMEBACK = false;

    //=========== REPOSITORIES ==============================//
//    protected AdminRepos adminRepos = TelegramBotRepositoryProvider.getAdminRepos();
    protected ButtonRepo buttonRepo = TelegramBotRepositoryProvider.getButtonRepo();
    protected DirectionRepo directionRepo = TelegramBotRepositoryProvider.getDirectionRepo();
    protected KeyboardMarkUpRepo keyboardMarkUpRepo = TelegramBotRepositoryProvider.getKeyboardMarkUpRepo();
    protected LanguageUserRepo languageUserRepo = TelegramBotRepositoryProvider.getLanguageUserRepo();
    protected MessageRepo messageRepo = TelegramBotRepositoryProvider.getMessageRepo();
    protected PropertiesRepo propertiesRepo = TelegramBotRepositoryProvider.getPropertiesRepo();
    protected UserRepo userRepo = TelegramBotRepositoryProvider.getUserRepo();
    protected RegistrCardQuestsRepo registrCardQuestsRepo = TelegramBotRepositoryProvider.getRegistrCardQuestsRepo();
    protected RegistrCardAnswersRepo registrCardAnswersRepo = TelegramBotRepositoryProvider.getRegistrCardAnswersRepo();
    protected UsersRegistrCardRepo usersRegistrCardRepo = TelegramBotRepositoryProvider.getUsersRegistrCardRepo();
    protected ContractRepo contractRepo = TelegramBotRepositoryProvider.getContractRepo();
    protected DormRegistrationRepo dormRegistrationRepo = TelegramBotRepositoryProvider.getDormRegistrationRepo();
    protected DormRepo dormRepo = TelegramBotRepositoryProvider.getDormRepo();
    protected SubjectRepo subjectRepo = TelegramBotRepositoryProvider.getSubjectRepo();
    protected ChosenDirectionRepo chosenDirectionRepo = TelegramBotRepositoryProvider.getChosenDirectionRepo();
    protected RequestApplicantRepo requestApplicantRepo = TelegramBotRepositoryProvider.getRequestApplicantRepo();
    protected OnayRepository onayRepository = TelegramBotRepositoryProvider.getOnayRepository();
    protected WithoutPreparationCoursesRepo withoutPreparationCoursesRepo = TelegramBotRepositoryProvider.getWithoutPreparationCoursesRepo();
    protected RequestDeductionsRepo requestDeductionsRepo = TelegramBotRepositoryProvider.getRequestDeductionsRepo();
    protected LossDocsRepo lossDocsRepo = TelegramBotRepositoryProvider.getLossDocsRepo();
    //=========================================================//
    protected KeyboardService keyboardService = new KeyboardService();

    protected String getButtonText(long id){
        Button button =  buttonRepo.findByIdAndLangId(id, getLanguage().getId());
        return button!= null ? button.getName(): null;
    }

    protected boolean isIIN(String updateMessageText) {
        try {
            Long.parseLong(updateMessageText);
            return updateMessageText.length() == 12;
        }
        catch (Exception e){
            return false;
        }
    }

    protected int getInt(String text){
        try {
            return Integer.parseInt(text);
        }catch (Exception e){
            return -1;
        }
    }

    protected long getLong(String text){
        try {
            return Long.parseLong(text);
        }catch (Exception e){
            return -1L;
        }
    }

    protected Language getLanguage() {
        if (chatId == 0) return Language.ru;
        return LanguageService.getLanguage(chatId);
    }
    protected Language getLanguage(long chatIdOfUser) {
        if (chatIdOfUser == 0) return Language.ru;
        return LanguageService.getLanguage(chatIdOfUser);
    }
    protected void editMessage(String text, int messageId) throws TelegramApiException {
        botUtils.editMessage(text, chatId, messageId);
    }

    protected void editMessageWithKeyboard(String text, int messageId, ReplyKeyboard replyKeyboard) throws TelegramApiException {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(text).setReplyMarkup((InlineKeyboardMarkup) replyKeyboard).setChatId(chatId).setMessageId(messageId).setParseMode("html");
        bot.execute(editMessageText);
    }


    private ButtonsLeaf buttonsLeaf;

    public abstract boolean execute() throws SQLException, TelegramApiException;

    protected int sendMessage(long messageId) throws TelegramApiException {
        return botUtils.sendMessage(messageId, chatId);
    }

//    protected int sendMessage(long messageId, long chatId) throws TelegramApiException {
//        return sendMessage(messageId, chatId);
//    }

    protected int sendDocument(String doc, long chatId) throws TelegramApiException,NullPointerException {

        return botUtils.sendDocument(doc,chatId);
    }
    protected void sendMediaGroup(List<InputMedia> files, long chatId) throws TelegramApiException,NullPointerException {
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(chatId).setMedia(files);
        bot.execute(sendMediaGroup);
    }

    protected void deleteMessages(int del) {
        deleteMessage(del);
        deleteMessage(updateMessageId);
    }

    protected void sendMediaGroupWhitDelete(List<InputMedia> files, List<Message> messageS,List<Integer> messagesId ,long chatId) throws TelegramApiException,NullPointerException {
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(chatId).setMedia(files);
        messageS = bot.execute(sendMediaGroup);
        messageS.forEach(message -> {
            messagesId.add(message.getMessageId());
        });

    }


    protected List<Message> getFileIdDFromMediaGroup(List<InputMedia> files, long chatId) throws TelegramApiException,NullPointerException {
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(chatId).setMedia(files);
        return bot.execute(sendMediaGroup);
    }


    protected int sendMessage(long messageId, long chatId) throws TelegramApiException {
        return botUtils.sendMessage(messageId, chatId);
    }


    protected int sendMessage(String text) throws TelegramApiException {
        return sendMessage(text, chatId);
    }

    protected int sendMessage(String text, long chatId) throws TelegramApiException {
        return sendMessage(text, chatId, null);
    }

    protected int sendMessage(String text, long chatId, Contact contact) throws TelegramApiException {
        lastSentMessageID = botUtils.sendMessage(text, chatId);
        if (contact != null) {
            botUtils.sendContact(chatId, contact);
        }
        return lastSentMessageID;
    }

//    protected void sendListProjects(String category) throws TelegramApiException {
//        if (category.equals(buttonRepo.findByIdAndLangId(228, currentLanguage.getId()).getName())) {
//            currentLanguage = LanguageService.getLanguage(chatId);
//            List<String> listOfContents = new ArrayList<>();
//            List<String> ids = new ArrayList<>();
//            contents = contestRepo.findAll();
//            if (currentLanguage.getId() == 1) {
//                contents.forEach(content -> {
//                    listOfContents.add(content.getNameInRus());
//                    ids.add(String.valueOf(content.getId()));
//                });
//                listOfContents.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
//                ids.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
//            } else {
//                contents.forEach(content -> {
//                    listOfContents.add(content.getNameInKz());
//                    ids.add(String.valueOf(content.getId()));
//                });
//                listOfContents.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
//                ids.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
//            }
//            buttonsLeaf = new ButtonsLeaf(listOfContents, ids, listOfContents.size());
//            delete = sendMessageWithKeyboard(getText(1459), buttonsLeaf.getListButton2());
//            waitingType = WaitingType.GET_INFO;
//        } else if (category.equals(buttonRepo.findByIdAndLangId(227, currentLanguage.getId()).getName())) {
//            currentLanguage = LanguageService.getLanguage(chatId);
//            List<String> listOfTraining = new ArrayList<>();
//            List<String> ids = new ArrayList<>();
//            trainingAndSeminars = trainingAndSeminarRepo.findAll();
//            if (currentLanguage.getId() == 1) {
//                trainingAndSeminars.forEach(trainingAndSeminar -> {
//                    listOfTraining.add(trainingAndSeminar.getNameInRus());
//                    ids.add(String.valueOf(trainingAndSeminar.getId()));
//                });
//                listOfTraining.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
//                ids.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
//            } else {
//                trainingAndSeminars.forEach(trainingAndSeminar -> {
//                    listOfTraining.add(trainingAndSeminar.getNameInKz());
//                    ids.add(String.valueOf(trainingAndSeminar.getId()));
//                });
//                listOfTraining.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
//                ids.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
//            }
//            buttonsLeaf = new ButtonsLeaf(listOfTraining, ids, listOfTraining.size());
//            sendMessageWithKeyboard(getText(1459), buttonsLeaf.getListButton2());
//            waitingType = WaitingType.GET_INFO;
//        } else {
//            currentLanguage = LanguageService.getLanguage(chatId);
//            List<String> courses = new ArrayList<>();
//            List<String> ids = new ArrayList<>();
//            coursesList = coursessRepo.findAll();
//            if (currentLanguage.getId() == 1) {
//                coursesList.forEach(course -> {
//                    courses.add(course.getRuName());
//                    ids.add(String.valueOf(course.getId()));
//                });
//                courses.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
//                ids.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
//            } else {
//                coursesList.forEach(course -> {
//                    courses.add(course.getKazName());
//                    ids.add(String.valueOf(course.getId()));
//                });
//                courses.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
//                ids.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
//            }
//            buttonsLeaf = new ButtonsLeaf(courses, ids, courses.size());
//            delete = sendMessageWithKeyboard(getText(1459), buttonsLeaf.getListButton2());
//            waitingType = WaitingType.GET_INFO;
//        }
//
//    }

    protected void deleteMessage() {
        deleteMessage(chatId, lastSentMessageID);
    }

    protected void deleteUpdateMessage() {
        deleteMessage(updateMessageId);
    }

    protected void deleteMessage(int messageId) {
        deleteMessage(chatId, messageId);
    }

    protected void deleteMessage(long chatId, int messageId) {
        if (messageId > 0)
            botUtils.deleteMessage(chatId, messageId);
    }

    protected String getText(int messageIdFromBD) {
        int lang = LanguageService.getLanguage(chatId).getId();
        com.example.spgtu.dao.entities.standart.Message mes;
        try {
            mes = messageRepo.findByIdAndLangId(messageIdFromBD, lang);
            return mes.getName();
        }catch (Exception e){
            e.printStackTrace();
        }
        return messageRepo.findByIdAndLangId(messageIdFromBD, LanguageService.getLanguage(chatId).getId()).getName();
    }



//    protected void getListLessons(int courseId) throws TelegramApiException {
//        currentCourse = coursessRepo.findById(courseId);
//        courseLessons = lessonsRepo.findByCoursessOrderById(coursessRepo.findById(courseId));
//        List<String> lessons = new ArrayList<>();
//        List<String> ids = new ArrayList<>();
//        if (currentLanguage.getId() == 1) {
//            courseLessons.forEach(lesson -> {
//                lessons.add(lesson.getRuName());
//                ids.add(String.valueOf(lesson.getId()));
//            });
//        } else if (currentLanguage.getId() == 2) {
//            courseLessons.forEach(lesson -> {
//                lessons.add(lesson.getRuName());
//                ids.add(String.valueOf(lesson.getId()));
//            });
//        }

        // конпка добавить урок
//        lessons.add(buttonRepo.findByIdAndLangId(242, LanguageService.getLanguage(chatId).getId()).getName());
//        ids.add(buttonRepo.findByIdAndLangId(242, LanguageService.getLanguage(chatId).getId()).getName());
//
//        // кнопка назад
//        lessons.add(buttonRepo.findByIdAndLangId(221, LanguageService.getLanguage(chatId).getId()).getName());
//        ids.add(buttonRepo.findByIdAndLangId(221, LanguageService.getLanguage(chatId).getId()).getName());
//        buttonsLeaf = new ButtonsLeaf(lessons, ids, lessons.size());
//
//        sendMessageWithKeyboard(getText(1481), buttonsLeaf.getListButton2());
//    }



    protected String getText(int messageIdFromBD, long chatId) {
        int lang = LanguageService.getLanguage(chatId).getId();
        com.example.spgtu.dao.entities.standart.Message mes;
        try {
            mes = messageRepo.findByIdAndLangId(messageIdFromBD, lang);
            return mes.getName();
        }catch (Exception e){
            e.printStackTrace();
        }
        return messageRepo.findByIdAndLangId(messageIdFromBD, LanguageService.getLanguage(chatId).getId()).getName();
    }

    void clear() {
        clear();
        update = null;
        bot = null;
    }

    protected boolean isButton(int buttonId) {
        if (updateMessageText != null)
            return updateMessageText.equals(buttonRepo.findByIdAndLangId(buttonId, LanguageService.getLanguage(chatId).getId()).getName());
        return false;
    }
    protected boolean isButton(String buttonName) {
        return updateMessageText.equals(buttonRepo.findByNameAndLangId(buttonName, LanguageService.getLanguage(chatId).getId()).getName());
    }

    public boolean isInitNotNormal(Update update, DefaultAbsSender bot) {
        if (botUtils == null) {
            botUtils = new BotUtil(bot);
        }
        this.update = update;
        this.bot = bot;
        chatId = UpdateUtil.getChatId(update);
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            updateMessage = callbackQuery.getMessage();
            updateMessageText = callbackQuery.getData();
            updateMessageId = updateMessage.getMessageId();
            editableTextOfMessage = callbackQuery.getMessage().getText();
        } else if (update.hasMessage()) {
            updateMessage = update.getMessage();
            updateMessageId = updateMessage.getMessageId();
            if (updateMessage.hasText()) {
                updateMessageText = updateMessage.getText();
            }
            if (updateMessage.hasPhoto()) {
                int size = update.getMessage().getPhoto().size();
                updateMessagePhoto = update.getMessage().getPhoto().get(size - 1).getFileId();
            } else {
                updateMessagePhoto = null;
            }
        }
        if (hasContact()) {
            updateMessagePhone = update.getMessage().getContact().getPhoneNumber();
        }
//        if (markChange == null) {
//            markChange = getText(Const.EDIT_BUTTON_ICON);
//        }
        return false;
    }

    protected boolean hasContact() {
        return update.hasMessage() && update.getMessage().getContact() != null;
    }


    protected boolean isAdmin() {
        User user = userRepo.findByChatId(chatId);
        return user != null && user.isAdmin();
    }

    protected boolean isRegistered() {
        return userRepo.countByChatId(chatId) > 0;
    }

    protected String getLinkForUser(long chatId, String userName) {
        return String.format("<a href = \"tg://user?id=%s\">%s</a>", String.valueOf(chatId), userName);
    }

//    protected int toDeleteMessage(int messageDeleteId) {
//        SetDeleteMessages.addMessage(chatId, messageDeleteId);
//        return messageDeleteId;
//    }
//
//    protected int toDeleteKeyboard(int messageDeleteId) {
//        SetDeleteMessages.addKeyboard(chatId, messageDeleteId);
//        return messageDeleteId;
//    }

    protected int sendMessageWithKeyboard(int messageId, ReplyKeyboard keyboard) throws TelegramApiException {
        return sendMessageWithKeyboard(getText(messageId), keyboard);
    }

    protected int sendMessageWithKeyboard(int messageId, long keyboardId) throws TelegramApiException{
        return sendMessageWithKeyboard(getText(messageId), keyboardService.getKeyboard(keyboardMarkUpRepo.findById(keyboardId), chatId));
    }

    protected int sendMessageWithKeyboard(String text, int keyboardId) throws TelegramApiException {
        return sendMessageWithKeyboard(text, (ReplyKeyboard) keyboardService.getKeyboard(keyboardMarkUpRepo.findById(keyboardId), chatId));
    }

    protected int sendMessageWithKeyboard(String text, ReplyKeyboard keyboard) throws TelegramApiException {
        lastSentMessageID = sendMessageWithKeyboard(text, keyboard, chatId);
        return lastSentMessageID;
    }
    protected int sendMessageWithKeyboard(String text, ReplyKeyboard keyboard, long chatId) throws TelegramApiException {
        return botUtils.sendMessageWithKeyboard(text, keyboard, chatId);
    }

    protected boolean hasCallbackQuery() {
        return update.hasCallbackQuery();
    }

    protected boolean hasPhoto() {
        return update.hasMessage() && update.getMessage().hasPhoto();
    }

    protected boolean hasDocument() {
        return update.hasMessage() && update.getMessage().hasDocument();
    }

    protected boolean hasAudio() {
        return update.hasMessage() && update.getMessage().getAudio() != null;
    }

    protected boolean hasVideo() {
        return update.hasMessage() && update.getMessage().getVideo() != null;
    }

    protected boolean hasMessageText() { return  update.hasMessage() && update.getMessage().hasText(); }





    protected boolean isLong(String substring) {
        try {
            Long.parseLong(substring);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
