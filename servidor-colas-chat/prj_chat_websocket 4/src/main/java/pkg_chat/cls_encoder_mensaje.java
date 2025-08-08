/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg_chat;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.*;
import java.io.StringWriter;

public class cls_encoder_mensaje implements Encoder.Text<cls_mensaje>{
    @Override
    public String encode(cls_mensaje m){
        JsonObject obj = Json.createObjectBuilder()
            .add("de", m.getDe())
            .add("para", m.getPara())
            .add("cuerpo", m.getCuerpo())
            .build();
        StringWriter st = new StringWriter();
        Json.createWriter(st).write(obj);
        return st.toString();
    }
    @Override public void init(EndpointConfig ec) {}
    @Override public void destroy() {}
}
