import React, { useState } from "react";
import { Button, Modal, Badge } from "react-bootstrap";
// import { MessageCircle } from "lucide-react"; // O usa Font Awesome
import ChatPage from "../pages/ChatPage"; // Importa tu chat aquí

export default function FloatingChatButton() {
  const [show, setShow] = useState(false);
  // Puedes usar un estado para notificaciones (ejemplo estático)
  const [hasNew, setHasNew] = useState(false);

  return (
    <>
      {/* Botón flotante */}
      <Button
        onClick={() => { setShow(true); setHasNew(false); }}
        style={{
          position: "fixed",
          bottom: "30px",
          right: "30px",
          zIndex: 9999,
          borderRadius: "50%",
          width: "64px",
          height: "64px",
          boxShadow: "0 4px 24px rgba(0,0,0,0.25)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          fontSize: "2.3rem",
          transition: "transform 0.18s",
          background: "linear-gradient(135deg, #0d6efd, #0dcaf0)",
        }}
        className="shadow-lg floating-chat-btn"
        aria-label="Abrir chat de soporte"
      >
        {/* Ícono Font Awesome (puedes cambiar por MessageCircle si prefieres) */}
        <i className="fas fa-comments" style={{ color: "#fff" }}></i>
        {hasNew && (
          <Badge
            bg="danger"
            style={{
              position: "absolute",
              top: 8,
              right: 10,
              fontSize: 11,
              borderRadius: "50%",
              minWidth: 18,
              minHeight: 18,
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              boxShadow: "0 1px 8px rgba(0,0,0,0.18)",
            }}
            pill
          >¡1!</Badge>
        )}
      </Button>

      {/* Modal del chat */}
      <Modal
        show={show}
        onHide={() => setShow(false)}
        size="md"
        backdrop="static"
        keyboard={false}
        centered
        contentClassName="chat-modal"
      >
        <Modal.Header closeButton className="bg-primary text-white">
          <Modal.Title>
            <i className="fas fa-headset me-2"></i>
            Soporte en línea
          </Modal.Title>
        </Modal.Header>
        <Modal.Body style={{ padding: 0, background: "#f7faff" }}>
          <ChatPage />
        </Modal.Body>
      </Modal>

      {/* Efecto animado al botón */}
      <style>
        {`
          .floating-chat-btn:hover {
            transform: scale(1.08) rotate(-8deg);
            box-shadow: 0 8px 36px rgba(13,110,253,0.22);
          }
          .chat-modal .modal-content {
            border-radius: 20px;
            overflow: hidden;
            box-shadow: 0 8px 48px rgba(13,110,253,0.09);
          }
          .chat-modal .modal-header {
            border-radius: 20px 20px 0 0;
          }
        `}
      </style>
    </>
  );
}
