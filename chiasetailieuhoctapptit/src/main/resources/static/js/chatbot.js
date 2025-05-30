class Chatbot {
    constructor() {
        this.isOpen = false;
        this.isMinimized = false;
        this.config = window.chatbotConfig || {};
        this.sessionId = null;
        this.userId = this.config.userId || null;

        this.userAvatar = this.config.userAvatar || '/images/default-user.png';
        this.userName = this.config.userName || 'User';

        this.csrfToken = this.getCsrfToken();
        
        this.initElements();
        this.bindEvents();
        this.setupAutoResize();
        this.initializeSession();
    }

    initElements() {
        this.trigger = document.getElementById('chatbot-trigger');
        this.modal = document.getElementById('chatbot-modal');
        this.messagesContainer = document.getElementById('chatbot-messages');
        this.input = document.getElementById('chatbot-input');
        this.sendBtn = document.getElementById('chatbot-send');
        this.closeBtn = document.getElementById('chatbot-close');
        this.minimizeBtn = document.getElementById('chatbot-minimize');
        this.charCount = document.getElementById('char-count');

        if (!this.trigger || !this.modal || !this.messagesContainer) {
            return;
        }
    }

    bindEvents() {
        if (!this.trigger) {
            return;
        }

        this.trigger.addEventListener('click', (e) => {
            e.stopPropagation();
            this.toggleChat();
        });
        
        this.closeBtn?.addEventListener('click', (e) => {
            e.stopPropagation();
            this.closeChat();
        });
        
        this.minimizeBtn?.addEventListener('click', (e) => {
            e.stopPropagation();
            this.toggleMinimize();
        });
        
        this.input?.addEventListener('input', () => this.handleInputChange());
        this.input?.addEventListener('keydown', (e) => this.handleKeyDown(e));
        this.sendBtn?.addEventListener('click', () => this.sendMessage());

        this.modal?.addEventListener('click', (e) => {
            e.stopPropagation();
        });

        document.addEventListener('click', (e) => {
            if (this.isOpen && !this.isMinimized) {
                if (!e.target.closest('#chatbot-container')) {
                    this.closeChat();
                }
            }
        });
    }

    setupAutoResize() {
        if (this.input) {
            this.input.addEventListener('input', () => {
                this.input.style.height = 'auto';
                this.input.style.height = Math.min(this.input.scrollHeight, 100) + 'px';
            });
        }
    }

    handleInputChange() {
        if (!this.input || !this.sendBtn) return;
        
        const text = this.input.value;
        const length = text.length;

        if (this.charCount) {
            this.charCount.textContent = length;
        }

        this.sendBtn.disabled = length === 0 || length > 1000;

        if (this.charCount) {
            if (length > 900) {
                this.charCount.style.color = '#dc3545'; // Red
            } else if (length > 800) {
                this.charCount.style.color = '#ffc107'; // Yellow
            } else {
                this.charCount.style.color = '#666'; // Default
            }
        }
    }

    getCsrfToken() {
        const metaToken = document.querySelector('meta[name="_csrf"]');
        if (metaToken) {
            return metaToken.getAttribute('content');
        }

        const cookies = document.cookie.split(';');
        for (let cookie of cookies) {
            const [name, value] = cookie.trim().split('=');
            if (name === 'XSRF-TOKEN') {
                return decodeURIComponent(value);
            }
        }
        
        return null;
    }

    getHeaders() {
        const headers = {};
        
        if (this.csrfToken) {
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]');
            const headerName = csrfHeader ? csrfHeader.getAttribute('content') : 'X-CSRF-TOKEN';
            headers[headerName] = this.csrfToken;
        }
        
        return headers;
    }

    async initializeSession() {
        const storedSessionId = localStorage.getItem('chatbot_session_id');
        
        if (storedSessionId) {
            this.sessionId = storedSessionId;
        } else {
            // Create new session
            await this.createNewSession();
        }
    }

    async createNewSession() {
        try {
            const formData = new FormData();
            if (this.userId) {
                formData.append('user_id', this.userId);
            }

            if (this.csrfToken) {
                const csrfParam = document.querySelector('meta[name="_csrf_parameter"]');
                const paramName = csrfParam ? csrfParam.getAttribute('content') : '_csrf';
                formData.append(paramName, this.csrfToken);
            }

            const response = await fetch('/ai/chatbot/session', {
                method: 'POST',
                headers: this.getHeaders(),
                body: formData
            });


            if (response.ok) {
                const data = await response.json();
                this.sessionId = data.session_id;
                localStorage.setItem('chatbot_session_id', this.sessionId);
            } else {
                const errorText = await response.text();
                this.sessionId = 'fallback_' + Date.now();
            }
        } catch (error) {
            this.sessionId = 'fallback_' + Date.now();
        }
    }

    toggleChat() {
        if (this.isOpen) {
            if (this.isMinimized) {
                this.restoreChat();
            } else {
                this.minimizeChat();
            }
        } else {
            this.openChat();
        }
    }

    openChat() {
        if (!this.modal) return;
        
        this.modal.classList.remove('hidden');
        this.isOpen = true;
        this.isMinimized = false;
        
        setTimeout(() => {
            this.modal.classList.add('show');
            this.input?.focus();
        }, 10);
    }

    closeChat() {
        if (!this.modal) return;
        
        this.modal.classList.remove('show');
        setTimeout(() => {
            this.modal.classList.add('hidden');
            this.isOpen = false;
            this.isMinimized = false;
        }, 300);
    }

    minimizeChat() {
        if (!this.modal) return;
        
        this.modal.classList.add('minimized');
        this.isMinimized = true;
        // Don't close, just minimize
    }

    restoreChat() {
        if (!this.modal) return;
        
        this.modal.classList.remove('minimized');
        this.isMinimized = false;
        this.input?.focus();
    }

    toggleMinimize() {
        if (this.isMinimized) {
            this.restoreChat();
        } else {
            this.minimizeChat();
        }
    }

    handleKeyDown(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            if (!this.sendBtn?.disabled) {
                this.sendMessage();
            }
        }
    }

    async sendMessage() {
        const text = this.input?.value?.trim();
        if (!text || !this.sessionId) return;

        this.addMessage(text, 'user');
        this.input.value = '';
        this.handleInputChange();
        this.showTyping();
        
        try {
            const formData = new FormData();
            formData.append('session_id', this.sessionId);
            formData.append('question', text);
            formData.append('chat_type', this.config.chatType || 'search');
            if (this.userId) {
                formData.append('user_id', this.userId);
            }

            if (this.csrfToken) {
                const csrfParam = document.querySelector('meta[name="_csrf_parameter"]');
                const paramName = csrfParam ? csrfParam.getAttribute('content') : '_csrf';
                formData.append(paramName, this.csrfToken);
            }

            for (let pair of formData.entries()) {
                console.log(pair[0] + ': ' + pair[1]);
            }

            const response = await fetch('/ai/chatbot/chat', {
                method: 'POST',
                headers: this.getHeaders(),
                body: formData
            });

            this.hideTyping();

            if (response.ok) {
                const data = await response.json();
                this.addMessage(data.answer || 'Không có phản hồi.', 'bot');
            } else {
                const errorText = await response.text();
                throw new Error(`HTTP ${response.status}: ${errorText}`);
            }
            
        } catch (error) {
            this.hideTyping();
            this.addMessage('Xin lỗi, đã có lỗi xảy ra. Vui lòng thử lại!', 'bot');
        }
    }

    addMessage(text, sender) {
        if (!this.messagesContainer) return;
        
        const messageDiv = document.createElement('div');
        messageDiv.className = `message ${sender}-message`;
        
        const currentTime = new Date().toLocaleTimeString('vi-VN', {
            hour: '2-digit',
            minute: '2-digit'
        });

        const avatarHtml = sender === 'bot' 
            ? '<img src="/img/gemma.png" alt="Bot" class="message-avatar-image" onerror="this.style.display=\'none\'; this.parentElement.innerHTML=\'🤖\'">'
            : `<img src="${this.userAvatar}" alt="${this.userName}" class="message-avatar-image" onerror="this.style.display='none'; this.parentElement.innerHTML='👤'">`;
        
        const senderName = sender === 'bot' ? 'AI Assistant' : this.userName;
        const showSenderName = sender === 'user';

        if (sender === 'user') {
            messageDiv.innerHTML = `
                <div class="message-content">
                    ${showSenderName ? `<div class="message-sender">${senderName}</div>` : ''}
                    <div class="message-text">${this.formatMessage(text)}</div>
                    <div class="message-time">${currentTime}</div>
                </div>
                <div class="message-avatar">
                    ${avatarHtml}
                </div>
            `;
        } else {
            messageDiv.innerHTML = `
                <div class="message-avatar">
                    ${avatarHtml}
                </div>
                <div class="message-content">
                    ${showSenderName ? `<div class="message-sender">${senderName}</div>` : ''}
                    <div class="message-text">${this.formatMessage(text)}</div>
                    <div class="message-time">${currentTime}</div>
                </div>
            `;
        }
        
        this.messagesContainer.appendChild(messageDiv);
        this.scrollToBottom();
        this.storeMessageLocally(text, sender);
    }

    formatMessage(text) {
        if (typeof text !== 'string') return '';
        
        return text
            .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
            .replace(/\*(.*?)\*/g, '<em>$1</em>')
            .replace(/`(.*?)`/g, '<code>$1</code>')
            .replace(/\n/g, '<br>');
    }

    showTyping() {
        if (!this.messagesContainer) return;
        
        const typingDiv = document.createElement('div');
        typingDiv.className = 'message bot-message typing-message';
        typingDiv.innerHTML = `
            <div class="message-avatar">
                <img src="/img/gemma.png" alt="Bot" class="message-avatar-image" onerror="this.style.display='none'; this.parentElement.innerHTML='🤖'">
            </div>
            <div class="message-content">
                <div class="typing-indicator">
                    <div class="typing-dot"></div>
                    <div class="typing-dot"></div>
                    <div class="typing-dot"></div>
                </div>
            </div>
        `;
        
        this.messagesContainer.appendChild(typingDiv);
        this.scrollToBottom();
    }

    hideTyping() {
        const typingMessage = this.messagesContainer?.querySelector('.typing-message');
        if (typingMessage) {
            typingMessage.remove();
        }
    }

    scrollToBottom() {
        if (this.messagesContainer) {
            this.messagesContainer.scrollTop = this.messagesContainer.scrollHeight;
        }
    }

    storeMessageLocally(text, sender) {
        if (!this.sessionId) return;
        
        try {
            const sessionKey = `chatbot_history_${this.sessionId}`;
            let history = JSON.parse(localStorage.getItem(sessionKey) || '[]');
            
            history.push({
                text: text,
                sender: sender,
                timestamp: Date.now()
            });

            if (history.length > 50) {
                history = history.slice(-50);
            }
            
            localStorage.setItem(sessionKey, JSON.stringify(history));
        } catch (error) {
            console.error('Error storing message locally:', error);
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const container = document.getElementById('chatbot-container');
    if (container) {
        console.log('Chatbot container found, initializing chatbot');
        try {
            window.chatbot = new Chatbot();
        } catch (error) {
            console.error('Failed to initialize chatbot:', error);
        }
    } else {
        console.error('Chatbot container not found');
    }
});