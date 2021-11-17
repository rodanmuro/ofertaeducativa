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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author rodanmuro
 */
public class PasarABelloCuatrimestre {

    int COLUMNA_OFERTA_PROGRAMA = 0;
    int COLUMNA_OFERTA_JORNADA = 4;
    int COLUMNA_OFERTA_ALFA = 5;
    int COLUMNA_OFERTA_NUMERICO = 6;
    int COLUMNA_OFERTA_CRUZADOSCOMPARTIDOS = 13;
    int COLUMNA_OFERTA_NRC = 9;
    int COLUMNA_OFERTA_DOCENTE = 11;
    int COLUMNA_OFERTA_INICIAL_FECHAS = 15;
    int COLUMNA_OFERTA_VIRTUAL = 12;
    int COLUMNA_OFERTA_CUPO = 10;

    String HOJA_OFERTA_NOMBRE = "Oferta educativa";

    int FILA_INICIAL_OFERTA = 2;
    int FILA_INICIAL_OFERTA_CUATRIMESTRAL = 786;

    int COLUMNA_BELLO_PONER_ADMINISTRADOR = 0;
    int COLUMNA_BELLO_PONER_ALFAMERICO = 2;
    int COLUMNA_BELLO_PONER_NRC = 8;
    int COLUMNA_BELLO_PONER_JORNADA = 9;
    int COLUMNA_BELLO_PONER_FECHA1 = 12;
    int COLUMNA_BELLO_PONER_CUPO = 25;

    int COLUMNA_BELLO_PONER_ADMINISTRADOR_CRUZADA1 = 28;
    int COLUMNA_BELLO_PONER_ALFAMERICO_CRUZADA1 = 29;
    int COLUMNA_BELLO_PONER_CUPO_CRUZADA1 = 32;
    int COLUMNA_BELLO_PONER_NRC_CRUZADA1 = 33;

    int COLUMNA_BELLO_PONER_ADMINISTRADOR_CRUZADA2 = 34;
    int COLUMNA_BELLO_PONER_ALFAMERICO_CRUZADA2 = 35;
    int COLUMNA_BELLO_PONER_CUPO_CRUZADA2 = 38;
    int COLUMNA_BELLO_PONER_NRC_CRUZADA2 = 39;

    int COLUMNA_BELLO_PONER_ADMINISTRADOR_CRUZADA3 = 40;
    int COLUMNA_BELLO_PONER_ALFAMERICO_CRUZADA3 = 41;
    int COLUMNA_BELLO_PONER_CUPO_CRUZADA3 = 44;
    int COLUMNA_BELLO_PONER_NRC_CRUZADA3 = 45;

    int COLUMNA_BELLO_PONER_DOCENTE = 47;

    int COLUMNA_BELLO_TOMAR_ALFA_NUMERICO = 0;
    int COLUMNA_BELLO_TOMAR_ADMINISTRADOR = 8;
    int COLUMNA_BELLO_TOMAR_PROGRAMA = 9;
    int COLUMNA_BELLO_PONER_FECHAFINAL = 11;

    int FILA_INICIAL_BELLO_PONER_DATOS = 6;
    int FILA_INICIAL_BELLO_TOMAR_DATOS = 1;
    int FILA_OFERTA_FECHAS = 1;

    String HOJA_BELLO_PONER_DATOS_NOMBRE = "PLANTILLA DISTANCIA";
    String HOJA_BELLO_TOMAR_DATOS_NOMBRE = "PLAN DE ESTUDIO DISTANCIA";

    String rutaOfertaEducativa;
    String RUTA_SALIDA_ARCHIVO_SEGUIMIENTOS;
    String RUTA_BELLO;

    ArrayList<String> listadoOfertaAlfa;
    ArrayList<String> listadoOfertaNumerico;

    ArrayList<String> listadoOfertaPrograma;
    ArrayList<String> listadoOfertaJornada;
    ArrayList<String> listadoOfertaAlfaNumerico;
    ArrayList<String> listadoOfertaNRC;
    ArrayList<String> listadoOfertaVirtual;
    ArrayList<String> listadoOfertaDocente;
    ArrayList<String> listadoOfertaCruzadosCompartidos;
    ArrayList<ArrayList<Date>> listadoOfertaSesiones;
    ArrayList<ArrayList<Date>> listadoOfertaSesionesHora;
    ArrayList<String> listadoOfertaCupos;
    ArrayList<String> listadoOfertaYaPaso;

    ArrayList<String> listadoBelloAlfaNumerico;
    ArrayList<String> listadoBelloAdministrador;
    ArrayList<String> listadoBelloProgramas;

    XSSFWorkbook libroOFertaEducativa;
    XSSFWorkbook libroBello;

    XSSFSheet HOJA_OFERTA;
    XSSFSheet HOJA_BELLO_PONER_DATOS;
    XSSFSheet HOJA_BELLO_TOMAR_DATOS;

    public PasarABelloCuatrimestre(String rutaMallaBase, String rutaOfertaEducativa, String rutaFormatoBello, String rutaSalida) {
        this.rutaOfertaEducativa = rutaOfertaEducativa;
        this.RUTA_BELLO = rutaFormatoBello;
        this.RUTA_SALIDA_ARCHIVO_SEGUIMIENTOS = rutaSalida;

        cargarArchivosWorkbook();

        HOJA_BELLO_PONER_DATOS = libroBello.getSheet(HOJA_BELLO_PONER_DATOS_NOMBRE);
        HOJA_BELLO_TOMAR_DATOS = libroBello.getSheet(HOJA_BELLO_TOMAR_DATOS_NOMBRE);

        cargarListadosOferta();
        cargarListadosBello();
        cargarSesionesOfertaFechaPorCadaFila();

        listadoOfertaAlfaNumerico = new ArrayList<String>();
        for (int i = 0; i < listadoOfertaAlfa.size(); i++) {
            listadoOfertaAlfaNumerico.add(listadoOfertaAlfa.get(i) + "" + listadoOfertaNumerico.get(i));
        }

        listadoOfertaYaPaso = new ArrayList<String>();
        for (int i = 0; i < listadoOfertaAlfaNumerico.size(); i++) {
            listadoOfertaYaPaso.add("No");
        }

        pasarDatosFormato();
        crearLibroEscribirDatos();

    }

    public void pasarDatosFormato() {
        int contadorFilasBello = 0;
        for (int i = 0; i < listadoOfertaAlfaNumerico.size(); i++) {
            String virtual = listadoOfertaVirtual.get(i);
            String jornada = retornarEncuentrosBello(listadoOfertaJornada.get(i));
            String programa = listadoOfertaPrograma.get(i);
            String alfaNumerico = listadoOfertaAlfaNumerico.get(i);
            String docente = listadoOfertaDocente.get(i);
            String cupo = listadoOfertaCupos.get(i);

            String ad = obtenerAdministadorAlfaNumericoPrograma(programa, alfaNumerico);
            String nrc = listadoOfertaNRC.get(i);
            XSSFRow fila = HOJA_BELLO_PONER_DATOS.getRow(contadorFilasBello + FILA_INICIAL_BELLO_PONER_DATOS);

            if (virtual.equals("No")) {
                if (esCruzada(i)) {

                    if (listadoOfertaYaPaso.get(i).equals("No")) {

                        colocarValoresFilaBello(fila, ad, alfaNumerico, nrc, jornada, docente, cupo);
                        pasar10PrimerasSesionesFilaBello(fila, i);

                        //COLOCAMOS LA FECHA FINAL
                        fila.getCell(COLUMNA_BELLO_PONER_FECHAFINAL).setCellValue(listadoOfertaSesiones.get(i).get(listadoOfertaSesiones.get(i).size() - 1));

                        listadoOfertaYaPaso.set(i, "Si");

                        int cupoCruzada1 = 0;
                        int cupoCruzada2 = 0;
                        int cupoTotal = 0;

                        cupoTotal = Integer.parseInt(cupo);

                        ArrayList<Integer> indices = indicesCompartido(listadoOfertaCruzadosCompartidos.get(i));
                        for (int j = 0; j < indices.size(); j++) {
                            listadoOfertaYaPaso.set(indices.get(j), "Si");

                            //acá se pasan en frente los cruzados
                            if (j == 1) {
                                programa = listadoOfertaPrograma.get(indices.get(j));
                                alfaNumerico = listadoOfertaAlfaNumerico.get(indices.get(j));
                                docente = listadoOfertaDocente.get(indices.get(j));
                                cupo = listadoOfertaCupos.get(indices.get(j));

                                ad = obtenerAdministadorAlfaNumericoPrograma(programa, alfaNumerico);
                                nrc = listadoOfertaNRC.get(indices.get(j));
                                fila = HOJA_BELLO_PONER_DATOS.getRow(contadorFilasBello + FILA_INICIAL_BELLO_PONER_DATOS);

                                fila.getCell(COLUMNA_BELLO_PONER_ADMINISTRADOR_CRUZADA1).setCellValue(ad);
                                fila.getCell(COLUMNA_BELLO_PONER_ALFAMERICO_CRUZADA1).setCellValue(alfaNumerico);
                                fila.getCell(COLUMNA_BELLO_PONER_NRC_CRUZADA1).setCellValue(nrc);
                                fila.getCell(COLUMNA_BELLO_PONER_CUPO_CRUZADA1).setCellValue(cupo);

                                cupoCruzada1 = Integer.parseInt(cupo);
                            }
                            if (j == 2) {
                                programa = listadoOfertaPrograma.get(indices.get(j));
                                alfaNumerico = listadoOfertaAlfaNumerico.get(indices.get(j));
                                docente = listadoOfertaDocente.get(indices.get(j));
                                cupo = listadoOfertaCupos.get(indices.get(j));

                                ad = obtenerAdministadorAlfaNumericoPrograma(programa, alfaNumerico);
                                nrc = listadoOfertaNRC.get(indices.get(j));
                                fila = HOJA_BELLO_PONER_DATOS.getRow(contadorFilasBello + FILA_INICIAL_BELLO_PONER_DATOS);

                                fila.getCell(COLUMNA_BELLO_PONER_ADMINISTRADOR_CRUZADA2).setCellValue(ad);
                                fila.getCell(COLUMNA_BELLO_PONER_ALFAMERICO_CRUZADA2).setCellValue(alfaNumerico);
                                fila.getCell(COLUMNA_BELLO_PONER_NRC_CRUZADA2).setCellValue(nrc);
                                fila.getCell(COLUMNA_BELLO_PONER_CUPO_CRUZADA2).setCellValue(cupo);

                                cupoCruzada2 = Integer.parseInt(cupo);
                            }

                        }
                        cupoTotal = cupoTotal + cupoCruzada1 + cupoCruzada2;
                        //en el caso del cupo se debe pasar el cupo total
                        fila.getCell(COLUMNA_BELLO_PONER_CUPO).setCellValue(cupoTotal);

                        contadorFilasBello++;
                    }

                } else {
                    //si no es cruzada
                    if (listadoOfertaYaPaso.get(i).equals("No")) {
                        colocarValoresFilaBello(fila, ad, alfaNumerico, nrc, jornada, docente, cupo);
                        pasar10PrimerasSesionesFilaBello(fila, i);

                        //COLOCAMOS LA FECHA FINAL
                        fila.getCell(COLUMNA_BELLO_PONER_FECHAFINAL).setCellValue(listadoOfertaSesiones.get(i).get(listadoOfertaSesiones.get(i).size() - 1));

                        listadoOfertaYaPaso.set(i, "Si");

                        if (esCompartida(i)) {
                            ArrayList<Integer> indices = indicesCompartido(listadoOfertaCruzadosCompartidos.get(i));
                            for (int j = 0; j < indices.size(); j++) {
                                listadoOfertaYaPaso.set(indices.get(j), "Si");
                            }
                        }
                        contadorFilasBello++;
                    }
                }

            }
        }
    }

    public void crearLibroEscribirDatos() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
            Date fechaYHoraSalida = new Date();
            String fechaSalidaFormateada = sdf.format(fechaYHoraSalida);

            FileOutputStream fos = new FileOutputStream(RUTA_SALIDA_ARCHIVO_SEGUIMIENTOS + "/formatoBello " + fechaSalidaFormateada + ".xlsx");
            libroBello.write(fos);
            libroBello.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean esCruzada(int id) {
        boolean c = false;
        if (listadoOfertaCruzadosCompartidos.get(id).indexOf("Cruzada") != -1) {
            return true;
        }
        return c;
    }

    public boolean esCompartida(int id) {
        boolean c = false;
        if (listadoOfertaCruzadosCompartidos.get(id).indexOf("Compartida") != -1) {
            return true;
        }
        return c;
    }

    public String obtenerAdministadorAlfaNumericoPrograma(String programa, String alfaNumerico) {
        String ad = "";

        for (int i = 0; i < listadoBelloAdministrador.size(); i++) {
            String programaBello = listadoBelloProgramas.get(i);
            String alfaNumericoBello = listadoBelloAlfaNumerico.get(i);

            if (programa.equals(programaBello) && alfaNumerico.equals(alfaNumericoBello)) {
                ad = listadoBelloAdministrador.get(i);
                return ad;
            }
        }

        //si luego de toda la búsqueda por programa, no se encontró coincidencia
        //entonces se hará búsqueda nuevamente sin tener en cuenta el programa
        for (int i = 0; i < listadoBelloAdministrador.size(); i++) {
            String alfaNumericoBello = listadoBelloAlfaNumerico.get(i);

            if (alfaNumerico.equals(alfaNumericoBello)) {
                ad = listadoBelloAdministrador.get(i);
                return ad;
            }
        }

        return ad;
    }

    public void cargarSesionesOfertaFechaPorCadaFila() {

        int contadorFilas = FILA_INICIAL_OFERTA_CUATRIMESTRAL;
        int contadorLista = 0;

        XSSFRow fila = HOJA_OFERTA.getRow(contadorFilas);
        listadoOfertaSesiones = new ArrayList<ArrayList<Date>>();
        listadoOfertaSesionesHora = new ArrayList<ArrayList<Date>>();

        while (!celdaVacia(fila, 0)) {
            ArrayList<Date> sesiones = new ArrayList<Date>();
            ArrayList<Date> sesionesHora = new ArrayList<Date>();

            String esVirtual = (String) retornarValor(fila.getCell(12));
            XSSFRow filaFechas = HOJA_OFERTA.getRow(FILA_OFERTA_FECHAS);
            XSSFRow filaFechaHora = fila;

            if (esVirtual.trim().equals("No")) {

                int contadorColumnas = COLUMNA_OFERTA_INICIAL_FECHAS;
                XSSFRow filaDias = HOJA_OFERTA.getRow(0);
                while (!celdaVacia(filaDias, contadorColumnas)) {
                    if (!celdaVacia(fila, contadorColumnas)) {
                        Date sesion = filaFechas.getCell(contadorColumnas).getDateCellValue();
                        Date sesionHora = new Date();
                        try {
                            if (celdaTieneFecha(filaFechaHora.getCell(contadorColumnas))) {
                                sesionHora = filaFechaHora.getCell(contadorColumnas).getDateCellValue();
                            } else {
                                if (laHoraTieneSlash(filaFechaHora.getCell(contadorColumnas))) {
                                    sesionHora = obtenerHorasSlash(filaFechaHora.getCell(contadorColumnas)).get(0);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        sesiones.add(sesion);
                        sesionesHora.add(sesionHora);
                    }
                    contadorColumnas++;
                }

            }

            listadoOfertaSesiones.add(sesiones);
            listadoOfertaSesionesHora.add(sesionesHora);

            contadorFilas++;
            contadorLista++;
            fila = HOJA_OFERTA.getRow(contadorFilas);
        }
    }

    public void cargarArchivosWorkbook() {
        try {
            FileInputStream fis1 = new FileInputStream(rutaOfertaEducativa);
            libroOFertaEducativa = new XSSFWorkbook(fis1);

            FileInputStream fis2 = new FileInputStream(RUTA_BELLO);
            libroBello = new XSSFWorkbook(fis2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarListadosOferta() {
        HOJA_OFERTA = libroOFertaEducativa.getSheet(HOJA_OFERTA_NOMBRE);

        listadoOfertaNRC = new ArrayList<String>();
        cargarColumnaEnListadoString(0,
                COLUMNA_OFERTA_NRC,
                FILA_INICIAL_OFERTA_CUATRIMESTRAL,
                libroOFertaEducativa,
                HOJA_OFERTA,
                listadoOfertaNRC);

        listadoOfertaAlfa = new ArrayList<String>();
        cargarColumnaEnListadoString(0,
                COLUMNA_OFERTA_ALFA,
                FILA_INICIAL_OFERTA_CUATRIMESTRAL,
                libroOFertaEducativa,
                HOJA_OFERTA,
                listadoOfertaAlfa);

        listadoOfertaNumerico = new ArrayList<String>();
        cargarColumnaEnListadoString(0,
                COLUMNA_OFERTA_NUMERICO,
                FILA_INICIAL_OFERTA_CUATRIMESTRAL,
                libroOFertaEducativa,
                HOJA_OFERTA,
                listadoOfertaNumerico);

        listadoOfertaNRC = new ArrayList<String>();
        cargarColumnaEnListadoString(0,
                COLUMNA_OFERTA_NRC,
                FILA_INICIAL_OFERTA_CUATRIMESTRAL,
                libroOFertaEducativa,
                HOJA_OFERTA,
                listadoOfertaNRC);

        listadoOfertaCruzadosCompartidos = new ArrayList<String>();
        cargarColumnaEnListadoString(0,
                COLUMNA_OFERTA_CRUZADOSCOMPARTIDOS,
                FILA_INICIAL_OFERTA_CUATRIMESTRAL,
                libroOFertaEducativa,
                HOJA_OFERTA,
                listadoOfertaCruzadosCompartidos);

        listadoOfertaVirtual = new ArrayList<String>();
        cargarColumnaEnListadoString(0,
                COLUMNA_OFERTA_VIRTUAL,
                FILA_INICIAL_OFERTA_CUATRIMESTRAL,
                libroOFertaEducativa,
                HOJA_OFERTA,
                listadoOfertaVirtual);

        listadoOfertaPrograma = new ArrayList<String>();
        cargarColumnaEnListadoString(0,
                COLUMNA_OFERTA_PROGRAMA,
                FILA_INICIAL_OFERTA_CUATRIMESTRAL,
                libroOFertaEducativa,
                HOJA_OFERTA,
                listadoOfertaPrograma);

        listadoOfertaJornada = new ArrayList<String>();
        cargarColumnaEnListadoString(0,
                COLUMNA_OFERTA_JORNADA,
                FILA_INICIAL_OFERTA_CUATRIMESTRAL,
                libroOFertaEducativa,
                HOJA_OFERTA,
                listadoOfertaJornada);

        listadoOfertaDocente = new ArrayList<String>();
        cargarColumnaEnListadoString(0,
                COLUMNA_OFERTA_DOCENTE,
                FILA_INICIAL_OFERTA_CUATRIMESTRAL,
                libroOFertaEducativa,
                HOJA_OFERTA,
                listadoOfertaDocente);

        listadoOfertaCupos = new ArrayList<String>();
        cargarColumnaEnListadoString(0,
                COLUMNA_OFERTA_CUPO,
                FILA_INICIAL_OFERTA_CUATRIMESTRAL,
                libroOFertaEducativa,
                HOJA_OFERTA,
                listadoOfertaCupos);
    }

    public void cargarListadosBello() {

        listadoBelloAdministrador = new ArrayList<String>();
        cargarColumnaEnListadoString(0,
                COLUMNA_BELLO_TOMAR_ADMINISTRADOR,
                FILA_INICIAL_BELLO_TOMAR_DATOS,
                libroBello,
                libroBello.getSheet(HOJA_BELLO_TOMAR_DATOS_NOMBRE),
                listadoBelloAdministrador);

        listadoBelloAlfaNumerico = new ArrayList<String>();
        cargarColumnaEnListadoString(0,
                COLUMNA_BELLO_TOMAR_ALFA_NUMERICO,
                FILA_INICIAL_BELLO_TOMAR_DATOS,
                libroBello,
                libroBello.getSheet(HOJA_BELLO_TOMAR_DATOS_NOMBRE),
                listadoBelloAlfaNumerico);

        listadoBelloProgramas = new ArrayList<String>();
        cargarColumnaEnListadoString(0,
                COLUMNA_BELLO_TOMAR_PROGRAMA,
                FILA_INICIAL_BELLO_TOMAR_DATOS,
                libroBello,
                libroBello.getSheet(HOJA_BELLO_TOMAR_DATOS_NOMBRE),
                listadoBelloProgramas);

    }

    public void cargarColumnaEnListadoString(int columnaContadora,
            int columnaCargar,
            int indiceFilaInicial,
            XSSFWorkbook libro,
            XSSFSheet hoja,
            ArrayList<String> lista) {

        int contadorFilas = indiceFilaInicial;
        XSSFRow fila = hoja.getRow(contadorFilas);
        while (!celdaVacia(fila, columnaContadora)) {

            if (celdaVacia(fila, columnaCargar)) {
                lista.add("");
            } else {
                lista.add("" + retornarValor(fila.getCell(columnaCargar)));
            }

            contadorFilas++;
            fila = hoja.getRow(contadorFilas);
        }
    }

    public void cargarColumnaEnListadointeger(int columnaContadora,
            int columnaCargar,
            int indiceFilaInicial,
            XSSFWorkbook libro,
            XSSFSheet hoja,
            ArrayList<Integer> lista) {

        int contadorFilas = indiceFilaInicial;
        XSSFRow fila = hoja.getRow(contadorFilas);
        while (!celdaVacia(fila, columnaContadora)) {

            lista.add((int) retornarValor(fila.getCell(columnaCargar)));

            contadorFilas++;
            fila = hoja.getRow(contadorFilas);
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

    public String retornarEncuentrosBello(String jornada) {
        String e = "";

        if (jornada.indexOf("SÁBADO") != -1) {
            e = "SÁBADOS";
        } else {
            e = "SEMANA";
        }

        return e;
    }

    public ArrayList<Integer> indicesCompartido(String compartido) {
        ArrayList<Integer> indices = new ArrayList<Integer>();

        for (int i = 0; i < listadoOfertaCruzadosCompartidos.size(); i++) {
            if (listadoOfertaCruzadosCompartidos.get(i).equals(compartido)) {
                indices.add(i);
            }
        }

        return indices;
    }

    public void agregarComentario(XSSFWorkbook wb, XSSFSheet sheet, XSSFRow row, XSSFCell cell, String mensaje) {

        CreationHelper factory = wb.getCreationHelper();

        Drawing drawing = sheet.createDrawingPatriarch();

        // When the comment box is visible, have it show in a 1x3 space
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(cell.getColumnIndex());
        anchor.setCol2(cell.getColumnIndex() + 1);
        anchor.setRow1(row.getRowNum());
        anchor.setRow2(row.getRowNum() + 3);

        // Create the comment and set the text+author
        Comment comment = drawing.createCellComment(anchor);
        RichTextString str = factory.createRichTextString(mensaje);
        comment.setString(str);
        comment.setAuthor("Coordinador");

        // Assign the comment to the cell
        cell.setCellComment(comment);
    }

    public Date fusionarFechaHora(Date f1, Date f2) {
        Date f = new Date();

        Calendar c1 = Calendar.getInstance();
        c1.setTime(f1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(f2);

        Calendar c3 = Calendar.getInstance();
        c3.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DATE), c2.get(Calendar.HOUR_OF_DAY), c2.get(Calendar.MINUTE), 0);

        f = c3.getTime();

        return f;
    }

    public void colocarValoresFilaBello(XSSFRow fila,
            String ad,
            String alfaNumerico,
            String nrc,
            String jornada,
            String docente,
            String cupo) {
        fila.getCell(COLUMNA_BELLO_PONER_ADMINISTRADOR).setCellValue(ad);
        fila.getCell(COLUMNA_BELLO_PONER_ALFAMERICO).setCellValue(alfaNumerico);
        fila.getCell(COLUMNA_BELLO_PONER_NRC).setCellValue(nrc);
        fila.getCell(COLUMNA_BELLO_PONER_JORNADA).setCellValue(jornada);
        fila.getCell(COLUMNA_BELLO_PONER_DOCENTE).setCellValue(docente);
        fila.getCell(COLUMNA_BELLO_PONER_CUPO).setCellValue(Integer.parseInt(cupo));
    }

    public void pasar10PrimerasSesionesFilaBello(XSSFRow fila, int indiceFilaListadoOferta) {

        for (int j = 0; j < 10; j++) {
            if (j > listadoOfertaSesiones.get(indiceFilaListadoOferta).size() - 1) {
                break;
            }
            fila.getCell(COLUMNA_BELLO_PONER_FECHA1 + j).setCellValue(listadoOfertaSesiones.get(indiceFilaListadoOferta).get(j));
            Date fechaFusionada = fusionarFechaHora(listadoOfertaSesiones.get(indiceFilaListadoOferta).get(j),
                    listadoOfertaSesionesHora.get(indiceFilaListadoOferta).get(j));
            agregarComentario(libroBello,
                    HOJA_BELLO_PONER_DATOS,
                    fila,
                    fila.getCell(COLUMNA_BELLO_PONER_FECHA1 + j),
                    fechaFusionada.toString());
        }

    }

    public boolean laHoraTieneSlash(XSSFCell celda) {

        boolean si = false;

        if (celda.getStringCellValue().indexOf("/") != -1) {
            si = true;
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

    public Date retornarFechaHoraSesion(XSSFCell celda) {
        int columna = celda.getColumnIndex();

        Date fechaEnCelda = celda.getDateCellValue();
        Calendar cFechaEnCelda = Calendar.getInstance();
        cFechaEnCelda.setTime(fechaEnCelda);

        int hora = cFechaEnCelda.get(Calendar.HOUR_OF_DAY);
        int minutos = cFechaEnCelda.get(Calendar.MINUTE);

        Date fechaEnFilaFechas = libroOFertaEducativa
                .getSheet(HOJA_OFERTA_NOMBRE)
                .getRow(FILA_OFERTA_FECHAS)
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

        Date fechaEnFilaFechas = libroOFertaEducativa
                .getSheet(HOJA_OFERTA_NOMBRE)
                .getRow(FILA_OFERTA_FECHAS)
                .getCell(columna).getDateCellValue();

        Calendar cFechaEnFilasFechas = Calendar.getInstance();
        cFechaEnFilasFechas.setTime(fechaEnFilaFechas);
        cFechaEnFilasFechas.set(Calendar.HOUR_OF_DAY, hora);
        cFechaEnFilasFechas.set(Calendar.MINUTE, minutos);

        Date fechayHoraSesion = cFechaEnFilasFechas.getTime();

        return fechayHoraSesion;
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
}
