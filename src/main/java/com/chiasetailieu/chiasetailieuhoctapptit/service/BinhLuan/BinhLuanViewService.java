package com.chiasetailieu.chiasetailieuhoctapptit.service.BinhLuan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chiasetailieu.chiasetailieuhoctapptit.model.BinhLuanModel.BinhLuanView;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.BinhLuanRepository.BinhLuanViewRepo;

@Service
public class BinhLuanViewService {
    @Autowired
    private BinhLuanViewRepo binhLuanViewRepo;

    public List<BinhLuanView> getCommentByDocumentId(long id){
        return binhLuanViewRepo.findByMaTaiLieu(id);
    }
}
