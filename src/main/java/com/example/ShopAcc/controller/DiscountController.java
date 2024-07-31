package com.example.ShopAcc.controller;

import com.example.ShopAcc.repository.DiscountDetailRepository;
import org.springframework.ui.Model;
import com.example.ShopAcc.service.Impl.DiscountDetailServiceImpl;
import com.example.ShopAcc.service.Impl.DiscountServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Discount")
public class DiscountController {

    double value;
    String displayValue;

    @Autowired
    private DiscountServiceImpl discountServiceImpl;

    @Autowired
    private DiscountDetailServiceImpl discountDetailServiceImpl;
    @Autowired
    private DiscountDetailRepository discountDetailRepository;

    @GetMapping("/discounts")
    public String getDiscounts(Model model, HttpSession session) {
        int userId = (int) session.getAttribute("userId");
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
        System.out.println("List of Key Value:");
        System.out.println(displayValueList);
        model.addAttribute("displayValueList", displayValueList);

        return "discounts"; // Thymeleaf view name
    }

    @PostMapping("/confirm")
    public String applyDiscount(@RequestParam("discountSelect") String discountValue,
                                @RequestParam("selectedDisplayValue") String displayValue,
                                Model model, HttpSession session) {
        int userId = (int) session.getAttribute("userId");
        double discount = Double.parseDouble(discountValue);
        System.out.println("discountValue: " + discount);
        this.value = discount;
        this.displayValue = displayValue;
        model.addAttribute("selectedDiscount", discount);
        model.addAttribute("displayValue", displayValue);
        List<Integer> discountIDList = discountServiceImpl.findDiscountIDbyUserID(userId);
        List<Integer> discountDetailIDList = discountDetailServiceImpl.findDiscountDetailIDbyDiscountID(discountIDList);
        for (Integer discountDetailID : discountDetailIDList) {
            String value = discountDetailServiceImpl.findValueStringByDiscountDetailID(discountDetailID);
            if(value.equals(this.displayValue)) {
                System.out.println("Found value: " + value + " Equal to: " + this.displayValue + " For detailID: " + discountDetailID);
                discountDetailRepository.updateDiscountQuantity(discountDetailID);
                break;
            }
        }
        return "confirmation"; // Return the view name for the confirmation page
    }

    private double parsePercentage(String percentage) {
        String numericPart = percentage.replace("%", "");
        double value = Double.parseDouble(numericPart);
        return value / 100;
    }
}



