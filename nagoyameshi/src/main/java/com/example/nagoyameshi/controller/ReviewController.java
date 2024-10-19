package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Memberinfo;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.Storeinfo;
import com.example.nagoyameshi.repository.MemberinfoRepository;
import com.example.nagoyameshi.repository.StoreinfoRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.ReviewService;

@Controller
public class ReviewController {

    @Autowired
    private StoreinfoRepository storeInfoRepository;
    @Autowired
    private MemberinfoRepository memberinfoRepository;


    @Autowired
    private ReviewService reviewService;

    @GetMapping("/review/{id}")
    public String getStoreinfo(@PathVariable("id") Integer id, Model model) {
        Storeinfo storeinfo = storeInfoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("店舗が見つかりません"));
        
        // storeを基にレビューを取得する
        List<Review> reviews = reviewService.findByStoreinfoOrderByCreatedAtDesc(storeinfo);
        
        double averageScore = reviews.stream().mapToInt(Review::getStar).average().orElse(0.0);

        model.addAttribute("storeinfo", storeinfo);
        model.addAttribute("reviews", reviews);
        model.addAttribute("averageScore", averageScore);

        return "reviews/review";
    }

    @PostMapping("/review")
    public String postReview(
            @RequestParam("storeId") int storeId,
            @RequestParam("comment") String comment,
            @RequestParam("star") int star) {

        // 現在の認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (userDetails == null) {
            // ユーザー詳細情報がnullである場合の処理
            // ログ出力または例外スロー
        }

        Integer memberinfoId = userDetails.getMemberinfoId();

        if (memberinfoId == null) {
            // memberinfoIdがnullである場合の処理
            // ログ出力または例外スロー
        }

        // ここにデバッグログを追加
        System.out.println("Retrieved memberinfoId: " + memberinfoId);

        // Memberinfoをデータベースから取得
        Memberinfo memberinfo = memberinfoRepository.findById(memberinfoId)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));

   
    
        Storeinfo storeInfo = storeInfoRepository.findById(storeId)
            .orElseThrow(() -> new IllegalArgumentException("店舗が見つかりません"));

        Review newReview = new Review();
        newReview.setStoreInfo(storeInfo);
        newReview.setComment(comment);
        newReview.setStar(star);
        newReview.setMemberinfo(memberinfo);  // 現在のユーザーのMemberinfoを設定

        reviewService.save(newReview);
        return "redirect:/storeinfo/" + storeId;
    }

    // listUserReviewsの修正はpostReviewと同様にします
    @GetMapping("/reviews/user_reviews")
    public String listUserReviews(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Integer memberinfoId = userDetails.getMemberinfoId();

        Memberinfo memberinfo = memberinfoRepository.findById(memberinfoId)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));

        List<Review> reviews = reviewService.findByMemberinfoOrderByCreatedAtDesc(memberinfo);

        model.addAttribute("reviews", reviews);
        return "reviews/user_reviews";
    }
    
    @GetMapping("/review/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Review review = reviewService.findById(id);
        model.addAttribute("review", review);
        return "reviews/edit_review";  // 編集フォームのテンプレートファイル
    }

    @PostMapping("/review/edit/{id}")
    public String updateReview(
            @PathVariable("id") int id,
            @RequestParam("comment") String comment,
            @RequestParam("star") int star) {

        Review review = reviewService.findById(id);
        review.setComment(comment);
        review.setStar(star);
        reviewService.save(review);

        return "redirect:/reviews/user_reviews";  // 更新後にレビュー一覧ページにリダイレクト
    }

    @GetMapping("/review/delete/{id}")
    public String showDeleteConfirmation(@PathVariable("id") int id, Model model) {
        Review review = reviewService.findById(id);
        model.addAttribute("review", review);
        return "reviews/delete_confirmation";  // 削除確認画面のテンプレートファイル
    }

    @PostMapping("/review/delete/{id}")
    public String deleteReview(@PathVariable("id") int id) {
        reviewService.deleteReview(id);
        return "redirect:/reviews/user_reviews";  // 削除後にレビュー一覧ページにリダイレクト
    }
}