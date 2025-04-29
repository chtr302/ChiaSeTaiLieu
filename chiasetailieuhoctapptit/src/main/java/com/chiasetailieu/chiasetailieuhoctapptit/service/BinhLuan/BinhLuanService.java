package com.chiasetailieu.chiasetailieuhoctapptit.service.BinhLuan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chiasetailieu.chiasetailieuhoctapptit.model.BinhLuanModel.BinhLuan;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.BinhLuanRepository.BinhLuanRepo;

@Service
public class BinhLuanService {
    @Autowired
    private BinhLuanRepo binhLuanRepo;

    public BinhLuan saveBinhLuan(BinhLuan binhLuan){
        return binhLuanRepo.save(binhLuan);
    }

    public List<BinhLuan> getCommentByDocumentId(long id){
        return binhLuanRepo.findByTaiLieu(id);
    }
}
