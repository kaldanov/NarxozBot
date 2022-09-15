package com.example.spgtu.dao.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    MALE("Мужской"),
    FEMALE("Женский");

    String val;


}
