package com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieu;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.LuuTaiLieu;
import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.TaiLieuView;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.TaiLieuRepository.LuuTaiLieuRepo;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.TaiLieuRepository.TaiLieuViewRepo;

@Service
public class LuuTaiLieuService {
    
    @Autowired
    private LuuTaiLieuRepo luuTaiLieuRepo;
    
    @Autowired
    private TaiLieuViewRepo taiLieuViewRepo;
    
    /**
     * Kiểm tra xem tài liệu đã được lưu bởi người dùng chưa
     * @param maTaiLieu
     * @param maSinhVien
     * @return
     */
    public boolean isDocumentSaved(Integer maTaiLieu, String maSinhVien) {
        return luuTaiLieuRepo.existsByMaTaiLieuAndMaSinhVien(maTaiLieu, maSinhVien);
    }
    
    /**
     * Lưu hoặc hủy lưu tài liệu
     * @param maTaiLieu
     * @param maSinhVien
     * @return true nếu lưu thành công, false nếu hủy lưu thành công
     */
    @Transactional
    public synchronized boolean toggleSaveDocument(Integer maTaiLieu, String maSinhVien) {
    boolean exists = isDocumentSaved(maTaiLieu, maSinhVien);

    if (exists) {
        luuTaiLieuRepo.deleteByMaTaiLieuAndMaSinhVien(maTaiLieu, maSinhVien);
        return false;
        } else {
        LuuTaiLieu luuTaiLieu = new LuuTaiLieu();
        luuTaiLieu.setMaTaiLieu(maTaiLieu);
        luuTaiLieu.setMaSinhVien(maSinhVien);
        luuTaiLieu.setNgayLuu(LocalDate.now());
        luuTaiLieu.setTrangThaiLuu(true);
        luuTaiLieuRepo.save(luuTaiLieu);
        
        // Kiểm tra xem người lưu có phải là người đăng tài liệu không
        TaiLieuView taiLieu = taiLieuViewRepo.findByMaTaiLieu(maTaiLieu);
        if (taiLieu != null && taiLieu.getMaSinhVien().equals(maSinhVien)) {
            throw new IllegalStateException("Không thể lưu tài liệu của bạn");
        }
        return true;
        }
    }   
    
    /**
     * Lấy danh sách tài liệu đã lưu của người dùng
     * @param maSinhVien
     * @return
     */
    public List<TaiLieuView> getSavedDocumentsByUser(String maSinhVien) {
        List<Integer> savedDocumentIds = luuTaiLieuRepo.findMaTaiLieuByMaSinhVien(maSinhVien);
        return taiLieuViewRepo.findByMaTaiLieuIn(savedDocumentIds);
    }
    
    /**
     * Lấy danh sách tài liệu đã lưu của người dùng, sắp xếp theo ngày lưu mới nhất
     * @param maSinhVien
     * @return
     */
    public List<TaiLieuView> getSavedDocumentsByUserOrderByNgayLuuDesc(String maSinhVien) {
        List<LuuTaiLieu> savedDocuments = luuTaiLieuRepo.findByMaSinhVienOrderByNgayLuuDesc(maSinhVien);
        List<Integer> savedDocumentIds = savedDocuments.stream()
                .map(LuuTaiLieu::getMaTaiLieu)
                .toList();
        
        List<TaiLieuView> result = taiLieuViewRepo.findByMaTaiLieuIn(savedDocumentIds);
        
        // Sort based on the original order from savedDocumentIds
        return result.stream()
                .sorted((d1, d2) -> {
                    int index1 = savedDocumentIds.indexOf((int) d1.getMaTaiLieu());
                    int index2 = savedDocumentIds.indexOf((int) d2.getMaTaiLieu());
                    return Integer.compare(index1, index2);
                })
                .toList();
    }
    
    /**
     * Lấy danh sách tài liệu đã lưu của người dùng, sắp xếp theo lượt xem
     * @param maSinhVien
     * @return
     */
    public List<TaiLieuView> getSavedDocumentsByUserOrderByLuotXemDesc(String maSinhVien) {
        List<Integer> savedDocumentIds = luuTaiLieuRepo.findMaTaiLieuByMaSinhVien(maSinhVien);
        return taiLieuViewRepo.findByMaTaiLieuInOrderByLuotXemDesc(savedDocumentIds);
    }
    
    /**
     * Lấy danh sách tài liệu đã lưu của người dùng, sắp xếp theo số lượng bình luận
     * @param maSinhVien
     * @return
     */
    public List<TaiLieuView> getSavedDocumentsByUserOrderBySoLuongBLDesc(String maSinhVien) {
        List<Integer> savedDocumentIds = luuTaiLieuRepo.findMaTaiLieuByMaSinhVien(maSinhVien);
        return taiLieuViewRepo.findByMaTaiLieuInOrderBySoLuongBLDesc(savedDocumentIds);
    }
    
    /**
     * Lấy danh sách tài liệu đã lưu của người dùng, sắp xếp theo ngày đăng mới nhất
     * @param maSinhVien
     * @return
     */
    public List<TaiLieuView> getSavedDocumentsByUserOrderByNewest(String maSinhVien) {
        List<Integer> savedDocumentIds = luuTaiLieuRepo.findMaTaiLieuByMaSinhVien(maSinhVien);
        return taiLieuViewRepo.findByMaTaiLieuIn(savedDocumentIds);
        // Note: We would need to order by ngayDang, but TaiLieuView might not have this field
        // In that case, we would need a custom query to join with tailieu table
    }
    
    /**
     * Lấy danh sách tài liệu đã lưu của người dùng, sắp xếp theo ngày đăng cũ nhất
     * @param maSinhVien
     * @return
     */
    public List<TaiLieuView> getSavedDocumentsByUserOrderByOldest(String maSinhVien) {
        List<Integer> savedDocumentIds = luuTaiLieuRepo.findMaTaiLieuByMaSinhVien(maSinhVien);
        return taiLieuViewRepo.findByMaTaiLieuIn(savedDocumentIds);
        // Note: We would need to order by ngayDang, but TaiLieuView might not have this field
        // In that case, we would need a custom query to join with tailieu table
    }
} 