package com.example.ShopAcc.service;

import com.example.ShopAcc.model.ProductDetail;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductDetailService {

    ProductDetail getProductdetailByID(int id);

    ProductDetail getProductDetailById(int id);
    List<ProductDetail> getAllProductDetails();
    Page<ProductDetail> getProductDetailsByProductId(int productId, int page, int size);
    void updateProductDetail(int id, ProductDetail productDetail);
    void deleteProductDetail(int id);
    void addProductDetail(ProductDetail productDetail);
    ProductDetail getProductDetailBySeri(String seri);
}
