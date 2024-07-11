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
@Table(name = "Purchasehistorydetail")
public class Purchasehistorydetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ID")
    private int ID;
    @Column(name = "`Code` ")
    private String Code;
    @Column(name = "Seri")
    private String Seri;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "Purchasehistoryid")
    private Purchasehistory Purchasehistory;
}
