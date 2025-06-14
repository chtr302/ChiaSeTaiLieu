package com.chiasetailieu.chiasetailieuhoctapptit.controller;

import com.chiasetailieu.chiasetailieuhoctapptit.model.LichSuXemModel.LichSuXemDTO;
import com.chiasetailieu.chiasetailieuhoctapptit.service.LichSuXem.LichSuXemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/history")
public class LichSuXemController {
    
    @Autowired
    private LichSuXemService lichSuXemService;
    
    /**
     * Ghi lại hoạt động xem tài liệu
     * @param maTaiLieu Mã tài liệu
     * @param principal Thông tin người dùng
     * @return Kết quả
     */
    @PostMapping("/view/{maTaiLieu}")
    public ResponseEntity<Map<String, Object>> recordViewActivity(
            @PathVariable Integer maTaiLieu,
            @AuthenticationPrincipal OidcUser principal) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (principal == null) {
                response.put("success", false);
                response.put("message", "Bạn cần đăng nhập để thực hiện chức năng này");
                return ResponseEntity.ok(response);
            }
            
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            
            lichSuXemService.recordViewActivity(maSV, maTaiLieu);
            
            response.put("success", true);
            response.put("message", "Đã ghi lại hoạt động xem");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Ghi lại hoạt động lưu tài liệu
     * @param maTaiLieu Mã tài liệu
     * @param principal Thông tin người dùng
     * @return Kết quả
     */
    @PostMapping("/save/{maTaiLieu}")
    public ResponseEntity<Map<String, Object>> recordSaveActivity(
            @PathVariable Integer maTaiLieu,
            @AuthenticationPrincipal OidcUser principal) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (principal == null) {
                response.put("success", false);
                response.put("message", "Bạn cần đăng nhập để thực hiện chức năng này");
                return ResponseEntity.ok(response);
            }
            
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            
            lichSuXemService.recordSaveActivity(maSV, maTaiLieu);
            
            response.put("success", true);
            response.put("message", "Đã ghi lại hoạt động lưu");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Lấy danh sách hoạt động gần đây
     * @param principal Thông tin người dùng
     * @return Danh sách hoạt động gần đây
     */
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentActivities(
            @AuthenticationPrincipal OidcUser principal) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (principal == null) {
                response.put("success", false);
                response.put("message", "Bạn cần đăng nhập để xem lịch sử");
                return ResponseEntity.ok(response);
            }
            
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            
            List<LichSuXemDTO> activities = lichSuXemService.getRecentActivities(maSV);
            
            response.put("success", true);
            response.put("activities", activities);
            response.put("totalCount", activities.size());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi lấy lịch sử: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Xóa lịch sử cũ (API cho admin hoặc user tự xóa)
     * @param principal Thông tin người dùng
     * @return Kết quả
     */
    @DeleteMapping("/cleanup")
    public ResponseEntity<Map<String, Object>> cleanupOldHistory(
            @AuthenticationPrincipal OidcUser principal) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (principal == null) {
                response.put("success", false);
                response.put("message", "Bạn cần đăng nhập để thực hiện chức năng này");
                return ResponseEntity.ok(response);
            }
            
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            
            int deletedCount = lichSuXemService.cleanupOldHistoryByUser(maSV);
            
            response.put("success", true);
            response.put("deletedCount", deletedCount);
            response.put("message", "Đã xóa " + deletedCount + " bản ghi lịch sử cũ");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public String testRecentsPage() {
        return "redirect:/test-recents.html";
    }
} 