document.addEventListener('DOMContentLoaded', function() {
    // Initialize pagination on page load
    initPagination();
    
    // Initialize filter functionality
    initFilterFunctionality();
});

// Pagination functionality
let currentPage = 1;
const itemsPerPage = 12;

function initPagination() {
    renderPage(currentPage);
    
    // Add event listeners to pagination buttons
    const prevButton = document.querySelector('.page-btn.prev');
    const nextButton = document.querySelector('.page-btn.next');
    
    if (prevButton) {
        prevButton.addEventListener('click', () => changePage('prev'));
    }
    
    if (nextButton) {
        nextButton.addEventListener('click', () => changePage('next'));
    }
}

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
    if (!pageNumbersContainer) return;
    
    pageNumbersContainer.innerHTML = '';

    // Add first page
    addPageButton(pageNumbersContainer, 1);
    
    // Add ellipsis if needed
    if (currentPage > 3) {
        addEllipsis(pageNumbersContainer);
    }
    
    // Add pages around current page
    for (let i = Math.max(2, currentPage - 1); i <= Math.min(totalPages - 1, currentPage + 1); i++) {
        addPageButton(pageNumbersContainer, i);
    }
    
    // Add ellipsis if needed
    if (currentPage < totalPages - 2) {
        addEllipsis(pageNumbersContainer);
    }
    
    // Add last page if there are more than 1 page
    if (totalPages > 1) {
        addPageButton(pageNumbersContainer, totalPages);
    }
}

function addPageButton(container, pageNumber) {
    const pageButton = document.createElement('button');
    pageButton.className = 'page-btn';
    pageButton.textContent = pageNumber;
    if (pageNumber === currentPage) {
        pageButton.classList.add('active');
    }
    pageButton.addEventListener('click', () => {
        currentPage = pageNumber;
        renderPage(currentPage);
    });
    container.appendChild(pageButton);
}

function addEllipsis(container) {
    const ellipsis = document.createElement('span');
    ellipsis.className = 'page-ellipsis';
    ellipsis.textContent = '...';
    container.appendChild(ellipsis);
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

// Filter functionality
function initFilterFunctionality() {
    const applyFilterBtn = document.getElementById('apply-filter');
    
    if (applyFilterBtn) {
        applyFilterBtn.addEventListener('click', applyFilters);
    }
    
    // Also apply filters when pressing Enter in any filter field
    const filterSelects = document.querySelectorAll('.filter-select');
    filterSelects.forEach(select => {
        select.addEventListener('change', function(event) {
            if (event.key === 'Enter') {
                applyFilters();
            }
        });
    });
}

function applyFilters() {
    const semesterFilter = document.getElementById('semester-filter').value;
    const typeFilter = document.getElementById('type-filter').value;
    const courseFilter = document.getElementById('course-filter').value;
    const sortFilter = document.getElementById('sort-filter').value;
    
    // Build query parameters
    const params = new URLSearchParams();
    if (semesterFilter) params.append('semester', semesterFilter);
    if (typeFilter) params.append('type', typeFilter);
    if (courseFilter) params.append('course', courseFilter);
    if (sortFilter) params.append('sort', sortFilter);
    
    // Redirect to the same page with filters
    window.location.href = window.location.pathname + '?' + params.toString();
} 