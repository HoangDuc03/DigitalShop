package com.example.ShopAcc.controller;

import com.example.ShopAcc.model.*;
import com.example.ShopAcc.repository.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/game")
public class GameControll {
    @Autowired
    RewardsRepository rewardsRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    DiscountDetailRepository discountDetailRepository;
    @Autowired
    RewardsListRepository rewardsListRepository;
    @GetMapping("/play")
    public String SpinWheel(Model model, HttpSession session){
        User user = (User) session.getAttribute("infor");
        int point =0;
        point = user.getPoint();
        model.addAttribute("user",point);
        return "SpinWheel";
    }
    @GetMapping("/rewardslist")
    public String RewardsList(HttpSession session,Model model){
        User user = (User) session.getAttribute("infor");
        List<RewardsList> rewardsLists = rewardsListRepository.rewardsList(user.getId());

        model.addAttribute("list",rewardsLists);
        return "RewardsHistory";
    }

    @ResponseBody
    @GetMapping("rewards")
    public List<Rewards> getRewards(){
        List<Rewards> rewards = rewardsRepository.findAll();
        return rewards;
    }

    @ResponseBody
    @PostMapping("rewards-choice")
    public Integer Choice(HttpSession session, Model model) {
        User user = (User) session.getAttribute("infor");
        int choice =0;
        List<Rewards> rewards = rewardsRepository.findAll();
        Random random = new Random();
        double randomDouble = random.nextDouble();

        double rd = 0.0 + (randomDouble * (100.0 - 0.0));
        double percent =0;
        for(int i=0;i<rewards.size();i++)
        {
            if(percent <= rd && rd <= (rewards.get(i).getPercent()+percent))
                choice = i;
            percent += rewards.get(i).getPercent();
        }

        if(user.getPoint() < 100 )
        {
            model.addAttribute("message","You don't have enough points");
            return -1;
        }
        else{
            user.setPoint(user.getPoint() - 100);
            userRepository.save(user);
            Rewards result = rewards.get(choice);

            if(result.getType().equals("Money"))
            {
                Wallet wallet = walletRepository.findByAccountid(user.getId());
                wallet.setWallet(wallet.getWallet() + (int)result.getValue());
                System.out.println(wallet.getWallet() + (int)result.getValue());
                walletRepository.save(wallet);
            }
            if(result.getType().equals("Voucher"))
            {
                Discount discount =new Discount();
                if(discountRepository.findByUserId(user.getId()).orElse(null) == null)
                {
                    discount.setUserId(user.getId());
                    discountRepository.save(discount);
                }

                discount = discountRepository.findByUserId(user.getId()).orElse(null);

                DiscountDetail discountDetail = new DiscountDetail();
                discountDetail.setDiscountID(discount.getId());
                discountDetail.setValue(String.valueOf(result.getValue()));
                discountDetail.setQuantity(1);
                discountDetail.setStatus(true);
                discountDetailRepository.save(discountDetail);
            }
            RewardsList rewardsList = new RewardsList();
            rewardsList.setName(result.getName());
            rewardsList.setUserid(user.getId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            rewardsList.setTime(formatter.format(LocalDate.now()));

            rewardsListRepository.save(rewardsList);
        }

        return rewards.size()-choice-1;
    }
}
