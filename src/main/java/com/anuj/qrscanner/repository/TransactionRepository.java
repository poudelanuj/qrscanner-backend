package com.anuj.qrscanner.repository;

import com.anuj.qrscanner.model.db.Transaction;
import com.anuj.qrscanner.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllBySenderUser(User user);

    List<Transaction> findAllByReceiverUser(User user);

}
