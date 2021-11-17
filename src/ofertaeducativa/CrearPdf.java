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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
//import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.util.RegionUtil;

/**
 *
 * @author Rodanmuro
 */
public class CrearPdf {

//    filaLg[0] = programa_1;
//    filaLg[1] = semestre_1;
//    filaLg[2] = jornada_1;
//    filaLg[3] = filaInicialPrograma;
//    filaLg[4] = filaFinalPrograma;
    ArrayList<Object[]> LISTADO_GRUPOS = new ArrayList<Object[]>();
    ArrayList<Object[]> LISTADO_DOCENTES = new ArrayList<Object[]>();
    String RUTA_ARCHIVO_EXCEL = "";
    int NUMERO_ENCABEZADOS_ANTES_FECHA = 18;//incluyendo el id de cruce compartido
    //recordar que en java el conteo de filas y columnas empieza desde cero
    //en la versión 2 del archivo de oferta educativa las fehcas empeizan en la columna 14
    //en la version 3 las fechas inician en la columna 18, base 0
    int COLUMNA_INICIAL_FECHAS = 18;

    //ESTE VALOR SE DEFINE EN EL CONSTRUCTOR, AL OBTENER EL ARCHIVO DE EXCEL
    int COLUMNA_FINAL_FECHAS = 0;

    int INDICE_FILA_PROGRAMA = 0;
    int INDICE_FILA_SEMESTRE = 1;
    int INDICE_FILA_JORNADA = 2;
    int INDICE_FILA_DOCENTE = 5;
    int INDICE_FILA_INICIAL_GRUPO = 3;
    int INDICE_FILA_FINAL_GRUPO = 4;
    int INDICE_INICIAN_FECHAS_DESTINO = 12;

    int INDICE_FILA_INICIAN_ASIGNATURAS_GRUPO_EN_SU_HORARIO = 2;

    FileInputStream iS;
    XSSFWorkbook workbook;
    XSSFSheet hoja;

    int INDICE_HOJA_GRUPOS = 0;

    int TOTAL_ENCABEZADOS_SIN_FECHAS = 18;

    public CrearPdf(String rutaArchivoExcel, int filaInicial) throws IOException {
        RUTA_ARCHIVO_EXCEL = rutaArchivoExcel;
        LISTADO_GRUPOS = listadoGrupos(rutaArchivoExcel, filaInicial);
        LISTADO_DOCENTES = listadoDocentes(rutaArchivoExcel, filaInicial);
        try {
            iS = new FileInputStream(RUTA_ARCHIVO_EXCEL);
            workbook = new XSSFWorkbook(iS);
            hoja = workbook.getSheetAt(INDICE_HOJA_GRUPOS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //se deben crear variables globales
        //en esta función definimos la constante COLUMNA_FINAL_FECHAS
        indiceFinalFechas();

        int indiceGrupo = 61;

//        System.out.println("primer grupo: programa: " + getPrograma(LISTADO_GRUPOS.get(indiceGrupo))
//                + " semestre " + getSemestre(LISTADO_GRUPOS.get(indiceGrupo))
//                + " jornada "
//                + getJornada(LISTADO_GRUPOS.get(indiceGrupo))
//                + " filainicial: " + getFilaInicialPrograma(LISTADO_GRUPOS.get(indiceGrupo))
//                + " filafinal: " + getFilaFinalPrograma(LISTADO_GRUPOS.get(indiceGrupo)));
//        System.out.println("numero de cabeceras: " + numeroCabeceras() +" numero de fechas: "+numeroFechas());
        System.out.println("columna inicial fechas: " + COLUMNA_INICIAL_FECHAS);
        System.out.println("columna final fechas: " + COLUMNA_FINAL_FECHAS);

//        for (int i = 0; i < LISTADO_GRUPOS.size(); i++) {
//            System.out.println("Elemento " + i + "columna menor primer grupo " + columnaMenorFechas(LISTADO_GRUPOS.get(i)) + " grupo " + getPrograma(LISTADO_GRUPOS.get(i)));
//            System.out.println("Elemento " + i + "columna mayor primer grupo " + columnaMayorFechas(LISTADO_GRUPOS.get(i)) + " grupo " + getPrograma(LISTADO_GRUPOS.get(i)));
//        }
//
//        for (int j = 0; j < LISTADO_GRUPOS.size(); j++) {
//            crearHorarioGrupo("hojaDestino", j);
//        }
    }

    public Object getPrograma(Object[] fila /*LISTADO_GRUPOS*/) {
        Object programa = null;
        programa = fila[INDICE_FILA_PROGRAMA];
        return programa;
    }

    public Object getSemestre(Object[] fila /*LISTADO_GRUPOS*/) {
        Object semestre = null;
        semestre = fila[INDICE_FILA_SEMESTRE];
        return semestre;
    }

    public Object getJornada(Object[] fila /*LISTADO_GRUPOS*/) {
        Object jornada = null;
        jornada = fila[INDICE_FILA_JORNADA];
        return jornada;
    }

    public Object getDocente(Object[] fila) {
        Object docente = null;
        docente = fila[INDICE_FILA_DOCENTE];
        return docente;
    }

    public int getFilaInicialPrograma(Object[] fila /*LISTADO_GRUPOS*/) {
        int filaInicialPrograma = 0;
        filaInicialPrograma = (int) fila[INDICE_FILA_INICIAL_GRUPO];
        return filaInicialPrograma;
    }

    public int getFilaFinalPrograma(Object[] fila /*LISTADO_GRUPOS*/) {
        int filaFinalPrograma = 0;
        filaFinalPrograma = (int) fila[INDICE_FILA_FINAL_GRUPO];
        return filaFinalPrograma;
    }

    public int columnaMenorFechas(Object[] fila /*LISTADO_GRUPOS*/) {
        int cif = 0;
        //devuelve la columna menor de las fechas para un grupo dado
        int fip = getFilaInicialPrograma(fila);
        int ffp = getFilaFinalPrograma(fila);

//        System.out.println("fila inical programa: " + fip);
//        System.out.println("fila final programa: " + ffp);
        try {
//            FileInputStream iS = new FileInputStream(RUTA_ARCHIVO_EXCEL);
//            XSSFWorkbook workbook = new XSSFWorkbook(iS);

//            XSSFSheet hoja = workbook.getSheetAt(INDICE_HOJA_GRUPOS);
            XSSFRow filaLeer;
            ArrayList<Integer> arrayListIndiceFechas = new ArrayList<Integer>();

            for (int i = fip; i <= ffp; i++) {
                //vamos a recorrer toda la fila hasta obtener la primera fecha
                //obtenemos la fila
                filaLeer = hoja.getRow(i);
                //y ahora la vamos a recorrer por columnas
                for (int j = COLUMNA_INICIAL_FECHAS; j < COLUMNA_FINAL_FECHAS; j++) {

//                    System.out.println("indice columna fuera: " + j);
                    if (filaLeer.getCell(j) != null) {
                        if (filaLeer.getCell(j).getCellType() != 3) {
//                        System.out.println("indice columna dentro: "+j);
                            arrayListIndiceFechas.add(j);
                            break;
                        }
                    }

                    if (j == COLUMNA_FINAL_FECHAS) {

                    }
                }
            }

            System.out.println("Tamaño del array list: " + arrayListIndiceFechas.size());
            //si se obtiene algún índice
            if (arrayListIndiceFechas.size() > 0) {
                cif = Collections.min(arrayListIndiceFechas);
            }
            //de lo contrario la columna inicial será cero

            for (int i = 0; i < arrayListIndiceFechas.size(); i++) {
//                System.out.println("Elementos del arralistindice fechas" + i + " " + arrayListIndiceFechas.get(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cif;
    }

    public int columnaMayorFechas(Object[] fila /*LISTADO_GRUPOS*/) {
        int cif = 0;
        //devuelve la columna menor de las fechas para un grupo dado
        int fip = getFilaInicialPrograma(fila);
        int ffp = getFilaFinalPrograma(fila);

//        System.out.println("fila inical programa: " + fip);
//        System.out.println("fila final programa: " + ffp);
        try {
//            FileInputStream iS = new FileInputStream(RUTA_ARCHIVO_EXCEL);
//            XSSFWorkbook workbook = new XSSFWorkbook(iS);
//
//            XSSFSheet hoja = workbook.getSheetAt(INDICE_HOJA_GRUPOS);
            XSSFRow filaLeer;
            ArrayList<Integer> arrayListIndiceFechas = new ArrayList<Integer>();

            for (int i = fip; i <= ffp; i++) {
                //vamos a recorrer toda la fila hasta obtener la primera fecha
                //obtenemos la fila
                filaLeer = hoja.getRow(i);
                //y ahora la vamos a recorrer por columnas, pero desde la final hasta la inicial
                for (int j = COLUMNA_FINAL_FECHAS; j >= COLUMNA_INICIAL_FECHAS; j--) {

                    if (filaLeer.getCell(j) != null) {
                        if (filaLeer.getCell(j).getCellType() != 3) {
                            arrayListIndiceFechas.add(j);
                            break;
                        }
                    }

                }
            }

            //si se obtiene algún índice
            if (arrayListIndiceFechas.size() > 0) {
                cif = Collections.max(arrayListIndiceFechas);
            }

            //de lo contrario la columna inicial será cero
//            for (int i = 0; i < arrayListIndiceFechas.size(); i++) {
//                System.out.println("Elementos del arralistindice fehas" + i + " " + arrayListIndiceFechas.get(i));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cif;
    }

    public ArrayList<Object[]> listadoGrupos(String rutaArchivoExcel, int filaInicial) {
        ArrayList<Object[]> lG = new ArrayList<Object[]>();

        //cada fila del lG será un arreglo de objetos, que por el momento, tendrá programa, semestre, jornada, filainicialgrupo, filafinalgrupo
        Object[] filaLg = new Object[5];

        String programa = "";
        String semestre = "";
        String pensum = "";
        String jornada = "";
        String alfa = "";
        Object numerico = null;
        double creditos = 0;
        String asignatura = "";
        Object nrc = null;
        double cupos = 0;
        String docente = "";

        ArrayList<String> arregloProgramas = new ArrayList<String>();
        ArrayList<String> arregloSemestres = new ArrayList<String>();
        ArrayList<String> arregloPensums = new ArrayList<String>();
        ArrayList<String> arregloJornadas = new ArrayList<String>();
        ArrayList<String> arregloAlfas = new ArrayList<String>();
        ArrayList<Double> arregloNumericos = new ArrayList<Double>();
        ArrayList<Double> arregloCreditos = new ArrayList<Double>();
        ArrayList<String> arregloAsignaturas = new ArrayList<String>();
        ArrayList<String> arregloNrcs = new ArrayList<String>();
        ArrayList<Integer> arregloCupos = new ArrayList<Integer>();
        ArrayList<String> arregloDocentes = new ArrayList<String>();

        ArrayList<Integer> arregloFilaInicialPrograma = new ArrayList<Integer>();
        ArrayList<Integer> arregloFilaFinalPrograma = new ArrayList<Integer>();

        int contadorFilas = 0;
        int contadorProgramas = 0;
        int filaInicialPrograma = 0;
        int filaFinalPrograma = 0;

        try {
            FileInputStream iS = new FileInputStream(rutaArchivoExcel);
            XSSFWorkbook workbook = new XSSFWorkbook(iS);

            XSSFSheet hoja = workbook.getSheetAt(INDICE_HOJA_GRUPOS);
            XSSFRow fila, fila_1;
            fila = hoja.getRow(filaInicial);
            XSSFCell celdaPrograma,
                    celdaSemestre,
                    celdaPensum,
                    celdaJornada,
                    celdaAlfa,
                    celdaNumerico,
                    celdaCreditos,
                    celdaAsignatura,
                    celdaNrc,
                    celdaCupos,
                    celdaDocente,
                    celdaPrograma_1,
                    celdaSemestre_1,
                    celdaAsignatura_1,
                    celdaJornada_1;

            celdaPrograma = fila.getCell(0);
            celdaSemestre = fila.getCell(1);

            //realizamos conteo mientras las celdas estén llenas. colocar celda != null
            //hace referencia a una celda no vacia.
            //se comienza en 1 por el conteno de la fila anterior
            while (celdaPrograma != null && celdaPrograma.getStringCellValue() != "") {

                fila = hoja.getRow(filaInicial + contadorFilas);
                celdaPrograma = fila.getCell(0);
                celdaSemestre = fila.getCell(1);
                //el del incidice dos ees una columna que tiene grupo que no se considera relavante
                celdaPensum = fila.getCell(3);
                celdaJornada = fila.getCell(4);
                celdaAlfa = fila.getCell(5);
                celdaNumerico = fila.getCell(6);
                celdaCreditos = fila.getCell(7);
                celdaAsignatura = fila.getCell(8);
                celdaNrc = fila.getCell(9);
                celdaCupos = fila.getCell(10);
                celdaDocente = fila.getCell(11);

                if (!celdaVacia(fila, 0)) {
                    /*if (celdaPrograma != null
                        || celdaSemestre != null
                        || celdaPensum != null
                        || celdaJornada != null
                        || celdaAlfa != null
                        || celdaNumerico != null
                        || celdaCreditos != null
                        || celdaAsignatura != null) {*/

                    try {

                        programa = celdaPrograma.getStringCellValue();
                        semestre = celdaSemestre.getStringCellValue();
                        pensum = celdaPensum.getStringCellValue();
                        jornada = celdaJornada.getStringCellValue();
                        alfa = celdaAlfa.getStringCellValue();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (celdaNumerico.getCellType() == 0) {
                        numerico = (int) celdaNumerico.getNumericCellValue();
                    }
                    if (celdaNumerico.getCellType() == 1) {
                        numerico = (String) celdaNumerico.getStringCellValue();
                    }

                    creditos = celdaCreditos.getNumericCellValue();
                    asignatura = celdaAsignatura.getStringCellValue();

//                System.out.println("tipo de celda: "+celdaNrc.getCellType());
                    if (celdaNrc.getCellType() == 0) {
                        nrc = (int) celdaNrc.getNumericCellValue();
                    }
                    if (celdaNrc.getCellType() == 1) {
                        nrc = (String) celdaNrc.getStringCellValue();
                    }
//                nrc = celdaNrc.getcellNumericCellValue();
                    cupos = celdaCupos.getNumericCellValue();
                    docente = celdaDocente.getStringCellValue();

//                System.out.println("contadorfila: " + contadorFilas + " programa: " + programa + " semestre: " + semestre +"nrc: "+nrc+" asignatura: "+asignatura);
                    String programa_1, semestre_1, jornada_1;
                    fila_1 = hoja.getRow(filaInicial + contadorFilas - 1);
                    celdaPrograma_1 = fila_1.getCell(0);
                    celdaSemestre_1 = fila_1.getCell(1);

                    celdaAsignatura_1 = fila_1.getCell(8);
                    celdaJornada_1 = fila_1.getCell(4);

                    programa_1 = celdaPrograma_1.getStringCellValue();
                    semestre_1 = celdaSemestre_1.getStringCellValue();

                    jornada_1 = celdaJornada_1.getStringCellValue();

                    if (contadorFilas > 0) {

                        if (programa.equals("EGPR") && semestre.equals("I")) {
                            System.out.println("aca");
                        }

                        if (programa != programa_1 || semestre != semestre_1 || celdaVacia(hoja.getRow(filaInicial + contadorFilas + 1), 0)) {
                            String nombreArchivo = (contadorProgramas + 1) + " " + programa_1 + " " + semestre_1 + " " + celdaJornada_1;

                            //la primera fila inicial será 2
                            if (contadorProgramas == 0) {
                                filaInicialPrograma = 2;
                                filaFinalPrograma = contadorFilas + 2 - 1;
//                            } else if (hoja.getRow(filaInicial + contadorFilas + 1).getCell(0) == null) {
                            } else if (celdaVacia(hoja.getRow(filaInicial + contadorFilas + 1), 0)) {
                                filaInicialPrograma = filaFinalPrograma + 1;
                                filaFinalPrograma = 2 + contadorFilas + 1 - 1;
                            } else {
                                filaInicialPrograma = filaFinalPrograma + 1;
                                filaFinalPrograma = contadorFilas + 2 - 1;
                            }

//                        System.out.println("contador programas: " + contadorProgramas);
                            arregloProgramas.add(programa_1);
                            arregloSemestres.add(semestre_1);
                            arregloJornadas.add(jornada_1);
                            arregloFilaInicialPrograma.add(filaInicialPrograma);
                            arregloFilaFinalPrograma.add(filaFinalPrograma);

                            filaLg[0] = programa_1;
                            filaLg[1] = semestre_1;
                            filaLg[2] = jornada_1;
                            filaLg[3] = filaInicialPrograma;
                            filaLg[4] = filaFinalPrograma;
//                        crearPdf("pdfs/"+nombreArchivo+".pdf");

                            lG.add(filaLg);

                            filaLg = new Object[5];

//                        System.out.println((contadorProgramas + 1) + " programa: " + programa_1 + " semestre:" + semestre_1 + " asignatura: " + celdaAsignatura_1 + " filainicialprograma: " + filaInicialPrograma + " filafinalprograma: " + filaFinalPrograma);
                            contadorProgramas++;
                        }
                    }
                }
                //el valor obtenido al salir de este while menos 1, es el número de nrcs en total en la hoja de Excel
                contadorFilas++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println("Valor total obtenido de los grupos: " + contadorProgramas);
        return lG;
    }

    public ArrayList<Object[]> listadoDocentes(String rutaArchivoExcel, int filaInicial) {
        ArrayList<Object[]> lG = new ArrayList<Object[]>();

        //cada fila del lG será un arreglo de objetos, que por el momento, tendrá programa, semestre, jornada, filainicialgrupo, filafinalgrupo
        Object[] filaLg = new Object[6];

        String programa = "";
        String semestre = "";
        String pensum = "";
        String jornada = "";
        String alfa = "";
        Object numerico = null;
        double creditos = 0;
        String asignatura = "";
        Object nrc = null;
        double cupos = 0;
        String docente = "";

        ArrayList<String> arregloProgramas = new ArrayList<String>();
        ArrayList<String> arregloSemestres = new ArrayList<String>();
        ArrayList<String> arregloPensums = new ArrayList<String>();
        ArrayList<String> arregloJornadas = new ArrayList<String>();
        ArrayList<String> arregloAlfas = new ArrayList<String>();
        ArrayList<Double> arregloNumericos = new ArrayList<Double>();
        ArrayList<Double> arregloCreditos = new ArrayList<Double>();
        ArrayList<String> arregloAsignaturas = new ArrayList<String>();
        ArrayList<String> arregloNrcs = new ArrayList<String>();
        ArrayList<Integer> arregloCupos = new ArrayList<Integer>();
        ArrayList<String> arregloDocentes = new ArrayList<String>();

        ArrayList<Integer> arregloFilaInicialPrograma = new ArrayList<Integer>();
        ArrayList<Integer> arregloFilaFinalPrograma = new ArrayList<Integer>();

        int contadorFilas = 0;
        int contadorDocentes = 0;
        int filaInicialPrograma = 0;
        int filaFinalPrograma = 0;

        try {
            FileInputStream iS = new FileInputStream(rutaArchivoExcel);
            XSSFWorkbook workbook = new XSSFWorkbook(iS);

            XSSFSheet hoja = workbook.getSheetAt(INDICE_HOJA_GRUPOS);
            XSSFRow fila, fila_1;
            fila = hoja.getRow(filaInicial);
            XSSFCell celdaPrograma,
                    celdaSemestre,
                    celdaPensum,
                    celdaJornada,
                    celdaAlfa,
                    celdaNumerico,
                    celdaCreditos,
                    celdaAsignatura,
                    celdaNrc,
                    celdaCupos,
                    celdaDocente,
                    celdaPrograma_1,
                    celdaSemestre_1,
                    celdaAsignatura_1,
                    celdaJornada_1,
                    celdaDocente_1;

            celdaPrograma = fila.getCell(0);
            celdaSemestre = fila.getCell(1);

            //realizamos conteo mientras las celdas estén llenas. colocar celda != null
            //hace referencia a una celda no vacia.
            //se comienza en 1 por el conteno de la fila anterior
            while (celdaPrograma != null && celdaPrograma.getStringCellValue() != "") {

                fila = hoja.getRow(filaInicial + contadorFilas);
                celdaPrograma = fila.getCell(0);
                celdaSemestre = fila.getCell(1);
                //el del incidice dos ees una columna que tiene grupo que no se considera relavante
                celdaPensum = fila.getCell(3);
                celdaJornada = fila.getCell(4);
                celdaAlfa = fila.getCell(5);
                celdaNumerico = fila.getCell(6);
                celdaCreditos = fila.getCell(7);
                celdaAsignatura = fila.getCell(8);
                celdaNrc = fila.getCell(9);
                celdaCupos = fila.getCell(10);
                celdaDocente = fila.getCell(11);

                if (!celdaVacia(fila, 0)) {
                    /*if (celdaPrograma != null
                        || celdaSemestre != null
                        || celdaPensum != null
                        || celdaJornada != null
                        || celdaAlfa != null
                        || celdaNumerico != null
                        || celdaCreditos != null
                        || celdaAsignatura != null) {*/
                    programa = celdaPrograma.getStringCellValue();
                    semestre = celdaSemestre.getStringCellValue();
                    pensum = celdaPensum.getStringCellValue();
                    jornada = celdaJornada.getStringCellValue();
                    alfa = celdaAlfa.getStringCellValue();
                    docente = celdaDocente.getStringCellValue();

                    if (celdaNumerico.getCellType() == 0) {
                        numerico = (int) celdaNumerico.getNumericCellValue();
                    }
                    if (celdaNumerico.getCellType() == 1) {
                        numerico = (String) celdaNumerico.getStringCellValue();
                    }

                    creditos = celdaCreditos.getNumericCellValue();
                    asignatura = celdaAsignatura.getStringCellValue();

//                System.out.println("tipo de celda: "+celdaNrc.getCellType());
                    if (celdaNrc.getCellType() == 0) {
                        nrc = (int) celdaNrc.getNumericCellValue();
                    }
                    if (celdaNrc.getCellType() == 1) {
                        nrc = (String) celdaNrc.getStringCellValue();
                    }
//                nrc = celdaNrc.getcellNumericCellValue();
                    cupos = celdaCupos.getNumericCellValue();
                    docente = celdaDocente.getStringCellValue();

//                System.out.println("contadorfila: " + contadorFilas + " programa: " + programa + " semestre: " + semestre +"nrc: "+nrc+" asignatura: "+asignatura);
                    String programa_1, semestre_1, jornada_1, docente_1;
                    fila_1 = hoja.getRow(filaInicial + contadorFilas - 1);
                    celdaPrograma_1 = fila_1.getCell(0);
                    celdaSemestre_1 = fila_1.getCell(1);

                    celdaAsignatura_1 = fila_1.getCell(8);
                    celdaJornada_1 = fila_1.getCell(4);
                    celdaDocente_1 = fila_1.getCell(11);

                    programa_1 = celdaPrograma_1.getStringCellValue();
                    semestre_1 = celdaSemestre_1.getStringCellValue();

                    jornada_1 = celdaJornada_1.getStringCellValue();

                    docente_1 = celdaDocente_1.getStringCellValue();

                    if (contadorFilas > 0) {
                        if (!docente.trim().equals(docente_1.trim())) {
                            String nombreArchivo = (contadorDocentes + 1) + " " + docente_1;

                            //la primera fila inicial será 2
                            if (contadorDocentes == 0) {
                                filaInicialPrograma = 2;
                                filaFinalPrograma = contadorFilas + 2 - 1;
                            } else if (hoja.getRow(filaInicial + contadorFilas + 1).getCell(0) == null) {
                                filaInicialPrograma = filaFinalPrograma + 1;
                                filaFinalPrograma = 2 + contadorFilas + 1 - 1;
                            } else {
                                filaInicialPrograma = filaFinalPrograma + 1;
                                filaFinalPrograma = contadorFilas + 2 - 1;
                            }

//                        System.out.println("contador programas: " + contadorProgramas);
                            arregloProgramas.add(programa_1);
                            arregloSemestres.add(semestre_1);
                            arregloJornadas.add(jornada_1);
                            arregloDocentes.add(docente_1);
                            arregloFilaInicialPrograma.add(filaInicialPrograma);
                            arregloFilaFinalPrograma.add(filaFinalPrograma);

                            filaLg[0] = programa_1;
                            filaLg[1] = semestre_1;
                            filaLg[2] = jornada_1;
                            filaLg[3] = filaInicialPrograma;
                            filaLg[4] = filaFinalPrograma;
                            filaLg[5] = docente_1;
//                        crearPdf("pdfs/"+nombreArchivo+".pdf");

                            lG.add(filaLg);

                            filaLg = new Object[6];

//                        System.out.println((contadorProgramas + 1) + " programa: " + programa_1 + " semestre:" + semestre_1 + " asignatura: " + celdaAsignatura_1 + " filainicialprograma: " + filaInicialPrograma + " filafinalprograma: " + filaFinalPrograma);
                            contadorDocentes++;
                        }
                    }
                }
                //el valor obtenido al salir de este while menos 1, es el número de nrcs en total en la hoja de Excel
                contadorFilas++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println("Valor total obtenido de los grupos: " + contadorProgramas);
        return lG;
    }

    public void crearPdf(String nombreArchivo) throws FileNotFoundException {

//        File file = new File(nombreArchivo);
//        file.getParentFile().mkdirs();
//
//        //Initialize PDF writer
//        PdfWriter writer = new PdfWriter(nombreArchivo);
//
//        //Initialize PDF document
//        PdfDocument pdf = new PdfDocument(writer);
//
//        // Initialize document
//        Document document = new Document(pdf, PageSize.A4.rotate());
//        document.add(new Paragraph("Hola mundo"));
//
//        //Close document
//        document.close();
    }

    public int numeroCabeceras() throws IOException {
        int nC;
        nC = 0;

//        iS = new FileInputStream(RUTA_ARCHIVO_EXCEL);
//        workbook = new XSSFWorkbook(iS);
//        System.out.println("ruta del archivo " + RUTA_ARCHIVO_EXCEL);
//        hoja = workbook.getSheetAt(INDICE_HOJA_GRUPOS);
        XSSFRow fila = hoja.getRow(0);

        int contadorColumnas = COLUMNA_INICIAL_FECHAS;
        while (!celdaVacia(fila, contadorColumnas)) {
//            System.out.println("celda " + contadorColumnas + " " + fila.getCell(contadorColumnas).getStringCellValue());
            contadorColumnas++;
        }

        nC = contadorColumnas - 1;

//        System.out.println("numero cabeceras: " + nC);
        return nC;
    }

    public int numeroFechas() throws IOException {
        return numeroCabeceras() - NUMERO_ENCABEZADOS_ANTES_FECHA;
    }

    public void indiceFinalFechas() throws IOException {
        COLUMNA_FINAL_FECHAS = COLUMNA_INICIAL_FECHAS + numeroFechas();
        System.out.println("numero de fechas" + numeroFechas() + "columna final fechas " + COLUMNA_FINAL_FECHAS);
    }

    public void crearHorarioGrupo(String rutaSalida, String nombreArchivo, String nombreHojaDestino, int numeroGrupo) {
        XSSFWorkbook libroDestino = new XSSFWorkbook();
        XSSFSheet hojaDestino = libroDestino.createSheet(nombreHojaDestino);

        //creamos los encabezados
        XSSFRow filaEncabezadosSinFechas = hojaDestino.createRow(0);

        //arreglo de encabezados
        for (int i = 0; i < TOTAL_ENCABEZADOS_SIN_FECHAS; i++) {

            try {
                XSSFCell celdaEncabezado = filaEncabezadosSinFechas.createCell(i);
                String encabezadoOrigen = hoja.getRow(0).getCell(i).getStringCellValue();
                celdaEncabezado.setCellValue(encabezadoOrigen);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //vamos a pasar las materias y datos del primer grupo
        //a continuación el valor de i hará el conteo sobre las columnas
        //el valor de j hará el conteo sobre las filas
        int contadorFilasDestino = 0;
        int indiceFilaInicialGrupo = getFilaInicialPrograma(LISTADO_GRUPOS.get(numeroGrupo));
        int indiceFilaFinalGrupo = getFilaFinalPrograma(LISTADO_GRUPOS.get(numeroGrupo));

        XSSFRow filaDestino;
        for (int i = 0; i <= indiceFilaFinalGrupo - indiceFilaInicialGrupo; i++) {
            //se bajan dos filas pues se necesitan poner en adelante los encabezados
            //de los días y las fechas
            filaDestino = hojaDestino.createRow(i + 2);

            //reformular estos dos for, primero se hace el conteo sobre las filas y
            //luego sobre las columnas
            for (int j = 0; j < TOTAL_ENCABEZADOS_SIN_FECHAS; j++) {

                XSSFCell celdaDestino = filaDestino.createCell(j);

                Object valorOrigen = new Object();

                if (hoja.getRow(indiceFilaInicialGrupo + i).getCell(j).getCellType() == 0) {
                    valorOrigen = (int) hoja.getRow(indiceFilaInicialGrupo + i).getCell(j).getNumericCellValue();
                    celdaDestino.setCellValue((int) valorOrigen);

                    //copiamos el estilo y lo pegamos
                    CellStyle clonarEstilo = libroDestino.createCellStyle();
                    clonarEstilo.cloneStyleFrom(hoja.getRow(indiceFilaInicialGrupo + i).getCell(j).getCellStyle());

                    celdaDestino.setCellStyle(clonarEstilo);
                }
                if (hoja.getRow(indiceFilaInicialGrupo + i).getCell(j).getCellType() == 1) {
                    valorOrigen = (String) hoja.getRow(indiceFilaInicialGrupo + i).getCell(j).getStringCellValue();
                    celdaDestino.setCellValue((String) valorOrigen);

                    //copiamos el estilo y lo pegamos
                    CellStyle clonarEstilo = libroDestino.createCellStyle();
                    clonarEstilo.cloneStyleFrom(hoja.getRow(indiceFilaInicialGrupo + i).getCell(j).getCellStyle());

                    celdaDestino.setCellStyle(clonarEstilo);
                }
                hojaDestino.autoSizeColumn(j);
                contadorFilasDestino++;
            }

        }

        XSSFRow filaDestinoDias = hojaDestino.getRow(0);
        XSSFRow filaDestinoFechas = hojaDestino.createRow(1);
        int columnaInicialFechasGrupo = columnaMenorFechas(LISTADO_GRUPOS.get(numeroGrupo));
        int columnaFinalFechasGrupo = columnaMayorFechas(LISTADO_GRUPOS.get(numeroGrupo));

        CellStyle cellStyle = libroDestino.createCellStyle();
        CreationHelper createHelper = libroDestino.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("d - MMM"));
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

        CellStyle cellStyleT = libroDestino.createCellStyle();
        cellStyleT.setDataFormat(createHelper.createDataFormat().getFormat(" hh:mm"));
        cellStyleT.setBorderTop(BorderStyle.THIN);
        cellStyleT.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleT.setBorderBottom(BorderStyle.THIN);
        cellStyleT.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleT.setBorderLeft(BorderStyle.THIN);
        cellStyleT.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleT.setBorderRight(BorderStyle.THIN);
        cellStyleT.setRightBorderColor(IndexedColors.BLACK.getIndex());

        int contadorColumnasDestino = 0;
        for (int i = columnaInicialFechasGrupo; i <= columnaFinalFechasGrupo; i++) {

            if (i != 0) {
                String valor = hoja.getRow(0).getCell(i).getStringCellValue();

//            System.out.println("valor: " + valor + " " + " COLUMNA_INICIAL_FECHAS " + COLUMNA_INICIAL_FECHAS + " columna_final_fechas " + COLUMNA_FINAL_FECHAS);
                XSSFCell celdaDestino = filaDestinoDias.createCell(contadorColumnasDestino + COLUMNA_INICIAL_FECHAS);
                celdaDestino.setCellValue((String) valor);

                Date valorNumerico = hoja.getRow(1).getCell(i).getDateCellValue();

//            System.out.println("valor: " + valorNumerico + " " + " COLUMNA_INICIAL_FECHAS " + COLUMNA_INICIAL_FECHAS + " columna_final_fechas " + COLUMNA_FINAL_FECHAS);
                XSSFCell celdaDestinoFechas = filaDestinoFechas.createCell(contadorColumnasDestino + COLUMNA_INICIAL_FECHAS);
                celdaDestinoFechas.setCellValue(valorNumerico);

                celdaDestinoFechas.setCellStyle(cellStyle);

                //podemos aprovechar el for acá presente para realizar el conteo de las columnas
                //el j hará recorreido entre las filas
                Object valorHora = new Object();
                contadorFilasDestino = 0;
                for (int j = indiceFilaInicialGrupo; j <= indiceFilaFinalGrupo; j++) {
                    XSSFRow filaDestinoHoras = hojaDestino.getRow(contadorFilasDestino + 2);

                    Object o1 = hoja.getRow(j);
                    Object o2 = hoja.getRow(j).getCell(i);
                    if (o1 == null || o2 == null) {
                        System.out.println("Nulo");
                    }

                    if (o2 == null) {
                        valorHora = "";
                        XSSFCell celdaDestinoHoras = filaDestinoHoras.createCell(contadorColumnasDestino + COLUMNA_INICIAL_FECHAS);
                        celdaDestinoHoras.setCellValue((String) valorHora);
                        celdaDestinoHoras.setCellStyle(cellStyleT);
                    }
                    if (o2 != null) {
                        if (hoja.getRow(j).getCell(i).getCellType() == 0) {
                            valorHora = hoja.getRow(j).getCell(i).getNumericCellValue();
                            XSSFCell celdaDestinoHoras = filaDestinoHoras.createCell(contadorColumnasDestino + COLUMNA_INICIAL_FECHAS);
                            celdaDestinoHoras.setCellValue((double) valorHora);
                            celdaDestinoHoras.setCellStyle(cellStyleT);

                        }
                        if (hoja.getRow(j).getCell(i).getCellType() == 1) {
                            valorHora = hoja.getRow(j).getCell(i).getStringCellValue();
                            XSSFCell celdaDestinoHoras = filaDestinoHoras.createCell(contadorColumnasDestino + COLUMNA_INICIAL_FECHAS);
                            celdaDestinoHoras.setCellValue((String) valorHora);
                            celdaDestinoHoras.setCellStyle(cellStyleT);
                        }
                    }

                    contadorFilasDestino++;
                }
                contadorColumnasDestino++;

            }

        }

        CellStyle estiloCeldaBordeada = libroDestino.createCellStyle();
        estiloCeldaBordeada.setBorderTop(BorderStyle.THIN);
        estiloCeldaBordeada.setTopBorderColor(IndexedColors.BLACK.getIndex());
        estiloCeldaBordeada.setBorderBottom(BorderStyle.THIN);
        estiloCeldaBordeada.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        estiloCeldaBordeada.setBorderLeft(BorderStyle.THIN);
        estiloCeldaBordeada.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        estiloCeldaBordeada.setBorderRight(BorderStyle.THIN);
        estiloCeldaBordeada.setRightBorderColor(IndexedColors.BLACK.getIndex());

        //el 2 que está acá corresponde al indice de la fila donde comienzan los grupos
        for (int i = 0; i < contadorFilasDestino + 2; i++) {
            for (int j = 0; j < contadorColumnasDestino + NUMERO_ENCABEZADOS_ANTES_FECHA; j++) {
                //no se le puede agregar el estilo de celda bordeada si tiene hora o fecha, pues
                //le quitará el formato de hora y fecha, en este primer if, se excluyen dichas zonas
                if ((i != 1 || j < COLUMNA_INICIAL_FECHAS) && (i < 2 || j < COLUMNA_INICIAL_FECHAS)) {
                    if (hojaDestino.getRow(i).getCell(j) == null) {
                        hojaDestino.getRow(i).createCell(j).setCellStyle(estiloCeldaBordeada);
                    } else {
                        //hasy que descomentar este estilo para colocar las celdas que están en color y dejarlas en blanco
//                        hojaDestino.getRow(i).getCell(j).setCellStyle(estiloCeldaBordeada);
                    }
                }
                //acá se incluye la zona de las horas, si la celda es nula, es decir, sin hora
                //entonces se le coloca el formato de celda bordeada
                if (i >= 2 && j >= COLUMNA_INICIAL_FECHAS) {
                    if (hojaDestino.getRow(i).getCell(j) == null) {
//                        if (hojaDestino.getRow(i).getCell(j).getCellType() == 3) {
                        hojaDestino.getRow(i).createCell(j).setCellStyle(estiloCeldaBordeada);
//                            hojaDestino.getRow(i).getCell(j).setCellStyle(estiloCeldaBordeada);
//                        }
                    }
                }
            }
        }

//        deleteColumn(hojaDestino, 0);
        //hacemos el recorrido ahora por todas las celdas en donde están las fechas
        for (int j = COLUMNA_INICIAL_FECHAS; j < COLUMNA_INICIAL_FECHAS + contadorColumnasDestino; j++) {
            if (columnaCeldasVacias(hojaDestino, j, 2, 2 + contadorFilasDestino)) {
                hojaDestino.setColumnHidden(j, true);
            };
        }
        hojaDestino.setColumnHidden(2, true);
        hojaDestino.setColumnHidden(3, true);
        //hojaDestino.setColumnHidden(9, true);
        hojaDestino.setColumnHidden(10, true);
        hojaDestino.setColumnHidden(11, true);
        hojaDestino.setColumnHidden(12, true);

        limpiarColumnas(hojaDestino);

        hojaDestino.createFreezePane(NUMERO_ENCABEZADOS_ANTES_FECHA, 2);

        hojaDestino.protectSheet("abcd1234");

        //a continuación vamos a cargar las horas que corresponden a las fechas programadas
        try {
            //creamos el archivo
            File archivoSalida = new File(rutaSalida + "/" + nombreArchivo + ".xlsx");
            FileOutputStream file = new FileOutputStream(archivoSalida);
            libroDestino.write(file);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void crearHorarioDocentes(String rutaSalida, String nombreArchivo, String nombreHojaDestino, int numeroGrupo) {

        XSSFWorkbook libroDestino = new XSSFWorkbook();
        XSSFSheet hojaDestino = libroDestino.createSheet(nombreHojaDestino);

        //creamos los encabezados
        XSSFRow filaEncabezadosSinFechas = hojaDestino.createRow(0);

        //arreglo de encabezados
        for (int i = 0; i < TOTAL_ENCABEZADOS_SIN_FECHAS; i++) {

            try {
                XSSFCell celdaEncabezado = filaEncabezadosSinFechas.createCell(i);
                String encabezadoOrigen = hoja.getRow(1).getCell(i).getStringCellValue();
                celdaEncabezado.setCellValue(encabezadoOrigen);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //vamos a pasar las materias y datos del primer grupo
        //a continuación el valor de i hará el conteo sobre las columnas
        //el valor de j hará el conteo sobre las filas
        //indiceFilaInicialGrupo es realmente docente, pero se dejará grupo por facilidad
        int contadorFilasDestino = 0;
        int indiceFilaInicialGrupo = getFilaInicialPrograma(LISTADO_DOCENTES.get(numeroGrupo));
        int indiceFilaFinalGrupo = getFilaFinalPrograma(LISTADO_DOCENTES.get(numeroGrupo));

        XSSFRow filaDestino;
        for (int i = 0; i <= indiceFilaFinalGrupo - indiceFilaInicialGrupo; i++) {
            //se bajan dos filas pues se necesitan poner en adelante los encabezados
            //de los días y las fechas
            filaDestino = hojaDestino.createRow(i + 2);

            //reformular estos dos for, primero se hace el conteo sobre las filas y
            //luego sobre las columnas
            for (int j = 0; j < TOTAL_ENCABEZADOS_SIN_FECHAS; j++) {

                XSSFCell celdaDestino = filaDestino.createCell(j);

                Object valorOrigen = new Object();

                if (hoja.getRow(indiceFilaInicialGrupo + i).getCell(j).getCellType() == 0) {
                    valorOrigen = (int) hoja.getRow(indiceFilaInicialGrupo + i).getCell(j).getNumericCellValue();
                    celdaDestino.setCellValue((int) valorOrigen);

                    //copiamos el estilo y lo pegamos
                    CellStyle clonarEstilo = libroDestino.createCellStyle();
                    clonarEstilo.cloneStyleFrom(hoja.getRow(indiceFilaInicialGrupo + i).getCell(j).getCellStyle());

                    celdaDestino.setCellStyle(clonarEstilo);
                }
                if (hoja.getRow(indiceFilaInicialGrupo + i).getCell(j).getCellType() == 1) {
                    valorOrigen = (String) hoja.getRow(indiceFilaInicialGrupo + i).getCell(j).getStringCellValue();
                    celdaDestino.setCellValue((String) valorOrigen);

                    //copiamos el estilo y lo pegamos
                    CellStyle clonarEstilo = libroDestino.createCellStyle();
                    clonarEstilo.cloneStyleFrom(hoja.getRow(indiceFilaInicialGrupo + i).getCell(j).getCellStyle());

                    celdaDestino.setCellStyle(clonarEstilo);
                }
                hojaDestino.autoSizeColumn(j);
                contadorFilasDestino++;
            }

        }

        XSSFRow filaDestinoDias = hojaDestino.getRow(0);
        XSSFRow filaDestinoFechas = hojaDestino.createRow(1);
        int columnaInicialFechasGrupo = columnaMenorFechas(LISTADO_DOCENTES.get(numeroGrupo));
        int columnaFinalFechasGrupo = columnaMayorFechas(LISTADO_DOCENTES.get(numeroGrupo));

        CellStyle cellStyle = libroDestino.createCellStyle();
        CreationHelper createHelper = libroDestino.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("d - MMM"));
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

        CellStyle cellStyleT = libroDestino.createCellStyle();
        cellStyleT.setDataFormat(createHelper.createDataFormat().getFormat(" hh:mm"));
        cellStyleT.setBorderTop(BorderStyle.THIN);
        cellStyleT.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleT.setBorderBottom(BorderStyle.THIN);
        cellStyleT.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleT.setBorderLeft(BorderStyle.THIN);
        cellStyleT.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleT.setBorderRight(BorderStyle.THIN);
        cellStyleT.setRightBorderColor(IndexedColors.BLACK.getIndex());

        int contadorColumnasDestino = 0;
        for (int i = columnaInicialFechasGrupo; i <= columnaFinalFechasGrupo; i++) {
            String valor = hoja.getRow(0).getCell(i).getStringCellValue();

//            System.out.println("valor: " + valor + " " + " COLUMNA_INICIAL_FECHAS " + COLUMNA_INICIAL_FECHAS + " columna_final_fechas " + COLUMNA_FINAL_FECHAS);
            XSSFCell celdaDestino = filaDestinoDias.createCell(contadorColumnasDestino + COLUMNA_INICIAL_FECHAS);
            celdaDestino.setCellValue((String) valor);

            Date valorNumerico = hoja.getRow(1).getCell(i).getDateCellValue();

//            System.out.println("valor: " + valorNumerico + " " + " COLUMNA_INICIAL_FECHAS " + COLUMNA_INICIAL_FECHAS + " columna_final_fechas " + COLUMNA_FINAL_FECHAS);
            XSSFCell celdaDestinoFechas = filaDestinoFechas.createCell(contadorColumnasDestino + COLUMNA_INICIAL_FECHAS);
            celdaDestinoFechas.setCellValue(valorNumerico);

            celdaDestinoFechas.setCellStyle(cellStyle);

            //podemos aprovechar el for acá presente para realizar el conteo de las columnas
            //el j hará recorreido entre las filas
            Object valorHora = new Object();
            contadorFilasDestino = 0;
            for (int j = indiceFilaInicialGrupo; j <= indiceFilaFinalGrupo; j++) {
                XSSFRow filaDestinoHoras = hojaDestino.getRow(contadorFilasDestino + 2);

                Object o1 = hoja.getRow(j);
                Object o2 = hoja.getRow(j).getCell(i);
                if (o1 == null || o2 == null) {
                    System.out.println("Nulo");
                }

                if (o2 == null) {
                    valorHora = "";
                    XSSFCell celdaDestinoHoras = filaDestinoHoras.createCell(contadorColumnasDestino + COLUMNA_INICIAL_FECHAS);
                    celdaDestinoHoras.setCellValue((String) valorHora);
                    celdaDestinoHoras.setCellStyle(cellStyleT);
                }
                if (o2 != null) {
                    if (hoja.getRow(j).getCell(i).getCellType() == 0) {
                        valorHora = hoja.getRow(j).getCell(i).getNumericCellValue();
                        XSSFCell celdaDestinoHoras = filaDestinoHoras.createCell(contadorColumnasDestino + COLUMNA_INICIAL_FECHAS);
                        celdaDestinoHoras.setCellValue((double) valorHora);
                        celdaDestinoHoras.setCellStyle(cellStyleT);

                    }
                    if (hoja.getRow(j).getCell(i).getCellType() == 1) {
                        valorHora = hoja.getRow(j).getCell(i).getStringCellValue();
                        XSSFCell celdaDestinoHoras = filaDestinoHoras.createCell(contadorColumnasDestino + COLUMNA_INICIAL_FECHAS);
                        celdaDestinoHoras.setCellValue((String) valorHora);
                        celdaDestinoHoras.setCellStyle(cellStyleT);
                    }
                }

                contadorFilasDestino++;
            }
            contadorColumnasDestino++;
        }

        CellStyle estiloCeldaBordeada = libroDestino.createCellStyle();
        estiloCeldaBordeada.setBorderTop(BorderStyle.THIN);
        estiloCeldaBordeada.setTopBorderColor(IndexedColors.BLACK.getIndex());
        estiloCeldaBordeada.setBorderBottom(BorderStyle.THIN);
        estiloCeldaBordeada.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        estiloCeldaBordeada.setBorderLeft(BorderStyle.THIN);
        estiloCeldaBordeada.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        estiloCeldaBordeada.setBorderRight(BorderStyle.THIN);
        estiloCeldaBordeada.setRightBorderColor(IndexedColors.BLACK.getIndex());

        //el 2 que está acá corresponde al indice de la fila donde comienzan los grupos
        for (int i = 0; i < contadorFilasDestino + 2; i++) {
            for (int j = 0; j < contadorColumnasDestino + NUMERO_ENCABEZADOS_ANTES_FECHA; j++) {
                //no se le puede agregar el estilo de celda bordeada si tiene hora o fecha, pues
                //le quitará el formato de hora y fecha, en este primer if, se excluyen dichas zonas
                if ((i != 1 || j < COLUMNA_INICIAL_FECHAS) && (i < 2 || j < COLUMNA_INICIAL_FECHAS)) {
                    if (hojaDestino.getRow(i).getCell(j) == null) {
                        hojaDestino.getRow(i).createCell(j).setCellStyle(estiloCeldaBordeada);
                    } else {
                        //hasy que descomentar este estilo para colocar las celdas que están en color y dejarlas en blanco
//                        hojaDestino.getRow(i).getCell(j).setCellStyle(estiloCeldaBordeada);
                    }
                }
                //acá se incluye la zona de las horas, si la celda es nula, es decir, sin hora
                //entonces se le coloca el formato de celda bordeada
                if (i >= 2 && j >= COLUMNA_INICIAL_FECHAS) {
                    if (hojaDestino.getRow(i).getCell(j) == null) {
//                        if (hojaDestino.getRow(i).getCell(j).getCellType() == 3) {
                        hojaDestino.getRow(i).createCell(j).setCellStyle(estiloCeldaBordeada);
//                            hojaDestino.getRow(i).getCell(j).setCellStyle(estiloCeldaBordeada);
//                        }
                    }
                }
            }
        }

//        deleteColumn(hojaDestino, 0);
        //hacemos el recorrido ahora por todas las celdas en donde están las fechas
        for (int j = COLUMNA_INICIAL_FECHAS; j < COLUMNA_INICIAL_FECHAS + contadorColumnasDestino; j++) {
            if (columnaCeldasVacias(hojaDestino, j, 2, 2 + contadorFilasDestino)) {
                hojaDestino.setColumnHidden(j, true);
            };
        }
        hojaDestino.setColumnHidden(2, true);
        hojaDestino.setColumnHidden(3, true);
        hojaDestino.setColumnHidden(5, true);
        hojaDestino.setColumnHidden(6, true);
        //hojaDestino.setColumnHidden(9, true);
        hojaDestino.setColumnHidden(10, true);
//        hojaDestino.setColumnHidden(11, true);
//        hojaDestino.setColumnHidden(12, true);

        limpiarColumnas(hojaDestino);

        hojaDestino.createFreezePane(NUMERO_ENCABEZADOS_ANTES_FECHA, 2);

//        hojaDestino.protectSheet("abcd1234");
        //a continuación vamos a cargar las horas que corresponden a las fechas programadas
        try {
            //creamos el archivo
            File archivoSalida = new File(rutaSalida + "/" + nombreArchivo + ".xlsx");
            FileOutputStream file = new FileOutputStream(archivoSalida);
            libroDestino.write(file);
            
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static CellStyle createBorderedStyle(XSSFWorkbook wb) {
        BorderStyle thin = BorderStyle.THIN;
        short black = IndexedColors.BLACK.getIndex();

        CellStyle style = wb.createCellStyle();
        style.setBorderRight(thin);
        style.setRightBorderColor(black);
        style.setBorderBottom(thin);
        style.setBottomBorderColor(black);
        style.setBorderLeft(thin);
        style.setLeftBorderColor(black);
        style.setBorderTop(thin);
        style.setTopBorderColor(black);
        return style;
    }

    public boolean columnaCeldasVacias(XSSFSheet hoja, int columna, int filaInicial, int filaFinal) {
        boolean vacia = true;

        for (int i = filaInicial; i < filaFinal; i++) {
            try {
//                if (hoja.getRow(i).getCell(columna).getCellType() != 3) {
                if (!celdaVacia(hoja.getRow(i), columna)) {
                    vacia = false;
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return vacia;
    }

    /**
     * Given a sheet, this method deletes a column from a sheet and moves all
     * the columns to the right of it to the left one cell.
     *
     * Note, this method will not update any formula references.
     *
     * @param sheet
     * @param column
     */
    public static void deleteColumn(XSSFSheet sheet, int columnToDelete) {
        int maxColumn = 0;
        for (int r = 0; r < sheet.getLastRowNum() + 1; r++) {
            XSSFRow row = sheet.getRow(r);

            // if no row exists here; then nothing to do; next!
            if (row == null) {
                continue;
            }

            // if the row doesn't have this many columns then we are good; next!
            int lastColumn = row.getLastCellNum();
            if (lastColumn > maxColumn) {
                maxColumn = lastColumn;
            }

            if (lastColumn < columnToDelete) {
                continue;
            }

            for (int x = columnToDelete + 1; x < lastColumn + 1; x++) {
                XSSFCell oldCell = row.getCell(x - 1);
                if (oldCell != null) {
                    row.removeCell(oldCell);
                }

                XSSFCell nextCell = row.getCell(x);
                if (nextCell != null) {
                    XSSFCell newCell = row.createCell(x - 1, nextCell.getCellType());
                    cloneCell(newCell, nextCell);
                }
            }
        }

        // Adjust the column widths
        for (int c = 0; c < maxColumn; c++) {
            sheet.setColumnWidth(c, sheet.getColumnWidth(c + 1));
        }
    }


    /*
     * Takes an existing Cell and merges all the styles and forumla
     * into the new one
     */
    private static void cloneCell(XSSFCell cNew, XSSFCell cOld) {
        cNew.setCellComment(cOld.getCellComment());
        cNew.setCellStyle(cOld.getCellStyle());

        switch (cNew.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN: {
                cNew.setCellValue(cOld.getBooleanCellValue());
                break;
            }
            case Cell.CELL_TYPE_NUMERIC: {
                cNew.setCellValue(cOld.getNumericCellValue());
                break;
            }
            case Cell.CELL_TYPE_STRING: {
                cNew.setCellValue(cOld.getStringCellValue());
                break;
            }
            case Cell.CELL_TYPE_ERROR: {
                cNew.setCellValue(cOld.getErrorCellValue());
                break;
            }
            case Cell.CELL_TYPE_FORMULA: {
                cNew.setCellFormula(cOld.getCellFormula());
                break;
            }
        }

    }

    public boolean celdaVacia(XSSFRow fila, int indiceCelda) {
        boolean cv = false;

        if (fila == null) {
            return true;
        }

        XSSFCell celda = fila.getCell(indiceCelda);

        if (celda == null) {
            cv = true;
        }
        if (celda != null) {
            int tipoCelda = celda.getCellType();
            if (tipoCelda == 0) {
                cv = false;
            }
            if (tipoCelda == 1) {
                if (celda.getStringCellValue().trim().equals("")) {
                    cv = true;
                } else {
                    cv = false;
                }
            }
            if (tipoCelda == 3) {
                cv = true;
            }
        }

        return cv;
    }

    /**
     * Esta función limpia las columnas que están en blanco
     */
    public void limpiarColumnas(XSSFSheet hojaDestino) {

        XSSFRow filaDias = hojaDestino.getRow(0);

        int contadorColumnas = 0;

        while (!celdaVacia(filaDias, INDICE_INICIAN_FECHAS_DESTINO + contadorColumnas)) {
            int contadorFilas = 0;
            XSSFRow fila = hojaDestino.getRow(INDICE_FILA_INICIAN_ASIGNATURAS_GRUPO_EN_SU_HORARIO);

            while (!celdaVacia(fila, 0)) {

                if (!celdaVacia(fila, INDICE_INICIAN_FECHAS_DESTINO + contadorColumnas)) {
                    break;
                }

                contadorFilas++;
                fila = hojaDestino.getRow(INDICE_FILA_INICIAN_ASIGNATURAS_GRUPO_EN_SU_HORARIO + contadorFilas);
            }

            if (celdaVacia(fila, 0)) {
                hojaDestino.setColumnHidden(INDICE_INICIAN_FECHAS_DESTINO + contadorColumnas, true);
            }

            contadorColumnas++;
        }

    }

}
