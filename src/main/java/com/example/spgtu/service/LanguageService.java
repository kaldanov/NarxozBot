package com.example.spgtu.service;

import com.example.spgtu.dao.entities.standart.LanguageUser;
import com.example.spgtu.dao.enums.Language;
import com.example.spgtu.dao.repositories.LanguageUserRepo;
import com.example.spgtu.dao.repositories.TelegramBotRepositoryProvider;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class LanguageService {

    private static Map<Long, Language> langMap = new HashMap<>();

    public static Language getLanguage(long chatId) {
        Language language = langMap.get(chatId);
        if (language == null) {
            LanguageUserRepo languageUserRepo = TelegramBotRepositoryProvider.getLanguageUserRepo();
            LanguageUser langUser = languageUserRepo.findAllByChatId(chatId);
            if (langUser != null) {
                language = langUser.getLanguage();
                langMap.put(chatId, language);
            }
            else language = Language.ru;
        }
        return language;
    }

    public static void setLanguage(long chatId, Language language) {
        langMap.put(chatId, language);
        LanguageUserRepo languageUserRepo = TelegramBotRepositoryProvider.getLanguageUserRepo();
        TelegramBotRepositoryProvider.getLanguageUserRepo().save(languageUserRepo.findAllByChatId(chatId) != null?languageUserRepo.findAllByChatId(chatId):new LanguageUser(chatId,language));
    }
}

