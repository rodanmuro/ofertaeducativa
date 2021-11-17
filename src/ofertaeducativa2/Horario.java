/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa2;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author rodanmuro
 */
public class Horario {

    ArrayList<Sesion> SESIONES;
    ArrayList<ArrayList<Date>> LISTADOFECHASPOSIBLESJORNADA;
    ArrayList<Integer> LISTADONUMERODIASJORNADA;//EL INDICE CORRESPONDE A LA CANTIDAD DE DIAS DE CADA JORNADA SEGUN EL ORDEN DE LISTADO DE JORNADAS
    ArrayList<ArrayList<Date>> LISTADODIASDIFERENTESJORNADA;
    ArrayList<Long> LISTADODURACIONTOTALMINUTOSJORNADA;
    ArrayList<ArrayList<Sesion>> LISTADOSESIONESPORFILA;
    NumeroSesionesAsignaturaGrupo NUMEROSESIONESASIGNATURAGRUPO;

    int UNIDADMINIMASESION = 45;
    int TOTALSESIONESAPROGRAMAR = 0;
    int TOTALSEMANASSEMESTRE = 17;

    String CADENACRUZADA = "cruzada";
    String CADENACOMPARTIDA = "compartida";

    CargaDeDatosExcel CDE;
    CalendarioTotalSemestre CT;

    boolean PROGRAMARVIRTUALES = false;

    /**
     * Es una clase que gestiona diferentes funciones entre las sesiones dadas
     * Por defecto inicializa un arreglo de sesiones, que representa el total de
     * sesiones de un horario completo para todos los docentes y grupos
     */
    public Horario(CalendarioTotalSemestre ct, CargaDeDatosExcel cde) {
        SESIONES = new ArrayList<Sesion>();
        CDE = cde;
        CT = ct;
        NUMEROSESIONESASIGNATURAGRUPO = new NumeroSesionesAsignaturaGrupo();
    }

    /**
     * Agrega una sesión al listado total de sesiones del horario
     *
     * @param s objeto sesion a programar
     */
    public void agregarSesion(Sesion s) {
        SESIONES.add(s);
    }

    /**
     * Devuelve el arraylist de sesiones, con todas las sesiones programadas
     * para este horario
     *
     * @return ArrayList<Sesion> con todas las sesiones del horario
     */
    public ArrayList<Sesion> getSesiones() {
        return SESIONES;
    }

    /**
     * Establece la duración mínima que puede tener una sesión en un horario
     *
     * @param d duración mínima de una sesión en minutos
     */
    public void setUnidadMinimaSesion(int d) {
        UNIDADMINIMASESION = d;
    }

    /**
     * Obtener la duracion mínima de una sesión en un horario
     *
     * @return int que representa la duración mínima de una sesión en un horario
     */
    public int getUnidadMinimaSesion() {
        return UNIDADMINIMASESION;
    }

    /**
     * Dos asignaturas son iguales si tienen igual alfanumerico e igual nombre
     *
     * @param a1
     * @param a2
     * @return verdadero si las asignaturas son iguales
     */
    public boolean asignaturasIguales(Asignatura a1, Asignatura a2) {
        boolean sonIguales = false;

        if (a1.getAlfaNumerico().equals(a2.getAlfaNumerico())
                && a1.getNombre().equals(a2.getNombre())) {
            sonIguales = true;
        }
        return sonIguales;
    }

    /**
     * Dos docentes son iguales si tienen el mismo nombre
     *
     * @param d1
     * @param d2
     * @return verdadero si los docentes son iguales
     */
    public boolean docentesIguales(Docente d1, Docente d2) {
        boolean sonIguales = false;

        if (d1.getNombre().equals(d2.getNombre())) {
            sonIguales = true;
        }
        return sonIguales;
    }

    /**
     * Dos grupos son iguales si son del mismo programa y semestre con su
     * respectiva letra
     *
     * @param g1
     * @param g2
     * @return true si los grupos son iguales
     */
    public static boolean gruposIguales(Grupo g1, Grupo g2) {
        boolean sonIguales = false;

        if (g1.getPrograma().equals(g2.getPrograma())
                && g1.getSemestre().equals(g2.getSemestre())) {
            sonIguales = true;
        }
        return sonIguales;
    }

    /**
     * Toma el listado de sesiones existentes para una asignatura con el grupo
     * dado
     *
     * @param asignatura objeto asignatura
     * @param grupo objeto grupo
     * @return ArrayList<Sesion> listado de sesiones para una asignatura con el
     * grupo dado
     */
    public ArrayList<Sesion> getSesionesAsignaturaGrupo(Asignatura asignatura, Grupo grupo) {
        ArrayList<Sesion> listado = new ArrayList<Sesion>();
        for (Sesion s : SESIONES) {
            if (asignaturasIguales(s.getAsignatura(), asignatura)
                    && gruposIguales(s.getGrupo(), grupo)) {
                listado.add(s);
            }
        }
        return listado;
    }

    /**
     * Función que obtiene el número de sesiones para una asignatura y un grupo
     *
     * @param asignatura
     * @param grupo
     * @return entero con la cantidad de sesiones para una asignatura y un grupo
     */
    public int getCantidadSesionesAsignaturaGrupo(Asignatura asignatura, Grupo grupo) {
        return getSesionesAsignaturaGrupo(asignatura, grupo).size();
    }

    /**
     * Esta función devuelve el total de horas que se le han programado a una
     * asignatura acorde con la duracion que se dé para la función. No será lo
     * mismo si se da una duración de 60 minutos, y si se da una duración de 45
     * minutos.
     *
     * @param asignatura
     * @param grupo
     * @param duracion entero en minutos con la duración mínima de una sesion de
     * clase
     * @return entero con la cantidad de horas que se han programda para una
     * asignatura en un grupo
     */
    public int getCantidadHorasAsignaturaGrupo(Asignatura asignatura, Grupo grupo, int duracion) {
        int cantidadHoras = 0;
        for (Sesion s : getSesionesAsignaturaGrupo(asignatura, grupo)) {
            cantidadHoras = cantidadHoras + s.getCantidadHorasSegunDuracion(duracion);
        }
        return cantidadHoras;
    }

    /**
     * Función que obtiene todas las sesiones para un grupo dado
     *
     * @param grupo
     * @return ArrayList de sesiones con todas las sesiones para el grupo
     */
    public ArrayList<Sesion> getSesionesGrupo(Grupo grupo) {
        ArrayList<Sesion> listado = new ArrayList<Sesion>();

        for (Sesion s : SESIONES) {
            if (gruposIguales(s.getGrupo(), grupo)) {
                listado.add(s);
            }
        }

        return listado;
    }

    public ArrayList<Sesion> getSesionesFecha(Date fecha) {
        ArrayList<Sesion> lsf = new ArrayList<Sesion>();

        Calendar cf = Calendar.getInstance();
        cf.setTime(fecha);
        int dia = cf.get(Calendar.DATE);
        int mes = cf.get(Calendar.MONTH);
        int ano = cf.get(Calendar.YEAR);

        for (Sesion s : SESIONES) {
            Calendar cfs = Calendar.getInstance();
            cfs.setTime(s.getFecha());

            int dias = cfs.get(Calendar.DATE);
            int mess = cfs.get(Calendar.MONTH);
            int anos = cfs.get(Calendar.YEAR);

            if (dia == dias && mes == mess && ano == anos) {
                lsf.add(s);
            }
        }

        return lsf;
    }

    public int getCantidadSesionesGrupo(Grupo grupo) {
        int cantidadSesiones = 0;

        cantidadSesiones = getSesionesGrupo(grupo).size();

        return cantidadSesiones;
    }

    public ArrayList<Sesion> getSesionesDocente(String docente) {
        ArrayList<Sesion> listado = new ArrayList<Sesion>();

        for (Sesion s : SESIONES) {
            if (s.getDocente().getNombre().equals(docente)) {
                listado.add(s);
            }
        }

        return listado;
    }

    public static ArrayList<Sesion> getSesionesDocente(String docente, ArrayList<Sesion> sesiones) {
        ArrayList<Sesion> listado = new ArrayList<Sesion>();

        for (Sesion s : sesiones) {
            if (s.getDocente().getNombre().equals(docente)) {
                listado.add(s);
            }
        }

        return listado;
    }

    public static ArrayList<Sesion> getSesionesDocenteFecha(String docente, Date fecha, ArrayList<Sesion> sesiones) {
        ArrayList<Sesion> listado = new ArrayList<Sesion>();

        Calendar cf = Calendar.getInstance();
        cf.setTime(fecha);
        int dia = cf.get(Calendar.DATE);
        int mes = cf.get(Calendar.MONTH);
        int ano = cf.get(Calendar.YEAR);

        for (Sesion s : sesiones) {
            Calendar cfs = Calendar.getInstance();
            cfs.setTime(s.getFecha());

            int dias = cfs.get(Calendar.DATE);
            int mess = cfs.get(Calendar.MONTH);
            int anos = cfs.get(Calendar.YEAR);

            if (s.getDocente().getNombre().equals(docente)
                    && dia == dias && mes == mess && ano == anos) {
                listado.add(s);
            }
        }

        return listado;
    }

    public static ArrayList<Sesion> getSesionesGrupoFecha(Grupo grupo, Date fecha, ArrayList<Sesion> sesiones) {
        ArrayList<Sesion> listado = new ArrayList<Sesion>();

        Calendar cf = Calendar.getInstance();
        cf.setTime(fecha);
        int dia = cf.get(Calendar.DATE);
        int mes = cf.get(Calendar.MONTH);
        int ano = cf.get(Calendar.YEAR);

        for (Sesion s : sesiones) {
            Calendar cfs = Calendar.getInstance();
            cfs.setTime(s.getFecha());

            int dias = cfs.get(Calendar.DATE);
            int mess = cfs.get(Calendar.MONTH);
            int anos = cfs.get(Calendar.YEAR);

            if (s.getGrupo().getPrograma().equals(grupo.getPrograma())
                    && s.getGrupo().getSemestre().equals(grupo.getSemestre())
                    && dia == dias && mes == mess && ano == anos) {
                listado.add(s);
            }
        }

        return listado;
    }

    public int getCantidadSesionesDocente(String docente) {
        int cantidadDocentes = 0;

        cantidadDocentes = getSesionesDocente(docente).size();

        return cantidadDocentes;
    }

    /**
     * Si se tiene una sesion uno con hora inicial y final a,b y otra sesion con
     * hora inicial y final c,d se dice que se cruzan si se cumple que c-a>0 y
     * c-b<0 o bien a-d<0 y d-b<0 o bien a=c Es decir si alguna de las horas de
     * inicia o final del uno están dentro del intervalo del otro Las sesiones 1
     * y 2 son dos sesiones a comparar @param sesion1 @param sesion2 @return
     * true si las sesiones se cruza
     */
    public boolean cruceDocenteDosSesiones(Sesion sesion1, Sesion sesion2) {

        boolean hayCruce = false;

        if (docentesIguales(sesion1.getDocente(), sesion2.getDocente())) {
            Date hi1, hf1, hi2, hf2;

            hi1 = sesion1.horaInicioSesion();
            hi2 = sesion2.horaInicioSesion();

            hf1 = sesion1.horaFinalSesion();
            hf2 = sesion2.horaFinalSesion();

            if (hi1.equals(hi2)) {
                return true;
            }
//            long dif1 = hi2.getTime() - hi1.getTime();
//            long dif2 = hi2.getTime() - hf1.getTime();
            if ((hi2.getTime() - hi1.getTime()) > 0
                    && (hi2.getTime() - hf1.getTime()) < 0) {
                return true;
            }
//            long dif3 = hi1.getTime() - hf2.getTime();
//            long dif4 = hf2.getTime() - hf1.getTime();
            if ((hi1.getTime() - hf2.getTime()) < 0
                    && (hf2.getTime() - hf1.getTime()) < 0) {
                return true;
            }
            if ((hi1.getTime() - hi2.getTime()) > 0
                    && (hi1.getTime() - hf2.getTime()) < 0) {
                return true;
            }
            if (hf1.getTime() - hi2.getTime() > 0 && hf1.getTime() - hf2.getTime() < 0) {
                return true;
            }

        }

        return hayCruce;
    }

    /**
     * Si se tiene un sesion uno con hora inicial y final a,b y otro sesion con
     * hora inicial y final c,d se dice que se cruzan si se cumple que c-a>0 y
     * c-b<0 o bien a-d<0 y d-b<0 o bien a=c Es decir si alguna de las horas de
     * inicia o final del uno están dentro del intervalo del otro El cruce entre
     * dos sesiones por grupo se da cuando a un grupo ya se le ha programado una
     * sesión en una hora dada, o el tiempo de la sesión a programar se cruza
     * con otra @param sesion1 @param sesion2 @return true si hay cruce de grupo
     */
    public boolean cruceGrupoDosSesiones(Sesion sesion1, Sesion sesion2) {
        boolean hayCruce = false;

        if (gruposIguales(sesion1.getGrupo(), sesion2.getGrupo())) {//??
            Date hi1, hf1, hi2, hf2;

            hi1 = sesion1.horaInicioSesion();
            hi2 = sesion2.horaInicioSesion();

            hf1 = sesion1.horaFinalSesion();
            hf2 = sesion2.horaFinalSesion();

            if (hi1.equals(hi2)) {
                return true;
            }
            if ((hi2.getTime() - hi1.getTime()) > 0
                    && (hi2.getTime() - hf1.getTime()) < 0) {
                return true;
            }
            if ((hi1.getTime() - hf2.getTime()) < 0
                    && (hf2.getTime() - hf1.getTime()) < 0) {
                return true;
            }
            if ((hi1.getTime() - hi2.getTime()) > 0
                    && (hi1.getTime() - hf2.getTime()) < 0) {
                return true;
            }
            if (hf1.getTime() - hi2.getTime() > 0 && hf1.getTime() - hf2.getTime() < 0) {
                return true;
            }
        }
        return hayCruce;
    }

    /**
     * Esta función compara una sesión dada con un conjunto de sesiones
     *
     * @param sesion
     * @param sesiones
     * @return true si hay cruce entre esa sesión y las comparadas
     */
    public boolean cruceDocenteSesionTotalSesiones(Sesion sesion, ArrayList<Sesion> sesiones) {
        for (Sesion sh : sesiones) {
            if (cruceDocenteDosSesiones(sesion, sh)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Esta función determina si hay cruce entre la sesión de un grupo, y un
     * conjunto de sesiones dadas
     *
     * @param sesion
     * @param sesiones
     * @return true si hay cruce entre la sesión, y las sesiones a comparar
     */
    public boolean cruceGrupoSesionTotalSesiones(Sesion sesion, ArrayList<Sesion> sesiones) {
        for (Sesion sh : sesiones) {
            if (cruceGrupoDosSesiones(sesion, sh)) {
                return true;
            }
        }
        return false;
    }

    public boolean cruceSalonDosSesiones(Sesion sesion1, Sesion sesion2) {
        boolean hayCruce = false;

        if (gruposIguales(sesion1.getGrupo(), sesion2.getGrupo())) {//??
            Date hi1, hf1, hi2, hf2;

            hi1 = sesion1.horaInicioSesion();
            hi2 = sesion2.horaInicioSesion();

            hf1 = sesion1.horaFinalSesion();
            hf2 = sesion2.horaFinalSesion();

            if (hi1.equals(hi2)) {
                return true;
            }
//            long dif1 = hi2.getTime() - hi1.getTime();
//            long dif2 = hi2.getTime() - hf1.getTime();
            if ((hi2.getTime() - hi1.getTime()) > 0
                    && (hi2.getTime() - hf1.getTime()) < 0) {
                return true;
            }
//            long dif3 = hi1.getTime() - hf2.getTime();
//            long dif4 = hf2.getTime() - hf1.getTime();
            if ((hi1.getTime() - hf2.getTime()) < 0
                    && (hf2.getTime() - hf1.getTime()) < 0) {
                return true;
            }
            if ((hi1.getTime() - hi2.getTime()) > 0
                    && (hi1.getTime() - hf2.getTime()) < 0) {
                return true;
            }
            if (hf1.getTime() - hi2.getTime() > 0 && hf1.getTime() - hf2.getTime() < 0) {
                return true;
            }
        }

        return hayCruce;
    }

    public boolean cruceDiaSesion(Sesion sesion, ArrayList<Sesion> sesiones) {
//        Docente docente = sesion.getDocente();
//        Asignatura asignatura = sesion.getAsignatura();
//        Grupo grupo = sesion.getGrupo();

        for (int i = 0; i < sesiones.size(); i++) {
            if (docentesIguales(sesion.getDocente(), sesiones.get(i).getDocente())
                    && asignaturasIguales(sesion.getAsignatura(), sesiones.get(i).getAsignatura())
                    && gruposIguales(sesion.getGrupo(), sesiones.get(i).getGrupo())) {
                if (fechasMismoDia(sesion.getFecha(), sesiones.get(i).getFecha())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean cruceSemanaSesion(Sesion sesion, ArrayList<Sesion> sesiones) {
//        Docente docente = sesion.getDocente();
//        Asignatura asignatura = sesion.getAsignatura();
//        Grupo grupo = sesion.getGrupo();

        for (int i = 0; i < sesiones.size(); i++) {
            if (docentesIguales(sesion.getDocente(), sesiones.get(i).getDocente())
                    && asignaturasIguales(sesion.getAsignatura(), sesiones.get(i).getAsignatura())
                    && gruposIguales(sesion.getGrupo(), sesiones.get(i).getGrupo())) {
                if (fechasMismaSemana(sesion.getFecha(), sesiones.get(i).getFecha())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Esta función determina que no se presente cruce entre dos sesiones
     * programadas antes de la periodidicad de la sesión, por ejemplo, que la
     * periodicidad de la sesión sea cada 7 dias y se programe cada dos dias, en
     * ese caso habría cruce de periodicidad
     *
     * @param sesion
     * @param sesiones
     * @param periodicidad
     * @return
     */
    public boolean crucePeriodicidadMateriaSesion(Sesion sesion, ArrayList<Sesion> sesiones, int periodicidad) {

//        Docente docente = sesion.getDocente();
//        Asignatura asignatura = sesion.getAsignatura();
//        Grupo grupo = sesion.getGrupo();
        for (int i = 0; i < sesiones.size(); i++) {
            if (docentesIguales(sesion.getDocente(), sesiones.get(i).getDocente())
                    && asignaturasIguales(sesion.getAsignatura(), sesiones.get(i).getAsignatura())
                    && gruposIguales(sesion.getGrupo(), sesiones.get(i).getGrupo())) {
                if (diferenciaDiasEntreDosFechas(sesion.getFecha(), sesiones.get(i).getFecha()) < periodicidad) {
                    return true;
                }
            }
        }

        return false;
    }

    public int diferenciaDiasEntreDosFechas(Date f1, Date f2) {
        Calendar cf1 = Calendar.getInstance();
        cf1.setTime(f1);

        Calendar cf2 = Calendar.getInstance();
        cf2.setTime(f2);

        return Math.abs(cf2.get(Calendar.DAY_OF_YEAR) - cf1.get(Calendar.DAY_OF_YEAR));
    }

    public boolean fechasMismoDia(Date f1, Date f2) {

        Calendar cf1 = Calendar.getInstance();
        cf1.setTime(f1);
        int diaf1 = cf1.get(Calendar.DATE);
        int mesf1 = cf1.get(Calendar.MONTH);
        int anof1 = cf1.get(Calendar.YEAR);

        Calendar cf2 = Calendar.getInstance();
        cf2.setTime(f2);
        int diaf2 = cf2.get(Calendar.DATE);
        int mesf2 = cf2.get(Calendar.MONTH);
        int anof2 = cf2.get(Calendar.YEAR);

        if (diaf1 == diaf2 && mesf1 == mesf2 && anof1 == anof2) {
            return true;
        }

        return false;
    }

    public boolean fechasMismaSemana(Date f1, Date f2) {
        Calendar cf1 = Calendar.getInstance();
        cf1.setTime(f1);

        Calendar cf2 = Calendar.getInstance();
        cf2.setTime(f2);

        if (cf1.getWeekYear() == cf2.getWeekYear()) {
            return true;
        }

        return false;
    }

    /**
     * Retorna el número de horas que hay en una sesión acorde con la duración
     * mínima dada
     *
     * @param sesion
     * @param duracionHora
     * @return int con la cantidad de horas que dura una sesión
     */
    public int getNumeroHorasSesion(Sesion sesion, int duracionHora) {
        return sesion.getDuracion() / duracionHora;
    }

    /**
     * Función que devuelve el listado de fechas válidas para una jornada dada
     * El arraylist listadoTotal, corresponde a TODAS las fechas para un
     * semestre desde el inicio hasta el fin del semestre, con una unidad mínima
     * de 15 minutos para iniciar una clase, dado que es el común divisor de 45
     * y 90 y 120, que son las duraciones mínimas que se presentarán en
     * Uniminuto para los primeros semestres y luego del 9
     *
     * @param listadoTotal
     * @param jornada
     * @return arraylist date con todas las fechas y las posibles horas para una
     * jornada dada
     */
    public ArrayList<Date> getFechasHorasValidasJornada(ArrayList<Date> listadoTotal, Jornada jornada) {
        ArrayList<Date> listadoFechas = new ArrayList<Date>();

        for (int i = 0; i < listadoTotal.size(); i++) {

            Calendar c = Calendar.getInstance();
            c.setTime(listadoTotal.get(i));
            int diaSemestre = c.get(Calendar.DAY_OF_WEEK);

            Date fechaValida = listadoTotal.get(i);

            for (TuplaDiaHoraInicialFinal t : jornada.getListadoTuplasDiaHoraInicialFinal()) {
                int diaJornada = t.getDia();

                if (diaJornada == diaSemestre
                        && horaEstaEntre(fechaValida, t)
                        && semanaClaseValida(jornada, fechaValida)
                        && fechaValida.after(jornada.getFechaInicial())
                        && fechaValida.before(jornada.getFechaFinal())) {
                    if (!horaMasDuracionSobrepasaFinalJornada(fechaValida, t, UNIDADMINIMASESION)) {
                        listadoFechas.add(fechaValida);
                    }
                }
            }

        }

        return listadoFechas;
    }

    /**
     * Función que devuelve el listado de fechas válidas para una jornada dada
     * El arraylist listadoTotal, corresponde a TODAS las fechas para un
     * semestre desde el inicio hasta el fin del semestre, con una unidad mínima
     * de 15 minutos para iniciar una clase, dado que es el común divisor de 45
     * y 90 y 120, que son las duraciones mínimas que se presentarán en
     * Uniminuto para los primeros semestres y luego del 9 Dado que esta función
     * coresponde a la presencial la diferencia con la función
     * getFechasHorasValidasJornada, está en que esta no valida la semana, sino
     * que devuelve las fechas del semestre completo
     *
     * @param listadoTotal
     * @param jornada
     * @return arraylist date con todas las fechas y las posibles horas para una
     * jornada dada
     */
    public ArrayList<Date> getFechasHorasValidasJornadaPresencial(ArrayList<Date> listadoTotal, Jornada jornada) {
        ArrayList<Date> listadoFechas = new ArrayList<Date>();

        for (int i = 0; i < listadoTotal.size(); i++) {

            Calendar c = Calendar.getInstance();
            c.setTime(listadoTotal.get(i));
            int diaSemestre = c.get(Calendar.DAY_OF_WEEK);

            Date fechaValida = listadoTotal.get(i);

            for (TuplaDiaHoraInicialFinal t : jornada.getListadoTuplasDiaHoraInicialFinal()) {
                int diaJornada = t.getDia();

                if (diaJornada == diaSemestre
                        && horaEstaEntre(fechaValida, t)
                        && fechaValida.after(jornada.getFechaInicial())
                        && fechaValida.before(jornada.getFechaFinal())) {
                    if (!horaMasDuracionSobrepasaFinalJornada(fechaValida, t, UNIDADMINIMASESION)) {
                        listadoFechas.add(fechaValida);
                    }
                }
            }

        }

        return listadoFechas;
    }

    public static boolean horaEstaEntre(Date fechaEvaluar, TuplaDiaHoraInicialFinal t) {
        boolean b = false;

        Calendar c = Calendar.getInstance();
        c.setTime(fechaEvaluar);

        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minutos = c.get(Calendar.MINUTE);

        TuplaDiaHoraInicialFinal tuplaAuxiliarHoraInicialFinal = new TuplaDiaHoraInicialFinal(t.getDia(), t.getHoraInicial(), t.getHoraFinal());

        tuplaAuxiliarHoraInicialFinal.setHoraInicial(
                new Hora(tuplaAuxiliarHoraInicialFinal.getHoraInicial().getHora() - 1,
                        tuplaAuxiliarHoraInicialFinal.getHoraInicial().getMinutos() + 59));

        Hora horaASerEvaluada = new Hora(hora, minutos);

        if (horaASerEvaluada.after(tuplaAuxiliarHoraInicialFinal.getHoraInicial()) && horaASerEvaluada.before(tuplaAuxiliarHoraInicialFinal.getHoraFinal())) {
            b = true;
        }

        return b;
    }

    public boolean horaMasDuracionSobrepasaFinalJornada(Date fechaEvaluar, TuplaDiaHoraInicialFinal t, int duracionMinutos) {
        boolean b = false;

        Calendar c = Calendar.getInstance();
        c.setTime(fechaEvaluar);

        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minutos = c.get(Calendar.MINUTE);

        c.add(Calendar.MINUTE, duracionMinutos);

        hora = c.get(Calendar.HOUR_OF_DAY);
        minutos = c.get(Calendar.MINUTE);

        TuplaDiaHoraInicialFinal tuplaAuxiliarHoraInicialFinal = new TuplaDiaHoraInicialFinal(t.getDia(), t.getHoraInicial(), t.getHoraFinal());

        tuplaAuxiliarHoraInicialFinal.setHoraInicial(
                new Hora(tuplaAuxiliarHoraInicialFinal.getHoraInicial().getHora() - 1,
                        tuplaAuxiliarHoraInicialFinal.getHoraInicial().getMinutos() + 59));

        Hora horaASerEvaluada = new Hora(hora, minutos);

        if (horaASerEvaluada.after(tuplaAuxiliarHoraInicialFinal.getHoraFinal())) {
            b = true;
        }

        return b;
    }

    /**
     * Dado un valor en cadena con el nombre en español del día (sin tener en
     * cuenta si está en mayúscula o minúscula) se devuelve el entero, según la
     * clase Calendar, siendo 1 para el domingo 2 para el lunes, 3 para el
     * martes, etcétera
     *
     * @param dia
     * @return
     */
    public static int deDiaCadenaAEntero(String dia) {
        int enteroDia = 0;

        dia = dia.toLowerCase();

        if (dia.equals("domingo")) {
            return 1;
        }
        if (dia.equals("lunes")) {
            return 2;
        }
        if (dia.equals("martes")) {
            return 3;
        }
        if (dia.equals("miércoles")) {
            return 4;
        }
        if (dia.equals("jueves")) {
            return 5;
        }
        if (dia.equals("viernes")) {
            return 6;
        }
        if (dia.equals("sábado")) {
            return 7;
        }

        return enteroDia;
    }

    /**
     * Esta función devuelve si una fecha dada está dentro de una semana válida
     * para una jornada, teniendo en cuenta la periodicidad de la jornada y su
     * fecha de inicio. Es decir si la periodicidad es cada 14 días se determina
     * si el día está dentro de una semana múltiplo de 14, de ser así, se
     * encuentra en una semana válida
     *
     * @param jornada
     * @param dia
     * @return
     */
    public boolean semanaClaseValida(Jornada jornada, Date dia) {
        boolean semanaValida = false;

        int periodicidad = jornada.getPeriodicidad();

        Calendar cDado = Calendar.getInstance();
        cDado.setTime(dia);

        cDado.set(Calendar.HOUR_OF_DAY, 0);
        cDado.set(Calendar.MINUTE, 0);
        cDado.set(Calendar.SECOND, 0);

        Calendar cInicioJornada = Calendar.getInstance();
        cInicioJornada.setTime(jornada.getFechaInicial());

        cInicioJornada.set(Calendar.HOUR_OF_DAY, 0);
        cInicioJornada.set(Calendar.MINUTE, 0);
        cInicioJornada.set(Calendar.SECOND, 0);

        int diasDiferencia = cDado.get(Calendar.DAY_OF_YEAR) - cInicioJornada.get(Calendar.DAY_OF_YEAR);

//        if (diasDiferencia % periodicidad == 0) {
//la fórmula a continuación se da para cuando se tienen varios días de la semana
//en una jornada. lo que se está haciendo es calcular que el residuo siempre esté durante 
//la primera parte de la semana; por ejemplo en el caso de cada 15 días, si se calcula solo el residuo
//para el primer día se tiene cero, en cambio con el margen se toma la jornada completa, durante esa primer semana
        if (diasDiferencia % periodicidad >= 0 && diasDiferencia % periodicidad <= 6) {
            semanaValida = true;
        }

        return semanaValida;
    }

    public boolean esFilaCruzada(CargaDeDatosExcel cde, int indice) {
        boolean cruzada = false;

        if (cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(indice).toLowerCase().indexOf(CADENACRUZADA) != -1) {
            return true;
        }

        return cruzada;
    }

    public boolean esFilaCompartida(CargaDeDatosExcel cde, int indice) {
        boolean cruzada = false;

        try {
            if (cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(indice).toLowerCase().indexOf(CADENACOMPARTIDA) != -1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cruzada;
    }

    public boolean esFilaCruzadaOCompartida(CargaDeDatosExcel cde, int indice) {
        boolean cruzadaCompartida = false;

        if (esFilaCruzada(cde, indice) || esFilaCompartida(cde, indice)) {
            return true;
        }

        return cruzadaCompartida;
    }

    public boolean esFilaVirtual(int i) {
        if (CDE.LISTADOOFERTAEDUCATIVA_VIRTUAL.get(i).trim().toLowerCase().equals("si")) {
            return true;
        }
        return false;
    }

    /**
     * Función que toma como argumentos el listado con todos los
     * idcruzadoscompartidos y la cadena que corresponde al cruzadocompartido
     * que se va a buscar en la lista
     *
     * @param listadoCruzadaCompartida
     * @param cadenaCruzadoCompartido
     * @return ArrayList<Integer> con los índices de los que están cruzados o
     * compartidos con el índice dada
     */
    public ArrayList<Integer> indicesCruzadoCompartido(ArrayList<String> listadoCruzadaCompartida, String cadenaCruzadoCompartido) {

        ArrayList<Integer> indices = new ArrayList<Integer>();

        for (int i = 0; i < listadoCruzadaCompartida.size(); i++) {
            if (listadoCruzadaCompartida.get(i).equals(cadenaCruzadoCompartido)) {
                indices.add(i);
            }
        }

        return indices;
    }

    /**
     * Primer tipo de asignación, se miran primero las cruzadas y las
     * compartidas Se recorre todo el listado de la oferta educativa, por cada
     * uno se mira si es cruzada o compartida, de serlo así, se toman los
     * índices de los cruzados o compartidos con ese índice
     *
     * @param cde
     */
    public void asignacionSesionesCruzadasCompartidas(CalendarioTotalSemestre ct, CargaDeDatosExcel cde) {
        for (int i = 0; i < cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            if (esFilaCruzadaOCompartida(cde, i)) {
                ArrayList<Integer> listadoIndices = indicesCruzadoCompartido(cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO,
                        cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(i));
                String nombreJornada = cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(i);
                Jornada jornada = seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, nombreJornada);

                if (jornada.esPresencial()) {
                    //agregarSesionesCruzadasCompartidasPresencial(ct, cde, listadoIndices);
                } else {
                    agregarSesionesCruzadasCompartidas(ct, cde, listadoIndices);
                }
            }
        }
    }

    /**
     * Primer tipo de asignación, se miran primero las cruzadas y las
     * compartidas pero en este caso sólo se asignan si son presenciales Se
     * recorre todo el listado de la oferta educativa, por cada uno se mira si
     * es cruzada o compartida, de serlo así, se toman los índices de los
     * cruzados o compartidos con ese índice
     *
     * @param cde
     */
    public void asignacionSesionesCruzadasCompartidasPresencial(CalendarioTotalSemestre ct, CargaDeDatosExcel cde) {
        for (int i = 0; i < cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            if (esFilaCruzadaOCompartida(cde, i)) {
                ArrayList<Integer> listadoIndices = indicesCruzadoCompartido(cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO,
                        cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(i));
                String nombreJornada = cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(i);
                Jornada jornada = seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, nombreJornada);

                if (!esFilaVirtual(i) && jornada.esPresencial()) {
                    agregarSesionesCruzadasCompartidasPresencial(ct, cde, listadoIndices);
                }
            }
        }
    }

    public void asignacionSesionesCruzadasCompartidasConCrucePeriodicidad(CalendarioTotalSemestre ct, CargaDeDatosExcel cde) {
        for (int i = 0; i < cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            if (esFilaCruzadaOCompartida(cde, i)) {
                ArrayList<Integer> listadoIndices = indicesCruzadoCompartido(cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO,
                        cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(i));

                String nombreJornada = cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(i);
                Jornada jornada = seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, nombreJornada);

                if (jornada.esPresencial()) {
                    System.out.println("No aplica la asignación de horarios para esta jornada que es presencial " + jornada.getNombre());
                } else {
                    agregarSesionesCruzadasCompartidasConCrucePeriodicidad(ct, cde, listadoIndices);
                }
            }
        }
    }

    public void asignacionSesionesCruzadasCompartidasAleatorias(CalendarioTotalSemestre ct, CargaDeDatosExcel cde, ArrayList<Integer> listadoIndicesAleatorios) {

        for (int i = 0; i < listadoIndicesAleatorios.size(); i++) {
            int j = listadoIndicesAleatorios.get(i);
            if (esFilaCruzadaOCompartida(cde, j)) {
                ArrayList<Integer> listadoIndices = indicesCruzadoCompartido(cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO,
                        cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(j));
                String nombreJornada = cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(i);
                Jornada jornada = seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, nombreJornada);

                if (!jornada.esPresencial()) {
                    agregarSesionesCruzadasCompartidas(ct, cde, listadoIndices);
                }
            }
        }
    }

    public void asignacionSesionesCruzadasCompartidasConCrucePeriodicidadAleatorias(CalendarioTotalSemestre ct, CargaDeDatosExcel cde, ArrayList<Integer> listadoIndicesAleatorios) {
        for (int i = 0; i < listadoIndicesAleatorios.size(); i++) {
            int j = listadoIndicesAleatorios.get(i);
            if (esFilaCruzadaOCompartida(cde, j)) {
                ArrayList<Integer> listadoIndices = indicesCruzadoCompartido(cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO,
                        cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(j));
                String nombreJornada = cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(i);
                Jornada jornada = seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, nombreJornada);

                if (!jornada.esPresencial()) {
                    agregarSesionesCruzadasCompartidasConCrucePeriodicidad(ct, cde, listadoIndices);
                }
            }
        }
    }

    public int totalSesionesCruzadasCompartidas() {
        int total = 0;

        for (int i = 0; i < CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            if (esFilaCruzadaOCompartida(CDE, i)) {
                total = total + CDE.LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(i);
            }
        }

        return total;
    }

    /**
     * Esta función devuelve todos los índices del 0 hasta el tamaño de la
     * oferta educativa que no sean virtuales, de forma aleatoria
     *
     * @return
     */
    public ArrayList<Integer> listadoIndicesAleatorios() {
        ArrayList<Integer> listadoIndices = new ArrayList<Integer>();
        for (int i = 0; i < CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            if (!esFilaVirtual(i)) {
                listadoIndices.add(i);
            }
        }
        Collections.shuffle(listadoIndices);
        return listadoIndices;
    }

    /**
     * Esta función toma una materia y trata de realizarle una asignación en
     * términos ideales, es decir, si la jornada para esa materia es cada 8
     * días, y la primera sesión se asigna a las 8am, entonces buscará repetir
     * este patrón cada 8 días, hasta completar el total de sesiones
     *
     * @param cde
     */
    public void asignacionesSesionesIdeal(CalendarioTotalSemestre ct, CargaDeDatosExcel cde) {
        for (int i = 0; i < cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {

            if (!cde.LISTADOOFERTAEDUCATIVA_VIRTUAL.get(i).equals("Si")) {//programar si no es virtual
                long t1 = System.currentTimeMillis();
                agregarSesionesIdealesFilaDada(ct, cde, i);
                long t2 = System.currentTimeMillis();
                long t = t2 - t1;
                //System.out.println("Tiempo de asignación para la fila: " + i + "," + t + "," + SESIONES.size());
            }
        }
    }

    /**
     * Si bien esta función dice "aleatoria" realmente lo que hace es ir
     * asignando sesiones según el orden dado en los índices del parámetro
     * listadoOrden. La palabra "ideal" hace referencia a que se debe asignar la
     * sesión coincidiendo con el periodo dado, por ejemplo, si la materia se ve
     * cada dos semanas, lo ideal es que se asigne según ese tiempo y no, por
     * ejemplo, cada semana, o dos veces en la misma semana, como en ocasiones
     * se puede presentar Asigna sesiones a las materias no virtuales, es decir
     * presenciales, y a aquellas que no están compartidas ni cruzadas. Este
     * método se apoya en agregarSesionesIdealesFilaDada
     *
     * @param ct
     * @param cde
     * @param listadoOrden
     */
    public void asignacionesAleatoriaSesionesIdeal(CalendarioTotalSemestre ct, CargaDeDatosExcel cde, ArrayList<Integer> listadoOrden) {
        for (int i = 0; i < listadoOrden.size(); i++) {
            int j = listadoOrden.get(i);
            if (cde.LISTADOOFERTAEDUCATIVA_VIRTUAL.get(j).trim().toLowerCase().equals("no")
                    && cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(j).trim().equals("")) {//programar si no es virtual y si no es cruzada o compartida

                String nombreJornada = cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(j);
                Jornada jornada = seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, nombreJornada);

//                if (jornada.esPresencial()) {
//                    agregarSesionesIdealesFilaDadaPresencial(ct, cde, j);
//                } else {
//                    agregarSesionesIdealesFilaDada(ct, cde, j);
//                }
                if (!jornada.esPresencial()) {
                    agregarSesionesIdealesFilaDada(ct, cde, j);
                }
            }
        }
    }

    /**
     * Si bien esta función dice "aleatoria" realmente lo que hace es ir
     * asignando sesiones según el orden dado en los índices del parámetro
     * listadoOrden. La palabra "ideal" hace referencia a que se debe asignar la
     * sesión coincidiendo con el periodo dado, por ejemplo, si la materia se ve
     * cada dos semanas, lo ideal es que se asigne según ese tiempo y no, por
     * ejemplo, cada semana, o dos veces en la misma semana, como en ocasiones
     * se puede presentar Asigna sesiones a las materias no virtuales, es decir
     * presenciales, y a aquellas que no están compartidas ni cruzadas. Este
     * método se apoya en agregarSesionesIdealesFilaDada
     *
     * @param ct
     * @param cde
     * @param listadoOrden
     */
    public void asignacionesAleatoriaSesionesIdealPresencial(CalendarioTotalSemestre ct, CargaDeDatosExcel cde, ArrayList<Integer> listadoOrden) {
        for (int i = 0; i < listadoOrden.size(); i++) {
            int j = listadoOrden.get(i);
            if (cde.LISTADOOFERTAEDUCATIVA_VIRTUAL.get(j).trim().toLowerCase().equals("no")
                    && cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(j).trim().equals("")) {//programar si no es virtual y si no es cruzada o compartida

                String nombreJornada = cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(j);
                Jornada jornada = seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, nombreJornada);

                if (!esFilaVirtual(i) && jornada.esPresencial()) {
                    agregarSesionesIdealesFilaDadaPresencial(ct, cde, j);
                }
            }
        }
    }

    /**
     * En esta función si bien aparece la palabra "aleatoria", realmente se
     * programa acorde con el orden que aparece en listadoOrden, sólo que acá no
     * se tiene en cuenta el cruce de periodicidad; se busca completar el total
     * de sesiones de una fila sin importar si se ven dos veces dentro del
     * tiempo de periodicidad. Por ejemplo, si una clase sólo se puede programar
     * una vez a la semana, en esta se busca que se programa dos veces con el
     * fin de cumplir el total de sesiones;por eso esta función se ejecuta
     * siempre luego de asignacionesAleatoriaSesionesIdeal
     *
     * @param ct
     * @param cde
     * @param listadoOrden
     */
    public void asignacionesAleatoriaSesionesConCrucePeriodicidad(CalendarioTotalSemestre ct, CargaDeDatosExcel cde, ArrayList<Integer> listadoOrden) {

        for (int i = 0; i < listadoOrden.size(); i++) {
            int j = listadoOrden.get(i);
            if (cde.LISTADOOFERTAEDUCATIVA_VIRTUAL.get(j).trim().toLowerCase().equals("no")
                    && cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(j).trim().equals("")) {//programar si no es virtual //si no está cruzado comp

                String nombreJornada = cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(j);
                Jornada jornada = seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, nombreJornada);

                if (!jornada.esPresencial()) {
                    agregarSesionesConCrucePeriodicidadFilaDada(ct, cde, j);
                }
            }
        }
    }

    /**
     * En esta función se hace el proceso de agregar el total de sesiones para
     * la fila dada La palabra "ideales" hace referencia a que acá se busca que
     * las clases de esa fila en particular cumplan con la periodicidad de ser
     * cada semana o cada dos semanas, dependiendo como estén programdas y
     * evitando que se programan dos sesiones antes de que se termine esa
     * periodicidad.
     *
     * @param ct
     * @param cde
     * @param i
     */
    public void agregarSesionesIdealesFilaDada(CalendarioTotalSemestre ct, CargaDeDatosExcel cde, int i) {

        //instanciamos cada uno de los objetos que se necesitarán apra crear la sesión
        String nombreDocente = cde.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i);
        String programa = cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i);
        String semestre = cde.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i);
        String nombreJornada = cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(i);
        String nombreAsignatura = cde.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i);
        String alfa = cde.LISTADOOFERTAEDUCATIVA_ALFA.get(i);
        String numerico = "";
        try {
            numerico = "" + cde.LISTADOOFERTAEDUCATIVA_NUMERICO.get(i);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int creditos = cde.LISTADOOFERTAEDUCATIVA_CREDITOS.get(i);
        int duracion = cde.LISTADOOFERTAEDUCATIVA_DURACIONIDEALSESION.get(i).intValue();
        int numeroSesiones = cde.LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(i).intValue();
        int periodicidad = cde.LISTADOOFERTAEDUCATIVA_PERIODICIDAD.get(i).intValue();
        String idCruceCompartido = cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(i);

        Jornada jornada = seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, nombreJornada);
        if (jornada == null) {
            ofertaeducativa.Validaciones.mostrarVentanaError("Se encontró una jornada nula en "
                    + nombreDocente
                    + " "
                    + programa + " "
                    + semestre);
            System.out.println("");
        }
        Docente docente = new Docente(nombreDocente);
        Grupo grupo = new Grupo(programa, semestre, jornada);
        Asignatura asignatura = new Asignatura(nombreAsignatura,
                alfa,
                numerico,
                creditos,
                numeroSesiones,
                idCruceCompartido
        );

        int tamano = NUMEROSESIONESASIGNATURAGRUPO.getSesionesProgramadas(asignatura, grupo);

        if (tamano < numeroSesiones) {

            //para esa jornada encontrada vamos ahora a tomar las fechas validas
            ArrayList<Date> listadoFechasValidasJornada
                    = getListadoHorasFechasValidasJornadaDesdeGuardado(cde, jornada);//getFechasHorasValidasJornada(ct.LISTADOTOTALHORASSEMESTRE_SINDIASPROHIBIDOS, jornada);

            //empezaremos a iterar sobre el listado de fechas válidas, tomando la primera
            //y determinando si es válida o no
            for (int j = 0; j < listadoFechasValidasJornada.size(); j++) {
                Date fecha = listadoFechasValidasJornada.get(j);

//                Calendar c = Calendar.getInstance();
//                c.setTime(fecha);
                Sesion sesion = new Sesion(docente, grupo, asignatura, fecha, duracion);

                if (!cruceDiaSesion(sesion, SESIONES)
                        && !cruceDocenteSesionTotalSesiones(sesion, SESIONES)
                        && !cruceGrupoSesionTotalSesiones(sesion, SESIONES)
                        && !crucePeriodicidadMateriaSesion(sesion, SESIONES, periodicidad)
                        && !sobrePasaFinalJornada(sesion, jornada)) {
                    SESIONES.add(sesion);
                    NUMEROSESIONESASIGNATURAGRUPO.sumarSesion(asignatura, grupo);
                }
                //si encontramos ya el total de sesiones
                if (NUMEROSESIONESASIGNATURAGRUPO.getSesionesProgramadas(asignatura, grupo) == numeroSesiones) {
                    break;
                }
            }
        }

    }

    public void agregarSesionesIdealesFilaDadaPresencial(CalendarioTotalSemestre ct,
            CargaDeDatosExcel cde, int i) {
        int totalSemanas = TOTALSEMANASSEMESTRE;
        int minutosProgramadosSemana = 0;

        //instanciamos cada uno de los objetos que se necesitarán apra crear la sesión
        String nombreDocente = cde.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i);
        String programa = cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i);
        String semestre = cde.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i);
        String nombreJornada = cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(i);
        String nombreAsignatura = cde.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i);
        String alfa = cde.LISTADOOFERTAEDUCATIVA_ALFA.get(i);
        String numerico = "";
        try {
            numerico = "" + cde.LISTADOOFERTAEDUCATIVA_NUMERICO.get(i);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int creditos = cde.LISTADOOFERTAEDUCATIVA_CREDITOS.get(i);
        int duracion = cde.LISTADOOFERTAEDUCATIVA_DURACIONIDEALSESION.get(i).intValue();
        int numeroSesiones = cde.LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(i).intValue();
        int periodicidad = cde.LISTADOOFERTAEDUCATIVA_PERIODICIDAD.get(i).intValue();
        String idCruceCompartido = cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(i);

        Jornada jornada = seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, nombreJornada);

        if (jornada == null) {
            ofertaeducativa.Validaciones.mostrarVentanaError("Se encontró una jornada nula en "
                    + nombreDocente
                    + " "
                    + programa + " "
                    + semestre);
        }
        Docente docente = new Docente(nombreDocente);
        Grupo grupo = new Grupo(programa, semestre, jornada);
        Asignatura asignatura = new Asignatura(nombreAsignatura,
                alfa,
                numerico,
                creditos,
                numeroSesiones,
                idCruceCompartido
        );

        ArrayList<TuplaDiaHoraInicialFinal> tuplas = new ArrayList<>();
        tuplas = jornada.getListadoTuplasDiaHoraInicialFinal();
        ArrayList<Date> listadoFechasValidasJornada
                = getListadoHorasFechasValidasJornadaDesdeGuardado(cde, jornada);

        for (TuplaDiaHoraInicialFinal tupla : tuplas) {

            Calendar fechaAAsignar = Calendar.getInstance();
            Calendar fechaLimiteDia = Calendar.getInstance();

            fechaLimiteDia.set(Calendar.DAY_OF_WEEK, tupla.getDia());
            fechaLimiteDia.set(Calendar.HOUR_OF_DAY, tupla.getHoraFinal().getHora());
            fechaLimiteDia.set(Calendar.MINUTE, tupla.getHoraFinal().getMinutos());

            fechaAAsignar.set(Calendar.DAY_OF_WEEK, tupla.getDia());
            fechaAAsignar.set(Calendar.HOUR_OF_DAY, tupla.getHoraInicial().getHora());
            fechaAAsignar.set(Calendar.MINUTE, tupla.getHoraInicial().getMinutos());

            int k = 0;

            while (fechaAAsignar.before(fechaLimiteDia)) {
                if (fechaAAsignar.get(Calendar.DAY_OF_MONTH) == 1 && fechaAAsignar.get(Calendar.MONTH) == 1) {
                    System.out.println("");
                }

                fechaAAsignar.getTime();
                fechaLimiteDia.getTime();

                ArrayList<Sesion> sesionesAAgregar = new ArrayList<>();

                for (Date d : listadoFechasValidasJornada) {
                    Calendar fechaParaTomarDMA = Calendar.getInstance();
                    fechaParaTomarDMA.setTime(d);
                    fechaParaTomarDMA.getTime();

                    if (fechaParaTomarDMA.get(Calendar.DAY_OF_WEEK) == fechaAAsignar.get(Calendar.DAY_OF_WEEK)) {

                        fechaAAsignar.set(Calendar.DAY_OF_MONTH, fechaParaTomarDMA.get(Calendar.DAY_OF_MONTH));
                        fechaAAsignar.set(Calendar.MONTH, fechaParaTomarDMA.get(Calendar.MONTH));
                        fechaAAsignar.set(Calendar.YEAR, fechaParaTomarDMA.get(Calendar.YEAR));

                        Sesion sesion = new Sesion(docente,
                                grupo, asignatura, fechaAAsignar.getTime(), duracion / numeroSesiones);

                        if (!cruceDiaSesion(sesion, SESIONES)
                                && !cruceDocenteSesionTotalSesiones(sesion, SESIONES)
                                && !cruceGrupoSesionTotalSesiones(sesion, SESIONES)
                                && !crucePeriodicidadMateriaSesion(sesion, SESIONES, periodicidad)
                                && !sobrePasaFinalJornada(sesion, jornada)) {
                            SESIONES.add(sesion);
                            sesionesAAgregar.add(sesion);
                        }
                    }
                }
                if (sesionesAAgregar.size() < totalSemanas) {
                    for (Sesion s : sesionesAAgregar) {
                        SESIONES.remove(s);
                    }
                } else {
                    minutosProgramadosSemana = minutosProgramadosSemana + duracion / numeroSesiones;
                    if (minutosProgramadosSemana == duracion) {
                        break;
                    }
                }
                k++;
                fechaAAsignar = Calendar.getInstance();
                fechaAAsignar.set(Calendar.DAY_OF_WEEK, tupla.getDia());
                fechaAAsignar.set(Calendar.HOUR_OF_DAY, tupla.getHoraInicial().getHora());
                fechaAAsignar.set(Calendar.MINUTE, tupla.getHoraInicial().getMinutos());
                fechaAAsignar.add(Calendar.MINUTE, 15 * k);
                fechaAAsignar.getTime();
            }
            if (minutosProgramadosSemana == duracion) {
                break;
            }
        }
    }

    /**
     * En esta función se hace el proceso de agregar el total de sesiones para
     * la fila dada sin tener en cuenta la periodicidad; es decir, que si una
     * materia está programada cada semana en esta función se busca que se
     * programa dos veces por semana, por esa razón se ejecuta siempre después
     * de buscar las sesiones ideales
     *
     * @param ct
     * @param cde
     * @param i
     */
    public void agregarSesionesConCrucePeriodicidadFilaDada(CalendarioTotalSemestre ct, CargaDeDatosExcel cde, int i) {

        //instanciamos cada uno de los objetos que se necesitarán apra crear la sesión
        String nombreDocente = cde.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i);
        String programa = cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i);
        String semestre = cde.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i);
        String nombreJornada = cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(i);
        String nombreAsignatura = cde.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i);
        String alfa = cde.LISTADOOFERTAEDUCATIVA_ALFA.get(i);
        String numerico = "";
        try {
            numerico = "" + cde.LISTADOOFERTAEDUCATIVA_NUMERICO.get(i);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int creditos = cde.LISTADOOFERTAEDUCATIVA_CREDITOS.get(i);
        int duracion = cde.LISTADOOFERTAEDUCATIVA_DURACIONIDEALSESION.get(i).intValue();
        int numeroSesiones = cde.LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(i).intValue();
        int periodicidad = cde.LISTADOOFERTAEDUCATIVA_PERIODICIDAD.get(i).intValue();
        String idCruceCompartido = cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(i);

        Jornada jornada = seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, nombreJornada);
        Docente docente = new Docente(nombreDocente);
        Grupo grupo = new Grupo(programa, semestre, jornada);
        Asignatura asignatura = new Asignatura(nombreAsignatura,
                alfa,
                numerico,
                creditos,
                numeroSesiones,
                idCruceCompartido
        );

        if (NUMEROSESIONESASIGNATURAGRUPO.getSesionesProgramadas(asignatura, grupo) < numeroSesiones) {
            //para esa jornada encontrada vamos ahora a tomar las fechas validas
            ArrayList<Date> listadoFechasValidasJornada
                    = getListadoHorasFechasValidasJornadaDesdeGuardado(cde, jornada);//getFechasHorasValidasJornada(ct.LISTADOTOTALHORASSEMESTRE_SINDIASPROHIBIDOS, jornada);

            //empezaremos a iterar sobre el listado de fechas válidas, tomando la primera
            //y determinando si es válida o no
            for (int j = 0; j < listadoFechasValidasJornada.size(); j++) {
                Date fecha = listadoFechasValidasJornada.get(j);
//                Calendar c = Calendar.getInstance();
//                c.setTime(fecha);

                Sesion sesion = new Sesion(docente, grupo, asignatura, fecha, duracion);

                if (!cruceDiaSesion(sesion, SESIONES)
                        && !cruceDocenteSesionTotalSesiones(sesion, SESIONES)
                        && !cruceGrupoSesionTotalSesiones(sesion, SESIONES)
                        && !sobrePasaFinalJornada(sesion, jornada)) {
                    SESIONES.add(sesion);
                    NUMEROSESIONESASIGNATURAGRUPO.sumarSesion(asignatura, grupo);
                    //System.out.println("Se agrego la sesion con cruce de periodicidad " + sesion.enCadena());
                }
                //si encontramos ya el total de sesiones
                if (NUMEROSESIONESASIGNATURAGRUPO.getSesionesProgramadas(asignatura, grupo) == numeroSesiones) {
                    break;
                }
            }
        }

    }

    public void agregarSesionesCruzadasCompartidas(CalendarioTotalSemestre ct, CargaDeDatosExcel cde, ArrayList<Integer> listadoIndices) {
        //instanciamos cada uno de los objetos que se necesitarán para crear la sesión
        ArrayList<Object[]> objetosParaSesion = new ArrayList<Object[]>();
        ArrayList<Jornada> jornadas = new ArrayList<Jornada>();
        ArrayList<Integer> duraciones = new ArrayList<Integer>();
        ArrayList<Integer> numerosSesiones = new ArrayList<Integer>();
        ArrayList<Integer> periodicidades = new ArrayList<Integer>();

        Jornada jornada = null;
        for (int i : listadoIndices) {
            //de acá 

            String nombreDocente = cde.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i);
            String programa = cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i);
            String semestre = cde.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i);
            String nombreJornada = cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(i);
            String nombreAsignatura = cde.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i);
            String alfa = cde.LISTADOOFERTAEDUCATIVA_ALFA.get(i);
            String numerico = cde.LISTADOOFERTAEDUCATIVA_NUMERICO.get(i);
            int creditos = cde.LISTADOOFERTAEDUCATIVA_CREDITOS.get(i);
            int duracion = cde.LISTADOOFERTAEDUCATIVA_DURACIONIDEALSESION.get(i).intValue();
            duraciones.add(duracion);
            int numeroSesiones = cde.LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(i).intValue();
            numerosSesiones.add(numeroSesiones);
            int periodicidad = cde.LISTADOOFERTAEDUCATIVA_PERIODICIDAD.get(i).intValue();
            periodicidades.add(periodicidad);
            String idCruceCompartido = cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(i);

            //la jornada tiene que ser la misma para las cruzadas o compartidas
            //de lo contrario no tiene sentido cruzarlas
            if (seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, nombreJornada) == null) {
                ofertaeducativa.Validaciones.mostrarVentanaError("Se ha encontrado una jornada que no "
                        + "existe en la malla base, al tratar de cruzar las compartidas de "
                        + nombreDocente
                        + " " + programa
                        + " " + semestre
                        + " " + nombreAsignatura
                        + " " + nombreJornada);
            }
            jornadas.add(seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, nombreJornada));
            Docente docente = new Docente(nombreDocente);
            Grupo grupo = new Grupo(programa, semestre, jornada);
            Asignatura asignatura = new Asignatura(nombreAsignatura,
                    alfa,
                    numerico,
                    creditos,
                    numeroSesiones,
                    idCruceCompartido
            );

            Object[] objetos = {jornada, docente, grupo, asignatura, duracion, numeroSesiones, periodicidad, idCruceCompartido};
            objetosParaSesion.add(objetos);

        }
        ArrayList<Date> listadoFechasValidasJornadaCruzada
                = fechasComunesEntreJornadas(jornadas);//getListadoHorasFechasValidasJornadaDesdeGuardado(cde, jornada);//getFechasHorasValidasJornada(ct.LISTADOTOTALHORASSEMESTRE_SINDIASPROHIBIDOS, jornada);
        ArrayList<Sesion> listadoSesiones = new ArrayList<Sesion>();
        for (int i = 0; i < listadoFechasValidasJornadaCruzada.size(); i++) {

            listadoSesiones = new ArrayList<Sesion>();
            Date fecha = listadoFechasValidasJornadaCruzada.get(i);
            for (Object[] arregloObjetos : objetosParaSesion) {
                jornada = (Jornada) (arregloObjetos[0]);
                Docente docente = (Docente) (arregloObjetos[1]);
                Grupo grupo = (Grupo) (arregloObjetos[2]);
                Asignatura asignatura = (Asignatura) (arregloObjetos[3]);
                int duracion = Collections.max(duraciones);//(int) (arregloObjetos[4]);
                int numeroSesiones = Collections.max(numerosSesiones);//(int) (arregloObjetos[5]);
                int periodicidad = Collections.max(periodicidades);//(int) (arregloObjetos[6]);
                Sesion sesion = new Sesion(docente, grupo, asignatura, fecha, duracion);
                if (NUMEROSESIONESASIGNATURAGRUPO.getSesionesProgramadas(asignatura, grupo) < numeroSesiones) {
                    //empezaremos a iterar sobre el listado de fechas válidas, tomando la primera
                    //y determinando si es válida o no

                    if (!cruceDiaSesion(sesion, SESIONES)
                            && !cruceDocenteSesionTotalSesiones(sesion, SESIONES)
                            && !cruceGrupoSesionTotalSesiones(sesion, SESIONES)
                            && !crucePeriodicidadMateriaSesion(sesion, SESIONES, periodicidad)
                            && !sobrePasaFinalDeAlgunaJornada(sesion, jornadas)) {
                        listadoSesiones.add(sesion);
                    }
                    //si encontramos ya el total de sesiones
                    if (NUMEROSESIONESASIGNATURAGRUPO.getSesionesProgramadas(asignatura, grupo) == numeroSesiones) {
                        break;
                    }
                }
            }
            if (listadoSesiones.size() == listadoIndices.size()) {
                for (Sesion s : listadoSesiones) {
                    SESIONES.add(s);
                    NUMEROSESIONESASIGNATURAGRUPO.sumarSesion(s.getAsignatura(), s.getGrupo());
                }
            }
        }

    }

    public void agregarSesionesCruzadasCompartidasPresencial(CalendarioTotalSemestre ct,
            CargaDeDatosExcel cde, ArrayList<Integer> listadoIndices) {

        int totalSemanas = TOTALSEMANASSEMESTRE;
        int minutosProgramadosSemana = 0;
//instanciamos cada uno de los objetos que se necesitarán para crear la sesión
        ArrayList<Object[]> objetosParaSesion = new ArrayList<Object[]>();

        ArrayList<Jornada> jornadas = new ArrayList<Jornada>();
        ArrayList<Integer> duraciones = new ArrayList<Integer>();
        ArrayList<Integer> numerosSesiones = new ArrayList<Integer>();
        ArrayList<Integer> periodicidades = new ArrayList<Integer>();

        Jornada jornada = null;
        for (int i : listadoIndices) {
            //de acá 

            String nombreDocente = cde.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i);
            String programa = cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i);
            String semestre = cde.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i);
            String nombreJornada = cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(i);
            String nombreAsignatura = cde.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i);
            String alfa = cde.LISTADOOFERTAEDUCATIVA_ALFA.get(i);
            String numerico = cde.LISTADOOFERTAEDUCATIVA_NUMERICO.get(i);
            int creditos = cde.LISTADOOFERTAEDUCATIVA_CREDITOS.get(i);
            int duracion = cde.LISTADOOFERTAEDUCATIVA_DURACIONIDEALSESION.get(i).intValue();
            duraciones.add(duracion);
            int numeroSesiones = cde.LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(i).intValue();
            numerosSesiones.add(numeroSesiones);
            int periodicidad = cde.LISTADOOFERTAEDUCATIVA_PERIODICIDAD.get(i).intValue();
            periodicidades.add(periodicidad);
            String idCruceCompartido = cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(i);

            //la jornada tiene que ser la misma para las cruzadas o compartidas
            //de lo contrario no tiene sentido cruzarlas
            if (seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, nombreJornada) == null) {
                ofertaeducativa.Validaciones.mostrarVentanaError("Se ha encontrado una jornada que no "
                        + "existe en la malla base, al tratar de cruzar las compartidas de "
                        + nombreDocente
                        + " " + programa
                        + " " + semestre
                        + " " + nombreAsignatura
                        + " " + nombreJornada);
            }
            jornada = seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, nombreJornada);
            jornadas.add(jornada);

            Docente docente = new Docente(nombreDocente);
            Grupo grupo = new Grupo(programa, semestre, jornada);
            Asignatura asignatura = new Asignatura(nombreAsignatura,
                    alfa,
                    numerico,
                    creditos,
                    numeroSesiones,
                    idCruceCompartido
            );

            Object[] objetos = {jornada, docente, grupo, asignatura, duracion, numeroSesiones, periodicidad, idCruceCompartido};
            objetosParaSesion.add(objetos);
        }

        int sesionesYaProgramadas = 0;
        for (Sesion s : SESIONES) {
            if (asignaturasIguales(s.ASIGNATURA, (Asignatura) objetosParaSesion.get(0)[3])
                    && gruposIguales(s.GRUPO, (Grupo) objetosParaSesion.get(0)[2])
                    && docentesIguales(s.DOCENTE, (Docente) objetosParaSesion.get(0)[1])) {
                sesionesYaProgramadas++;
            }
        }

        if (sesionesYaProgramadas < (int) objetosParaSesion.get(0)[5] * totalSemanas) {

            ArrayList<TuplaDiaHoraInicialFinal> tuplas = new ArrayList<>();

            //esto tiene la limitación que las dos jornadas a cruzar
            //deben ser iguales; ya que el programa no puede deducri
            //cuáles tuplas son comunes
            jornada = (Jornada) objetosParaSesion.get(0)[0];
            tuplas = jornada.getListadoTuplasDiaHoraInicialFinal();
            ArrayList<Date> listadoFechasValidasJornadaCruzada
                    = fechasComunesEntreJornadas(jornadas);//getListadoHorasFechasValidasJornadaDesdeGuardado(cde, jornada);//getFechasHorasValidasJornada(ct.LISTADOTOTALHORASSEMESTRE_SINDIASPROHIBIDOS, jornada);

            for (TuplaDiaHoraInicialFinal tupla : tuplas) {

                Calendar fechaAAsignar = Calendar.getInstance();
                Calendar fechaLimiteDia = Calendar.getInstance();

                fechaLimiteDia.set(Calendar.DAY_OF_WEEK, tupla.getDia());
                fechaLimiteDia.set(Calendar.HOUR_OF_DAY, tupla.getHoraFinal().getHora());
                fechaLimiteDia.set(Calendar.MINUTE, tupla.getHoraFinal().getMinutos());

                fechaAAsignar.set(Calendar.DAY_OF_WEEK, tupla.getDia());
                fechaAAsignar.set(Calendar.HOUR_OF_DAY, tupla.getHoraInicial().getHora());
                fechaAAsignar.set(Calendar.MINUTE, tupla.getHoraInicial().getMinutos());

                int k = 0;

                while (fechaAAsignar.before(fechaLimiteDia)) {

                    ArrayList<Sesion> sesionesAAgregar = new ArrayList<>();

                    for (Date d : listadoFechasValidasJornadaCruzada) {

                        Calendar fechaParaTomarDMA = Calendar.getInstance();
                        fechaParaTomarDMA.setTime(d);

                        if (fechaParaTomarDMA.get(Calendar.DAY_OF_WEEK) == fechaAAsignar.get(Calendar.DAY_OF_WEEK)) {

                            fechaAAsignar.set(Calendar.DAY_OF_MONTH, fechaParaTomarDMA.get(Calendar.DAY_OF_MONTH));
                            fechaAAsignar.set(Calendar.MONTH, fechaParaTomarDMA.get(Calendar.MONTH));
                            fechaAAsignar.set(Calendar.YEAR, fechaParaTomarDMA.get(Calendar.YEAR));

                            ArrayList<Sesion> listadoSesiones = new ArrayList<>();
                            for (Object[] arregloObjetos : objetosParaSesion) {
                                jornada = (Jornada) (arregloObjetos[0]);
                                Docente docente = (Docente) (arregloObjetos[1]);
                                Grupo grupo = (Grupo) (arregloObjetos[2]);
                                Asignatura asignatura = (Asignatura) (arregloObjetos[3]);
                                int duracion = Collections.max(duraciones);//(int) (arregloObjetos[4]);
                                int numeroSesiones = Collections.max(numerosSesiones);//(int) (arregloObjetos[5]);
                                int periodicidad = Collections.max(periodicidades);//(int) (arregloObjetos[6]);
                                Sesion sesion = new Sesion(docente,
                                        grupo, asignatura, fechaAAsignar.getTime(), duracion / numeroSesiones);

                                //if (NUMEROSESIONESASIGNATURAGRUPO.getSesionesProgramadas(asignatura, grupo) 
                                //      < numeroSesiones * totalSemanas) {
                                if (!cruceDiaSesion(sesion, SESIONES)
                                        && !cruceDocenteSesionTotalSesiones(sesion, SESIONES)
                                        && !cruceGrupoSesionTotalSesiones(sesion, SESIONES)
                                        && !crucePeriodicidadMateriaSesion(sesion, SESIONES, periodicidad)
                                        && !sobrePasaFinalDeAlgunaJornada(sesion, jornadas)) {

                                    listadoSesiones.add(sesion);
                                }
                                if (listadoSesiones.size() == listadoIndices.size()) {
                                    for (Sesion s : listadoSesiones) {
                                        SESIONES.add(s);
                                        sesionesAAgregar.add(s);
                                        //NUMEROSESIONESASIGNATURAGRUPO.sumarSesion(asignatura, grupo);
                                    }
                                }
                                //}
                            }
                        }
                    }
                    if (sesionesAAgregar.size() < totalSemanas * listadoIndices.size()) {//en el caso de las cruzadas cambiar por sesionesAAgregar.size()==totalSemanas*totalIndicesACruzar
                        for (Sesion s : sesionesAAgregar) {
                            SESIONES.remove(s);
                        }
                    } else {
                        minutosProgramadosSemana = minutosProgramadosSemana + Collections.max(duraciones) / Collections.max(numerosSesiones);

                        if (minutosProgramadosSemana == Collections.max(duraciones)) {
                            break;
                        }
                    }
                    k++;
                    fechaAAsignar = Calendar.getInstance();
                    fechaAAsignar.set(Calendar.DAY_OF_WEEK, tupla.getDia());
                    fechaAAsignar.set(Calendar.HOUR_OF_DAY, tupla.getHoraInicial().getHora());
                    fechaAAsignar.set(Calendar.MINUTE, tupla.getHoraInicial().getMinutos());

                    fechaAAsignar.add(Calendar.MINUTE, 15 * k);
                }
                if (minutosProgramadosSemana == Collections.max(duraciones)) {
                    break;
                }
            }
        }
    }

    public void agregarSesionesCruzadasCompartidasConCrucePeriodicidad(CalendarioTotalSemestre ct, CargaDeDatosExcel cde, ArrayList<Integer> listadoIndices) {
        //instanciamos cada uno de los objetos que se necesitarán para crear la sesión
        ArrayList<Object[]> objetosParaSesion = new ArrayList<Object[]>();
        ArrayList<Jornada> jornadas = new ArrayList<Jornada>();
        ArrayList<Integer> duraciones = new ArrayList<Integer>();
        ArrayList<Integer> numerosSesiones = new ArrayList<Integer>();
        ArrayList<Integer> periodicidades = new ArrayList<Integer>();

        Jornada jornada = null;
        for (int i : listadoIndices) {
            //de acá 

            String nombreDocente = cde.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i);
            String programa = cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i);
            String semestre = cde.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i);
            String nombreJornada = cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(i);
            String nombreAsignatura = cde.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i);
            String alfa = cde.LISTADOOFERTAEDUCATIVA_ALFA.get(i);
            String numerico = cde.LISTADOOFERTAEDUCATIVA_NUMERICO.get(i);
            int creditos = cde.LISTADOOFERTAEDUCATIVA_CREDITOS.get(i);
            int duracion = cde.LISTADOOFERTAEDUCATIVA_DURACIONIDEALSESION.get(i).intValue();
            duraciones.add(duracion);
            int numeroSesiones = cde.LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(i).intValue();
            numerosSesiones.add(numeroSesiones);
            int periodicidad = cde.LISTADOOFERTAEDUCATIVA_PERIODICIDAD.get(i).intValue();
            periodicidades.add(periodicidad);
            String idCruceCompartido = cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(i);

            //la jornada tiene que ser la misma para las cruzadas o compartidas
            //de lo contrario no tiene sentido cruzarlas
            jornadas.add(seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, nombreJornada));
            Docente docente = new Docente(nombreDocente);
            Grupo grupo = new Grupo(programa, semestre, jornada);
            Asignatura asignatura = new Asignatura(nombreAsignatura,
                    alfa,
                    numerico,
                    creditos,
                    numeroSesiones,
                    idCruceCompartido
            );

            Object[] objetos = {jornada, docente, grupo, asignatura, duracion, numeroSesiones, periodicidad, idCruceCompartido};
            objetosParaSesion.add(objetos);

        }
        ArrayList<Date> listadoFechasValidasJornadaCruzada
                = fechasComunesEntreJornadas(jornadas);//getListadoHorasFechasValidasJornadaDesdeGuardado(cde, jornada);//getFechasHorasValidasJornada(ct.LISTADOTOTALHORASSEMESTRE_SINDIASPROHIBIDOS, jornada);
        ArrayList<Sesion> listadoSesiones = new ArrayList<Sesion>();
        for (int i = 0; i < listadoFechasValidasJornadaCruzada.size(); i++) {

            listadoSesiones = new ArrayList<Sesion>();
            Date fecha = listadoFechasValidasJornadaCruzada.get(i);
            for (Object[] arregloObjetos : objetosParaSesion) {
                jornada = (Jornada) (arregloObjetos[0]);
                Docente docente = (Docente) (arregloObjetos[1]);
                Grupo grupo = (Grupo) (arregloObjetos[2]);
                Asignatura asignatura = (Asignatura) (arregloObjetos[3]);
                int duracion = Collections.max(duraciones);//(int) (arregloObjetos[4]);
                int numeroSesiones = Collections.max(numerosSesiones);//(int) (arregloObjetos[5]);
                int periodicidad = Collections.max(periodicidades);//(int) (arregloObjetos[6]);
                Sesion sesion = new Sesion(docente, grupo, asignatura, fecha, duracion);

                if (NUMEROSESIONESASIGNATURAGRUPO.getSesionesProgramadas(asignatura, grupo) < numeroSesiones) {
                    //empezaremos a iterar sobre el listado de fechas válidas, tomando la primera
                    //y determinando si es válida o no

                    if (!cruceDiaSesion(sesion, SESIONES)
                            && !cruceDocenteSesionTotalSesiones(sesion, SESIONES)
                            && !cruceGrupoSesionTotalSesiones(sesion, SESIONES)
                            && !sobrePasaFinalDeAlgunaJornada(sesion, jornadas)) {
                        listadoSesiones.add(sesion);
                        //System.out.println("Se agrego la sesion cruzada compartida con cruce de periodicidad " + sesion.enCadena());
                    }
                    //si encontramos ya el total de sesiones
                    if (NUMEROSESIONESASIGNATURAGRUPO.getSesionesProgramadas(asignatura, grupo) == numeroSesiones) {
                        break;
                    }
                }
            }
            if (listadoSesiones.size() == listadoIndices.size()) {
                for (Sesion s : listadoSesiones) {
                    SESIONES.add(s);
                    NUMEROSESIONESASIGNATURAGRUPO.sumarSesion(s.getAsignatura(), s.getGrupo());
                }
            }
        }

    }

    public boolean sobrePasaFinalJornada(Sesion sesion, Jornada jornada) {
        boolean sobrepasa = true;

        ArrayList<TuplaDiaHoraInicialFinal> listadoTuplas = jornada.getListadoTuplasDiaHoraInicialFinal();
        ArrayList<Boolean> listadoVerificacionPorTupla = new ArrayList<Boolean>();

        for (int i = 0; i < listadoTuplas.size(); i++) {
            //creamos la fecha final de una tupla y la pasamos
            //a calendar, es lo que sigue, lla idea es que
            //si comparo la fecha, con una tutpla cuyo día sea igual
            //puedo crear una fecha para ver si, la fecha dada en la sesión
            //sobre pasa esa fecha que he creado
            Date fecha = sesion.getFecha();
            Calendar ci = Calendar.getInstance();
            ci.setTime(fecha);
            Hora horaInicialSesion = new Hora(ci.get(Calendar.HOUR_OF_DAY), ci.get(Calendar.MINUTE));

            Calendar cf = Calendar.getInstance();
            cf.setTime(fecha);
            int dia = cf.get(Calendar.DAY_OF_WEEK);
            cf.add(Calendar.MINUTE, sesion.getDuracion());
            Hora horaFinalSesion = new Hora(cf.get(Calendar.HOUR_OF_DAY), cf.get(Calendar.MINUTE));

            Hora horaInicialJornada = listadoTuplas.get(i).getHoraInicial();
            Hora horaFinalJornada = listadoTuplas.get(i).getHoraFinal();

            if (listadoTuplas.get(i).DIA == dia) {

                if ((horaInicialJornada.beforeIgual(horaInicialSesion)
                        && horaInicialSesion.beforeIgual(horaFinalJornada))
                        && (horaInicialJornada.beforeIgual(horaFinalSesion)
                        && horaFinalSesion.beforeIgual(horaFinalJornada))) {
                    listadoVerificacionPorTupla.add(true);
                } else {
                    listadoVerificacionPorTupla.add(false);
                }

            }
        }

        for (int i = 0; i < listadoVerificacionPorTupla.size(); i++) {
            if (listadoVerificacionPorTupla.get(i)) {
                return false;
            }
        }

        return sobrepasa;
    }

    public boolean sobrePasaFinalDeAlgunaJornada(Sesion sesion, ArrayList<Jornada> jornadas) {
        boolean sobrepasaalgunajornada = false;
        ArrayList<Boolean> boleanos = new ArrayList<Boolean>();

        for (int i = 0; i < jornadas.size(); i++) {
            if (sobrePasaFinalJornada(sesion, jornadas.get(i))) {
                boleanos.add(true);
            } else {
                boleanos.add(false);
            }
        }

        for (int i = 0; i < boleanos.size(); i++) {
            if (boleanos.get(i)) {
                return true;
            }
        }

        return sobrepasaalgunajornada;
    }

    public Jornada seleccionarJornadaNombreDado(ArrayList<Jornada> listadoJornadas, String nombreJornada) {
        Jornada jornada = null;
        for (int i = 0; i < listadoJornadas.size(); i++) {
            if (listadoJornadas.get(i).getNombre().toLowerCase().trim().equals(nombreJornada.toLowerCase().trim())) {
                return listadoJornadas.get(i);
            }
        }
        return jornada;
    }

    public ArrayList<Integer> aleatorizarIndicesOrdenAsignacion(CargaDeDatosExcel cde) {
        ArrayList<Integer> listadoIndices = new ArrayList<Integer>();

        int tamano = cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.size();

        //se van a aleatorizar los indices de los presenciales
        //no cruzados, ni compartidos, y cuyo docente esté asignado
        for (int i = 0; i < tamano; i++) {
            if (cde.LISTADOOFERTAEDUCATIVA_VIRTUAL.get(i).trim().toLowerCase().equals("no")
                    && cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(i).trim().equals("")
                    && !cde.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i).trim().equals("")) {
                listadoIndices.add(i);
            }
        }

        Collections.shuffle(listadoIndices);

        return listadoIndices;
    }

    public void cargarHorasValidasTodasJornadas(CalendarioTotalSemestre cts, CargaDeDatosExcel cde) {
        ArrayList<Jornada> listadoJornadas = new ArrayList<Jornada>(cde.LISTADOJORNADAS);
        LISTADOFECHASPOSIBLESJORNADA = new ArrayList<ArrayList<Date>>();

        for (Jornada j : listadoJornadas) {

            if (!j.esPresencial()) {
                LISTADOFECHASPOSIBLESJORNADA.add(getFechasHorasValidasJornada(cts.LISTADOTOTALHORASSEMESTRE_SINDIASPROHIBIDOS, j));
            } else {
                LISTADOFECHASPOSIBLESJORNADA.add(getFechasHorasValidasJornadaPresencial(cts.LISTADOTOTALHORASSEMESTRE, j));
            }

        }

    }

    public void contarDiasParaTodasJornadas(CalendarioTotalSemestre cts, CargaDeDatosExcel cde) {
        ArrayList<Jornada> listadoJornadas = new ArrayList<Jornada>(cde.LISTADOJORNADAS);
        LISTADONUMERODIASJORNADA = new ArrayList<Integer>();
        LISTADODIASDIFERENTESJORNADA = new ArrayList<ArrayList<Date>>();

        for (int i = 0; i < listadoJornadas.size(); i++) {
            ArrayList<Integer> diasTotales = new ArrayList<Integer>();
            LISTADODIASDIFERENTESJORNADA.add(new ArrayList<Date>());

            for (int j = 0; j < LISTADOFECHASPOSIBLESJORNADA.get(i).size(); j++) {
                Date fecha = LISTADOFECHASPOSIBLESJORNADA.get(i).get(j);
                Calendar c = Calendar.getInstance();
                c.setTime(fecha);
                if (j == 0) {
                    diasTotales.add(c.get(Calendar.DAY_OF_YEAR));
                    LISTADODIASDIFERENTESJORNADA.get(i).add(c.getTime());
                } else {
                    if (diasTotales.indexOf(Integer.valueOf(c.get(Calendar.DAY_OF_YEAR))) == -1) {
                        diasTotales.add(c.get(Calendar.DAY_OF_YEAR));
                        LISTADODIASDIFERENTESJORNADA.get(i).add(c.getTime());
                    }
                }
            }
            LISTADONUMERODIASJORNADA.add(diasTotales.size());
        }
    }

    public void crearListadoDuracionTotalMinutosTodasJornadas(CalendarioTotalSemestre cts, CargaDeDatosExcel cde) {
        LISTADODURACIONTOTALMINUTOSJORNADA = new ArrayList<Long>();
        for (int i = 0; i < cde.LISTADOJORNADAS.size(); i++) {
            int duracionTotalMinutos = LISTADOFECHASPOSIBLESJORNADA.get(i).size() * 15 + 30 * LISTADONUMERODIASJORNADA.get(i);
            LISTADODURACIONTOTALMINUTOSJORNADA.add((long) duracionTotalMinutos);
        }
    }

    public ArrayList<Date> getListadoHorasFechasValidasJornadaDesdeGuardado(CargaDeDatosExcel cde, Jornada jornada) {
        ArrayList<Date> listadoFechas = new ArrayList<Date>();

        int indiceJornada = 0;
        if (jornada == null) {
            System.out.println("jornada null");
        }
        for (int i = 0; i < cde.LISTADOJORNADAS.size(); i++) {
            if (cde.LISTADOJORNADAS.get(i).getNombre().equals(jornada.getNombre())) {
                indiceJornada = i;
            }
        }

        listadoFechas = new ArrayList<Date>(LISTADOFECHASPOSIBLESJORNADA.get(indiceJornada));

        return listadoFechas;
    }

    public String obtenerCadenaJornadaSesion(CargaDeDatosExcel cde, Sesion sesion) {
        String cadenaJornada = "";

        for (int i = 0; i < cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            if (sesion.getGrupo().getPrograma().trim().equals(cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i).trim())
                    && sesion.getGrupo().getSemestre().trim().equals(cde.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i).trim())
                    && sesion.getAsignatura().getNombre().trim().equals(cde.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i).trim())
                    && sesion.getDocente().getNombre().trim().equals(cde.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i).trim())) {
                return cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(i).trim();
            }
        }

        return cadenaJornada;
    }

    public static ArrayList<String> getListadoDepuradoDocentesDesdeSesiones(ArrayList<Sesion> sesiones) {
        ArrayList<String> listadoDepuradoDocentes = new ArrayList<String>();

        for (Sesion sesion : sesiones) {
            String nombre = sesion.getDocente().getNombre();
            if (listadoDepuradoDocentes.indexOf(nombre) == -1) {
                listadoDepuradoDocentes.add(nombre);
            }
        }

        return listadoDepuradoDocentes;
    }

    public static ArrayList<Grupo> getListadoDepuradoGruposDesdeSesiones(ArrayList<Sesion> sesiones) {
        ArrayList<Grupo> listadoDepuradoGrupos = new ArrayList<Grupo>();

        for (Sesion sesion : sesiones) {
            boolean esta = false;
            for (int i = 0; i < listadoDepuradoGrupos.size(); i++) {
                if (gruposIguales(listadoDepuradoGrupos.get(i), sesion.getGrupo())) {
                    esta = true;
                    break;
                }
            }
            if (!esta) {
                listadoDepuradoGrupos.add(sesion.getGrupo());
            }
        }

        return listadoDepuradoGrupos;
    }

    public String obtenerCadenaNRCSesion(CargaDeDatosExcel cde, Sesion sesion) {
        String cadenaNRC = "";

        for (int i = 0; i < cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            if (sesion.getGrupo().getPrograma().trim().equals(cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i).trim())
                    && sesion.getGrupo().getSemestre().trim().equals(cde.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i).trim())
                    && sesion.getAsignatura().getNombre().trim().equals(cde.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i).trim())
                    && sesion.getDocente().getNombre().trim().equals(cde.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i).trim())) {
                return cde.LISTADOOFERTAEDUCATIVA_NRC.get(i).trim();
            }
        }

        return cadenaNRC;
    }

    public int obtenerCupoEstimadoFilaSesion(CargaDeDatosExcel cde, Sesion sesion) {
        int cupoestimado = -1;

        for (int i = 0; i < cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            if (sesion.getGrupo().getPrograma().trim().equals(cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i).trim())
                    && sesion.getGrupo().getSemestre().trim().equals(cde.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i).trim())
                    && sesion.getAsignatura().getNombre().trim().equals(cde.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i).trim())
                    && sesion.getDocente().getNombre().trim().equals(cde.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i).trim())) {
                return cde.LISTADOOFERTAEDUCATIVA_CUPO.get(i);
            }
        }

        return cupoestimado;
    }

    /**
     * Función que cuenta el total de sesiones presenciales a programar
     *
     * @param cde
     * @return
     */
    public int totalSesionesProgramar(CargaDeDatosExcel cde) {
        int total = 0;
        for (int i = 0; i < cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            String virtual = cde.LISTADOOFERTAEDUCATIVA_VIRTUAL.get(i);
            if (virtual.toLowerCase().trim().equals("no") && !PROGRAMARVIRTUALES) {

                if (seleccionarJornadaNombreDado(cde.LISTADOJORNADAS, cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(i)).esPresencial()) {
                    total = total + cde.LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(i) * TOTALSEMANASSEMESTRE;
                } else {
                    total = total + cde.LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(i);
                }
            }
        }
        TOTALSESIONESAPROGRAMAR = total;
        return total;
    }

    public long obtenerMinutosJornadaCadena(CargaDeDatosExcel cde, String jornada) {
        long minutos = 0;
        for (int i = 0; i < LISTADODURACIONTOTALMINUTOSJORNADA.size(); i++) {
            if (jornada.trim().toLowerCase().equals(cde.LISTADOJORNADAS.get(i).getNombre().trim().toLowerCase())) {
                return LISTADODURACIONTOTALMINUTOSJORNADA.get(i);
            }
        }
        return minutos;
    }

    public ArrayList<Sesion> listadoSesionesPorFila(int i) {
        ArrayList<Sesion> listadoSesiones = new ArrayList<Sesion>();

        String programa = CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i);
        String semestre = CDE.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i);
        String asignatura = CDE.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i);
        String docente = CDE.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i);
        for (int j = 0; j < SESIONES.size(); j++) {
            if (SESIONES.get(j).GRUPO.getPrograma().trim().equals(programa.trim())
                    && SESIONES.get(j).GRUPO.getSemestre().trim().equals(semestre.trim())
                    && SESIONES.get(j).ASIGNATURA.getNombre().trim().equals(asignatura.trim())
                    && SESIONES.get(j).DOCENTE.getNombre().trim().equals(docente.trim())) {
                listadoSesiones.add(SESIONES.get(j));
            }
        }
        return listadoSesiones;
    }

    public void crearListadoSesionesPorFila() {
        LISTADOSESIONESPORFILA = new ArrayList<ArrayList<Sesion>>();
        for (int i = 0; i < CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            LISTADOSESIONESPORFILA.add(listadoSesionesPorFila(i));
        }
    }

    public ArrayList<Date> fechasComunesEntreJornadas(ArrayList<Jornada> jornadas) {
        ArrayList<Date> fechasComunes = new ArrayList<Date>();

        for (int i = 0; i < jornadas.size() - 1; i++) {
            if (i == 0) {

                if (jornadas.get(i) == null || jornadas.get(i + 1) == null) {
                    System.out.println("");
                }

                ArrayList<Date> fechas0 = new ArrayList<Date>(getListadoHorasFechasValidasJornadaDesdeGuardado(CDE, jornadas.get(i)));
                ArrayList<Date> fechas1 = new ArrayList<Date>(getListadoHorasFechasValidasJornadaDesdeGuardado(CDE, jornadas.get(i + 1)));
                fechasComunes = interseccionFechas(fechas0, fechas1);
            } else {
                ArrayList<Date> fechas1 = new ArrayList<Date>(getListadoHorasFechasValidasJornadaDesdeGuardado(CDE, jornadas.get(i + 1)));
                fechasComunes = interseccionFechas(fechasComunes, fechas1);
            }
        }

        return fechasComunes;
    }

    public ArrayList<Date> interseccionFechas(ArrayList<Date> fechas1, ArrayList<Date> fechas2) {
        ArrayList<Date> interseccion = new ArrayList<Date>();
        for (Date f1 : fechas1) {
            for (Date f2 : fechas2) {
                if (fechasIguales(f1, f2)) {
                    interseccion.add(f1);
                    break;
                }
            }
        }
        return interseccion;
    }

    public boolean fechasIguales(Date f1, Date f2) {
        boolean iguales = false;

        Calendar c1 = Calendar.getInstance();
        c1.setTime(f1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(f2);

        if (c1.get(Calendar.DATE) == c2.get(Calendar.DATE)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY)
                && c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE)) {
            return true;
        }

        return iguales;
    }

    public boolean sesionesIguales(Sesion s1, Sesion s2) {

        if (asignaturasIguales(s1.ASIGNATURA, s2.ASIGNATURA)
                && gruposIguales(s1.GRUPO, s2.GRUPO)
                && docentesIguales(s1.DOCENTE, s2.DOCENTE)
                && fechasIguales(s1.FECHA, s2.FECHA)
                && s1.DURACION == s2.DURACION) {
            return true;
        }

        return false;
    }

    public boolean salonesIguales(Salon s1, Salon s2) {
        if (s1.getNombre().trim().equals(s2.getNombre().trim())) {
            return true;
        }
        return false;
    }

    public void cambioDocente(int indiceOrigen, int indiceFinal) {
        String docenteOrigen = CDE.LISTADOOFERTAEDUCATIVA_DOCENTE.get(indiceOrigen);
        String docenteFinal = CDE.LISTADOOFERTAEDUCATIVA_DOCENTE.get(indiceFinal);

        CDE.LISTADOOFERTAEDUCATIVA_DOCENTE.set(indiceFinal, docenteOrigen);
        CDE.LISTADOOFERTAEDUCATIVA_DOCENTE.set(indiceOrigen, docenteFinal);
    }

    public void removerSesionesParaFilaDada(int i) {
        ArrayList<Sesion> sesiones = LISTADOSESIONESPORFILA.get(i);
        for (int j = 0; j < sesiones.size(); j++) {
            for (int k = 0; k < SESIONES.size(); k++) {
                if (sesionesIguales(sesiones.get(j), SESIONES.get(k))) {
                    SESIONES.remove(k);
                }
            }
        }
    }

    public boolean sePuedeHacerIntercambio(String nombreDocente, String asignatura, String nombreJornada, int cantidadCreditos, int i) {

        if (CDE.LISTADOOFERTAEDUCATIVA_CREDITOS.get(i) == cantidadCreditos
                && CDE.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i).trim().toLowerCase().equals(asignatura.trim().toLowerCase())
                && !CDE.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i).trim().toLowerCase().equals(nombreDocente.trim().toLowerCase())
                && !CDE.LISTADOOFERTAEDUCATIVA_JORNADA.get(i).trim().toLowerCase().equals(nombreJornada.trim().toLowerCase())) {
            return true;
        }
        return false;
    }

    public boolean seDebehacerInterCambio(int indiceFila) {
        if (LISTADOSESIONESPORFILA.get(indiceFila).size() < CDE.LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(indiceFila)) {
            return true;
        }
        return false;
    }

    public boolean esElectiva(int indice) {
        if (CDE.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(indice).trim().toLowerCase().indexOf("electiva") != -1) {
            return true;
        }
        return false;
    }

    public void intercambiarAsignaciones() {
        for (int i = 0; i < CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            if (!esElectiva(i) && !esFilaVirtual(i) && !esFilaCruzadaOCompartida(CDE, i)) {
                if (seDebehacerInterCambio(i)) {
                    String docente = CDE.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i);
                    String asignatura = CDE.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i);
                    String jornada = CDE.LISTADOOFERTAEDUCATIVA_JORNADA.get(i);
                    int creditos = CDE.LISTADOOFERTAEDUCATIVA_CREDITOS.get(i);
                    for (int j = 0; j < CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); j++) {
                        if (!esElectiva(j) && !esFilaVirtual(j) && !esFilaCruzadaOCompartida(CDE, j)) {
                            if (sePuedeHacerIntercambio(docente, asignatura, jornada, creditos, j)) {

                                ArrayList<Sesion> listadoSesiones1Antes = new ArrayList<Sesion>(LISTADOSESIONESPORFILA.get(i));
                                ArrayList<Sesion> listadoSesiones2Antes = new ArrayList<Sesion>(LISTADOSESIONESPORFILA.get(j));

                                int totalSesionesProgramadasAntes = listadoSesiones1Antes.size() + listadoSesiones2Antes.size();

                                removerSesionesParaFilaDada(i);
                                removerSesionesParaFilaDada(j);

                                cambioDocente(i, j);

                                agregarSesionesIdealesFilaDada(CT, CDE, j);
                                agregarSesionesConCrucePeriodicidadFilaDada(CT, CDE, j);

                                agregarSesionesIdealesFilaDada(CT, CDE, i);
                                agregarSesionesConCrucePeriodicidadFilaDada(CT, CDE, i);

                                ArrayList<Sesion> listadoSesiones1Despues = new ArrayList<Sesion>(listadoSesionesPorFila(i));
                                ArrayList<Sesion> listadoSesiones2Despues = new ArrayList<Sesion>(listadoSesionesPorFila(j));

                                int totalSesionesProgramadasDespues = listadoSesiones1Despues.size() + listadoSesiones2Despues.size();

                                if (totalSesionesProgramadasDespues > totalSesionesProgramadasAntes) {

                                    LISTADOSESIONESPORFILA.set(i, new ArrayList<Sesion>(listadoSesiones1Despues));
                                    LISTADOSESIONESPORFILA.set(j, new ArrayList<Sesion>(listadoSesiones2Despues));

                                    System.out.println("Cambio efectivo " + SESIONES.size());
                                    System.out.println("Pasar "
                                            + CDE.LISTADOOFERTAEDUCATIVA_DOCENTE.get(j)
                                            + " EN EL GRUPO " + CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i) + " " + CDE.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i)
                                            + " A el grupo " + CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(j) + " " + CDE.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(j)
                                            + "del docente " + CDE.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i)
                                            + " aumentan" + (totalSesionesProgramadasDespues - totalSesionesProgramadasAntes)
                                    );
                                    System.out.println("Sis indices son " + i + " " + j);
                                } else {
                                    LISTADOSESIONESPORFILA.set(i, new ArrayList<Sesion>(listadoSesiones1Despues));
                                    LISTADOSESIONESPORFILA.set(j, new ArrayList<Sesion>(listadoSesiones2Despues));

                                    removerSesionesParaFilaDada(i);
                                    removerSesionesParaFilaDada(j);

                                    for (int k = 0; k < listadoSesiones1Antes.size(); k++) {
                                        SESIONES.add(listadoSesiones1Antes.get(k));
                                    }
                                    for (int k = 0; k < listadoSesiones2Antes.size(); k++) {
                                        SESIONES.add(listadoSesiones2Antes.get(k));
                                    }

                                    cambioDocente(i, j);

                                    LISTADOSESIONESPORFILA.set(i, new ArrayList<Sesion>(listadoSesiones1Antes));
                                    LISTADOSESIONESPORFILA.set(j, new ArrayList<Sesion>(listadoSesiones2Antes));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int mayorDuracionCompartida(int filaOfertaEducativa) {
        int duracion = -1;
        if (esFilaCompartida(CDE, filaOfertaEducativa)) {
            String idCompartida = CDE.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(filaOfertaEducativa);
            ArrayList<Integer> listadoIndices = indicesCruzadoCompartido(CDE.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO,
                    idCompartida);
            ArrayList<Integer> listadoDuraciones = new ArrayList<Integer>();
            for (int i = 0; i < listadoIndices.size(); i++) {
                listadoDuraciones.add(CDE.LISTADOOFERTAEDUCATIVA_DURACIONIDEALSESION.get(listadoIndices.get(i)));
            }
            return Collections.max(listadoDuraciones);
        }
        return duracion;
    }

    public int mayorCantidadSesionesCompartida(int filaOfertaEducativa) {
        int numeroSesiones = -1;
        if (esFilaCompartida(CDE, filaOfertaEducativa)) {
            String idCompartida = CDE.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(filaOfertaEducativa);
            ArrayList<Integer> listadoIndices = indicesCruzadoCompartido(CDE.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO,
                    idCompartida);
            ArrayList<Integer> listadoNumeroSesiones = new ArrayList<Integer>();
            for (int i = 0; i < listadoIndices.size(); i++) {
                listadoNumeroSesiones.add(CDE.LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(listadoIndices.get(i)));
            }
            return Collections.max(listadoNumeroSesiones);
        }
        return numeroSesiones;
    }
}
