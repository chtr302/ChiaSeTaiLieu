package com.chiasetailieu.chiasetailieuhoctapptit.service.BinhLuan;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chiasetailieu.chiasetailieuhoctapptit.model.BinhLuanModel.BaoCao;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.BinhLuanRepository.BaoCaoRepository;

@Service
public class BaoCaoService {
    
    @Autowired
    private BaoCaoRepository baoCaoRepository;

    /**
     * Lưu báo cáo tài liệu vào database
     */
    public BaoCao saveDocumentReport(String tieuDe, String noiDung, Integer maTaiLieu, String maSinhVien) {
        BaoCao baoCao = new BaoCao();
        baoCao.setTieuDeBaoCao(tieuDe);
        baoCao.setNoiDungBaoCao(noiDung);
        baoCao.setNgayBaoCao(LocalDate.now());
        baoCao.setTrangThai(false); // false = chưa xử lý
        baoCao.setMaTaiLieu(maTaiLieu);
        baoCao.setMaSinhVien(maSinhVien);
        
        return baoCaoRepository.save(baoCao);
    }

    /**
     * Lưu báo cáo bình luận vào database 
     * Lưu với nội dung bao gồm thông tin bình luận bị báo cáo
     */
    public BaoCao saveCommentReport(String tieuDe, String noiDung, Integer maTaiLieu, String maSinhVien) {
        BaoCao baoCao = new BaoCao();
        baoCao.setTieuDeBaoCao(tieuDe);
        baoCao.setNoiDungBaoCao(noiDung);
        baoCao.setNgayBaoCao(LocalDate.now());
        baoCao.setTrangThai(false); // false = chưa xử lý
        baoCao.setMaTaiLieu(maTaiLieu);
        baoCao.setMaSinhVien(maSinhVien);
        
        return baoCaoRepository.save(baoCao);
    }
} 