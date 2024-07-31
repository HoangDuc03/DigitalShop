package com.example.ShopAcc.service;

import com.example.ShopAcc.dto.ProductDto;
import com.example.ShopAcc.dto.ResponseObject;
import com.example.ShopAcc.model.Product;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<Product> getAll();


    Page<Product> getProducts(String search, int page, int size, String sortDirection, String priceRange);

    Page<Product> getProductsByStatus(String search, int page, int size, String sortDirection, boolean status);

    Product createProduct(ProductDto productDto);

    Product saveProduct(Product product);
    Product saveProduct(Product product, MultipartFile file);

    void softDelete(int id);

    List<Product> filterByStatus(boolean status);

    Product getProductByID(int id);

    Product getProductByID_Q(int id);

    String getProductnameByID(int id);
    int countRemainProduct(String name);

    void updateSoldQuantity(List<Integer> productID, List<Integer> quantities);


}
