package com.chiasetailieu.chiasetailieuhoctapptit.controller;

import com.chiasetailieu.chiasetailieuhoctapptit.model.FileModel.File;
import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.TaiLieu;
import com.chiasetailieu.chiasetailieuhoctapptit.service.File.FileService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieu.TaiLieuService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.MonHoc.MonHocService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.LoaiTaiLieu.LoaiTaiLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private FileService fileService;
    @Autowired
    private TaiLieuService taiLieuService;
    @Autowired
    private MonHocService monHocService;
    @Autowired
    private LoaiTaiLieuService loaiTaiLieuService;

    @GetMapping
    public String showUploadPage(Model model, @AuthenticationPrincipal OidcUser principal) {
        // Lấy danh sách môn học (Course)
        List<?> danhSachMonHoc = monHocService.getAllMonHoc();
        model.addAttribute("danhSachMonHoc", danhSachMonHoc);

        // Lấy danh sách loại tài liệu (Category)
        List<?> loaiTaiLieu = loaiTaiLieuService.getLoaiTaiLieu();
        model.addAttribute("loaiTaiLieu", loaiTaiLieu);

        // Nếu muốn truyền thông tin sinh viên (nếu cần)
        // if (principal != null) {
        //     model.addAttribute("sinhVien", sinhVienService.saveOrUpdateSinhVien(principal));
        // }
        return "upload";
    }

    @PostMapping
    public String uploadDocuments(
            @AuthenticationPrincipal OidcUser principal,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam Map<String, String> params,
            RedirectAttributes redirectAttributes
    ) {
        if (principal == null) {
            return "redirect:/signin";
        }
        try {
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));

            // Nếu chỉ upload 1 file (modal cũ)
            if ((files == null || files.isEmpty()) && file != null && !file.isEmpty()) {
                String tieuDe = params.get("tieuDe");
                String moTa = params.get("moTa");
                String maLoai = params.get("maLoai");
                String maMonHoc = params.get("maMonHoc");
                String tags = params.get("tags");

                File fileEntity = fileService.saveFile(file);
                TaiLieu taiLieu = new TaiLieu();
                taiLieu.setTieuDe(tieuDe);
                taiLieu.setMoTa(moTa);
                taiLieu.setMaSinhVien(maSV);
                taiLieu.setMaLoai(maLoai);
                taiLieu.setMaMonHoc(maMonHoc);
                taiLieu.setLuotXem(0);
                taiLieu.setUpVote(0);
                taiLieu.setDownVote(0);
                taiLieu.setNgayDang(java.time.LocalDate.now());
                taiLieu.setMaFile((long) fileEntity.getMaFile());
                taiLieuService.saveTaiLieu(taiLieu);

                redirectAttributes.addFlashAttribute("successMessage", "Tài liệu '" + tieuDe + "' đã được tải lên thành công!");
                return "redirect:/documents";
            }

            // Xử lý upload nhiều file từ trang upload.html
            if (files == null || files.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy file để tải lên.");
                return "redirect:/documents";
            }

            for (int i = 0; i < files.size(); i++) {
                MultipartFile mf = files.get(i);
                String tieuDe = params.get("tieuDe_" + i);
                String moTa = params.get("moTa_" + i);
                String maLoai = params.get("maLoai_" + i);
                String maMonHoc = params.get("maMonHoc_" + i);
                String tags = params.get("tags_" + i);

                if (mf == null || mf.isEmpty() || tieuDe == null || moTa == null || maLoai == null || maMonHoc == null) {
                    continue; // Bỏ qua file thiếu thông tin
                }

                File fileEntity = fileService.saveFile(mf);
                TaiLieu taiLieu = new TaiLieu();
                taiLieu.setTieuDe(tieuDe);
                taiLieu.setMoTa(moTa);
                taiLieu.setMaSinhVien(maSV);
                taiLieu.setMaLoai(maLoai);
                taiLieu.setMaMonHoc(maMonHoc);
                taiLieu.setLuotXem(0);
                taiLieu.setUpVote(0);
                taiLieu.setDownVote(0);
                taiLieu.setNgayDang(java.time.LocalDate.now());
                taiLieu.setMaFile((long) fileEntity.getMaFile());
                taiLieuService.saveTaiLieu(taiLieu);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Tải lên thành công " + files.size() + " tài liệu!");
        } catch (Exception e) {
            System.err.println("LỖI khi upload nhiều file: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi tải lên: " + e.getMessage());
        }
        return "redirect:/documents";
    }
}
