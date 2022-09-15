package com.example.spgtu.dao.repositories;

import com.example.spgtu.dao.entities.custom.RegistrCardQuests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrCardQuestsRepo extends JpaRepository<RegistrCardQuests, Long> {
    RegistrCardQuests findById(long id);
}
