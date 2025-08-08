import React, { useRef, useState, useEffect } from "react";
import { Button, Form, InputGroup, Card, Badge, ListGroup, Row, Col } from "react-bootstrap";

export default function ChatPage() {
  const [username, setUsername] = useState("");
  const [login, setLogin] = useState(false);
  const [messages, setMessages] = useState([]);
  const [text, setText] = useState("");
  const [to, setTo] = useState("ALL");
  const [usuarios, setUsuarios] = useState(["ALL"]);
  const [status, setStatus] = useState("offline");
  const ws = useRef(null);
  const chatEndRef = useRef(null);

  // Conectar WS y recibir mensajes
  useEffect(() => {
    if (login && username) {
      const url = `ws://localhost:9090/prj_chat_websocket/chat/${username}`;
      ws.current = new WebSocket(url);

      ws.current.onopen = () => setStatus("online");
      ws.current.onclose = () => setStatus("offline");
      ws.current.onerror = () => setStatus("offline");

      ws.current.onmessage = (evt) => {
        let incoming;
        try {
          const data = JSON.parse(evt.data);
          incoming = {
            from: data.de,
            to: data.para,
            text: data.cuerpo,
          };
          // Nuevo usuario detectado
          if (
            data.de &&
            data.de !== username &&
            !usuarios.includes(data.de)
          ) {
            setUsuarios((u) => [...new Set([...u, data.de])]);
          }
        } catch {
          incoming = { from: "sistema", to: "ALL", text: evt.data };
        }
        setMessages((m) => [...m, incoming]);
      };

      return () => ws.current.close();
    }
  }, [login, username, usuarios]);

  // Scroll al final cada vez que cambian mensajes
  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages, to]);

  // Enviar mensaje
  const sendMessage = (e) => {
    e.preventDefault();
    if (!text.trim() || status !== "online") return;
    const payload = { de: username, para: to, cuerpo: text.trim() };
    ws.current.send(JSON.stringify(payload));
    setText("");
  };

  // Si no has hecho login, muestra la tarjeta de acceso
  if (!login) {
    return (
      <div className="container py-5">
        <Card className="p-4 mx-auto shadow" style={{ maxWidth: 400 }}>
          <h3 className="mb-3 text-center">
            <i className="fas fa-comments text-primary me-2"></i>
            Acceder al Chat
          </h3>
          <InputGroup className="my-3">
            <Form.Control
              placeholder="Tu nombre"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              autoFocus
            />
            <Button
              variant="primary"
              onClick={() => setLogin(true)}
              disabled={!username}
            >
              Entrar
            </Button>
          </InputGroup>
        </Card>
      </div>
    );
  }

  // Filtra mensajes según “to” seleccionado
  const visibles = messages.filter(
    (m) => to === "ALL" || m.from === to || m.to === to
  );

  return (
    <div className="container py-4">
      <Card className="p-3 shadow-lg" style={{ maxWidth: 600, margin: "0 auto" }}>
        <Row>
          <Col xs={8}>
            <div className="d-flex justify-content-start align-items-center mb-2 gap-2">
              <i className="fas fa-comments fa-lg text-primary"></i>
              <h5 className="mb-0">Chat</h5>
              <Badge bg={status === "online" ? "success" : "secondary"} className="ms-2">
                {status}
              </Badge>
            </div>
          </Col>
          <Col xs={4} className="d-flex align-items-center justify-content-end">
            <span className="text-muted" style={{ fontSize: 13 }}>
              <i className="fas fa-user me-1"></i> {username}
            </span>
          </Col>
        </Row>

        <Form.Select
          className="mb-2"
          value={to}
          onChange={(e) => setTo(e.target.value)}
          style={{ maxWidth: 250 }}
        >
          {usuarios.map((u) => (
            <option key={u} value={u}>
              {u === "ALL" ? "Todos" : u}
            </option>
          ))}
        </Form.Select>

        {/* Área de mensajes */}
        <div
          style={{
            minHeight: 220,
            maxHeight: 300,
            overflowY: "auto",
            background: "#f7faff",
            padding: 10,
            borderRadius: 8,
            border: "1px solid #e0e6ef"
          }}
        >
          {visibles.length === 0 && (
            <div className="text-center text-muted">Sin mensajes</div>
          )}

          {visibles.map((m, i) => {
            const esMio = m.from === username;
            return (
              <div
                key={i}
                className={`d-flex ${
                  esMio ? "justify-content-end" : "justify-content-start"
                } mb-2`}
                style={{ animation: "fadeIn 0.32s" }}
              >
                <div
                  style={{
                    background: esMio ? "#d1e7dd" : "#e9ecef",
                    padding: "8px 15px",
                    borderRadius: esMio ? "18px 18px 4px 18px" : "18px 18px 18px 4px",
                    maxWidth: "80%",
                    boxShadow: "0 2px 6px rgba(30,40,50,0.05)",
                    wordBreak: "break-word",
                  }}
                >
                  <small className="text-muted">
                    <i className={`fas fa-user-circle me-1 ${esMio ? "text-success" : "text-primary"}`}></i>
                    <strong>{m.from}</strong>
                    {" "}
                    <span style={{ fontWeight: 400 }}>→</span>{" "}
                    <em>{m.to}</em>
                  </small>
                  <div style={{ fontSize: 15 }}>{m.text}</div>
                </div>
              </div>
            );
          })}
          <div ref={chatEndRef} />
        </div>

        <Form onSubmit={sendMessage} className="mt-3 d-flex gap-2">
          <Form.Control
            value={text}
            onChange={(e) => setText(e.target.value)}
            placeholder="Escribe tu mensaje..."
            autoFocus
          />
          <Button type="submit" variant="primary" disabled={status !== "online"}>
            <i className="fas fa-paper-plane me-1"></i>Enviar
          </Button>
        </Form>
      </Card>

      {/* Animación de entrada */}
      <style>
        {`
        @keyframes fadeIn {
          from { opacity: 0; transform: translateY(20px);}
          to   { opacity: 1; transform: translateY(0);}
        }
        `}
      </style>
    </div>
  );
}
