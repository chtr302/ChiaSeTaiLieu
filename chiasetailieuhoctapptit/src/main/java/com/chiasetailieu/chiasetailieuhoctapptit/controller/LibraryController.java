package com.chiasetailieu.chiasetailieuhoctapptit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chiasetailieu.chiasetailieuhoctapptit.model.SinhVienModel.SinhVien;
import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.TaiLieuView;
import com.chiasetailieu.chiasetailieuhoctapptit.service.SinhVien.SinhVienService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieu.TaiLieuViewService;

@Controller
@RequestMapping("/library")
public class LibraryController {

    @Autowired
    private SinhVienService sinhVienService;
    @Autowired
    private TaiLieuViewService taiLieuViewService;

    @GetMapping
    public String library(@AuthenticationPrincipal OidcUser principal, Model model){
        try {
            SinhVien sinhVien = sinhVienService.saveOrUpdateSinhVien(principal);
            model.addAttribute("sinhVien", sinhVien);
            
            List<TaiLieuView> allDocuments = taiLieuViewService.getTaiLieu();
            model.addAttribute("Documents", allDocuments);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi tải trang library: " + e.getMessage());
        }
        
        return "library";
    }
}
