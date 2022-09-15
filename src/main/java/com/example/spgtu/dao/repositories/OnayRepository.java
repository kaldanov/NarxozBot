package com.example.spgtu.dao.repositories;

import com.example.spgtu.dao.entities.custom.Onay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OnayRepository extends JpaRepository<Onay, Integer> {

    List<Onay> findAllByDateBetweenOrderById(Date dateStart, Date dateEnd);
    List<Onay> findAll();
    Onay findBySenderChatId(long chatId);
}
