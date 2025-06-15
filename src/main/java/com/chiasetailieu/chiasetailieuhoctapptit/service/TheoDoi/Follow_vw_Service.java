package com.chiasetailieu.chiasetailieuhoctapptit.service.TheoDoi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chiasetailieu.chiasetailieuhoctapptit.model.TheoDoi.Follow_VW;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.TheoDoi.Follow_vw_Repo;

@Service
public class Follow_vw_Service {
    @Autowired
    private Follow_vw_Repo follow_vw_Repo;

    public Follow_VW getFollowStatsbyMaSV(String maSV){
        return follow_vw_Repo.findByMaSV(maSV);
    }
}
