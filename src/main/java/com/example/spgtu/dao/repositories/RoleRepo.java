package com.example.spgtu.dao.repositories;


import com.example.spgtu.dao.entities.standart.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
//    long countByUserId(long newAdminChatId);
}
