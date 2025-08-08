import { Card, Button, Row, Col, Container } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/useAuth'; // Cambia esto según tu hook de autenticación

const modules = [
  { name: "Nómina", enabled: true, icon: <span style={{fontSize: "3rem"}}>👤</span>, route: "/motivos" },
  { name: "Contabilidad", enabled: false, icon: <span style={{fontSize: "3rem"}}>💼</span>, route: "#" },
  { name: "Facturación", enabled: false, icon: <span style={{fontSize: "3rem"}}>🧾</span>, route: "#" },
  { name: "Activos", enabled: false, icon: <span style={{fontSize: "3rem"}}>🏢</span>, route: "#" },
];

function MenuDashboard() {
  const navigate = useNavigate();
  const { logout } = useAuth(); // Ajusta según tu lógica

  return (
    <Container className="mt-4">
      {/* Botón salir */}
      <div className="d-flex justify-content-end mb-2">
        <Button variant="outline-danger" size="sm" onClick={logout}>
          Salir
        </Button>
      </div>

      {/* Título */}
      <h2 className="text-center mb-4">Módulos del Sistema</h2>
       

      {/* Módulos 2x2 */}
      <Row className="g-4 justify-content-center">
        {modules.map((mod) => (
          <Col xs={12} sm={6} md={6} lg={6} xl={6} key={mod.name}>
            <Card className="text-center shadow-sm" style={{ opacity: mod.enabled ? 1 : 0.5 }}>
              <Card.Body>
                <div>{mod.icon}</div>
                <Button
                  variant="primary"
                  disabled={!mod.enabled}
                  className="mt-3 w-100"
                  onClick={() => mod.enabled && navigate(mod.route)}
                >
                  {mod.name}
                </Button>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </Container>
  );
}

export default MenuDashboard;

