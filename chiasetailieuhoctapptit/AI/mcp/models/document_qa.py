import logging, requests

logger = logging.getLogger("DocumentQA")

class DocumentQA:
    def __init__(self, model_name="gemma3:4b", api = "http://localhost:11434"):
        self.model_name = model_name
        self.api = api
        self.generate_endpoint = f"{api}/api/generate"

        try:
            self._check_connection()
        except Exception as e:
            logger.error(f"chua ket noi dien ollalma: {str(e)}")

    def _check_connection(self):
        try:
            response = requests.get(f"{self.api}/api/tags")
            if response.status_code != 200:
                raise ConnectionError(f"Hinh nhu may chua bat ollama")
            
            models = response.json().get("models", [])
            model_availble = any(model['name'] == self.model_name for model in models)
        except Exception as e:
            raise ConnectionError(f"Hinh nhu may chua bat ollama")
        
    def answer_question(self, document_text, question, context, max_length=300):
        try:
            max_document_length = 6000
            if len(document_text) > max_document_length:
                logger.info(f"Tài liệu dài, cắt ngắn còn {max_document_length} ký tự")
                document_text = document_text[:max_document_length]
            prompt = ""
            if context and len(context.strip()) > 0:
                prompt += f"""Dưới đây là lịch sử các câu hỏi và trả lời trước đó:

    {context}

    """
                
            prompt += f"""Tôi sẽ cung cấp một tài liệu và một câu hỏi. Hãy trả lời câu hỏi dựa vào thông tin trong tài liệu.
    Chỉ sử dụng thông tin từ tài liệu đã cho. Nếu không thể trả lời từ tài liệu, hãy nói rằng "Tôi không tìm thấy thông tin để trả lời câu hỏi này trong tài liệu."

    ### TÀI LIỆU:
    {document_text}

    ### CÂU HỎI:
    {question}

    ### TRẢ LỜI:"""
            
            payload = {
                "model": self.model_name,
                "prompt": prompt,
                "stream": False,
                "options": {
                    "temperature": 0.7,
                    "top_p": 0.9,
                    "max_tokens": max_length,
                    "stop": ["###"]
                }
            }
            
            response = requests.post(self.generate_endpoint, json=payload)
            
            if response.status_code != 200:
                error_msg = f"Lỗi API Ollama: {response.status_code} - {response.text}"
                logger.error(error_msg)
                return f"Không thể tạo câu trả lời: {error_msg}"

            result = response.json()
            answer = result.get("response", "")
            
            return answer.strip()
            
        except Exception as e:
            logger.error(f"Lỗi khi tạo câu trả lời: {str(e)}")
            return f"Đã xảy ra lỗi khi xử lý câu hỏi: {str(e)}"
