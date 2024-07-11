package com.example.ShopAcc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductTypeDTO {
    private int id;
    private String productName;
    private int price;
    private String describe;
    private int sold;
}
