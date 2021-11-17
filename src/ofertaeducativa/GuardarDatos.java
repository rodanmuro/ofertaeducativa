/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Usuario
 */
public class GuardarDatos {

    Constantes C;
    static String CARPETADATA = "data/";

    public GuardarDatos(Constantes c) {
        C = c;
    }

    public static void guardarDatos(Constantes c) {
        try {
            
            FileOutputStream fos = new FileOutputStream(CARPETADATA+"constantesOE.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(c);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Constantes recuperarDatos() {
        Constantes c = null;
        try {
            File f = new File(CARPETADATA+"constantesOE.ser");
            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                c = (Constantes) ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
    
    public static void guardarHorario(){
    
    }
}
