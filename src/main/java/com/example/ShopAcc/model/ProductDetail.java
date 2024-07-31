package com.example.ShopAcc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "productdetail")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "Productid")
    private int productid;

    @Column(name = "Seri")
    private String seri;

    @Column(name = "Code")
    private String code;

    @Column(name = "Status")
    private boolean status;
}

