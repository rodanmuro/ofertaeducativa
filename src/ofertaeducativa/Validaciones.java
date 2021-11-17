/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JOptionPane;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Usuario
 */
public class Validaciones {
    
    /**
     * Muestra un JOption Pane con el mensaje de error de la Excepción recogida
     *
     * @param e
     */
    public static void mostrarVentanaError(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        JOptionPane.showMessageDialog(null, errors.toString());
    }

    /**
     * Muestra un JOptionPane con el mensaje de error
     *
     * @param error String con el error a mostrar
     */
    public static void mostrarVentanaError(String error) {
        JOptionPane.showMessageDialog(null, error);
    }
    
    public static void mostrarErroresTotal(String textoPersonalizado, Exception e) {
        Validaciones.mostrarVentanaError(e);
        Validaciones.mostrarVentanaError(textoPersonalizado);
        e.printStackTrace();
    }
    
    
    
}
