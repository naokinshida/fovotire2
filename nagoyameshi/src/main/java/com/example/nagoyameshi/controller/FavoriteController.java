package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.FavoriteService;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // ユーザーIDでお気に入りを取得
    @GetMapping("/by-user/{userId}")
    public List<Favorite> getFavorites(@PathVariable Long userId) {
        return favoriteService.getFavoritesByUserId(userId);
    }

    // お気に入りの追加
    @PostMapping("/{storeId}/add")
    public ResponseEntity<String> addFavorite(@PathVariable Integer storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        int userId = userDetails.getMemberinfo().getId();
        Favorite favorite = new Favorite();
        favorite.setStoreId(storeId);
        favorite.setUserId(userId);

        boolean isAlreadyFavorite = favoriteService.isFavorite(userId, storeId);
        if (isAlreadyFavorite) {
            // すでにお気に入りに登録されている場合
            return ResponseEntity.status(HttpStatus.CONFLICT).body("すでに登録されています");
        }

        favoriteService.addFavorite(favorite);
     // Favorite savedFavorite = favoriteService.addFavorite(favorite);
        return ResponseEntity.noContent().build(); // ResponseEntity.ok(savedFavorite);
    }

 /// お気に入りの削除
    @PostMapping("/{id}/delete") 
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.ok().build();
    }
}