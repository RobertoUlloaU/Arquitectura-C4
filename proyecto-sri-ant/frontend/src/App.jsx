import React, {useState} from 'react';
import axios from 'axios';

function App() {
  const [email, setEmail] = useState('');
  const [ruc, setRuc] = useState('');
  const [placa, setPlaca] = useState('');
  const [contrib, setContrib] = useState(null);
  const [veh, setVeh] = useState(null);
  const [ant, setAnt] = useState(null);

  const verificar = async () => {
    try {
      const existe = await axios.get(`http://localhost:8080/api/existe-ruc/${ruc}`);
      if (existe.data.existe) {
        const c = await axios.get(`http://localhost:8080/api/contribuyente/${ruc}`);
        setContrib(c.data);
      } else {
        setContrib({error: 'No es contribuyente'});
      }
    } catch (err) {
      setContrib({error: 'Error al consultar SRI'});
    }
  };

  const buscarVehiculo = async () => {
    try {
      const v = await axios.get(`http://localhost:8080/api/vehiculo/${placa}`);
      setVeh(v.data);
    } catch (err) {
      setVeh({error: 'Error al consultar vehiculo'});
    }
  };

  const consultarAnt = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/ant/puntos', { params: { tipoIdentificacion: 'CED', identificacion: ruc, placa } });
      setAnt(res.data.raw);
    } catch (err) {
      setAnt('Error al consultar ANT');
    }
  };

  return (
    <div style={{maxWidth:800, margin:'2rem auto', fontFamily:'Arial'}}>
      <h2>Sistema SRI - ANT (skeleton)</h2>
      <div style={{marginBottom:10}}>
        <label>Email: </label>
        <input value={email} onChange={e=>setEmail(e.target.value)} placeholder="correo@ejemplo.com" />
      </div>
      <div style={{marginBottom:10}}>
        <label>RUC / Cédula: </label>
        <input value={ruc} onChange={e=>setRuc(e.target.value)} placeholder="RUC o cédula" />
        <button onClick={verificar} style={{marginLeft:8}}>Verificar RUC</button>
      </div>
      {contrib && <pre>{JSON.stringify(contrib, null, 2)}</pre>}

      <div style={{marginTop:20}}>
        <label>Placa: </label>
        <input value={placa} onChange={e=>setPlaca(e.target.value)} placeholder="AAA000" />
        <button onClick={buscarVehiculo} style={{marginLeft:8}}>Buscar Vehículo</button>
      </div>
      {veh && <pre>{JSON.stringify(veh, null, 2)}</pre>}

      <div style={{marginTop:20}}>
        <button onClick={consultarAnt}>Consultar Puntos ANT</button>
      </div>
      {ant && <pre style={{whiteSpace:'pre-wrap'}}>{typeof ant === 'string' ? ant : JSON.stringify(ant, null,2)}</pre>}
    </div>
  );
}

export default App;
