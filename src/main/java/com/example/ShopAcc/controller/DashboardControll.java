package com.example.ShopAcc.controller;

import com.example.ShopAcc.repository.PurchaseHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class DashboardControll {
    @Autowired
    PurchaseHistoryRepo purchaseHistoryRepo;
    @GetMapping("/dashboard")
    public String dashboard(Model model){
        int earn  = purchaseHistoryRepo.Earning();
        int earnyear  = purchaseHistoryRepo.EarningInYear();

        int Allsold = purchaseHistoryRepo.AllpurchaseProduct();
        int SoldInMonth = purchaseHistoryRepo.PurchaseProductInMonth();
        int SoldInYear = purchaseHistoryRepo.PurchaseProductInYear();

        model.addAttribute("earn",earn);
        model.addAttribute("earnyear",earnyear);

        model.addAttribute("Sold",Allsold);
        model.addAttribute("SoldInMonth",SoldInMonth);
        model.addAttribute("SoldInYear",SoldInYear);

        List<Integer> month = new ArrayList<>();
        List<Integer> earnx = new ArrayList<>();
        for(int i=1;i<=12;i++)
        {
            month.add(i);
            earnx.add(purchaseHistoryRepo.PurchaseProductInMonth(i).orElse(0));
        }

        model.addAttribute("chartMonth",month);
        model.addAttribute("chartEarn",earnx);
        return  "dashboard";
    }
}
