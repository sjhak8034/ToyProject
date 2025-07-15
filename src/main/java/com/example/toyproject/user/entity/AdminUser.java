package com.example.toyproject.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "admin_users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AdminUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public AdminUser(String name, User user) {
        this.name = name;
        this.user = user;
    }
}
