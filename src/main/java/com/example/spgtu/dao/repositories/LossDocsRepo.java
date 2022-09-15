package com.example.spgtu.dao.repositories;


import com.example.spgtu.dao.entities.custom.LossDocs;
import com.example.spgtu.dao.entities.standart.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LossDocsRepo extends JpaRepository<LossDocs, Long> {
    List<LossDocs> findAllByDateBetween(Date start, Date end);
}
