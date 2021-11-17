/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa;

import java.util.ArrayList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 *
 * @author Usuario
 */
public class ValidacionesOfertaEducativa {

    public static boolean validarEncabezadosOfertaEducativaInicialCrearSeguimientos(XSSFSheet hojaOfertaOriginal) {

        ArrayList<String> listadoEncabezados = new ArrayList<String>();

        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_PROGRAMA);
        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_SEMESTRE);
        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_JORNADA);
        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_ALFA);
        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_NUMERICO);
        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_CREDITOS);
        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_ASIGNATURA);
        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_NRC);
        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_CUPO);
        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_DOCENTE);
        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_SALON);
        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_VIRTUAL);
        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_ICRUCECOMP);
        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_NUMEROSESIONES);
        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_DURACIONSESION);
        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_PERIODICIDAD);
        listadoEncabezados.add(ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_CUPOMAXIMO);

        for (String encabezado : listadoEncabezados) {

            XSSFCell celda = ObtenerEncabezados.obtenerCeldaEncabezado(hojaOfertaOriginal,
                    ValoresEncabezados.OFERTAEDUCATIVA_INDICEFILAENCABEZADOS,
                    encabezado);

            if (celda == null) {
                Validaciones.mostrarVentanaError("El encabezado "
                        + encabezado
                        + " no se ha encontrado en la hoja " + hojaOfertaOriginal.getSheetName()
                        + " del libro de oferta educativa");
                return false;
            }

        }
        
        return true;

    }

}
