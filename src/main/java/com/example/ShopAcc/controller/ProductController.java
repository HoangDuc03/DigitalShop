package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.ProductDto;
import com.example.ShopAcc.dto.ResponseObject;
import com.example.ShopAcc.dto.UserDto;
import com.example.ShopAcc.model.Product;
import com.example.ShopAcc.repository.ProductRepository;
import com.example.ShopAcc.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

//@SessionAttributes("userdto")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @ModelAttribute("userdto")
    public UserDto userDto() {
        return new UserDto();
    }

    @GetMapping("/Product")
    public String Productshow(@ModelAttribute("userdto") UserDto userDto,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "4") int size,
                              @RequestParam(defaultValue = "asc") String sort,
                              @RequestParam(defaultValue = "") String priceRange,
                              Model model) {
        Page<Product> products = productService.getProducts(search, page, size, sort, priceRange);
        System.out.println("Hello: " + products.getContent());
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("priceRange", priceRange);
        return "product";
    }

    @GetMapping("/manage-product")
    public String manageProduct(@ModelAttribute("userdto") UserDto userDto,
                                @RequestParam(defaultValue = "") String search,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "4") int size,
                                @RequestParam(defaultValue = "asc") String sort,
                                @RequestParam(defaultValue = "true") boolean status,
                                Model model) {
        Page<Product> products = productService.getProductsByStatus(search, page, size, sort, status);
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("size", size);
        model.addAttribute("status", status);
        return "manage-product";
    }

    @GetMapping("/delete-product/{productId}")
    public String deleteProduct(@PathVariable Long productId, RedirectAttributes redirectAttributes) {
        productService.softDelete(Math.toIntExact(productId));
        redirectAttributes.addFlashAttribute("successMessage", "Success!");
        return "redirect:/manage-product";
    }

    @GetMapping("/edit-product/{id}")
    public String showEditProductForm(@PathVariable int id, Model model) {
        Product product = productService.getProductByID(id);
        model.addAttribute("product", product);
        return "edit-product";
    }

    // POST request to handle form submission
    @PostMapping("/edit-product/{id}")
    public String handleEditProductForm(@PathVariable int id, @ModelAttribute("product") Product updatedProduct, RedirectAttributes redirectAttributes) {
        Product product = productService.getProductByID(id);
        if (product != null) {
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            product.setSold(updatedProduct.getSold());
            product.setImage(updatedProduct.getImage());
            product.setDescribes(updatedProduct.getDescribes());
            product.setStatus(updatedProduct.isStatus());
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Update product Success!");
            return "redirect:/manage-product";
        }
        return "redirect:/manage-product";
    }
    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute("newProduct") Product newProduct ,RedirectAttributes redirectAttributes) {
        productService.saveProduct(newProduct);
        redirectAttributes.addFlashAttribute("successMessage", "Update product Success!");
        return "redirect:/manage-product";
    }
}
