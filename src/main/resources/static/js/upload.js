document.addEventListener('DOMContentLoaded', function() {
  // Step navigation
  const step1 = document.getElementById('step-1');
  const step2 = document.getElementById('step-2');
  const step3 = document.getElementById('step-3');
  const step1Indicator = document.getElementById('step-1-indicator');
  const step2Indicator = document.getElementById('step-2-indicator');
  const step3Indicator = document.getElementById('step-3-indicator');
  const toStep2Btn = document.getElementById('to-step-2');
  const toStep3Btn = document.getElementById('to-step-3');
  const backToStep1Btn = document.getElementById('back-to-step-1');
  const uploadFileInput = document.getElementById('upload-file');
  const fileSelectedInfo = document.getElementById('file-selected-info');
  const fileListDiv = document.getElementById('file-list');
  const fileLimitMsg = document.getElementById('file-limit-msg');
  const fileDetailsContainer = document.getElementById('file-details-container');
  let uploadedFiles = [];

  // Bước 1: Chọn file, tối đa 5 file
  if (uploadFileInput) {
    uploadFileInput.addEventListener('change', function(e) {
      const files = Array.from(uploadFileInput.files);
      if (files.length === 0) return;
      let newFiles = [];
      let totalFiles = uploadedFiles.length;
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        if (!file.name.endsWith('.pdf')) {
          alert('Chỉ chấp nhận file PDF!');
          continue;
        }
        if (totalFiles >= 5) {
          fileLimitMsg.style.display = 'block';
          break;
        }
        if (uploadedFiles.some(f => f.name === file.name && f.size === file.size) ||
            newFiles.some(f => f.name === file.name && f.size === file.size)) {
          continue;
        }
        newFiles.push(file);
        totalFiles++;
      }
      if (newFiles.length > 0) {
        uploadedFiles = uploadedFiles.concat(newFiles);
        fileLimitMsg.style.display = uploadedFiles.length >= 5 ? 'block' : 'none';
      }
      uploadFileInput.value = '';
      renderFileList();
      toStep2Btn.disabled = uploadedFiles.length === 0;
    });
  }

  function renderFileList() {
    if (uploadedFiles.length === 0) {
      fileSelectedInfo.textContent = '';
      fileListDiv.innerHTML = '';
      toStep2Btn.disabled = true;
      return;
    }
    fileSelectedInfo.textContent = uploadedFiles.length + ' file(s) đã chọn';
    let html = '<ul style="padding-left:18px;">';
    uploadedFiles.forEach((f, idx) => {
      html += `<li>${idx+1}. ${f.name} (${Math.round(f.size/1024/1024*100)/100} MB)
        <button type="button" class="remove-file-btn" data-idx="${idx}" style="margin-left:8px;color:#ef4444;background:none;border:none;cursor:pointer;">X</button>
      </li>`;
    });
    html += '</ul>';
    fileListDiv.innerHTML = html;
    // xóa file
    fileListDiv.querySelectorAll('.remove-file-btn').forEach(btn => {
      btn.onclick = function() {
        const idx = parseInt(btn.getAttribute('data-idx'));
        uploadedFiles.splice(idx, 1);
        renderFileList();
        fileLimitMsg.style.display = uploadedFiles.length >= 5 ? 'block' : 'none';
        toStep2Btn.disabled = uploadedFiles.length === 0;
      };
    });
    toStep2Btn.disabled = uploadedFiles.length === 0;
  }

  // 2: Render detail cho từng file
  if (toStep2Btn) {
    toStep2Btn.addEventListener('click', function() {
      if (uploadedFiles.length === 0 || uploadedFiles.length > 5) {
        return;
      }
      step1.style.display = 'none';
      step2.style.display = 'block';
      step1Indicator.classList.remove('active');
      step2Indicator.classList.add('active');
      // Render detail form cho từng file đã chọn bằng template (option đã render bằng Thymeleaf)
      if (fileDetailsContainer) {
        fileDetailsContainer.innerHTML = '';
        let template = document.getElementById('file-detail-template');
        let templateHTML = template.innerHTML;
        uploadedFiles.forEach((file, idx) => {
          // Thay thế file-index và file-name
          let html = templateHTML
            .replace('<span class="file-index"></span>', `<span class="file-index">${idx + 1}</span>`)
            .replace('<span class="file-name"></span>', `<span class="file-name">${file.name}</span>`);
          // Tạo một div tạm để set lại name cho các input/select
          let tempDiv = document.createElement('div');
          tempDiv.innerHTML = html;
          tempDiv.querySelector('.tieuDe').setAttribute('name', `tieuDe_${idx}`);
          tempDiv.querySelector('.maLoai').setAttribute('name', `maLoai_${idx}`);
          tempDiv.querySelector('.maMonHoc').setAttribute('name', `maMonHoc_${idx}`);
          tempDiv.querySelector('.tags').setAttribute('name', `tags_${idx}`);
          tempDiv.querySelector('.moTa').setAttribute('name', `moTa_${idx}`);
          fileDetailsContainer.appendChild(tempDiv.firstElementChild);
        });
      }
    });
  }

  // Bước 2 -> 1
  if (backToStep1Btn) {
    backToStep1Btn.addEventListener('click', function() {
      step2.style.display = 'none';
      step1.style.display = 'block';
      step2Indicator.classList.remove('active');
      step1Indicator.classList.add('active');
    });
  }

  // 3 (Upload)
  if (toStep3Btn) {
    toStep3Btn.addEventListener('click', function() {
      const form = document.getElementById('upload-step2-form');
      let valid = true;
      for (let i = 0; i < uploadedFiles.length; i++) {
        if (!form[`tieuDe_${i}`].value || !form[`maLoai_${i}`].value || !form[`maMonHoc_${i}`].value || !form[`moTa_${i}`].value) {
          valid = false;
          break;
        }
      }
      if (!valid) {
        form.reportValidity();
        return;
      }
      toStep3Btn.disabled = true;
      toStep3Btn.textContent = 'Đang tải lên...';

      // Gửi tất cả file và detail trong 1 lần 
      const formData = new FormData();
      uploadedFiles.forEach((file, idx) => {
        formData.append('files', file);
        formData.append(`tieuDe_${idx}`, form[`tieuDe_${idx}`].value);
        formData.append(`maLoai_${idx}`, form[`maLoai_${idx}`].value);
        formData.append(`maMonHoc_${idx}`, form[`maMonHoc_${idx}`].value);
        formData.append(`tags_${idx}`, form[`tags_${idx}`].value);
        formData.append(`moTa_${idx}`, form[`moTa_${idx}`].value);
      });
      formData.append('totalFiles', uploadedFiles.length);

      // Lấy CSRF token từ meta tag
      const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
      const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
      const headers = {};
      if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
      }

      fetch('/documents/upload', {
        method: 'POST',
        body: formData,
        headers: headers
      }).then(res => {
        if (res.ok) {
          step2.style.display = 'none';
          step3.style.display = 'block';
          step2Indicator.classList.remove('active');
          step3Indicator.classList.add('active');
          // Reset lại danh sách file và form
          uploadedFiles = [];
          renderFileList();
          // Reset detail form
          if (fileDetailsContainer) fileDetailsContainer.innerHTML = '';
        } else {
          return res.text().then(msg => { throw new Error(msg || 'Tải lên thất bại!'); });
        }
      }).catch((err) => {
        const errorDiv = document.getElementById('upload-error-message');
        if (errorDiv) {
          errorDiv.textContent = err.message;
          errorDiv.style.display = 'block';
        } else {
          alert(err.message);
        }
      }).finally(() => {
        toStep3Btn.disabled = false;
        toStep3Btn.textContent = 'Tải lên';
      });
    });
  }

  // Ẩn lỗi khi upload lại
  const errorDiv = document.getElementById('upload-error-message');
  if (toStep3Btn) {
    toStep3Btn.addEventListener('click', function() {
      if (errorDiv) errorDiv.style.display = 'none';
    });
  }
});
