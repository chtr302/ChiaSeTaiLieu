package com.chiasetailieu.chiasetailieuhoctapptit.service.VoteService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.TaiLieu;
import com.chiasetailieu.chiasetailieuhoctapptit.model.VoteModel.Vote;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.TaiLieuRepository.TaiLieuRepo;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.VoteRepository.VoteRepository;

@Service
public class VoteService {
    
    @Autowired
    private VoteRepository voteRepository;
    
    @Autowired
    private TaiLieuRepo taiLieuRepo;
    
    @Transactional
    public String vote(Long maTaiLieu, String maSinhVien, int loaiVote) {
        // Kiểm tra tài liệu có tồn tại không
        Optional<TaiLieu> taiLieuOpt = taiLieuRepo.findById(maTaiLieu);
        if (!taiLieuOpt.isPresent()) {
            return "Tài liệu không tồn tại";
        }
        
        TaiLieu taiLieu = taiLieuOpt.get();
        
        // Kiểm tra user đã vote cho tài liệu này chưa
        Optional<Vote> existingVote = voteRepository.findByMaTaiLieuAndMaSinhVien(maTaiLieu, maSinhVien);
        
        if (existingVote.isPresent()) {
            Vote currentVote = existingVote.get();
            
            // Nếu vote giống nhau thì bỏ vote (toggle off)
            if (currentVote.getLoaiVote().equals(loaiVote)) {
                voteRepository.delete(currentVote);
                updateTaiLieuVoteCount(taiLieu);
                return "Đã bỏ vote";
            } else {
                // Nếu khác thì cập nhật vote
                currentVote.setLoaiVote(loaiVote);
                voteRepository.save(currentVote);
                updateTaiLieuVoteCount(taiLieu);
                return loaiVote == 1 ? "Đã upvote" : "Đã downvote";
            }
        } else {
            // Tạo vote mới
            Vote newVote = new Vote(maTaiLieu, maSinhVien, loaiVote);
            voteRepository.save(newVote);
            updateTaiLieuVoteCount(taiLieu);
            return loaiVote == 1 ? "Đã upvote" : "Đã downvote";
        }
    }
    
    private void updateTaiLieuVoteCount(TaiLieu taiLieu) {
        long upvoteCount = voteRepository.countUpvotesByMaTaiLieu(taiLieu.getMaTaiLieu());
        long downvoteCount = voteRepository.countDownvotesByMaTaiLieu(taiLieu.getMaTaiLieu());
        
        // Logic theo yêu cầu: downvote được lưu nhưng chỉ hiển thị khi upvote vượt quá
        taiLieu.setUpVote((int) upvoteCount);
        taiLieu.setDownVote((int) downvoteCount);
        
        taiLieuRepo.save(taiLieu);
    }
    
    // Tính điểm hiển thị theo logic yêu cầu
    public int getDisplayScore(TaiLieu taiLieu) {
        int upvote = taiLieu.getUpVote() != null ? taiLieu.getUpVote() : 0;
        int downvote = taiLieu.getDownVote() != null ? taiLieu.getDownVote() : 0;
        
        // Nếu chưa có upvote nào và có downvote thì hiển thị 0
        if (upvote == 0 && downvote > 0) {
            return 0;
        }
        
        // Nếu có upvote thì hiển thị upvote - downvote
        return upvote - downvote;
    }
    
    // Kiểm tra user đã vote gì cho tài liệu này
    public Integer getUserVoteStatus(Long maTaiLieu, String maSinhVien) {
        Optional<Vote> vote = voteRepository.findByMaTaiLieuAndMaSinhVien(maTaiLieu, maSinhVien);
        return vote.map(Vote::getLoaiVote).orElse(0);
    }
    
    // Lấy thông tin chi tiết vote
    public VoteInfo getVoteInfo(Long maTaiLieu, String maSinhVien) {
        long upvoteCount = voteRepository.countUpvotesByMaTaiLieu(maTaiLieu);
        long downvoteCount = voteRepository.countDownvotesByMaTaiLieu(maTaiLieu);
        Integer userVote = getUserVoteStatus(maTaiLieu, maSinhVien);
        
        // Tính điểm hiển thị
        int displayScore;
        if (upvoteCount == 0 && downvoteCount > 0) {
            displayScore = 0; // Không hiển thị âm khi chưa có upvote
        } else {
            displayScore = (int) (upvoteCount - downvoteCount);
        }
        
        return new VoteInfo(displayScore, (int) upvoteCount, (int) downvoteCount, userVote);
    }
    
    // Class để trả về thông tin vote
    public static class VoteInfo {
        private final int displayScore;
        private final int upvoteCount;
        private final int downvoteCount;
        private final int userVote;
        
        public VoteInfo(int displayScore, int upvoteCount, int downvoteCount, int userVote) {
            this.displayScore = displayScore;
            this.upvoteCount = upvoteCount;
            this.downvoteCount = downvoteCount;
            this.userVote = userVote;
        }
        
        // Getters
        public int getDisplayScore() { return displayScore; }
        public int getUpvoteCount() { return upvoteCount; }
        public int getDownvoteCount() { return downvoteCount; }
        public int getUserVote() { return userVote; }
    }
    public long countUpvotesOfSinhVien(@Param("maSinhVien") String maSV){
        return  voteRepository.countUpvotesOfSinhVien(maSV);
    }
} 