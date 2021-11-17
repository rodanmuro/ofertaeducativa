/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa;

/**
 *
 * @author Usuario
 */
public class ArregloOcupaciones {
    /**
     * Por defecto hora inicial para el arreglo de ocupaciones son las 700,
     * es decir, las 7AM. La hora siempre se representa como un entero.
     */
    int HORA_INICIAL = 700;
    /**
     * Por defecto hora final para el arreglo de ocupaciones son las 2200,
     * es decir, las 10PM o 22 horas en horario militar. La hora siempre se representa como un entero.
     */
    int HORA_FINAL = 2200;
    /**
     * El sector hace referencia a los minutos que dura cada una de las divisiones de
     * toda esta jornada que por defecto va desde las 7AM a las 10PM. Por defecto el valor
     * es 15 minutos. Para toda la jornada son un total de 60 sectores
     */
    int DURACION_SECTOR_MINUTOS = 15;
    
    /**
     * Total de sectores en los cuales se divida toda esta jornada de 7AM a 10PM
     */
    int TOTAL_SECTORES = ((HORA_FINAL - HORA_INICIAL) / 100) * (60 / DURACION_SECTOR_MINUTOS);
    
    /**
     * Esta es una secuencia de enteros en donde cada elemento va en el siguiente orden
     * 700,715,745,800...2145. Se inicializa en el constructor
     */
    int[] secuenciaSectores = new int[TOTAL_SECTORES];
    /**
     * Arreglo de enteros iguales a cero inicializados en el constructor
     */
    int[] posicionesOcupadas = new int[TOTAL_SECTORES];

    int HORA_INICIO_CLASE = 0;
    int DURACION = 0;
    
    /**
     * Esta clase genera un arreglo de enteros con unos y ceros
     * Los unos representan que un sector dentro de una jornada está ocupado
     * Se hace como apoyo con el fin de posteriormente visualizar más fácilmente
     * las horas ocupadas para un docente en un día
     * 
     * Es muy importante tener en cuenta que sólo se puede evaluar un docente para
     * una fecha dada.
     * 
     * Si bien acá no se tiene en cuenta la fecha en si, sino el inicio de una hora como tal
     * esta clase sólo tiene coherencia en la medida que se evalúa para un docente y una fecha
     * dada
     */
    public ArregloOcupaciones() {
        inicializarPosicionesOcupadas();
        inicializarArregloSectores();
    }

    public ArregloOcupaciones(int hora_Inicial, int hora_Final, int duracion_Sector_Minutos) {
        HORA_INICIAL = hora_Inicial;
        HORA_FINAL = hora_Final;
        DURACION_SECTOR_MINUTOS = duracion_Sector_Minutos;
        TOTAL_SECTORES = ((HORA_FINAL - HORA_INICIAL) / 100) * (60 / DURACION_SECTOR_MINUTOS);

        secuenciaSectores = new int[TOTAL_SECTORES];
        posicionesOcupadas = new int[TOTAL_SECTORES];
        
        inicializarPosicionesOcupadas();
        inicializarArregloSectores();
    }
    
    public int[] getArregloPosicionesOcupadas(){
        return posicionesOcupadas;
    }
    
    public int[] getArregloSectores(){
        return secuenciaSectores;
    }

    private void inicializarPosicionesOcupadas() {
        for (int i = 0; i < posicionesOcupadas.length; i++) {
            posicionesOcupadas[i] = 0;
        }
    }

    private void inicializarArregloSectores() {
        int horaInicial = HORA_INICIAL;
        for (int i = 0; i < secuenciaSectores.length; i++) {
            secuenciaSectores[i] = horaInicial;
            horaInicial = horaInicial + 15;
            if (horaInicial % 100 >= 60) {
                horaInicial = (horaInicial + 100) - (horaInicial % 100);
            }
        }
    }
    
    /**
     * El grupo se da acorde a la siguiente organización
     * 0,1,2,3 (grupo 0)
     * 4,5,6,7 (grupo 1)
     * ...
     * Para el índice cero están las 700, perteneciendo éste 700 al primer grupo
     * @param hora
     * @return Grupo al cual pertenece la hora
     */
    private int grupoHora(int hora) {
        int grupo = 0;

        grupo = Math.round((hora - HORA_INICIAL) / 100);

        return grupo;
    }
    
    /**
     * Luego de que una hora está en un grupo, también es necesario saber
     * a cuál índice pertenece. Por ejemplo
     * 0:700,1:715,2:730,3:745
     * 4:800,5:815,6:830,7:845
     * 
     * En el caso de las 845 está en el grupo 1 (esto es base 0) y en el índice 3
     * dentro de ese grupo
     * @param hora
     * @return 
     */
    private int indiceGrupo(int hora) {
        int indiceGrupo = 0;

        int grupoHora = grupoHora(hora);

        indiceGrupo = ((hora - grupoHora * 100) - HORA_INICIAL) / 15;

        return indiceGrupo;
    }
    
    /**
     * Toma una hora y retorna su índice dentro de un grupo
     * @param hora
     * @return 
     */
    private int horaAIndice(int hora) {
        int indice = 0;

        indice = indiceGrupo(hora);

        return indice;
    }
    
    /**
     * Toma una hora y retorna la posición absoluta dentro de un grupo de sectores
     * En el caso por defecto se tienen un total de 60 sectores.
     * las 815 por ejemplo están en la posición 5 dentro de esos sectores.
     * @param hora
     * @return 
     */
    private int horaAPosicion(int hora) {
        int posicion = 0;

        posicion = horaAIndice(hora) + 4 * grupoHora(hora);

        return posicion;

    }

    public static String arregloAString(int[] arreglo) {
        String arregloString = "";

        for (int i = 0; i < arreglo.length; i++) {
            if (i < arreglo.length - 1) {
                arregloString = arregloString + i + ":" + arreglo[i] + ",";
                if (i % 10 == 0 && i != 0) {
                    arregloString = arregloString + "\n";
                }
            } else {
                arregloString = arregloString + i + ":" + arreglo[i];
            }
        }

        return arregloString;
    }

    public static String arregloAStringSectorOcupacion(int[] sector, int[] ocupacion) {
        String arregloString = "";
        String color = "";

        for (int i = 0; i < sector.length; i++) {
            if (i < sector.length - 1) {
                arregloString = arregloString + color + ocupacion[i] + ":" + sector[i] + ",";
                if (i % 10 == 0 && i != 0) {
                    arregloString = arregloString + "\n";
                }
            } else {
                arregloString = arregloString + color + ocupacion[i] + ":" + sector[i];
            }
        }

        return arregloString;
    }
    
    /**
     * Toma una hora inicial (la cual es un valor entero, por ejemplo 815) 
     * y agrega unos en las horas ocupadas dentro de los 60 sectores
     * por defecto. Esto lo hace modificando el arreglo posicionesOcupadas, tomando la hora inicial
     * encontrando su posición dentro de los sectores, agregando un 1 allí y luego tantos unos como
     * duracion/DURACION_SECTOR_MINUTOS haya
     * @param duracion
     * @param horaInicioClase 
     */
    public void agregarOcupaciones(int duracion, int horaInicioClase) {
        int posicion = horaAPosicion(horaInicioClase);
        int numeroEspacios = duracion / DURACION_SECTOR_MINUTOS;

        for (int i = posicion; i < posicion + numeroEspacios; i++) {
            posicionesOcupadas[i] = 1;
        }
    }

}
