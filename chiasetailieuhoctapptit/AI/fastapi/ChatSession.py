from datetime import datetime, timedelta

class ChatSession:
    def __init__(self, session_id: str, user_id: str = None):
        self.session_id = session_id
        self.user_id = user_id
        self.created_at = datetime.now()
        self.last_activity = datetime.now()
        self.conversation_history = []
        self.preferences = {}
        self.context = {}
    
    def add_conversation(self, question: str, answer: str):
        self.conversation_history.append({
            'timestamp': datetime.now().isoformat(),
            'question': question,
            'answer': answer
        })
        self.last_activity = datetime.now()

        if len(self.conversation_history) > 10:
            self.conversation_history = self.conversation_history[-10:]
    
    def get_recent_context(self, limit=3):
        recent = self.conversation_history[-limit:] if self.conversation_history else []
        return "\n".join([f"Q: {conv['question']}\nA: {conv['answer']}" for conv in recent])
    
    def is_expired(self, hours=24):
        return datetime.now() - self.last_activity > timedelta(hours=hours)