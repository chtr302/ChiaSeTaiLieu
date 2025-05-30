package com.chiasetailieu.chiasetailieuhoctapptit.repository.TheoDoi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.TheoDoi.Follow_VW;

@Repository
public interface Follow_vw_Repo extends JpaRepository<Follow_VW, String> {
    Follow_VW findByMaSV(String maSV);
}
