package com.example.ShopAcc.controller;

import com.example.ShopAcc.model.Purchasehistory;
import com.example.ShopAcc.model.Purchasehistorydetail;
import com.example.ShopAcc.model.User;
import com.example.ShopAcc.repository.PurchaseHistoryDetailRepo;
import com.example.ShopAcc.repository.PurchaseHistoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class PurchaseHistoryAdminController {

    private final PurchaseHistoryRepo purchaseHistoryRepo;
    private final PurchaseHistoryDetailRepo purchaseHistoryDetailRepo;

    @GetMapping("/viewPurchaseHistory")
    public String showPurchaseList(Model model,
                                   @RequestParam(value = "message", required = false) String message,
                                   @RequestParam(defaultValue = "0") String page,
                                   @RequestParam(defaultValue = "5") int size,
                                   @RequestParam(required = false) String productName,
                                   @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date date,
                                   HttpSession session) {
        User user = (User) session.getAttribute("infor");
        if (user == null || user.getRoleID().getRoleID() != 1) {
            return "redirect:/home";
        }
        int pageNum;
        try {
            pageNum = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            return "redirect:/admin/viewPurchaseHistory?page=0";
        }
        Page<Purchasehistory> purchaseHistories = purchaseHistoryRepo.findAll(PageRequest.of(0, size));
        if (pageNum < 0) {
            return "redirect:/admin/viewPurchaseHistory?page=0";
        }
        if (pageNum >= purchaseHistories.getTotalPages()) {
            return "redirect:/admin/viewPurchaseHistory?page=" + (purchaseHistories.getTotalPages() - 1);
        }
        Pageable pageable = PageRequest.of(pageNum, size);
        if (productName == null) {
            productName = "";
        }
        if (date != null) {
            purchaseHistories = purchaseHistoryRepo.findAllByProductNameContainingAndPurchasedate(productName, date, pageable);
        } else {
            purchaseHistories = purchaseHistoryRepo.findAllByProductNameContaining(productName, pageable);
        }
        model.addAttribute("purchases", purchaseHistories.getContent());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", purchaseHistories.getTotalPages());
        model.addAttribute("totalItems", purchaseHistories.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("productName", productName);
        model.addAttribute("date", date);
        if (message != null) {
            model.addAttribute("message", message);
        }
        if (purchaseHistories.isEmpty()) {
            model.addAttribute("errorMessage", "No results found matching your search criteria.");
        }
        List<Purchasehistory> purchases = purchaseHistories.getContent();
        if (!purchases.isEmpty()) {
            session.setAttribute("purchaseId", purchases.get(0).getID());
        }
        return "viewPurchaseHistory";
    }

    @GetMapping("/detail")
    public String viewPurchaseDetail(Model model, @RequestParam int id) {
        Integer purchaseId = id;
        if (purchaseId == null) {
            return "redirect:/admin/viewPurchaseHistory?message=Purchase history not found";
        }
        Optional<Purchasehistory> purchaseOptional = purchaseHistoryRepo.findById(purchaseId);
        if (!purchaseOptional.isPresent()) {
            return "redirect:/admin/viewPurchaseHistory?message=Purchase history not found";
        }
        Purchasehistory purchasehistory = purchaseOptional.get();
        List<Purchasehistorydetail> purchasehistorydetails = purchaseHistoryDetailRepo.findAllByPurchasehistoryid(purchaseId);
        String fullName = "";
        if (purchasehistory.getUser() != null) {
            fullName = purchasehistory.getUser().getFullName();
        }
        model.addAttribute("purchase", purchasehistory);
        model.addAttribute("purchaseDetails", purchasehistorydetails);
        model.addAttribute("fullName", fullName);
        return "viewPurchaseDetail";
    }
}
