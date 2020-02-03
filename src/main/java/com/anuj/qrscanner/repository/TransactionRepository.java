package com.anuj.qrscanner.repository;

import com.anuj.qrscanner.model.db.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
