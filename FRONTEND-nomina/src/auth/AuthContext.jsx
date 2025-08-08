/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState, useEffect } from "react";
import api from "../api/axios";

export const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  /* ▶︎ Consulta /api/user SOLO si hay JWT */
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      setLoading(false);
      setUser(null);
      return;
    }

    api
      .get("/api/user")
      .then(r => setUser(r.data))
      .catch(() => {
        localStorage.removeItem("token");   // token inválido
        setUser(null);
      })
      .finally(() => setLoading(false));
  }, []);

  /* logout: borra token y va a /login */
  const logout = () => {
    localStorage.removeItem("token");
    window.location.replace("/login");
  };

  return (
    <AuthContext.Provider value={{ user, loading, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);

// src/auth/AuthContext.jsx  (en el interceptor de Axios)

/*
api.interceptors.response.use(
  r => r,
  err => {
    if (err.response?.status === 401) {
      // Evita borrar token si aún no sabemos por qué falló:
      // localStorage.removeItem("token");
      if (window.location.pathname !== "/login") {
        window.location.replace("/login");
      }
    }
    return Promise.reject(err);
  }
);
*/