document.addEventListener('DOMContentLoaded', function () {
    const input = document.getElementById('navbar-search-input');
    const suggestionsBox = document.getElementById('navbar-search-suggestions');

    let debounceTimeout = null;
    let selectedIndex = -1;

    input.addEventListener('input', function () {
        const query = input.value.trim();
        if (debounceTimeout) clearTimeout(debounceTimeout);

        if (!query) {
            suggestionsBox.innerHTML = '';
            suggestionsBox.classList.add('hidden');
            selectedIndex = -1;
            return;
        }
        debounceTimeout = setTimeout(() => {
            fetch('/documents/api/search/suggestions?query=' + encodeURIComponent(query))
                .then(res => res.json())
                .then(suggestions => {
                    if (!suggestions || suggestions.length === 0) {
                        suggestionsBox.innerHTML = '<div class="navbar-suggestion-empty">Không tìm thấy tài liệu phù hợp</div>';
                        suggestionsBox.classList.remove('hidden');
                        selectedIndex = -1;
                        return;
                    }
                    suggestionsBox.innerHTML = suggestions.map((doc, idx) => {
                        let meta = '';
                        if (doc.monHoc && doc.loaiTaiLieu) {
                            meta = `${doc.monHoc} • ${doc.loaiTaiLieu}`;
                        } else if (doc.monHoc) {
                            meta = doc.monHoc;
                        } else if (doc.loaiTaiLieu) {
                            meta = doc.loaiTaiLieu;
                        }
                        return `
                        <a href="/documents/detail/${doc.id}" class="navbar-suggestion-item${idx === 0 ? ' selected' : ''}" data-index="${idx}">
                            <img src="${doc.thumbnail ? '/documents/thumbnails/' + doc.thumbnail : '/img/default-document.png'}" alt="" class="navbar-suggestion-thumb">
                            <div class="navbar-suggestion-info">
                                <div class="navbar-suggestion-title">${doc.tenTaiLieu}</div>
                                <div class="navbar-suggestion-meta">${meta}</div>
                            </div>
                        </a>
                        `;
                    }).join('');
                    suggestionsBox.classList.remove('hidden');
                    selectedIndex = -1;
                })
                .catch(() => {
                    suggestionsBox.innerHTML = '<div class="navbar-suggestion-empty">Không tìm thấy tài liệu phù hợp</div>';
                    suggestionsBox.classList.remove('hidden');
                    selectedIndex = -1;
                });
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

    // Hiển thị suggestion box khi focus nếu có nội dung
    input.addEventListener('focus', function () {
        if (suggestionsBox.innerHTML.trim()) {
            suggestionsBox.classList.remove('hidden');
        }
    });

    input.addEventListener('blur', function () {
        setTimeout(() => suggestionsBox.classList.add('hidden'), 120);
    });

    // Click chọn suggestion
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
