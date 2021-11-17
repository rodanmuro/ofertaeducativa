/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa2;

import java.awt.Desktop;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class CrearHorario {

    public CrearHorario(String rutaMallaBase, String rutaOfertaEducativa, String rutaSalida) {
        
        System.out.println("Se comienzan a crear horarios");
        long tf = System.currentTimeMillis();
        
        
        CargaDeDatosExcel cde = new CargaDeDatosExcel();
        cde.cargaJornadas(rutaMallaBase);
        cde.cargarOfertaEducativa(rutaOfertaEducativa);
        cde.cargaFechasImportantes(rutaMallaBase);
        cde.cargaSalones(rutaMallaBase);
        cde.cargaOcupacionSalones(rutaMallaBase);

        CalendarioTotalSemestre calendarioSemestre
                = new CalendarioTotalSemestre(cde.LISTADOFECHAINICIAL.get(0), 
                        cde.LISTADOFECHAFINAL.get(0), cde.LISTADOFECHASPROHIBIDAS, 15);

        Horario h = new Horario(calendarioSemestre, cde);
        h.cargarHorasValidasTodasJornadas(calendarioSemestre, cde);
        h.contarDiasParaTodasJornadas(calendarioSemestre, cde);
        h.crearListadoDuracionTotalMinutosTodasJornadas(calendarioSemestre, cde);

        Validaciones v = new Validaciones(cde);

        for (int i = 0; i < cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            if (!v.jornadaValidaFilaOfertaEducativa(i)) {
                System.out.println("Jornada no válida " + cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(i));
                ofertaeducativa.Validaciones.mostrarVentanaError("Se encontró una jornada no válida, llamada "+cde.LISTADOOFERTAEDUCATIVA_JORNADA.get(i)
                +" que corresponde al programa "+cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.get(i)+" grupo "+cde.LISTADOOFERTAEDUCATIVA_SEMESTRE.get(i)
                +" asignatura "+ cde.LISTADOOFERTAEDUCATIVA_ASIGNATURA.get(i)+". No se puede continuar el programa.");
                break;
            }
        }

        cde.crearListadoDepuradoDocentes();
        cde.crearListadoDepuradoCreditosDocente();
        cde.crearListadoDepuradoSesionesDocente();
        cde.crearListadoOrdenadoDepuradoDocentesCreditos();
        cde.crearListadoOrdenadoDepuradoDocentesSesiones();
        cde.crearListadoIndicesOrdenAsignacionMayorCreditos();
        cde.crearListadoIndicesOrdenAsignacionMayorSesiones();
        cde.crearListadoDepuradoGrupos();
        cde.crearListadoDepuradoTotalMinutosSesionesGrupo();
        cde.crearListadoDepuradoCreditosDocenteSinRepetirCompartidos();
        cde.crearListadoPrioridadDocentesProgramar(rutaMallaBase);
        cde.crearListadoPrioridadIdCruceCompartidoProgramar(rutaMallaBase);
        cde.crearListadoDepuradoSesionesDocenteSinRepetirCompartidos();

        //esta parte es para verificar en que consisten los listados
        ArrayList<String> docentesPrioridad = cde.LISTADODOCENTESPRIORIDADPROGRAMAR;
        ArrayList<String> idCruceCompartidoProgramar = cde.LISTADOIDCRUCECOMPARTIDOPRIORIDADPROGRAMAR;
        //esta parte es para verificar en que consisten los listados

//        h.crearArchivoGrupoMinutosJornadaMinutos();
//        h.asignacionesSesionesIdeal(calendarioSemestre, cde);
        //en esta parte se validan la zona de horarios
//        v.validarZonaHorasSalidaHorarios();
//        v.validarDuracionesEnCeldas();
        ArrayList<Sesion> listadoSesionesDesdeHorario = cde.cargaSesionesDesdeSalidaHorarios();
        double efectividadGuardada = 0;

        h.SESIONES = new ArrayList<Sesion>(listadoSesionesDesdeHorario);
        CrearArchivosSalida cashdh = new CrearArchivosSalida(calendarioSemestre, cde, h, rutaSalida);
//        cashdh.crearArchivoTotalSesionesDesdeHorariosExcel(h.SESIONES, h.TOTALSESIONESAPROGRAMAR, h.SESIONES.size(), efectividadGuardada, v);
        cashdh.crearArchivoCreditosDocenteSinRepetirCompartidos();

        AsignacionSalones as = new AsignacionSalones(cde, h, cashdh);
//        as.asignarSalonesACompartidas();
//        as.asignarSalones();
//        as.escribirSalonesEnHojaTotalSesiones();

        ArrayList<Integer> mejorListadoEnteros = new ArrayList<Integer>();

        Constantes constantes = new Constantes();
        //ArrayList<Integer> mlia = GuardarDatos.recuperarDatos().LISTADOINDICESALEATORIOOPTIMO;
        //h.SESIONES = new ArrayList<Sesion>(GuardarDatos.recuperarDatos().SESIONES);

        ArrayList<Sesion> mls = new ArrayList<Sesion>();

        //en esta sesión se realizará el ensayo que corresponde a la programación de los índices
        //según la prioridad dada en la malla base
        ArrayList<ArrayList<Integer>> listadoIndicesSegunPrioridad = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> listadoIndicesSegunPrioridadIdCruceCompartido = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < docentesPrioridad.size(); i++) {
            listadoIndicesSegunPrioridad.add(cde.listadoIndicesDocenteDado(docentesPrioridad.get(i)));
        }
        for (int i = 0; i < idCruceCompartidoProgramar.size(); i++) {
            listadoIndicesSegunPrioridadIdCruceCompartido.add(cde.indicesCruzadoCompartido(cde.LISTADOOFERTAEDUCATIVA_IDCRUCECOMPARTIDO, idCruceCompartidoProgramar.get(i)));
        }

        h.totalSesionesProgramar(cde);

        ArrayList<Integer> listadoOrdenado = new ArrayList<Integer>();
        for (int i = 0; i < cde.LISTADOOFERTAEDUCATIVA_PROGRAMA.size(); i++) {
            if (cde.LISTADOOFERTAEDUCATIVA_VIRTUAL.get(i).trim().equals("No")) {
                listadoOrdenado.add(i);
            }
        }

        CrearArchivosSalida cash = new CrearArchivosSalida(calendarioSemestre, cde, h, rutaSalida);

        for (int i = 0; i < 1; i++) {

            h.SESIONES = new ArrayList<Sesion>();
            
            long t1 = System.currentTimeMillis();
            h.asignacionSesionesCruzadasCompartidasPresencial(calendarioSemestre, cde);
            System.out.println("total sesiones asignacionSesionesCruzadasCompartidasPresencial: "+h.SESIONES.size());
            h.asignacionesAleatoriaSesionesIdealPresencial(calendarioSemestre, cde, listadoOrdenado);
            System.out.println("total sesiones asignacionesAleatoriaSesionesIdealPresencial: "+h.SESIONES.size());
            long t0 = System.currentTimeMillis();
            
            System.out.println("tiempo asignaciones presencial: "+(t0-t1));
            System.out.println("total sesiones presencial: "+h.SESIONES.size());
            
            //h.asignacionSesionesCruzadasCompartidas(calendarioSemestre, cde);

            //esta parte corresponde a la signación de horario según listado de prioridad
            for (int j = 0; j < listadoIndicesSegunPrioridad.size(); j++) {
                //h.asignacionesAleatoriaSesionesIdeal(calendarioSemestre, cde, listadoIndicesSegunPrioridad.get(j));
                //h.asignacionesAleatoriaSesionesConCrucePeriodicidad(calendarioSemestre, cde, listadoIndicesSegunPrioridad.get(j));
            }

            for (int j = 0; j < listadoIndicesSegunPrioridadIdCruceCompartido.size(); j++) {
                //h.agregarSesionesCruzadasCompartidas(calendarioSemestre, cde, listadoIndicesSegunPrioridadIdCruceCompartido.get(j));
                //h.agregarSesionesCruzadasCompartidasConCrucePeriodicidad(calendarioSemestre, cde, listadoIndicesSegunPrioridadIdCruceCompartido.get(j));
            }
            
            t1 = System.currentTimeMillis();
            h.asignacionSesionesCruzadasCompartidasAleatorias(calendarioSemestre, cde, listadoOrdenado);
            System.out.println("sesiones luego de asignacionSesionesCruzadasCompartidasAleatorias: "+h.SESIONES.size());
            h.asignacionSesionesCruzadasCompartidasConCrucePeriodicidadAleatorias(calendarioSemestre, cde, listadoOrdenado);
            t0 = System.currentTimeMillis();
            System.out.println("tiempo asignaciones compartidas distancia: "+(t0-t1));
//
            int sesionesProgramadas = h.SESIONES.size();
            System.out.println("sesiones luego de compartidas distancia: "+h.SESIONES.size());

            ArrayList<Integer> listadoIndicesAleatorios = h.listadoIndicesAleatorios();
            t1 = System.currentTimeMillis();
            h.asignacionesAleatoriaSesionesIdeal(calendarioSemestre, cde, listadoOrdenado);
            t0 = System.currentTimeMillis();
            System.out.println("tiempo asignacionesAleatoriaSesionesIdeal"+(t0-t1));
            System.out.println("sesiones luego de asignacionesAleatoriaSesionesIdeal: "+h.SESIONES.size());

            t1 = System.currentTimeMillis();
            h.asignacionesAleatoriaSesionesConCrucePeriodicidad(calendarioSemestre, cde, listadoOrdenado);
            t0 = System.currentTimeMillis();
            System.out.println("tiempo asignacionesAleatoriaSesionesConCrucePeriodicidad"+(t0-t1));
            System.out.println("sesiones luego de asignacionesAleatoriaSesionesConCrucePeriodicidad: "+h.SESIONES.size());
//            h.asignacionesAleatoriaSesionesIdeal(calendarioSemestre, cde, listadoIndicesMD2);
//            h.asignacionesAleatoriaSesionesConCrucePeriodicidad(calendarioSemestre, cde, listadoIndicesMD2);

//            h.asignacionSesionesCruzadasCompartidas(calendarioSemestre, cde);
//            ArrayList<Integer> listadoIndicesAleatoriosCrCom = h.listadoIndicesAleatorios();
//            h.asignacionSesionesCruzadasCompartidasAleatorias(calendarioSemestre, cde, listadoOrdenado);
//            h.asignacionSesionesCruzadasCompartidasConCrucePeriodicidadAleatorias(calendarioSemestre, cde, listadoOrdenado);
            long t2 = System.currentTimeMillis();
            long t = t2 - t1;

            t1 = System.currentTimeMillis();
//            h.asignacionSesionesCruzadasCompartidasConCrucePeriodicidad(calendarioSemestre, cde);
//            h.asignacionSesionesCruzadasCompartidasConCrucePeriodicidadAleatorias(calendarioSemestre, cde, listadoIndicesAleatoriosCrCom);
            t2 = System.currentTimeMillis();
            t = t2 - t1;

            t1 = System.currentTimeMillis();
//            ArrayList<Integer> lia = new ArrayList<Integer>(cde.LISTADOINDICESORDENASIGNACIONSESIONES);//h.aleatorizarIndicesOrdenAsignacion(cde);//new ArrayList<Integer>(cde.LISTADOINDICESORDENASIGNACIONCREDITOS);
//            lia = h.aleatorizarIndicesOrdenAsignacion(cde);//new ArrayList<Integer>(cde.LISTADOINDICESORDENASIGNACIONCREDITOS);
            ArrayList<Integer> lia = new ArrayList<Integer>();

            t = t2 - t1;
//            System.out.println(i + " tiempo asignacion sesiones ideal " + t + " programadas " + h.SESIONES.size() + " efect: " + (double) ((double) h.SESIONES.size() / (double) 4608));

            t1 = System.currentTimeMillis();
//            h.asignacionesAleatoriaSesionesConCrucePeriodicidad(calendarioSemestre, cde, listadoOrdenado);
            t2 = System.currentTimeMillis();
            long t02 = System.currentTimeMillis();
            t = t02 - t0;

            if (i == 0) {
                h.crearListadoSesionesPorFila();
//                h.crearArchivoTotalSesionesProgramadasFila();
            }

            double efectividad = (double) ((double) h.SESIONES.size() / (double) h.TOTALSESIONESAPROGRAMAR);

            if (efectividad > efectividadGuardada) {
                efectividadGuardada = efectividad;
                mejorListadoEnteros = new ArrayList<Integer>(lia);
                mls = new ArrayList<Sesion>(h.SESIONES);
            }

            cash.SESIONESAPROGRAMAR = h.TOTALSESIONESAPROGRAMAR;
            cash.SESIONESPROGRAMADAS = h.SESIONES.size();
            cash.EFECTIVIDADHORARIO = efectividad;

            System.out.println(/*i + " tiempo asignacion sesiones ideal con cruce periodicidad " + */t + "," + h.SESIONES.size() + "," + (double) h.TOTALSESIONESAPROGRAMAR + "," + (double) ((double) h.SESIONES.size() / (double) h.TOTALSESIONESAPROGRAMAR) /*+ "a programar " + h.TOTALSESIONESAPROGRAMAR + " programadas " + h.SESIONES.size() + " efect: " + (double) ((double) h.SESIONES.size() / (double) h.TOTALSESIONESAPROGRAMAR)*/);
//            System.out.println(t);
        }
        h.crearListadoSesionesPorFila();
        constantes = new Constantes();
        constantes.LISTADOINDICESALEATORIOOPTIMO = new ArrayList<Integer>(mejorListadoEnteros);
        mls = new ArrayList<Sesion>(h.SESIONES);
        constantes.SESIONES = new ArrayList<Sesion>(mls);
        GuardarDatos.guardarDatos(constantes);
        cash.crearArchivoTotalSesiones(mls, h.TOTALSESIONESAPROGRAMAR, h.SESIONES.size(), efectividadGuardada);
        cash.crearArchivoTotalSesionesProgramadasFila();
        cash.crearArchivoCreditosDocenteSinRepetirCompartidos();
        cash.crearArchivoGrupoMinutosJornadaMinutos();
        cash.crearArchivoSesionesDocenteSinRepetirCompartidos();

        as = new AsignacionSalones(cde, h, cash);
        as.asignarSalonesACompartidas();
        as.asignarSalones();
        as.escribirSalonesEnHojaTotalSesiones();
        as.asignarSalones();
        Desktop d = Desktop.getDesktop();

        cash.cargarArchivoOfertaEducativa();
        cash.crearFormatoFechaDiaNombreMes();
        cash.crearFormatoFechahHoraMinuto();
        cash.obtenerNombresDiasTodasLasFechasSemestre();
        cash.calcularIndiceColumnaInicialParaColocarFechas();
        cash.colocarEncabezadosDiasFechasLibroSalida();
        cash.colocarFechasSesionesLibro(as);
        cash.escribirEnElLibrodeSalida();
        try {
//            d.open(new File("sesionesporfila.xlsx"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("tiempo para crear horario "+(System.currentTimeMillis()-tf));
        System.out.println("total sesiones "+h.SESIONES.size());
        JOptionPane.showMessageDialog(null, "Proceso de creación de horario terminado");
    }

}
