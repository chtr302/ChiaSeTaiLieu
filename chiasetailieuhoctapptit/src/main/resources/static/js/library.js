document.addEventListener('DOMContentLoaded', () => {
    // Check if coming from profile click
    checkProfileNavigation();

    // Initialize library popup
    initLibraryPopup();

    // Initialize recents dropdown
    initRecentsDropdown();
    
    // Mobile menu toggle
    initMobileMenu();

    // Tab functionality
    const tabButtons = document.querySelectorAll('.tab-button'); 

    tabButtons.forEach(tab => {
        tab.addEventListener('click', () => {
            // Remove 'active' class from all tabs
            tabButtons.forEach(t => t.classList.remove('active'));

            // Add 'active' class to the clicked tab
            tab.classList.add('active');
            
            // Show corresponding content
            const targetId = tab.getAttribute('data-tab');
            const tabContents = document.querySelectorAll('.tab-content');
            tabContents.forEach(content => {
                content.classList.remove('active');
                content.style.display = 'none';
            });
            
            const targetContent = document.getElementById(targetId);
            if (targetContent) {
                targetContent.classList.add('active');
                targetContent.style.display = 'block';
            }
        });
    });
    
    // Initialize filters for My Library section
    initMyLibraryFilters();
    
    // Initialize filters for Saved Documents section
    initSavedDocumentsFilters();
    
});

// Check if navigation came from profile click
function checkProfileNavigation() {
    // Check URL parameters or sessionStorage
    const fromProfile = new URLSearchParams(window.location.search).get('from') === 'profile' 
                       || sessionStorage.getItem('fromProfile') === 'true';
    
    if (fromProfile) {
        // Show popup
        const libraryPopup = document.getElementById('library-popup');
        if (libraryPopup) {
            libraryPopup.classList.add('show');
            
            // Clear the flag
            sessionStorage.removeItem('fromProfile');
        }
    }
}

// Initialize library popup
function initLibraryPopup() {
    const libraryPopup = document.getElementById('library-popup');
    const closePopupBtn = document.getElementById('close-library-popup');
    
    if (!libraryPopup || !closePopupBtn) return;
    
    // Close popup when clicking the close button
    closePopupBtn.addEventListener('click', () => {
        libraryPopup.classList.remove('show');
    });
    
    // Close popup when clicking outside
    document.addEventListener('click', (e) => {
        if (libraryPopup.classList.contains('show') && 
            !e.target.closest('#library-popup') && 
            !e.target.closest('#user-profile')) {
            libraryPopup.classList.remove('show');
        }
    });
    
    // Handle upload button in popup
    const uploadBtn = libraryPopup.querySelector('.btn-primary');
    if (uploadBtn) {
        uploadBtn.addEventListener('click', () => {
            const modalUploadBtn = document.getElementById('upload-btn');
            if (modalUploadBtn) {
                modalUploadBtn.click();
                libraryPopup.classList.remove('show');
            }
        });
    }
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

// Initialize filters for My Library section
function initMyLibraryFilters() {
    const interactionFilter = document.getElementById('my-library-interaction-filter');
    
    if (!interactionFilter) return;
    
    interactionFilter.addEventListener('change', function() {
        const sortValue = this.value;
        window.location.href = `/library?myLibrarySort=${sortValue}`;
    });
}

// Initialize filters for Saved Documents section
function initSavedDocumentsFilters() {
    const interactionFilter = document.getElementById('saved-documents-interaction-filter');
    const timeFilter = document.getElementById('saved-documents-time-filter');
    
    if (!interactionFilter || !timeFilter) return;
    
    // Function to update URL with filter values
    const applyFilters = () => {
        // Get current values from URL
        const searchParams = new URLSearchParams(window.location.search);
        const myLibrarySort = searchParams.get('myLibrarySort') || 'newest';
        
        // Get current filter values
        const interactionValue = interactionFilter.value;
        const timeValue = timeFilter.value;
        
        // Determine which filter to use (interaction takes precedence)
        const savedSort = interactionValue !== 'newest' && interactionValue !== 'oldest' ? 
                          interactionValue : timeValue;
        
        // Update URL
        window.location.href = `/library?myLibrarySort=${myLibrarySort}&savedSort=${savedSort}`;
    };
    
    // Apply filters when changed
    interactionFilter.addEventListener('change', function() {
        // If interaction filter is not time-based, use it and reset time filter
        if (this.value !== 'newest' && this.value !== 'oldest') {
            applyFilters();
        }
    });
    
    timeFilter.addEventListener('change', function() {
        // If interaction filter is time-based or empty, use time filter
        if (interactionFilter.value === 'newest' || interactionFilter.value === 'oldest') {
            applyFilters();
        }
    });
}


// Initialize the upload button in empty state
function initEmptyStateUploadButton() {
    const uploadBtnEmpty = document.getElementById('upload-btn-empty');
    if (!uploadBtnEmpty) return;
    
    uploadBtnEmpty.addEventListener('click', () => {
        const modalUploadBtn = document.getElementById('upload-btn');
        if (modalUploadBtn) {
            modalUploadBtn.click();
        }
    });
}