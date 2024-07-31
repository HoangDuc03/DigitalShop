package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.UserDto;
import com.example.ShopAcc.model.Product;
import com.example.ShopAcc.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("userdto")
@RequiredArgsConstructor
@RequestMapping("")
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
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("priceRange", priceRange);
        return "product";
    }

    @GetMapping("/admin/manage-product")
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

    @GetMapping("/admin/delete-product/{productId}")
    public String deleteProduct(@PathVariable Long productId, RedirectAttributes redirectAttributes) {
        productService.softDelete(Math.toIntExact(productId));
        redirectAttributes.addFlashAttribute("successMessage", "Success!");
        return "redirect:/admin/manage-product";
    }

    @GetMapping("/admin/edit-product/{id}")
    public String showEditProductForm(@PathVariable int id, Model model) {
        Product product = productService.getProductByID(id);
        model.addAttribute("product", product);
        return "edit-product";
    }

    @PostMapping("/admin/edit-product/{id}")
    public String handleEditProductForm(@PathVariable int id,
                                        @ModelAttribute("product") Product updatedProduct,
                                        @RequestParam("file") MultipartFile file,
                                        RedirectAttributes redirectAttributes) {
        Product product = productService.getProductByID(id);
        if (product != null) {
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            product.setSold(updatedProduct.getSold());
            product.setDescribes(updatedProduct.getDescribes());
            product.setStatus(updatedProduct.isStatus());

            productService.saveProduct(product, file);

            redirectAttributes.addFlashAttribute("successMessage", "Update product Success!");
            return "redirect:/admin/manage-product";
        }
        return "redirect:/admin/manage-product";
    }

    @PostMapping("/admin/add-product")
    public String addProduct(@ModelAttribute("newProduct") Product newProduct,
                             @RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) {
        productService.saveProduct(newProduct, file);
        redirectAttributes.addFlashAttribute("successMessage", "Product added successfully!");
        return "redirect:/admin/manage-product";
    }
}
