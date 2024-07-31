package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.dto.ProductDto;
import com.example.ShopAcc.model.Product;
import com.example.ShopAcc.repository.ProductRepository;
import com.example.ShopAcc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir");

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
        Sort sort = Sort.by("ID");
        sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? sort.ascending() : sort.descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return productRepository.findProductByStatusEqualsAndNameContaining(status, search, pageRequest);
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
        try {
            StringBuilder fileUrl = new StringBuilder();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY + uploadDir, product.getFile().getOriginalFilename());
            fileUrl.append(product.getFile().getOriginalFilename());
            Files.write(fileNameAndPath, product.getFile().getBytes());
            product.setImage(fileUrl.toString());
        } catch (IOException e) {
            System.out.println(e);
        }
        return productRepository.save(product);
    }
    @Override
    public Product saveProduct(Product product, MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                StringBuilder fileUrl = new StringBuilder();
                Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY + uploadDir, file.getOriginalFilename());
                Files.write(fileNameAndPath, file.getBytes());
                fileUrl.append(file.getOriginalFilename());
                product.setImage(fileUrl.toString());
                System.out.println("File saved to: " + fileNameAndPath.toString());
            }
        } catch (IOException e) {
            System.out.println(e);
        }
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

    @Override
    public Product getProductByID_Q(int id) {
        for (Product product : productRepository.findAll()) {
            if(product.getID() == id)
                return product;
        }
        return null;
    }

    @Override
    public String getProductnameByID(int id) {
        for (Product product : productRepository.findAll()) {
            if(product.getID() == id)
                return product.getName();
        }
        return null;
    }

    @Override
    public int countRemainProduct(String name) {
        return productRepository.countByNameAndStatus(name);
    }

    @Override
    public void updateSoldQuantity(List<Integer> productIDs, List<Integer> quantities) {
        for (int i = 0; i < productIDs.size(); i++) {
            int productId = productIDs.get(i);
            int quantity = quantities.get(i);
            productRepository.updateSoldQuantity(productId, quantity);
        }
    }


}