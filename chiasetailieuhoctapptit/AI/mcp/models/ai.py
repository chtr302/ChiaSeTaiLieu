import logging
from .summarizer import Summarizer
from .pdf_processor import PDFProcessor
from .text_utils import TextUtils
from .document_qa import DocumentQA


logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("AI")

class AI:
    def __init__(self):
        self.summarizer = Summarizer()
        self.pdf_processor = PDFProcessor()
        self.text_utils = TextUtils()
        self._document_qa = None

    @property
    def doucument_qa(self):
        if self._document_qa is None:
            self._document_qa = DocumentQA()
        return self._document_qa

    def summarize_with_consistency(self, text, max_length=300, attempts=1, min_length=50):
        return self.summarizer.summarize_with_consistency(text, max_length, attempts, min_length)

    def summarize_long_document(self, text, max_length=300):
        """Tóm tắt văn bản dài"""
        return self.summarizer.summarize_long_document(text, max_length, self.text_utils)
    
    def extract_text_from_pdf(self, pdf_path=None, pdf_bytes=None):
        """Trích xuất văn bản từ file PDF"""
        return self.pdf_processor.extract_text_from_pdf(pdf_path, pdf_bytes)
    
    def summarize_pdf_document(self, pdf_path=None, pdf_bytes=None, max_length=300, min_length=50):
        """Trích xuất và tóm tắt nội dung file PDF"""
        text = self.extract_text_from_pdf(pdf_path, pdf_bytes)
        if isinstance(text, str) and text.startswith("Lỗi"):
            return text

        return self.summarize_with_consistency(text, max_length, min_length)
    
    def answer_question(self, document_text, question,context, max_length=300):
        return self.doucument_qa.answer_question(document_text, question, context)
    
   
