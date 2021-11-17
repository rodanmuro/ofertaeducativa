/**
 * La clase Sesion hace parte de la refacgtorización de la creación de horarios
 * Un objeto sesion está conformado por los elementos
 * String docente, String nomenclaturaPrograma, String grupo, Date fecha, int duracion
 * Crear una sesión sirve para compararla posteriormente sabiendo si se cruza o no
 * con otra sesion.
 * La comparación entre dos sesiones se hace en la clase Horario
 *
 */
package ofertaeducativa2;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author rodanmuro
 */
public class Sesion implements Serializable {

    Docente DOCENTE = null;
    Grupo GRUPO = null;
    Asignatura ASIGNATURA = null;
    Date FECHA = null;
    int DURACION = 0;

    /**
     *
     * @param docente
     * @param grupo
     * @param asignatura
     * @param fecha
     * @param duracion
     */
    public Sesion(Docente docente, Grupo grupo, Asignatura asignatura, Date fecha, int duracion/*, Salon salon*/) {

        DOCENTE = docente;
        GRUPO = grupo;
        ASIGNATURA = asignatura;
        FECHA = fecha;
        DURACION = duracion;

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String sFecha = sdf.format(fecha);

        try {
            this.FECHA = sdf.parse(sFecha);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.DURACION = duracion;
    }

    public int getCantidadHorasSegunDuracion(int duracion) {
        return this.DURACION / duracion;
    }

    public Date horaInicioSesion() {
        Calendar c = Calendar.getInstance();
        c.setTime(this.FECHA);

        return c.getTime();
    }
    
    public int horaInicioSesionEntero() {
        Calendar c = Calendar.getInstance();
        c.setTime(this.FECHA);
        
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minutos = c.get(Calendar.MINUTE);
        
        int horaInicio = hora*100+minutos;

        return horaInicio;
    }

    public Date horaFinalSesion() {
        Calendar c = Calendar.getInstance();
        c.setTime(this.FECHA);
        c.add(Calendar.MINUTE, this.DURACION);

        return c.getTime();
    }

    public Docente getDocente() {
        return DOCENTE;
    }

    public Grupo getGrupo() {
        return GRUPO;
    }

    public Asignatura getAsignatura() {
        return ASIGNATURA;
    }

    public Date getFecha() {
        return FECHA;
    }

    public int getDuracion() {
        return DURACION;
    }

    public String diaSemanaEspanol() {

        String diaSemana = "";

        Calendar fecha = Calendar.getInstance();
        fecha.setTime(FECHA);

        int diaNumero = fecha.get(Calendar.DAY_OF_WEEK);

        switch (diaNumero) {
            case 1:
                diaSemana = "domingo";
                break;
            case 2:
                diaSemana = "lunes";
                break;
            case 3:
                diaSemana = "martes";
                break;
            case 4:
                diaSemana = "miércoles";
                break;
            case 5:
                diaSemana = "jueves";
                break;
            case 6:
                diaSemana = "viernes";
                break;
            case 7:
                diaSemana = "sábado";
                break;

        }

        return diaSemana;

    }
    
    public static String diaSemanaEspanol(Date date) {

        String diaSemana = "";

        Calendar fecha = Calendar.getInstance();
        fecha.setTime(date);

        int diaNumero = fecha.get(Calendar.DAY_OF_WEEK);

        switch (diaNumero) {
            case 1:
                diaSemana = "domingo";
                break;
            case 2:
                diaSemana = "lunes";
                break;
            case 3:
                diaSemana = "martes";
                break;
            case 4:
                diaSemana = "miércoles";
                break;
            case 5:
                diaSemana = "jueves";
                break;
            case 6:
                diaSemana = "viernes";
                break;
            case 7:
                diaSemana = "sábado";
                break;

        }

        return diaSemana;

    }

    public void setDocente(Docente docente) {
        DOCENTE = docente;
    }

    public void setGrupo(Grupo grupo) {
        GRUPO = grupo;
    }

    public void setAsignatura(Asignatura asignatura) {
        ASIGNATURA = asignatura;
    }

    public void setFecha(Date fecha) {
        FECHA = fecha;
    }

    public void setDuracion(int duracion) {
        DURACION = duracion;
    }

    public String enCadena() {
        return "DOCENTE: " + DOCENTE.getNombre()
                + " GRUPO: " + GRUPO.getPrograma()
                + " " + GRUPO.getSemestre()
                + " ASIGNATURA " + ASIGNATURA.getNombre()
                + " HORAINICIOSESION " + horaInicioSesion()
                + " HORAFINALSESION " + horaFinalSesion();
    }
}
