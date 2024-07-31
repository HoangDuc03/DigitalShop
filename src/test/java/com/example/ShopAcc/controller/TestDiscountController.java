package com.example.ShopAcc.controller;

import com.example.ShopAcc.service.Impl.DiscountServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.example.ShopAcc")
public class TestDiscountController {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestDiscountController.class);
        DiscountServiceImpl discountServiceImpl = context.getBean(DiscountServiceImpl.class);

        List<Integer> discountIDList = discountServiceImpl.findDiscountIDbyUserID(7);
        if (discountIDList.isEmpty()) {
            System.out.println("No Discount ID found for user");
        } else {
            for (Integer integer : discountIDList) {
                System.out.println("Discount ID: " + integer);
            }
        }
    }
}
