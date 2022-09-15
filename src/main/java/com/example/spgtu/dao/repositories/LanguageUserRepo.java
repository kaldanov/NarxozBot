package com.example.spgtu.dao.repositories;

import com.example.spgtu.dao.entities.standart.LanguageUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageUserRepo extends JpaRepository<LanguageUser, Long> {
    LanguageUser findAllByChatId(long chatId);
}
