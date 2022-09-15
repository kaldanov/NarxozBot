package com.example.spgtu.dao.entities.custom;

import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.dao.enums.Gender;
import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Entity
@Data
public class DormRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated
    private Gender gender;

//    private String roomType;
//    private String socialGroup;
    private String fio;
    private String IIN;
    private String address;
    private String eduDirection;
    private String course;
    private String educationForm;
    private String cardId;
    Date dateIssueCard;
    String whoGiveCard;
    String contractFileId;
    String statementFileId;

    @Column(columnDefinition = "boolean default false")
    boolean archive;

    @ManyToOne
    Dorm dorm;

    Date dateStart;
    int countMonth;

    @ManyToOne
    User sender;

    public String getDateStr(){
        return getDateStr2(dateStart);
    }

    private String getDateStr2(Date date) {
        SimpleDateFormat formatMonth = new SimpleDateFormat("M");
        String mo = formatMonth.format(date);
        SimpleDateFormat formatYear321 = new SimpleDateFormat("Y");
        String ye = formatYear321.format(date);
        Month jan = Month.of(Integer.parseInt(mo));
        SimpleDateFormat formatDateDay = new SimpleDateFormat("dd");
        String day = formatDateDay.format(date);
        Locale loc = Locale.forLanguageTag("ru");
        return "«" + day + "» " + jan.getDisplayName(TextStyle.FULL_STANDALONE, loc) + " " + ye;
    }

    public String getDateStrPlusOneMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateStart);
        calendar.add(Calendar.MONTH, 1);
        return getDateStr2(calendar.getTime());
    }

    public int getPrice() {
        if (countMonth == 1)
            return dorm.getPriceOneMonth();
        else if (countMonth == 2)
            return dorm.getPriceTwoMonth();
        else if (countMonth == 5)
            return dorm.getPriceFiveMonth();
        else if (countMonth == 10)
            return dorm.getPriceTenMonth();
        return -1;
    }
}
