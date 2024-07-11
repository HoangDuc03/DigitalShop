package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.UserDto;
import com.example.ShopAcc.model.Product;
import com.example.ShopAcc.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/*
- Get user through class UserDto and return to home for manipulations
 */

@Controller
@AllArgsConstructor
@SessionAttributes("userdto")
public class HomeController {
    @ModelAttribute("userdto")
    public UserDto userDto(){
        return new UserDto();
    }
    @Autowired
    ProductService productService;
    @GetMapping("/home")
    public String showHomeForm(@ModelAttribute("userdto") UserDto userDto,
                               @RequestParam(defaultValue = "") String search,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "4") int size,
                               @RequestParam(defaultValue = "asc") String sort,
                               @RequestParam(defaultValue = "") String priceRange,
                               Model model)  {
        Page<Product> products = productService.getProducts(search, page, size, sort, priceRange);
        System.out.println(products.getContent());
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("priceRange", priceRange);

        return "/home";
    }
    @GetMapping("")
    public String HomeForm(@ModelAttribute("userdto") UserDto userDto,Model model){
        return "/home";
    }
}
