import { useEffect } from "react";

export default function OAuth2Success() {
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token  = params.get("token");

    if (token) localStorage.setItem("token", token);
    window.location.replace("/");       // vuelve al dashboard
  }, []);

  return <p style={{ padding: "2rem" }}>Autenticando…</p>;
}
