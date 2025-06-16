package com.chiasetailieu.chiasetailieuhoctapptit.service.AI;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class AIService {
    private final WebClient webClient;

    public AIService(WebClient aiServiceClient){
        this.webClient = aiServiceClient;
    }
    public Mono<Map<String, Object>> summarizeUploadedFile(File file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new FileSystemResource(file));

        return webClient.post()
            .uri("/summarize-upload")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData(builder.build()))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
            .flatMap(resp -> {
                String taskId = resp.get("task_id").toString();
                return pollSummaryUntilDone(taskId, 20, Duration.ofSeconds(2));
            })
            .timeout(Duration.ofMinutes(10))
            .onErrorResume(e -> Mono.just(Map.of(
                "error", "AI Service Error",
                "message", e.getMessage()
            )));
    }

    private Mono<Map<String,Object>> pollSummaryUntilDone(String taskId, int retries, Duration delay) {
        return webClient.get()
            .uri("/summary-result/{id}", taskId)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
            .flatMap(result -> {
                String status = result.get("status").toString();
                if ("completed".equals(status)) {
                    return Mono.just(result);
                }
                if (retries > 1) {
                    return Mono.delay(delay)
                               .then(pollSummaryUntilDone(taskId, retries - 1, delay));
                }
                return Mono.error(new RuntimeException("Summary not ready after retries"));
            });
    }
    
    public Mono<Map<String, Object>> createQASession(String filePath, String filename) {
        return webClient.post()
            .uri("/create_session")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("file_name", filename))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .onErrorMap(e -> new RuntimeException("Error creating Q&A session: " + e.getMessage(), e));
    }

    public Mono<Map<String, Object>> askQuestionInSession(String sessionId, String question) {
        return webClient.post()
            .uri("/session/{sessionId}/ask", sessionId)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("question", question)
                .with("include_history", "true"))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .onErrorMap(e -> new RuntimeException("Error asking question in session: " + e.getMessage(), e));
    }

    public Mono<Map<String, Object>> getSessionHistory(String sessionId) {
        return webClient.get()
            .uri("/session/{sessionId}/history", sessionId)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .onErrorMap(e -> new RuntimeException("Error getting session history: " + e.getMessage(), e));
    }

    public Mono<Map<String, Object>> askQuestion(String filePath, String question) {
        String filename = Paths.get(filePath).getFileName().toString();
        return webClient.post()
            .uri("/ask-document")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("filename", filename)
                               .with("question", question))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>(){})
            .onErrorResume(e -> Mono.just(Map.of("error","Lỗi khi kết nối đến AI Service","message",e.getMessage())));
    }    

    public Mono<Map<String, Object>> chatbotSession(String maSV){
        return webClient.post()
            .uri("/chatbot/session")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("user_id", maSV))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .onErrorMap(e -> new RuntimeException("Error creating Chatbot session: " + e.getMessage(), e));
    }

    public Mono<Map<String, Object>> chatWithChatbotInSession(String sessionId, String question, String chat_type, String maSV){
        return webClient.post()
            .uri("/chatbot/chat")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("session_id", sessionId)
                .with("question", question)
                .with("chat_type", chat_type)
                .with("user_id", maSV))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .onErrorMap(e -> new RuntimeException("Error asking question in session: " + e.getMessage(), e));
    }

    public Mono<Map<String, Object>> chatWithChatbot(String sessionId, String question, String chatType, String userId) {
    // Nếu sessionId null, tạo session mới
    if (sessionId == null || sessionId.trim().isEmpty()) {
        return chatbotSession(userId)
            .flatMap(sessionResponse -> {
                String newSessionId = sessionResponse.get("session_id").toString();
                return chatWithChatbotInSession(newSessionId, question, chatType, userId);
            });
    } else {
        return chatWithChatbotInSession(sessionId, question, chatType, userId);
    }
}

    public Mono<Map<String, Object>> checkAIServiceHealth() {
        return webClient.get()
            .uri("/health")
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .onErrorResume(e -> {
                return Mono.just(Map.of(
                    "status", "unavailable",
                    "error", e.getMessage()
                ));
            });
    }
}
