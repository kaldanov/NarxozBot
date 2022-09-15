package com.example.spgtu.dao.entities.standart;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Properties {
    @Id
    @GeneratedValue()
    private Long id;
    private String name;
    private String value;
}
