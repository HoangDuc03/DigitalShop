
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
    private int id;

    @Column(name = "`Code` ")
    private String code;

    @Column(name = "Seri")
    private String seri;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "Purchasehistoryid")

    private Purchasehistory Purchasehistory;

    @Override
    public String toString() {
        return "Purchasehistorydetail{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", seri='" + seri + '\'' +
                ", Purchasehistory=" + Purchasehistory +
                '}';
    }
}
