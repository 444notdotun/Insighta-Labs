import axios from "axios";

const BASE_URL = import.meta.env.VITE_API_URL || "https://insighta-labs-183135031185.us-central1.run.app"

const instance = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
    "X-API-Version": "1",
  },
  withCredentials: false,
});

instance.interceptors.request.use((config) => {
  const token = localStorage.getItem("access_token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

instance.interceptors.response.use(
  (res) => res,
  async (error) => {
    const original = error.config;
    if (error.response?.status === 401 && !original._retry) {
      original._retry = true;
      const refreshToken = localStorage.getItem("refresh_token");
      if (!refreshToken) {
        window.location.href = "/login";
        return Promise.reject(error);
      }
      try {
        const res = await axios.post(`${BASE_URL}/auth/refresh`, {
          refreshToken,
        });
        const { accessToken, refreshToken: newRefresh } = res.data.data;
        localStorage.setItem("access_token", accessToken);
        localStorage.setItem("refresh_token", newRefresh);
        original.headers.Authorization = `Bearer ${accessToken}`;
        return instance(original);
      } catch {
        localStorage.removeItem("access_token");
        localStorage.removeItem("refresh_token");
        window.location.href = "/login";
      }
    }
    return Promise.reject(error);
  }
);

export default instance;
