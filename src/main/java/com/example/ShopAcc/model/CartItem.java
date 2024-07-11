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
@Table(name = "Cart")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "Productid")
    private Product product;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "Accountid")
    private User user;

    @Column(name="Quantity")
    private int quantity;

}
