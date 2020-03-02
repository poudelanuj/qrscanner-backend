package com.anuj.qrscanner.model.db;

import com.anuj.qrscanner.constant.TransactionStatus;
import com.anuj.qrscanner.constant.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
public class Transaction {

    @Id
    @Column(name="id_transaction")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransaction;

    @Column(name = "transaction_value")
    private double transactionValue;

    @ManyToOne
    @JoinColumn(name = "source_id_user")
    private User sourceUser;

    @ManyToOne
    @JoinColumn(name = "destination_id_user")
    private User destinationUser;

    @Basic
    @Column(name = "transaction_start_date", nullable = false)
    private Date transactionStartDate;

    @Basic
    @Column(name = "transaction_accept_date")
    private Date transactionAcceptDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

}
