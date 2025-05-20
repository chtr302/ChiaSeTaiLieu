document.addEventListener('DOMContentLoaded', function () {
    const input = document.getElementById('navbar-search-input');
    const suggestionsBox = document.getElementById('navbar-search-suggestions');
    const allDocuments = window.allDocuments || [];

    let debounceTimeout = null;
    let selectedIndex = -1;

    function removeVietnameseTones(str) {
        return str.normalize('NFD')
            .replace(/[\u0300-\u036f]/g, '')
            .replace(/đ/g, 'd').replace(/Đ/g, 'D');
    }

    input.addEventListener('input', function () {
        const query = input.value.trim().toLowerCase();
        const queryNoAccent = removeVietnameseTones(query);
        if (debounceTimeout) clearTimeout(debounceTimeout);

        if (!query) {
            suggestionsBox.innerHTML = '';
            suggestionsBox.classList.add('hidden');
            selectedIndex = -1;
            return;
        }
        debounceTimeout = setTimeout(() => {
            let suggestions = [];
            try {
                suggestions = allDocuments.filter(doc => {
                    if (!doc.tieuDe) return false;
                    const title = doc.tieuDe.toLowerCase();
                    const titleNoAccent = removeVietnameseTones(title);
                    return title.includes(query) || titleNoAccent.includes(queryNoAccent);
                }).slice(0, 10);
            } catch (e) {}
            if (!suggestions || suggestions.length === 0) {
                suggestionsBox.innerHTML = '';
                suggestionsBox.classList.add('hidden');
                selectedIndex = -1;
                return;
            }
            suggestionsBox.innerHTML = suggestions.map((doc, idx) => `
                <a href="/documents/detail/${doc.maTaiLieu}" class="navbar-suggestion-item${idx === 0 ? ' selected' : ''}" data-index="${idx}">
                    <img src="${doc.thumbnail ? '/documents/thumbnails/' + doc.thumbnail : '/img/default-document.png'}" alt="" class="navbar-suggestion-thumb">
                    <div class="navbar-suggestion-info">
                        <div class="navbar-suggestion-title">${doc.tieuDe}</div>
                        <div class="navbar-suggestion-meta">${doc.tenMon || ''}${doc.tenMon && doc.tenLoai ? ' • ' : ''}${doc.tenLoai || ''}</div>
                    </div>
                </a>
            `).join('');
            suggestionsBox.classList.remove('hidden');
            selectedIndex = -1;
        }, 180);
    });

    input.addEventListener('keydown', function(e) {
        const items = suggestionsBox.querySelectorAll('.navbar-suggestion-item');
        if (!suggestionsBox.classList.contains('hidden') && items.length > 0) {
            if (e.key === 'ArrowDown') {
                e.preventDefault();
                selectedIndex = (selectedIndex + 1) % items.length;
                updateSuggestionHighlight(items);
            } else if (e.key === 'ArrowUp') {
                e.preventDefault();
                selectedIndex = (selectedIndex - 1 + items.length) % items.length;
                updateSuggestionHighlight(items);
            } else if (e.key === 'Enter') {
                if (selectedIndex >= 0 && selectedIndex < items.length) {
                    window.location.href = items[selectedIndex].getAttribute('href');
                    return;
                }
                // Nếu không chọn thì search như cũ
                const query = input.value.trim();
                if (query) {
                    window.location.href = '/documents/all?q=' + encodeURIComponent(query);
                }
            }
        } else if (e.key === 'Enter') {
            const query = input.value.trim();
            if (query) {
                window.location.href = '/documents/all?q=' + encodeURIComponent(query);
            }
        }
    });

    function updateSuggestionHighlight(items) {
        items.forEach((item, idx) => {
            if (idx === selectedIndex) {
                item.classList.add('selected');
                item.scrollIntoView({ block: 'nearest' });
            } else {
                item.classList.remove('selected');
            }
        });
    }

    input.addEventListener('blur', function () {
        setTimeout(() => suggestionsBox.classList.add('hidden'), 120);
    });
    input.addEventListener('focus', function () {
        if (suggestionsBox.innerHTML.trim()) suggestionsBox.classList.remove('hidden');
    });

    suggestionsBox.addEventListener('mousedown', function (e) {
        let target = e.target;
        while (target && !target.classList.contains('navbar-suggestion-item') && target !== suggestionsBox) {
            target = target.parentElement;
        }
        if (target && target.classList.contains('navbar-suggestion-item')) {
            window.location.href = target.getAttribute('href');
        }
    });

    document.addEventListener('mousedown', function(e) {
        if (!input.contains(e.target) && !suggestionsBox.contains(e.target)) {
            suggestionsBox.classList.add('hidden');
            selectedIndex = -1;
        }
    });
});
