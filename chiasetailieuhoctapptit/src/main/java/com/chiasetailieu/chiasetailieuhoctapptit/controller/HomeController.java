package com.chiasetailieu.chiasetailieuhoctapptit.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chiasetailieu.chiasetailieuhoctapptit.model.SinhVien;


@Controller
public class HomeController {
    
    @GetMapping("/")
    public String index(@AuthenticationPrincipal OidcUser principal, Model model) {
        if (principal != null) {
            String maSV = principal.getEmail().substring(0, principal.getEmail().indexOf('@'));
            model.addAttribute("sinhVien" , new SinhVien(
                maSV, (String) principal.getClaims().get("name"), principal.getEmail(), (String) principal.getClaims().get("picture")
            ));
        }
        return "index";
    }
    
    @GetMapping("/documents")
    public String documents(@AuthenticationPrincipal OidcUser principal, Model model) {
        if (principal == null) {
            return "redirect:/signin";
        }
        String maSV = principal.getEmail().substring(0, principal.getEmail().indexOf('@'));
        model.addAttribute("sinhVien" , new SinhVien(
            maSV, (String) principal.getClaims().get("name"), principal.getEmail(), (String) principal.getClaims().get("picture")
        ));
        System.out.println(maSV + principal.getName() + principal.getEmail());
        return "documents";
    }
    
    @GetMapping("/signin")
    public String signin() {
        return "signin";
    }

    @GetMapping("/auth-error")
    public String authError(@RequestParam(required= false) String error, Model model){
        model.addAttribute("errorMessage", "Chỉ sinh viên thuộc Học viện Công nghệ Bưu chính viễn thông cơ sở tại TP.Hồ Chí Minh mới được phép đăng nhập. Vui lòng sử dụng email thuộc Học viện");
        return "auth-error";
    }
}