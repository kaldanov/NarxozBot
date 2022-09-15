package com.example.spgtu.dao.repositories;

import com.example.spgtu.dao.entities.custom.RequestApplicant;
import com.example.spgtu.dao.entities.custom.WithoutPreparationCourses;
import com.example.spgtu.dao.entities.standart.Button;
import com.example.spgtu.dao.entities.standart.User;
import com.example.spgtu.dao.enums.TypeReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface RequestApplicantRepo extends JpaRepository<RequestApplicant, Long> {

    RequestApplicant findAllByPreparationCoursesSenderAndTypeReference(User sender, TypeReference type);
    RequestApplicant findById(long id);

    List<RequestApplicant> findAllByAcceptedIsNullAndTypeReference(TypeReference type);
    List<RequestApplicant> findAllByDateBetween(Date start, Date end);

}
