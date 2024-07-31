package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.UserDto;
import com.example.ShopAcc.model.Product;
import com.example.ShopAcc.service.ProductService;
import com.example.ShopAcc.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductDetailController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/ProductDetail/detail")
    public String getProductDetail(@RequestParam("id") int productId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        boolean isLoggedIn = (session != null && session.getAttribute("infor") != null);
        model.addAttribute("isLoggedIn", isLoggedIn);

        if (!isLoggedIn) {
            // Store the current URL in the session
            String currentUrl = request.getRequestURI() + "?" + request.getQueryString();
            assert session != null;
            session.setAttribute("redirectAfterLogin", currentUrl);
        }

        // Get product details from the service and add to model
        Product productDetail = productService.getProductByID(productId);
        if(productDetail.isStatus() == false ) return "redirect:/Product";
        model.addAttribute("productDetail", productDetail);

        // Get current user info from service and add to model
        UserDto userDto = userService.getCurrentUserDto();
        model.addAttribute("userdto", userDto);

        return "product_detail"; // Return the Thymeleaf template for product details
    }
}
