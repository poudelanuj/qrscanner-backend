package com.anuj.qrscanner.repository;

import com.anuj.qrscanner.constant.TransactionStatus;
import com.anuj.qrscanner.model.db.Transaction;
import com.anuj.qrscanner.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllBySourceUser(User user);

    List<Transaction> findAllByDestinationUser(User user);

    List<Transaction> findAllByTransactionStatus(TransactionStatus transactionStatus);

    List<Transaction> findAllBySourceUserOrDestinationUserOrderByTransactionStartDateDesc(User sourceUser, User destinationUser)  ;

}
