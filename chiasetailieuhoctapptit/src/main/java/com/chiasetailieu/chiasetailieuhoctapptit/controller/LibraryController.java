package com.chiasetailieu.chiasetailieuhoctapptit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chiasetailieu.chiasetailieuhoctapptit.model.LoaiTaiLieuModel.LoaiTaiLieu;
import com.chiasetailieu.chiasetailieuhoctapptit.model.MonHocModel.MonHoc;
import com.chiasetailieu.chiasetailieuhoctapptit.model.SinhVienModel.SinhVien;
import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.TaiLieuView;
import com.chiasetailieu.chiasetailieuhoctapptit.model.TheoDoi.Follow_VW;
import com.chiasetailieu.chiasetailieuhoctapptit.service.LoaiTaiLieu.LoaiTaiLieuService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.MonHoc.MonHocService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.SinhVien.SinhVienService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieu.LuuTaiLieuService;
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
    @Autowired
    private LuuTaiLieuService luuTaiLieuService;
    @Autowired
    private MonHocService monHocService;
    @Autowired
    private LoaiTaiLieuService loaiTaiLieuService;

    @GetMapping
    public String library(@AuthenticationPrincipal OidcUser principal, 
                         @RequestParam(value = "myLibrarySort", required = false, defaultValue = "newest") String myLibrarySort,
                         @RequestParam(value = "savedSort", required = false, defaultValue = "newest") String savedSort,
                         Model model) {
        if (principal == null) {
            return "redirect:/signin";
        }
        
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
            
            // Lấy danh sách tài liệu người dùng đã đăng
            List<TaiLieuView> myUploads = taiLieuViewService.findByMaSinhVien(sinhVien.getMaSV());
            model.addAttribute("myUploads", myUploads);
            model.addAttribute("uploadCount", myUploads.size());
            
            // Lấy danh sách môn học và loại tài liệu
            List<MonHoc> danhSachMonHoc = monHocService.getAllMonHoc();
            List<LoaiTaiLieu> loaiTaiLieu = loaiTaiLieuService.getLoaiTaiLieu();
            model.addAttribute("danhSachMonHoc", danhSachMonHoc);
            model.addAttribute("loaiTaiLieu", loaiTaiLieu);
            
            // Lấy danh sách tài liệu đã lưu dựa trên tùy chọn sắp xếp
            List<TaiLieuView> savedDocuments = getSavedDocumentsSorted(sinhVien.getMaSV(), savedSort);
            model.addAttribute("savedDocuments", savedDocuments);
            model.addAttribute("savedCount", savedDocuments.size());
            
            // Ghi nhớ tùy chọn sắp xếp
            model.addAttribute("myLibrarySort", myLibrarySort);
            model.addAttribute("savedSort", savedSort);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi tải trang library: " + e.getMessage());
        }
        
        return "library";
    }
    
    /**
     * Lấy danh sách tài liệu đã lưu theo tiêu chí sắp xếp
     * @param maSinhVien
     * @param sortOption
     * @return
     */
    private List<TaiLieuView> getSavedDocumentsSorted(String maSinhVien, String sortOption) {
        switch (sortOption) {
            case "most-viewed":
                return luuTaiLieuService.getSavedDocumentsByUserOrderByLuotXemDesc(maSinhVien);
            case "most-commented":
                return luuTaiLieuService.getSavedDocumentsByUserOrderBySoLuongBLDesc(maSinhVien);
            case "oldest":
                return luuTaiLieuService.getSavedDocumentsByUserOrderByOldest(maSinhVien);
            case "recently-saved":
                return luuTaiLieuService.getSavedDocumentsByUserOrderByNgayLuuDesc(maSinhVien);
            case "newest":
            default:
                return luuTaiLieuService.getSavedDocumentsByUserOrderByNewest(maSinhVien);
        }
    }
    
    /**
     * API để lấy danh sách tài liệu đã đăng theo tiêu chí sắp xếp
     */
    @GetMapping("/my-uploads")
    @ResponseBody
    public List<TaiLieuView> getMyUploads(@AuthenticationPrincipal OidcUser principal,
                                         @RequestParam(value = "sort", required = false, defaultValue = "newest") String sort) {
        if (principal == null) {
            return List.of();
        }
        
        try {
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            
            switch (sort) {
                case "most-viewed":
                    return taiLieuViewService.findByMaSinhVienOrderByLuotXemDesc(maSV);
                case "most-commented":
                    return taiLieuViewService.findByMaSinhVienOrderBySoLuongBLDesc(maSV);
                case "oldest":
                    return taiLieuViewService.findByMaSinhVienOrderByNgayDangAsc(maSV);
                case "newest":
                default:
                    return taiLieuViewService.findByMaSinhVienOrderByNgayDangDesc(maSV);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh sách tài liệu đã đăng: " + e.getMessage());
            return List.of();
        }
    }

    @GetMapping("/saved-documents")
    @ResponseBody
    public List<TaiLieuView> getSavedDocuments(@AuthenticationPrincipal OidcUser principal,
                                              @RequestParam(value = "sort", required = false, defaultValue = "newest") String sort) {
        if (principal == null) {
            return List.of();
        }
        
        try {
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            
            return getSavedDocumentsSorted(maSV, sort);
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh sách tài liệu đã lưu: " + e.getMessage());
            return List.of();
        }
    }
}
