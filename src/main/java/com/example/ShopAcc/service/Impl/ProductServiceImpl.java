package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.dto.ProductDto;
import com.example.ShopAcc.model.Product;
import com.example.ShopAcc.repository.ProductRepository;
import com.example.ShopAcc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getProducts(String search, int page, int size, String sortDirection, String priceRange) {
        Sort sort = Sort.by("price");
        sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? sort.ascending() : sort.descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        int minPrice = 0;
        int maxPrice = Integer.MAX_VALUE;

        if (priceRange != null && !priceRange.isEmpty()) {
            String[] priceBounds = priceRange.split("-");
            if (!priceBounds[0].isEmpty()) {
                minPrice = Integer.parseInt(priceBounds[0]);
            }
            if (priceBounds.length > 1 && !priceBounds[1].isEmpty()) {
                maxPrice = Integer.parseInt(priceBounds[1]);
            }
        }

        if (maxPrice == Integer.MAX_VALUE) {
            return productRepository.findProductTypeByStatusEqualsAndNameContainingAndPriceGreaterThanEqual(true, search, minPrice, pageRequest);
        } else if (minPrice == 0) {
            return productRepository.findProductTypeByStatusEqualsAndNameContainingAndPriceLessThanEqual(true, search, maxPrice, pageRequest);
        } else {
            return productRepository.findProductTypeByStatusEqualsAndNameContainingAndPriceBetween(true, search, minPrice, maxPrice, pageRequest);
        }
    }

    @Override
    public Page<Product> getProductsByStatus(String search, int page, int size, String sortDirection, boolean status) {
        Sort sort = Sort.by("price");
        sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? sort.ascending() : sort.descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return productRepository.findProductTypeByStatusEqualsAndNameContaining(status, search, pageRequest);
    }

    @Override
    public Product createProduct(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .Sold(productDto.getSold())
                .image(productDto.getImage())
                .Describes(productDto.getDescribes())
                .status(productDto.isStatus())
                .build();

        return productRepository.save(product);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void softDelete(int id) {
        Optional<Product> existingProductOpt = productRepository.findById(id);
        if (existingProductOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found: " + id);
        }

        Product existingProduct = existingProductOpt.get();
        existingProduct.setStatus(!existingProduct.isStatus());
        productRepository.save(existingProduct);
    }

    @Override
    public List<Product> filterByStatus(boolean status) {
        return productRepository.findByStatus(status);
    }

    @Override
    public Product getProductByID(int id) {
        return productRepository.findProductByID(id);
    }
}
