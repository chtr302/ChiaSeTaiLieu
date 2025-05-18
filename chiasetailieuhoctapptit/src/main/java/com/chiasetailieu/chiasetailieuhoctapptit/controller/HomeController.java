package com.chiasetailieu.chiasetailieuhoctapptit.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.stream.Collectors;
import java.util.List;
import com.chiasetailieu.chiasetailieuhoctapptit.model.SinhVienModel.SinhVien;
import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.TaiLieuView;
import com.chiasetailieu.chiasetailieuhoctapptit.service.SinhVien.SinhVienService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieu.TaiLieuViewService;

@Controller
public class HomeController {
    
    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private TaiLieuViewService taiLieuViewService;

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
            // Thêm dòng này để truyền danh sách tài liệu cho index.html
            List<TaiLieuView> allDocs = taiLieuViewService.getTaiLieu();
            model.addAttribute("Documents", allDocs);
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

    // API trả về danh sách tiêu đề tài liệu liên quan đến từ khóa
    @GetMapping("/api/search-documents")
    @ResponseBody
    public List<String> searchDocuments(@RequestParam("q") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return List.of();
        List<TaiLieuView> allDocs = taiLieuViewService.getTaiLieu();
        String lower = keyword.trim().toLowerCase();
        return allDocs.stream()
            .map(TaiLieuView::getTieuDe)
            .filter(title -> title != null && title.toLowerCase().contains(lower))
            .limit(10)
            .collect(Collectors.toList());
    }
}