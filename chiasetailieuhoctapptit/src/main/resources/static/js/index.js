const searchInput = document.getElementById('searchInput');
const suggestionBox = document.getElementById('suggestionBox');

// Hàm loại bỏ dấu tiếng Việt
function removeVietnameseTones(str) {
  return str.normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .replace(/đ/g, 'd').replace(/Đ/g, 'D');
}

// Lấy dữ liệu tài liệu từ biến toàn cục đã truyền từ index.html
const allDocuments = window.allDocuments || [];

let debounceTimeout = null;
let selectedIndex = -1;

searchInput.addEventListener('input', function() {
  const query = this.value.trim().toLowerCase();
  const queryNoAccent = removeVietnameseTones(query);
  if (debounceTimeout) clearTimeout(debounceTimeout);

  if (!query) {
    suggestionBox.style.display = 'none';
    suggestionBox.innerHTML = '';
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
        // So khớp cả có dấu và không dấu
        return title.includes(query) || titleNoAccent.includes(queryNoAccent);
      }).slice(0, 10);
    } catch (e) {}
    if (!suggestions || suggestions.length === 0) {
      suggestionBox.style.display = 'block';
      suggestionBox.innerHTML = '<div class="suggestion-empty">Không tìm thấy tài liệu phù hợp</div>';
      selectedIndex = -1;
      return;
    }
    suggestionBox.innerHTML = suggestions.map((doc, idx) => `
      <a href="/documents/detail/${doc.maTaiLieu}" class="suggestion-item-link${idx === 0 ? ' selected' : ''}" data-index="${idx}">
        <img src="${doc.thumbnail ? '/documents/thumbnails/' + doc.thumbnail : '/img/default-document.png'}" class="suggestion-thumb" alt="thumb">
        <span class="suggestion-title">${doc.tieuDe}
          <span class="suggestion-meta">
            ${doc.tenMon ? doc.tenMon : ''}${doc.tenMon && doc.tenLoai ? ' • ' : ''}${doc.tenLoai ? doc.tenLoai : ''}
          </span>
        </span>
      </a>
    `).join('');
    suggestionBox.style.display = 'block';
    selectedIndex = -1;
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
      // Nếu không chọn thì search như cũ
      const query = this.value.trim();
      if (query) {
        // Kiểm tra nếu không có gợi ý nào thì truyền thêm param notfound
        if (items.length === 0) {
          window.location.href = '/documents/all?q=' + encodeURIComponent(query) + '&notfound=1';
        } else {
          window.location.href = '/documents/all?q=' + encodeURIComponent(query);
        }
      }
    }
  } else if (e.key === 'Enter') {
    const query = this.value.trim();
    if (query) {
      // Nếu suggestionBox không hiển thị hoặc không có item nào
      if (!suggestionBox.innerHTML.trim() || suggestionBox.querySelectorAll('.suggestion-item-link').length === 0) {
        window.location.href = '/documents/all?q=' + encodeURIComponent(query) + '&notfound=1';
      } else {
        window.location.href = '/documents/all?q=' + encodeURIComponent(query);
      }
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
