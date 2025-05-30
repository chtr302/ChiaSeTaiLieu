// PDF.js configuration
pdfjsLib.GlobalWorkerOptions.workerSrc = 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.16.105/pdf.worker.min.js';

// Set page-specific notification function
window.pageShowNotification = showNotification;

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
    if (e.target === document.getElementById('report-comment-modal')) {
      document.getElementById('report-comment-modal').style.display = 'none';
    }
  });
  
  // Comment reply buttons
  document.querySelectorAll('.comment-reply-btn').forEach(button => {
    button.addEventListener('click', function() {
      const commentId = this.getAttribute('data-comment-id');
      const replyForm = document.getElementById('reply-form-' + commentId);
      
      // Hide all other reply forms
      document.querySelectorAll('.reply-form').forEach(form => {
        if (form.id !== 'reply-form-' + commentId) {
          form.style.display = 'none';
        }
      });
      
      // Toggle this reply form
      replyForm.style.display = replyForm.style.display === 'none' ? 'block' : 'none';
      
      // Focus on textarea if showing form
      if (replyForm.style.display === 'block') {
        document.getElementById('reply-input-' + commentId).focus();
      }
    });
  });
  
  // Reply cancel buttons
  document.querySelectorAll('.reply-cancel-btn').forEach(button => {
    button.addEventListener('click', function() {
      const commentId = this.getAttribute('data-comment-id');
      const replyForm = document.getElementById('reply-form-' + commentId);
      replyForm.style.display = 'none';
      document.getElementById('reply-input-' + commentId).value = '';
    });
  });
  
  // Reply submit buttons
  document.querySelectorAll('.reply-submit-btn').forEach(button => {
    button.addEventListener('click', function() {
      const commentId = this.getAttribute('data-comment-id');
      const replyContent = document.getElementById('reply-input-' + commentId).value.trim();
      
      if (replyContent) {
        submitReply(commentId, replyContent);
      }
    });
  });
  
  // Comment report buttons
  document.querySelectorAll('.comment-report-btn').forEach(button => {
    button.addEventListener('click', function() {
      const commentId = this.getAttribute('data-comment-id');
      document.getElementById('comment-id-to-report').value = commentId;
      document.getElementById('report-comment-modal').style.display = 'flex';
    });
  });
  
  // Report comment modal close button
  const closeReportCommentModal = document.getElementById('close-report-comment-modal');
  if (closeReportCommentModal) {
    closeReportCommentModal.addEventListener('click', function() {
      document.getElementById('report-comment-modal').style.display = 'none';
    });
  }
  
  // Cancel report comment button
  const cancelReportComment = document.getElementById('cancel-report-comment');
  if (cancelReportComment) {
    cancelReportComment.addEventListener('click', function() {
      document.getElementById('report-comment-modal').style.display = 'none';
    });
  }
  
  // Report comment form submission
  const reportCommentForm = document.getElementById('report-comment-form');
  if (reportCommentForm) {
    reportCommentForm.addEventListener('submit', function(e) {
      e.preventDefault();
      submitCommentReport();
    });
  }
  
});

// Submit report function
function submitReport() {
  const docId = window.location.pathname.split('/').pop();
  const reason = document.getElementById('report-reason').value;
  const description = document.getElementById('report-description').value;
  
  // Lấy CSRF token và header
  const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
  
  fetch(`/documents/report/${docId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      [csrfHeader]: csrfToken
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


// Submit reply function
function submitReply(commentId, replyContent) {
  const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

  if (!csrfToken || !csrfHeader) {
    console.error('CSRF token or header not found');
    showNotification('Không thể gửi trả lời do thiếu CSRF token.', 'error');
    return;
  }

  fetch(`/documents/reply/${commentId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      [csrfHeader]: csrfToken
    },
    body: JSON.stringify({ content: replyContent })
  })
  .then(response => {
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    return response.json();
  })
  .then(data => {
    if (data.success) {
      // Add the new reply to the UI
      const repliesList = document.getElementById('replies-' + commentId);
      
      const newReply = document.createElement('div');
      newReply.className = 'reply-item';
      newReply.innerHTML = `
        <img class="reply-avatar" src="${data.hinhAnh || '/img/default-avatar.png'}" 
             onerror="this.src='/img/default-avatar.png'" alt="${data.authorName}">
        <div class="reply-content">
          <div class="reply-header">
            <span class="reply-author">${data.authorName}</span>
            <span class="reply-time">${data.formattedDate}</span>
          </div>
          <div class="reply-text">${replyContent}</div>
        </div>
      `;
      
      repliesList.appendChild(newReply);
      
      // Hide the reply form and reset input
      const replyForm = document.getElementById('reply-form-' + commentId);
      replyForm.style.display = 'none';
      document.getElementById('reply-input-' + commentId).value = '';
      
      showNotification('Trả lời đã được đăng thành công!', 'success');
    } else {
      showNotification(data.message || 'Không thể đăng trả lời.', 'error');
    }
  })
  .catch(error => {
    console.error('Error posting reply:', error);
    showNotification('Đã có lỗi xảy ra khi đăng trả lời: ' + error.message, 'error');
  });
}

// Submit comment report function
function submitCommentReport() {
  const commentId = document.getElementById('comment-id-to-report').value;
  const title = document.getElementById('report-comment-title').value;
  const reason = document.getElementById('report-comment-reason').value;
  const description = document.getElementById('report-comment-description').value;
  
  const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

  if (!csrfToken || !csrfHeader) {
    console.error('CSRF token or header not found');
    showNotification('Không thể gửi báo cáo do thiếu CSRF token.', 'error');
    return;
  }
  
  fetch(`/documents/report-comment/${commentId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      [csrfHeader]: csrfToken
    },
    body: JSON.stringify({
      title: title,
      reason: reason,
      description: description
    })
  })
  .then(response => {
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    return response.json();
  })
  .then(data => {
    if (data.success) {
      // Close modal
      document.getElementById('report-comment-modal').style.display = 'none';
      
      // Reset form
      document.getElementById('report-comment-form').reset();
      
      showNotification('Cảm ơn bạn đã báo cáo! Chúng tôi sẽ xem xét bình luận này.', 'success');
    } else {
      showNotification(data.message || 'Không thể gửi báo cáo.', 'error');
    }
  })
  .catch(error => {
    console.error('Error submitting comment report:', error);
    showNotification('Đã có lỗi xảy ra khi gửi báo cáo.', 'error');
  });
}

// Post comment function
function postComment(comment) {
  const docId = window.location.pathname.split('/').pop();
  
  const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

  if (!csrfToken || !csrfHeader) {
    console.error('CSRF token or header not found');
    showNotification('Không thể đăng bình luận do thiếu CSRF token.', 'error');
    return;
  }

  fetch(`/documents/comment/${docId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      [csrfHeader]: csrfToken
    },
    body: JSON.stringify({ content: comment })
  })
  .then(response => {
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    return response.json();
  })
  .then(data => {
    if (data.success) {
      // Clear comment input
      document.getElementById('comment-input').value = '';
      
      // Show success message
      showNotification('Bình luận đã được đăng thành công!', 'success');
      
      // Reload page to show new comment
      setTimeout(() => {
        window.location.reload();
      }, 1000);
    } else {
      showNotification(data.message || 'Không thể đăng bình luận.', 'error');
    }
  })
  .catch(error => {
    console.error('Error posting comment:', error);
    showNotification('Đã có lỗi xảy ra khi đăng bình luận: ' + error.message, 'error');
  });
}