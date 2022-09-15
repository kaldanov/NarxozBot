package com.example.spgtu.dao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum TypeDocs {
    STUDENT_CARD("Студенческий билет", 5300),
    CREDIT_BOOK("Зачетная книжка", 8000),
    BADGE("Бейджик",1000),
    PROXY_CARD_NEW("Прокси карта получение", 5000),
    PROXY_CARD_RESTORE("Прокси карта восстановление", 5000);

    String val;
    int price;
}
