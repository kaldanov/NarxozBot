package com.example.spgtu.dao.repositories;

import com.example.spgtu.dao.entities.custom.Direction;
import com.example.spgtu.dao.entities.custom.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SubjectRepo extends JpaRepository<Subject, Long> {
    Subject findById(long id);

}
