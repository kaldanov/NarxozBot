package com.example.spgtu.dao.entities.standart;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@Entity
public class Button {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long      id;

    private String   name;
    private Integer  commandId;
//    private String   url;
    private Integer  langId;
//    private Boolean  requestContact;
//    private Integer  messageId;

//    public Boolean getRequestContact() {
//        return requestContact;
//    }
}
