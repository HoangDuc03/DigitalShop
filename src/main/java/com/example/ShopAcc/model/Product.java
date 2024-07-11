package com.example.ShopAcc.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Product")

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int ID;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "Sold")
    private int Sold;

    @Column(name = "image")
    private String image;

    @Column(name = "Describes")
    private String Describes;

    @Column(name = "status")
    private boolean status;
}

