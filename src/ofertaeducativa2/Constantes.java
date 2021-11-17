/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa2;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class Constantes implements Serializable{
    ArrayList<Integer> LISTADOINDICESALEATORIOOPTIMO;
    ArrayList<Sesion> SESIONES;
    
    public Constantes(){
        LISTADOINDICESALEATORIOOPTIMO = new ArrayList<Integer>();
        SESIONES = new ArrayList<Sesion>();
    }
    
}
