document.addEventListener('DOMContentLoaded', () => {
    // Tab functionality
    const tabButtons = document.querySelectorAll('.tab-button'); // Use the specific class for tabs

    tabButtons.forEach(tab => {
        tab.addEventListener('click', () => {
            // Remove 'active' class from all tabs
            tabButtons.forEach(t => {
                t.classList.remove('active');
                // Optionally reset hover styles if needed, though CSS handles this usually
            });

            // Add 'active' class to the clicked tab
            tab.classList.add('active');
        });
    });

    // Add any other JavaScript functionality here if needed
    document.addEventListener('DOMContentLoaded', function() {
        const tabs = document.querySelectorAll('.tab-button');
        const tabContents = document.querySelectorAll('.tab-content');
    
        tabs.forEach(tab => {
            tab.addEventListener('click', () => {
                const targetId = tab.getAttribute('data-tab');
                const targetContent = document.getElementById(targetId);
    
                // Remove active class from all tabs and content
                tabs.forEach(t => t.classList.remove('active'));
                tabContents.forEach(tc => {
                    tc.classList.remove('active');
                    tc.style.display = 'none'; // Hide content
                });
    
                // Add active class to the clicked tab and corresponding content
                tab.classList.add('active');
                if (targetContent) {
                    targetContent.classList.add('active');
                    targetContent.style.display = 'block'; // Show content
                } else {
                    console.warn(`Content for tab '${targetId}' not found.`);
                }
            });
        });
    
        // Optional: Ensure the initially active tab's content is visible
        const initialActiveTab = document.querySelector('.tab-button.active');
        if (initialActiveTab) {
            const initialTargetId = initialActiveTab.getAttribute('data-tab');
            const initialTargetContent = document.getElementById(initialTargetId);
            if (initialTargetContent) {
                 initialTargetContent.style.display = 'block';
            }
        }
    });
});