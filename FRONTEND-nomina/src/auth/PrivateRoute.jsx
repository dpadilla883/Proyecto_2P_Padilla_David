import { Navigate, Outlet } from "react-router-dom";

/**
 * Protege las rutas internas.
 * Si existe token en localStorage -> muestra el componente;
 * de lo contrario redirige a /login.
 */
export default function PrivateRoute() {
  const token = localStorage.getItem("token");
  return token ? <Outlet /> : <Navigate to="/login" replace />;
}
