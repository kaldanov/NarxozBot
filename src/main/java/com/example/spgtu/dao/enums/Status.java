package com.example.spgtu.dao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@Getter
@ToString
public enum Status {
    STUDENT(1),
    APPLICANT  (2);

    private int id;

    public static Status getById(int id) {
        for (Status status : values()) {
            if (status.id == (id)) return status;
        }
        return APPLICANT;
    }
}
