package com.chiasetailieu.chiasetailieuhoctapptit.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chiasetailieu.chiasetailieuhoctapptit.model.File;
import com.chiasetailieu.chiasetailieuhoctapptit.model.LoaiTaiLieu;
import com.chiasetailieu.chiasetailieuhoctapptit.model.MonHoc;
import com.chiasetailieu.chiasetailieuhoctapptit.model.SinhVien;
import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieu;
import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuView;
import com.chiasetailieu.chiasetailieuhoctapptit.service.FileService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.LoaiTaiLieuService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.MonHocService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.SinhVienService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieuService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieuViewService;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private FileService fileService;
    
    @Autowired
    private TaiLieuService taiLieuService;
    
    @Autowired
    private SinhVienService sinhVienService;
    
    @Autowired
    private MonHocService monHocService;

    @Autowired
    private LoaiTaiLieuService loaiTaiLieuService;

    @Autowired
    private TaiLieuViewService taiLieuViewService;
    
    
    @Value("${thumbnail.dir:./thumbnails}")
    private String thumbnailDir;

    @GetMapping
    public String showDocuments(@AuthenticationPrincipal OidcUser principal, Model model) {
        if (principal == null) {
            return "redirect:/signin";
        }
        
        try {
            SinhVien sinhVien = sinhVienService.saveOrUpdateSinhVien(principal);
            model.addAttribute("sinhVien", sinhVien);
            
            List<MonHoc> danhSachMonHoc = monHocService.getAllMonHoc();
            model.addAttribute("danhSachMonHoc", danhSachMonHoc);

            List<LoaiTaiLieu> loaiTaiLieu = loaiTaiLieuService.getLoaiTaiLieu();
            model.addAttribute("loaiTaiLieu", loaiTaiLieu);
            
            List<TaiLieuView> allDocuments = taiLieuViewService.getTaiLieu();
            model.addAttribute("Documents", allDocuments);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi tải trang documents: " + e.getMessage());
        }
        
        return "documents";
    }

    @PostMapping("/upload")
    public String uploadDocument(
            @AuthenticationPrincipal OidcUser principal,
            @RequestParam("file") MultipartFile file,
            @RequestParam("tieuDe") String tieuDe,
            @RequestParam("moTa") String moTa,
            @RequestParam("maLoai") Integer maLoai,
            @RequestParam("maMonHoc") String maMonHoc,
            @RequestParam(value = "tags", required = false) String tags,
            RedirectAttributes redirectAttributes) {
        
        if (principal == null) {
            return "redirect:/signin";
        }
        
        try {            
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            
            // BƯỚC 1: Lưu file vào thư mục và database
            File fileEntity = fileService.saveFile(file);            
            // BƯỚC 2: Tạo đối tượng TaiLieu
            TaiLieu taiLieu = new TaiLieu();
            taiLieu.setTieuDe(tieuDe);
            taiLieu.setMoTa(moTa);
            taiLieu.setMaSinhVien(maSV);
            taiLieu.setMaLoai(maLoai);
            taiLieu.setMaMonHoc(maMonHoc);
            taiLieu.setLuotXem(0);
            taiLieu.setUpVote(0);
            taiLieu.setDownVote(0);
            taiLieu.setNgayDang(LocalDate.now());
            taiLieu.setMaFile((long) fileEntity.getMaFile());
            
            // BƯỚC 3: Lưu tài liệu vào database
            taiLieuService.saveTaiLieu(taiLieu);
            
            // BƯỚC 4: Thông báo thành công
            redirectAttributes.addFlashAttribute("successMessage", 
                "Tài liệu '" + tieuDe + "' đã được tải lên thành công!");
            
        } catch (Exception e) {
            System.err.println("LỖI khi upload tài liệu: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Có lỗi xảy ra khi tải lên tài liệu: " + e.getMessage());
        }
        
        return "redirect:/documents";
    }
    @GetMapping("/thumbnails/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getThumbnail(@PathVariable String filename) {
        try {
            
            Path thumbnailPath = Paths.get(thumbnailDir).resolve(filename).normalize();
            System.out.println("Full path: " + thumbnailPath.toAbsolutePath());
            
            Resource resource = new UrlResource(thumbnailPath.toUri());
            
            if(resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "image/png")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}