package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.DiscountDetailDTO;
import com.example.ShopAcc.service.Impl.DiscountDetailServiceImpl;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private DiscountDetailServiceImpl discountDetailService;

    @GetMapping("/discounts")
    public String getAllDiscountDetails(Model model) {
        List<DiscountDetailDTO> discountDetails = discountDetailService.getAllDiscountDetailsWithUserID();
        model.addAttribute("discountDetails", discountDetails);
        return "admin_discount";
    }
}

