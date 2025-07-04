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
    
    // Initialize course item click events
    initCourseItemClickEvents();

    // Initialize category item click events
    initCategoryItemClickEvents();

    // Initialize recents dropdown
    initRecentsDropdown();

    // Mobile menu toggle
    initMobileMenu();

    // Keep sidebar and header fixed during scroll
    initFixedElements();
    
    // Handle profile link click
    initProfileLink();

    // Khởi tạo toggle follow cho course
    initCourseFollowButtons();
});

// Handle profile link click to set a flag for the library page
function initProfileLink() {
    const profileLink = document.querySelector('.user-profile-link');
    if (!profileLink) return;
    
    profileLink.addEventListener('click', function(e) {
        // Set a session storage flag to inform the library page
        // that navigation is coming from the profile link
        sessionStorage.setItem('fromProfile', 'true');
    });
}

// Initialize recents dropdown
function initRecentsDropdown() {
    const recentsDropdown = document.getElementById('recents-dropdown');
    const recentsContent = document.getElementById('recents-dropdown-content');
    
    if (!recentsDropdown || !recentsContent) return;
    
    recentsDropdown.addEventListener('click', function(e) {
        if (e.target.closest('.dropdown-content')) return;
        
        // Toggle dropdown
        this.classList.toggle('open');
        
        // Load recent items if dropdown is open and empty
        if (this.classList.contains('open') && recentsContent.children.length === 0) {
            loadRecentItems();
        }
    });
}

// Load recent items (from local storage or could be from API)
function loadRecentItems() {
    const recentsContent = document.getElementById('recents-dropdown-content');
    if (!recentsContent) return;
    
    // Example recent items - this would be loaded from backend in a real app
    const recentItems = [
        { 
            type: 'document', 
            title: 'Introduction to Data Structures', 
            time: 'Today, 10:45 AM',
            icon: 'fa-file-pdf',
            url: '/documents/detail/1'
        },
        { 
            type: 'course', 
            title: 'Operating Systems', 
            time: 'Yesterday, 3:20 PM',
            icon: 'fa-book',
            url: '/documents/course/2'
        },
        { 
            type: 'document', 
            title: 'Algorithm Analysis Notes', 
            time: 'May 15, 2023',
            icon: 'fa-file-alt',
            url: '/documents/detail/3'
        }
    ];
    
    // Clear any existing content
    recentsContent.innerHTML = '';
    
    // Add recent items
    if (recentItems.length === 0) {
        recentsContent.innerHTML = '<div class="empty-recents">No recent activity</div>';
    } else {
        recentItems.forEach(item => {
            const recentItemEl = document.createElement('div');
            recentItemEl.className = 'recent-item';
            recentItemEl.innerHTML = `
                <div class="recent-item-icon">
                    <i class="fas ${item.icon}"></i>
                </div>
                <div class="recent-item-details">
                    <a href="${item.url}" class="recent-item-title">${item.title}</a>
                    <div class="recent-item-time">${item.time}</div>
                </div>
            `;
            recentsContent.appendChild(recentItemEl);
        });
    }
}

// Mobile menu toggle
function initMobileMenu() {
    const menuToggle = document.getElementById('menu-toggle');
    const sidebar = document.getElementById('sidebar');
    
    if (!menuToggle || !sidebar) return;
    
    menuToggle.addEventListener('click', function() {
        sidebar.classList.toggle('active');
        document.body.classList.toggle('sidebar-open');
    });
    
    // Close sidebar when clicking outside
    document.addEventListener('click', function(e) {
        if (sidebar.classList.contains('active') && 
            !e.target.closest('#sidebar') && 
            !e.target.closest('#menu-toggle')) {
            sidebar.classList.remove('active');
            document.body.classList.remove('sidebar-open');
        }
    });
}

// Keep sidebar and header fixed during scroll
function initFixedElements() {
    const sidebar = document.getElementById('sidebar');
    const contentHeader = document.querySelector('.content-header');
    
    if (!sidebar || !contentHeader) return;
    
    window.addEventListener('scroll', function() {
        const scrollTop = window.scrollY;
        
        // Nothing to adjust here since we've set them as fixed in CSS
        // This function could be used to add/remove classes based on scroll position if needed
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

    // Prevent click propagation and only trigger once
    dropZone.addEventListener('click', (e) => {
        if (e.target === fileInput || fileInput.contains(e.target)) return;
        if (e.target.closest('.remove-file')) return;
        e.preventDefault();
        fileInput.click();
    });

    fileInput.addEventListener('change', handleFileSelection);

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
            fileInput.files = files;
            handleFileSelection();   
        }
    }, false);
    
    // Handle file selection (for both drag-drop and input click)
    function handleFileSelection() {
        const fileInput = document.getElementById('file-input');
        const uploadDefault = document.querySelector('.upload-default');
        const filePreview = document.getElementById('file-preview');
        const titleInput = document.getElementById('tieuDe'); // Lấy trường "Title"
    
        if (fileInput.files && fileInput.files[0]) {
            const file = fileInput.files[0];
    
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
    
            // Set file name (without extension) to the title input
            const fileNameWithoutExtension = file.name.replace(/\.[^/.]+$/, ''); // Loại bỏ phần mở rộng
            if (titleInput) {
                titleInput.value = fileNameWithoutExtension; // Gán tên file vào trường "Title"
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
        // 
    });
}

function formatFileSize(bytes) {
    if (bytes < 1024) return bytes + ' B';
    else if (bytes < 1048576) return (bytes / 1024).toFixed(1) + ' KB';
    else return (bytes / 1048576).toFixed(1) + ' MB';
}

// Display notification
function showNotification(message, type = 'info') {
    let iconClass = 'fa-info-circle';
    let notificationType = type;

    if (message === 'Đã theo dõi môn học!' || message === "Đã theo dõi môn học") {
        notificationType = 'success';
        iconClass = 'fa-check-circle';
    } else if (message === 'Đã bỏ theo dõi môn học.' || message === "Đã bỏ theo dõi môn học") {
        notificationType = 'info';
        iconClass = 'fa-info-circle';
    } else if (message === 'Bạn đã theo dõi môn học này!' || message === "Bạn đã theo dõi môn học này") {
        notificationType = 'info';
        iconClass = 'fa-info-circle';
    } else if (type === 'success') {
        iconClass = 'fa-check-circle';
    } else if (type === 'error') {
        iconClass = 'fa-exclamation-circle';
    }

    const notification = document.createElement('div');
    notification.className = `notification ${notificationType}`;
    notification.innerHTML = `
        <span class="notification-icon">
            <i class="fas ${iconClass}"></i>
        </span>
        <span class="notification-message">${message}</span>
        <button class="notification-close" aria-label="Đóng">
            <i class="fas fa-times"></i>
        </button>
    `;

    document.body.appendChild(notification);

    // Show notification with animation
    setTimeout(() => {
        notification.classList.add('show');
    }, 10);

    // Auto-hide notification after 1 seconds
    const hideTimeout = setTimeout(() => {
        hideNotification(notification);
    }, 1000);

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

// Initialize course item click events
function initCourseItemClickEvents() {
    const courseItems = document.querySelectorAll('.course-item');
    courseItems.forEach(item => {
        item.addEventListener('click', (event) => {
            // If the user clicked on the link, let the default action happen
            if (event.target.closest('.course-link')) {
                return;
            }
            
            // Otherwise, simulate clicking the link
            const courseLink = item.querySelector('.course-link');
            if (courseLink) {
                courseLink.click();
            }
        });
    });
}

// Initialize category item click events
function initCategoryItemClickEvents() {
    const categoryItems = document.querySelectorAll('.category-item');
    categoryItems.forEach(item => {
        item.addEventListener('click', (event) => {
            // If the user clicked on the link, let the default action happen
            if (event.target.closest('.category-link')) {
                return;
            }
            
            // Otherwise, simulate clicking the link
            const categoryLink = item.querySelector('.category-link');
            if (categoryLink) {
                categoryLink.click();
            }
        });
    });
}

// Khởi tạo toggle follow cho course
function initCourseFollowButtons() {
    document.querySelectorAll('.course-item').forEach(function(courseItem) {
        const courseId = courseItem.getAttribute('data-course-id');
        const followBtn = courseItem.querySelector('.follow-btn');
        const followingBtn = courseItem.querySelector('.following-btn');
        if (!courseId || !followBtn || !followingBtn) return;

        // Check follow status on load
        fetch('/api/follow-course/is-following?maMon=' + encodeURIComponent(courseId))
            .then(res => res.json())
            .then(data => {
                if (data.following) {
                    followBtn.style.display = 'none';
                    followingBtn.style.display = '';
                    setFollowingBtnBehavior(followBtn, followingBtn, courseId);
                } else {
                    followBtn.style.display = '';
                    followingBtn.style.display = 'none';
                    setFollowBtnBehavior(followBtn, followingBtn, courseId);
                }
            });

        // Always set up both behaviors in case of dynamic UI change
        setFollowBtnBehavior(followBtn, followingBtn, courseId);
        setFollowingBtnBehavior(followBtn, followingBtn, courseId);
    });

    // For library page: handle unfollow in followed courses section
    document.querySelectorAll('#courses-following .following-btn').forEach(function(followingBtn) {
        const courseContent = followingBtn.closest('.course-content');
        const courseId = courseContent ? courseContent.getAttribute('data-course-id') : null;
        if (!courseId) return;

        followingBtn.addEventListener('mouseenter', function() {
            this.querySelector('.btn-text').textContent = 'Unfollow';
            this.classList.add('unfollow-hover');
        });
        followingBtn.addEventListener('mouseleave', function() {
            this.querySelector('.btn-text').textContent = 'Following';
            this.classList.remove('unfollow-hover');
        });

        followingBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            fetch('/api/follow-course/unfollow?maMon=' + encodeURIComponent(courseId), {
                method: 'POST',
                headers: {
                    ...getCsrfHeaders(),
                    'Accept': 'application/json'
                }
            })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    const card = followingBtn.closest('.document-card');
                    if (card) card.remove();
                    showNotification('Đã bỏ theo dõi môn học.', 'info');
                }
            });
        });
    });
}

// Helper: set behavior for follow button
function setFollowBtnBehavior(followBtn, followingBtn, courseId) {
    followBtn.onclick = function(e) {
        e.stopPropagation();
        fetch('/api/follow-course/follow?maMon=' + encodeURIComponent(courseId), {
            method: 'POST',
            headers: getCsrfHeaders()
        })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                followBtn.style.display = 'none';
                followingBtn.style.display = '';
                followingBtn.querySelector('.btn-text').textContent = 'Following';
                showNotification('Đã theo dõi môn học!', 'success');
            } else {
                showNotification('Bạn đã theo dõi môn học này!', 'info');
            }
        });
    };
}

// Helper: set behavior for following button (hover to unfollow)
function setFollowingBtnBehavior(followBtn, followingBtn, courseId) {
    followingBtn.onmouseenter = function() {
        this.querySelector('.btn-text').textContent = 'Unfollow';
        this.classList.add('unfollow-hover');
    };
    followingBtn.onmouseleave = function() {
        this.querySelector('.btn-text').textContent = 'Following';
        this.classList.remove('unfollow-hover');
    };
    followingBtn.onclick = function(e) {
        e.stopPropagation();
        // Only unfollow if currently showing "Unfollow"
        if (this.classList.contains('unfollow-hover')) {
            fetch('/api/follow-course/unfollow?maMon=' + encodeURIComponent(courseId), {
                method: 'POST',
                headers: getCsrfHeaders()
            })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    followBtn.style.display = '';
                    followingBtn.style.display = 'none';
                    showNotification('Đã bỏ theo dõi môn học.', 'info');
                }
            });
        }
    };
}

function getCsrfHeaders() {
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
    if (csrfToken && csrfHeader) {
        return { [csrfHeader]: csrfToken };
    }
    return {};
}