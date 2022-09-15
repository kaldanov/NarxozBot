package com.example.spgtu.dao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum TypeReferenceStudent {
    ABOUT_LEARNING(1000),
    TO_GIVE_MY_DOCS (0);

    int price;
}
