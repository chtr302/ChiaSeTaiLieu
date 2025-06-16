import os,requests,json
from dotenv import load_dotenv
load_dotenv()

MODEL_NAME = os.getenv('AI_MODEL')
MODEL_API = os.getenv('AI_API')

class DatabaseAI:
    def __init__(self, model_name = MODEL_NAME, api = MODEL_API):
        self.model_name = model_name
        self.api = api
        self.generation_api = f'{api}/api/generate'

    def _call_model(self, prompt, temperature=0.1):
        try:
            payload = {
                "model": self.model_name,
                "prompt": prompt,
                "stream": False,
                "options": {
                    "temperature": temperature,
                    "top_p": 0.9,
                    "top_k": 40
                }
            }
            response = requests.post(self.generation_api, json=payload, timeout=500)
            if response.status_code == 200:
                result = response.json()
                return result.get('response', '').strip()
            else:
                return f"Error: HTTP {response.status_code}"
        except requests.exceptions.RequestException as e:
            return f"Hinh nhu chua bat Ollama: {str(e)}"
    
    def generate_sql(self, question, schema_info):
        prompt = f"""
        Bạn là một chuyên gia SQL. Nhiệm vụ: tạo câu SQL từ câu hỏi tiếng Việt.

        === DATABASE SCHEMA ===
        {schema_info}

        === QUY TẮC ===
        1. CHỈ trả về câu SQL, KHÔNG giải thích
        2. Sử dụng LIMIT cho SELECT thông thường (tối đa 20 rows)
        3. COUNT, SUM, AVG không cần LIMIT
        4. KHÔNG sử dụng: DELETE, UPDATE, DROP, INSERT, ALTER, TRUNCATE
        5. Sử dụng JOIN khi cần thiết
        6. Tên table/column phải CHÍNH XÁC theo schema
        7. Sử dụng LIKE '%keyword%' cho tìm kiếm text
        8. Sắp xếp kết quả hợp lý (ORDER BY)

        === VÍ DỤ ===
        Câu hỏi: "Tài liệu nào được xem nhiều nhất?"
        SQL: SELECT t.TieuDe, s.HoVaTen, t.LuotXem FROM TaiLieu t JOIN SinhVien s ON t.MaSinhVien = s.MaSinhVien ORDER BY t.LuotXem DESC LIMIT 10;

        Câu hỏi: "TRAN CONG HAU đã đăng bao nhiêu tài liệu?"
        SQL: SELECT COUNT(*) as SoLuongTaiLieu FROM TaiLieu t JOIN SinhVien s ON t.MaSinhVien = s.MaSinhVien WHERE s.HoVaTen = 'TRAN CONG HAU';

        Câu hỏi: "Tìm tài liệu về AI"
        SQL: SELECT t.TieuDe, t.MoTa, s.HoVaTen FROM TaiLieu t JOIN SinhVien s ON t.MaSinhVien = s.MaSinhVien WHERE t.TieuDe LIKE '%AI%' OR t.MoTa LIKE '%AI%' OR t.Tags LIKE '%AI%' LIMIT 10;

        === CÂU HỎI ===
        {question}

        === SQL ===
        """
        sql = self._call_model(prompt, temperature=0.1)
        cleaned_sql = self._clean_sql(sql)
        
        return cleaned_sql
    
    def _clean_sql(self, sql: str) -> str:
        """Làm sạch SQL query"""
        if not sql or sql.startswith("Error") or sql.startswith("Lỗi"):
            return ""

        if "```" in sql:
            lines = sql.split('\n')
            sql_lines = []
            in_code_block = False
            
            for line in lines:
                line_stripped = line.strip()

                if line_stripped.startswith('```'):
                    in_code_block = not in_code_block
                    continue

                if in_code_block and line_stripped:
                    sql_lines.append(line_stripped)

            if sql_lines:
                sql = ' '.join(sql_lines)
            else:
                sql = sql.replace('```sql', '').replace('```', '').strip()

        sql = sql.strip()

        if sql.lower().startswith('sql:'):
            sql = sql[4:].strip()

        sql = sql.rstrip(';')

        sql = ' '.join(sql.split())
        
        return sql.strip()
    
    def format_result(self, question, query, data):
        if not data:
            return "❌ Không tìm thấy kết quả nào."

        prompt = f"""
        Tạo câu trả lời ngắn gọn, dễ đọc từ dữ liệu sau:

        Câu hỏi: "{question}"
        Dữ liệu: {json.dumps(data, ensure_ascii=False, default=str)}

        YÊU CẦU:
        - Sử dụng emoji cho dễ nhìn (📚, 📖, 👤, 👁️)
        - Format: **Tên tài liệu** rồi thông tin phụ
        - Mỗi kết quả 1-2 dòng, không quá dài
        - Không dùng bảng markdown
        - Tối đa 10 kết quả

        VÍ DỤ FORMAT:
        📚 **Tìm thấy X kết quả:**

        **1. Tên tài liệu**
           📖 Môn học | 👤 Tác giả | 👁️ X lượt xem

        TRẢ LỜI:
        """
        
        response = self._call_model(prompt, temperature=0.2)
        return response
    
    def detect_intent(self, question: str) -> str:
        """Phân loại intent của câu hỏi"""
        prompt = f"""
        Phân loại câu hỏi sau vào 1 trong các category:

        CATEGORIES:
        - database: câu hỏi về dữ liệu, thống kê, tìm kiếm trong hệ thống
        - document_qa: câu hỏi về nội dung cụ thể của 1 tài liệu  
        - general: chào hỏi, giới thiệu chung

        EXAMPLES:
        "Tài liệu nào phổ biến nhất?" → database
        "Môn học nào có nhiều tài liệu?" → database
        "Tài liệu này nói về gì?" → document_qa
        "Xin chào" → general
        "Hướng dẫn sử dụng Spring Boot?" → document_qa

        QUESTION: "{question}"
        CATEGORY:
        """
                
        intent = self._call_ollama(prompt, temperature=0.1)
        return intent.strip().lower()
            
    def generate_search_keywords(self, question: str) -> list:
        """Tạo keywords cho tìm kiếm từ câu hỏi"""
        prompt = f"""
        Từ câu hỏi sau, trích xuất các từ khóa quan trọng để tìm kiếm tài liệu.

        Câu hỏi: "{question}"

        Quy tắc:
        - Trả về tối đa 5 từ khóa
        - Mỗi từ khóa trên 1 dòng
        - Chỉ trả về từ khóa, không giải thích
        - Ưu tiên: tên công nghệ, môn học, chủ đề chính

        Từ khóa:
        """
        
        keywords_text = self._call_model(prompt, temperature=0.2)
        keywords = [kw.strip() for kw in keywords_text.split('\n') if kw.strip()]
        return keywords[:5]
    
    def validate_sql(self, sql: str) -> dict:
        """Validate SQL query về mặt security và syntax"""
        forbidden_keywords = [
            'DELETE', 'UPDATE', 'DROP', 'INSERT', 'ALTER', 
            'TRUNCATE', 'CREATE', 'GRANT', 'REVOKE'
        ]
        
        sql_upper = sql.upper()
        for keyword in forbidden_keywords:
            if keyword in sql_upper:
                return {
                    "valid": False,
                    "error": f"Từ khóa '{keyword}' không được phép sử dụng"
                }

        if not any(sql_upper.strip().startswith(cmd) for cmd in ['SELECT', 'SHOW', 'DESCRIBE']):
            return {
                "valid": False,
                "error": "Chỉ cho phép câu lệnh SELECT, SHOW, DESCRIBE"
            }

        if (sql_upper.strip().startswith('SELECT') and 
            'LIMIT' not in sql_upper and
            not any(func in sql_upper for func in ['COUNT(', 'SUM(', 'AVG(', 'MAX(', 'MIN('])):
            return {
                "valid": False,
                "error": "Câu SELECT phải có LIMIT để giới hạn kết quả (trừ khi sử dụng COUNT, SUM, AVG)"
            }
            
        return {"valid": True}
    
    def validate_sql_query(self, query: str) -> tuple[bool, str]:
        """Validate SQL query for safety"""
        query_lower = query.lower().strip()

        if not query_lower.startswith(('select', 'show', 'describe', 'explain')):
            return False, "Chỉ cho phép các câu lệnh SELECT, SHOW, DESCRIBE, EXPLAIN"

        dangerous_keywords = ['drop', 'delete', 'insert', 'update', 'alter', 'create', 'truncate']
        for keyword in dangerous_keywords:
            if keyword in query_lower:
                return False, f"Không cho phép sử dụng từ khóa: {keyword.upper()}"

        if query_lower.startswith('select'):

            has_aggregate = any(func in query_lower for func in ['count(', 'sum(', 'avg(', 'max(', 'min(', 'group by'])
            has_show_tables = 'show tables' in query_lower or 'describe' in query_lower
            has_limit = 'limit' in query_lower
            
            if not has_aggregate and not has_show_tables and not has_limit:
                query = query.rstrip(';') + ' LIMIT 50'
                return True, query
    
        return True, query
    
    def _classify_intent(self, question):
        prompt = f"""
        Phân loại câu hỏi sau vào 1 trong các category. CHỈ trả về tên category, KHÔNG giải thích.

        CATEGORIES:
        - greeting: chào hỏi, xin chào
        - help: hỏi về hướng dẫn, help, làm gì
        - capabilities: hỏi về khả năng, có thể làm gì, chức năng
        - database: tìm kiếm tài liệu, thống kê, top, phổ biến, số lượng
        - general: câu hỏi khác

        EXAMPLES:
        "Xin chào" → greeting
        "Hello" → greeting
        "Help" → help  
        "Hướng dẫn sử dụng" → help
        "Bạn có thể làm gì?" → capabilities
        "Chức năng của bạn" → capabilities
        "Tài liệu phổ biến nhất" → database
        "Top 10 tài liệu" → database
        "Có bao nhiêu tài liệu" → database
        "Tìm tài liệu Java" → database

        QUESTION: "{question}"
        CATEGORY:
        """
        try:
            response = self._call_model(prompt)
            intent = response.strip().lower()
            valid_intents = ["greeting", "help", "capabilities", "database", "general"]
            if intent not in valid_intents:
                return "general"
            return intent
        except:
            return "general"
    
    def classify_intent(self, user_input: str) -> str:
        """Classify user intent"""
        input_lower = user_input.lower().strip()
        
        # Database queries
        database_keywords = [
            'môn học', 'subject', 'tài liệu', 'document', 'file',
            'danh sách', 'list', 'có những', 'có gì', 'bao nhiêu',
            'tìm', 'search', 'xem', 'show', 'hiển thị'
        ]
        
        if any(keyword in input_lower for keyword in database_keywords):
            return 'database'
        
        # Greeting
        if any(word in input_lower for word in ['xin chào', 'hello', 'hi', 'chào']):
            return 'greeting'
        
        # Help
        if any(word in input_lower for word in ['help', 'hỗ trợ', 'giúp', 'hướng dẫn']):
            return 'help'
        
        return 'general'
    
    def smart_database_query(self, user_question: str) -> str:
        """Smart database query based on user question"""
        question_lower = user_question.lower().strip()
        
        try:
            # Common patterns
            if any(phrase in question_lower for phrase in ['môn học', 'subject']):
                if any(phrase in question_lower for phrase in ['có những', 'danh sách', 'list']):
                    # Liệt kê môn học
                    query = "SELECT DISTINCT subject_name FROM subjects LIMIT 20"
                    return self._execute_safe_query(query)
                
            if any(phrase in question_lower for phrase in ['tài liệu', 'document']):
                if any(phrase in question_lower for phrase in ['có những', 'danh sách', 'list']):
                    # Liệt kê tài liệu
                    query = "SELECT document_name, subject_name FROM documents LIMIT 20"
                    return self._execute_safe_query(query)
            
            if any(phrase in question_lower for phrase in ['bao nhiêu', 'count', 'số lượng']):
                if 'môn học' in question_lower:
                    query = "SELECT COUNT(*) as total_subjects FROM subjects"
                    return self._execute_safe_query(query)
                elif 'tài liệu' in question_lower:
                    query = "SELECT COUNT(*) as total_documents FROM documents"
                    return self._execute_safe_query(query)
            
            # Fallback - general search
            return self._general_search(user_question)
            
        except Exception as e:
            return f"Lỗi truy vấn database: {str(e)}"

    def _execute_safe_query(self, query: str) -> str:
        """Execute query safely"""
        try:
            is_valid, validated_query = self.validate_sql_query(query)
            if not is_valid:
                return f"Lỗi validation: {validated_query}"
            
            cursor = self.connection.cursor()
            cursor.execute(validated_query)
            results = cursor.fetchall()
            cursor.close()
            
            if not results:
                return "Không tìm thấy kết quả nào."
            
            # Format results nicely
            return self._format_results(results, validated_query)
            
        except Exception as e:
            return f"Lỗi thực thi: {str(e)}"

    def _format_results(self, results, query):
        """Format query results for display"""
        if 'COUNT(' in query.upper():
            return f"Tổng số: {results[0][0]}"
        
        if len(results) == 1 and len(results[0]) == 1:
            return str(results[0][0])
        
        # Format as list
        formatted = []
        for i, row in enumerate(results[:10], 1):  # Limit display to 10 rows
            if len(row) == 1:
                formatted.append(f"{i}. {row[0]}")
            else:
                formatted.append(f"{i}. {' - '.join(str(col) for col in row)}")
        
        result_text = "\n".join(formatted)
        if len(results) > 10:
            result_text += f"\n... và {len(results) - 10} kết quả khác"
        
        return result_text

