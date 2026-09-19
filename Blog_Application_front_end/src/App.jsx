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
const formatAuditDate = (value) => value ? new Date(value).toLocaleString() : 'Not available';

function Layout({ children, user, onLogout }) {
  const navigate = useNavigate();

  const handleLogout = () => {
    onLogout();
    navigate('/');
  };

  return (
    <>
      <nav className="navbar">
        <Link className="brand" to="/"><span className="brand-mark">B</span><span>Ink & Insight</span></Link>
        <div className="nav-links">
          <Link className="nav-link" to="/">Home</Link>
          <Link className="nav-link" to="/posts">Posts</Link>
          {user && <Link className="nav-link" to="/posts/new">New post</Link>}
          <Link className="nav-link" to="/categories">Categories</Link>
          {user ? (
            <>
              <Link className="nav-link" to="/change-password">Change password</Link>
              <button className="btn btn-secondary" onClick={handleLogout}>Logout</button>
            </>
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

function HomePage({ user }) {
  const [posts, setPosts] = useState([]);
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    if (user) {
      apiRequest('/posts?pageNumber=0&pageSize=3&sortBy=postId&sortDirection=desc')
        .then((response) => setPosts(response.content || []))
        .catch(() => {});
    }
    apiRequest('/categories').then(setCategories).catch(() => {});
  }, [user]);

  return (
    <div className="page home-page">
      <section className="hero-banner">
        <div className="hero-copy">
          <span className="eyebrow">A space for curious minds</span>
          <h1>Ideas worth <em>sharing.</em></h1>
          <p>Stories, perspectives, and practical wisdom from people who love to learn in public.</p>
          <div className="inline-actions">
            <Link className="btn btn-primary" to="/posts">Explore stories <span aria-hidden="true">→</span></Link>
            <Link className="text-link" to="/register">Join the community</Link>
          </div>
        </div>
        <div className="hero-art" aria-hidden="true"><span className="art-circle"></span><span className="art-line"></span><span className="art-word">THINK<br /><i>freely</i></span></div>
      </section>
      <section className="section-heading">
        <div><span className="eyebrow">Fresh from the community</span><h2>Latest stories</h2></div>
        <Link className="text-link" to="/posts">View all stories →</Link>
      </section>
      <section className="grid story-grid">
        {posts.length ? posts.map((post, index) => (
          <Link className={`story-card story-accent-${index + 1}`} key={post.postId} to={`/posts/${post.postId}`}>
            <div className="story-visual"><span>{post.category?.categoryTitle || 'FEATURED'}</span></div>
            <div className="story-body"><span className="story-meta">5 min read · Community</span><h3>{post.postTitle || 'Untitled story'}</h3><p className="muted">{post.postContent ? `${post.postContent.slice(0, 100)}...` : 'Discover a new perspective from our community.'}</p><span className="read-more">Read story →</span></div>
          </Link>
        )) : <div className="empty-state card"><h3>Your next story starts here.</h3><p className="muted">Be the first to share an idea with the community.</p><Link className="btn btn-primary" to="/register">Start writing</Link></div>}
      </section>
      <section className="community-strip">
        <div><span className="eyebrow">Find your people</span><h2>Explore by topic</h2></div>
        <div className="topic-list">{categories.slice(0, 5).map((category) => <Link key={category.categoryId} to="/categories" className="topic-pill">{category.categoryTitle}</Link>)}{!categories.length && <span className="muted">Ideas · Creativity · Technology · Life</span>}</div>
      </section>
    </div>
  );
}

function PostsPage() {
  const user = getCurrentUser();
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [refresh, setRefresh] = useState(0);

  useEffect(() => {
    apiRequest('/posts?pageNumber=0&pageSize=10&sortBy=postId&sortDirection=asc')
      .then((response) => setPosts(response.content || []))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [refresh]);

  const canModify = (post) => user?.roles?.includes('ROLE_ADMIN') || post.user?.userId === user?.userId;
  const deletePost = async (postId) => {
    if (!window.confirm('Delete this post?')) return;
    try {
      await apiRequest(`/posts/${postId}`, { method: 'DELETE' });
      setRefresh((value) => value + 1);
    } catch (err) {
      setError(err.message);
    }
  };

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
            <p className="story-meta">Created by {post.createdBy || post.user?.userName || 'Unknown'} · {formatAuditDate(post.createdAt || post.addedDate)}</p>
            <p className="muted">{post.postContent ? `${post.postContent.slice(0, 120)}...` : 'No content'}</p>
            <p className="engagement-counts">{post.likeCount || 0} likes · {post.commentCount || 0} comments · {post.shareCount || 0} shares</p>
            <div className="inline-actions">
              <Link className="btn btn-primary" to={`/posts/${post.postId}`}>View</Link>
              {canModify(post) && <><Link className="btn btn-secondary" to={`/posts/${post.postId}/edit`}>Edit</Link><button className="btn btn-danger" onClick={() => deletePost(post.postId)}>Delete</button></>}
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
  const isAdmin = user?.roles?.includes('ROLE_ADMIN');
  const navigate = useNavigate();
  const [categories, setCategories] = useState([]);
  const [form, setForm] = useState({ postTitle: '', postContent: '', categoryId: '' });
  const [categoryForm, setCategoryForm] = useState({ categoryTitle: '', categoryDescription: '' });
  const [image, setImage] = useState(null);
  const [loading, setLoading] = useState(isEditing);
  const [saving, setSaving] = useState(false);
  const [creatingCategory, setCreatingCategory] = useState(false);
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

  const handleCreateCategory = async (event) => {
    event.preventDefault();
    setCreatingCategory(true);
    setError('');
    try {
      const category = await apiRequest('/categories', { method: 'POST', body: categoryForm });
      setCategories((current) => [...current, category]);
      setForm((current) => ({ ...current, categoryId: category.categoryId }));
      setCategoryForm({ categoryTitle: '', categoryDescription: '' });
    } catch (err) {
      setError(err.message);
    } finally {
      setCreatingCategory(false);
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
              <select id="categoryId" required value={form.categoryId} disabled={!categories.length}
                onChange={(event) => setForm({ ...form, categoryId: event.target.value })}>
                <option value="">{categories.length ? 'Select a category' : 'No categories yet'}</option>
                {categories.map((category) => <option key={category.categoryId} value={category.categoryId}>{category.categoryTitle}</option>)}
              </select>
              {!categories.length && isAdmin && (
                <div className="inline-category-form">
                  <span className="muted">Create a category before publishing your post.</span>
                  <input required minLength={4} placeholder="Category name" value={categoryForm.categoryTitle}
                    onChange={(event) => setCategoryForm({ ...categoryForm, categoryTitle: event.target.value })} />
                  <input required minLength={15} placeholder="Short description (at least 15 characters)" value={categoryForm.categoryDescription}
                    onChange={(event) => setCategoryForm({ ...categoryForm, categoryDescription: event.target.value })} />
                  <button type="button" className="btn btn-secondary" onClick={handleCreateCategory} disabled={creatingCategory}>
                    {creatingCategory ? 'Creating...' : 'Create category'}
                  </button>
                </div>
              )}
              {!categories.length && !isAdmin && <span className="muted">No categories are available yet. Ask an administrator to create one.</span>}
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
  const user = getCurrentUser();
  const [post, setPost] = useState(null);
  const [error, setError] = useState('');
  const [comment, setComment] = useState('');
  const [liked, setLiked] = useState(false);
  const [likeCount, setLikeCount] = useState(0);
  const [shareCount, setShareCount] = useState(0);
  const [message, setMessage] = useState('');
  useEffect(() => {
    apiRequest(`/posts/${postId}`).then((result) => {
      setPost(result);
      setLikeCount(result.likeCount || 0);
      setShareCount(result.shareCount || 0);
    }).catch((err) => setError(err.message));
  }, [postId]);
  const handleLike = async () => {
    const result = liked
      ? await apiRequest(`/posts/${postId}/like`, { method: 'DELETE' })
      : await apiRequest(`/posts/${postId}/like`, { method: 'POST' });
    setLiked(result.liked);
    setLikeCount(result.likeCount);
  };
  const handleComment = async (event) => {
    event.preventDefault();
    if (!comment.trim()) return;
    const result = await apiRequest(`/post/${postId}/comments`, { method: 'POST', body: { commentContent: comment } });
    setPost((current) => ({ ...current, comments: [...(current.comments || []), result], commentCount: (current.commentCount || 0) + 1 }));
    setComment('');
  };
  const handleShare = async () => {
    const url = window.location.href;
    if (navigator.share) await navigator.share({ title: post.postTitle, url });
    else await navigator.clipboard.writeText(url);
    const result = await apiRequest(`/posts/${postId}/share`, { method: 'POST' });
    setShareCount(result.shareCount);
    setMessage(navigator.share ? 'Shared successfully.' : 'Link copied to clipboard.');
  };
  if (error) return <div className="page"><div className="card">Error: {error}</div></div>;
  if (!post) return <div className="page"><div className="card">Loading post...</div></div>;
  return <div className="page"><article className="card post-detail"><span className="eyebrow">{post.category?.categoryTitle || 'Story'}</span><h1 className="page-title">{post.postTitle}</h1><p className="story-meta">Created by {post.createdBy || post.user?.userName || 'Unknown'} · {formatAuditDate(post.createdAt || post.addedDate)}{post.modifiedAt && ` · Modified by ${post.modifiedBy || 'Unknown'} on ${formatAuditDate(post.modifiedAt)}`}</p><p className="muted">{post.postContent}</p><p className="engagement-counts">{likeCount} likes · {post.commentCount || 0} comments · {shareCount} shares</p><div className="post-actions"><button className="btn btn-secondary" onClick={handleLike}>{liked ? 'Unlike' : 'Like'} {likeCount ? `(${likeCount})` : ''}</button><button className="btn btn-secondary" onClick={handleShare}>Share</button><Link className="btn btn-secondary" to="/posts">Back to posts</Link></div>{message && <p className="muted">{message}</p>}<form className="comment-form" onSubmit={handleComment}><label htmlFor="comment">Join the conversation</label><textarea id="comment" required minLength={2} maxLength={500} value={comment} onChange={(event) => setComment(event.target.value)} placeholder="Add a thoughtful comment..." /><button className="btn btn-primary" type="submit">Post comment</button></form><div className="comments">{(post.comments || []).map((item) => <div className="comment" key={item.commentId}><p>{item.commentContent}</p></div>)}</div></article></div>;
}

function CategoriesPage() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    apiRequest('/categories').then(setCategories).catch(console.error).finally(() => setLoading(false));
  }, []);
  return <div className="page"><h1 className="page-title">Categories</h1><div className="card">{loading ? 'Loading categories...' : <ul>{categories.length === 0 ? <li>No categories available.</li> : categories.map((cat) => <li key={cat.categoryId}><strong>{cat.categoryTitle}</strong><span className="story-meta"> · Created by {cat.createdBy || 'Unknown'} · {formatAuditDate(cat.createdAt)}</span>{cat.modifiedAt && <span className="story-meta"> · Modified by {cat.modifiedBy || 'Unknown'} on {formatAuditDate(cat.modifiedAt)}</span>}</li>)}</ul>}</div></div>;
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
      const loggedInUser = { userId: data.userId, roles: data.roles || [] };
      setCurrentUser(loggedInUser);
      onLogin(loggedInUser);
      navigate('/posts');
    } catch (err) {
      setError(err.message);
    }

  };
  return <div className="page" style={{ maxWidth: 500, margin: '0 auto' }}><div className="card"><h1 className="page-title">Login</h1><form className="form" onSubmit={handleSubmit}><div className="field"><label>Email</label><input type="email" required value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} /></div><div className="field"><label>Password</label><input type="password" required value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} /></div>{error && <p className="muted" style={{ color: '#fca5a5' }}>{error}</p>}<button type="submit" className="btn btn-primary">Login</button></form></div></div>;
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

function ChangePasswordPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ currentPassword: '', newPassword: '', confirmPassword: '' });
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');
    setMessage('');
    if (form.newPassword !== form.confirmPassword) {
      setError('New password and confirmation do not match.');
      return;
    }
    try {
      await apiRequest('/users/change-password', {
        method: 'POST',
        body: { currentPassword: form.currentPassword, newPassword: form.newPassword },
      });
      setMessage('Password changed successfully.');
      setForm({ currentPassword: '', newPassword: '', confirmPassword: '' });
      setTimeout(() => navigate('/posts'), 900);
    } catch (err) {
      setError(err.message);
    }
  };
  return <div className="page" style={{ maxWidth: 500, margin: '0 auto' }}><div className="card">
    <h1 className="page-title">Change password</h1>
    <form className="form" onSubmit={handleSubmit}>
      <div className="field"><label htmlFor="currentPassword">Current password</label><input id="currentPassword" required type="password" value={form.currentPassword} onChange={(e) => setForm({ ...form, currentPassword: e.target.value })} /></div>
      <div className="field"><label htmlFor="newPassword">New password</label><input id="newPassword" required minLength={4} maxLength={10} type="password" value={form.newPassword} onChange={(e) => setForm({ ...form, newPassword: e.target.value })} /></div>
      <div className="field"><label htmlFor="confirmPassword">Confirm new password</label><input id="confirmPassword" required minLength={4} maxLength={10} type="password" value={form.confirmPassword} onChange={(e) => setForm({ ...form, confirmPassword: e.target.value })} /></div>
      {error && <p className="muted" style={{ color: '#fca5a5' }}>{error}</p>}
      {message && <p className="muted">{message}</p>}
      <button type="submit" className="btn btn-primary">Update password</button>
    </form>
  </div></div>;
}

export default function App() {
  const [user, setUser] = useState(getCurrentUser());
  useEffect(() => { if (!getAuthToken()) setUser(null); }, []);
  const handleLogout = () => { setAuthToken(null); setCurrentUser(null); setUser(null); };
  return <Layout user={user} onLogout={handleLogout}><Routes>
    <Route path="/" element={<HomePage user={user} />} />
    <Route path="/posts" element={user ? <PostsPage /> : <Navigate to="/login" replace />} />
    <Route path="/posts/new" element={user ? <PostFormPage user={user} /> : <Navigate to="/login" replace />} />
    <Route path="/posts/:postId/edit" element={user ? <PostFormPage user={user} /> : <Navigate to="/login" replace />} />
    <Route path="/posts/:postId" element={user ? <PostDetailsPage /> : <Navigate to="/login" replace />} />
    <Route path="/categories" element={<CategoriesPage />} />
    <Route path="/login" element={<LoginPage onLogin={setUser} />} />
    <Route path="/register" element={<RegisterPage />} />
    <Route path="/change-password" element={user ? <ChangePasswordPage /> : <Navigate to="/login" replace />} />
    <Route path="*" element={<Navigate to="/" replace />} />
  </Routes></Layout>;
}
