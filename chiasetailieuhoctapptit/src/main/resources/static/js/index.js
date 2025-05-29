const searchInput = document.getElementById('searchInput');
const suggestionBox = document.getElementById('suggestionBox');

let debounceTimeout = null;
let selectedIndex = -1;

searchInput.addEventListener('input', function() {
  const query = this.value.trim();
  if (debounceTimeout) clearTimeout(debounceTimeout);

  if (!query) {
    suggestionBox.style.display = 'none';
    suggestionBox.innerHTML = '';
    selectedIndex = -1;
    return;
  }
  debounceTimeout = setTimeout(() => {
    fetch('/documents/api/search/suggestions?query=' + encodeURIComponent(query))
      .then(res => res.json())
      .then(suggestions => {
        if (!suggestions || suggestions.length === 0) {
          suggestionBox.innerHTML = '<div class="navbar-suggestion-empty">Không tìm thấy tài liệu phù hợp</div>';
          suggestionBox.style.display = 'block';
          selectedIndex = -1;
          return;
        }
        suggestionBox.innerHTML = suggestions.map((doc, idx) => {
          let meta = '';
          if (doc.monHoc && doc.loaiTaiLieu) {
            meta = `${doc.monHoc} • ${doc.loaiTaiLieu}`;
          } else if (doc.monHoc) {
            meta = doc.monHoc;
          } else if (doc.loaiTaiLieu) {
            meta = doc.loaiTaiLieu;
          }
          return `
            <a href="/documents/detail/${doc.id}" class="suggestion-item-link${idx === 0 ? ' selected' : ''}" data-index="${idx}">
              <img src="${doc.thumbnail ? '/documents/thumbnails/' + doc.thumbnail : '/img/default-document.png'}" class="suggestion-thumb" alt="thumb">
              <span class="suggestion-title">${doc.tenTaiLieu}
                <span class="suggestion-meta">${meta}</span>
              </span>
            </a>
          `;
        }).join('');
        suggestionBox.style.display = 'block';
        selectedIndex = -1;
      })
      .catch(() => {
        suggestionBox.innerHTML = '<div class="navbar-suggestion-empty">Không tìm thấy tài liệu phù hợp</div>';
        suggestionBox.style.display = 'block';
        selectedIndex = -1;
      });
  }, 200);
});

searchInput.addEventListener('keydown', function(e) {
  const items = suggestionBox.querySelectorAll('.suggestion-item-link');
  if (suggestionBox.style.display === 'block' && items.length > 0) {
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
      const query = this.value.trim();
      if (query) {
        window.location.href = '/documents/all?q=' + encodeURIComponent(query);
      }
    }
  } else if (e.key === 'Enter') {
    const query = this.value.trim();
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

document.addEventListener('click', function(e) {
  if (!searchInput.contains(e.target) && !suggestionBox.contains(e.target)) {
    suggestionBox.style.display = 'none';
    selectedIndex = -1;
  }
});
