package com.example.ShopAcc.controller;

import com.example.ShopAcc.model.Rewards;
import com.example.ShopAcc.repository.RewardsRepository;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class GameManagementControll {
    @Autowired
    RewardsRepository rewardsRepository;

    @GetMapping("/game")
    public String display(Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size){
        double SumPercent = rewardsRepository.SumPercent();
        String percent_string = String.format("%.2f",SumPercent);
        SumPercent = Double.parseDouble(percent_string);

        if(
                SumPercent != 100.00
        )
        {
            model.addAttribute("message","The ratio of all items is not 100%!");
        }
        Page<Rewards> reward =rewardsRepository.findAll(PageRequest.of(page, size));

        model.addAttribute("totalPages", reward.getTotalPages());
        model.addAttribute("item",reward);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        return "game-manager";
    }
    @GetMapping("/game/divide")
    public String Divide(Model model){
        List<Rewards> rewards =rewardsRepository.findAll();
        int size =rewards.size();
        double percent = (double) 100/size;
        String percent_string = String.format("%.2f",percent);
        percent = Double.parseDouble(percent_string);
        for(int i=0;i<size;i++)
        {

            if(i == (size -1)) {
                percent = (100.00 - percent*size) + percent;
                percent_string = String.format("%.2f",percent);
                percent = Double.parseDouble(percent_string);
            }
            rewards.get(i).setPercent(percent);
            rewardsRepository.save(rewards.get(i));
        }
        return "redirect:../game";
    }

    @GetMapping("/game/add")
    public String AddItemDisplay(Model model){
        double SumPer = rewardsRepository.SumPercent();
        model.addAttribute("sumpercent",String.format("%.2f",100 - SumPer));
        return "game-manager-add";
    }
    @PostMapping("/game/add")
    public String AddItem(Model model,
                          @RequestParam("type") String type,
                          @RequestParam("name") String name,
                          @RequestParam("value") double value,
                          @RequestParam("percent") Double percent){

        double SumPercent = rewardsRepository.SumPercent();
        if(SumPercent + percent > 100)
        {
            model.addAttribute("message","Total ratio greater than 100%");
            double SumPer = rewardsRepository.SumPercent();
            model.addAttribute("sumpercent",String.format("%.2f",100 - SumPer));
            return "game-manager-add";
        }

        Rewards rewards =new Rewards();

        rewards.setName(name);
        rewards.setType(type);
        rewards.setPercent(percent);
        rewards.setValue(value);
        rewardsRepository.save(rewards);

        return "redirect:../game";
    }

    @GetMapping("/game/edit")
    public String EditGameDisplay(Model model,
                                  @RequestParam("id") Integer id
    ){
        Rewards rewards = rewardsRepository.findById(id).orElse(null);
        double SumPer = rewardsRepository.SumPercent();


        model.addAttribute("sumpercent",String.format("%.2f",100 - SumPer + rewards.getPercent()));
        model.addAttribute("item",rewards);
        return "game-manager-edit";
    }

    @PostMapping("/game/edit")
    public String EditGame(Model model,
                           @RequestParam("id") Integer id,
                           @RequestParam("type") String type,
                           @RequestParam("name") String name,
                           @RequestParam("value") double value,
                           @RequestParam("percent") Double percent
    ){
        Rewards rewards = rewardsRepository.findById(id).orElse(null);
        double SumPer = rewardsRepository.SumPercent();

        double SumPercent = rewardsRepository.SumPercent();
        String per = String.format("%.2f",SumPercent + percent - rewards.getPercent());

        if( Double.parseDouble(per) > 100)
        {
            model.addAttribute("message","Total ratio greater than 100%");
            model.addAttribute("item",rewards);
            model.addAttribute("sumpercent",String.format("%.2f",100 - SumPer + rewards.getPercent()));
            return "game-manager-edit";
        }
        rewards.setType(type);
        rewards.setName(name);
        rewards.setPercent(percent);
        rewards.setValue(value);
        rewardsRepository.save(rewards);

        return "redirect:../game";
    }

    @GetMapping("/game/delete")
    public String Delete(Model model,
                                  @RequestParam("id") Integer id
    ){
        Rewards rewards = rewardsRepository.findById(id).orElse(null);
        rewardsRepository.delete(rewards);
        return "redirect:../game";
    }

    @GetMapping("/game/reset")
    public String Reset(Model model){
        List<Rewards> rewards =rewardsRepository.findAll();
        int size =rewards.size();
        for(int i=0;i<size;i++)
        {

            rewards.get(i).setPercent(0.0);
            rewardsRepository.save(rewards.get(i));
        }
        return "redirect:../game";
    }
}
