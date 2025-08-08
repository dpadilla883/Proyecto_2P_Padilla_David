
import { useState } from 'react';
import api from '../api/axios';
import '../styles/tablasModernas.css'; // Importa tu CSS moderno aquí

/*
 *  Matriz Motivos por Empleado ─ Reporte 2
 *  – muestra columnas dinámicas (códigos de motivo)
 *  – botón "Actualizar" consulta; tabla inicia vacía
 *  – botón "Imprimir" = window.print()
 */
export default function ReporteMatriz() {

  // rango por defecto = mes actual, pero NO se consulta todavía
  const hoy        = new Date();
  const yyyy_mm_dd = d => d.toISOString().slice(0,10);
  const finDefault = yyyy_mm_dd(hoy);
  hoy.setDate(1);
  const iniDefault = yyyy_mm_dd(hoy);

  const [desde, setDesde] = useState(iniDefault);
  const [hasta, setHasta] = useState(finDefault);

  const [filas,   setFilas]   = useState([]);   // data
  const [columnas,setColumnas]= useState([]);   // códigos motivo
  const [totCol,  setTotCol ] = useState({});   // totales por motivo
  const [loading, setLoading] = useState(false);

  /* ---------------------------------------------------------------- */
  async function consultar(){
    setLoading(true);
    try{
      const {data} = await api.get('/api/reportes/matriz-motivos',{
        params:{desde, hasta}
      });

      // ─── calcular columnas dinámicamente ──────────────────────────
      const cols = new Set();
      data.forEach(r =>
        Object.keys(r.valores).forEach(c=>cols.add(c)));
      const ordCols = [...cols].sort();

      // ─── totales de columna ───────────────────────────────────────
      const tot = ordCols.reduce((o,c)=>(o[c]=0,o),{});

      data.forEach(r=>{
        ordCols.forEach(c=>{
          tot[c] += Number(r.valores[c] || 0);
        });
      });

      setFilas(data);
      setColumnas(ordCols);
      setTotCol(tot);

    }catch(e){
      console.error(e);
      alert('Error al consultar');
    } finally {
      setLoading(false);
    }
  }

  function imprimir(){
    window.print();
  }

  /* === UI ========================================================== */
  return(
    <div className="container py-4">

      <h2 className="text-center mb-4">
        <i className="fas fa-table me-2"></i>
        Matriz motivos por empleado
      </h2>

      <div className="card shadow mb-4">
        <div className="card-body">
          <div className="row g-2 mb-3">
            <div className="col-auto">
              <label className="form-label me-2">Fecha inicio</label>
              <input type="date" className="form-control"
                     value={desde} onChange={e=>setDesde(e.target.value)}/>
            </div>
            <div className="col-auto">
              <label className="form-label me-2">Fecha fin</label>
              <input type="date" className="form-control"
                     value={hasta} onChange={e=>setHasta(e.target.value)}/>
            </div>
            <div className="col-auto align-self-end">
              <button className="btn btn-primary me-2"
                      onClick={consultar} disabled={loading}>
                {loading
                  ? <span className="spinner-border spinner-border-sm me-2"></span>
                  : <i className="fas fa-sync-alt me-1"></i>
                }
                Actualizar
              </button>
              <button className="btn btn-outline-secondary"
                      onClick={imprimir}>
                <i className="fas fa-print me-1"></i>Imprimir
              </button>
            </div>
          </div>
        </div>
      </div>

      <div className="card shadow">
        <div className="card-header bg-primary text-white">
          <h5 className="mb-0">
            <i className="fas fa-list me-2"></i>Reporte matriz de motivos
          </h5>
        </div>
        <div className="card-body">
          <div className="table-responsive">
            <table className="table table-hover align-middle mb-0 text-end">
              <thead className="bg-primary text-white" style={{ borderBottom: "4px solid #0d6efd" }}>
                <tr>
                  <th className="text-start">#</th>
                  <th className="text-start">Empleado</th>
                  {columnas.map(c=><th key={c}>{c}</th>)}
                  <th>Total</th>
                </tr>
              </thead>

              <tbody>
                {filas.length===0 &&
                  <tr>
                    <td colSpan={columnas.length+3} className="text-center py-3">
                      <div className="alert alert-info mb-0">
                        <i className="fas fa-info-circle me-2"></i>
                        Sin datos
                      </div>
                    </td>
                  </tr>
                }

                {filas.map((r,i)=>{
                  const totalFila = columnas.reduce(
                    (s,c)=>s+Number(r.valores[c]||0),0);

                  return(
                    <tr key={r.empleadoId}>
                      <td className="text-start">
                        <span className="badge bg-secondary">{i+1}</span>
                      </td>
                      <td className="text-start">{r.empleado}</td>
                      {columnas.map(c=>
                        <td key={c}>
                          <span className="badge bg-light text-dark">
                            {Number(r.valores[c]||0).toFixed(2)}
                          </span>
                        </td>
                      )}
                      <td>
                        <span className="badge bg-info text-dark">
                          {totalFila.toFixed(2)}
                        </span>
                      </td>
                    </tr>
                  );
                })}
              </tbody>

              {filas.length>0 &&
                <tfoot className="fw-bold">
                  <tr>
                    <td colSpan="2" className="text-start">TOTAL</td>
                    {columnas.map(c=>
                      <td key={c}>
                        <span className="badge bg-success">
                          {totCol[c].toFixed(2)}
                        </span>
                      </td>
                    )}
                    <td>
                      <span className="badge bg-primary">
                        {Object.values(totCol).reduce((s,v)=>s+v,0).toFixed(2)}
                      </span>
                    </td>
                  </tr>
                </tfoot>
              }
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}
