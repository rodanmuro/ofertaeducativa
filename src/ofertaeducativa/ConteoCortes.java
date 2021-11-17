/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa;

//libreias de excel de apache poi
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
//import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
//import org.junit.internal.Throwables;

/**
 *
 * @author Rodanmuro
 */
public class ConteoCortes {

    ArrayList<Object[]> LISTADO_GRUPOS = new ArrayList<Object[]>();
    String RUTA_ARCHIVO_EXCEL = "";

    Date FECHA_ACTUAL = null;
    boolean INCLUIR_FECHA_ACTUAL = false;

    int NUMERO_ENCABEZADOS_ANTES_FECHA = 13;
    //recordar que en java el conteo de filas y columnas empieza desde cero
    //para el archivo de oferta educativa 2017-2 el valor es 13
    //en el caso de 2018-1
    int COLUMNA_INICIAL_FECHAS = 15;

    //ESTE VALOR SE DEFINE EN EL CONSTRUCTOR, AL OBTENER EL ARCHIVO DE EXCEL
    int COLUMNA_FINAL_FECHAS = 0;

    int INDICE_FILA_PROGRAMA = 0;
    int INDICE_FILA_SEMESTRE = 1;
    int INDICE_FILA_JORNADA = 2;
    int INDICE_FILA_INICIAL_GRUPO = 3;
    int INDICE_FILA_FINAL_GRUPO = 4;

    FileInputStream iS;
    XSSFWorkbook workbook, libroDestino;

    XSSFSheet hoja;

    int INDICE_HOJA_GRUPOS = 0;

    int TOTAL_ENCABEZADOS_SIN_FECHAS = 12;
    int INDICE_FILA_FECHAS = 1;
    int INDICE_FILA_NOMBRES_DIAS = 0;

    public ConteoCortes(String rutaArchivoExcel, int filaInicial, Date fechaActual, boolean incluirFechaActual) {

        //en esta función definimos la constante COLUMNA_FINAL_FECHAS
        RUTA_ARCHIVO_EXCEL = rutaArchivoExcel;
        try {
            iS = new FileInputStream(RUTA_ARCHIVO_EXCEL);
            workbook = new XSSFWorkbook(iS);
            hoja = workbook.getSheetAt(INDICE_HOJA_GRUPOS);
            indiceFinalFechas();
            FECHA_ACTUAL = fechaActual;
            INCLUIR_FECHA_ACTUAL = incluirFechaActual;

            libroDestino = new XSSFWorkbook();

        } catch (Exception e) {
            e.printStackTrace();
        }

//        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
//        Date hora = obtenerCeldasFechasFila(5).get(0).getDateCellValue();
//        formatoHora.format(hora);
//        System.out.println("hora: "+ formatoHora.format(hora));;
        pasarDatosAlArchivoSalida(2);

//        crearArchivoSalida("C:\\Users\\Rodanmuro\\Desktop\\carpetasalida", "cortes");
    }

    public void pasarDatosAlArchivoSalida(int filaInicial) {

        SimpleDateFormat formatoMesDia = new SimpleDateFormat("EE-MMM-d");
        XSSFSheet hojaDestino = libroDestino.createSheet(formatoMesDia.format(FECHA_ACTUAL));

        //pasamos los encabezados
        hojaDestino.createRow(0).createCell(0).setCellValue("Programa");
        hojaDestino.getRow(0).createCell(1).setCellValue("Semestre");
        hojaDestino.getRow(0).createCell(2).setCellValue("Jornada");
        hojaDestino.getRow(0).createCell(3).setCellValue("Asignatura");
        hojaDestino.getRow(0).createCell(4).setCellValue("NRC");
        hojaDestino.getRow(0).createCell(5).setCellValue("Total fechas dadas");
        hojaDestino.getRow(0).createCell(6).setCellValue("Total fechas programadas");

        //vamos a recorrer el archivo origen
        int contadorFilasOrigen = 0;
        XSSFCell celdaGuia = hoja.getRow(filaInicial + contadorFilasOrigen).getCell(0);

        while ( !celdaVacia(hoja.getRow(filaInicial + contadorFilasOrigen), 0) /*celdaGuia != null*/ && celdaGuia.getCellType() != 3) {
            //fila por fila vamos pasandos los datos

            String programa = hoja.getRow(2 + contadorFilasOrigen).getCell(0).getStringCellValue();
            String semestre = hoja.getRow(2 + contadorFilasOrigen).getCell(1).getStringCellValue();
            String jornada = hoja.getRow(2 + contadorFilasOrigen).getCell(4).getStringCellValue();
            String asignatura = hoja.getRow(2 + contadorFilasOrigen).getCell(8).getStringCellValue();

            hojaDestino.createRow(1 + contadorFilasOrigen).createCell(0).setCellValue(programa);
            hojaDestino.getRow(1 + contadorFilasOrigen).createCell(1).setCellValue(semestre);
            hojaDestino.getRow(1 + contadorFilasOrigen).createCell(2).setCellValue(jornada);
            hojaDestino.getRow(1 + contadorFilasOrigen).createCell(3).setCellValue(asignatura);

            Object nrc = null;
            if (hoja.getRow(2 + contadorFilasOrigen).getCell(9).getCellType() == 0) {
                nrc = hoja.getRow(2 + contadorFilasOrigen).getCell(9).getNumericCellValue();
                hojaDestino.getRow(1 + contadorFilasOrigen).createCell(4).setCellValue((Double) nrc);
            }
            if (hoja.getRow(2 + contadorFilasOrigen).getCell(9).getCellType() == 1) {
                nrc = (String) hoja.getRow(2 + contadorFilasOrigen).getCell(9).getStringCellValue();
                hojaDestino.getRow(1 + contadorFilasOrigen).createCell(4).setCellValue((String) nrc);
            }

            ArrayList<XSSFCell> celdasFechasFila = obtenerCeldasFechasFila(filaInicial + contadorFilasOrigen);
            ArrayList<Date> listadoFechasProgramadas = listadoFechasProgramadas(celdasFechasFila);

            int totalFechasHaDado = totalFechasDadas(listadoFechasProgramadas, FECHA_ACTUAL, INCLUIR_FECHA_ACTUAL);
            int totalFechasProgramado = totalFechasProgramadas(celdasFechasFila);

            hojaDestino.getRow(1 + contadorFilasOrigen).createCell(5).setCellValue(totalFechasHaDado);
            hojaDestino.getRow(1 + contadorFilasOrigen).createCell(6).setCellValue(totalFechasProgramado);

            XSSFCellStyle formatoCeldaMesDia = libroDestino.createCellStyle();
            XSSFCreationHelper fayuda = libroDestino.getCreationHelper();
            formatoCeldaMesDia.setDataFormat(fayuda.createDataFormat().getFormat("dd-MMM"));

            for (int i = 0; i < listadoFechasProgramadas.size(); i++) {
                //acá obtengo las fechas programadas

                String fechaDiaMesDia = formatoMesDia.format(listadoFechasProgramadas.get(i));
                Date datefechaDiaMesDia = new Date();
                try {
                    datefechaDiaMesDia = formatoMesDia.parse(fechaDiaMesDia);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //acá obtengo las horas de esas fechas
                SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

//                String valor = celdasFechasFila.get(i).getStringCellValue();
                System.out.println("valor fila" + celdasFechasFila.get(i).getRowIndex() + " columna " + celdasFechasFila.get(i).getColumnIndex());
                //String hora = formatoHora.format(celdasFechasFila.get(i).getDateCellValue());

//System.out.println("fila: "+contadorFilasOrigen+ "celda "+celdasFechasFila.get(i).getDateCellValue());
//                hojaDestino.getRow(1 + contadorFilasOrigen).createCell(7 + i).setCellValue(fechaDiaMesDia + " " + hora);
                hojaDestino.getRow(1 + contadorFilasOrigen).createCell(7 + i).setCellValue(datefechaDiaMesDia);
                hojaDestino.getRow(1 + contadorFilasOrigen).getCell(7 + i).setCellStyle(formatoCeldaMesDia);

            }

            
            try {
                celdaGuia = hoja.getRow(filaInicial + contadorFilasOrigen).getCell(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            contadorFilasOrigen++;
            
        }

    }

    public void crearArchivoSalida(String rutaSalida, String nombreArchivo) {

        try {
            //creamos el archivo
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");

            Date fechaYHoraSalida = new Date();
            String fechaSalidaFormateada = sdf.format(fechaYHoraSalida);

            File archivoSalida = new File(rutaSalida + "/" + nombreArchivo + " " + fechaSalidaFormateada + ".xlsx");
            System.out.println("Se creará el archivo: " + rutaSalida + "/" + nombreArchivo + " " + fechaYHoraSalida.toString() + ".xlsx");
            FileOutputStream file = new FileOutputStream(archivoSalida);
            libroDestino.write(file);
            file.close();
            JOptionPane.showMessageDialog(null, "Archivo creado");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al crear el archivo");
            e.printStackTrace();
        }

    }

    public Date obtenerFechaCeldaDada(XSSFCell celda) {
        Date fecha = null;
        int columnaIndiceFecha = celda.getColumnIndex();
        int filaIndiceFecha = INDICE_FILA_FECHAS;

        return hoja.getRow(filaIndiceFecha).getCell(columnaIndiceFecha).getDateCellValue();

//        return fecha;
    }

    public String obtenerNombreDiaCeldaDada(XSSFCell celda) {
        Date fecha = null;
        int columnaIndiceFecha = celda.getColumnIndex();
        int filaIndiceFecha = INDICE_FILA_NOMBRES_DIAS;

        return hoja.getRow(filaIndiceFecha).getCell(columnaIndiceFecha).getStringCellValue();

//        return fecha;
    }

    public ArrayList<XSSFCell> obtenerCeldasFechasFila(int fila) {
        ArrayList<XSSFCell> celdasFechas = new ArrayList<XSSFCell>();

        for (int i = COLUMNA_INICIAL_FECHAS; i <= COLUMNA_FINAL_FECHAS; i++) {
            XSSFRow filaXL = hoja.getRow(fila);
            XSSFCell celda = filaXL.getCell(i);
            if (!celdaVacia(filaXL, i) /*celda != null*/) {
                if (celda.getCellType() != 3) {
                    celdasFechas.add(celda);
                }
            }
        }

        return celdasFechas;
    }

    public ArrayList<Date> listadoFechasProgramadas(ArrayList<XSSFCell> listadoCeldasProgramadas) {

        ArrayList<Date> listadoFechasProgramadas = new ArrayList<>();

        for (int i = 0; i < listadoCeldasProgramadas.size(); i++) {
            listadoFechasProgramadas.add(obtenerFechaCeldaDada(listadoCeldasProgramadas.get(i)));
        }

        return listadoFechasProgramadas;
    }

    public int totalFechasDadas(ArrayList<Date> listadoFechasProgramadas, Date fechaActual, boolean incluirFechaActual) {

        int totalfechasdadas = 0;

        SimpleDateFormat formatoAnoMesDia = new SimpleDateFormat("yyyy-MM-dd");

        Date fechaActualAdentro = new Date();
        try {
            fechaActualAdentro = formatoAnoMesDia.parse(formatoAnoMesDia.format(fechaActual));
        } catch (ParseException ex) {
            Logger.getLogger(ConteoCortes.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < listadoFechasProgramadas.size(); i++) {
            Date fechaProgramada = listadoFechasProgramadas.get(i);
            try {
                fechaProgramada = formatoAnoMesDia.parse(formatoAnoMesDia.format(fechaProgramada));
            } catch (ParseException ex) {
                Logger.getLogger(ConteoCortes.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (incluirFechaActual) {
                if (fechaActualAdentro.after(fechaProgramada) || fechaActualAdentro.equals(fechaProgramada)) {
                    totalfechasdadas++;
                }
            } else if (fechaActualAdentro.after(fechaProgramada)) {
                totalfechasdadas++;
            }
        }

        return totalfechasdadas;

    }

    public int totalFechasProgramadas(ArrayList<XSSFCell> listadoFechasProgramadas) {
        return listadoFechasProgramadas.size();
    }

    public void indiceFinalFechas() throws IOException {
        COLUMNA_FINAL_FECHAS = COLUMNA_INICIAL_FECHAS + numeroFechas();
        System.out.println("numero de fechas: " + numeroFechas() + " columna final fechas " + COLUMNA_FINAL_FECHAS);
    }

    public int numeroFechas() throws IOException {
        return numeroCabeceras() - NUMERO_ENCABEZADOS_ANTES_FECHA;
    }

    public int numeroCabeceras() throws IOException {
        int nC;
        nC = 0;

//        iS = new FileInputStream(RUTA_ARCHIVO_EXCEL);
//        workbook = new XSSFWorkbook(iS);
//        System.out.println("ruta del archivo " + RUTA_ARCHIVO_EXCEL);
//        hoja = workbook.getSheetAt(INDICE_HOJA_GRUPOS);
        XSSFRow fila = hoja.getRow(INDICE_FILA_FECHAS);

        int contadorColumnas = 0;
        while (!celdaVacia(fila, contadorColumnas)) {
//            System.out.println("celda " + contadorColumnas + " " + fila.getCell(contadorColumnas).getStringCellValue());
            contadorColumnas++;
        }

        nC = contadorColumnas-1;

//        System.out.println("numero cabeceras: " + nC);
        return nC;
    }

    public boolean celdaVacia(XSSFRow fila, int indiceCelda) {
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
