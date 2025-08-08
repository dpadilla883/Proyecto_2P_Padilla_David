import { Routes, Route, Navigate, useLocation } from 'react-router-dom';
import Navbar from './components/Navbar';
import PrivateRoute from './auth/PrivateRoute';
import './styles/tablasModernas.css';
import OAuth2Success from './pages/OAuth2Success';


import Login            from './pages/Login';
import Motivos          from './pages/Motivos';
import Empleados        from './pages/Empleados';
import Nomina           from './pages/Nomina';
import ReporteValores   from './pages/ReporteValores';
import ReporteMatriz    from './pages/ReporteMatriz';
import MenuDashboard    from './pages/MenuDashboard';
import FloatingChatButton from "./components/FloatingChatButton";

export default function App() {
  const location = useLocation();
  // Mostrar Navbar solo si NO estamos en dashboard ni login
  const hideNavbar = location.pathname === '/login' || location.pathname === '/';

  return (
    <>
      {!hideNavbar && <Navbar />}
      <Routes>
        {/* pública */}
        <Route path="/login" element={<Login />} />
        <Route path="/oauth2/success" element={<OAuth2Success />} />
        {/* dashboard general */}
        <Route element={<PrivateRoute />}>
          <Route path="/" element={<MenuDashboard />} /> {/* Dashboard como HOME */}
          <Route path="/motivos"    element={<Motivos />} />
          <Route path="/empleados"  element={<Empleados />} />
          <Route path="/nominas"    element={<Nomina />} />
          <Route path="/reporte1"   element={<ReporteValores />} />
          <Route path="/reporte2"   element={<ReporteMatriz />} />
          
        </Route>

        {/* fallback */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
      {/* SOLO mostrar chat si NO es login */}
      {location.pathname !== '/login' && <FloatingChatButton />}
    </>
  );
}
