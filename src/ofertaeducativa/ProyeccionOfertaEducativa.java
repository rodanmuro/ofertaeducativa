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
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Rodanmuro
 */
public class ProyeccionOfertaEducativa {

    //archivos
    String rutaArchivoMallas;
    String rutaArchivoCupos;
    String rutaArchivoOfertaEducativaTotal;
    String rutaLibroProyeccionPrimerSemestre;
    String rutaLibroColores;

    XSSFWorkbook libroMallas;
    XSSFWorkbook libroCupos;
    XSSFWorkbook libroExcelOfertaEducativaTotal;
    XSSFWorkbook libroProyeccionPrimerSemestre;
    XSSFWorkbook libroColores;

    //archivo de mallas curriculares hoja carreras
    XSSFWorkbook mallasCurriculares;
    XSSFWorkbook archivoCupos;
    //estos datos salen del archivo que por ahora se llama bases de datos.xlsx hoja carreras
    ArrayList<String> listadoCarreras;
    ArrayList<Integer> listadoIdsCarreras;
    ArrayList<String> nomenclaturaCarreras;

    //listado de los valores de los colores
    ArrayList<Integer> listadoR;
    ArrayList<Integer> listadoG;
    ArrayList<Integer> listadoB;

    //archivo de mallas curriculares hoja asignaturas
    //estos datos salen del archivo que por ahora se llama Bases de datos.xlsx, hoja asignaturas
    ArrayList<String> listadoAsignturas;
    ArrayList<Integer> listadoSemestres;
    ArrayList<Integer> idsCarreras;
    ArrayList<String> listadoAlfa;
    ArrayList<String> listadoNumerico;
    ArrayList<Integer> listadoCreditos;
    ArrayList<Integer> listadoSoloVirtual;
    ArrayList<String> listadopAlfa;
    ArrayList<String> listadopNumerico;
    ArrayList<Integer> listadoEsPractica;
    ArrayList<Integer> listadoNumeroSesiones;
    ArrayList<Integer> listadoDuracionSesiones;//en minutos
    ArrayList<Integer> listadoPeriodicidad;//En dias
    ArrayList<Integer> listadoCupoMaximo;

    //archivo de cupos
    //estos datos salen del archivo xlsx que crea la clase, ProyeccionDeCupos.java, y de la hoja llamada Proyeccion
    ArrayList<String> pProgramas;
    ArrayList<String> pSemestres;
    ArrayList<Integer> pCupos;

    //listados del reporte de cupos
    ArrayList<String> cProgramas;//dado como nomenclatura
    ArrayList<String> cAsignatura;
    ArrayList<Integer> cSemestre;
    ArrayList<Integer> cCupoTotal;
    ArrayList<String> cAlfa;
    ArrayList<String> cNumerico;

    //variables para la totalidad por asignaturas
    ArrayList<String> tAsignatura;
    ArrayList<Integer> tCupo;

    //la oferta educativa total, la creada para todos los programas
    ArrayList<ArrayList<Object[]>> listadoOfertaEducativaTotal;
    //el siguiente es un array list que no está clasificado por grupos como el anterior, sino que va derecho
    ArrayList<Object[]> listadoDesglosadoOfertaEducativaTotal;

    //listado de sugerencias para fusionar grupos
    ArrayList<Object[]> listadoSugerenciasCombinarGrupos1;
    ArrayList<Object[]> listadoSugerenciasCombinarGrupos2;

    //listado Observaciones
    ArrayList<String> listadoObervaciones = new ArrayList<String>();

    //variables relativas al archivo con las hojas de seguimiento de materias
    XSSFWorkbook libroSeguimientos;
    String rutaLibroSeguimientos;
    int columnaAlfaSeguimientos = 0;
    int columnaNumericoSeguimientos = 1;
    int columnaAsignaturasSeguimientos = 2;
    int columnaHorasSeguimientos = 4;//en la ultima version estas horas no existen
    int columnaCreditosSeguimientos = 3;
    int columnaSemestresSeguimientos = 4;
    int columnaInicianGrupos = 5;

    int filaEncabezados = 1;
    int filaInicialAlfas = 3;
    int filaNombresGrupos = 1;
    int filaJornadas = 0;
    int filaInscritosEstimados = 2;
    int cantidadMaximaEstudiantes = 43;

    int filaInicialCupos = 2;

    String PERIODOACTUAL = "";

    String MENSAJE_ERROR_INEXISTENCIA_HOJA_SEGUIMIENTOS = "";

    public ProyeccionOfertaEducativa(String rutaLibroMallas,
            String rutaLibroSeguimientos,
            String rutaLibroCupos,
            String rutaLibroProyeccionPrimerSemestre,
            String rutaCarpetaSalida,
            String periodoSiguiente,
            String periodoActual) {

        PERIODOACTUAL = periodoActual;

        //creamos el archivo con hora de salida
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
        Date fechaYHoraSalida = new Date();
        String fechaSalidaFormateada = sdf.format(fechaYHoraSalida);
        rutaArchivoOfertaEducativaTotal = rutaCarpetaSalida + "/Oferta Educativa " + periodoSiguiente + " " + fechaSalidaFormateada + ".xlsx";

        listadoOfertaEducativaTotal = new ArrayList<ArrayList<Object[]>>();
        listadoDesglosadoOfertaEducativaTotal = new ArrayList<Object[]>();

        listadoSugerenciasCombinarGrupos1 = new ArrayList<Object[]>();
        listadoSugerenciasCombinarGrupos2 = new ArrayList<Object[]>();

        rutaLibroColores = "colores archivo insumo para generar colores para oferta educativa.xlsx";

        listadoR = new ArrayList<Integer>();
        listadoG = new ArrayList<Integer>();
        listadoB = new ArrayList<Integer>();

        inicializarInstanciasListadosMallaBase();

        cProgramas = new ArrayList<String>();
        cAsignatura = new ArrayList<String>();
        cAlfa = new ArrayList<String>();
        cNumerico = new ArrayList<String>();
        cSemestre = new ArrayList<Integer>();
        cCupoTotal = new ArrayList<Integer>();

        try {
            File archivoMallas = new File(rutaLibroMallas);
            File archivoCupos = new File(rutaLibroCupos);

            libroMallas = new XSSFWorkbook(archivoMallas);

            rutaArchivoCupos = rutaLibroCupos;

            FileInputStream is = new FileInputStream(rutaArchivoCupos);
            libroCupos = new XSSFWorkbook(is);

            //cargamos el libro de proyeccion de primer semestre
            this.rutaLibroProyeccionPrimerSemestre = rutaLibroProyeccionPrimerSemestre;

            if (!this.rutaLibroProyeccionPrimerSemestre.trim().equals("")) {
                FileInputStream isps = new FileInputStream(this.rutaLibroProyeccionPrimerSemestre);
                libroProyeccionPrimerSemestre = new XSSFWorkbook(isps);
            }

            //se debe cargar tambien el archivo con los seguimientos
            this.rutaLibroSeguimientos = rutaLibroSeguimientos;
            FileInputStream isSeguimientos = new FileInputStream(this.rutaLibroSeguimientos);
            libroSeguimientos = new XSSFWorkbook(isSeguimientos);

            //cargamos carreras
            int contadorFilas = 0;
            XSSFSheet hojaCarreras = libroMallas.getSheet("carreras");
            XSSFRow fila = hojaCarreras.getRow(contadorFilas + 1);

            while (hojaCarreras.getRow(contadorFilas + 1) != null && hojaCarreras.getRow(contadorFilas + 1).getCell(1) != null && !hojaCarreras.getRow(contadorFilas + 1).getCell(1).getStringCellValue().trim().equals("")) {

                XSSFCell celdaId = hojaCarreras.getRow(contadorFilas + 1).getCell(0);
                XSSFCell celdaCarrera = hojaCarreras.getRow(contadorFilas + 1).getCell(1);
                XSSFCell celdaNomenclatura = hojaCarreras.getRow(contadorFilas + 1).getCell(2);

                listadoIdsCarreras.add((int) celdaId.getNumericCellValue());
                listadoCarreras.add(celdaCarrera.getStringCellValue());
                nomenclaturaCarreras.add(celdaNomenclatura.getStringCellValue());

                contadorFilas++;

            }

            //cargo la informacion de las asignaturas
            contadorFilas = 0;
            XSSFSheet hojaAsignaturas = libroMallas.getSheet("asignaturas");
            fila = hojaAsignaturas.getRow(contadorFilas + 1);

            while (hojaAsignaturas.getRow(contadorFilas + 1) != null && hojaAsignaturas.getRow(contadorFilas + 1).getCell(1) != null && !hojaAsignaturas.getRow(contadorFilas + 1).getCell(1).getStringCellValue().trim().equals("")) {

                XSSFCell celdaAsignatura = hojaAsignaturas.getRow(contadorFilas + 1).getCell(1);
                XSSFCell celdaIdCarrera = hojaAsignaturas.getRow(contadorFilas + 1).getCell(2);
                XSSFCell celdaSemestre = hojaAsignaturas.getRow(contadorFilas + 1).getCell(3);
                XSSFCell celdaAlfa = hojaAsignaturas.getRow(contadorFilas + 1).getCell(4);
                XSSFCell celdaNumerico = hojaAsignaturas.getRow(contadorFilas + 1).getCell(5);
                XSSFCell celdaCreditos = hojaAsignaturas.getRow(contadorFilas + 1).getCell(6);
                XSSFCell celdaSoloVirtual = hojaAsignaturas.getRow(contadorFilas + 1).getCell(7);
                XSSFCell celdapAlfa = hojaAsignaturas.getRow(contadorFilas + 1).getCell(9);
                XSSFCell celdapNumerico = hojaAsignaturas.getRow(contadorFilas + 1).getCell(10);
                XSSFCell celdaEsPractica = hojaAsignaturas.getRow(contadorFilas + 1).getCell(11);
                XSSFCell celdaNumeroSesiones = hojaAsignaturas.getRow(contadorFilas + 1).getCell(12);
                XSSFCell celdaDuracionIdealSesiones = hojaAsignaturas.getRow(contadorFilas + 1).getCell(13);
                XSSFCell celdaPeriodicidad = hojaAsignaturas.getRow(contadorFilas + 1).getCell(14);
                XSSFCell celdaCupoMaximo = hojaAsignaturas.getRow(contadorFilas + 1).getCell(15);

                listadoAsignturas.add(celdaAsignatura.getStringCellValue());
                listadoSemestres.add((int) celdaSemestre.getNumericCellValue());
                idsCarreras.add((int) celdaIdCarrera.getNumericCellValue());
                listadoAlfa.add(celdaAlfa.getStringCellValue());

                //para realizar un cambio adecuado en el numerico, que ya no es numerico sino
                //alfanumerico también, entonces se debe agregar lo siguiente:
                listadoNumerico.add((String) ("" + retornarValor(celdaNumerico)));

                listadoCreditos.add((int) celdaCreditos.getNumericCellValue());
                listadoSoloVirtual.add((int) celdaSoloVirtual.getNumericCellValue());

                //cargamos los prerequisitos, se sabe que algunas celdas estarán en blanco
                //por eso los prerequisitos tienen estos if
                if (celdapAlfa == null) {
                    listadopAlfa.add("");
                } else {
                    listadopAlfa.add(celdapAlfa.getStringCellValue());
                }

//                if (celdapNumerico == null) {
//                    listadopNumerico.add("");
//                } else if (celdapNumerico.getCellType() != 0) {
//                    listadopNumerico.add("");
//                } else {
//                    listadopNumerico.add((String) "" + retornarValor(celdapNumerico));
//                }
                if (!celdaVacia(hojaAsignaturas.getRow(contadorFilas + 1), 10)) {
                    listadopNumerico.add((String) "" + retornarValor(celdapNumerico));
                } else {
                    listadopNumerico.add("");
                }

                //cargamos si es práctica o no
                listadoEsPractica.add((int) celdaEsPractica.getNumericCellValue());

                //cargamos el numero de sesiones
                listadoNumeroSesiones.add((int) celdaNumeroSesiones.getNumericCellValue());

                //cargamos la duracion de las sesiones
                listadoDuracionSesiones.add((int) celdaDuracionIdealSesiones.getNumericCellValue());

                //cargamos la periodicidad de las sesiones
                try {
                    listadoPeriodicidad.add((int) celdaPeriodicidad.getNumericCellValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //cargamos el cupo maximo
                if (celdaVacia(hojaAsignaturas.getRow(contadorFilas + 1), 12)) {
                    listadoCupoMaximo.add(cantidadMaximaEstudiantes);
                } else {
                    listadoCupoMaximo.add((int) retornarValor(celdaCupoMaximo));
                }

                contadorFilas++;

            }

            //ahora cargamos la informacion de la hoja de los cupos
            pProgramas = new ArrayList<String>();
            pSemestres = new ArrayList<String>();
            pCupos = new ArrayList<Integer>();

            //cargo la informacion de las asignaturas y sus cupos
            contadorFilas = 0;

            XSSFSheet hojaProyeccion = libroCupos.getSheetAt(0);
            fila = hojaAsignaturas.getRow(contadorFilas + filaInicialCupos);

//            while (hojaProyeccion.getRow(contadorFilas + filaInicialCupos) != null && hojaProyeccion.getRow(contadorFilas + 1).getCell(1) != null && !hojaProyeccion.getRow(contadorFilas + 1).getCell(1).getStringCellValue().trim().equals("")) {
            while (!celdaVacia(hojaProyeccion.getRow(contadorFilas + filaInicialCupos), 0)) {
                XSSFCell celdaNomenclatura = hojaProyeccion.getRow(contadorFilas + filaInicialCupos).getCell(0);
                XSSFCell celdaGrupo = hojaProyeccion.getRow(contadorFilas + filaInicialCupos).getCell(1);
                XSSFCell celdaCupo = hojaProyeccion.getRow(contadorFilas + filaInicialCupos).getCell(3);

                pProgramas.add(celdaNomenclatura.getStringCellValue());
                pSemestres.add(celdaGrupo.getStringCellValue());
                pCupos.add((int) celdaCupo.getNumericCellValue());

                contadorFilas++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        cargarListadoColores();
//
        if (!rutaLibroProyeccionPrimerSemestre.trim().equals("")) {
            crearOfertaEducativPrimerSemestre2();
        }

//
        crearOfertaEducativaTotal();
//
        crearArchivoExcelOfertaEducativaTotal();
//
        crearListadoSugerenciasFusionGrupos();
//
        crearHojaExcelRecomendacionesFusionGrupos();
//
        creacionHojaCuposVirtuales();
//
        crearHojaObservaciones();

        JOptionPane.showMessageDialog(null, "<html>  "
                + MENSAJE_ERROR_INEXISTENCIA_HOJA_SEGUIMIENTOS
                + "<br>"
                + "Se ha terminado de crear la oferta educativa"
                + " </html>");

        String[] elementosAsignaturas = new String[10];

        elementosAsignaturas[0] = "Alfa";
        elementosAsignaturas[1] = "Numerico";
        elementosAsignaturas[2] = "Asignatura";
        elementosAsignaturas[3] = "Horas";
        elementosAsignaturas[4] = "Créditos";
        elementosAsignaturas[5] = "Semestre";
        elementosAsignaturas[6] = "Grupo";
        elementosAsignaturas[7] = "Programa";
        elementosAsignaturas[8] = "Jornada";
        elementosAsignaturas[9] = "Matricula";

//        normalizacionNombresAsignaturas();
        for (int i = 0; i < listadoSugerenciasCombinarGrupos1.size(); i++) {
            String cadena1 = imprimirElementosAsignatura(listadoSugerenciasCombinarGrupos1, i, elementosAsignaturas);
            String cadena2 = imprimirElementosAsignatura(listadoSugerenciasCombinarGrupos2, i, elementosAsignaturas);

            System.out.println(i + " Fusionar " + cadena1 + " ---CON--- " + cadena2);
        }

//
//        //se recorre cada grupo
//        for (int i = 0; i < listadoOfertaEducativaTotal.size(); i++) {
//            //por cada grupo
//            ArrayList<Object[]> listadoOfertaGrupo = new ArrayList<Object[]>(listadoOfertaEducativaTotal.get(i));
//
//            //por cada grupo, imprimimos la oferta educativa
//            for (int j = 0; j < listadoOfertaGrupo.size(); j++) {
//                //por el grupo tomamos la asignatura y sus datos
//                Object[] elementosAsignatura = new Object[10];
//                elementosAsignatura = listadoOfertaGrupo.get(j);
//
//                String alfa = (String) elementosAsignatura[0];
//                int numerico = (int) elementosAsignatura[1];
//                String asignatura = (String) elementosAsignatura[2];
//                int horas = (int) elementosAsignatura[3];
//                String grupoProyectado = (String) elementosAsignatura[6];
//                String nom = (String) elementosAsignatura[7];
//                int matricula = (int) elementosAsignatura[9];
//
//                //System.out.println("Alfa: " + alfa + " Numerico: " + numerico + " Asignatura: " + asignatura + " Horas: " + horas + " Grupo: " + grupoProyectado + " Programa: " + nom + " Matricula: " + matricula);
//            }
//        }
    }

    /**
     * Estos listados salen de la malla base es decir, del archivo, que por
     * ahora se llama base de datos
     */
    public void inicializarInstanciasListadosMallaBase() {
        listadoIdsCarreras = new ArrayList<Integer>();
        listadoCarreras = new ArrayList<String>();
        nomenclaturaCarreras = new ArrayList<String>();

        listadoAsignturas = new ArrayList<String>();
        listadoSemestres = new ArrayList<Integer>();
        idsCarreras = new ArrayList<Integer>();
        listadoAlfa = new ArrayList<String>();
        listadoNumerico = new ArrayList<String>();
        listadoCreditos = new ArrayList<Integer>();
        listadoSoloVirtual = new ArrayList<Integer>();
        //estos dos listados corresponden a los prerequisitos
        listadopAlfa = new ArrayList<String>();
        listadopNumerico = new ArrayList<String>();
        listadoEsPractica = new ArrayList<Integer>();
        listadoNumeroSesiones = new ArrayList<Integer>();
        listadoDuracionSesiones = new ArrayList<Integer>();
        listadoPeriodicidad = new ArrayList<Integer>();
        listadoCupoMaximo = new ArrayList<Integer>();
    }

    public void cargarInformacionMallaBase() {

    }

    /**
     * Determina si una asignatura es virtual, consultando en la base de datos o
     * malla curricular principal.
     *
     * @param alfa
     * @param numerico
     * @return
     */
    public boolean esVirtual(String nomenclaturaCarrera, String alfa, String numerico) {

        boolean ev = false;

        //convertimos la nomenclatura de la carrera a idsCarrera
        int idCarrera = -1;
        for (int i = 0; i < listadoCarreras.size(); i++) {
            if (nomenclaturaCarreras.get(i).trim().equals(nomenclaturaCarrera.trim())) {
                idCarrera = listadoIdsCarreras.get(i);
                break;
            }
        }

        for (int i = 0; i < listadoAsignturas.size(); i++) {
            if (idCarrera == idsCarreras.get(i) && listadoAlfa.get(i).equals(alfa) && listadoNumerico.get(i).equals(numerico) && listadoSoloVirtual.get(i) == 1) {
                ev = true;
                break;
            }
        }

        return ev;

    }

    public static Object retornarValor(XSSFCell celda) {
        Object valor = null;

        if (celda.getCellTypeEnum() == CellType.NUMERIC) {
            valor = (int) celda.getNumericCellValue();
        }
        if (celda.getCellTypeEnum() == CellType.STRING) {
            valor = celda.getStringCellValue();
        }
        return valor;
    }

    //debemos crear una función que nos cargue el máximo de creditos por semestre y por carrera
    //según la malla curricular
    public int maximoCreditosSemestreCarrera(int idCarrera, int semestre) {
        int mc = 0;

        for (int i = 0; i < listadoAsignturas.size(); i++) {
            if (idsCarreras.get(i) == idCarrera && listadoSemestres.get(i) == semestre) {
                mc = mc + listadoCreditos.get(i);
            }
        }

        return mc;
    }

    public int maximoCreditosSemestreCarreraSeguimiento(String nomenclatura, int semestre) {
        int mc = 0;

        XSSFSheet hojaSeguimientos = null;
        XSSFRow fila = null;

        try {
            hojaSeguimientos = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL.trim());
            fila = hojaSeguimientos.getRow(filaInicialAlfas);
        } catch (Exception e) {
            Validaciones.mostrarErroresTotal("Ocurrió un error al buscar la hoja del libro de seguimientos "
                    + nomenclatura + " " + PERIODOACTUAL.trim(), e);
        }
        int contadorFilas = 0;
        while (fila != null) {
            if (fila.getCell(columnaSemestresSeguimientos) != null) {
                if (fila.getCell(columnaSemestresSeguimientos).getCellType() == 0) {
                    int semestreHoja = (int) fila.getCell(columnaSemestresSeguimientos).getNumericCellValue();
                    if (semestre == semestreHoja) {
                        mc = mc + (int) fila.getCell(columnaCreditosSeguimientos).getNumericCellValue();
                    }
                    contadorFilas++;
                    fila = hojaSeguimientos.getRow(filaInicialAlfas + contadorFilas);
                } else {
                    break;
                }

            } else {
                break;
            }

        }

        return mc;
    }

    public int creditosProgramadosOferta(ArrayList<Object[]> oferta) {
        int cp = 0;

        for (int i = 0; i < oferta.size(); i++) {
            cp = cp + (int) oferta.get(i)[4];
        }

        return cp;
    }

    public boolean debeTenerPracticas(String programa, int semestre) {
        boolean dp = false;
        int idCarrera = 0;

        //pasamos el programa a id
        for (int i = 0; i < listadoCarreras.size(); i++) {
            if (programa.equals(nomenclaturaCarreras.get(i))) {
                idCarrera = listadoIdsCarreras.get(i);
            }
        }

        for (int i = 0; i < listadoAsignturas.size(); i++) {
            if (idCarrera == idsCarreras.get(i) && semestre == listadoSemestres.get(i) && listadoEsPractica.get(i) == 1) {
                dp = true;
                break;
            }
        }

        return dp;
    }

    public boolean laOfertaTienePracticas(String programa, int semestre, ArrayList<Object[]> oferta) {
        boolean to = false;

        for (int i = 0; i < oferta.size(); i++) {
            if (esPractica((String) oferta.get(i)[0], (String) oferta.get(i)[1])) {
                to = true;
                break;
            }
        }

        return to;
    }

    public boolean esPractica(String alfa, String numerico) {
        boolean ep = false;

        for (int i = 0; i < listadoAsignturas.size(); i++) {

            //en el caso de comparar un elemento de listado numerico es necesario saber
            //si es un integer o un string
            if (listadoAlfa.get(i).equals(alfa) && ((String) listadoNumerico.get(i)).equals("" + numerico) && listadoEsPractica.get(i) == 1) {
                ep = true;
                break;
            }

        }

        return ep;
    }

    public Object[] practicaParaEseSemestrePrograma(String programa, int semestre) {
        Object[] sp = new Object[2];
        int idCarrera = 0;

        //pasamos el programa a id
        for (int i = 0; i < listadoCarreras.size(); i++) {
            if (programa.equals(nomenclaturaCarreras.get(i))) {
                idCarrera = listadoIdsCarreras.get(i);
            }
        }

        for (int i = 0; i < listadoAsignturas.size(); i++) {
            if (idCarrera == idsCarreras.get(i) && semestre == listadoSemestres.get(i) && listadoEsPractica.get(i) == 1) {

                sp[0] = listadoAlfa.get(i);
                sp[1] = listadoNumerico.get(i);

                break;
            }
        }

        return sp;
    }

    public int creditosParaUnAlfaNumerico(String alfa, String numerico) {
        int c = 0;
        for (int i = 0; i < listadoAlfa.size(); i++) {
            if (alfa.equals(listadoAlfa.get(i)) && (numerico).equals((String) listadoNumerico.get(i))) {
                c = listadoCreditos.get(i);
            }
        }
        return c;
    }

    /**
     * Esta función devuelve un listado, en cada elemento del listado hay una
     * arreglo de objetos dicho arreglo está conformado por alfa, numerico,
     * asignatura, horas, creditos, semestre, grupo, programa, jornada. El
     * insumo de grupos se sacará de las hojas de seguimiento, y los cupos se
     * tomarán como insumo posterior para la fusión de grupos
     *
     * @param nomenclatura
     * @param grupo
     * @return
     */
    public ArrayList<Object[]> crearOfertaGrupo2(String nomenclatura, String grupo) {

        ArrayList<Object[]> oferta = new ArrayList<Object[]>();

        //hacemos un recorrido para encontrar la columna del respectivo grupo en la hoja de
        //seguimientos
        XSSFRow filaEncabezados = null;
        XSSFRow filaNombresJornada = null;

        try {
            filaEncabezados = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL).getRow(this.filaEncabezados);
            filaNombresJornada = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL).getRow(this.filaJornadas);
        } catch (Exception e) {
            Validaciones.mostrarErroresTotal("Ocurrió un error al obtener datos de "
                    + nomenclatura + " " + PERIODOACTUAL, e);
        }

        int contadorColumnas = 0;

        contadorColumnas = conteoColumnasGruposArchivoSeguimientos(filaEncabezados, grupo);

        int columnaGrupoDado = contadorColumnas;

        //el valor de contadorColumnas es el que corresponde a la columna en donde está el grupo
        //empezaremos a bajar desde allí para guardar las asignaturas que se pueden ofertar para
        //dicho grupo.
        //la condición de parada es llegar al máximo de créditos para ese semestre
        //int semestre = semestreRomanoEntero((String) semestreGrupo(grupo)[0]);
        //en esta nueva versión el semestre ya es un número directamente
        int semestre = Integer.parseInt((String) semestreGrupo(grupo)[0]);
//recordar que el máximo de créditos se calcula para el semestre posterior al que se encuentra el 
        //grupo
        int mc = maximoCreditosSemestreCarreraSeguimiento(nomenclatura, semestre + 1);
        int conteoCreditos = 0;

        int contadorFilas = 0;
        int creditosProgramados = 0;

        //buscamos primero las materias obligatorias ya que estas se deben programar primero
        //para ello debemos recorrer todas las filas
        XSSFRow fila = null;
        try {
            fila = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL).getRow(this.filaInicialAlfas + contadorFilas);
        } catch (Exception e) {
            Validaciones.mostrarErroresTotal("Ocurrió un error en "
                    + nomenclatura + " " + PERIODOACTUAL, e);
        }

        while (!celdaVacia(fila, 0)) {
            //este while sirve para cargar las materias obligatorias
            //luego de ello se agregan las materias para completar para oferta
            if (materiaObligatoria(fila, columnaGrupoDado)) {
                Object[] elementosOferta = new Object[14];

                elementosOferta[0] = fila.getCell(columnaAlfaSeguimientos).getStringCellValue();//alfa
                elementosOferta[1] = "" + retornarValor(fila.getCell(columnaNumericoSeguimientos));//numerico
                elementosOferta[2] = fila.getCell(columnaAsignaturasSeguimientos).getStringCellValue();//asignatura
                elementosOferta[3] = (int) fila.getCell(columnaHorasSeguimientos).getNumericCellValue();//horas//en la última versión no existe esta
                elementosOferta[4] = (int) fila.getCell(columnaCreditosSeguimientos).getNumericCellValue();//creditos
                elementosOferta[5] = (int) fila.getCell(columnaSemestresSeguimientos).getNumericCellValue();//semestre actual

                //creamos el semestre proyectado para el siguiente semestre
                //el semestre proyedctao ya es un número directamente, no es necesario pasarlo
                //de número entero a número romano, con la nueva versión el semestre se escribe
                //por ejemplo 3 4, para indicadar semestre 3 grupo 4
                int semestreProyectado = semestre + 1;
                //creamos el semestre sin proyectar
                int semestreSinProyectar = semestre;

                String letraGrupoProyectado = (String) semestreGrupo(grupo)[1];
                String grupoProyectadoSemestreGrupo = semestreProyectado + "G" + letraGrupoProyectado;

                String grupoSinProyectarSemestreGrupo = semestreSinProyectar + "G" + letraGrupoProyectado;

                if (letraGrupoProyectado.trim().equals("")) {
                    grupoProyectadoSemestreGrupo = "" + semestreProyectado;
                }

                elementosOferta[6] = grupoProyectadoSemestreGrupo;//en la nueva versión es el semestrenumero, es decir, ell semestre ya se escribe, por ejemplo 4 3 cuarto semestre grupo 3//versión vieja grupo en romano y letra proyectado
                elementosOferta[7] = nomenclatura;//Programa en forma de nomenclatura
                elementosOferta[8] = (String) filaNombresJornada.getCell(columnaGrupoDado).getStringCellValue();//jornada
                elementosOferta[9] = matriculaActualGrupo(nomenclatura, grupoSinProyectarSemestreGrupo.trim());//cupo matriculado en dicho grupo

                elementosOferta[10] = retornarNumeroSesionesAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                elementosOferta[11] = retornarDuracionSesionAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                elementosOferta[12] = retornarPeriodicidadAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                elementosOferta[13] = retornarCupoMaximoAsignatura(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);

                //si la asginatura es virtual, al nombre de la misma se le va a colocar - Virtual
                if (esVirtual((String) elementosOferta[7], (String) elementosOferta[0], (String) elementosOferta[1])) {
                    elementosOferta[2] = (String) elementosOferta[2] + " - Virtual";
                }

                if (yaVioPrerequisito((String) elementosOferta[0], (String) elementosOferta[1], columnaGrupoDado, nomenclatura)) {

                    conteoCreditos = conteoCreditos + (int) elementosOferta[4];
                    creditosProgramados = conteoCreditos;

                    //este if acá se coloca, porque al agregar la oferta se puede pasar
                    //por ejemplo: si el maximo de creditos es 13, y llevamos 11 creditos programados
                    //y la materia que sigue es de 3 creditos, vamos a tener 14 creditos, y si no tiene
                    //este if, va a quedar programado
                    if (creditosProgramados > mc) {
                        //si nos pasamos de los creditos, no asignamos oferta, sino que pasamos a la fila posterior
                        //y restamos esos créditos que acabamos de sumar. Es importante tener en cuenta, que él siempre busca en línea
                        //no se ha implementado una solución si se acaban las materias y no se pueden programar créditos
                        conteoCreditos = conteoCreditos - (int) elementosOferta[4];
                        creditosProgramados = conteoCreditos;
                    } else {
                        oferta.add(elementosOferta);
                    }
                }
//                System.out.println("Programa: " + nomenclatura + " Grupo proyectado " + grupoProyectadoRomanoLetra + " " + " Asignatura " + (String) fila.getCell(3).getStringCellValue());

            }
            contadorFilas++;
            fila = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL).getRow(this.filaInicialAlfas + contadorFilas);
        }

        contadorFilas = 0;
        while (conteoCreditos < mc) {
            fila = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL).getRow(this.filaInicialAlfas + contadorFilas);

            boolean cv = celdaVacia(fila, 0);
            if (cv) {
                break;
            }

            //si la materia ya está vista, o es obligatoria, se debe contiuar
            //ya que una materia obligatoria ya se debió programar
            if (materiaVista(fila, (columnaGrupoDado)) || materiaObligatoria(fila, (columnaGrupoDado))) {
                contadorFilas++;
            } else {

                Object[] elementosOferta = new Object[14];

                elementosOferta[0] = fila.getCell(columnaAlfaSeguimientos).getStringCellValue();//alfa
                elementosOferta[1] = "" + retornarValor(fila.getCell(columnaNumericoSeguimientos));//numerico
                elementosOferta[2] = fila.getCell(columnaAsignaturasSeguimientos).getStringCellValue();//asignatura
                elementosOferta[3] = (int) fila.getCell(columnaHorasSeguimientos).getNumericCellValue();//horas//en la última versión no existe esta
                elementosOferta[4] = (int) fila.getCell(columnaCreditosSeguimientos).getNumericCellValue();//creditos
                elementosOferta[5] = (int) fila.getCell(columnaSemestresSeguimientos).getNumericCellValue();//semestre actual

                //creamos el semestre proyectado para el siguiente semestre
                int semestreProyectado = semestre + 1;
                //creamos el semestre sin proyectar
                int semestreSinProyectar = semestre;

                String letraGrupoProyectado = (String) semestreGrupo(grupo)[1];
                String grupoProyectadoSemestre = semestreProyectado + "G" + letraGrupoProyectado;

                String grupoSinProyectarRomanoLetra = semestreSinProyectar + "G" + letraGrupoProyectado;

                if (letraGrupoProyectado.trim().equals("")) {
                    grupoProyectadoSemestre = "" + semestreProyectado;
                }

                elementosOferta[6] = grupoProyectadoSemestre;//nueva versión es numero nnumero//grupo en romano y letra proyectado
                elementosOferta[7] = nomenclatura;//Programa en forma de nomenclatura
                elementosOferta[8] = (String) filaNombresJornada.getCell(columnaGrupoDado).getStringCellValue();//jornada
                elementosOferta[9] = matriculaActualGrupo(nomenclatura, grupoSinProyectarRomanoLetra.trim());//cupo matriculado en dicho grupo

                elementosOferta[10] = retornarNumeroSesionesAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                elementosOferta[11] = retornarDuracionSesionAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                elementosOferta[12] = retornarPeriodicidadAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                elementosOferta[13] = retornarCupoMaximoAsignatura(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);

                //si la asginatura es virtual, al nombre de la misma se le va a colocar - Virtual
                if (esVirtual((String) elementosOferta[7], (String) elementosOferta[0], (String) elementosOferta[1])) {
                    elementosOferta[2] = (String) elementosOferta[2] + " - Virtual";
                }

                if (yaVioPrerequisito((String) elementosOferta[0], (String) elementosOferta[1], columnaGrupoDado, nomenclatura)) {

                    conteoCreditos = conteoCreditos + (int) elementosOferta[4];
                    creditosProgramados = conteoCreditos;

                    //este if acá se coloca, porque al agregar la oferta se puede pasar
                    //por ejemplo: si el maximo de creditos es 13, y llevamos 11 creditos programados
                    //y la materia que sigue es de 3 creditos, vamos a tener 14 creditos, y si no tiene
                    //este if, va a quedar programado
                    if (creditosProgramados > mc) {
                        //si nos pasamos de los creditos, no asignamos oferta, sino que pasamos a la fila posterior
                        //y restamos esos créditos que acabamos de sumar. Es importante tener en cuenta, que él siempre busca en línea
                        //no se ha implementado una solución si se acaban las materias y no se pueden programar créditos
                        conteoCreditos = conteoCreditos - (int) elementosOferta[4];
                        creditosProgramados = conteoCreditos;
                    } else {
                        oferta.add(elementosOferta);
                    }

                }
//                System.out.println("Programa: " + nomenclatura + " Grupo proyectado " + grupoProyectadoRomanoLetra + " " + " Asignatura " + (String) fila.getCell(3).getStringCellValue());

                contadorFilas++;
            }
        }

        //en el caso que falte un crédito en la oferta creada
        if (mc - creditosProgramados == 1) {
            int indiceMateriaConDosCreditos = -1;
            for (int i = 0; i < oferta.size(); i++) {
                //recorremos la oferta, para encontrar una materia de dos creditos
                if ((int) oferta.get(i)[4] == 2) {
                    indiceMateriaConDosCreditos = i;
                }
            }

            //tomamos el alfanumerico de la ultima materia de la oferta
            String alfa = (String) oferta.get(oferta.size() - 1)[0];
            String numerico = (String) "" + oferta.get(oferta.size() - 1)[1];

            contadorFilas = 0;
            fila = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL).getRow(this.filaInicialAlfas + contadorFilas);

            if (indiceMateriaConDosCreditos != -1) {
                while (!celdaVacia(fila, 0)) {
                    String alfah = fila.getCell(columnaAlfaSeguimientos).getStringCellValue();
                    String numericoh = (String) "" + retornarValor(fila.getCell(columnaNumericoSeguimientos));

                    if (alfah.equals(alfa) && numerico.equals(numericoh)) {
                        contadorFilas = contadorFilas + 1;
                        break;
                    }

                    contadorFilas++;
                    fila = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL).getRow(this.filaInicialAlfas + contadorFilas);
                }

                //el contador de filas quedó con el valor posterior al de la fila, donde está la materia
                fila = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL).getRow(this.filaInicialAlfas + contadorFilas);
                while (!celdaVacia(fila, 0)) {

                    String alfav = fila.getCell(0).getStringCellValue();
                    String numericov = (String) "" + retornarValor(fila.getCell(1));

                    try {
                        if (fila.getCell(columnaCreditosSeguimientos).getNumericCellValue() == 3 && yaVioPrerequisito(alfav, numericov, columnaGrupoDado, nomenclatura)) {
                            //quitamos el elemento
                            oferta.remove(indiceMateriaConDosCreditos);
                            //agregamos el encontrado
                            Object[] elementosOferta = new Object[14];

                            elementosOferta[0] = fila.getCell(columnaAlfaSeguimientos).getStringCellValue();//alfa
                            elementosOferta[1] = "" + retornarValor(fila.getCell(columnaNumericoSeguimientos));//numerico
                            elementosOferta[2] = fila.getCell(columnaAsignaturasSeguimientos).getStringCellValue();//asignatura
                            elementosOferta[3] = (int) fila.getCell(columnaCreditosSeguimientos).getNumericCellValue();//horas
                            elementosOferta[4] = (int) fila.getCell(columnaCreditosSeguimientos).getNumericCellValue();//creditos
                            elementosOferta[5] = (int) fila.getCell(columnaSemestresSeguimientos).getNumericCellValue();//semestre actual

                            //creamos el semestre proyectado para el siguiente semestre
                            int semestreProyectado = semestre + 1;
                            int semestreSinProyectar = semestre;
                            String letraGrupoProyectado = (String) semestreGrupo(grupo)[1];
                            String grupoProyectadoSemestreEntero = semestreProyectado + "G" + letraGrupoProyectado;

                            String grupoSinProyectarSemestreEntero = semestreSinProyectar + "G" + letraGrupoProyectado;

                            if (letraGrupoProyectado.trim().equals("")) {
                                grupoProyectadoSemestreEntero = "" + semestreProyectado;
                            }

                            elementosOferta[6] = grupoProyectadoSemestreEntero;//grupo en romano y letra proyectado
                            elementosOferta[7] = nomenclatura;//Programa en forma de nomenclatura
                            elementosOferta[8] = (String) filaNombresJornada.getCell(columnaGrupoDado).getStringCellValue();//jornada
                            elementosOferta[9] = matriculaActualGrupo(nomenclatura, grupoSinProyectarSemestreEntero.trim());//cupo matriculado en dicho grupo

                            elementosOferta[10] = retornarNumeroSesionesAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                            elementosOferta[11] = retornarDuracionSesionAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                            elementosOferta[12] = retornarPeriodicidadAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                            elementosOferta[13] = retornarCupoMaximoAsignatura(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                            //si la asginatura es virtual, al nombre de la misma se le va a colocar - Virtual
                            if (esVirtual((String) elementosOferta[7], (String) elementosOferta[0], (String) elementosOferta[1])) {
                                elementosOferta[2] = (String) elementosOferta[2] + " - Virtual";
                            }

                            oferta.add(elementosOferta);
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    contadorFilas++;
                    fila = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL).getRow(this.filaInicialAlfas + contadorFilas);
                }
            }

        }

        /////////////////////////segunda forma de oferta
        //se realiza la diferencia de créditos, entre los programados y el máximo de créditos
        //hasta acá ya se han recorrido todas las materias, si hay creditos faltantes entonces se hace necesario
        //mover materias
        int cpo = creditosProgramadosOferta(oferta);
        if (cpo < mc && creditosProgramados != 0) {
            String cadena = "Por debajo,Maxímo de créditos: " + mc + ",Programados: " + cpo + " " + ",Programa: " + nomenclatura + ",Grupo " + oferta.get(oferta.size() - 1)[6] + ",Créditos programados están por debajo de los del semestre";
            System.out.println(cadena);
            listadoObervaciones.add(cadena);
        }
        if (cpo > mc && creditosProgramados != 0) {
            String cadena = "Por encima,Maxímo de créditos: " + mc + ",Programados: " + cpo + " " + ",Programa: " + nomenclatura + ",Grupo " + oferta.get(oferta.size() - 1)[6] + ",Créditos programados están por encima de los del semestre";;
            System.out.println(cadena);
            listadoObervaciones.add(cadena);
        }

        //si la oferta no tiene práctica y la debe tener, entonces
        //se quitará una materia que tenga la misma cantidad de créditos, que la práctica
        //y se agregará dicha práctica
        if (oferta.size() > 0) {
            String p = (String) oferta.get(0)[7];
            String s = (String) oferta.get(0)[6];
            int sn = Integer.parseInt((String) semestreGrupo(s)[0]);

            if (debeTenerPracticas(p, sn)) {
                if (!laOfertaTienePracticas(p, sn, oferta)) {

                    //se debe obtener el alfa numerico de la práctica que se debe ver ese semestre
                    Object[] alfaNumericoPractica = practicaParaEseSemestrePrograma(nomenclatura, sn);
                    //obtenemos los créditos de esa práctica
                    int creditosPractica = creditosParaUnAlfaNumerico((String) alfaNumericoPractica[0], (String) "" + alfaNumericoPractica[1]);

                    //recorremos la oferta para determinar que materia será cambiada
                    int indiceMateriaConIgualCreditos = -1;
                    for (int i = 0; i < oferta.size(); i++) {
                        //recorremos la oferta, para encontrar una materia de dos creditos
                        if ((int) oferta.get(i)[4] == creditosPractica) {
                            indiceMateriaConIgualCreditos = i;
                        }
                    }

                    //el contador de filas quedó con el valor posterior al de la fila, donde está la materia
                    contadorFilas = 0;
                    fila = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL).getRow(this.filaInicialAlfas + contadorFilas);
                    while (!celdaVacia(fila, 0)) {

                        String alfapractica = (String) alfaNumericoPractica[0];
                        String numericopractica = (String) "" + alfaNumericoPractica[1];

                        if (fila.getCell(columnaAlfaSeguimientos).getStringCellValue().trim().equals(alfapractica)
                                && retornarValor(fila.getCell(columnaNumericoSeguimientos)).equals(numericopractica)
                                && indiceMateriaConIgualCreditos != -1) {
                            //quitamos el elemento
                            oferta.remove(indiceMateriaConIgualCreditos);
                            //agregamos el encontrado
                            Object[] elementosOferta = new Object[14];

                            elementosOferta[0] = fila.getCell(columnaAlfaSeguimientos).getStringCellValue();//alfa
                            elementosOferta[1] = (String) "" + retornarValor(fila.getCell(columnaNumericoSeguimientos));//numerico
                            elementosOferta[2] = fila.getCell(columnaAsignaturasSeguimientos).getStringCellValue();//asignatura
                            elementosOferta[3] = (int) fila.getCell(columnaCreditosSeguimientos).getNumericCellValue();//horas
                            elementosOferta[4] = (int) fila.getCell(columnaCreditosSeguimientos).getNumericCellValue();//creditos
                            elementosOferta[5] = (int) fila.getCell(columnaSemestresSeguimientos).getNumericCellValue();//semestre actual

                            int semestreSinProyectar = semestre;
                            //creamos el semestre proyectado para el siguiente semestre
                            int semestreProyectado = semestre + 1;
                            String letraGrupoProyectado = (String) semestreGrupo(grupo)[1];
                            String grupoProyectadoSemestreGrupo = semestreProyectado + " " + letraGrupoProyectado;

                            String grupoSinProyectarSemestreGrupo = semestreSinProyectar + " " + letraGrupoProyectado;

                            if (letraGrupoProyectado.trim().equals("")) {
                                grupoProyectadoSemestreGrupo = "" + semestreProyectado;
                            }

                            elementosOferta[6] = grupoProyectadoSemestreGrupo;//grupo en romano y letra proyectado
                            elementosOferta[7] = nomenclatura;//Programa en forma de nomenclatura
                            elementosOferta[8] = (String) filaNombresJornada.getCell(columnaGrupoDado).getStringCellValue();//jornada
                            elementosOferta[9] = matriculaActualGrupo(nomenclatura, grupoSinProyectarSemestreGrupo.trim());//cupo matriculado en dicho grupo

                            elementosOferta[10] = retornarNumeroSesionesAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                            elementosOferta[11] = retornarDuracionSesionAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                            elementosOferta[12] = retornarPeriodicidadAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                            elementosOferta[13] = retornarCupoMaximoAsignatura(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                            //si la asginatura es virtual, al nombre de la misma se le va a colocar - Virtual
                            if (esVirtual((String) elementosOferta[7], (String) elementosOferta[0], (String) "" + elementosOferta[1])) {
                                elementosOferta[2] = (String) elementosOferta[2] + " - Virtual";
                            }

                            oferta.add(elementosOferta);
                            break;
                        }
                        contadorFilas++;
                        fila = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL).getRow(this.filaInicialAlfas + contadorFilas);
                    }

                }
            }
        }

        //revisamos si la oferta debe tener practicas, y si no la tiene, generamos una observacion
        if (oferta.size() > 0) {
            String p = (String) oferta.get(0)[7];
            String s = (String) oferta.get(0)[6];
            int sn = Integer.parseInt((String) semestreGrupo(s)[0]);

            if (debeTenerPracticas(p, sn)) {
                if (!laOfertaTienePracticas(p, sn, oferta)) {
                    String cadena = "El grupo " + p + " " + s + " no tiene práctica programada";
                    System.out.println(cadena);
                    listadoObervaciones.add(cadena);
                }
            }
        }

        //si la oferta está lista la agregamos al listadodetallados de materias
        for (int i = 0; i < oferta.size(); i++) {
            listadoDesglosadoOfertaEducativaTotal.add(oferta.get(i));
        }
        return oferta;
    }

    public ArrayList<Object> auxliarCrearOferta() {
        ArrayList<Object> oferta = new ArrayList<Object>();

        return oferta;
    }

    /**
     * Esta función devuelve un listado, en cada elemento del listado hay una
     * arreglo de objetos dicho arreglo está conformado por alfa, numerico,
     * asignatura, horas, creditos, semestre, grupo, programa, jornada. El
     * insumo de grupos se sacará de las hojas de seguimiento, y los cupos se
     * tomarán como insumo posterior para la fusión de grupos
     *
     * @param nomenclatura
     * @param grupo
     * @return
     */
    public ArrayList<Object[]> crearOfertaGrupo(String nomenclatura, String grupo) {

        ArrayList<Object[]> oferta = new ArrayList<Object[]>();

        //hacemos un recorrido para encontrar la columna del respectivo grupo en la hoja de
        //seguimientos
        XSSFRow filaEncabezados = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL).getRow(this.filaEncabezados);
        XSSFRow filaNombresJornada = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL).getRow(this.filaJornadas);

        int contadorColumnas = 0;
        while (filaEncabezados.getCell(contadorColumnas) != null) {
            if (filaEncabezados.getCell(contadorColumnas).getStringCellValue().equals(grupo)) {
                break;
            }
            contadorColumnas++;
        }

        int columnaGrupoDado = contadorColumnas;

        //el valor de contadorColumnas es el que corresponde a la columna en donde está el grupo
        //empezaremos a bajar desde allí para guardar las asignaturas que se pueden ofertar para
        //dicho grupo.
        //la condición de parada es llegar al máximo de créditos para ese semestre
        int semestre = semestreRomanoEntero((String) semestreGrupo(grupo)[0]);
        //recordar que el máximo de créditos se calcula para el semestre posterior al que se encuentra el 
        //grupo
        int mc = maximoCreditosSemestreCarreraSeguimiento(nomenclatura, semestre + 1);
        int conteoCreditos = 0;

        int contadorFilas = 0;
        int creditosProgramados = 0;
        while (conteoCreditos <= mc) {
            XSSFRow fila = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL).getRow(this.filaInicialAlfas + contadorFilas);
            if (fila != null) {
                XSSFCell celdaSiProgramada = fila.getCell(columnaGrupoDado);

                if (celdaSiProgramada != null) {
                    String valorCelda = celdaSiProgramada.getStringCellValue();
                    if (!valorCelda.trim().equals("")) {
                        //si la celda no está nula, y su contenido no es vacio, entonces ya la vió
                        //por tanto, se pasa a la siguiente celda
                        contadorFilas++;
                    } else {
                        //si la celda está vacía, ella quiere decir que no la ha visto, por tanto
                        //se agrega a la oferta, y se suman los créditos

                        //si agregandolo me paso, debo parar
                        if (conteoCreditos + (int) fila.getCell(5).getNumericCellValue() > mc) {
                            break;
                        }

                        Object[] elementosOferta = new Object[14];

                        elementosOferta[0] = fila.getCell(0).getStringCellValue();//alfa
                        elementosOferta[1] = (String) "" + retornarValor(fila.getCell(1));//numerico
                        elementosOferta[2] = fila.getCell(2).getStringCellValue();//asignatura
                        elementosOferta[3] = (int) fila.getCell(4).getNumericCellValue();//horas
                        elementosOferta[4] = (int) fila.getCell(5).getNumericCellValue();//creditos
                        elementosOferta[5] = (int) fila.getCell(6).getNumericCellValue();//semestre actual

                        String semestreSinProyectar = semestreEnteroRomano(semestre);
                        //creamos el semestre proyectado para el siguiente semestre
                        String semestreProyectado = semestreEnteroRomano(semestre + 1);
                        String letraGrupoProyectado = (String) semestreGrupo(grupo)[1];
                        String grupoProyectadoRomanoLetra = semestreProyectado + " " + letraGrupoProyectado;

                        String grupoSinProyectarRomanoLetra = semestreSinProyectar + " " + letraGrupoProyectado;

                        if (letraGrupoProyectado.trim().equals("")) {
                            grupoProyectadoRomanoLetra = semestreProyectado;
                        }

                        elementosOferta[6] = grupoProyectadoRomanoLetra;//grupo en romano y letra proyectado
                        elementosOferta[7] = nomenclatura;//Programa en forma de nomenclatura
                        elementosOferta[8] = (String) filaNombresJornada.getCell(columnaGrupoDado).getStringCellValue();//jornada
                        elementosOferta[9] = matriculaActualGrupo(nomenclatura, grupoSinProyectarRomanoLetra.trim());//cupo matriculado en dicho grupo

                        elementosOferta[10] = retornarNumeroSesionesAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                        elementosOferta[11] = retornarDuracionSesionAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                        elementosOferta[12] = retornarPeriodicidadAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                        elementosOferta[13] = retornarCupoMaximoAsignatura(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                        //si la asginatura es virtual, al nombre de la misma se le va a colocar - Virtual
                        if (esVirtual((String) elementosOferta[7], (String) elementosOferta[0], (String) elementosOferta[1])) {
                            elementosOferta[2] = (String) elementosOferta[2] + " - Virtual";
                        }

                        if (yaVioPrerequisito((String) elementosOferta[0], (String) elementosOferta[1], columnaGrupoDado, nomenclatura)) {
                            oferta.add(elementosOferta);
                            listadoDesglosadoOfertaEducativaTotal.add(elementosOferta);

                            conteoCreditos = conteoCreditos + (int) elementosOferta[4];
                            creditosProgramados = conteoCreditos;
                        }

                        //luego de guardada la información se pasa a la siguiente fila
                        contadorFilas++;
                    }
                }
                if (celdaSiProgramada == null) {
                    //si entra acá es que la celda es nula
                    //si es nula, pero al lado izquierdo hay materia
                    if (fila.getCell(columnaAsignaturasSeguimientos) != null) {
                        if (!fila.getCell(columnaAsignaturasSeguimientos).getStringCellValue().equals("")) {
                            //la celda está nula, pero hay una materia, entonces se debe programar
                            //si la celda está vacía, ella quiere decir que no la ha visto, por tanto
                            //se agrega a la oferta, y se suman los créditos

                            //si agregandolo me paso, debo parar
                            if (conteoCreditos + (int) fila.getCell(5).getNumericCellValue() > mc) {
                                break;
                            }

                            Object[] elementosOferta = new Object[14];

                            elementosOferta[0] = fila.getCell(0).getStringCellValue();//alfa
                            elementosOferta[1] = (String) "" + retornarValor(fila.getCell(1));//numerico
                            elementosOferta[2] = fila.getCell(2).getStringCellValue();//asignatura
                            elementosOferta[3] = (int) fila.getCell(4).getNumericCellValue();//horas
                            elementosOferta[4] = (int) fila.getCell(5).getNumericCellValue();//creditos
                            elementosOferta[5] = fila.getCell(6).getNumericCellValue();//semestre actual

                            //creamos el semestre proyectado para el siguiente semestre
                            String semestreProyectado = semestreEnteroRomano(semestre + 1);
                            String letraGrupoProyectado = (String) semestreGrupo(grupo)[1];
                            String grupoProyectadoRomanoLetra = semestreProyectado + " " + letraGrupoProyectado;

                            if (letraGrupoProyectado.trim().equals("")) {
                                grupoProyectadoRomanoLetra = semestreProyectado;
                            }

                            elementosOferta[6] = grupoProyectadoRomanoLetra;//grupo en romano y letra proyectado
                            elementosOferta[7] = nomenclatura;//Programa en forma de nomenclatura
                            elementosOferta[8] = (String) filaNombresJornada.getCell(columnaGrupoDado).getStringCellValue();//jornada
                            elementosOferta[9] = matriculaActualGrupo(nomenclatura, grupoProyectadoRomanoLetra.trim());//cupo matriculado en dicho grupo

                            elementosOferta[10] = retornarNumeroSesionesAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                            elementosOferta[11] = retornarDuracionSesionAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                            elementosOferta[12] = retornarPeriodicidadAlfaNumericoCarrera(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                            elementosOferta[13] = retornarCupoMaximoAsignatura(fila.getCell(columnaAlfaSeguimientos).getStringCellValue(), "" + retornarValor(fila.getCell(columnaNumericoSeguimientos)), nomenclatura);
                            //si la asginatura es virtual, al nombre de la misma se le va a colocar - Virtual
                            if (esVirtual((String) elementosOferta[7], (String) elementosOferta[0], (String) elementosOferta[1])) {
                                elementosOferta[2] = (String) elementosOferta[2] + " - Virtual";
                            }

                            if (yaVioPrerequisito((String) elementosOferta[0], (String) elementosOferta[1], columnaGrupoDado, nomenclatura)) {
                                oferta.add(elementosOferta);
                                listadoDesglosadoOfertaEducativaTotal.add(elementosOferta);

                                conteoCreditos = conteoCreditos + (int) elementosOferta[4];
                                creditosProgramados = conteoCreditos;
                            }

                            //luego de guardada la información se pasa a la siguiente fila
                            contadorFilas++;

                        } else {
                            break;
                        }
                    }
                    if (fila.getCell(columnaAsignaturasSeguimientos) == null) {
                        //si entra acá es porque no hay materia, por tanto se detiene el while
                        break;
                    }

                }
            }

            if (fila == null) {
                break;
            }

        }

        //se realiza la diferencia de créditos, entre los programados y el máximo de créditos
        int creditosFaltantes = mc - creditosProgramados;
        if (creditosFaltantes > 0) {

            //esta parte consiste en repetir el anterior while, hasta que se acaben las materias, o bien
            //hasta que se complete la cantidad de créditos, la diferencia con el anterior
            //es que acá se exige estrictamente la igualdad de créditos
        }//acá finaliza el if de los créditos faltantes
        if (creditosProgramados < mc && creditosProgramados != 0) {
            System.out.println("Maxímo de créditos: " + mc + " Programados: " + creditosProgramados + " " + " Programa: " + nomenclatura + " Grupo " + oferta.get(oferta.size() - 1)[6]);
        }

        return oferta;
    }

    public void crearOfertaEducativPrimerSemestre() {
        //esta función tiene como insumos la malla curricular general (Base de datos.xlsx)
        //y el archivo con los números de metas educativas, llamado en este momento proyeccionprimersemestre.xlsx

        int filaEncabezados = 0;

        int filaInicialProgramasArchivoMetas = 1;

        int columnaInicialProgramasArchivoMetas = 0;
        int columnaJornadaNoche = 2;
        int columnaJornadaSabadoD = 3; //sabado mañana
        int columnaJornadaSabadoT = 4; //sabado tarde

        int columnaInicialValoresGruposCupo = 2;
        int totalProgramasMetas = 7;//9 en el año 2019-1 
//        LEID
//EGFI
//AEMD
//ASST
//EGPR
//LPID
//PSID
//COSD
//COPD

        String jornadaMetas = "";

        int totalJornadas = 0;

        //debe estar en la hoja 1
        XSSFSheet hojaMetas = libroProyeccionPrimerSemestre.getSheetAt(0);

        for (int i = 0; i < totalProgramasMetas; i++) {
            int contadorGrupos = 0;
            XSSFRow fila = hojaMetas.getRow(filaInicialProgramasArchivoMetas + i);
            String programa = fila.getCell(columnaInicialProgramasArchivoMetas).getStringCellValue().trim();

            int contadorColumnas = 0;

            //contamos el total de jornadas propuestas en el archivo de metas
            //el nomnre de la jornada está en la misma columna pero en la fila anterior
            XSSFRow filaEnc = hojaMetas.getRow(filaEncabezados);
            while (filaEnc.getCell(columnaInicialValoresGruposCupo + totalJornadas) != null && !filaEnc.getCell(columnaInicialValoresGruposCupo + totalJornadas).getStringCellValue().trim().equals("")) {
                totalJornadas++;
            }

            //while (fila.getCell(columnaInicialValoresGruposCupo + contadorColumnas) != null && !fila.getCell(columnaInicialValoresGruposCupo + contadorColumnas).getStringCellValue().trim().equals("")) {
            for (int j = 0; j < totalJornadas; j++) {
                String gruposCupo = "";

                try {
                    gruposCupo = fila.getCell(columnaInicialValoresGruposCupo + contadorColumnas).getStringCellValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int numeroGrupos = numeroGruposCupo(gruposCupo)[0];
                int cupo = numeroGruposCupo(gruposCupo)[1];

                String jornada = jornadaDeMetasANormal(filaEnc.getCell(columnaInicialValoresGruposCupo + contadorColumnas).getStringCellValue().trim());

                contadorColumnas++;

                //por cada grupo creado acá, cuyo cupo sea diferente de cero, se debe crear
                //la proyección de materias, tomando como insumo la malla curricular base. recoredar que acá todos son de primer semestre
                if (cupo != 0) {
                    for (int l = 0; l < numeroGrupos; l++) {

                        for (int k = 0; k < listadoAsignturas.size(); k++) {
                            if (listadoSemestres.get(k) == 1 && idsCarreras.get(k) == idCarrera(programa)) {
                                Object[] elementosAsignaturas = new Object[14];

                                elementosAsignaturas[0] = listadoAlfa.get(k);//"Alfa";
                                elementosAsignaturas[1] = listadoNumerico.get(k);//"Numerico";
                                elementosAsignaturas[2] = listadoAsignturas.get(k);//"Asignatura";
                                elementosAsignaturas[3] = listadoCreditos.get(k) * 2;//"Horas";
                                elementosAsignaturas[4] = listadoCreditos.get(k);//"Créditos";
                                elementosAsignaturas[5] = listadoSemestres.get(k);//"Semestre";
                                elementosAsignaturas[6] = "I " + devolverLetraSegunIndice(contadorGrupos);//"Grupo";
                                elementosAsignaturas[7] = programa;//"Programa";
                                elementosAsignaturas[8] = jornada;//"Jornada";
                                elementosAsignaturas[9] = cupo;//"Matricula";

                                elementosAsignaturas[10] = listadoNumeroSesiones.get(k);
                                elementosAsignaturas[11] = listadoDuracionSesiones.get(k);
                                elementosAsignaturas[12] = listadoPeriodicidad.get(k);
                                elementosAsignaturas[13] = listadoCupoMaximo.get(k);

                                //si la asginatura es virtual, al nombre de la misma se le va a colocar - Virtual
                                if (esVirtual((String) elementosAsignaturas[7], (String) elementosAsignaturas[0], (String) elementosAsignaturas[1])) {
                                    elementosAsignaturas[2] = (String) elementosAsignaturas[2] + " - Virtual";
                                }

                                listadoDesglosadoOfertaEducativaTotal.add(elementosAsignaturas);

                            }
                        }

                        //el contador de grupos se reinicia con el cambio de fila, o bien con el cambio de programa
                        contadorGrupos++;

                    }

                }

                System.out.println("Primer semestre Numero Grupos: " + numeroGrupos + " cupos: " + cupo + " jornada: " + jornada + " programa: " + programa);
            }

        }

    }

    public void crearOfertaEducativPrimerSemestre2() {
        //esta función tiene como insumos la malla curricular general (Base de datos.xlsx)
        //y el archivo con los números de metas educativas, llamado en este momento proyeccionprimersemestre.xlsx

        int filaEncabezados = 0;

        int filaInicialProgramasArchivoMetas = 1;

        //debe estar en la hoja 1
        XSSFSheet hojaMetas = libroProyeccionPrimerSemestre.getSheetAt(0);

        int columnaPrograma = obtenerCeldaEncabezado(hojaMetas, filaEncabezados, "programa").getColumnIndex();
        int columnaGrupo = obtenerCeldaEncabezado(hojaMetas, filaEncabezados, "grupo").getColumnIndex();
        int columnaJornada = obtenerCeldaEncabezado(hojaMetas, filaEncabezados, "jornada").getColumnIndex();
        int columnaCupo = obtenerCeldaEncabezado(hojaMetas, filaEncabezados, "meta").getColumnIndex();

        int i = 0;
        while (!celdaVacia(hojaMetas.getRow(filaInicialProgramasArchivoMetas + i), 0)) {

            XSSFRow fila = hojaMetas.getRow(filaInicialProgramasArchivoMetas + i);
            String programa = fila.getCell(columnaPrograma).getStringCellValue().trim();
            if (programa.equals("CSOD")) {
                System.out.println("");
            }
            String grupo = fila.getCell(columnaGrupo).getStringCellValue().trim();
            String jornada = fila.getCell(columnaJornada).getStringCellValue().trim();
            int cupo = (int) fila.getCell(columnaCupo).getNumericCellValue();

            //por cada grupo creado acá, cuyo cupo sea diferente de cero, se debe crear
            //la proyección de materias, tomando como insumo la malla curricular base. recoredar que acá todos son de primer semestre
            for (int k = 0; k < listadoAsignturas.size(); k++) {
                if (listadoSemestres.get(k) == 1 && idsCarreras.get(k) == idCarrera(programa)) {
                    Object[] elementosAsignaturas = new Object[14];

                    elementosAsignaturas[0] = listadoAlfa.get(k);//"Alfa";
                    elementosAsignaturas[1] = listadoNumerico.get(k);//"Numerico";
                    elementosAsignaturas[2] = listadoAsignturas.get(k);//"Asignatura";
                    elementosAsignaturas[3] = listadoCreditos.get(k) * 2;//"Horas";
                    elementosAsignaturas[4] = listadoCreditos.get(k);//"Créditos";
                    elementosAsignaturas[5] = listadoSemestres.get(k);//"Semestre";
                    elementosAsignaturas[6] = grupo;//"Grupo";
                    elementosAsignaturas[7] = programa;//"Programa";
                    elementosAsignaturas[8] = jornada;//"Jornada";
                    elementosAsignaturas[9] = cupo;//"Matricula";

                    elementosAsignaturas[10] = listadoNumeroSesiones.get(k);
                    elementosAsignaturas[11] = listadoDuracionSesiones.get(k);
                    elementosAsignaturas[12] = listadoPeriodicidad.get(k);
                    elementosAsignaturas[13] = listadoCupoMaximo.get(k);

                    //si la asginatura es virtual, al nombre de la misma se le va a colocar - Virtual
                    if (esVirtual((String) elementosAsignaturas[7], (String) elementosAsignaturas[0], (String) elementosAsignaturas[1])) {
                        elementosAsignaturas[2] = (String) elementosAsignaturas[2] + " - Virtual";
                    }

                    listadoDesglosadoOfertaEducativaTotal.add(elementosAsignaturas);

                }
            }

            System.out.println("Primer semestre " + " cupos: " + cupo + " jornada: " + jornada + " programa: " + programa);

            i++;
        }

    }

    public String jornadaDeMetasANormal(String jornadaArchivoMetas) {
        String j = "";

        if (jornadaArchivoMetas.equals("NOCHE")) {
            j = "M y J";//las jornadas en la noche son M y J o Mi y Vi, pero como en el archivo de metas
            //no se especifica, simplemente se pone M y J y luego se define manual
        }
        if (jornadaArchivoMetas.equals("SÁBADO MAÑANA")) {
            j = "SÁBADO D";
        }
        if (jornadaArchivoMetas.equals("SÁBADO TARDE")) {
            j = "SÁBADO T";
        }

        return j;
    }

    /**
     * Arreglo que devuelve en el componente 0 el número de grupos, y en el
     * componente 1 la matrícula para cada grupo, esto acorde con la estructira
     * del primer archivo que enviaron de metas
     *
     * @param gruposCupo
     * @return
     */
    public int[] numeroGruposCupo(String gruposCupo) {
        int[] ngc = new int[2];
        ngc[0] = 0;
        ngc[1] = 0;

        //la estructura del gruposCupo es una cadena que viene así A (B)
        //donde A es el número de grupo, los parentesis van en el archivo que se pasó
        //y b representa el cupo por grupo
        if (!gruposCupo.trim().equals("")) {
            int indiceParentesisApertura = gruposCupo.indexOf("(");
            String ng = gruposCupo.substring(0, indiceParentesisApertura - 1);
            int ngn = Integer.parseInt(ng.replace(" ", "").trim());

            int indiceParentesisCierre = gruposCupo.indexOf(")");
            String c = gruposCupo.substring(indiceParentesisApertura + 1, indiceParentesisCierre);
            int cn = Integer.parseInt(c);

            ngc[0] = ngn;
            ngc[1] = cn;
        }

        return ngc;
    }

    /**
     * Función que entrega una letra del abcdario según el número que se dé para
     * 0 entrega A, para 1 entrega B, etc
     *
     * @param indice
     * @return
     */
    public String devolverLetraSegunIndice(int indice) {
        String l = "";

        if (indice == 0) {
            l = "A";
        }
        if (indice == 1) {
            l = "B";
        }
        if (indice == 2) {
            l = "C";
        }
        if (indice == 3) {
            l = "D";
        }
        if (indice == 4) {
            l = "E";
        }
        if (indice == 5) {
            l = "F";
        }
        if (indice == 6) {
            l = "G";
        }
        if (indice == 7) {
            l = "H";
        }
        if (indice == 8) {
            l = "I";
        }
        if (indice == 9) {
            l = "J";
        }
        if (indice == 10) {
            l = "K";
        }

        return l;
    }

    /**
     * ESta función devuelve la matricula que tiene el grupo para el sigueinte
     * semestre. Toma como insumo los cupos que se presentan en el archivo de
     * seguimientos dado que en este archivo de seguimientos el usuario puede
     * modificar los grupos y sus cupos
     *
     * @param grupoProyectado
     * @return
     */
    public int matriculaActualGrupo(String nomenclatura, String grupoSinProyectar) {
        grupoSinProyectar = grupoSinProyectar.toLowerCase();
        int ma = 0;

//        for (int i = 0; i < pProgramas.size(); i++) {
//            if (pProgramas.get(i).equals(nomenclatura) && pSemestres.get(i).equals(grupoSinProyectar)) {
//                ma = pCupos.get(i);
//            }
//        }
        int contadorColumnas = 0;

        XSSFSheet hoja = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL);
        while (!celdaVacia(hoja.getRow(this.filaNombresGrupos), columnaInicianGrupos + contadorColumnas)) {
            String grupo = hoja.getRow(this.filaNombresGrupos).getCell(columnaInicianGrupos + contadorColumnas).getStringCellValue().toLowerCase();
            if (grupo.trim().equals(grupoSinProyectar.trim())) {
                if (!celdaVacia(hoja.getRow(this.filaInscritosEstimados), columnaInicianGrupos + contadorColumnas)) {
                    ma = (int) hoja.getRow(this.filaInscritosEstimados).getCell(columnaInicianGrupos + contadorColumnas).getNumericCellValue();
                } else {
                    System.out.println("Celda de cupos vacía");
                    JOptionPane.showMessageDialog(null, "Error. El programa no se ejecutará correctamente. Celda de inscritos vacia en archivo de seguimientos, hoja " + nomenclatura + ", columna" + (columnaInicianGrupos + contadorColumnas + 1) + ". Corrija el error y vuelva a ejecutar");
                }

            }
            contadorColumnas++;
        }

        return ma;
    }

    /**
     * ESta función devuelve la matricula que tiene el grupo para el sigueinte
     * semestre. Toma como insumo el archivo de proyección creado en la clase
     * ProyeccionDeCupos.java En este archivo pryectado, se crea el grupo para
     * el siguiente semestre, por lo cual se tomará el grupo que se proyecta
     * para el semestre siguiente
     *
     * @param grupoProyectado
     * @return
     */
    public int matriculaActualGrupoSeguimientos(String nomenclatura, String grupoActual) {
        int ma = 0;

        for (int i = 0; i < pProgramas.size(); i++) {
            if (pProgramas.get(i).equals(nomenclatura) && pSemestres.get(i).equals(grupoActual)) {
                ma = pCupos.get(i);
            }
        }

        return ma;
    }

    public ArrayList<String> gruposPrograma(String nomenclatura) {
        ArrayList<String> gp = new ArrayList<String>();

        XSSFSheet hojaSeguimientoPrograma = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL);
        XSSFRow filaNombresGrupos = null;

        try {
            filaNombresGrupos = hojaSeguimientoPrograma.getRow(this.filaNombresGrupos);
        } catch (Exception e) {
            Validaciones.mostrarErroresTotal("Error en "
                    + nomenclatura + " " + PERIODOACTUAL + " al crear los grupos por programa", e);
        }

        int contadorColumnas = 1;
        XSSFCell celdaNombreGrupo = filaNombresGrupos.getCell(columnaSemestresSeguimientos + contadorColumnas);

        while (filaNombresGrupos.getCell(columnaSemestresSeguimientos + contadorColumnas) != null) {
            celdaNombreGrupo = filaNombresGrupos.getCell(columnaSemestresSeguimientos + contadorColumnas);

            if (!celdaNombreGrupo.getStringCellValue().trim().equals("")) {
                //si no está vacio es porque hay un grupo
                String grupo = celdaNombreGrupo.getStringCellValue().trim();
                gp.add(grupo);
                contadorColumnas++;
            } else {
                break;
            }
        }

        return gp;
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

    /**
     * ASigna en la variable listadoOfertaEducativaTotal la oferta de cada
     * grupo. Cada elemento de esa lista es un arraylist con los elementos del
     * grupo, siendo los elementos su jornada, alfanumericos de las materias,
     * entre otros.
     *
     */
    public void crearOfertaEducativaTotal() {

        for (int i = 0; i < nomenclaturaCarreras.size(); i++) {

            if (hojaExiste(libroSeguimientos, nomenclaturaCarreras.get(i) + " " + PERIODOACTUAL)) {
                ArrayList<String> grupos = new ArrayList<String>(gruposPrograma(nomenclaturaCarreras.get(i)));

                for (int j = 0; j < grupos.size(); j++) {
                    ArrayList<Object[]> oferta = crearOfertaGrupo2(nomenclaturaCarreras.get(i), grupos.get(j));

                    listadoOfertaEducativaTotal.add(oferta);
                }

            } else {
                String mensajeInexistenciaHoja = "La hoja llamada " + nomenclaturaCarreras.get(i) + " " + PERIODOACTUAL + " no existe en el libro de seguimientos";
                MENSAJE_ERROR_INEXISTENCIA_HOJA_SEGUIMIENTOS = MENSAJE_ERROR_INEXISTENCIA_HOJA_SEGUIMIENTOS + " " + mensajeInexistenciaHoja;
                System.out.println(mensajeInexistenciaHoja);
            }

        }
        System.out.println("Se terminó de crear la oferta educativa total con un total de " + listadoOfertaEducativaTotal.size() + " elementos");
    }

    public void crearArchivoExcelOfertaEducativaTotal() {

        try {
            //rutaArchivoOfertaEducativaTotal = "C:\\Users\\Rodanmuro\\Desktop\\ofertaeducativatotal.xlsx";
            File archivoExcel = new File(rutaArchivoOfertaEducativaTotal);
            FileOutputStream fos = new FileOutputStream(archivoExcel);
            libroExcelOfertaEducativaTotal = new XSSFWorkbook();

            if (hojaExiste(libroExcelOfertaEducativaTotal, "Oferta educativa")) {
                int indice = libroExcelOfertaEducativaTotal.getSheetIndex("Oferta educativa");
                libroExcelOfertaEducativaTotal.removeSheetAt(indice);
            }

            XSSFSheet hoja = libroExcelOfertaEducativaTotal.createSheet("Oferta educativa");

            int contadorFilas = 0;

            //creamos los encabezados
            XSSFRow filaEncabezados = hoja.createRow(1);
            XSSFCell celdaEncabezadoPrograma = filaEncabezados.createCell(0);
            celdaEncabezadoPrograma.setCellValue("Programa");

            XSSFCell celdaEncabezadoSemestre = filaEncabezados.createCell(1);
            celdaEncabezadoSemestre.setCellValue("Semestre");

            XSSFCell celdaEncabezadoGrupo = filaEncabezados.createCell(2);
            celdaEncabezadoGrupo.setCellValue("Momento");

            XSSFCell celdaEncabezadoPensum = filaEncabezados.createCell(3);
            celdaEncabezadoPensum.setCellValue("Pensum");

            XSSFCell celdaEncabezadoJornada = filaEncabezados.createCell(4);
            celdaEncabezadoJornada.setCellValue("Jornada");

            XSSFCell celdaEncabezadoAlfa = filaEncabezados.createCell(5);
            celdaEncabezadoAlfa.setCellValue("Alfa");

            XSSFCell celdaEncabezadoNumerico = filaEncabezados.createCell(6);
            celdaEncabezadoNumerico.setCellValue("Numerico");

            XSSFCell celdaEncabezadoCreditos = filaEncabezados.createCell(7);
            celdaEncabezadoCreditos.setCellValue("Créditos");

            XSSFCell celdaEncabezadoAsignatura = filaEncabezados.createCell(8);
            celdaEncabezadoAsignatura.setCellValue("Asignatura");

            XSSFCell celdaEncabezadoNRC = filaEncabezados.createCell(9);
            celdaEncabezadoNRC.setCellValue("NRC");

            XSSFCell celdaEncabezadoCupo = filaEncabezados.createCell(10);
            celdaEncabezadoCupo.setCellValue("Cupo");

            XSSFCell celdaEncabezadoDocente = filaEncabezados.createCell(11);
            celdaEncabezadoDocente.setCellValue("Docente");

            XSSFCell celdaEncabezadoVirtual = filaEncabezados.createCell(12);
            celdaEncabezadoVirtual.setCellValue("Virtual");

            XSSFCell celdaEncabezadoIdCruce = filaEncabezados.createCell(13);
            celdaEncabezadoIdCruce.setCellValue("IDCruceComp");

            XSSFCell celdaEncabezadoNumeroSesiones = filaEncabezados.createCell(14);
            celdaEncabezadoNumeroSesiones.setCellValue("NumeroSesiones");

            XSSFCell celdaEncabezadoDuracionIdealSesiones = filaEncabezados.createCell(15);
            celdaEncabezadoDuracionIdealSesiones.setCellValue("DuracionSesion");

            XSSFCell celdaEncabezadoPeriodicidad = filaEncabezados.createCell(16);
            celdaEncabezadoPeriodicidad.setCellValue("Periodicidad");

            XSSFCell celdaEncabezadoCupoMaximo = filaEncabezados.createCell(17);
            celdaEncabezadoCupoMaximo.setCellValue("CupoMaximo");
            
            XSSFCell celdaEncabezadoSalon = filaEncabezados.createCell(18);
            celdaEncabezadoSalon.setCellValue("Salón");

            int conteoGrupos = 0;

            //por cada grupo, imprimimos la oferta educativa
            System.out.println("El listado desglosado de la oferta educativa total tiene un tamaño de " + listadoDesglosadoOfertaEducativaTotal.size() + " elementos");
            for (int j = 0; j < listadoDesglosadoOfertaEducativaTotal.size(); j++) {
                //por el grupo tomamos la asignatura y sus datos
                Object[] elementosAsignatura = new Object[14];
                elementosAsignatura = listadoDesglosadoOfertaEducativaTotal.get(j);

                String alfa = (String) elementosAsignatura[0];
                String numerico = (String) "" + elementosAsignatura[1];
                String asignatura = (String) elementosAsignatura[2];
                int horas = (int) elementosAsignatura[3];
                String grupoProyectado = (String) elementosAsignatura[6];
                String nom = (String) elementosAsignatura[7];
                int matricula = (int) elementosAsignatura[9];
                String jornada = (String) elementosAsignatura[8];
                int creditos = (int) elementosAsignatura[4];

                int numeroSesiones = (int) elementosAsignatura[10];
                int duracionSesiones = (int) elementosAsignatura[11];
                int periodicidad = (int) elementosAsignatura[12];
                int cupoMaximo = (int) elementosAsignatura[13];

                //creamos el estilo con el cual será pintado cada celda
                int r = listadoR.get(conteoGrupos);
                int g = listadoG.get(conteoGrupos);
                int b = listadoB.get(conteoGrupos);

                byte[] rgb = new byte[3];
                rgb[0] = (byte) r;
                rgb[1] = (byte) g;
                rgb[2] = (byte) b;

                XSSFColor myColor = new XSSFColor();
                myColor.setRGB(rgb);

                //aca agregamos el color
                XSSFCellStyle cs = libroExcelOfertaEducativaTotal.createCellStyle();

                cs.setFillForegroundColor(myColor);
                cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                //aca agregamos los bordes
                cs.setBorderTop(BorderStyle.THIN);
                cs.setTopBorderColor(IndexedColors.BLACK.getIndex());

                cs.setBorderBottom(BorderStyle.THIN);
                cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());

                cs.setBorderLeft(BorderStyle.THIN);
                cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());

                cs.setBorderRight(BorderStyle.THIN);
                cs.setRightBorderColor(IndexedColors.BLACK.getIndex());

                XSSFRow fila = hoja.createRow(contadorFilas + 2);
                XSSFCell celdaPrograma = fila.createCell(0);//nomenclatura
                celdaPrograma.setCellValue(nom);
                celdaPrograma.setCellStyle(cs);

                XSSFCell celdaSemestre = fila.createCell(1);//grupo proyectado
                celdaSemestre.setCellValue(grupoProyectado);
                celdaSemestre.setCellStyle(cs);

                XSSFCell celdaJornada = fila.createCell(4);
                celdaJornada.setCellValue(jornada);
                celdaJornada.setCellStyle(cs);

                XSSFCell celdaAlfa = fila.createCell(5);
                celdaAlfa.setCellValue(alfa);
                celdaAlfa.setCellStyle(cs);

                XSSFCell celdaNumerico = fila.createCell(6);
                celdaNumerico.setCellValue(numerico);
                celdaNumerico.setCellStyle(cs);

                XSSFCell celdaCreditos = fila.createCell(7);
                celdaCreditos.setCellValue(creditos);
                celdaCreditos.setCellStyle(cs);

                XSSFCell celdaAsignatura = fila.createCell(8);
                celdaAsignatura.setCellValue(asignatura);
                celdaAsignatura.setCellStyle(cs);

                XSSFCell celdaMatriculaActual = fila.createCell(10);
                celdaMatriculaActual.setCellValue(matricula);
                celdaMatriculaActual.setCellStyle(cs);

                XSSFCell celdaVirtual = fila.createCell(12);
                if (esVirtual((String) elementosAsignatura[7], alfa, numerico)) {
                    celdaVirtual.setCellValue("Si");
                } else {
                    celdaVirtual.setCellValue("No");
                }
                celdaVirtual.setCellStyle(cs);

                //se van a crear las celdas grupo, pensum nrc y docente
                XSSFCell celdaGrupo = fila.createCell(2);
                celdaGrupo.setCellStyle(cs);

                XSSFCell celdaPensum = fila.createCell(3);
                celdaPensum.setCellStyle(cs);

                XSSFCell celdaNRC = fila.createCell(9);
                celdaNRC.setCellStyle(cs);

                XSSFCell celdaDocente = fila.createCell(11);
                celdaDocente.setCellStyle(cs);

                XSSFCell celdaIDCruce = fila.createCell(13);
                celdaIDCruce.setCellStyle(cs);

                //estas celdas tienen contenido que se saca
                //de la malla curricular base
                XSSFCell celdaNumeroSesiones = fila.createCell(14);
                celdaNumeroSesiones.setCellValue(numeroSesiones);
                celdaNumeroSesiones.setCellStyle(cs);

                XSSFCell celdaDuracionSesiones = fila.createCell(15);
                celdaDuracionSesiones.setCellValue(duracionSesiones);
                celdaDuracionSesiones.setCellStyle(cs);

                XSSFCell celdaPeriodicidad = fila.createCell(16);
                celdaPeriodicidad.setCellValue(periodicidad);
                celdaPeriodicidad.setCellStyle(cs);

                XSSFCell celdaCupoMaximo = fila.createCell(17);
                celdaCupoMaximo.setCellValue(cupoMaximo);
                celdaCupoMaximo.setCellStyle(cs);
                
                XSSFCell celdaSalon = fila.createCell(18);
                celdaSalon.setCellStyle(cs);
                
                contadorFilas++;

                if (j != (listadoDesglosadoOfertaEducativaTotal.size() - 1)) {

                    String nomp = (String) listadoDesglosadoOfertaEducativaTotal.get(j + 1)[7];
                    String grupoProyectadop = (String) listadoDesglosadoOfertaEducativaTotal.get(j + 1)[6];

                    if (!nom.equals(nomp) || !grupoProyectado.equals(grupoProyectadop)) {
                        conteoGrupos++;
                    }
                }

            }

            libroExcelOfertaEducativaTotal.write(fos);
            //libroExcelOfertaEducativaTotal.close();

            System.out.println("Se ha creado el archivo de excel con la oferta educativa total");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    public void crearHojaExcelRecomendacionesFusionGrupos() {
        try {
            File archivoExcel = new File(rutaArchivoOfertaEducativaTotal);
            FileOutputStream fos = new FileOutputStream(archivoExcel);

            if (hojaExiste(libroExcelOfertaEducativaTotal, "Recomendaciones fusión")) {
                int indice = libroExcelOfertaEducativaTotal.getSheetIndex("Recomendaciones fusión");
                libroExcelOfertaEducativaTotal.removeSheetAt(indice);
            }

            XSSFSheet hoja = libroExcelOfertaEducativaTotal.createSheet("Recomendaciones fusión");

            int contadorFilas = 0;

            //creamos los encabezados
            XSSFRow filaEncabezados = hoja.createRow(0);
            XSSFCell celdaEncabezadoPrograma = filaEncabezados.createCell(0);
            celdaEncabezadoPrograma.setCellValue("Ejecutar");

            XSSFCell celdaEncabezadoSemestre = filaEncabezados.createCell(1);
            celdaEncabezadoSemestre.setCellValue("Razón");

            XSSFCell celdaEncabezadoTotal = filaEncabezados.createCell(2);
            celdaEncabezadoTotal.setCellValue("Total");

            XSSFCell celdaEncabezadoJornada = filaEncabezados.createCell(3);
            celdaEncabezadoJornada.setCellValue("Programa");

            XSSFCell celdaEncabezadoAlfa = filaEncabezados.createCell(4);
            celdaEncabezadoAlfa.setCellValue("Alfa");

            XSSFCell celdaEncabezadoNumerico = filaEncabezados.createCell(5);
            celdaEncabezadoNumerico.setCellValue("Numerico");

            XSSFCell celdaEncabezadoCreditos = filaEncabezados.createCell(6);
            celdaEncabezadoCreditos.setCellValue("Asignatura");

            XSSFCell celdaEncabezadoAsignatura = filaEncabezados.createCell(7);
            celdaEncabezadoAsignatura.setCellValue("Créditos");

            XSSFCell celdaEncabezadoCupo = filaEncabezados.createCell(8);
            celdaEncabezadoCupo.setCellValue("Grupo");

            XSSFCell celdaEncabezadoVirtual = filaEncabezados.createCell(9);
            celdaEncabezadoVirtual.setCellValue("Mat");

            XSSFCell celdaEncabezadojornada = filaEncabezados.createCell(10);
            celdaEncabezadojornada.setCellValue("Jornada");

            //////////////Con
            XSSFCell celdaEncabezadoCon = filaEncabezados.createCell(11);
            celdaEncabezadoCon.setCellValue("Con");

            XSSFCell celdaEncabezadoJornada2 = filaEncabezados.createCell(12);
            celdaEncabezadoJornada2.setCellValue("Programa");

            XSSFCell celdaEncabezadoAlfa2 = filaEncabezados.createCell(13);
            celdaEncabezadoAlfa2.setCellValue("Alfa");

            XSSFCell celdaEncabezadoNumerico2 = filaEncabezados.createCell(14);
            celdaEncabezadoNumerico2.setCellValue("Numerico");

            XSSFCell celdaEncabezadoCreditos2 = filaEncabezados.createCell(15);
            celdaEncabezadoCreditos2.setCellValue("Asignatura");

            XSSFCell celdaEncabezadoAsignatura2 = filaEncabezados.createCell(16);
            celdaEncabezadoAsignatura2.setCellValue("Créditos");

            XSSFCell celdaEncabezadoCupo2 = filaEncabezados.createCell(17);
            celdaEncabezadoCupo2.setCellValue("Grupo");

            XSSFCell celdaEncabezadoVirtual2 = filaEncabezados.createCell(18);
            celdaEncabezadoVirtual2.setCellValue("Mat");

            XSSFCell celdaEncabezadoJornada3 = filaEncabezados.createCell(19);
            celdaEncabezadoJornada3.setCellValue("Jornada");

            //el encabezado Ejecutar hace referencia a si o no, la fusión se ejecutará
            //el encabezado razón tomará los valores Cruzada, porque tienen el mismo contenido
            //en términos del programa, el mismo nombre de la asignatura
            //el otro valor es Compartido, en este caso todo es igual
//            elementosAsignaturas[0] = "Alfa";
//        elementosAsignaturas[1] = "Numerico";
//        elementosAsignaturas[2] = "Asignatura";
//        elementosAsignaturas[3] = "Horas";
//        elementosAsignaturas[4] = "Créditos";
//        elementosAsignaturas[5] = "Semestre";
//        elementosAsignaturas[6] = "Grupo";
//        elementosAsignaturas[7] = "Programa";
//        elementosAsignaturas[8] = "Jornada";
//        elementosAsignaturas[9] = "Matricula";
            for (int i = 0; i < listadoSugerenciasCombinarGrupos1.size(); i++) {
                XSSFRow fila = hoja.createRow(i + 1);
                fila.createCell(0).setCellValue("si");

                String razon = "";
                //si los alfa numericos son iguales, la razon es compartida
                String alfa = (String) listadoSugerenciasCombinarGrupos1.get(i)[0];
                String numerico = (String) "" + listadoSugerenciasCombinarGrupos1.get(i)[1];

                String alfa2 = (String) listadoSugerenciasCombinarGrupos2.get(i)[0];
                String numerico2 = (String) "" + listadoSugerenciasCombinarGrupos2.get(i)[1];

                if (alfa.equals(alfa2) && numerico.equals(numerico2)) {
                    razon = "Compartida";
                } //si los nombres son iguales, pero los alfa numericos son diferentes, la razon es lista cruzada
                else {
                    razon = "Cruzada";
                }

                //escribimos la razón
                fila.createCell(1).setCellValue(razon);

                //el total de cupoas
                int suma = (int) listadoSugerenciasCombinarGrupos1.get(i)[9] + (int) listadoSugerenciasCombinarGrupos2.get(i)[9];
                fila.createCell(2).setCellValue(suma);

                fila.createCell(3).setCellValue((String) listadoSugerenciasCombinarGrupos1.get(i)[7]);
                fila.createCell(4).setCellValue(alfa);
                fila.createCell(5).setCellValue(numerico);
                fila.createCell(6).setCellValue((String) listadoSugerenciasCombinarGrupos1.get(i)[2]);
                fila.createCell(7).setCellValue((int) listadoSugerenciasCombinarGrupos1.get(i)[4]);
                fila.createCell(8).setCellValue((String) listadoSugerenciasCombinarGrupos1.get(i)[6]);
                fila.createCell(9).setCellValue((int) listadoSugerenciasCombinarGrupos1.get(i)[9]);
                fila.createCell(10).setCellValue((String) listadoSugerenciasCombinarGrupos1.get(i)[8]);

                //con
                XSSFCell celdaCon = fila.createCell(11);
                celdaCon.setCellValue("Con");
                //Ojo, acá se genera el código para la realización de colores
                //rellenar celda con un color de fondo
                XSSFCellStyle cs = libroExcelOfertaEducativaTotal.createCellStyle();
                cs.setFillForegroundColor(new XSSFColor(new java.awt.Color(111, 212, 232)));
                cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                celdaCon.setCellStyle(cs);

                fila.createCell(12).setCellValue((String) listadoSugerenciasCombinarGrupos2.get(i)[7]);
                fila.createCell(13).setCellValue(alfa2);
                fila.createCell(14).setCellValue(numerico2);
                fila.createCell(15).setCellValue((String) listadoSugerenciasCombinarGrupos2.get(i)[2]);
                fila.createCell(16).setCellValue((int) listadoSugerenciasCombinarGrupos2.get(i)[4]);
                fila.createCell(17).setCellValue((String) listadoSugerenciasCombinarGrupos2.get(i)[6]);
                fila.createCell(18).setCellValue((int) listadoSugerenciasCombinarGrupos2.get(i)[9]);
                fila.createCell(19).setCellValue((String) listadoSugerenciasCombinarGrupos2.get(i)[8]);

                //se agregará como NRC para compartidas el 1000000, para cruzadas el 2000000
                //se debe llevar un conteo de dichos valores
            }

            libroExcelOfertaEducativaTotal.write(fos);
//            libroExcelOfertaEducativaTotal.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int retornarCupoMaximoAsignatura(String alfa, String numerico, String nomenclaturaCarrera) {
        //mismo alfanumerico, mismo cupo
        int c = 0;

        for (int i = 0; i < listadoAsignturas.size(); i++) {
            String a = listadoAlfa.get(i);
            String n = listadoNumerico.get(i);
            int id = idsCarreras.get(i);
            int idCarrera = idCarrera(nomenclaturaCarrera);

            if (alfa.equals(a) && numerico.equals(n) && idCarrera == id) {
                c = listadoCupoMaximo.get(i);
            }
        }

        return c;
    }

    public void crearListadoSugerenciasFusionGrupos() {
        ArrayList<Object[]> l = new ArrayList<Object[]>(listadoDesglosadoOfertaEducativaTotal);

        for (int i = 0; i < l.size(); i++) {
            String alfa = (String) l.get(i)[0];
            String numerico = (String) "" + l.get(i)[1];
            String jornada = (String) l.get(i)[8];
            int matricula = (int) l.get(i)[9];
            String programa = (String) l.get(i)[7];
            String asignatura = (String) l.get(i)[2];

            for (int j = i + 1; j < l.size(); j++) {
                String alfaj = (String) l.get(j)[0];
                String numericoj = (String) "" + l.get(j)[1];
                String jornadaj = (String) l.get(j)[8];
                int matriculaj = (int) l.get(j)[9];
                String programaj = (String) l.get(j)[7];
                String asignaturaj = (String) l.get(j)[2];

                //las condiciones son, si los alfa numericos son iguales, si están en la misma jornada
                //y si los dos grupos suman 40 o menos
                //solo se genera recomenación de fusión si la asignatura no es virtual
                if (!esVirtual(programa, alfa, numerico)) {
                    if ((alfa.equals(alfaj)
                            && numerico.equals(numericoj)
                            && jornada.equals(jornadaj)
                            && (matricula + matriculaj) <= retornarCupoMaximoAsignatura(alfa, numerico, programaj) /*cantidadMaximaEstudiantes*/)
                            || ((asignatura.equals(asignaturaj))
                            && jornada.equals(jornadaj)
                            && (matricula + matriculaj) <= retornarCupoMaximoAsignatura(alfa, numerico, programaj))/*cantidadMaximaEstudiantes)*/) {

                        if (asignatura.equals("Electiva CPC") || asignatura.equals("Electiva CP")) {
                            if (alfa.equals(alfaj) && numerico == numericoj) {
                                listadoSugerenciasCombinarGrupos1.add(l.get(i));
                                listadoSugerenciasCombinarGrupos2.add(l.get(j));
                            }
                        } else {
                            listadoSugerenciasCombinarGrupos1.add(l.get(i));
                            listadoSugerenciasCombinarGrupos2.add(l.get(j));
                        }

                    }
                }

            }

        }
    }

    public String imprimirElementosAsignatura(ArrayList<Object[]> l, int i, String[] nombresParametros) {

        String cadena = "";

//        for (int i = 0; i < l.size(); i++) {
        Object[] elemetos = l.get(i);

//            cadena = "";
        for (int j = 0; j < nombresParametros.length; j++) {
            cadena = cadena + " " + nombresParametros[j] + ": " + l.get(i)[j];
        }

//        }
        return cadena;

    }

    /**
     * Esta función toma las nombres de las asignaturas desde el archivo de
     * bases de datos los busca por alfa numérico en cada una de las hojas de
     * seguimientos, y les coloca el mismo nombre de la base de datos
     */
    public void normalizacionNombresAsignaturas() {
        int numeroHojas = libroSeguimientos.getNumberOfSheets();

        for (int i = 0; i < numeroHojas; i++) {
            XSSFSheet hoja = libroSeguimientos.getSheetAt(i);
            String nombreHoja = hoja.getSheetName();

            int contadorFilas = 3;
            int columnaAlfa = 0;
            int columnaNumerico = 1;
            int columnaAsignaturaCorta = 2;
            int columnaAsignaturaLarga = 3;

            while (hoja.getRow(contadorFilas) != null && hoja.getRow(contadorFilas).getCell(columnaAlfa) != null && !hoja.getRow(contadorFilas).getCell(columnaAlfa).getStringCellValue().trim().equals("")) {
                //obtenemos el alfa numerico de la hoja de seguimientos
                String alfa = hoja.getRow(contadorFilas).getCell(columnaAlfa).getStringCellValue().trim();
                String numerico = "";
                try {
                    numerico = (String) retornarValor(hoja.getRow(contadorFilas).getCell(columnaNumerico));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //ahora los buscamos en el listado de la base de datos
                for (int j = 0; j < listadoAsignturas.size(); j++) {
                    if (alfa.equals(listadoAlfa.get(j).trim()) && numerico.equals(listadoNumerico.get(j))) {
                        //pra dicho alfa numerico tomamos el nombre de la asignatura de la base de datos
                        String asignatura = listadoAsignturas.get(j).trim();

                        //y colocamos dicho nombre en la hoja de seguimientos
                        System.out.println("Asigntura: " + asignatura + " nombre de la hoja: " + nombreHoja);
                        hoja.getRow(contadorFilas).getCell(columnaAsignaturaCorta).setCellValue(asignatura);
                        hoja.getRow(contadorFilas).getCell(columnaAsignaturaLarga).setCellValue(asignatura);
                    }
                }

                contadorFilas++;

            }

        }

        //cuando se han escrito todas las materias es necesario escribir en ela rchivo y guardar
        try {
            FileOutputStream fos = new FileOutputStream(rutaLibroSeguimientos);
            libroSeguimientos.write(fos);
            libroSeguimientos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Esta función toma las creditos de las asignaturas desde el archivo de
     * bases de datos los busca por alfa numérico en cada una de las hojas de
     * seguimientos, y les coloca el mismo credito de la base de datos
     */
    public void normalizacionCreditos() {
        int numeroHojas = libroSeguimientos.getNumberOfSheets();

        for (int i = 0; i < numeroHojas; i++) {
            XSSFSheet hoja = libroSeguimientos.getSheetAt(i);
            String nombreHoja = hoja.getSheetName();

            int contadorFilas = 3;
            int columnaAlfa = 0;
            int columnaNumerico = 1;
            int columnaAsignaturaCorta = 2;
            int columnaAsignaturaLarga = 3;
            int columnaCreditos = 5;

            while (!celdaVacia(hoja.getRow(contadorFilas), 0)) {
                //obtenemos el alfa numerico de la hoja de seguimientos
                String alfa = hoja.getRow(contadorFilas).getCell(columnaAlfa).getStringCellValue().trim();
                String numerico = "";
                try {
                    numerico = (String) retornarValor(hoja.getRow(contadorFilas).getCell(columnaNumerico));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //ahora los buscamos en el listado de la base de datos
                for (int j = 0; j < listadoAsignturas.size(); j++) {
                    if (alfa.equals(listadoAlfa.get(j).trim()) && numerico.equals(listadoNumerico.get(j))) {
                        //pra dicho alfa numerico tomamos el nombre de la asignatura de la base de datos
                        int creditos = listadoCreditos.get(j);

                        //y colocamos dichos creditos nombre en la hoja de seguimientos
                        System.out.println("Creditos: " + creditos + " nombre de la hoja: " + nombreHoja);
                        hoja.getRow(contadorFilas).getCell(columnaCreditos).setCellValue(creditos);
                        hoja.getRow(contadorFilas).getCell(columnaCreditos).setCellValue(creditos);
                    }
                }

                contadorFilas++;

            }

        }

        //cuando se han escrito todas las materias es necesario escribir en ela rchivo y guardar
        try {
            FileOutputStream fos = new FileOutputStream(rutaLibroSeguimientos);
            libroSeguimientos.write(fos);
            libroSeguimientos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//la siguiente función devuelve el id de la carrera, si le entregamos la nomenclatura 
    //de la misma
    public int idCarrera(String nomenclatura) {
        int id = 0;

        int indiceNomenclatura = nomenclaturaCarreras.indexOf(nomenclatura);
        try {
            id = listadoIdsCarreras.get(indiceNomenclatura);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;

    }

    /**
     * Dada una celda, en este caso de la columna dos del archivo de consolidado
     * se devuelvde Un arreglo con el índice 0 el valor en romano, y con el
     * índice 1, el grupo (A, B, C, D...)
     *
     * @param semestreRomanoGrupo
     * @return
     */
    public Object[] semestreGrupo(String semestreSemestreGrupo) {
        semestreSemestreGrupo = semestreSemestreGrupo.toLowerCase();
        Object[] semestreGrupo = new Object[2];
        String semestre = "";
        String grupo = "";

        if (semestreSemestreGrupo.trim().indexOf("g") == -1) {
            semestre = semestreSemestreGrupo.trim();
//            System.out.println("romano solo: " + romano);
        } else {
            semestre = semestreSemestreGrupo.trim().substring(0, semestreSemestreGrupo.indexOf("g"));
//            System.out.println("romano " + romano);

            grupo = semestreSemestreGrupo.trim().substring(semestreSemestreGrupo.indexOf("g") + 1);
//            System.out.println("grupo " + grupo);
        }

        semestreGrupo[0] = semestre;
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

    public String semestreEnteroRomano(int entero) {
        String semestre = "";

        if (entero == 1) {
            semestre = "I";
        }
        if (entero == 2) {
            semestre = "II";
        }
        if (entero == 3) {
            semestre = "III";
        }
        if (entero == 4) {
            semestre = "IV";
        }
        if (entero == 5) {
            semestre = "V";
        }
        if (entero == 6) {
            semestre = "VI";
        }
        if (entero == 7) {
            semestre = "VII";
        }
        if (entero == 8) {
            semestre = "VIII";
        }
        if (entero == 9) {
            semestre = "IX";
        }
        if (entero == 10) {
            semestre = "X";
        }
        if (entero == 11) {
            semestre = "XI";
        }

        return semestre;
    }

    public void cargarListadoColores() {

        int contadorFilas = 0;
        try {
            FileInputStream is = new FileInputStream(rutaLibroColores);
            libroColores = new XSSFWorkbook(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        XSSFSheet hojaColores = libroColores.getSheet("colores");

        while (hojaColores.getRow(contadorFilas) != null
                && hojaColores.getRow(contadorFilas).getCell(0) != null) {
            int r = (int) hojaColores.getRow(contadorFilas).getCell(1).getNumericCellValue();
            int g = (int) hojaColores.getRow(contadorFilas).getCell(2).getNumericCellValue();
            int b = (int) hojaColores.getRow(contadorFilas).getCell(3).getNumericCellValue();

            listadoR.add(r);
            listadoG.add(g);
            listadoB.add(b);

            contadorFilas++;
        }

        System.out.println("tamaño r: " + listadoR.size());
        System.out.println("Listado de colores cargado");
    }

    public boolean tienePrerequisito(String alfa, String numerico) {
        boolean p = false;

        for (int i = 0; i < listadopAlfa.size(); i++) {
            if (listadoAlfa.get(i).equals(alfa) && listadoNumerico.get(i).equals(numerico)) {
                if (!listadopAlfa.equals("")) {
                    p = true;
                }
            }
        }

        return p;
    }

    public Object[] alfaNumericoPrerequisito(String programa, String alfa, String numerico) {
        Object[] anpr = new Object[2];
        anpr[0] = "";
        anpr[1] = "";

        for (int i = 0; i < listadoAlfa.size(); i++) {
            if (idsCarreras.get(i) == idCarrera(programa) && listadoAlfa.get(i).equals(alfa) && listadoNumerico.get(i).equals(numerico)) {
                anpr[0] = listadopAlfa.get(i);
                anpr[1] = listadopNumerico.get(i);
            }
        }

        return anpr;
    }

    public boolean yaVioPrerequisito(String alfa, String numerico, int columnaGrupo, String nomenclatura) {
        //esta función depende completamente de los archivos de seguimiento
        //y de la estructura para acá establecida de dichos archivos

        if (alfa.equals("UVCE") && numerico.equals("UV023")) {
            //System.out.println("Presupuestos!!!");
        }

        boolean yv = false;

        int contadorFilas = 0;

        String alfap = (String) alfaNumericoPrerequisito(nomenclatura, alfa.trim(), numerico)[0];
        String numericop = "";
        try {
            numericop = (String) alfaNumericoPrerequisito(nomenclatura, alfa.trim(), numerico)[1];
        } catch (Exception e) {
            e.printStackTrace();
        }

        XSSFRow fila = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL).getRow(this.filaInicialAlfas + contadorFilas);

        while (!celdaVacia(fila, 0)) {

            if (fila.getCell(columnaAlfaSeguimientos).getStringCellValue().equals(alfap) && retornarValor(fila.getCell(columnaNumericoSeguimientos)).equals(numericop)) {

                //ojo la celda donde se coloca si la vio o no la vio, no puede ser numerica, debe llevar una x o palabras
                if (materiaVista(fila, columnaGrupo)) {
                    yv = true;

                    break;
                }

            }
            contadorFilas++;
            fila = libroSeguimientos.getSheet(nomenclatura + " " + PERIODOACTUAL).getRow(this.filaInicialAlfas + contadorFilas);
        }

        //si no tiene prerequisisto entonces alfap es vacio, por tanto se puede decir que ya vio el prerequisito
        if (alfap == null) {
            System.out.println("alfap " + alfap);
        }
        if (alfap.trim().equals("")) {
            yv = true;
        }

        return yv;
    }

    public static boolean celdaVacia(XSSFRow fila, int indiceCelda) {
        boolean cv = false;

        if (fila == null) {
            return true;
        }

        XSSFCell celda = fila.getCell(indiceCelda);

        if (celda == null) {
            cv = true;
        }
        if (celda != null) {
            int tipoCelda = celda.getCellType();
            if (tipoCelda == 0) {
                cv = false;
            }
            if (tipoCelda == 1) {
                if (celda.getStringCellValue().trim().equals("")) {
                    cv = true;
                } else {
                    cv = false;
                }
            }
            if (tipoCelda == 3) {
                cv = true;
            }
        }

        return cv;
    }

    public boolean materiaVista(XSSFRow fila, int indiceCelda) {
        boolean mv = false;
        if (fila == null) {
            return false;
        }

        if (celdaVacia(fila, indiceCelda)) {

            mv = false;
        } else {
            XSSFCell celda = fila.getCell(indiceCelda);
            String valorCelda = (String) retornarValor(celda);

            if (valorCelda.trim().equals("x") || valorCelda.trim().equals("X")) {
                mv = true;
            }
        }
        return mv;
    }

    public boolean materiaObligatoria(XSSFRow fila, int indiceCelda) {
        boolean mv = false;
        if (fila == null) {
            return false;
        }

        if (celdaVacia(fila, indiceCelda)) {

            mv = false;
        } else {
            XSSFCell celda = fila.getCell(indiceCelda);
            String valorCelda = (String) retornarValor(celda);

            if (valorCelda.trim().equals("O") || valorCelda.trim().equals("o")) {
                mv = true;
            }
        }
        return mv;
    }

    /**
     * Esta función cuenta las columnas totales del archivo de seguimeintos para
     * una hoja dada cuenta las columnas totales que presentan grupos I A, II A,
     * etcétera
     *
     * @return
     */
    public int conteoColumnasGruposArchivoSeguimientos(XSSFRow filaEncabezados, String grupo) {
        int c = 0;

//        while (filaEncabezados.getCell(c) != null) {
        while (!celdaVacia(filaEncabezados, columnaInicianGrupos + c)) {
            if (filaEncabezados.getCell(columnaInicianGrupos + c).getStringCellValue().trim().equals(grupo.trim())) {
                break;
            }
            c++;
        }
        return c + columnaInicianGrupos;
    }

    public void creacionHojaCuposVirtuales() {
        ArrayList<String> listadoAsignaturasVirtuales = new ArrayList<String>();
        ArrayList<Integer> listadoMatriculasVirtuales = new ArrayList<Integer>();

        try {

            if (!hojaExiste(libroExcelOfertaEducativaTotal, "Cupos Virtuales")) {
                libroExcelOfertaEducativaTotal.createSheet("Cupos virtuales");
            }

            XSSFSheet hojaCuposVirtuales = libroExcelOfertaEducativaTotal.getSheet("Cupos virtuales");
            int contadorFilasOrigen = 2;
            int contadorFilasDestino = 1;

            XSSFSheet hojaOfertaEducativa = libroExcelOfertaEducativaTotal.getSheet("Oferta educativa");
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

            File archivoExcel = new File(rutaArchivoOfertaEducativaTotal);
            FileOutputStream fos = new FileOutputStream(archivoExcel);
            libroExcelOfertaEducativaTotal.write(fos);
//            libroExcelOfertaEducativaTotal.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void crearHojaObservaciones() {
        try {

            if (!hojaExiste(libroExcelOfertaEducativaTotal, "Observaciones")) {
                libroExcelOfertaEducativaTotal.createSheet("Observaciones");
            }

            XSSFSheet hojaObservaciones = libroExcelOfertaEducativaTotal.getSheet("Observaciones");
            int contadorFilasOrigen = 2;
            int contadorFilasDestino = 1;

            for (int i = 0; i < listadoObervaciones.size(); i++) {
                hojaObservaciones.createRow(i).createCell(0).setCellValue(listadoObervaciones.get(i));
            }

            File archivoExcel = new File(rutaArchivoOfertaEducativaTotal);
            FileOutputStream fos = new FileOutputStream(archivoExcel);
            libroExcelOfertaEducativaTotal.write(fos);
            libroExcelOfertaEducativaTotal.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> cruceCompartidosSinPareja(String rutaArchivoFusionesEjecutadas) {
        ArrayList<String> listadoSinPareja = new ArrayList<String>();

        //esta función depende de la configuración actual del archivo de oferta educativa
        try {
            FileInputStream fis = new FileInputStream(rutaArchivoFusionesEjecutadas);
            XSSFWorkbook libro = new XSSFWorkbook(fis);

            XSSFSheet hoja = libro.getSheet("Oferta educativa");

            String cadena = "";
            int contadorFilas = 0;
            while (!celdaVacia(hoja.getRow(contadorFilas + 2), 0)) {

                if (!celdaVacia(hoja.getRow(contadorFilas + 2), 13)) {
                    String cruzcomp = hoja.getRow(contadorFilas + 2).getCell(13).getStringCellValue();
                    cadena = cruzcomp + " No tiene pareja "
                            + hoja.getRow(contadorFilas + 2).getCell(0).getStringCellValue()
                            + " "
                            + hoja.getRow(contadorFilas + 2).getCell(1).getStringCellValue()
                            + " "
                            + hoja.getRow(contadorFilas + 2).getCell(8).getStringCellValue();
                    listadoSinPareja.add(cadena);

                    int contadorFilas2 = 0;
                    while (!celdaVacia(hoja.getRow(contadorFilas2 + 2), 0)) {
                        if ((contadorFilas2 + 2) != (contadorFilas + 2) && cruzcomp.equals(hoja.getRow(contadorFilas2 + 2).getCell(13).getStringCellValue())) {
//                            cadena=cruzcomp+" Si tiene pareja";
//                            System.out.println(""+cadena);
                            listadoSinPareja.remove(listadoSinPareja.size() - 1);
                            break;
                        }
                        contadorFilas2++;
                    }
                }
                contadorFilas++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listadoSinPareja;
    }

    public static void ejecutarRecomendacionesFusion(String rutaArchivoFusion, JTextArea textArea) {
        //esta función depende de la configuración actual de los archivos de oferta educativa
        try {
            System.out.println("Se comienzan a ejecutar las recomendaciones de fusión");
            File archivoFusion = new File(rutaArchivoFusion);
            String nombreArchivo = archivoFusion.getName();
            String rutaAbsolutaDirectorio = archivoFusion.getParentFile().getAbsolutePath() + "\\";

            FileInputStream fis = new FileInputStream(rutaArchivoFusion);
            XSSFWorkbook libroFusiones = new XSSFWorkbook(fis);

            //acá en fila, cargamos la de la hoja de recomendaciones
            XSSFSheet hojaRecomendaciones = libroFusiones.getSheet("Recomendaciones fusión");
            XSSFRow fila = hojaRecomendaciones.getRow(1);
            int contadorFilas = 1;
            int contadorCruzadas = 1;
            int contadorCompartidas = 1;

            XSSFSheet hojaOferta = libroFusiones.getSheet("Oferta educativa");

            while (!celdaVacia(fila, 0)) {

                String ejecutar = fila.getCell(0).getStringCellValue();

                if (ejecutar.equals("si")) {
                    String compartidaCruzada = fila.getCell(1).getStringCellValue();

                    String programa1 = fila.getCell(3).getStringCellValue();
                    String alfa1 = fila.getCell(4).getStringCellValue();
                    String numerico1 = (String) "" + retornarValor(fila.getCell(5));
                    String grupo1 = fila.getCell(8).getStringCellValue();
                    String jornada1 = fila.getCell(10).getStringCellValue();

                    String programa2 = fila.getCell(12).getStringCellValue();
                    String alfa2 = fila.getCell(13).getStringCellValue();
                    String numerico2 = (String) "" + retornarValor(fila.getCell(14));
                    String grupo2 = fila.getCell(17).getStringCellValue();
                    String jornada2 = fila.getCell(19).getStringCellValue();

                    //vamos a la hoja ofertaeducativa
                    int contadorFilasOferta = 2;
                    XSSFRow filaOferta = hojaOferta.getRow(contadorFilasOferta);
                    while (!celdaVacia(filaOferta, 0)) {
                        String programa0 = filaOferta.getCell(0).getStringCellValue();
                        String alfa0 = filaOferta.getCell(5).getStringCellValue();
                        String numerico0 = (String) "" + retornarValor(filaOferta.getCell(6));
                        String grupo0 = filaOferta.getCell(1).getStringCellValue();
                        String jornada0 = filaOferta.getCell(4).getStringCellValue();

                        if (programa0.trim().equals(programa1.trim())
                                && alfa0.trim().equals(alfa1.trim())
                                && numerico0.trim().equals(numerico1.trim())
                                && grupo0.trim().equals(grupo1.trim())
                                && jornada0.trim().equals(jornada1.trim())) {

                            if (compartidaCruzada.equals("Cruzada")) {
                                filaOferta.getCell(13).setCellValue("Cruzada" + contadorCruzadas);
                            }
                            if (compartidaCruzada.equals("Compartida")) {
                                filaOferta.getCell(13).setCellValue("Compartida" + contadorCompartidas);
                                if (contadorCompartidas == 6) {
                                    System.out.println("");
                                }
                            }
                            break;
                        }

                        contadorFilasOferta++;
                        filaOferta = hojaOferta.getRow(contadorFilasOferta);
                    }

                    contadorFilasOferta = 2;
                    filaOferta = hojaOferta.getRow(contadorFilasOferta);
                    while (!celdaVacia(filaOferta, 0)) {
                        String programa0 = filaOferta.getCell(0).getStringCellValue();
                        String alfa0 = filaOferta.getCell(5).getStringCellValue();
                        String numerico0 = (String) "" + retornarValor(filaOferta.getCell(6));
                        String grupo0 = filaOferta.getCell(1).getStringCellValue();
                        String jornada0 = filaOferta.getCell(4).getStringCellValue();

                        if (programa0.trim().equals(programa2.trim())
                                && alfa0.trim().equals(alfa2.trim())
                                && numerico0.trim().equals(numerico2.trim())
                                && grupo0.trim().equals(grupo2.trim())
                                && jornada0.trim().equals(jornada2.trim())) {

                            if (compartidaCruzada.equals("Cruzada")) {
                                filaOferta.getCell(13).setCellValue("Cruzada" + contadorCruzadas);
                                contadorCruzadas++;
                            }
                            if (compartidaCruzada.equals("Compartida")) {
                                filaOferta.getCell(13).setCellValue("Compartida" + contadorCompartidas);
                                if (contadorCompartidas == 6) {
                                    System.out.println("");
                                }
                                contadorCompartidas++;
                            }
                            break;
                        }

                        contadorFilasOferta++;
                        filaOferta = hojaOferta.getRow(contadorFilasOferta);
                    }

                }

                contadorFilas++;
                fila = hojaRecomendaciones.getRow(contadorFilas);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
            Date fechaYHoraSalida = new Date();
            String fechaSalidaFormateada = sdf.format(fechaYHoraSalida);

            FileOutputStream fos = new FileOutputStream(rutaAbsolutaDirectorio + "CruzComp " + fechaSalidaFormateada + " " + nombreArchivo);
            libroFusiones.write(fos);
            libroFusiones.close();

            ArrayList<String> listadoSinPareja = cruceCompartidosSinPareja(rutaAbsolutaDirectorio + "CruzComp " + fechaSalidaFormateada + " " + nombreArchivo);

            String cadenaTotal = "Recomendaciones de fusión revisadas y pasadas sin problema";
            for (int i = 0; i < listadoSinPareja.size(); i++) {
                String cadena = "Sin pareja " + listadoSinPareja.get(i);

                if (i == 0) {
                    cadenaTotal = cadena;
                } else {
                    cadenaTotal = cadenaTotal + "\n" + cadena;
                }

                System.out.println(cadena);

            }
            textArea.setText(cadenaTotal);
            System.out.println("Se terminan de ejecutar las recomendaciones de fusión");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int retornarNumeroSesionesAlfaNumericoCarrera(String alfa,
            String numerico,
            String nomenclaturaCarrera) {

        int numeroSesiones = 0;

        for (int i = 0; i < listadoAsignturas.size(); i++) {
            if (alfa.equals(listadoAlfa.get(i))
                    && numerico.equals(listadoNumerico.get(i))
                    && idCarrera(nomenclaturaCarrera) == idsCarreras.get(i)) {
                numeroSesiones = listadoNumeroSesiones.get(i);
            }
        }

        return numeroSesiones;
    }

    public int retornarDuracionSesionAlfaNumericoCarrera(String alfa,
            String numerico,
            String nomenclaturaCarrera) {

        int duracionSesion = 0;

        for (int i = 0; i < listadoAsignturas.size(); i++) {
            if (alfa.equals(listadoAlfa.get(i))
                    && numerico.equals(listadoNumerico.get(i))
                    && idCarrera(nomenclaturaCarrera) == idsCarreras.get(i)) {
                duracionSesion = listadoDuracionSesiones.get(i);
            }
        }

        return duracionSesion;
    }

    public int retornarPeriodicidadAlfaNumericoCarrera(String alfa,
            String numerico,
            String nomenclaturaCarrera) {

        int periodicidad = 0;

        for (int i = 0; i < listadoAsignturas.size(); i++) {
            if (alfa.equals(listadoAlfa.get(i))
                    && numerico.equals(listadoNumerico.get(i))
                    && idCarrera(nomenclaturaCarrera) == idsCarreras.get(i)) {
                periodicidad = listadoPeriodicidad.get(i);
            }
        }

        return periodicidad;
    }

    public XSSFCell obtenerCeldaEncabezado(XSSFSheet hoja, int indiceFila, String encabezado) {
        XSSFCell celda = null;
        int j = 0;
        while (!celdaVacia(hoja.getRow(indiceFila), j)/* && j < INDICECOLUMNAINICIANFECHAS*/) {
            if (hoja.getRow(indiceFila).getCell(j).getStringCellValue().toLowerCase().equals(encabezado.toLowerCase())) {
                celda = hoja.getRow(indiceFila).getCell(j);
                break;
            }
            j++;
        }
        return celda;
    }
}
