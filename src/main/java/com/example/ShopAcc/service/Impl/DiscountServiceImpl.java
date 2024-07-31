package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountServiceImpl {
    @Autowired
    private DiscountRepository discountRepository;

    public List<Integer> findDiscountIDbyUserID(int userId) {
        return discountRepository.findDiscountIDbyUserID(userId);
    }
}
