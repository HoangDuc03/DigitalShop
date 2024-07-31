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
@Table(name = "Rewardslist")
public class RewardsList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ID")
    private int ID;

    @Column(name = "Accountid")
    private int userid;

    @Column(name = "name")
    private String name;

    @Column(name= "Createdat")
    private String time;
}
