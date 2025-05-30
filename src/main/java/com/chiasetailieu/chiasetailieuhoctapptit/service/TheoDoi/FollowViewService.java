package com.chiasetailieu.chiasetailieuhoctapptit.service.TheoDoi;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chiasetailieu.chiasetailieuhoctapptit.model.TheoDoi.FollowView;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.TheoDoi.FollowViewRepo;

@Service
public class FollowViewService {
    
    @Autowired
    private FollowViewRepo followViewRepository;
    
    public List<Map<String, Object>> getFollowers(String userId) {
        List<FollowView> followers = followViewRepository.findByNguoiDung(userId);
        
        return followers.stream().map(f -> {
            Map<String, Object> follower = new HashMap<>();
            follower.put("maSV", f.getDangTheoDoi());
            follower.put("hoVaTen", f.getHoTenDTD());
            follower.put("hinhAnh", f.getHinhAnhDTD());
            return follower;
        }).collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> getFollowing(String userId) {
        List<FollowView> following = followViewRepository.findByDangTheoDoi(userId);
        
        return following.stream().map(f -> {
            Map<String, Object> followed = new HashMap<>();
            followed.put("maSV", f.getNguoiDung());
            followed.put("hoVaTen", f.getHoTenND());
            followed.put("hinhAnh", f.getHinhAnhND());
            return followed;
        }).collect(Collectors.toList());
    }
}