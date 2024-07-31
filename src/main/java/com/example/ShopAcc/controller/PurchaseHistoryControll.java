
package com.example.ShopAcc.controller;
import com.example.ShopAcc.model.Purchasehistory;
import com.example.ShopAcc.model.Purchasehistorydetail;
import com.example.ShopAcc.model.User;
import com.example.ShopAcc.repository.PurchaseHistoryDetailRepo;
import com.example.ShopAcc.repository.PurchaseHistoryRepo;
import com.example.ShopAcc.service.Impl.PurchaseHistoryDetailServiceImpl;
import com.example.ShopAcc.service.Impl.PurchaseHistoryServiceImpl;
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
    private int productid;
    @Autowired
    PurchaseHistoryRepo phRepo;
    @Autowired
    PurchaseHistoryDetailRepo phdRepo;
    @Autowired
    private PurchaseHistoryDetailServiceImpl purchaseServiceImpl;
    @Autowired
    private PurchaseHistoryServiceImpl purchaseHistoryServiceImpl;

    @GetMapping("/profile/purchasehistory")
    public String DisplayPurchaseHistory(Model model, HttpSession session, @RequestParam(required = false) String name, @RequestParam("page") Optional<Integer> p){

        if(session.getAttribute("infor") == null)  {
            return "redirect:/home";
        }

        int pg = p.orElse(0);
        Page<Purchasehistory> list;
        if(pg < 0){
            name=null;
            pg =0;
        }
        while(true)
        {
            Pageable pageale = PageRequest.of(pg,5);
            User user = (User) session.getAttribute("infor");
            int id = user.getId();
            if(name == null)
            name="";

            list = phRepo.findAllByName(name,id,pageale);

            if(!list.isEmpty())
                break;
            if(pg < 0){
                name=null;
                pg++;
            }

            if(list.isEmpty() && pg==0) break;
            pg--;
        }
        model.addAttribute("list",list);
        model.addAttribute("page",pg);

        return "purchasehistory";
    }

    @GetMapping("/profile/purchasehistory/detail")
    public String DisplayPurchaseHistoryDetail(@RequestParam int productid, Model model){
        this.productid = productid;
        List<Purchasehistorydetail> list = phdRepo.findAllById(productid);

        Purchasehistory history = phRepo.getReferenceById(productid);

        model.addAttribute("list",list);
        model.addAttribute("title",history);
        return "purchasehistorydetail";
    }

    @GetMapping("/profile/purchasehistory/latestdetail")
    public String DisplayLatestPurchaseHistoryDetail(Model model){
        int latestPurchaseID = purchaseHistoryServiceImpl.findLatesID();
        System.out.println("latestPurchaseID: " + latestPurchaseID);
        List<Purchasehistorydetail> lst = purchaseServiceImpl.findLatestBill(latestPurchaseID);
        System.out.println("List Of Product in the latest history:");
        for (Purchasehistorydetail purchasehistorydetail : lst) {
            System.out.println(purchasehistorydetail.toString());
        }

        Purchasehistory history = phRepo.getReferenceById(latestPurchaseID);

        model.addAttribute("list",lst);
        model.addAttribute("title",history);
        return"purchasehistorydetail";
    }
}

