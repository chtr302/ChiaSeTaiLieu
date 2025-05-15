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
    
    if (followersCount) followersCount.classList.add('clickable');
    if (followingCount) followingCount.classList.add('clickable');

    const modalElement = document.getElementById('followModal');
    let followModal;
    
    if (modalElement) {
        followModal = new bootstrap.Modal(modalElement);

        if (followersCount) {
            followersCount.addEventListener('click', function() {
                const userId = this.getAttribute('data-userid');
                if (!userId) return;
                
                document.getElementById('followModalLabel').textContent = 'Danh sách người theo dõi';
                showLoading();
                
                // Lấy danh sách followers - sửa URL đúng endpoint
                fetch(`/user/followers/${userId}`)
                    .then(response => response.json())
                    .then(data => {
                        renderFollowList(data);
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        showNoResults('Có lỗi xảy ra khi tải dữ liệu');
                    });
                
                followModal.show();
            });
        }
        
        // Xử lý click vào Following count
        if (followingCount) {
            followingCount.addEventListener('click', function() {
                const userId = this.getAttribute('data-userid');
                if (!userId) return;
                
                document.getElementById('followModalLabel').textContent = 'Danh sách người đang theo dõi';
                showLoading();
                
                // Lấy danh sách following - sửa URL đúng endpoint
                fetch(`/user/following/${userId}`)
                    .then(response => response.json())
                    .then(data => {
                        renderFollowList(data);
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        showNoResults('Có lỗi xảy ra khi tải dữ liệu');
                    });
                
                followModal.show();
            });
        }
    }
    
    // Hiển thị danh sách người dùng trong modal
    function renderFollowList(users) {
        const followList = document.querySelector('.follow-list');
        const loadingSpinner = document.querySelector('.loading-spinner');
        const noResults = document.querySelector('.no-results');
        
        // Ẩn loading
        if (loadingSpinner) loadingSpinner.style.display = 'none';
        
        // Xóa danh sách cũ
        if (followList) followList.innerHTML = '';
        
        // Hiển thị thông báo nếu không có người dùng
        if (!users || users.length === 0) {
            if (noResults) noResults.classList.remove('d-none');
            return;
        }
        
        // Ẩn thông báo không có kết quả
        if (noResults) noResults.classList.add('d-none');
        
        // Hiển thị danh sách người dùng
        users.forEach(user => {
            const listItem = document.createElement('li');
            listItem.className = 'list-group-item follow-list-item';
            
            listItem.innerHTML = `
                <a href="/user/${user.maSV}">
                    <img src="${user.hinhAnh || '/img/default-avatar.jpg'}" 
                         alt="${user.hoVaTen}" 
                         class="follow-avatar"
                         referrerpolicy="no-referrer">
                    <div class="follow-info">
                        <p class="follow-name">${user.hoVaTen}</p>
                        <p class="follow-id">@${user.maSV}</p>
                    </div>
                </a>
            `;
            
            followList.appendChild(listItem);
        });
    }
    
    // Hiển thị loading
    function showLoading() {
        const loadingSpinner = document.querySelector('.loading-spinner');
        const noResults = document.querySelector('.no-results');
        const followList = document.querySelector('.follow-list');
        
        if (loadingSpinner) loadingSpinner.style.display = 'block';
        if (noResults) noResults.classList.add('d-none');
        if (followList) followList.innerHTML = '';
    }
    
    // Hiển thị thông báo không có kết quả
    function showNoResults(message = 'Không có người dùng nào') {
        const loadingSpinner = document.querySelector('.loading-spinner');
        const noResults = document.querySelector('.no-results');
        const messageEl = noResults ? noResults.querySelector('p') : null;
        
        if (loadingSpinner) loadingSpinner.style.display = 'none';
        if (noResults) noResults.classList.remove('d-none');
        if (messageEl) messageEl.textContent = message;
    }
});