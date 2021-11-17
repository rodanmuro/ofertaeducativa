/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa2;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Usuario
 */
public class CargaDeDatosExcel {

    int FILAINICIALENCABEZADOSOFERTAEDUCATIVA = 1;
    int FILAENCABEZADOSNOMBRESDIAS = 0;
    int INDICECOLUMNAINICIANFECHAS = 18;

    String NOMBREHOJAJORNADAS = "jornadas";
    String NOMBREHOJAFECHASIMPORTANTES = "fechasimportantes";
    String NOMBREHOJAOFERTAEDUCATIVA = "Oferta educativa";
    String NOMBREHOJASALONES = "salones";
    String NOMBREHOJAOCUPACIONSALONES = "ocupación de salones";
    String CADENACRUZADA = "cruzada";
    String CADENACOMPARTIDA = "compartida";
    String NOMBREHOJAPRIORIDADESPROGRAMACION = "prioridadProgramacion";

    String RUTAARCHIVOOFERTAEDUCATIVA = "";

    Date FECHAINICIALSEMESTRE = null;
    Date FECHAFINALSEMESTRE = null;
    ArrayList<Date> LISTADOFECHAINICIAL = null;
    ArrayList<Date> LISTADOFECHAFINAL = null;
    ArrayList<Date> LISTADOFECHASPROHIBIDAS = null;

    ArrayList<String> LISTADOOFERTAEDUCATIVA_PROGRAMA;
    ArrayList<String> LISTADOOFERTAEDUCATIVA_SEMESTRE;
    ArrayList<String> LISTADOOFERTAEDUCATIVA_MOMENTO;
    ArrayList<String> LISTADOOFERTAEDUCATIVA_PENSUM;
    ArrayList<String> LISTADOOFERTAEDUCATIVA_JORNADA;
    ArrayList<String> LISTADOOFERTAEDUCATIVA_ALFA;
    ArrayList<String> LISTADOOFERTAEDUCATIVA_NUMERICO;
    ArrayList<Integer> LISTADOOFERTAEDUCATIVA_CREDITOS;
    ArrayList<String> LISTADOOFERTAEDUCATIVA_ASIGNATURA;
    ArrayList<String> LISTADOOFERTAEDUCATIVA_NRC;
    ArrayList<Integer> LISTADOOFERTAEDUCATIVA_CUPO;
    ArrayList<String> LISTADOOFERTAEDUCATIVA_DOCENTE;
    ArrayList<String> LISTADOOFERTAEDUCATIVA_SALON;
    ArrayList<String> LISTADOOFERTAEDUCATIVA_VIRTUAL;
    ArrayList<String> LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO;
    ArrayList<Integer> LISTADOOFERTAEDUCATIVA_NUMEROSESIONES;
    ArrayList<Integer> LISTADOOFERTAEDUCATIVA_DURACIONIDEALSESION;
    ArrayList<Integer> LISTADOOFERTAEDUCATIVA_PERIODICIDAD;
    ArrayList<Integer> LISTADOOFERTAEDUCATIVA_CUPOMAXIMO;

    ArrayList<String> LISTADOCOORDENADASCELDASSESIONES;

    ArrayList<String> LISTADODEPURADOGRUPOSCADENA;
    ArrayList<Integer> LISTADODEPURADOTOTALMINUTOSSESIONESGRUPO;

    ArrayList<Docente> LISTADODEPURADODOCENTES;
    ArrayList<Integer> LISTADODEPURADOCREDITOSDOCENTE;
    ArrayList<Integer> LISTADODEPURADOSESIONESDOCENTE;

    ArrayList<Docente> LISTADOORDENADODEPURADODOCENTESCREDITOS;
    ArrayList<Integer> LISTADOORDENADODEPURADOCREDITOSDOCENTE;//DE MAYOR A MENOR CANTIDAD CREDITOS
    ArrayList<Integer> LISTADOORDENADODEPURADOCREDITOSDOCENTESINREPETIRCOMPARTIDOS;
    ArrayList<Integer> LISTADODEPURADOCREDITOSDOCENTESINREPETIRCOMPARTIDOS;
    ArrayList<Integer> LISTADODEPURADOSESIONESDOCENTESINREPETIRCOMPARTIDOS;

    ArrayList<Docente> LISTADOORDENADODEPURADODOCENTESSESIONES;
    ArrayList<Integer> LISTADOORDENADODEPURADOSESIONESDOCENTE;//DE MAYOR A MENOR CANTIDAD SESIONES

    ArrayList<Integer> LISTADOINDICESORDENASIGNACIONCREDITOS;
    ArrayList<Integer> LISTADOINDICESORDENASIGNACIONSESIONES;

    ArrayList<String> LISTADODOCENTESPRIORIDADPROGRAMAR;
    ArrayList<String> LISTADOIDCRUCECOMPARTIDOPRIORIDADPROGRAMAR;

    ArrayList<Jornada> LISTADOJORNADAS;
    ArrayList<Salon> LISTADOSALONES;
    
    ArrayList<OcupacionSalon> LISTADOOCUPACIONSALONES;

    String ENCABEZADOOFERTAEDUCATIVA_PROGRAMA = "programa";
    String ENCABEZADOOFERTAEDUCATIVA_SEMESTRE = "semestre";
    String ENCABEZADOOFERTAEDUCATIVA_GRUPO = "grupo";
    String ENCABEZADOOFERTAEDUCATIVA_PENSUM = "pensum";
    String ENCABEZADOOFERTAEDUCATIVA_JORNADA = "jornada";
    String ENCABEZADOOFERTAEDUCATIVA_ALFA = "alfa";
    String ENCABEZADOOFERTAEDUCATIVA_NUMERICO = "numerico";
    String ENCABEZADOOFERTAEDUCATIVA_CREDITOS = "creditos";
    String ENCABEZADOOFERTAEDUCATIVA_ASIGNATURA = "asignatura";
    String ENCABEZADOOFERTAEDUCATIVA_NRC = "nrc";
    String ENCABEZADOOFERTAEDUCATIVA_CUPO = "cupo";
    String ENCABEZADOOFERTAEDUCATIVA_DOCENTE = "docente";
    String ENCABEZADOOFERTAEDUCATIVA_SALON = "salón";
    String ENCABEZADOOFERTAEDUCATIVA_VIRTUAL = "virtual";
    String ENCABEZADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO = "idcrucecomp";
    String ENCABEZADOOFERTAEDUCATIVA_NUMEROSESIONES = "numerosesiones";
    String ENCABEZADOOFERTAEDUCATIVA_DURACIONIDEALSESION = "duracionsesion";
    String ENCABEZADOOFERTAEDUCATIVA_PERIODICIDAD = "periodicidad";
    String ENCABEZADOOFERTAEDUCATIVA_CUPOMAXIMO = "cupomaximo";

    String ENCABEZADOMALLABASE_INICIOSEMESTRE = "iniciosemestre";
    String ENCABEZADOMALLABASE_FINALSEMESTRE = "finalsemestre";
    String ENCABEZADOMALLABASE_DIASFESTIVOS = "diasfestivos";
    String ENCABEZADOMALLABASE_DOCENTEPRIORIDADPROGRAMACION = "docente";
    String ENCABEZADOMALLABASE_IDCRUCECOMPARTIDOPRIORIDADPROGRAMACION = "idcrucecompartido";
   
    public CargaDeDatosExcel() {

    }

    public void cargarOfertaEducativa(String rutaOfertaEducativa) {
        try {
            RUTAARCHIVOOFERTAEDUCATIVA = rutaOfertaEducativa;
            FileInputStream fis = new FileInputStream(RUTAARCHIVOOFERTAEDUCATIVA);

            XSSFWorkbook libroOfertaEducativa = new XSSFWorkbook(fis);

            LISTADOOFERTAEDUCATIVA_PROGRAMA = new ArrayList<String>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "programa",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_PROGRAMA,
                    "string");

            LISTADOOFERTAEDUCATIVA_SEMESTRE = new ArrayList<String>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "semestre",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_SEMESTRE,
                    "string");

            LISTADOOFERTAEDUCATIVA_MOMENTO = new ArrayList<String>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "momento",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_MOMENTO,
                    "string");

            LISTADOOFERTAEDUCATIVA_PENSUM = new ArrayList<String>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "pensum",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_PENSUM,
                    "string");

            LISTADOOFERTAEDUCATIVA_JORNADA = new ArrayList<String>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "jornada",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_JORNADA,
                    "string");

            LISTADOOFERTAEDUCATIVA_ALFA = new ArrayList<String>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "alfa",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_ALFA,
                    "string");

            LISTADOOFERTAEDUCATIVA_NUMERICO = new ArrayList<String>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "numerico",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_NUMERICO,
                    "string");

            LISTADOOFERTAEDUCATIVA_CREDITOS = new ArrayList<Integer>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "créditos",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_CREDITOS,
                    "integer");

            LISTADOOFERTAEDUCATIVA_ASIGNATURA = new ArrayList<String>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "asignatura",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_ASIGNATURA,
                    "string");

            LISTADOOFERTAEDUCATIVA_NRC = new ArrayList<String>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "nrc",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_NRC,
                    "string");

            LISTADOOFERTAEDUCATIVA_CUPO = new ArrayList<Integer>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "cupo",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_CUPO,
                    "integer");

            LISTADOOFERTAEDUCATIVA_DOCENTE = new ArrayList<String>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "docente",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_DOCENTE,
                    "string");
            
            LISTADOOFERTAEDUCATIVA_SALON = new ArrayList<String>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "salón",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_SALON,
                    "string");
            
            LISTADOOFERTAEDUCATIVA_VIRTUAL = new ArrayList<String>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "virtual",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_VIRTUAL,
                    "string");

            LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO = new ArrayList<String>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "IDCruceComp",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO,
                    "string");

            LISTADOOFERTAEDUCATIVA_NUMEROSESIONES = new ArrayList<Integer>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "NumeroSesiones",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_NUMEROSESIONES,
                    "integer");

            LISTADOOFERTAEDUCATIVA_DURACIONIDEALSESION = new ArrayList<Integer>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "DuracionSesion",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_DURACIONIDEALSESION,
                    "integer");

            LISTADOOFERTAEDUCATIVA_PERIODICIDAD = new ArrayList<Integer>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "Periodicidad",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_PERIODICIDAD,
                    "integer");

            LISTADOOFERTAEDUCATIVA_CUPOMAXIMO = new ArrayList<Integer>();
            cargarColumnaEnArrayList(libroOfertaEducativa,
                    "Oferta educativa",
                    "CupoMaximo",
                    "programa",
                    1,
                    LISTADOOFERTAEDUCATIVA_CUPOMAXIMO,
                    "integer");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * las fechas importantes son los días festivos, las fechas de inicio y fin
     * de semestre
     *
     * @param rutaLibroFechasImportantes
     */
    public void cargaFechasImportantes(String rutaLibroFechasImportantes) {
        try {

            FileInputStream fis = new FileInputStream(rutaLibroFechasImportantes);
            XSSFWorkbook libroFechasImportantes = new XSSFWorkbook(fis);

            XSSFSheet hojaFechasImportantes = libroFechasImportantes.getSheet(NOMBREHOJAFECHASIMPORTANTES);

            LISTADOFECHAINICIAL = new ArrayList<Date>();
            cargarColumnaEnArrayList(libroFechasImportantes,
                    NOMBREHOJAFECHASIMPORTANTES,
                    ENCABEZADOMALLABASE_INICIOSEMESTRE,
                    ENCABEZADOMALLABASE_INICIOSEMESTRE,
                    0,
                    LISTADOFECHAINICIAL,
                    "date");

            LISTADOFECHAFINAL = new ArrayList<Date>();
            cargarColumnaEnArrayList(libroFechasImportantes,
                    NOMBREHOJAFECHASIMPORTANTES,
                    ENCABEZADOMALLABASE_FINALSEMESTRE,
                    ENCABEZADOMALLABASE_FINALSEMESTRE,
                    0,
                    LISTADOFECHAFINAL,
                    "date");

            LISTADOFECHASPROHIBIDAS = new ArrayList<Date>();
            cargarColumnaEnArrayList(libroFechasImportantes,
                    NOMBREHOJAFECHASIMPORTANTES,
                    ENCABEZADOMALLABASE_DIASFESTIVOS,
                    ENCABEZADOMALLABASE_DIASFESTIVOS,
                    0,
                    LISTADOFECHASPROHIBIDAS,
                    "date");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void crearListadoPrioridadDocentesProgramar(String rutaLibroPrioridadDocentes) {
        try {

            FileInputStream fis = new FileInputStream(rutaLibroPrioridadDocentes);
            XSSFWorkbook libroPrioridadProgramacion = new XSSFWorkbook(fis);

            XSSFSheet hojaFechasImportantes = libroPrioridadProgramacion.getSheet(NOMBREHOJAFECHASIMPORTANTES);

            LISTADODOCENTESPRIORIDADPROGRAMAR = new ArrayList<String>();
            cargarColumnaEnArrayList(libroPrioridadProgramacion,
                    NOMBREHOJAPRIORIDADESPROGRAMACION,
                    ENCABEZADOMALLABASE_DOCENTEPRIORIDADPROGRAMACION,
                    ENCABEZADOMALLABASE_DOCENTEPRIORIDADPROGRAMACION,
                    0,
                    LISTADODOCENTESPRIORIDADPROGRAMAR,
                    "string");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void crearListadoPrioridadIdCruceCompartidoProgramar(String rutaLibroPrioridadIdCruceCompartido) {
        try {

            FileInputStream fis = new FileInputStream(rutaLibroPrioridadIdCruceCompartido);
            XSSFWorkbook libroPrioridadProgramacion = new XSSFWorkbook(fis);

            XSSFSheet hojaFechasImportantes = libroPrioridadProgramacion.getSheet(NOMBREHOJAFECHASIMPORTANTES);

            LISTADOIDCRUCECOMPARTIDOPRIORIDADPROGRAMAR = new ArrayList<String>();
            cargarColumnaEnArrayList(libroPrioridadProgramacion,
                    NOMBREHOJAPRIORIDADESPROGRAMACION,
                    ENCABEZADOMALLABASE_IDCRUCECOMPARTIDOPRIORIDADPROGRAMACION,
                    ENCABEZADOMALLABASE_IDCRUCECOMPARTIDOPRIORIDADPROGRAMACION,
                    0,
                    LISTADOIDCRUCECOMPARTIDOPRIORIDADPROGRAMAR,
                    "string");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Object> elementosFilaListadoGuardadoIndiceExcel(int indiceExcel) {
        ArrayList<Object> listado = new ArrayList<Object>();

        int indice = indiceExcel - (FILAINICIALENCABEZADOSOFERTAEDUCATIVA + 2);

        listado.add(LISTADOOFERTAEDUCATIVA_PROGRAMA.get(indice));
        listado.add(LISTADOOFERTAEDUCATIVA_SEMESTRE.get(indice));
        listado.add(LISTADOOFERTAEDUCATIVA_MOMENTO.get(indice));
        listado.add(LISTADOOFERTAEDUCATIVA_PENSUM.get(indice));
        listado.add(LISTADOOFERTAEDUCATIVA_JORNADA.get(indice));
        listado.add(LISTADOOFERTAEDUCATIVA_ALFA.get(indice));
        listado.add(LISTADOOFERTAEDUCATIVA_NUMERICO.get(indice));
        listado.add(LISTADOOFERTAEDUCATIVA_CREDITOS.get(indice));
        listado.add(LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(indice));
        listado.add(LISTADOOFERTAEDUCATIVA_NRC.get(indice));
        listado.add(LISTADOOFERTAEDUCATIVA_CUPO.get(indice));
        listado.add(LISTADOOFERTAEDUCATIVA_DOCENTE.get(indice));
        listado.add(LISTADOOFERTAEDUCATIVA_VIRTUAL.get(indice));
        listado.add(LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(indice));

        return listado;
    }

    public void cargarColumnaEnArrayList(XSSFWorkbook libroFuente, String nombreHojaFuente, String encabezadoFuente, String encabezadoContador, int filaInicial, ArrayList listadoObjetivo, String tipoArreglo) {
        int contadorFilas = 0;

        XSSFSheet hojaFuente = libroFuente.getSheet(nombreHojaFuente);
        XSSFCell celdaContador = obtenerCeldaEncabezado(hojaFuente, filaInicial, encabezadoContador);
        XSSFCell celdaDatos = obtenerCeldaEncabezado(hojaFuente, filaInicial, encabezadoFuente);
        

        int indiceColumnaContadora = celdaContador.getColumnIndex();
        int indiceColumnaDatos = celdaDatos.getColumnIndex();

        XSSFRow filaContadora = hojaFuente.getRow(filaInicial + FILAINICIALENCABEZADOSOFERTAEDUCATIVA);

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
            filaContadora = hojaFuente.getRow(filaInicial + FILAINICIALENCABEZADOSOFERTAEDUCATIVA + contadorFilas);
        }
    }

    public boolean esCeldaNumerica(XSSFCell celda) {
        boolean si = false;
        if (celda.getCellTypeEnum() == CellType.NUMERIC) {
            si = true;
        }
        return si;
    }

    public boolean esCeldaCadena(XSSFCell celda) {
        boolean si = false;
        if (celda.getCellTypeEnum() == CellType.STRING) {
            si = true;
        }
        return si;
    }

    public boolean esCeldaBlanco(XSSFCell celda) {
        boolean si = false;
        if (celda.getCellTypeEnum() == CellType.BLANK) {
            si = true;
        }
        return si;
    }

    /**
     * Esta función crea las instancias Jornada que existen en el archivo de
     * basededatos de la malla curricular. En dicho archivo hay una hoja llamada
     * jornadas, en las cuales están todos los datos de las jornadas existentes
     * para el semestre lectivo
     *
     * @param libroJornadas
     * @return
     */
    public ArrayList<Jornada> cargaJornadas(String rutaLibroJornadas) {
        ArrayList<Jornada> listadoJornadas = new ArrayList<Jornada>();

        try {
            FileInputStream fis = new FileInputStream(rutaLibroJornadas);
            XSSFWorkbook libroJornadas = new XSSFWorkbook(fis);

            XSSFSheet hojaJornadas = libroJornadas.getSheet(NOMBREHOJAJORNADAS);
            XSSFCell celdaEncabezadoJornada = obtenerCeldaEncabezado(hojaJornadas, 0, "jornada");
            XSSFCell celdaEncabezadoDias = obtenerCeldaEncabezado(hojaJornadas, 0, "dias");
            XSSFCell celdaEncabezadoHorasInicialesYFinales = obtenerCeldaEncabezado(hojaJornadas, 0, "horasInicialesYFinales");
            XSSFCell celdaEncabezadoPeriodicidad = obtenerCeldaEncabezado(hojaJornadas, 0, "periodicidad");
            XSSFCell celdaEncabezadoFechaInicial = obtenerCeldaEncabezado(hojaJornadas, 0, "fechaInicial");
            XSSFCell celdaEncabezadoFechaFinal = obtenerCeldaEncabezado(hojaJornadas, 0, "fechaFinal");

            int j = 0;
            while (!celdaVacia(hojaJornadas.getRow(1 + j), 0)) {
                String nombreJornada = hojaJornadas.getRow(j + 1).getCell(celdaEncabezadoJornada.getColumnIndex()).getStringCellValue();
                String diasEnBrutoParaJornada = hojaJornadas.getRow(j + 1).getCell(celdaEncabezadoDias.getColumnIndex()).getStringCellValue();
                String horasEnBrutoJornada = hojaJornadas.getRow(j + 1).getCell(celdaEncabezadoHorasInicialesYFinales.getColumnIndex()).getStringCellValue();
                int periodicidadJornada = (int) hojaJornadas.getRow(j + 1).getCell(celdaEncabezadoPeriodicidad.getColumnIndex()).getNumericCellValue();
                Date fechaInicioJornada = hojaJornadas.getRow(j + 1).getCell(celdaEncabezadoFechaInicial.getColumnIndex()).getDateCellValue();
                Date fechaFinalJornada = hojaJornadas.getRow(j + 1).getCell(celdaEncabezadoFechaFinal.getColumnIndex()).getDateCellValue();

                ArrayList<Integer> listadoEnterosDiasJornada = new ArrayList<Integer>(arregloEnterosDiasCadena(diasEnBrutoParaJornada));
                ArrayList<String> listadoTotalHorasCadenaEnBruto = new ArrayList<String>(arregloCadenasHorasConDosPuntos(horasEnBrutoJornada));
                ArrayList<Hora> listadoTotalHorasEnBruto = new ArrayList<Hora>(listadoHorasParaListadoCadenaHoras(listadoTotalHorasCadenaEnBruto));
                ArrayList<Hora> listadoHorasIniciales = new ArrayList<Hora>(listadoHorasIniciales(listadoTotalHorasEnBruto));
                ArrayList<Hora> listadoHorasFinales = new ArrayList<Hora>(listadoHorasFinales(listadoTotalHorasEnBruto));
                
                //para el tipo de formato lunes,martes,miércoles y una sola hora 7:00,20:00
                //se va a entender que todos esos días se tiene esa misma hora
                if(listadoHorasIniciales.size()==1 && listadoHorasFinales.size()==1){
                    for (int i = 0; i < listadoEnterosDiasJornada.size()-1; i++) {
                        listadoHorasIniciales.add(listadoHorasIniciales.get(0));
                        listadoHorasFinales.add(listadoHorasFinales.get(0));
                    }
                }
                    
                if(nombreJornada.equals("JornadaEnsayo")){
                    System.out.println("");
                }
                //con estos datos recogidos ahora vamos a crear el objeto jornada, aunque antes, su respectiva tupla
                ArrayList<TuplaDiaHoraInicialFinal> listadoTuplasJornada = new ArrayList<TuplaDiaHoraInicialFinal>();
                for (int i = 0; i < listadoEnterosDiasJornada.size(); i++) {
                    TuplaDiaHoraInicialFinal tupla = new TuplaDiaHoraInicialFinal(listadoEnterosDiasJornada.get(i),
                            listadoHorasIniciales.get(i), listadoHorasFinales.get(i));
                    listadoTuplasJornada.add(tupla);
                }

                listadoJornadas.add(new Jornada(nombreJornada, listadoTuplasJornada, periodicidadJornada, fechaInicioJornada, fechaFinalJornada));
                j++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        LISTADOJORNADAS = new ArrayList<Jornada>(listadoJornadas);

        return listadoJornadas;
    }

    public ArrayList<Salon> cargaSalones(String rutaLibroSalones) {
        
        ArrayList<Salon> listadoSalones = new ArrayList<Salon>();

        try {
            FileInputStream fis = new FileInputStream(rutaLibroSalones);
            XSSFWorkbook libroSalones = new XSSFWorkbook(fis);

            XSSFSheet hojaSalones = libroSalones.getSheet(NOMBREHOJASALONES);
            XSSFCell celdaEncabezadoSalon = obtenerCeldaEncabezado(hojaSalones, 0, "salon");
            XSSFCell celdaEncabezadoCantidadSillas = obtenerCeldaEncabezado(hojaSalones, 0, "capacidad");
            XSSFCell celdaEncabezadoObservaciones = obtenerCeldaEncabezado(hojaSalones, 0, "observaciones");
            
            int j = 0;
            while (!celdaVacia(hojaSalones.getRow(1 + j), 0)) {
                String nombreSalon =  hojaSalones.getRow(j + 1).getCell(celdaEncabezadoSalon.getColumnIndex()).getStringCellValue();
                int cantidad = (int) hojaSalones.getRow(j + 1).getCell(celdaEncabezadoCantidadSillas.getColumnIndex()).getNumericCellValue();
                String observacion = hojaSalones.getRow(j + 1).getCell(celdaEncabezadoObservaciones.getColumnIndex()).getStringCellValue();
                
                listadoSalones.add(new Salon(nombreSalon, cantidad, observacion));
                j++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        LISTADOSALONES = new ArrayList<Salon>(listadoSalones);

        return listadoSalones;
    }
    
    public ArrayList<OcupacionSalon> cargaOcupacionSalones(String rutaLibroJornadas) {
        ArrayList<OcupacionSalon> listadoOcupacionSalon = new ArrayList<OcupacionSalon>();

        try {
            FileInputStream fis = new FileInputStream(rutaLibroJornadas);
            XSSFWorkbook libroJornadas = new XSSFWorkbook(fis);

            XSSFSheet hojaOcupacionSalones = libroJornadas.getSheet(NOMBREHOJAOCUPACIONSALONES);
            XSSFCell celdaEncabezadoSalon = obtenerCeldaEncabezado(hojaOcupacionSalones, 0, "salón");
            XSSFCell celdaEncabezadoDias = obtenerCeldaEncabezado(hojaOcupacionSalones, 0, "dias");
            XSSFCell celdaEncabezadoHorasInicialesYFinales = obtenerCeldaEncabezado(hojaOcupacionSalones, 0, "horasInicialesYFinales");
            XSSFCell celdaEncabezadoPeriodicidad = obtenerCeldaEncabezado(hojaOcupacionSalones, 0, "periodicidad");
            XSSFCell celdaEncabezadoFechaInicial = obtenerCeldaEncabezado(hojaOcupacionSalones, 0, "fechaInicial");
            XSSFCell celdaEncabezadoFechaFinal = obtenerCeldaEncabezado(hojaOcupacionSalones, 0, "fechaFinal");

            int j = 0;
            while (!celdaVacia(hojaOcupacionSalones.getRow(1 + j), 0)) {
                String nombreSalon = hojaOcupacionSalones.getRow(j + 1).getCell(celdaEncabezadoSalon.getColumnIndex()).getStringCellValue();
                String diasEnBrutoParaJornada = hojaOcupacionSalones.getRow(j + 1).getCell(celdaEncabezadoDias.getColumnIndex()).getStringCellValue();
                String horasEnBrutoJornada = hojaOcupacionSalones.getRow(j + 1).getCell(celdaEncabezadoHorasInicialesYFinales.getColumnIndex()).getStringCellValue();
                int periodicidadJornada = (int) hojaOcupacionSalones.getRow(j + 1).getCell(celdaEncabezadoPeriodicidad.getColumnIndex()).getNumericCellValue();
                Date fechaInicioJornada = hojaOcupacionSalones.getRow(j + 1).getCell(celdaEncabezadoFechaInicial.getColumnIndex()).getDateCellValue();
                Date fechaFinalJornada = hojaOcupacionSalones.getRow(j + 1).getCell(celdaEncabezadoFechaFinal.getColumnIndex()).getDateCellValue();

                ArrayList<Integer> listadoEnterosDiasJornada = new ArrayList<Integer>(arregloEnterosDiasCadena(diasEnBrutoParaJornada));
                ArrayList<String> listadoTotalHorasCadenaEnBruto = new ArrayList<String>(arregloCadenasHorasConDosPuntos(horasEnBrutoJornada));
                ArrayList<Hora> listadoTotalHorasEnBruto = new ArrayList<Hora>(listadoHorasParaListadoCadenaHoras(listadoTotalHorasCadenaEnBruto));
                ArrayList<Hora> listadoHorasIniciales = new ArrayList<Hora>(listadoHorasIniciales(listadoTotalHorasEnBruto));
                ArrayList<Hora> listadoHorasFinales = new ArrayList<Hora>(listadoHorasFinales(listadoTotalHorasEnBruto));
                
                //para el tipo de formato lunes,martes,miércoles y una sola hora 7:00,20:00
                //se va a entender que todos esos días se tiene esa misma hora
                if(listadoHorasIniciales.size()==1 && listadoHorasFinales.size()==1){
                    for (int i = 0; i < listadoEnterosDiasJornada.size()-1; i++) {
                        listadoHorasIniciales.add(listadoHorasIniciales.get(0));
                        listadoHorasFinales.add(listadoHorasFinales.get(0));
                    }
                }
                    
                //con estos datos recogidos ahora vamos a crear el objeto jornada, aunque antes, su respectiva tupla
                ArrayList<TuplaDiaHoraInicialFinal> listadoTuplasJornada = new ArrayList<TuplaDiaHoraInicialFinal>();
                for (int i = 0; i < listadoEnterosDiasJornada.size(); i++) {
                    TuplaDiaHoraInicialFinal tupla = new TuplaDiaHoraInicialFinal(listadoEnterosDiasJornada.get(i),
                            listadoHorasIniciales.get(i), listadoHorasFinales.get(i));
                    listadoTuplasJornada.add(tupla);
                }
                
                //con el nombre del salón creamos el objeto salón
                int cupoSalon = 0;
                String observacion = "";
                for (int i = 0; i < LISTADOSALONES.size(); i++) {
                    if(LISTADOSALONES.get(i).getNombre().trim().toLowerCase().equals(nombreSalon.trim().toLowerCase())){
                       cupoSalon = LISTADOSALONES.get(i).getCupo();
                       observacion = LISTADOSALONES.get(i).getObservacion();
                       break;
                    }
                }
                Salon salon = new Salon(nombreSalon, cupoSalon, observacion);

                listadoOcupacionSalon.add(new OcupacionSalon(salon, listadoTuplasJornada, periodicidadJornada, fechaInicioJornada, fechaFinalJornada));
                j++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        LISTADOOCUPACIONSALONES = new ArrayList<OcupacionSalon>(listadoOcupacionSalon);

        return listadoOcupacionSalon;
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

    public XSSFCell obtenerCeldaEncabezado(XSSFSheet hoja, int indiceFila, String encabezado) {
        XSSFCell celda = null;
        int j = 0;
        while (!celdaVacia(hoja.getRow(indiceFila), j)/* && j < INDICECOLUMNAINICIANFECHAS*/) {
            if (hoja.getRow(indiceFila).getCell(j).getStringCellValue().toLowerCase().trim().equals(encabezado.toLowerCase().trim())) {
                celda = hoja.getRow(indiceFila).getCell(j);
                break;
            }
            j++;
        }
        if(celda==null){
            Validaciones.mostrarVentanaError("Al parecer no se ha encontrado el encabezado "+encabezado+" en el libro de Oferta Educativa y se ha generado un error ");
        }
        return celda;
    }

    /**
     * Esta función retorna un arreglo de enteros que representan los días de la
     * semana numericamente. Toma los días escritos en una cadena separada por
     * comas Fundamentalmente la idea es tomarlos de una cadena que esté en una
     * cadena de excel ASí por ejemplo, toma la cadena domingo,lunes,martes y
     * devuelve el arraylist<integer>
     * con los elementos 1,2,3
     *
     * @param cadenaDiasSeparadosComa
     * @return ArrayList<Integer> enteros que corresponden a los respectivos
     * días de la semana, según Calendar
     */
    public ArrayList<Integer> arregloEnterosDiasCadena(String cadenaDiasSeparadosComa) {
        String[] arregloDiasCadena = cadenaDiasSeparadosComa.split(",");
        ArrayList<Integer> arregloEnteros = new ArrayList<Integer>();

        for (String dia : arregloDiasCadena) {
            arregloEnteros.add(Horario.deDiaCadenaAEntero(dia.trim()));
        }
        return arregloEnteros;
    }

    /**
     * Esta es una función axuliar que se utiliza en
     * listadoHorasParaListadoCadenaHoras con el fin de tomar el listado de
     * horas que están escritos en una celda de excel y con ellas definir las
     * horas iniciales y finales de cada día en una jornada
     *
     * @param cadenaHorasSeparadosComa
     * @return
     */
    public ArrayList<String> arregloCadenasHorasConDosPuntos(String cadenaHorasSeparadosComa) {
        String[] arregloHorasCadena = cadenaHorasSeparadosComa.split(",");
        ArrayList<String> listaHorasCadena = new ArrayList<String>();

        for (String hora : arregloHorasCadena) {
            listaHorasCadena.add(hora.trim());
        }
        return listaHorasCadena;
    }

    /**
     * Esta es una función auxiliar que luego se utiliza para del retorno de
     * esta funci´no tomar las horas iniciales y las horas finales
     *
     * @param listadoHorasCadena
     * @return
     */
    public ArrayList<Hora> listadoHorasParaListadoCadenaHoras(ArrayList<String> listadoHorasCadena) {
        ArrayList<Hora> listadoHoras = new ArrayList<Hora>();

        for (String horaCadena : listadoHorasCadena) {

            int hora = Integer.parseInt(horaCadena.split(":")[0].trim());
            int minutos = Integer.parseInt(horaCadena.split(":")[1].trim());

            listadoHoras.add(new Hora(hora, minutos));

        }
        return listadoHoras;
    }

    /**
     * Función auxiliar que devuelve un listado con las horas iniciales para un
     * día en una jornada. Esta función debe ir en llave con la función
     * listadoHorasIniciales y con la función que devuelve el listado de días,
     * con ellas se genera cada una de las tuplas para el objeto jornada
     *
     * @param listadoTotalHoras
     * @return
     */
    public ArrayList<Hora> listadoHorasIniciales(ArrayList<Hora> listadoTotalHoras) {
        ArrayList<Hora> lhi = new ArrayList<Hora>();

        for (int i = 0; i < listadoTotalHoras.size(); i++) {
            if (i % 2 == 0) {
                lhi.add(listadoTotalHoras.get(i));
            }
        }
        return lhi;
    }

    /**
     * Función auxiliar que devuelve un listado con las horas finales para un
     * día en una jornada. Esta función debe ir en llave con la función
     * listadoHorasIniciales y con la función que devuelve el listado de días,
     * con ellas se genera cada una de las tuplas para el objeto jornada
     *
     * @param listadoTotalHoras
     * @return
     */
    public ArrayList<Hora> listadoHorasFinales(ArrayList<Hora> listadoTotalHoras) {
        ArrayList<Hora> lhf = new ArrayList<Hora>();

        for (int i = 0; i < listadoTotalHoras.size(); i++) {
            if (i % 2 != 0) {
                lhf.add(listadoTotalHoras.get(i));
            }
        }
        return lhf;
    }

    public void crearListadoDepuradoDocentes() {
        LISTADODEPURADODOCENTES = new ArrayList<Docente>();
        ArrayList<String> listadoDocentes = new ArrayList<String>();
        for (int i = 0; i < LISTADOOFERTAEDUCATIVA_DOCENTE.size(); i++) {
            String docente = LISTADOOFERTAEDUCATIVA_DOCENTE.get(i).trim();
            if (!docente.trim().equals("")) {
                if (listadoDocentes.indexOf(docente) == -1) {
                    listadoDocentes.add(docente);
                    LISTADODEPURADODOCENTES.add(new Docente(docente));
                }
            }
        }
    }

    public void crearListadoDepuradoGrupos() {
        LISTADODEPURADOGRUPOSCADENA = new ArrayList<String>();
        ArrayList<String> listadoGrupos = new ArrayList<String>();

        for (int i = 0; i < LISTADOOFERTAEDUCATIVA_DOCENTE.size(); i++) {
            String programa = LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i);
            String semestre = LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i);

            String grupo = programa + " " + semestre;

            if (!programa.trim().equals("") && !semestre.trim().equals("")) {
                if (listadoGrupos.indexOf(grupo) == -1) {
                    listadoGrupos.add(grupo);
                    LISTADODEPURADOGRUPOSCADENA.add(grupo);
                }
            }
        }
    }

    public void crearListadoDepuradoTotalMinutosSesionesGrupo() {
        LISTADODEPURADOTOTALMINUTOSSESIONESGRUPO = new ArrayList<Integer>();

        for (int i = 0; i < LISTADODEPURADOGRUPOSCADENA.size(); i++) {
            int cantidadMinutos = 0;
            for (int j = 0; j < LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); j++) {

                if (!esFilaVirtual(j)) {
                    String programa = LISTADOOFERTAEDUCATIVA_PROGRAMA.get(j);
                    String semestre = LISTADOOFERTAEDUCATIVA_SEMESTRE.get(j);
                    String grupo = programa + " " + semestre;

                    if (grupo.trim().equals(LISTADODEPURADOGRUPOSCADENA.get(i))) {
                        int duracionSesion = LISTADOOFERTAEDUCATIVA_DURACIONIDEALSESION.get(j);
                        int numeroSesiones = LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(j);

                        cantidadMinutos = cantidadMinutos + duracionSesion * numeroSesiones;
                    }
                }

            }

            LISTADODEPURADOTOTALMINUTOSSESIONESGRUPO.add(cantidadMinutos);
        }

    }

    public void crearListadoDepuradoCreditosDocente() {
        LISTADODEPURADOCREDITOSDOCENTE = new ArrayList<Integer>();

        for (int i = 0; i < LISTADODEPURADODOCENTES.size(); i++) {
            LISTADODEPURADOCREDITOSDOCENTE.add(0);

            for (int j = 0; j < LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); j++) {
                String docente = LISTADODEPURADODOCENTES.get(i).getNombre();
                if (docente.equals(LISTADOOFERTAEDUCATIVA_DOCENTE.get(j))) {
                    LISTADODEPURADOCREDITOSDOCENTE.set(i, LISTADODEPURADOCREDITOSDOCENTE.get(i) + LISTADOOFERTAEDUCATIVA_CREDITOS.get(j));
                }
            }
        }
    }

    public void crearListadoDepuradoCreditosDocenteSinRepetirCompartidos() {
        LISTADODEPURADOCREDITOSDOCENTESINREPETIRCOMPARTIDOS = new ArrayList<Integer>();

        for (int i = 0; i < LISTADODEPURADODOCENTES.size(); i++) {
            LISTADODEPURADOCREDITOSDOCENTESINREPETIRCOMPARTIDOS.add(0);
            ArrayList<String> listadoCrCom = new ArrayList<String>();

            for (int j = 0; j < LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); j++) {
                String docente = LISTADODEPURADODOCENTES.get(i).getNombre();
                if (docente.equals(LISTADOOFERTAEDUCATIVA_DOCENTE.get(j))) {

                    if (esFilaCruzadaCompartida(j)) {
                        if (listadoCrCom.indexOf(LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(i)) == -1) {
                            listadoCrCom.add(LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(i));
                            LISTADODEPURADOCREDITOSDOCENTESINREPETIRCOMPARTIDOS.set(i, LISTADODEPURADOCREDITOSDOCENTESINREPETIRCOMPARTIDOS.get(i) + LISTADOOFERTAEDUCATIVA_CREDITOS.get(j));
                        }
                    } else {
                        LISTADODEPURADOCREDITOSDOCENTESINREPETIRCOMPARTIDOS.set(i, LISTADODEPURADOCREDITOSDOCENTESINREPETIRCOMPARTIDOS.get(i) + LISTADOOFERTAEDUCATIVA_CREDITOS.get(j));
                    }

                }
            }
        }
    }
    
    public void crearListadoDepuradoSesionesDocenteSinRepetirCompartidos() {
        LISTADODEPURADOSESIONESDOCENTESINREPETIRCOMPARTIDOS = new ArrayList<Integer>();

        for (int i = 0; i < LISTADODEPURADODOCENTES.size(); i++) {
            LISTADODEPURADOSESIONESDOCENTESINREPETIRCOMPARTIDOS.add(0);
            ArrayList<String> listadoCrCom = new ArrayList<String>();

            for (int j = 0; j < LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); j++) {
                String docente = LISTADODEPURADODOCENTES.get(i).getNombre();
                if (docente.equals(LISTADOOFERTAEDUCATIVA_DOCENTE.get(j))) {

                    if (esFilaCruzadaCompartida(j)) {
                        if (listadoCrCom.indexOf(LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(i)) == -1) {
                            listadoCrCom.add(LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(i));
                            LISTADODEPURADOSESIONESDOCENTESINREPETIRCOMPARTIDOS.set(i, LISTADODEPURADOSESIONESDOCENTESINREPETIRCOMPARTIDOS.get(i) + LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(j));
                        }
                    } else {
                        LISTADODEPURADOSESIONESDOCENTESINREPETIRCOMPARTIDOS.set(i, LISTADODEPURADOSESIONESDOCENTESINREPETIRCOMPARTIDOS.get(i) + LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(j));
                    }
                }
            }
        }
    }

    public void crearListadoDepuradoSesionesDocente() {
        LISTADODEPURADOSESIONESDOCENTE = new ArrayList<Integer>();

        for (int i = 0; i < LISTADODEPURADODOCENTES.size(); i++) {
            LISTADODEPURADOSESIONESDOCENTE.add(0);

            for (int j = 0; j < LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); j++) {
                //no se suman las sesiones si la fila es virtual, y cruzada.
                //o bien, sólo se cuentan las sesiones presenciales y las materias compartidas
                if (LISTADOOFERTAEDUCATIVA_VIRTUAL.get(j).trim().toLowerCase().equals("no")
                        && !esFilaCruzadaCompartida(j)) {
                    String docente = LISTADODEPURADODOCENTES.get(i).getNombre();
                    if (docente.equals(LISTADOOFERTAEDUCATIVA_DOCENTE.get(j))) {
                        LISTADODEPURADOSESIONESDOCENTE.set(i, LISTADODEPURADOSESIONESDOCENTE.get(i) + LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(j));
                    }
                }
            }
        }
    }

    /**
     * Esta función crea el listado de docentes depurados, ordenando de mayor a
     * menor el número de créditos. En el índice cero va el docente con mayor
     * cantidad de créditos
     */
    public void crearListadoOrdenadoDepuradoDocentesCreditos() {
        LISTADOORDENADODEPURADODOCENTESCREDITOS = new ArrayList<Docente>();
        LISTADOORDENADODEPURADOCREDITOSDOCENTE = new ArrayList<Integer>();

        ArrayList<Integer> listadoCreditosAuxiliar = new ArrayList<Integer>(LISTADODEPURADOCREDITOSDOCENTE);
        ArrayList<Docente> listadoDocentesAuxiliar = new ArrayList<Docente>(LISTADODEPURADODOCENTES);

        for (int i = 0; i < LISTADODEPURADOCREDITOSDOCENTE.size(); i++) {
            int maximo = Collections.max(listadoCreditosAuxiliar);
            LISTADOORDENADODEPURADOCREDITOSDOCENTE.add(maximo);

            int indiceMaximo = listadoCreditosAuxiliar.indexOf(maximo);

            LISTADOORDENADODEPURADODOCENTESCREDITOS.add(listadoDocentesAuxiliar.get(indiceMaximo));

            listadoCreditosAuxiliar.remove(indiceMaximo);
            listadoDocentesAuxiliar.remove(indiceMaximo);
        }
    }

    /**
     * Esta función crea el listado de docentes depurados, ordenando de mayor a
     * menor el número de sesiones total del docente que sean no virtuales. En
     * el índice cero va el docente con mayor cantidad de sesiones
     */
    public void crearListadoOrdenadoDepuradoDocentesSesiones() {
        LISTADOORDENADODEPURADODOCENTESSESIONES = new ArrayList<Docente>();
        LISTADOORDENADODEPURADOSESIONESDOCENTE = new ArrayList<Integer>();

        ArrayList<Integer> listadoSesionesAuxiliar = new ArrayList<Integer>(LISTADODEPURADOSESIONESDOCENTE);
        ArrayList<Docente> listadoDocentesAuxiliar = new ArrayList<Docente>(LISTADODEPURADODOCENTES);

        for (int i = 0; i < LISTADODEPURADOSESIONESDOCENTE.size(); i++) {
            int maximo = Collections.max(listadoSesionesAuxiliar);
            LISTADOORDENADODEPURADOSESIONESDOCENTE.add(maximo);

            int indiceMaximo = listadoSesionesAuxiliar.indexOf(maximo);

            LISTADOORDENADODEPURADODOCENTESSESIONES.add(listadoDocentesAuxiliar.get(indiceMaximo));

            listadoSesionesAuxiliar.remove(indiceMaximo);
            listadoDocentesAuxiliar.remove(indiceMaximo);
        }
    }

    /**
     * Esta fubcinón genera unos indices con orden de asignación según el
     * docente que tiene el mayor número de créditos hacia abajo
     */
    public void crearListadoIndicesOrdenAsignacionMayorCreditos() {
        LISTADOINDICESORDENASIGNACIONCREDITOS = new ArrayList<Integer>();

        for (int i = 0; i < LISTADOORDENADODEPURADODOCENTESCREDITOS.size(); i++) {

            for (int j = 0; j < LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); j++) {
                String nombreDocente = LISTADOORDENADODEPURADODOCENTESCREDITOS.get(i).getNombre();

                if (nombreDocente.trim().equals(LISTADOOFERTAEDUCATIVA_DOCENTE.get(j).trim())
                        && !esFilaVirtual(j)
                        && !esFilaCruzadaCompartida(j)) {
                    LISTADOINDICESORDENASIGNACIONCREDITOS.add(j);
                }
            }
        }
    }

    /**
     * Esta fubcinón genera unos indices con orden de asignación según el
     * docente que tiene el mayor número de sesiones hacia abajo
     */
    public void crearListadoIndicesOrdenAsignacionMayorSesiones() {
        LISTADOINDICESORDENASIGNACIONSESIONES = new ArrayList<Integer>();
        for (int i = 0; i < LISTADOORDENADODEPURADODOCENTESSESIONES.size(); i++) {
            for (int j = 0; j < LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); j++) {
                String nombreDocente = LISTADOORDENADODEPURADODOCENTESSESIONES.get(i).getNombre();
                if (nombreDocente.trim().equals(LISTADOOFERTAEDUCATIVA_DOCENTE.get(j).trim())
                        && !esFilaVirtual(j)
                        && !esFilaCruzadaCompartida(j)) {
                    LISTADOINDICESORDENASIGNACIONSESIONES.add(j);
                }
            }
        }
    }

    public boolean esFilaVirtual(int i) {
        if (LISTADOOFERTAEDUCATIVA_VIRTUAL.get(i).trim().toLowerCase().equals("si")) {
            return true;
        }
        return false;
    }

    public boolean esFilaCruzadaCompartida(int i) {
        if (LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(i).trim().equals("")) {
            return false;
        }
        return true;
    }

    /**
     * Esta función toma la oferta educativa cargado en los
     * LISTADOOFERTAEDUCATIVA y busca los indices para el nombre de un prfesor
     * dado Los índices corresponden a materias presenciales y no compartidas
     *
     * @param nombre
     * @return
     */
    public ArrayList<Integer> listadoIndicesDocenteDado(String nombre) {
        ArrayList<Integer> listadoindices = new ArrayList<Integer>();

        for (int i = 0; i < LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            if (LISTADOOFERTAEDUCATIVA_DOCENTE.get(i).trim().equals(nombre)
                    && !esFilaVirtual(i)
                    && !esFilaCruzadaCompartida(i)) {
                listadoindices.add(i);
            }
        }

        return listadoindices;
    }

    public ArrayList<Integer> listadoIndicesJornadaContengaCadena(String subCadena) {
        ArrayList<Integer> listadoindices = new ArrayList<Integer>();

        for (int i = 0; i < LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            if (LISTADOOFERTAEDUCATIVA_JORNADA.get(i).trim().indexOf(subCadena) != -1
                    && !esFilaVirtual(i)
                    && !esFilaCruzadaCompartida(i)) {
                listadoindices.add(i);
            }
        }

        return listadoindices;
    }

    public String obtenerJornadaCadenaGrupoCadenaDado(String cadenaGrupo) {
        String cadenaJornada = "";
        for (int i = 0; i < LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            String programa = LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i);
            String semestre = LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i);

            String grupo = programa + " " + semestre;
            if (grupo.equals(cadenaGrupo)) {
                return LISTADOOFERTAEDUCATIVA_JORNADA.get(i);
            }
        }
        return cadenaJornada;
    }

    public ArrayList<Sesion> cargaSesionesDesdeSalidaHorarios() {
        LISTADOCOORDENADASCELDASSESIONES = new ArrayList<String>();
        ArrayList<Sesion> listadoSesiones = new ArrayList<Sesion>();
        try {
            FileInputStream fis = new FileInputStream(RUTAARCHIVOOFERTAEDUCATIVA);
            XSSFWorkbook libroHorarios = new XSSFWorkbook(fis);
            XSSFSheet hojaHorarios = libroHorarios.getSheet(NOMBREHOJAOFERTAEDUCATIVA);
            XSSFRow filaEncabezadosNombresDias = hojaHorarios.getRow(FILAENCABEZADOSNOMBRESDIAS);

            int contadorFilas = FILAINICIALENCABEZADOSOFERTAEDUCATIVA + 1;
            XSSFRow filaEncabezadosDias = hojaHorarios.getRow(FILAINICIALENCABEZADOSOFERTAEDUCATIVA);
            XSSFRow filaContadora = hojaHorarios.getRow(contadorFilas);

            while (!celdaVacia(filaContadora, 0)) {
                int contadorColumnas = INDICECOLUMNAINICIANFECHAS;
                while (!celdaVacia(filaEncabezadosNombresDias, contadorColumnas)) {
                    //recorrido de la región donde se colocan las sesiones
                    if (!celdaVacia(filaContadora, contadorColumnas)) {
                        XSSFCell celda = filaContadora.getCell(contadorColumnas);
                        //Sesion(Docente docente, Grupo grupo, Asignatura asignatura, Date fecha, int duracion)

                        //por el momento la sesion solo se carga si la celda está bien formateada a fecha
                        //queda pendiente que tome otros datos como la palabra "virtual" y el formato hh:mm / hh:mm
                        if (celda.getCellTypeEnum() == CellType.NUMERIC && DateUtil.isCellDateFormatted(celda)) {
                            int filaContadoraListadosOfertaEducativa = contadorFilas - (FILAINICIALENCABEZADOSOFERTAEDUCATIVA + 1);

                            Docente docente = new Docente(LISTADOOFERTAEDUCATIVA_DOCENTE.get(filaContadoraListadosOfertaEducativa));
                            Grupo grupo = new Grupo(LISTADOOFERTAEDUCATIVA_PROGRAMA.get(filaContadoraListadosOfertaEducativa),
                                    LISTADOOFERTAEDUCATIVA_SEMESTRE.get(filaContadoraListadosOfertaEducativa),
                                    seleccionarJornadaNombreDado(LISTADOJORNADAS, LISTADOOFERTAEDUCATIVA_JORNADA.get(filaContadoraListadosOfertaEducativa)));
                            Asignatura asignatura = new Asignatura(LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(filaContadoraListadosOfertaEducativa),
                                    LISTADOOFERTAEDUCATIVA_ALFA.get(filaContadoraListadosOfertaEducativa),
                                    LISTADOOFERTAEDUCATIVA_NUMERICO.get(filaContadoraListadosOfertaEducativa),
                                    LISTADOOFERTAEDUCATIVA_CREDITOS.get(filaContadoraListadosOfertaEducativa),
                                    LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(filaContadoraListadosOfertaEducativa),
                                    LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(filaContadoraListadosOfertaEducativa));
                            Date fecha = fechaSesionDesdeCeldaIdealHorarios(celda);
                            int duracion = -2;
                            if (contadorFilas == 47) {
                                System.out.println("");
                            }
                            if (esFilaCompartida(this, filaContadoraListadosOfertaEducativa)) {
                                duracion = mayorDuracionCompartida(filaContadoraListadosOfertaEducativa);
                            } else {
                                duracion = LISTADOOFERTAEDUCATIVA_DURACIONIDEALSESION.get(filaContadoraListadosOfertaEducativa);
                            }

                            listadoSesiones.add(new Sesion(docente, grupo, asignatura, fecha, duracion));
                            LISTADOCOORDENADASCELDASSESIONES.add(celda.getAddress().formatAsString());
                        }
                    }

                    contadorColumnas++;
                }
                contadorFilas++;
                filaContadora = hojaHorarios.getRow(contadorFilas);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listadoSesiones;
    }

    public Jornada seleccionarJornadaNombreDado(ArrayList<Jornada> listadoJornadas, String nombreJornada) {
        Jornada jornada = null;
        for (int i = 0; i < listadoJornadas.size(); i++) {
            if (listadoJornadas.get(i).getNombre().toLowerCase().trim().equals(nombreJornada.toLowerCase().trim())) {
                return listadoJornadas.get(i);
            }
        }
        if(jornada==null){
            ofertaeducativa.Validaciones.mostrarVentanaError("No se encuentra la jornada llamada "+nombreJornada);
            System.out.println("Error al seleccionar la jornada llamada "+nombreJornada);
        }
        return jornada;
    }

    public Date fechaSesionDesdeCeldaIdealHorarios(XSSFCell celda) {
        int columnaFecha = celda.getColumnIndex();
        XSSFCell celdaDia = celda.getSheet().getRow(FILAINICIALENCABEZADOSOFERTAEDUCATIVA).getCell(columnaFecha);

        Date fecha = celdaDia.getDateCellValue();

        Calendar c = Calendar.getInstance();
        c.setTime(fecha);

        Date horaFecha = celda.getDateCellValue();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(horaFecha);

        c.set(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, c2.get(Calendar.MINUTE));

        return c.getTime();
    }

    public int mayorDuracionCompartida(int filaOfertaEducativa) {
        int duracion = -1;
        if (esFilaCompartida(this, filaOfertaEducativa)) {
            String idCompartida = this.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(filaOfertaEducativa);
            ArrayList<Integer> listadoIndices = indicesCruzadoCompartido(this.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO,
                    idCompartida);
            ArrayList<Integer> listadoDuraciones = new ArrayList<Integer>();
            for (int i = 0; i < listadoIndices.size(); i++) {
                listadoDuraciones.add(this.LISTADOOFERTAEDUCATIVA_DURACIONIDEALSESION.get(listadoIndices.get(i)));
            }
            return Collections.max(listadoDuraciones);
        }
        return duracion;
    }

    public boolean esFilaCompartida(CargaDeDatosExcel cde, int indice) {
        boolean cruzada = false;

        if (cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(indice).toLowerCase().indexOf(CADENACOMPARTIDA) != -1) {
            return true;
        }

        return cruzada;
    }

    /**
     * Función que toma como argumentos el listado con todos los
     * idcruzadoscompartidos y la cadena que corresponde al cruzadocompartido
     * que se va a buscar en la lista
     *
     * @param listadoCruzadaCompartida
     * @param cadenaCruzadoCompartido
     * @return ArrayList<Integer> con los índices de los que están cruzados o
     * compartidos con el índice dada
     */
    public ArrayList<Integer> indicesCruzadoCompartido(ArrayList<String> listadoCruzadaCompartida, String cadenaCruzadoCompartido) {

        ArrayList<Integer> indices = new ArrayList<Integer>();

        for (int i = 0; i < listadoCruzadaCompartida.size(); i++) {
            if (listadoCruzadaCompartida.get(i).equals(cadenaCruzadoCompartido)) {
                indices.add(i);
            }
        }

        return indices;

    }

}
