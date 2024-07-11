package com.example.ShopAcc.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private int id;
    private String name;
    private int price;
    private int sold;
    private String image;
    private String describes;
    private boolean status;
}