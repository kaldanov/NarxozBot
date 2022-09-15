package com.example.spgtu.dao.entities.custom;

import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.dao.enums.TypeDocs;
import com.example.spgtu.dao.enums.TypeReferenceStudent;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class LossDocs {
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

    @Enumerated
    TypeDocs typeDocs;

}
