package com.example.ShopAcc.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObject {
    private String status;
    private String message;
    private Object data;
}
