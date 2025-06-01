package com.chiasetailieu.chiasetailieuhoctapptit.repository.TheoDoi;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.TheoDoi.FollowView;

@Repository
public interface FollowViewRepo extends JpaRepository<FollowView, Long> {
    List<FollowView> findByNguoiDung(String userId);
    List<FollowView> findByDangTheoDoi(String userId);
}