package com.example.ShopAcc.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Productdetail")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "Productid")
    private Product product;

    @Column(name = "Seri")
    private String seri;

    @Column(name="Code")
    private String code;
    @Override
    public String toString() {
        return "ProductDetail{" +
                "id=" + id +
                ", warehouseID=" + product.getID() +
                ", seri='" + seri + '\'' +
                '}';
    }
}
