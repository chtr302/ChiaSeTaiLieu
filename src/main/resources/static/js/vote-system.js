/**
 * Vote System - Common JavaScript for all pages
 * Supports voting functionality across the entire application
 */

// Vote document function
function voteDocument(docId, voteType) {
  const url = `/api/vote/${voteType}`;
  
  console.log(`Voting for document ${docId} with type ${voteType}`);
  
  // Prevent double voting by disabling buttons temporarily
  const allVoteButtons = document.querySelectorAll(`[data-id="${docId}"]`);
  allVoteButtons.forEach(btn => {
    btn.disabled = true;
    btn.style.opacity = '0.6';
  });
  
  // Lấy CSRF token và header
  const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
  
  if (!csrfToken || !csrfHeader) {
    // Re-enable buttons on error
    allVoteButtons.forEach(btn => {
      btn.disabled = false;
      btn.style.opacity = '1';
    });
    showNotification('Không thể thực hiện vote do thiếu CSRF token.', 'error');
    return;
  }
  
  fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      [csrfHeader]: csrfToken
    },
    body: new URLSearchParams({
      maTaiLieu: docId
    })
  })
  .then(response => {
    if (response.ok) {
      return response.text();
    } else {
      return response.text().then(text => {
        throw new Error(text);
      });
    }
  })
  .then(message => {
    console.log(`Vote response: ${message}`);
    // Cập nhật UI sau khi vote thành công
    updateVoteUI(docId);
    showNotification(message, 'success');
  })
  .catch(error => {
    console.error('Error voting document:', error);
    showNotification(error.message || 'Đã có lỗi xảy ra khi đánh giá tài liệu.', 'error');
  })
  .finally(() => {
    // Re-enable buttons
    allVoteButtons.forEach(btn => {
      btn.disabled = false;
      btn.style.opacity = '1';
    });
  });
}

// Function để cập nhật UI sau khi vote
function updateVoteUI(docId) {
  console.log(`Updating UI for document ${docId}`);
  
  fetch(`/api/vote/info/${docId}`)
  .then(response => response.json())
  .then(data => {
    console.log(`Vote data for document ${docId}:`, data);
    
    // Cập nhật tất cả vote count elements cho document này (có thể có nhiều trên cùng trang)
    const voteCountSelectors = [
      `[data-doc-id="${docId}"] .vote-count`,
      `[data-docid="${docId}"] .vote-count`, 
      `.document-item[data-id="${docId}"] .vote-count`,
      `.document-card[data-id="${docId}"] .vote-count`,
      '#vote-score' // For detail page
    ];
    
    let elementsUpdated = 0;
    voteCountSelectors.forEach(selector => {
      const elements = document.querySelectorAll(selector);
      elements.forEach(element => {
        element.textContent = data.displayScore;
        elementsUpdated++;
        console.log(`Updated vote count for element with selector ${selector}: ${data.displayScore}`);
      });
    });
    
    console.log(`Total vote count elements updated: ${elementsUpdated}`);
    
    // Cập nhật trạng thái nút vote cho TẤT CẢ containers của document này
    const containerSelectors = [
      `[data-doc-id="${docId}"]`,
      `[data-docid="${docId}"]`,
      `.document-item[data-id="${docId}"]`,
      `.document-card[data-id="${docId}"]`
    ];
    
    let containersUpdated = 0;
    containerSelectors.forEach(selector => {
      const containers = document.querySelectorAll(selector);
      containers.forEach(container => {
        const upvoteBtn = container.querySelector('.upvote-btn');
        const downvoteBtn = container.querySelector('.downvote-btn');
        
        if (upvoteBtn && downvoteBtn) {
          // Reset trạng thái
          upvoteBtn.classList.remove('voted');
          downvoteBtn.classList.remove('voted');
          
          // Thêm class voted cho nút được chọn
          if (data.userVote === 1) {
            upvoteBtn.classList.add('voted');
          } else if (data.userVote === -1) {
            downvoteBtn.classList.add('voted');
          }
          
          containersUpdated++;
          console.log(`Updated vote buttons for container with selector ${selector}, userVote: ${data.userVote}`);
        }
      });
    });
    
    console.log(`Total vote containers updated: ${containersUpdated}`);
    
    // Special handling for document detail page
    if (window.location.pathname.includes('/detail/') && 
        window.location.pathname.split('/').pop() === docId) {
      const detailUpvoteBtn = document.querySelector('.upvote-btn');
      const detailDownvoteBtn = document.querySelector('.downvote-btn');
      
      if (detailUpvoteBtn && detailDownvoteBtn) {
        detailUpvoteBtn.classList.remove('voted');
        detailDownvoteBtn.classList.remove('voted');
        
        if (data.userVote === 1) {
          detailUpvoteBtn.classList.add('voted');
        } else if (data.userVote === -1) {
          detailDownvoteBtn.classList.add('voted');
        }
        
        console.log(`Updated detail page vote buttons, userVote: ${data.userVote}`);
      }
    }
  })
  .catch(error => {
    console.error('Error updating vote UI:', error);
  });
}

// Function để load vote status cho tất cả documents trên trang
function loadAllVoteStatuses() {
  // Tìm tất cả vote buttons trên trang
  const voteButtons = document.querySelectorAll('.upvote-btn, .downvote-btn');
  const docIds = new Set();
  
  voteButtons.forEach(button => {
    const docId = button.getAttribute('data-id') || 
                  button.getAttribute('data-docid') || 
                  button.closest('[data-doc-id]')?.getAttribute('data-doc-id') ||
                  button.closest('[data-docid]')?.getAttribute('data-docid') ||
                  button.closest('.document-item')?.getAttribute('data-id');
    
    if (docId) {
      docIds.add(docId);
    }
  });
  
  // Load vote status cho từng document
  docIds.forEach(docId => {
    updateVoteUI(docId);
  });
}

// Initialize vote system
function initVoteSystem() {
  // Add event listeners cho tất cả vote buttons
  document.addEventListener('click', function(e) {
    if (e.target.closest('.upvote-btn')) {
      e.preventDefault();
      const button = e.target.closest('.upvote-btn');
      const docId = getDocumentId(button);
      
      if (docId) {
        voteDocument(docId, 'upvote');
      } else {
        console.error('Cannot find document ID for upvote button');
      }
    }
    
    if (e.target.closest('.downvote-btn')) {
      e.preventDefault();
      const button = e.target.closest('.downvote-btn');
      const docId = getDocumentId(button);
      
      if (docId) {
        voteDocument(docId, 'downvote');
      } else {
        console.error('Cannot find document ID for downvote button');
      }
    }
  });
  
  // Load vote statuses khi trang load xong
  loadAllVoteStatuses();
}

// Helper function để lấy document ID từ button
function getDocumentId(button) {
  return button.getAttribute('data-id') || 
         button.getAttribute('data-docid') || 
         button.closest('[data-doc-id]')?.getAttribute('data-doc-id') ||
         button.closest('[data-docid]')?.getAttribute('data-docid') ||
         button.closest('.document-item')?.getAttribute('data-id') ||
         button.closest('.document-card')?.getAttribute('data-id') ||
         // For document detail page
         (window.location.pathname.includes('/detail/') ? 
          window.location.pathname.split('/').pop() : null);
}

// Default notification function (can be overridden by page-specific implementations)
function showNotification(message, type = 'info') {
  // Check if page has custom showNotification function
  if (window.pageShowNotification && typeof window.pageShowNotification === 'function') {
    window.pageShowNotification(message, type);
    return;
  }
  
  // Fallback notification
  const notification = document.createElement('div');
  notification.className = `notification notification-${type}`;
  notification.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    background: ${type === 'success' ? '#4CAF50' : type === 'error' ? '#f44336' : '#2196F3'};
    color: white;
    padding: 12px 20px;
    border-radius: 4px;
    z-index: 10000;
    max-width: 300px;
    box-shadow: 0 2px 5px rgba(0,0,0,0.2);
  `;
  notification.textContent = message;
  
  document.body.appendChild(notification);
  
  // Auto remove after 3 seconds
  setTimeout(() => {
    if (notification.parentNode) {
      notification.parentNode.removeChild(notification);
    }
  }, 3000);
}

// Initialize when DOM is ready
document.addEventListener('DOMContentLoaded', initVoteSystem); 