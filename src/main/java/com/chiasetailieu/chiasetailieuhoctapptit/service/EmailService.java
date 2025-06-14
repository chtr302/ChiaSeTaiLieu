package com.chiasetailieu.chiasetailieuhoctapptit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.chiasetailieu.chiasetailieuhoctapptit.model.SinhVienModel.SinhVien;
import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.TaiLieuView;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Gửi email thông báo khi có báo cáo tài liệu
     */
    public void sendDocumentReportNotification(SinhVien reportedStudent, SinhVien reporter, 
                                             TaiLieuView document, String reason, String description) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(reportedStudent.getEmail());
            message.setSubject("Thông báo: Tài liệu của bạn đã bị báo cáo");
            
            String emailContent = String.format(
                "Xin chào %s,\n\n" +
                "Tài liệu '%s' (ID: %d) của bạn đã bị báo cáo bởi sinh viên %s (%s).\n\n" +
                "Lý do báo cáo: %s\n" +
                "Mô tả chi tiết: %s\n\n" +
                "Vui lòng kiểm tra và chỉnh sửa tài liệu nếu cần thiết.\n" +
                "Nếu bạn có thắc mắc, vui lòng liên hệ với quản trị viên.\n\n" +
                "Trân trọng,\n" +
                "Hệ thống Chia sẻ Tài liệu PTIT",
                reportedStudent.getHoVaTen(),
                document.getTieuDe(),
                document.getMaTaiLieu(),
                reporter.getHoVaTen(),
                reporter.getMaSV(),
                reason,
                description
            );
            
            message.setText(emailContent);
            mailSender.send(message);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi gửi email báo cáo tài liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gửi email thông báo khi có báo cáo bình luận
     */
    public void sendCommentReportNotification(SinhVien reportedStudent, SinhVien reporter, 
                                            TaiLieuView document, String title, String commentContent, String description) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(reportedStudent.getEmail());
            message.setSubject("Thông báo: Bình luận của bạn đã bị báo cáo");
            
            String emailContent = String.format(
                "Xin chào %s,\n\n" +
                "Bình luận của bạn trong tài liệu '%s' (ID: %d) đã bị báo cáo bởi sinh viên %s (%s).\n\n" +
                "Tiêu đề báo cáo: %s\n" +
                "Nội dung bình luận bị báo cáo: %s\n" +
                "Mô tả chi tiết: %s\n\n" +
                "Vui lòng kiểm tra và chỉnh sửa bình luận nếu cần thiết.\n" +
                "Nếu bạn có thắc mắc, vui lòng liên hệ với quản trị viên.\n\n" +
                "Trân trọng,\n" +
                "Hệ thống Chia sẻ Tài liệu PTIT",
                reportedStudent.getHoVaTen(),
                document.getTieuDe(),
                document.getMaTaiLieu(),
                reporter.getHoVaTen(),
                reporter.getMaSV(),
                title,
                commentContent,
                description
            );
            
            message.setText(emailContent);
            mailSender.send(message);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi gửi email báo cáo bình luận: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 