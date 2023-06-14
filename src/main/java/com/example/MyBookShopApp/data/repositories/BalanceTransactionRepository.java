package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.payments.BalanceTransaction;
import com.example.MyBookShopApp.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction, Integer> {
    List<BalanceTransaction> findAllByUser(User user);
}
