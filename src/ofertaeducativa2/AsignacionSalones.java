/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import static ofertaeducativa2.Horario.horaEstaEntre;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Usuario
 */
public class AsignacionSalones {

    CargaDeDatosExcel CDE;
    Horario H;
    CrearArchivosSalida CASH;
    ArrayList<Salon> LISTADOSALONES;
    ArrayList<Salon> LISTADOSALONESENORDENMENORCUPO;
    ArrayList<Salon> LISTADOSALONESASIGNADOS;
    String OBSERVACIONPROGRAMACIONPREVIA = "USARLO SOLO EN LAS CLASES SOLICITADAS";

    /**
     *
     * @param cde
     * @param h
     */
    public AsignacionSalones(CargaDeDatosExcel cde, Horario h, CrearArchivosSalida cash) {
        CDE = cde;
        H = h;
        CASH = cash;
        LISTADOSALONES = new ArrayList<Salon>(CDE.LISTADOSALONES);
        organizarSalonesPorCapacidadMenorAMayor();
        inicializarSalonesAAsignar();
    }

    public void inicializarSalonesAAsignar() {
        LISTADOSALONESASIGNADOS = new ArrayList<Salon>();
        for (int i = 0; i < H.SESIONES.size(); i++) {
            LISTADOSALONESASIGNADOS.add(new Salon("sinasignar " + i, 0, ""));
        }
    }

    public boolean sesionSalonAsignado(int indice) {
        if (LISTADOSALONESASIGNADOS.get(indice).getNombre().indexOf("sinasignar") != -1) {
            return false;
        }
        return true;
    }

    public void asignarSalonesACompartidas() {

        boolean sobrepasa = false;
        boolean cruce = false;
        for (int i = 0; i < H.SESIONES.size(); i++) {
            String nombreSalonPreviamenteProgramado = sesionGrupoConSalonProgramado(H.SESIONES.get(i)).trim();
            if (nombreSalonPreviamenteProgramado.equals("")) {
                if (esSesionCompartida(H.SESIONES.get(i)) && !sesionSalonAsignado(i)) {
                    for (int j = 0; j < LISTADOSALONESENORDENMENORCUPO.size(); j++) {
                        if (!sobrepasaCapacidadSalon(H.SESIONES.get(i), LISTADOSALONESENORDENMENORCUPO.get(j))
                                && !esSalonConProgramacionPrevia(LISTADOSALONESENORDENMENORCUPO.get(j))
                                && !hayCruceOcupacion(H.SESIONES.get(i), LISTADOSALONESENORDENMENORCUPO.get(j))) {
                            if (!cruceSalonDosSesiones(LISTADOSALONESENORDENMENORCUPO.get(j), H.SESIONES.get(i))) {
                                LISTADOSALONESASIGNADOS.set(i, LISTADOSALONESENORDENMENORCUPO.get(j));

                                String idCompartida = cadenaCompartidaSesion(H.SESIONES.get(i));
                                ArrayList<Integer> indicesCompartidos = listadoIndicesSesionCompartidaDada(idCompartida);

                                for (int k = 0; k < indicesCompartidos.size(); k++) {

                                    if (!cruceSalonDosSesionesParaCompartida(LISTADOSALONESENORDENMENORCUPO.get(j), H.SESIONES.get(indicesCompartidos.get(k)), idCompartida)
                                            && !sesionSalonAsignado(indicesCompartidos.get(k))) {
                                        LISTADOSALONESASIGNADOS.set(indicesCompartidos.get(k), LISTADOSALONESENORDENMENORCUPO.get(j));
                                    }
                                }
                                break;
                            }

                        }
                        if (j == LISTADOSALONESENORDENMENORCUPO.size() - 1) {
                            //LISTADOSALONESASIGNADOS.add(new Salon("Sin asignar " + i, 0));
                            System.out.println("No se puedo asignar salon a "
                                    + H.SESIONES.get(i).getGrupo().getPrograma()
                                    + " " + H.SESIONES.get(i).getGrupo().getSemestre()
                                    + " " + H.SESIONES.get(i).getAsignatura().getNombre()
                                    + " " + H.SESIONES.get(i).getFecha()
                            );
                        }
                    }
                }
            } else {
                Salon salonProgramado = null;

                for (int j = 0; j < LISTADOSALONES.size(); j++) {
                    if (nombreSalonPreviamenteProgramado.equals(LISTADOSALONES.get(j).getNombre().trim())) {
                        salonProgramado = LISTADOSALONES.get(j);
                        break;
                    }
                }

                if (salonProgramado == null) {
                    System.out.println("Salón " + nombreSalonPreviamenteProgramado + " asignado en oferta educativa no encontrado en el listado de salones");
                    System.out.println("No se puedo asignar salon a "
                            + H.SESIONES.get(i).getGrupo().getPrograma()
                            + " " + H.SESIONES.get(i).getGrupo().getSemestre()
                            + " " + H.SESIONES.get(i).getAsignatura().getNombre()
                            + " " + H.SESIONES.get(i).getFecha()
                    );
                } else {
                    LISTADOSALONESASIGNADOS.set(i, salonProgramado);
                }
            }
        }
        System.out.println("Se ha terminado la función de asignación de salones a compartidos");
    }

    /**
     * Esta función determina si una sesión dada es de un grupo que ya tiene un
     * salón fijo, y que fue asignado en la columna salón de la oferta educativa
     * por alguno de los directores de programa
     *
     * @param sesion
     * @return sesionSalonProgramado
     */
    public String sesionGrupoConSalonProgramado(Sesion sesion) {
        String sesionSalonProgramado = "";

        for (int i = 0; i < CDE.LISTADOOFERTAEDUCATIVA_SALON.size(); i++) {
            String cadenaSesion = sesion.getDocente().getNombre().trim()
                    + "" + sesion.getGrupo().getPrograma().trim()
                    + "" + sesion.getGrupo().getSemestre().trim()
                    + "" + sesion.getAsignatura().getAlfaNumerico().trim()
                    + "" + sesion.getAsignatura().getNombre().trim();

            String cadenaFilaOfertaEducativa = CDE.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i).trim()
                    + "" + CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i).trim()
                    + "" + CDE.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i).trim()
                    + "" + CDE.LISTADOOFERTAEDUCATIVA_ALFA.get(i).trim()
                    + "" + CDE.LISTADOOFERTAEDUCATIVA_NUMERICO.get(i).trim()
                    + "" + CDE.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i).trim();

            if (cadenaSesion.equals(cadenaFilaOfertaEducativa)) {
                sesionSalonProgramado = CDE.LISTADOOFERTAEDUCATIVA_SALON.get(i);
                break;
            }
        }

        return sesionSalonProgramado;
    }

    /**
     * Esta función determina si para ocupar un salón se debe hacer con
     * programación previa acorde con la observación de
     * OBSERVACIONPROGRAMACIONPREVIA
     *
     * @param salon
     * @return
     */
    public boolean esSalonConProgramacionPrevia(Salon salon) {
        boolean programacionPrevia = false;
        if (salon.getObservacion().trim().equals(OBSERVACIONPROGRAMACIONPREVIA.trim())) {
            programacionPrevia = true;
        }
        return programacionPrevia;
    }

    public void asignarSalones() {

        boolean sobrepasa = false;
        boolean cruce = false;
        for (int i = 0; i < H.SESIONES.size(); i++) {
            Sesion sesion1 = H.SESIONES.get(i);
            
            
            String nombreSalonPreviamenteProgramado = sesionGrupoConSalonProgramado(H.SESIONES.get(i)).trim();
            if (nombreSalonPreviamenteProgramado.equals("")) {
                boolean sesionesCompletas = false;
                if (!esSesionCompartida(H.SESIONES.get(i)) && !sesionSalonAsignado(i)) {
                    for (int j = 0; j < LISTADOSALONESENORDENMENORCUPO.size(); j++) {
                        if (!sobrepasaCapacidadSalon(H.SESIONES.get(i), LISTADOSALONESENORDENMENORCUPO.get(j))
                                && !esSalonConProgramacionPrevia(LISTADOSALONESENORDENMENORCUPO.get(j))
                                && !hayCruceOcupacion(H.SESIONES.get(i), LISTADOSALONESENORDENMENORCUPO.get(j))) {

                            //Prueba
//                            
//                            int numeroSesionesParaSesion1 = numeroSesionesAProgramarSesionDada(sesion1);
//                            int numeroSalonesAsignados = 0;
//                            
//                            
//                            for (int k = 0; k < H.SESIONES.size(); k++) {
//                                
//                                Sesion sesion2 = H.SESIONES.get(k);
//                                
//                                if (sonDosSesionesDelMismoNRC(sesion1, sesion2)) {
//                                    if (!cruceSalonDosSesiones(LISTADOSALONESENORDENMENORCUPO.get(j), H.SESIONES.get(k))) {
//                                        LISTADOSALONESASIGNADOS.set(k, LISTADOSALONESENORDENMENORCUPO.get(j));
//                                        numeroSalonesAsignados++;
//                                    }
//                                }
//                                if(numeroSalonesAsignados==numeroSesionesParaSesion1){
//                                    sesionesCompletas = true;
//                                    break;
//                                }
//                            }
                            //Final Prueba

                            //Original
                            if (!cruceSalonDosSesiones(LISTADOSALONESENORDENMENORCUPO.get(j), H.SESIONES.get(i))) {
                                LISTADOSALONESASIGNADOS.set(i, LISTADOSALONESENORDENMENORCUPO.get(j));
                                break;
                            }
                            //original
                        }
                        if (j == LISTADOSALONESENORDENMENORCUPO.size() - 1 /*|| sesionesCompletas*/) {
                            //LISTADOSALONESASIGNADOS.add(new Salon("Sin asignar " + i, 0));
                            System.out.println("No se pudo asignar salon a "
                                    + H.SESIONES.get(i).getGrupo().getPrograma()
                                    + " " + H.SESIONES.get(i).getGrupo().getSemestre()
                                    + " " + H.SESIONES.get(i).getAsignatura().getNombre()
                                    + " " + H.SESIONES.get(i).getFecha()
                            );
                        }
                    }
                }
            } else {

                Salon salonProgramado = null;

                for (int j = 0; j < LISTADOSALONES.size(); j++) {
                    if (nombreSalonPreviamenteProgramado.equals(LISTADOSALONES.get(j).getNombre().trim())) {
                        salonProgramado = LISTADOSALONES.get(j);
                        break;
                    }
                }

                if (salonProgramado == null) {
                    System.out.println("Salón " + nombreSalonPreviamenteProgramado + " asignado en oferta educativa no encontrado en el listado de salones");
                    System.out.println("No se puedo asignar salon a "
                            + H.SESIONES.get(i).getGrupo().getPrograma()
                            + " " + H.SESIONES.get(i).getGrupo().getSemestre()
                            + " " + H.SESIONES.get(i).getAsignatura().getNombre()
                            + " " + H.SESIONES.get(i).getFecha()
                    );
                } else {
                    LISTADOSALONESASIGNADOS.set(i, salonProgramado);
                }
            }
        }
        System.out.println("Se ha terminado la función de asignar salones");
    }

    public void escribirSalonesEnHojaTotalSesiones() {
        try {

            FileInputStream fis = new FileInputStream(CASH.CARPETASALIDA + "" + CASH.NOMBREARCHIVOTOTALSESIONES);
            XSSFWorkbook libroTotalSesiones = new XSSFWorkbook(fis);
            XSSFSheet hojaSesiones = libroTotalSesiones.getSheetAt(0);

            hojaSesiones.getRow(0).createCell(14).setCellValue("salón");
            hojaSesiones.getRow(0).createCell(15).setCellValue("capacidadSalón");

            for (int i = 1; i < H.SESIONES.size() + 1; i++) {
                hojaSesiones.getRow(i).createCell(14).setCellValue(LISTADOSALONESASIGNADOS.get(i - 1).getNombre());
                hojaSesiones.getRow(i).createCell(15).setCellValue(LISTADOSALONESASIGNADOS.get(i - 1).getCupo());
            }

            FileOutputStream fos = new FileOutputStream(CASH.CARPETASALIDA + "" + CASH.NOMBREARCHIVOTOTALSESIONES);
            libroTotalSesiones.write(fos);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Se ha terminado la función de escribirEnHojaTotalSesiones");
    }

    public void organizarSalonesPorCapacidadMenorAMayor() {
        LISTADOSALONESENORDENMENORCUPO = new ArrayList<Salon>(LISTADOSALONES);
        Collections.sort(LISTADOSALONESENORDENMENORCUPO, new Comparator<Salon>() {
            @Override
            public int compare(Salon s1, Salon s2) {
                return s1.getCupo() - s2.getCupo(); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    public int cantidadEstudiantesSesionDada(Sesion s) {
        int cantidad = 0;

        if (esSesionCompartida(s)) {

            ArrayList<Integer> listadoIndices = H.indicesCruzadoCompartido(CDE.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO, cadenaCompartidaSesion(s));

            for (int i = 0; i < listadoIndices.size(); i++) {
                cantidad = cantidad + cantidadEstudiantesFila(listadoIndices.get(i));
            }

        } else {
            cantidad = cantidadEstudiantesFila(filaEnOfertaParaUnaSesion(s));
        }

        return cantidad;
    }

    public String cadenaCompartidaSesion(Sesion s) {
        return CDE.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO.get(filaEnOfertaParaUnaSesion(s));
    }

    public boolean esSesionCompartida(Sesion s) {

        if (H.esFilaCompartida(CDE, filaEnOfertaParaUnaSesion(s))) {
            return true;
        }
        return false;
    }

    public int filaEnOfertaParaUnaSesion(Sesion s) {
        int fila = -1;
        for (int i = 0; i < CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            if (!CDE.esFilaVirtual(i)) {
                if (s.GRUPO.getPrograma().trim().equals(CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i).trim())
                        && s.GRUPO.getSemestre().trim().equals(CDE.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i).trim())
                        && s.ASIGNATURA.getNombre().trim().equals(CDE.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i).trim())
                        && s.DOCENTE.getNombre().trim().equals(CDE.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i).trim())) {
                    return i;
                }
            }
        }
        if(fila==-1){
            System.out.println("");
        }
        return fila;
    }

    public int cantidadEstudiantesFila(int fila) {
        return CDE.LISTADOOFERTAEDUCATIVA_CUPO.get(fila);
    }

    public boolean salonesIguales(Salon s1, Salon s2) {
        if (s1.getNombre().trim().equals(s2.getNombre().trim())) {
            return true;
        }
        return false;
    }

    /**
     * Si se tiene una sesion uno con hora inicial y final a,b y otra sesion con
     * hora inicial y final c,d se dice que se cruzan si se cumple que c-a>0 y
     * c-b<0 o bien a-d<0 y d-b<0 o bien a=c Es decir si alguna de las horas de
     * inicia o final del uno están dentro del intervalo del otro Las sesiones 1
     * y 2 son dos sesiones a comparar @param sesion1 @param sesion2 @return
     * true si las sesiones se cruza
     */
    public boolean cruceSalonDosSesiones(Salon salon1, Sesion sesion1) {

        boolean hayCruce = false;

        for (int i = 0; i < LISTADOSALONESASIGNADOS.size(); i++) {
            Salon salon2 = LISTADOSALONESASIGNADOS.get(i);
            if (salonesIguales(salon1, salon2)) {
                Sesion sesion2 = H.SESIONES.get(i);

                Date hi1, hf1, hi2, hf2;

                hi1 = sesion1.horaInicioSesion();
                hi2 = sesion2.horaInicioSesion();

                hf1 = sesion1.horaFinalSesion();
                hf2 = sesion2.horaFinalSesion();

                if (hi1.equals(hi2)) {
                    return true;
                }
//            long dif1 = hi2.getTime() - hi1.getTime();
//            long dif2 = hi2.getTime() - hf1.getTime();
                if ((hi2.getTime() - hi1.getTime()) > 0
                        && (hi2.getTime() - hf1.getTime()) < 0) {
                    return true;
                }
//            long dif3 = hi1.getTime() - hf2.getTime();
//            long dif4 = hf2.getTime() - hf1.getTime();
                if ((hi1.getTime() - hf2.getTime()) < 0
                        && (hf2.getTime() - hf1.getTime()) < 0) {
                    return true;
                }
                if ((hi1.getTime() - hi2.getTime()) > 0
                        && (hi1.getTime() - hf2.getTime()) < 0) {
                    return true;
                }
                if (hf1.getTime() - hi2.getTime() > 0 && hf1.getTime() - hf2.getTime() < 0) {
                    return true;
                }

            }
        }

        return hayCruce;
    }

    /**
     * Si se tiene una sesion uno con hora inicial y final a,b y otra sesion con
     * hora inicial y final c,d se dice que se cruzan si se cumple que c-a>0 y
     * c-b<0 o bien a-d<0 y d-b<0 o bien a=c Es decir si alguna de las horas de
     * inicia o final del uno están dentro del intervalo del otro Las sesiones 1
     * y 2 son dos sesiones a comparar @param sesion1 @param sesion2 @return
     * true si las sesiones se cruza. Esta función determina que no haya cruce
     * pero sólo con las compartidas de id diferente
     */
    public boolean cruceSalonDosSesionesParaCompartida(Salon salon1, Sesion sesion1, String idCompartida1) {

        boolean hayCruce = false;

        for (int i = 0; i < LISTADOSALONESASIGNADOS.size(); i++) {
            Salon salon2 = LISTADOSALONESASIGNADOS.get(i);
            if (i == 3861) {
                System.out.println("");
            }
            String idCompartida2 = cadenaCompartidaSesion(H.SESIONES.get(i));
            if (salonesIguales(salon1, salon2) && !idCompartida2.equals(idCompartida1)) {
                Sesion sesion2 = H.SESIONES.get(i);

                Date hi1, hf1, hi2, hf2;

                hi1 = sesion1.horaInicioSesion();
                hi2 = sesion2.horaInicioSesion();

                hf1 = sesion1.horaFinalSesion();
                hf2 = sesion2.horaFinalSesion();

                if (hi1.equals(hi2)) {
                    return true;
                }
//            long dif1 = hi2.getTime() - hi1.getTime();
//            long dif2 = hi2.getTime() - hf1.getTime();
                if ((hi2.getTime() - hi1.getTime()) > 0
                        && (hi2.getTime() - hf1.getTime()) < 0) {
                    return true;
                }
//            long dif3 = hi1.getTime() - hf2.getTime();
//            long dif4 = hf2.getTime() - hf1.getTime();
                if ((hi1.getTime() - hf2.getTime()) < 0
                        && (hf2.getTime() - hf1.getTime()) < 0) {
                    return true;
                }
                if ((hi1.getTime() - hi2.getTime()) > 0
                        && (hi1.getTime() - hf2.getTime()) < 0) {
                    return true;
                }
                if (hf1.getTime() - hi2.getTime() > 0 && hf1.getTime() - hf2.getTime() < 0) {
                    return true;
                }

            }
        }

        return hayCruce;
    }

    public boolean sobrepasaCapacidadSalon(Sesion sesion, Salon salon) {
        if (salon.getCupo() < cantidadEstudiantesSesionDada(sesion)) {
            return true;
        }
        return false;
    }

    public String getCadenaGrupoSesion(Sesion s) {
        return s.GRUPO.getPrograma().trim() + s.GRUPO.getSemestre().trim();
    }

    public boolean grupoConSalonAsignado(String cadenaGrupo) {
        for (int i = 0; i < H.SESIONES.size(); i++) {
            if (LISTADOSALONES.size() > i + 1) {
                if (getCadenaGrupoSesion(H.SESIONES.get(i)).equals(cadenaGrupo)) {
                    return true;
                }
            } else {
                break;
            }
        }
        return false;
    }

    public ArrayList<Integer> listadoIndicesSesionCompartidaDada(String idcompartida) {
        ArrayList<Integer> listadoIndices = new ArrayList<Integer>();

        for (int i = 0; i < H.SESIONES.size(); i++) {
            if (esSesionCompartida(H.SESIONES.get(i))) {
                if (cadenaCompartidaSesion(H.SESIONES.get(i)).trim().equals(idcompartida)) {
                    listadoIndices.add(i);
                }
            }
        }

        return listadoIndices;
    }

    public boolean hayCruceOcupacion(Sesion sesion, Salon salon) {
        boolean cruceOcupacion = false;

        String nombreSalon = salon.getNombre().trim();

        for (int i = 0; i < CDE.LISTADOOCUPACIONSALONES.size(); i++) {

            if (CDE.LISTADOOCUPACIONSALONES.get(i).getNombresSalon().trim().equals(nombreSalon)) {
                Calendar fechaSesion = Calendar.getInstance();
                fechaSesion.setTime(sesion.getFecha());
                int diaSesion = fechaSesion.get(Calendar.DAY_OF_WEEK);
                boolean elDiaDeLaSemanaDeLaSesionCoincideConUnDiaDeLaSemanaDeLaOcupacion = false;

                for (int j = 0; j < CDE.LISTADOOCUPACIONSALONES.get(i).getListadoTuplasDiaHoraInicialFinal().size(); j++) {
                    if (CDE.LISTADOOCUPACIONSALONES.get(i).getListadoTuplasDiaHoraInicialFinal().get(j).getDia() == diaSesion) {
                        elDiaDeLaSemanaDeLaSesionCoincideConUnDiaDeLaSemanaDeLaOcupacion = true;
                        break;
                    }
                }

                if (elDiaDeLaSemanaDeLaSesionCoincideConUnDiaDeLaSemanaDeLaOcupacion) {
                    if (semanaOcupacionValida(CDE.LISTADOOCUPACIONSALONES.get(i), sesion.getFecha())) {
                        Calendar fechaOcupacion = Calendar.getInstance();
                        fechaOcupacion.setTime(sesion.getFecha());

                        Calendar fechaOcupacionHoraInicial = Calendar.getInstance();
                        fechaOcupacionHoraInicial.setTime(sesion.getFecha());

                        Calendar fechaOcupacionHoraFinal = Calendar.getInstance();
                        fechaOcupacionHoraFinal.setTime(sesion.getFecha());

                        for (int j = 0; j < CDE.LISTADOOCUPACIONSALONES.get(i).getListadoTuplasDiaHoraInicialFinal().size(); j++) {
                            if (CDE.LISTADOOCUPACIONSALONES.get(i).getListadoTuplasDiaHoraInicialFinal().get(j).getDia()
                                    == diaSesion) {
                                Hora horaInicial = CDE.LISTADOOCUPACIONSALONES.get(i).getListadoTuplasDiaHoraInicialFinal().get(j).getHoraInicial();
                                Hora horaFinal = CDE.LISTADOOCUPACIONSALONES.get(i).getListadoTuplasDiaHoraInicialFinal().get(j).getHoraFinal();

                                fechaOcupacionHoraInicial.set(Calendar.HOUR_OF_DAY, horaInicial.getHora());
                                fechaOcupacionHoraInicial.set(Calendar.MINUTE, horaInicial.getMinutos());

                                fechaOcupacionHoraFinal.set(Calendar.HOUR_OF_DAY, horaFinal.getHora());
                                fechaOcupacionHoraFinal.set(Calendar.MINUTE, horaFinal.getMinutos());

                                Date hi1, hf1, hi2, hf2;

                                hi1 = sesion.horaInicioSesion();
                                hi2 = fechaOcupacionHoraInicial.getTime();

                                hf1 = sesion.horaFinalSesion();
                                hf2 = fechaOcupacionHoraFinal.getTime();

                                if (hi1.equals(hi2)) {
                                    return true;
                                }
                                if ((hi2.getTime() - hi1.getTime()) > 0
                                        && (hi2.getTime() - hf1.getTime()) < 0) {
                                    return true;
                                }
                                if ((hi1.getTime() - hf2.getTime()) < 0
                                        && (hf2.getTime() - hf1.getTime()) < 0) {
                                    return true;
                                }
                                if ((hi1.getTime() - hi2.getTime()) > 0
                                        && (hi1.getTime() - hf2.getTime()) < 0) {
                                    return true;
                                }
                                if (hf1.getTime() - hi2.getTime() > 0 && hf1.getTime() - hf2.getTime() < 0) {
                                    return true;
                                }

                            }
                        }

                    }
                }
            }
        }

        return cruceOcupacion;
    }

    /**
     * Esta función devuelve si una fecha dada está dentro de una semana válida
     * para una ocupacion, teniendo en cuenta la periodicidad de la jornada y su
     * fecha de inicio. Es decir si la periodicidad es cada 14 días se determina
     * si el día está dentro de una semana múltiplo de 14, de ser así, se
     * encuentra en una semana válida
     *
     * @param ocupacion
     * @param dia
     * @return
     */
    public boolean semanaOcupacionValida(OcupacionSalon ocupacion, Date dia) {
        boolean semanaValida = false;

        int periodicidad = ocupacion.getPeriodicidad();

        Calendar cDado = Calendar.getInstance();
        cDado.setTime(dia);

        cDado.set(Calendar.HOUR_OF_DAY, 0);
        cDado.set(Calendar.MINUTE, 0);
        cDado.set(Calendar.SECOND, 0);

        Calendar cInicioJornada = Calendar.getInstance();
        cInicioJornada.setTime(ocupacion.getFechaInicial());

        cInicioJornada.set(Calendar.HOUR_OF_DAY, 0);
        cInicioJornada.set(Calendar.MINUTE, 0);
        cInicioJornada.set(Calendar.SECOND, 0);

        int diasDiferencia = cDado.get(Calendar.DAY_OF_YEAR) - cInicioJornada.get(Calendar.DAY_OF_YEAR);

//        if (diasDiferencia % periodicidad == 0) {
//la fórmula a continuación se da para cuando se tienen varios días de la semana
//en una jornada. lo que se está haciendo es calcular que el residuo siempre esté durante 
//la primera parte de la semana; por ejemplo en el caso de cada 15 días, si se calcula solo el residuo
//para el primer día se tiene cero, en cambio con el margen se toma la jornada completa, durante esa primer semana
        if (diasDiferencia % periodicidad >= 0 && diasDiferencia % periodicidad <= 6) {
            semanaValida = true;
        }

        return semanaValida;
    }

    public ArrayList<Salon> listadoSalonesPorFila(int i) {
        ArrayList<Salon> listadoSalones = new ArrayList<Salon>();

        String programa = CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i);
        String semestre = CDE.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i);
        String asignatura = CDE.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i);
        String docente = CDE.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i);
        for (int j = 0; j < H.SESIONES.size(); j++) {
            if (H.SESIONES.get(j).GRUPO.getPrograma().trim().equals(programa.trim())
                    && H.SESIONES.get(j).GRUPO.getSemestre().trim().equals(semestre.trim())
                    && H.SESIONES.get(j).ASIGNATURA.getNombre().trim().equals(asignatura.trim())
                    && H.SESIONES.get(j).DOCENTE.getNombre().trim().equals(docente.trim())) {
                listadoSalones.add(LISTADOSALONESASIGNADOS.get(j));
            }
        }
        return listadoSalones;
    }

    public boolean sonDosSesionesDelMismoNRC(Sesion sesion1, Sesion sesion2) {
        boolean mismoNRC = false;

        String nombreAsignatura1 = sesion1.getAsignatura().getNombre().toLowerCase().trim();
        String nombreAsignatura2 = sesion2.getAsignatura().getNombre().toLowerCase().trim();

        String alfanumerico1 = sesion1.getAsignatura().getAlfaNumerico().toLowerCase().trim();
        String alfanumerico2 = sesion2.getAsignatura().getAlfaNumerico().toLowerCase().trim();

        String docente1 = sesion1.getDocente().getNombre().toLowerCase().trim();
        String docente2 = sesion2.getDocente().getNombre().toLowerCase().trim();

        String grupo1 = sesion1.getGrupo().getPrograma().toLowerCase().trim() + sesion1.getGrupo().getSemestre().toLowerCase().trim();
        String grupo2 = sesion2.getGrupo().getPrograma().toLowerCase().trim() + sesion2.getGrupo().getSemestre().toLowerCase().trim();

        if (nombreAsignatura1.equals(nombreAsignatura2)
                && alfanumerico1.equals(alfanumerico2)
                && docente1.equals(docente2)
                && grupo1.equals(grupo2)) {
            return true;
        }
        return mismoNRC;
    }
    
    public int numeroSesionesAProgramarSesionDada(Sesion sesion){
        int numeroSesiones = 0;
        
        String asignatura = sesion.getAsignatura().getNombre().toLowerCase().trim();
        String alfanumerico = sesion.getAsignatura().getAlfaNumerico().toLowerCase().trim();
        String grupo = sesion.getGrupo().getPrograma().toLowerCase().trim()+sesion.getGrupo().getSemestre().toLowerCase().trim();
        String docente = sesion.getDocente().getNombre().toLowerCase().trim();
        
        
 
        
        for (int i = 0; i < CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            
            String asignaturaOferta = CDE.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i).toLowerCase().trim();
            String alfanumericoOferta = CDE.LISTADOOFERTAEDUCATIVA_ALFA.get(i).toLowerCase().trim() + CDE.LISTADOOFERTAEDUCATIVA_NUMERICO.get(i).toLowerCase().trim();
            String grupoOFerta = CDE.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i).toLowerCase().trim() + CDE.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i).toLowerCase().trim();
            String docenteOferta = CDE.LISTADOOFERTAEDUCATIVA_DOCENTE.get(i).toLowerCase().trim(); 
            
            if(asignatura.equals(asignaturaOferta)
                    && alfanumerico.equals(alfanumericoOferta)
                    && grupo.equals(grupoOFerta)
                    && docente.equals(docenteOferta)){
                
                return CDE.LISTADOOFERTAEDUCATIVA_NUMEROSESIONES.get(i);
                
            }
        }
        
        return numeroSesiones;
    
    }

}
