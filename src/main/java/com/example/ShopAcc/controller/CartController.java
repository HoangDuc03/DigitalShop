package com.example.ShopAcc.controller;

import com.example.ShopAcc.model.CartItem;
import com.example.ShopAcc.model.Product;
import com.example.ShopAcc.model.Wallet;
import com.example.ShopAcc.repository.ProductDetailRepository;
import com.example.ShopAcc.repository.WalletRepository;
import com.example.ShopAcc.service.CartItemService;
import com.example.ShopAcc.service.Impl.DiscountDetailServiceImpl;
import com.example.ShopAcc.service.Impl.DiscountServiceImpl;
import com.example.ShopAcc.service.Impl.ProductServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.util.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private ProductServiceImpl productServiceImpl;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private DiscountServiceImpl discountServiceImpl;
    @Autowired
    private DiscountDetailServiceImpl discountDetailServiceImpl;


    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") int productId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        int currentAvailable = productDetailRepository.countByIDAndStatus(productId);
        System.out.println("Current Quantity of ProductType have Type.ID = " + productId + " in Warehouse: " + currentAvailable);
        int result = cartItemService.addToCart(productId, userId, currentAvailable);
        if(result == 1) {
            return "redirect:/cart/view";
        }
        return "redirect:/Product?errorWarehouseQuantity";
    }

    @PostMapping("/updateQuantity")
    public String updateQuantity(@RequestParam("cartItemId") int cartItemId,
                                 @RequestParam("quantity") int quantity,
                                 @RequestParam("productId") int productId,
                                 HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        int available = productDetailRepository.countByIDAndStatus(productId);
        int result = cartItemService.updateQuantity(cartItemId, quantity, available);

        if (result == 1) {
            return "redirect:/cart/view?OutOfStock";
        } else if (result == 2) {
            return "redirect:/cart/view?cartnotfound";
        }
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

    @GetMapping("/view")
    public String viewCart(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        List<Integer> discountIDList = discountServiceImpl.findDiscountIDbyUserID(userId);
        List<Integer> discountDetailIDList = discountDetailServiceImpl.findDiscountDetailIDbyDiscountID(discountIDList);
        List<Map<String, Double>> displayValueList = new ArrayList<>();
        for (Integer detailID : discountDetailIDList) {
            String value = discountDetailServiceImpl.findValueStringByDiscountDetailID(detailID);
            double discount = parsePercentage(value);
            Map<String, Double> displayValueMap = new HashMap<>();
            displayValueMap.put(value, discount);
            displayValueList.add(displayValueMap);
        }
        model.addAttribute("displayValueList", displayValueList);
        Wallet wallet = walletRepository.findByAccountid(userId);
        int currentWallet = wallet.getWallet();
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String walletFormatted = formatter.format(currentWallet);
        //System.out.println("UserID: " + userId + ". Have current wallet: " + walletFormatted);
        model.addAttribute("currentWallet", walletFormatted);
        List<CartItem> cartItems = cartItemService.getAllCartItemsByUserId(userId);
        List<Map<String, Object>> cartItemDetails = new ArrayList<>();
        double totalCartPrice = 0;

        for (CartItem cartItem : cartItems) {
            Map<String, Object> itemDetail = new HashMap<>();
            itemDetail.put("id", cartItem.getId());
            itemDetail.put("productId", cartItem.getProductId());
            itemDetail.put("quantity", cartItem.getQuantity());

            Product product = productServiceImpl.getProductByID_Q(cartItem.getProductId());
            int currentAvailableC = productDetailRepository.countByIDAndStatus(cartItem.getProductId());

            if (product != null) {
                itemDetail.put("currentAvailable", currentAvailableC);
                int price = product.getPrice();
                itemDetail.put("price", price);
                itemDetail.put("totalPrice", cartItem.getQuantity() * price);
                totalCartPrice += cartItem.getQuantity() * price;
            }

            cartItemDetails.add(itemDetail);
        }

        model.addAttribute("cartItems", cartItemDetails);
        model.addAttribute("totalCartPrice", totalCartPrice);

        return "cart_view";
    }

    private double parsePercentage(String percentage) {
        String numericPart = percentage.replace("%", "");
        double value = Double.parseDouble(numericPart);
        return value / 100;
    }
}
