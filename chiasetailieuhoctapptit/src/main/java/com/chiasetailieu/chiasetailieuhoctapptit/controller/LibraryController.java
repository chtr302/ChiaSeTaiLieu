package com.chiasetailieu.chiasetailieuhoctapptit.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.chiasetailieu.chiasetailieuhoctapptit.service.SinhVien.SinhVienService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieu.TaiLieuService;

@Controller
public class LibraryController {

    // @Autowired
    // private SinhVienService sinhVienService;
    
    // @Autowired
    // private TaiLieuService taiLieuService;

    @GetMapping("/library")
    public String showLibrary() {
        // if (principal == null) {
        //     return "redirect:/signin";
        // }

        // try {
        //     var sinhVien = sinhVienService.saveOrUpdateSinhVien(principal);
        //     model.addAttribute("sinhVien", sinhVien);
            
        //     // Thêm thông tin thống kê
        //     String uploadCount = taiLieuService.countDocumentsByUser(sinhVien.getMaSV());
        //     model.addAttribute("uploadCount", uploadCount);
            
        //     // TODO: Thêm các thống kê khác như upvotes, comments
            
        //     // TODO: Lấy danh sách tài liệu của user
            
        // } catch (Exception e) {
        //     System.err.println("Error in LibraryController: " + e.getMessage());
        // }

        return "library";
    }
}