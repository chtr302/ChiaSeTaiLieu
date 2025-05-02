package com.chiasetailieu.chiasetailieuhoctapptit.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chiasetailieu.chiasetailieuhoctapptit.model.BinhLuanModel.BinhLuan;
import com.chiasetailieu.chiasetailieuhoctapptit.model.BinhLuanModel.BinhLuanView;
import com.chiasetailieu.chiasetailieuhoctapptit.model.FileModel.File;
import com.chiasetailieu.chiasetailieuhoctapptit.model.LoaiTaiLieuModel.LoaiTaiLieu;
import com.chiasetailieu.chiasetailieuhoctapptit.model.MonHocModel.MonHoc;
import com.chiasetailieu.chiasetailieuhoctapptit.model.SinhVienModel.SinhVien;
import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.TaiLieu;
import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.TaiLieuView;
import com.chiasetailieu.chiasetailieuhoctapptit.service.BinhLuan.BinhLuanService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.BinhLuan.BinhLuanViewService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.File.FileService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.LoaiTaiLieu.LoaiTaiLieuService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.MonHoc.MonHocService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.SinhVien.SinhVienService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieu.TaiLieuService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieu.TaiLieuViewService;


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
    @Autowired
    private BinhLuanService binhLuanService;
    @Autowired
    private BinhLuanViewService binhLuanViewService;
    
    @Value("${thumbnail.dir:./thumbnails}") // lấy đường dẫn để lưu và lấy thumbnail
    private String thumbnailDir;

    @Value("${upload.dir:./uploads}")
    private String uploadDir;

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
            @AuthenticationPrincipal OidcUser principal, // Lấy thông tin đăng nhập của người dùng
            @RequestParam("file") MultipartFile file, // Lấy các thông tin nhận được từ View
            @RequestParam("tieuDe") String tieuDe,
            @RequestParam("moTa") String moTa,
            @RequestParam("maLoai") String maLoai,
            @RequestParam("maMonHoc") String maMonHoc,
            @RequestParam(value = "tags", required = false) String tags,
            RedirectAttributes redirectAttributes) {
        
        if (principal == null) {
            return "redirect:/signin";
        }
        
        try {            
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            
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
            taiLieu.setNgayDang(LocalDate.now());
            taiLieu.setMaFile((long) fileEntity.getMaFile());
            
            taiLieuService.saveTaiLieu(taiLieu);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Tài liệu '" + tieuDe + "' đã được tải lên thành công!");
            
        } catch (java.io.IOException e) {
            System.err.println("LỖI khi xử lý file: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Có lỗi xảy ra khi xử lý tập tin: " + e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("LỖI dữ liệu không hợp lệ: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Dữ liệu không hợp lệ: " + e.getMessage());
        }
        
        return "redirect:/documents";
    }

    @GetMapping("/thumbnails/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getThumbnail(@PathVariable String filename) {
        try {
            
            Path thumbnailPath = Paths.get(thumbnailDir).resolve(filename).normalize(); // Dựa vào đường dẫn ở trên lấy thumbnail và trả về
            
            Resource resource = new UrlResource(thumbnailPath.toUri());
            
            if(resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "image/png")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (java.nio.file.InvalidPathException | java.net.MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<Resource> viewDocument(@PathVariable("id") long id){
        try {
            TaiLieuView taiLieuView = taiLieuViewService.findByMaTaiLieu(id);
            Path filePath = Paths.get(uploadDir).resolve(taiLieuView.getDuongDanFile()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + taiLieuView.getTieuDe() + "\"")
                .body(resource);
        } catch (java.io.IOException e) {
            System.err.println("Lỗi truy cập tệp: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        } catch (java.nio.file.InvalidPathException e) {
            System.err.println("Đường dẫn tệp không hợp lệ: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/detail/{id}")
    public String showDocumentDetail(
            @PathVariable("id") long id,
            @AuthenticationPrincipal OidcUser principal,
            Model model) {
        
        if (principal == null) {
            return "redirect:/signin";
        }
        
        try {
            SinhVien sinhVien = sinhVienService.saveOrUpdateSinhVien(principal);
            model.addAttribute("sinhVien", sinhVien);
            
            TaiLieuView taiLieuView = taiLieuViewService.findByMaTaiLieu(id);
            
            if (taiLieuView == null) {
                return "redirect:/documents?error=document_not_found";
            }
            
            // Tăng lượt xem
            taiLieuService.incrementViewCount(id);
            
            // Xử lý tags nếu có
            // if (taiLieu.getTags() != null && !taiLieu.getTags().isEmpty()) {
            //     String[] tags = taiLieu.getTags().split(",");
            //     model.addAttribute("tags", tags);
            // }
            
            model.addAttribute("taiLieu", taiLieuView);
            
            List<BinhLuanView> comments = binhLuanViewService.getCommentByDocumentId(id);
            model.addAttribute("comments", comments);
            
            return "documents-detail";
            
        } catch (Exception e) {
            return "redirect:/documents?error=error_loading_document";
        }
    }

    @PostMapping("/comment/{id}")
    @ResponseBody
    public Map<String, Object> addComment(@PathVariable("id") long id,@AuthenticationPrincipal OidcUser principal, @RequestBody Map<String, String> commentData){
        Map<String, Object> response = new HashMap<>();
        try {
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            String content = commentData.get("content");
            
            if (content == null || content.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Nội dung bình luận không được để trống");
                return response;
            }
            BinhLuan binhLuan = new BinhLuan();
            binhLuan.setNoiDung(content);
            binhLuan.setSinhVien(maSV);
            binhLuan.setTaiLieu(id);
            binhLuanService.saveBinhLuan(binhLuan);

            SinhVien sinhVien = sinhVienService.getSinhVienbyMaSinhVien(maSV);
            response.put("success", true);
            response.put("commentId", binhLuan.getMaBinhLuan());
            response.put("authorName", sinhVien.getHoVaTen());
            response.put("hinhAnh", sinhVien.getHinhAnh());
            response.put("formattedDate", binhLuan.getNgayBinhLuan());
            return response;
        }catch (Exception e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra khi thêm bình luận");
            return response;
        }
    }

    
}