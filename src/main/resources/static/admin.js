const API_BASE = '/api/messages';

let allMessages = [];
let currentFilter = 'all';

document.addEventListener('DOMContentLoaded', () => {
    loadTrends();
    loadMessages();
    setupFilterTabs();
});

function setupFilterTabs() {
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            currentFilter = btn.dataset.filter;
            renderMessages();
        });
    });
}

async function loadTrends() {
    try {
        const res = await fetch(`${API_BASE}/trends`);
        const data = await res.json();
        document.getElementById('totalCount').textContent = data.totalMessages ?? 0;
        document.getElementById('newCount').textContent = data.newCount ?? 0;
        document.getElementById('resolvedCount').textContent = data.resolvedCount ?? 0;
        document.getElementById('feedbackCount').textContent = data.feedbackCount ?? 0;
        document.getElementById('inquiryCount').textContent = data.inquiryCount ?? 0;
    } catch (err) {
        showError('Failed to load statistics.');
    }
}

async function loadMessages() {
    try {
        const res = await fetch(API_BASE);
        if (!res.ok) throw new Error('Failed to fetch');
        allMessages = await res.json();
        renderMessages();
    } catch (err) {
        document.getElementById('messagesList').innerHTML =
            '<div class="no-messages">Unable to load messages. Make sure the server is running.</div>';
        showError('Failed to load messages.');
    }
}

function getFilteredMessages() {
    if (currentFilter === 'unresponded') {
        return allMessages.filter(m => m.status === 'NEW');
    }
    if (currentFilter === 'responded') {
        return allMessages.filter(m => m.status === 'RESOLVED');
    }
    return allMessages;
}

function renderMessages() {
    const container = document.getElementById('messagesList');
    const messages = getFilteredMessages();

    if (messages.length === 0) {
        container.innerHTML = '<div class="no-messages">No messages to display.</div>';
        return;
    }

    container.innerHTML = messages.map(msg => createMessageCard(msg)).join('');

    container.querySelectorAll('.status-select').forEach(select => {
        select.addEventListener('change', (e) => updateStatus(msgIdFromElement(e.target), e.target.value));
    });

    container.querySelectorAll('.respond-form').forEach(form => {
        form.addEventListener('submit', (e) => {
            e.preventDefault();
            const id = msgIdFromElement(form);
            const textarea = form.querySelector('textarea');
            respondToMessage(id, textarea.value.trim());
        });
    });
}

function msgIdFromElement(el) {
    return parseInt(el.closest('.message-card').dataset.id, 10);
}

function createMessageCard(msg) {
    const date = msg.createdAt ? new Date(msg.createdAt).toLocaleString() : '-';
    const hasResponse = msg.adminResponse && msg.adminResponse.length > 0;

    return `
        <div class="message-card" data-id="${msg.id}">
            <div class="message-card-header">
                <div>
                    <span class="customer-name">${escapeHtml(msg.customerName)}</span>
                    <span class="badge badge-${msg.type.toLowerCase()}">${msg.type}</span>
                    <span class="badge badge-${msg.status.toLowerCase()}">${msg.status}</span>
                </div>
                <div>
                    <select class="status-select" data-status="${msg.status}">
                        <option value="NEW" ${msg.status === 'NEW' ? 'selected' : ''}>New</option>
                        <option value="PENDING" ${msg.status === 'PENDING' ? 'selected' : ''}>Pending</option>
                        <option value="RESOLVED" ${msg.status === 'RESOLVED' ? 'selected' : ''}>Resolved</option>
                    </select>
                </div>
            </div>
            <div class="timestamp">${date} • ${escapeHtml(msg.customerEmail)}</div>
            <div class="content">${escapeHtml(msg.content)}</div>
            ${hasResponse ? `
                <div class="admin-response">
                    <div class="admin-response-label">Your Response</div>
                    ${escapeHtml(msg.adminResponse)}
                </div>
            ` : ''}
            ${!hasResponse ? `
                <form class="respond-form">
                    <textarea placeholder="Type your response..." required></textarea>
                    <div class="btn-group">
                        <button type="submit" class="btn-small btn-primary">Send Response</button>
                    </div>
                </form>
            ` : ''}
        </div>
    `;
}

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

async function updateStatus(id, status) {
    try {
        const res = await fetch(`${API_BASE}/${id}/status`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status }),
        });
        if (!res.ok) throw new Error('Update failed');
        const updated = await res.json();
        const idx = allMessages.findIndex(m => m.id === id);
        if (idx >= 0) allMessages[idx] = updated;
        loadTrends();
        renderMessages();
    } catch (err) {
        showError('Failed to update status.');
    }
}

async function respondToMessage(id, response) {
    if (!response) return;
    try {
        const res = await fetch(`${API_BASE}/${id}/respond`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ response }),
        });
        if (!res.ok) throw new Error('Response failed');
        const updated = await res.json();
        const idx = allMessages.findIndex(m => m.id === id);
        if (idx >= 0) allMessages[idx] = updated;
        loadTrends();
        renderMessages();
    } catch (err) {
        showError('Failed to send response.');
    }
}

function showError(msg) {
    const banner = document.getElementById('errorBanner');
    banner.textContent = msg;
    banner.classList.remove('hidden');
    setTimeout(() => banner.classList.add('hidden'), 5000);
}
