package com.example.spgtu.dao.entities.custom;

import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.dao.enums.Gender;
import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
public class WithoutPreparationCourses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //    private long chatId;
    @ManyToOne
    private User sender;
    //    private String surname;
    private String fio;  /////////////////////////////////////////////////////
    @ManyToOne
    private ChosenDirection chosenDirection;/////////////////////////////////////////////////////
    private String educationForm;/////////////////////////////////////////////////////
    private String address;/////////////////////////////////////////////////////
    private String iin;/////////////////////////////////////////////////////
    private Date date;


    //contract
    private String countHours;/////////////////////////////////////////////////////
    private String numberCard;/////////////////////////////////////////////////////
    private Date dateIssueCard;/////////////////////////////////////////////////////
    private String cardGive;

    //docs
    private String contractFileId;
    private String statementToHandleFileId;

    public String getBirthYear() {
        String ry = "0";
        try {

            int y = Integer.parseInt(iin.substring(0, 2));
            if (y > 0 && y < Calendar.getInstance().get(Calendar.YEAR)){
                ry = "20" + iin.substring(0, 2);
            }
            else ry = "19" + iin.substring(0, 2);
        }catch (Exception e){e.printStackTrace();}
        return ry;
    }

    public String getBirthDay() {
        return getBirthYear() + "."+iin.substring(2,4) + "." + iin.substring(4,6);
    }

    public String getLastName() {
        if (fio.split(" ").length == 3)
            return fio.split(" ")[0];
        return fio;
    }

    public String getFirstname() {
        if (fio.split(" ").length == 3)
            return fio.split(" ")[1] + " " +fio.split(" ")[2];
        return fio;
    }
}
