/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg_chat;

import javax.json.Json;
import javax.websocket.*;
import java.io.StringReader;
import javax.json.JsonObject;

public class cls_decoder_mensaje implements Decoder.Text<cls_mensaje>{
    @Override
    public cls_mensaje decode(String s) throws DecodeException {
    JsonObject obj = Json.createReader(new StringReader(s)).readObject();
    return new cls_mensaje(
        obj.getString("de"),
        obj.getString("para"),
        obj.getString("cuerpo")
    );
}
    @Override public boolean willDecode(String s){ return s!=null && !s.isEmpty(); }
    @Override public void init(EndpointConfig ec){}
    @Override public void destroy(){}
}
