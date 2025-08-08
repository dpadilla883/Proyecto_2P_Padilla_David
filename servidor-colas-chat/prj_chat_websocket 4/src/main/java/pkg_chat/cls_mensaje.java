/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg_chat;

/**
 *
 * @author David
 */


public class cls_mensaje {
    private String de;
    private String para;
    private String cuerpo;

    public cls_mensaje() {}
    public cls_mensaje(String de, String para, String cuerpo){
        this.de = de; this.para = para; this.cuerpo = cuerpo;
    }
    /* getters y setters */
    public String getDe(){return de;} public void setDe(String de){this.de=de;}
    public String getPara(){return para;} public void setPara(String p){this.para=p;}
    public String getCuerpo(){return cuerpo;} public void setCuerpo(String c){this.cuerpo=c;}
}
