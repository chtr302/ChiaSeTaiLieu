import re
import logging

logger = logging.getLogger("TextUtils")

class TextUtils:
    def __init__(self):
        pass
    
    def preprocess_text(self, text):
        """Tiền xử lý văn bản đầu vào"""
        text = re.sub(r'[\t\n]+', '\n', text)
        text = re.sub(r' +', ' ', text)
        text = re.sub(r'\n\s*\n', '\n\n', text)
        return text.strip()
    
    def clean_summary_output(self, text):
        """Làm sạch kết quả tóm tắt"""
        text = text.replace("\\n", "\n")
        if "* " in text:
            pass
        text = re.sub(r'\*\*([^*]+)\*\*', r'\1', text)
        text = re.sub(r'\*([^*]+)\*', r'\1', text)
        text = re.sub(r'\*\*([IVXLCDM]+)\.\s*', r'\1. ', text, flags=re.IGNORECASE)
        special_chars_to_clean = ["\\", "\"", "\'", "\t"]
        for char in special_chars_to_clean:
            text = text.replace(char, "")
        text = re.sub(r' +', ' ', text)
        text = re.sub(r'\n\s*\n\s*\n', '\n\n', text)
        text = text.replace("**cánh diện**", "thành phần")
        return text.strip()
    
    def evaluate_summary_quality(self, summary, original_text):
        """Đánh giá chất lượng của bản tóm tắt"""
        quality_score = 10
        reasons = []
        
        # Kiểm tra độ dài
        word_count = len(summary.split())
        if word_count < 20:
            quality_score -= 3
            reasons.append("Tóm tắt quá ngắn")
        elif word_count > 100:
            quality_score -= 1
            reasons.append("Tóm tắt hơi dài")

        # Kiểm tra cụm từ lặp lại
        words = summary.lower().split()
        repeated_phrases = 0
        for i in range(len(words) - 3):
            if i + 3 < len(words):
                phrase = ' '.join(words[i:i+3])
                if summary.lower().count(phrase) > 2:
                    repeated_phrases += 1
        
        if repeated_phrases > 2:
            quality_score -= 2
            reasons.append("Có cụm từ lặp lại")

        # Kiểm tra từ khóa quan trọng
        important_words = set([word.lower() for word in original_text.split() 
                            if len(word) > 5 and word.isalpha()])
        summary_words = set([word.lower() for word in summary.split() 
                            if len(word) > 5 and word.isalpha()])
        
        keyword_overlap = len(important_words.intersection(summary_words)) / max(len(important_words), 1)
        if keyword_overlap < 0.1:
            quality_score -= 2
            reasons.append("Thiếu từ khóa chính")
        elif keyword_overlap > 0.3:
            quality_score += 1
            reasons.append("Nhiều từ khóa chính")

        # Kiểm tra định dạng và ngữ pháp
        if re.search(r'[.!?]\s*[a-z]', summary):
            quality_score -= 1
            reasons.append("Lỗi viết hoa sau dấu câu")
        
        if "\\n" in summary or "\\t" in summary:
            quality_score -= 1
            reasons.append("Có ký tự đặc biệt")

        # Giới hạn điểm chất lượng từ 0-10
        quality_score = max(0, min(10, quality_score))
        
        return quality_score, "; ".join(reasons)