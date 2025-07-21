package com.example.toyproject.user.entity;

import com.example.toyproject.common.entity.BaseTimeEntity;
import com.example.toyproject.common.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.time.LocalDateTime;

@DynamicInsert
@DynamicUpdate
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String nickname;

    private String name;

    private String password;

    @Lob
    private String picture;

    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String username, String nickname, String name, String email, String password, Role role, String picture) {
        this.username = username;
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.role = role;
    }

    public void update(String name, String picture){
        this.name = name;
        this.picture = picture;
    }

    public void delete(){
        this.deletedAt = LocalDateTime.now();
    }
}
