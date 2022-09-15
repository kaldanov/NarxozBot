package com.example.spgtu.dao.entities.custom;

import com.example.spgtu.dao.enums.TypeReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class RequestApplicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated
    TypeReference typeReference;

    @ManyToOne
    PreparationCourses preparationCourses;

    Boolean accepted;

    Date date;

    String days;

    String fileId;

}
