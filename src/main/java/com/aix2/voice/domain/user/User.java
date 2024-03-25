package com.aix2.voice.domain.user;

import com.aix2.voice.domain.time.DefaultTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Getter
@Table(name = "user")
public class User extends DefaultTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {}

    @Builder
    public User(String name, String email, String password, Role role){
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

}
