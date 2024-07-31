package com.example.ShopAcc.controller;

import com.example.ShopAcc.model.User;
import com.example.ShopAcc.model.WalletHistory;
import com.example.ShopAcc.repository.WallethistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class WalletHistoryAdminController {

    private final WallethistoryRepository walletHistoryRepo;

    @GetMapping("/viewWalletHistory")
    public String showWalletHistory(Model model,
                                    @RequestParam(value = "message", required = false) String message,
                                    @RequestParam(defaultValue = "0") String page,
                                    @RequestParam(defaultValue = "5") int size,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date date,
                                    @RequestParam(required = false) Boolean searched,
                                    HttpSession session) {
        User user = (User) session.getAttribute("infor");
        if (user == null || user.getRoleID().getRoleID() != 1) {
            return "redirect:/home";
        }
        int pageNum;
        try {
            pageNum = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            return "redirect:/admin/viewWalletHistory?page=0";
        }
        if (pageNum < 0) {
            return "redirect:/admin/viewWalletHistory?page=0";
        }
        Pageable pageable = PageRequest.of(pageNum, size);
        Page<WalletHistory> walletHistories;
        if (date != null) {
            walletHistories = walletHistoryRepo.findAllByDate(date, pageable);
        } else {
            walletHistories = walletHistoryRepo.findAll(pageable);
        }
        if (walletHistories.isEmpty() && pageNum > 0) {
            return "redirect:/admin/viewWalletHistory?page=0" + (date != null ? "&date=" + date : "");
        }
        model.addAttribute("walletHistories", walletHistories.getContent());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", walletHistories.getTotalPages());
        model.addAttribute("totalItems", walletHistories.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("date", date);
        if (message != null) {
            model.addAttribute("message", message);
        }
        if (searched != null && searched && walletHistories.isEmpty()) {
            model.addAttribute("message", "There is no wallet history that matches your search");
        }
        List<WalletHistory> walletHistoriesList = walletHistories.getContent();
        if (!walletHistoriesList.isEmpty()) {
            session.setAttribute("walletHistoryId", walletHistoriesList.get(0).getId());
        }
        return "viewWalletHistory";
    }
}
