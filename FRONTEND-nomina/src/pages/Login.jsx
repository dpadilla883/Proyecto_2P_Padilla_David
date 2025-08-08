import { useAuth } from "../auth/AuthContext";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Container, Button, Card } from 'react-bootstrap';

export default function Login() {
  const { user, loading } = useAuth();
  const navigate = useNavigate();

  // Redirigir si ya hay token
  useEffect(() => {
    if (localStorage.getItem("token")) navigate("/", { replace: true });
  }, [navigate]);

  if (loading) return <p className="p-4">Verificando sesión…</p>;

  if (user) {
    navigate("/", { replace: true });
    return null;
  }

  const HOST = (import.meta.env.VITE_API_URL || 'http://localhost:8083')
                  .replace(/\/api\/v1$/, '');
  const loginGithub = () =>
    (window.location.href = `${HOST}/oauth2/authorization/github`);
  const loginGoogle = () =>
    (window.location.href = `${HOST}/oauth2/authorization/google`);

  return (
    <Container className="d-flex flex-column align-items-center justify-content-center" style={{ minHeight: "100vh" }}>
      <Card className="shadow-lg p-4" style={{ maxWidth: 400, width: "100%" }}>
        <div className="text-center mb-4">
          <i className="fas fa-user-circle fa-3x text-primary mb-2"></i>
          <h2 className="fw-bold mb-2">Iniciar sesión</h2>
          <div className="text-muted mb-3" style={{ fontSize: 16 }}>
            Elige tu proveedor de autenticación:
          </div>
        </div>

        <div className="d-grid gap-3">
          <Button
            onClick={loginGithub}
            variant="dark"
            size="lg"
            className="d-flex align-items-center justify-content-center"
          >
            <i className="fab fa-github fa-lg me-2" />
            Entrar con&nbsp;<strong>GitHub</strong>
          </Button>

          <Button
            onClick={loginGoogle}
            variant="danger"
            size="lg"
            className="d-flex align-items-center justify-content-center"
          >
            <i className="fab fa-google fa-lg me-2" />
            Entrar con&nbsp;<strong>Google</strong>
          </Button>
        </div>
      </Card>
    </Container>
  );
}
