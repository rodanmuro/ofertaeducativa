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
import java.util.Date;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author rodanmuro
 */
public class ReciclarNRC {

    int COLUMNA_OFERTA_ALFA = 5;
    int COLUMNA_OFERTA_NUMERICO = 6;
    int COLUMNA_OFERTA_CRUZADOSCOMPARTIDOS = 13;
    int COLUMNA_OFERTA_NRC = 9;
    
    String ENCABEZADOOFERTAALFA = "alfa";
    String ENCABEZADOOFERTANUMERICO = "numerico";
    String ENCABEZADOOFERTACRUZADOSCOMPARTIDOS = "IDCruceComp";
    String ENCBEZADOOFERTANRC = "nrc";
            

    int COLUMNA_BANNER_ALFA = 4;
    int COLUMNA_BANNER_NUMERICO = 5;
    int COLUMNA_BANNER_NRC = 1;
    
    String ENCABEZADOBANNERALFA = "ALFA";
    String ENCABEZADOBANNERNUMERICO = "NUM";
    String ENCABEZADOBANNERNRC = "NRC";
            

    int FILA_INICIAL_OFERTA = 2;
    int FILA_INICIAL_FUENTE_BANNER = 1;

    String rutaOfertaEducativa;
    String rutaFuenteNRC;
    String RUTA_SALIDA_ARCHIVO_SEGUIMIENTOS;
    String CARPETASALIDA = "salidahorarios/";

    ArrayList<String> listadoOfertaAlfaNumerico;
    ArrayList<String> listadoOfertaCruzadosCompartidos;

    ArrayList<String> listadoBannerAlfaNumerico;
    ArrayList<Integer> listadoBannerNRC;

    ArrayList<String> listadoNRCReciclados;

    XSSFWorkbook libroOFertaEducativa;
    XSSFWorkbook libroFuenteNRC;

    public ReciclarNRC(String rutaOferta, String rutaNRC, String rutaSalida) {
        

        rutaOfertaEducativa = rutaOferta;
        rutaFuenteNRC = rutaNRC;
        RUTA_SALIDA_ARCHIVO_SEGUIMIENTOS = rutaSalida;
        CARPETASALIDA = rutaSalida;
        
        cargarArchivosWorkbook();
        
        COLUMNA_BANNER_ALFA = obtenerCeldaEncabezado(libroFuenteNRC.getSheetAt(0), 0, ENCABEZADOBANNERALFA).getColumnIndex();
        COLUMNA_BANNER_NUMERICO = obtenerCeldaEncabezado(libroFuenteNRC.getSheetAt(0), 0, ENCABEZADOBANNERNUMERICO).getColumnIndex();
        COLUMNA_BANNER_NRC = obtenerCeldaEncabezado(libroFuenteNRC.getSheetAt(0), 0, ENCABEZADOBANNERNRC).getColumnIndex();
        
        COLUMNA_OFERTA_ALFA = obtenerCeldaEncabezado(libroOFertaEducativa.getSheet("Oferta educativa"), 1, ENCABEZADOOFERTAALFA).getColumnIndex();
        COLUMNA_OFERTA_NUMERICO = obtenerCeldaEncabezado(libroOFertaEducativa.getSheet("Oferta educativa"), 1, ENCABEZADOOFERTANUMERICO).getColumnIndex();
        COLUMNA_OFERTA_NRC = obtenerCeldaEncabezado(libroOFertaEducativa.getSheet("Oferta educativa"), 1, ENCBEZADOOFERTANRC).getColumnIndex();
        COLUMNA_OFERTA_CRUZADOSCOMPARTIDOS = obtenerCeldaEncabezado(libroOFertaEducativa.getSheet("Oferta educativa"), 1, ENCABEZADOOFERTACRUZADOSCOMPARTIDOS).getColumnIndex();
        
        cargarListadosOfertaEducativa();
        cargarListadosFuenteNRC();

        crearListadoNRCReciclados();

        for (int i = 0; i < listadoOfertaCruzadosCompartidos.size(); i++) {
            System.out.println(i + " alfanumerico " + listadoOfertaAlfaNumerico.get(i)
                    + " cc " + listadoOfertaCruzadosCompartidos.get(i)
                    + " nrc " + listadoNRCReciclados.get(i));

        }
        
        

        System.out.println("indices Cruzada4 " + indicesCompartido("Cruzada4"));
        System.out.println("indices Compartida14 " + indicesCompartido("Compartida14"));
        
        pasarNrcAlaOfertaEducativa();
        System.out.println("nrc pasados");
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
        return celda;
    }

    public void crearListadoNRCReciclados() {
        listadoNRCReciclados = new ArrayList<String>();
        //lo llenamos de espacios en blanco
        for (String alfaNumericoOferta : listadoOfertaAlfaNumerico) {
            listadoNRCReciclados.add("");
        }

        for (int i = 0; i < listadoOfertaAlfaNumerico.size(); i++) {
            if (listadoNRCReciclados.get(i).equals("")) {
                int nrc = 0;
                for (int j = 0; j < listadoBannerAlfaNumerico.size(); j++) {
                    if (listadoBannerAlfaNumerico.get(j).equals(listadoOfertaAlfaNumerico.get(i))) {
                        nrc = listadoBannerNRC.get(j);
                        listadoBannerNRC.remove(j);
                        listadoBannerAlfaNumerico.remove(j);
                        break;
                    }
                }

                if (esCompartida(i)) {
                    String compartida = listadoOfertaCruzadosCompartidos.get(i);
                    ArrayList<Integer> indices = new ArrayList<Integer>(indicesCompartido(compartida));

                    for (int j = 0; j < indices.size(); j++) {
                        if (nrc == 0) {
                            listadoNRCReciclados.set(indices.get(j), "Crear");
                        } else {
                            listadoNRCReciclados.set(indices.get(j), "" + nrc);
                        }
                    }
                } else {
                    if (nrc == 0) {
                        listadoNRCReciclados.set(i, "Crear");
                    } else {
                        listadoNRCReciclados.set(i, "" + nrc);
                    }

                }
            }
        }
    }

    public void cargarArchivosWorkbook() {
        try {
            FileInputStream fis1 = new FileInputStream(rutaOfertaEducativa);
            libroOFertaEducativa = new XSSFWorkbook(fis1);

            FileInputStream fis2 = new FileInputStream(rutaFuenteNRC);
            libroFuenteNRC = new XSSFWorkbook(fis2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarListadosOfertaEducativa() {
        XSSFSheet hoja = libroOFertaEducativa.getSheet("Oferta educativa");
        listadoOfertaAlfaNumerico = new ArrayList<String>();
        listadoOfertaCruzadosCompartidos = new ArrayList<String>();

        int contadorFilas = FILA_INICIAL_OFERTA;
        XSSFRow fila = hoja.getRow(contadorFilas);

        while (!celdaVacia(fila, 0)) {
            String alfa = (String) retornarValor(fila.getCell(COLUMNA_OFERTA_ALFA));
            String numerico = (String) "" + retornarValor(fila.getCell(COLUMNA_OFERTA_NUMERICO));

            listadoOfertaAlfaNumerico.add(alfa + " " + numerico);

            if (celdaVacia(fila, COLUMNA_OFERTA_CRUZADOSCOMPARTIDOS)) {
                listadoOfertaCruzadosCompartidos.add("");
            } else {
                listadoOfertaCruzadosCompartidos.add((String) retornarValor(fila.getCell(COLUMNA_OFERTA_CRUZADOSCOMPARTIDOS)));
            }

            contadorFilas++;
            fila = hoja.getRow(contadorFilas);
        }
    }

    public void cargarListadosFuenteNRC() {

        XSSFSheet hoja = libroFuenteNRC.getSheetAt(0);
        listadoBannerAlfaNumerico = new ArrayList<String>();
        listadoBannerNRC = new ArrayList<Integer>();

        int contadorFilas = FILA_INICIAL_FUENTE_BANNER;
        XSSFRow fila = hoja.getRow(contadorFilas);

        while (!celdaVacia(fila, 0)) {
            String alfa = (String) retornarValor(fila.getCell(COLUMNA_BANNER_ALFA));
            String numerico = (String) "" + retornarValor(fila.getCell(COLUMNA_BANNER_NUMERICO));

            int nrc = (int) retornarValor(fila.getCell(COLUMNA_BANNER_NRC));

            listadoBannerAlfaNumerico.add(alfa + " " + numerico);
            listadoBannerNRC.add(nrc);

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

    public ArrayList<Integer> indicesCompartido(String compartido) {
        ArrayList<Integer> indices = new ArrayList<Integer>();

        for (int i = 0; i < listadoOfertaCruzadosCompartidos.size(); i++) {
            if (listadoOfertaCruzadosCompartidos.get(i).equals(compartido)) {
                indices.add(i);
            }
        }

        return indices;
    }

    public boolean esCompartida(int id) {
        boolean c = false;
        if (listadoOfertaCruzadosCompartidos.get(id).indexOf("Compartida") != -1) {
            return true;
        }
        return c;
    }
    
    public boolean esCruzada(int id) {
        boolean c = false;
        if (listadoOfertaCruzadosCompartidos.get(id).indexOf("Cruzada") != -1) {
            return true;
        }
        return c;
    }

    public void pasarNrcAlaOfertaEducativa() {
        try {
            FileInputStream fis = new FileInputStream(rutaOfertaEducativa);
            XSSFWorkbook libroSalida = new XSSFWorkbook(fis);
            XSSFSheet hoja = libroSalida.getSheet("Oferta educativa");
            
            //pasamos los nrc encontrados al libro
            for (int i = 0; i < listadoNRCReciclados.size(); i++) {
                String nrc = listadoNRCReciclados.get(i);
                hoja.getRow(i+FILA_INICIAL_OFERTA).getCell(COLUMNA_OFERTA_NRC).setCellValue(nrc);
            }
            
            //creamos el archivo con hora de salida
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
            Date fechaYHoraSalida = new Date();
            String fechaSalidaFormateada = sdf.format(fechaYHoraSalida);
            
            String nombreArchivoSalida = CARPETASALIDA+"/Oferta NRC Reciclados"+fechaSalidaFormateada+".xlsx";
            
            FileOutputStream fos = new FileOutputStream(nombreArchivoSalida);
            
            libroSalida.write(fos);
            libroSalida.close();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
