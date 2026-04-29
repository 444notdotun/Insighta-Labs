import { useState, useEffect } from "react";
import axios from "../api/axios";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const [profiles, setProfiles] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const [filters, setFilters] = useState({
    gender: "",
    age_group: "",
    country_id: "",
    min_age: "",
    max_age: "",
    page: 1,
    limit: 10,
  });

  useEffect(() => {
    fetchProfiles();
  }, [filters.page]);

  const fetchProfiles = async () => {
    setLoading(true);
    setError(null);
    try {
      const params = Object.fromEntries(
        Object.entries(filters).filter(([_, v]) => v !== "")
      );
      const res = await axios.get("/api/profiles", { params });
      setProfiles(res.data.data || []);
      setTotalPages(res.data.total_pages || 0);
    } catch (err) {
      setError("Failed to fetch profiles");
    } finally {
      setLoading(false);
    }
  };

  const handleExport = async () => {
    try {
      const params = Object.fromEntries(
        Object.entries(filters).filter(([_, v]) => v !== "")
      );
      const res = await axios.get("/api/profiles/export", {
        params,
        responseType: "blob",
      });
      const url = window.URL.createObjectURL(new Blob([res.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "profiles.csv");
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch {
      setError("Export failed");
    }
  };

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div className="dashboard">
      <header className="dash-header">
        <div className="dash-logo">
          <span>⚡</span> Insighta Labs<span className="plus">+</span>
        </div>
        <div className="dash-user">
          <span className="role-badge">{user?.role}</span>
          <button className="logout-btn" onClick={handleLogout}>Logout</button>
        </div>
      </header>

      <main className="dash-main">
        <div className="filters-panel">
          <h2>Filters</h2>
          <select value={filters.gender} onChange={e => setFilters({...filters, gender: e.target.value})}>
            <option value="">All Genders</option>
            <option value="male">Male</option>
            <option value="female">Female</option>
          </select>

          <select value={filters.age_group} onChange={e => setFilters({...filters, age_group: e.target.value})}>
            <option value="">All Age Groups</option>
            <option value="child">Child</option>
            <option value="teenager">Teenager</option>
            <option value="adult">Adult</option>
            <option value="senior">Senior</option>
          </select>

          <input
            placeholder="Country Code (e.g. NG)"
            value={filters.country_id}
            maxLength={2}
            onChange={e => setFilters({...filters, country_id: e.target.value.toUpperCase()})}
          />

          <div className="age-range">
            <input type="number" placeholder="Min Age" value={filters.min_age}
              onChange={e => setFilters({...filters, min_age: e.target.value})} />
            <input type="number" placeholder="Max Age" value={filters.max_age}
              onChange={e => setFilters({...filters, max_age: e.target.value})} />
          </div>

          <button className="search-btn" onClick={fetchProfiles}>Search</button>
          <button className="export-btn" onClick={handleExport}>Export CSV</button>
        </div>

        <div className="results-panel">
          {error && <div className="error-msg">{error}</div>}
          {loading ? (
            <div className="loading">Loading profiles...</div>
          ) : (
            <>
              <table className="profiles-table">
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Gender</th>
                    <th>Age</th>
                    <th>Age Group</th>
                    <th>Country</th>
                    {user?.role === "ADMIN" && <th>Actions</th>}
                  </tr>
                </thead>
                <tbody>
                  {profiles.map(p => (
                    <tr key={p.id}>
                      <td>{p.name}</td>
                      <td>{p.gender}</td>
                      <td>{p.age}</td>
                      <td>{p.ageGroup}</td>
                      <td>{p.countryId}</td>
                      {user?.role === "ADMIN" && (
                        <td>
                          <button className="delete-btn"
                            onClick={() => handleDelete(p.id)}>Delete</button>
                        </td>
                      )}
                    </tr>
                  ))}
                </tbody>
              </table>

              <div className="pagination">
                <button disabled={filters.page === 1}
                  onClick={() => setFilters({...filters, page: filters.page - 1})}>← Prev</button>
                <span>Page {filters.page} of {totalPages}</span>
                <button disabled={filters.page >= totalPages}
                  onClick={() => setFilters({...filters, page: filters.page + 1})}>Next →</button>
              </div>
            </>
          )}
        </div>
      </main>
    </div>
  );

  async function handleDelete(id) {
    if (!confirm("Delete this profile?")) return;
    try {
      await axios.delete(`/api/profiles/${id}`);
      fetchProfiles();
    } catch {
      setError("Delete failed");
    }
  }
}
