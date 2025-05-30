package com.chiasetailieu.chiasetailieuhoctapptit.repository.FileRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.FileModel.File;

@Repository
public interface FileRepo extends JpaRepository<File, Integer> {
    List<File> findByMaFile(int maFile);
}
