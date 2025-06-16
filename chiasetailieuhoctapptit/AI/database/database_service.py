import os, mysql.connector,sys
from dotenv import load_dotenv
sys.path.append(os.path.join(os.path.dirname(__file__)))
from database_ai import DatabaseAI

load_dotenv()

class Database:
    def __init__(self):
        self.config = {
            'host' : os.getenv('DB_HOST','localhost'),
            'port' : int(os.getenv('DB_PORT',3306)),
            'database' : os.getenv('DB_NAME', 'ChiaSeTaiLieu'),
            'user' : os.getenv('DB_USER'),
            'password' : os.getenv('DB_PASSWORD'),
            'autocommit' : True
        }

        schema_file = os.path.join(os.path.dirname(__file__),'database_info.txt')
        try:
            with open(schema_file, 'r') as f:
                self.schema_info = f.read()
        except FileNotFoundError:
            self.schema_info = "Khong co thong tin database"

        self.ai = DatabaseAI()
    
    def get_connection(self):
        try:
            connection = mysql.connector.connect(**self.config)
            return connection
        except mysql.connector.Error as e:
            return f'Khong vao duoc database: {e}'
        
    def execute_query(self, query):
        try:
            connection = self.get_connection()
            if not connection:
                return {'error' : 'Khong ket noi duoc database'}
            with connection.cursor(dictionary=True) as cursor:
                cursor.execute(query)
                results = cursor.fetchall()
            connection.close()
            return {
                'success' : True,
                'data' : results,
                'count' : len(results),
                'query' : query
            }
        except mysql.connector.Error as e:
            return {"error": f"loi sql: {str(e)}", "sql": query}
        except Exception as e:
            return {"error": f"loi gi do: {str(e)}", "sql": query}

    def get_popular_documents(self, limit=10):
        sql = f"""
        SELECT t.TieuDe, s.HoVaTen, t.LuotXem 
        FROM TaiLieu t 
        JOIN SinhVien s ON t.MaSinhVien = s.MaSinhVien 
        ORDER BY t.LuotXem DESC 
        LIMIT {limit}
        """
        return self.execute_query(sql)
    
    def classify_intent(self, question):
        return self.ai._classify_intent(question)
 
    def smart_database_query(self, question):
        if not self.ai:
            return "Khong co AI"
        try:
            sql = self.ai.generate_sql(question, self.schema_info)

            validation = self.ai.validate_sql(sql)

            if not validation["valid"]:
                return f"{validation['error']}"

            result = self.execute_query(sql)
            if "error" in result:
                return f"{result['error']}"

            if result.get('count', 0) == 0:
                return "Không tìm thấy kết quả nào."
            
            formatted_response = self.ai.format_result(question, sql, result['data'])
            return formatted_response
            
        except Exception as e:
            return f"Lỗi xử lý: {str(e)}"
        
    def _improved_keyword_routing(self,question):
        greeting_patterns = [
            "xin chào", "hello", "hi", "chào bạn", "hey", "chào", 
            "good morning", "good afternoon"
        ]
        if any(pattern in question for pattern in greeting_patterns):
            return self._get_greeting_response()
        
        capability_patterns = [
            "bạn có thể làm gì", "có thể làm những gì", "chức năng", 
            "what can you do", "khả năng", "tính năng"
        ]
        if any(pattern in question for pattern in capability_patterns):
            return self._get_capabilities_response()
        
        help_patterns = [
            "help", "giúp đỡ", "hướng dẫn", "cách sử dụng", "làm thế nào"
        ]
        if any(pattern in question for pattern in help_patterns):
            return self._get_help_response()
        
        database_patterns = [
            "tài liệu phổ biến", "top tài liệu", "thống kê tài liệu",
            "tìm tài liệu", "search tài liệu", "có bao nhiêu tài liệu",
            "tài liệu về java", "tài liệu về python", "môn học",
            "lượt xem nhiều nhất", "download nhiều nhất"
        ]
        if any(pattern in question for pattern in database_patterns):
            return self.smart_database_query(question)
        
        return self._get_general_fallback()

    def _get_greeting_response(self) -> str:
        return """👋 **Chào mừng đến với Thư viện Tài liệu PTIT!**

    **Tôi là AI Assistant, có thể giúp bạn:**

    **Thống kê & Tìm kiếm**
    • "Tài liệu nào phổ biến nhất?"
    • "Top 10 tài liệu về Java"
    • "Có bao nhiêu tài liệu về AI?"

    **Tìm kiếm thông minh**
    • "Tìm tài liệu về Spring Boot"
    • "Tài liệu React mới nhất"
    • "Môn Lập trình Web có gì hay?"

    **Thông tin tác giả**
    • "Sinh viên nào đăng nhiều tài liệu nhất?"

    **Xu hướng**
    • "Tài liệu được xem nhiều tuần này"
    • "Chủ đề hot nhất hiện tại"

    **Gõ "help" để xem hướng dẫn chi tiết!**

    **Hãy thử hỏi tôi ngay!**"""

    def _get_capabilities_response(self) -> str:
        """Response cho capabilities intent"""
        return """**Khả năng của tôi - AI Assistant cho Thư viện PTIT:**

    **1. TÌM KIẾM & THỐNG KÊ**
      Tìm tài liệu theo chủ đề, môn học
      Thống kê tài liệu phổ biến, top downloads
      Phân tích xu hướng và ranking
      Tìm kiếm theo tác giả, sinh viên

    **2. PHÂN TÍCH DỮ LIỆU**
      Text-to-SQL: Chuyển câu hỏi → truy vấn database
      Thống kê lượt xem, download, rating
      Báo cáo tài liệu theo thời gian
      So sánh độ phổ biến giữa các chủ đề

    **3. HỘI THOẠI THÔNG MINH**
      Hiểu ngôn ngữ tự nhiên (tiếng Việt + English)
      Gợi ý tài liệu phù hợp
      Trả lời với format đẹp, emoji
      Context awareness

    **4. CÔNG NGHỆ SỬ DỤNG**
      AI Model: Ollama Gemma 3
      Database: MySQL với AI-generated SQL
      API: FastAPI + JSON-RPC
      Language Processing: Vietnamese + English

    **Hỏi tôi bất cứ điều gì về tài liệu!**"""

    def _get_help_response(self) -> str:
        return """**Hướng dẫn sử dụng AI Assistant:**

    **Các loại câu hỏi tôi có thể trả lời:**

    **1. Tìm kiếm theo chủ đề**
    ```
    • "Tìm tài liệu về Python"
    • "Có tài liệu nào về Machine Learning?"
    • "List tài liệu môn Cơ sở dữ liệu"
    ```

    **2. Thống kê và Top lists**
    ```
    • "Top 10 tài liệu được xem nhiều nhất"
    • "Tài liệu nào hot nhất tuần này?"
    • "Thống kê tài liệu theo môn học"
    ```

    **3. Thông tin tác giả**
    ```
    • "Sinh viên nào đăng nhiều tài liệu nhất?"
    • "Thống kê theo người đăng"
    ```

    **4. Lọc và sắp xếp**
    ```
    • "Tài liệu mới nhất về React"
    • "Sắp xếp theo lượt download"
    • "Tài liệu có rating cao nhất"
    ```

    **5. Xu hướng và phân tích**
    ```
    • "Chủ đề nào đang hot?"
    • "Tài liệu được quan tâm gần đây"
    • "So sánh độ phổ biến Java vs Python"
    ```

    **Tips:** Hỏi cụ thể để có câu trả lời chính xác!

    **Bắt đầu hỏi ngay!**"""

    def _get_general_fallback(self) -> str:
        """Default fallback response"""
        return """**Tôi chưa hiểu rõ câu hỏi của bạn.**

    **Gợi ý câu hỏi phổ biến:**
    • "Tài liệu về Java phổ biến nhất"
    • "Có bao nhiêu tài liệu trong hệ thống?"
    • "Top 10 tác giả có nhiều tài liệu nhất"
    • "Bạn có thể làm gì?" - xem khả năng
    • "Help" - xem hướng dẫn đầy đủ

    **Hoặc duyệt theo danh mục:**
    • **Lập trình Web** → "Tài liệu React, Vue, Angular"
    • **Mobile** → "Tài liệu Flutter, React Native"  
    • **AI/ML** → "Machine Learning, Deep Learning"
    • **Backend** → "Spring Boot, Node.js, Python"

    **Thử hỏi theo cách khác!** """

    def _keyword_based_routing(self,question_lower: str) -> str:
        """Fallback keyword routing"""
        db_keywords = [
            "tài liệu về", "top tài liệu", "phổ biến", "thống kê", 
            "bao nhiêu tài liệu", "tìm tài liệu", "môn học"
        ]
        
        if any(keyword in question_lower for keyword in db_keywords):
            return self.smart_database_query(question_lower)
        
        return self._get_general_fallback()
    
    def get_all_documents(self):
        """Lấy tất cả tài liệu từ database"""
        query = """
        SELECT 
            tl.MaTaiLieu as id,
            tl.TieuDe as title, 
            tl.MoTa as description,
            tl.NgayDang as date_created,
            sv.HoVaTen as author,
            mh.TenMon as subject,
            lt.TenLoai as document_type
        FROM TaiLieu tl
        LEFT JOIN SinhVien sv ON tl.MaSinhVien = sv.MaSinhVien  
        LEFT JOIN vw_MonHoc mh ON tl.MonHoc = mh.MaMon
        LEFT JOIN LoaiTaiLieu lt ON tl.MaLoai = lt.MaLoai
        ORDER BY tl.NgayDang DESC
        LIMIT 100
        """
        result = self.execute_query(query)
        if result.get('success'):
            return result['data']
        return []
    
    def get_document_content(self, doc_id):
        """Lấy nội dung tài liệu theo ID"""
        query = f"""
        SELECT 
            tl.TieuDe as title,
            tl.MoTa as description,
            f.DuongDan as file_path,
            f.TenFile as filename
        FROM TaiLieu tl
        LEFT JOIN File f ON tl.MaFile = f.MaFile
        WHERE tl.MaTaiLieu = {doc_id}
        """
        result = self.execute_query(query)
        if result.get('success') and result['data']:
            doc = result['data'][0]
            # Nếu có file PDF, extract text
            if doc['file_path'] and doc['file_path'].endswith('.pdf'):
                try:
                    sys.path.append(os.path.join(os.path.dirname(__file__), '..', 'mcp'))
                    text_content = self.ai.pdf_processor.extract_text_from_pdf(doc['file_path'])
                    return f"Tiêu đề: {doc['title']}\n\nMô tả: {doc['description']}\n\nNội dung:\n{text_content}"
                except:
                    pass
            return f"Tiêu đề: {doc['title']}\n\nMô tả: {doc['description']}"
        return "Không tìm thấy tài liệu"
    
    def get_documents_by_subject(self, subject):
        """Lấy tài liệu theo môn học"""
        query = f"""
        SELECT 
            tl.MaTaiLieu as id,
            tl.TieuDe as title,
            tl.MoTa as description,
            tl.NgayDang as date_created
        FROM TaiLieu tl
        LEFT JOIN vw_MonHoc mh ON tl.MonHoc = mh.MaMon
        WHERE mh.TenMon LIKE '%{subject}%'
        ORDER BY tl.NgayDang DESC
        """
        result = self.execute_query(query)
        if result.get('success'):
            return result['data']
        return []