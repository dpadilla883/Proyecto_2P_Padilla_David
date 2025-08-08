/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg_chat;

import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;

/**
 * Configurator para habilitar CORS en el handshake de WebSocket.
 */
public class cls_configurator_chat extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig config,
                                HandshakeRequest request,
                                HandshakeResponse response) {
        // Permite conexiones desde cualquier origen. 
        // Para restringir a tu React en localhost:5173, reemplaza "*" por "http://localhost:5173"
        response.getHeaders().put("Access-Control-Allow-Origin",
            java.util.Collections.singletonList("*"));
    }
}
