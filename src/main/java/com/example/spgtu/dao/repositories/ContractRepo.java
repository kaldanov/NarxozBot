package com.example.spgtu.dao.repositories;

import com.example.spgtu.dao.entities.custom.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepo extends JpaRepository<Contract, Long> {
    Contract findByChatId(long chatId);
}
