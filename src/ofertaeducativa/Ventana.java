/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ofertaeducativa;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.ListModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.DefaultListModel;

import ofertaeducativa2.CrearHorario;

/**
 *
 * @author Rodanmuro
 */
public class Ventana extends javax.swing.JFrame {

    DefaultListModel modeloLista, modeloListaDocentes;
    JFileChooser selectorArchivoEntrada;
    JFileChooser selectorCarpetaSalida;
    JFileChooser selectorArchivoEntradaHorariosDocentes;
    JFileChooser selectorArchivoGeneral;
    CrearPdf pdfs;
    //en creación
    //String ARCHIVO_RAIZ_APLICACION = "./"+"dist";
    //para el build
    String ARCHIVO_RAIZ_APLICACION = "";
    String RUTA_ENTRADAS = "E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos";
    String RUTA_ENTRADA_MALLA_BASE = "";
    String RUTA_ENTRADA_FUENTE_BANNER = "";
    String RUTA_ENTRADA_OFERTA_ACTUAL = "";

    String RUTA_SALIDAS = "E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos";
    String RUTA_SALIDA_SEGUIMIENTOS = "\\SalidaSeguimientos";

    ProyeccionOfertaEducativa ofertaEducativa = null;
    Constantes CG = null;

    SimpleDateFormat formatoDMA;

    /**
     * Creates new form Ventana
     */
    public Ventana() throws IOException {

        initComponents();
        formatoDMA = new SimpleDateFormat("d-M-yyyy");
        Constantes c = new Constantes(
                "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", "", new ArrayList<String>()/*fechas prohibidas*/);
        CG = c;
        Constantes cg = GuardarDatos.recuperarDatos();
        if (cg != null) {
            CG = cg;
        }
        cargarCuadrosTexto();

    }

    public void cargarGrupos(String rutaArchivo) throws IOException {
        //"carpeta/CONSOLIDADO HORARIO 2017-15 Último.xlsx"
        pdfs = new CrearPdf(rutaArchivo, 2);
        modeloLista = new DefaultListModel();
        listListadoGrupos.setModel(modeloLista);

        ArrayList<Object[]> listadoGrupos = new ArrayList<Object[]>();
        listadoGrupos = pdfs.LISTADO_GRUPOS;

        for (int i = 0; i < listadoGrupos.size(); i++) {
            String programa = (String) pdfs.getPrograma(listadoGrupos.get(i));
            String semestre = (String) pdfs.getSemestre(listadoGrupos.get(i));
            String jornada = (String) pdfs.getJornada(listadoGrupos.get(i));
            modeloLista.addElement((i + 1) + " " + programa + " " + semestre + " " + jornada);
        }

    }

    public void cargarDocentes(String rutaArchivo) throws IOException {
        pdfs = new CrearPdf(rutaArchivo, 2);
        modeloListaDocentes = new DefaultListModel();
        listListadoDocentes.setModel(modeloListaDocentes);

        ArrayList<Object[]> listadoDocentes = new ArrayList<Object[]>();
        listadoDocentes = pdfs.LISTADO_DOCENTES;
        System.out.println("Tamaño listado docentes " + listadoDocentes.size());

        for (int i = 0; i < listadoDocentes.size(); i++) {
//            String programa = (String) pdfs.getPrograma(listadoGrupos.get(i));
//            String semestre = (String) pdfs.getSemestre(listadoGrupos.get(i));
//            String jornada = (String) pdfs.getJornada(listadoGrupos.get(i));
            String docente = (String) pdfs.getDocente(listadoDocentes.get(i));
            modeloListaDocentes.addElement((i + 1) + " " + docente);
            System.out.println((i + 1) + " " + docente);
        }
    }

    public void cargarCuadrosTexto() {
        tfRutaArchivoEntrada.setText(CG.RUTAOFERTAEDUCATIVAHORARIOGRUPOS);
        tfCarpetaSalida.setText(CG.CARPETASALIDAHORARIOGRUPOS);
        tfRutaArchivoEntradaHorariosDocentes.setText(CG.RUTAOFERTAEDUCATIVAHORARIODOCENTES);
        tfCarpetaSalida2.setText(CG.CARPETASALIDAHORARIODOCENTES);
        tfRutaArchivoEntrada1.setText(CG.RUTAOFERTAEDUCATIVACONTEOCORTES);
        tfCarpetaSalida1.setText(CG.CARPETASALIDACONTEOCORTES);
        jTextFieldMallaCurricular.setText(CG.RUTAMALLACURRICULARBASECREARSEGUIMIENTOS);
        jTextFieldMatriculaNRC.setText(CG.RUTAEXCELMATRICULASNRCBANNERCREARSEGUIMIENTO);
        jTextFieldOfertaEducativaActual.setText(CG.RUTAOFERTAEDUCATIVACREARSEGUIMIENTOS);
        jTextFieldCarpetaSalida.setText(CG.CARPETASALIDASEGUIMIENTOS);
        jTextPeriodoActual.setText(CG.PERIODOACTUAL);
        jTextFieldMallaBase.setText(CG.RUTAMALLACURRICULARBASEPROYECCION);
        jTextFieldSeguimientos.setText(CG.RUTALIBROSEGUIMIENTOS);
        jTextFieldCupos.setText(CG.RUTALIBROCUPOSESTIMADOS);
        jTextFieldPrimerSemestre.setText(CG.RUTALIBROMETASPRIMERSEMESTRES);
        jTextFieldCarpetaSalida2.setText(CG.CARPETASALIDAOFERTAEDUCATIVAPROYECCION);
        jTextPeriodoSiguiente.setText(CG.PERIODOSIGUIENTE);
        jTextFieldOfertaACruzar.setText(CG.RUTAOFERTAEDUCATIVACRUZARCOMPARTIR);
        jTextFieldMallaBaseCrearHorario.setText(CG.RUTAMALLACURRICULARBASECREARHORARIO);
        jTextFieldOfertaEducativaCrearHorario.setText(CG.RUTAOFERTAEDUCATIVACREARHORARIO);
        jTextFieldCarpetaSalidaCrearHorario.setText(CG.CARPETASALIDAOFERTAEDUCATIVA);
        jTextFieldFuenteBannerNRC.setText(CG.RUTAEXCELMATRICULASNRCBANNERCREARHORARIOS);
        jTextFieldFormatoPasarABello.setText(CG.RUTALIBROFORMATOBELLO);

        jDateChooserInicioSemestre.setDate(fechaFormateada(CG.FECHAINICIOSEMESTRE));
        jDateChooserFinalSemestre.setDate(fechaFormateada(CG.FECHAFINALSEMESTRE));
        jDateChooserInicioLyJ.setDate(fechaFormateada(CG.FECHAINICIOLYJ));
        jDateChooserInicioLyM.setDate(fechaFormateada(CG.FECHAINICIOLYM));
        jDateChooserInicioMyJ.setDate(fechaFormateada(CG.FECHAINICIOMYJ));
        jDateChooserInicioMiyVi.setDate(fechaFormateada(CG.FECHAINICIOMIYVI));
        jDateChooserMiyViP.setDate(fechaFormateada(CG.FECHAINICIOMIYVIP));
        jDateChooserInicioSabadoD1.setDate(fechaFormateada(CG.FECHAINICIOSABADOD));
        jDateChooserInicioSabadoD2.setDate(fechaFormateada(CG.FECHAINICIOSABADOD2));
        jDateChooserInicioSabadoT1.setDate(fechaFormateada(CG.FECHAINICIOSABADOT));
        jDateChooserInicioSabadoT2.setDate(fechaFormateada(CG.FECHAINICIOSABADOT2));

        DefaultListModel<String> dlm = new DefaultListModel<String>();
        for (int i = 0; i < CG.FECHASPROHIBIDAS.size(); i++) {
            dlm.addElement(CG.FECHASPROHIBIDAS.get(i));
        }
        jListFechasProhibidas.setModel(dlm);

    }

    public void guardarVariablesTextField() {
        CG.RUTAOFERTAEDUCATIVAHORARIOGRUPOS = tfRutaArchivoEntrada.getText();
        CG.CARPETASALIDAHORARIOGRUPOS = tfCarpetaSalida.getText();
        CG.RUTAOFERTAEDUCATIVAHORARIODOCENTES = tfRutaArchivoEntradaHorariosDocentes.getText();
        CG.CARPETASALIDAHORARIODOCENTES = tfCarpetaSalida2.getText();
        CG.RUTAOFERTAEDUCATIVACONTEOCORTES = tfRutaArchivoEntrada1.getText();
        CG.CARPETASALIDACONTEOCORTES = tfCarpetaSalida1.getText();
        CG.RUTAMALLACURRICULARBASECREARSEGUIMIENTOS = jTextFieldMallaCurricular.getText();
        CG.RUTAEXCELMATRICULASNRCBANNERCREARSEGUIMIENTO = jTextFieldMatriculaNRC.getText();
        CG.RUTAOFERTAEDUCATIVACREARSEGUIMIENTOS = jTextFieldOfertaEducativaActual.getText();
        CG.CARPETASALIDASEGUIMIENTOS = jTextFieldCarpetaSalida.getText();
        CG.PERIODOACTUAL = jTextPeriodoActual.getText();
        CG.RUTAMALLACURRICULARBASEPROYECCION = jTextFieldMallaBase.getText();
        CG.RUTALIBROSEGUIMIENTOS = jTextFieldSeguimientos.getText();
        CG.RUTALIBROCUPOSESTIMADOS = jTextFieldCupos.getText();
        CG.RUTALIBROMETASPRIMERSEMESTRES = jTextFieldPrimerSemestre.getText();
        CG.CARPETASALIDAOFERTAEDUCATIVAPROYECCION = jTextFieldCarpetaSalida2.getText();
        CG.PERIODOSIGUIENTE = jTextPeriodoSiguiente.getText();
        CG.RUTAOFERTAEDUCATIVACRUZARCOMPARTIR = jTextFieldOfertaACruzar.getText();
        CG.RUTAMALLACURRICULARBASECREARHORARIO = jTextFieldMallaBaseCrearHorario.getText();
        CG.RUTAOFERTAEDUCATIVACREARHORARIO = jTextFieldOfertaEducativaCrearHorario.getText();
        CG.CARPETASALIDAOFERTAEDUCATIVA = jTextFieldCarpetaSalidaCrearHorario.getText();
        CG.RUTAEXCELMATRICULASNRCBANNERCREARHORARIOS = jTextFieldFuenteBannerNRC.getText();
        CG.RUTALIBROFORMATOBELLO = jTextFieldFormatoPasarABello.getText();

        CG.FECHAINICIOSEMESTRE = formatoDMA.format(jDateChooserInicioSemestre.getDate());
        CG.FECHAFINALSEMESTRE = formatoDMA.format(jDateChooserFinalSemestre.getDate());
        CG.FECHAINICIOLYJ = formatoDMA.format(jDateChooserInicioLyJ.getDate());
        CG.FECHAINICIOLYM = formatoDMA.format(jDateChooserInicioLyM.getDate());
        CG.FECHAINICIOMYJ = formatoDMA.format(jDateChooserInicioMyJ.getDate());
        CG.FECHAINICIOMIYVI = formatoDMA.format(jDateChooserInicioMiyVi.getDate());
        CG.FECHAINICIOMIYVIP = formatoDMA.format(jDateChooserMiyViP.getDate());
        CG.FECHAINICIOSABADOD = formatoDMA.format(jDateChooserInicioSabadoD1.getDate());
        CG.FECHAINICIOSABADOD2 = formatoDMA.format(jDateChooserInicioSabadoD2.getDate());
        CG.FECHAINICIOSABADOT = formatoDMA.format(jDateChooserInicioSabadoT1.getDate());
        CG.FECHAINICIOSABADOT2 = formatoDMA.format(jDateChooserInicioSabadoT2.getDate());

        CG.FECHASPROHIBIDAS = formatearFechasProhibidas();

        GuardarDatos.guardarDatos(CG);
    }

    public Date fechaFormateada(String fecha) {
        Date d = new Date();
        try {
            d = formatoDMA.parse(fecha);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public ArrayList<String> formatearFechasProhibidas() {
        ArrayList<String> listadoF = new ArrayList<String>();

        for (int i = 0; i < jListFechasProhibidas.getModel().getSize(); i++) {

            try {
                Date d = new Date();
                d = formatoDMA.parse(jListFechasProhibidas.getModel().getElementAt(i));
                listadoF.add(formatoDMA.format(d));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return listadoF;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listListadoGrupos = new javax.swing.JList<>();
        tfRutaArchivoEntrada = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        tfCarpetaSalida = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        tfRutaArchivoEntradaHorariosDocentes = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        tfCarpetaSalida2 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jButton9 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listListadoDocentes = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        tfRutaArchivoEntrada1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfCarpetaSalida1 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jRadioButton2 = new javax.swing.JRadioButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        areaTextoConteoCortes = new javax.swing.JTextArea();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel27 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldMallaCurricular = new javax.swing.JTextField();
        jTextFieldOfertaEducativaActual = new javax.swing.JTextField();
        jTextFieldCarpetaSalida = new javax.swing.JTextField();
        botonAbrirFileChooserMalla = new javax.swing.JButton();
        botonAbrirFileChooserOfertaActual = new javax.swing.JButton();
        botonAbrirFileChooserSeguimientos = new javax.swing.JButton();
        botonCrearArchivoSeguimientos = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldMatriculaNRC = new javax.swing.JTextField();
        botonAbrirFileChooserMatriculaNRC = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jTextPeriodoActual = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldMallaBase = new javax.swing.JTextField();
        jTextFieldSeguimientos = new javax.swing.JTextField();
        jTextFieldCupos = new javax.swing.JTextField();
        jTextFieldPrimerSemestre = new javax.swing.JTextField();
        botonAbrirFileChooserMalla2 = new javax.swing.JButton();
        botonAbrirFileChooserSeguimientos2 = new javax.swing.JButton();
        botonAbrirFileChooserCupos = new javax.swing.JToggleButton();
        botonAbrirFileChooserMetaPrimerSemestre = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jTextPeriodoSiguiente = new javax.swing.JTextField();
        botonCrearProyeccionOferta = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jTextFieldCarpetaSalida2 = new javax.swing.JTextField();
        abrirFileChooserCarpetaSalida = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        abrirOfertaCruzarCompartir = new javax.swing.JButton();
        jTextFieldOfertaACruzar = new javax.swing.JTextField();
        botonEjecutarRecomendaciones = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextAreaObservacionesProyeccion = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jTextFieldMallaBaseCrearHorario = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jTextFieldOfertaEducativaCrearHorario = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jTextFieldCarpetaSalidaCrearHorario = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jTextFieldFuenteBannerNRC = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jTextFieldFormatoPasarABello = new javax.swing.JTextField();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jCheckBoxTenerEnCuentaHorariosYaEscritos = new javax.swing.JCheckBox();
        jButton18 = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jButtonCrearHorariosCuatrimestre = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextAreaSalidasMensajesCrearHorarios = new javax.swing.JTextArea();
        jCheckBoxPermitirCruceDia = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jDateChooserInicioSemestre = new com.toedter.calendar.JDateChooser();
        jDateChooserFinalSemestre = new com.toedter.calendar.JDateChooser();
        jDateChooserInicioLyM = new com.toedter.calendar.JDateChooser();
        jDateChooserInicioLyJ = new com.toedter.calendar.JDateChooser();
        jDateChooserInicioMyJ = new com.toedter.calendar.JDateChooser();
        jDateChooserInicioMiyVi = new com.toedter.calendar.JDateChooser();
        jDateChooserMiyViP = new com.toedter.calendar.JDateChooser();
        jDateChooserInicioSabadoD1 = new com.toedter.calendar.JDateChooser();
        jDateChooserInicioSabadoD2 = new com.toedter.calendar.JDateChooser();
        jDateChooserInicioSabadoT1 = new com.toedter.calendar.JDateChooser();
        jDateChooserInicioSabadoT2 = new com.toedter.calendar.JDateChooser();
        jLabel26 = new javax.swing.JLabel();
        jDateChooserFechasProhibidas = new com.toedter.calendar.JDateChooser();
        jScrollPane5 = new javax.swing.JScrollPane();
        jListFechasProhibidas = new javax.swing.JList<>();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Creación de Oferta Educativa");
        setMinimumSize(new java.awt.Dimension(800, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        jTabbedPane1.setToolTipText("Creación de Oferta Educativa");

        jScrollPane2.setViewportView(listListadoGrupos);

        tfRutaArchivoEntrada.setEditable(false);
        tfRutaArchivoEntrada.setText("\"D:\\Uniminuto 2016 2\\Proyecto Horario\\OFERTA 2017-2 Julio 4 cambiando fechas.xlsx\"");
        tfRutaArchivoEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfRutaArchivoEntradaActionPerformed(evt);
            }
        });

        jLabel1.setText("Archivo de entrada:");

        jButton1.setText("Seleccionar archivo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jRadioButton1.setText("Seleccionar todos");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Crear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setText("Carpeta de salida:");

        tfCarpetaSalida.setText("C:\\Users\\Rodanmuro\\Desktop\\carpeta");

        jButton3.setText("Carpeta de salida");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tfRutaArchivoEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                                    .addComponent(tfCarpetaSalida))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton1)
                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 744, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(52, 52, 52))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfRutaArchivoEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfCarpetaSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jButton2))
                .addContainerGap(703, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Horario de grupos", jPanel1);

        jLabel6.setText("Archivo de entrada:");

        tfRutaArchivoEntradaHorariosDocentes.setEditable(false);
        tfRutaArchivoEntradaHorariosDocentes.setText("\"D:\\Uniminuto 2016 2\\Proyecto Horario\\OFERTA 2017-2 Julio 4 cambiando fechas.xlsx\"");
        tfRutaArchivoEntradaHorariosDocentes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfRutaArchivoEntradaHorariosDocentesActionPerformed(evt);
            }
        });

        jButton7.setText("Seleccionar archivo");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel7.setText("Carpeta de salida:");

        tfCarpetaSalida2.setText("C:\\Users\\Rodanmuro\\Desktop\\carpeta");

        jButton8.setText("Carpeta de salida");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jRadioButton3.setText("Seleccionar todos");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        jButton9.setText("Crear");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(listListadoDocentes);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfCarpetaSalida2, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(tfRutaArchivoEntradaHorariosDocentes, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jRadioButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton9)))
                        .addGap(0, 267, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tfRutaArchivoEntradaHorariosDocentes, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7))
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tfCarpetaSalida2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton3)
                    .addComponent(jButton9))
                .addGap(691, 691, 691))
        );

        jTabbedPane1.addTab("Horario de docentes", jPanel3);

        jPanel2.setLayout(null);

        tfRutaArchivoEntrada1.setEditable(false);
        tfRutaArchivoEntrada1.setText("E:\\Uniminuto 2018 1\\Proyecto Horario\\Horarios 2018-1 Nrc Llenos Sin Cuatrimestrales.xlsx");
        tfRutaArchivoEntrada1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfRutaArchivoEntrada1ActionPerformed(evt);
            }
        });
        jPanel2.add(tfRutaArchivoEntrada1);
        tfRutaArchivoEntrada1.setBounds(116, 20, 439, 20);

        jLabel3.setText("Archivo de entrada:");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(10, 23, 96, 14);

        jLabel4.setText("Carpeta de salida:");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(10, 110, 88, 14);

        tfCarpetaSalida1.setText("C:\\Users\\Rodanmuro\\Desktop\\carpetasalida");
        tfCarpetaSalida1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfCarpetaSalida1ActionPerformed(evt);
            }
        });
        jPanel2.add(tfCarpetaSalida1);
        tfCarpetaSalida1.setBounds(120, 110, 439, 20);

        jButton4.setText("Seleccionar archivo");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4);
        jButton4.setBounds(565, 19, 134, 23);

        jButton5.setText("Carpeta de salida");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5);
        jButton5.setBounds(570, 110, 134, 23);

        jLabel5.setText("Seleccionar la fecha de corte:");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(10, 140, 142, 25);

        jRadioButton2.setText("Incluir la fecha de corte en el rango");
        jPanel2.add(jRadioButton2);
        jRadioButton2.setBounds(300, 140, 195, 23);

        jButton6.setText("Crear");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6);
        jButton6.setBounds(640, 140, 59, 23);

        areaTextoConteoCortes.setColumns(20);
        areaTextoConteoCortes.setRows(5);
        jScrollPane3.setViewportView(areaTextoConteoCortes);

        jPanel2.add(jScrollPane3);
        jScrollPane3.setBounds(10, 180, 689, 225);
        jPanel2.add(jDateChooser1);
        jDateChooser1.setBounds(160, 140, 119, 20);

        jLabel27.setText("Recordar que las fechas deben comenzar en la columna 14 (base 0). Así que cortar y pegar las fechas");
        jPanel2.add(jLabel27);
        jLabel27.setBounds(10, 60, 680, 14);

        jTabbedPane1.addTab("Conteo cortes", jPanel2);

        jPanel4.setLayout(null);

        jLabel8.setText("Seleccionar malla curricular base:");
        jPanel4.add(jLabel8);
        jLabel8.setBounds(20, 20, 158, 14);

        jLabel9.setText("Seleccionar oferta periodo actual:");
        jPanel4.add(jLabel9);
        jLabel9.setBounds(20, 100, 162, 14);

        jLabel10.setText("Seleccionar carpeta de salida:");
        jPanel4.add(jLabel10);
        jLabel10.setBounds(20, 140, 143, 14);
        jPanel4.add(jTextFieldMallaCurricular);
        jTextFieldMallaCurricular.setBounds(260, 20, 282, 20);
        jPanel4.add(jTextFieldOfertaEducativaActual);
        jTextFieldOfertaEducativaActual.setBounds(260, 100, 282, 20);
        jPanel4.add(jTextFieldCarpetaSalida);
        jTextFieldCarpetaSalida.setBounds(260, 140, 282, 20);

        botonAbrirFileChooserMalla.setText("Seleccionar malla");
        botonAbrirFileChooserMalla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAbrirFileChooserMallaActionPerformed(evt);
            }
        });
        jPanel4.add(botonAbrirFileChooserMalla);
        botonAbrirFileChooserMalla.setBounds(560, 20, 159, 23);

        botonAbrirFileChooserOfertaActual.setText("Seleccionar oferta actual");
        botonAbrirFileChooserOfertaActual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAbrirFileChooserOfertaActualActionPerformed(evt);
            }
        });
        jPanel4.add(botonAbrirFileChooserOfertaActual);
        botonAbrirFileChooserOfertaActual.setBounds(560, 100, 159, 23);

        botonAbrirFileChooserSeguimientos.setText("Carpeta salida");
        botonAbrirFileChooserSeguimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAbrirFileChooserSeguimientosActionPerformed(evt);
            }
        });
        jPanel4.add(botonAbrirFileChooserSeguimientos);
        botonAbrirFileChooserSeguimientos.setBounds(560, 140, 159, 23);

        botonCrearArchivoSeguimientos.setText("Crear");
        botonCrearArchivoSeguimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearArchivoSeguimientosActionPerformed(evt);
            }
        });
        jPanel4.add(botonCrearArchivoSeguimientos);
        botonCrearArchivoSeguimientos.setBounds(660, 200, 59, 23);

        jLabel11.setText("Seleccionar el Excel con las matrículas por NRC:");
        jPanel4.add(jLabel11);
        jLabel11.setBounds(20, 60, 227, 14);

        jTextFieldMatriculaNRC.setMaximumSize(new java.awt.Dimension(6, 20));
        jPanel4.add(jTextFieldMatriculaNRC);
        jTextFieldMatriculaNRC.setBounds(260, 60, 282, 20);

        botonAbrirFileChooserMatriculaNRC.setText("Seleccionar fuente Banner");
        botonAbrirFileChooserMatriculaNRC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAbrirFileChooserMatriculaNRCActionPerformed(evt);
            }
        });
        jPanel4.add(botonAbrirFileChooserMatriculaNRC);
        botonAbrirFileChooserMatriculaNRC.setBounds(560, 60, 159, 23);

        jLabel12.setText("Escribir periodo actual:");
        jPanel4.add(jLabel12);
        jLabel12.setBounds(20, 190, 109, 14);

        jTextPeriodoActual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextPeriodoActualActionPerformed(evt);
            }
        });
        jPanel4.add(jTextPeriodoActual);
        jTextPeriodoActual.setBounds(260, 190, 104, 20);

        jTabbedPane1.addTab("Crear Seguimientos", jPanel4);

        jLabel13.setText("Seleccionar malla curricular base:");

        jLabel14.setText("Seleccionar libro con los seguimientos:");

        jLabel15.setText("Seleccionar libro con el archivo de cupos estimados:");

        jLabel16.setText("Seleccionar libro con las metas del primer semestre:");

        jTextFieldPrimerSemestre.setMaximumSize(new java.awt.Dimension(6, 40));
        jTextFieldPrimerSemestre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPrimerSemestreActionPerformed(evt);
            }
        });

        botonAbrirFileChooserMalla2.setText("Seleccionar Malla");
        botonAbrirFileChooserMalla2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAbrirFileChooserMalla2ActionPerformed(evt);
            }
        });

        botonAbrirFileChooserSeguimientos2.setText("Seleccionar Seguimientos");
        botonAbrirFileChooserSeguimientos2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAbrirFileChooserSeguimientos2ActionPerformed(evt);
            }
        });

        botonAbrirFileChooserCupos.setText("Seleccionar cupos");
        botonAbrirFileChooserCupos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAbrirFileChooserCuposActionPerformed(evt);
            }
        });

        botonAbrirFileChooserMetaPrimerSemestre.setText("Seleccionar primer semestre");
        botonAbrirFileChooserMetaPrimerSemestre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAbrirFileChooserMetaPrimerSemestreActionPerformed(evt);
            }
        });

        jLabel17.setText("Escribir periodo siguiente:");

        jTextPeriodoSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextPeriodoSiguienteActionPerformed(evt);
            }
        });

        botonCrearProyeccionOferta.setText("Crear");
        botonCrearProyeccionOferta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearProyeccionOfertaActionPerformed(evt);
            }
        });

        jLabel18.setText("Seleccionar carpeta de salida");

        abrirFileChooserCarpetaSalida.setText("Seleccionar carpeta salida");
        abrirFileChooserCarpetaSalida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirFileChooserCarpetaSalidaActionPerformed(evt);
            }
        });

        jLabel19.setText("Seleccionar oferta para cruzar y compartir");

        abrirOfertaCruzarCompartir.setText("Seleccionar oferta");
        abrirOfertaCruzarCompartir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirOfertaCruzarCompartirActionPerformed(evt);
            }
        });

        botonEjecutarRecomendaciones.setText("Ejecutar recomendaciones");
        botonEjecutarRecomendaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEjecutarRecomendacionesActionPerformed(evt);
            }
        });

        jTextAreaObservacionesProyeccion.setColumns(20);
        jTextAreaObservacionesProyeccion.setRows(5);
        jScrollPane4.setViewportView(jTextAreaObservacionesProyeccion);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jTextFieldSeguimientos, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel13)
                                                    .addComponent(jLabel14)
                                                    .addComponent(jLabel15))
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                                        .addGap(71, 71, 71)
                                                        .addComponent(jTextFieldCupos, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTextFieldMallaBase, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel16)
                                                .addComponent(jLabel17))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jTextPeriodoSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTextFieldCarpetaSalida2, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTextFieldOfertaACruzar, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTextFieldPrimerSemestre, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addComponent(jLabel18))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(botonAbrirFileChooserMetaPrimerSemestre, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                                    .addComponent(botonAbrirFileChooserCupos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(botonAbrirFileChooserSeguimientos2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(botonAbrirFileChooserMalla2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(abrirFileChooserCarpetaSalida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(botonCrearProyeccionOferta))
                                    .addComponent(abrirOfertaCruzarCompartir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addGap(302, 302, 302)
                                .addComponent(botonEjecutarRecomendaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(69, 69, 69))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextFieldMallaBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonAbrirFileChooserMalla2))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextFieldSeguimientos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonAbrirFileChooserSeguimientos2))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextFieldCupos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonAbrirFileChooserCupos))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextFieldPrimerSemestre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonAbrirFileChooserMetaPrimerSemestre))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jTextFieldCarpetaSalida2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(abrirFileChooserCarpetaSalida))
                .addGap(17, 17, 17)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jTextPeriodoSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonCrearProyeccionOferta))
                .addGap(56, 56, 56)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(abrirOfertaCruzarCompartir)
                    .addComponent(jTextFieldOfertaACruzar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(botonEjecutarRecomendaciones)
                .addGap(50, 50, 50)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(638, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Proyección", jPanel5);

        jLabel20.setText("Seleccionar Malla Curricular Base:");

        jButton10.setText("Seleccionar Malla");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel21.setText("Seleccionar Oferta Educativa:");

        jButton11.setText("Seleccionar Oferta");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel22.setText("Seleccionar Carpeta Salida:");

        jTextFieldCarpetaSalidaCrearHorario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCarpetaSalidaCrearHorarioActionPerformed(evt);
            }
        });

        jButton12.setText("Seleccionar Carpeta");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText("Crear horarios");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel23.setText("Seleccionar fuente Banner NRC");

        jButton14.setText("Seleccionar Banner");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setText("Reciclar NRC");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jLabel24.setText("Seleccionar formato  Bello");

        jButton16.setText("Seleccionar formato");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setText("Pasar datos");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jCheckBoxTenerEnCuentaHorariosYaEscritos.setSelected(true);
        jCheckBoxTenerEnCuentaHorariosYaEscritos.setText("Tener en cuenta los horarios ya escritos");
        jCheckBoxTenerEnCuentaHorariosYaEscritos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxTenerEnCuentaHorariosYaEscritosActionPerformed(evt);
            }
        });

        jButton18.setText("Revisar horarios ya escritos");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jLabel25.setText("Si el archivo ya tiene horarios escritos, llene los dos primeros cuadros de texto y haga click en este botón:");

        jButtonCrearHorariosCuatrimestre.setText("Crear horarios cuatrimestres");
        jButtonCrearHorariosCuatrimestre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCrearHorariosCuatrimestreActionPerformed(evt);
            }
        });

        jTextAreaSalidasMensajesCrearHorarios.setColumns(20);
        jTextAreaSalidasMensajesCrearHorarios.setRows(5);
        jScrollPane6.setViewportView(jTextAreaSalidasMensajesCrearHorarios);

        jCheckBoxPermitirCruceDia.setSelected(true);
        jCheckBoxPermitirCruceDia.setText("Permitir dos o más sesiones el mismo día");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel24)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel20)
                                                    .addComponent(jLabel21)
                                                    .addComponent(jLabel22))
                                                .addGap(18, 18, 18))
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(jLabel23)
                                                .addGap(28, 28, 28)))
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jCheckBoxTenerEnCuentaHorariosYaEscritos)
                                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jTextFieldOfertaEducativaCrearHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTextFieldCarpetaSalidaCrearHorario)
                                                .addComponent(jTextFieldFuenteBannerNRC, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTextFieldFormatoPasarABello, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTextFieldMallaBaseCrearHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jCheckBoxPermitirCruceDia))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                                    .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButtonCrearHorariosCuatrimestre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGap(85, 85, 85))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jTextFieldMallaBaseCrearHorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jTextFieldOfertaEducativaCrearHorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jTextFieldCarpetaSalidaCrearHorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton13)
                    .addComponent(jCheckBoxTenerEnCuentaHorariosYaEscritos))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jCheckBoxPermitirCruceDia)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel25)
                        .addGap(70, 70, 70))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton18)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonCrearHorariosCuatrimestre)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton14)
                    .addComponent(jTextFieldFuenteBannerNRC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addGap(11, 11, 11)
                .addComponent(jButton15)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton16)
                    .addComponent(jTextFieldFormatoPasarABello, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGap(18, 18, 18)
                .addComponent(jButton17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(647, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Crear Horario", jPanel6);

        jLabel36.setText("Fecha inicio semestre:");

        jLabel37.setText("Fecha final de semestre:");

        jLabel38.setText("Fecha inicio de jornada Lunes y Jueves (L y J):");

        jLabel39.setText("Fecha inicio de jornada Lunes y Martes (L y M):");

        jLabel40.setText("Fecha inicio de jornada Martes y Jueves (M y J):");

        jLabel41.setText("Fecha inicio de jornada Miércoles y Viernes (Mi y Vi):");

        jLabel42.setText("Fecha inicio de jornada Miércoles y Viernes Primero (Mi y Vi P):");

        jLabel43.setText("Fecha inicio de jornada Sábado D 1  (SÁBADO D):");

        jLabel44.setText("Fecha inicio de jornada Sábado D 2  (SÁBADO D2):");

        jLabel45.setText("Fecha inicio de jornada Sábado T 1  (SÁBADO T):");

        jLabel46.setText("Fecha inicio de jornada Sábado T 2  (SÁBADO T2):");

        jLabel26.setText("Fechas prohibidas:");

        jListFechasProhibidas.setToolTipText("");
        jScrollPane5.setViewportView(jListFechasProhibidas);

        jButton19.setText("Agregar");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton20.setText("Quitar");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36)
                            .addComponent(jLabel37)
                            .addComponent(jLabel38)
                            .addComponent(jLabel39)
                            .addComponent(jLabel40)
                            .addComponent(jLabel41))
                        .addGap(52, 52, 52)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jDateChooserInicioSemestre, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                            .addComponent(jDateChooserFinalSemestre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooserInicioLyM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooserInicioLyJ, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooserInicioMyJ, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooserInicioMiyVi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel42)
                            .addComponent(jLabel43)
                            .addComponent(jLabel44)
                            .addComponent(jLabel45)
                            .addComponent(jLabel46))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooserMiyViP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooserInicioSabadoD1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooserInicioSabadoD2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooserInicioSabadoT1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooserInicioSabadoT2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(52, 52, 52)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jDateChooserFechasProhibidas, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton19))
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooserMiyViP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jDateChooserInicioSemestre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel36))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jDateChooserFinalSemestre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel37))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jDateChooserInicioLyM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jDateChooserInicioLyJ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel38))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jDateChooserInicioMyJ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel40))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jDateChooserInicioMiyVi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel41)))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton19)
                                    .addComponent(jDateChooserFechasProhibidas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(14, 14, 14)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton20)))
                            .addComponent(jLabel26))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel42)
                        .addGap(3, 3, 3)))
                .addGap(12, 12, 12)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(jDateChooserInicioSabadoD1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooserInicioSabadoD2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooserInicioSabadoT1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel46)
                    .addComponent(jDateChooserInicioSabadoT2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(850, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Configuración de fechas", jPanel8);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(0, 0, 842, 1296);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:

    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        guardarVariablesTextField();
        System.out.println("Guardando");
    }//GEN-LAST:event_formWindowClosing

    private void jButtonCrearHorariosCuatrimestreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCrearHorariosCuatrimestreActionPerformed
        // TODO add your handling code here:
        new ProgramarCuatrimestre("E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\SalidaOfertaEducativa\\Horarios 2018-1 Nrc Llenos Gustavo.xlsx",
                "E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\Bases de datos3.xlsx"
        );
    }//GEN-LAST:event_jButtonCrearHorariosCuatrimestreActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        new RevisionHorariosEscritosLibro(jTextFieldOfertaEducativaCrearHorario.getText(),
                jTextFieldMallaBaseCrearHorario.getText()
        );
        //        new RevisionHorariosEscritosLibro("E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\SalidaOfertaEducativa\\Horarios 2018-1 Nrc Llenos Gustavo.xlsx",
        //                "E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\Bases de datos3.xlsx"
        //        );
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jCheckBoxTenerEnCuentaHorariosYaEscritosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxTenerEnCuentaHorariosYaEscritosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxTenerEnCuentaHorariosYaEscritosActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:

        guardarVariablesTextField();

        new PasarABelloCuatrimestre(
                jTextFieldMallaBaseCrearHorario.getText(),
                jTextFieldOfertaEducativaCrearHorario.getText(),
                jTextFieldFormatoPasarABello.getText(),
                jTextFieldCarpetaSalidaCrearHorario.getText()
        );

        //        new PasarABelloCuatrimestre("E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\Bases de datos3.xlsx",
        //                "E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\SalidaOfertaEducativa\\Horarios 2018-1 Nrc Llenos Gustavo.xlsx",
        //                "E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\PEREIRA.xlsx",
        //                "E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\SalidaOfertaEducativa\\");
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:

        guardarVariablesTextField();

        new ReciclarNRC(jTextFieldOfertaEducativaCrearHorario.getText(),
                jTextFieldFuenteBannerNRC.getText(),
                jTextFieldCarpetaSalidaCrearHorario.getText());
        //        new ReciclarNRC("E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\SalidaOfertaEducativa\\Oferta 2018-1 Docentes 2.xlsx",
        //                "E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\Fuentes Banner NRC 2018-1.xlsx",
        //                "E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\SalidaOfertaEducativa\\");
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        JFileChooser selector = new JFileChooser("E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\");

        int returnVal = selector.showOpenDialog(this.rootPane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String rutaEntrada = selector.getSelectedFile().getAbsolutePath();
            jTextFieldFuenteBannerNRC.setText(rutaEntrada);
            System.out.println("You chose to open this file: "
                    + selector.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:

        javax.swing.JOptionPane.showMessageDialog(rootPane, "Se comenzarán a crear los horarios");
        guardarVariablesTextField();
        
        
        new CrearHorario(jTextFieldMallaBaseCrearHorario.getText(), 
                jTextFieldOfertaEducativaCrearHorario.getText(),
                jTextFieldCarpetaSalidaCrearHorario.getText()
        );
        

//        String fechas[] = new String[11];
//        fechas[0] = CG.FECHAINICIOSEMESTRE;
//        fechas[1] = CG.FECHAFINALSEMESTRE;
//        fechas[2] = CG.FECHAINICIOLYJ;
//        fechas[3] = CG.FECHAINICIOLYM;
//        fechas[4] = CG.FECHAINICIOMYJ;
//        fechas[5] = CG.FECHAINICIOMIYVIP;
//        fechas[6] = CG.FECHAINICIOMIYVI;
//        fechas[7] = CG.FECHAINICIOSABADOD;
//        fechas[8] = CG.FECHAINICIOSABADOD2;
//        fechas[9] = CG.FECHAINICIOSABADOT;
//        fechas[10] = CG.FECHAINICIOSABADOT2;
//
//        ArrayList<String> fechasProhibidas = null;
//        fechasProhibidas = new ArrayList<String>(CG.FECHASPROHIBIDAS);
//
//        jTextAreaSalidasMensajesCrearHorarios.setText("Se validará la coherencia de los alfa numérico de la oferta vs la malla curricular base");
//
//        CreacionHorarios ch = new CreacionHorarios(jTextFieldOfertaEducativaCrearHorario.getText(),
//                jTextFieldMallaBaseCrearHorario.getText(),
//                jCheckBoxTenerEnCuentaHorariosYaEscritos.isSelected(),
//                jCheckBoxPermitirCruceDia.isSelected(),
//                fechas,
//                fechasProhibidas);
//        if (ch.erroresValidacionOfertaMalla.size() == 0) {
//            jTextAreaSalidasMensajesCrearHorarios.setText(jTextAreaSalidasMensajesCrearHorarios.getText() + "\n validación de los alfa numérico de la oferta vs la malla curricular base correcta");
//        } else {
//            jTextAreaSalidasMensajesCrearHorarios.setText(jTextAreaSalidasMensajesCrearHorarios.getText() + "\n Se presentaron los siguientes errores de validación: ");
//            for (String me : ch.erroresValidacionOfertaMalla) {
//                jTextAreaSalidasMensajesCrearHorarios.setText(jTextAreaSalidasMensajesCrearHorarios.getText() + "\n" + me);
//            }
//        }
        //        new CreacionHorarios("E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\SalidaOfertaEducativa\\Horarios 2018-1 Nrc Llenos Gustavo.xlsx",
        //                "E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\Bases de datos3.xlsx",
        //                jCheckBoxTenerEnCuentaHorariosYaEscritos.isSelected()
        //        );
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        JFileChooser selector = new JFileChooser(jTextFieldCarpetaSalidaCrearHorario.getText());

        selector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = selector.showOpenDialog(this.rootPane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String rutaEntrada = selector.getSelectedFile().getAbsolutePath();
            jTextFieldCarpetaSalidaCrearHorario.setText(rutaEntrada);
            System.out.println("You chose to open this file: "
                    + selector.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jTextFieldCarpetaSalidaCrearHorarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCarpetaSalidaCrearHorarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldCarpetaSalidaCrearHorarioActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        JFileChooser selector = new JFileChooser(jTextFieldOfertaEducativaCrearHorario.getText());

        int returnVal = selector.showOpenDialog(this.rootPane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String rutaEntrada = selector.getSelectedFile().getAbsolutePath();
            jTextFieldOfertaEducativaCrearHorario.setText(rutaEntrada);
            System.out.println("You chose to open this file: "
                    + selector.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        JFileChooser selector = new JFileChooser(jTextFieldMallaBaseCrearHorario.getText());

        int returnVal = selector.showOpenDialog(this.rootPane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String rutaEntrada = selector.getSelectedFile().getAbsolutePath();
            jTextFieldMallaBaseCrearHorario.setText(rutaEntrada);
            System.out.println("You chose to open this file: "
                    + selector.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void botonEjecutarRecomendacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEjecutarRecomendacionesActionPerformed
        // TODO add your handling code here:
        guardarVariablesTextField();
        ProyeccionOfertaEducativa.ejecutarRecomendacionesFusion(jTextFieldOfertaACruzar.getText(), jTextAreaObservacionesProyeccion);
    }//GEN-LAST:event_botonEjecutarRecomendacionesActionPerformed

    private void abrirOfertaCruzarCompartirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abrirOfertaCruzarCompartirActionPerformed
        // TODO add your handling code here:
        JFileChooser selector = new JFileChooser("E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\SalidaOfertaEducativa\\");

        int returnVal = selector.showOpenDialog(this.rootPane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String rutaEntrada = selector.getSelectedFile().getAbsolutePath();
            jTextFieldOfertaACruzar.setText(rutaEntrada);
            System.out.println("You chose to open this file: "
                    + selector.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_abrirOfertaCruzarCompartirActionPerformed

    private void abrirFileChooserCarpetaSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abrirFileChooserCarpetaSalidaActionPerformed
        // TODO add your handling code here:
        selectorArchivoGeneral = new JFileChooser(jTextFieldCarpetaSalida2.getText());
        selectorArchivoGeneral.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        //        FileNameExtensionFilter filtroExcel = new FileNameExtensionFilter("Archivos de Excel", "xls", "xlsx");
        //        selectorArchivoGeneral.setFileFilter(filtroExcel);
        int r = selectorArchivoGeneral.showOpenDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            jTextFieldCarpetaSalida2.setText(selectorArchivoGeneral.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_abrirFileChooserCarpetaSalidaActionPerformed

    private void botonCrearProyeccionOfertaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearProyeccionOfertaActionPerformed
        // TODO add your handling code here:
        jTextAreaObservacionesProyeccion.setText(new Date().toString() + " Se comenzará a crear la oferta educativa \n");

        guardarVariablesTextField();

        try {
            new Thread() {
                public void run() {
                    ofertaEducativa = new ProyeccionOfertaEducativa(jTextFieldMallaBase.getText(),
                            jTextFieldSeguimientos.getText(),
                            jTextFieldCupos.getText(),
                            jTextFieldPrimerSemestre.getText(),
                            jTextFieldCarpetaSalida2.getText(),
                            jTextPeriodoSiguiente.getText(),
                            jTextPeriodoActual.getText()
                    );
                    String texto = jTextAreaObservacionesProyeccion.getText();
                    jTextAreaObservacionesProyeccion.setText(texto + " " + new Date().toString() + "Se ha terminado de crear la oferta educativa \n "
                            + ofertaEducativa.MENSAJE_ERROR_INEXISTENCIA_HOJA_SEGUIMIENTOS);
                }
            }.start();
        } catch (Exception e) {
            Validaciones.mostrarErroresTotal(" Se ha presentado un error al crear la oferta educativa total ", e);
        }

        //        ofertaEducativa = new ProyeccionOfertaEducativa("E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\Bases de datos3.xlsx",
        //                "E:\\Uniminuto 2018 1\\Proyecto Horario\\Salidas Seguimientos\\seguimientoLlenosMayo3de2018 v2.xlsx",
        //                "E:\\Uniminuto 2018 1\\Proyecto Horario\\Salidas Seguimientos\\estimadoCuposGrupo170418064739.xlsx",
        //                "E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\proyeccionprimersemestre2.xlsx",
        //                "E:\\Uniminuto 2018 1\\Proyecto Horario\\Oferta Educativa 2018 2\\",
        //                "2018-2");
    }//GEN-LAST:event_botonCrearProyeccionOfertaActionPerformed

    private void jTextPeriodoSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextPeriodoSiguienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextPeriodoSiguienteActionPerformed

    private void botonAbrirFileChooserMetaPrimerSemestreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAbrirFileChooserMetaPrimerSemestreActionPerformed
        // TODO add your handling code here:
        selectorArchivoGeneral = new JFileChooser(jTextFieldPrimerSemestre.getText());

        FileNameExtensionFilter filtroExcel = new FileNameExtensionFilter("Archivos de Excel", "xls", "xlsx");
        selectorArchivoGeneral.setFileFilter(filtroExcel);

        int r = selectorArchivoGeneral.showOpenDialog(this);

        if (r == JFileChooser.APPROVE_OPTION) {
            jTextFieldPrimerSemestre.setText(selectorArchivoGeneral.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_botonAbrirFileChooserMetaPrimerSemestreActionPerformed

    private void botonAbrirFileChooserCuposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAbrirFileChooserCuposActionPerformed
        // TODO add your handling code here:
        selectorArchivoGeneral = new JFileChooser(jTextFieldCupos.getText());

        FileNameExtensionFilter filtroExcel = new FileNameExtensionFilter("Archivos de Excel", "xls", "xlsx");
        selectorArchivoGeneral.setFileFilter(filtroExcel);

        int r = selectorArchivoGeneral.showOpenDialog(this);

        if (r == JFileChooser.APPROVE_OPTION) {
            jTextFieldCupos.setText(selectorArchivoGeneral.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_botonAbrirFileChooserCuposActionPerformed

    private void botonAbrirFileChooserSeguimientos2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAbrirFileChooserSeguimientos2ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        selectorArchivoGeneral = new JFileChooser(jTextFieldSeguimientos.getText());

        FileNameExtensionFilter filtroExcel = new FileNameExtensionFilter("Archivos de Excel", "xls", "xlsx");
        selectorArchivoGeneral.setFileFilter(filtroExcel);

        int r = selectorArchivoGeneral.showOpenDialog(this);

        if (r == JFileChooser.APPROVE_OPTION) {
            jTextFieldSeguimientos.setText(selectorArchivoGeneral.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_botonAbrirFileChooserSeguimientos2ActionPerformed

    private void botonAbrirFileChooserMalla2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAbrirFileChooserMalla2ActionPerformed
        // TODO add your handling code here:
        selectorArchivoGeneral = new JFileChooser(jTextFieldMallaBase.getText());

        FileNameExtensionFilter filtroExcel = new FileNameExtensionFilter("Archivos de Excel", "xls", "xlsx");
        selectorArchivoGeneral.setFileFilter(filtroExcel);

        int r = selectorArchivoGeneral.showOpenDialog(this);

        if (r == JFileChooser.APPROVE_OPTION) {
            jTextFieldMallaBase.setText(selectorArchivoGeneral.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_botonAbrirFileChooserMalla2ActionPerformed

    private void jTextPeriodoActualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextPeriodoActualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextPeriodoActualActionPerformed

    private void botonAbrirFileChooserMatriculaNRCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAbrirFileChooserMatriculaNRCActionPerformed
        // TODO add your handling code here:
        selectorArchivoGeneral = new JFileChooser(jTextFieldMatriculaNRC.getText());

        FileNameExtensionFilter filtroExcel = new FileNameExtensionFilter("Archivos de Excel", "xls", "xlsx");
        selectorArchivoGeneral.setFileFilter(filtroExcel);

        int r = selectorArchivoGeneral.showOpenDialog(this);

        if (r == JFileChooser.APPROVE_OPTION) {
            jTextFieldMatriculaNRC.setText(selectorArchivoGeneral.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_botonAbrirFileChooserMatriculaNRCActionPerformed

    private void botonCrearArchivoSeguimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearArchivoSeguimientosActionPerformed
        // TODO add your handling code here:
        guardarVariablesTextField();

        new CrearSeguimientos(jTextFieldMallaCurricular.getText(),
                jTextFieldMatriculaNRC.getText(),
                jTextFieldOfertaEducativaActual.getText(),
                jTextFieldCarpetaSalida.getText(),
                jTextPeriodoActual.getText());
//        new CrearSeguimientos("E:\\Uniminuto 2017 2\\Proyecto Horario\\ArchivosEnsayoCrearSeguimientos\\Bases de datos3.xlsx",
//                "E:\\Uniminuto 2018 1\\Proyecto Horario\\Archivo de matriculas por NRC\\Matriculas por NRC 2018 1.xlsx",
//                "E:\\Uniminuto 2018 1\\Proyecto Horario\\Oferta Educativa 2018 1\\Horarios 2018-1 Nrc Llenos Sin Cuatrimestrales.xlsx",
//                "E:\\Uniminuto 2018 1\\Proyecto Horario\\Salidas Seguimientos",
//                "2018-1");
    }//GEN-LAST:event_botonCrearArchivoSeguimientosActionPerformed

    private void botonAbrirFileChooserSeguimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAbrirFileChooserSeguimientosActionPerformed
        // TODO add your handling code here:
        selectorArchivoGeneral = new JFileChooser(jTextFieldCarpetaSalida.getText());
        selectorArchivoGeneral.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        //        FileNameExtensionFilter filtroExcel = new FileNameExtensionFilter("Archivos de Excel", "xls", "xlsx");
        //        selectorArchivoGeneral.setFileFilter(filtroExcel);
        int r = selectorArchivoGeneral.showOpenDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            jTextFieldCarpetaSalida.setText(selectorArchivoGeneral.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_botonAbrirFileChooserSeguimientosActionPerformed

    private void botonAbrirFileChooserOfertaActualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAbrirFileChooserOfertaActualActionPerformed
        // TODO add your handling code here:
        selectorArchivoGeneral = new JFileChooser(jTextFieldOfertaEducativaActual.getText());

        FileNameExtensionFilter filtroExcel = new FileNameExtensionFilter("Archivos de Excel", "xls", "xlsx");
        selectorArchivoGeneral.setFileFilter(filtroExcel);

        int r = selectorArchivoGeneral.showOpenDialog(this);

        if (r == JFileChooser.APPROVE_OPTION) {
            jTextFieldOfertaEducativaActual.setText(selectorArchivoGeneral.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_botonAbrirFileChooserOfertaActualActionPerformed

    private void botonAbrirFileChooserMallaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAbrirFileChooserMallaActionPerformed
        selectorArchivoGeneral = new JFileChooser(jTextFieldMallaCurricular.getText());

        FileNameExtensionFilter filtroExcel = new FileNameExtensionFilter("Archivos de Excel", "xls", "xlsx");
        selectorArchivoGeneral.setFileFilter(filtroExcel);

        int r = selectorArchivoGeneral.showOpenDialog(this);

        if (r == JFileChooser.APPROVE_OPTION) {
            jTextFieldMallaCurricular.setText(selectorArchivoGeneral.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_botonAbrirFileChooserMallaActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:

        guardarVariablesTextField();

        new ConteoCortes(tfRutaArchivoEntrada1.getText(),
                2,
                jDateChooser1.getDate(),
                jRadioButton2.isSelected()).
                crearArchivoSalida(tfCarpetaSalida1.getText(), "conteoClases");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        selectorCarpetaSalida = new JFileChooser(tfCarpetaSalida1.getText());

        selectorCarpetaSalida.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        File directorioEntrada = new File(Ventana.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String subcadenaPath = directorioEntrada.getAbsolutePath().toString().substring(0, directorioEntrada.getAbsolutePath().toString().lastIndexOf(File.separator));

        selectorCarpetaSalida.setCurrentDirectory(new File(subcadenaPath + "/SalidaConteoCortes"));
        //        FileNameExtensionFilter filter = new FileNameExtensionFilter(
        //                "Archivos de Excel", "xls", "xlsx");
        //        selectorCarpetaSalida.setFileFilter(filter);
        int returnVal = selectorCarpetaSalida.showOpenDialog(this.rootPane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String rutaEntrada = selectorCarpetaSalida.getSelectedFile().getAbsolutePath();
            tfCarpetaSalida1.setText(rutaEntrada);
            areaTextoConteoCortes.setText(areaTextoConteoCortes.getText() + " \n" + "Se ha seleccionado la carpeta destino " + rutaEntrada);
            //            try {
            //                cargarGrupos(rutaEntrada);
            //            } catch (IOException ex) {
            //                Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
            //            }
            System.out.println("You chose to open this file: "
                    + selectorCarpetaSalida.getSelectedFile().getAbsolutePath());
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        selectorArchivoEntrada = new JFileChooser(tfRutaArchivoEntrada1.getText());
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Archivos de Excel", "xls", "xlsx");
        selectorArchivoEntrada.setFileFilter(filter);
        //por ahora para el archivo por defecto al abrir el file chooser
        //es "./dist"

        File directorioEntrada = new File(Ventana.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String subcadenaPath = directorioEntrada.getAbsolutePath().toString().substring(0, directorioEntrada.getAbsolutePath().toString().lastIndexOf(File.separator));
        String subcadenaPathCompleta = directorioEntrada.getAbsolutePath().toString();
        System.out.println("subcadenaPath: " + subcadenaPath + "/EntradaHorarios" + " subcadenaPathCompleta" + subcadenaPathCompleta);

        selectorArchivoEntrada.setCurrentDirectory(new File(subcadenaPath + "/EntradaHorarios"));
        areaTextoConteoCortes.setText(areaTextoConteoCortes.getText() + "Abriendo el archivo de entrada " + subcadenaPath);
        //        areaTextoConteoCortes.setText(areaTextoConteoCortes.getText()+" Abriendo el archivo raiz: "+new File(ARCHIVO_RAIZ_APLICACION+"/Entrada Horarios").getAbsolutePath()+"\n"+Ventana.class.getProtectionDomain().getCodeSource().getLocation().toString());
        int returnVal = selectorArchivoEntrada.showOpenDialog(this.rootPane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String rutaEntrada = selectorArchivoEntrada.getSelectedFile().getAbsolutePath();
            tfRutaArchivoEntrada1.setText(rutaEntrada);
            areaTextoConteoCortes.setText(areaTextoConteoCortes.getText() + "Se ha seleccionado el archivo " + rutaEntrada);
            try {
                cargarGrupos(rutaEntrada);
                areaTextoConteoCortes.setText(areaTextoConteoCortes.getText() + "\n" + "Se cargó el archivo. " + rutaEntrada);
            } catch (IOException ex) {
                areaTextoConteoCortes.setText(areaTextoConteoCortes.getText() + "\n" + "Ocurrió un error al cargar el archivo: \n" + ex.getMessage());
                Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("You chose to open this file: "
                    + selectorArchivoEntrada.getSelectedFile().getAbsolutePath());
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void tfCarpetaSalida1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfCarpetaSalida1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfCarpetaSalida1ActionPerformed

    private void tfRutaArchivoEntrada1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfRutaArchivoEntrada1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfRutaArchivoEntrada1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

        guardarVariablesTextField();

        int[] indicesSeleccionados;
        indicesSeleccionados = listListadoDocentes.getSelectedIndices();
        for (int i = 0; i < indicesSeleccionados.length; i++) {
            pdfs.crearHorarioDocentes(tfCarpetaSalida.getText(), listListadoDocentes.getSelectedValuesList().get(i), "Horario", indicesSeleccionados[i]);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        if (jRadioButton3.isSelected()) {
            int tamanoListadoGrupos = listListadoDocentes.getModel().getSize();
            int[] totalIndicesListadoGrupos = new int[tamanoListadoGrupos];
            System.out.println("Cantidad en el listado de docentes" + tamanoListadoGrupos);
            for (int i = 0; i < tamanoListadoGrupos; i++) {
                totalIndicesListadoGrupos[i] = i;
            }

            for (int i = 0; i < tamanoListadoGrupos; i++) {
                listListadoDocentes.setSelectedIndices(totalIndicesListadoGrupos);
                //                listListadoGrupos.setSelectedIndex(i);
            }

            System.out.println("Está seleccionado");
        } else {
            int[] totalIndicesListadoGrupos = new int[0];
            listListadoDocentes.setSelectedIndices(totalIndicesListadoGrupos);
            System.out.println("No está seleccionado");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        // TODO add your handling code here:
        selectorCarpetaSalida = new JFileChooser(tfCarpetaSalida2.getText());
        selectorCarpetaSalida.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //        FileNameExtensionFilter filter = new FileNameExtensionFilter(
        //                "Archivos de Excel", "xls", "xlsx");
        //        selectorCarpetaSalida.setFileFilter(filter);
        int returnVal = selectorCarpetaSalida.showOpenDialog(this.rootPane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String rutaEntrada = selectorCarpetaSalida.getSelectedFile().getAbsolutePath();
            tfCarpetaSalida2.setText(rutaEntrada);
            System.out.println("You chose to open this file: "
                    + selectorCarpetaSalida.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        selectorArchivoEntradaHorariosDocentes = new JFileChooser(tfRutaArchivoEntradaHorariosDocentes.getText());
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Archivos de Excel", "xls", "xlsx");
        selectorArchivoEntradaHorariosDocentes.setFileFilter(filter);
        selectorArchivoEntradaHorariosDocentes.setCurrentDirectory(new File("D:\\Uniminuto 2016 2\\Proyecto Horario\\OFERTA 2017-2 Julio 4 cambiando fechas.xlsx"));
        int returnVal = selectorArchivoEntradaHorariosDocentes.showOpenDialog(this.rootPane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String rutaEntrada = selectorArchivoEntradaHorariosDocentes.getSelectedFile().getAbsolutePath();
            tfRutaArchivoEntradaHorariosDocentes.setText(rutaEntrada);
            try {
                cargarDocentes(rutaEntrada);
            } catch (IOException ex) {
                Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("You chose to open this file: "
                    + selectorArchivoEntradaHorariosDocentes.getSelectedFile().getAbsolutePath());
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void tfRutaArchivoEntradaHorariosDocentesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfRutaArchivoEntradaHorariosDocentesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfRutaArchivoEntradaHorariosDocentesActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        selectorCarpetaSalida = new JFileChooser(tfCarpetaSalida.getText());
        selectorCarpetaSalida.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //        FileNameExtensionFilter filter = new FileNameExtensionFilter(
        //                "Archivos de Excel", "xls", "xlsx");
        //        selectorCarpetaSalida.setFileFilter(filter);
        int returnVal = selectorCarpetaSalida.showOpenDialog(this.rootPane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String rutaEntrada = selectorCarpetaSalida.getSelectedFile().getAbsolutePath();
            tfCarpetaSalida.setText(rutaEntrada);
            System.out.println("You chose to open this file: "
                    + selectorCarpetaSalida.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        guardarVariablesTextField();

        int[] indicesSeleccionados;
        indicesSeleccionados = listListadoGrupos.getSelectedIndices();
        for (int i = 0; i < indicesSeleccionados.length; i++) {
            pdfs.crearHorarioGrupo(tfCarpetaSalida.getText(), listListadoGrupos.getSelectedValuesList().get(i), "Horario", indicesSeleccionados[i]);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
        if (jRadioButton1.isSelected()) {
            int tamanoListadoGrupos = listListadoGrupos.getModel().getSize();
            int[] totalIndicesListadoGrupos = new int[tamanoListadoGrupos];
            System.out.println("Cantidad en el listado de grupos" + tamanoListadoGrupos);
            for (int i = 0; i < tamanoListadoGrupos; i++) {
                totalIndicesListadoGrupos[i] = i;
            }

            for (int i = 0; i < tamanoListadoGrupos; i++) {
                listListadoGrupos.setSelectedIndices(totalIndicesListadoGrupos);
                //                listListadoGrupos.setSelectedIndex(i);
            }

            System.out.println("Está seleccionado");
        } else {
            int[] totalIndicesListadoGrupos = new int[0];
            listListadoGrupos.setSelectedIndices(totalIndicesListadoGrupos);
            System.out.println("No está seleccionado");
        }
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        selectorArchivoEntrada = new JFileChooser(tfRutaArchivoEntrada.getText());
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Archivos de Excel", "xls", "xlsx");
        selectorArchivoEntrada.setFileFilter(filter);
        selectorArchivoEntrada.setCurrentDirectory(new File("D:\\Uniminuto 2016 2\\Proyecto Horario\\OFERTA 2017-2 Julio 4 cambiando fechas.xlsx"));
        int returnVal = selectorArchivoEntrada.showOpenDialog(this.rootPane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String rutaEntrada = selectorArchivoEntrada.getSelectedFile().getAbsolutePath();
            tfRutaArchivoEntrada.setText(rutaEntrada);
            try {
                cargarGrupos(rutaEntrada);
            } catch (IOException ex) {
                Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("You chose to open this file: "
                    + selectorArchivoEntrada.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tfRutaArchivoEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfRutaArchivoEntradaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfRutaArchivoEntradaActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
        int[] indices = jListFechasProhibidas.getSelectedIndices();
        DefaultListModel<String> listadoFP = new DefaultListModel<>();

        for (int i = 0; i < jListFechasProhibidas.getModel().getSize(); i++) {
            if (!jListFechasProhibidas.isSelectedIndex(i)) {
                listadoFP.addElement(jListFechasProhibidas.getModel().getElementAt(i));
            }
        }

        jListFechasProhibidas.setModel(listadoFP);
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
        DefaultListModel<String> listadoFP = new DefaultListModel<String>();
        for (int i = 0; i < jListFechasProhibidas.getModel().getSize(); i++) {
            listadoFP.addElement(jListFechasProhibidas.getModel().getElementAt(i));
        }
        if (!jDateChooserFechasProhibidas.getDate().toString().trim().equals("")) {
            try {

                Date fecha1 = jDateChooserFechasProhibidas.getDate();

                listadoFP.addElement(formatoDMA.format(fecha1));
                //formatoDMA.parse("13-02-2018");
                jListFechasProhibidas.setModel(listadoFP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }//GEN-LAST:event_jButton19ActionPerformed

    private void jTextFieldPrimerSemestreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPrimerSemestreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldPrimerSemestreActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                try {
                    new Ventana().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton abrirFileChooserCarpetaSalida;
    private javax.swing.JButton abrirOfertaCruzarCompartir;
    private javax.swing.JTextArea areaTextoConteoCortes;
    private javax.swing.JToggleButton botonAbrirFileChooserCupos;
    private javax.swing.JButton botonAbrirFileChooserMalla;
    private javax.swing.JButton botonAbrirFileChooserMalla2;
    private javax.swing.JButton botonAbrirFileChooserMatriculaNRC;
    private javax.swing.JButton botonAbrirFileChooserMetaPrimerSemestre;
    private javax.swing.JButton botonAbrirFileChooserOfertaActual;
    private javax.swing.JButton botonAbrirFileChooserSeguimientos;
    private javax.swing.JButton botonAbrirFileChooserSeguimientos2;
    private javax.swing.JButton botonCrearArchivoSeguimientos;
    private javax.swing.JButton botonCrearProyeccionOferta;
    private javax.swing.JButton botonEjecutarRecomendaciones;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonCrearHorariosCuatrimestre;
    private javax.swing.JCheckBox jCheckBoxPermitirCruceDia;
    private javax.swing.JCheckBox jCheckBoxTenerEnCuentaHorariosYaEscritos;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooserFechasProhibidas;
    private com.toedter.calendar.JDateChooser jDateChooserFinalSemestre;
    private com.toedter.calendar.JDateChooser jDateChooserInicioLyJ;
    private com.toedter.calendar.JDateChooser jDateChooserInicioLyM;
    private com.toedter.calendar.JDateChooser jDateChooserInicioMiyVi;
    private com.toedter.calendar.JDateChooser jDateChooserInicioMyJ;
    private com.toedter.calendar.JDateChooser jDateChooserInicioSabadoD1;
    private com.toedter.calendar.JDateChooser jDateChooserInicioSabadoD2;
    private com.toedter.calendar.JDateChooser jDateChooserInicioSabadoT1;
    private com.toedter.calendar.JDateChooser jDateChooserInicioSabadoT2;
    private com.toedter.calendar.JDateChooser jDateChooserInicioSemestre;
    private com.toedter.calendar.JDateChooser jDateChooserMiyViP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jListFechasProhibidas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextAreaObservacionesProyeccion;
    private javax.swing.JTextArea jTextAreaSalidasMensajesCrearHorarios;
    private javax.swing.JTextField jTextFieldCarpetaSalida;
    private javax.swing.JTextField jTextFieldCarpetaSalida2;
    private javax.swing.JTextField jTextFieldCarpetaSalidaCrearHorario;
    private javax.swing.JTextField jTextFieldCupos;
    private javax.swing.JTextField jTextFieldFormatoPasarABello;
    private javax.swing.JTextField jTextFieldFuenteBannerNRC;
    private javax.swing.JTextField jTextFieldMallaBase;
    private javax.swing.JTextField jTextFieldMallaBaseCrearHorario;
    private javax.swing.JTextField jTextFieldMallaCurricular;
    private javax.swing.JTextField jTextFieldMatriculaNRC;
    private javax.swing.JTextField jTextFieldOfertaACruzar;
    private javax.swing.JTextField jTextFieldOfertaEducativaActual;
    private javax.swing.JTextField jTextFieldOfertaEducativaCrearHorario;
    private javax.swing.JTextField jTextFieldPrimerSemestre;
    private javax.swing.JTextField jTextFieldSeguimientos;
    private javax.swing.JTextField jTextPeriodoActual;
    private javax.swing.JTextField jTextPeriodoSiguiente;
    private javax.swing.JList<String> listListadoDocentes;
    private javax.swing.JList<String> listListadoGrupos;
    private javax.swing.JTextField tfCarpetaSalida;
    private javax.swing.JTextField tfCarpetaSalida1;
    private javax.swing.JTextField tfCarpetaSalida2;
    private javax.swing.JTextField tfRutaArchivoEntrada;
    private javax.swing.JTextField tfRutaArchivoEntrada1;
    private javax.swing.JTextField tfRutaArchivoEntradaHorariosDocentes;
    // End of variables declaration//GEN-END:variables
}
