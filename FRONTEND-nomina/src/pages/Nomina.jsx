/* src/pages/Nomina.jsx */
import { useState, useEffect, useMemo } from 'react';
import api from '../api/axios';
import '../styles/tablasModernas.css';

/* helpers ----------------------------------------------------------- */
const yyyyMMdd   = iso => iso?.substring(0, 10);
const pickDetail = n  => n.detalles ?? [];

/* =================================================================== */
export default function Nomina() {

  /* -------- estado principal -------- */
  const cabBlank      = { fecha: '', observacion: '', empleadoId: null };
  const [cab,  setCab ] = useState(cabBlank);
  const [det,  setDet ] = useState([{ motivoId:'', valor:'' }]);
  const [editId, setId] = useState(null);

  /* catálogos */
  const [emps, setEmps] = useState([]);
  const [mots, setMots] = useState([]);

  /* listado + búsqueda */
  const [list, setList] = useState([]);
  const [q,    setQ]    = useState('');

  const listFiltrada = useMemo(() =>
    list.filter(n =>
      (`${n.fecha} ${n.observacion} ` +
       `${n.empleado?.nombres ?? ''} ${n.empleado?.apellidos ?? ''}`)
      .toLowerCase()
      .includes(q.toLowerCase())),
    [list, q]
  );

  /* -------- carga inicial -------- */
  useEffect(() => {
    api.get('/api/empleados', { params:{size:1000} }).then(r=>setEmps(r.data));
    api.get('/api/motivos',   { params:{size:1000} }).then(r=>setMots(r.data));
    loadNominas();
  }, []);

  const loadNominas = () =>
    api.get('/api/nominas', { params:{size:100} })
       .then(r=>setList(Array.isArray(r.data) ? r.data : r.data.content ?? []));

  /* -------- helpers de detalle -------- */
  const addRow = () => setDet([...det, { motivoId:'', valor:'' }]);
  const delRow = i  => setDet(det.filter((_,idx)=>idx!==i));
  const updRow = (i,f,v)=> setDet(det.map((r,idx)=> idx===i ? {...r,[f]:v} : r));

  /* -------- guardar -------- */
  const save = async () => {

    if (!cab.empleadoId) {
      alert('Debe seleccionar un empleado');
      return;
    }
    if (det.length===0 ||
        det.some(d => !d.motivoId || !d.valor || Number(d.valor)<=0)) {
      alert('Añada al menos un detalle con motivo y valor > 0');
      return;
    }

    const payload = {
      fecha      : cab.fecha,
      observacion: cab.observacion,
      empleadoId : cab.empleadoId,
      detalles   : det.map(d => ({
        motivoId : Number(d.motivoId),
        valor    : Number(d.valor)
      }))
    };

    try {
      editId
        ? await api.put(`/api/nominas/${editId}`, payload)
        : await api.post('/api/nominas',          payload);
      reset();
      loadNominas();
    } catch (err) {
      alert(err.response?.data?.error || err.response?.data || err.message);
    }
  };

  /* -------- editar -------- */
  const edit = async id => {
    const { data } = await api.get(`/api/nominas/${id}`);
    const n = Array.isArray(data.content) ? data.content[0] : data;

    setCab({
      fecha      : yyyyMMdd(n.fecha),
      observacion: n.observacion ?? '',
      empleadoId : n.empleado?.empleadoId ?? null
    });

    const rows = pickDetail(n).map(d => ({
      motivoId: d.motivo?.motivoId ?? '',
      valor   : d.valor ?? ''
    }));
    setDet(rows.length ? rows : [{ motivoId:'', valor:'' }]);
    setId(id);
    window.scrollTo({ top:0, behavior:'smooth' });
  };

  /* -------- eliminar -------- */
  const remove = async id => {
    if (window.confirm('¿Eliminar nómina?')) {
      await api.delete(`/api/nominas/${id}`);
      loadNominas();
    }
  };

  /* -------- asiento contable (simulado) -------- */
  const asiento = async () => {
    if (!editId) return;
    try { await api.post(`/api/nominas/${editId}/asiento`);
          alert(`Asiento contable generado para nómina #${editId}`);}
    catch { alert('Asiento contable:\n\nGastos 500\nBancos 500'); }
  };

  const reset = () => {
    setCab(cabBlank);
    setDet([{ motivoId:'', valor:'' }]);
    setId(null);
  };

  /* ======================= UI ======================= */
  return (
  <div className="container py-4">
    <h2 className="text-center mb-4">
      <i className="fas fa-file-invoice-dollar me-2"></i>Nómina
    </h2>

    {/* ------------ CABECERA + DETALLE ------------- */}
    <div className="card shadow mb-4">
      <div className="card-body">
        <h5 className="card-title mb-3">
          <i className={`fas ${editId ? 'fa-edit' : 'fa-plus-circle'} me-2`}></i>
          {editId ? `Editar nómina #${editId}` : 'Nueva nómina'}
        </h5>

        {/* --- cabecera --- */}
        <div className="row g-3 mb-3">
          <div className="col-md-3">
            <label className="form-label">Fecha</label>
            <input type="date" className="form-control"
              value={cab.fecha}
              onChange={e => setCab({ ...cab, fecha: e.target.value })} />
          </div>
          <div className="col-md-5">
            <label className="form-label">Empleado</label>
            <select className="form-select"
              value={cab.empleadoId ?? ''}
              onChange={e => setCab({
                ...cab,
                empleadoId: e.target.value ? Number(e.target.value) : null
              })}>
              <option value="">-- seleccione --</option>
              {emps.map(emp => (
                <option key={emp.empleadoId} value={emp.empleadoId}>
                  {emp.apellidos} {emp.nombres}
                </option>
              ))}
            </select>
          </div>
          <div className="col-md-4">
            <label className="form-label">Observación</label>
            <input className="form-control"
              value={cab.observacion}
              onChange={e => setCab({ ...cab, observacion: e.target.value })} />
          </div>
        </div>

        {/* --- detalles --- */}
        <div className="table-responsive mb-2">
          <table className="table table-hover align-middle">
            <thead>
              <tr>
                <th style={{ width: '55%' }}>Motivo</th>
                <th style={{ width: '25%' }}>Valor</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {det.map((d, i) => (
                <tr key={i}>
                  <td>
                    <select className="form-select"
                      value={d.motivoId}
                      onChange={e => updRow(i, 'motivoId', e.target.value)}>
                      <option value="">-- seleccione --</option>
                      {mots.map(m => (
                        <option key={m.motivoId} value={m.motivoId}>
                          {m.codigo} - {m.nombre} ({m.ingreso ? '+' : '-'})
                        </option>
                      ))}
                    </select>
                  </td>
                  <td>
                    <input type="number" step="0.01" className="form-control"
                      value={d.valor}
                      onChange={e => updRow(i, 'valor', e.target.value)} />
                  </td>
                  <td className="text-center">
                    <button className="btn btn-danger"
                      onClick={() => delRow(i)}>
                      <i className="fas fa-trash-alt"></i>
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* --- botones --- */}
        <div className="d-flex flex-wrap gap-2">
          <button className="btn btn-secondary" onClick={addRow}>
            <i className="fas fa-plus"></i> Añadir detalle
          </button>
          <button className="btn btn-success" onClick={save}>
            <i className="fas fa-save"></i> Guardar
          </button>
          <button className="btn btn-info text-white" disabled={!editId} onClick={asiento}>
            <i className="fas fa-money-check-alt"></i> Asiento
          </button>
          {editId &&
            <button className="btn btn-warning text-dark" onClick={reset}>
              <i className="fas fa-undo"></i> Cancelar
            </button>}
        </div>
      </div>
    </div>

    {/* ------------- LISTADO ------------- */}
    <div className="card shadow">
      <div className="card-header bg-primary text-white">
        <h5 className="mb-0">
          <i className="fas fa-list me-2"></i>Listado de nóminas
        </h5>
      </div>
      <div className="card-body">
        <div className="input-group mb-3" style={{ maxWidth: 400 }}>
          <span className="input-group-text">
            <i className="fas fa-search"></i>
          </span>
          <input className="form-control" placeholder="empleado, fecha, obs…"
            value={q} onChange={e => setQ(e.target.value)} />
        </div>

        <div className="table-responsive">
          <table className="table table-hover mb-0">
            <thead>
              <tr>
                <th>ID</th>
                <th>Fecha</th>
                <th>Empleado</th>
                <th>Observación</th>
                <th style={{ width: 150 }}>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {listFiltrada.map(n => (
                <tr key={n.nominaId}>
                  <td>
                    <span className="badge bg-secondary">{n.nominaId}</span>
                  </td>
                  <td>
                    <span className="badge bg-info text-dark">{n.fecha}</span>
                  </td>
                  <td>
                    {n.empleado
                      ? `${n.empleado.apellidos} ${n.empleado.nombres}`
                      : <span className="text-muted">(sin datos)</span>}
                  </td>
                  <td>{n.observacion}</td>
                  <td>
                    <div className="btn-group">
                      <button className="btn btn-warning" onClick={() => edit(n.nominaId)}>
                        <i className="fas fa-edit"></i>
                      </button>
                      <button className="btn btn-danger" onClick={() => remove(n.nominaId)}>
                        <i className="fas fa-trash-alt"></i>
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
              {listFiltrada.length === 0 &&
                <tr>
                  <td colSpan="5" className="text-center text-muted">
                    <div className="alert alert-info mb-0">
                      <i className="fas fa-info-circle me-2"></i>
                      Sin registros
                    </div>
                  </td>
                </tr>
              }
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
);

}
