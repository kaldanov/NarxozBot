package com.example.spgtu.dao.entities.custom;

import com.example.spgtu.dao.enums.Gender;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Dorm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Gender gender;
    private Integer countPlaces;

    private Boolean fullRoom;

    private Integer priceOneMonth;
    private Integer priceTwoMonth;
    private Integer priceFiveMonth;
    private Integer priceTenMonth;

}
