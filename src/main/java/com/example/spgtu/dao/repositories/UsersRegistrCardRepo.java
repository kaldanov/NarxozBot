package com.example.spgtu.dao.repositories;

import com.example.spgtu.dao.entities.custom.PreparationCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UsersRegistrCardRepo extends JpaRepository<PreparationCourses, Long> {
//    List<UsersRegistrCard> findAllBySender(User user);
    List<PreparationCourses> findAllBySenderChatIdOrderById(long chatId);
    PreparationCourses findById(long id);

    List<PreparationCourses> findAllByDateBetween(Date start, Date end);

    PreparationCourses findByDate(Date date);
}
