package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.standart.Button;
import com.example.spgtu.dao.enums.Language;
import com.example.spgtu.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;

public class id017_EditButtons extends Command {
    private int inlineMessId;
    private int wrongMessId;
    private int infoMessId;
    private int notFoundMess;
    private Button currentButton;
    private Language currentLang = getLanguage(chatId);
    private List<Button> searchResultButtons;

    @Override
    public boolean execute() throws TelegramApiException {
        if (!isRegistered() || !isAdmin()) {
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }
        switch (waitingType) {
            case START:
                deleteUpdateMess();
//                if (isButton(80)) { // editing button
                    infoMessId = sendMessage(76);
                    waitingType = WaitingType.SEARCH_BUTTON;
//                }
//                else {
//                    sendMessageWithKeyboard(getText(80), 18);
//                }
                return COMEBACK;
            case SEARCH_BUTTON:
                deleteUpdateMess();
                if (hasMessageText()) {
                    currentLang = getLanguage();
                    searchResultButtons = buttonRepo.findAllByNameContainingAndLangIdOrderById(updateMessageText, currentLang.getId());
                    if (searchResultButtons.size() != 0) {
                        deleteMessage(notFoundMess);
                        deleteMessage(infoMessId);
                        inlineMessId = sendMessage(getInfoButtons(searchResultButtons));
                        waitingType = WaitingType.CHOOSE_OPTION;
                    } else {
                        sendNotFound();
                    }
                } else {
                    deleteUpdateMess();
                    sendWrongData();
                }
                return COMEBACK;
            case CHOOSE_OPTION:
                deleteUpdateMess();
                if (updateMessageText.contains("/editName")) { //edit name
                    currentButton = buttonRepo.findByIdAndLangId(getLong(updateMessageText.substring(9)), currentLang.getId());
                    if (currentButton == null) {
                        sendWrongData();
                        return COMEBACK;
                    }
                    deleteMessage(inlineMessId);
                    inlineMessId = sendMessage(getInfoForEdit(currentButton));
//                        editMessage(getInfoForEdit(currentButton), inlineMessId);
//                        infoMessId = sendMessage(57);
                    waitingType = WaitingType.SET_TEXT;
                } else if (updateMessageText.contains("/back")) { // back
                    deleteMessage(infoMessId);
                    deleteMessage(inlineMessId);
                    infoMessId = sendMessage(76);
                    waitingType = WaitingType.SEARCH_BUTTON;
                } else if (updateMessageText.contains("/swapLanguage")) { //swap lang
                    if (currentLang.getId() == 1)
                        currentLang = Language.kz;
                    else
                        currentLang = Language.ru;
                    List<Button> newSearchRes = new ArrayList<>();
                    for (Button button : searchResultButtons) {
                        newSearchRes.add(buttonRepo.findByIdAndLangId(button.getId(), currentLang.getId()));
                    }
                    searchResultButtons = newSearchRes;
                    if (currentButton != null)
                        currentButton = buttonRepo.findByIdAndLangId(currentButton.getId(), currentLang.getId());
                    newSearchRes = null;
                    editMessage(getInfoButtons(searchResultButtons), inlineMessId);
                }
                return COMEBACK;
            case SET_TEXT:
                deleteUpdateMess();
                if (hasMessageText() && updateMessageText.length() < 100) {
                    if (updateMessageText.equals("/cancel")) {
                        deleteMessage(infoMessId);
                        deleteMessage(inlineMessId);
                        inlineMessId = sendMessage(getInfoButtons(searchResultButtons));
                        waitingType = WaitingType.CHOOSE_OPTION;
                        return COMEBACK;
                    } else if (buttonRepo.findByNameAndLangId(updateMessageText, 1) != null || buttonRepo.findByNameAndLangId(updateMessageText, 2) != null || updateMessageText.equals("/swapLanguage") || updateMessageText.equals("/back") || updateMessageText.contains("/editName")) {
                        deleteMessage(infoMessId);
                        infoMessId = sendMessage(79);
                        return COMEBACK;
                    } else {
                        deleteWrongMess();
                        buttonRepo.update(updateMessageText, currentButton.getId(), currentLang.getId());
                        deleteMessage(inlineMessId);
                        deleteMessage(infoMessId);

                        searchResultButtons =  updateButtons(searchResultButtons);
//                        searchResultButtons = buttonRepository.findAllByNameContainingAndLangIdOrderById(currentSearchValue, currentLang.getId());
                        currentButton = buttonRepo.findByIdAndLangId(currentButton.getId(), currentLang.getId());
                        inlineMessId = sendMessage(getInfoButtons(searchResultButtons));
                        waitingType = WaitingType.CHOOSE_OPTION;
                    }
                } else {
                    sendWrongData();
                }
                return COMEBACK;
        }
        return EXIT;
    }

    private List<Button> updateButtons(List<Button> searchResultButtons) {
        List<Button> newSearchRes = new ArrayList<>();
        for (Button button : searchResultButtons) {
            newSearchRes.add(buttonRepo.findByIdAndLangId(button.getId(), currentLang.getId()));
        }
        return newSearchRes;
    }

    private String getInfoForEdit(Button currentButton) {
        return getText(79) + currentButton.getName() + next +
                getText(81);
    }

    private String getInfoButtons(List<Button> searchResultButtons) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getText(72)).append(next).append(next);
        for (Button button : searchResultButtons) {
            stringBuilder.append(button.getName()).append(" \uD83D\uDD8A /editName").append(button.getId()).append(next).append(next);
        }
        stringBuilder.append(next).append(next).append(getText(73)).append(currentLang.name()).append(next);

        stringBuilder.append("/swapLanguage").append(" ").append(getText(74)).append(next)
                .append("/back").append(" ").append(getText(75));

        return stringBuilder.toString();
    }

    private void sendNotFound() throws TelegramApiException {
        deleteMessage(updateMessageId);
        deleteMessage(notFoundMess);
        notFoundMess = sendMessage(77, chatId);
    }


//    private Long getLong(String updateMessageText) {
//        try {
//            return Long.parseLong(updateMessageText);
//        } catch (Exception e) {
//            return -1L;
//        }
//    }

    private void deleteUpdateMess() {
        deleteMessage(updateMessageId);
    }

    private void deleteWrongMess() {
        deleteMessage(wrongMessId);
    }

    private void sendWrongData() throws TelegramApiException {
        deleteMessage(updateMessageId);
        deleteMessage(wrongMessId);
        wrongMessId = sendMessage(45, chatId);
    }
}