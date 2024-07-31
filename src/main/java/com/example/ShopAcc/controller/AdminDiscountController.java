package com.example.ShopAcc.controller;

import com.example.ShopAcc.model.Discount;
import com.example.ShopAcc.model.DiscountDetail;
import com.example.ShopAcc.model.User;
import com.example.ShopAcc.repository.DiscountDetailRepository;
import com.example.ShopAcc.repository.DiscountRepository;
import com.example.ShopAcc.repository.UserRepository;
import com.example.ShopAcc.service.Impl.DiscountDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/discounts")
public class AdminDiscountController {

    @Autowired
    private DiscountDetailServiceImpl discountDetailService;
    @Autowired
    private DiscountDetailRepository discountDetailRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private UserRepository userRepository;

    // Display form for adding new discount detail
    @GetMapping("/new")
    public String showAddDiscountForm(Model model) {
        model.addAttribute("discountDetail", new DiscountDetail());
        return "add_discount_form"; // Assuming you have a Thymeleaf template for adding new discount
    }

    // Handle form submission for adding new discount detail
    @PostMapping("/new")
    public String addNewDiscount(@ModelAttribute("discountDetail") DiscountDetail discountDetail,
                                 @RequestParam("userId") int userId,
                                 Model model) {
        try {
            // Log the received userId for debugging purposes
            System.out.println("Received userId: " + userId);

            if (! userRepository.existsById(userId)) {
                // If the user does not exist, add an error message to the model
                model.addAttribute("errorMessage", "User ID " + userId + " does not exist.");
                // Return the form with the error message
                return "redirect:/admin/discounts?NotExisted";
            }
            // Create or retrieve the Discount entity
            Discount discount = discountRepository.findByUserId(userId)
                    .orElseGet(() -> {
                        Discount newDiscount = new Discount();
                        newDiscount.setUserId(userId);
                        return discountRepository.save(newDiscount);
                    });

            // Set the Discount ID in the DiscountDetail entity
            discountDetail.setDiscountID(discount.getId());

            // Append "%" if not already present
            if (!discountDetail.getValue().contains("%")) {
                discountDetail.setValue(discountDetail.getValue() + "%");
            }

            // Save the discount detail using the repository
            discountDetailRepository.save(discountDetail);

            return "redirect:/admin/discounts";
        } catch (Exception e) {
            // Log the error
            System.out.println(e.getMessage());

            // Add error message to the model to be displayed in the view
            model.addAttribute("errorMessage", "An error occurred while saving the discount detail. Please try again.");
            return "redirect:/admin/discounts";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditDiscountForm(@PathVariable("id") int id, Model model) {
        DiscountDetail discountDetail = discountDetailService.findDiscountDetailById(id);
        if (discountDetail == null) {
            return "redirect:/admin/discounts?error=notfound";
        }
        model.addAttribute("discountDetail", discountDetail);
        return "edit_discount_form"; // Thymeleaf template for editing discount details
    }

    @PostMapping("/edit/{id}")
    public String editDiscount(@PathVariable("id") int id, @ModelAttribute("discountDetail") DiscountDetail discountDetail) {
        // Append "%" if not already present
        if (!discountDetail.getValue().contains("%")) {
            discountDetail.setValue(discountDetail.getValue() + "%");
        }
        discountDetailService.updateDiscountDetail(id, discountDetail);
        return "redirect:/admin/discounts";
    }

    @GetMapping("/delete/{id}")
    public String deleteDiscount(@PathVariable("id") int id) {
        discountDetailService.deleteDiscountDetailById(id);
        return "redirect:/admin/discounts";
    }
}
