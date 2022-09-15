package com.example.spgtu.dao.entities.custom;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nameRus;
    private String nameKaz;


    public String getName(int langId) {
        if (langId == 1)
            return nameRus;
        return nameKaz;
    }
}
