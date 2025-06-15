package com.chiasetailieu.chiasetailieuhoctapptit.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class ThumbnailService {

    @Value("${thumbnail.dir:./thumbnails}")
    private String thumbnailDir;

    @Value("${thumbnail.width:300}")
    private int thumbnailWidth;
    
    @Value("${thumbnail.height:400}")
    private int thumbnailHeight;

    @PostConstruct
    public void init() {
        try {
            Path thumbnailPath = Paths.get(thumbnailDir);
            if (!Files.exists(thumbnailPath)) {
                Files.createDirectories(thumbnailPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Không thể tạo thư mục thumbnail", e);
        }
    }

    /**
     * Tạo thumbnail từ file PDF
     * @param pdfFile đường dẫn đến file PDF
     * @return tên file thumbnail
     */
    public String generatePdfThumbnail(String pdfFile) {
        String thumbnailFileName = UUID.randomUUID().toString() + ".png";
        Path thumbnailPath = Paths.get(thumbnailDir, thumbnailFileName);
        
        try {
            File pdf = new File(pdfFile);
            try (PDDocument document = PDDocument.load(pdf)) {
                PDFRenderer renderer = new PDFRenderer(document);
                
                BufferedImage image = renderer.renderImageWithDPI(0, 300);
                
                BufferedImage thumbnail = resizeImage(image, thumbnailWidth, thumbnailHeight);
                
                ImageIO.write(thumbnail, "PNG", thumbnailPath.toFile());
            }
            
            return thumbnailFileName;
        } catch (IOException e) {
            return "default-pdf.png";
        }
    }
    

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        
        return resizedImage;
    }
}