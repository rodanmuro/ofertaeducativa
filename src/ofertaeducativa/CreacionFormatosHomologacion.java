/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Rodanmuro
 */
public class CreacionFormatosHomologacion {

    String RUTA_ARCHIVO_ESTUDIANTES_HOMOLOGADOS = "D:\\Uniminuto 2017 1\\Programas de homologación\\archivosensayo\\asod.xlsx";
    String RUTA_ARCHIVO_FORMATOS_HOMOLOGACION = "D:\\Uniminuto 2017 1\\Programas de homologación\\archivosensayo\\Formato Homologación.xls";

    FileInputStream isArchivoEstudiantesHomologados, isArchivoFormatosHomologacion;
    XSSFWorkbook wEStudiantesHomologados, wFormatosHomologacion;
    XSSFSheet hEStudiantesHomologados, hMatrizFormatosHomologacion;

    public CreacionFormatosHomologacion(String rutaArchivoEstudiantesHomologados,
            String rutaArchivoFormatosHomologacion) {

        RUTA_ARCHIVO_ESTUDIANTES_HOMOLOGADOS = rutaArchivoEstudiantesHomologados;
        RUTA_ARCHIVO_FORMATOS_HOMOLOGACION = rutaArchivoFormatosHomologacion;

        try {
            isArchivoEstudiantesHomologados = new FileInputStream(RUTA_ARCHIVO_ESTUDIANTES_HOMOLOGADOS);
            wEStudiantesHomologados = new XSSFWorkbook(isArchivoEstudiantesHomologados);
            hEStudiantesHomologados = wEStudiantesHomologados.getSheetAt(0);

            isArchivoFormatosHomologacion = new FileInputStream(RUTA_ARCHIVO_FORMATOS_HOMOLOGACION);
            wFormatosHomologacion = new XSSFWorkbook(isArchivoFormatosHomologacion);
            hMatrizFormatosHomologacion = wFormatosHomologacion.getSheetAt(0);

            //según los registros dados en la hoja asod.xlsx podemos ir creando cada hoja clonada
            int contadorFilas = 0;
            int contadorHojas = 0;
            String nombre = hEStudiantesHomologados.getRow(0).getCell(0).getStringCellValue().trim();
            while (!nombre.equals("")) {
                contadorFilas++;
                

                nombre = "";
                if (hEStudiantesHomologados.getRow(contadorFilas) != null) {
                    if (hEStudiantesHomologados.getRow(contadorFilas).getCell(0) != null) {
                        nombre = hEStudiantesHomologados.getRow(contadorFilas).getCell(0).getStringCellValue().trim();
                    }

                    //vamos a crear un formato cada que se cumpla el filtro
                    String asignatura = "";//hEStudiantesHomologados.getRow(contadorFilas).getCell(17).getStringCellValue().trim();
                    if (hEStudiantesHomologados.getRow(contadorFilas).getCell(17) != null) {
                        asignatura = hEStudiantesHomologados.getRow(contadorFilas).getCell(17).getStringCellValue().trim();
                    }

                    int creditos = 0;//(int) hEStudiantesHomologados.getRow(contadorFilas).getCell(18).getNumericCellValue();
                    if (hEStudiantesHomologados.getRow(contadorFilas).getCell(18) != null) {
                        if((int) hEStudiantesHomologados.getRow(contadorFilas).getCell(18).getCellType()==0){
                        
                            creditos = (int) hEStudiantesHomologados.getRow(contadorFilas).getCell(18).getNumericCellValue();
                        
                        }
                        
                    }

                    double notaFinal = 0;
                    if (hEStudiantesHomologados.getRow(contadorFilas).getCell(20) != null) {
                        if (hEStudiantesHomologados.getRow(contadorFilas).getCell(20).getCellType() == 0) {
                            notaFinal = hEStudiantesHomologados.getRow(contadorFilas).getCell(20).getNumericCellValue();
                        }
                    }
//                    System.out.println("nombre: "+nombre+" asignatura: "+asignatura+" creditos "+creditos+" nota final "+notaFinal+" contadorHojas "+contadorHojas);
                    if ((asignatura.equals("GESTION BASICA DE INFORMACION") || asignatura.equals("GESTION BAS. DE LA INFORMACION") || asignatura.equals("GESTION BASICA DE LA INFORMA"))
                            && (creditos == 2 || creditos == 4)
                            && (notaFinal>=3/*notaFinal == 3 || notaFinal == 4 || notaFinal == 5*/)) {
                        contadorHojas++;
//obtenemos el id del estudiante
                        int idEstudiante = (int) hEStudiantesHomologados.getRow(contadorFilas).getCell(2).getNumericCellValue();
                        
                        System.out.println(/*"Se va a clonar la hoja Entré!!!!" +*/ nombre /*+ " contador filas " + contadorFilas + " id " + idEstudiante+" contador hojas"+contadorHojas*/);
                        
                        XSSFSheet hojaClonada = wFormatosHomologacion.cloneSheet(0, "Formato " + contadorHojas + " " + idEstudiante);
                        hojaClonada.getRow(10).getCell(8).setCellValue(idEstudiante);
                        
                    }
                }
            }

            //creamos el archivo
            File archivoSalida = new File(RUTA_ARCHIVO_FORMATOS_HOMOLOGACION);
            FileOutputStream file = new FileOutputStream(archivoSalida);
            
//            XSSFFormulaEvaluator.evaluateAllFormulaCells(wFormatosHomologacion);
            
            wFormatosHomologacion.write(file);
            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
