package com.example.spgtu.dao.repositories;

import com.example.spgtu.dao.entities.standart.Keyboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyboardMarkUpRepo extends JpaRepository<Keyboard, Long> {
    Keyboard findById(long key);

    int countById(int id);
}
