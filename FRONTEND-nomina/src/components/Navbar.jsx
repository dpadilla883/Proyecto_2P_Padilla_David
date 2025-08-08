import { NavLink } from 'react-router-dom';
import { Navbar as BsNav, Nav, Container, Button } from 'react-bootstrap';
import { useAuth } from '../auth/useAuth';

export default function Navbar() {
  const { user, logout } = useAuth();

  return (
    <BsNav
      bg="white"
      expand="lg"
      className="shadow-sm py-2"
      style={{
        borderBottom: "3px solid #0d6efd",
        minHeight: 65,
        fontWeight: 500,
      }}
    >
      <Container fluid>
        {/* Logo / Brand siempre visible */}
        <BsNav.Brand as={NavLink} to="/" className="d-flex align-items-center gap-2 text-primary" style={{ fontWeight: 800, fontSize: 23 }}>
          <i className="fas fa-chart-bar fa-lg text-primary" />
          Nomina-UI
        </BsNav.Brand>

        {/* Botón hamburguesa SOLO si hay usuario */}
        {user && <BsNav.Toggle aria-controls="mainNav" />}

        <BsNav.Collapse id="mainNav">
          {/* Enlaces SOLO cuando hay usuario */}
          {user && (
            <Nav className="me-auto" style={{ fontSize: 16 }}>
              <Nav.Link as={NavLink} to="/motivos">
                <i className="fas fa-clipboard-list me-2" />
                Motivos
              </Nav.Link>
              <Nav.Link as={NavLink} to="/empleados">
                <i className="fas fa-users me-2" />
                Empleados
              </Nav.Link>
              <Nav.Link as={NavLink} to="/nominas">
                <i className="fas fa-file-invoice-dollar me-2" />
                Nómina
              </Nav.Link>
              <Nav.Link as={NavLink} to="/reporte1">
                <i className="fas fa-money-check-alt me-2" />
                Reporte Valores
              </Nav.Link>
              <Nav.Link as={NavLink} to="/reporte2">
                <i className="fas fa-table me-2" />
                Reporte Matriz
              </Nav.Link>
            </Nav>
          )}

          {/* Perfil + botón Salir */}
          {user && (
            <div className="d-flex align-items-center gap-3 ms-auto">
              <div className="d-flex align-items-center gap-2 px-2">
                <img
                  src={user.avatar_url}
                  alt="avatar"
                  height={36}
                  width={36}
                  style={{ borderRadius: "50%", border: "2px solid #0d6efd", objectFit: "cover" }}
                />
                <span style={{ fontWeight: 600, color: "#0d6efd" }}>
                  {user.name ?? user.login}
                </span>
              </div>
              <Button
                size="sm"
                variant="outline-primary"
                className="fw-bold"
                onClick={logout}
              >
                <i className="fas fa-sign-out-alt me-1"></i>
                Salir
              </Button>
            </div>
          )}
        </BsNav.Collapse>
      </Container>
    </BsNav>
  );
}
