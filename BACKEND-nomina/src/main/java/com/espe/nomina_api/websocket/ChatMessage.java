/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.nomina_api.websocket;



import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChatMessage {

    private String de;     // remitente
    private String para;   // destino ("ALL" difusión)
    private String cuerpo; // texto
}
