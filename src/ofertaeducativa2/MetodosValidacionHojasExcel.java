/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa2;

import java.util.ArrayList;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Usuario
 */
public class MetodosValidacionHojasExcel {

    public MetodosValidacionHojasExcel() {
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

    public static XSSFCell obtenerCeldaEncabezado(XSSFSheet hoja, int indiceFila, String encabezado) {
        XSSFCell celda = null;
        for (int i = 0; i < hoja.getRow(indiceFila).getLastCellNum(); i++) {
            if (hoja.getRow(indiceFila).getCell(i) != null) {
                if (hoja.getRow(indiceFila).getCell(i).getStringCellValue().toLowerCase().trim().equals(encabezado.toLowerCase().trim())) {
                    celda = hoja.getRow(indiceFila).getCell(i);
                    break;
                }
            }
        }
        return celda;
    }

    public static boolean esCeldaNumerica(XSSFCell celda) {
        boolean si = false;
        if (celda.getCellTypeEnum() == CellType.NUMERIC) {
            si = true;
        }
        return si;
    }

    public static boolean esCeldaCadena(XSSFCell celda) {
        boolean si = false;
        if (celda.getCellTypeEnum() == CellType.STRING) {
            si = true;
        }
        return si;
    }

    public static boolean esCeldaBlanco(XSSFCell celda) {
        boolean si = false;
        if (celda.getCellTypeEnum() == CellType.BLANK) {
            si = true;
        }
        return si;
    }

    public static void cargarColumnaEnArrayList(XSSFWorkbook libroFuente, String nombreHojaFuente, String encabezadoFuente, String encabezadoContador, int filaInicial, int FILAINICIALENCABEZADOS, ArrayList listadoObjetivo, String tipoArreglo) {
        int contadorFilas = 0;

        XSSFSheet hojaFuente = libroFuente.getSheet(nombreHojaFuente);
        XSSFCell celdaContador = obtenerCeldaEncabezado(hojaFuente, filaInicial, encabezadoContador);
        XSSFCell celdaDatos = obtenerCeldaEncabezado(hojaFuente, filaInicial, encabezadoFuente);

        int indiceColumnaContadora = celdaContador.getColumnIndex();
        int indiceColumnaDatos = celdaDatos.getColumnIndex();

        XSSFRow filaContadora = hojaFuente.getRow(filaInicial + FILAINICIALENCABEZADOS);

        while (!celdaVacia(filaContadora, indiceColumnaContadora)) {
            if (esCeldaNumerica(filaContadora.getCell(indiceColumnaDatos))) {
                if (tipoArreglo.equals("date")) {
                    listadoObjetivo.add(filaContadora.getCell(indiceColumnaDatos).getDateCellValue());
                } else {
                    if (tipoArreglo.equals("string")) {
                        //se desea cargar un string, pero la celda tiene un numero
                        listadoObjetivo.add("" + filaContadora.getCell(indiceColumnaDatos).getNumericCellValue());
                    } else {
                        listadoObjetivo.add((int) filaContadora.getCell(indiceColumnaDatos).getNumericCellValue());
                    }

                }
            }
            if (esCeldaCadena(filaContadora.getCell(indiceColumnaDatos))) {
                listadoObjetivo.add(filaContadora.getCell(indiceColumnaDatos).getStringCellValue());
            }
            if (esCeldaBlanco(filaContadora.getCell(indiceColumnaDatos))) {
                if (tipoArreglo.equals("string")) {
                    listadoObjetivo.add("");
                }
                if (tipoArreglo.equals("integer")) {
                    listadoObjetivo.add(0);
                }
            }

            contadorFilas++;
            filaContadora = hojaFuente.getRow(filaInicial + FILAINICIALENCABEZADOS + contadorFilas);
        }
    }
    
}