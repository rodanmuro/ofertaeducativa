/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa;

//import ofertaeducativa.Sesion;
//import ofertaeducativa.Horario;
import java.util.Calendar;
import java.util.Date;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Rodanmuro
 */
public class MainEnsayos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            XSSFWorkbook libro = new XSSFWorkbook(
                    "F:\\Uniminuto 2020 2\\Programación 2021-1\\Insumos 1\\SALIDA HORARIOS 202065 07072020 ParaEnsayosConElPrograma.xlsx");
            ValidacionesOfertaEducativa.validarEncabezadosOfertaEducativaInicialCrearSeguimientos(libro.getSheetAt(0)); 
        
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
