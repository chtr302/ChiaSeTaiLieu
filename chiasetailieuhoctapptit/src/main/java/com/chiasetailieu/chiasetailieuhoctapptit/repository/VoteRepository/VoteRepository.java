package com.chiasetailieu.chiasetailieuhoctapptit.repository.VoteRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.VoteModel.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    
    // Tìm vote của một user cho một tài liệu cụ thể
    Optional<Vote> findByMaTaiLieuAndMaSinhVien(Long maTaiLieu, String maSinhVien);
    
    // Đếm số upvote cho một tài liệu
    @Query("SELECT COUNT(v) FROM Vote v WHERE v.maTaiLieu = :maTaiLieu AND v.loaiVote = 1")
    long countUpvotesByMaTaiLieu(@Param("maTaiLieu") Long maTaiLieu);
    
    // Đếm số downvote cho một tài liệu
    @Query("SELECT COUNT(v) FROM Vote v WHERE v.maTaiLieu = :maTaiLieu AND v.loaiVote = -1")
    long countDownvotesByMaTaiLieu(@Param("maTaiLieu") Long maTaiLieu);
    
    // Xóa vote của user cho tài liệu
    void deleteByMaTaiLieuAndMaSinhVien(Long maTaiLieu, String maSinhVien);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.maSinhVien = :maSinhVien AND v.loaiVote = 1")
    long countUpvotesOfSinhVien(@Param("maSinhVien") String maSV);
} 