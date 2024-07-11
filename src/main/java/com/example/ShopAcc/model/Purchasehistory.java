package com.example.ShopAcc.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Purchasehistory")
public class Purchasehistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ID")
    private int ID;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "Productid")
    private Product product;

    @Column(name = "price")
    private int price;

    @Column(name = "Date")
    private String purchasedate;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "Accountid")
    private User user;

}
