package com.example.spgtu.dao.entities.custom;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class ChosenDirection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    Direction direction;

    @ManyToOne
    Subject chosenSubject;


}
