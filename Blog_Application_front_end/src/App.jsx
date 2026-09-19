import { useEffect, useState } from 'react';
import { Routes, Route, Link, Navigate, useNavigate, useParams } from 'react-router-dom';
import {
  apiRequest,
  setAuthToken,
  getAuthToken,
  getCurrentUser,
  setCurrentUser,
} from './api';

const API_BASE = 'http://localhost:8080/api';

function Layout({ children, user, onLogout }) {
  return (
    <>
      <nav className="navbar">
        <h2 style={{ margin: 0 }}>Blog Application</h2>
        <div className="nav-links">
          <Link className="nav-link" to="/">Home</Link>
          <Link className="nav-link" to="/posts">Posts</Link>
          {user && <Link className="nav-link" to="/posts/new">New post</Link>}
          <Link className="nav-link" to="/categories">Categories</Link>
          {user ? (
            <button className="btn btn-secondary" onClick={onLogout}>Logout</button>
          ) : (
            <>
              <Link className="nav-link" to="/login">Login</Link>
              <Link className="nav-link" to="/register">Register</Link>
            </>
          )}
        </div>
      </nav>
      <div className="container">{children}</div>
    </>
  );
}

function HomePage() {
  return (
    <div className="page hero">
      <div className="card">
        <h1 className="page-title">Welcome to the blog</h1>
        <p className="muted">Manage posts, categories, users, and comments through a modern React front-end connected to the Java Spring Boot API.</p>
        <div className="inline-actions">
          <Link className="btn btn-primary" to="/posts">View posts</Link>
          <Link className="btn btn-secondary" to="/login">Login</Link>
        </div>
      </div>
      <div className="card">
        <h3>Backend API</h3>
        <p className="muted">Base URL: {API_BASE}</p>
        <ul>
          <li>Auth: /api/auth/login</li>
          <li>Users: /api/users</li>
          <li>Categories: /api/categories</li>
          <li>Posts: /api/posts</li>
        </ul>
      </div>
    </div>
  );
}

function PostsPage() {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    apiRequest('/posts?pageNumber=0&pageSize=10&sortBy=postId&sortDirection=asc')
      .then((response) => setPosts(response.content || []))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="page"><div className="card">Loading posts...</div></div>;
  if (error) return <div className="page"><div className="card">Error: {error}</div></div>;

  return (
    <div className="page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1 className="page-title">Posts</h1>
        <Link className="btn btn-primary" to="/posts/new">Create post</Link>
      </div>
      <div className="grid">
        {posts.length === 0 ? <div className="card">No posts found.</div> : posts.map((post) => (
          <div className="post-card" key={post.postId}>
            <h3>{post.postTitle || 'Untitled Post'}</h3>
            <p className="muted">{post.postContent ? `${post.postContent.slice(0, 120)}...` : 'No content'}</p>
            <div className="inline-actions">
              <Link className="btn btn-primary" to={`/posts/${post.postId}`}>View</Link>
              <Link className="btn btn-secondary" to={`/posts/${post.postId}/edit`}>Edit</Link>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

function PostFormPage({ user }) {
  const { postId } = useParams();
  const isEditing = Boolean(postId);
  const navigate = useNavigate();
  const [categories, setCategories] = useState([]);
  const [form, setForm] = useState({ postTitle: '', postContent: '', categoryId: '' });
  const [image, setImage] = useState(null);
  const [loading, setLoading] = useState(isEditing);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    Promise.all([
      apiRequest('/categories').then(setCategories),
      isEditing ? apiRequest(`/posts/${postId}`) : Promise.resolve(null),
    ]).then(([, post]) => {
      if (post) {
        setForm({
          postTitle: post.postTitle || '',
          postContent: post.postContent || '',
          categoryId: post.category?.categoryId || '',
        });
      }
    }).catch((err) => setError(err.message)).finally(() => setLoading(false));
  }, [isEditing, postId]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!isEditing && !user?.userId) {
      setError('Your account information is missing. Please log in again.');
      return;
    }
    setSaving(true);
    setError('');
    try {
      const payload = { postTitle: form.postTitle, postContent: form.postContent };
      const endpoint = isEditing
        ? `/posts/${postId}`
        : `/user/${user.userId}/category/${form.categoryId}/posts`;
      const savedPost = await apiRequest(endpoint, { method: isEditing ? 'PUT' : 'POST', body: payload });
      if (image) {
        const imageData = new FormData();
        imageData.append('image', image);
        await apiRequest(`/posts/${isEditing ? postId : savedPost.postId}/image`, {
          method: 'POST',
          body: imageData,
        });
      }
      navigate('/posts');
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <div className="page"><div className="card">Loading post...</div></div>;
  return (
    <div className="page" style={{ maxWidth: 760, margin: '0 auto' }}>
      <div className="card">
        <h1 className="page-title">{isEditing ? 'Edit post' : 'Create post'}</h1>
        <form className="form" onSubmit={handleSubmit}>
          <div className="field">
            <label htmlFor="postTitle">Title</label>
            <input id="postTitle" required minLength={4} maxLength={100} value={form.postTitle}
              onChange={(event) => setForm({ ...form, postTitle: event.target.value })} />
          </div>
          {!isEditing && (
            <div className="field">
              <label htmlFor="categoryId">Category</label>
              <select id="categoryId" required value={form.categoryId}
                onChange={(event) => setForm({ ...form, categoryId: event.target.value })}>
                <option value="">Select a category</option>
                {categories.map((category) => <option key={category.categoryId} value={category.categoryId}>{category.categoryTitle}</option>)}
              </select>
            </div>
          )}
          <div className="field">
            <label htmlFor="postContent">Content</label>
            <textarea id="postContent" required minLength={50} maxLength={1000} value={form.postContent}
              onChange={(event) => setForm({ ...form, postContent: event.target.value })} />
          </div>
          <div className="field">
            <label htmlFor="image">Image</label>
            <input id="image" type="file" accept="image/*" onChange={(event) => setImage(event.target.files?.[0] || null)} />
          </div>
          {error && <p className="muted" style={{ color: '#fca5a5' }}>{error}</p>}
          <div className="inline-actions">
            <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? 'Saving...' : 'Save post'}</button>
            <Link className="btn btn-secondary" to="/posts">Cancel</Link>
          </div>
        </form>
      </div>
    </div>
  );
}

function PostDetailsPage() {
  const { postId } = useParams();
  const [post, setPost] = useState(null);
  const [error, setError] = useState('');
  useEffect(() => {
    apiRequest(`/posts/${postId}`).then(setPost).catch((err) => setError(err.message));
  }, [postId]);
  if (error) return <div className="page"><div className="card">Error: {error}</div></div>;
  if (!post) return <div className="page"><div className="card">Loading post...</div></div>;
  return <div className="page"><article className="card"><h1 className="page-title">{post.postTitle}</h1><p className="muted">{post.postContent}</p><Link className="btn btn-secondary" to="/posts">Back to posts</Link></article></div>;
}

function CategoriesPage() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    apiRequest('/categories').then(setCategories).catch(console.error).finally(() => setLoading(false));
  }, []);
  return <div className="page"><h1 className="page-title">Categories</h1><div className="card">{loading ? 'Loading categories...' : <ul>{categories.length === 0 ? <li>No categories available.</li> : categories.map((cat) => <li key={cat.categoryId}>{cat.categoryTitle}</li>)}</ul>}</div></div>;
}

function LoginPage({ onLogin }) {
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await fetch(`${API_BASE}/auth/login`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(form) });
      const data = await response.json();
      if (!response.ok) throw new Error(data.message || 'Invalid username or password');
      if (!data.token || !data.userId) throw new Error('Incomplete login response from backend');
      setAuthToken(data.token);
      setCurrentUser({ userId: data.userId });
      onLogin({ userId: data.userId });
      navigate('/posts');
    } catch (err) {
      setError(err.message);
    }

    function RegisterPage() {
      const navigate = useNavigate();
      const [form, setForm] = useState({ userName: '', email: '', password: '', about: '' });
      const [error, setError] = useState('');
      const handleSubmit = async (event) => {
        event.preventDefault();
        try {
          await apiRequest('/users', { method: 'POST', body: form });
          navigate('/login');
        } catch (err) {
          setError(err.message);
        }
      };
      return <div className="page" style={{ maxWidth: 500, margin: '0 auto' }}><div className="card"><h1 className="page-title">Register</h1><form className="form" onSubmit={handleSubmit}>
        <div className="field"><label>Name</label><input required minLength={4} value={form.userName} onChange={(e) => setForm({ ...form, userName: e.target.value })} /></div>
        <div className="field"><label>Email</label><input required type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} /></div>
        <div className="field"><label>Password</label><input required minLength={4} maxLength={10} type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} /></div>
        <div className="field"><label>About</label><textarea required value={form.about} onChange={(e) => setForm({ ...form, about: e.target.value })} /></div>
        {error && <p className="muted" style={{ color: '#fca5a5' }}>{error}</p>}
        <button type="submit" className="btn btn-primary">Create account</button>
      </form></div></div>;
    }
  };
  return <div className="page" style={{ maxWidth: 500, margin: '0 auto' }}><div className="card"><h1 className="page-title">Login</h1><form className="form" onSubmit={handleSubmit}><div className="field"><label>Email</label><input type="email" required value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} /></div><div className="field"><label>Password</label><input type="password" required value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} /></div>{error && <p className="muted" style={{ color: '#fca5a5' }}>{error}</p>}<button type="submit" className="btn btn-primary">Login</button></form></div></div>;
}

export default function App() {
  const [user, setUser] = useState(getCurrentUser());
  useEffect(() => { if (!getAuthToken()) setUser(null); }, []);
  const handleLogout = () => { setAuthToken(null); setCurrentUser(null); setUser(null); };
  return <Layout user={user} onLogout={handleLogout}><Routes>
    <Route path="/" element={<HomePage />} />
    <Route path="/posts" element={<PostsPage />} />
    <Route path="/posts/new" element={user ? <PostFormPage user={user} /> : <Navigate to="/login" replace />} />
    <Route path="/posts/:postId/edit" element={user ? <PostFormPage user={user} /> : <Navigate to="/login" replace />} />
    <Route path="/posts/:postId" element={<PostDetailsPage />} />
    <Route path="/categories" element={<CategoriesPage />} />
    <Route path="/login" element={<LoginPage onLogin={setUser} />} />
    <Route path="/register" element={<RegisterPage />} />
    <Route path="*" element={<Navigate to="/" replace />} />
  </Routes></Layout>;
}
