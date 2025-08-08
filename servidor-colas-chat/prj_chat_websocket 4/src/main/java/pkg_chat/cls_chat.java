/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg_chat;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
     // importa el configurator que habilita CORS

@ServerEndpoint(
    value        = "/chat/{user}",
    decoders     = cls_decoder_mensaje.class,
    encoders     = cls_encoder_mensaje.class,
    configurator = cls_configurator_chat.class
)
public class cls_chat {

    private static final Set<Session> sessions =
        Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session, @PathParam("user") String user) {
        session.getUserProperties().put("user", user);
        sessions.add(session);
        System.out.println("Nuevo usuario conectado: " + user);
    }

    @OnMessage
    public void onMessage(Session session, cls_mensaje msg) throws IOException {
        // Usa los getters correctos:
        String from   = msg.getDe();
        String to     = msg.getPara();
        String cuerpo = msg.getCuerpo();

        System.out.printf("Mensaje de %s ➜ %s: %s%n", from, to, cuerpo);

        synchronized (sessions) {
            for (Session s : sessions) {
                String target = (String) s.getUserProperties().get("user");
                if ("ALL".equals(to) || target.equals(to)) {
                    try {
                        s.getBasicRemote().sendObject(msg);
                    } catch (EncodeException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        String user = (String) session.getUserProperties().get("user");
        sessions.remove(session);
        System.out.println("Usuario desconectado: " + user);
    }

    @OnError
    public void onError(Session session, Throwable ex) {
        ex.printStackTrace();
    }
}
