package com.chiasetailieu.chiasetailieuhoctapptit.service.MonHoc;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chiasetailieu.chiasetailieuhoctapptit.model.MonHocModel.FollowCourse;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.MonHocRepository.FollowCourseRepo;

@Service
public class FollowCourseService {
    @Autowired
    private FollowCourseRepo followCourseRepo;

    public boolean isFollowing(String maSinhVien, String maMon) {
        return followCourseRepo.findByMaSinhVienAndMaMon(maSinhVien, maMon).isPresent();
    }

    public boolean follow(String maSinhVien, String maMon) {
        if (isFollowing(maSinhVien, maMon)) return false;
        followCourseRepo.save(new FollowCourse(maSinhVien, maMon));
        return true;
    }

    @Transactional
    public boolean unfollow(String maSinhVien, String maMon) {
        Optional<FollowCourse> fc = followCourseRepo.findByMaSinhVienAndMaMon(maSinhVien, maMon);
        if (fc.isPresent()) {
            followCourseRepo.delete(fc.get());
            return true;
        }
        return false;
    }

    public List<FollowCourse> getFollowedCourses(String maSinhVien) {
        return followCourseRepo.findByMaSinhVien(maSinhVien);
    }
}
