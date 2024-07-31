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
@Table(name = "Wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ID")
    private int ID;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "Accountid")
    private User Accountid;

    @Column(name = "Wallet")
    private int wallet;

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + ID +
                ", accountId=" + Accountid.getId() +
                ", wallet=" + wallet +
                '}';
    }
}
