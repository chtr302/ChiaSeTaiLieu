/**
 * Document saving functionality for ChiaSeTaiLieu
 * This script handles saving and unsaving documents across all pages 
 */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize save buttons
    initSaveButtons();
    
    // Check saved status for all documents on the page
    checkSavedStatusBatch();
    
    // Create notification container once at the beginning
    if (!document.getElementById('notification-container')) {
        const notificationContainer = document.createElement('div');
        notificationContainer.id = 'notification-container';
        document.body.appendChild(notificationContainer);
    }
});

/**
 * Initialize all save buttons on the page
 */
function initSaveButtons() {
    const saveButtons = document.querySelectorAll('.save-btn');
    
    if (!saveButtons.length) return;
    
    // Get CSRF token if available
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
    
    saveButtons.forEach(button => {
        const documentId = button.getAttribute('data-id');
        if (!documentId) return;
        
        button.addEventListener('click', async function() {
            try {
                const response = await fetch(`/documents/save/${documentId}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        ...(csrfToken && csrfHeader ? { [csrfHeader]: csrfToken } : {})
                    }
                });
                
                const data = await response.json();
                
                if (data.success) {
                    // Update button state
                    if (data.saved) {
                        this.classList.add('saved');
                        this.textContent = 'Saved';
                    } else {
                        this.classList.remove('saved');
                        this.textContent = 'Save';
                    }
                    
                    // Show notification
                    showNotification(data.message, data.saved ? 'success' : 'info');
                } else {
                    console.error('Error:', data.message);
                    showNotification(data.message, 'error');
                }
            } catch (error) {
                console.error('Error saving document:', error);
                showNotification('Lỗi khi lưu tài liệu', 'error');
            }
        });
    });
}

/**
 * Check saved status for all documents on the page
 */
function checkSavedStatusBatch() {
    const saveButtons = document.querySelectorAll('.save-btn');
    if (!saveButtons.length) return;
    
    // Collect all document IDs on the page
    const documentIds = Array.from(saveButtons)
        .map(button => button.getAttribute('data-id'))
        .filter(id => id); // Filter out any undefined/null values
    
    if (!documentIds.length) return;
    
    // Get CSRF token if available
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
    
    // Fetch saved status for all documents in a single request
    fetch('/documents/check-saved-batch', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            ...(csrfToken && csrfHeader ? { [csrfHeader]: csrfToken } : {})
        },
        body: JSON.stringify(documentIds)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success && data.savedStatuses) {
            // Update button state for each document
            saveButtons.forEach(button => {
                const docId = button.getAttribute('data-id');
                if (docId && data.savedStatuses[docId]) {
                    button.classList.add('saved');
                    button.textContent = 'Saved';
                }
            });
        }
    })
    .catch(error => console.error('Error checking saved status:', error));
}

/**
 * Show notification
 * @param {string} message - Message to display
 * @param {string} type - Type of notification (success, error, info)
 */
function showNotification(message, type = 'info') {
    // Get notification container
    let notificationContainer = document.getElementById('notification-container');
    if (!notificationContainer) {
        notificationContainer = document.createElement('div');
        notificationContainer.id = 'notification-container';
        document.body.appendChild(notificationContainer);
    }
    
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    
    // Add to container
    notificationContainer.appendChild(notification);
    
    // Add a slight delay before adding class to ensure CSS animation works
    setTimeout(() => {
        notification.classList.add('active');
    }, 10);
    
    // Remove after 5 seconds (longer display time)
    setTimeout(() => {
        notification.classList.add('fade-out');
        // Wait for animation to complete before removing
        setTimeout(() => {
            if (notification && notification.parentNode) {
                notification.remove();
            }
        }, 800); // Match the CSS fade-out animation duration
    }, 5000); // Display for 5 seconds
} 