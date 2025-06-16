from fastapi import FastAPI, UploadFile, File, HTTPException, Query, Form, BackgroundTasks
from fastapi.middleware.cors import CORSMiddleware
import os, uuid, requests, uvicorn
from datetime import datetime
from aiohttp import ClientSession
from ChatSession import ChatSession

summary_results = {}
document_session = {}
chatbot_sessions = {}

server = FastAPI(title="Document")

server.add_middleware(
    CORSMiddleware,
    allow_origins = ['http://localhost:8080'],
    allow_credentials = True,
    allow_methods = ['GET','POST','PUT','DELETE'],
    allow_headers = ['*']
)

UPLOAD_DIR = os.path.join(os.path.dirname(__file__), 'uploads')
os.makedirs(UPLOAD_DIR, exist_ok=True)
APP_UPLOAD_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), '..','..', 'chiasetailieuhoctapptit', 'uploads'))

MCP_SERVER_URL = os.environ.get("MCP_SERVER_URL","http://localhost:8001/jsonrpc")
MCP_TOOL = "summarize_pdf_file"

async def save_upload_file(file: UploadFile, directory: str) -> str:
    """lưu file vào uploads"""
    filename = f"{uuid.uuid4()}_{file.filename}"
    file_path = os.path.join(directory, filename)
    
    with open(file_path, "wb") as buffer:
        content = await file.read()
        buffer.write(content)
    
    return file_path

async def call_mcp_summarize_pdf(file_path: str, max_length: int = 300, min_length: int = 50) -> str:
    """Gọi MCP API để tóm tắt file PDF"""
    payload = {
        "jsonrpc": "2.0",
        "method": MCP_TOOL,
        "params": {
            "file_path": file_path,
            "max_length": max_length,
            "min_length": min_length
        },
        "id": str(uuid.uuid4())
    }
    
    try:
        response = requests.post(
            MCP_SERVER_URL,
            json=payload,
            headers={"Content-Type": "application/json"}
        )
        if response.status_code != 200:
            return f"Lỗi khi gọi MCP API: {response.status_code}"
        result = response.json()
        if "error" in result:
            return f"MCP trả về lỗi: {result['error']['message']}"
        return result["result"]
        
    except Exception as e:
        return f"Lỗi kết nối đến MCP: {str(e)}"
    
async def process_summary_task(file_path: str, task_id: str, max_length: int = 300, min_length: int = 50):
    """Xử lý tóm tắt trong background task"""
    try:
        summary = await call_mcp_summarize_pdf(file_path, max_length, min_length)
        
        # Lưu kết quả
        summary_results[task_id] = {
            "status": "completed",
            "summary": summary,
            "summary_length": len(summary) if isinstance(summary, str) else 0,
            "completed_at": datetime.now().isoformat()
        }
        
    except Exception as e:
        summary_results[task_id] = {
            "status": "error",
            "error": str(e),
            "completed_at": datetime.now().isoformat()
        }
    
@server.get("/health")
def heath():
    return {
        'status' : "Ket noi thanh cong roi nhe",
        "thoi gian de biet ne" : datetime.now().isoformat()
    }

@server.post('/summarize-upload')
async def summarize_pdf(
    file: UploadFile = File(...), 
    background_tasks: BackgroundTasks = None,
    max_length: int = Query(300, description="Độ dài tối đa của bản tóm tắt"),
    min_length: int = Query(50, description="Độ dài tối thiểu của bản tóm tắt")
):
    """Upload và tóm tắt file PDF"""
    # Kiểm tra định dạng file
    if not file.filename.lower().endswith('.pdf'):
        raise HTTPException(status_code=400, detail='Chỉ hỗ trợ file PDF')
        
    try:
        file_path = await save_upload_file(file, UPLOAD_DIR)

        task_id = str(uuid.uuid4())

        summary_results[task_id] = {
            "status": "processing",
            "filename": file.filename,
            "file_path": file_path,
            "created_at": datetime.now().isoformat()
        }

        background_tasks.add_task(
            process_summary_task,
            file_path,
            task_id,
            max_length,
            min_length
        )

        return {
            "task_id": task_id,
            "status": "processing",
            "filename": file.filename,
            "message": "Đang xử lý tóm tắt, vui lòng kiểm tra kết quả sau"
        }
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
    
@server.get('/summary-result/{task_id}')
async def get_summary_result(task_id: str):
    if task_id not in summary_results:
        raise HTTPException(status_code=404, detail="Task ID không tồn tại")
    return summary_results[task_id]

@server.post('/summarize-pdf-sync')
async def summarize_pdf_sync(
    file: UploadFile = File(...),
    max_length: int = Query(300, description="Độ dài tối đa của bản tóm tắt"),
    min_length: int = Query(50, description="Độ dài tối thiểu của bản tóm tắt")
):
    """Upload và tóm tắt file PDF (đồng bộ - chờ kết quả)"""
    if not file.filename.lower().endswith('.pdf'):
        raise HTTPException(status_code=400, detail='Chỉ hỗ trợ file PDF')
        
    try:
        # Lưu file
        file_path = await save_upload_file(file, UPLOAD_DIR)
        
        # Gọi MCP API để tóm tắt
        summary = await call_mcp_summarize_pdf(file_path, max_length, min_length)
        
        # Trả về kết quả
        return {
            "status": "completed",
            "filename": file.filename,
            "summary": summary,
            "summary_length": len(summary) if isinstance(summary, str) else 0
        }
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
    
@server.post('/ask-document')
async def answer_question(
    filename: str = Form(...),
    question: str = Form(...)):
    try:
        file_path = os.path.join(APP_UPLOAD_DIR, filename)
        answer = await call_mcp_qa_pdf(file_path, question)
        return {
            "status": "completed",
            "filename": filename,
            "question": question,
            "answer": answer
        }
    except Exception as e:
        return f"loi: {str(e)}"

async def call_mcp_qa_pdf(file_path: str, question: str, max_length: int = 300):
    """Gọi MCP API để trả lời câu hỏi từ PDF"""
    request_data = {
        "jsonrpc": "2.0",
        "method": "answer_question",
        "params": {
            "file_path": file_path,
            "question": question,
        },
        "id": 1
    }
    
    async with ClientSession() as session:
        async with session.post(f"{MCP_SERVER_URL}", json=request_data) as response:
            if response.status != 200:
                error_text = await response.text()
                raise HTTPException(status_code=response.status, detail=f"MCP API Error: {error_text}")
            
            result = await response.json()
            if "error" in result:
                raise HTTPException(status_code=500, detail=f"MCP API Error: {result['error']['message']}")
                
            return result["result"]

@server.post("/create_session")
async def create_session(file_name: str = Form(...)):
    try:
        file_path = os.path.join(APP_UPLOAD_DIR, file_name)
        text = await call_mcp_extract_pdf(file_path)
        session_id = str(uuid.uuid4())

        document_session[session_id] = {
            "file_path" : file_path,
            'file_name' : file_name,
            "document_text" : text,
            "created_at" : datetime.now().isoformat(),
            "history" : []
        }
        return {
            "session_id" : session_id,
            "status" : "ok",
            
        }
    except Exception as e:
       raise HTTPException(
        status_code=500,
        detail={
            "message": f"Lỗi khi tạo phiên làm việc",
            "error": str(e)
        }
    )
    
async def call_mcp_extract_pdf(file_path: str) -> str:
    request_data = {
        "jsonrpc" : "2.0",
        "method" : "extract_pdf",
        "params" : {
            "file_path" : file_path
        },
        "id" : 1
    }
    async with ClientSession() as session:
        async with session.post(f"{MCP_SERVER_URL}", json=request_data) as response:
            if response.status != 200:
                error_text = await response.text()
                raise HTTPException(status_code=response.status, detail=f"MCP API Error: {error_text}")
            
            result = await response.json()
            if "error" in result:
                raise HTTPException(status_code=500, detail=f"MCP API Error: {result['error']['message']}")
                
            return result["result"]

@server.post("/session/{session_id}/ask")
async def ask_in_session(
    session_id: str,
    question: str = Form(...),
    include_history: bool = Form(False)
):
    
    if session_id not in document_session:
        raise HTTPException(status_code=404, detail="Phiên không tồn tại hoặc đã hết hạn")
    try:
        session = document_session[session_id]
        document_text = session["document_text"]

        context = ""
        if include_history and session["history"]:
            history = session["history"][-3:]
            for i, item in enumerate(history):
                context += f"Câu hỏi {i+1}: {item['question']}\n"
                context += f"Trả lời {i+1}: {item['answer']}\n\n"
        request_data = {
            "jsonrpc": "2.0",
            "method": "answer_question",
            "params": {
                "document_text": document_text,
                "question": question,
                "context": context if include_history else ""
            },
            "id": str(uuid.uuid4())
        }
        async with ClientSession() as session_client:
            async with session_client.post(MCP_SERVER_URL, json=request_data) as response:
                if response.status != 200:
                    error_text = await response.text()
                    raise HTTPException(status_code=response.status, detail=f"MCP API Error: {error_text}")
                
                result = await response.json()
                if "error" in result:
                    raise HTTPException(status_code=500, detail=f"MCP API Error: {result['error']['message']}")
                
                answer = result["result"]

        session["history"].append({
            "question": question,
            "answer": answer,
        })
        
        return {
            "session_id": session_id,
            "question": question,
            "answer": answer
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Lỗi khi xử lý câu hỏi: {str(e)}")

@server.get("/session/{session_id}/history")
async def get_session_history(session_id: str, limit: int = Query(5, ge=1, le=20)):
    if session_id not in document_session:
        raise HTTPException(status_code=404, detail="Phiên không tồn tại")
    
    session = document_session[session_id]
    history = session["history"]
    limited_history = history[-limit:] if len(history) > limit else history
    
    return {
        "session_id": session_id,
        "file_name": session["file_name"],
        "history": limited_history
    }

async def call_mcp_chatbot(question):
    request_data = {
        "jsonrpc" : "2.0",
        "method" : "chatbot",
        "params" : {
            "question" : question
        },
        "id" : 212
    }
    async with ClientSession() as session:
        async with session.post(f"{MCP_SERVER_URL}", json=request_data) as response:
            if response.status != 200:
                error_text = await response.text()
                raise HTTPException(status_code=response.status, detail=f"MCP API Error: {error_text}")
            result = await response.json()
            if "error" in result:
                raise HTTPException(status_code=500, detail=f"MCP API Error: {result['error']['message']}")
                
            return result["result"]
        
def cleanup_expired_chatbot_sessions():
    """Cleanup expired chatbot sessions"""
    expired = [sid for sid, session in chatbot_sessions.items() if session.is_expired()]
    for sid in expired:
        del chatbot_sessions[sid]

def get_or_create_chatbot_session(session_id: str, user_id: str = None) -> ChatSession:
    """Get existing chatbot session or create new one"""
    if session_id not in chatbot_sessions:
        chatbot_sessions[session_id] = ChatSession(session_id, user_id)
    else:
        chatbot_sessions[session_id].last_activity = datetime.now()
    
    return chatbot_sessions[session_id]

@server.post("/chatbot/session")
async def create_chatbot_session(user_id: str = Form(None)):
    """Tạo session mới cho chatbot"""
    try:
        session_id = f"{uuid.uuid4()}"
        session = ChatSession(session_id, user_id)
        chatbot_sessions[session_id] = session
        
        return {
            "session_id": session_id,
            "status": "created",
            "created_at": session.created_at.isoformat()
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Lỗi tạo session: {str(e)}")
    
@server.post("/chatbot/session")
async def create_chatbot_session(user_id: str = Form(None)):
    """Tạo session mới cho chatbot"""
    try:
        session_id = f"{uuid.uuid4()}"
        session = ChatSession(session_id, user_id)
        chatbot_sessions[session_id] = session
        
        return {
            "session_id": session_id,
            "status": "created",
            "created_at": session.created_at.isoformat()
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Lỗi tạo session: {str(e)}")
    
@server.post("/chatbot/chat")
async def chatbot_conversation(
    session_id: str = Form(...),
    question: str = Form(...),
    chat_type: str = Form("general"),
    user_id: str = Form(None)
):
    """Chat với chatbot có context"""
    try:
        if len(chatbot_sessions) > 100:
            cleanup_expired_chatbot_sessions()

        session = get_or_create_chatbot_session(session_id, user_id)

        context = session.get_recent_context()

        enhanced_question = question
        if context and len(session.conversation_history) > 0:
            enhanced_question = f"Ngữ cảnh cuộc trò chuyện trước:\n{context}\n\nCâu hỏi hiện tại: {question}"

        answer = await call_mcp_chatbot(enhanced_question)
        
        # Store conversation
        session.add_conversation(question, answer)
        
        return {
            "session_id": session_id,
            "question": question,
            "answer": answer,
            "chat_type": chat_type,
            "conversation_count": len(session.conversation_history)
        }
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Lỗi chatbot: {str(e)}")

@server.get("/chatbot/session/{session_id}/history")
async def get_chatbot_history(session_id: str, limit: int = Query(10, ge=1, le=50)):
    """Lấy lịch sử chat của session"""
    if session_id not in chatbot_sessions:
        raise HTTPException(status_code=404, detail="Session không tồn tại")
    
    session = chatbot_sessions[session_id]
    history = session.conversation_history[-limit:] if len(session.conversation_history) > limit else session.conversation_history
    
    return {
        "session_id": session_id,
        "history": history,
        "total_conversations": len(session.conversation_history),
        "last_activity": session.last_activity.isoformat()
    }

if __name__ == "__main__":
    uvicorn.run("main:server", host="0.0.0.0", port=8000, reload=True)