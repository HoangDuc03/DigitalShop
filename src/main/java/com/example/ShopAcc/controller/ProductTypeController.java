//package com.example.ShopAcc.controller;
//
//import com.example.ShopAcc.dto.ProductTypeDTO;
//import com.example.ShopAcc.dto.UserDto;
//import com.example.ShopAcc.service.ProductTypeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.SessionAttributes;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("")
//@SessionAttributes("userdto")
//public class ProductTypeController {
//    @Autowired
//    ProductTypeService productTypeService;
//
//    @ModelAttribute("userdto")
//    public UserDto userDto(){
//        return new UserDto();
//    }
//
//    @GetMapping("/Product")
//    public String showProductTypes(Model model) {
//        List<ProductTypeDTO> productTypes = productTypeService.getAllProductTypes();
//        model.addAttribute("productTypes", productTypes);
//        return "product";
//    }
//}
