pdfjsLib.GlobalWorkerOptions.workerSrc = 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.16.105/pdf.worker.min.js';

document.addEventListener('DOMContentLoaded', function() {
  if (document.getElementById('successMessage')) {
    showNotification(document.getElementById('successMessage').textContent, 'success');
  }
  if (document.getElementById('errorMessage')) {
    showNotification(document.getElementById('errorMessage').textContent, 'error');
  }
  
  const menuToggle = document.getElementById('menu-toggle');
  const sidebar = document.getElementById('sidebar');
  
  if (menuToggle && sidebar) {
    menuToggle.addEventListener('click', function() {
      sidebar.classList.toggle('active');
    });
  }
  
  initPdfViewer();
  
  const upvoteBtn = document.querySelector('.vote-btn.upvote');
  const downvoteBtn = document.querySelector('.vote-btn.downvote');
  
  if (upvoteBtn) {
    upvoteBtn.addEventListener('click', function() {
      const docId = this.getAttribute('data-id');
      voteDocument(docId, 'up');
    });
  }
  
  if (downvoteBtn) {
    downvoteBtn.addEventListener('click', function() {
      const docId = this.getAttribute('data-id');
      voteDocument(docId, 'down');
    });
  }
  
  const commentInput = document.getElementById('comment-input');
  const postCommentBtn = document.getElementById('post-comment');
  
  if (commentInput && postCommentBtn) {
    postCommentBtn.addEventListener('click', function() {
      const comment = commentInput.value.trim();
      if (comment) {
        postComment(comment);
      }
    });
    
    commentInput.addEventListener('keydown', function(e) {
      if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        const comment = commentInput.value.trim();
        if (comment) {
          postComment(comment);
        }
      }
    });
  }
  
  const reportBtn = document.getElementById('report-document');
  const reportModal = document.getElementById('report-modal');
  const closeReportModal = document.getElementById('close-report-modal');
  const cancelReport = document.getElementById('cancel-report');
  const reportForm = document.getElementById('report-form');
  
  if (reportBtn && reportModal) {
    reportBtn.addEventListener('click', function() {
      reportModal.style.display = 'flex';
    });
    
    if (closeReportModal) {
      closeReportModal.addEventListener('click', function() {
        reportModal.style.display = 'none';
      });
    }
    
    if (cancelReport) {
      cancelReport.addEventListener('click', function() {
        reportModal.style.display = 'none';
      });
    }
    
    if (reportForm) {
      reportForm.addEventListener('submit', function(e) {
        e.preventDefault();
        submitReport();
      });
    }
  }
  
  const chatbotBtn = document.getElementById('ai-chatbot-btn');
  const chatbotModal = document.getElementById('chatbot-modal');
  const closeChatbotModal = document.getElementById('close-chatbot-modal');
  const sendQuestionBtn = document.getElementById('send-question');
  const chatbotInput = document.getElementById('chatbot-input');
  
  if (chatbotBtn && chatbotModal) {
    chatbotBtn.addEventListener('click', function() {
      chatbotModal.style.display = 'flex';
      
      // Chỉ khởi tạo chatbot khi chưa có session
      if (!currentQASessionId) {
        initChatbot();
      }
      
      const messages = document.getElementById('chatbot-messages');
      if (messages) {
        messages.scrollTop = messages.scrollHeight;
      }
    });
    
    if (closeChatbotModal) {
      closeChatbotModal.addEventListener('click', function() {
        chatbotModal.style.display = 'none';
      });
    }
    
    if (sendQuestionBtn && chatbotInput) {
      sendQuestionBtn.addEventListener('click', function() {
        const question = chatbotInput.value.trim();
        if (question) {
          sendQuestion(question);
        }
      });
      
      chatbotInput.addEventListener('keydown', function(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
          e.preventDefault();
          const question = chatbotInput.value.trim();
          if (question) {
            sendQuestion(question);
          }
        }
      });
    }
  }
  
  window.addEventListener('click', function(e) {
    if (e.target === reportModal) {
      reportModal.style.display = 'none';
    }
    if (e.target === chatbotModal) {
      chatbotModal.style.display = 'none';
    }
  });

  // Modal sửa thông tin tài liệu
  const editBtn = document.getElementById('edit-document-btn');
  const editModal = document.getElementById('edit-document-modal');
  const closeEditModal = document.getElementById('close-edit-document-modal');
  const cancelEdit = document.getElementById('cancel-edit-document');
  const saveEditBtn = document.getElementById('save-edit-document');

  if (editBtn && editModal) {
    editBtn.addEventListener('click', function() {
      // Chỉ cần mở modal, không set lại giá trị các trường
      editModal.style.display = 'flex';
    });
    if (closeEditModal) closeEditModal.onclick = () => editModal.style.display = 'none';
    if (cancelEdit) cancelEdit.onclick = () => editModal.style.display = 'none';
    window.addEventListener('click', function(e) {
      if (e.target === editModal) editModal.style.display = 'none';
    });
  }

  // Xử lý lưu thay đổi tài liệu
  if (saveEditBtn) {
    saveEditBtn.addEventListener('click', function(e) {
      e.preventDefault();
      const tieuDe = document.getElementById('edit-tieuDe').value.trim();
      const maLoai = document.getElementById('edit-maLoai').value;
      const maMonHoc = document.getElementById('edit-maMonHoc').value;
      const moTa = document.getElementById('edit-moTa').value.trim();
      const tags = document.getElementById('edit-tags').value.trim();
      const docId = window.location.pathname.split('/').pop();

      if (!tieuDe || !maLoai || !maMonHoc || !moTa) {
        showNotification('Vui lòng điền đầy đủ thông tin!', 'error');
        return;
      }

      // CSRF token
      const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
      const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

      fetch(`/documents/edit/${docId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          [csrfHeader]: csrfToken
        },
        body: JSON.stringify({
          tieuDe: tieuDe,
          maLoai: maLoai,
          maMonHoc: maMonHoc,
          moTa: moTa,
          tags: tags
        })
      })
      .then(res => res.json())
      .then(data => {
        if (data.success) {
          showNotification('Cập nhật tài liệu thành công!', 'success');
          setTimeout(() => window.location.reload(), 1200);
        } else {
          showNotification(data.message || 'Cập nhật thất bại!', 'error');
        }
      })
      .catch(() => {
        showNotification('Có lỗi xảy ra khi cập nhật!', 'error');
      });
    });
  }
});

function initPdfViewer() {
  const pdfContainer = document.getElementById('pdf-viewer');
  if (!pdfContainer) return;
  
  const pdfUrl = pdfContainer.getAttribute('data-pdf-url');
  console.log('PDF URL:', pdfUrl);
  let pdfDoc = null;
  let pageNum = 1;
  let pageRendering = false;
  let pageNumPending = null;
  let scale = 1.0;
  
  function renderPage(num) {
    pageRendering = true;
    
    // Remove any existing pages
    const existingPages = pdfContainer.querySelectorAll('.pdf-page');
    existingPages.forEach(page => page.remove());
    
    pdfDoc.getPage(num).then(function(page) {
      // Get container width to adjust scale for full width
      const containerWidth = pdfContainer.clientWidth - 40; // Accounting for padding
      const viewport = page.getViewport({scale: 1.0});
      
      // Adjust scale to fit container width
      const containerScale = containerWidth / viewport.width;
      const finalScale = Math.min(scale, containerScale);
      
      const finalViewport = page.getViewport({scale: finalScale});
      
      const canvas = document.createElement('canvas');
      canvas.className = 'pdf-page';
      canvas.width = finalViewport.width;
      canvas.height = finalViewport.height;
      
      const ctx = canvas.getContext('2d');
      const renderContext = {
        canvasContext: ctx,
        viewport: finalViewport
      };
      
      pdfContainer.appendChild(canvas);
      
      const renderTask = page.render(renderContext);
      
      renderTask.promise.then(function() {
        pageRendering = false;
        if (pageNumPending !== null) {
          renderPage(pageNumPending);
          pageNumPending = null;
        }
        
        // Update page number display
        document.getElementById('page-num').value = num;
      });
    });
  }
  
  function queueRenderPage(num) {
    if (pageRendering) {
      pageNumPending = num;
    } else {
      renderPage(num);
    }
  }
  
  function onPrevPage() {
    if (pageNum <= 1) {
      return;
    }
    pageNum--;
    queueRenderPage(pageNum);
  }
  
  function onNextPage() {
    if (pageNum >= pdfDoc.numPages) {
      return;
    }
    pageNum++;
    queueRenderPage(pageNum);
  }
  
  function onZoomIn() {
    scale += 0.25;
    if (scale > 3) scale = 3;
    queueRenderPage(pageNum);
    document.getElementById('zoom-select').value = scale.toString();
  }
  
  function onZoomOut() {
    scale -= 0.25;
    if (scale < 0.5) scale = 0.5;
    queueRenderPage(pageNum);
    document.getElementById('zoom-select').value = scale.toString();
  }
  
  function onFullScreen() {
    if (!document.fullscreenElement) {
      pdfContainer.requestFullscreen().catch(err => {
        console.error('Error attempting to enable full-screen mode:', err);
      });
    } else {
      if (document.exitFullscreen) {
        document.exitFullscreen();
      }
    }
  }
  
  // Bind PDF navigation buttons
  document.getElementById('prev-page').addEventListener('click', onPrevPage);
  document.getElementById('next-page').addEventListener('click', onNextPage);
  document.getElementById('zoom-in').addEventListener('click', onZoomIn);
  document.getElementById('zoom-out').addEventListener('click', onZoomOut);
  document.getElementById('fullscreen').addEventListener('click', onFullScreen);
  
  // Page input navigation
  document.getElementById('page-num').addEventListener('change', function() {
    const newPageNum = parseInt(this.value);
    if (newPageNum >= 1 && newPageNum <= pdfDoc.numPages) {
      pageNum = newPageNum;
      queueRenderPage(pageNum);
    } else {
      this.value = pageNum;
    }
  });
  
  // Zoom select
  document.getElementById('zoom-select').addEventListener('change', function() {
    scale = parseFloat(this.value);
    queueRenderPage(pageNum);
  });
  
  // Load PDF
  pdfjsLib.getDocument(pdfUrl).promise.then(function(doc) {
    pdfDoc = doc;
    
    // Update page count
    document.getElementById('page-count').textContent = doc.numPages;
    
    // Add window resize listener to rerender when size changes
    window.addEventListener('resize', function() {
      queueRenderPage(pageNum);
    });
    
    // Initial render
    renderPage(pageNum);
  }).catch(function(error) {
    console.error('Error loading PDF:', error);
    pdfContainer.innerHTML = `
      <div class="pdf-error">
        <i class="fas fa-exclamation-triangle"></i>
        <p>Không thể tải tài liệu PDF. Vui lòng thử lại sau.</p>
      </div>
    `;
  });
}

function voteDocument(docId, voteType) {
  const url = `/documents/${voteType}vote/${docId}`;
  
  fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    }
  })
  .then(response => response.json())
  .then(data => {
    if (data.success) {
      // Update vote counts in UI
      const upvoteBtn = document.querySelector('.vote-btn.upvote span');
      const downvoteBtn = document.querySelector('.vote-btn.downvote span');
      
      if (upvoteBtn) {
        upvoteBtn.textContent = data.upVotes;
      }
      
      if (downvoteBtn) {
        downvoteBtn.textContent = data.downVotes;
      }
      
      showNotification(data.message, 'success');
    } else {
      showNotification(data.message, 'error');
    }
  })
  .catch(error => {
    console.error('Error voting document:', error);
    showNotification('Đã có lỗi xảy ra khi đánh giá tài liệu.', 'error');
  });
}

function postComment(commentText) {
  const docId = window.location.pathname.split('/').pop();
  const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

  if (!csrfToken || !csrfHeader) {
      console.error('CSRF token or header not found');
      showNotification('Không thể gửi bình luận do thiếu CSRF token.', 'error');
      return;
  }

  fetch(`/documents/comment/${docId}`, {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json',
          [csrfHeader]: csrfToken // Thêm CSRF token
      },
      body: JSON.stringify({ content: commentText })
  })
  .then(response => {
      if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
      }
      return response.json();
  })
  .then(data => {
      if (data.success) {
          const commentsList = document.querySelector('.comments-list');
          const emptyComments = document.querySelector('.empty-comments');
          
          if (emptyComments) {
              emptyComments.remove();
          }
          
          const newComment = document.createElement('div');
          newComment.className = 'comment-item';
          newComment.innerHTML = `
              <img class="comment-avatar" src="${data.hinhAnh || '/img/default-avatar.png'}" 
                   onerror="this.src='/img/default-avatar.png'" alt="${data.authorName}">
              <div class="comment-content">
                  <div class="comment-header">
                      <span class="comment-author">${data.authorName}</span>
                      <span class="comment-time">${data.formattedDate}</span>
                  </div>
                  <div class="comment-text">${commentText}</div>
                  <div class="comment-actions">
                      <button class="comment-reply-btn">Trả lời</button>
                      <button class="comment-report-btn">Báo cáo</button>
                  </div>
              </div>
          `;
          
          commentsList.insertBefore(newComment, commentsList.firstChild);
          document.getElementById('comment-input').value = '';
          
          const commentCountElement = document.querySelector('.comment-count span');
          if (commentCountElement) {
              const currentCount = parseInt(commentCountElement.textContent) || 0;
              commentCountElement.textContent = currentCount + 1;
          }
          
          showNotification('Bình luận đã được đăng thành công!', 'success');
      } else {
          showNotification(data.message || 'Không thể đăng bình luận.', 'error');
      }
  })
  .catch(error => {
      console.error('Error posting comment:', error);
      showNotification('Đã có lỗi xảy ra khi đăng bình luận: ' + error.message, 'error');
  });
}

function submitReport() {
  const docId = window.location.pathname.split('/').pop();
  const reason = document.getElementById('report-reason').value;
  const description = document.getElementById('report-description').value;
  
  fetch(`/documents/report/${docId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      reason: reason,
      description: description
    })
  })
  .then(response => response.json())
  .then(data => {
    if (data.success) {
      // Close modal
      document.getElementById('report-modal').style.display = 'none';
      
      // Reset form
      document.getElementById('report-form').reset();
      
      showNotification('Cảm ơn bạn đã báo cáo! Chúng tôi sẽ xem xét tài liệu này.', 'success');
    } else {
      showNotification(data.message || 'Không thể gửi báo cáo.', 'error');
    }
  })
  .catch(error => {
    console.error('Error submitting report:', error);
    showNotification('Đã có lỗi xảy ra khi gửi báo cáo.', 'error');
  });
}

// Biến toàn cục để lưu sessionId
let currentQASessionId = null;

// Hàm khởi tạo chatbot
function initChatbot() {
  const pdfContainer = document.getElementById('pdf-viewer');
  const filename = pdfContainer?.getAttribute('data-filename');
  const chatMessages = document.getElementById('chatbot-messages');
  
  // Lưu tin nhắn chào mừng mặc định (thay vì xóa tất cả tin nhắn cũ)
  const welcomeMessage = chatMessages.querySelector('.bot-message:first-child');
  const welcomeContent = welcomeMessage ? welcomeMessage.innerHTML : null;
  
  // Kiểm tra session đã lưu trong localStorage
  const docId = window.location.pathname.split('/').pop();
  currentQASessionId = localStorage.getItem('qa_session_id_' + docId);
  
  // Nếu có session đã lưu, kiểm tra xem nó còn hoạt động không
  if (currentQASessionId && chatMessages) {
    // Hiển thị thông báo đang tải
    const loadingElement = document.createElement('div');
    loadingElement.className = 'chat-message system-message';
    loadingElement.innerHTML = `
      <div class="message-content text-center">
        <p><i class="fas fa-sync fa-spin"></i> Đang tải lịch sử hội thoại...</p>
      </div>
    `;
    chatMessages.appendChild(loadingElement);
    
    // Kiểm tra session còn hữu dụng không
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    
    fetch('/ai/session/history?sessionId=' + currentQASessionId, {
      headers: {
        [csrfHeader]: csrfToken
      }
    })
    .then(response => {
      if (!response.ok) throw new Error('Session không hợp lệ');
      return response.json();
    })
    .then(data => {
      // Xóa thông báo đang tải
      chatMessages.removeChild(loadingElement);
      
      // Hiển thị thông báo đã kết nối
      const reconnectElement = document.createElement('div');
      reconnectElement.className = 'chat-message system-message';
      reconnectElement.innerHTML = `
        <div class="message-content text-center">
          <p>Đã kết nối lại với phiên trò chuyện trước đó.</p>
        </div>
      `;
      chatMessages.appendChild(reconnectElement);
      
      // Hiển thị lịch sử hội thoại
      if (data.history && data.history.length > 0) {
        data.history.forEach(item => {
          addMessageToChat('user', item.question);
          addMessageToChat('ai', item.answer);
        });
      }
    })
    .catch(error => {
      console.error('Error checking session:', error);
      // Session không còn hợp lệ, xóa khỏi localStorage
      currentQASessionId = null;
      localStorage.removeItem('qa_session_id_' + docId);
      
      // Xóa thông báo đang tải
      chatMessages.removeChild(loadingElement);
    });
  }
}

// Hàm gửi câu hỏi đến chatbot
function sendQuestion(question) {
  const docId = window.location.pathname.split('/').pop();
  const pdfContainer = document.getElementById('pdf-viewer');
  const filename = pdfContainer.getAttribute('data-filename');
  const chatMessages = document.getElementById('chatbot-messages');
  const chatInput = document.getElementById('chatbot-input');
  const documentTitle = pdfContainer.getAttribute('data-document-title') || "Tài liệu";

  const userAvatar = document.getElementById('current-user-avatar')?.value || '/img/default-avatar.png';
  const userName = document.getElementById('current-user-name')?.value || 'Bạn';
  
  // Thêm tin nhắn người dùng vào chat
  addMessageToChat('user', question);
  
  // Hiển thị trạng thái đang tải
  const loadingId = showLoadingMessage();
  
  // Xóa input
  chatInput.value = '';
  
  // Lấy CSRF token và header
  const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
  
  // Kiểm tra xem đã có sessionId chưa
  if (!currentQASessionId) {
    // Nếu chưa có session, tạo mới
    fetch('/ai/create-session', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        [csrfHeader]: csrfToken
      },
      body: new URLSearchParams({
        filename: filename
      })
    })
    .then(response => {
      if (!response.ok) throw new Error('Không thể tạo phiên hỏi đáp');
      return response.json();
    })
    .then(data => {
      // Lưu session ID
      currentQASessionId = data.session_id;
      localStorage.setItem('qa_session_id_' + docId, currentQASessionId);
      
      // Tiếp tục gửi câu hỏi với session mới
      askQuestionWithSession(question, loadingId, csrfToken, csrfHeader);
    })
    .catch(error => {
      console.error('Error creating session:', error);
      hideLoadingMessage(loadingId);
      
      // Hiển thị thông báo lỗi
      addMessageToChat('system', 'Không thể tạo phiên hỏi đáp. Vui lòng thử lại sau.');
    });
  } else {
    // Đã có session, gửi câu hỏi ngay
    askQuestionWithSession(question, loadingId, csrfToken, csrfHeader);
  }
}

// Hàm gửi câu hỏi khi đã có session
function askQuestionWithSession(question, loadingId, csrfToken, csrfHeader) {
  fetch('/ai/session/ask', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      [csrfHeader]: csrfToken
    },
    body: new URLSearchParams({
      sessionId: currentQASessionId,
      question: question
    })
  })
  .then(response => {
    if (!response.ok) {
      // Nếu session không hợp lệ, thử tạo lại session
      if (response.status === 404) {
        currentQASessionId = null;
        const docId = window.location.pathname.split('/').pop();
        localStorage.removeItem('qa_session_id_' + docId);
        
        // Gọi lại hàm sendQuestion để tạo session mới
        hideLoadingMessage(loadingId);
        addMessageToChat('system', 'Phiên hỏi đáp đã hết hạn. Đang tạo phiên mới...');
        sendQuestion(question);
        return null;
      }
      throw new Error('Lỗi khi gửi câu hỏi');
    }
    return response.json();
  })
  .then(data => {
    if (!data) return; // Đã xử lý tạo session mới ở trên
    
    // Ẩn thông báo đang tải
    hideLoadingMessage(loadingId);
    
    // Hiển thị câu trả lời
    addMessageToChat('ai', data.answer);
  })
  .catch(error => {
    console.error('Error asking question:', error);
    
    // Ẩn thông báo đang tải
    hideLoadingMessage(loadingId);
    
    // Hiển thị thông báo lỗi
    addMessageToChat('system', 'Xin lỗi, đã có lỗi xảy ra khi xử lý câu hỏi của bạn.');
  });
}

// Thêm tin nhắn vào chat
function addMessageToChat(sender, content) {
  const chatMessages = document.getElementById('chatbot-messages');
  if (!chatMessages) return;
  
  const pdfContainer = document.getElementById('pdf-viewer');
  const documentTitle = pdfContainer?.getAttribute('data-document-title') || "Tài liệu";
  const userAvatar = document.getElementById('current-user-avatar')?.value || '/img/default-avatar.png';
  const userName = document.getElementById('current-user-name')?.value || 'Bạn';
  
  let messageElement;
  
  if (sender === 'user') {
    messageElement = document.createElement('div');
    messageElement.className = 'chat-message user-message';
    messageElement.innerHTML = `
      <div class="message-avatar">
        <img src="${userAvatar}" alt="${userName}" onerror="this.src='/img/default-avatar.png'">
      </div>
      <div class="message-content">
        <p>${escapeHtml(content)}</p>
      </div>
    `;
  } else if (sender === 'ai') {
    messageElement = document.createElement('div');
    messageElement.className = 'chat-message bot-message';
    messageElement.innerHTML = `
      <div class="message-avatar">
        <img src="/img/gemma.png" alt="Gemma">
      </div>
      <div class="message-content">
        <div class="message-file"><strong>Tài liệu:</strong> ${documentTitle}</div>
        <p>${content ? formatChatbotResponse(content) : 'Xin lỗi, tôi không thể xử lý câu hỏi của bạn lúc này.'}</p>
      </div>
    `;
  } else {
    // System message
    messageElement = document.createElement('div');
    messageElement.className = 'chat-message system-message';
    messageElement.innerHTML = `
      <div class="message-content text-center">
        <p>${escapeHtml(content)}</p>
      </div>
    `;
  }
  
  chatMessages.appendChild(messageElement);
  chatMessages.scrollTop = chatMessages.scrollHeight;
}

// Hiển thị thông báo đang tải
function showLoadingMessage() {
  const chatMessages = document.getElementById('chatbot-messages');
  if (!chatMessages) return null;
  
  const loadingId = 'loading-' + Date.now();
  const loadingElement = document.createElement('div');
  loadingElement.id = loadingId;
  loadingElement.className = 'chat-message bot-message loading';
  loadingElement.innerHTML = `
    <div class="message-avatar">
      <img src="/img/gemma.png" alt="Gemma">
    </div>
    <div class="message-content">
      <p>Đang suy nghĩ...</p>
    </div>
  `;
  
  chatMessages.appendChild(loadingElement);
  chatMessages.scrollTop = chatMessages.scrollHeight;
  
  return loadingId;
}

// Ẩn thông báo đang tải
function hideLoadingMessage(loadingId) {
  if (!loadingId) return;
  
  const loadingElement = document.getElementById(loadingId);
  if (loadingElement) {
    const chatMessages = document.getElementById('chatbot-messages');
    chatMessages.removeChild(loadingElement);
  }
}

// Thêm vào phần khởi tạo document onload
document.addEventListener('DOMContentLoaded', function() {
  // ...existing code...
  
  const chatbotBtn = document.getElementById('ai-chatbot-btn');
  const chatbotModal = document.getElementById('chatbot-modal');
  const closeChatbotModal = document.getElementById('close-chatbot-modal');
  const sendQuestionBtn = document.getElementById('send-question');
  const chatbotInput = document.getElementById('chatbot-input');
  
  if (chatbotBtn && chatbotModal) {
    chatbotBtn.addEventListener('click', function() {
      chatbotModal.style.display = 'flex';
      
      // Chỉ khởi tạo chatbot khi chưa có session
      if (!currentQASessionId) {
        initChatbot();
      }
      
      const messages = document.getElementById('chatbot-messages');
      if (messages) {
        messages.scrollTop = messages.scrollHeight;
      }
    });
    
    if (closeChatbotModal) {
      closeChatbotModal.addEventListener('click', function() {
        chatbotModal.style.display = 'none';
      });
    }
    
    if (sendQuestionBtn && chatbotInput) {
      sendQuestionBtn.addEventListener('click', function() {
        const question = chatbotInput.value.trim();
        if (question) {
          sendQuestion(question);
        }
      });
      
      chatbotInput.addEventListener('keydown', function(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
          e.preventDefault();
          const question = chatbotInput.value.trim();
          if (question) {
            sendQuestion(question);
          }
        }
      });
    }
  }
  
  // ...existing code...
});
function showNotification(message, type) {

  let notification = document.getElementById('notification');
  if (!notification) {
    notification = document.createElement('div');
    notification.id = 'notification';
    document.body.appendChild(notification);
  }

  notification.textContent = message;
  notification.className = 'notification ' + type;

  notification.style.display = 'block';
  notification.style.opacity = '1';

  setTimeout(function() {
    notification.style.opacity = '0';
    setTimeout(function() {
      notification.style.display = 'none';
    }, 500);
  }, 5000);
}

function escapeHtml(unsafe) {
  return unsafe
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}

function formatChatbotResponse(text) {
  text = text.replace(/\n/g, '<br>');
  text = text.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
  text = text.replace(/\*(.*?)\*/g, '<em>$1</em>');
  return text;
}