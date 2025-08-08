import { useState, useEffect, useMemo } from 'react';
import api from '../api/axios';
import '../styles/tablasModernas.css';

export default function Empleados() {
  /* ---------- estado ---------- */
  const blank = {
    cedula: '',
    nombres: '',
    apellidos: '',
    fechaIngreso: '',
    sueldoBase: ''
  };

  const [empleados, setEmpleados]   = useState([]);
  const [filter, setFilter]         = useState('');
  const [form, setForm]             = useState(blank);
  const [editingId, setEditingId]   = useState(null);

  /* ---------- cargar lista ---------- */
  const load = () =>
    api.get('/api/empleados', { params: { size: 1000 } })   // trae hasta 1000 filas
       .then(r => setEmpleados(r.data));

  useEffect(() => { load(); }, []);

  /* ---------- CRUD ---------- */
  const save = async () => {
    try {
      const payload = { ...form, sueldoBase: Number(form.sueldoBase) };
      const cfg = { headers: { 'Content-Type': 'application/json' } };

      if (editingId) {
        await api.put(`/api/empleados/${editingId}`, payload, cfg);
      } else {
        await api.post('/api/empleados', payload, cfg);
      }
      setForm(blank);
      setEditingId(null);
      load();
    } catch (err) {
      const msg = err.response?.data?.error || err.response?.data || err.message;
      alert(msg);
    }
  };

  const edit = e => {
    setForm({
      cedula: e.cedula,
      nombres: e.nombres,
      apellidos: e.apellidos,
      fechaIngreso: e.fechaIngreso,
      sueldoBase: e.sueldoBase
    });
    setEditingId(e.empleadoId);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const remove = async id => {
    if (window.confirm('¿Eliminar empleado?')) {
      await api.delete(`/api/empleados/${id}`);
      load();
    }
  };

  /* ---------- filtro ---------- */
  const empleadosFiltrados = useMemo(
    () => empleados.filter(e =>
      `${e.cedula} ${e.nombres} ${e.apellidos}`.toLowerCase()
        .includes(filter.toLowerCase())
    ),
    [empleados, filter]
  );

  /* ---------- UI ---------- */
  return (
    <div className="container py-4">
      <h2 className="text-center mb-4">
        <i className="fas fa-id-badge me-2"></i>Gestión de Empleados
      </h2>

      {/* Formulario alta/edición */}
      <div className="card shadow mb-4">
        <div className="card-body">
          <h5 className="card-title mb-3">
            <i className={`fas ${editingId ? 'fa-user-edit' : 'fa-user-plus'} me-2`}></i>
            {editingId ? 'Editar empleado' : 'Nuevo empleado'}
          </h5>
          <div className="row g-3">
            <div className="col-md-3">
              <label className="form-label">Cédula</label>
              <input
                className="form-control"
                value={form.cedula}
                onChange={e => setForm({ ...form, cedula: e.target.value })}
              />
            </div>
            <div className="col-md-3">
              <label className="form-label">Nombres</label>
              <input
                className="form-control"
                value={form.nombres}
                onChange={e => setForm({ ...form, nombres: e.target.value })}
              />
            </div>
            <div className="col-md-3">
              <label className="form-label">Apellidos</label>
              <input
                className="form-control"
                value={form.apellidos}
                onChange={e => setForm({ ...form, apellidos: e.target.value })}
              />
            </div>
            <div className="col-md-3">
              <label className="form-label">Fecha ingreso</label>
              <input
                type="date"
                className="form-control"
                value={form.fechaIngreso}
                onChange={e => setForm({ ...form, fechaIngreso: e.target.value })}
              />
            </div>
            <div className="col-md-3">
              <label className="form-label">Sueldo base</label>
              <input
                type="number"
                step="0.01"
                className="form-control"
                value={form.sueldoBase}
                onChange={e => setForm({ ...form, sueldoBase: e.target.value })}
              />
            </div>
            <div className="col-md-2 d-flex align-items-end">
              <button className="btn btn-success w-100" onClick={save}>
                <i className="fas fa-save me-2"></i>
                Guardar
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* búsqueda */}
      <div className="input-group mb-4">
        <span className="input-group-text">
          <i className="fas fa-search"></i>
        </span>
        <input
          className="form-control"
          placeholder="Buscar cédula o nombre…"
          value={filter}
          onChange={e => setFilter(e.target.value)}
        />
      </div>

      {/* Tabla moderna */}
      <div className="card shadow">
        <div className="card-header bg-primary text-white">
          <h5 className="mb-0">
            <i className="fas fa-users me-2"></i>Lista de Empleados
          </h5>
        </div>
        <div className="table-responsive">
          <table className="table table-hover mb-0">
            <thead>
              <tr>
                <th>ID</th>
                <th>Cédula</th>
                <th>Nombres</th>
                <th>Apellidos</th>
                <th>Ingreso</th>
                <th>Sueldo</th>
                <th style={{ width: 130 }}>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {empleadosFiltrados.map(e => (
                <tr key={e.empleadoId}>
                  <td>{e.empleadoId}</td>
                  <td>
                    <span className="badge bg-secondary">{e.cedula}</span>
                  </td>
                  <td>{e.nombres}</td>
                  <td>{e.apellidos}</td>
                  <td>
                    <span className="badge bg-info text-dark">
                      {e.fechaIngreso}
                    </span>
                  </td>
                  <td>
                    <span className="badge bg-light text-dark">
                      {Number(e.sueldoBase).toFixed(2)}
                    </span>
                  </td>
                  <td>
                    <div className="btn-group">
                      <button className="btn btn-warning" onClick={() => edit(e)}>
                        <i className="fas fa-edit"></i>
                      </button>
                      <button className="btn btn-danger" onClick={() => remove(e.empleadoId)}>
                        <i className="fas fa-trash-alt"></i>
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
              {empleadosFiltrados.length === 0 && (
                <tr>
                  <td colSpan="7" className="text-center text-muted">
                    <div className="alert alert-info mb-0">
                      <i className="fas fa-info-circle me-2"></i>
                      Sin resultados
                    </div>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
