package com.example.ShopAcc.controller;
import com.example.ShopAcc.model.Purchasehistory;
import com.example.ShopAcc.model.Purchasehistorydetail;
import com.example.ShopAcc.model.User;
import com.example.ShopAcc.repository.PurchaseHistoryDetailRepo;
import com.example.ShopAcc.repository.PurchaseHistoryRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
//  profile/purchasehistory?id=1

@Controller
@RequestMapping("")
public class PurchaseHistoryControll {
    @Autowired
    PurchaseHistoryRepo phRepo;
    @Autowired
    PurchaseHistoryDetailRepo phdRepo;
    @GetMapping("/profile/purchasehistory")
    public String DisplayPurchaseHistory(Model model, HttpSession session, @RequestParam(required = false) String name, @RequestParam("page") Optional<Integer> p){

        if(session.getAttribute("infor") == null)  {
            return "redirect:/home";
        }

        int pg = p.orElse(0);
        Page<Purchasehistory> list;
        while(true)
        {
            Pageable pageale = PageRequest.of(pg,5);
            User user = (User) session.getAttribute("infor");
            int id = user.getId();
            if(name == null)
                list = phRepo.findAllByAccountid(id,pageale);
            else
                list = phRepo.findAllByName(name,id,pageale);
            pg--;

            if(!list.isEmpty())
                break;
            if(pg < 0){
                name=null;
                pg++;
            }
            if(list.isEmpty() && pg==0) break;
        }
        model.addAttribute("list",list);
        model.addAttribute("page",pg);

        return "purchasehistory";
    }

    @GetMapping("/profile/purchasehistory/detail")
    public String DisplayPurchaseHistoryDetail(@RequestParam int id, Model model){
        List<Purchasehistorydetail> list = phdRepo.findAllById(id);
        Purchasehistory history = phRepo.getReferenceById(id);
        model.addAttribute("list",list);
        model.addAttribute("title",history);
        return "purchasehistorydetail";
    }

}
