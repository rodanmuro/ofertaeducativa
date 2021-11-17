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
public class Estudiante {
    String NOMBRE;
    int ID;
    String PROGRAMA;

    public Estudiante() {
    }
    
    public void setNombre(String nombre){
        NOMBRE = nombre;
    }
    
    public void setId(int id){
        ID = id;
    }
    
    public void setPrograma(String programa){
        PROGRAMA = programa;
    }
    
    public String  getNombre(String nombre){
        return NOMBRE;
    }
    
    public int getId(int id){
        return ID;
    }
    
    public String getPrograma(String programa){
        return PROGRAMA;
    }
    
}
