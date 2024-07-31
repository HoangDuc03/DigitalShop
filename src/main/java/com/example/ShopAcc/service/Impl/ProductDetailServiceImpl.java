package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.model.ProductDetail;
import com.example.ShopAcc.repository.ProductDetailRepository;
import com.example.ShopAcc.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


import java.util.List;
import org.springframework.data.domain.PageRequest;



@Service
public class ProductDetailServiceImpl implements ProductDetailService {
    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Override
    public ProductDetail getProductdetailByID(int id) {
        for (ProductDetail productDetail : productDetailRepository.findAll()) {
            if(productDetail.getId() == id)
                return productDetail;
        }
        return null;
    }

    @Override
    public ProductDetail getProductDetailById(int id) {
        for (ProductDetail productDetail : productDetailRepository.findAll()) {
            if(productDetail.getId() == id)
                return productDetail;
        }
        return null;
    }

    @Override
    public List<ProductDetail> getAllProductDetails() {
        return productDetailRepository.findAll();
    }

    @Override
    public Page<ProductDetail> getProductDetailsByProductId(int productId, int page, int size) {
        return productDetailRepository.findByProductid(productId, PageRequest.of(page, size));
    }

    @Override
    public void updateProductDetail(int id, ProductDetail productDetail) {
        ProductDetail existingProductDetail = productDetailRepository.findById(id);
        existingProductDetail.setSeri(productDetail.getSeri());
        existingProductDetail.setCode(productDetail.getCode());
        productDetailRepository.save(existingProductDetail);
    }
    @Override
    public void deleteProductDetail(int id) {
        productDetailRepository.deleteById(id);
    }

    @Override
    public void addProductDetail(ProductDetail productDetail) {
        productDetailRepository.save(productDetail);
    }

    @Override
    public ProductDetail getProductDetailBySeri(String seri) {
        return productDetailRepository.findBySeri(seri);
    }

}
