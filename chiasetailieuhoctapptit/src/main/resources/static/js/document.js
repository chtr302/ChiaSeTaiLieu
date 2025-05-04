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

    // Initialize pagination on page load
    renderPage(currentPage);

    // Initialize 'By Course' tab functionality
    initCourseTabFunctionality();
});

// Tab switching functionality
function initTabSwitching() {
    const tabButtons = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');

    tabButtons.forEach(button => {
        button.addEventListener('click', () => {
            // Remove active class from all buttons and tabs
            tabButtons.forEach(btn => btn.classList.remove('active'));
            tabContents.forEach(content => {
                content.classList.remove('active');
                content.classList.add('hidden');
            });

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

// Pagination functionality
let currentPage = 1;
const itemsPerPage = 8;

function renderPage(page) {
    const allDocuments = document.querySelectorAll('.document-card');
    const totalPages = Math.ceil(allDocuments.length / itemsPerPage);

    // Hide all documents
    allDocuments.forEach(doc => doc.style.display = 'none');

    // Show documents for the current page
    const start = (page - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    for (let i = start; i < end && i < allDocuments.length; i++) {
        allDocuments[i].style.display = 'block';
    }

    // Update pagination controls
    updatePaginationControls(totalPages);
}

function updatePaginationControls(totalPages) {
    const pageNumbersContainer = document.getElementById('page-numbers');
    pageNumbersContainer.innerHTML = '';

    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.className = 'page-btn';
        pageButton.textContent = i;
        if (i === currentPage) {
            pageButton.classList.add('active');
        }
        pageButton.addEventListener('click', () => {
            currentPage = i;
            renderPage(currentPage);
        });
        pageNumbersContainer.appendChild(pageButton);
    }
}

function changePage(direction) {
    const allDocuments = document.querySelectorAll('.document-card');
    const totalPages = Math.ceil(allDocuments.length / itemsPerPage);

    if (direction === 'prev' && currentPage > 1) {
        currentPage--;
    } else if (direction === 'next' && currentPage < totalPages) {
        currentPage++;
    }

    renderPage(currentPage);
}

// --- NEW: Functionality for 'By Course' Tab ---
function initCourseTabFunctionality() {
    const coursesTab = document.getElementById('courses'); // The main container for the courses tab
    if (!coursesTab) return; // Don't proceed if the tab doesn't exist

    const coursesList = coursesTab.querySelector('#courses-list');
    const courseDocumentsView = coursesTab.querySelector('#course-documents-view');
    const backToCoursesBtn = coursesTab.querySelector('#back-to-courses');
    const courseTitle = coursesTab.querySelector('#course-title');
    const courseDocumentsContainer = coursesTab.querySelector('#course-documents-container');

    // Check if all necessary elements within the courses tab exist
    if (!coursesList || !courseDocumentsView || !backToCoursesBtn || !courseTitle || !courseDocumentsContainer) {
        console.warn("Elements within the 'By Course' tab (#courses) are missing. Functionality might be limited.");
        return;
    }

    // Event delegation for 'View Documents' buttons within the courses tab
    coursesList.addEventListener('click', function(event) {
        const viewBtn = event.target.closest('.view-course-btn');
        if (viewBtn) {
            const courseId = viewBtn.getAttribute('data-course'); // Get maKhoaHoc
            const courseItem = viewBtn.closest('.course-item');
            const courseNameElement = courseItem ? courseItem.querySelector('h3') : null;
            const courseName = courseNameElement ? courseNameElement.textContent : 'Selected Course';

            // Hide courses list and show documents view WITHIN the courses tab
            coursesList.classList.add('hidden');
            courseDocumentsView.classList.remove('hidden');
            courseTitle.textContent = `${courseName} Documents`;

            // --- Load Documents ---
            fetchDocumentsForCourse(courseId, courseDocumentsContainer);
        }
    });

    // Back to courses button listener within the courses tab
    backToCoursesBtn.addEventListener('click', () => {
        coursesList.classList.remove('hidden');
        courseDocumentsView.classList.add('hidden');
        courseDocumentsContainer.innerHTML = ''; // Clear loaded documents
    });
}

// --- NEW: Fetch documents for a specific course ---
function fetchDocumentsForCourse(courseId, container) {
    container.innerHTML = '<p class="text-gray-500 text-center col-span-full">Loading documents...</p>';

    // Simulate fetching data - replace with actual fetch
    fetch(`/api/courses/${courseId}/documents`) // Replace with your actual API endpoint
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch documents');
            }
            return response.json();
        })
        .then(documents => {
            container.innerHTML = ''; // Clear loading message

            if (documents.length === 0) {
                container.innerHTML = '<p class="text-gray-500 text-center col-span-full">No documents found for this course.</p>';
                return;
            }

            documents.forEach(doc => {
                const card = document.createElement('div');
                card.className = 'document-card bg-white rounded-lg shadow-md overflow-hidden transition-all duration-300 hover:shadow-lg';
                card.innerHTML = `
                    <div class="p-5">
                        <div class="flex justify-between items-start mb-3">
                            <span class="type-badge px-3 py-1 bg-blue-100 text-blue-800 text-xs font-semibold rounded-full">${doc.type}</span>
                            <span class="text-gray-500 text-sm"><i class="fas fa-eye mr-1"></i> ${doc.views}</span>
                        </div>
                        <h3 class="text-lg font-semibold text-gray-800 mb-2">${doc.title}</h3>
                        <p class="text-gray-600 text-sm mb-4">${doc.description}</p>
                        <div class="flex justify-between items-center text-sm text-gray-500">
                            <span><i class="fas fa-university mr-1"></i> ${doc.university}</span>
                            <span>${doc.pages} pages</span>
                        </div>
                    </div>
                    <div class="bg-gray-50 px-5 py-3 flex justify-between items-center">
                        <div class="flex items-center">
                            <img src="/img/default-avatar.png" alt="Professor" class="w-6 h-6 rounded-full mr-2 object-cover"> <span class="text-sm text-gray-700">${doc.professor}</span>
                        </div>
                        <button class="text-blue-600 hover:text-blue-800" title="Download">
                            <i class="fas fa-download"></i>
                        </button>
                    </div>
                `;
                container.appendChild(card);
            });
        })
        .catch(error => {
            console.error(error);
            container.innerHTML = '<p class="text-red-500 text-center col-span-full">Failed to load documents. Please try again later.</p>';
        });
}