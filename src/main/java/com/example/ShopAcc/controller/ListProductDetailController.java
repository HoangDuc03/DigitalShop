package com.example.ShopAcc.controller;

import com.example.ShopAcc.model.Product;
import com.example.ShopAcc.model.ProductDetail;
import com.example.ShopAcc.service.ExcelProductDetailService;
import com.example.ShopAcc.service.ProductDetailService;
import com.example.ShopAcc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/admin/product-details")
public class ListProductDetailController {

    private static final Logger log = LoggerFactory.getLogger(ListProductDetailController.class);

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ExcelProductDetailService excelProductDetailService;

    @GetMapping("/product/{productId}")
    public String getProductDetailsByProductId(
            @PathVariable int productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        Page<ProductDetail> productDetailsPage = productDetailService.getProductDetailsByProductId(productId, page, size);
        Product product = productService.getProductByID(productId);
        model.addAttribute("productDetails", productDetailsPage.getContent());
        model.addAttribute("totalPages", productDetailsPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("productId", productId);
        model.addAttribute("productName", product.getName());
        model.addAttribute("size", size);
        return "list_product_detail";
    }

    @GetMapping("/add")
    public String addProductDetailForm(@RequestParam("productId") int productId, Model model) {
        Product product = productService.getProductByID(productId);
        if (product == null) {
            return "home"; // Redirect to products listing page or handle appropriately
        }
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProductid(product.getID());
        model.addAttribute("productDetail", productDetail);
        model.addAttribute("productName", product.getName());
        return "add_product_detail";
    }

    @PostMapping("/add")
    public String addProductDetail(@ModelAttribute("productDetail") ProductDetail productDetail, Model model) {
        ProductDetail existingProductDetail = productDetailService.getProductDetailBySeri(productDetail.getSeri());
        if (existingProductDetail != null) {
            model.addAttribute("message", "Product detail with the same seri already exists.");
            model.addAttribute("productDetail", productDetail);
            model.addAttribute("productName", productService.getProductByID(productDetail.getProductid()).getName());
            return "add_product_detail";
        }

        productDetailService.addProductDetail(productDetail);
        model.addAttribute("addSucces", "Add item successfully.");
        return "redirect:/admin/product-details/product/" + productDetail.getProductid();
    }


    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("productId") int productId, Model model) {

        model.addAttribute("productId", productId);
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload");
            return "redirect:/admin/product-details/product/" + productId;
        }

        try {
            excelProductDetailService.saveProductDetailsFromExcel(file, productId);
            model.addAttribute("message", "File uploaded successfully");
        } catch (IOException e) {
            model.addAttribute("message", "Error uploading file: " + e.getMessage());
            log.error("Error uploading file", e);  // Add logging here
        } catch (Exception e) {
            model.addAttribute("message", "An unexpected error occurred: " + e.getMessage());
            log.error("Unexpected error during file upload", e);
        }

        return "redirect:/admin/product-details/product/" + productId;
    }

    @GetMapping("/delete/{id}")
    public String deleteProductDetail(@PathVariable int id) {
        ProductDetail productDetail = productDetailService.getProductDetailById(id);
        productDetailService.deleteProductDetail(id);
        return "redirect:/admin/product-details/product/" + productDetail.getProductid();
    }

    @GetMapping("/edit/{id}")
    public String editProductDetail(@PathVariable int id, Model model) {
        ProductDetail productDetail = productDetailService.getProductDetailById(id);
        model.addAttribute("productDetail", productDetail);
        return "edit_product_detail";
    }

    @PostMapping("/edit/{id}")
    public String updateProductDetail(@PathVariable int id, @ModelAttribute("productDetail") ProductDetail productDetail, Model model) {
        ProductDetail existingProductDetail = productDetailService.getProductDetailBySeri(productDetail.getSeri());
        ProductDetail currentProductDetail = productDetailService.getProductDetailById(id);
        if (existingProductDetail != null && !(existingProductDetail.getId()==(currentProductDetail.getId()))) {
            model.addAttribute("message", "Product detail with the same seri already exists.");
            model.addAttribute("productDetail", productDetail);
            return "edit_product_detail";
        }

        productDetail.setId(id);
        productDetail.setProductid(currentProductDetail.getProductid());
        productDetailService.updateProductDetail(id, productDetail);
        return "redirect:/admin/product-details/product/" + productDetail.getProductid();
    }
}
