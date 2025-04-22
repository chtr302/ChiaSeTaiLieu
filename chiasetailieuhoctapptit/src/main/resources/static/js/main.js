document.addEventListener('DOMContentLoaded', function() {
  "use strict";
  
  /**
   * Modal Upload Functionality
   */
  const uploadBtn = document.getElementById('upload-btn');
  const uploadModal = document.getElementById('upload-modal');
  const closeModal = document.getElementById('close-modal');
  const cancelUpload = document.getElementById('cancel-upload');
  
  if (uploadBtn && uploadModal) {
    uploadBtn.addEventListener('click', function() {
      console.log('Upload button clicked');
      uploadModal.style.display = 'flex';
    });
  }
  
  if (closeModal && uploadModal) {
    closeModal.addEventListener('click', function() {
      uploadModal.style.display = 'none';
    });
  }
  
  if (cancelUpload && uploadModal) {
    cancelUpload.addEventListener('click', function() {
      uploadModal.style.display = 'none';
    });
  }
  
  // Close modal when clicking outside
  window.addEventListener('click', function(event) {
    if (event.target === uploadModal) {
      uploadModal.style.display = 'none';
    }
  });
  
  /**
   * File upload interaction
   */
  const fileUpload = document.querySelector('.file-upload');
  const fileInput = document.getElementById('file-input');
  
  if (fileUpload && fileInput) {
    fileUpload.addEventListener('click', function() {
      fileInput.click();
    });
    
    fileInput.addEventListener('change', function(e) {
      if (e.target.files.length > 0) {
        console.log('Files selected:', e.target.files);
        // Hiển thị tên file đã chọn
        const fileName = e.target.files[0].name;
        const uploadTitle = fileUpload.querySelector('.upload-title');
        if (uploadTitle) {
          uploadTitle.textContent = fileName;
        }
      }
    });
    
    // Drag and drop support
    fileUpload.addEventListener('dragover', function(e) {
      e.preventDefault();
      this.style.borderColor = '#3b82f6';
      this.style.backgroundColor = '#eff6ff';
    });
    
    fileUpload.addEventListener('dragleave', function(e) {
      e.preventDefault();
      this.style.borderColor = '#d1d5db';
      this.style.backgroundColor = 'transparent';
    });
    
    fileUpload.addEventListener('drop', function(e) {
      e.preventDefault();
      this.style.borderColor = '#d1d5db';
      this.style.backgroundColor = 'transparent';
      
      if (e.dataTransfer.files.length > 0) {
        fileInput.files = e.dataTransfer.files;
        const fileName = e.dataTransfer.files[0].name;
        const uploadTitle = fileUpload.querySelector('.upload-title');
        if (uploadTitle) {
          uploadTitle.textContent = fileName;
        }
        console.log('Files dropped:', e.dataTransfer.files);
      }
    });
  }
  
  /**
   * Course cards functionality
   */
  document.querySelectorAll('.course-card').forEach(card => {
    card.addEventListener('click', function() {
      const courseName = this.querySelector('.course-title').textContent.trim();
      window.location.href = `/documents?course=${encodeURIComponent(courseName)}`;
    });
  });
  
  /**
   * Document voting functionality 
   */
  document.querySelectorAll('.upvote-btn').forEach(button => {
    button.addEventListener('click', function() {
      const voteCount = this.parentElement.querySelector('.vote-count');
      voteCount.textContent = parseInt(voteCount.textContent) + 1;
    });
  });
  
  document.querySelectorAll('.downvote-btn').forEach(button => {
    button.addEventListener('click', function() {
      const voteCount = this.parentElement.querySelector('.vote-count');
      voteCount.textContent = parseInt(voteCount.textContent) - 1;
    });
  });
  
  /**
   * Mobile menu toggle for sidebar
   */
  const menuToggle = document.getElementById('menu-toggle');
  const sidebar = document.getElementById('sidebar');
  
  if (menuToggle && sidebar) {
    menuToggle.addEventListener('click', function() {
      if (sidebar.style.display === 'none' || sidebar.style.display === '') {
        sidebar.style.display = 'block';
      } else {
        sidebar.style.display = 'none';
      }
    });
    
    // Hide sidebar on small screens by default
    function adjustSidebar() {
      if (window.innerWidth <= 768) {
        sidebar.style.display = 'none';
      } else {
        sidebar.style.display = 'flex';
      }
    }
    
    window.addEventListener('resize', adjustSidebar);
    adjustSidebar(); // Run on page load
  }
});