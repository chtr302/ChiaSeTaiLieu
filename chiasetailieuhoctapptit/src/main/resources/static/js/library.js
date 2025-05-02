document.addEventListener('DOMContentLoaded', function() {
    loadDocuments();
    initializeFilters();
    
    // Load more button functionality
    const loadMoreBtn = document.querySelector('.load-more-container .btn-secondary');
    if (loadMoreBtn) {
        loadMoreBtn.addEventListener('click', loadMoreDocuments);
    }
});

function loadDocuments() {
    const documentGrid = document.querySelector('.document-grid');
    if (!documentGrid) return;

    // We'll populate this with data from the server later
    const documents = [
        {
            type: 'pdf',
            title: 'Introduction to Economics',
            description: 'Lecture notes covering basic economic principles',
            pages: 12
        },
        {
            type: 'word',
            title: 'Marketing Strategy',
            description: 'Case studies and analysis of successful campaigns',
            pages: 24
        },
        // Add more sample documents as needed
    ];

    documents.forEach(doc => {
        const card = createDocumentCard(doc);
        documentGrid.appendChild(card);
    });
}

function createDocumentCard(doc) {
    const card = document.createElement('div');
    card.className = 'document-card';
    
    const iconClass = getIconClass(doc.type);
    
    card.innerHTML = `
        <div class="doc-preview ${doc.type}-preview">
            <i class="fas ${iconClass}"></i>
        </div>
        <h3 class="doc-title">${doc.title}</h3>
        <p class="doc-description">${doc.description}</p>
        <div class="doc-footer">
            <span class="doc-pages">${doc.pages} pages</span>
            <button class="doc-view-btn">View details</button>
        </div>
    `;
    
    return card;
}

function getIconClass(type) {
    const iconMap = {
        'pdf': 'fa-file-pdf',
        'word': 'fa-file-word',
        'excel': 'fa-file-excel',
        'ppt': 'fa-file-powerpoint',
        'text': 'fa-file-alt'
    };
    return iconMap[type] || 'fa-file';
}

function initializeFilters() {
    const filterBtn = document.querySelector('.btn-primary');
    if (filterBtn) {
        filterBtn.addEventListener('click', applyFilters);
    }
}

function applyFilters() {
    const subject = document.querySelector('.filter-select:first-child').value;
    const sortBy = document.querySelector('.filter-select:last-child').value;
    // We'll implement filtering logic here later
    console.log('Filtering by:', subject, 'Sort by:', sortBy);
}

function loadMoreDocuments() {
    // We'll implement load more functionality here later
    console.log('Loading more documents...');
}