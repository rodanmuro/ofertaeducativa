/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa2;

/**
 *
 * @author Usuario
 */
public class Salon {
    String NOMBRE = "";
    int CUPO = 0;
    String OBSERVACION = "";

    public Salon(String nombre, int cupo, String observacion) {
        NOMBRE = nombre;
        CUPO = cupo;
        OBSERVACION = observacion;
    }
    
    public void setNombre(String nombre){
        NOMBRE = nombre;
    }
    
    public void setCupo(int cupo){
        CUPO = cupo;
    }
    
    public void setObservacion(String observacion){
        NOMBRE = observacion;
    }
    
    public String getNombre(){
        return NOMBRE;
    }
    
    public int getCupo(){
        return CUPO;
    }
    
    public String getObservacion(){
        return OBSERVACION;
    }
    
}
