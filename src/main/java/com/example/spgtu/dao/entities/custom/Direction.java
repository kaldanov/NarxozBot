package com.example.spgtu.dao.entities.custom;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Direction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameRus;
    private String nameKaz;
    private String code;
    private String faculty;



    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    List<Subject> required;



    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    List<Subject> forChoose;


    public String getName(int langId) {
        if (langId == 1)
            return nameRus;
        return nameKaz;
    }

    public boolean isSubjectForChose(Subject byId) {
        for (Subject subject : forChoose){
            if (subject.getId() == byId.getId())
                return true;
        }
        return false;
    }
}
