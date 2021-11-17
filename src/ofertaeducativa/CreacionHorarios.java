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
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.util.Random;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFComment;

/**
 *
 * @author Rodanmuro
 */
public class CreacionHorarios {

    ArrayList<Object[]> genesPlantilla;
    ArrayList<Object[]> listadoHojaCalculo;
    ArrayList<ArrayList<Date>> arregloSesiones;
    ArrayList<ArrayList<Date>> mejorArregloSesiones;

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

    int INDICE_FILAS_NOMBRES_DIAS = 0;
    int INDICE_FILA_FECHAS = 1;

    int COLUMNA_PROGRAMA = 0;
    int COLUMNA_SEMESTRE = 1;
    int COLUMNA_ASIGNATURA = 8;
    int COLUMNA_DOCENTE = 11;
    int COLUMNA_NRC = 9;

//    int SEMESTRE_CLASES_CORTAS = 6; //en 2018 1
    int SEMESTRE_CLASES_CORTAS = 7;//en 2018 2

    int MAXIMO_SALONES_MARTES_Y_JUEVES = 22;
    int MAXIMO_SALONES_MIERCOLES_Y_VIERNES = 22;
    int MAXIMO_SALONES_SABADO = 100;//NÚMERO HIPOTÉTICO 

    int PROGRAMADOS = 0;
    int NOPROGRAMADOS = 0;

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

    Date FECHA_INICIAL_SEMESTRE_SEMANA;
    Date FECHA_INICIAL_SEMESTRE_SABADOS;
    Date FECHA_FINAL_SEMESTRE;

    String[] jornadas = {"M y J", "Mi y Vi", "SÁBADO D", "SÁBADO T", "SÁBADO D2", "SÁBADO T2", "SÁBADO", "L y J", "L y M"};

    ArrayList<String> listadoProgramas;
    ArrayList<String> listadoSemestres;
    ArrayList<String> listadoJornadas;
    ArrayList<Integer> listadoCreditos;
    ArrayList<String> listadoAsignaturas;
    ArrayList<String> listadoNRC;
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

    ArrayList<Integer> listadoBaseAsginaturasIdCarreras;
    ArrayList<Integer> listadoBaseAsginaturasIdSemestre;
    ArrayList<String> listadoBaseAsginaturasAlfa;
    ArrayList<String> listadoBaseAsginaturasNumerico;
    ArrayList<Integer> listadoBaseAsginaturasCredito;
    ArrayList<Integer> listadoBaseAsginaturasVirtualNoVirtual;
    ArrayList<Integer> listadoBaseAsginaturasSemana;//equivalente a número de sesiones
    ArrayList<Integer> listadoBaseDuracionSesionIdealMinutos;
    ArrayList<Integer> listadoBasePeriodicidadDias;

    //los nrc repetidos están conformados por una lista con los nrc repetidos
    //guardada en listadorNRCRepetidos, y los respectivos números de los ids con respecto
    //a todos los listados listadoPRogramas, listadoSemestres, etc, guardados en idsNRCRepetidos
    ArrayList<Integer> listadorNRCRepetidos = new ArrayList<Integer>();
//    ArrayList<ArrayList<Integer>> idsNRCRepetidos = new ArrayList<ArrayList<Integer>>();

    ArrayList<ArrayList<Integer>> idsCompartidosCruzadosRepetidos = new ArrayList<ArrayList<Integer>>();

    ArrayList<Integer> indicesTodaOfertaDiferenteOrden = new ArrayList<Integer>();

    //listado de los docentes sin repetir
    ArrayList<String> listadoDepuradoDocentes;
    ArrayList<String> listadoDepuradoDocentesOrdenado = new ArrayList<String>();
    //listado de los grupos sin repetir
    ArrayList<String[]> listadoDepuradoGrupos;
    ArrayList<Integer> listadoDepuradoCreditosTotalesDocente = new ArrayList<Integer>();
    ArrayList<Integer> listadoDepuradoCreditosTotalesDocenteOrdenado = new ArrayList<Integer>();

    ArrayList<String> erroresValidacionOfertaMalla;

    String NOMBREHOJAOFERTA = "Oferta educativa";

    boolean PERMITIRCRUCEDIA = false;

    /**
     *
     * @param libroOferta
     * @param mallaBase
     * @param tenerEnCuentaHorariosEscritos
     * @param fechasInicio
     * @param fechasProhibidas
     */
    public CreacionHorarios(String libroOferta,
            String mallaBase,
            boolean tenerEnCuentaHorariosEscritos,
            boolean permitirCruceDia,
            String[] fechasInicio,
            ArrayList<String> fechasProhibidas
    ) {

        inicializarConstantes(libroOferta, permitirCruceDia);
        asignarFechasIniciales(
                fechasInicio[4],//myj
                fechasInicio[5],//miyvi
                fechasInicio[7],//sabadod1
                fechasInicio[8],//sabadod2
                fechasInicio[9],//sabadot1
                fechasInicio[10],//sabadot2
                fechasInicio[7],//sabado
                fechasInicio[3],//lym
                fechasInicio[2],//lyj
                fechasInicio[6],//miyvip
                fechasInicio[3],//iniciosemestresemana
                fechasInicio[7],//iniciosemestresabados
                fechasInicio[1]//fechafinalsemestre
        );
        asignarFechasProhibidas(fechasProhibidas);
        cargarMallaBase(mallaBase);
        cargarOfertaEducativa(libroOferta);
        validarAlfaNumericosOfertaVsAlfaNumericosMallaBase();

        if (erroresValidacionOfertaMalla.size() == 0) {
            crearDiasYFechasHojaDestino(); //a ejecutar
            crearListadoDepuradoDocentes();
            crearListadoDepuradoGrupos();
            crearListadoCuposMayoresDepurado();
            crearlistadoCruzadosCompartidosRepetidos();
            cantidadCreditosPorDocente();
            ordenarListadoDepuradoDocentesPorCreditos();
//            imprimirConsoleParDocenteTotalCreditos();
//            imprimirConsoleParDocenteTotalCreditosOrdenado();

//            asignacionDeSesionesNRCRepetidos();//a ejecutar
//            asignacionDeSesiones2();//a ejecutar
//            //como ensayo se procederá a ejecutar sesiones 1, se reccomienda crear un arreglo que tenga las id de los grupos
//            //que no se pudieron programar.
//            asignacionDeSesiones();//a ejecutar
            inicializarIndicesOrdenProgramacion();

            if (tenerEnCuentaHorariosEscritos) {
                recogerFechasSesionesYaProgramadas();
            }
            imprimirConsolaSesionesDe("EGFI", "I", "ARROYAVE GARCIA JORGE ANDRES", "METODOLOGÍA DE LA INVESTIGACIÓN", "Crear");

            horariosRandomizados();

//            imprimirConsolaSesionesDe("PSID", "X B", "MUÑOZ MUÑOZ CRISTIAN FERNAN", "Opción de grado", "37196");
//            imprimirSesionesDocente();
//            imprimirSesionesGrupo();
//            imprimirDatosHojaExcel();
//            imprimirSesionesSegunOrdenExcel();
//            imprimirNRCRepetidos();
//            imprimirNRCRepetidosConIds();
//            crearListadoDepuradoColores(); 
//            limpiarRegionHoras();//a ejecutar
            //esta parte es la que escribe las sesiones en la hoja de calculo de la salida
            imprimirSesionesHojaCalculo();//a ejecutar, esta función escribe sobre el archivo de excel, pasa las sesiones allí
//            imprimirSesionesSegunOrdenExcel();
            revisarSesionesGenerarObservaciones();//a ejecutar
//            crearHojaObservacionesHorarios();//a ejecutar
//            limpiarFilaFinal(filaPosteriorFinalArchivoOrigen());
//            imprimirDatosHojaExcel();//a ejecutar
//            conteoFechasTodasColumnas();
//            creacionHojaCuposVirtuales();//a ejecutar
//        segundaRevisionSesionesEscritasEnElLibro();
        }

    }

    public void inicializarConstantes(String libroOferta, boolean permitirCruceDia) {
        RUTA_LIBRO_OFERTA = libroOferta;

        listadoProgramas = new ArrayList<String>();
        listadoSemestres = new ArrayList<String>();
        listadoJornadas = new ArrayList<String>();
        listadoCreditos = new ArrayList<Integer>();
        listadoAsignaturas = new ArrayList<String>();
        listadoNRC = new ArrayList<String>();
        listadoDocentes = new ArrayList<String>();
        listadoCupos = new ArrayList<Integer>();

        listadoAlfa = new ArrayList<String>();
        listadoNumerico = new ArrayList<String>();

        listadoDepuradoDocentes = new ArrayList<String>();
        listadoCuposMayores = new ArrayList<Integer>();

        arregloSesiones = new ArrayList<ArrayList<Date>>();
        mejorArregloSesiones = new ArrayList<ArrayList<Date>>();

        listadoDepuradoColores = new ArrayList<CellStyle>();

        listadoObservacionesHorarios = new ArrayList<String>();

        listadoAlfaNumericos8Semanas = new ArrayList<String>();

        listadoCruzadosCompartidos = new ArrayList<String>();

        listadoCompartidosCruzadosRepetidos = new ArrayList<String>();

        idsCruzadosCompartidos = new ArrayList<Integer>();

        listadoBaseCarrerasID = new ArrayList<Integer>();
        listadoBaseCarrerasNomenclatura = new ArrayList<String>();

        listadoBaseAsginaturasAlfa = new ArrayList<String>();
        listadoBaseAsginaturasCredito = new ArrayList<Integer>();
        listadoBaseAsginaturasVirtualNoVirtual = new ArrayList<Integer>();
        listadoBaseAsginaturasIdCarreras = new ArrayList<Integer>();
        listadoBaseAsginaturasIdSemestre = new ArrayList<Integer>();
        listadoBaseAsginaturasNumerico = new ArrayList<String>();
        listadoBaseAsginaturasSemana = new ArrayList<Integer>();
        listadoBaseDuracionSesionIdealMinutos = new ArrayList<Integer>();
        listadoBasePeriodicidadDias = new ArrayList<Integer>();

        erroresValidacionOfertaMalla = new ArrayList<String>();

        PERMITIRCRUCEDIA = permitirCruceDia;

    }

    public void asignarFechasIniciales(
            String fechamyj,
            String fechamiyvi,
            String fechasabadod1,
            String fechasabadod2,
            String fechasabadot1,
            String fechasabadot2,
            String fechasabado,
            String fechalym,
            String fechalyj,
            String fechamiyvip,
            String fechainicialsemestresemena,
            String fechainicialsemestresabados,
            String fechafinalsemestre
    ) {
        try {
            SimpleDateFormat formatoDMA = new SimpleDateFormat("d-M-yyyy");
            Date fecha1 = formatoDMA.parse(fechamyj);//formatoDMA.parse("13-02-2018");//M y J
            Date fecha2 = formatoDMA.parse(fechamiyvi);//formatoDMA.parse("14-02-2018");//Mi y Vi
            Date fecha3 = formatoDMA.parse(fechasabadod1);//formatoDMA.parse("27-01-2018");//SÁBADO D1
            Date fecha4 = formatoDMA.parse(fechasabadod2);//formatoDMA.parse("03-02-2018");//SÁBADO D2
            Date fecha5 = formatoDMA.parse(fechasabadot1);//formatoDMA.parse("27-01-2018");//SÁBADO T1
            Date fecha6 = formatoDMA.parse(fechasabadot2);//formatoDMA.parse("03-02-2018");//SÁBADO T2
            Date fecha7 = formatoDMA.parse(fechasabadod1);//formatoDMA.parse("05-08-2018");//Sábado
            Date fecha8 = formatoDMA.parse(fechalym);//formatoDMA.parse("29-01-2018");//L y M
            Date fecha9 = formatoDMA.parse(fechalyj);//formatoDMA.parse("29-01-2018");//L y J
            Date fecha10 = formatoDMA.parse(fechamiyvip);//formatoDMA.parse("31-01-2018");//Mi y Vi P

            FECHA_INICIAL_SEMESTRE_SEMANA = formatoDMA.parse(fechainicialsemestresemena);//formatoDMA.parse("27-01-2018");

            Date fechaInicialSemestreSabados = formatoDMA.parse(fechainicialsemestresabados);//formatoDMA.parse("27-01-2018");//Sábado
            FECHA_INICIAL_SEMESTRE_SABADOS = fechaInicialSemestreSabados;

            Date fechaFinalSemestre = formatoDMA.parse(fechafinalsemestre);//formatoDMA.parse("16-06-2018");//Sábado
            FECHA_FINAL_SEMESTRE = fechaFinalSemestre;

            setFechasIniciales(fecha1, fecha2, fecha3, fecha4, fecha5, fecha6, fecha7, fecha8, fecha9, fecha10);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void asignarFechasProhibidas(ArrayList<String> fechasProhibidas) {
        try {
            for (String cadenaFechaProhibida : fechasProhibidas) {
                SimpleDateFormat format = new SimpleDateFormat("d-M-yyyy");
                Date fechaProhibida = format.parse(cadenaFechaProhibida);
                DIAS_PROHIBIDOS.add(fechaProhibida);
            }

//            SimpleDateFormat format = new SimpleDateFormat("d-M-yyyy");
//            Date fechaProhibida = format.parse("19-03-2018");
//            DIAS_PROHIBIDOS.add(fechaProhibida);
//
//            fechaProhibida = format.parse("26-03-2018");
//            DIAS_PROHIBIDOS.add(fechaProhibida);
//            fechaProhibida = format.parse("27-03-2018");
//            DIAS_PROHIBIDOS.add(fechaProhibida);
//            fechaProhibida = format.parse("28-03-2018");
//            DIAS_PROHIBIDOS.add(fechaProhibida);
//            fechaProhibida = format.parse("29-03-2018");
//            DIAS_PROHIBIDOS.add(fechaProhibida);
//            fechaProhibida = format.parse("30-03-2018");
//            DIAS_PROHIBIDOS.add(fechaProhibida);
//            fechaProhibida = format.parse("31-03-2018");
//            DIAS_PROHIBIDOS.add(fechaProhibida);
//
//            fechaProhibida = format.parse("01-05-2018");
//            DIAS_PROHIBIDOS.add(fechaProhibida);
//
//            fechaProhibida = format.parse("14-05-2018");
//            DIAS_PROHIBIDOS.add(fechaProhibida);
//
//            fechaProhibida = format.parse("04-06-2018");
//            DIAS_PROHIBIDOS.add(fechaProhibida);
//
//            fechaProhibida = format.parse("11-06-2018");
//            DIAS_PROHIBIDOS.add(fechaProhibida);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                listadoBaseAsginaturasIdCarreras.add((int) retornarValor(filaBaseAsignaturas.getCell(2)));
                listadoBaseAsginaturasIdSemestre.add((int) retornarValor(filaBaseAsignaturas.getCell(3)));

                listadoBaseAsginaturasAlfa.add((String) retornarValor(filaBaseAsignaturas.getCell(4)));
                listadoBaseAsginaturasNumerico.add((String) "" + retornarValor(filaBaseAsignaturas.getCell(5)));
                listadoBaseAsginaturasCredito.add((int) retornarValor(filaBaseAsignaturas.getCell(6)));
                listadoBaseAsginaturasVirtualNoVirtual.add((int) retornarValor(filaBaseAsignaturas.getCell(7)));
                listadoBaseDuracionSesionIdealMinutos.add((int) retornarValor(filaBaseAsignaturas.getCell(13)));
                listadoBasePeriodicidadDias.add((int) retornarValor(filaBaseAsignaturas.getCell(14)));

                listadoBaseAsginaturasSemana.add((int) retornarValor(filaBaseAsignaturas.getCell(12)));

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
            String gNRC = "";
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

//si una materia es virtual, no se le generan horarios
                Object[] arregloGen = new Object[8];
                String alfa = (String) retornarValor(consolidadoOrigen.getSheet("Oferta educativa").getRow(j).getCell(COLUMNA_OFERTA_ALFA));
                String numerico = (String) "" + retornarValor(consolidadoOrigen.getSheet("Oferta educativa").getRow(j).getCell(COLUMNA_OFERTA_NUMERICO));

                if (!esVirtual(alfa.trim() + numerico.trim())) {
                    if (!celdaVacia(fila, 0)) {

                        //asignamos todos los valores que se necesitan para crear un gen
                        gprograma = fila.getCell(0).getStringCellValue();
                        gsemestre = fila.getCell(1).getStringCellValue();
                        gjornada = jornada;
                        gcreditos = creditos;
                        gAsignatura = fila.getCell(8).getStringCellValue();
                        gNRC = (String) "" + retornarValor(fila.getCell(COLUMNA_NRC));//0 temporal
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
                        //y tampoco de EGFI
                        if (true/*!gprograma.equals("EGPR") && !gprograma.equals("EGFI")*/) {
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
                            listadoNRC.add("" + gNRC);
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

    public boolean es8Semanas(String alfaNumerico) {
        //si es de 8 o 16 semanas no se distingue del programa

        boolean es = false;

//        for (int i = 0; i < listadoBaseAsginaturasAlfa.size(); i++) {
//            String an = listadoBaseAsginaturasAlfa.get(i) + "" + listadoBaseAsginaturasNumerico.get(i);
//            if (an.equals(alfaNumerico)) {
//                if (listadoBaseAsginaturasSemana.get(i).trim().equals("")) {
//                    es = false;
//                    break;
//                }
//                if (listadoBaseAsginaturasSemana.get(i).trim().equals("8")) {
//                    es = true;
//                    break;
//                }
//            }
//        }
        for (int i = 0; i < listadoBaseAsginaturasAlfa.size(); i++) {
            String an = listadoBaseAsginaturasAlfa.get(i) + "" + listadoBaseAsginaturasNumerico.get(i);
            if (an.equals(alfaNumerico)) {
                if (listadoBaseAsginaturasSemana.get(i) == 8) {
                    es = false;
                    break;
                }
            }
        }

        return es;
    }

    public boolean es10Semanas(String alfaNumerico) {
        //si es de 8 o 16 semanas no se distingue del programa

        boolean es = false;

//        for (int i = 0; i < listadoBaseAsginaturasAlfa.size(); i++) {
//            String an = listadoBaseAsginaturasAlfa.get(i) + "" + listadoBaseAsginaturasNumerico.get(i);
//            if (an.equals(alfaNumerico)) {
//                if (listadoBaseAsginaturasSemana.get(i).trim().equals("")) {
//                    es = false;
//                    break;
//                }
//                if (listadoBaseAsginaturasSemana.get(i).trim().equals("10")) {
//                    es = true;
//                    break;
//                }
//            }
//        }
        for (int i = 0; i < listadoBaseAsginaturasAlfa.size(); i++) {
            String an = listadoBaseAsginaturasAlfa.get(i) + "" + listadoBaseAsginaturasNumerico.get(i);
            if (an.equals(alfaNumerico)) {
                if (listadoBaseAsginaturasSemana.get(i) == 10) {
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

//        for (int i = 0; i < listadoBaseAsginaturasAlfa.size(); i++) {
//            String an = listadoBaseAsginaturasAlfa.get(i) + "" + listadoBaseAsginaturasNumerico.get(i);
//            if (an.equals(alfaNumerico)) {
//                if (listadoBaseAsginaturasSemana.get(i).trim().equals("")) {
//                    es = false;
//                    break;
//                }
//                if (listadoBaseAsginaturasSemana.get(i).trim().equals("16")) {
//                    es = true;
//                    break;
//                }
//            }
//        }
        for (int i = 0; i < listadoBaseAsginaturasAlfa.size(); i++) {
            String an = listadoBaseAsginaturasAlfa.get(i) + "" + listadoBaseAsginaturasNumerico.get(i);
            if (an.equals(alfaNumerico)) {
                if (listadoBaseAsginaturasSemana.get(i) == 16) {
                    es = true;
                    break;
                }
            }
        }

        return es;
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

    public int numeroCreditosAlfaNumerico(String alfaNumerico) {

        int c = 0;

        for (int i = 0; i < listadoBaseAsginaturasAlfa.size(); i++) {
            String an = listadoBaseAsginaturasAlfa.get(i) + "" + listadoBaseAsginaturasNumerico.get(i);
            if (an.equals(alfaNumerico)) {
                c = listadoBaseAsginaturasCredito.get(i);
                break;
            }
        }

        return c;
    }

    public int numeroSesionesIdeales(String programa, String alfaNumerico, int creditos) {
        int n = 0;

//        if (es8Semanas(alfaNumerico) || es10Semanas(alfaNumerico) || es16Semanas(alfaNumerico)) {
//            if (es8Semanas(alfaNumerico)) {
//                n = 8;
//            }
//            if (es10Semanas(alfaNumerico)) {
//                n = 10;
//            }
//            if (es16Semanas(alfaNumerico)) {
//                n = 16;
//            }
//        } else {
//            n = creditos * 2;
//        }
        for (int i = 0; i < listadoBaseAsginaturasSemana.size(); i++) {
            int idCarreraBase = obtenerIdMallaBaseProgramaDado(programa);

            if (listadoBaseAsginaturasIdCarreras.get(i)==idCarreraBase && (listadoBaseAsginaturasAlfa.get(i).trim() + listadoBaseAsginaturasNumerico.get(i).trim()).equals(alfaNumerico)) {
                n = listadoBaseAsginaturasSemana.get(i);
            }
        }

        return n;
    }

    public int numeroSemanasSumarFinalizarAsignatura(String programa, String alfaNumerico, int creditos) {
        int n = 0;

        if (es8Semanas(alfaNumerico) || es16Semanas(alfaNumerico)) {
            if (es8Semanas(alfaNumerico)) {
                n = numeroSesionesIdeales(programa, alfaNumerico, creditos) - 1;
            }
            if (es16Semanas(alfaNumerico)) {
                n = numeroSesionesIdeales(programa, alfaNumerico, creditos) - 1;
            }
        } else {
            n = creditos * 2 * 2 - 2;
        }

        return n;
    }

    public int duracionAsignatura(String programa, String alfaNumerico) {
        int d = 0;
// esta duración que está comentada se basa en funciones previas
//        int s = semestreAlfaNumerico(programa, alfaNumerico);
//
//        if (s > SEMESTRE_CLASES_CORTAS) {
//            d = 120;
//        } else {
//
//            if (es8Semanas(alfaNumerico) || es16Semanas(alfaNumerico)) {
//                if (es8Semanas(alfaNumerico)) {
//                    d = 45 * numeroCreditosAlfaNumerico(alfaNumerico);
//                }
//                if (es16Semanas(alfaNumerico)) {
//                    //este es temporal dadas las condiciones de espacio
//                    //mientras se esté en el deogracias
//                    d = 45;
//                }
//            } else {
//                d = 90;
//            }
//
//        }

//esta nueva forma de realizar la duración se basa en lo que se digite en la malla base
        for (int i = 0; i < listadoBaseDuracionSesionIdealMinutos.size(); i++) {

            if (nomenclaturaPrograma(listadoBaseAsginaturasIdCarreras.get(i)).trim().equals(programa.trim())
                    && (listadoBaseAsginaturasAlfa.get(i).trim() + listadoBaseAsginaturasNumerico.get(i).trim()).equals(alfaNumerico)) {
                d = listadoBaseDuracionSesionIdealMinutos.get(i);
            }
        }

        return d;
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

    public void crearHojaObservacionesHorarios() {
        try {

            FileInputStream fis = new FileInputStream(RUTA_LIBRO_OFERTA);
            consolidadoOrigen = new XSSFWorkbook(fis);

            if (!hojaExiste(consolidadoOrigen, "Observaciones horarios")) {
                consolidadoOrigen.createSheet("Observaciones horarios");
            }

            XSSFSheet hojaObservaciones = consolidadoOrigen.getSheet("Observaciones horarios");
            int contadorFilasOrigen = 2;
            int contadorFilasDestino = 1;

            for (int i = 0; i < listadoObservacionesHorarios.size(); i++) {
                hojaObservaciones.createRow(i).createCell(0).setCellValue(listadoObservacionesHorarios.get(i));
            }

            File archivoExcel = new File(RUTA_LIBRO_OFERTA);
            FileOutputStream fos = new FileOutputStream(archivoExcel);
            consolidadoOrigen.write(fos);
            consolidadoOrigen.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hojaExiste(XSSFWorkbook libro, String nombreHoja) {
        boolean he = false;

        for (int i = 0; i < libro.getNumberOfSheets(); i++) {
            if (libro.getSheetName(i).equals(nombreHoja)) {
                he = true;
            }
        }

        return he;
    }

    public void asignacionDeSesiones() {
        System.out.println("Entro a programar sesiones 1");
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

            ArrayList<Date> fechasPosiblesJornada = new ArrayList<Date>();
            ArrayList<int[]> horasJornada = new ArrayList<int[]>();
            int semestreNumero = 0;

            String programa = listadoProgramas.get(i);
            String semestreCadena = listadoSemestres.get(i);
            int numeroCreditos = listadoCreditos.get(i);

            String alfaNumerico = listadoAlfa.get(i).trim() + "" + listadoNumerico.get(i).trim();

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
                    boolean cruceDia = getCruceDia(programa, listadoSemestres.get(i), listadoDocentes.get(i), listadoAsignaturas.get(i), horaCreada);

                    if (arregloSesiones.get(i).size() < numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
                        if (!cruceDocente && !cruceGrupo && !diaProhibido && !superaSalones && !cruceDia) {
                            //cumplidas estas condiciones se procede a asignar la fecha

                            arregloSesiones.get(i).add(horaCreada);
                        }
                    } else {
//                        System.out.println("Sesiones completas para i = " + i);
                    }

                    if (arregloSesiones.get(i).size() == numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
                        break;
                    }

                }

                if (arregloSesiones.get(i).size() == numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
                    break;
                }

            }

            if (arregloSesiones.get(i).size() < numeroSesionesIdeales(programa,  alfaNumerico, numeroCreditos)) {
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

        System.out.println("Programados: " + conteoProgramados + " No programados: " + conteoNoProgramados);

//        System.out.println("sesiones que no se ven");
//        for (int i = 0; i < arregloSesiones.get(604).size(); i++) {
//            System.out.println("programa " + listadoProgramas.get(604)
//                    + " semestre " + listadoSemestres.get(604)
//                    + " alfanumerico " + listadoAlfa.get(604)
//                    + " docente " + listadoDocentes.get(604)
//                    + " " + listadoNumerico.get(604) + arregloSesiones.get(604).get(i));
//        }
    }

    public void asignacionDeSesionesIndices() {
        System.out.println("Entro a programar sesiones indices");
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

        for (int ir = 0; ir < listadoProgramas.size(); ir++) {
            int i = indicesTodaOfertaDiferenteOrden.get(ir);

            ArrayList<Date> fechasPosiblesJornada = new ArrayList<Date>();
            ArrayList<int[]> horasJornada = new ArrayList<int[]>();
            int semestreNumero = 0;

            String programa = listadoProgramas.get(i);
            String semestreCadena = listadoSemestres.get(i);
            int numeroCreditos = listadoCreditos.get(i);

            String alfaNumerico = listadoAlfa.get(i).trim() + "" + listadoNumerico.get(i).trim();

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
                    boolean cruceDia = getCruceDia(programa, listadoSemestres.get(i), listadoDocentes.get(i), listadoAsignaturas.get(i), horaCreada);

                    if (arregloSesiones.get(i).size() < numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
                        if (!cruceDocente && !cruceGrupo && !diaProhibido && !superaSalones && !cruceDia) {
                            //cumplidas estas condiciones se procede a asignar la fecha

                            arregloSesiones.get(i).add(horaCreada);
                        }
                    } else {
//                        System.out.println("Sesiones completas para i = " + i);
                    }

                    if (arregloSesiones.get(i).size() == numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
                        break;
                    }

                }

                if (arregloSesiones.get(i).size() == numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
                    break;
                }

            }

            if (arregloSesiones.get(i).size() < numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
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

        System.out.println("Programados: " + conteoProgramados + " No programados: " + conteoNoProgramados);

//        System.out.println("sesiones que no se ven");
//        for (int i = 0; i < arregloSesiones.get(604).size(); i++) {
//            System.out.println("programa " + listadoProgramas.get(604)
//                    + " semestre " + listadoSemestres.get(604)
//                    + " alfanumerico " + listadoAlfa.get(604)
//                    + " docente " + listadoDocentes.get(604)
//                    + " " + listadoNumerico.get(604) + arregloSesiones.get(604).get(i));
//        }
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

            ArrayList<Date> fechasPosiblesJornada = new ArrayList<Date>();
            ArrayList<int[]> horasJornada = new ArrayList<int[]>();
            int semestreNumero = 0;

            String programa = listadoProgramas.get(i);
            String semestreCadena = listadoSemestres.get(i);
            int numeroCreditos = listadoCreditos.get(i);

            String jornada = listadoJornadas.get(i);
            String alfaNumerico = listadoAlfa.get(i).trim() + "" + listadoNumerico.get(i).trim();

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
                    boolean cruceDia = getCruceDia(programa, listadoSemestres.get(i), listadoDocentes.get(i), listadoAsignaturas.get(i), horaCreada);

                    if (arregloSesiones.get(i).size() < numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
                        if (!cruceDocente && !cruceGrupo && !diaProhibido && !superaSalones && !cruceDia) {

                            Date finalSesiones = sumarRestarSemanasFecha(horaCreada, numeroSemanasSumarFinalizarAsignatura(programa, alfaNumerico, numeroCreditos)/*numeroCreditos * 2 * 2 - 2*/);
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
                    if (arregloSesiones.get(i).size() == numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
                        break;
                    }

                }
                if (esCruzada(i)) {
                    numeroCreditos = mayorCreditosCruzadas(i);
                }
                if (arregloSesiones.get(i).size() == numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
                    break;
                }

            }

            if (arregloSesiones.get(i).size() < numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
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

        System.out.println("Programados: " + conteoProgramados + " No programados: " + conteoNoProgramados);
//        for (int i = 0; i < arregloSesiones.get(345).size(); i++) {
//            System.out.println("programa " + listadoProgramas.get(345)
//                    + " semestre " + listadoSemestres.get(345)
//                    + " alfanumerico " + listadoAlfa.get(345)
//                    + " docente " + listadoDocentes.get(345)
//                    + " " + listadoNumerico.get(345) + arregloSesiones.get(345).get(i));
//        }
//        for (int i = 0; i < arregloSesiones.get(346).size(); i++) {
//            System.out.println("programa " + listadoProgramas.get(346)
//                    + " semestre " + listadoSemestres.get(346)
//                    + " alfanumerico " + listadoAlfa.get(346)
//                    + " docente " + listadoDocentes.get(346)
//                    + " " + listadoNumerico.get(346) + arregloSesiones.get(346).get(i));
//        }
    }

    public void asignacionDeSesiones2indices() {
        System.out.println("Entro a programar sesiones 2 con indices");
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

        for (int ir = 0; ir < listadoProgramas.size(); ir++) {
            int i = indicesTodaOfertaDiferenteOrden.get(ir);

            ArrayList<Date> fechasPosiblesJornada = new ArrayList<Date>();
            ArrayList<int[]> horasJornada = new ArrayList<int[]>();
            int semestreNumero = 0;

            String programa = listadoProgramas.get(i);
            String semestreCadena = listadoSemestres.get(i);
            int numeroCreditos = listadoCreditos.get(i);

            String jornada = listadoJornadas.get(i);
            String alfaNumerico = listadoAlfa.get(i).trim() + "" + listadoNumerico.get(i).trim();

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
                    boolean cruceDia = getCruceDia(programa, listadoSemestres.get(i), listadoDocentes.get(i), listadoAsignaturas.get(i), horaCreada);

                    if (arregloSesiones.get(i).size() < numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
                        if (!cruceDocente && !cruceGrupo && !diaProhibido && !superaSalones && !cruceDia) {

                            Date finalSesiones = sumarRestarSemanasFecha(horaCreada, numeroSemanasSumarFinalizarAsignatura(programa, alfaNumerico, numeroCreditos)/*numeroCreditos * 2 * 2 - 2*/);
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
                    if (arregloSesiones.get(i).size() == numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
                        break;
                    }

                }
                if (esCruzada(i)) {
                    numeroCreditos = mayorCreditosCruzadas(i);
                }
                if (arregloSesiones.get(i).size() == numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
                    break;
                }

            }

            if (arregloSesiones.get(i).size() < numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
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

        System.out.println("Programados: " + conteoProgramados + " No programados: " + conteoNoProgramados);
//        for (int i = 0; i < arregloSesiones.get(345).size(); i++) {
//            System.out.println("programa " + listadoProgramas.get(345)
//                    + " semestre " + listadoSemestres.get(345)
//                    + " alfanumerico " + listadoAlfa.get(345)
//                    + " docente " + listadoDocentes.get(345)
//                    + " " + listadoNumerico.get(345) + arregloSesiones.get(345).get(i));
//        }
//        for (int i = 0; i < arregloSesiones.get(346).size(); i++) {
//            System.out.println("programa " + listadoProgramas.get(346)
//                    + " semestre " + listadoSemestres.get(346)
//                    + " alfanumerico " + listadoAlfa.get(346)
//                    + " docente " + listadoDocentes.get(346)
//                    + " " + listadoNumerico.get(346) + arregloSesiones.get(346).get(i));
//        }

    }

    public void asignacionDeSesionesNRCRepetidos() {
        int conteoProgramados = 0;
        int conteoNoProgramados = 0;

        if (arregloSesiones.size() == 0) {
            for (int i = 0; i < listadoProgramas.size(); i++) {
                arregloSesiones.add(new ArrayList<Date>());
            }
        }

        for (int m = 0; m < listadoCompartidosCruzadosRepetidos.size(); m++) {
            for (int n = 0; n < 1/*idsNRCRepetidos.get(m).size()*/; n++) {
                int id = idsCompartidosCruzadosRepetidos.get(m).get(n);
                int t = idsCompartidosCruzadosRepetidos.get(m).size();

                ArrayList<Date> fechasPosiblesJornada = new ArrayList<Date>();
                ArrayList<int[]> horasJornada = new ArrayList<int[]>();
                int semestreNumero = 0;

                String programa = listadoProgramas.get(id);
                String semestreCadena = listadoSemestres.get(id);
                int numeroCreditos = listadoCreditos.get(id);

                String alfaNumerico = listadoAlfa.get(id).trim() + "" + listadoNumerico.get(id).trim();

                String jornada = listadoJornadas.get(id);
                fechasPosiblesJornada = listadoFechasValidasAsignatura(jornada, programa, alfaNumerico);
                semestreNumero = semestreRomanoEntero((String) semestreGrupo(listadoSemestres.get(id))[0]);

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

                        boolean[] cruceDocente = new boolean[t];
                        boolean[] cruceGrupo = new boolean[t];
                        boolean[] diaProhibido = new boolean[t];
                        boolean[] superaSalones = new boolean[t];
                        boolean[] cruceDia = new boolean[t];
                        boolean hayCruce = false;

                        for (int l = 0; l < t; l++) {
                            int ids = idsCompartidosCruzadosRepetidos.get(m).get(l);
                            cruceDocente[l] = getCruceDocente(listadoDocentes.get(ids), semestreNumero, horaCreada, alfaNumerico);
                            cruceGrupo[l] = getCruceGrupo(listadoProgramas.get(ids), listadoSemestres.get(ids), semestreRomanoEntero((String) semestreGrupo(listadoSemestres.get(ids))[0]), horaCreada, alfaNumerico);
                            diaProhibido[l] = esDiaProhibido(horaCreada);
                            superaSalones[l] = superaMaximoSalones(horaCreada, jornada);
                            cruceDia[l] = getCruceDia(programa, listadoSemestres.get(ids), listadoDocentes.get(ids), listadoAsignaturas.get(ids), horaCreada);

                            if (cruceDocente[l] || cruceGrupo[l] || diaProhibido[l] || superaSalones[l] || cruceDia[l]) {
                                hayCruce = true;
                            }
                        }

                        if (arregloSesiones.get(id).size() < numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
                            if (!hayCruce) {

                                Date finalSesiones = sumarRestarSemanasFecha(horaCreada, numeroSemanasSumarFinalizarAsignatura(programa, alfaNumerico, numeroCreditos)/*numeroCreditos * 2 * 2 - 2*/);
                                Calendar c = Calendar.getInstance();
                                c.setTime(finalSesiones);

//                            System.out.println("final sesioens"+finalSesiones.toString() +" si empieza en: "+horaCreada.
                                Calendar c2 = Calendar.getInstance();
                                c2.setTime(FECHA_FINAL_SEMESTRE);
                                c2.add(Calendar.HOUR, 24);
//                            System.out.println("fecha final sesiones: " + c.getTime().toString());
//                            System.out.println("fecha final semestre: " + c2.getTime().toString());
//                            System.out.println("fecha final sesiones esta antes dle final del semestre: "+c.before(c2));

                                if (arregloSesiones.get(id).size() == 0 && c2.before(c)) {
                                    if (horasJornada.size() > 1) {
                                        break;//hora que sigue si es fines de semana, si es entre semana, se salta la fecha sin saltar la hora
                                    } else {

                                    }

                                } else {
                                    for (int l = 0; l < t; l++) {
                                        int ids = idsCompartidosCruzadosRepetidos.get(m).get(l);

                                        arregloSesiones.get(ids).add(horaCreada);
                                    }

                                }
                            }
                        } else {
                            System.out.println("Sesiones completas para i = " + id);
                        }

                        //el numero de créditos puede variar en dos materias que estén cruzadas
                        //se toma el número de créditos mayor entre dos materias cruzadas
                        if (esCruzada(id)) {
                            numeroCreditos = mayorCreditosCruzadas(id);
                        }
                        if (arregloSesiones.get(id).size() == numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
                            break;
                        }

                    }
                    if (esCruzada(id)) {
                        numeroCreditos = mayorCreditosCruzadas(id);
                    }
                    if (arregloSesiones.get(id).size() == numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
                        break;
                    }

                }

                if (arregloSesiones.get(id).size() < numeroSesionesIdeales(programa, alfaNumerico, numeroCreditos)) {
                    //System.out.println("No se pudieron encontrar todas las sesiones para i = " + i + " Total sesiones " + arregloSesiones.get(i).size() + " asignatura " + listadoAsignaturas.get(i) + " programa " + listadoProgramas.get(i) + " semestre: " + listadoSemestres.get(i)+" Creditos: "+numeroCreditos+" Docente: "+listadoDocentes.get(i));
                    for (int l = 0; l < arregloSesiones.get(id).size(); l++) {
                        //System.out.println("Sesion: " + l + " " + arregloSesiones.get(i).get(l).toString());
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

        System.out.println("Creando asignaciones para NRC repetidos");
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

    public void generarIndividuo() {
        arregloSesiones = new ArrayList<ArrayList<Date>>();
        for (int i = 0; i < listadoProgramas.size(); i++) {

            String jornada = listadoJornadas.get(i);
            int creditos = listadoCreditos.get(i);
            int semestre = semestreRomanoEntero((String) semestreGrupo(listadoSemestres.get(i))[0]);
            ArrayList<Date> listaFechasConHoraAleatoria = listadoFechasAleatoriasAsignaturaTipo1(jornada, creditos);
            ArrayList<Date> ald = posiblesHorasJornada(jornada, semestre, listaFechasConHoraAleatoria);

//            for (int j = 0; j < ald.size(); j++) {
            arregloSesiones.add(ald);
//            }

        }

    }

    public void crearListadoDepuradoDocentes() {
        //lista de los docentes sin repetir
        for (int i = 0; i < listadoDocentes.size(); i++) {
            String docente = listadoDocentes.get(i);
            if (!docente.trim().equals("")) {
                if (listadoDepuradoDocentes.indexOf(docente) == -1) {
                    listadoDepuradoDocentes.add(docente);
                }
            }
        }
        System.out.println("Listado depurado de docentes creado " + listadoDepuradoDocentes.size());
    }

    public void crearListadoDepuradoGrupos() {
        //listado de grupos sin repetir, un grupo se diferencia de otro
        //mediante la tupla (programa, semestre)
        listadoDepuradoGrupos = new ArrayList<String[]>();

        for (int i = 0; i < listadoProgramas.size(); i++) {
            String[] programasGrupo = new String[2];

            programasGrupo[0] = listadoProgramas.get(i);
            programasGrupo[1] = listadoSemestres.get(i);

            boolean existe = false;
            for (int j = 0; j < listadoDepuradoGrupos.size(); j++) {
                if (listadoDepuradoGrupos.get(j)[0].equals(programasGrupo[0]) && listadoDepuradoGrupos.get(j)[1].equals(programasGrupo[1])) {

                    existe = true;
                    break;

                }
            }

            if (!existe) {
//                System.out.println("entre");
                listadoDepuradoGrupos.add(programasGrupo);
            }

        }
    }

    public void crearlistadoCruzadosCompartidosRepetidos() {
        ArrayList<String> dlistadoCruzadosCompartidos = new ArrayList<String>(listadoCruzadosCompartidos);

        int contadorNRCRepetidos = 0;
        for (int i = 0; i < dlistadoCruzadosCompartidos.size(); i++) {
            String compartidoCruzado = dlistadoCruzadosCompartidos.get(i);
            if (!compartidoCruzado.trim().equals("")) {
                for (int j = 0; j < dlistadoCruzadosCompartidos.size(); j++) {
                    if (i != j) {
                        if (compartidoCruzado.equals(listadoCruzadosCompartidos.get(j))) {

                            if (listadoCompartidosCruzadosRepetidos.indexOf(compartidoCruzado) == -1) {

                                listadoCompartidosCruzadosRepetidos.add(compartidoCruzado);
                                idsCompartidosCruzadosRepetidos.add(new ArrayList<Integer>());
                                idsCompartidosCruzadosRepetidos.get(contadorNRCRepetidos).add(j);
                                contadorNRCRepetidos++;
                            } else {
                                int id = listadoCompartidosCruzadosRepetidos.indexOf(compartidoCruzado);

                                //arreglo temporal, evitar que entren j repetidas
                                if (idsCompartidosCruzadosRepetidos.get(id).indexOf(j) == -1) {
                                    idsCompartidosCruzadosRepetidos.get(id).add(j);
                                }

                            }
                        }
                    }
                }
            }

        }
//        System.out.println("Listado columnas cruzadoscompartidos: " + listadoCruzadosCompartidos.size());
//        System.out.println("Listado de NrC Repetidos creado: " + listadoCompartidosCruzadosRepetidos.size());

//        int i = 0;
//        for (String s : listadoCompartidosCruzadosRepetidos) {
//            System.out.println("compartidos cruzados: " + s + " ids" + idsCompartidosCruzadosRepetidos.get(i).toString()+","+listadoAsignaturas.get(idsCompartidosCruzadosRepetidos.get(i).get(0)) +" se repite "+cantidadVecesRepiteCruzadaCompartida(i)+ " tamaño directo"+idsCompartidosCruzadosRepetidos.get(i).size());
//            i++;
//        }
    }

//    public void crearListadoCompartidosCruzadosRepetidos() {
//        ArrayList<String> dListadoNRC = new ArrayList<String>(listadoCruzadosCompartidos);
//
//        int contadorNRCRepetidos = 0;
//        for (int i = 0; i < dListadoNRC.size(); i++) {
//            String compartidoCruzado = dListadoNRC.get(i);
//            for (int j = 0; j < dListadoNRC.size(); j++) {
//                if (i != j) {
//                    if (compartidoCruzado.equals(listadoCruzadosCompartidos.get(j))) {
//                        if (listadoCruzadosCompartidos.indexOf(compartidoCruzado) == -1) {
//                            if (!compartidoCruzado.equals("")) {
//                                //listadoCompartidasCruzadasRepetidas.add(nrc);
//                                idsCruzadosCompartidos.add(new ArrayList<String>());
//                                idsCruzadosCompartidos.get(contadorNRCRepetidos).add(j);
//                                contadorNRCRepetidos++;
//                            }
//
//                        } else {
//                            int id = listadoCruzadosCompartidos.indexOf(compartidoCruzado);
//                            idsCruzadosCompartidos.get(id).add(j);
//                        }
//                    }
//                }
//            }
//        }
//    }
    public void crearListadoCuposMayoresDepurado() {

        for (int i = 0; i < listadoDepuradoGrupos.size(); i++) {
            ArrayList<Integer> cuposGrupo = new ArrayList<Integer>();

            for (int j = 0; j < listadoProgramas.size(); j++) {
                String programaDepurado = listadoDepuradoGrupos.get(i)[0];
                String semestreDepurado = listadoDepuradoGrupos.get(i)[1];

                String programa = listadoProgramas.get(j);
                String semestre = listadoSemestres.get(j);

                if (programa.trim().equals(programaDepurado) && semestre.trim().equals(semestreDepurado)) {
                    cuposGrupo.add(listadoCupos.get(j));
                }
            }

            int cupoMayor = Collections.max(cuposGrupo);

            listadoCuposMayores.add(cupoMayor);
        }

    }

    public void crearListadoDepuradoColores() {
        int contadorColores = 0;
        for (int i = 0; i < listadoDepuradoGrupos.size(); i++) {
            int contadorFilas = 0;

            while (consolidadoOrigen.getSheetAt(0).getRow(contadorFilas + 2) != null)/* && !consolidadoOrigen.getSheetAt(0).getRow(contadorFilas+2).getCell(0).getStringCellValue().trim().equals(""))*/ {
                String programa = listadoDepuradoGrupos.get(i)[0];
                String semestre = listadoDepuradoGrupos.get(i)[1];

                if (consolidadoOrigen.getSheetAt(0).getRow(contadorFilas + 2).getCell(0) != null) {
                    String programaExcel = consolidadoOrigen.getSheetAt(0).getRow(contadorFilas + 2).getCell(0).getStringCellValue();
                    String semestreExcel = consolidadoOrigen.getSheetAt(0).getRow(contadorFilas + 2).getCell(1).getStringCellValue();

                    if (programaExcel.equals(programa) && semestreExcel.equals(semestre)) {
                        XSSFColor color = consolidadoOrigen.getSheetAt(0).getRow(contadorFilas + 2).getCell(1).getCellStyle().getFillBackgroundXSSFColor();

                        //copiamos el estilo y lo pegamos
                        CellStyle clonarEstilo = consolidadoOrigen.createCellStyle();
                        clonarEstilo.cloneStyleFrom(consolidadoOrigen.getSheetAt(0).getRow(contadorFilas + 2).getCell(0).getCellStyle());

                        listadoDepuradoColores.add(clonarEstilo);

                        break;
                    }

                }
                contadorFilas++;

            }
        }
        try {
            File archivoConsolidadoOrigen = new File("carpeta/CONSOLIDADO HORARIO 2017-15 Último2.xlsx");
            FileOutputStream os = new FileOutputStream(archivoConsolidadoOrigen);
            //consolidadoOrigen.getSheetAt(2).getRow(0).getCell(0).setCellValue("hola mundo");
            consolidadoOrigen.write(os);

            consolidadoOrigen.close();
        } catch (Exception e) {
        }

    }

    public ArrayList<Date> fechasPosibles() {
        ArrayList<Date> fP = new ArrayList<Date>();

        return fP;
    }

    public Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);

        return calendar.getTime();
    }

    public Date sumarRestarSemanasFecha(Date fecha, int semanas) {
        return sumarRestarDiasFecha(fecha, semanas * 7);
    }

    public Date fechaInicialSemestre() {

        return FECHA_INICIAL_SEMESTRE_SEMANA;
        //return consolidadoOrigen.getSheetAt(INDICE_HOJA_FECHAS).getRow(INDICE_FILA_FECHA_INICIAL).getCell(INDICE_COLUMNA_FECHA_INICIAL).getDateCellValue();
    }

    public Date fechaFinalSemestre() {
//        XSSFSheet hojaFechas = consolidadoOrigen.getSheetAt(INDICE_HOJA_FECHAS);
//        XSSFRow filaFechas = hojaFechas.getRow(INDICE_FILA_FECHA_INICIAL);
//        Date fechaFinal = null;
//
//        int contadorColumnas = 0;
//        while (filaFechas.getCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas) != null) {
//
//            if (filaFechas.getCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas).getCellType() != 3) {
//                fechaFinal = filaFechas.getCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas).getDateCellValue();
////                System.out.println("contador columnas " + (INDICE_COLUMNA_FECHA_INICIAL+contadorColumnas)+" tipo de celda "+filaFechas.getCell(INDICE_COLUMNA_FECHA_INICIAL).getCellType());
//                contadorColumnas++;
//            } else {
//                break;
//            }
//
//        }
        Date fechaFinal = new Date(2017, 12, 9);
        return FECHA_FINAL_SEMESTRE;
    }

    public Date ultimaFechaValidaJornada(String jornada) {
        Date ultimaFechaValida = null;
        if (jornada.equals("Mi y Vi")) {
            //obtenemos la ultima fecha válida
            Date ffs = fechaFinalSemestre();
            Date fa = null;
            //vamos retrocediendo uno por uno y apenas encontremos un martes o un viernes
            //tomamos esa fecha como fecha válida
            fa = ffs;
            SimpleDateFormat formatoDiaSemana = new SimpleDateFormat("u");

//            System.out.println("es dia prohibido? " + fa + " " + esDiaProhibido(fa));
            while (!formatoDiaSemana.format(fa).equals("3")
                    && !formatoDiaSemana.format(fa).equals("5")) {
                fa = sumarRestarDiasFecha(fa, -1);
                if (esDiaProhibido(fa)) {
                    fa = sumarRestarDiasFecha(fa, -1);
                }
            }
            ultimaFechaValida = fa;
        }
        if (jornada.equals("M y J")) {
            //obtenemos la ultima fecha válida
            Date ffs = fechaFinalSemestre();
            Date fa = null;
            //vamos retrocediendo uno por uno y apenas encontremos un martes o un viernes
            //tomamos esa fecha como fecha válida
            fa = ffs;
            SimpleDateFormat formatoDiaSemana = new SimpleDateFormat("u");

            while (!formatoDiaSemana.format(fa).equals("2") && !formatoDiaSemana.format(fa).equals("4")) {
                fa = sumarRestarDiasFecha(fa, -1);
                if (esDiaProhibido(fa)) {
                    fa = sumarRestarDiasFecha(fa, -1);
                }
            }
            ultimaFechaValida = fa;
        }
        if (jornada.equals("SÁBADO D")) {
            ultimaFechaValida = sumarRestarSemanasFecha(fechaFinalSemestre(), -1);
            while (esDiaProhibido(ultimaFechaValida)) {
                ultimaFechaValida = sumarRestarSemanasFecha(ultimaFechaValida, -1);
            }
        }
        if (jornada.equals("SÁBADO T")) {
            ultimaFechaValida = sumarRestarSemanasFecha(fechaFinalSemestre(), -1);
            while (esDiaProhibido(ultimaFechaValida)) {
                ultimaFechaValida = sumarRestarSemanasFecha(ultimaFechaValida, -1);
            }
        }
        if (jornada.equals("SÁBADO D2")) {
            ultimaFechaValida = fechaFinalSemestre();
            while (esDiaProhibido(ultimaFechaValida)) {
                ultimaFechaValida = sumarRestarSemanasFecha(ultimaFechaValida, -1);
            }
        }
        if (jornada.equals("SÁBADO T2")) {
            ultimaFechaValida = fechaFinalSemestre();
            while (esDiaProhibido(ultimaFechaValida)) {
                ultimaFechaValida = sumarRestarSemanasFecha(ultimaFechaValida, -1);
            }
        }
        if (jornada.equals("SÁBADO")) {
            ultimaFechaValida = sumarRestarSemanasFecha(fechaFinalSemestre(), -1);
            while (esDiaProhibido(ultimaFechaValida)) {
                ultimaFechaValida = sumarRestarSemanasFecha(ultimaFechaValida, -1);
            }
        }

        return ultimaFechaValida;
    }

    public Date fechaMaximaIniciarAsignatura(String jornada, int numeroCreditos) {
        Date ultimaFechaValida = null;
        ultimaFechaValida = sumarRestarSemanasFecha(ultimaFechaValidaJornada(jornada), -numeroCreditos * 2 * 2 + 2);

        return ultimaFechaValida;
    }

    public void setDiasProhibidos(Date fechaProhibida) {
        DIAS_PROHIBIDOS.add(fechaProhibida);
    }

    public Date getDiasProhibidos(int indice) {
        return DIAS_PROHIBIDOS.get(indice);
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

    public void eliminarDiaProhibido(Date fecha) {
        DIAS_PROHIBIDOS.remove(fecha);
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
    }

    public Date getFechaInicialJornada(String jornada) {
        Date fecha = null;

        if (jornada.equals("M y J")) {
            fecha = FECHA_INICIAL_JORNADA_MYJ;
        }
        if (jornada.equals("Mi y Vi")) {
            fecha = FECHA_INICIAL_JORNADA_MIYVI;
        }
        if (jornada.equals("SÁBADO D")) {
            fecha = FECHA_INICIAL_JORNADA_SABADOD1;
        }
        if (jornada.equals("SÁBADO D2")) {
            fecha = FECHA_INICIAL_JORNADA_SABADOD2;
        }
        if (jornada.equals("SÁBADO T")) {
            fecha = FECHA_INICIAL_JORNADA_SABADOT1;
        }
        if (jornada.equals("SÁBADO T2")) {
            fecha = FECHA_INICIAL_JORNADA_SABADOT2;
        }
        if (jornada.equals("SÁBADO")) {
            fecha = FECHA_INICIAL_JORNADA_SABADO;
        }

        return fecha;
    }

    public void setFechasIniciales(Date fecha1, Date fecha2,
            Date fecha3, Date fecha4,
            Date fecha5, Date fecha6,
            Date fecha7, Date fecha8, Date fecha9, Date fecha10) {

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

    }

    public ArrayList<Date> listadoFechasValidasIniciarAsignatura(String jornada, int numeroCreditos) {
        ArrayList<Date> listadoFechas = new ArrayList<Date>();

        Date fa = fechaMaximaIniciarAsignatura(jornada, numeroCreditos);
        SimpleDateFormat formatoNumeroDia = new SimpleDateFormat("u");

//        System.out.println("Dentro de: " + jornada.equals("SÁBADO T1"));
        if (jornada.equals("M y J")) {
            while (fa.compareTo(FECHA_INICIAL_JORNADA_MYJ) >= 0) {
                if (formatoNumeroDia.format(fa).equals("2") || formatoNumeroDia.format(fa).equals("4")) {
                    if (!esDiaProhibido(fa)) {
                        listadoFechas.add(fa);
                    }
                }
                fa = sumarRestarDiasFecha(fa, -1);
            }
        }
        if (jornada.equals("Mi y Vi")) {
            while (fa.compareTo(FECHA_INICIAL_JORNADA_MIYVI) >= 0) {
                if (formatoNumeroDia.format(fa).equals("3") || formatoNumeroDia.format(fa).equals("5")) {
                    if (!esDiaProhibido(fa)) {
                        listadoFechas.add(fa);
                    }
                }
                fa = sumarRestarDiasFecha(fa, -1);
            }
        }
        if (jornada.equals("SÁBADO D")) {
            while (fa.compareTo(FECHA_INICIAL_JORNADA_SABADOD1) >= 0) {
                if (!esDiaProhibido(fa)) {
                    listadoFechas.add(fa);
                }
                fa = sumarRestarSemanasFecha(fa, -2);
            }
        }
        if (jornada.equals("SÁBADO D2")) {
            while (fa.compareTo(FECHA_INICIAL_JORNADA_SABADOD2) >= 0) {
                if (!esDiaProhibido(fa)) {
                    listadoFechas.add(fa);
                }
                fa = sumarRestarSemanasFecha(fa, -2);
            }
        }
        if (jornada.equals("SÁBADO T")) {
            while (fa.compareTo(FECHA_INICIAL_JORNADA_SABADOT1) >= 0) {
                if (!esDiaProhibido(fa)) {
                    listadoFechas.add(fa);
                }
                fa = sumarRestarSemanasFecha(fa, -2);
            }
        }
        if (jornada.equals("SÁBADO T2")) {
            while (fa.compareTo(FECHA_INICIAL_JORNADA_SABADOT2) >= 0) {
                if (!esDiaProhibido(fa)) {
                    listadoFechas.add(fa);
                }
                fa = sumarRestarSemanasFecha(fa, -2);
            }
        }
        if (jornada.equals("SÁBADO")) {
            while (fa.compareTo(FECHA_INICIAL_JORNADA_SABADO) >= 0) {
                if (!esDiaProhibido(fa)) {
                    listadoFechas.add(fa);
                }
                fa = sumarRestarSemanasFecha(fa, -2);
            }
        }

        return listadoFechas;
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
                if (alfaNumerico.equals("FHUM1100")) {

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

                    if (alfaNumerico.equals("LENG1010")
                            || alfaNumerico.equals("UVCEUV061")
                            || alfaNumerico.equals("UVCEUV193")) {

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
                if (alfaNumerico.equals("FHUM1100")) {
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
            if (periodicidadDiasAsignatura(programa, alfaNumerico) == 7) {
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
        
        if(jornada.equals("SÁBADO D") && programa.equals("AEMD") && alfaNumerico.equals("UVCEUV081")){
            System.out.println("listado fechas para: "+jornada+" "+programa+" "+alfaNumerico+" "+listadoFechas);
        }

        return listadoFechas;
    }

    public Date fechaAleatoriaInicialInicioAsignatura(String jornada, int numeroCreditos) {
        Date fechaaleatoriainicial = null;
        ArrayList<Date> listadoFechas = listadoFechasValidasIniciarAsignatura(jornada, numeroCreditos);
        int tamano = listadoFechas.size();

        fechaaleatoriainicial = listadoFechas.get(randInt(0, tamano - 1));

        return fechaaleatoriainicial;
    }

    public ArrayList<Date> listadoFechasAleatoriasAsignaturaTipo1(String jornada, int numeroCreditos) {
        //tipo 1, quiere decir, par una fecha dada 2*numeroCreditos
        //seria de tipo ideal porque sería todas las sesiones cada 15 días
        ArrayList<Date> listadofechas = new ArrayList<Date>();

        Date fechaAleatoriaInicial = fechaAleatoriaInicialInicioAsignatura(jornada, numeroCreditos);
        listadofechas.add(fechaAleatoriaInicial);

        for (int i = 1; i < numeroCreditos * 2; i++) {
            listadofechas.add(sumarRestarSemanasFecha(fechaAleatoriaInicial, 2 * i));
        }

        return listadofechas;
    }

    public int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
//    Random rand;
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
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
     *
     * @param jornada
     * @param semestre
     * @param listadoFechasAleatoriasTipo1
     * @return Devuelve un ArrayList<Date> modificado con la hora aleatoria
     * seleccionada en posibleHoraInicioClase
     *
     */
    public ArrayList<Date> posiblesHorasJornada(String jornada, int semestre, ArrayList<Date> listadoFechasAleatoriasTipo1) {
        ArrayList<Date> listadoFechasAleatoriosConHora = new ArrayList<Date>();
        ArrayList<Calendar> listadoFechasAleatoriosConHoraCalendario = new ArrayList<Calendar>();

        Calendar fechaHoraAsignada = Calendar.getInstance();
        Calendar horaAleatoriaAuxiliar = Calendar.getInstance();

        int[] horaAleatoria = posibleHoraInicioClase(jornada, semestre);

        //acá se deben elegir las horas adecuadas dependiente de la jornada
        for (int i = 0; i < listadoFechasAleatoriasTipo1.size(); i++) {
            fechaHoraAsignada.setTime(listadoFechasAleatoriasTipo1.get(i));
            fechaHoraAsignada.set(Calendar.HOUR_OF_DAY, horaAleatoria[0]);
            fechaHoraAsignada.set(Calendar.MINUTE, horaAleatoria[1]);
            listadoFechasAleatoriosConHora.add(fechaHoraAsignada.getTime());
        }

        return listadoFechasAleatoriosConHora;
    }

    /**
     *
     * @param jornada
     * @param semestre
     * @return Arreglo de dos enteros. El primer índice con la hora, y el
     * segundo con los minutos
     */
    public int[] posibleHoraInicioClase(String jornada, int semestre) {
        int horaAleatoria = 0;
        int[] horasMinutos = new int[2];
        horasMinutos[0] = 0;
        horasMinutos[1] = 0;

        if (jornada.equals("M y J")) {
            horasMinutos[0] = 7;
            horasMinutos[1] = 0;
        }
        if (jornada.equals("Mi y Vi")) {
            horasMinutos[0] = 7;
            horasMinutos[1] = 0;
        }
        if (jornada.equals("SÁBADO")) {
            //aún no definidos. observación, la especialización es cada 15 días también
        }
        if (jornada.equals("SÁBADO D") || jornada.equals("SÁBADO D2")) {
            int unidadMinutos = 60;

            if (semestre <= SEMESTRE_CLASES_CORTAS) {
                int aleatorio = randInt(0, 3);
                if (aleatorio == 0) {
                    horasMinutos[0] = 7;
                    horasMinutos[1] = 0;
                }
                if (aleatorio == 1) {
                    horasMinutos[0] = 8;
                    horasMinutos[1] = 30;
                }
                if (aleatorio == 2) {
                    horasMinutos[0] = 10;
                    horasMinutos[1] = 0;
                }
                if (aleatorio == 3) {
                    horasMinutos[0] = 11;
                    horasMinutos[1] = 30;
                }
            } else {
                int aleatorio = randInt(0, 2);
                if (aleatorio == 0) {
                    horasMinutos[0] = 7;
                    horasMinutos[1] = 0;
                }
                if (aleatorio == 1) {
                    horasMinutos[0] = 9;
                    horasMinutos[1] = 0;
                }
                if (aleatorio == 2) {
                    horasMinutos[0] = 11;
                    horasMinutos[1] = 0;
                }
            }
        }
        if (jornada.equals("SÁBADO T") || jornada.equals("SÁBADO T2")) {

            if (semestre <= SEMESTRE_CLASES_CORTAS) {
                //clases con duración de bloques de 45 minutos
                int aleatorio = randInt(0, 1);
                if (aleatorio == 0) {
                    aleatorio = randInt(0, 3);

                    if (aleatorio == 0) {
                        horasMinutos[0] = 13;
                        horasMinutos[1] = 0;
                    }
                    if (aleatorio == 1) {
                        horasMinutos[0] = 14;
                        horasMinutos[1] = 30;
                    }
                    if (aleatorio == 2) {
                        horasMinutos[0] = 16;
                        horasMinutos[1] = 0;
                    }
                    if (aleatorio == 3) {
                        horasMinutos[0] = 17;
                        horasMinutos[1] = 30;
                    }

                } else {
                    aleatorio = randInt(0, 3);

                    if (aleatorio == 0) {
                        horasMinutos[0] = 14;
                        horasMinutos[1] = 0;
                    }
                    if (aleatorio == 1) {
                        horasMinutos[0] = 15;
                        horasMinutos[1] = 30;
                    }
                    if (aleatorio == 2) {
                        horasMinutos[0] = 17;
                        horasMinutos[1] = 0;
                    }
                    if (aleatorio == 3) {
                        horasMinutos[0] = 18;
                        horasMinutos[1] = 30;
                    }
                }

            } else {
                //clases con duración de 60 minutos
                int aleatorio = randInt(0, 1);
                if (aleatorio == 0) {
                    aleatorio = randInt(0, 3);

                    if (aleatorio == 0) {
                        horasMinutos[0] = 13;
                        horasMinutos[1] = 0;
                    }
                    if (aleatorio == 1) {
                        horasMinutos[0] = 15;
                        horasMinutos[1] = 0;
                    }
                    if (aleatorio == 2) {
                        horasMinutos[0] = 17;
                        horasMinutos[1] = 0;
                    }

                } else {
                    aleatorio = randInt(0, 2);

                    if (aleatorio == 0) {
                        horasMinutos[0] = 14;
                        horasMinutos[1] = 0;
                    }
                    if (aleatorio == 1) {
                        horasMinutos[0] = 16;
                        horasMinutos[1] = 0;
                    }
                    if (aleatorio == 2) {
                        horasMinutos[0] = 18;
                        horasMinutos[1] = 0;
                    }
                }
            }
        }
        return horasMinutos;
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
            if (alfaNumerico.equals("LENG1010")) {
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

    public int periodicidadSemanas(String programa, String alfaNumerico) {
        int p = 0;

        return p;
    }

    public boolean getCruceDocente(String docente,
            int semestre,
            Date fechaAAsignar,
            String alfaNumerico) {
        boolean cruce = false;

        for (int i = 0; i < listadoProgramas.size(); i++) {
            if (listadoDocentes.get(i).trim().equals(docente.trim())) {
                alfaNumerico = listadoAlfa.get(i).trim() + "" + listadoNumerico.get(i).trim();

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
                                d2 = duracionAsignatura(listadoProgramas.get(i), listadoAlfa.get(i) + "" + listadoNumerico.get(i));
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
                                d2 = duracionAsignatura(listadoProgramas.get(i), listadoAlfa.get(i) + "" + listadoNumerico.get(i));
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

    public boolean getCruceDia(String programa, String grupo, String docente, String asignatura, Date fechaAAsginar) {
        boolean sonElmismoDia = false;

        if (!PERMITIRCRUCEDIA) {
            for (int i = 0; i < listadoProgramas.size(); i++) {
                String p, g, d, a;

                if (arregloSesiones.size() != 0) {
                    p = listadoProgramas.get(i).trim();
                    g = listadoSemestres.get(i).trim();
                    d = listadoDocentes.get(i).trim();
                    a = listadoAsignaturas.get(i).trim();

                    if (p.equals(programa) && g.equals(grupo) && d.equals(docente) && a.equals(asignatura)) {
                        ArrayList<Date> sesiones = arregloSesiones.get(i);

                        for (Date s : sesiones) {
                            if (sonElMismoDia(fechaAAsginar, s)) {
                                return true;
                            }
                        }
                    }
                }

            }
        }

        return sonElmismoDia;
    }

    public boolean sonElMismoDia(Date f1, Date f2) {
        boolean mD = false;

        Calendar c1 = Calendar.getInstance();
        c1.setTime(f1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(f2);

        //no se considera necesario comparar años, pues siempre serán igual
        if (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
            mD = true;
        }

        return mD;
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
//el individuo está conformado por cromosomas
    //los cromososmas están formados por genes
    //cada gen está formado por alelos
    //los alelos son grupo (semestre) carrera nrc docente fecha y hora

    public boolean esVirtual(String alfaNumerico) {
        boolean eV = false;

        for (int i = 0; i < listadoBaseAsginaturasSemana.size(); i++) {

            if ((listadoBaseAsginaturasAlfa.get(i).trim() + listadoBaseAsginaturasNumerico.get(i).trim()).equals(alfaNumerico)) {
                if (listadoBaseAsginaturasVirtualNoVirtual.get(i) == 1) {
                    return true;
                }
            }
        }
        return eV;
    }

    public Date horaAleatoria(String jornada, int semestre) {
        Date hora = null;

        SimpleDateFormat fHora24 = new SimpleDateFormat("hh:mm");

        fHora24.format(hora);

        return hora;
    }

    public void agregarSesiones() {

    }

    public void agregarSesion(int indiceFila, Date fecha) {
        String jornada = listadoJornadas.get(indiceFila);

    }

    public void reemplazarSesion() {
    }

    public void quitarSesion() {
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

    public void imprimirSesionesDocente() {
        System.out.println("Sesiones docentes -----------------------------------");
        for (int i = 0; i < listadoDepuradoDocentes.size(); i++) {
            String docente = listadoDepuradoDocentes.get(i);
            int cS = 1;

            for (int j = 0; j < listadoProgramas.size(); j++) {
                if (docente.equals(listadoDocentes.get(j))) {
                    for (int k = 0; k < arregloSesiones.get(j).size(); k++) {
                        System.out.println("Docente " + listadoDepuradoDocentes.get(i) + " Sesión " + cS + " Semestre: " + listadoAsignaturas.get(j) + " Programa " + listadoProgramas.get(j) + " Semestre " + listadoSemestres.get(j) + " " + arregloSesiones.get(j).get(k));
                        cS++;
                    }
                }
            }
        }
    }

    public void limpiarRegionHoras() {
        try {
            FileInputStream fis = new FileInputStream(RUTA_LIBRO_OFERTA);
//            XSSFWorkbook libroOrigen = consolidadoOrigen;
            XSSFWorkbook libroOrigen = new XSSFWorkbook(fis);
            XSSFSheet hojaOferta = libroOrigen.getSheet("Oferta educativa");

            int contadorFilas = INDICE_FILA_INICIAN_GRUPOS;
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

    public void imprimirSesionesHojaCalculo() {
        System.out.println("Se van a imprimir las sesiones en la hoja de cálculo");
        try {
            FileInputStream fis = new FileInputStream(RUTA_LIBRO_OFERTA);
            XSSFWorkbook libroOrigen = new XSSFWorkbook(fis);
            XSSFSheet hojaOferta = libroOrigen.getSheet("Oferta educativa");

            for (int i = 0; i < arregloSesiones.size(); i++) {

                if (i == 701) {
                    i = i;
                }
                String programa = listadoProgramas.get(i);
                String grupo = listadoSemestres.get(i);
                String asignatura = listadoAsignaturas.get(i);
                String docente = listadoDocentes.get(i);
                String nrc = (String) "" + listadoNRC.get(i);

                int contadorFilasGrupo = 0;
                XSSFRow filaGrupo = hojaOferta.getRow(INDICE_FILA_INICIAN_GRUPOS + contadorFilasGrupo);
                boolean encontrado = false;
                while (!celdaVacia(filaGrupo, 0)) {
                    if (filaGrupo.getCell(COLUMNA_PROGRAMA).getStringCellValue().trim().equals(programa)
                            && filaGrupo.getCell(COLUMNA_SEMESTRE).getStringCellValue().trim().equals(grupo)
                            && filaGrupo.getCell(COLUMNA_ASIGNATURA).getStringCellValue().trim().equals(asignatura)
                            && filaGrupo.getCell(COLUMNA_DOCENTE).getStringCellValue().trim().equals(docente)
                            && ((String) "" + retornarValor(filaGrupo.getCell(COLUMNA_NRC))).trim().equals(nrc)) {
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
                                    //si la celda no está vacia (ya hay una fecha ahí, sobre todo para el caso de cruces de días)
                                    if(!celdaVacia(celdaAsignar.getRow(), celdaAsignar.getColumnIndex())){
                                        
                                        //la celda tiene fecha
                                        if(celdaTieneFecha(celdaAsignar)){
                                            
                                            Date f = retornarFechaHoraSesion(celdaAsignar);
                                            celdaAsignar.setCellValue(convertirDateACadenaHorasMinutosDosPuntos(f)+"/"+convertirDateACadenaHorasMinutosDosPuntos(fechaSesion));
                                            
                                        }else{
                                            celdaAsignar.setCellValue(celdaAsignar.getStringCellValue()+"/"+convertirDateACadenaHorasMinutosDosPuntos(fechaSesion));
                                        }
                                        
                                    }else{
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

//            FileOutputStream fos = new FileOutputStream(RUTA_LIBRO_OFERTA);
//            libroOrigen.write(fos);
//            libroOrigen.close();
            
            //creamos el archivo con hora de salida
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
            Date fechaYHoraSalida = new Date();
            String fechaSalidaFormateada = sdf.format(fechaYHoraSalida);

            String rutaLibroSalidaHorarios = (new File(RUTA_LIBRO_OFERTA).getParent()) + "/horarios" + fechaSalidaFormateada.toString() + ".xlsx";
            FileOutputStream fosSalidaHorarios = new FileOutputStream(rutaLibroSalidaHorarios);
            libroOrigen.write(fosSalidaHorarios);

            fosSalidaHorarios.close();
            libroOrigen.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Se imprimieron las sesiones en la hoja de cálculo");
    }

    public void revisarSesionesGenerarObservaciones() {

        for (int i = 0; i < listadoProgramas.size(); i++) {

            int c = listadoCreditos.get(i);
            String programa = listadoProgramas.get(i);
            String semestre = listadoSemestres.get(i);
            String asignatura = listadoAsignaturas.get(i);
            ArrayList<Date> listadoSesiones = new ArrayList<Date>(arregloSesiones.get(i));
            int tamano = listadoSesiones.size();

            if (numeroSesionesIdeales(programa, listadoAlfa.get(i) + "" + listadoNumerico.get(i), c) > arregloSesiones.get(i).size()) {

                String cadena = "Programa: " + listadoProgramas.get(i) + ",Asignatura: " + listadoAsignaturas.get(i) + ",Grupo " + listadoSemestres.get(i) + ",Sesiones programadas: " + arregloSesiones.get(i).size() + ",Sesiones a programar: " + numeroSesionesIdeales(programa, listadoAlfa.get(i) + "" + listadoNumerico.get(i), c) + ",Jornada " + listadoJornadas.get(i) + ",Docente: " + listadoDocentes.get(i);
                listadoObservacionesHorarios.add(cadena);
                System.out.println(cadena);
            }
        }

    }

    public void revisarSesionesObtenerNumeroNoProgramados() {
        NOPROGRAMADOS = 0;
        for (int i = 0; i < listadoProgramas.size(); i++) {

            int c = listadoCreditos.get(i);
            String programa = listadoProgramas.get(i);
            String semestre = listadoSemestres.get(i);
            String asignatura = listadoAsignaturas.get(i);
            ArrayList<Date> listadoSesiones = new ArrayList<Date>(arregloSesiones.get(i));
            int tamano = listadoSesiones.size();

            if (numeroSesionesIdeales(programa,listadoAlfa.get(i) + "" + listadoNumerico.get(i), c) > arregloSesiones.get(i).size()) {
                NOPROGRAMADOS = NOPROGRAMADOS + 1;
            }
        }
        System.out.println("Sesiones revisadas Programados" + (listadoProgramas.size() - NOPROGRAMADOS) + " No programados: " + NOPROGRAMADOS);
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

    public void imprimirSesionesGrupo() {
        System.out.println("Sesiones grupos -----------------------------------");
        try {
            for (int i = 0; i < listadoDepuradoGrupos.size(); i++) {
                String programa = listadoDepuradoGrupos.get(i)[0];
                String semestre = listadoDepuradoGrupos.get(i)[1];

                for (int j = 0; j < listadoProgramas.size(); j++) {
                    if (programa.equals(listadoProgramas.get(j)) && semestre.equals(listadoSemestres.get(j))) {
                        for (int k = 0; k < arregloSesiones.get(j).size(); k++) {

                            System.out.println("Grupo: " + listadoDepuradoGrupos.get(i)[0] + " " + listadoDepuradoGrupos.get(i)[1] + " Sesión " + k + " Semestre: " + listadoAsignaturas.get(j) + " Programa " + listadoProgramas.get(j) + " Semestre " + listadoSemestres.get(j) + " " + arregloSesiones.get(j).get(k));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void imprimirDatosHojaExcel() {
        System.out.println("Listado excel -----------------------------------");
        for (int i = 0; i < listadoProgramas.size(); i++) {
            System.out.println("Programa: " + listadoProgramas.get(i) + " Semestre: " + listadoSemestres.get(i) + " Asignatura" + listadoAsignaturas.get(i) + " Docente: " + listadoDocentes.get(i) + " Cantidad de sesiones programadas: " + arregloSesiones.get(i).size());
        }
    }

    public void imprimirSesionesSegunOrdenExcel() {
        System.out.println("Listado sesiones segun excel -----------------------------");
        for (int i = 0; i < listadoProgramas.size(); i++) {
            for (int j = 0; j < arregloSesiones.get(i).size(); j++) {
                System.out.println(i + " Programa: " + listadoProgramas.get(i) + " Semestre: " + listadoSemestres.get(i) + " Asignatura" + listadoAsignaturas.get(i) + " Docente: " + listadoDocentes.get(i) + " Cantidad de sesiones programadas: " + arregloSesiones.get(i).size() + " Sesion: " + j + " " + arregloSesiones.get(i).get(j));
            }

        }
    }

    public void imprimirNRCRepetidos() {
        System.out.println("Listado nrc repetidos-----------------------------");
        for (int i = 0; i < listadorNRCRepetidos.size(); i++) {
            System.out.println("NRC Repetido: " + listadorNRCRepetidos.get(i) + " Cantidad de veces repetido " + idsCompartidosCruzadosRepetidos.get(i).size());
        }
    }

    public void imprimirNRCRepetidosConIds() {
        System.out.println("Listado nrc repetidos con ids-----------------------------");
        for (int i = 0; i < listadorNRCRepetidos.size(); i++) {
            for (int j = 0; j < idsCompartidosCruzadosRepetidos.get(i).size(); j++) {

                System.out.println("NRC Repetido: " + listadorNRCRepetidos.get(i) + " " + idsCompartidosCruzadosRepetidos.get(i).get(j));

            }

        }
    }

    public boolean cruceDocente(String docente, Date fecha) {
        boolean cD = false;
        return cD;
    }

    public boolean cruceGrupo(String programa, String semestre, Date fecha) {
        boolean cG = false;
        return cG;
    }

    public void crearDiasYFechasHojaDestino() {
        System.out.println("Creando fechas hoja destino");
        //aca se colocan todas las fechas del semestre
        try {
            Date fa = FECHA_INICIAL_SEMESTRE_SEMANA;//fechaMaximaIniciarAsignatura(jornada, numeroCreditos);
            SimpleDateFormat formatoNumeroDia = new SimpleDateFormat("u");

            SimpleDateFormat formatoDiaEspañol = new SimpleDateFormat("EEEE");

            SimpleDateFormat formatoMesDia = new SimpleDateFormat("MMM-d");

            FileInputStream fis = new FileInputStream(RUTA_LIBRO_OFERTA);
            XSSFWorkbook libroDestino = new XSSFWorkbook(fis);

            XSSFSheet hojaDestino = libroDestino.getSheet("Oferta educativa");
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

            fa = FECHA_INICIAL_SEMESTRE_SABADOS;
            while (fa.compareTo(fechaFinalSemestre()) <= 0) {
                if (formatoNumeroDia.format(fa).equals("6")) {

                    XSSFCell celda = filaNombresDias.getCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas);

                    if (celda == null) {
                        celda = filaNombresDias.createCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas);
                    }

                    celda.setCellValue(formatoDiaEspañol.format(fa));

                    XSSFCell celdaFechas = filaFechas.getCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas);

                    if (celdaFechas == null) {
                        celdaFechas = filaFechas.createCell(INDICE_COLUMNA_FECHA_INICIAL + contadorColumnas);
                    }

                    CreationHelper createHelper = libroDestino.getCreationHelper();
                    CellStyle estiloHora = libroDestino.createCellStyle();
                    estiloHora.setDataFormat(createHelper.createDataFormat().getFormat("d-mmm"));
                    celdaFechas.setCellValue(fa);
                    celdaFechas.setCellStyle(estiloHora);

                    //System.out.println("fa: " + fa + " " + formatoDiaEspañol.format(fa) + " mes dia " + formatoMesDia.format(fa));
                    contadorColumnas++;

                }

                fa = sumarRestarDiasFecha(fa, 7);
            }

            FileOutputStream fos = new FileOutputStream(RUTA_LIBRO_OFERTA);
            libroDestino.write(fos);
            libroDestino.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public boolean esCruzadaDocente(int i) {
        boolean cruzada = false;

        if (esCruzada(i)) {
            ArrayList<Integer> listado = listadoIndicesCruzadosCompartidosIndiceDado(i);
            String docente = listadoDocentes.get(i);

            for (int j = 0; j < listado.size(); j++) {
                if (docente.trim().equals(listadoDocentes.get(listado.get(j)))) {
                    if (i != listado.get(j)) {
                        return true;
                    }
                }
            }
        }

        return cruzada;
    }

    public int cantidadVecesRepiteCruzadaCompartida(int i) {
        int cantidad = 0;

        for (int j = 0; j < idsCompartidosCruzadosRepetidos.size(); j++) {
            for (int k = 0; k < idsCompartidosCruzadosRepetidos.get(j).size(); k++) {
                if (i == idsCompartidosCruzadosRepetidos.get(j).get(k)) {
                    cantidad = idsCompartidosCruzadosRepetidos.get(j).size();
                    return cantidad;
                }
            }
        }

        return cantidad;
    }

    public ArrayList<Integer> listadoIndicesCruzadosCompartidosIndiceDado(int i) {
        ArrayList<Integer> listado = new ArrayList<Integer>();

        for (int j = 0; j < idsCompartidosCruzadosRepetidos.size(); j++) {
            for (int k = 0; k < idsCompartidosCruzadosRepetidos.get(j).size(); k++) {
                if (i == idsCompartidosCruzadosRepetidos.get(j).get(k)) {
                    listado = idsCompartidosCruzadosRepetidos.get(j);
                    return listado;
                }
            }
        }

        return listado;
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

    public XSSFRow filaPosteriorFinalArchivoOrigen() {
        XSSFRow filafinal = null;

        try {
            XSSFSheet hojaOferta = consolidadoOrigen.getSheet("Oferta educativa");

            int contadorFilas = INDICE_FILA_INICIAN_GRUPOS;
            XSSFRow filaContadora = hojaOferta.getRow(contadorFilas);

            while (!celdaVacia(filaContadora, 0)) {
                contadorFilas++;
                filaContadora = hojaOferta.getRow(contadorFilas);
            }

            filafinal = hojaOferta.getRow(contadorFilas + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filafinal;
    }

    public void limpiarFilaFinal(XSSFRow filaFinal) {
        //el contador de columnas se hace con respecto a los nombres de los días
        try {

            FileInputStream fis = new FileInputStream(RUTA_LIBRO_OFERTA);
            XSSFWorkbook libroOferta = new XSSFWorkbook(fis);
            XSSFSheet hojaOferta = libroOferta.getSheet("Oferta educativa");

            XSSFRow filaDias = hojaOferta.getRow(INDICE_FILAS_NOMBRES_DIAS);
            int contadorColumnas = INDICE_COLUMNA_FECHA_INICIAL;

            while (!celdaVacia(filaDias, contadorColumnas)) {
                if (!celdaVacia(filaFinal, contadorColumnas)) {
                    filaFinal.getCell(contadorColumnas).setCellValue("");
                }
                contadorColumnas++;
            }

            FileOutputStream fos = new FileOutputStream(RUTA_LIBRO_OFERTA);
            libroOferta.write(fos);
            libroOferta.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int conteoFechasColumna(int columna) {
        int c = 0;
        int cr = 0;
        try {
            //celdas contadas para el libro de oferta educatia
            FileInputStream fis = new FileInputStream(RUTA_LIBRO_OFERTA);
            XSSFWorkbook libroOferta = new XSSFWorkbook(fis);
            XSSFSheet hojaOferta = libroOferta.getSheet("Oferta educativa");

            int contadorFilas = INDICE_FILA_INICIAN_GRUPOS;
            XSSFRow filaContador = hojaOferta.getRow(contadorFilas);

            while (!celdaVacia(filaContador, 0)) {

                if (!celdaVacia(filaContador, columna)) {
                    c++;
                    if (!celdaVacia(filaContador, INDICE_COLUMNA_CRUZADA_COMPARTIDA)) {
                        cr++;
                    }
                }

                contadorFilas++;
                filaContador = hojaOferta.getRow(contadorFilas);
            }

            XSSFRow filaFinalMAsuno = filaPosteriorFinalArchivoOrigen();
            int indice = filaFinalMAsuno.getRowNum();

            XSSFRow filaFinalMasUno = hojaOferta.getRow(indice);

            if (filaFinalMasUno.getCell(columna) == null) {
                filaFinalMasUno.createCell(columna).setCellValue(c - cr / 2);
            } else {
                filaFinalMasUno.getCell(columna).setCellValue(c - cr / 2);
            }

            FileOutputStream fos = new FileOutputStream(RUTA_LIBRO_OFERTA);
            libroOferta.write(fos);
            libroOferta.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return c - cr / 2;
    }

    public void conteoFechasTodasColumnas() {

        XSSFRow filaDias = consolidadoOrigen.getSheet("Oferta educativa").getRow(0);
        int contadorColumnas = INDICE_COLUMNA_FECHA_INICIAL;

        while (!celdaVacia(filaDias, contadorColumnas)) {
            conteoFechasColumna(contadorColumnas);
            contadorColumnas++;
        }
    }

    public void creacionHojaCuposVirtuales() {
        ArrayList<String> listadoAsignaturasVirtuales = new ArrayList<String>();
        ArrayList<Integer> listadoMatriculasVirtuales = new ArrayList<Integer>();

        try {
            FileInputStream fis = new FileInputStream(RUTA_LIBRO_OFERTA);
            XSSFWorkbook libroOferta = new XSSFWorkbook(fis);

            if (!hojaExiste(libroOferta, "Cupos virtuales")) {
                libroOferta.createSheet("Cupos virtuales");
            }

            XSSFSheet hojaCuposVirtuales = libroOferta.getSheet("Cupos virtuales");
            int contadorFilasOrigen = 2;
            int contadorFilasDestino = 1;

            XSSFSheet hojaOfertaEducativa = libroOferta.getSheet("Oferta educativa");
            XSSFRow filaOferta = hojaOfertaEducativa.getRow(contadorFilasOrigen);

            //ccreamos encabezados
            hojaCuposVirtuales.createRow(0).createCell(0).setCellValue("Programa");
            hojaCuposVirtuales.getRow(0).createCell(1).setCellValue("Semestre");
            hojaCuposVirtuales.getRow(0).createCell(2).setCellValue("Asignatura");
            hojaCuposVirtuales.getRow(0).createCell(3).setCellValue("Cupo");

            while (!celdaVacia(filaOferta, 0)) {
                String programa, asignatura, semestre, virtual;

                programa = filaOferta.getCell(0).getStringCellValue();
                asignatura = filaOferta.getCell(8).getStringCellValue();
                semestre = filaOferta.getCell(1).getStringCellValue();

                int matricula = (int) filaOferta.getCell(10).getNumericCellValue();
                virtual = filaOferta.getCell(12).getStringCellValue();

                if (virtual.equals("Si")) {
                    //creamos acumulados
                    if (listadoAsignaturasVirtuales.indexOf(asignatura.trim()) != -1) {
                        int indice = listadoAsignaturasVirtuales.indexOf(asignatura);
                        listadoMatriculasVirtuales.set(indice, listadoMatriculasVirtuales.get(indice) + matricula);
                    } else {
                        listadoAsignaturasVirtuales.add(asignatura);
                        listadoMatriculasVirtuales.add(matricula);
                    }

                    XSSFRow fila = hojaCuposVirtuales.createRow(contadorFilasDestino);

                    fila.createCell(0).setCellValue(programa);
                    fila.createCell(1).setCellValue(semestre);
                    fila.createCell(2).setCellValue(asignatura);
                    fila.createCell(3).setCellValue(matricula);
                    contadorFilasDestino++;
                }

                for (int i = 0; i < listadoAsignaturasVirtuales.size(); i++) {
                    XSSFRow filaAcumuladas = hojaCuposVirtuales.getRow(i + 1);
                    XSSFCell celdaAcumuladasAsignatura = filaAcumuladas.createCell(5);
                    XSSFCell celdaAcumuladasCupos = filaAcumuladas.createCell(6);

                    celdaAcumuladasAsignatura.setCellValue(listadoAsignaturasVirtuales.get(i));
                    celdaAcumuladasCupos.setCellValue(listadoMatriculasVirtuales.get(i));
                }

                contadorFilasOrigen++;
                filaOferta = hojaOfertaEducativa.getRow(contadorFilasOrigen);
            }

            File archivoExcel = new File(RUTA_LIBRO_OFERTA);
            FileOutputStream fos = new FileOutputStream(archivoExcel);
            libroOferta.write(fos);
            libroOferta.close();

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
                String alfaNumerico = alfa.trim() + "" + numerico.trim();

                String programa = (String) retornarValor(fila.getCell(COLUMNA_OFERTA_PROGRAMA));
                String docente = (String) retornarValor(fila.getCell(COLUMNA_OFERTA_DOCENTE));
                String grupo = (String) retornarValor(fila.getCell(COLUMNA_OFERTA_GRUPO));
                String asignatura = (String) retornarValor(fila.getCell(COLUMNA_OFERTA_ASIGNATURA));

                int creditos = (int) retornarValor(fila.getCell(COLUMNA_OFERTA_CREDITOS));

                int sesionesIdeales = numeroSesionesIdeales(programa, alfaNumerico, creditos);
                int sesionesProgramadas = 0;

                int contadorColumnas = INDICE_COLUMNA_FECHA_INICIAL;
                XSSFRow filaDias = hoja.getRow(0);
                while (!celdaVacia(filaDias, contadorColumnas)) {
                    if (!celdaVacia(fila, contadorColumnas)) {
                        sesionesProgramadas++;
                    }
                    contadorColumnas++;
                }

                //la salida de los errores luego de la revisión del libro final
                //no se escribe el libro, por el momento sólo se hace en salida de consola
                if (sesionesProgramadas < sesionesIdeales) {
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

    public ArrayList<String> validarAlfaNumericosOfertaVsAlfaNumericosMallaBase() {
        ArrayList<String> errores = new ArrayList<String>();

        for (int i = 0; i < listadoProgramas.size(); i++) {
            String programa = listadoProgramas.get(i).trim();
            String alfaNumerico = listadoAlfa.get(i).trim() + "" + listadoNumerico.get(i).trim();

            int conteoBase = 0;
            for (int j = 0; j < listadoBaseAsginaturasAlfa.size(); j++) {
                if (nomenclaturaPrograma(listadoBaseAsginaturasIdCarreras.get(j)).trim().equals(programa)
                        && alfaNumerico.equals((listadoBaseAsginaturasAlfa.get(j).trim()
                                + "" + listadoBaseAsginaturasNumerico.get(j).trim()))) {
                    break;
                }
                conteoBase = j;
            }
            if (conteoBase == listadoBaseAsginaturasAlfa.size() - 1) {
                errores.add("El programa y alfanumerico: " + programa + " " + alfaNumerico + " no se encuentra en la malla base");
            }
        }

        erroresValidacionOfertaMalla = errores;
        return errores;
    }

    public int periodicidadDiasAsignatura(String programa, String alfaNumerico) {
        int n = 0;
        
        int idCarreraBase = obtenerIdMallaBaseProgramaDado(programa);

        for (int i = 0; i < listadoBaseAsginaturasSemana.size(); i++) {

            if (listadoBaseAsginaturasIdCarreras.get(i)==idCarreraBase && (listadoBaseAsginaturasAlfa.get(i).trim() + listadoBaseAsginaturasNumerico.get(i).trim()).equals(alfaNumerico)) {
                n = listadoBasePeriodicidadDias.get(i);
            }
        }

        return n;

    }

    public void cantidadCreditosPorDocente() {
        //importante, esta cantidad de créditos no tiene en cuenta las virtuales
        //esta cantidad de créditos se hace con el fin de programar
        int creditos = 0;
        for (int i = 0; i < listadoDepuradoDocentes.size(); i++) {
            creditos = 0;
            for (int j = 0; j < listadoProgramas.size(); j++) {
                if (listadoDepuradoDocentes.get(i).trim().equals(listadoDocentes.get(j).trim())) {
                    if (!esCruzadaDocente(j)) {
                        creditos = creditos + listadoCreditos.get(j);
                    } else {

                        if (j == listadoIndicesCruzadosCompartidosIndiceDado(j).get(0)) {
                            creditos = creditos + listadoCreditos.get(j);
                        }
                    }
                }
            }

            listadoDepuradoCreditosTotalesDocente.add(creditos);
        }

    }

    public void imprimirConsoleParDocenteTotalCreditos() {
        for (int i = 0; i < listadoDepuradoDocentes.size(); i++) {
            System.out.println("Docente: " + listadoDepuradoDocentes.get(i) + " Total creditos: " + listadoDepuradoCreditosTotalesDocente.get(i));
        }
    }

    public void imprimirConsoleParDocenteTotalCreditosOrdenado() {
        System.out.println("tamaño sin ordenar: " + listadoDepuradoDocentes.size() + " tamaño ordenado: " + listadoDepuradoDocentesOrdenado.size());

        for (int i = 0; i < listadoDepuradoDocentes.size(); i++) {
            System.out.println("Docente: " + listadoDepuradoDocentesOrdenado.get(i) + " Total creditos: " + listadoDepuradoCreditosTotalesDocenteOrdenado.get(i));
        }
    }

    public void ordenarListadoDepuradoDocentesPorCreditos() {
        ArrayList<String> listadoDocentes = new ArrayList<String>(listadoDepuradoDocentes);
        ArrayList<Integer> listadoCreditos = new ArrayList<Integer>(listadoDepuradoCreditosTotalesDocente);

        for (int i = 0; i < listadoDepuradoDocentes.size(); i++) {

            int maximo = Collections.max(listadoCreditos);
            int indice = listadoCreditos.indexOf(maximo);

            listadoDepuradoDocentesOrdenado.add(listadoDocentes.get(indice));
            listadoDepuradoCreditosTotalesDocenteOrdenado.add(listadoCreditos.get(indice));

            listadoDocentes.remove(indice);
            listadoCreditos.remove(indice);

        }

    }

    public void randomizarOrdenProgramacion() {

        Collections.shuffle(indicesTodaOfertaDiferenteOrden);

        System.out.println("indices aleatorios: " + indicesTodaOfertaDiferenteOrden);

    }

    public double calcularEfectividadHorario() {
        PROGRAMADOS = listadoProgramas.size() - NOPROGRAMADOS;

        double e = ((double) PROGRAMADOS) / (PROGRAMADOS + NOPROGRAMADOS);
        System.out.println("PROGRAMADOS:" + PROGRAMADOS + " NOPROGRAMADOS: " + NOPROGRAMADOS + " PORCENTAJE EFECTIVIDAD " + e);
        return e;
    }

    public void inicializarIndicesOrdenProgramacion() {
        for (int i = 0; i < listadoProgramas.size(); i++) {
            indicesTodaOfertaDiferenteOrden.add(i);
        }
    }

    public void horariosRandomizados() {
        double efectividadHorario = 0.0;
        int iteeracionMejor = 0;

        for (int iteracion = 0; iteracion < 1; iteracion++) {
            System.out.println("iteracion " + iteracion);
//            arregloSesiones = new ArrayList<ArrayList<Date>>();

            long t1 = System.currentTimeMillis();
            asignacionDeSesionesNRCRepetidos();//a ejecutar

//            randomizarOrdenProgramacion();
            ordenarIndicesProgramacion();
            asignacionDeSesiones2indices();
            asignacionDeSesionesIndices();

            long t2 = System.currentTimeMillis();

            revisarSesionesObtenerNumeroNoProgramados();

            if (iteracion == 0) {
                mejorArregloSesiones = new ArrayList<ArrayList<Date>>(arregloSesiones);
                efectividadHorario = calcularEfectividadHorario();
            } else {
                if (calcularEfectividadHorario() > efectividadHorario) {
                    mejorArregloSesiones = new ArrayList<ArrayList<Date>>(arregloSesiones);
                    efectividadHorario = calcularEfectividadHorario();
                    iteeracionMejor = iteracion;
                }
                System.out.println("Hasta ahora la mejor iteracion: " + iteeracionMejor);
                System.out.println("Hasta ahora la mayor efectividad: " + efectividadHorario);
            }
            System.out.println("Efectividad del horario: " + efectividadHorario);
            System.out.println("Tiempo usado: " + (t2 - t1));
        }

        arregloSesiones = new ArrayList<ArrayList<Date>>(mejorArregloSesiones);
    }

    public void imprimirConsolaSesionesDe(String programa, String grupo, String docente, String asignatura, String NRC) {

        for (int i = 0; i < listadoProgramas.size(); i++) {
            String p, g, d, a, nrc;

            p = listadoProgramas.get(i).trim();
            g = listadoSemestres.get(i).trim();
            d = listadoDocentes.get(i).trim();
            a = listadoAsignaturas.get(i).trim();
            nrc = listadoNRC.get(i);

            if (p.equals(programa) && g.equals(grupo) && d.equals(docente) && a.equals(asignatura)) {
                ArrayList<Date> sesiones = arregloSesiones.get(i);
                System.out.println("Sesiones para " + p + " " + g + " " + d + " " + " " + a);
                for (Date s : sesiones) {
                    System.out.println("" + s);
                }
            }
        }

    }

    public void ordenarIndicesProgramacion() {
        ArrayList<String> tPrograma, tGrupo, tAlfa, tNumerico, tDocente, tAsignatura, tNRC;

        tPrograma = new ArrayList<String>(listadoProgramas);
        tGrupo = new ArrayList<String>(listadoSemestres);
        tAlfa = new ArrayList<String>(listadoAlfa);
        tNumerico = new ArrayList<String>(listadoNumerico);
        tDocente = new ArrayList<String>(listadoDocentes);
        tAsignatura = new ArrayList<String>(listadoAsignaturas);
        tNRC = new ArrayList<String>(listadoNRC);

        indicesTodaOfertaDiferenteOrden = new ArrayList<Integer>();

        for (int i = 0; i < listadoDepuradoDocentesOrdenado.size(); i++) {
            String docente = listadoDepuradoDocentesOrdenado.get(i);

            int siguiente = 0;
            for (int j = siguiente; j < tPrograma.size(); j++) {
                if (docente.trim().equals(tDocente.get(j).trim())) {
                    indicesTodaOfertaDiferenteOrden.add(j);
                    siguiente = j + 1;
                }
            }
        }

        System.out.println("" + indicesTodaOfertaDiferenteOrden);

    }

    public void recogerFechasSesionesYaProgramadas() {
        XSSFSheet hojaOferta = consolidadoOrigen.getSheet(NOMBREHOJAOFERTA);
        XSSFRow filaFechas = hojaOferta.getRow(INDICE_FILA_FECHAS);
        XSSFRow filaGrupos = hojaOferta.getRow(INDICE_FILA_INICIAN_GRUPOS);

        int contadorFilas = 0;
        int contadorFilasArray = 0;

        while (!celdaVacia(filaGrupos, 0)) {
            //se recogen las sesiones, sólo si no son virtuales
            if (contadorFilasArray == 701) {
                contadorFilas = contadorFilas;
            }
            if (contadorFilasArray < listadoAlfa.size()) {
                if (!esVirtual(filaGrupos.getCell(COLUMNA_OFERTA_ALFA).getStringCellValue().trim() + "" + (String) ("" + retornarValor(filaGrupos.getCell(COLUMNA_OFERTA_NUMERICO))))) {

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
    
    public String convertirDateACadenaHorasMinutosDosPuntos(Date f){
        String cadena="";
        
        Calendar c = Calendar.getInstance();
        c.setTime(f);
        
        int horaDia = c.get(Calendar.HOUR_OF_DAY);
        int minutos = c.get(Calendar.MINUTE);
        
        if(minutos==0){
            cadena = horaDia+":"+minutos;
        }else{
            cadena = horaDia+":"+minutos;
        }
        
        return cadena;
    }
    
    public int obtenerIdMallaBaseProgramaDado(String programa){
        int id=0;
        
        for(int i=0; i<listadoBaseCarrerasID.size(); i++){
            if(programa.trim().equals(listadoBaseCarrerasNomenclatura.get(i))){
                id = listadoBaseCarrerasID.get(i);
                return id;
            }
        }
        
        return id;
                
    }

}
