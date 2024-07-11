package com.example.ShopAcc.service.impl;

import com.example.ShopAcc.dto.ProductTypeDTO;
import com.example.ShopAcc.model.Product;
import com.example.ShopAcc.repository.ProductTypeRepository;
import com.example.ShopAcc.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductTypeServiceImpl extends ProductTypeService {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Override
    public List<ProductTypeDTO> getAllProductTypes() {
        List<Product> productTypes = productTypeRepository.findAll();
        return productTypes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ProductTypeDTO convertToDTO(Product productType) {
        return ProductTypeDTO.builder()
                .id(productType.getID())
                .productName(productType.getName())
                .price(productType.getPrice())
                .describe(productType.getDescribes())
                .sold(productType.getSold())
                .build();
    }
}
