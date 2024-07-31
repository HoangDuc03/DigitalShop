package com.example.ShopAcc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Rewards")
public class Rewards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ID")
    private  int ID;

    @Column(name="`type`")
    private String type;
    @Column(name="`value`")
    private double value;
    @Column(name="Name")
    private String name;
    @Column(name="percent")
    private Double percent;
}
