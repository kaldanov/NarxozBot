package com.example.spgtu.dao.repositories;

import com.example.spgtu.dao.entities.custom.RequestDeductions;
import com.example.spgtu.dao.entities.custom.WithoutPreparationCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RequestDeductionsRepo extends JpaRepository<RequestDeductions, Long> {
    List<RequestDeductions> findAllByDateBetween(Date start, Date end);

}
