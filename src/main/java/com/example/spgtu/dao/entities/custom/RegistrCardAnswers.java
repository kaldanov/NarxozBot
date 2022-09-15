package com.example.spgtu.dao.entities.custom;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class RegistrCardAnswers {
    @Id
    @GeneratedValue
    private long id;
    private String ruAnswer;
    private String kzAnswer;

    @ManyToOne
    RegistrCardQuests registrCardQuests;

}
