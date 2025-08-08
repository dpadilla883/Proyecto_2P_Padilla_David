/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.nomina_api.websocket;


import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.time.LocalTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/chat/{user}")          // ← ruta definitiva
public class ChatEndpoint {

    private static final Set<Session> sesiones = ConcurrentHashMap.newKeySet();

    @OnOpen
    public void alAbrir(Session sesion, @PathParam("user") String user) {
        sesion.getUserProperties().put("user", user);
        sesiones.add(sesion);
        broadcast("🟢 " + user + " se conectó");
    }

    @OnMessage
    public void alRecibir(String texto, Session sesion) {
        String user = (String) sesion.getUserProperties().get("user");
        String msg  = "[" + LocalTime.now().withNano(0) + "] " + user + ": " + texto;
        broadcast(msg);
    }

    @OnClose
    public void alCerrar(Session sesion) {
        sesiones.remove(sesion);
        broadcast("🔴 " + sesion.getUserProperties().get("user") + " salió");
    }

    @OnError
    public void alError(Session s, Throwable e) {
        e.printStackTrace();
    }

    /* ——— util ——— */
    private void broadcast(String msg) {
        sesiones.forEach(s -> s.getAsyncRemote().sendText(msg));
    }
}
