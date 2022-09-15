package com.example.spgtu.dao.entities.standart;

import com.example.spgtu.dao.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

//import javax.persistence.*;

@Data
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String phone;
    private String email; //only for student
//    private String password; //only for student
    private String userName;
//    private String IIN;

    @Enumerated
    Status status;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Role> roles;

    public boolean isAdmin() {
        for (Role role : roles) {
            if (role.getId() == 1)
                return true;
        }
        return false;
    }
    public boolean isOdp() {
        for (Role role : roles) {
            if (role.getId() == 2)
                return true;
        }
        return false;
    }

    public void addRole(Role roleAdmin) {
        roles.add(roleAdmin);
    }

    public void doNotAdmin() {
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getId() == 1)
                roles.remove(i);
        }
    }

    public boolean isApplicant() {
        if (status != null)
            return status.equals(Status.APPLICANT);
        return false;
    }
    public boolean isStudent() {
        if (status != null)
            return status.equals(Status.STUDENT);
        return false;
    }

    public void doNotODP() {
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getId() == 2)
                roles.remove(i);
        }
    }

    public boolean isDekanat() {
        for (Role role : roles) {
            if (role.getId() == 3)
                return true;
        }
        return false;
    }

    public boolean isOnay() {
        for (Role role : roles) {
            if (role.getId() == 4)
                return true;
        }
        return false;
    }
    public boolean isBron() {
        for (Role role : roles) {
            if (role.getId() == 5)
                return true;
        }
        return false;
    }
    public boolean isBuh() {
        for (Role role : roles) {
            if (role.getId() == 6)
                return true;
        }
        return false;
    }

    public void doNotDekanat() {
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getId() == 3)
                roles.remove(i);
        }
    }
    public void doNotOnay() {
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getId() == 4)
                roles.remove(i);
        }
    }
    public void doNotBron() {
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getId() == 5)
                roles.remove(i);
        }
    }
    public void doNotBuh() {
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getId() == 6)
                roles.remove(i);
        }
    }
}