package com.chiasetailieu.chiasetailieuhoctapptit.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import com.chiasetailieu.chiasetailieuhoctapptit.model.SinhVienModel.SinhVien;
import com.chiasetailieu.chiasetailieuhoctapptit.model.TheoDoi.Follow_VW;
import com.chiasetailieu.chiasetailieuhoctapptit.service.SinhVien.SinhVienService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TheoDoi.FollowViewService;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TheoDoi.Follow_vw_Service;
import com.chiasetailieu.chiasetailieuhoctapptit.service.TheoDoi.TheoDoiService;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private SinhVienService sinhVienService;
    @Autowired
    private Follow_vw_Service follow_vw_Service;
    @Autowired
    private TheoDoiService theoDoiService;
    @Autowired
    private FollowViewService followViewService;

    @GetMapping("/{id}")
    public String user(@PathVariable("id") String userId, Model model, @AuthenticationPrincipal OidcUser principal){

        try{
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf('@'));
            SinhVien nguoiDung = null;
            nguoiDung = sinhVienService.getSinhVienbyMaSinhVien(maSV);
            model.addAttribute("sinhVien", nguoiDung);

            SinhVien profileSinhVien = sinhVienService.getSinhVienbyMaSinhVien(userId);
            model.addAttribute("profileUser", profileSinhVien);
            System.out.println(profileSinhVien.getHinhAnh());

            boolean isFollowing = theoDoiService.isFollowings(maSV, userId);
            model.addAttribute("isFollowing", isFollowing);
            
            Follow_VW follow_Stats = follow_vw_Service.getFollowStatsbyMaSV(profileSinhVien.getMaSV());
            if (follow_Stats != null) {
                model.addAttribute("followerCount", follow_Stats.getFollower());
                model.addAttribute("followingCount", follow_Stats.getFollowing());
            }

        } catch (Exception e){
            System.out.println("Co loi roi: " + e);
        }
        return "user";
    }

    @PostMapping("/follow")
    @ResponseBody
    public ResponseEntity<?> toggleFollow(@RequestParam("userId") String userId, @RequestParam("action") String action, @AuthenticationPrincipal OidcUser principal){
        try{
            String email = principal.getEmail();
            String maSV = email.substring(0, email.indexOf("@"));
            if (maSV.equals(userId)){
                return ResponseEntity.badRequest().body(createResponse(false, "Bạn không thể theo dõi chính mình.", null));
            }
            boolean success = false;
            String message = "";
            boolean isNowFollowing = false;
            if ("follow".equals(action)) {
                success = theoDoiService.follow(maSV, userId);
                message = "Đã theo dõi";
                isNowFollowing = true;
            } else if ("unfollow".equals(action)){
                success = theoDoiService.unfollow(maSV, userId);
                message = "Đã hủy theo dõi";
                isNowFollowing = false;
            }
            if (success) {
                Follow_VW follow_Stats = follow_vw_Service.getFollowStatsbyMaSV(userId);
                int followerCount = follow_Stats != null ? follow_Stats.getFollower() : 0;

                Map<String, Object> data = new HashMap<>();
                data.put("followerCount", followerCount);
                data.put("isFollowing", isNowFollowing);
                
                return ResponseEntity.ok(createResponse(true, message, data));
            } else {
                return ResponseEntity.badRequest()
                        .body(createResponse(false, "Không thể thực hiện thao tác, vui lòng thử lại.", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse(false, "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    private Map<String, Object> createResponse(boolean success, String message, Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        
        if (data != null) {
            response.putAll(data);
        }
        return response;
    }

    @GetMapping("/followers/{id}")
    @ResponseBody
    public ResponseEntity<?> getFollowers(@PathVariable("id") String userId) {
        try {
            List<Map<String, Object>> followers = followViewService.getFollowers(userId);
            return ResponseEntity.ok(followers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy danh sách người theo dõi: " + e.getMessage());
        }
    }

    @GetMapping("/following/{id}")
    @ResponseBody
    public ResponseEntity<?> getFollowing(@PathVariable("id") String userId) {
        try {
            List<Map<String, Object>> following = followViewService.getFollowing(userId);
            return ResponseEntity.ok(following);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy danh sách người đang theo dõi: " + e.getMessage());
        }
    }
}
