package com.anuj.qrscanner.model.db;

import com.anuj.qrscanner.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id_user", updatable = false, columnDefinition = "BINARY(16)")
    private UUID idUser;


    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "password", unique = true)
    private String password;

    @Column(name = "current_balance")
    private double currentBalance;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role"))
    private Set<Role> roles = new HashSet<>();



}
