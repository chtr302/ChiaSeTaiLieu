package com.chiasetailieu.chiasetailieuhoctapptit.service.TheoDoi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TheoDoiService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean isFollowings(String followerMaSV, String followingMaSV){
        String query = "SELECT COUNT(*) as count FROM vw_Follow WHERE NguoiDung = ? AND DangTheoDoi = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, followerMaSV, followingMaSV);
        return count != null && count > 0;
    }

    public boolean follow(String followerMaSV, String followingMaSV){
        try{
            String checkSql = "SELECT COUNT(*) FROM TheoDoi WHERE NguoiDung = ? AND DangTheoDoi = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, followerMaSV, followingMaSV);
            if (count != null && count > 0) {
                String updateSql = "UPDATE TheoDoi SET TrangThai = 0 WHERE NguoiDung = ? AND DangTheoDoi = ?";
                jdbcTemplate.update(updateSql, followerMaSV, followingMaSV);
            } else {
                String insertSql = "INSERT INTO TheoDoi (NguoiDung, DangTheoDoi, TrangThai) VALUES (?, ?, 0)";
                jdbcTemplate.update(insertSql, followerMaSV, followingMaSV);
            }
            return true;
        } catch (org.springframework.dao.DataAccessException | IllegalArgumentException e) {
            return false;
        }
    }
    public boolean unfollow(String followerMaSV, String followingMaSV) {
        try {
            String sql = "UPDATE TheoDoi SET TrangThai = 1 WHERE NguoiDung = ? AND DangTheoDoi = ?";
            int rowsAffected = jdbcTemplate.update(sql, followerMaSV, followingMaSV);
            return rowsAffected > 0;
        } catch (org.springframework.dao.DataAccessException | IllegalArgumentException e) {
            return false;
        }
    }
}
