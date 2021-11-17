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
public class Asignatura implements Serializable {
    
    String NOMBRE = "";
    String ALFANUMERICO = "";
    String ALFA = "";
    String NUMERICO = "";
    String IDCRUCECOMPARTIDO="";
    int CREDITOS = 0;
    int NUMEROSESIONES = 0;

    public Asignatura(String nombre, String alfa, String numerico, int creditos, int numeroSesiones, String idCruceCompartido) {
        NOMBRE = nombre;
        ALFA = alfa;
        NUMERICO = numerico;
        ALFANUMERICO = alfa+""+numerico;
        CREDITOS = creditos;
        NUMEROSESIONES = numeroSesiones;
        IDCRUCECOMPARTIDO = idCruceCompartido;
    }
    
    public String getNombre(){
        return NOMBRE;
    }
    
    public String getAlfa(){
        return ALFA;
    }
    
    public String getNumerico(){
        return NUMERICO;
    }
    
    public String getAlfaNumerico(){
        return ALFANUMERICO;
    }
    
    public int getCreditos(){
        return CREDITOS;
    }
    
    public String getIdCruceCompartido(){
        return IDCRUCECOMPARTIDO;
    }
    
    public void setNombre(String nombre){
        NOMBRE = nombre;
    }
    
    public void setAlfa(String alfa){
        ALFA = alfa;
    }
    
    public void setNumerico(String numerico){
        NUMERICO = numerico;
    }
    
    public void setAlfaNumerico(String alfaNumerico){
        ALFANUMERICO = alfaNumerico;
    }
    
    public void setCreditos(int creditos){
        CREDITOS = creditos;
    }
    
}
