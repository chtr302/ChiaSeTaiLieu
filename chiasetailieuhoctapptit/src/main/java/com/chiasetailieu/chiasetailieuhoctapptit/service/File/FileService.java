package com.chiasetailieu.chiasetailieuhoctapptit.service.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chiasetailieu.chiasetailieuhoctapptit.model.FileModel.File;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.FileRepository.FileRepo;
import com.chiasetailieu.chiasetailieuhoctapptit.service.ThumbnailService;

import jakarta.annotation.PostConstruct;

@Service
public class FileService {
    
    @Autowired
    private FileRepo fileRepo;
    
    @Autowired
    private ThumbnailService thumbnailService;
    
    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;
    
    @PostConstruct
    public void init() {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Không thể tạo thư mục upload", e);
        }
    }
    
    public File saveFile(MultipartFile file) throws IOException {
        // Tạo tên file mới để tránh trùng lặp
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String newFilename = UUID.randomUUID().toString() + "." + fileExtension;
        
        // Tạo đường dẫn lưu file
        Path targetLocation = Paths.get(uploadDir).resolve(newFilename);
        
        // Copy file vào thư mục đích
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        System.out.println("Đã lưu file: " + targetLocation.toAbsolutePath());
        
        // Tạo entity File để lưu vào database
        File fileEntity = new File();
        fileEntity.setTenFile(originalFilename);
        fileEntity.setDuongDanFile(newFilename);
        fileEntity.setDinhDang(fileExtension);
        fileEntity.setKichThuoc(file.getSize());
        
        if (fileExtension.equalsIgnoreCase("pdf")) {
            try {
                String thumbnailName = thumbnailService.generatePdfThumbnail(targetLocation.toString());
                fileEntity.setThumbnail(thumbnailName);
            } catch (Exception e) {
                fileEntity.setThumbnail("default-pdf.png");
            }
        } else {
            fileEntity.setThumbnail("default.png");
        }
        
        // Lưu thông tin file vào database
        return fileRepo.save(fileEntity);
    }
    
    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0) return "";
        return filename.substring(dotIndex + 1);
    }

    public List<File> getFileById(int id){
        return fileRepo.findByMaFile(id);
    }
}