package com.chiasetailieu.chiasetailieuhoctapptit.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
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
import com.chiasetailieu.chiasetailieuhoctapptit.service.BinhLuan.BaoCaoService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.BinhLuan.BinhLuanService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.BinhLuan.BinhLuanViewService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.EmailService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.File.FileService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.LoaiTaiLieu.LoaiTaiLieuService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.MonHoc.MonHocService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.SinhVien.SinhVienService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieu.LuuTaiLieuService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieu.TaiLieuService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieu.TaiLieuViewService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.LichSuXem.LichSuXemService;

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
    @Autowired
    private LuuTaiLieuService luuTaiLieuService;
    @Autowired
    private LichSuXemService lichSuXemService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BaoCaoService baoCaoService;
    
    @Value("${thumbnail.dir:./thumbnails}") // lấy đường dẫn để lưu và lấy thumbnail
    private String thumbnailDir;

    @Value("${upload.dir:./uploads}")
    private String uploadDir;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public String showDocuments(@AuthenticationPrincipal OidcUser principal, Model model,
                                @RequestParam(value = "q", required = false) String keyword) {
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

            // Lọc theo từ khóa nếu có (lọc cả có dấu và không dấu)
            if (keyword != null && !keyword.trim().isEmpty()) {
                String lower = keyword.trim().toLowerCase();
                String lowerNoAccent = removeVietnameseTones(lower);
                allDocuments = allDocuments.stream()
                    .filter(doc -> {
                        if (doc.getTieuDe() == null) return false;
                        String title = doc.getTieuDe().toLowerCase();
                        String titleNoAccent = removeVietnameseTones(title);
                        // So khớp cả có dấu và không dấu
                        return title.contains(lower) || titleNoAccent.contains(lowerNoAccent);
                    })
                    .toList();
            }
            model.addAttribute("Documents", allDocuments);

            List<TaiLieuView> mostViewedDocuments = allDocuments.stream()
                .filter(doc -> doc.getLuotXem() > 0)
                .sorted((doc1, doc2) -> Long.compare(doc2.getLuotXem(), doc1.getLuotXem()))
                .limit(5)
                .toList();
            model.addAttribute("mostViewedDocuments", mostViewedDocuments);
        } catch (Exception e) {
            System.err.println("Lỗi khi tải trang documents: " + e.getMessage());
        }
        
        return "documents";
    }

    // Sửa lại để hỗ trợ upload từ trang upload.html (nhiều file) và modal (1 file)
    @PostMapping("/upload")
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

            // Nếu upload nhiều file từ trang upload.html
            if (files != null && !files.isEmpty()) {
                for (int i = 0; i < files.size(); i++) {
                    MultipartFile mf = files.get(i);
                    String tieuDe = params.get("tieuDe_" + i);
                    String moTa = params.get("moTa_" + i);
                    String maLoai = params.get("maLoai_" + i);
                    String maMonHoc = params.get("maMonHoc_" + i);
                    String tags = params.get("tags_" + i);

                    if (mf == null || mf.isEmpty() || tieuDe == null || moTa == null || maLoai == null || maMonHoc == null) {
                        continue;
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
                    taiLieu.setNgayDang(LocalDate.now());
                    taiLieu.setMaFile((long) fileEntity.getMaFile());
                    taiLieu.setTags(tags);
                    taiLieuService.saveTaiLieu(taiLieu);
                }
                redirectAttributes.addFlashAttribute("successMessage", "Tải lên thành công " + files.size() + " tài liệu!");
                return "redirect:/documents";
            }

            // Nếu chỉ upload 1 file (modal)
            if (file != null && !file.isEmpty()) {
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
                taiLieu.setNgayDang(LocalDate.now());
                taiLieu.setMaFile((long) fileEntity.getMaFile());
                taiLieu.setTags(tags);
                taiLieuService.saveTaiLieu(taiLieu);

                redirectAttributes.addFlashAttribute("successMessage", "Tài liệu '" + tieuDe + "' đã được tải lên thành công!");
                return "redirect:/documents";
            }

            // Không có file nào được gửi lên
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy file để tải lên.");
            return "redirect:/documents";
        } catch (IOException | IllegalArgumentException | NullPointerException e) {
            System.err.println("Lỗi xử lý: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xử lý file: " + e.getMessage());
            return "redirect:/documents";
        }
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
            
            // Ghi lại lịch sử xem tài liệu
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            lichSuXemService.recordViewActivity(maSV, (int) id);
            
            // Thêm danh sách loại tài liệu và môn học cho modal chỉnh sửa
            List<LoaiTaiLieu> loaiTaiLieu = loaiTaiLieuService.getLoaiTaiLieu();
            model.addAttribute("loaiTaiLieu", loaiTaiLieu);

            List<MonHoc> danhSachMonHoc = monHocService.getAllMonHoc();
            model.addAttribute("danhSachMonHoc", danhSachMonHoc);

            model.addAttribute("taiLieu", taiLieuView);
            
            List<BinhLuanView> comments = binhLuanViewService.getCommentByDocumentId(id);
            model.addAttribute("comments", comments);
            
            String sql = "SELECT tb.MaTraLoi, tb.NoiDung, tb.NgayTraLoi, tb.MaBinhLuan, sv.HoVaTen, sv.HinhAnh " +
                    "FROM TraLoiBinhLuan tb JOIN SinhVien sv ON tb.MaSinhVien = sv.MaSinhVien " +
                    "JOIN BinhLuan bl ON tb.MaBinhLuan = bl.MaBinhLuan " +
                    "WHERE bl.MaTaiLieu = ?";

            List<Map<String, Object>> replies = jdbcTemplate.queryForList(sql, id);
            model.addAttribute("replies", replies);
            
            return "documents-detail";
            
        } catch (NullPointerException e) {
            System.err.println("NullPointerException khi tải chi tiết tài liệu: " + e.getMessage());
            return "redirect:/documents?error=error_loading_document";
        } catch (RuntimeException e) {
            System.err.println("RuntimeException khi tải chi tiết tài liệu: " + e.getMessage());
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

    @GetMapping("/all")
    public String showAllDocuments(
            @AuthenticationPrincipal OidcUser principal,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "sort", defaultValue = "newest") String sort,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "course", required = false) String course,
            @RequestParam(value = "semester", required = false) String semester,
            Model model) {
        
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
            
            // Get all documents with filters
            List<TaiLieuView> allDocuments = taiLieuViewService.getTaiLieu();
            
            // Apply filters
            if (type != null && !type.isEmpty()) {
                allDocuments = allDocuments.stream()
                    .filter(doc -> doc.getTenLoai().equals(type))
                    .toList();
            }
            
            if (course != null && !course.isEmpty()) {
                allDocuments = allDocuments.stream()
                    .filter(doc -> doc.getTenMon().equals(course))
                    .toList();
            }
            
            // Apply sorting - based on available fields in TaiLieuView
            allDocuments = switch (sort) {
                case "views" -> allDocuments.stream()
                        .sorted((a, b) -> Long.compare(b.getLuotXem(), a.getLuotXem()))
                        .toList();
                case "rating" -> allDocuments.stream()
                        .sorted((a, b) -> Long.compare(b.getDanhGia(), a.getDanhGia()))
                        .toList();
                default -> allDocuments.stream()
                        .sorted((a, b) -> Long.compare(b.getMaTaiLieu(), a.getMaTaiLieu()))
                        .toList();
            };
            
            // Pagination
            int pageSize = 16; // Number of documents per page
            int totalDocuments = allDocuments.size();
            int totalPages = (int) Math.ceil((double) totalDocuments / pageSize);
            
            // Ensure page is within valid range
            if (page < 1) page = 1;
            if (page > totalPages && totalPages > 0) page = totalPages;
            
            // Get documents for current page
            int startIdx = (page - 1) * pageSize;
            int endIdx = Math.min(startIdx + pageSize, totalDocuments);
            
            List<TaiLieuView> currentPageDocuments = 
                (startIdx < totalDocuments) ? allDocuments.subList(startIdx, endIdx) : List.of();
            
            // Add to model
            model.addAttribute("Documents", currentPageDocuments);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", page);
            model.addAttribute("selectedSort", sort);
            model.addAttribute("selectedType", type);
            model.addAttribute("selectedCourse", course);
            model.addAttribute("selectedSemester", semester);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi tải trang all documents: " + e.getMessage());
            
        }
        
        return "view-documents/all-documents";
    }

    @GetMapping("/courses")
    public String showCourses(
            @AuthenticationPrincipal OidcUser principal,
            Model model) {
        
        if (principal == null) {
            return "redirect:/signin";
        }
        
        try {
            SinhVien sinhVien = sinhVienService.saveOrUpdateSinhVien(principal);
            model.addAttribute("sinhVien", sinhVien);
            
            List<MonHoc> danhSachMonHoc = monHocService.getAllMonHoc();
            model.addAttribute("danhSachMonHoc", danhSachMonHoc);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi tải trang courses: " + e.getMessage());
            
        }
        
        return "see-more-documents/courses";
    }
    
    @GetMapping("/categories")
    public String showCategories(
            @AuthenticationPrincipal OidcUser principal,
            Model model) {
        
        if (principal == null) {
            return "redirect:/signin";
        }
        
        try {
            SinhVien sinhVien = sinhVienService.saveOrUpdateSinhVien(principal);
            model.addAttribute("sinhVien", sinhVien);
            
            List<LoaiTaiLieu> loaiTaiLieu = loaiTaiLieuService.getLoaiTaiLieu();
            model.addAttribute("loaiTaiLieu", loaiTaiLieu);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi tải trang categories: " + e.getMessage());
            
        }
        
        return "see-more-documents/categories";
    }
    
    @GetMapping("/most-viewed")
    public String showMostViewed(
            @AuthenticationPrincipal OidcUser principal,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {
        
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
            
            // Get all documents and sort by view count
            List<TaiLieuView> allDocuments = taiLieuViewService.getTaiLieu().stream()
                .sorted((a, b) -> Long.compare(b.getLuotXem(), a.getLuotXem()))
                .toList();
            
            // Pagination
            int pageSize = 16; // Number of documents per page
            int totalDocuments = allDocuments.size();
            int totalPages = (int) Math.ceil((double) totalDocuments / pageSize);
            
            // Ensure page is within valid range
            if (page < 1) page = 1;
            if (page > totalPages && totalPages > 0) page = totalPages;
            
            // Get documents for current page
            int startIdx = (page - 1) * pageSize;
            int endIdx = Math.min(startIdx + pageSize, totalDocuments);
            
            List<TaiLieuView> currentPageDocuments = 
                (startIdx < totalDocuments) ? allDocuments.subList(startIdx, endIdx) : List.of();
            
            // Add to model
            model.addAttribute("Documents", currentPageDocuments);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", page);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi tải trang most viewed: " + e.getMessage());
            
        }
        
        return "most-viewed";
    }
    
    @GetMapping("/course/{courseId}")
    public String showCourseDocuments(
            @PathVariable("courseId") String courseId,
            @AuthenticationPrincipal OidcUser principal,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "sort", defaultValue = "newest") String sort,
            @RequestParam(value = "type", required = false) String type,
            Model model) {
        
        if (principal == null) {
            return "redirect:/signin";
        }
        
        try {
            SinhVien sinhVien = sinhVienService.saveOrUpdateSinhVien(principal);
            model.addAttribute("sinhVien", sinhVien);
            
            // Get course information
            Optional<MonHoc> courseOptional = monHocService.getMonHocByMa(courseId);
            if (courseOptional.isEmpty()) {
                return "redirect:/documents/courses?error=course_not_found";
            }
            MonHoc course = courseOptional.get();
            model.addAttribute("course", course);
            
            // Add course-related data
            model.addAttribute("isFollowing", false); // This would be based on user data
            
            List<LoaiTaiLieu> loaiTaiLieu = loaiTaiLieuService.getLoaiTaiLieu();
            model.addAttribute("loaiTaiLieu", loaiTaiLieu);
            
            // Get documents for this course
            List<TaiLieuView> allDocuments = taiLieuViewService.getTaiLieu();
            List<TaiLieuView> courseDocuments = allDocuments.stream()
                .filter(doc -> doc.getTenMon().equals(course.getTenMon()))
                .toList();
            
            // Apply type filter if provided
            if (type != null && !type.isEmpty()) {
                courseDocuments = courseDocuments.stream()
                    .filter(doc -> doc.getTenLoai().equals(type))
                    .toList();
            }
            
            // Apply sorting
            courseDocuments = switch (sort) {
                case "views" -> courseDocuments.stream()
                        .sorted((a, b) -> Long.compare(b.getLuotXem(), a.getLuotXem()))
                        .toList();
                case "rating" -> courseDocuments.stream()
                        .sorted((a, b) -> Long.compare(b.getDanhGia(), a.getDanhGia()))
                        .toList();
                default -> courseDocuments.stream()
                        .sorted((a, b) -> Long.compare(b.getMaTaiLieu(), a.getMaTaiLieu()))
                        .toList();
            };
            
            // Pagination
            int pageSize = 16; // Number of documents per page
            int totalDocuments = courseDocuments.size();
            int totalPages = (int) Math.ceil((double) totalDocuments / pageSize);
            
            // Ensure page is within valid range
            if (page < 1) page = 1;
            if (page > totalPages && totalPages > 0) page = totalPages;
            
            // Get documents for current page
            int startIdx = (page - 1) * pageSize;
            int endIdx = Math.min(startIdx + pageSize, totalDocuments);
            
            List<TaiLieuView> currentPageDocuments = 
                (startIdx < totalDocuments) ? courseDocuments.subList(startIdx, endIdx) : List.of();
            
            // Add to model
            model.addAttribute("Documents", currentPageDocuments);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", page);
            model.addAttribute("selectedSort", sort);
            model.addAttribute("selectedType", type);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi tải trang course documents: " + e.getMessage());
            return "redirect:/documents/courses?error=error_loading_course";
        }
        
        return "view-documents/course-documents";
    }
    
    @GetMapping("/category/{categoryId}")
    public String showCategoryDocuments(
            @PathVariable("categoryId") String categoryId,
            @AuthenticationPrincipal OidcUser principal,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "sort", defaultValue = "newest") String sort,
            @RequestParam(value = "course", required = false) String course,
            Model model) {
        
        if (principal == null) {
            return "redirect:/signin";
        }
        
        try {
            SinhVien sinhVien = sinhVienService.saveOrUpdateSinhVien(principal);
            model.addAttribute("sinhVien", sinhVien);
            
            // Get category information
            Optional<LoaiTaiLieu> categoryOptional = loaiTaiLieuService.getLoaiTaiLieuByMa(categoryId);
            if (categoryOptional.isEmpty()) {
                return "redirect:/documents/categories?error=category_not_found";
            }
            LoaiTaiLieu category = categoryOptional.get();
            model.addAttribute("category", category);
            
            List<MonHoc> danhSachMonHoc = monHocService.getAllMonHoc();
            model.addAttribute("danhSachMonHoc", danhSachMonHoc);
            
            // Add isFollowing attribute for the category (similar to course)
            model.addAttribute("isFollowing", false);
            
            // Get documents for this category
            List<TaiLieuView> allDocuments = taiLieuViewService.getTaiLieu();
            List<TaiLieuView> categoryDocuments = allDocuments.stream()
                .filter(doc -> doc.getTenLoai().equals(category.getTenLoai()))
                .toList();
            
            // Apply course filter if provided 
            if (course != null && !course.isEmpty()) {
                final String courseId = course;
                // Find the corresponding course name
                Optional<MonHoc> monHoc = danhSachMonHoc.stream()
                    .filter(mon -> mon.getMaMon().equals(courseId))
                    .findFirst();
                
                if (monHoc.isPresent()) {
                    final String tenMon = monHoc.get().getTenMon();
                    categoryDocuments = categoryDocuments.stream()
                        .filter(doc -> doc.getTenMon().equals(tenMon))
                        .toList();
                }
            }
            
            // Apply sorting
            categoryDocuments = switch (sort) {
                case "views" -> categoryDocuments.stream()
                        .sorted((a, b) -> Long.compare(b.getLuotXem(), a.getLuotXem()))
                        .toList();
                case "rating" -> categoryDocuments.stream()
                        .sorted((a, b) -> Long.compare(b.getDanhGia(), a.getDanhGia()))
                        .toList();
                default -> categoryDocuments.stream()
                        .sorted((a, b) -> Long.compare(b.getMaTaiLieu(), a.getMaTaiLieu()))
                        .toList();
            };
            
            // Pagination
            int pageSize = 16; // Number of documents per page
            int totalDocuments = categoryDocuments.size();
            int totalPages = (int) Math.ceil((double) totalDocuments / pageSize);
            
            // Ensure page is within valid range
            if (page < 1) page = 1;
            if (page > totalPages && totalPages > 0) page = totalPages;
            
            // Get documents for current page
            int startIdx = (page - 1) * pageSize;
            int endIdx = Math.min(startIdx + pageSize, totalDocuments);
            
            List<TaiLieuView> currentPageDocuments = 
                (startIdx < totalDocuments) ? categoryDocuments.subList(startIdx, endIdx) : List.of();
            
            // Add to model
            model.addAttribute("Documents", currentPageDocuments);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", page);
            model.addAttribute("selectedSort", sort);
            model.addAttribute("selectedCourse", course);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi tải trang category documents: " + e.getMessage());
            return "redirect:/documents/categories?error=error_loading_category";
        }
        
        return "category-documents";
    }

    @GetMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<?> editDocumentNotAllowed() {
        return ResponseEntity.status(405).body("Method Not Allowed");
    }

    // API cập nhật thông tin tài liệu
    @PostMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> editDocument(
            @PathVariable("id") long id,
            @AuthenticationPrincipal OidcUser principal,
            @RequestBody Map<String, String> body
    ) {
        Map<String, Object> response = new HashMap<>();
        if (principal == null) {
            response.put("success", false);
            response.put("message", "Bạn chưa đăng nhập.");
            return response;
        }
        try {
            TaiLieu taiLieu = taiLieuService.getTaiLieuById(id);
            if (taiLieu == null) {
                response.put("success", false);
                response.put("message", "Không tìm thấy tài liệu.");
                return response;
            }
            // Chỉ cho phép chủ sở hữu hoặc admin sửa (nếu cần)
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            if (!maSV.equals(taiLieu.getMaSinhVien())) {
                response.put("success", false);
                response.put("message", "Bạn không có quyền sửa tài liệu này.");
                return response;
            }
            // Cập nhật thông tin
            taiLieu.setTieuDe(body.getOrDefault("tieuDe", taiLieu.getTieuDe()));
            taiLieu.setMaLoai(body.getOrDefault("maLoai", taiLieu.getMaLoai()));
            taiLieu.setMaMonHoc(body.getOrDefault("maMonHoc", taiLieu.getMaMonHoc()));
            taiLieu.setMoTa(body.getOrDefault("moTa", taiLieu.getMoTa()));
            taiLieu.setTags(body.getOrDefault("tags", taiLieu.getTags()));
            taiLieuService.saveTaiLieu(taiLieu);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra khi cập nhật: " + e.getMessage());
        }
        return response;
    }

    // Thêm hàm loại bỏ dấu tiếng Việt
    private String removeVietnameseTones(String str) {
        if (str == null) return "";
        String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        temp = temp.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        temp = temp.replace('đ', 'd').replace('Đ', 'D');
        return temp;
    }

    // --- API GỢI Ý TÌM KIẾM CHO NAVBAR VÀ INDEX ---
    @GetMapping("/api/search/suggestions")
    @ResponseBody
    public List<Map<String, Object>> searchSuggestions(
            @RequestParam("query") String query
    ) {
        if (query == null || query.trim().isEmpty()) return List.of();
        String lower = query.trim().toLowerCase();
        String lowerNoAccent = removeVietnameseTones(lower);
        List<TaiLieuView> allDocuments = taiLieuViewService.getTaiLieu();
        return allDocuments.stream()
            .filter(doc -> {
                if (doc.getTieuDe() == null) return false;
                String title = doc.getTieuDe().toLowerCase();
                String titleNoAccent = removeVietnameseTones(title);
                return title.contains(lower) || titleNoAccent.contains(lowerNoAccent);
            })
            .limit(10)
            .map(doc -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", doc.getMaTaiLieu());
                map.put("tenTaiLieu", doc.getTieuDe());
                map.put("monHoc", doc.getTenMon());
                map.put("loaiTaiLieu", doc.getTenLoai());
                map.put("thumbnail", doc.getThumbnail());
                return map;
            })
            .toList();
    }

    @PostMapping("/reply/{commentId}")
    @ResponseBody
    public Map<String, Object> replyToComment(@PathVariable("commentId") int commentId, 
                                             @AuthenticationPrincipal OidcUser principal, 
                                             @RequestBody Map<String, String> replyData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            String content = replyData.get("content");
            
            if (content == null || content.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Nội dung trả lời không được để trống");
                return response;
            }

            Optional<BinhLuan> binhLuanOpt = binhLuanService.getBinhLuanById(commentId);
            if (binhLuanOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Bình luận không tồn tại");
                return response;
            }

            SinhVien sinhVien = sinhVienService.getSinhVienbyMaSinhVien(maSV);

            try {
                String insertSql = "INSERT INTO TraLoiBinhLuan (NoiDung, NgayTraLoi, MaSinhVien, MaBinhLuan) VALUES (?, ?, ?, ?)";

                jdbcTemplate.update(insertSql, content, LocalDate.now(), maSV, commentId);

                response.put("success", true);
                response.put("authorName", sinhVien.getHoVaTen());
                response.put("hinhAnh", sinhVien.getHinhAnh());
                response.put("formattedDate", LocalDate.now().toString());
            } catch (org.springframework.dao.DataAccessException e) {
                throw new RuntimeException("Không thể lưu trả lời bình luận: " + e.getMessage());
            } catch (RuntimeException e) {
                throw new RuntimeException("Không thể lưu trả lời bình luận: " + e.getMessage());
            }
            
            return response;
        } catch (org.springframework.dao.DataAccessException e) {
            response.put("success", false);
            response.put("message", "Có lỗi truy cập dữ liệu khi thêm trả lời: " + e.getMessage());
            return response;
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra khi thêm trả lời: " + e.getMessage());
            return response;
        }
    }
    
    // Phương thức để báo cáo tài liệu
    @PostMapping("/report/{documentId}")
    @ResponseBody
    public Map<String, Object> reportDocument(@PathVariable("documentId") long documentId, 
                                             @AuthenticationPrincipal OidcUser principal, 
                                             @RequestBody Map<String, String> reportData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            String reason = reportData.get("reason");
            String description = reportData.get("description");
            
            if (description == null || description.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Nội dung báo cáo không được để trống");
                return response;
            }
            
            // Kiểm tra tài liệu tồn tại
            TaiLieuView taiLieu = taiLieuViewService.findByMaTaiLieu(documentId);
            if (taiLieu == null) {
                response.put("success", false);
                response.put("message", "Tài liệu không tồn tại");
                return response;
            }
            
            // Kiểm tra không tự báo cáo tài liệu của mình
            if (maSV.equals(taiLieu.getMaSinhVien())) {
                response.put("success", false);
                response.put("message", "Bạn không thể báo cáo tài liệu của chính mình");
                return response;
            }
            
            // Tạo tiêu đề báo cáo
            String title = "Báo cáo tài liệu: " + reason;
            
            try {
                // Lưu báo cáo vào database sử dụng BaoCaoService
                baoCaoService.saveDocumentReport(title, description, (int) documentId, maSV);
                
                // Lấy thông tin sinh viên báo cáo và sinh viên bị báo cáo
                SinhVien reporter = sinhVienService.getSinhVienbyMaSinhVien(maSV);
                SinhVien reportedStudent = sinhVienService.getSinhVienbyMaSinhVien(taiLieu.getMaSinhVien());
                
                // Gửi email thông báo đến sinh viên bị báo cáo
                if (reportedStudent != null && reporter != null) {
                    emailService.sendDocumentReportNotification(reportedStudent, reporter, taiLieu, reason, description);
                }
                
                response.put("success", true);
                response.put("message", "Báo cáo đã được gửi thành công và email thông báo đã được gửi đến chủ sở hữu tài liệu");
            } catch (RuntimeException e) {
                throw new RuntimeException("Không thể lưu báo cáo tài liệu: " + e.getMessage());
            }
            
            return response;
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra khi gửi báo cáo: " + e.getMessage());
            return response;
        }
    }
    
    // Phương thức để báo cáo bình luận
    @PostMapping("/report-comment/{commentId}")
    @ResponseBody
    public Map<String, Object> reportComment(@PathVariable("commentId") int commentId, 
                                            @AuthenticationPrincipal OidcUser principal, 
                                            @RequestBody Map<String, String> reportData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            String title = reportData.get("title");
            String description = reportData.get("description");
            
            if (description == null || description.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Nội dung báo cáo không được để trống");
                return response;
            }
            
            if (title == null || title.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Tiêu đề báo cáo không được để trống");
                return response;
            }
            
            // Kiểm tra bình luận tồn tại
            Optional<BinhLuan> binhLuanOpt = binhLuanService.getBinhLuanById(commentId);
            if (binhLuanOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Bình luận không tồn tại");
                return response;
            }
            
            // Lấy bình luận và tài liệu ID
            BinhLuan binhLuan = binhLuanOpt.get();
            long taiLieuId = binhLuan.getTaiLieu();
            
            // Kiểm tra không tự báo cáo bình luận của mình
            if (maSV.equals(binhLuan.getSinhVien())) {
                response.put("success", false);
                response.put("message", "Bạn không thể báo cáo bình luận của chính mình");
                return response;
            }
            
            // Lấy thông tin tài liệu
            TaiLieuView taiLieu = taiLieuViewService.findByMaTaiLieu(taiLieuId);
            if (taiLieu == null) {
                response.put("success", false);
                response.put("message", "Tài liệu liên quan không tồn tại");
                return response;
            }
            
            // Tạo nội dung báo cáo bao gồm thông tin bình luận (cắt ngắn để phù hợp với varchar(45))
            String commentPreview = binhLuan.getNoiDung().length() > 30 
                ? binhLuan.getNoiDung().substring(0, 30) + "..." 
                : binhLuan.getNoiDung();
            String baoCaoContent = "BL#" + commentId + ": " + commentPreview;
            
            try {
                // Lưu báo cáo vào database sử dụng BaoCaoService
                baoCaoService.saveCommentReport(title, baoCaoContent, (int) taiLieuId, maSV);
                
                // Lấy thông tin sinh viên báo cáo và sinh viên bị báo cáo
                SinhVien reporter = sinhVienService.getSinhVienbyMaSinhVien(maSV);
                SinhVien reportedStudent = sinhVienService.getSinhVienbyMaSinhVien(binhLuan.getSinhVien());
                
                // Gửi email thông báo đến sinh viên bị báo cáo
                if (reportedStudent != null && reporter != null) {
                    emailService.sendCommentReportNotification(reportedStudent, reporter, taiLieu, title, binhLuan.getNoiDung(), description);
                }
                
                response.put("success", true);
                response.put("message", "Báo cáo bình luận đã được gửi thành công và email thông báo đã được gửi đến chủ sở hữu bình luận");
            } catch (org.springframework.dao.DataAccessException e) {
                throw new RuntimeException("Không thể lưu báo cáo bình luận: " + e.getMessage());
            } catch (RuntimeException e) {
                throw new RuntimeException("Không thể lưu báo cáo bình luận: " + e.getMessage());
            }
            
            return response;
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra khi gửi báo cáo bình luận: " + e.getMessage());
            return response;
        }
    }

    @GetMapping("/download/{id}")
    @ResponseBody
    public ResponseEntity<Resource> downloadDocument(@PathVariable("id") long id){
        try {
            TaiLieuView taiLieuView = taiLieuViewService.findByMaTaiLieu(id);
            if (taiLieuView == null) {
                return ResponseEntity.notFound().build();
            }
            
            Path filePath = Paths.get(uploadDir).resolve(taiLieuView.getDuongDanFile()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if(!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            // Tạo tên file tải xuống phù hợp, loại bỏ ký tự đặc biệt
            String originalFilename = taiLieuView.getDuongDanFile();
            String fileExtension = "";
            if (originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            String downloadFilename = removeVietnameseTones(taiLieuView.getTieuDe()) + fileExtension;
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadFilename + "\"")
                    .body(resource);
        } catch (java.io.IOException e) {
            System.err.println("Lỗi truy cập tệp khi tải xuống: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        } catch (java.nio.file.InvalidPathException e) {
            System.err.println("Đường dẫn tệp không hợp lệ khi tải xuống: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Toggle lưu hoặc hủy lưu tài liệu
     * @param id ID của tài liệu
     * @param principal Thông tin người dùng
     * @return Kết quả lưu/hủy lưu
     */
    @PostMapping("/save/{id}")
    @ResponseBody
    public Map<String, Object> toggleSaveDocument(@PathVariable("id") Integer id, @AuthenticationPrincipal OidcUser principal) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (principal == null) {
                response.put("success", false);
                response.put("message", "Bạn cần đăng nhập để thực hiện chức năng này");
                return response;
            }
            
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            
            boolean isSaved = luuTaiLieuService.toggleSaveDocument(id, maSV);
            
            response.put("success", true);
            response.put("saved", isSaved);
            response.put("message", isSaved ? "Đã lưu tài liệu thành công" : "Đã hủy lưu tài liệu");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Kiểm tra trạng thái lưu của tài liệu
     * @param id ID của tài liệu
     * @param principal Thông tin người dùng
     * @return Trạng thái lưu
     */
    @GetMapping("/check-saved/{id}")
    @ResponseBody
    public Map<String, Object> checkSavedStatus(@PathVariable("id") Integer id, @AuthenticationPrincipal OidcUser principal) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (principal == null) {
                response.put("success", false);
                response.put("message", "Bạn cần đăng nhập để thực hiện chức năng này");
                return response;
            }
            
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            
            boolean isSaved = luuTaiLieuService.isDocumentSaved(id, maSV);
            
            response.put("success", true);
            response.put("saved", isSaved);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Kiểm tra trạng thái lưu của nhiều tài liệu
     * @param ids Danh sách ID của tài liệu
     * @param principal Thông tin người dùng
     * @return Trạng thái lưu của từng tài liệu
     */
    @PostMapping("/check-saved-batch")
    @ResponseBody
    public Map<String, Object> checkSavedStatusBatch(@RequestBody List<Integer> ids, @AuthenticationPrincipal OidcUser principal) {
        Map<String, Object> response = new HashMap<>();
        Map<Integer, Boolean> savedStatuses = new HashMap<>();
        
        try {
            if (principal == null) {
                response.put("success", false);
                response.put("message", "Bạn cần đăng nhập để thực hiện chức năng này");
                return response;
            }
            
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            
            for (Integer id : ids) {
                boolean isSaved = luuTaiLieuService.isDocumentSaved(id, maSV);
                savedStatuses.put(id, isSaved);
            }
            
            response.put("success", true);
            response.put("savedStatuses", savedStatuses);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }
        
        return response;
    }
}