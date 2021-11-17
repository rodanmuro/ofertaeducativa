/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Usuario
 */
public class OcupacionSalon {

    Salon SALON;
    String NOMBRESALON = "";
    ArrayList<TuplaDiaHoraInicialFinal> TUPLADIAHORAINICIALFINAL = null;
    int PERIODICIDAD = 0;
    int CUPOSALON = 0;
    Date FECHAINICIAL;
    Date FECHAFINAL;
    int DURACIONMINUTOSJORNADA = 0;

    /**
     *
     * @param nombre
     * @param tuplaDiaHoraInicialFinal
     */
    public OcupacionSalon(Salon salon,
            ArrayList<TuplaDiaHoraInicialFinal> tuplaDiaHoraInicialFinal,
            int periodicidadDias,
            Date fechaInicial,
            Date fechaFinal
    ) {
        SALON = salon;
        NOMBRESALON = salon.getNombre();
        CUPOSALON = salon.getCupo();
        TUPLADIAHORAINICIALFINAL = tuplaDiaHoraInicialFinal;
        PERIODICIDAD = periodicidadDias;
        FECHAINICIAL = fechaInicial;
        FECHAFINAL = fechaFinal;

        if (fechaInicial != null && fechaFinal != null) {
            normalizarFechaInicial(fechaInicial);
            normalizarFechaFinal(fechaFinal);
        }

    }

    public void normalizarFechaInicial(Date fechaInicial) {

        Calendar c = Calendar.getInstance();
        c.setTime(fechaInicial);
        c.set(Calendar.HOUR_OF_DAY, 1);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        FECHAINICIAL = c.getTime();
    }

    public void normalizarFechaFinal(Date fechaFinal) {

        Calendar c = Calendar.getInstance();
        c.setTime(fechaFinal);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        FECHAFINAL = c.getTime();
    }

    public String getNombresSalon() {
        return NOMBRESALON;
    }

    public ArrayList<TuplaDiaHoraInicialFinal> getListadoTuplasDiaHoraInicialFinal() {
        return TUPLADIAHORAINICIALFINAL;
    }

    public int getPeriodicidad() {
        return PERIODICIDAD;
    }

    public Date getFechaInicial() {
        return FECHAINICIAL;
    }

    public Date getFechaFinal() {
        return FECHAFINAL;
    }

    public void setPeriodicidad(int valor) {
        PERIODICIDAD = valor;
    }

    public void setTuplaDiaHoraInicialFinal(ArrayList<TuplaDiaHoraInicialFinal> tuplaDiaHoraInicialFinal) {
        TUPLADIAHORAINICIALFINAL = tuplaDiaHoraInicialFinal;
    }

    public void setFechaInicial(Date fechaInicial) {
        FECHAINICIAL = fechaInicial;
    }

    public String toString() {

        String cadena = "Ocupación : " + getNombresSalon();
        for (int i = 0; i < getListadoTuplasDiaHoraInicialFinal().size(); i++) {
            cadena = cadena + " " + getListadoTuplasDiaHoraInicialFinal().get(i).toString();
        }
        cadena = cadena + " Periodicidad cada " + getPeriodicidad() + " días";
        return cadena;
    }

    public long duracionMinutosJornada() {
        long minutes = 0;
        for (int i = 0; i < this.TUPLADIAHORAINICIALFINAL.size(); i++) {
            TuplaDiaHoraInicialFinal tdhf = TUPLADIAHORAINICIALFINAL.get(i);
            Calendar ci = Calendar.getInstance();
            ci.set(Calendar.HOUR_OF_DAY, tdhf.getHoraInicial().getHora());
            ci.set(Calendar.MINUTE, tdhf.getHoraInicial().getMinutos());

            Calendar cf = Calendar.getInstance();
            cf.set(Calendar.HOUR_OF_DAY, tdhf.getHoraFinal().getHora());
            cf.set(Calendar.MINUTE, tdhf.getHoraFinal().getMinutos());

            long diff = cf.getTime().getTime() - ci.getTime().getTime();

            minutes = minutes + TimeUnit.MILLISECONDS.toMinutes(diff);
        }
        return minutes;
    }

}
