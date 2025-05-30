package com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.TaiLieuView;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.TaiLieuRepository.TaiLieuViewRepo;

@Service
public class TaiLieuViewService {
    @Autowired
    private TaiLieuViewRepo taiLieuViewRepo;

    public List<TaiLieuView> getTaiLieu(){
        return taiLieuViewRepo.findAll();
    }

    public TaiLieuView findByMaTaiLieu(Long id){
        return  taiLieuViewRepo.findByMaTaiLieu(id);
    }
    
    /**
     * Lấy danh sách tài liệu theo MaSinhVien
     * @param maSinhVien
     * @return
     */
    public List<TaiLieuView> findByMaSinhVien(String maSinhVien) {
        return taiLieuViewRepo.findByMaSinhVien(maSinhVien);
    }
    
    /**
     * Lấy danh sách tài liệu theo MaSinhVien, sắp xếp theo lượt xem giảm dần
     * @param maSinhVien
     * @return
     */
    public List<TaiLieuView> findByMaSinhVienOrderByLuotXemDesc(String maSinhVien) {
        return taiLieuViewRepo.findByMaSinhVienOrderByLuotXemDesc(maSinhVien);
    }
    
    /**
     * Lấy danh sách tài liệu theo MaSinhVien, sắp xếp theo số lượng bình luận giảm dần
     * @param maSinhVien
     * @return
     */
    public List<TaiLieuView> findByMaSinhVienOrderBySoLuongBLDesc(String maSinhVien) {
        return taiLieuViewRepo.findByMaSinhVienOrderBySoLuongBLDesc(maSinhVien);
    }
    
    /**
     * Lấy danh sách tài liệu theo MaSinhVien, sắp xếp theo ngày đăng tăng dần (cũ nhất)
     * @param maSinhVien
     * @return
     */
    public List<TaiLieuView> findByMaSinhVienOrderByNgayDangAsc(String maSinhVien) {
        return taiLieuViewRepo.findByMaSinhVienOrderByNgayDangAsc(maSinhVien);
    }
    
    /**
     * Lấy danh sách tài liệu theo MaSinhVien, sắp xếp theo ngày đăng giảm dần (mới nhất)
     * @param maSinhVien
     * @return
     */
    public List<TaiLieuView> findByMaSinhVienOrderByNgayDangDesc(String maSinhVien) {
        return taiLieuViewRepo.findByMaSinhVienOrderByNgayDangDesc(maSinhVien);
    }
}
