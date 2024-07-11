package com.example.ShopAcc.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "Username", nullable = false)
    private String userName;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Fullname", nullable = true)
    private String fullName;

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "Image", nullable = false)
    private String image;

    @Column(name = "Phone", nullable = false)
    private String phoneNumber;

    @Column(name = "Createdat", nullable = false)
    private String createdAt;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "Roleid")
    private Role roleID;

    @Column(name = "Status", nullable = false)
    private boolean status;
}
