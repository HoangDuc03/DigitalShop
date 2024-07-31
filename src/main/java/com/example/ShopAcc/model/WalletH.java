package com.example.ShopAcc.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Wallethistory")
public class WalletH {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ID")
    private int ID;

    @Column(name = "Walletid")
    private int walletid;

    @Column(name = "Money")
    private int money;

    @Column(name = "date")
    private String date;

    @Column(name = "`Describe`")
    private String describe;

}