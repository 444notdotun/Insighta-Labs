import { useEffect, useRef } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import axios from "../api/axios";
import { useAuth } from "../context/AuthContext";

export default function Callback() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const { setUser } = useAuth();
    const called = useRef(false);

    useEffect(() => {
        if (called.current) return;
        called.current = true;

        const code = searchParams.get("code");
        const codeVerifier = localStorage.getItem("code_verifier");

        if (!code || !codeVerifier) {
            navigate("/login");
            return;
        }

        axios
            .get(`/auth/github/callback?code=${code}&codeVerifier=${codeVerifier}`)
            .then((res) => {
                const { accessToken, refreshToken, username, userId } = res.data.data;
                localStorage.setItem("access_token", accessToken);
                localStorage.setItem("refresh_token", refreshToken);
                localStorage.removeItem("code_verifier");
                setUser({ userId, username, accessToken });
                setTimeout(() => navigate("/dashboard"), 100);
            })
            .catch(() => navigate("/login"));
    }, []);

    return (
        <div className="callback-loading">
            <div className="spinner"></div>
            <p>Authenticating with GitHub...</p>
        </div>
    );
}