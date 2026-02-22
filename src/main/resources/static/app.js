const API_BASE = '/api/messages';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('messageForm');
    const tabs = document.querySelectorAll('.tab-btn');
    const contentLabel = document.getElementById('contentLabel');
    const contentPlaceholder = document.getElementById('content');
    const confirmationDiv = document.getElementById('confirmationMessage');
    const sentTimeSpan = document.getElementById('sentTime');
    const submitBtn = document.getElementById('submitBtn');

    let currentType = 'feedback';

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
            currentType = tab.dataset.type;

            if (currentType === 'feedback') {
                contentLabel.textContent = 'Your Feedback';
                contentPlaceholder.placeholder = 'Share your thoughts about our brownies, service, or experience...';
            } else {
                contentLabel.textContent = 'Your Inquiry';
                contentPlaceholder.placeholder = 'Ask a question or report an issue...';
            }

            confirmationDiv.classList.add('hidden');
        });
    });

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        confirmationDiv.classList.add('hidden');

        const name = document.getElementById('name').value.trim();
        const email = document.getElementById('email').value.trim();
        const content = document.getElementById('content').value.trim();

        if (!name || !email || !content) {
            showError('Please fill in all fields.');
            return;
        }

        submitBtn.disabled = true;
        submitBtn.classList.add('loading');

        try {
            const endpoint = currentType === 'feedback' ? '/feedback' : '/inquiry';
            const response = await fetch(`${API_BASE}${endpoint}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ name, email, content }),
            });

            if (!response.ok) {
                throw new Error('Failed to send message');
            }

            const data = await response.json();
            const sentDate = new Date(data.createdAt);
            sentTimeSpan.textContent = sentDate.toLocaleString();

            confirmationDiv.classList.remove('hidden');
            form.reset();

        } catch (err) {
            showError('Unable to send message. Please try again later.');
        } finally {
            submitBtn.disabled = false;
            submitBtn.classList.remove('loading');
        }
    });
});

function showError(message) {
    let errorDiv = document.querySelector('.error-message');
    if (!errorDiv) {
        errorDiv = document.createElement('div');
        errorDiv.className = 'error-message';
        document.querySelector('.chatbox-container').insertBefore(errorDiv, document.querySelector('.message-form'));
    }
    errorDiv.textContent = message;
    errorDiv.style.display = 'block';
    setTimeout(() => {
        errorDiv.style.display = 'none';
    }, 5000);
}
