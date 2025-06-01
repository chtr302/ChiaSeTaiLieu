package com.chiasetailieu.chiasetailieuhoctapptit.repository.MonHocRepository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.MonHocModel.FollowCourse;

@Repository
public interface FollowCourseRepo extends JpaRepository<FollowCourse, Long> {
    Optional<FollowCourse> findByMaSinhVienAndMaMon(String maSinhVien, String maMon);
    List<FollowCourse> findByMaSinhVien(String maSinhVien);
}
