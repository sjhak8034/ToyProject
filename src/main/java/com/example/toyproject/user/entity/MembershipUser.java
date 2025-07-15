package com.example.toyproject.user.entity;

import com.example.toyproject.common.enums.BankType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MembershipUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BankType bankType;

    private String accountNumber;

    private String accountHolder;

    private Double credit;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public MembershipUser(BankType bankType, String accountNumber, String accountHolder, Double credit, User user) {
        this.bankType = bankType;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.credit = credit;
        this.user = user;
    }
}
