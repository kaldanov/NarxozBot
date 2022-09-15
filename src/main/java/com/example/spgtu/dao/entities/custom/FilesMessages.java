package com.example.spgtu.dao.entities.custom;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class FilesMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 4096)
    private String text;
}
