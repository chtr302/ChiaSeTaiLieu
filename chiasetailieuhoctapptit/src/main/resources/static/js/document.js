document.addEventListener('DOMContentLoaded', function() {
    console.log('Document.js loaded');
    
    // Modal upload functionality
    const uploadBtn = document.getElementById('upload-btn');
    const uploadModal = document.getElementById('upload-modal');
    const closeModal = document.getElementById('close-modal');
    const cancelUpload = document.getElementById('cancel-upload');
    const fileInput = document.getElementById('file-input');
    const filePreview = document.getElementById('file-preview');
    const uploadForm = document.getElementById('upload-form');

    // Kiểm tra xem các phần tử có tồn tại không để tránh lỗi
    if (uploadBtn) {
        uploadBtn.addEventListener('click', function() {
            console.log('Upload button clicked');
            if (uploadModal) {
                uploadModal.style.display = 'flex';
            }
        });
    }
    
    if (closeModal) {
        closeModal.addEventListener('click', function() {
            console.log('Close modal clicked');
            if (uploadModal) {
                uploadModal.style.display = 'none';
            }
        });
    }
    
    if (cancelUpload) {
        cancelUpload.addEventListener('click', function() {
            console.log('Cancel upload clicked');
            if (uploadModal) {
                uploadModal.style.display = 'none';
            }
        });
    }

    // Tab switching functionality
    const tabButtons = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');
    
    tabButtons.forEach(button => {
        button.addEventListener('click', function() {
            const target = this.dataset.target;
            
            // Toggle active classes
            tabButtons.forEach(btn => btn.classList.remove('active'));
            tabContents.forEach(content => content.classList.add('hidden'));
            
            this.classList.add('active');
            document.getElementById(target).classList.remove('hidden');
        });
    });

    // Menu toggle for responsive sidebar
    const menuToggle = document.getElementById('menu-toggle');
    const sidebar = document.getElementById('sidebar');
    
    if (menuToggle && sidebar) {
        menuToggle.addEventListener('click', function() {
            sidebar.classList.toggle('active');
        });
    }

    // Display notification from server
    function showNotification(message, type) {
        console.log(`Showing ${type} notification: ${message}`);
        
        // Create notification element
        const notification = document.createElement('div');
        notification.className = `notification ${type}`;
        notification.style.position = 'fixed';
        notification.style.top = '20px';
        notification.style.right = '20px';
        notification.style.padding = '15px 20px';
        notification.style.backgroundColor = type === 'success' ? '#10b981' : '#ef4444';
        notification.style.color = 'white';
        notification.style.borderRadius = '6px';
        notification.style.boxShadow = '0 4px 6px rgba(0, 0, 0, 0.1)';
        notification.style.zIndex = '1000';
        notification.style.opacity = '0';
        notification.style.transition = 'opacity 0.3s';
        
        notification.innerHTML = message;
        
        document.body.appendChild(notification);
        
        // Show notification
        setTimeout(() => {
            notification.style.opacity = '1';
        }, 10);
        
        // Hide notification after 5 seconds
        setTimeout(() => {
            notification.style.opacity = '0';
            setTimeout(() => {
                if (document.body.contains(notification)) {
                    document.body.removeChild(notification);
                }
            }, 300);
        }, 5000);
    }

    // Check for success or error messages
    const successMessage = document.getElementById('successMessage');
    const errorMessage = document.getElementById('errorMessage');
    
    if (successMessage && successMessage.textContent) {
        showNotification(successMessage.textContent, 'success');
    }
    
    if (errorMessage && errorMessage.textContent) {
        showNotification(errorMessage.textContent, 'error');
    }
    
    // File upload handling
    if (fileInput) {
        fileInput.addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                // Validate file type
                if (file.type !== 'application/pdf') {
                    alert('Chỉ chấp nhận file PDF');
                    fileInput.value = '';
                    return;
                }
                
                // Validate file size (max 50MB)
                if (file.size > 50 * 1024 * 1024) {
                    alert('Kích thước file không được vượt quá 50MB');
                    fileInput.value = '';
                    return;
                }
                
                // Show file preview
                if (filePreview) {
                    filePreview.innerHTML = `
                        <div class="file-details">
                            <div class="file-icon"><i class="fas fa-file-pdf"></i></div>
                            <div class="file-info">
                                <p class="file-name">${file.name}</p>
                                <p class="file-size">${formatFileSize(file.size)}</p>
                            </div>
                            <button class="remove-file"><i class="fas fa-times"></i></button>
                        </div>
                    `;
                    
                    // Add event listener to remove button
                    const removeBtn = filePreview.querySelector('.remove-file');
                    if (removeBtn) {
                        removeBtn.addEventListener('click', function(e) {
                            e.preventDefault();
                            fileInput.value = '';
                            filePreview.innerHTML = '';
                        });
                    }
                }
            }
        });
    }
    
    // Format file size
    function formatFileSize(bytes) {
        if (bytes < 1024) return bytes + ' bytes';
        else if (bytes < 1048576) return (bytes / 1024).toFixed(2) + ' KB';
        else return (bytes / 1048576).toFixed(2) + ' MB';
    }

    // Fixes specifically for the upload modal issue
    if (uploadBtn && uploadModal) {
        uploadBtn.onclick = function() {
            console.log("Upload button clicked via onclick");
            uploadModal.style.display = 'flex';
        };
    }
});

// Add global click handler to debug
document.addEventListener('click', function(e) {
    if (e.target.id === 'upload-btn' || e.target.closest('#upload-btn')) {
        console.log('Upload button clicked via global handler');
        const modal = document.getElementById('upload-modal');
        if (modal) {
            modal.style.display = 'flex';
        }
    }
});