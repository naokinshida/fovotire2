package com.example.nagoyameshi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.billingportal.Session;
import com.stripe.param.billingportal.SessionCreateParams;

@Service
public class StripeService {

    @Value("${stripe.api.secret}")
    private String stripeApiKey;

    public String createBillingPortalSession(String customerId, String returnUrl) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        SessionCreateParams params = SessionCreateParams.builder()
            .setCustomer(customerId)  // Stripe Customer ID
            .setReturnUrl(returnUrl)   // ポータル終了後のリダイレクトURL
            .build();

        Session session = Session.create(params);
        return session.getUrl();  // BillingポータルのURL取得
    }
}