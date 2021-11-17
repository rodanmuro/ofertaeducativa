/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa2;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author rodanmuro
 */
public class Hora implements Serializable{
    
    int HORA = 0;
    int MINUTOS = 0;
    

    public Hora(int hora, int minutos) {
        HORA = hora;
        MINUTOS = minutos;
    }

    public int getMinutos() {
        return MINUTOS;
    }

    public int getHora() {
        return HORA;
    }

    public void setMinutos(int minutos) {
        MINUTOS = minutos;
    }

    public void setHora(int hora) {
        HORA = hora;
    }
    
    public boolean before(Hora hora){
        boolean xbeforey = false;
        
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY, this.getHora());
        c1.set(Calendar.MINUTE, this.getMinutos());
        c1.set(Calendar.SECOND, 0);
        
        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.HOUR_OF_DAY, hora.getHora());
        c2.set(Calendar.MINUTE, hora.getMinutos());
        c2.set(Calendar.SECOND, 0);
        
        
        if(c1.before(c2)){
            xbeforey = true;
        }
        
        
        return xbeforey;
    }
    
    public boolean beforeIgual(Hora hora){
        boolean xbeforey = false;
        
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY, this.getHora());
        c1.set(Calendar.MINUTE, this.getMinutos());
        c1.set(Calendar.SECOND, 0);
        
        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.HOUR_OF_DAY, hora.getHora());
        c2.set(Calendar.MINUTE, hora.getMinutos());
        c2.set(Calendar.SECOND, 0);
        
        
        if(c1.before(c2) || c1.equals(c2)){
            xbeforey = true;
        }
        
        return xbeforey;
    }
    
    public boolean after(Hora hora){
        boolean xbeforey = false;
        
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY, this.getHora());
        c1.set(Calendar.MINUTE, this.getMinutos());
        c1.set(Calendar.SECOND, 0);
        
        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.HOUR_OF_DAY, hora.getHora());
        c2.set(Calendar.MINUTE, hora.getMinutos());
        c2.set(Calendar.SECOND, 0);
        
        
        if(c1.after(c2)){
            xbeforey = true;
        }
        
        
        return xbeforey;
    }
    
    public boolean afterIgual(Hora hora){
        boolean xbeforey = false;
        
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY, this.getHora());
        c1.set(Calendar.MINUTE, this.getMinutos());
        c1.set(Calendar.SECOND, 0);
        
        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.HOUR_OF_DAY, hora.getHora());
        c2.set(Calendar.MINUTE, hora.getMinutos());
        c2.set(Calendar.SECOND, 0);
        
        
        if(c1.after(c2) || c1.equals(c2)){
            xbeforey = true;
        }
        
        return xbeforey;
    }
    
    public String toString(){
        return "Horas: "+getHora()+" Minutos "+getMinutos();
    }

}
