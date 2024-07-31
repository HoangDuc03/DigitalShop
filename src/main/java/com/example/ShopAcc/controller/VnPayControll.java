package com.example.ShopAcc.controller;

import com.example.ShopAcc.config.vnpayConfig;
import com.example.ShopAcc.model.User;
import com.example.ShopAcc.model.Wallet;
import com.example.ShopAcc.model.WalletH;
import com.example.ShopAcc.model.WalletHistory;
import com.example.ShopAcc.repository.WalletHRepo;
import com.example.ShopAcc.repository.WalletRepository;
import com.example.ShopAcc.repository.WallethistoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/payment")
public class VnPayControll {
    @Autowired
    WalletHRepo wallethistoryRepository;
    @Autowired
    WalletRepository walletRepository;

    @PostMapping("/create_payment")
    public void createpayment(HttpServletResponse reps, HttpServletRequest req , HttpSession session) throws IOException {

        String orderType = "other";
        long amount = Integer.parseInt(req.getParameter("amount")) * 100;
        String bankCode = req.getParameter("bankCode");

        String vnp_TxnRef = vnpayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = vnpayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnpayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", vnpayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = null;
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", vnpayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = vnpayConfig.hmacSHA512(vnpayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnpayConfig.vnp_PayUrl + "?" + queryUrl;

        User user =(User) session.getAttribute("infor");
        Wallet wallet = walletRepository.findByAccountid(user.getId());
        session.setAttribute("wallet",wallet);
        WalletH wallethistory = new WalletH();
        int money = (int) (amount /100);
        wallethistory.setMoney(money);
        wallethistory.setWalletid(wallet.getID());
        wallethistory.setDescribe(vnp_TxnRef);
        //wallethistory.setUser(null);

        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String formattedTimestamp = formatters.format(now);
        wallethistory.setDate(formattedTimestamp);

        wallethistoryRepository.saveWalletPross(wallet.getID(),money,formattedTimestamp,vnp_TxnRef);

        reps.sendRedirect(paymentUrl);
    }
    @GetMapping("/create_payment")
    public String payment_template(){

        return "paymentpage";
    }

    @GetMapping("successfuly")
    public String payment_success(
            @RequestParam(value = "vnp_Amount") String money,
            @RequestParam(value = "vnp_PayDate") String date,
            @RequestParam(value = "vnp_ResponseCode") String code,
            @RequestParam(value = "vnp_TxnRef") String TransactionNo,
            HttpSession session
    ){

        Wallet wallet = (Wallet) session.getAttribute("wallet");
        WalletH wallethistory = wallethistoryRepository.findWallet(wallet.getID(),TransactionNo);
        if(wallethistory!= null) {
            if (code.equals("00")) {
                int m;
                try {
                    m = Integer.parseInt(money) / 100;
                } catch (Exception e) {
                    m = 0;
                }

                if (wallethistory.getMoney() == m) {
                    wallethistory.setDescribe("VNPAY");
                    wallethistoryRepository.save(wallethistory);

                    wallet.setWallet(wallet.getWallet() + m);
                    walletRepository.save(wallet);

                    System.out.println(wallethistory.getDescribe());
                } else {
                    wallethistoryRepository.delete(wallethistory);
                }
            } else {
                wallethistoryRepository.delete(wallethistory);
            }
        }
        return "redirect:/home";
    }
}
