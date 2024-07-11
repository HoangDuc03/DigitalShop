package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.model.ProductDetail;
import com.example.ShopAcc.repository.ProductDetailRepository;
import com.example.ShopAcc.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Override
    public ProductDetail getProductDetailById(int id) {
        System.out.println("Product ID in Service: " + id);
        ProductDetail productDetail = productDetailRepository.findById(id);
        if (productDetail != null) {
            System.out.println("Product Detail retrieved: " + productDetail);
        } else {
            System.out.println("Product Detail is null");
        }
        return productDetail;
    }
}
