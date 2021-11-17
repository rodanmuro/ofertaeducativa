/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa2;

import java.util.HashMap;

/**
 *
 * @author Usuario
 */
public class NumeroSesionesAsignaturaGrupo extends HashMap<String, Integer>{

    HashMap<String, Integer> LISTADOSESIONESPROGRAMADAS;

    public NumeroSesionesAsignaturaGrupo() {
        LISTADOSESIONESPROGRAMADAS = new HashMap<>();
    }

    private void agregarNuevoParAsignaturaGrupo(String key) {
        if (!LISTADOSESIONESPROGRAMADAS.containsKey(key)) {
            LISTADOSESIONESPROGRAMADAS.put(key, 0);
        }
    }

    public void sumarSesion(Asignatura asignatura, Grupo grupo) {
        String key = generarKey(asignatura, grupo);
        agregarNuevoParAsignaturaGrupo(key);
        int valorActual = this.LISTADOSESIONESPROGRAMADAS.get(key);
        LISTADOSESIONESPROGRAMADAS.put(key, valorActual + 1);
    }
    
    public void restarSesion(Asignatura asignatura, Grupo grupo) {
        String key = generarKey(asignatura, grupo);
        agregarNuevoParAsignaturaGrupo(key);
        int valorActual = this.LISTADOSESIONESPROGRAMADAS.get(key);
        LISTADOSESIONESPROGRAMADAS.put(key, valorActual -1);
    }
    
    private String generarKey(Asignatura asignatura, Grupo grupo){
        String key = asignatura.getNombre()+asignatura.getAlfaNumerico()
                +grupo.getPrograma()+grupo.getSemestre();
        return key;
    }
    
    public int getSesionesProgramadas(Asignatura asignatura, Grupo grupo){
        
        if(LISTADOSESIONESPROGRAMADAS.get(generarKey(asignatura, grupo))==null){
            return 0;
        }
        
        return LISTADOSESIONESPROGRAMADAS.get(generarKey(asignatura, grupo));
    }

}
