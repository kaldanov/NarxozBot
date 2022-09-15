package com.example.spgtu.dao.entities.standart;

import com.example.spgtu.dao.enums.Language;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@ToString
@Entity
public class LanguageUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private long     chatId;
    private Language language;



    public LanguageUser(long chatId, Language language) {
        this.chatId = chatId;
        this.language = language;
    }

    public LanguageUser() {}
}

