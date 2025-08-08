/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


(()=> {
  "use strict";
  let ws, yo;

  // Elementos
  const $ = id=>document.getElementById(id);
  const login  = $("login"), chat = $("chat");
  const sel    = $("selPara"), area = $("areaChat");

  $("btnLogin").onclick = ()=>{
    yo = $("txtNombre").value.trim();
    if(!yo){alert("Ingresa tu nombre");return;}

    const ctx = window.location.pathname.replace(/\/index\.html$/,'');
    ws = new WebSocket(`ws://${location.host}${ctx}/chat/${yo}`);

    ws.onopen    = ()=>{login.style.display="none"; chat.style.display="block";};
    ws.onerror   = e=>console.error(e);
    ws.onmessage = e=>{
       const m = JSON.parse(e.data);
       if(m.cuerpo?.startsWith("USERS:")){
          const users = m.cuerpo.substring(6).split(",");
          sel.innerHTML='<option value="ALL">(Todos)</option>';
          users.filter(u=>u!==yo).forEach(u=> sel.innerHTML+=`<option>${u}</option>`);
       }else{
          area.innerHTML += `<div><b>${m.de} ➜ ${m.para}:</b> ${m.cuerpo}</div>`;
          area.scrollTop = area.scrollHeight;
       }
    };
  };

  $("btnEnviar").onclick = ()=>{
    const msg = {de: yo, para: sel.value, cuerpo: $("txtMensaje").value};
    ws.send(JSON.stringify(msg));
    $("txtMensaje").value="";
  };
})();
