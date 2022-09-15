package com.example.spgtu.dao.repositories;

import com.example.spgtu.dao.entities.custom.Dorm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DormRepo extends JpaRepository<Dorm, Long> {
//    List<Dorm> findByFullAndTypeRoom(boolean b, int typeRoom);

    List<Dorm> findByFullRoomIsFalseAndCountPlacesOrderById(int countPlace);
    List<Dorm> findByFullRoomIsFalse();
    List<Dorm> findAllByOrderById();
    Dorm findById(long id);

}

