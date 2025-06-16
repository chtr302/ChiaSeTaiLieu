package com.chiasetailieu.chiasetailieuhoctapptit.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.chiasetailieu.chiasetailieuhoctapptit.service.AI.AIService;

import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/ai")
public class AIController {
    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;
    private final WebClient webClient;
    private final AIService aiService;

    public AIController(WebClient aiServiceClient, AIService aiService) {
        this.webClient = aiServiceClient;
        this.aiService = aiService;
    }

    @GetMapping("/health")
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> checkHealth() {
        return aiService.checkAIServiceHealth()
            .map(ResponseEntity::ok);
    }
    @PostMapping("/summarize-upload")
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> summarizeUploadedFile(
            @RequestParam("file") MultipartFile file) {
        try {
            java.io.File tmp = java.io.File.createTempFile("upload-", "-" + file.getOriginalFilename());
            file.transferTo(tmp);

            return aiService.summarizeUploadedFile(tmp)
                .map(ResponseEntity::ok)
                .doFinally(sig -> tmp.delete());
        } catch (IOException e) {
            return Mono.just(ResponseEntity.status(500).body(Map.of(
                "error", "File Processing Error",
                "message", "Không thể xử lý file: " + e.getMessage()
            )));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return Mono.just(ResponseEntity.status(400).body(Map.of(
                "error", "Invalid Request",
                "message", "Yêu cầu không hợp lệ: " + e.getMessage()
            )));
        }
    }
    
    @PostMapping("/create-session")
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> createQASession(
            @RequestParam("filename") String filename) {
        File file = Paths.get(uploadDir, filename).toFile();
        if (!file.exists()) {
            return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "File không tồn tại", "message", filename)));
        }
        
        return aiService.createQASession(file.getAbsolutePath(), filename)
            .map(ResponseEntity::ok)
            .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "AI Service Error", "message", e.getMessage()))));
    }

    @PostMapping("/session/ask")
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> askQuestionInSession(
            @RequestParam("sessionId") String sessionId,
            @RequestParam("question") String question) {
        
        return aiService.askQuestionInSession(sessionId, question)
            .map(ResponseEntity::ok)
            .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "AI Service Error", "message", e.getMessage()))));
    }

    @GetMapping("/session/history")
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> getSessionHistory(
            @RequestParam("sessionId") String sessionId) {
        
        return aiService.getSessionHistory(sessionId)
            .map(ResponseEntity::ok)
            .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "AI Service Error", "message", e.getMessage()))));
    }

    @PostMapping("/ask-question")
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> askQuestion(
            @RequestParam("filename") String filename,
            @RequestParam("question") String question) {
        File file = Paths.get(uploadDir, filename).toFile();
        if (!file.exists()) {
            return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error","File không tồn tại","message", filename)));
        }
        System.out.println(webClient);
        return aiService.askQuestion(file.getAbsolutePath(), question)
            .map(ResponseEntity::ok)
            .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error","AI Service Error","message",e.getMessage()))));
    }

    @PostMapping("/chatbot/session")
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> createchatbotSession(
            @RequestParam("user_id") String user_id) {
        return aiService.chatbotSession(user_id)
            .map(ResponseEntity::ok)
            .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", "Chatbot Session Error", "message", e.getMessage()))));
    }

    @PostMapping("/chatbot/chat")
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> chatWithChatbot(
            @RequestParam("session_id") String sessionId,
            @RequestParam("question") String question,
            @RequestParam(value = "chat_type", defaultValue = "general") String chatType,
            @RequestParam(value = "user_id", required = false) String userId) {
        
        return aiService.chatWithChatbotInSession(sessionId, question, chatType, userId)
            .map(ResponseEntity::ok)
            .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Chatbot Error", "message", e.getMessage()))));
    }
    @PostMapping("/chatbot/auto")
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> autoChat(
            @RequestParam("question") String question,
            @RequestParam(value = "session_id", required = false) String sessionId,
            @RequestParam(value = "chat_type", defaultValue = "search") String chatType,
            @RequestParam(value = "user_id", required = false) String userId) {
        
        return aiService.chatWithChatbot(sessionId, question, chatType, userId)
            .map(ResponseEntity::ok)
            .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Auto Chat Error", "message", e.getMessage()))));
    }
}
