/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;

/**
 * Clase temporal para crear la programación del cuatrimestre que viene
 *
 * @author rodanmuro
 */
public class ProgramarCuatrimestre {

    ArrayList<Object[]> genesPlantilla;
    ArrayList<Object[]> listadoHojaCalculo;
    ArrayList<ArrayList<Date>> arregloSesiones;

    Random rand;

    String RUTA_LIBRO_OFERTA = "";
    String RUTA_MALLA_BASE = "";

    XSSFWorkbook consolidadoOrigen;
    XSSFWorkbook libroBase;

    XSSFSheet hojaBaseCarreras;
    XSSFSheet hojaBaseAsignaturas;

    //RECORDAR QUE ESTOS VALORES SON BASE 0
    int COLUMNA_OFERTA_ALFA = 5;
    int COLUMNA_OFERTA_NUMERICO = 6;
    int COLUMNA_OFERTA_CRUZADOSCOMPARTIDOS = 13;
    int COLUMNA_OFERTA_NRC = 9;
    int COLUMNA_OFERTA_DOCENTE = 11;
    int COLUMNA_OFERTA_INICIAL_FECHAS = 15;
    int COLUMNA_OFERTA_CREDITOS = 7;
    int COLUMNA_OFERTA_PROGRAMA = 0;
    int COLUMNA_OFERTA_GRUPO = 1;
    int COLUMNA_OFERTA_ASIGNATURA = 8;

    int INDICE_COLUMNA_FECHA_INICIAL = 15;
    int INDICE_FILA_FECHA_INICIAL = 1;
    int INDICE_HOJA_FECHAS = 0;
    int INDICE_FILA_INICIAN_GRUPOS = 2;
    int INDICE_COLUMNA_CRUZADA_COMPARTIDA = 13;
    int INDICE_COLUMNA_PRIMERA_CELDA_VACIA_FECHAS = 0;
    int INDICE_COLUMNA_SEMESTRESGRUPO = 1;

    int INDICE_FILAS_NOMBRES_DIAS = 0;
    int INDICE_FILA_FECHAS = 1;

    int COLUMNA_PROGRAMA = 0;
    int COLUMNA_SEMESTRE = 1;
    int COLUMNA_ASIGNATURA = 8;
    int COLUMNA_DOCENTE = 11;

    int SEMESTRE_CLASES_CORTAS = 6;

    int MAXIMO_SALONES_MARTES_Y_JUEVES = 22;
    int MAXIMO_SALONES_MIERCOLES_Y_VIERNES = 22;
    int MAXIMO_SALONES_SABADO = 28;//NÚMERO HIPOTÉTICO 

    String CADENA_VIRTUAL = " - Virtual";

    ArrayList<Date> DIAS_PROHIBIDOS = new ArrayList<Date>();

    Date FECHA_INICIAL_JORNADA_LYM;
    Date FECHA_INICIAL_JORNADA_LYJ;

    Date FECHA_INICIAL_JORNADA_MYJ;
    Date FECHA_INICIAL_JORNADA_MIYVI;
    Date FECHA_INICIAL_JORNADA_SABADOD1;
    Date FECHA_INICIAL_JORNADA_SABADOT1;
    Date FECHA_INICIAL_JORNADA_SABADOD2;
    Date FECHA_INICIAL_JORNADA_SABADOT2;
    Date FECHA_INICIAL_JORNADA_SABADO;
    Date FECHA_INICIAL_JORNADA_MIYVIP;
    Date FECHA_INICIAL_SEMESTRE_CUATRIMESTRAL;
    Date FECHA_FINAL_SEMESTRE_CUATRIMESTRAL;

    Date FECHA_INICIAL_SEMESTRE_SEMANA;
    Date FECHA_INICIAL_SEMESTRE_SABADOS;
    Date FECHA_FINAL_SEMESTRE;

    String[] jornadas = {"M y J", "Mi y Vi", "SÁBADO D", "SÁBADO T", "SÁBADO D2", "SÁBADO T2", "SÁBADO", "L y J", "L y M", "SABADO C"};

    ArrayList<String> listadoProgramas;
    ArrayList<String> listadoSemestres;
    ArrayList<String> listadoJornadas;
    ArrayList<Integer> listadoCreditos;
    ArrayList<String> listadoAsignaturas;
    ArrayList<Integer> listadoNRC;
    ArrayList<String> listadoDocentes;
    ArrayList<CellStyle> listadoDepuradoColores;
    ArrayList<Integer> listadoCupos;
    ArrayList<String> listadoCruzadosCompartidos;

    ArrayList<String> listadoCompartidosCruzadosRepetidos;

    ArrayList<String> listadoAlfa;
    ArrayList<String> listadoNumerico;

    ArrayList<String> listadoAlfaNumericos8Semanas;

    ArrayList<Integer> listadoCuposMayores;

    ArrayList<String> listadoObservacionesHorarios;

    ArrayList<Integer> idsCruzadosCompartidos;

    ArrayList<Integer> listadoBaseCarrerasID;
    ArrayList<String> listadoBaseCarrerasNomenclatura;

    ArrayList<String> listadoBaseAsginaturasNombres;
    ArrayList<Integer> listadoBaseAsginaturasIdCarreras;
    ArrayList<Integer> listadoBaseAsginaturasIdSemestre;
    ArrayList<String> listadoBaseAsginaturasAlfa;
    ArrayList<String> listadoBaseAsginaturasNumerico;
    ArrayList<Integer> listadoBaseAsginaturasCredito;
    ArrayList<String> listadoBaseAsginaturasSemana;
    ArrayList<Integer> listadoBaseAsginaturasVirtual;

    //los nrc repetidos están conformados por una lista con los nrc repetidos
    //guardada en listadorNRCRepetidos, y los respectivos números de los ids con respecto
    //a todos los listados listadoPRogramas, listadoSemestres, etc, guardados en idsNRCRepetidos
    ArrayList<Integer> listadorNRCRepetidos = new ArrayList<Integer>();
//    ArrayList<ArrayList<Integer>> idsNRCRepetidos = new ArrayList<ArrayList<Integer>>();

    ArrayList<ArrayList<Integer>> idsCompartidosCruzadosRepetidos = new ArrayList<ArrayList<Integer>>();

    //listado de los docentes sin repetir
    ArrayList<String> listadoDepuradoDocentes;
    //listado de los grupos sin repetir
    ArrayList<String[]> listadoDepuradoGrupos;

    String NOMBREHOJAOFERTA = "Oferta educativa";

    public ProgramarCuatrimestre(String libroOferta, String mallaBase) {
        inicializarConstantes(libroOferta);
        cargarMallaBase(mallaBase);
        cargarOfertaEducativa(libroOferta);
        asignarFechasIniciales();

        segundaRevisionSesionesEscritasEnElLibro();
//        crearDiasYFechasHojaDestino(false, true);
//        validarZonaFechas();
        recogerFechasSesionesYaProgramadas();
        System.out.println("tamaño del arreglo de sesiones: " + arregloSesiones.size());
        int grupo = arregloSesiones.size() - 7;
        System.out.println("sesiones programadas para  " + grupo + " " + arregloSesiones.get(grupo).size()
                + " programa: " + listadoProgramas.get(grupo)
                + "docente " + listadoDocentes.get(grupo) + " asignatura " + listadoAsignaturas.get(grupo));
        for (Date fecha : arregloSesiones.get(grupo)) {
            System.out.println("fecha " + fecha);
        }
        ArrayList<String> asignaturasPrimero = asignaturasProgramaSemestre("COSD", 1, false);
        ArrayList<String> dcoentes = obtenerListadoDocentesDictanAlfaNumerico("UVCEUV061");
        for (String a : asignaturasPrimero) {
            System.out.println("" + a);
        }

        System.out.println("es cuatrimeestral " + esGrupoCuatrimestral(listadoSemestres.get(grupo)));
        limpiarRegionHorasCuatrimestral();

        asignacionSesionesCuatrimestrales();
        System.out.println("luego de la asignación sesiones programadas para  " + grupo + " " + arregloSesiones.get(grupo).size()
                + " programa: " + listadoProgramas.get(grupo)
                + "docente " + listadoDocentes.get(grupo) + " asignatura " + listadoAsignaturas.get(grupo));
        for (Date fecha : arregloSesiones.get(grupo)) {
            System.out.println("fecha " + fecha);
        }

        imprimirSesionesHojaCalculo();
//        segundaRevisionSesionesEscritasEnElLibro();
    }

    public void inicializarConstantes(String libroOferta) {
        RUTA_LIBRO_OFERTA = libroOferta;

        listadoProgramas = new ArrayList<String>();
        listadoSemestres = new ArrayList<String>();
        listadoJornadas = new ArrayList<String>();
        listadoCreditos = new ArrayList<Integer>();
        listadoAsignaturas = new ArrayList<String>();
        listadoNRC = new ArrayList<Integer>();
        listadoDocentes = new ArrayList<String>();
        listadoCupos = new ArrayList<Integer>();

        listadoAlfa = new ArrayList<String>();
        listadoNumerico = new ArrayList<String>();

        listadoDepuradoDocentes = new ArrayList<String>();
        listadoCuposMayores = new ArrayList<Integer>();

        arregloSesiones = new ArrayList<ArrayList<Date>>();

        listadoDepuradoColores = new ArrayList<CellStyle>();

        listadoObservacionesHorarios = new ArrayList<String>();

        listadoAlfaNumericos8Semanas = new ArrayList<String>();

        listadoCruzadosCompartidos = new ArrayList<String>();

        listadoCompartidosCruzadosRepetidos = new ArrayList<String>();

        idsCruzadosCompartidos = new ArrayList<Integer>();

        listadoBaseCarrerasID = new ArrayList<Integer>();
        listadoBaseCarrerasNomenclatura = new ArrayList<String>();

        listadoBaseAsginaturasNombres = new ArrayList<String>();
        listadoBaseAsginaturasAlfa = new ArrayList<String>();
        listadoBaseAsginaturasCredito = new ArrayList<Integer>();
        listadoBaseAsginaturasIdCarreras = new ArrayList<Integer>();
        listadoBaseAsginaturasIdSemestre = new ArrayList<Integer>();
        listadoBaseAsginaturasNumerico = new ArrayList<String>();
        listadoBaseAsginaturasSemana = new ArrayList<String>();
        listadoBaseAsginaturasVirtual = new ArrayList<Integer>();
    }

    public void cargarMallaBase(String mallaBase) {
        try {
            FileInputStream fisMalla = new FileInputStream(mallaBase);
            XSSFWorkbook libroMallaBase = new XSSFWorkbook(fisMalla);
            libroBase = libroMallaBase;

            hojaBaseCarreras = libroBase.getSheet("carreras");
            hojaBaseAsignaturas = libroBase.getSheet("asignaturas");

            //cargamos la malla base
            int contadorFilas = 1;
            XSSFRow filaBaseCarreras = hojaBaseCarreras.getRow(contadorFilas);
            while (!celdaVacia(filaBaseCarreras, 0)) {
                listadoBaseCarrerasID.add((int) retornarValor(filaBaseCarreras.getCell(0)));
                listadoBaseCarrerasNomenclatura.add((String) "" + retornarValor(filaBaseCarreras.getCell(2)));

                contadorFilas++;
                filaBaseCarreras = hojaBaseCarreras.getRow(contadorFilas);
            }

            contadorFilas = 1;
            XSSFRow filaBaseAsignaturas = hojaBaseAsignaturas.getRow(contadorFilas);
            while (!celdaVacia(filaBaseAsignaturas, 0)) {
                listadoBaseAsginaturasNombres.add((String) retornarValor(filaBaseAsignaturas.getCell(1)));
                listadoBaseAsginaturasIdCarreras.add((int) retornarValor(filaBaseAsignaturas.getCell(2)));
                listadoBaseAsginaturasIdSemestre.add((int) retornarValor(filaBaseAsignaturas.getCell(3)));

                listadoBaseAsginaturasAlfa.add((String) retornarValor(filaBaseAsignaturas.getCell(4)));
                listadoBaseAsginaturasNumerico.add((String) "" + retornarValor(filaBaseAsignaturas.getCell(5)));
                listadoBaseAsginaturasCredito.add((int) retornarValor(filaBaseAsignaturas.getCell(6)));
                listadoBaseAsginaturasVirtual.add((int) retornarValor(filaBaseAsignaturas.getCell(7)));

                if (celdaVacia(filaBaseAsignaturas, 12)) {
                    listadoBaseAsginaturasSemana.add("");
                } else {
                    listadoBaseAsginaturasSemana.add((String) "" + retornarValor(filaBaseAsignaturas.getCell(12)));
                }

                contadorFilas++;
                filaBaseAsignaturas = hojaBaseAsignaturas.getRow(contadorFilas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarOfertaEducativa(String libroOferta) {
        try {
            FileInputStream fisOferta = new FileInputStream(libroOferta);
            XSSFWorkbook cO = new XSSFWorkbook(fisOferta);
            consolidadoOrigen = cO;

            //Date fecha = consolidadoOrigen.getSheetAt(0).getRow(1).getCell(20).getDateCellValue();
            //vamos a hacer recorrido por toda la hoja
            //hacemos numeroFila
            int numeroFila = 0;

            //vamos a calcular el tiempo que se toma en recorrer todas las filas y generar una población
            long tiempoInicio = System.currentTimeMillis();

            //inicializamos los elementos que iran dentro de cada uno de los genes
            String gprograma = "";
            String gsemestre = "";
            String gjornada = "";

            String galfa = "";
            String gnumerico = "";

            int gcreditos = 0;
            String gAsignatura = "";
            int gNRC = 0;
            String gdocente = "";
            Date gfechaHoraSesion = null;
            int gcupo = 0;
            String gCompartidoCruzado = "";

            genesPlantilla = new ArrayList<Object[]>();

            int j = INDICE_FILA_INICIAN_GRUPOS;

            while (!celdaVacia(consolidadoOrigen.getSheetAt(0).getRow(j), 0)) {
//                }

//                for (int j = 2; j < 716; j++) {
                // j es el contador de las filas, en el archivo "ultimo2" va hasta el 671
                //valorDeExcepcion = j;
                XSSFRow fila = consolidadoOrigen.getSheetAt(0).getRow(j);
                int semestre = (int) semestreRomanoEntero((String) semestreGrupo(fila.getCell(1).getStringCellValue())[0]);
                String jornada = fila.getCell(4).getStringCellValue();
                int creditos = (int) fila.getCell(7).getNumericCellValue();

                //es virtual o no
                if (esVirtual(fila.getCell(8))) {
                } else {
                }

//si una materia es virtual, no se le generan horarios
                Object[] arregloGen = new Object[8];

                if (!esVirtual(fila.getCell(8))) {
                    if (!celdaVacia(fila, 0)) {

                        //asignamos todos los valores que se necesitan para crear un gen
                        gprograma = fila.getCell(0).getStringCellValue();
                        gsemestre = fila.getCell(1).getStringCellValue();
                        gjornada = jornada;
                        gcreditos = creditos;
                        gAsignatura = fila.getCell(8).getStringCellValue();
                        gNRC = 0;//(int) fila.getCell(9).getNumericCellValue();//0 temporal
                        gdocente = fila.getCell(11).getStringCellValue();
                        if (fila.getCell(10).getCellType() == 0) {
                            gcupo = (int) fila.getCell(10).getNumericCellValue();
                        } else {
                            gcupo = 0;
                        }
                        galfa = (String) "" + retornarValor(fila.getCell(5));
                        gnumerico = (String) "" + retornarValor(fila.getCell(6));

                        if (celdaVacia(fila, 13)) {
                            gCompartidoCruzado = "";
                        } else {
                            gCompartidoCruzado = (String) retornarValor(fila.getCell(13));
                        }

                        //No vamos a agregar las materias de EGPR
                        if (!gprograma.equals("EGPR")) {
                            //gCompartidoCruzado = fila.getCell(13).getStringCellValue();
                            listadoProgramas.add(gprograma.trim());
                            listadoSemestres.add(gsemestre.trim());

                            if (gjornada.trim().equals("SÁBADO D")
                                    || gjornada.trim().equals("SÁBADO T")) {
                                if (celdaVacia(fila, 2)) {
                                    if (gjornada.trim().equals("SÁBADO D")) {
                                        listadoJornadas.add("SÁBADO D2");
                                    }
                                    if (gjornada.trim().equals("SÁBADO T")) {
                                        listadoJornadas.add("SÁBADO T2");
                                    }
                                } else {
                                    listadoJornadas.add(gjornada.trim());
                                }
                            } else {

                                if (gjornada.trim().equals("Mi y Vi")
                                        && semestreRomanoEntero((String) semestreGrupo((String) retornarValor(fila.getCell(1)))[0]) == 1) {
                                    listadoJornadas.add("Mi y Vi P");
                                } else {
                                    listadoJornadas.add(gjornada.trim());
                                }

                            }

                            listadoCreditos.add(gcreditos);
                            listadoAsignaturas.add(gAsignatura.trim());
                            listadoNRC.add(gNRC);
                            listadoDocentes.add(gdocente.trim());
                            listadoCupos.add(gcupo);
                            listadoAlfa.add(galfa);
                            listadoNumerico.add(gnumerico);

                            listadoCruzadosCompartidos.add(gCompartidoCruzado);
                        }

                    }

                } else {
                }
                j++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ESTA FUNCIÓN sirve para revisar el libro ya creado y determinar posibles
     * errores en las sesiones programadas.
     */
    public void segundaRevisionSesionesEscritasEnElLibro() {
        System.out.println("Se comienza a revisar el libro");

        int contadorFilas = INDICE_FILA_INICIAN_GRUPOS;

        XSSFSheet hoja = consolidadoOrigen.getSheet("Oferta educativa");
        XSSFRow fila = hoja.getRow(contadorFilas);

        while (!celdaVacia(fila, INDICE_HOJA_FECHAS)) {
            String esVirtual = (String) retornarValor(fila.getCell(12));

            if (esVirtual.trim().equals("No")) {
                String alfa = (String) retornarValor(fila.getCell(COLUMNA_OFERTA_ALFA));
                String numerico = (String) "" + retornarValor(fila.getCell(COLUMNA_OFERTA_NUMERICO));
                String alfaNumerico = alfa + " " + numerico;

                String programa = (String) retornarValor(fila.getCell(COLUMNA_OFERTA_PROGRAMA));
                String docente = (String) retornarValor(fila.getCell(COLUMNA_OFERTA_DOCENTE));
                String grupo = (String) retornarValor(fila.getCell(COLUMNA_OFERTA_GRUPO));
                String asignatura = (String) retornarValor(fila.getCell(COLUMNA_OFERTA_ASIGNATURA));

                int creditos = (int) retornarValor(fila.getCell(COLUMNA_OFERTA_CREDITOS));

                int sesionesIdeales = numeroSesionesIdeales(alfaNumerico, creditos);
                int sesionesProgramadas = 0;

                int contadorColumnas = INDICE_COLUMNA_FECHA_INICIAL;
                XSSFRow filaDias = hoja.getRow(0);
                while (!celdaVacia(filaDias, contadorColumnas)) {
                    if (!celdaVacia(fila, contadorColumnas)) {
                        if (celdaTieneFecha(fila.getCell(contadorColumnas))) {
                            sesionesProgramadas++;
                        } else {
                            if (laHoraTieneSlash(fila.getCell(contadorColumnas))) {
                                sesionesProgramadas = sesionesProgramadas + 2;
                            }
                        }

                    }
                    contadorColumnas++;
                }

                //la salida de los errores luego de la revisión del libro final
                //no se escribe el libro, por el momento sólo se hace en salida de consola
                if (sesionesProgramadas < sesionesIdeales && !programa.equals("EGPR")) {
                    System.out.println(contadorFilas + " ERROR, " + "SESIONES PROGRAMADAS " + sesionesProgramadas
                            + ",SESIONES IDEALES " + sesionesIdeales + "," + programa + "," + grupo + "," + docente + "," + asignatura + "," + alfaNumerico);
                } else {
                    //System.out.println(contadorFilas+" Sesiones correctas "+programa+" "+grupo+" "+docente+" "+asignatura);
                }

            }

            contadorFilas++;
            fila = hoja.getRow(contadorFilas);
        }
        System.out.println("Se terminó de revisar");
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

    public int numeroSesionesIdeales(String alfaNumerico, int creditos) {
        int n = 0;

        if (es8Semanas(alfaNumerico) || es10Semanas(alfaNumerico) || es16Semanas(alfaNumerico)) {
            if (es8Semanas(alfaNumerico)) {
                n = 8;
            }
            if (es10Semanas(alfaNumerico)) {
                n = 10;
            }
            if (es16Semanas(alfaNumerico)) {
                n = 16;
            }
        } else {
            n = creditos * 2;
        }

        return n;
    }

    public boolean es8Semanas(String alfaNumerico) {
        //si es de 8 o 16 semanas no se distingue del programa

        boolean es = false;

        for (int i = 0; i < listadoBaseAsginaturasAlfa.size(); i++) {
            String an = listadoBaseAsginaturasAlfa.get(i) + " " + listadoBaseAsginaturasNumerico.get(i);
            if (an.equals(alfaNumerico)) {
                if (listadoBaseAsginaturasSemana.get(i).trim().equals("")) {
                    es = false;
                    break;
                }
                if (listadoBaseAsginaturasSemana.get(i).trim().equals("8")) {
                    es = true;
                    break;
                }
            }
        }

        return es;
    }

    public boolean es10Semanas(String alfaNumerico) {
        //si es de 8 o 16 semanas no se distingue del programa

        boolean es = false;

        for (int i = 0; i < listadoBaseAsginaturasAlfa.size(); i++) {
            String an = listadoBaseAsginaturasAlfa.get(i) + " " + listadoBaseAsginaturasNumerico.get(i);
            if (an.equals(alfaNumerico)) {
                if (listadoBaseAsginaturasSemana.get(i).trim().equals("")) {
                    es = false;
                    break;
                }
                if (listadoBaseAsginaturasSemana.get(i).trim().equals("10")) {
                    es = true;
                    break;
                }
            }
        }

        return es;
    }

    public boolean es16Semanas(String alfaNumerico) {
        //si es de 8 o 16 semanas no se distingue del programa

        boolean es = false;

        for (int i = 0; i < listadoBaseAsginaturasAlfa.size(); i++) {
            String an = listadoBaseAsginaturasAlfa.get(i) + " " + listadoBaseAsginaturasNumerico.get(i);
            if (an.equals(alfaNumerico)) {
                if (listadoBaseAsginaturasSemana.get(i).trim().equals("")) {
                    es = false;
                    break;
                }
                if (listadoBaseAsginaturasSemana.get(i).trim().equals("16")) {
                    es = true;
                    break;
                }
            }
        }

        return es;
    }

    public boolean esVirtual(XSSFCell celdaAsignatura) {
        boolean eV = false;

        String asignatura = celdaAsignatura.getStringCellValue();
        if (asignatura.indexOf(CADENA_VIRTUAL) != -1) {
            eV = true;
        }

        //en el caso que sea Responsabilidad social una práctica de vida
        //se le deben programar cuatro encuentros
//        if(asignatura.indexOf("Responsabilidad social una práctica de vida")!=-1){
//            eV=false;
//        }
        return eV;
    }

    public int semestreRomanoEntero(String semestreRomano) {
        int semestre = 0;

        if (semestreRomano.equals("I")) {
            semestre = 1;
        }
        if (semestreRomano.equals("II")) {
            semestre = 2;
        }
        if (semestreRomano.equals("III")) {
            semestre = 3;
        }
        if (semestreRomano.equals("IV")) {
            semestre = 4;
        }
        if (semestreRomano.equals("V")) {
            semestre = 5;
        }
        if (semestreRomano.equals("VI")) {
            semestre = 6;
        }
        if (semestreRomano.equals("VII")) {
            semestre = 7;
        }
        if (semestreRomano.equals("VIII")) {
            semestre = 8;
        }
        if (semestreRomano.equals("IX")) {
            semestre = 9;
        }
        if (semestreRomano.equals("X")) {
            semestre = 10;
        }
        return semestre;
    }

    /**
     * Dada una celda, en este caso de la columna dos del archivo de consolidado
     * se devuelvde Un arreglo con el índice 0 el valor en romano, y con el
     * índice 1, el grupo (A, B, C, D...)
     *
     * @param semestreRomanoGrupo
     * @return
     */
    public Object[] semestreGrupo(String semestreRomanoGrupo) {
        Object[] semestreGrupo = new Object[2];
        String romano = "";
        String grupo = "";

        if (semestreRomanoGrupo.trim().indexOf(" ") == -1) {
            romano = semestreRomanoGrupo.trim();
//            System.out.println("romano solo: " + romano);
        } else {
            romano = semestreRomanoGrupo.trim().substring(0, semestreRomanoGrupo.indexOf(" "));
//            System.out.println("romano " + romano);

            grupo = semestreRomanoGrupo.trim().substring(semestreRomanoGrupo.indexOf(" ") + 1);
//            System.out.println("grupo " + grupo);
        }

        semestreGrupo[0] = romano;
        semestreGrupo[1] = grupo;

        return semestreGrupo;
    }

    public void crearDiasYFechasHojaDestino(boolean programarEntreSemana, boolean programaSabados) {
        System.out.println("Creando fechas hoja destino");
        //aca se colocan todas las fechas del semestre
        try {
            Date fa = FECHA_INICIAL_SEMESTRE_SEMANA;//fechaMaximaIniciarAsignatura(jornada, numeroCreditos);
            SimpleDateFormat formatoNumeroDia = new SimpleDateFormat("u");

            SimpleDateFormat formatoDiaEspañol = new SimpleDateFormat("EEEE");

            SimpleDateFormat formatoMesDia = new SimpleDateFormat("MMM-d");

            FileInputStream fis = new FileInputStream(RUTA_LIBRO_OFERTA);
            XSSFWorkbook libroDestino = new XSSFWorkbook(fis);

            XSSFSheet hojaDestino = libroDestino.getSheet(NOMBREHOJAOFERTA);
            XSSFRow filaNombresDias, filaFechas;

            if (hojaDestino.getRow(0) == null) {
                filaNombresDias = hojaDestino.createRow(0);
            } else {
                filaNombresDias = hojaDestino.getRow(0);
            }

            if (hojaDestino.getRow(1) == null) {
                filaFechas = hojaDestino.createRow(1);
            } else {
                filaFechas = hojaDestino.getRow(1);
            }

            int contadorColumnas = 0;

            if (programarEntreSemana) {
                while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                    if (formatoNumeroDia.format(fa).equals("1")
                            || formatoNumeroDia.format(fa).equals("2")
                            || formatoNumeroDia.format(fa).equals("3")
                            || formatoNumeroDia.format(fa).equals("4")
                            || formatoNumeroDia.format(fa).equals("5")) {

                        XSSFCell celda = filaNombresDias.getCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas);

                        if (celda == null) {
                            celda = filaNombresDias.createCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas);
                        }

                        celda.setCellValue(formatoDiaEspañol.format(fa));

                        XSSFCell celdaFechas = filaFechas.getCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas);

                        if (celdaFechas == null) {
                            celdaFechas = filaFechas.createCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas);
                        }

                        celdaFechas.setCellValue(fa);

//                    System.out.println("fa: " + fa + " " + formatoDiaEspañol.format(fa) + " mes dia " + formatoMesDia.format(fa));
                        contadorColumnas++;
                    }

                    fa = sumarRestarDiasFecha(fa, 1);
                }
            }

            if (programaSabados) {
                fa = FECHA_INICIAL_SEMESTRE_CUATRIMESTRAL;
                while (fa.compareTo(FECHA_FINAL_SEMESTRE_CUATRIMESTRAL) <= 0) {
                    if (formatoNumeroDia.format(fa).equals("6")) {

                        XSSFCell celda = filaNombresDias.getCell(INDICE_COLUMNA_PRIMERA_CELDA_VACIA_FECHAS + contadorColumnas);

                        if (celda == null) {
                            celda = filaNombresDias.createCell(INDICE_COLUMNA_PRIMERA_CELDA_VACIA_FECHAS + contadorColumnas);
                        }

                        celda.setCellValue(formatoDiaEspañol.format(fa));

                        XSSFCell celdaFechas = filaFechas.getCell(INDICE_COLUMNA_PRIMERA_CELDA_VACIA_FECHAS + contadorColumnas);

                        if (celdaFechas == null) {
                            celdaFechas = filaFechas.createCell(INDICE_COLUMNA_PRIMERA_CELDA_VACIA_FECHAS + contadorColumnas);
                        }

                        CreationHelper createHelper = libroDestino.getCreationHelper();
                        CellStyle estiloHora = libroDestino.createCellStyle();
                        estiloHora.setDataFormat(createHelper.createDataFormat().getFormat("d-mmm"));
                        celdaFechas.setCellValue(fa);
                        celdaFechas.setCellStyle(estiloHora);

                        System.out.println("fa: " + fa + " " + formatoDiaEspañol.format(fa) + " mes dia " + formatoMesDia.format(fa));

                        contadorColumnas++;

                    }

                    fa = sumarRestarDiasFecha(fa, 7);
                }
            }

            FileOutputStream fos = new FileOutputStream(RUTA_LIBRO_OFERTA);
            libroDestino.write(fos);
            libroDestino.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int primeraColumnaVaciaParaPonerFechasCuatrimestre() {
        int indiceColumna = 0;
        XSSFRow filaFechas = consolidadoOrigen.getSheet(NOMBREHOJAOFERTA).getRow(INDICE_FILA_FECHAS);

        int contadorColumnas = INDICE_COLUMNA_FECHA_INICIAL;
        while (!celdaVacia(filaFechas, contadorColumnas)) {
            contadorColumnas++;
        }

        INDICE_COLUMNA_PRIMERA_CELDA_VACIA_FECHAS = contadorColumnas;

        return contadorColumnas;
    }

    public Date fechaFinalSemestre() {
        return FECHA_FINAL_SEMESTRE;
    }

    public Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);

        return calendar.getTime();
    }

    public void asignarFechasIniciales() {
        try {
            SimpleDateFormat formatoDMA = new SimpleDateFormat("d-M-yyyy");
            Date fecha1 = formatoDMA.parse("13-02-2018");//M y J
            Date fecha2 = formatoDMA.parse("14-02-2018");//Mi y Vi
            Date fecha3 = formatoDMA.parse("05-05-2018");//SÁBADO D Grupo 1
            Date fecha4 = formatoDMA.parse("05-05-2018");//SÁBADO D2
            Date fecha5 = formatoDMA.parse("05-05-2018");//SÁBADO T1
            Date fecha6 = formatoDMA.parse("05-05-2018");//SÁBADO T2
            Date fecha7 = formatoDMA.parse("05-08-2018");//Sábado
            Date fecha8 = formatoDMA.parse("29-01-2018");//L y M
            Date fecha9 = formatoDMA.parse("29-01-2018");//L y J
            Date fecha10 = formatoDMA.parse("31-01-2018");//Mi y Vi P
            Date fecha11 = formatoDMA.parse("05-05-2018");//Mi y Vi P

            FECHA_INICIAL_SEMESTRE_SEMANA = formatoDMA.parse("27-01-2018");

            Date fechaInicialSemestreSabados = formatoDMA.parse("27-01-2018");//Sábado
            FECHA_INICIAL_SEMESTRE_SABADOS = fechaInicialSemestreSabados;

            Date fechaFinalSemestre = formatoDMA.parse("18-08-2018");//Sábado
            FECHA_FINAL_SEMESTRE = fechaFinalSemestre;

            Date fechaInicialSemestreCuatrimestral = formatoDMA.parse("05-05-2018");//Sábado cuatrimestral
            FECHA_INICIAL_SEMESTRE_CUATRIMESTRAL = fechaInicialSemestreCuatrimestral;

            Date fechaFinalSemestreCuatrimestral = formatoDMA.parse("18-08-2018");//Sábado cuatrimestral
            FECHA_FINAL_SEMESTRE_CUATRIMESTRAL = fechaFinalSemestreCuatrimestral;

            setFechasIniciales(fecha1, fecha2, fecha3, fecha4, fecha5, fecha6, fecha7, fecha8, fecha9, fecha10, fecha11);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFechasIniciales(Date fecha1, Date fecha2,
            Date fecha3, Date fecha4,
            Date fecha5, Date fecha6,
            Date fecha7, Date fecha8, Date fecha9, Date fecha10, Date fecha11) {

        setFechaInicialJornada("M y J", fecha1);
        setFechaInicialJornada("Mi y Vi", fecha2);
        setFechaInicialJornada("SÁBADO D", fecha3);
        setFechaInicialJornada("SÁBADO D2", fecha4);
        setFechaInicialJornada("SÁBADO T", fecha5);
        setFechaInicialJornada("SÁBADO T2", fecha6);
        setFechaInicialJornada("SÁBADO", fecha7);
        setFechaInicialJornada("L y M", fecha8);
        setFechaInicialJornada("L y J", fecha9);
        setFechaInicialJornada("Mi y Vi P", fecha10);
        setFechaInicialJornada("SÁBADO C", fecha11);

    }

    public void setFechaInicialJornada(String jornada, Date fecha) {
        if (jornada.equals("M y J")) {
            FECHA_INICIAL_JORNADA_MYJ = fecha;
        }
        if (jornada.equals("Mi y Vi")) {
            FECHA_INICIAL_JORNADA_MIYVI = fecha;
        }
        if (jornada.equals("SÁBADO D")) {
            FECHA_INICIAL_JORNADA_SABADOD1 = fecha;
        }
        if (jornada.equals("SÁBADO D2")) {
            FECHA_INICIAL_JORNADA_SABADOD2 = fecha;
        }
        if (jornada.equals("SÁBADO T")) {
            FECHA_INICIAL_JORNADA_SABADOT1 = fecha;
        }
        if (jornada.equals("SÁBADO T2")) {
            FECHA_INICIAL_JORNADA_SABADOT2 = fecha;
        }
        if (jornada.equals("SÁBADO")) {
            FECHA_INICIAL_JORNADA_SABADO = fecha;
        }
        if (jornada.equals("L y M")) {
            FECHA_INICIAL_JORNADA_LYM = fecha;
        }
        if (jornada.equals("L y J")) {
            FECHA_INICIAL_JORNADA_LYJ = fecha;
        }
        if (jornada.equals("Mi y Vi P")) {
            FECHA_INICIAL_JORNADA_MIYVIP = fecha;
        }
        if (jornada.equals("SABADO C")) {
            FECHA_INICIAL_SEMESTRE_CUATRIMESTRAL = fecha;
        }
    }

    public boolean celdaTieneFecha(XSSFCell celda) {
        boolean si = false;

        if (celda == null) {
            si = false;
        } else {
            if (celdaVacia(celda.getRow(), celda.getColumnIndex())) {
                si = false;
            } else {
                if (celda.getCellTypeEnum() == CellType.NUMERIC) {
                    if (DateUtil.isCellDateFormatted(celda)) {
                        si = true;
                    }
                }
            }
        }

        return si;
    }

    public void validarZonaFechas() {

        XSSFSheet hojaOferta = consolidadoOrigen.getSheet(NOMBREHOJAOFERTA);
        XSSFRow filaFechas = hojaOferta.getRow(INDICE_FILA_FECHAS);
        XSSFRow filaGrupos = hojaOferta.getRow(INDICE_FILA_INICIAN_GRUPOS);

        int contadorFilas = 0;

        while (!celdaVacia(filaGrupos, 0)) {
            int contadorColumnas = 0;
            while (!celdaVacia(filaFechas, INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas)) {
                //obtenemos el indice de la fila y de la columna
                XSSFCell celdaAMirar = filaGrupos.getCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas);

                if (!celdaVacia(filaGrupos, INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas)) {
                    if (celdaTieneFecha(celdaAMirar)) {
//                        System.out.println("celda con contenido que es fecha válida Fila "
//                                + (INDICE_FILA_INICIAN_GRUPOS + contadorFilas)
//                                + " Columnas"
//                                + (INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas)
//                                + " contenido " + celdaAMirar.getDateCellValue() + " fecha sesion " + retornarFechaHoraSesion(celdaAMirar));
                    } else {
//                        System.out.println("celda con contenido que no es fecha Fila  "
//                                + (INDICE_FILA_INICIAN_GRUPOS + contadorFilas)
//                                + " Columnas" + (INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas)
//                                + " contenido " + retornarValor(celdaAMirar));
                        if (laHoraTieneSlash(celdaAMirar)) {
                            ArrayList<Date> horas = obtenerHorasSlash(celdaAMirar);
                            System.out.println("Contenido con Slash: fila" + celdaAMirar.getRowIndex() + " columna:" + celdaAMirar.getColumnIndex() + " " + horas.get(0) + " " + horas.get(1));
                        }
                    }
                }
                contadorColumnas++;
            }
            contadorFilas++;
            filaGrupos = hojaOferta.getRow(INDICE_FILA_INICIAN_GRUPOS + contadorFilas);
        }
    }

    public Date retornarFechaHoraSesion(XSSFCell celda) {
        int columna = celda.getColumnIndex();

        Date fechaEnCelda = celda.getDateCellValue();
        Calendar cFechaEnCelda = Calendar.getInstance();
        cFechaEnCelda.setTime(fechaEnCelda);

        int hora = cFechaEnCelda.get(Calendar.HOUR_OF_DAY);
        int minutos = cFechaEnCelda.get(Calendar.MINUTE);

        Date fechaEnFilaFechas = consolidadoOrigen
                .getSheet(NOMBREHOJAOFERTA)
                .getRow(INDICE_FILA_FECHAS)
                .getCell(columna).getDateCellValue();

        Calendar cFechaEnFilasFechas = Calendar.getInstance();
        cFechaEnFilasFechas.setTime(fechaEnFilaFechas);
        cFechaEnFilasFechas.set(Calendar.HOUR_OF_DAY, hora);
        cFechaEnFilasFechas.set(Calendar.MINUTE, minutos);

        Date fechayHoraSesion = cFechaEnFilasFechas.getTime();

        return fechayHoraSesion;
    }

    public Date retornarFechaHoraSesionSlash(XSSFCell celda, int hora, int minutos) {
        int columna = celda.getColumnIndex();

        Date fechaEnFilaFechas = consolidadoOrigen
                .getSheet(NOMBREHOJAOFERTA)
                .getRow(INDICE_FILA_FECHAS)
                .getCell(columna).getDateCellValue();

        Calendar cFechaEnFilasFechas = Calendar.getInstance();
        cFechaEnFilasFechas.setTime(fechaEnFilaFechas);
        cFechaEnFilasFechas.set(Calendar.HOUR_OF_DAY, hora);
        cFechaEnFilasFechas.set(Calendar.MINUTE, minutos);

        Date fechayHoraSesion = cFechaEnFilasFechas.getTime();

        return fechayHoraSesion;
    }

    public boolean laHoraTieneSlash(XSSFCell celda) {

        boolean si = false;

        if (celda.getStringCellValue().indexOf("/") != -1) {
            si = true;
        }

        return si;

    }

    public int obtenerHorasCadenaDosPuntos(String cadenaHora) {
        String hora = cadenaHora.substring(0, cadenaHora.indexOf(":"));

        if (cadenaHora.substring(0, 1).equals("0")) {
            hora = cadenaHora.substring(1, cadenaHora.indexOf(":"));
        }

        return Integer.parseInt(hora);
    }

    public int obtenerMinutosCadenaDosPuntos(String cadenaHora) {
        int minutosEntero = 0;
        String minutos = cadenaHora.substring(cadenaHora.indexOf(":") + 1);

        if (minutos.substring(0, 1).equals("0")) {
            minutos = minutos.substring(1);
        }

        try {
            minutosEntero = Integer.parseInt(minutos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return minutosEntero;
    }

    public ArrayList<Date> obtenerHorasSlash(XSSFCell celda) {
        ArrayList<Date> listadoFechas = new ArrayList<Date>();

        if (laHoraTieneSlash(celda)) {
            String cadenaHoras = celda.getStringCellValue().replace(" ", "");
            int indiceCaracterSlash = cadenaHoras.indexOf("/");
            String primeraHora = cadenaHoras.substring(0, indiceCaracterSlash);
            String segundaHora = cadenaHoras.substring(indiceCaracterSlash + 1);

            int horas = obtenerHorasCadenaDosPuntos(primeraHora);
            int minutos = obtenerMinutosCadenaDosPuntos(primeraHora);

            listadoFechas.add(retornarFechaHoraSesionSlash(celda, horas, minutos));

            horas = obtenerHorasCadenaDosPuntos(segundaHora);
            minutos = obtenerMinutosCadenaDosPuntos(segundaHora);

            listadoFechas.add(retornarFechaHoraSesionSlash(celda, horas, minutos));
        }

        return listadoFechas;
    }

    public void recogerFechasSesionesYaProgramadas() {
        XSSFSheet hojaOferta = consolidadoOrigen.getSheet(NOMBREHOJAOFERTA);
        XSSFRow filaFechas = hojaOferta.getRow(INDICE_FILA_FECHAS);
        XSSFRow filaGrupos = hojaOferta.getRow(INDICE_FILA_INICIAN_GRUPOS);

        int contadorFilas = 0;
        int contadorFilasArray = 0;

        while (!celdaVacia(filaGrupos, 0)) {
            //se recogen las sesiones, sólo si no son virtuales
            if (!esVirtual(filaGrupos.getCell(COLUMNA_OFERTA_ASIGNATURA))
                    && !filaGrupos.getCell(COLUMNA_OFERTA_PROGRAMA).getStringCellValue().equals("EGPR")) {

                arregloSesiones.add(new ArrayList<Date>());

                int contadorColumnas = 0;
                while (!celdaVacia(filaFechas, INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas)) {
                    //obtenemos el indice de la fila y de la columna
                    XSSFCell celdaAMirar = filaGrupos.getCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas);

                    if (!celdaVacia(filaGrupos, INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas)) {
                        if (celdaTieneFecha(celdaAMirar)) {
                            arregloSesiones.get(contadorFilasArray).add(retornarFechaHoraSesion(celdaAMirar));
                        } else {

                            if (laHoraTieneSlash(celdaAMirar)) {
                                ArrayList<Date> horas = obtenerHorasSlash(celdaAMirar);
                                arregloSesiones.get(contadorFilasArray).add(horas.get(0));
                                arregloSesiones.get(contadorFilasArray).add(horas.get(1));
                            }
                        }
                    }
                    contadorColumnas++;
                }
                contadorFilasArray++;
            }
            contadorFilas++;
            filaGrupos = hojaOferta.getRow(INDICE_FILA_INICIAN_GRUPOS + contadorFilas);
        }
    }

    public int idParaNomenclaturaCarrera(String nomenclatura) {
        int id = 0;
        for (int i = 0; i < listadoBaseCarrerasNomenclatura.size(); i++) {
            if (listadoBaseCarrerasNomenclatura.get(i).equals(nomenclatura)) {
                id = listadoBaseCarrerasID.get(i);
                break;
            }
        }
        return id;
    }

    /**
     * Función que devuelve un arraylist con los alfanumericos de las
     * asignaturas para un semestre y una carrera dada, tomando los datos de la
     * malla base.
     *
     * @param nomenclaturaPrograma
     * @param semestre
     * @param virtual
     * @return
     */
    public ArrayList<String> asignaturasProgramaSemestre(String nomenclaturaPrograma, int semestre, boolean incluirVirtuales) {
        ArrayList<String> asignaturas = new ArrayList<String>();

        for (int i = 0; i < listadoBaseAsginaturasAlfa.size(); i++) {

            if (idParaNomenclaturaCarrera(nomenclaturaPrograma) == listadoBaseAsginaturasIdCarreras.get(i)) {
                if (listadoBaseAsginaturasIdSemestre.get(i) == semestre) {
                    if (incluirVirtuales) {
                        if (listadoBaseAsginaturasVirtual.get(i) == 1) {
                            String alfaNumerico = listadoBaseAsginaturasAlfa.get(i) + "" + listadoBaseAsginaturasNumerico.get(i);
                            asignaturas.add(alfaNumerico);
                        } else {
                            String alfaNumerico = listadoBaseAsginaturasAlfa.get(i) + "" + listadoBaseAsginaturasNumerico.get(i);
                            asignaturas.add(alfaNumerico);
                        }
                    } else {
                        if (listadoBaseAsginaturasVirtual.get(i) != 1) {
                            String alfaNumerico = listadoBaseAsginaturasAlfa.get(i) + "" + listadoBaseAsginaturasNumerico.get(i);
                            asignaturas.add(alfaNumerico);
                        }
                    }
                }
            }

        }

        return asignaturas;
    }

    public ArrayList<String> obtenerListadoDocentesDictanAlfaNumerico(String alfaNumerico) {
        ArrayList<String> listado = new ArrayList<String>();

        for (int i = 0; i < listadoDocentes.size(); i++) {
            String aN = listadoAlfa.get(i) + listadoNumerico.get(i);

            if (alfaNumerico.equals(aN) && !listado.contains(listadoDocentes.get(i))) {
                listado.add(listadoDocentes.get(i));
            }
        }

        return listado;
    }

    public boolean esGrupoCuatrimestral(String grupoSemestre) {
        boolean es = false;
        String letra = (String) semestreGrupo(grupoSemestre)[1];
        if (letra.length() > 1) {
            if (letra.substring(letra.length() - 1).equals("C")) {
                es = true;
            }
        }
        return es;
    }

    public void asignacionDeSesiones2() {
        System.out.println("Entro a programar sesiones 2");
        //en esta sección vamos a realizar el proceso de asignación de sesiones
        //para ello vamos a recorrer todo el arreglo, de filas del excel, que está
        //guardado en las listas
//        for (int i = 0; i < listadoProgramas.size(); i++) {
//            arregloSesiones.add(new ArrayList<Date>());
//        }

        //conteo de grupos programados y los que  no
        int conteoProgramados, conteoNoProgramados;
        conteoProgramados = 0;
        conteoNoProgramados = 0;

        for (int i = 0; i < listadoProgramas.size(); i++) {
            if (esGrupoCuatrimestral(listadoSemestres.get(i))) {
                ArrayList<Date> fechasPosiblesJornada = new ArrayList<Date>();
                ArrayList<int[]> horasJornada = new ArrayList<int[]>();
                int semestreNumero = 0;

                String programa = listadoProgramas.get(i);
                String semestreCadena = listadoSemestres.get(i);
                int numeroCreditos = listadoCreditos.get(i);

                String jornada = listadoJornadas.get(i);
                String alfaNumerico = listadoAlfa.get(i) + " " + listadoNumerico.get(i);

                if (i == 36) {
                    System.out.println("asca");
                }

                fechasPosiblesJornada = listadoFechasValidasAsignatura(jornada, programa, alfaNumerico);
                semestreNumero = semestreRomanoEntero((String) semestreGrupo(listadoSemestres.get(i))[0]);

                horasJornada = listadoHorasClase(jornada, semestreNumero, alfaNumerico);

//                for (int m = 0; m < horasJornada.size(); m++) {
//                    System.out.println(" m: " + m + " Horas en lista: " + horasJornada.get(m)[0] + " Minutos en lista: " + horasJornada.get(m)[1]);
//                }
//                System.out.println("horas jornada " + horasJornada.size() + " numero creditos " + numeroCreditos + " jornada " + jornada);
                boolean saltarHora = false;
                for (int j = 0; j < horasJornada.size(); j++) {

                    for (int k = 0; k < fechasPosiblesJornada.size(); k++) {

//                    System.out.println("" + j + " hora " + horasJornada.get(j)[0] + " minutos " + horasJornada.get(j)[1] + " tamaño " + horasJornada.size());
                        Date horaCreada = crearSesionFechaHora(fechasPosiblesJornada.get(k), horasJornada.get(j)[0], horasJornada.get(j)[1]);

                        boolean cruceDocente = getCruceDocente(listadoDocentes.get(i), semestreNumero, horaCreada, alfaNumerico);
                        boolean cruceGrupo = getCruceGrupo(programa, semestreCadena, semestreNumero, horaCreada, alfaNumerico);
                        boolean diaProhibido = esDiaProhibido(fechasPosiblesJornada.get(k));
                        boolean superaSalones = superaMaximoSalones(horaCreada, jornada);

                        if (arregloSesiones.get(i).size() < numeroSesionesIdeales(alfaNumerico, numeroCreditos)) {
                            if (!cruceDocente && !cruceGrupo && !diaProhibido && !superaSalones) {

                                Date finalSesiones = sumarRestarSemanasFecha(horaCreada, numeroSemanasSumarFinalizarAsignatura(alfaNumerico, numeroCreditos)/*numeroCreditos * 2 * 2 - 2*/);
                                Calendar c = Calendar.getInstance();
                                c.setTime(finalSesiones);

//                            System.out.println("final sesioens"+finalSesiones.toString() +" si empieza en: "+horaCreada.
                                Calendar c2 = Calendar.getInstance();
                                c2.setTime(FECHA_FINAL_SEMESTRE);
                                c2.add(Calendar.HOUR, 24);
//                            System.out.println("fecha final sesiones: " + c.getTime().toString());
//                            System.out.println("fecha final semestre: " + c2.getTime().toString());
//                            System.out.println("fecha final sesiones esta antes dle final del semestre: "+c.before(c2));

                                if (arregloSesiones.get(i).size() == 0 && c2.before(c)) {
                                    if (horasJornada.size() > 1) {
                                        break;//hora que sigue si es fines de semana, si es entre semana, se salta la fecha sin saltar la hora
                                    } else {

                                    }

                                } else {
                                    arregloSesiones.get(i).add(horaCreada);
                                }
                            }
                        } else {
//                        System.out.println("Sesiones completas para i = " + i);
                        }
                        if (esCruzada(i)) {
                            numeroCreditos = mayorCreditosCruzadas(i);
                        }
                        if (arregloSesiones.get(i).size() == numeroSesionesIdeales(alfaNumerico, numeroCreditos)) {
                            break;
                        }

                    }
                    if (esCruzada(i)) {
                        numeroCreditos = mayorCreditosCruzadas(i);
                    }
                    if (arregloSesiones.get(i).size() == numeroSesionesIdeales(alfaNumerico, numeroCreditos)) {
                        break;
                    }

                }

                if (arregloSesiones.get(i).size() < numeroSesionesIdeales(alfaNumerico, numeroCreditos)) {
                    //System.out.println("No se pudieron encontrar todas las sesiones para i = " + i + " Total sesiones " + arregloSesiones.get(i).size() + " asignatura " + listadoAsignaturas.get(i) + " programa " + listadoProgramas.get(i) + " semestre: " + listadoSemestres.get(i)+" Creditos: "+numeroCreditos+" Docente: "+listadoDocentes.get(i));
                    for (int l = 0; l < arregloSesiones.get(i).size(); l++) {
                        //System.out.println("Sesion: " + l + " " + arregloSesiones.get(i).get(l).toString());
                    }

                    conteoNoProgramados++;
//                System.out.println("no programados" + conteoNoProgramados);
                } else {
//                System.out.println("Se puedieron encontrar las sesiones para i = " + i + " Total sesiones " + arregloSesiones.get(i).size() + " asignatura " + listadoAsignaturas.get(i) + " programa " + listadoProgramas.get(i) + " semestre: " + listadoSemestres.get(i));
//                for (int l = 0; l < arregloSesiones.get(i).size(); l++) {
//                    System.out.println("Sesion: " + l + " " + arregloSesiones.get(i).get(l).toString());
//                }
                    conteoProgramados++;
//                System.out.println("programados" + conteoProgramados);
                }
            }
        }

        System.out.println("Programados: " + conteoProgramados + " No programados: " + conteoNoProgramados);
        for (int i = 0; i < arregloSesiones.get(345).size(); i++) {
            System.out.println("programa " + listadoProgramas.get(345)
                    + " semestre " + listadoSemestres.get(345)
                    + " alfanumerico " + listadoAlfa.get(345)
                    + " docente " + listadoDocentes.get(345)
                    + " " + listadoNumerico.get(345) + arregloSesiones.get(345).get(i));
        }
        for (int i = 0; i < arregloSesiones.get(346).size(); i++) {
            System.out.println("programa " + listadoProgramas.get(346)
                    + " semestre " + listadoSemestres.get(346)
                    + " alfanumerico " + listadoAlfa.get(346)
                    + " docente " + listadoDocentes.get(346)
                    + " " + listadoNumerico.get(346) + arregloSesiones.get(346).get(i));
        }

    }

    public void asignacionDeSesiones() {
        //en esta sección vamos a realizar el proceso de asignación de sesiones
        //para ello vamos a recorrer todo el arreglo, de filas del excel, que está
        //guardado en las listas

        //este for se hace solo la primera vez, cuando se van a agregar las sesiones
        // como esta función de asignacióndesesiones, se hace luego de l aasignaciones de sesionces2
        //no hay necesidad de volver a ejecutar, a menos que esta función se ejecute de primera, por el momento estará comentada
//        for (int i = 0; i < listadoProgramas.size(); i++) {
//            if(arregloSesiones.get(i)==null){
//                arregloSesiones.add(new ArrayList<Date>());
//            }
//        }
        //conteo de grupos programados y los que  no
        int conteoProgramados, conteoNoProgramados;
        conteoProgramados = 0;
        conteoNoProgramados = 0;

        for (int i = 0; i < listadoProgramas.size(); i++) {

            if (esGrupoCuatrimestral(listadoSemestres.get(i))) {
                ArrayList<Date> fechasPosiblesJornada = new ArrayList<Date>();
                ArrayList<int[]> horasJornada = new ArrayList<int[]>();
                int semestreNumero = 0;

                String programa = listadoProgramas.get(i);
                String semestreCadena = listadoSemestres.get(i);
                int numeroCreditos = listadoCreditos.get(i);

                String alfaNumerico = listadoAlfa.get(i) + " " + listadoNumerico.get(i);

                String jornada = listadoJornadas.get(i);
                fechasPosiblesJornada = listadoFechasValidasAsignatura(jornada, programa, alfaNumerico);

                semestreNumero = semestreRomanoEntero((String) semestreGrupo(listadoSemestres.get(i))[0]);

                horasJornada = listadoHorasClase(jornada, semestreNumero, alfaNumerico);

//                for (int m = 0; m < horasJornada.size(); m++) {
//                    System.out.println(" m: " + m + " Horas en lista: " + horasJornada.get(m)[0] + " Minutos en lista: " + horasJornada.get(m)[1]);
//                }
//                System.out.println("horas jornada " + horasJornada.size() + " numero creditos " + numeroCreditos + " jornada " + jornada);
                for (int j = 0; j < horasJornada.size(); j++) {
//                    System.out.println("" + j + " hora " + horasJornada.get(j)[0] + " minutos " + horasJornada.get(j)[1] + " tamaño " + horasJornada.size());

                    for (int k = 0; k < fechasPosiblesJornada.size(); k++) {

                        Date horaCreada = crearSesionFechaHora(fechasPosiblesJornada.get(k), horasJornada.get(j)[0], horasJornada.get(j)[1]);

                        boolean cruceDocente = getCruceDocente(listadoDocentes.get(i), semestreNumero, horaCreada, alfaNumerico);
                        boolean cruceGrupo = getCruceGrupo(programa, semestreCadena, semestreNumero, horaCreada, alfaNumerico);
                        boolean diaProhibido = esDiaProhibido(fechasPosiblesJornada.get(k));
                        boolean superaSalones = superaMaximoSalones(horaCreada, jornada);

                        if (arregloSesiones.get(i).size() < numeroSesionesIdeales(alfaNumerico, numeroCreditos)) {
                            if (!cruceDocente && !cruceGrupo && !diaProhibido && !superaSalones) {
                                //cumplidas estas condiciones se procede a asignar la fecha

                                arregloSesiones.get(i).add(horaCreada);
                            }
                        } else {
//                        System.out.println("Sesiones completas para i = " + i);
                        }

                        if (arregloSesiones.get(i).size() == numeroSesionesIdeales(alfaNumerico, numeroCreditos)) {
                            break;
                        }
                    }
                    if (arregloSesiones.get(i).size() == numeroSesionesIdeales(alfaNumerico, numeroCreditos)) {
                        break;
                    }

                }

                if (arregloSesiones.get(i).size() < numeroSesionesIdeales(alfaNumerico, numeroCreditos)) {
                    //System.out.println("No se pudieron encontrar todas las sesiones para i = " + i + " Total sesiones " + arregloSesiones.get(i).size() + " asignatura " + listadoAsignaturas.get(i) + " programa " + listadoProgramas.get(i) + " semestre: " + listadoSemestres.get(i)+" creditos "+listadoCreditos.get(i)+" docente "+listadoDocentes.get(i)+" jornada "+listadoJornadas.get(i));
                    for (int l = 0; l < arregloSesiones.get(i).size(); l++) {
//                    System.out.println("Sesion: " + l + " " + arregloSesiones.get(i).get(l).toString());
                    }

                    conteoNoProgramados++;
                } else {
//                System.out.println("Se puedieron encontrar las sesiones para i = " + i + " Total sesiones " + arregloSesiones.get(i).size() + " asignatura " + listadoAsignaturas.get(i) + " programa " + listadoProgramas.get(i) + " semestre: " + listadoSemestres.get(i));
//                for (int l = 0; l < arregloSesiones.get(i).size(); l++) {
//                    System.out.println("Sesion: " + l + " " + arregloSesiones.get(i).get(l).toString());
//                }
                    conteoProgramados++;
                }
            }
        }

        System.out.println("Programados: " + conteoProgramados + " No programados: " + conteoNoProgramados);

        System.out.println("sesiones que no se ven");
        for (int i = 0; i < arregloSesiones.get(604).size(); i++) {
            System.out.println("programa " + listadoProgramas.get(604)
                    + " semestre " + listadoSemestres.get(604)
                    + " alfanumerico " + listadoAlfa.get(604)
                    + " docente " + listadoDocentes.get(604)
                    + " " + listadoNumerico.get(604) + arregloSesiones.get(604).get(i));
        }

    }

    public Date crearSesionFechaHora(Date fecha, int hora, int minutos) {
        Date s;

        Calendar c = Calendar.getInstance();
        c.setTime(fecha);

        c.set(Calendar.HOUR_OF_DAY, hora);
        c.set(Calendar.MINUTE, minutos);

        s = c.getTime();

        return s;
    }

    public boolean getCruceDocente(String docente,
            int semestre,
            Date fechaAAsignar,
            String alfaNumerico) {
        boolean cruce = false;

        for (int i = 0; i < listadoProgramas.size(); i++) {
            if (listadoDocentes.get(i).trim().equals(docente.trim())) {
                alfaNumerico = listadoAlfa.get(i) + " " + listadoNumerico.get(i);

                for (int j = 0; j < arregloSesiones.get(i).size(); j++) {
                    if (arregloSesiones.get(i).size() > 0) {
                        int diferencia = diferenciaMinutosSesiones(fechaAAsignar, arregloSesiones.get(i).get(j));
                        ArrayList<Date> fechasCreadas = new ArrayList<Date>(arregloSesiones.get(i));
                        Date f = arregloSesiones.get(i).get(j);
                        String asignatura = listadoAsignaturas.get(i);

                        //debo tener en cuenta la duración de la materia con la cual la voy a comparar
                        //si la fecha que voy a asignar es posterior a la fecha con la cual voya  comparar
                        //entonces debo tener en cuenta la duración de esa sesion con la cual voy a comparar
                        if (fechaAAsignar.after(f)) {
                            //para el semestre debo tomar
                            semestre = semestreRomanoEntero((String) semestreGrupo(listadoSemestres.get(i))[0]);
                        }
                        if (esCruzada(i)) {
                            semestre = mayorSemestreCruzadas(i);
                        }

                        if (semestre > SEMESTRE_CLASES_CORTAS) {

                            if (diferenciaMinutosSesiones(fechaAAsignar, arregloSesiones.get(i).get(j))
                                    < duracionAsignatura(listadoProgramas.get(i), alfaNumerico)) {
                                cruce = true;
                            }

                        }
                        if (semestre <= SEMESTRE_CLASES_CORTAS) {
                            int d = diferenciaMinutosSesiones(fechaAAsignar, arregloSesiones.get(i).get(j));
                            int d2 = duracionAsignatura(listadoProgramas.get(i), alfaNumerico);

                            if (fechaAAsignar.before(arregloSesiones.get(i).get(j))) {
                                d2 = duracionAsignatura(listadoProgramas.get(i), alfaNumerico);
                            } else {
                                d2 = duracionAsignatura(listadoProgramas.get(i), listadoAlfa.get(i) + " " + listadoNumerico.get(i));
                            }

                            if (d < d2) {
//                                System.out.println("aca estamos");
                                return true;
                            }
//                            if (diferenciaMinutosSesiones(fechaAAsignar, arregloSesiones.get(i).get(j))
//                                    < duracionAsignatura(listadoProgramas.get(i), listadoAlfa.get(i) + " " + listadoNumerico.get(i))) {
//                                cruce = true;
//                            }
                        }

                        Calendar c = Calendar.getInstance();
                        c.set(2017, 10, 25, 7, 0);
                        Date d = c.getTime();
                        boolean b = diferenciaMinutosSesiones(d, fechaAAsignar) == 0;
                        int dif = diferenciaMinutosSesiones(fechaAAsignar, arregloSesiones.get(i).get(j));
                        Date fechaAEstudio = arregloSesiones.get(i).get(j);

                        if (b) {
//                            System.out.println("");
                        }
//                        System.out.println(""+b+" "+dif);

                        if (b && docente.trim().equals("DIAZ URIBE MARCELA MARIA") && listadoProgramas.get(i).equals("ASOD") && listadoSemestres.get(i).equals("VI B")) {
                            ArrayList<Date> listadoFechasProgramadas = new ArrayList<Date>(arregloSesiones.get(i));
                            System.out.println(listadoFechasProgramadas + "" + fechaAEstudio + "" + diferencia + " " + f + " " + fechasCreadas + " " + asignatura);
                        }

                    }
                }
            }
        }

        return cruce;

    }

    public boolean getCruceGrupo(String programa, String semestreCadena, int semestre, Date fechaAAsignar, String alfaNumerico) {
        boolean cruce = false;

        for (int i = 0; i < listadoProgramas.size(); i++) {
//            alfaNumerico = listadoAlfa.get(i) + " " + listadoNumerico.get(i);
            if (listadoProgramas.get(i).equals(programa) && listadoSemestres.get(i).equals(semestreCadena)) {

                for (int j = 0; j < arregloSesiones.get(i).size(); j++) {

                    if (arregloSesiones.get(i).size() > 0) {

                        Date f = arregloSesiones.get(i).get(j);

                        //debo tener en cuenta la duración de la materia con la cual la voy a comparar
                        //si la fecha que voy a asignar es posterior a la fecha con la cual voya  comparar
                        //entonces debo tener en cuenta la duración de esa sesion con la cual voy a comparar
                        if (fechaAAsignar.after(f)) {
                            //para el semestre debo tomar
                            semestre = semestreRomanoEntero((String) semestreGrupo(listadoSemestres.get(i))[0]);
                        }

                        if (esCruzada(i)) {
                            semestre = mayorSemestreCruzadas(i);
                        }

                        if (semestre > SEMESTRE_CLASES_CORTAS) {

                            if (diferenciaMinutosSesiones(fechaAAsignar, arregloSesiones.get(i).get(j))
                                    < duracionAsignatura(listadoProgramas.get(i), alfaNumerico)) {

                                return true;

                            }
                        }
                        if (semestre <= SEMESTRE_CLASES_CORTAS) {

                            int d = diferenciaMinutosSesiones(fechaAAsignar, arregloSesiones.get(i).get(j));
                            int d2 = duracionAsignatura(listadoProgramas.get(i), alfaNumerico);

                            if (fechaAAsignar.before(arregloSesiones.get(i).get(j))) {
                                d2 = duracionAsignatura(listadoProgramas.get(i), alfaNumerico);
                            } else {
                                d2 = duracionAsignatura(listadoProgramas.get(i), listadoAlfa.get(i) + " " + listadoNumerico.get(i));
                            }

                            if (d < d2) {
//                                System.out.println("aca estamos");
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return cruce;

    }

    public boolean esDiaProhibido(Date fecha) {
        boolean esdiaprohibido = false;
        if (DIAS_PROHIBIDOS.indexOf(fecha) == -1) {
            esdiaprohibido = false;
        } else {
            esdiaprohibido = true;
        }
        return esdiaprohibido;
    }

    public boolean superaMaximoSalones(Date fechaAsignar, String jornada) {
        boolean s = false;

        int sesionesProgramadas = contarSesionesFechaHora(fechaAsignar);

        Calendar c = Calendar.getInstance();
        c.set(2017, 7, 15, 19, 0, 0);
        Date fecha = c.getTime();

        if (jornada.equals("M y J") && sesionesProgramadas > MAXIMO_SALONES_MARTES_Y_JUEVES - 1) {
            s = true;
        }
        if (jornada.equals("Mi y Vi") && sesionesProgramadas > MAXIMO_SALONES_MIERCOLES_Y_VIERNES - 1) {
            s = true;
        }
        if (jornada.equals("SÁBADO D") && sesionesProgramadas > MAXIMO_SALONES_SABADO - 1) {
            s = true;
        }
        if (jornada.equals("SÁBADO T") && sesionesProgramadas > MAXIMO_SALONES_SABADO - 1) {
            s = true;
        }
        if (jornada.equals("SÁBADO") && sesionesProgramadas > MAXIMO_SALONES_SABADO - 1) {
            s = true;
        }

        return s;
    }

    /**
     * Esta función entrega las fechas posibles para un horanda dada
     *
     * @param jornada
     * @return
     */
    public ArrayList<Date> listadoFechasValidasAsignatura(String jornada, String programa, String alfaNumerico) {
        ArrayList<Date> listadoFechas = new ArrayList<Date>();

        Date fa = fechaInicialSemestre();//fechaMaximaIniciarAsignatura(jornada, numeroCreditos);
        SimpleDateFormat formatoNumeroDia = new SimpleDateFormat("u");

        if (jornada.equals("L y J")) {
            fa = FECHA_INICIAL_JORNADA_LYJ;
        }

        if (jornada.equals("L y M")) {
            fa = FECHA_INICIAL_JORNADA_LYM;
        }

        if (jornada.equals("M y J")) {
            fa = FECHA_INICIAL_JORNADA_MYJ;
        }
        if (jornada.equals("Mi y Vi")) {
            fa = FECHA_INICIAL_JORNADA_MIYVI;
        }
        if (jornada.equals("SÁBADO D")) {
            fa = FECHA_INICIAL_JORNADA_SABADOD1;
        }
        if (jornada.equals("SÁBADO D2")) {
            fa = FECHA_INICIAL_JORNADA_SABADOD2;
        }
        if (jornada.equals("SÁBADO T")) {
            fa = FECHA_INICIAL_JORNADA_SABADOT1;
        }
        if (jornada.equals("SÁBADO T2")) {
            fa = FECHA_INICIAL_JORNADA_SABADOT2;
        }
        if (jornada.equals("Mi y Vi P")) {
            fa = FECHA_INICIAL_JORNADA_MIYVIP;
        }

        if (jornada.equals("Mi y Vi P")) {
            //si la materia es de 8 o 16 semanas, generalmente las de primer
            //semestre, entonces las materias se deben ver cada 8 días
            //así se entrega como salida de esta función un listado de fechas
            //cada 8 días los lunes, y luego cada 8 días los jueves
            if (es8Semanas(alfaNumerico) || es16Semanas(alfaNumerico)) {
//                System.out.println("es de 8 o 16");
//este if es temporal para colocar para aprendizaje autónomo, ya qye se quiere que este
//quede para el jueves
                if (alfaNumerico.equals("FHUM 1100")) {

                    //esto quiere decir que esta materia solo se verá los viernes 
                    //en este caso
                    fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_MIYVIP, 2);

                    while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                        if (formatoNumeroDia.format(fa).equals("5")) {
                            if (!esDiaProhibido(fa)) {
                                listadoFechas.add(fa);
                            }
                        }
                        fa = sumarRestarDiasFecha(fa, 7);
                    }

                } else {

                    if (alfaNumerico.equals("LENG 1010")
                            || alfaNumerico.equals("UVCE UV061")
                            || alfaNumerico.equals("UVCE UV193")
                            || alfaNumerico.equals("UVDE 1020")
                            || alfaNumerico.equals("PSID 120")
                            || alfaNumerico.equals("PSID 120")) {

                        while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                            if (formatoNumeroDia.format(fa).equals("3")) {
                                if (!esDiaProhibido(fa)) {
                                    listadoFechas.add(fa);
                                }
                            }
                            fa = sumarRestarDiasFecha(fa, 7);
                        }

                    } else {
                        while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                            if (formatoNumeroDia.format(fa).equals("3")) {
                                if (!esDiaProhibido(fa)) {
                                    listadoFechas.add(fa);
                                }
                            }
                            fa = sumarRestarDiasFecha(fa, 7);
                        }

                        fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_MIYVIP, 3);

                        while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                            if (formatoNumeroDia.format(fa).equals("5")) {
                                if (!esDiaProhibido(fa)) {
                                    listadoFechas.add(fa);
                                }
                            }
                            fa = sumarRestarDiasFecha(fa, 7);
                        }
                    }

                }

            } else {
                //en caso que estando en esa jornada de MIERCOLES Y VIERNES DE PRIMIPAROS
                //no sea una materia de 8 y 16 semanas
                //entonces se programaran las materias cada
                //15 días, como se ha hecho previamente
                //primero se agrega una lista de lunes cada 15 días
                //y a dicha lista se unen posteriormente una lista de jueves cada
                //15 días
                listadoFechas.add(fa);

                while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                    if (formatoNumeroDia.format(fa).equals("3")) {
                        if (!esDiaProhibido(fa)) {
                            listadoFechas.add(fa);
                        }
                    }
                    fa = sumarRestarDiasFecha(fa, 14);
                }

                fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_MIYVIP, 7);

                while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                    if (formatoNumeroDia.format(fa).equals("3")) {
                        if (!esDiaProhibido(fa)) {
                            listadoFechas.add(fa);
                        }
                    }
                    fa = sumarRestarDiasFecha(fa, 14);
                }

                fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_MIYVIP, 3);

                while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                    if (formatoNumeroDia.format(fa).equals("5")) {
                        if (!esDiaProhibido(fa)) {
                            listadoFechas.add(fa);
                        }
                    }
                    fa = sumarRestarDiasFecha(fa, 14);
                }

                fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_MIYVIP, 3 + 7);

                while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                    if (formatoNumeroDia.format(fa).equals("5")) {
                        if (!esDiaProhibido(fa)) {
                            listadoFechas.add(fa);
                        }
                    }
                    fa = sumarRestarDiasFecha(fa, 14);
                }

            }

        }

        if (jornada.equals("L y J")) {
            //si la materia es de 8 o 16 semanas, generalmente las de primer
            //semestre, entonces las materias se deben ver cada 8 días
            //así se entrega como salida de esta función un listado de fechas
            //cada 8 días los lunes, y luego cada 8 días los jueves
            if (es8Semanas(alfaNumerico) || es16Semanas(alfaNumerico)) {
//                System.out.println("es de 8 o 16");
//este if es temporal para colocar para aprendizaje autónomo, ya qye se quiere que este
//quede para el jueves
                if (alfaNumerico.equals("FHUM 1100")) {

                    //esto quiere decir que esta materia solo se verá los jueves en este caso
                    fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_LYJ, 3);

                    while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                        if (formatoNumeroDia.format(fa).equals("4")) {
                            if (!esDiaProhibido(fa)) {
                                listadoFechas.add(fa);
                            }
                        }
                        fa = sumarRestarDiasFecha(fa, 7);
                    }

                } else {

                    if (alfaNumerico.equals("LENG 1010")
                            || alfaNumerico.equals("UVCE UV061")
                            || alfaNumerico.equals("UVCE UV193")) {

                        while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                            if (formatoNumeroDia.format(fa).equals("1")) {
                                if (!esDiaProhibido(fa)) {
                                    listadoFechas.add(fa);
                                }
                            }
                            fa = sumarRestarDiasFecha(fa, 7);
                        }

                    } else {
                        while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                            if (formatoNumeroDia.format(fa).equals("1")) {
                                if (!esDiaProhibido(fa)) {
                                    listadoFechas.add(fa);
                                }
                            }
                            fa = sumarRestarDiasFecha(fa, 7);
                        }

                        fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_LYJ, 3);

                        while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                            if (formatoNumeroDia.format(fa).equals("4")) {
                                if (!esDiaProhibido(fa)) {
                                    listadoFechas.add(fa);
                                }
                            }
                            fa = sumarRestarDiasFecha(fa, 7);
                        }
                    }

                }

            } else {
                //en caso que estando en esa jornada de lunes y jueves
                //no sea una materia de 8 y 16 semanas
                //entonces se programaran las materias cada
                //15 días, como se ha hecho previamente
                //primero se agrega una lista de lunes cada 15 días
                //y a dicha lista se unen posteriormente una lista de jueves cada
                //15 días

                listadoFechas.add(fa);
                while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                    if (formatoNumeroDia.format(fa).equals("1")) {
                        if (!esDiaProhibido(fa)) {
                            listadoFechas.add(fa);
                        }
                    }
                    fa = sumarRestarDiasFecha(fa, 14);
                }

                fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_LYJ, 7);

                while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                    if (formatoNumeroDia.format(fa).equals("1")) {
                        if (!esDiaProhibido(fa)) {
                            listadoFechas.add(fa);
                        }
                    }
                    fa = sumarRestarDiasFecha(fa, 14);
                }

                fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_LYJ, 3);

                while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                    if (formatoNumeroDia.format(fa).equals("4")) {
                        if (!esDiaProhibido(fa)) {
                            listadoFechas.add(fa);
                        }
                    }
                    fa = sumarRestarDiasFecha(fa, 14);
                }

                fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_LYJ, 3 + 7);

                while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                    if (formatoNumeroDia.format(fa).equals("4")) {
                        if (!esDiaProhibido(fa)) {
                            listadoFechas.add(fa);
                        }
                    }
                    fa = sumarRestarDiasFecha(fa, 14);
                }

            }

        }

        if (jornada.equals("L y M")) {
            //si la materia es de 8 o 16 semanas, generalmente las de primer
            //semestre, entonces las materias se deben ver cada 8 días
            //así se entrega como salida de esta función un listado de fechas
            //cada 8 días los lunes, y luego cada 8 días los jueves
            if (es8Semanas(alfaNumerico) || es16Semanas(alfaNumerico)) {
                //el siguiente if es temporal para aprendizaje autónomo
                //y es para que en estas fechas sólo se coloque aprendizaje 
                //autónomo los días martes o jueves
                //es decir si es aprendizaje autónomo sólo se devuelve el martes
                if (alfaNumerico.equals("FHUM 1100")) {
                    //en esta parte para aprendizaje atuónomo solo se entregan 
                    //martes. como recomendación para un futuro
                    fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_LYM, 1);

                    while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                        if (formatoNumeroDia.format(fa).equals("2")) {
                            if (!esDiaProhibido(fa)) {
                                listadoFechas.add(fa);
                            }
                        }
                        fa = sumarRestarDiasFecha(fa, 7);
                    }

                } else {
                    //acá va lo que se entrega normalmente para martes y jueves
                    while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                        if (formatoNumeroDia.format(fa).equals("1")) {
                            if (!esDiaProhibido(fa)) {
                                listadoFechas.add(fa);
                            }
                        }
                        fa = sumarRestarDiasFecha(fa, 7);
                    }

                    fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_LYM, 1);

                    while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                        if (formatoNumeroDia.format(fa).equals("2")) {
                            if (!esDiaProhibido(fa)) {
                                listadoFechas.add(fa);
                            }
                        }
                        fa = sumarRestarDiasFecha(fa, 7);
                    }
                }

            } else {
                //en caso que estando en esa jornada de lunes y jueves
                //no sea una materia de 8 y 16 semanas
                //entonces se programaran las materias cada
                //15 días, como se ha hecho previamente
                //primero se agrega una lista de lunes cada 15 días
                //y a dicha lista se unen posteriormente una lista de jueves cada
                //15 días
                while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                    if (formatoNumeroDia.format(fa).equals("1")) {
                        if (!esDiaProhibido(fa)) {
                            listadoFechas.add(fa);
                        }
                    }
                    fa = sumarRestarDiasFecha(fa, 14);
                }

                fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_LYM, 7);

                while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                    if (formatoNumeroDia.format(fa).equals("1")) {
                        if (!esDiaProhibido(fa)) {
                            listadoFechas.add(fa);
                        }
                    }
                    fa = sumarRestarDiasFecha(fa, 14);
                }

                fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_LYM, 1);

                while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                    if (formatoNumeroDia.format(fa).equals("2")) {
                        if (!esDiaProhibido(fa)) {
                            listadoFechas.add(fa);
                        }
                    }
                    fa = sumarRestarDiasFecha(fa, 14);
                }

                fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_LYM, 1 + 7);

                while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                    if (formatoNumeroDia.format(fa).equals("2")) {
                        if (!esDiaProhibido(fa)) {
                            listadoFechas.add(fa);
                        }
                    }
                    fa = sumarRestarDiasFecha(fa, 14);
                }

            }

        }

        if (jornada.equals("M y J")) {
            while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                if (formatoNumeroDia.format(fa).equals("2")) {
                    if (!esDiaProhibido(fa)) {
                        listadoFechas.add(fa);
                    }
                }
                fa = sumarRestarDiasFecha(fa, 14);
            }

            fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_MYJ, 7);

            while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                if (formatoNumeroDia.format(fa).equals("2")) {
                    if (!esDiaProhibido(fa)) {
                        listadoFechas.add(fa);
                    }
                }
                fa = sumarRestarDiasFecha(fa, 14);
            }

            fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_MYJ, 2);

            while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                if (formatoNumeroDia.format(fa).equals("4")) {
                    if (!esDiaProhibido(fa)) {
                        listadoFechas.add(fa);
                    }
                }
                fa = sumarRestarDiasFecha(fa, 14);
            }

            fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_MYJ, 2 + 7);

            while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                if (formatoNumeroDia.format(fa).equals("4")) {
                    if (!esDiaProhibido(fa)) {
                        listadoFechas.add(fa);
                    }
                }
                fa = sumarRestarDiasFecha(fa, 14);
            }
        }
        if (jornada.equals("Mi y Vi")) {
            while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                if (formatoNumeroDia.format(fa).equals("3")) {
                    if (!esDiaProhibido(fa)) {
                        listadoFechas.add(fa);
                    }
                }
                fa = sumarRestarDiasFecha(fa, 14);
            }

            fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_MIYVI, 7);

            while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                if (formatoNumeroDia.format(fa).equals("3")) {
                    if (!esDiaProhibido(fa)) {
                        listadoFechas.add(fa);
                    }
                }
                fa = sumarRestarDiasFecha(fa, 14);
            }

            fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_MIYVI, 2);

            while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                if (formatoNumeroDia.format(fa).equals("5")) {
                    if (!esDiaProhibido(fa)) {
                        listadoFechas.add(fa);
                    }
                }
                fa = sumarRestarDiasFecha(fa, 14);
            }

            fa = sumarRestarDiasFecha(FECHA_INICIAL_JORNADA_MIYVI, 2 + 7);

            while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                if (formatoNumeroDia.format(fa).equals("5")) {
                    if (!esDiaProhibido(fa)) {
                        listadoFechas.add(fa);
                    }
                }
                fa = sumarRestarDiasFecha(fa, 14);
            }
        }

        if (jornada.equals("SÁBADO D")
                || jornada.equals("SÁBADO D2")
                || jornada.equals("SÁBADO T")
                || jornada.equals("SÁBADO T2")
                || jornada.equals("SÁBADO")) {
            if (es8Semanas(alfaNumerico) || es16Semanas(alfaNumerico)) {
                while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                    if (!esDiaProhibido(fa)) {
                        listadoFechas.add(fa);
                    }
                    fa = sumarRestarSemanasFecha(fa, 1);
                }
            } else {
                while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                    if (!esDiaProhibido(fa)) {
                        listadoFechas.add(fa);
                    }
                    fa = sumarRestarSemanasFecha(fa, 2);
                }
            }
        }

        return listadoFechas;
    }

    public ArrayList<int[]> listadoHorasClase(String jornada, int semestre, String alfaNumerico) {
        ArrayList<int[]> lh = new ArrayList<int[]>();

        int[] horasMinutos = new int[2];
        horasMinutos[0] = 0;
        horasMinutos[1] = 0;

        if (jornada.equals("M y J")) {
            horasMinutos[0] = 19;
            horasMinutos[1] = 0;

            lh.add(horasMinutos);
        }
        if (jornada.equals("Mi y Vi")) {
            horasMinutos[0] = 19;
            horasMinutos[1] = 0;

            lh.add(horasMinutos);
        }
        if (jornada.equals("SÁBADO")) {
            //aún no definidos. observación, la especialización es cada 15 días también
        }
        if (jornada.equals("SÁBADO D") || jornada.equals("SÁBADO D2")) {

            if (semestre <= SEMESTRE_CLASES_CORTAS) {

                horasMinutos[0] = 7;
                horasMinutos[1] = 0;
                lh.add(horasMinutos);

                horasMinutos = new int[2];
                horasMinutos[0] = 8;
                horasMinutos[1] = 30;
                lh.add(horasMinutos);

                horasMinutos = new int[2];
                horasMinutos[0] = 10;
                horasMinutos[1] = 0;
                lh.add(horasMinutos);

                horasMinutos = new int[2];
                horasMinutos[0] = 11;
                horasMinutos[1] = 30;
                lh.add(horasMinutos);

            } else {
                horasMinutos = new int[2];
                horasMinutos[0] = 7;
                horasMinutos[1] = 0;
                lh.add(horasMinutos);

                horasMinutos = new int[2];
                horasMinutos[0] = 9;
                horasMinutos[1] = 0;
                lh.add(horasMinutos);

                horasMinutos = new int[2];
                horasMinutos[0] = 11;
                horasMinutos[1] = 0;
                lh.add(horasMinutos);
            }
        }
        if (jornada.equals("SÁBADO T") || jornada.equals("SÁBADO T2")) {

            if (semestre <= SEMESTRE_CLASES_CORTAS) {
                //clases con duración de bloques de 45 minutos

//                horasMinutos = new int[2];
//                horasMinutos[0] = 13;
//                horasMinutos[1] = 0;
//                lh.add(horasMinutos);
                horasMinutos = new int[2];
                horasMinutos[0] = 14;
                horasMinutos[1] = 0;
                lh.add(horasMinutos);

                horasMinutos = new int[2];
                horasMinutos[0] = 15;
                horasMinutos[1] = 30;
                lh.add(horasMinutos);

                horasMinutos = new int[2];
                horasMinutos[0] = 17;
                horasMinutos[1] = 0;
                lh.add(horasMinutos);

                horasMinutos = new int[2];
                horasMinutos[0] = 18;
                horasMinutos[1] = 30;
                lh.add(horasMinutos);

            } else {
                //clases con duración de 60 minutos

                horasMinutos = new int[2];
                horasMinutos[0] = 14;
                horasMinutos[1] = 0;
                lh.add(horasMinutos);

                horasMinutos = new int[2];
                horasMinutos[0] = 16;
                horasMinutos[1] = 0;
                lh.add(horasMinutos);

                horasMinutos = new int[2];
                horasMinutos[0] = 18;
                horasMinutos[1] = 0;
                lh.add(horasMinutos);
            }
        }

        if (jornada.equals("L y J") || jornada.equals("L y M") || jornada.equals("Mi y Vi P")) {
            if (alfaNumerico.equals("LENG 1010")) {
                horasMinutos = new int[2];
                horasMinutos[0] = 19;
                horasMinutos[1] = 0;
                lh.add(horasMinutos);
            } else {
                if (numeroCreditosAlfaNumerico(alfaNumerico) == 3) {
                    horasMinutos = new int[2];
                    horasMinutos[0] = 19;
                    horasMinutos[1] = 0;
                    lh.add(horasMinutos);

                    horasMinutos = new int[2];
                    horasMinutos[0] = 19;
                    horasMinutos[1] = 45;
                    lh.add(horasMinutos);
                }
                if (numeroCreditosAlfaNumerico(alfaNumerico) == 2) {
                    horasMinutos = new int[2];
                    horasMinutos[0] = 19;
                    horasMinutos[1] = 0;
                    lh.add(horasMinutos);

                    horasMinutos = new int[2];
                    horasMinutos[0] = 19;
                    horasMinutos[1] = 45;
                    lh.add(horasMinutos);

                    horasMinutos = new int[2];
                    horasMinutos[0] = 20;
                    horasMinutos[1] = 30;
                    lh.add(horasMinutos);

                }

            }

        }

        return lh;
    }

    //estas dos fechas que se van a restar se hacen solo si son para el mismo dia
    public int diferenciaMinutosSesiones(Date f1, Date f2) {
        int dM = 0;

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(f1);
        c2.setTime(f2);

//        dM = c1.getTime()-c2.getTime();
        dM = (int) (((f1.getTime() - f2.getTime()) / 1000) / 60);

//        System.out.println("Diferencia en minutos: " + Math.abs(dM));
        return Math.abs(dM);
    }

    public boolean esCruzada(int i) {
        boolean cruzada = false;

        for (int j = 0; j < idsCompartidosCruzadosRepetidos.size(); j++) {
            for (int k = 0; k < idsCompartidosCruzadosRepetidos.get(j).size(); k++) {
                if (i == idsCompartidosCruzadosRepetidos.get(j).get(k)) {
                    cruzada = true;
                    return cruzada;
                }
            }
        }

        return cruzada;
    }

    public int mayorSemestreCruzadas(int i) {
        int s = 0;

        for (int j = 0; j < idsCompartidosCruzadosRepetidos.size(); j++) {
            for (int k = 0; k < idsCompartidosCruzadosRepetidos.get(j).size(); k++) {
                if (i == idsCompartidosCruzadosRepetidos.get(j).get(k)) {

                    int id1 = idsCompartidosCruzadosRepetidos.get(j).get(0);
                    int id2 = idsCompartidosCruzadosRepetidos.get(j).get(1);

                    int s1 = semestreRomanoEntero((String) semestreGrupo(listadoSemestres.get(id1))[0]);
                    int s2 = semestreRomanoEntero((String) semestreGrupo(listadoSemestres.get(id2))[0]);

                    return Math.max(s1, s2);

                }
            }
        }

        return s;
    }

    public int mayorCreditosCruzadas(int i) {
        int s = 0;

        for (int j = 0; j < idsCompartidosCruzadosRepetidos.size(); j++) {
            for (int k = 0; k < idsCompartidosCruzadosRepetidos.get(j).size(); k++) {
                if (i == idsCompartidosCruzadosRepetidos.get(j).get(k)) {

                    int id1 = idsCompartidosCruzadosRepetidos.get(j).get(0);
                    int id2 = idsCompartidosCruzadosRepetidos.get(j).get(1);

                    int s1 = listadoCreditos.get(id1);
                    int s2 = listadoCreditos.get(id2);

                    return Math.max(s1, s2);

                }
            }
        }

        return s;
    }

    public int duracionAsignatura(String programa, String alfaNumerico) {
        int d = 0;

        int s = semestreAlfaNumerico(programa, alfaNumerico);

        if (s > SEMESTRE_CLASES_CORTAS) {
            d = 120;
        } else {

            if (es8Semanas(alfaNumerico) || es16Semanas(alfaNumerico)) {
                if (es8Semanas(alfaNumerico)) {
                    d = 45 * numeroCreditosAlfaNumerico(alfaNumerico);
                }
                if (es16Semanas(alfaNumerico)) {
                    //este es temporal dadas las condiciones de espacio
                    //mientras se esté en el deogracias
                    d = 45;
                }
            } else {
                d = 90;
            }

        }

        return d;
    }

    public int contarSesionesFechaHora(Date fecha) {
        int n = 0;
        int cruzadas = 0;

        for (int i = 0; i < arregloSesiones.size(); i++) {
            for (int j = 0; j < arregloSesiones.get(i).size(); j++) {
                int dm = diferenciaMinutosSesiones(fecha, arregloSesiones.get(i).get(j));

                if (dm == 0) {
                    if (esCruzada(i)) {
                        cruzadas++;
                    }

                    n++;
                }
            }
        }

        return n - cruzadas / 2;

    }

    public Date fechaInicialSemestre() {

        return FECHA_INICIAL_SEMESTRE_SEMANA;
        //return consolidadoOrigen.getSheetAt(INDICE_HOJA_FECHAS).getRow(INDICE_FILA_FECHA_INICIAL).getCell(INDICE_COLUMNA_FECHA_INICIAL).getDateCellValue();
    }

    public Date sumarRestarSemanasFecha(Date fecha, int semanas) {
        return sumarRestarDiasFecha(fecha, semanas * 7);
    }

    public int numeroCreditosAlfaNumerico(String alfaNumerico) {

        int c = 0;

        for (int i = 0; i < listadoBaseAsginaturasAlfa.size(); i++) {
            String an = listadoBaseAsginaturasAlfa.get(i) + " " + listadoBaseAsginaturasNumerico.get(i);
            if (an.equals(alfaNumerico)) {
                c = listadoBaseAsginaturasCredito.get(i);
                break;
            }
        }

        return c;
    }

    public int semestreAlfaNumerico(String programa, String alfaNumerico) {
        int s = 0;

        for (int i = 0; i < listadoBaseAsginaturasAlfa.size(); i++) {
            String programaBase = nomenclaturaPrograma(listadoBaseAsginaturasIdCarreras.get(i));
            String alfaNumericoBase = listadoBaseAsginaturasAlfa.get(i) + " " + listadoBaseAsginaturasNumerico.get(i);

            if (programa.equals(programaBase) && alfaNumerico.equals(alfaNumericoBase)) {
                s = listadoBaseAsginaturasIdSemestre.get(i);
                break;
            }
        }

        return s;
    }

    public String nomenclaturaPrograma(int id) {
        String np = "";

        for (int i = 0; i < listadoBaseCarrerasID.size(); i++) {
            if (listadoBaseCarrerasID.get(i) == id) {
                np = listadoBaseCarrerasNomenclatura.get(i);
                break;
            }
        }

        return np;
    }

    public int numeroSemanasSumarFinalizarAsignatura(String alfaNumerico, int creditos) {
        int n = 0;

        if (es8Semanas(alfaNumerico) || es16Semanas(alfaNumerico)) {
            if (es8Semanas(alfaNumerico)) {
                n = numeroSesionesIdeales(alfaNumerico, creditos) - 1;
            }
            if (es16Semanas(alfaNumerico)) {
                n = numeroSesionesIdeales(alfaNumerico, creditos) - 1;
            }
        } else {
            n = creditos * 2 * 2 - 2;
        }

        return n;
    }

    public void asignacionSesionesCuatrimestrales() {
        asignacionDeSesiones2();
        asignacionDeSesiones();
    }

    public void imprimirSesionesHojaCalculo() {
        try {
            FileInputStream fis = new FileInputStream(RUTA_LIBRO_OFERTA);
            XSSFWorkbook libroOrigen = new XSSFWorkbook(fis);
            XSSFSheet hojaOferta = libroOrigen.getSheet("Oferta educativa");

            for (int i = 0; i < arregloSesiones.size(); i++) {
                if (esGrupoCuatrimestral(listadoSemestres.get(i))) {

                    String programa = listadoProgramas.get(i);
                    String grupo = listadoSemestres.get(i);
                    String asignatura = listadoAsignaturas.get(i);
                    String docente = listadoDocentes.get(i);

                    int contadorFilasGrupo = 0;
                    XSSFRow filaGrupo = hojaOferta.getRow(INDICE_FILA_INICIAN_GRUPOS + contadorFilasGrupo);
                    boolean encontrado = false;
                    while (!celdaVacia(filaGrupo, 0)) {
                        if (filaGrupo.getCell(COLUMNA_PROGRAMA).getStringCellValue().trim().equals(programa)
                                && filaGrupo.getCell(COLUMNA_SEMESTRE).getStringCellValue().trim().equals(grupo)
                                && filaGrupo.getCell(COLUMNA_ASIGNATURA).getStringCellValue().trim().equals(asignatura)
                                && filaGrupo.getCell(COLUMNA_DOCENTE).getStringCellValue().trim().equals(docente)) {
                            encontrado = true;
                            break;
                        }

                        contadorFilasGrupo++;
                        filaGrupo = hojaOferta.getRow(INDICE_FILA_INICIAN_GRUPOS + contadorFilasGrupo);
                    }

                    if (!encontrado) {
                        System.out.println("No se encontró " + programa + ", grupo: " + grupo + " asignatura " + asignatura);
                    }

                    if (encontrado) {
                        for (int j = 0; j < arregloSesiones.get(i).size(); j++) {
                            //el arreglo de sesiones va en el orden de la hoja de oferta educativa

                            XSSFRow filaFechas = hojaOferta.getRow(INDICE_FILA_FECHAS);

                            int contadorColumnas = 0;
                            XSSFCell celdaFecha = filaFechas.getCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas);
                            Date fechaSesion = arregloSesiones.get(i).get(j);

                            SimpleDateFormat formatoMesDia = new SimpleDateFormat("MMMM-d");

                            while (!celdaVacia(filaFechas, INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas)) {
                                Date fechaHoja = celdaFecha.getDateCellValue();

                                String fechaHojaCadena = formatoMesDia.format(fechaHoja);
                                String fechaSesionCadena = formatoMesDia.format(fechaSesion);

                                if (fechaHojaCadena.equals(fechaSesionCadena)) {

                                    //escribimos el dato en la celda encontrada
                                    XSSFCell celdaAsignar = filaGrupo.getCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas);
                                    if (celdaAsignar == null) {
                                        celdaAsignar = filaGrupo.createCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas);
                                        celdaAsignar.setCellValue(fechaSesion);
                                    } else {
                                        if (!celdaVacia(filaGrupo, INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas)) {

                                            Date fecha1 = celdaAsignar.getDateCellValue();
                                            Date fecha2 = fechaSesion;

                                            celdaAsignar.setCellValue(convertirDosFechasHorasConSlash(fecha1, fecha2));

                                        } else {
                                            celdaAsignar.setCellValue(fechaSesion);
                                        }

                                    }

                                    break;
                                }

                                contadorColumnas++;
                                celdaFecha = filaFechas.getCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas);
                            }

                        }
                    }
                }
            }

            FileOutputStream fos = new FileOutputStream(RUTA_LIBRO_OFERTA);
            libroOrigen.write(fos);
            libroOrigen.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String convertirDosFechasHorasConSlash(Date fecha1, Date fecha2) {
        String horas = "";

        Calendar c1 = Calendar.getInstance();
        c1.setTime(fecha1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(fecha2);

        int hora1 = c1.get(Calendar.HOUR_OF_DAY);
        int minuto1 = c1.get(Calendar.MINUTE);

        int hora2 = c2.get(Calendar.HOUR_OF_DAY);
        int minuto2 = c2.get(Calendar.MINUTE);

        String minuto1C = "";
        if (minuto1 == 0) {
            minuto1C = "00";
        } else {
            minuto1C = "" + minuto1;
        }

        String minuto2C = "";
        if (minuto2 == 0) {
            minuto2C = "00";
        } else {
            minuto2C = "" + minuto2;
        }

        horas = hora1 + ":" + minuto1C + "/" + hora2 + ":" + minuto2C;
        System.out.println("hora " + horas);

        return horas;
    }

    public void limpiarRegionHorasCuatrimestral() {
        try {
//            FileInputStream fis = new FileInputStream(RUTA_LIBRO_OFERTA);
            XSSFWorkbook libroOrigen = consolidadoOrigen;
            XSSFSheet hojaOferta = libroOrigen.getSheet("Oferta educativa");

            int contadorFilas = indiceFilaInicianCuatrimestrales();
            XSSFRow filaGrupos = hojaOferta.getRow(contadorFilas);

            CreationHelper createHelper = libroOrigen.getCreationHelper();
            CellStyle estiloHora = libroOrigen.createCellStyle();
            estiloHora.setDataFormat(createHelper.createDataFormat().getFormat("h:mm"));

            while (!celdaVacia(filaGrupos, 0)) {
                int contadorColumnas = INDICE_COLUMNA_FECHA_INICIAL;

                //la guía para moverse horizontalmente es la primera fila, donde están los nombres de los días
                XSSFRow filaDias = hojaOferta.getRow(0);

                while (!celdaVacia(filaDias, contadorColumnas)) {
                    try {
                        XSSFCell celda = filaGrupos.createCell(contadorColumnas);
                        celda.setCellValue("");
                        celda.setCellStyle(estiloHora);
                        contadorColumnas++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                contadorFilas++;
                filaGrupos = hojaOferta.getRow(contadorFilas);
            }

            FileOutputStream fos = new FileOutputStream(RUTA_LIBRO_OFERTA);
            libroOrigen.write(fos);
            libroOrigen.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int indiceFilaInicianCuatrimestrales() {
        int indice = 0;

        XSSFWorkbook libroOrigen = consolidadoOrigen;
        XSSFSheet hojaOferta = libroOrigen.getSheet("Oferta educativa");
        XSSFRow filaGrupos = hojaOferta.getRow(INDICE_FILA_INICIAN_GRUPOS);

        int contadorFilas = 0;

        while (!celdaVacia(filaGrupos, 0)) {
            if (esGrupoCuatrimestral(filaGrupos.getCell(INDICE_COLUMNA_SEMESTRESGRUPO).getStringCellValue())) {
                indice = INDICE_FILA_INICIAN_GRUPOS + contadorFilas;
                break;
            }
            contadorFilas++;
            filaGrupos = hojaOferta.getRow(INDICE_FILA_INICIAN_GRUPOS + contadorFilas);
        }

        return indice;
    }
}
