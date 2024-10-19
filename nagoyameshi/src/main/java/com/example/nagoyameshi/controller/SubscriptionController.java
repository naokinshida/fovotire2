package com.example.nagoyameshi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.nagoyameshi.service.MemberinfoService;
import com.example.nagoyameshi.service.StripeService;

@Controller
public class SubscriptionController {

    private final StripeService stripeService;
    private final MemberinfoService memberinfoService;

    public SubscriptionController(StripeService stripeService, MemberinfoService memberinfoService) {
        this.stripeService = stripeService;
        this.memberinfoService = memberinfoService;
    }

    @GetMapping("/subscribe")
    public String showSubscriptionForm() {
        return "subscribe/subscribe";  // サブディレクトリとテンプレート名を含めて指定
    }

    @PostMapping("/subscribe")
    public String handleSubscription() {
        // Stripe APIと連携してサブスクリプションを作成する処理
        return "redirect:/success";  // 成功時のリダイレクト処理
    }

    @GetMapping("/Subscription_Cancellation")
    public String cancelSubscription(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // 認証されたユーザーのメールアドレスを取得

        if (email != null && !email.isEmpty()) {
            try {
                // 顧客情報からCustomer IDを取得し、BillingポータルのURLを生成
                String customerId = memberinfoService.getCustomerIdByEmail(email);
                String returnUrl = "https://nagoyameshi11-5bbefe4dedec.herokuapp.com/"; // 実際のリダイレクト先URLに置き換えてください
                String billingUrl = stripeService.createBillingPortalSession(customerId, returnUrl);
                model.addAttribute("billingUrl", billingUrl);
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Error creating billing portal session");
            }
        } else {
            model.addAttribute("errorMessage", "User email not found");
        }
        return "subscribe/Subscription_Cancellation";  // サブディレクトリとテンプレート名を含めて指定
    }
}