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
 * @author rodanmuro
 */
public class Grupo implements Serializable{

    String PROGRAMA = "";
    String SEMESTRE = "";
    Jornada JORNADA = null;
    ArrayList<Jornada> JORNADAS = null;
    
    /**
     * Esta clase se conforma por el programa que es una nomenclatura que define la carrera
     * por ejemplo, en uniminuto Administración de Empresas es AEMD
     * @param String programa
     * @param String semestre
     * @param String jornada 
     */
    public Grupo(String programa, String semestre, Jornada jornada) {
        PROGRAMA = programa;
        SEMESTRE = semestre;
        JORNADA = jornada;
    }

    public String getPrograma() {
        return PROGRAMA;
    }

    public String getSemestre() {
        return SEMESTRE;
    }

    public void setPrograma(String programa) {
        PROGRAMA = programa;
    }

    public void setSemestre(String semestre) {
        SEMESTRE = semestre;
    }

}
