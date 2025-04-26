package com.chiasetailieu.chiasetailieuhoctapptit.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chiasetailieu.chiasetailieuhoctapptit.model.SinhVien;
import com.chiasetailieu.chiasetailieuhoctapptit.service.SinhVienService;

@Controller
public class HomeController {
    
    @Autowired
    private SinhVienService sinhVienService;

    @GetMapping("/")
    public String root() {
        return "signin";
    }
    @GetMapping("/signin")
    public String signin() {
        return "signin";
    }
    @GetMapping("/index")
    public String index(@AuthenticationPrincipal OidcUser principal, Model model) {
        if (principal == null) {
            return "redirect:/signin";
        }
        try {
            SinhVien sinhVien = sinhVienService.saveOrUpdateSinhVien(principal);
            model.addAttribute("sinhVien", sinhVien);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return "index";
    }
    @GetMapping("/auth-error")
    public String authError(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("errorMessage", "Bạn không thế đăng nhập");
        return "auth-error";
    }
}