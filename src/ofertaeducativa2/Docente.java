/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa2;

import java.io.Serializable;

/**
 *
 * @author rodanmuro
 */
public class Docente implements Serializable {

    String NOMBRE = "";

    public Docente(String nombre) {
        NOMBRE = nombre;
    }

    public String getNombre() {
        return NOMBRE;
    }

    public void setNombre(String nombre) {
        NOMBRE = nombre;
    }

}
