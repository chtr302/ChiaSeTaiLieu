package com.chiasetailieu.chiasetailieuhoctapptit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chiasetailieu.chiasetailieuhoctapptit.service.VoteService.VoteService;

@RestController
@RequestMapping("/api/vote")
public class VoteController {
    
    @Autowired
    private VoteService voteService;
    
    @PostMapping("/upvote")
    public ResponseEntity<?> upvote(@RequestParam Long maTaiLieu, @AuthenticationPrincipal OidcUser principal) {
        if (principal == null) {
            return ResponseEntity.badRequest().body("Vui lòng đăng nhập để vote");
        }
        
        String email = principal.getEmail();
        String maSinhVien = email.substring(0, email.indexOf('@'));
        
        try {
            String result = voteService.vote(maTaiLieu, maSinhVien, 1);
            String message;
            
            if (result.equals("Đã bỏ vote")) {
                message = "Đã bỏ vote thành công";
            } else {
                message = "Cảm ơn bạn đã đánh giá tài liệu";
            }
            
            return ResponseEntity.ok().body(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra: " + e.getMessage());
        }
    }
    
    @PostMapping("/downvote")
    public ResponseEntity<?> downvote(@RequestParam Long maTaiLieu, @AuthenticationPrincipal OidcUser principal) {
        if (principal == null) {
            return ResponseEntity.badRequest().body("Vui lòng đăng nhập để vote");
        }
        
        String email = principal.getEmail();
        String maSinhVien = email.substring(0, email.indexOf('@'));
        
        try {
            String result = voteService.vote(maTaiLieu, maSinhVien, -1);
            String message;
            
            if (result.equals("Đã bỏ vote")) {
                message = "Đã bỏ vote thành công";
            } else {
                message = "Cảm ơn bạn đã đánh giá tài liệu";
            }
            
            return ResponseEntity.ok().body(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra: " + e.getMessage());
        }
    }
    
    @GetMapping("/info/{maTaiLieu}")
    public ResponseEntity<?> getVoteInfo(@PathVariable Long maTaiLieu, @AuthenticationPrincipal OidcUser principal) {
        String maSinhVien = "";
        
        if (principal != null) {
            String email = principal.getEmail();
            maSinhVien = email.substring(0, email.indexOf('@'));
        }
        
        try {
            VoteService.VoteInfo voteInfo = voteService.getVoteInfo(maTaiLieu, maSinhVien);
            return ResponseEntity.ok(voteInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra: " + e.getMessage());
        }
    }
} 