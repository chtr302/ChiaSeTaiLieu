from mcp.server.fastmcp import FastMCP
from fastapi import FastAPI, Request, HTTPException
from transformers import pipeline
import json,os, uvicorn,sys
from models import AI
sys.path.append(os.path.join(os.path.dirname(__file__), '..'))
from database.database_service import Database

mcp = FastMCP('AI Doucment')
server = FastAPI()

ai = AI()
db = Database()

@mcp.tool(description="Tạo tóm tắt chất lượng cao từ văn bản tiếng Việt")
def document_description(text: str, max_length = 300, min_length= 50) -> str:
    try:
        if not text or len(text.strip()) == 0:
            raise ValueError("Không có nội dung để tóm tắt")

        word_count = len(text.split())
        
        if word_count < 1000:
            result = ai.summarize_with_consistency(text, max_length=max_length, min_length=min_length)
        else:
            result = ai.summarize_long_document(text, max_length=max_length)

        quality_score, reason = ai.text_utils.evaluate_summary_quality(result, text)

        if quality_score < 5:
            if word_count < 1000:
                alternative_summary = ai.summarize_long_document(text, max_length=max_length)
            else:
                alternative_summary = ai.summarize_with_consistency(text, max_length=max_length, min_length=min_length)
            alt_quality, alt_reason = ai.text_utils.evaluate_summary_quality(alternative_summary, text)
            if alt_quality > quality_score:
                result = alternative_summary
        result = ai.text_utils.clean_summary_output(result)

        return result
        
    except Exception as e:
        return f"Lỗi: {str(e)}"
    
@mcp.tool(description="Tóm tắt từ file PDF")
def summarize_pdf_file(file_path: str, max_length: int = 300, min_length: int = 50) -> str:
    try:
        if not os.path.exists(file_path):
            return f"Khong tim thay duong dan file {file_path}"

        text = ai.pdf_processor.extract_text_from_pdf(pdf_path=file_path)
        
        if isinstance(text, str) and text.startswith("Lỗi"):
            return text

        return document_description(text, max_length, min_length)
        
    except Exception as e:
        return f"Lỗi: {str(e)}"

@mcp.tool(description="Q&A")
def answer_question(document_text: str, question: str,context:str= "", max_length: int = 300) -> str:
    try:
        if not question or len(question.strip()) == 0:
            return 'Vui lòng cung cấp câu hỏi'
        
        answer = ai.answer_question(document_text, question,context)
        return answer
    except Exception as e:
        return f"Lỗi: {str(e)}"

@mcp.tool(description="Extract PDF")
def extract_pdf(file_path: str) -> str:
    try:
        text = ai.extract_text_from_pdf(file_path)
        return text
    except Exception as e:
        return f"Lỗi: {str(e)}"

@mcp.tool(description="Chatbot tìm kiếm và thống kê tài liệu")
def chatbot(question: str) -> str:
    try:
        question_lower = question.lower().strip()

        if db.ai:
            try:
                intent = db.classify_intent(question)
                if intent == 'database':
                    return db.smart_database_query(question)
                elif intent == 'greeting':
                    return db._get_greeting_response()
                elif intent == 'help':
                    return db._get_help_response()
                elif intent == "capabilities":
                    return db._get_capabilities_response()
                else:
                    return db._keyword_based_routing()
            except Exception as e:
                print(f"AI intent classification failed: {e}")
                return db._improved_keyword_routing(question_lower)
        else:
            return db._improved_keyword_routing(question_lower)
    except Exception as e:
        return f"Lỗi chatbot: {str(e)}"

tools = {
    'document_description' : document_description,
    'summarize_pdf_file' : summarize_pdf_file,
    'answer_question' : answer_question,
    'extract_pdf' : extract_pdf,
    'chatbot' : chatbot,
}

@server.post("/jsonrpc")
async def jsonrpc_endpoint(request: Request):
    try:
        json_data = await request.json()
        if json_data.get("jsonrpc") != "2.0" or "method" not in json_data or "id" not in json_data:
            raise HTTPException(status_code=400, detail="Invalid JSON-RPC request")
        method = json_data["method"]
        params = json_data.get("params", {})
        if method not in tools:
            return {"jsonrpc": "2.0", "error": {"code": -32601, "message": "Method not found"}, "id": json_data["id"]}
        try:
            result = tools[method](**params)
            return {"jsonrpc": "2.0", "result": result, "id": json_data["id"]}
        except Exception as e:
            return {"jsonrpc": "2.0", "error": {"code": -32000, "message": str(e)}, "id": json_data["id"]}
    except json.JSONDecodeError:
        raise HTTPException(status_code=400, detail="Invalid JSON data")
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Server error: {str(e)}")

server.mount("/mcp", mcp.streamable_http_app())

if __name__ == "__main__":
    uvicorn.run("mcp_server:server", host="0.0.0.0", port=8001, reload=True)