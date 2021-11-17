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
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataConsolidateFunction;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Usuario
 */
public class CrearArchivosSalida {

    CalendarioTotalSemestre CTS;
    CargaDeDatosExcel CDE;
    Horario H;
    XSSFWorkbook LIBROSALIDA;
    int SESIONESAPROGRAMAR = 0;
    int SESIONESPROGRAMADAS = 0;
    double EFECTIVIDADHORARIO = 0;
    ArrayList<String> LISTADONOMBRESDIASTODOSEMESTRE;
    ArrayList<Date> LISTADOFECHASDIASDISTINTOSTODOSEMESTRE;
    ArrayList<Date> LISTADOFECHASORDENPUESTOEXCEL;
    int INDICECOLUMNAINICIANFECHAS = 18;
    String NOMBREHOJAOFERTAEDUCATIVA = "Oferta educativa";
    String NOMBREARCHIVOTOTALSESIONES = "";
    String NOMBREARCHIVOTOTALSESIONESPORFILA = "";
    String CARPETASALIDA = "salidahorarios/";
    int INDICEFILAENCABEZADOSOFERTAEDUCATIVA = 1;
    int INDICEFILADONDESEPONENLOSDIAS = 0;
    int INDICEFILADONDESEPONENLASFECHAS = 1;

    CellStyle FORMATODIAMES;
    CellStyle FORMATOHORAMINUTO;
    CellStyle FORMATODIAMESANOMINUTOSEGUNDO;

    public CrearArchivosSalida(CalendarioTotalSemestre cts, CargaDeDatosExcel cde, Horario h, String rutaSalida) {
        CTS = cts;
        CDE = cde;
        H = h;

        if (!rutaSalida.trim().equals("")) {
            CARPETASALIDA = rutaSalida + "/";
        }
    }

    public void cargarArchivoOfertaEducativa() {
        try {
            FileInputStream fis = new FileInputStream(CDE.RUTAARCHIVOOFERTAEDUCATIVA);
            LIBROSALIDA = new XSSFWorkbook(fis);
//            FileOutputStream fos = new FileOutputStream("Salida Horarios.xlsx");
//            libroSalida.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void crearFormatoFechaDiaNombreMes() {
        FORMATODIAMES = LIBROSALIDA.createCellStyle();
        FORMATODIAMES.setDataFormat(LIBROSALIDA.getCreationHelper().createDataFormat().getFormat("d-mmm"));
    }

    public void crearFormatoFechahHoraMinuto() {
        FORMATOHORAMINUTO = LIBROSALIDA.createCellStyle();
        FORMATOHORAMINUTO.setDataFormat(LIBROSALIDA.getCreationHelper().createDataFormat().getFormat("h:mm"));
    }

    public void crearFormatoDiaMesHoraMinuto(XSSFWorkbook libro) {
        FORMATODIAMESANOMINUTOSEGUNDO = libro.createCellStyle();
        FORMATODIAMESANOMINUTOSEGUNDO.setDataFormat(libro.getCreationHelper().createDataFormat().getFormat("dd/MMM/yyyy HH:mm"));
    }

    public void colocarEncabezadosDiasFechasLibroSalida() {
        LISTADOFECHASORDENPUESTOEXCEL = new ArrayList<Date>();

        XSSFSheet hojaOfertaEducativa = LIBROSALIDA.getSheet(NOMBREHOJAOFERTAEDUCATIVA);
        XSSFRow filaEncabezados = hojaOfertaEducativa.createRow(INDICEFILADONDESEPONENLOSDIAS);
        XSSFRow filaFechasEncabezados = hojaOfertaEducativa.getRow(INDICEFILADONDESEPONENLASFECHAS);
        int contadorDiaPuesto = 0;
        for (int i = 0; i < LISTADONOMBRESDIASTODOSEMESTRE.size(); i++) {
            if (!LISTADONOMBRESDIASTODOSEMESTRE.get(i).equals("sábado") && !LISTADONOMBRESDIASTODOSEMESTRE.get(i).equals("domingo")) {
                if (celdaVacia(filaEncabezados, INDICECOLUMNAINICIANFECHAS + contadorDiaPuesto)) {
                    filaEncabezados.createCell(INDICECOLUMNAINICIANFECHAS + contadorDiaPuesto).setCellValue(LISTADONOMBRESDIASTODOSEMESTRE.get(i));
                    filaFechasEncabezados.createCell(INDICECOLUMNAINICIANFECHAS + contadorDiaPuesto).setCellValue(LISTADOFECHASDIASDISTINTOSTODOSEMESTRE.get(i));
                    filaFechasEncabezados.getCell(INDICECOLUMNAINICIANFECHAS + contadorDiaPuesto).setCellStyle(FORMATODIAMES);
                    LISTADOFECHASORDENPUESTOEXCEL.add(LISTADOFECHASDIASDISTINTOSTODOSEMESTRE.get(i));
                    contadorDiaPuesto++;
                }
            }
        }

        for (int i = 0; i < LISTADONOMBRESDIASTODOSEMESTRE.size(); i++) {
            if (LISTADONOMBRESDIASTODOSEMESTRE.get(i).equals("sábado")) {
                if (celdaVacia(filaEncabezados, INDICECOLUMNAINICIANFECHAS + contadorDiaPuesto)) {
                    filaEncabezados.createCell(INDICECOLUMNAINICIANFECHAS + contadorDiaPuesto).setCellValue(LISTADONOMBRESDIASTODOSEMESTRE.get(i));
                    filaFechasEncabezados.createCell(INDICECOLUMNAINICIANFECHAS + contadorDiaPuesto).setCellValue(LISTADOFECHASDIASDISTINTOSTODOSEMESTRE.get(i));
                    filaFechasEncabezados.getCell(INDICECOLUMNAINICIANFECHAS + contadorDiaPuesto).setCellStyle(FORMATODIAMES);
                    LISTADOFECHASORDENPUESTOEXCEL.add(LISTADOFECHASDIASDISTINTOSTODOSEMESTRE.get(i));
                    contadorDiaPuesto++;
                }
            }
        }
    }

    public void colocarFechasSesionesLibro(AsignacionSalones as) {
        for (int i = 0; i < CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            ArrayList<Sesion> listadoSesionesFila = new ArrayList<Sesion>(H.LISTADOSESIONESPORFILA.get(i));
            ArrayList<Salon> listadoSalonesFila = new ArrayList<Salon>(as.listadoSalonesPorFila(i));

            for (int j = 0; j < listadoSesionesFila.size(); j++) {
                Date fecha1 = listadoSesionesFila.get(j).getFecha();

                for (int k = 0; k < LISTADOFECHASORDENPUESTOEXCEL.size(); k++) {
                    Date fecha2 = LISTADOFECHASORDENPUESTOEXCEL.get(k);

                    if (H.fechasMismoDia(fecha1, fecha2)) {
                        int duracion = -1;
                        int numeroSesiones = -1;
                        if (H.esFilaCompartida(CDE, i)) {
                            duracion = H.mayorDuracionCompartida(i);
                            numeroSesiones = H.mayorCantidadSesionesCompartida(i);
                        } else {
                            duracion = CDE.LISTADOOFERTAEDUCATIVA_DURACIONIDEALSESION.get(i);
                            numeroSesiones = CDE.LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(i);
                        }

                        LIBROSALIDA.getSheet(NOMBREHOJAOFERTAEDUCATIVA).getRow(i + INDICEFILAENCABEZADOSOFERTAEDUCATIVA + 1).createCell(INDICECOLUMNAINICIANFECHAS + k).setCellValue(fecha1);
                        LIBROSALIDA.getSheet(NOMBREHOJAOFERTAEDUCATIVA).getRow(i + INDICEFILAENCABEZADOSOFERTAEDUCATIVA + 1).getCell(INDICECOLUMNAINICIANFECHAS + k).setCellStyle(FORMATOHORAMINUTO);

                        if (H.seleccionarJornadaNombreDado(CDE.LISTADOJORNADAS, CDE.LISTADOOFERTAEDUCATIVA_JORNADA.get(i)).esPresencial()) {
                            agregarComentario(LIBROSALIDA,
                                    LIBROSALIDA.getSheet(NOMBREHOJAOFERTAEDUCATIVA),
                                    LIBROSALIDA.getSheet(NOMBREHOJAOFERTAEDUCATIVA).getRow(i + INDICEFILAENCABEZADOSOFERTAEDUCATIVA + 1),
                                    LIBROSALIDA.getSheet(NOMBREHOJAOFERTAEDUCATIVA).getRow(i + INDICEFILAENCABEZADOSOFERTAEDUCATIVA + 1).getCell(INDICECOLUMNAINICIANFECHAS + k),
                                    "Duración de la sesión "
                                    + duracion/numeroSesiones
                                    + " minutos. "
                                    + "Total sesiones semanales: "
                                    + numeroSesiones
                                    + " Salón: "//por el momento el salón no se asignará ya que no cumple el requirimiento de salón fijo para NRC
                                    + listadoSalonesFila.get(j).getNombre());
                        } else {
                            agregarComentario(LIBROSALIDA,
                                    LIBROSALIDA.getSheet(NOMBREHOJAOFERTAEDUCATIVA),
                                    LIBROSALIDA.getSheet(NOMBREHOJAOFERTAEDUCATIVA).getRow(i + INDICEFILAENCABEZADOSOFERTAEDUCATIVA + 1),
                                    LIBROSALIDA.getSheet(NOMBREHOJAOFERTAEDUCATIVA).getRow(i + INDICEFILAENCABEZADOSOFERTAEDUCATIVA + 1).getCell(INDICECOLUMNAINICIANFECHAS + k),
                                    "Duración "
                                    + duracion
                                    + " minutos. "
                                    + "Total sesiones: "
                                    + numeroSesiones
                                    + " Salón: "//por el momento el salón no se asignará ya que no cumple el requirimiento de salón fijo para NRC
                                    + listadoSalonesFila.get(j).getNombre());
                        }

                    }
                }
            }
        }
    }

    public void escribirEnElLibrodeSalida() {
        try {
            FileOutputStream fos = new FileOutputStream(CARPETASALIDA + "Salida Horarios "
                    + SESIONESAPROGRAMAR
                    + " "
                    + SESIONESPROGRAMADAS
                    + " "
                    + EFECTIVIDADHORARIO
                    + ".xlsx");
            LIBROSALIDA.write(fos);
            fos.close();
        } catch (Exception e) {
            ofertaeducativa.Validaciones.mostrarErroresTotal(
                    "Ocurrió un error al escribir en el libro de Salida Horarios. "
                    + "Verifique que la ruta de salida sea la correcta y exista", e);
            e.printStackTrace();
        }
    }

    public void calcularIndiceColumnaInicialParaColocarFechas() {
        try {
            XSSFSheet hojaOfertaEducativa = LIBROSALIDA.getSheet(NOMBREHOJAOFERTAEDUCATIVA);
            XSSFRow filaEncabezados = hojaOfertaEducativa.getRow(INDICEFILAENCABEZADOSOFERTAEDUCATIVA);
            int contadorColumnas = 0;
            while (!celdaVacia(filaEncabezados, contadorColumnas)) {
                contadorColumnas++;
            }

            INDICECOLUMNAINICIANFECHAS = contadorColumnas;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void obtenerNombresDiasTodasLasFechasSemestre() {
        ArrayList<String> nombresDias = new ArrayList<String>();
        ArrayList<Integer> diasTotales = new ArrayList<Integer>();
        ArrayList<Date> fechasDiasDiferentesTotales = new ArrayList<Date>();

        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        String[] dayOfWeek = symbols.getWeekdays();

        for (int i = 0; i < CTS.LISTADOTOTALHORASSEMESTRE_SINDIASPROHIBIDOS.size(); i++) {
            Date fecha = CTS.LISTADOTOTALHORASSEMESTRE_SINDIASPROHIBIDOS.get(i);
            Calendar c = Calendar.getInstance();
            c.setTime(fecha);
            if (i == 0) {
                diasTotales.add(c.get(Calendar.DAY_OF_YEAR));
                nombresDias.add(dayOfWeek[c.get(Calendar.DAY_OF_WEEK)]);
                fechasDiasDiferentesTotales.add(fecha);
            } else {
                if (diasTotales.indexOf(Integer.valueOf(c.get(Calendar.DAY_OF_YEAR))) == -1) {
                    diasTotales.add(c.get(Calendar.DAY_OF_YEAR));
                    nombresDias.add(dayOfWeek[c.get(Calendar.DAY_OF_WEEK)]);
                    fechasDiasDiferentesTotales.add(fecha);
                }
            }
        }

        LISTADONOMBRESDIASTODOSEMESTRE = new ArrayList<String>(nombresDias);
        LISTADOFECHASDIASDISTINTOSTODOSEMESTRE = new ArrayList<Date>(fechasDiasDiferentesTotales);
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

    public void agregarComentario(XSSFWorkbook wb, XSSFSheet sheet, XSSFRow row, XSSFCell cell, String mensaje) {

        CreationHelper factory = wb.getCreationHelper();

        Drawing drawing = sheet.createDrawingPatriarch();

        // When the comment box is visible, have it show in a 1x3 space
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(cell.getColumnIndex() + 2);
        anchor.setCol2(cell.getColumnIndex() + 5);
        anchor.setRow1(row.getRowNum() + 2);
        anchor.setRow2(row.getRowNum() + 5);
        if (cell.getCellComment() == null) {
            // Create the comment and set the text+author
            Comment comment = drawing.createCellComment(anchor);
            RichTextString str = factory.createRichTextString(mensaje);
            comment.setString(str);
            comment.setAuthor("Coordinador");

            // Assign the comment to the cell
            cell.setCellComment(comment);
        }

    }

    public void crearArchivoGrupoMinutosJornadaMinutos() {
        try {
            FileOutputStream fos = new FileOutputStream(CARPETASALIDA + "minutosporgrupo a progr efectividad "
                    + EFECTIVIDADHORARIO
                    + " sesiones programdas " + SESIONESPROGRAMADAS + " sesiones a programar " + SESIONESAPROGRAMAR + ".csv");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write("grupo,jornada,mintotaljor,mintotalgrupo, faltan, sepuede, faltanhoras");
            bw.newLine();
            for (int i = 0; i < CDE.LISTADODEPURADOGRUPOSCADENA.size(); i++) {
                String grupo = CDE.LISTADODEPURADOGRUPOSCADENA.get(i);
                String jornada = CDE.obtenerJornadaCadenaGrupoCadenaDado(grupo);
                long minutosTotalGrupo = CDE.LISTADODEPURADOTOTALMINUTOSSESIONESGRUPO.get(i);
                long minutosJornada = H.obtenerMinutosJornadaCadena(CDE, jornada);
                bw.write(grupo + "," + jornada + "," + minutosJornada + "," + minutosTotalGrupo);
                bw.newLine();
            }
            bw.close();

            FileOutputStream fosxl = new FileOutputStream(CARPETASALIDA + "minutosporgrupovsjornada efectividad "
                    + EFECTIVIDADHORARIO
                    + " sesiones programdas " + SESIONESPROGRAMADAS + " sesiones a programar " + SESIONESAPROGRAMAR + ".xlsx");
            XSSFWorkbook libroSalidaSesionesPorFila = new XSSFWorkbook();
            XSSFSheet hoja = libroSalidaSesionesPorFila.createSheet();

            XSSFRow filaEncabezado = hoja.createRow(0);
            pasarEncabezadosFilaHojaExcel("grupo,jornada,mintotaljor,mintotalgrupo, faltan, sepuede, faltanhoras",
                    filaEncabezado);

            for (int i = 0; i < CDE.LISTADODEPURADOGRUPOSCADENA.size(); i++) {
                XSSFRow fila = hoja.createRow(i + 1);
                String grupo = CDE.LISTADODEPURADOGRUPOSCADENA.get(i);
                String jornada = CDE.obtenerJornadaCadenaGrupoCadenaDado(grupo);
                long minutosTotalGrupo = CDE.LISTADODEPURADOTOTALMINUTOSSESIONESGRUPO.get(i);
                long minutosJornada = H.obtenerMinutosJornadaCadena(CDE, jornada);

                long faltan = minutosJornada - minutosTotalGrupo;
                String sepuede = "No";
                if (faltan >= 0) {
                    sepuede = "Si";
                }
                long faltanhoras = faltan / 60;

                pasarDatosFilaHojaExcel(grupo
                        + ","
                        + jornada
                        + ","
                        + minutosJornada
                        + "," + minutosTotalGrupo
                        + "," + faltan
                        + "," + sepuede
                        + "," + faltanhoras,
                        fila);
            }

            hoja.setAutoFilter(new CellRangeAddress(0, CDE.LISTADODEPURADOGRUPOSCADENA.size() - 1, 0, 6));
            libroSalidaSesionesPorFila.write(fosxl);

        } catch (Exception e) {
            ofertaeducativa.Validaciones.mostrarErroresTotal("Ocurrió un error "
                    + "al crear el lbro de minutos por jornada", e);
            e.printStackTrace();
        }
    }

    public void crearArchivoTotalSesionesProgramadasFila() {
        try {
            FileOutputStream fosxl = new FileOutputStream(CARPETASALIDA + "sesionesporfila efectividad "
                    + EFECTIVIDADHORARIO
                    + " sesiones programadas " + SESIONESPROGRAMADAS + " sesiones a programar " + SESIONESAPROGRAMAR + ".xlsx");
            XSSFWorkbook libroSalidaSesionesPorFila = new XSSFWorkbook();
            XSSFSheet hoja = libroSalidaSesionesPorFila.createSheet();

            XSSFRow filaEncabezado = hoja.createRow(0);
            pasarEncabezadosFilaHojaExcel("programa,semestre,jornada,asignatura,nrc,docente,virtual,duracionsesion,cruzcomp,sesionesaprogramar,sesionesprogramadas,faltan",
                    filaEncabezado);

            for (int i = 0; i < CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
                String programa = CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i);
                String semestre = CDE.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i);
                String jornada = CDE.LISTADOOFERTAEDUCATIVA_JORNADA.get(i);
                String asignatura = CDE.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i);
                String nrc = CDE.LISTADOOFERTAEDUCATIVA_NRC.get(i);
                String docente = CDE.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i);
                String virtual = CDE.LISTADOOFERTAEDUCATIVA_VIRTUAL.get(i);
                int duracionSesion = CDE.LISTADOOFERTAEDUCATIVA_DURACIONIDEALSESION.get(i);
                String cruzComp = CDE.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(i);
                int sesionesAProgramar = CDE.LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(i);
                if (CDE.seleccionarJornadaNombreDado(CDE.LISTADOJORNADAS, jornada).esPresencial()) {
                    sesionesAProgramar = H.TOTALSEMANASSEMESTRE * sesionesAProgramar;
                }
                int sesionesProgramadas = H.LISTADOSESIONESPORFILA.get(i).size();
                int faltantes = sesionesAProgramar - sesionesProgramadas;
                
                if(faltantes<0){
                    faltantes = 0;
                }

                XSSFRow fila = hoja.createRow(i + 1);
                pasarDatosFilaHojaExcel(programa
                        + "," + semestre
                        + "," + jornada
                        + "," + asignatura
                        + "," + nrc
                        + "," + docente
                        + "," + virtual
                        + "," + duracionSesion
                        + "," + cruzComp
                        + "," + sesionesAProgramar
                        + "," + sesionesProgramadas
                        + "," + faltantes,
                        fila);
            }

            hoja.setAutoFilter(new CellRangeAddress(0, CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.size() - 1, 0, 10));

//            XSSFSheet hojaFaltantesDocente = libroSalidaSesionesPorFila.createSheet("FALTANTES POR DOCENTE");
//            
//            int firstRow = hoja.getFirstRowNum();
//            int lastRow = hoja.getLastRowNum();
//            int firstCol = hoja.getRow(0).getFirstCellNum();
//            int lastCol = hoja.getRow(0).getLastCellNum();
//
//            CellReference topLeft = new CellReference(0, 0);
//            CellReference botRight = new CellReference(CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.size() - 1, 10);
//            
//            AreaReference aref = new AreaReference(topLeft, botRight);
//            
//            XSSFPivotTable tablaDinamica = hoja.createPivotTable(aref, new CellReference("M1"));
//            tablaDinamica.addRowLabel(4);//columna docente
//            tablaDinamica.addDataColumn(5, true);
//            tablaDinamica.addReportFilter(5);//columna virtual
            libroSalidaSesionesPorFila.write(fosxl);
        } catch (Exception e) {
            ofertaeducativa.Validaciones.mostrarErroresTotal("Ocurrió un error "
                    + "al crear el lbro de total sesiones programadas por fila", e);
            e.printStackTrace();
        }
    }

    public String horaMilitarInicioDesdeFechaSesion(Sesion s) {
        Date fecha = s.getFecha();

        Calendar c = Calendar.getInstance();
        c.setTime(fecha);

        int minutosNumero = c.get(Calendar.MINUTE);
        String minutosCadena = "" + minutosNumero;
        if (minutosNumero == 0) {
            minutosCadena = "00";
        }

        return c.get(Calendar.HOUR_OF_DAY) + "" + minutosCadena;
    }

    public String horaMilitarFinalDesdeFechaSesion(Sesion s) {
        Date fecha = s.getFecha();

        Calendar c = Calendar.getInstance();
        c.setTime(fecha);

        c.add(Calendar.MINUTE, s.getDuracion() - 1);

        int minutosNumero = c.get(Calendar.MINUTE);
        String minutosCadena = "" + minutosNumero;
        if (minutosNumero == 0) {
            minutosCadena = "00";
        }

        return c.get(Calendar.HOUR_OF_DAY) + "" + minutosCadena;
    }

    public void crearArchivoTotalSesiones(ArrayList<Sesion> mls, int sesionesAProgramar, int sesionesProgramadas, double efectividad) {
        try {
            //se debe tener en cuenta que las columnas celda origen sale de los horarois ya cargados
            //en el excel
            NOMBREARCHIVOTOTALSESIONES = "totalsesiones a progr " + sesionesAProgramar + " sespro" + sesionesProgramadas + " efect " + efectividad + " .xlsx";
            FileOutputStream fosxl = new FileOutputStream(CARPETASALIDA + NOMBREARCHIVOTOTALSESIONES);
            XSSFWorkbook libroSalidaTotalSesiones = new XSSFWorkbook();
            XSSFSheet hoja = libroSalidaTotalSesiones.createSheet();
            crearFormatoDiaMesHoraMinuto(libroSalidaTotalSesiones);

            XSSFRow filaEncabezado = hoja.createRow(0);
            pasarEncabezadosFilaHojaExcel("asig,progr,sem,nrc,doc,dur,fecha,diaSemana,ns,idCrCo,jornada,cupoest,horainicio,horafinal", filaEncabezado);

            for (int i = 0; i < mls.size(); i++) {
                XSSFRow fila = hoja.createRow(i + 1);
                pasarDatosFilaHojaExcelConFecha(crearCadenaSalidaSesionesEnCSV(mls, i), fila, 6);
            }

            hoja.setAutoFilter(new CellRangeAddress(0, mls.size() - 1, 0, 8));

            libroSalidaTotalSesiones.write(fosxl);

        } catch (Exception e) {
            ofertaeducativa.Validaciones.mostrarErroresTotal("Ocurrió un error "
                    + "al crear el lbro del total de sesiones", e);
            e.printStackTrace();
        }
    }

    /**
     * Se crea un archivo con horarios de Excel previamente realizados
     *
     * @param mls
     * @param sesionesAProgramar
     * @param sesionesProgramadas
     * @param efectividad
     * @param v
     */
    public void crearArchivoTotalSesionesDesdeHorariosExcel(ArrayList<Sesion> mls, int sesionesAProgramar, int sesionesProgramadas, double efectividad, Validaciones v) {
        try {
            //se debe tener en cuenta que las columnas celda origen sale de los horarois ya cargados
            //en el excel
            NOMBREARCHIVOTOTALSESIONES = "totalsesiones a progr " + sesionesAProgramar + " sespro" + sesionesProgramadas + " efect " + efectividad + " .xlsx";
            FileOutputStream fosxl = new FileOutputStream(NOMBREARCHIVOTOTALSESIONES);
            XSSFWorkbook libroSalidaTotalSesiones = new XSSFWorkbook();
            XSSFSheet hoja = libroSalidaTotalSesiones.createSheet();
            crearFormatoDiaMesHoraMinuto(libroSalidaTotalSesiones);

            XSSFRow filaEncabezado = hoja.createRow(0);
            pasarEncabezadosFilaHojaExcel("asig,progr,sem,nrc,doc,dur,fecha,ns,idCrCo,jornada,cupoest,celdaorigen,fechabann,horainicio,horafinal,observ", filaEncabezado);

            for (int i = 0; i < mls.size(); i++) {
                XSSFRow fila = hoja.createRow(i + 1);
                pasarDatosFilaHojaExcelConFechaDesdeHorariosExcel(crearCadenaSalidaSesionesEnCSVDesdeHorarios(mls, i, v), fila, 6, v);
            }

            hoja.setAutoFilter(new CellRangeAddress(0, mls.size() - 1, 0, 8));

            libroSalidaTotalSesiones.write(fosxl);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pasarEncabezadosFilaHojaExcel(String listaEncabezados, XSSFRow fila) {
        String[] listadoEncabezados = listaEncabezados.split(",");
        for (int i = 0; i < listadoEncabezados.length; i++) {
            fila.createCell(i).setCellValue(listadoEncabezados[i]);
        }
    }

    public void pasarDatosFilaHojaExcel(String listaEncabezados, XSSFRow fila) {
        String[] listadoEncabezados = listaEncabezados.split(",");
        for (int i = 0; i < listadoEncabezados.length; i++) {
            fila.createCell(i).setCellValue(listadoEncabezados[i]);
        }
    }

    public void pasarDatosFilaHojaExcelConFecha(String listaEncabezados, XSSFRow fila, int columnaFecha) {

        String[] listadoEncabezados = listaEncabezados.split(",");
        for (int i = 0; i < listadoEncabezados.length; i++) {
            if (i == columnaFecha) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
                    Date fecha = formatter.parse(listadoEncabezados[i]);
                    fila.createCell(i).setCellValue(fecha);
                    fila.getCell(i).setCellStyle(FORMATODIAMESANOMINUTOSEGUNDO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                fila.createCell(i).setCellValue(listadoEncabezados[i]);
            }
        }
    }

    public void pasarDatosFilaHojaExcelConFechaDesdeHorariosExcel(String listaEncabezados, XSSFRow fila, int columnaFecha, Validaciones v) {

        String[] listadoEncabezados = listaEncabezados.split(",");
        for (int i = 0; i < listadoEncabezados.length; i++) {
            if (i == columnaFecha) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
                    Date fecha = formatter.parse(listadoEncabezados[i]);
                    fila.createCell(i).setCellValue(fecha);
                    fila.getCell(i).setCellStyle(FORMATODIAMESANOMINUTOSEGUNDO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                fila.createCell(i).setCellValue(listadoEncabezados[i]);
            }
        }
    }

    public String observacionDuracionValidacionesCoordenadaDada(Validaciones v, String coordenada) {
        String observacion = "";
        for (int i = 0; i < v.LISTADOCOORDENADASCELDASCONOBSERVACIONESDURACION.size(); i++) {
            if (coordenada.equals(v.LISTADOCOORDENADASCELDASCONOBSERVACIONESDURACION.get(i))) {
                return v.LISTADOOBSERVACIONESDURACION.get(i);
            }
        }
        return observacion;
    }

    /**
     * Crea una cadena que posteriormente puede ser escrita en un archivo excel
     * Para crear dicha cadena toma h.Sesiones, es decir cada uno de los
     * elementos de cada fila de la h.Sesiones
     *
     * @param mls
     * @param fila
     * @return
     */
    public String crearCadenaSalidaSesionesEnCSV(ArrayList<Sesion> mls, int fila) {
        String cadenaFila = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
        Sesion s = mls.get(fila);
        String nrc = H.obtenerCadenaNRCSesion(CDE, s);
        String jornada = H.obtenerCadenaJornadaSesion(CDE, s);
        int cupoEstimado = H.obtenerCupoEstimadoFilaSesion(CDE, s);

        try {
            cadenaFila = s.ASIGNATURA.getNombre()
                    + "," + s.GRUPO.getPrograma()
                    + "," + s.GRUPO.getSemestre()
                    + "," + nrc
                    + "," + s.DOCENTE.getNombre()
                    + "," + s.DURACION
                    + "," + formatter.format(s.FECHA)
                    + "," + s.diaSemanaEspanol()
                    + "," + s.ASIGNATURA.NUMEROSESIONES
                    + "," + s.ASIGNATURA.getIdCruceCompartido()
                    + "," + jornada
                    + "," + cupoEstimado
                    //+ "," + CDE.LISTADOCOORDENADASCELDASSESIONES.get(fila)
                    + "," + horaMilitarInicioDesdeFechaSesion(s)
                    + "," + horaMilitarFinalDesdeFechaSesion(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cadenaFila;
    }

    public String crearCadenaSalidaSesionesEnCSVDesdeHorarios(ArrayList<Sesion> mls, int fila, Validaciones v) {
        String cadenaFila = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
        Sesion s = mls.get(fila);
        String nrc = H.obtenerCadenaNRCSesion(CDE, s);
        String jornada = H.obtenerCadenaJornadaSesion(CDE, s);
        int cupoEstimado = H.obtenerCupoEstimadoFilaSesion(CDE, s);
        String observacion = observacionDuracionValidacionesCoordenadaDada(v, CDE.LISTADOCOORDENADASCELDASSESIONES.get(fila));
        String fechaFormatoBanner = pasarFechaFormatoBanner(s.FECHA);//dd-mm-aaaa

        try {
            cadenaFila = s.ASIGNATURA.getNombre()
                    + "," + s.GRUPO.getPrograma()
                    + "," + s.GRUPO.getSemestre()
                    + "," + nrc
                    + "," + s.DOCENTE.getNombre()
                    + "," + s.DURACION
                    + "," + formatter.format(s.FECHA)
                    + "," + s.ASIGNATURA.NUMEROSESIONES
                    + "," + s.ASIGNATURA.getIdCruceCompartido()
                    + "," + jornada
                    + "," + cupoEstimado
                    + "," + CDE.LISTADOCOORDENADASCELDASSESIONES.get(fila)
                    + "," + fechaFormatoBanner
                    + "," + horaMilitarInicioDesdeFechaSesion(s)
                    + "," + horaMilitarFinalDesdeFechaSesion(s)
                    + "," + observacion;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cadenaFila;
    }

    public void imprimirSalidaTotalSesionesCSV(ArrayList<Sesion> mls) {
        for (int i = 0; i < H.SESIONES.size(); i++) {
            System.out.println(crearCadenaSalidaSesionesEnCSV(mls, i));
        }
    }

    public void crearFormatoFechaDiaMesAnoHoraMinuto(CellStyle estiloDiaMesAnoHoraMinuto, XSSFWorkbook libroSalida) {
        estiloDiaMesAnoHoraMinuto = libroSalida.createCellStyle();
        estiloDiaMesAnoHoraMinuto.setDataFormat(libroSalida.getCreationHelper().createDataFormat().getFormat("d/mm/yyyy h:mm"));
    }

    public void crearArchivoCreditosDocenteSinRepetirCompartidos() {
        try {
            FileOutputStream fosxl = new FileOutputStream(CARPETASALIDA + "creditosPorDocenteSinRepetirCompartidos efectividad "
                    + EFECTIVIDADHORARIO
                    + " sesiones programdas " + SESIONESPROGRAMADAS + " sesiones a programar " + SESIONESAPROGRAMAR + ".xlsx");
            XSSFWorkbook libroCreditos = new XSSFWorkbook();
            XSSFSheet hoja = libroCreditos.createSheet();

            XSSFRow filaEncabezado = hoja.createRow(0);
            pasarEncabezadosFilaHojaExcel("docente, créditos", filaEncabezado);

            for (int i = 0; i < CDE.LISTADODEPURADOCREDITOSDOCENTESINREPETIRCOMPARTIDOS.size(); i++) {
                String docente = CDE.LISTADODEPURADODOCENTES.get(i).getNombre();
                int creditos = CDE.LISTADODEPURADOCREDITOSDOCENTESINREPETIRCOMPARTIDOS.get(i);
                XSSFRow fila = hoja.createRow(i + 1);
                pasarDatosFilaHojaExcel(docente + "," + creditos, fila);
            }

            hoja.setAutoFilter(new CellRangeAddress(0, CDE.LISTADODEPURADOCREDITOSDOCENTESINREPETIRCOMPARTIDOS.size() - 1, 0, 1));
            libroCreditos.write(fosxl);
        } catch (Exception e) {
            ofertaeducativa.Validaciones.mostrarErroresTotal("Ocurrió un error "
                    + "al crear el lbro de docentes sin repetir compartidos", e);
            e.printStackTrace();
        }
    }

    public void crearArchivoSesionesDocenteSinRepetirCompartidos() {
        try {
            FileOutputStream fosxl = new FileOutputStream(CARPETASALIDA + "sesionesPorDocenteSinRepetirCompartidos efectividad "
                    + EFECTIVIDADHORARIO
                    + " sesiones programdas " + SESIONESPROGRAMADAS + " sesiones a programar " + SESIONESAPROGRAMAR + ".xlsx");
            XSSFWorkbook libroSesiones = new XSSFWorkbook();
            XSSFSheet hoja = libroSesiones.createSheet();

            XSSFRow filaEncabezado = hoja.createRow(0);
            pasarEncabezadosFilaHojaExcel("docente, sesiones", filaEncabezado);

            for (int i = 0; i < CDE.LISTADODEPURADOSESIONESDOCENTESINREPETIRCOMPARTIDOS.size(); i++) {
                String docente = CDE.LISTADODEPURADODOCENTES.get(i).getNombre();
                int sesiones = CDE.LISTADODEPURADOSESIONESDOCENTESINREPETIRCOMPARTIDOS.get(i);
                XSSFRow fila = hoja.createRow(i + 1);
                pasarDatosFilaHojaExcel(docente + "," + sesiones, fila);
            }

            hoja.setAutoFilter(new CellRangeAddress(0, CDE.LISTADODEPURADOSESIONESDOCENTESINREPETIRCOMPARTIDOS.size() - 1, 0, 1));
            libroSesiones.write(fosxl);
        } catch (Exception e) {
            ofertaeducativa.Validaciones.mostrarErroresTotal("Ocurrió un error "
                    + "al crear el lbro de sesiones por docente sin repetir compartidos", e);
            e.printStackTrace();
        }
    }

    private String pasarFechaFormatoBanner(Date FECHA) {
        Calendar c = Calendar.getInstance();
        c.setTime(FECHA);

        String dia = "" + c.get(Calendar.DATE);
        String mes = "" + (c.get(Calendar.MONTH) + 1);
        String ano = "" + c.get(Calendar.YEAR);

        return dia + "-" + mes + "-" + ano;
    }
}
