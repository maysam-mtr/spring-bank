package com.example.bankingApp.repositories;

import com.example.bankingApp.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for account class
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
