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
import com.chiasetailieu.chiasetailieuhoctapptit.model.TheoDoi.Follow_VW;
import com.chiasetailieu.chiasetailieuhoctapptit.service.SinhVien.SinhVienService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieu.TaiLieuViewService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TheoDoi.Follow_vw_Service;

@Controller
@RequestMapping("/library")
public class LibraryController {

    @Autowired
    private SinhVienService sinhVienService;
    @Autowired
    private TaiLieuViewService taiLieuViewService;
    @Autowired
    private Follow_vw_Service follow_vw_Service;

    @GetMapping
    public String library(@AuthenticationPrincipal OidcUser principal, Model model){
        try {
            SinhVien sinhVien = sinhVienService.saveOrUpdateSinhVien(principal);
            model.addAttribute("sinhVien", sinhVien);

            Follow_VW follow_Stats = follow_vw_Service.getFollowStatsbyMaSV(sinhVien.getMaSV());
            if (follow_Stats != null) {
                model.addAttribute("followerCount", follow_Stats.getFollower());
                model.addAttribute("followingCount", follow_Stats.getFollowing());
            } else {
                model.addAttribute("followerCount", 0);
                model.addAttribute("followingCount", 0);
            }
            
            List<TaiLieuView> allDocuments = taiLieuViewService.getTaiLieu();
            model.addAttribute("Documents", allDocuments);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi tải trang library: " + e.getMessage());
        }
        
        return "library";
    }
}
