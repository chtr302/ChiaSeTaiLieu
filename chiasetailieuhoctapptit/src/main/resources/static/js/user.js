document.addEventListener('DOMContentLoaded', function() {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    const followBtn = document.querySelector('.follow-btn');
    
    if (followBtn) {
        followBtn.addEventListener('click', function() {
            const userId = this.getAttribute('data-userid');
            const action = this.getAttribute('data-action') || (this.classList.contains('followed') ? 'unfollow' : 'follow');

            const btnText = this.querySelector('span');
            const originalText = btnText.textContent;
            btnText.textContent = 'Đang xử lý...';
            this.disabled = true;

            const headers = {
                'Content-Type': 'application/x-www-form-urlencoded',
            };

            if (csrfHeader && csrfToken) {
                headers[csrfHeader] = csrfToken;
            }

            fetch('/user/follow', {
                method: 'POST',
                headers: headers,
                body: `userId=${userId}&action=${action}`
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {

                    if (data.isFollowing) {

                        this.classList.remove('btn-primary');
                        this.classList.add('btn-secondary', 'followed');
                        btnText.textContent = 'Following';
                        this.setAttribute('data-action', 'unfollow');
                    } else {

                        this.classList.remove('btn-secondary', 'followed');
                        this.classList.add('btn-primary');
                        btnText.textContent = 'Follow';
                        this.setAttribute('data-action', 'follow');
                    }
                    

                    const followerCountEl = document.querySelector('#followers-count .follow-count');
                    if (followerCountEl && data.followerCount !== undefined) {
                        followerCountEl.textContent = data.followerCount;
                    }
                } else {

                    alert(data.message || 'Có lỗi xảy ra');
                    btnText.textContent = originalText;
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Có lỗi xảy ra khi thực hiện thao tác. Vui lòng thử lại!');
                btnText.textContent = originalText;
            })
            .finally(() => {
                this.disabled = false;
            });
        });
    }

    const followersCount = document.getElementById('followers-count');
    const followingCount = document.getElementById('following-count');
    const overlay = document.getElementById('user-follow-overlay');
    const modalTitle = document.getElementById('user-follow-modal-title');
    const closeBtn = document.querySelector('.close-user-follow-modal');
    const followList = overlay ? overlay.querySelector('.follow-list') : null;
    const loadingSpinner = overlay ? overlay.querySelector('.loading-spinner') : null;
    const noResults = overlay ? overlay.querySelector('.no-results') : null;

    function showOverlayList(type, userId) {
        if (!overlay || !followList) return;
        overlay.style.display = 'flex';
        modalTitle.textContent = type === 'followers' ? 'Người theo dõi' : 'Đang theo dõi';
        showLoadingOverlay();
        let endpoint = '';
        // Đảo lại endpoint cho đúng ý nghĩa
        if (type === 'followers') {
            endpoint = `/user/following/${userId}`;
        } else {
            endpoint = `/user/followers/${userId}`;
        }
        fetch(endpoint)
            .then(response => response.json())
            .then(data => {
                renderUserListOverlay(data);
            })
            .catch(() => {
                showNoResultsOverlay('Có lỗi xảy ra khi tải dữ liệu');
            });
    }

    if (followersCount) {
        followersCount.addEventListener('click', function() {
            const userId = this.getAttribute('data-userid');
            showOverlayList('followers', userId);
        });
    }
    if (followingCount) {
        followingCount.addEventListener('click', function() {
            const userId = this.getAttribute('data-userid');
            showOverlayList('following', userId);
        });
    }
    if (closeBtn && overlay) {
        closeBtn.addEventListener('click', function() {
            overlay.style.display = 'none';
            if (followList) followList.innerHTML = '';
            if (loadingSpinner) loadingSpinner.style.display = 'none';
            if (noResults) noResults.classList.add('d-none');
        });
    }

    function renderUserListOverlay(users) {
        if (!followList) return;
        if (loadingSpinner) loadingSpinner.style.display = 'none';
        followList.innerHTML = '';
        if (!users || users.length === 0) {
            if (noResults) noResults.classList.remove('d-none');
            return;
        }
        if (noResults) noResults.classList.add('d-none');
        users.forEach(user => {
            const li = document.createElement('li');
            li.className = 'list-group-item follow-list-item';
            li.innerHTML = `
                <a href="/user/${user.maSV}">
                    <img src="${user.hinhAnh || '/img/default-avatar.jpg'}"
                         alt="${user.hoVaTen || ''}"
                         class="follow-avatar"
                         referrerpolicy="no-referrer">
                    <div class="follow-info">
                        <p class="follow-name">${user.hoVaTen || ''}</p>
                        <p class="follow-id">@${user.maSV}</p>
                    </div>
                </a>
            `;
            followList.appendChild(li);
        });
    }

    function showLoadingOverlay() {
        if (loadingSpinner) loadingSpinner.style.display = 'block';
        if (noResults) noResults.classList.add('d-none');
        if (followList) followList.innerHTML = '';
    }

    function showNoResultsOverlay(message = 'Không có người dùng nào') {
        if (loadingSpinner) loadingSpinner.style.display = 'none';
        if (noResults) noResults.classList.remove('d-none');
        const messageEl = noResults ? noResults.querySelector('p') : null;
        if (messageEl) messageEl.textContent = message;
        if (followList) followList.innerHTML = '';
    }
});