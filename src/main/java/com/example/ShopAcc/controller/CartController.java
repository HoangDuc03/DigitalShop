package com.example.ShopAcc.controller;

import com.example.ShopAcc.repository.ProductDetailRepository;
import com.example.ShopAcc.service.CartItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") int productId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        cartItemService.addToCart(productId, userId);
        return "redirect:/cart/view";
    }

    @PostMapping("/updateQuantity")
    public String updateQuantity(@RequestParam("cartItemId") int cartItemId,
                                 @RequestParam("quantity") int quantity, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        cartItemService.updateQuantity(cartItemId, quantity);
        return "redirect:/cart/view";
    }

    @PostMapping("/remove")
    public String removeCartItem(@RequestParam("cartItemId") int cartItemId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        cartItemService.deleteCartItem(cartItemId);
        return "redirect:/cart/view";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        cartItemService.clearCart(userId);
        return "redirect:/Product";
    }

    @PostMapping("/purchase")
    public String purchaseCart(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        // Perform the purchase operation
        // cartItemService.purchaseCart(userId);

        return "redirect:/purchase/confirmation";
    }

    @GetMapping("/view")
    @ResponseBody
    public String viewCart(Model model, HttpSession session) {
        return "view";
    }
}
