package com.example.ShopAcc.controller;

import com.example.ShopAcc.model.*;
import com.example.ShopAcc.repository.*;
import com.example.ShopAcc.service.CartItemService;
import com.example.ShopAcc.service.Impl.DiscountDetailServiceImpl;
import com.example.ShopAcc.service.Impl.DiscountServiceImpl;
import com.example.ShopAcc.service.Impl.PurchaseHistoryDetailServiceImpl;
import com.example.ShopAcc.service.Impl.PurchaseHistoryServiceImpl;
import com.example.ShopAcc.service.ProductService;
import com.example.ShopAcc.service.WalletService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private DiscountServiceImpl discountServiceImpl;
    @Autowired
    private DiscountDetailServiceImpl discountDetailServiceImpl;
    @Autowired
    private DiscountDetailRepository discountDetailRepository;
    @Autowired
    private PurchaseHistoryRepo purchasehistoryrepo;
    @Autowired
    private PurchaseHistoryDetailRepo purchaseHistoryDetailRepo;
    @Autowired
    private ProductRepository productRepository;


    @PostMapping("/purchaseSelected")
    public String purchaseSelectedItems(@RequestParam("totalPrice") int totalPrice,
                                        @RequestParam("selectedItems") List<Integer> selectedItems,
                                        @RequestParam("quantities") List<Integer> quantities,
                                        @RequestParam("productIds") List<Integer> productIds,
                                        @RequestParam("selectedDisplayValue") String displayValue,
                                        RedirectAttributes redirectAttributes,
                                        HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        Wallet wallet = walletService.getWalletUser(userId);
        if (wallet == null) {
            return "redirect:/cart/view?WalletNotFound";
        }

        int currentWallet = wallet.getWallet();
        int newWallet = walletService.updateWallet(totalPrice, currentWallet);

        if (newWallet != currentWallet || displayValue.equals("100%")) {
            wallet.setWallet(newWallet);
            cartItemService.purchaseSelectedItems(selectedItems, userId);

            List<Integer> productDetailIdsToUpdate = cartItemService.getProductDetailIdsToUpdate(productIds, quantities);
            productService.updateSoldQuantity(productIds, quantities);

            productDetailRepository.updateProductStatus(productDetailIdsToUpdate);

            List<Integer> discountIDList = discountServiceImpl.findDiscountIDbyUserID(userId);
            List<Integer> discountDetailIDList = discountDetailServiceImpl.findDiscountDetailIDbyDiscountID(discountIDList);

            for (Integer discountDetailID : discountDetailIDList) {
                String value = discountDetailServiceImpl.findValueStringByDiscountDetailID(discountDetailID);
                if (value.equals(displayValue)) {
                    discountDetailRepository.updateDiscountQuantity(discountDetailID);
                    break;
                }
            }

            for (int i = 0; i < productIds.size(); i++) {
                Product product = productRepository.findProductByID(productIds.get(i));
                int value = ValueDiscount(displayValue);
                Purchasehistory purchasehistory = new Purchasehistory();
                purchasehistory.setProduct(product);
                purchasehistory.setPrice((product.getPrice() * quantities.get(i)) - (product.getPrice() / 100 * quantities.get(i) * value));

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                purchasehistory.setPurchasedate(new Date());
                purchasehistory.setUser((User) session.getAttribute("infor"));
                purchasehistoryrepo.save(purchasehistory);

                for (Integer productdetailid : productDetailIdsToUpdate) {
                    ProductDetail productDetail = productDetailRepository.findById(productdetailid).orElse(null);
                    if (productDetail != null && productDetail.getProductid() == product.getID()) {
                        Purchasehistorydetail purchasehistorydetail = new Purchasehistorydetail();
                        purchasehistorydetail.setCode(productDetail.getCode());
                        purchasehistorydetail.setSeri(productDetail.getSeri());
                        purchasehistorydetail.setPurchasehistory(purchasehistory);
                        purchaseHistoryDetailRepo.save(purchasehistorydetail);
                    }
                }
            }

            redirectAttributes.addFlashAttribute("purchaseSuccess", true);
            return "redirect:/cart/view";
        }

        return "redirect:/cart/view?OutOfWallet";
    }

    private int ValueDiscount(String value_init) {
        if (value_init.length() == 0) return 0;
        value_init = value_init.trim();
        value_init = value_init.substring(0, value_init.length() - 1);
        return Integer.parseInt(value_init);
    }
}