package com.example.spgtu.dao.repositories;

import com.example.spgtu.dao.entities.custom.ChosenDirection;
import com.example.spgtu.dao.entities.standart.Button;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ChosenDirectionRepo extends JpaRepository<ChosenDirection, Long> {

}
