
import { useEffect, useState } from 'react';
import api from '../api/axios';
import '../styles/tablasModernas.css'; // ¡No olvides importar tu CSS moderno!

export default function ReporteValores() {
  /* ─── estado ─────────────────────────────────────────────── */
  const [desde, setDesde]   = useState('');
  const [hasta, setHasta]   = useState('');
  const [rows,  setRows]    = useState([]);
  const [loading, setLoading] = useState(false);

  /* ─── helpers ────────────────────────────────────────────── */
  const fetchData = async () => {
    if (!desde || !hasta) {
      alert('Seleccione ambas fechas'); return;
    }
    if (desde > hasta)  { alert('La fecha inicio no puede ser mayor'); return; }

    setLoading(true);
    try {
      const r = await api.get('/api/reportes/valores-a-pagar', {
        params: { desde, hasta }
      });
      setRows(
        r.data.map(d => ({
          empleado: d.empleado,
          valor:    Number(d.valorARecibir ?? 0)
        }))
      );
    } catch (err) { console.error(err); alert('Error al consultar'); }
    finally      { setLoading(false); }
  };

  const imprimir = () => window.print();

  /* ─── carga inicial (últimos 30 días) ────────────────────── */
  useEffect(() => {
    const hoy   = new Date().toISOString().substring(0,10);
    const mesAtras = new Date(Date.now() - 30*24*60*60*1000)
                     .toISOString().substring(0,10);
    setDesde(mesAtras);
    setHasta(hoy);
  }, []);

  /* ─── render ─────────────────────────────────────────────── */
  return (
    <div className="container py-4">
      <h2 className="text-center mb-4">
        <i className="fas fa-money-check-alt me-2"></i>
        Valores a pagar por empleado
      </h2>

      {/* --- filtros --- */}
      <div className="card shadow mb-4">
        <div className="card-body">
          <div className="row g-3 align-items-end mb-3">
            <div className="col-md-3">
              <label className="form-label">Fecha inicio</label>
              <input
                type="date"
                className="form-control"
                value={desde}
                onChange={e=>setDesde(e.target.value)}
              />
            </div>
            <div className="col-md-3">
              <label className="form-label">Fecha fin</label>
              <input
                type="date"
                className="form-control"
                value={hasta}
                onChange={e=>setHasta(e.target.value)}
              />
            </div>
            <div className="col-auto">
              <button className="btn btn-primary me-2" onClick={fetchData} disabled={loading}>
                {loading
                  ? <span className="spinner-border spinner-border-sm me-2"></span>
                  : <i className="fas fa-sync-alt me-1"></i>
                }
                Actualizar
              </button>
              <button className="btn btn-outline-secondary" onClick={imprimir}>
                <i className="fas fa-print me-1"></i>Imprimir
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* --- tabla moderna --- */}
      <div className="card shadow">
        <div className="card-header bg-primary text-white">
          <h5 className="mb-0">
            <i className="fas fa-list me-2"></i>Valores a pagar por empleado
          </h5>
        </div>
        <div className="card-body">
          <div className="table-responsive">
            <table className="table table-hover mb-0">
              <thead className="bg-primary text-white" style={{ borderBottom: "4px solid #0d6efd" }}>
                <tr>
                  <th style={{ width: 60 }}>#</th>
                  <th>Empleado</th>
                  <th className="text-end">Valor a pagar</th>
                </tr>
              </thead>
              <tbody>
                {rows.length === 0 ? (
                  <tr>
                    <td colSpan={3} className="text-center text-muted py-3">
                      <div className="alert alert-info mb-0">
                        <i className="fas fa-info-circle me-2"></i>
                        Sin datos
                      </div>
                    </td>
                  </tr>
                ) : (
                  rows.map((r, i) => (
                    <tr key={i}>
                      <td>
                        <span className="badge bg-secondary">{i + 1}</span>
                      </td>
                      <td>{r.empleado}</td>
                      <td className="text-end">
                        <span className="badge bg-success">
                          {(r.valor ?? 0).toFixed(2)}
                        </span>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
              {rows.length > 0 && (
                <tfoot>
                  <tr className="fw-bold">
                    <td colSpan={2} className="text-end">TOTAL</td>
                    <td className="text-end">
                      <span className="badge bg-primary">
                        {rows.reduce((s, r) => s + r.valor, 0).toFixed(2)}
                      </span>
                    </td>
                  </tr>
                </tfoot>
              )}
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}
