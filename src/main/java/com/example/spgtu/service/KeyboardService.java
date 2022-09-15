package com.example.spgtu.service;

import com.example.spgtu.dao.entities.standart.Button;
import com.example.spgtu.dao.entities.standart.Keyboard;
import com.example.spgtu.dao.enums.Language;
import com.example.spgtu.dao.repositories.ButtonRepo;
import com.example.spgtu.dao.repositories.KeyboardMarkUpRepo;
import com.example.spgtu.dao.repositories.TelegramBotRepositoryProvider;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardService {

    KeyboardMarkUpRepo keyboardMarkUpRepo = TelegramBotRepositoryProvider.getKeyboardMarkUpRepo();
    ButtonRepo buttonRepo = TelegramBotRepositoryProvider.getButtonRepo();

    ReplyKeyboard getReplyKeyboard(String[] rows, Language language) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        boolean isRequestContact = false;
        for (String buttonIdsString : rows) {
            KeyboardRow keyboardRow = new KeyboardRow();
            String[] buttonIds = buttonIdsString.split(",");
            for (String buttonId : buttonIds) {
                Button buttonFromDb = buttonRepo.findByIdAndLangId(Integer.parseInt(buttonId), language.getId());
                KeyboardButton button = new KeyboardButton();
                String buttonText = buttonFromDb.getName();
                button.setText(buttonText);

                keyboardRow.add(button);
            }
            keyboardRowList.add(keyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setOneTimeKeyboard(isRequestContact);
        return replyKeyboardMarkup;
    }


    public ReplyKeyboard getReplyKeyboardRequestContact( Language language) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setRequestContact(true);

        Button buttonFromDb = buttonRepo.findByIdAndLangId(10, language.getId());

        String buttonText = buttonFromDb.getName();
        button.setText(buttonText);
        keyboardRow.add(button);

        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }


    InlineKeyboardMarkup getInlineKeyboard(String[] rowIds, long chatId) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (String buttonIdsString : rowIds) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            String[] buttonIds = buttonIdsString.split(",");
            for (String buttonId : buttonIds) {
                Button buttonFromDb = buttonRepo.findByIdAndLangId(Integer.parseInt(buttonId), LanguageService.getLanguage(chatId).getId());
                InlineKeyboardButton button = new InlineKeyboardButton();
                String buttonText = buttonFromDb.getName();
                button.setText(buttonText);
                buttonText = buttonText.length() < 64 ? buttonText : buttonText.substring(0, 64);
                button.setCallbackData(buttonText);
                row.add(button);
            }
            rows.add(row);
        }
        keyboard.setKeyboard(rows);
        return keyboard;
    }


    public ReplyKeyboard getKeyboard(long keyboardId, long chatId) {
        return getKeyboard(keyboardMarkUpRepo.findById(keyboardId), chatId);

    }
    public ReplyKeyboard getKeyboard(Keyboard keyboard, long chatId) {
        String buttonIds = keyboard.getButton_ids();
        if (buttonIds == null) {
            return null;
        }
        String[] rows = buttonIds.split(";");
        if (keyboard.isInline()) {
            return getInlineKeyboard(rows, chatId);
        }
        else {
            return getReplyKeyboard(rows, LanguageService.getLanguage(chatId));
        }
    }



    public List<List<InlineKeyboardButton>> getRowsKeyboard(long keyboardId, long chatId) {
        String buttonIds1 = keyboardMarkUpRepo.findById(keyboardId).getButton_ids();
        if (buttonIds1 == null) {
            return null;
        }
        String[] rowIds = buttonIds1.split(";");

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (String buttonIdsString : rowIds) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            String[] buttonIds = buttonIdsString.split(",");
            for (String buttonId : buttonIds) {
                Button buttonFromDb = buttonRepo.findByIdAndLangId(Integer.parseInt(buttonId), LanguageService.getLanguage(chatId).getId());
                InlineKeyboardButton button = new InlineKeyboardButton();
                String buttonText = buttonFromDb.getName();
                button.setText(buttonText);
                button.setCallbackData(buttonText);
                row.add(button);
            }
            rows.add(row);
        }
        return rows;

    }
}

