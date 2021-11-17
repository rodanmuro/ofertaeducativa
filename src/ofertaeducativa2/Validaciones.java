/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa2;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
public class Validaciones {

    CargaDeDatosExcel CDE;

    ArrayList<String> LISTADOCOORDENADASCELDASCONOBSERVACIONESDURACION;
    ArrayList<String> LISTADOOBSERVACIONESDURACION;

    public Validaciones(CargaDeDatosExcel cde) {
        CDE = cde;
        LISTADOCOORDENADASCELDASCONOBSERVACIONESDURACION = new ArrayList<String>();
        LISTADOOBSERVACIONESDURACION = new ArrayList<String>();
    }

    public boolean jornadaValidaFilaOfertaEducativa(int i) {
        boolean esValida = false;

        String jornada = CDE.LISTADOOFERTAEDUCATIVA_JORNADA.get(i);
        for (int j = 0; j < CDE.LISTADOJORNADAS.size(); j++) {
            if (CDE.LISTADOJORNADAS.get(j).getNombre().trim().equals(jornada.trim())) {
                return true;
            }
        }

        return esValida;
    }

    public void validarZonaHorasSalidaHorarios() {
        try {
            FileInputStream fis = new FileInputStream(CDE.RUTAARCHIVOOFERTAEDUCATIVA);
            XSSFWorkbook libroHorarios = new XSSFWorkbook(fis);
            XSSFSheet hojaHorarios = libroHorarios.getSheet(CDE.NOMBREHOJAOFERTAEDUCATIVA);
            XSSFRow filaEncabezadosNombresDias = hojaHorarios.getRow(CDE.FILAENCABEZADOSNOMBRESDIAS);

            int contadorFilas = CDE.FILAINICIALENCABEZADOSOFERTAEDUCATIVA + 1;
            XSSFRow filaContadora = hojaHorarios.getRow(contadorFilas);

            while (!CDE.celdaVacia(filaContadora, 0)) {
                int contadorColumnas = CDE.INDICECOLUMNAINICIANFECHAS;
                while (!CDE.celdaVacia(filaEncabezadosNombresDias, contadorColumnas)) {

                    if (!CDE.celdaVacia(filaContadora, contadorColumnas)) {
                        XSSFCell celda = filaContadora.getCell(contadorColumnas);
                        if (celda.getCellTypeEnum() == CellType.STRING) {
                            String cadenaMensajeCeldaTexto = "La celda en la fila "
                                    + (contadorFilas)
                                    + " y la columna " + (contadorColumnas)
                                    + " " + (new CellAddress(contadorFilas, contadorColumnas)).formatAsString()
                                    + " tiene el texto " + celda.getStringCellValue();
                            System.out.println(cadenaMensajeCeldaTexto);
                        }
                        if (celda.getCellTypeEnum() == CellType.NUMERIC) {
                            if (!DateUtil.isCellDateFormatted(celda)) {
                                String cadenaMensajeCeldaNumericaSinFecha = "La celda en la fila "
                                        + (contadorFilas)
                                        + " y la columna " + (contadorColumnas)
                                        + " " + (new CellAddress(contadorFilas, contadorColumnas)).formatAsString()
                                        + " tiene el número " + celda.getNumericCellValue() + " pero no está formateada como texto";
                                System.out.println(cadenaMensajeCeldaNumericaSinFecha);

                            }
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

    }

    public boolean existeDuracionCelda(XSSFCell celda) {
        if (celda.getCellComment() != null) {
            String comentario = celda.getCellComment().getString().getString().toLowerCase();
            if ((comentario.indexOf("duración") != -1 && comentario.indexOf("minutos") != -1)
                    || (comentario.indexOf("duracion") != -1 && comentario.indexOf("minutos") != -1)) {
                celda.getAddress().formatAsString();
                if (comentario.indexOf("duración") != -1) {
                    int inicioPalabraDuracion = comentario.indexOf("duración");
                    int longitudPalabraDuracion = "duración".length();

                    int inicioDuracionMinutos = inicioPalabraDuracion + longitudPalabraDuracion + 1;

                    int inicioPalabraMinutos = comentario.indexOf("minutos");
                    int finalDuracionMinutos = inicioPalabraMinutos - 1;

                    String duracionCadena = comentario.substring(inicioDuracionMinutos, finalDuracionMinutos).trim();

                    try {
                        Integer.parseInt(duracionCadena);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }

                }
                if (comentario.indexOf("duracion") != -1) {
                    int inicioPalabraDuracion = comentario.indexOf("duracion");
                    int longitudPalabraDuracion = "duracion".length();

                    int inicioDuracionMinutos = inicioPalabraDuracion + longitudPalabraDuracion + 1;

                    int inicioPalabraMinutos = comentario.indexOf("minutos");
                    int finalDuracionMinutos = inicioPalabraMinutos - 1;

                    String duracionCadena = comentario.substring(inicioDuracionMinutos, finalDuracionMinutos).trim();

                    try {
                        Integer.parseInt(duracionCadena);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public int obtenerDuracionComentarioCelda(XSSFCell celda) {
        String comentario = celda.getCellComment().getString().getString().toLowerCase();
        if ((comentario.indexOf("duración") != -1 && comentario.indexOf("minutos") != -1)
                || (comentario.indexOf("duracion") != -1 && comentario.indexOf("minutos") != -1)) {
            if (comentario.indexOf("duración") != -1) {
                int inicioPalabraDuracion = comentario.indexOf("duración");
                int longitudPalabraDuracion = "duración".length();

                int inicioDuracionMinutos = inicioPalabraDuracion + longitudPalabraDuracion + 1;

                int inicioPalabraMinutos = comentario.indexOf("minutos");
                int finalDuracionMinutos = inicioPalabraMinutos - 1;

                String duracionCadena = comentario.substring(inicioDuracionMinutos, finalDuracionMinutos).trim();

                return Integer.parseInt(duracionCadena);

            }
            if (comentario.indexOf("duracion") != -1) {
                int inicioPalabraDuracion = comentario.indexOf("duracion");
                int longitudPalabraDuracion = "duracion".length();

                int inicioDuracionMinutos = inicioPalabraDuracion + longitudPalabraDuracion + 1;

                int inicioPalabraMinutos = comentario.indexOf("minutos");
                int finalDuracionMinutos = inicioPalabraMinutos - 1;

                String duracionCadena = comentario.substring(inicioDuracionMinutos, finalDuracionMinutos).trim();

                return Integer.parseInt(duracionCadena);
            }
        }

        return -1;
    }

    public boolean duracionCoincideConElCriterioFila(XSSFCell celda) {
        XSSFRow fila = celda.getRow();
        int indiceFilaEnOfertaEducativa = fila.getRowNum() - (CDE.FILAINICIALENCABEZADOSOFERTAEDUCATIVA + 1);

        int duracionFila = -1;
        int duracionComentario = -2;

        if (!CDE.esFilaVirtual(indiceFilaEnOfertaEducativa)) {
            if (CDE.esFilaCompartida(CDE, indiceFilaEnOfertaEducativa)) {
                duracionFila = CDE.mayorDuracionCompartida(indiceFilaEnOfertaEducativa);
            } else {
                duracionFila = CDE.LISTADOOFERTAEDUCATIVA_DURACIONIDEALSESION.get(indiceFilaEnOfertaEducativa);
            }
            duracionComentario = obtenerDuracionComentarioCelda(celda);

            if (duracionFila != duracionComentario) {
//                System.out.println("En la celda "
//                        +celda.getAddress().formatAsString()
//                        +" se presenta una duración que no coincide con el criterio de la fila. Revisar");
                return false;
            }
        }
        return true;
    }

    public void validarDuracionesEnCeldas() {
        try {
            FileInputStream fis = new FileInputStream(CDE.RUTAARCHIVOOFERTAEDUCATIVA);
            XSSFWorkbook libroHorarios = new XSSFWorkbook(fis);
            XSSFSheet hojaHorarios = libroHorarios.getSheet(CDE.NOMBREHOJAOFERTAEDUCATIVA);
            XSSFRow filaEncabezadosNombresDias = hojaHorarios.getRow(CDE.FILAENCABEZADOSNOMBRESDIAS);

            int contadorFilas = CDE.FILAINICIALENCABEZADOSOFERTAEDUCATIVA + 1;
            XSSFRow filaContadora = hojaHorarios.getRow(contadorFilas);

            while (!CDE.celdaVacia(filaContadora, 0)) {
                int contadorColumnas = CDE.INDICECOLUMNAINICIANFECHAS;
                while (!CDE.celdaVacia(filaEncabezadosNombresDias, contadorColumnas)) {

                    if (!CDE.celdaVacia(filaContadora, contadorColumnas)) {
                        XSSFCell celda = filaContadora.getCell(contadorColumnas);
                        if (celda.getCellTypeEnum() == CellType.STRING) {
                            if (existeDuracionCelda(celda)) {
                                if (!duracionCoincideConElCriterioFila(celda)) {
                                    String cadenaObservacion = "La duración en la celda " + celda.getAddress().formatAsString() + " no coincide con el criterio de la fila. Revisar";
                                    System.out.println(cadenaObservacion);
                                    LISTADOCOORDENADASCELDASCONOBSERVACIONESDURACION.add(celda.getAddress().formatAsString());
                                    LISTADOOBSERVACIONESDURACION.add(cadenaObservacion);
                                }
                            } else {
                                String cadenaObservacion = "La celda " + celda.getAddress().formatAsString() + " no presenta duración en la celda o está mal formateado el comentario";
                                System.out.println(cadenaObservacion);
                                LISTADOCOORDENADASCELDASCONOBSERVACIONESDURACION.add(celda.getAddress().formatAsString());
                                LISTADOOBSERVACIONESDURACION.add(cadenaObservacion);
                            }
                        }
                        if (celda.getCellTypeEnum() == CellType.NUMERIC) {
                            if (existeDuracionCelda(celda)) {
                                if (!duracionCoincideConElCriterioFila(celda)) {
                                    String cadenaObservacion = "La duración en la celda " + celda.getAddress().formatAsString() + " no coincide con el criterio de la fila. Revisar";
                                    System.out.println(cadenaObservacion);
                                    LISTADOCOORDENADASCELDASCONOBSERVACIONESDURACION.add(celda.getAddress().formatAsString());
                                    LISTADOOBSERVACIONESDURACION.add(cadenaObservacion);
                                }
                            } else {
                                String cadenaObservacion = "La celda " + celda.getAddress().formatAsString() + " no presenta duración en la celda o está mal formateado el comentario";
                                System.out.println(cadenaObservacion);
                                LISTADOCOORDENADASCELDASCONOBSERVACIONESDURACION.add(celda.getAddress().formatAsString());
                                LISTADOOBSERVACIONESDURACION.add(cadenaObservacion);
                            }
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

    }

    /**
     * Verifica que todos los encabezados que se supone deben tener un archivo,
     * existan en un libro de Excel, en una fila dada
     *
     * @param libro
     * @param nombreHoja
     * @param fila
     * @param encabezados
     * @return Un ArrayList<String> de tamaño 1 con la palabra ok, si todos los
     * encabezados existen, de lo contrario un ArrayList<String> con los
     * mensajes de los encabezados faltantes
     *
     */
    public static ArrayList<String> encabezadosExistentes(XSSFWorkbook libro, String nombreHoja, int fila, ArrayList<String> encabezados) {
        ArrayList<String> resultadoRevision = new ArrayList<String>();
        resultadoRevision.add("ok");

        int numeroEncabezadosNoEncontrados = 0;
        for (String encabezado : encabezados) {
            if (MetodosValidacionHojasExcel.obtenerCeldaEncabezado(libro.getSheet(nombreHoja), fila, encabezado) == null) {
                if (numeroEncabezadosNoEncontrados == 0) {
                    resultadoRevision.set(0, "falta el encabezado " + encabezado);
                } else {
                    resultadoRevision.add("falta el encabezado " + encabezado);
                }
                numeroEncabezadosNoEncontrados++;
            }
        }

        return resultadoRevision;
    }

    public static ArrayList<String> columnaTipo(XSSFWorkbook libro, String nombreHoja, String encabezado, int indiceFila, String tipo) {
        ArrayList<String> observaciones = new ArrayList<String>();
        String observacion = "Se ha encontrado una celda que no cumple con la condicion ";

        observaciones.add("ok");

        XSSFCell celdaEncabezado = MetodosValidacionHojasExcel.obtenerCeldaEncabezado(libro.getSheet(nombreHoja), indiceFila, encabezado);
        int indiceColumnaEncabezado = celdaEncabezado.getColumnIndex();

        XSSFSheet hoja = libro.getSheet(nombreHoja);
        /**
         * Tipos de columna texto, numero, texto-novacio, numero-novacio
         */
        int contadorObservaciones = 0;
        for (int i = indiceFila + 1; i < hoja.getLastRowNum() + 1; i++) {

            XSSFRow fila = hoja.getRow(i);
            if (fila == null && tipo.indexOf("-novacio") != -1) {
                if (contadorObservaciones == 0) {
                    observaciones.set(contadorObservaciones, observacion + " " + tipo + " en " + (new CellAddress(i, indiceColumnaEncabezado).formatAsString()) + " celda en blanco o nula");
                } else {
                    observaciones.add(observacion + " " + tipo + " en " + (new CellAddress(i, indiceColumnaEncabezado).formatAsString()) + " celda en blanco o nula");
                }
                contadorObservaciones++;
            }

            if (fila != null) {
                XSSFCell celdaAnalizar = fila.getCell(indiceColumnaEncabezado);
                if (tipo.toLowerCase().trim().equals("texto")) {
                    if (!MetodosValidacionHojasExcel.celdaVacia(fila, indiceColumnaEncabezado)
                            && !MetodosValidacionHojasExcel.esCeldaCadena(celdaAnalizar)) {
                        if (contadorObservaciones == 0) {
                            observaciones.set(contadorObservaciones, observacion + " " + tipo + " en " + celdaAnalizar.getAddress().formatAsString());
                        } else {
                            observaciones.add(observacion + " " + tipo + " en " + celdaAnalizar.getAddress().formatAsString());
                        }
                        contadorObservaciones++;
                    }
                }
                if (tipo.toLowerCase().trim().equals("texto-novacio")) {
                    if (MetodosValidacionHojasExcel.celdaVacia(fila, indiceColumnaEncabezado)
                            || !MetodosValidacionHojasExcel.esCeldaCadena(celdaAnalizar)) {
                        if (contadorObservaciones == 0) {
                            if (celdaAnalizar == null) {
                                observaciones.set(contadorObservaciones, observacion + " " + tipo + " en " + (new CellAddress(fila.getRowNum(), indiceColumnaEncabezado).formatAsString()) + " celda en blanco o nula");
                            } else {
                                observaciones.set(contadorObservaciones, observacion + " " + tipo + " en " + celdaAnalizar.getAddress().formatAsString());
                            }
                        } else {
                            if (celdaAnalizar == null) {
                                observaciones.add(observacion + " " + tipo + " en " + (new CellAddress(fila.getRowNum(), indiceColumnaEncabezado).formatAsString()) + " celda en blanco o nula");
                            } else {
                                observaciones.add(observacion + " " + tipo + " en " + celdaAnalizar.getAddress().formatAsString());
                            }
                        }
                        contadorObservaciones++;
                    }
                }
                if (tipo.toLowerCase().trim().equals("numero")) {
                    if (!MetodosValidacionHojasExcel.celdaVacia(fila, indiceColumnaEncabezado)
                            && !MetodosValidacionHojasExcel.esCeldaNumerica(celdaAnalizar)) {
                        if (contadorObservaciones == 0) {
                            if (celdaAnalizar == null) {
                                observaciones.set(contadorObservaciones, observacion + " " + tipo + " en " + (new CellAddress(fila.getRowNum(), indiceColumnaEncabezado).formatAsString()) + " celda en blanco o nula");
                            } else {
                                observaciones.set(contadorObservaciones, observacion + " " + tipo + " en " + celdaAnalizar.getAddress().formatAsString());
                            }
                        } else {
                            if (celdaAnalizar == null) {
                                observaciones.add(observacion + " " + tipo + " en " + (new CellAddress(fila.getRowNum(), indiceColumnaEncabezado).formatAsString()) + " celda en blanco o nula");
                            } else {
                                observaciones.add(observacion + " " + tipo + " en " + celdaAnalizar.getAddress().formatAsString());
                            }
                        }
                        contadorObservaciones++;
                    }
                }
                if (tipo.toLowerCase().trim().equals("numero-novacio")) {
                    if (MetodosValidacionHojasExcel.celdaVacia(fila, indiceColumnaEncabezado)
                            || !MetodosValidacionHojasExcel.esCeldaNumerica(celdaAnalizar)) {
                        if (contadorObservaciones == 0) {
                            observaciones.set(contadorObservaciones, observacion + " " + tipo + " en " + celdaAnalizar.getAddress().formatAsString());
                        } else {
                            observaciones.add(observacion + " " + tipo + " en " + celdaAnalizar.getAddress().formatAsString());
                        }
                        contadorObservaciones++;
                    }
                }
            }
            
            if(i%10000==0){
                System.out.println("valor "+i);
            }
        }
        return observaciones;
    }
    
    /**
     * Muestra un JOption Pane con el mensaje de error de la Excepción recogida
     *
     * @param e
     */
    public static void mostrarVentanaError(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        JOptionPane.showMessageDialog(null, errors.toString());
    }

    /**
     * Muestra un JOptionPane con el mensaje de error
     *
     * @param error String con el error a mostrar
     */
    public static void mostrarVentanaError(String error) {
        JOptionPane.showMessageDialog(null, error);
    }
    
    public static void mostrarErroresTotal(String textoPersonalizado, Exception e) {
        Validaciones.mostrarVentanaError(e);
        Validaciones.mostrarVentanaError(textoPersonalizado);
        e.printStackTrace();
    }

}
