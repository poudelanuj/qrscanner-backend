package com.anuj.qrscanner.model.db;

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
    private Long idUser;

    @Column(name = "transaction_value")
    private double transactionValue;

    @ManyToOne
    @JoinColumn(name = "sender_id_user")
    private User serderUser;

    @ManyToOne
    @JoinColumn(name = "receiver_id_user")
    private User receiverUser;

    @Basic
    @Column(name = "transaction_start_time", nullable = false)
    private Timestamp transactionStartTime;

    @Basic
    @Column(name = "transaction_accept_time", nullable = false)
    private Timestamp transactionAcceptTime;


    @Column(name = "pending")
    private boolean pending = true;


}
