import logging, torch, re
from transformers import pipeline, AutoModelForSeq2SeqLM, AutoTokenizer

logger = logging.getLogger("Summarizer")

class Summarizer:
    def __init__(self, vi_model_name="NlpHUST/t5-small-vi-summarization", en_model_name="facebook/bart-large-cnn"):
        if torch.backends.mps.is_available():
            self.device = "mps"  # Metal Performance Shaders cho Apple Silicon
        elif torch.cuda.is_available():
            self.device = 1  # GPU
        else:
            self.device = -1  # CPU

        # VN - NLPHust
        try:
            self.vi_tokenizer = AutoTokenizer.from_pretrained(vi_model_name)
            self.vi_model = AutoModelForSeq2SeqLM.from_pretrained(vi_model_name)
            self.vi_pipeline = pipeline(
                "summarization",
                model=self.vi_model,
                tokenizer=self.vi_tokenizer,
                device=self.device,
                framework='pt'
            )
            self.vi_model_name = vi_model_name
        except Exception as e:
            logger.error(f"Lỗi khi tải mô hình tiếng Việt: {str(e)}")
            self.vi_pipeline = None

        # EN - facebook/bart-large-cnn
        try:
            self.en_tokenizer = AutoTokenizer.from_pretrained(en_model_name)
            self.en_model = AutoModelForSeq2SeqLM.from_pretrained(en_model_name)
            self.en_pipeline = pipeline(
                "summarization",
                model=self.en_model,
                tokenizer=self.en_tokenizer,
                device=self.device,
                framework='pt'
            )
            self.en_model_name = en_model_name
        except Exception as e:
            logger.error(f"Lỗi khi tải mô hình tiếng Anh: {str(e)}")
            self.en_pipeline = None

        if self.vi_pipeline is None and self.en_pipeline is None:
            try:
                self.model = pipeline(
                    "summarization",
                    model="facebook/bart-large-cnn",
                    device=self.device,
                    framework='pt'
                )
            except Exception as e:
                logger.error(f"Không thể tải bất kỳ mô hình nào: {str(e)}")
                raise RuntimeError("Không thể khởi tạo bất kỳ mô hình tóm tắt nào")
    
    def summarize_with_consistency(self, text, max_length=300, attempts=1, min_length=50):
        if not text or len(text.strip()) < 50:
            return "Văn bản quá ngắn để tóm tắt"

        max_input_length = 5000
        if len(text) > max_input_length:
            text = text[:max_input_length]

        is_vietnamese = self._is_vietnamese(text)

        if is_vietnamese:
                
            # Thêm prefix nếu cần thiết cho NLPHust
            if not text.startswith("tóm tắt:"):
                text = "tóm tắt: " + text

            try:
                summary = self.vi_pipeline(
                    text,
                    max_length=max_length,
                    min_length=min_length,
                    do_sample=False,
                    num_beams=4, # dùng beam search để tăng khả năng tìm kiếm từ khóa
                    no_repeat_ngram_size=3, # tránh lặp từ
                    early_stopping=True # dừng nếu tìm được result tốt
                )[0]['summary_text']
                return summary
            except Exception as e:
                logger.error(f"lỗi tóm tắt tiếng việt: {str(e)}")
        else:
            logger.info("NLPHust lỗi - dùng BART")

            return self._summarize_multiple_temps(self.en_pipeline, text, max_length, min_length, attempts)
    
    def _summarize_multiple_temps(self, model, text, max_length, min_length, attempts=1):
        results = []
        temperatures = [0.0, 0.3]

        for temp in temperatures[:attempts]:
            try:
                summary = model(
                    text,
                    max_length=max_length,
                    min_length=min_length,
                    do_sample=(temp > 0),
                    temperature=temp,
                    top_p=0.95,
                    num_beams=4 if temp == 0 else 2,
                    no_repeat_ngram_size=3,
                    early_stopping=True
                )[0]['summary_text']
                results.append(summary)
            except Exception as e:
                logger.error(f"Lỗi khi tóm tắt với temp={temp}: {str(e)}")
                continue
                
        if len(results) > 1:
            candidates = [(r, self._check_summary_quality(r)) for r in results]
            best_result = max(candidates, key=lambda x: x[1])[0]
            return best_result
            
        return results[0] if results else "Không thể tạo tóm tắt do lỗi xử lý."
    
    def summarize_long_document(self, text, max_length=300, text_utils=None):
        """Tóm tắt văn bản dài bằng cách chia nhỏ"""
        if text_utils:
            text = text_utils.preprocess_text(text)

        if len(text.split()) < 5000:
            return self.summarize_with_consistency(text, max_length)

        chunks = self._split_into_chunks(text, max_chunk_size=5000)

        chunk_summaries = []
        for i, chunk in enumerate(chunks):
            summary = self.summarize_with_consistency(chunk, max_length=200)
            chunk_summaries.append(summary)

        combined_summary = " ".join(chunk_summaries)

        final_summary = self.summarize_with_consistency(combined_summary, max_length)
        
        if text_utils:
            final_summary = text_utils.clean_summary_output(final_summary)
            
        return final_summary

    def _split_into_chunks(self, text, max_chunk_size=5000):
        """Chia văn bản dài thành các phần nhỏ hơn"""
        paragraphs = text.split('\n\n')
        chunks = []
        current_chunk = []
        current_size = 0
        
        for para in paragraphs:
            para_words = para.split()
            para_size = len(para_words)
            
            if para_size > max_chunk_size:
                if current_chunk:
                    chunks.append(' '.join(current_chunk))
                    current_chunk = []
                    current_size = 0
                
                for i in range(0, para_size, max_chunk_size):
                    sub_para = ' '.join(para_words[i:min(i + max_chunk_size, para_size)])
                    chunks.append(sub_para)
            else:
                if current_size + para_size > max_chunk_size:
                    chunks.append(' '.join(current_chunk))
                    current_chunk = [para]
                    current_size = para_size
                else:
                    current_chunk.append(para)
                    current_size += para_size
        
        if current_chunk:
            chunks.append(' '.join(current_chunk))
        
        return chunks
    
    def _check_summary_quality(self, summary):
        """Đánh giá chất lượng của bản tóm tắt và trả về điểm (0-10)"""
        score = 10

        word_count = len(summary.split())
        if word_count < 20:
            score -= 5  # quá ngắn
        elif word_count < 50:
            score -= 2  # hơi ngắn
        
        # Kiểm tra lặp lại
        words = summary.lower().split()
        repeated_phrases = 0
        for i in range(len(words) - 3):
            if i + 3 < len(words):
                phrase = ' '.join(words[i:i+3])
                if summary.lower().count(phrase) > 2:
                    repeated_phrases += 1
        score -= repeated_phrases * 2  # Trừ điểm cho mỗi cụm từ lặp lại

        nonsense_patterns = ["the the", "of the the", "and and"]
        for pattern in nonsense_patterns:
            if pattern in summary.lower():
                score -= 2
        
        return max(0, score)  # Điểm tối thiểu là 0
        
    def _is_vietnamese(self, text):
        """Xác định đây có phải tiếng việt không"""
        sample = text.lower()[:1000]
        words = re.sub(r'[.,!?;:"\'()[\]{}]', ' ', sample).split()

        vn_chars = 'áàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđ'

        vn_words = {
            'và', 'của', 'là', 'có', 'không', 'người', 'đã', 'được', 'trong', 'cho',
            'các', 'với', 'này', 'để', 'một', 'những', 'khi', 'từ', 'mà', 'đến',
            'tại', 'về', 'như', 'nên', 'vì', 'theo', 'tôi', 'sẽ', 'thì', 'rồi'
        }
        
        # Tính điểm
        char_score = sum(1 for c in sample if c in vn_chars)
        word_score = sum(3 for word in words if word in vn_words)
        pattern_score = 5 if any(p in sample for p in ['đã được', 'sẽ được', 'có thể']) else 0
        
        total_score = char_score + word_score + pattern_score
        
        logger.info(f"Phát hiện ngôn ngữ: {total_score} điểm - {'Tiếng Việt' if total_score >= 8 else 'Tiếng Anh'}")
        return total_score >= 8