package com.example.spgtu.dao.repositories;

import com.example.spgtu.dao.entities.standart.Properties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public interface PropertiesRepo extends JpaRepository<Properties, Long> {
    Properties findById(long id);
}
