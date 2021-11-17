/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa2;

import java.io.Serializable;

/**
 *
 * @author rodanmuro
 */
public class TuplaDiaHoraInicialFinal implements Serializable {

    int DIA = 0;
    Hora HORAINICIAL = null;
    Hora HORAFINAL = null;
    
    /**
     * Este tupla es la combinación de un día, una hora inicial y una hora final
     * La hora final, es el máximo tiempo de dicha tupla.
     * Si hablamos de una tupla 3, horainicial 17 y horafinal 19:30, quiere decir
     * que el maximo valor que toma la tupla es ese 19:30. El valor que corresponde al día domingo
     * es el 1, para el lunes el 2, y así sucesivamente, según la clase Calendar
     * @param dia
     * @param horaInicial
     * @param horaFinal 
     */
    public TuplaDiaHoraInicialFinal(int dia, Hora horaInicial, Hora horaFinal) {
        DIA = dia;
        HORAINICIAL = horaInicial;
        HORAFINAL = horaFinal;
    }

    public int getDia() {
        return DIA;
    }

    public Hora getHoraInicial() {
        return HORAINICIAL;
    }

    public Hora getHoraFinal() {
        return HORAFINAL;
    }

    public void setDia(int dia) {
        DIA = dia;
    }

    public void setHoraInicial(Hora horaInicial) {
        HORAINICIAL = horaInicial;
    }

    public void setHoraFinal(Hora horaFinal) {
        HORAFINAL = horaFinal;
    }
    
    public String toString(){
        return "Día: " + getDia()+ " "+getHoraInicial().toString()+" "+getHoraFinal().toString();
    }

}
