import { useEffect, useState, useMemo } from 'react';
import api from '../api/axios';
import '../styles/tablasModernas.css';

export default function Motivos() {
  /* ---------- state ---------- */
  const blank = { codigo: '', nombre: '', ingreso: true };
  const [motivos, setMotivos] = useState([]);
  const [filter, setFilter] = useState('');
  const [form, setForm] = useState(blank);
  const [editingId, setEditingId] = useState(null);

  /* ---------- helpers ---------- */
  const load = () => api.get('/api/motivos').then(r => setMotivos(r.data));
  useEffect(() => { load(); }, []);

  const save = async () => {
    try {
      if (editingId) {
        await api.put(`/api/motivos/${editingId}`, form);
      } else {
        await api.post('/api/motivos', form);
      }
      setForm(blank);
      setEditingId(null);
      load();
    } catch (err) {
      alert(err.response?.data?.error || err.message);
    }
  };

  const edit = m => {
    setForm({ codigo: m.codigo, nombre: m.nombre, ingreso: m.ingreso });
    setEditingId(m.motivoId);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const remove = async id => {
    if (window.confirm('¿Eliminar motivo?')) {
      await api.delete(`/api/motivos/${id}`);
      load();
    }
  };

  const motivosFiltrados = useMemo(
    () =>
      motivos.filter(m =>
        `${m.codigo} ${m.nombre}`.toLowerCase().includes(filter.toLowerCase())
      ),
    [motivos, filter]
  );

  /* ---------- ui ---------- */
  return (
    <div className="container py-4">
      <h2 className="text-center mb-4">
        <i className="fas fa-book-open me-2"></i>Motivos de Ingreso / Egreso
      </h2>

      {/* Formulario alta/edición */}
      <div className="card shadow mb-4">
        <div className="card-body">
          <h5 className="card-title mb-3">
            <i className="fas fa-plus-circle me-2"></i>
            {editingId ? 'Editar motivo' : 'Nuevo motivo'}
          </h5>
          <div className="row g-3">
            <div className="col-md-3">
              <label className="form-label">Código</label>
              <input
                className="form-control"
                value={form.codigo}
                onChange={e => setForm({ ...form, codigo: e.target.value })}
              />
            </div>
            <div className="col-md-4">
              <label className="form-label">Nombre</label>
              <input
                className="form-control"
                value={form.nombre}
                onChange={e => setForm({ ...form, nombre: e.target.value })}
              />
            </div>
            <div className="col-md-3">
              <label className="form-label">Tipo</label>
              <select
                className="form-select"
                value={form.ingreso ? 'true' : 'false'}
                onChange={e =>
                  setForm({ ...form, ingreso: e.target.value === 'true' })
                }
              >
                <option value="true">Ingreso</option>
                <option value="false">Egreso</option>
              </select>
            </div>
            <div className="col-md-2 d-flex align-items-end">
              <button className="btn btn-success w-100" onClick={save}>
                <i className="fas fa-save me-2"></i>Guardar
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
          placeholder="Buscar código o nombre…"
          value={filter}
          onChange={e => setFilter(e.target.value)}
        />
      </div>

      {/* Tabla moderna */}
      <div className="card shadow">
        <div className="card-header bg-primary text-white">
          <h5 className="mb-0">
            <i className="fas fa-list me-2"></i>Lista de Motivos
          </h5>
        </div>
        <div className="table-responsive">
          <table className="table table-hover mb-0">
            <thead>
              <tr>
                <th>ID</th>
                <th>Código</th>
                <th>Nombre</th>
                <th>Tipo</th>
                <th style={{ width: 130 }}>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {motivosFiltrados.map(m => (
                <tr key={m.motivoId}>
                  <td>{m.motivoId}</td>
                  <td>
                    <span className="badge bg-secondary">{m.codigo}</span>
                  </td>
                  <td>{m.nombre}</td>
                  <td>
                    <span className={`badge ${m.ingreso ? 'bg-success' : 'bg-danger'}`}>
                      {m.ingreso ? 'Ingreso' : 'Egreso'}
                    </span>
                  </td>
                  <td>
                    <div className="btn-group">
                      <button
                        className="btn btn-warning"
                        onClick={() => edit(m)}
                      >
                        <i className="fas fa-edit"></i>
                      </button>
                      <button
                        className="btn btn-danger"
                        onClick={() => remove(m.motivoId)}
                      >
                        <i className="fas fa-trash-alt"></i>
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
              {motivosFiltrados.length === 0 && (
                <tr>
                  <td colSpan="5" className="text-center text-muted">
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