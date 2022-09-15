package com.example.spgtu.dao.repositories;

import com.example.spgtu.dao.entities.custom.Dorm;
import com.example.spgtu.dao.entities.custom.DormRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DormRegistrationRepo extends JpaRepository<DormRegistration, Long> {
//    List<DormRegistration> findAllByDorm(Dorm dorm);
    List<DormRegistration> findAllByDormAndArchiveFalse(Dorm dorm);
    List<DormRegistration> findAllByDateStartBetweenOrderById(Date start, Date end);
    DormRegistration findBySenderChatId(long chatId);
    DormRegistration findById(long id);
}
