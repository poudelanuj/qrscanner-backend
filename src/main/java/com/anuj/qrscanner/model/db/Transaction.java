package com.anuj.qrscanner.model.db;

import com.anuj.qrscanner.constant.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

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
    @JoinColumn(name = "sender_id_user")
    private User senderUser;

    @ManyToOne
    @JoinColumn(name = "receiver_id_user")
    private User receiverUser;

    @Basic
    @Column(name = "transaction_start_time", nullable = false)
    private Timestamp transactionStartTime;

    @Basic
    @Column(name = "transaction_accept_time", nullable = false)
    private Timestamp transactionAcceptTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;


}
