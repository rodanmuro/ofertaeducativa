/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Usuario
 */
public class CalendarioTotalSemestre {

    ArrayList<Date> LISTADOTOTALHORASSEMESTRE_SINDIASPROHIBIDOS = new ArrayList<>();
    /**
     * Esta constante incluye los días prohibidos, y es con el fin de facilitar
     * la programación de los horarios presenciales
     */
    ArrayList<Date> LISTADOTOTALHORASSEMESTRE = new ArrayList<>();
    ArrayList<Date> LISTADOFECHASPROHIBIDAS = new ArrayList<>();
    static int DURACIONHORAMININMA = 15;//en minutos

    public CalendarioTotalSemestre(Date fechaInicial, Date fechaFinal, ArrayList<Date> listadoFechasProhibidas, int duracionMinima) {
        inicializarConstantes(fechaInicial, fechaFinal, listadoFechasProhibidas, duracionMinima);
    }

    private void inicializarConstantes(Date fechaInicial, Date fechaFinal, ArrayList<Date> listadoFechasProhibidas, int duracionMinima) {
        Calendar fi = Calendar.getInstance();
        fi.setTime(normalizarFechaInicial(fechaInicial));

        Calendar ff = Calendar.getInstance();
        ff.setTime(normalizarFechaFinal(fechaFinal));

        Calendar f = Calendar.getInstance();
        f = fi;

        DURACIONHORAMININMA = duracionMinima;

        LISTADOFECHASPROHIBIDAS = new ArrayList<Date>();
        for (Date d : listadoFechasProhibidas) {
            LISTADOFECHASPROHIBIDAS.add(d);
        }

        while (f.before(ff)) {
            if (!esDiaProhibido(f.getTime())) {
                if (f.get(Calendar.HOUR_OF_DAY) > 6 && f.get(Calendar.HOUR_OF_DAY) < 22) {
                    LISTADOTOTALHORASSEMESTRE_SINDIASPROHIBIDOS.add(f.getTime());
                }
            }
            
            LISTADOTOTALHORASSEMESTRE.add(f.getTime());
            
            f.add(Calendar.MINUTE, DURACIONHORAMININMA);
        }
    }

    public ArrayList<Date> obtenerTodasLasHorasSemestreSinDiasProhibidos() {
        return LISTADOTOTALHORASSEMESTRE_SINDIASPROHIBIDOS;
    }
    
    /**
     * Devuelve un Arraylist<Date> con el total de fechas sin tener en
     * cuenta las fechas festivos, con la orientación de programar las presenciales
     * @return 
     */
    public ArrayList<Date> obtenerTodasLasHorasSemestre() {
        return LISTADOTOTALHORASSEMESTRE;
    }

    public Date normalizarFechaInicial(Date fechaInicial) {

        Calendar c = Calendar.getInstance();
        c.setTime(fechaInicial);
        c.set(Calendar.HOUR_OF_DAY, 7);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();

    }

    public Date normalizarFechaFinal(Date fechaFinal) {

        Calendar c = Calendar.getInstance();
        c.setTime(fechaFinal);
        c.set(Calendar.HOUR_OF_DAY, 22);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();

    }

    public boolean esDiaProhibido(Date fecha) {
        boolean esProhibido = false;

        Calendar c1 = Calendar.getInstance();
        c1.setTime(fecha);

        int dia = c1.get(Calendar.DATE);
        int mes = c1.get(Calendar.MONTH);
        int ano = c1.get(Calendar.YEAR);

        for (Date d : LISTADOFECHASPROHIBIDAS) {
            Calendar c2 = Calendar.getInstance();
            c2.setTime(d);

            int dia2 = c2.get(Calendar.DATE);
            int mes2 = c2.get(Calendar.MONTH);
            int ano2 = c2.get(Calendar.YEAR);

            if (dia == dia2
                    && mes == mes2
                    && ano == ano2) {
                return true;
            }

        }

        return esProhibido;
    }

}
