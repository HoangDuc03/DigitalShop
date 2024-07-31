package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.dto.DiscountDetailDTO;
import com.example.ShopAcc.model.DiscountDetail;
import com.example.ShopAcc.repository.DiscountDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiscountDetailServiceImpl {
    @Autowired
    private DiscountDetailRepository discountDetailRepository;

    public List<Integer> findDiscountDetailIDbyDiscountID(List<Integer> discountID){
        return discountDetailRepository.findDiscountDetailIDbyDiscountID(discountID);
    }

    public double findValueByDiscountDetailID(int discountDetailID){
        return  discountDetailRepository.findValueByDiscountDetailID(discountDetailID);
    }

    public String findValueStringByDiscountDetailID(int discountDetailID){
        return  discountDetailRepository.findValueStringByDiscountDetailID(discountDetailID);
    }

    public void saveDiscountDetail(DiscountDetail discountDetail) {
        discountDetailRepository.save(discountDetail);
    }

    public DiscountDetail findDiscountDetailById(int id) {
        return discountDetailRepository.findById(id).orElse(null);
    }

    public void updateDiscountDetail(int id, DiscountDetail discountDetail) {
        discountDetailRepository.updateDiscountDetail(id, discountDetail.getQuantity(), discountDetail.getValue(), discountDetail.isStatus());
    }

    public void deleteDiscountDetailById(int id) {
        discountDetailRepository.deleteById(id);
    }

    public List<DiscountDetailDTO> getAllDiscountDetailsWithUserID() {
        return discountDetailRepository.findAllWithUserID();
    }
}
