package com.example.spgtu.dao.repositories;

import com.example.spgtu.dao.entities.custom.RegistrCardAnswers;
import com.example.spgtu.dao.entities.custom.RegistrCardQuests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrCardAnswersRepo extends JpaRepository<RegistrCardAnswers, Long> {
    List<RegistrCardAnswers> findByRegistrCardQuestsOrderById(RegistrCardQuests registrCardQuest);
    RegistrCardAnswers findById(long id);
}
