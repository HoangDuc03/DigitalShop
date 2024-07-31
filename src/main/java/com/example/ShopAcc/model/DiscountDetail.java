package com.example.ShopAcc.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "Discountdetail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "DiscountID")
    private int discountID;

    @Column(name="Quantity")
    private int quantity;

    @Column(name="Value")
    private String value;

    @Column(name="Status")
    private boolean status;

}

