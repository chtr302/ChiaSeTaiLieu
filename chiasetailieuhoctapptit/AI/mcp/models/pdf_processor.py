import os
import logging
import re
import pypdf
from io import BytesIO

logger = logging.getLogger("PDFProcessor")

class PDFProcessor:
    def __init__(self):
        pass
        
    def extract_text_from_pdf(self, pdf_path=None, pdf_bytes=None):
        try:
            if pdf_path and os.path.exists(pdf_path):
                with open(pdf_path, 'rb') as file:
                    reader = pypdf.PdfReader(file)
                    page_count = len(reader.pages)
                    text = ""
                    for page_num in range(page_count):
                        page = reader.pages[page_num]
                        page_text = page.extract_text() or ""
                        text += page_text + "\n\n"
                        
            elif pdf_bytes:
                reader = pypdf.PdfReader(BytesIO(pdf_bytes))
                page_count = len(reader.pages)
                
                text = ""
                for page_num in range(page_count):
                    page = reader.pages[page_num]
                    page_text = page.extract_text() or ""
                    text += page_text + "\n\n"
                    
            else:
                raise ValueError("Cần cung cấp đường dẫn file PDF hoặc dữ liệu bytes của PDF")

            processed_text = self._clean_pdf_text(text)
            
            return processed_text
            
        except Exception as e:
            return f"Lỗi khi đọc file PDF: {str(e)}"
            
    def _clean_pdf_text(self, text):
        """Làm sạch văn bản trích xuất từ PDF"""
        if not text:
            return ""

        text = re.sub(r'\s+', ' ', text)

        text = re.sub(r'(\.\s+)([A-Z])', r'\1\n\2', text)
        text = re.sub(r'(\?\s+)([A-Z])', r'\1\n\2', text)
        text = re.sub(r'(!\s+)([A-Z])', r'\1\n\2', text)

        lines = text.split('\n')
        cleaned_lines = []
        header_pattern = re.compile(r'^[\d\s]*$')
        
        for line in lines:
            if not header_pattern.match(line) and len(line.strip()) > 0:
                cleaned_lines.append(line)
                
        text = '\n'.join(cleaned_lines)

        text = re.sub(r'\n\s*\n', '\n\n', text)
        
        return text.strip()
