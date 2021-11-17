/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import javafx.scene.paint.Color;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Este archivo sirve para crear los seguimientos. Muchos datos que se cargan
 * acá ya están en el archivo CrearPdf Por tanto es posible refactorizar todos
 * esos datos para centralizarlos con el fin de optimizar el tiempo.
 *
 * @author rodanmuro
 */
public class CrearSeguimientos {

    int MAXIMO_SEMESTRES = 20;
    int COLUMNA_ASIGNATURA_MALLA_PRINCIPAL = 1;
    int COLUMNA_ALFA_MALLA_PRINCIPAL = 4;
    int COLUMNA_NUMERICO_MALLA_PRINCIPAL = 5;
    int COLUMNA_CREDITOS_MALLA_PRINCIPAL = 6;
    int COLUMNA_INICIO_GRUPOS_SEGUIMIENTOS = 5;

    String COLUMNA_INSCRITOS_FUENTE_BANNER = "INSCRITOS";

    String RUTA_MALLA_CURRICULAR = "";
    String RUTA_INSCRITOS_NRC = "";
    String RUTA_OFERTA_ACTUAL = "";
    String RUTA_SALIDA_ARCHIVO_SEGUIMIENTOS = "";
    String RUTA_SALIDA_INSCRITOS_NRC_OFERTA = "";
    String RUTA_SALIDA_CUPOS_ESTIMADOS = "";
    String PERIODO_ACTUAL = "";

    XSSFWorkbook libroMallaCurricular;
    XSSFWorkbook libroInscritosNRC;
    XSSFWorkbook libroOfertaActual;
    XSSFWorkbook libroSalidaInscritosNRC;
    XSSFWorkbook libroCuposEstimadosGrupos;
    XSSFWorkbook libroSalidaSeguimientos;

    ArrayList<String> listadoProgramas;
    ArrayList<String> listadoSemestres;
    ArrayList<String> listadoJornadas;
    ArrayList<String> listadoProgramaSemestre;
    ArrayList<ArrayList<String>> listadoTotalAsignaturasSemestres;

    /**
     *
     * @param rutamallacurricular
     * @param rutainscritosnrc
     * @param rutaofertaactual
     */
    public CrearSeguimientos(String rutamallacurricular,
            String rutainscritosnrc,
            String rutaofertaactual,
            String rutasalidaseguimientos,
            String periodoActual) {

        RUTA_MALLA_CURRICULAR = rutamallacurricular;
        RUTA_INSCRITOS_NRC = rutainscritosnrc;
        RUTA_OFERTA_ACTUAL = rutaofertaactual;
        RUTA_SALIDA_ARCHIVO_SEGUIMIENTOS = rutasalidaseguimientos;

        PERIODO_ACTUAL = periodoActual;

        libroSalidaInscritosNRC = new XSSFWorkbook();
        libroCuposEstimadosGrupos = new XSSFWorkbook();
        libroSalidaSeguimientos = new XSSFWorkbook();

        libroCuposEstimadosGrupos.createSheet();

        try {
            FileInputStream fismallacurricular = new FileInputStream(RUTA_MALLA_CURRICULAR);
            libroMallaCurricular = new XSSFWorkbook(fismallacurricular);

            FileInputStream fislibroinscritosnrc = new FileInputStream(RUTA_INSCRITOS_NRC);
            libroInscritosNRC = new XSSFWorkbook(fislibroinscritosnrc);

            FileInputStream fisofertaactual = new FileInputStream(RUTA_OFERTA_ACTUAL);
            libroOfertaActual = new XSSFWorkbook(fisofertaactual);

        } catch (Exception e) {

            Validaciones.mostrarErroresTotal("<html>Ocurrió un error al cargar los libros de " + "<br>"
                    + " " + RUTA_MALLA_CURRICULAR + "<br>"
                    + " " + RUTA_INSCRITOS_NRC + "<br>"
                    + " " + RUTA_OFERTA_ACTUAL + "<br>"
                    + " por favor revisar que dichas rutas estén bien y sean los archivos adecuados </html>",
                    e);
            e.printStackTrace();
        }

        if (new File(RUTA_SALIDA_ARCHIVO_SEGUIMIENTOS).exists()) {
            crearLibroSalidaInscritosNRC();
            inscritosPorNRC();
            crearLibroSalida();
            listadoGrupos();
            cuposEstimadosGrupo();
            crearHojaPorCarrera();
            crearSalidaSeguimientos();
            JOptionPane.showMessageDialog(null, "Se han terminado de crear los archivos de seguimientos");
        } else {
            Validaciones.mostrarVentanaError("<html> La carpeta de salida seleccionada: <br> "
                    + " " + RUTA_SALIDA_ARCHIVO_SEGUIMIENTOS + "<br>"
                    + " no existe. No se puede ejecutar la creación de los seguimientos"
                    + "</html>");
        }

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

    public Object retornarValor(XSSFCell celda) {
        Object valor = null;

        if (celda.getCellTypeEnum() == CellType.NUMERIC) {
            valor = (int) celda.getNumericCellValue();
        }
        if (celda.getCellTypeEnum() == CellType.STRING) {
            valor = celda.getStringCellValue();
        }
        return valor;
    }

    /**
     * Esta función toma el valor de una celda origen, verifica si dicho origen
     * es numérico o es una cadena, y luego lo pone en la celda destino
     *
     * @param celdaOrigen
     * @param celdaDestino
     */
    public void ponerValor(XSSFCell celdaOrigen, XSSFCell celdaDestino) {

        if (celdaOrigen.getCellTypeEnum() == CellType.NUMERIC) {
            double valor = celdaOrigen.getNumericCellValue();
            celdaDestino.setCellValue(valor);
        }
        if (celdaOrigen.getCellTypeEnum() == CellType.STRING) {
            String cadena = celdaOrigen.getStringCellValue();
            celdaDestino.setCellValue(cadena);
        }

    }

    public boolean celdasIgualContenido(XSSFCell celda1, XSSFCell celda2) {
        boolean iguales = false;

        if (celda1.getCellTypeEnum().equals(celda2.getCellTypeEnum())) {
            if (celda1.getCellTypeEnum() == CellType.NUMERIC) {
                if (celda1.getNumericCellValue() == celda2.getNumericCellValue()) {
                    iguales = true;
                }
            }
            if (celda1.getCellTypeEnum() == CellType.STRING) {
                if (celda1.getStringCellValue().equals(celda2.getStringCellValue())) {
                    iguales = true;
                }
            }
        }

        return iguales;
    }

    public void crearLibroSalidaInscritosNRC() {

        XSSFSheet hojaOfertaOriginal = libroOfertaActual.getSheetAt(0);

        //confuigrarmos el libros de salida
        libroSalidaInscritosNRC.createSheet();
        XSSFSheet hojaDestinoInscritosNRC = libroSalidaInscritosNRC.getSheetAt(0);

        int i = 0;

        if (ValidacionesOfertaEducativa.
                validarEncabezadosOfertaEducativaInicialCrearSeguimientos(hojaOfertaOriginal)) {
            int columnaPrograma = ObtenerEncabezados.obtenerCeldaEncabezado(hojaOfertaOriginal, 
                    ValoresEncabezados.OFERTAEDUCATIVA_INDICEFILAENCABEZADOS, 
                    ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_PROGRAMA).getColumnIndex();
            int columnaSemestre = ObtenerEncabezados.obtenerCeldaEncabezado(hojaOfertaOriginal, 
                    ValoresEncabezados.OFERTAEDUCATIVA_INDICEFILAENCABEZADOS, 
                    ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_SEMESTRE).getColumnIndex();
            int columnaJornada = ObtenerEncabezados.obtenerCeldaEncabezado(hojaOfertaOriginal, 
                    ValoresEncabezados.OFERTAEDUCATIVA_INDICEFILAENCABEZADOS, 
                    ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_JORNADA).getColumnIndex();
            int columnaAlfa = ObtenerEncabezados.obtenerCeldaEncabezado(hojaOfertaOriginal, 
                    ValoresEncabezados.OFERTAEDUCATIVA_INDICEFILAENCABEZADOS, 
                    ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_ALFA).getColumnIndex();
            int columnaNumerico = ObtenerEncabezados.obtenerCeldaEncabezado(hojaOfertaOriginal, 
                    ValoresEncabezados.OFERTAEDUCATIVA_INDICEFILAENCABEZADOS, 
                    ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_NUMERICO).getColumnIndex();
            int columnaCreditos = ObtenerEncabezados.obtenerCeldaEncabezado(hojaOfertaOriginal, 
                    ValoresEncabezados.OFERTAEDUCATIVA_INDICEFILAENCABEZADOS, 
                    ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_CREDITOS).getColumnIndex();
            int columnaAsignatura = ObtenerEncabezados.obtenerCeldaEncabezado(hojaOfertaOriginal, 
                    ValoresEncabezados.OFERTAEDUCATIVA_INDICEFILAENCABEZADOS, 
                    ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_ASIGNATURA).getColumnIndex();
            int columnaNRC = ObtenerEncabezados.obtenerCeldaEncabezado(hojaOfertaOriginal, 
                    ValoresEncabezados.OFERTAEDUCATIVA_INDICEFILAENCABEZADOS, 
                    ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_NRC).getColumnIndex();
            int columnaCupo = ObtenerEncabezados.obtenerCeldaEncabezado(hojaOfertaOriginal, 
                    ValoresEncabezados.OFERTAEDUCATIVA_INDICEFILAENCABEZADOS, 
                    ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_CUPO).getColumnIndex();
            int columnaIdCrucComp = ObtenerEncabezados.obtenerCeldaEncabezado(hojaOfertaOriginal, 
                    ValoresEncabezados.OFERTAEDUCATIVA_INDICEFILAENCABEZADOS, 
                    ValoresEncabezados.ENCABEZADOOFERTAEDUCATIVA_ICRUCECOMP).getColumnIndex();
            
            
            
            while (!celdaVacia(hojaOfertaOriginal.getRow(2 + i), 0)) {

                //tomamos programa 0, semestre 1, jornada 4, alfa 5, 
                //numerico 6, creditos 7, asingtura 8, nrc 9, cupomaximo 9, idcrucecompartidas 10
                //en la hoja nueva es necesario crear cada fila
                ponerValor(hojaOfertaOriginal.getRow(2 + i).getCell(columnaPrograma), hojaDestinoInscritosNRC.createRow(2 + i).createCell(0));
                ponerValor(hojaOfertaOriginal.getRow(2 + i).getCell(columnaSemestre), hojaDestinoInscritosNRC.getRow(2 + i).createCell(1));
                ponerValor(hojaOfertaOriginal.getRow(2 + i).getCell(columnaJornada), hojaDestinoInscritosNRC.getRow(2 + i).createCell(2));
                ponerValor(hojaOfertaOriginal.getRow(2 + i).getCell(columnaAlfa), hojaDestinoInscritosNRC.getRow(2 + i).createCell(3));
                ponerValor(hojaOfertaOriginal.getRow(2 + i).getCell(columnaNumerico), hojaDestinoInscritosNRC.getRow(2 + i).createCell(4));
                ponerValor(hojaOfertaOriginal.getRow(2 + i).getCell(columnaCreditos), hojaDestinoInscritosNRC.getRow(2 + i).createCell(5));
                ponerValor(hojaOfertaOriginal.getRow(2 + i).getCell(columnaAsignatura), hojaDestinoInscritosNRC.getRow(2 + i).createCell(6));
                ponerValor(hojaOfertaOriginal.getRow(2 + i).getCell(columnaNRC), hojaDestinoInscritosNRC.getRow(2 + i).createCell(7));
                ponerValor(hojaOfertaOriginal.getRow(2 + i).getCell(columnaCupo), hojaDestinoInscritosNRC.getRow(2 + i).createCell(8));
                //la celda 13 corresponde a crucecompartido, en el 2017-1 este valor estaba en 12, ahora paso a 13, importante tener en cuenta
                ponerValor(hojaOfertaOriginal.getRow(2 + i).getCell(columnaIdCrucComp), hojaDestinoInscritosNRC.getRow(2 + i).createCell(9));

                i++;
            }
        }

    }

    public void inscritosPorNRC() {

        XSSFSheet hojaOfertaOriginal = libroOfertaActual.getSheetAt(0);

        //confuigrarmos el libros de salida
        XSSFSheet hojaDestinoInscritosNRC = libroSalidaInscritosNRC.getSheetAt(0);

        //hoja donde están los iscritos por nrc
        XSSFSheet hojaInscritosNRC = libroInscritosNRC.getSheetAt(0);

        int i = 0;

        //este libro toma la oferta, y va buscando en el orden de la oferta educativa actual
        while (!celdaVacia(hojaOfertaOriginal.getRow(2 + i), 0)) {
            //tomaos el nrc

            XSSFCell celdaNRCOferta = hojaOfertaOriginal.getRow(2 + i).getCell(9);
            int j = 0;

            while (!celdaVacia(hojaInscritosNRC.getRow(1 + j), 0)) {

//                    System.out.println(""+filaprueba+" "+celdaPrueba);
                XSSFCell celdaNRCInscritos = hojaInscritosNRC.getRow(1 + j).getCell(1);

//                XSSFCell celdaNRCInscritos = hojaInscritosNRC.getRow(1 + j).getCell(1);
                if (celdasIgualContenido(celdaNRCOferta, celdaNRCInscritos)) {
                    XSSFCell celdaInscritos = null;

                    try {
                        //la columna base 0 es la 16 en donde están los inscritos
                        int columnaInscritos = ObtenerEncabezados.obtenerCeldaEncabezado(hojaInscritosNRC, 0, COLUMNA_INSCRITOS_FUENTE_BANNER).getColumnIndex();

                        celdaInscritos = hojaInscritosNRC.getRow(1 + j).getCell(columnaInscritos);
                        int inscritos = (int) celdaInscritos.getNumericCellValue();
                        //la escritura en la hoja de inscritos por nrc supone que
                        //tiene el msimo orden de la oferta educativa
                        hojaDestinoInscritosNRC.getRow(i + 2).createCell(10).setCellValue(inscritos);
                    } catch (Exception e) {
                        System.out.println("Error en la celda "
                                + celdaInscritos.getAddress().formatAsString()
                                + " de la hoja " + celdaInscritos.getSheet().getSheetName()
                                + " en el libro " + RUTA_INSCRITOS_NRC
                        );
                        Validaciones.mostrarErroresTotal("Error en la celda "
                                + celdaInscritos.getAddress().formatAsString()
                                + " de la hoja " + celdaInscritos.getSheet().getSheetName()
                                + " del libro " + RUTA_INSCRITOS_NRC, e);
                        e.printStackTrace();
                    }
                    break;
                }

                j++;
            }

            i++;
        }

    }

    public void listadoGrupos() {
        XSSFSheet hojaInscritos = libroSalidaInscritosNRC.getSheetAt(0);
        int i = 0;

        listadoProgramas = new ArrayList<String>();
        listadoSemestres = new ArrayList<String>();
        listadoJornadas = new ArrayList<String>();
        listadoProgramaSemestre = new ArrayList<String>();

        while (!celdaVacia(hojaInscritos.getRow(2 + i), 0)) {
            String programa = hojaInscritos.getRow(2 + i).getCell(0).getStringCellValue();
            String grupo = hojaInscritos.getRow(2 + i).getCell(1).getStringCellValue();
            String jornada = hojaInscritos.getRow(2 + i).getCell(2).getStringCellValue();

            if (listadoProgramaSemestre.indexOf(programa + grupo) == -1) {
                listadoProgramas.add(programa);
                listadoSemestres.add(grupo);
                listadoJornadas.add(jornada);
                listadoProgramaSemestre.add(programa + grupo);
            }

            i++;
        }
    }

    public void cuposEstimadosGrupo() {

        XSSFSheet hojaInscritos = libroSalidaInscritosNRC.getSheetAt(0);

        for (int i = 0; i < listadoProgramas.size(); i++) {
            int j = 0;
            int maximo = 0;
            ArrayList<Integer> arregloCupos = new ArrayList<Integer>();

            while (!celdaVacia(hojaInscritos.getRow(j + 2), 0)) {
                String programa = hojaInscritos.getRow(j + 2).getCell(0).getStringCellValue();
                String grupo = hojaInscritos.getRow(j + 2).getCell(1).getStringCellValue();

                if (programa.equals(listadoProgramas.get(i)) && grupo.equals(listadoSemestres.get(i))) {
                    if (!celdaVacia(hojaInscritos.getRow(j + 2), 10) 
                            && celdaVacia(hojaInscritos.getRow(j + 2), 9) 
                            /*&& celdaVacia(hojaInscritos.getRow(j + 2), 9)*/) {
                        arregloCupos.add((int) hojaInscritos.getRow(j + 2).getCell(10).getNumericCellValue());
                    }
                }
                j++;
            }

            if (arregloCupos.size() == 0) {
                maximo = 0;
            } else {
                maximo = Collections.max(arregloCupos);
            }

            libroCuposEstimadosGrupos.getSheetAt(0).createRow(2 + i).createCell(0).setCellValue(listadoProgramas.get(i));
            libroCuposEstimadosGrupos.getSheetAt(0).getRow(2 + i).createCell(1).setCellValue(listadoSemestres.get(i));
            libroCuposEstimadosGrupos.getSheetAt(0).getRow(2 + i).createCell(2).setCellValue(listadoJornadas.get(i));
            libroCuposEstimadosGrupos.getSheetAt(0).getRow(2 + i).createCell(3).setCellValue(maximo);

        }

        try {

            //creamos el archivo con hora de salida
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
            Date fechaYHoraSalida = new Date();
            String fechaSalidaFormateada = sdf.format(fechaYHoraSalida);

            RUTA_SALIDA_CUPOS_ESTIMADOS = RUTA_SALIDA_ARCHIVO_SEGUIMIENTOS + "/estimadoCuposGrupo" + fechaSalidaFormateada.toString() + ".xlsx";
            FileOutputStream fosSalidaInscritos = new FileOutputStream(RUTA_SALIDA_CUPOS_ESTIMADOS);
            libroCuposEstimadosGrupos.write(fosSalidaInscritos);

            fosSalidaInscritos.close();
            libroCuposEstimadosGrupos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void crearLibroSalida() {
        try {

            //creamos el archivo con hora de salida
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
            Date fechaYHoraSalida = new Date();
            String fechaSalidaFormateada = sdf.format(fechaYHoraSalida);

            RUTA_SALIDA_INSCRITOS_NRC_OFERTA = RUTA_SALIDA_ARCHIVO_SEGUIMIENTOS + "/inscritosPorNrc" + fechaSalidaFormateada.toString() + ".xlsx";
            FileOutputStream fosSalidaInscritos = new FileOutputStream(RUTA_SALIDA_INSCRITOS_NRC_OFERTA);
            libroSalidaInscritosNRC.write(fosSalidaInscritos);

            fosSalidaInscritos.close();
            libroSalidaInscritosNRC.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void crearHojaPorCarrera() {

        //se toma el libro malla base
        XSSFSheet hojaCarreras = libroMallaCurricular.getSheet("carreras");
        XSSFSheet hojaAsignaturas = libroMallaCurricular.getSheet("asignaturas");

        if (hojaCarreras == null) {
            Validaciones.mostrarVentanaError("No se ha encontrado la hoja llamada carreras en el archivo "
                    + RUTA_MALLA_CURRICULAR + " No se puede ejecutar el proceso de crear seguimientos");
        }
        if (hojaAsignaturas == null) {
            Validaciones.mostrarVentanaError("No se ha encontrado la hoja llamada asignaturas en el archivo "
                    + RUTA_MALLA_CURRICULAR + " No se puede ejecutar el proceso de crear seguimientos");
        }

        int i = 0;
        XSSFSheet hojaCupos = libroCuposEstimadosGrupos.getSheetAt(0);

        //creamos el estilo de centrado
        CellStyle estiloCentrado = libroSalidaSeguimientos.createCellStyle();
        estiloCentrado.setAlignment(HorizontalAlignment.CENTER);

        while (!celdaVacia(hojaCarreras.getRow(i + 1), 0)) {

            listadoTotalAsignaturasSemestres = new ArrayList<ArrayList<String>>();

            //inicializamos cada uno de los elementos interiores del listados de materias
            //recordar que cada elemetno interior es un listado de materias
            //del semestre i+1 (siendo i el elemento i-esimo de listadoAsignaturasSemestre)
            for (int j = 0; j < MAXIMO_SEMESTRES; j++) {
                ArrayList<String> listadoAsignaturaSemestre = new ArrayList<String>();
                listadoTotalAsignaturasSemestres.add(listadoAsignaturaSemestre);
            }

            String nomenclatura = hojaCarreras.getRow(i + 1).getCell(2).getStringCellValue();
            int idCarrera = (int) hojaCarreras.getRow(i + 1).getCell(0).getNumericCellValue();

            String nombreCarrera = hojaCarreras.getRow(i + 1).getCell(1).getStringCellValue();

            XSSFSheet hojaCarrera = libroSalidaSeguimientos.createSheet(nomenclatura + " " + PERIODO_ACTUAL);
            hojaCarrera.createRow(0).createCell(0).setCellValue(nombreCarrera);

            //creamos las filas donde se ponene los grupos, y los inscritos
            hojaCarrera.createRow(1).createCell(0).setCellValue("Semestre-Grupo");
            hojaCarrera.createRow(2).createCell(0).setCellValue("Inscritos");
            hojaCarrera.getRow(2).createCell(3).setCellValue("Créditos");
            hojaCarrera.getRow(2).createCell(4).setCellValue("Semestre");

            int j = 0;
            //en este while se recopilan las materias por semestre 
            while (!celdaVacia(hojaAsignaturas.getRow(j + 1), 0)) {
                if (hojaAsignaturas.getRow(j + 1).getCell(2).getNumericCellValue() == idCarrera) {
                    int semestre = (int) hojaAsignaturas.getRow(j + 1).getCell(3).getNumericCellValue();

                    String alfa = hojaAsignaturas.getRow(j + 1).getCell(COLUMNA_ALFA_MALLA_PRINCIPAL).getStringCellValue();
                    Object numerico = retornarValor(hojaAsignaturas.getRow(j + 1).getCell(COLUMNA_NUMERICO_MALLA_PRINCIPAL));
                    int creditos = (int) hojaAsignaturas.getRow(j + 1).getCell(COLUMNA_CREDITOS_MALLA_PRINCIPAL).getNumericCellValue();

                    String asignatura = hojaAsignaturas.getRow(j + 1).getCell(COLUMNA_ASIGNATURA_MALLA_PRINCIPAL).getStringCellValue();

                    //semestre-1 ya que los semestres comienzan en 1, pero el indice de las listas
                    //comienza en 0
                    listadoTotalAsignaturasSemestres.get(semestre - 1).add(alfa + "," + numerico + "," + asignatura + "," + creditos);

                }

                j++;
            }
            //recopiladas las materias se imprimen en la respectiva hoja
            int k = 1;
            int m = 1;
            for (ArrayList<String> materiasSemestre : listadoTotalAsignaturasSemestres) {
                if (materiasSemestre.size() > 0) {
                    for (String datosAsignatura : materiasSemestre) {
                        //se desglosan los datos de la asignatura para pegarlos
                        //en la hoja de seguimientos
                        String[] dA = datosAsignatura.split(",");

                        //alfa
                        hojaCarrera.createRow(k + 2).createCell(0).setCellValue(dA[0]);
                        //numerico
                        hojaCarrera.getRow(k + 2).createCell(1).setCellValue(dA[1]);
                        //asginatura
                        hojaCarrera.getRow(k + 2).createCell(2).setCellValue(dA[2]);
                        //creditos
                        if (dA[3].indexOf("Escuela y So") != -1) {
                            System.out.println(dA[3]);
                        }
                        hojaCarrera.getRow(k + 2).createCell(3).setCellValue(Integer.parseInt(dA[3]));
                        //semestre
                        hojaCarrera.getRow(k + 2).createCell(4).setCellValue(m);
                        k++;
                    }
                }
                m++;
            }

            //para cada hoja debemos colocar los grupos
            //como se tiene la nomenclatura de la hoja mas el periodo actual
            //se aprovecha para hacerlo en este espacio
            //para ellos vamos a recorrer todos los grupos de la hoja estimadoCuposGrupo 
            //usando un while, tomando los datos de cada fila que son
            //el programa,el semestre, la jornada y el cupo estimado
            int n = 2;
            int contadorGrupos = 0;
            while (!celdaVacia(hojaCupos.getRow(n), 0)) {
                String programaHojaCupos = hojaCupos.getRow(n).getCell(0).getStringCellValue().trim();

                if (programaHojaCupos.equals(nomenclatura)) {
                    String grupo = hojaCupos.getRow(n).getCell(1).getStringCellValue();
                    String jornada = hojaCupos.getRow(n).getCell(2).getStringCellValue();
                    int inscritos = (int) hojaCupos.getRow(n).getCell(3).getNumericCellValue();

                    hojaCarrera.getRow(0).createCell(COLUMNA_INICIO_GRUPOS_SEGUIMIENTOS + contadorGrupos).setCellValue(jornada);
                    hojaCarrera.getRow(1).createCell(COLUMNA_INICIO_GRUPOS_SEGUIMIENTOS + contadorGrupos).setCellValue(grupo);
                    hojaCarrera.getRow(1).getCell(COLUMNA_INICIO_GRUPOS_SEGUIMIENTOS + contadorGrupos).setCellStyle(estiloCentrado);
                    hojaCarrera.getRow(2).createCell(COLUMNA_INICIO_GRUPOS_SEGUIMIENTOS + contadorGrupos).setCellValue(inscritos);
                    hojaCarrera.getRow(2).getCell(COLUMNA_INICIO_GRUPOS_SEGUIMIENTOS + contadorGrupos).setCellStyle(estiloCentrado);

                    contadorGrupos++;

                }

                n++;
            }

            //ajustamos el tamaño de las celdas
            for (int l = 1; l < COLUMNA_INICIO_GRUPOS_SEGUIMIENTOS + contadorGrupos; l++) {
                hojaCarrera.autoSizeColumn(l);
            }

            //damos el formato de linea a los cambios de semestre
            int p = 0;
            CellStyle estiloBordeGrueso = libroSalidaSeguimientos.createCellStyle();
            estiloBordeGrueso.setBorderLeft(BorderStyle.THIN);
            estiloBordeGrueso.setBorderRight(BorderStyle.THIN);
            estiloBordeGrueso.setBorderTop(BorderStyle.THIN);
            estiloBordeGrueso.setBorderBottom(BorderStyle.MEDIUM);

            CellStyle estiloBordeGruesoFondoAzul = libroSalidaSeguimientos.createCellStyle();
            estiloBordeGruesoFondoAzul.setBorderLeft(BorderStyle.THIN);
            estiloBordeGruesoFondoAzul.setBorderRight(BorderStyle.THIN);
            estiloBordeGruesoFondoAzul.setBorderTop(BorderStyle.THIN);
            estiloBordeGruesoFondoAzul.setBorderBottom(BorderStyle.MEDIUM);
            estiloBordeGruesoFondoAzul.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            estiloBordeGruesoFondoAzul.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle estiloBordeDelgadoFondoAzul = libroSalidaSeguimientos.createCellStyle();
            estiloBordeDelgadoFondoAzul.setBorderLeft(BorderStyle.THIN);
            estiloBordeDelgadoFondoAzul.setBorderRight(BorderStyle.THIN);
            estiloBordeDelgadoFondoAzul.setBorderTop(BorderStyle.THIN);
            estiloBordeDelgadoFondoAzul.setBorderBottom(BorderStyle.THIN);
            estiloBordeDelgadoFondoAzul.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            estiloBordeDelgadoFondoAzul.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle estiloBordeDelgadoSinFindo = libroSalidaSeguimientos.createCellStyle();
            estiloBordeDelgadoSinFindo.setBorderLeft(BorderStyle.THIN);
            estiloBordeDelgadoSinFindo.setBorderRight(BorderStyle.THIN);
            estiloBordeDelgadoSinFindo.setBorderTop(BorderStyle.THIN);
            estiloBordeDelgadoSinFindo.setBorderBottom(BorderStyle.THIN);

            CellStyle estiloCentradoBordeGruesoDebajo = libroSalidaSeguimientos.createCellStyle();
            estiloCentradoBordeGruesoDebajo.setAlignment(HorizontalAlignment.CENTER);
            estiloCentradoBordeGruesoDebajo.setBorderBottom(BorderStyle.MEDIUM);

            //colocamos los bordes gruesos al cambio de semestre
            while (!celdaVacia(hojaCarrera.getRow(p + 3), 0)) {
                int semestre = (int) hojaCarrera.getRow(p + 3).getCell(4).getNumericCellValue();

                int semestreS = 0;
                if (!celdaVacia(hojaCarrera.getRow(p + 3 + 1), 0)) {
                    semestreS = (int) hojaCarrera.getRow(p + 3 + 1).getCell(4).getNumericCellValue();
                }

                //si hay cambio de semestre, o bien si el semestre siguiente es cero, es decir
                //ya no hay más materias
                if (semestre != semestreS) {
                    for (int l = 0; l < COLUMNA_INICIO_GRUPOS_SEGUIMIENTOS + contadorGrupos; l++) {
                        if (hojaCarrera.getRow(p + 3).getCell(4).getNumericCellValue() % 2 == 0) {
                            if (celdaVacia(hojaCarrera.getRow(p + 3), l)) {
                                hojaCarrera.getRow(p + 3).createCell(l).setCellStyle(estiloBordeGruesoFondoAzul);
                            } else {
                                hojaCarrera.getRow(p + 3).getCell(l).setCellStyle(estiloBordeGruesoFondoAzul);
                            }
                        } else {
                            if (celdaVacia(hojaCarrera.getRow(p + 3), l)) {
                                hojaCarrera.getRow(p + 3).createCell(l).setCellStyle(estiloBordeGrueso);
                            } else {
                                hojaCarrera.getRow(p + 3).getCell(l).setCellStyle(estiloBordeGrueso);
                            }
                        }
                    }
                } else {
                    for (int l = 0; l < COLUMNA_INICIO_GRUPOS_SEGUIMIENTOS + contadorGrupos; l++) {

                        if (hojaCarrera.getRow(p + 3).getCell(4).getNumericCellValue() % 2 == 0) {
                            if (celdaVacia(hojaCarrera.getRow(p + 3), l)) {
                                hojaCarrera.getRow(p + 3).createCell(l).setCellStyle(estiloBordeDelgadoFondoAzul);
                            } else {
                                hojaCarrera.getRow(p + 3).getCell(l).setCellStyle(estiloBordeDelgadoFondoAzul);
                            }
                        } else {
                            if (celdaVacia(hojaCarrera.getRow(p + 3), l)) {
                                hojaCarrera.getRow(p + 3).createCell(l).setCellStyle(estiloBordeDelgadoSinFindo);
                            } else {
                                hojaCarrera.getRow(p + 3).getCell(l).setCellStyle(estiloBordeDelgadoSinFindo);
                            }
                        }

                    }
                }

                p++;
            }

            //colocamos el borde grueso al principio
            for (int l = 0; l < COLUMNA_INICIO_GRUPOS_SEGUIMIENTOS + contadorGrupos; l++) {
                if (!celdaVacia(hojaCarrera.getRow(2), l)) {
                    hojaCarrera.getRow(2).getCell(l).setCellStyle(estiloCentradoBordeGruesoDebajo);
                } else {
                    hojaCarrera.getRow(2).createCell(l).setCellStyle(estiloCentradoBordeGruesoDebajo);
                }

            }

            //inmovilizamos filas y columnas
            hojaCarrera.createFreezePane(5, 3);

            i++;
        }

    }

    public void crearSalidaSeguimientos() {
        try {

            //creamos el archivo con hora de salida
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
            Date fechaYHoraSalida = new Date();
            String fechaSalidaFormateada = sdf.format(fechaYHoraSalida);

            RUTA_SALIDA_INSCRITOS_NRC_OFERTA = RUTA_SALIDA_ARCHIVO_SEGUIMIENTOS + "/seguimientosCarrera" + fechaSalidaFormateada.toString() + ".xlsx";
            FileOutputStream fosSalidaInscritos = new FileOutputStream(RUTA_SALIDA_INSCRITOS_NRC_OFERTA);
            libroSalidaSeguimientos.write(fosSalidaInscritos);

            fosSalidaInscritos.close();
            libroSalidaSeguimientos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
