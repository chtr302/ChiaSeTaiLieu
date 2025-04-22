// Sign-in page JavaScript
//   }
document.addEventListener('DOMContentLoaded', function() {
    // Create floating particles
    createParticles();
    
    // Password visibility toggle
    const togglePassword = document.getElementById('togglePassword');
    const password = document.getElementById('password');
    togglePassword.addEventListener('click', function() {
        const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
        password.setAttribute('type', type);
        this.innerHTML = type === 'password' ? '<i class="fas fa-eye"></i>' : '<i class="fas fa-eye-slash"></i>';
    });
    
    // Form validation
    const loginForm = document.getElementById('loginForm');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const emailError = document.getElementById('email-error');
    const passwordError = document.getElementById('password-error');
    
    // Email validation
    emailInput.addEventListener('input', function() {
        if (this.validity.valid) {
            emailError.style.display = 'none';
        } else {
            emailError.style.display = 'block';
        }
    });
    
    // Password validation
    passwordInput.addEventListener('input', function() {
        if (this.value.length >= 6) {
            passwordError.style.display = 'none';
        } else {
            passwordError.style.display = 'block';
        }
    });
    
    // Submit form with validation
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        let isValid = true;
        
        // Validate email
        if (!emailInput.value.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
            emailError.style.display = 'block';
            isValid = false;
        } else {
            emailError.style.display = 'none';
        }
        
        // Validate password
        if (passwordInput.value.length < 6) {
            passwordError.style.display = 'block';
            isValid = false;
        } else {
            passwordError.style.display = 'none';
        }
        
        if (isValid) {
            // Simulate successful login
            const submitBtn = document.getElementById('submitBtn');
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Signing in...';
            submitBtn.disabled = true;
            
            // Simulate API call
            setTimeout(() => {
                submitBtn.innerHTML = '<i class="fas fa-check"></i> Success! Redirecting...';
                setTimeout(() => {
                    alert('Login successful! Redirecting to dashboard...');
                    // In a real app, you would redirect here
                    // window.location.href = 'dashboard.html';
                }, 1000);
            }, 1500);
        }
    });
    
    // Create ripple effect on buttons
    document.querySelectorAll('.submit-btn, .social-icon').forEach(button => {
        button.addEventListener('click', function(e) {
            const rect = this.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;
            
            const ripple = document.createElement('span');
            ripple.className = 'ripple';
            ripple.style.left = x + 'px';
            ripple.style.top = y + 'px';
            
            this.appendChild(ripple);
            
            setTimeout(() => {
                ripple.remove();
            }, 600);
        });
    });
});

// Create floating particles for background
function createParticles() {
    const particlesContainer = document.getElementById('particles');
    const particleCount = 20;
    
    for (let i = 0; i < particleCount; i++) {
        const particle = document.createElement('div');
        particle.classList.add('particle');
        
        // Random position
        const posX = Math.random() * 100;
        const posY = Math.random() * 100;
        
        // Random size
        const size = Math.random() * 5 + 1;
        
        // Random animation properties
        const duration = Math.random() * 20 + 10;
        const delay = Math.random() * 5;
        
        // Apply styles
        particle.style.width = `${size}px`;
        particle.style.height = `${size}px`;
        particle.style.left = `${posX}%`;
        particle.style.top = `${posY}%`;
        particle.style.animation = `float ${duration}s ease-in-out ${delay}s infinite alternate`;
        particle.style.opacity = Math.random() * 0.5 + 0.1;
        
        // Add custom animation for each particle
        const keyframes = `
            @keyframes float {
                0% {
                    transform: translate(0, 0);
                }
                50% {
                    transform: translate(${Math.random() * 100 - 50}px, ${Math.random() * 100 - 50}px);
                }
                100% {
                    transform: translate(0, 0);
                }
            }
        `;
        
        const style = document.createElement('style');
        style.innerHTML = keyframes;
        document.head.appendChild(style);
        
        particlesContainer.appendChild(particle);
    }
}