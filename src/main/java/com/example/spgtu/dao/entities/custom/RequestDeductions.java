package com.example.spgtu.dao.entities.custom;

import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.dao.enums.Gender;
import com.example.spgtu.dao.enums.TypeReference;
import com.example.spgtu.dao.enums.TypeReferenceStudent;
import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
public class RequestDeductions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User sender;
    private String fio;

    private String educationForm;
    private String address;
    private String course;
    private String eduDirection;
    private Date date;
    private String phone;
    private String fileId;
    private Long kaspiOrderId;

    @Enumerated
    TypeReferenceStudent typeReferenceStudent;

}
