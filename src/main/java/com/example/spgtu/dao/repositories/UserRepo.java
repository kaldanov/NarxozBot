package com.example.spgtu.dao.repositories;

import com.example.spgtu.dao.entities.standart.Role;
import com.example.spgtu.dao.entities.standart.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    long countByChatId(long newAdminChatId);
    User findByChatId(long chatId);

    User findByPhone(String phone);
    User findById(long id);

    List<User> findAllByRolesContains(Role role);

}