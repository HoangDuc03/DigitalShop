package com.example.ShopAcc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class DiscountDetailDTO {
    private Integer userID;
    private Integer id;
    private Integer discountID;
    private Integer quantity;
    private String value;
    private Boolean status;
}
