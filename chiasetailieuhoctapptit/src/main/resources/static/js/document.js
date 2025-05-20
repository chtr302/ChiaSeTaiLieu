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

// Xử lý enter cho thanh tìm kiếm phụ
function handleDocumentSearchInput(e) {
  if (e.key === 'Enter') {
    const value = e.target.value.trim();
    if (value) {
      window.location.href = '/documents?q=' + encodeURIComponent(value);
    }
  }
}
const secondaryInput = document.getElementById('secondaryDocumentSearch');
if (secondaryInput) {
  secondaryInput.addEventListener('keydown', handleDocumentSearchInput);
}