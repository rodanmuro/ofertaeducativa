/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author rodanmuro
 */
public class RevisionHorariosEscritosLibro {
    
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

    int INDICE_FILAS_NOMBRES_DIAS = 0;
    int INDICE_FILA_FECHAS = 1;

    int COLUMNA_PROGRAMA = 0;
    int COLUMNA_SEMESTRE = 1;
    int COLUMNA_ASIGNATURA = 8;
    int COLUMNA_DOCENTE = 11;

    int SEMESTRE_CLASES_CORTAS = 6;

    int MAXIMO_SALONES_MARTES_Y_JUEVES = 22;
    int MAXIMO_SALONES_MIERCOLES_Y_VIERNES = 22;
    int MAXIMO_SALONES_SABADO = 100;//NÚMERO HIPOTÉTICO 

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

    ArrayList<Integer> listadoBaseAsginaturasIdCarreras;
    ArrayList<Integer> listadoBaseAsginaturasIdSemestre;
    ArrayList<String> listadoBaseAsginaturasAlfa;
    ArrayList<String> listadoBaseAsginaturasNumerico;
    ArrayList<Integer> listadoBaseAsginaturasCredito;
    ArrayList<String> listadoBaseAsginaturasSemana;

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

    public RevisionHorariosEscritosLibro(String libroOferta, String mallaBase) {
        inicializarConstantes(libroOferta);
        cargarMallaBase(mallaBase);
        cargarOfertaEducativa(libroOferta);
        
        segundaRevisionSesionesEscritasEnElLibro();
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

        listadoBaseAsginaturasAlfa = new ArrayList<String>();
        listadoBaseAsginaturasCredito = new ArrayList<Integer>();
        listadoBaseAsginaturasIdCarreras = new ArrayList<Integer>();
        listadoBaseAsginaturasIdSemestre = new ArrayList<Integer>();
        listadoBaseAsginaturasNumerico = new ArrayList<String>();
        listadoBaseAsginaturasSemana = new ArrayList<String>();
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
                        sesionesProgramadas++;
                    }
                    contadorColumnas++;
                }

                //la salida de los errores luego de la revisión del libro final
                //no se escribe el libro, por el momento sólo se hace en salida de consola
                if (sesionesProgramadas < sesionesIdeales && !programa.equals("EGPR")) {
                    System.out.println((contadorFilas+1) + " ERROR, " + "SESIONES PROGRAMADAS " + sesionesProgramadas
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
}
