package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.UserDto;
import com.example.ShopAcc.model.ProductDetail;
import com.example.ShopAcc.service.ProductDetailService;
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
    private ProductDetailService productDetailService;

    @Autowired
    private UserService userService;

    @GetMapping("/ProductDetail/detail")
    public String getProductDetail(@RequestParam("id") int productId, Model model, HttpServletRequest request) {
        // Kiểm tra session để xác định trạng thái đăng nhập của người dùng
        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("infor") != null);

        // Đánh dấu trạng thái đăng nhập trong model
        model.addAttribute("isLoggedIn", isLoggedIn);

        // Lấy chi tiết sản phẩm từ service và đưa vào model
        ProductDetail productDetail = productDetailService.getProductDetailById(productId);
        model.addAttribute("productDetail", productDetail);

        // Lấy thông tin người dùng từ service và đưa vào model
        UserDto userDto = userService.getCurrentUserDto(); // Đây là ví dụ method để lấy thông tin người dùng hiện tại
        model.addAttribute("userdto", userDto);

        // Thêm request URI vào model (nếu cần thiết)
        String requestURI = request.getRequestURI();
        model.addAttribute("requestURI", requestURI);

        return "product_detail"; // Trả về tên template Thymeleaf để hiển thị chi tiết sản phẩm
    }

}
