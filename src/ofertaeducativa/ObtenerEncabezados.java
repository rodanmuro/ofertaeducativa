/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 *
 * @author Usuario
 */
public class ObtenerEncabezados {
    
    /**
     * Devuelve la columna para un encabezado dado. La fila donde están los encabezados
     * no puede tener encabezados numéricos
     * @param hoja
     * @param indiceFila
     * @param encabezado
     * @return 
     */
    public static XSSFCell obtenerCeldaEncabezado(XSSFSheet hoja, int indiceFila, String encabezado) {
        XSSFCell celda = null;
        int j = 0;
        while (!celdaVacia(hoja.getRow(indiceFila), j)/* && j < INDICECOLUMNAINICIANFECHAS*/) {

            if (hoja.getRow(indiceFila).getCell(j).getCellTypeEnum() == CellType.STRING) {
                if (hoja.getRow(indiceFila).getCell(j).getStringCellValue().toLowerCase().trim().equals(encabezado.toLowerCase().trim())) {
                    celda = hoja.getRow(indiceFila).getCell(j);
                    return celda;
                }
            }else{
                break;
            }

            j++;
        }
        return celda;
    }

    public static boolean celdaVacia(XSSFRow fila, int indiceCelda) {
        boolean cv = false;

        if (fila == null) {
            return true;
        }

        if (fila.getCell(indiceCelda) == null) {
            return true;
        }

        if (fila.getCell(indiceCelda).getCellTypeEnum() == CellType.BLANK) {
            return true;
        }

        if (fila.getCell(indiceCelda).getCellTypeEnum() == CellType.STRING) {
            if (fila.getCell(indiceCelda).getStringCellValue().trim().equals("")) {
                return true;
            }
        }

        return cv;
    }

}
