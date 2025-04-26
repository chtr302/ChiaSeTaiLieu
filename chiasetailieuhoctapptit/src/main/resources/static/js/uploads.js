document.addEventListener('DOMContentLoaded', function() {
    console.log('Upload.js loaded and running');
    
    // Các phần tử DOM chính
    const uploadButton = document.getElementById('upload-btn');
    const modalUpload = document.getElementById('upload-modal');
    const closeModalBtn = document.getElementById('close-modal');
    const cancelUploadBtn = document.getElementById('cancel-upload');
    const fileInput = document.getElementById('file-input');
    const filePreview = document.getElementById('file-preview');
    const uploadForm = document.getElementById('upload-form');
    
    console.log('Upload button element:', uploadButton);
    console.log('Modal element:', modalUpload);

    // Hiển thị modal upload
    if (uploadButton) {
        uploadButton.addEventListener('click', function(e) {
            e.preventDefault();
            console.log('Upload button clicked');
            
            if (modalUpload) {
                modalUpload.style.display = 'flex';
                console.log('Modal display style set to flex');
            } else {
                console.error('Modal element not found!');
            }
            
            return false;
        });
    } else {
        console.error('Upload button not found!');
    }
    
    // Đóng modal
    function closeModal() {
        if (modalUpload) {
            modalUpload.style.display = 'none';
            console.log('Modal closed');
        }
    }
    
    // Xử lý đóng modal
    if (closeModalBtn) {
        closeModalBtn.addEventListener('click', closeModal);
    }
    
    if (cancelUploadBtn) {
        cancelUploadBtn.addEventListener('click', closeModal);
    }
    
    // FIX: Kết nối sự kiện click cho vùng upload
    const uploadArea = document.querySelector('.file-upload');
    if (uploadArea && fileInput) {
        console.log('Setting up click handler for upload area');
        
        // Đảm bảo file input có position và kích thước phù hợp
        fileInput.style.position = 'absolute';
        fileInput.style.top = '0';
        fileInput.style.left = '0';
        fileInput.style.width = '100%';
        fileInput.style.height = '100%';
        fileInput.style.opacity = '0';
        fileInput.style.cursor = 'pointer';
        fileInput.style.zIndex = '10';
        
        // Đảm bảo upload area có position relative
        uploadArea.style.position = 'relative';
        uploadArea.style.cursor = 'pointer';
        
        // Thêm sự kiện click cho cả khu vực upload
        uploadArea.addEventListener('click', function(e) {
            console.log('Upload area clicked');
            // Nếu click vào nút xóa thì không mở file chooser
            if (e.target.closest('.remove-file')) {
                return;
            }
            fileInput.click();
        });
        
        // Highlight khi kéo file vào
        uploadArea.addEventListener('dragover', function(e) {
            e.preventDefault();
            uploadArea.classList.add('highlight');
        });
        
        uploadArea.addEventListener('dragleave', function() {
            uploadArea.classList.remove('highlight');
        });
        
        uploadArea.addEventListener('drop', function(e) {
            e.preventDefault();
            uploadArea.classList.remove('highlight');
            
            if (e.dataTransfer.files.length) {
                fileInput.files = e.dataTransfer.files;
                // Kích hoạt sự kiện change
                const event = new Event('change', { bubbles: true });
                fileInput.dispatchEvent(event);
            }
        });
    }
    
    // Xử lý khi người dùng chọn file
    if (fileInput) {
        fileInput.addEventListener('change', function(e) {
            const file = e.target.files[0];
            console.log('File selected:', file);
            
            if (file) {
                // Chỉ chấp nhận PDF
                if (file.type !== 'application/pdf') {
                    alert('Chỉ chấp nhận file PDF');
                    fileInput.value = '';
                    return;
                }
                
                // Giới hạn kích thước (50MB)
                if (file.size > 50 * 1024 * 1024) {
                    alert('Kích thước file không được vượt quá 50MB');
                    fileInput.value = '';
                    return;
                }
                
                // Hiển thị preview file
                if (filePreview) {
                    console.log('Showing file preview');
                    filePreview.style.display = 'block';
                    // Ẩn icon và text mặc định
                    const uploadDefault = document.querySelector('.upload-default');
                    if (uploadDefault) {
                        uploadDefault.style.display = 'none';
                    }
                    
                    filePreview.innerHTML = `
                        <div class="file-details">
                            <div class="file-icon"><i class="fas fa-file-pdf"></i></div>
                            <div class="file-info">
                                <p class="file-name">${file.name}</p>
                                <p class="file-size">${formatFileSize(file.size)}</p>
                            </div>
                            <button type="button" class="remove-file"><i class="fas fa-times"></i></button>
                        </div>
                    `;
                    
                    // Cập nhật tiêu đề từ tên file
                    const titleInput = document.querySelector('input[name="tieuDe"]');
                    if (titleInput) {
                        titleInput.value = file.name.replace('.pdf', '');
                    }
                    
                    // Xử lý nút xóa file
                    const removeBtn = filePreview.querySelector('.remove-file');
                    if (removeBtn) {
                        removeBtn.addEventListener('click', function(e) {
                            e.preventDefault();
                            e.stopPropagation(); // Ngăn không cho kích hoạt sự kiện click của upload area
                            fileInput.value = '';
                            filePreview.innerHTML = '';
                            filePreview.style.display = 'none';
                            // Hiện lại icon và text mặc định
                            if (uploadDefault) {
                                uploadDefault.style.display = 'block';
                            }
                            if (titleInput) {
                                titleInput.value = '';
                            }
                        });
                    }
                }
            }
        });
    } else {
        console.error('File input element not found!');
    }
    
    // Format kích thước file
    function formatFileSize(bytes) {
        if (bytes < 1024) return bytes + ' bytes';
        else if (bytes < 1048576) return (bytes / 1024).toFixed(2) + ' KB';
        else return (bytes / 1048576).toFixed(2) + ' MB';
    }
    
    // Kiểm tra form trước khi submit
    if (uploadForm) {
        uploadForm.addEventListener('submit', function(e) {
            console.log('Form submitted');
            
            // Kiểm tra file đã chọn chưa
            if (!fileInput || !fileInput.files[0]) {
                e.preventDefault();
                alert('Vui lòng chọn file PDF để tải lên');
                return false;
            }
            
            // Form hợp lệ, hiển thị loading
            const submitBtn = document.getElementById('save-upload');
            if (submitBtn) {
                submitBtn.disabled = true;
                submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang tải...';
            }
            
            return true;
        });
    }
});