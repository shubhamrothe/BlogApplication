const API_BASE = 'http://localhost:8080/api';

export function getAuthToken() {
  return localStorage.getItem('blog_token');
}

export function setAuthToken(token) {
  if (token) localStorage.setItem('blog_token', token);
  else localStorage.removeItem('blog_token');
}

export function getCurrentUser() {
  const value = localStorage.getItem('blog_user');
  return value ? JSON.parse(value) : null;
}

export function setCurrentUser(user) {
  if (user) localStorage.setItem('blog_user', JSON.stringify(user));
  else localStorage.removeItem('blog_user');
}

export async function apiRequest(endpoint, options = {}) {
  const token = getAuthToken();
  const headers = {
    ...(options.body instanceof FormData ? {} : { 'Content-Type': 'application/json' }),
    ...(token ? { ['Authorization']: ['Bearer', token].join(' ') } : {}),
    ...(options.headers || {}),
  };
  const config = { ...options, headers };

  if (config.body && typeof config.body !== 'string') config.body = JSON.stringify(config.body);

  const response = await fetch(`${API_BASE}${endpoint}`, config);
  const contentType = response.headers.get('content-type') || '';
  const payload = contentType.includes('application/json') ? await response.json() : await response.text();
  if (!response.ok) {
    const message = typeof payload === 'string'
      ? payload
      : payload?.message || Object.values(payload || {}).join(', ') || 'Request failed';
    throw new Error(message);
  }
  return payload;
}

export default API_BASE;
