document.addEventListener('DOMContentLoaded', function() {
  "use strict";

  document.querySelectorAll('.course-card').forEach(card => {
    card.addEventListener('click', function() {
      const courseName = this.querySelector('.course-title').textContent.trim();
      window.location.href = `/documents?course=${encodeURIComponent(courseName)}`;
    });
  });

  document.querySelectorAll('.upvote-btn').forEach(button => {
    button.addEventListener('click', function() {
      const voteCount = this.parentElement.querySelector('.vote-count');
      voteCount.textContent = parseInt(voteCount.textContent) + 1;
    });
  });

  document.querySelectorAll('.downvote-btn').forEach(button => {
    button.addEventListener('click', function() {
      const voteCount = this.parentElement.querySelector('.vote-count');
      voteCount.textContent = parseInt(voteCount.textContent) - 1;
    });
  });
});