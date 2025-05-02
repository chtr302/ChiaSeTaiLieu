document.addEventListener('DOMContentLoaded', function() {
    // Display success/error messages if present
    const successMessage = document.getElementById('successMessage');
    const errorMessage = document.getElementById('errorMessage');
    
    if (successMessage && successMessage.textContent.trim() !== '') {
        showNotification(successMessage.textContent, 'success');
    }
    
    if (errorMessage && errorMessage.textContent.trim() !== '') {
        showNotification(errorMessage.textContent, 'error');
    }
    
    // Tab switching functionality
    initTabSwitching();
    
    // Upload document functionality
    initUploadModal();
    
    // File upload handling with drag and drop
    initFileUploadHandling();
});

// Tab switching functionality
function initTabSwitching() {
    const tabButtons = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');
    
    tabButtons.forEach(button => {
        button.addEventListener('click', () => {
            // Remove active class from all buttons and tabs
            tabButtons.forEach(btn => btn.classList.remove('active'));
            tabContents.forEach(content => content.classList.add('hidden'));
            
            // Add active class to clicked button
            button.classList.add('active');
            
            // Show corresponding tab content
            const targetTab = button.dataset.target;
            const tabContent = document.getElementById(targetTab);
            if (tabContent) {
                tabContent.classList.remove('hidden');
                tabContent.classList.add('active');
            }
        });
    });
}

// Upload modal functionality
function initUploadModal() {
    const uploadBtn = document.getElementById('upload-btn');
    const uploadModal = document.getElementById('upload-modal');
    const closeModalBtn = document.getElementById('close-modal');
    const cancelUploadBtn = document.getElementById('cancel-upload');
    const uploadForm = document.getElementById('upload-form');
    
    // Open modal
    if (uploadBtn) {
        uploadBtn.addEventListener('click', () => {
            uploadModal.style.display = 'flex';
            document.body.style.overflow = 'hidden'; // Prevent scrolling
        });
    }
    
    // Close modal
    const closeModal = () => {
        uploadModal.style.display = 'none';
        document.body.style.overflow = 'auto'; // Enable scrolling
        resetUploadForm();
    };
    
    if (closeModalBtn) closeModalBtn.addEventListener('click', closeModal);
    if (cancelUploadBtn) cancelUploadBtn.addEventListener('click', closeModal);
    
    // Close modal when clicking outside
    uploadModal.addEventListener('click', (event) => {
        if (event.target === uploadModal) {
            closeModal();
        }
    });
}

// File upload handling with drag and drop
function initFileUploadHandling() {
    const fileInput = document.getElementById('file-input');
    const uploadDefault = document.querySelector('.upload-default');
    const filePreview = document.getElementById('file-preview');
    const uploadForm = document.getElementById('upload-form');
    const dropZone = document.querySelector('.file-upload');
    
    if (!fileInput || !uploadDefault || !filePreview || !dropZone) return;

    // Ngăn chặn việc click lan truyền và chỉ trigger một lần
    dropZone.addEventListener('click', (e) => {
        if (e.target === fileInput || fileInput.contains(e.target)) return;
        if (e.target.closest('.remove-file')) return;
        e.preventDefault();
        fileInput.click();
    });

    fileInput.addEventListener('change',handleFileSelection);


    // Handle drag and drop
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
        dropZone.addEventListener(eventName, preventDefaults, false);
    });
    
    function preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
    }
    
    // Highlight drop zone when file is dragged over it
    ['dragenter', 'dragover'].forEach(eventName => {
        dropZone.addEventListener(eventName, () => {
            dropZone.classList.add('highlight');
        }, false);
    });
    
    ['dragleave', 'drop'].forEach(eventName => {
        dropZone.addEventListener(eventName, () => {
            dropZone.classList.remove('highlight');
        }, false);
    });
    
    // Handle dropped files with debounce
    dropZone.addEventListener('drop', (e) => {
        const dt = e.dataTransfer;
        const files = dt.files;
        if (files.length > 0) {
            fileInput.files = files; t
            handleFileSelection();   
        }
    }, false);
    
    // Handle file selection (for both drag-drop and input click)
    function handleFileSelection() {
        const fileInput = document.getElementById('file-input');
        const uploadDefault = document.querySelector('.upload-default');
        const filePreview = document.getElementById('file-preview');
        
        if (fileInput.files && fileInput.files[0]) {
            const file = fileInput.files[0];
            
            // Prevent multiple triggers by checking if preview is already shown
            if (filePreview.style.display === 'block') {
                return;
            }
            
            // Validate file type (PDF only)
            if (file.type !== 'application/pdf') {
                showNotification('Chỉ chấp nhận file PDF!', 'error');
                resetFileInput();
                return;
            }
            
            // Validate file size (max 50MB)
            if (file.size > 50 * 1024 * 1024) {
                showNotification('Kích thước file tối đa là 50MB!', 'error');
                resetFileInput();
                return;
            }
            
            // Show file preview
            uploadDefault.style.display = 'none';
            filePreview.style.display = 'block';
            filePreview.innerHTML = `
                <div class="preview-file">
                    <i class="fas fa-file-pdf"></i>
                    <div class="file-info">
                        <p class="file-name">${file.name}</p>
                        <p class="file-size">${formatFileSize(file.size)}</p>
                    </div>
                    <button type="button" class="remove-file"><i class="fas fa-times"></i></button>
                </div>
            `;
            
            // Add event listener to remove file button once
            const removeButton = document.querySelector('.remove-file');
            if (removeButton) {
                removeButton.addEventListener('click', resetFileInput, { once: true });
            }
        }
    }
    
    // Reset file input
    function resetFileInput() {
        fileInput.value = '';
        uploadDefault.style.display = 'block';
        filePreview.style.display = 'none';
        filePreview.innerHTML = '';
    }
    
    // Reset entire upload form
    function resetUploadForm() {
        uploadForm.reset();
        resetFileInput();
    }
    
    // Handle form submission
    uploadForm.addEventListener('submit', (event) => {
        // Form validation is handled by HTML5 required attributes
        // You can add additional validation here if needed
    });
}

// Helper function to format file size
function formatFileSize(bytes) {
    if (bytes < 1024) return bytes + ' B';
    else if (bytes < 1048576) return (bytes / 1024).toFixed(1) + ' KB';
    else return (bytes / 1048576).toFixed(1) + ' MB';
}

// Display notification
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.innerHTML = `
        <div class="notification-icon">
            <i class="fas ${type === 'success' ? 'fa-check-circle' : 
                           type === 'error' ? 'fa-exclamation-circle' : 'fa-info-circle'}"></i>
        </div>
        <div class="notification-message">${message}</div>
        <button class="notification-close"><i class="fas fa-times"></i></button>
    `;
    
    document.body.appendChild(notification);
    
    // Show notification with animation
    setTimeout(() => {
        notification.classList.add('show');
    }, 10);
    
    // Auto-hide notification after 5 seconds
    const hideTimeout = setTimeout(() => {
        hideNotification(notification);
    }, 5000);
    
    // Add close button functionality
    notification.querySelector('.notification-close').addEventListener('click', () => {
        clearTimeout(hideTimeout);
        hideNotification(notification);
    });
}

// Hide notification
function hideNotification(notification) {
    notification.classList.remove('show');
    notification.classList.add('hide');
    
    // Remove notification from DOM after animation completes
    setTimeout(() => {
        notification.remove();
    }, 300);
}