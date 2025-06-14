package com.chiasetailieu.chiasetailieuhoctapptit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.HashMap;
import java.util.Map;

import com.chiasetailieu.chiasetailieuhoctapptit.service.MonHoc.FollowCourseService;

@RestController
@RequestMapping("/api/follow-course")
public class FollowCourseController {
    @Autowired
    private FollowCourseService followCourseService;

    @PostMapping("/follow")
    public Map<String, Object> follow(@RequestParam String maMon, @AuthenticationPrincipal OidcUser oidcUser) {
        Map<String, Object> res = new HashMap<>();
        if (oidcUser == null) {
            res.put("success", false);
            res.put("error", "Not authenticated");
            return res;
        }
        String email = oidcUser.getEmail();
        String maSinhVien = email.substring(0, email.indexOf('@'));
        boolean success = followCourseService.follow(maSinhVien, maMon);
        res.put("success", success);
        return res;
    }

    @PostMapping("/unfollow")
    public Map<String, Object> unfollow(@RequestParam String maMon, @AuthenticationPrincipal OidcUser oidcUser) {
        Map<String, Object> res = new HashMap<>();
        if (oidcUser == null) {
            res.put("success", false);
            res.put("error", "Not authenticated");
            return res;
        }
        String email = oidcUser.getEmail();
        String maSinhVien = email.substring(0, email.indexOf('@'));
        boolean success = followCourseService.unfollow(maSinhVien, maMon);
        res.put("success", success);
        return res;
    }

    @GetMapping("/is-following")
    public Map<String, Object> isFollowing(@RequestParam String maMon, @AuthenticationPrincipal OidcUser oidcUser) {
        Map<String, Object> res = new HashMap<>();
        if (oidcUser == null) {
            res.put("following", false);
            res.put("error", "Not authenticated");
            return res;
        }
        String email = oidcUser.getEmail();
        String maSinhVien = email.substring(0, email.indexOf('@'));
        boolean following = followCourseService.isFollowing(maSinhVien, maMon);
        res.put("following", following);
        return res;
    }
}
