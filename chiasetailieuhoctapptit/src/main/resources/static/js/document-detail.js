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
  
  // Close modals when clicking outside
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
          moTa: moTa
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

// PDF Viewer functions
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

// Vote document function
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

// Post comment function
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

// Submit report function
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

// Send question to AI chatbot
function sendQuestion(question) {
  const docId = window.location.pathname.split('/').pop();
  const pdfContainer = document.getElementById('pdf-viewer');
  const filename = pdfContainer.getAttribute('data-filename');
  const chatMessages = document.getElementById('chatbot-messages');
  const chatInput = document.getElementById('chatbot-input');
  
  // Add user message to chat
  const userMessageElement = document.createElement('div');
  userMessageElement.className = 'chat-message user-message';
  userMessageElement.innerHTML = `
    <div class="message-avatar">
      <i class="fas fa-user"></i>
    </div>
    <div class="message-content">
      <p>${escapeHtml(question)}</p>
    </div>
  `;
  chatMessages.appendChild(userMessageElement);
  
  // Add loading message
  const loadingElement = document.createElement('div');
  loadingElement.className = 'chat-message bot-message loading';
  loadingElement.innerHTML = `
    <div class="message-avatar">
      <i class="fas fa-robot"></i>
    </div>
    <div class="message-content">
      <p>Đang suy nghĩ...</p>
    </div>
  `;
  chatMessages.appendChild(loadingElement);
  
  // Scroll to bottom
  chatMessages.scrollTop = chatMessages.scrollHeight;
  
  // Clear input
  chatInput.value = '';
  
  // Lấy CSRF token và header
  const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
  // Send request to backend
  fetch('/ai/ask-question', {
     method: 'POST',
     headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      [csrfHeader]: csrfToken
     },
     body: new URLSearchParams({
       filename: filename,
       question: question
     })
    
  })
  .then(response => response.json())
  .then(data => {
     // Remove loading message
     chatMessages.removeChild(loadingElement);

     const botMessageElement = document.createElement('div');
     botMessageElement.className = 'chat-message bot-message';
     botMessageElement.innerHTML = `
      <div class="message-avatar">
        <i class="fas fa-robot"></i>
      </div>
      <div class="message-content">
        <div class="message-file"><strong>Tài liệu:</strong> ${data.filename}</div>
        <p>${data.answer ? formatChatbotResponse(data.answer) : 'Xin lỗi, tôi không thể xử lý câu hỏi của bạn lúc này.'}</p>
      </div>
    `;
     chatMessages.appendChild(botMessageElement);
     
     // Scroll to bottom
     chatMessages.scrollTop = chatMessages.scrollHeight;
   })
  .catch(error => {
    console.error('Error getting chatbot response:', error);
    
    chatMessages.removeChild(loadingElement);

    const errorMessageElement = document.createElement('div');
    errorMessageElement.className = 'chat-message bot-message error';
    errorMessageElement.innerHTML = `
      <div class="message-avatar">
        <i class="fas fa-robot"></i>
      </div>
      <div class="message-content">
        <p>Xin lỗi, đã có lỗi xảy ra. Vui lòng thử lại sau.</p>
      </div>
    `;
    chatMessages.appendChild(errorMessageElement);

    chatMessages.scrollTop = chatMessages.scrollHeight;
  });
}

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