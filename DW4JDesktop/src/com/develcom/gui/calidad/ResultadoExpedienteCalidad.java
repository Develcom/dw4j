package com.develcom.gui.calidad;

import com.develcom.dao.DatosTabla;
import com.develcom.dao.Expediente;
import com.develcom.dao.ManejoSesion;
import com.develcom.dao.OtrosDatos;
import com.develcom.expediente.ConsultaDinamica;
import com.develcom.expediente.Indice;
import com.develcom.gui.Principal;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.gui.tools.ModeloTabla;
import com.develcom.tools.Constantes;
import com.develcom.tools.trazas.Traza;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.apache.log4j.Level;

public class ResultadoExpedienteCalidad extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = -3975522717559022979L;
    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(ResultadoExpedienteCalidad.class);
    /**
     * Resultado de las claves primarias y segunda de la consulta dinamica
     */
    private List<DatosTabla> datosTablas = new ArrayList<DatosTabla>();
    /**
     * Modelo para la tabla
     */
    private ModeloTabla modeloTabla;
    /**
     * Datos del expediente
     */
    private Expediente expe;
    /**
     * Listado del resultado de la consulta dinamica
     */
    private List<ConsultaDinamica> consultaDinamicas;
    private static List<com.develcom.calidad.ConsultaDinamica> consultaDinamicasAprobar;
    private String idExpedienteCalidad = null;

    /**
     * Constructor que se usa cuando se aprueba o se rechaza un documento
     */
    public ResultadoExpedienteCalidad() {
        initComponents();
        CentraVentanas.centrar(this, Principal.desktop);
        expe = ManejoSesion.getExpediente();

        if (Constantes.ACCION.equalsIgnoreCase("APROBAR")) {
            setTitle("Aprobar o Rechazar Documentos Digitalizados");
        } else if (Constantes.ACCION.equalsIgnoreCase("ELIMINAR")) {
            setTitle("Eliminar Tipos de Documentos Dañados");
        }
//
//        llenarDatosTabla();
//        llenarTabla();
        this.setVisible(true);
    }

    /**
     * Constructor
     *
     * @param consultaDinamicas Resultado de la consulta dinamica
     */
    public ResultadoExpedienteCalidad(List<ConsultaDinamica> consultaDinamicas) {
        this.consultaDinamicas = consultaDinamicas;
        this.expe = ManejoSesion.getExpediente();
        initComponents();
        CentraVentanas.centrar(this, Principal.desktop);

        if (Constantes.ACCION.equalsIgnoreCase("APROBAR")) {
            setTitle("Aprobar o Rechazar Documentos Digitalizados");
        } else if (Constantes.ACCION.equalsIgnoreCase("ELIMINAR")) {
            setTitle("Eliminar Tipos de Documentos Dañados");
        }

        llenarDatosTabla();
        llenarTabla();
        this.setVisible(true);
    }

    /**
     * Crea las etiquetas para las cabezera de la tabla
     *
     * @param arg Argumento con el nombre de la etiqueta
     * @return La etiqueta creada
     */
//    private String crearEtiqueta(String arg) {
//
//        arg = arg.replace("_", " ");
//        arg = arg.toLowerCase();
//        char[] cs = arg.toCharArray();
//        char ch = cs[0];
//        cs[0] = Character.toUpperCase(ch);
//        arg = String.valueOf(cs);
//
//        return arg;
//    }
    public void convertir() {

        ConsultaDinamica conDinamica;
        Indice indice;
        List<com.develcom.calidad.Indice> indices;
        boolean finder = false;
//        int totalExpediente;
//        Object idExpediente, tmp = "";

        if (consultaDinamicasAprobar != null) {

            if (idExpedienteCalidad != null) {
                for (com.develcom.calidad.ConsultaDinamica cd : consultaDinamicasAprobar) {

                    indices = cd.getIndices();
                    for (int i = 0; i < indices.size(); i++) {
                        com.develcom.calidad.Indice ind = indices.get(i);
//                    for (com.develcom.calidad.Indice ind : indices) {
                        try {
                            if (idExpedienteCalidad.equalsIgnoreCase(ind.getValor().toString())) {
                                traza.trace("removiendo el expediente " + ind.getValor().toString() + " del listado", Level.INFO);

                                indices.remove(i);
                                indices.remove(i);
                                indices.remove(i);
                                indices.remove(i);

                                consultaDinamicasAprobar.remove(cd);
                                com.develcom.calidad.ConsultaDinamica dinamica = new com.develcom.calidad.ConsultaDinamica();
                                dinamica.setExiste(true);

                                for (com.develcom.calidad.Indice in : indices) {
                                    dinamica.getIndices().add(in);
                                }
                                consultaDinamicasAprobar.add(dinamica);
                                finder = true;
                                break;
                            }
                        } catch (Exception e) {
                        }
                    }
                    if (finder) {
                        break;
                    }
                }
            }

            if (consultaDinamicas != null) {
                if (!consultaDinamicas.isEmpty()) {
                    consultaDinamicas.clear();
                }
            } else {
                consultaDinamicas = new ArrayList<ConsultaDinamica>();
            }

            for (com.develcom.calidad.ConsultaDinamica cd : consultaDinamicasAprobar) {

                conDinamica = new ConsultaDinamica();
                indices = cd.getIndices();

//                idExpediente = buscarIdExpediente(indices);
//                if(!idExpediente.equals(tmp)){
//                    tmp=idExpediente;
                for (com.develcom.calidad.Indice ind : indices) {
                    indice = new Indice();

                    indice.setIndice(ind.getIndice());
                    indice.setClave(ind.getClave());
                    indice.setTipo(ind.getTipo());
                    indice.setValor(ind.getValor());
                    conDinamica.getIndices().add(indice);
                }
                consultaDinamicas.add(conDinamica);
//                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Problemas en la convercion para aprobacion de documentos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            this.dispose();
            System.gc();

        }
//        totalExpediente = consultaDinamicas.size();
        if (!consultaDinamicas.isEmpty()) {
            llenarDatosTabla();
            llenarTabla();
//            setTitle("Resultado de la Consulta de Expedientes");
            this.setVisible(true);
        }
    }

//    private Object buscarIdExpediente(List<com.develcom.calidad.Indice> indices){
//        Object resp=null;
//        for(com.develcom.calidad.Indice ind : indices){
//            try{
//                if(ind.getClave().equalsIgnoreCase("y")){
//                    resp=ind.getValor();
//                }
//            } catch (NullPointerException e){}
//        }
//        return resp;
//    }
    /**
     * Crea el objeto con los indices primarios y segundos, del resultado de la
     * consulta dinamica
     */
    private void llenarDatosTabla() {

        traza.trace("armando el data table", Level.INFO);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        List<DatosTabla> datosTablasTmp = new ArrayList<DatosTabla>();
        DatosTabla dt = null;
        OtrosDatos od;
        List<OtrosDatos> listOtrosDatos = new ArrayList<OtrosDatos>();
        DatosTabla dtCabezeras = new DatosTabla();
        boolean sec1 = true, sec2 = true, sec3 = true, ban = true;
        int cont = 0;
        List<Indice> listaIndices;

        for (ConsultaDinamica cd : consultaDinamicas) {

            listaIndices = cd.getIndices();

            for (Indice ind : listaIndices) {

                if (ban) {
                    dt = new DatosTabla();
                    sec1 = true;
                    sec2 = true;
                    sec3 = true;
                }

                if (ind.getClave().equalsIgnoreCase("y")) {

                    if (ind.getTipo().equalsIgnoreCase("FECHA")) {

                        dtCabezeras.setClavePrimaria(ind.getIndice());
                        dt.setClavePrimaria(ind.getIndice());
                        dt.setDatoPrimario(ind.getValor().toString());

                    } else {

                        dtCabezeras.setClavePrimaria(ind.getIndice());
                        dt.setClavePrimaria(ind.getIndice());
                        dt.setDatoPrimario(ind.getValor().toString());

                    }

                    ban = false;
                    cont++;
                    traza.trace(dt.getClavePrimaria() + " - " + dt.getDatoPrimario(), Level.INFO);

                } else if (ind.getClave().equalsIgnoreCase("s")) {

                    if (sec1) {

                        if (ind.getTipo().equalsIgnoreCase("FECHA")) {
                            try {

                                dtCabezeras.setClaveSec1(ind.getIndice());
                                dt.setClaveSec1(ind.getIndice());
                                dt.setDatoSec1(ind.getValor().toString());
                            } catch (NullPointerException e) {
                                dtCabezeras.setClaveSec1(ind.getIndice());
                                dt.setClaveSec1(ind.getIndice());
                                dt.setDatoSec1("");
                            }
                        } else {

                            dtCabezeras.setClaveSec1(ind.getIndice());
                            dt.setClaveSec1(ind.getIndice());
                            dt.setDatoSec1(ind.getValor().toString());
                        }

                        sec1 = false;
                        cont++;
                        traza.trace(dt.getClaveSec1() + " - " + dt.getDatoSec1(), Level.INFO);

                    } else if (sec2) {

                        if (ind.getTipo().equalsIgnoreCase("FECHA")) {
                            try {

                                dtCabezeras.setClaveSec2(ind.getIndice());
                                dt.setClaveSec2(ind.getIndice());
                                dt.setDatoSec2(ind.getValor().toString());
                            } catch (NullPointerException e) {
                                dtCabezeras.setClaveSec2(ind.getIndice());
                                dt.setClaveSec2(ind.getIndice());
                                dt.setDatoSec2("");
                            }
                        } else {

                            dtCabezeras.setClaveSec2(ind.getIndice());
                            dt.setClaveSec2(ind.getIndice());
                            dt.setDatoSec2(ind.getValor().toString());
                        }

                        sec2 = false;
                        cont++;
                        traza.trace(dt.getClaveSec2() + " - " + dt.getDatoSec2(), Level.INFO);

                    } else if (sec3) {

                        if (ind.getTipo().equalsIgnoreCase("FECHA")) {
                            try {
//                                XMLGregorianCalendar fechaIngreso = (XMLGregorianCalendar) ind.getValor();
//                                Calendar c = fechaIngreso.toGregorianCalendar();

                                dtCabezeras.setClaveSec3(ind.getIndice());
                                dt.setClaveSec3(ind.getIndice());
                                dt.setDatoSec3(ind.getValor().toString());
                            } catch (NullPointerException e) {
                                dtCabezeras.setClaveSec3(ind.getIndice());
                                dt.setClaveSec3(ind.getIndice());
                                dt.setDatoSec3("");
                            }
                        } else {

                            dtCabezeras.setClaveSec3(ind.getIndice());
                            dt.setClaveSec3(ind.getIndice());
                            dt.setDatoSec3(ind.getValor().toString());
                        }

                        sec3 = false;
                        cont++;
                        traza.trace(dt.getClaveSec3() + " - " + dt.getDatoSec3(), Level.INFO);

                    }
                } else {
                    od = new OtrosDatos();
                    od.setNombre(ind.getIndice());
                    od.setValor(ind.getValor().toString());
                    listOtrosDatos.add(od);
                    dt.setOtrosDatos(listOtrosDatos);
                    dtCabezeras.setOtrosDatos(listOtrosDatos);
                    traza.trace("otros datos: " + od.getNombre() + " - " + od.getValor(), Level.INFO);
//                    cont++;
                }

                if (cont == 4) {
                    datosTablas.add(dt);
                    ban = true;
                    cont = 0;
                }
            }
        }

        for (DatosTabla dt1 : datosTablas) {

            traza.trace("clave primaria " + dt1.getClavePrimaria() + " dato " + dt1.getDatoPrimario(), Level.INFO);
            traza.trace("clave Sec1 " + dt1.getClaveSec1() + " dato " + dt1.getDatoSec1(), Level.INFO);
            traza.trace("clave Sec2 " + dt1.getClaveSec2() + " dato " + dt1.getDatoSec2(), Level.INFO);
            traza.trace("clave Sec3 " + dt1.getClaveSec3() + " dato " + dt1.getDatoSec3(), Level.INFO);
        }

        crearCabezeras(dtCabezeras);
    }

    /**
     * Crea la cabezera de la tabla resultado consulta dinamica
     *
     * @param dt Las etiquetas con los nombres de las cabezeras
     */
    private void crearCabezeras(DatosTabla dt) {
        int cont = 0;
        modeloTabla = new ModeloTabla();

        if (dt.getOtrosDatos() == null) {
            modeloTabla.addColumn(dt.getClavePrimaria());
            modeloTabla.addColumn(dt.getClaveSec1());
            modeloTabla.addColumn(dt.getClaveSec2());
            modeloTabla.addColumn(dt.getClaveSec3());
            //modeloTabla.addColumn("SubCategoria");
        } else {
            try {
                if (!dt.getClavePrimaria().equalsIgnoreCase("") || dt.getClavePrimaria() != null) {
                    modeloTabla.addColumn(dt.getClavePrimaria());
                    cont++;
                }
            } catch (NullPointerException e) {
            }

            try {
                if (!dt.getClaveSec1().equalsIgnoreCase("") || dt.getClaveSec1() != null) {
                    modeloTabla.addColumn(dt.getClaveSec1());
                    cont++;
                }
            } catch (NullPointerException e) {
            }

            try {
                if (!dt.getClaveSec2().equalsIgnoreCase("") || dt.getClaveSec2() != null) {
                    modeloTabla.addColumn(dt.getClaveSec2());
                    cont++;
                }
            } catch (NullPointerException e) {
            }

            try {
                if (!dt.getClaveSec3().equalsIgnoreCase("") || dt.getClaveSec3() != null) {
                    modeloTabla.addColumn(dt.getClaveSec3());
                    cont++;
                }
            } catch (NullPointerException e) {
            }

            if (cont != 4) {
                for (OtrosDatos od : dt.getOtrosDatos()) {
                    modeloTabla.addColumn(od.getNombre());
                    cont++;
                    if (cont == 4) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Crea la tabla con los datos de la consulta dinamica
     */
    private void llenarTabla() {

        Vector filas;
        //int cont=1;

        for (DatosTabla dt : datosTablas) {
            int cont = 0;
            filas = new Vector();

            if (dt.getOtrosDatos() == null) {
                filas.add(dt.getDatoPrimario());
                filas.add(dt.getDatoSec1());
                filas.add(dt.getDatoSec2());
                filas.add(dt.getDatoSec3());
                //filas.add(dt.getSubCategoria());
                modeloTabla.addRow(filas);
            } else {
                try {
                    if (!dt.getDatoPrimario().equalsIgnoreCase("") || dt.getDatoPrimario() != null) {
                        filas.add(dt.getDatoPrimario());
                        cont++;
                    }
                } catch (NullPointerException e) {
                }

                try {
                    if (!dt.getDatoSec1().equalsIgnoreCase("") || dt.getDatoSec1() != null) {
                        filas.add(dt.getDatoSec1());
                        cont++;
                    }
                } catch (NullPointerException e) {
                }

                try {
                    if (!dt.getDatoSec2().equalsIgnoreCase("") || dt.getDatoSec2() != null) {
                        filas.add(dt.getDatoSec2());
                        cont++;
                    }
                } catch (NullPointerException e) {
                }

                try {
                    if (!dt.getDatoSec3().equalsIgnoreCase("") || dt.getDatoSec3() != null) {
                        filas.add(dt.getDatoSec3());
                        cont++;
                    }
                } catch (NullPointerException e) {
                }

                if (cont != 4) {
                    for (OtrosDatos od : dt.getOtrosDatos()) {

                        filas.add(od.getValor());
                        cont++;
                        if (cont == 4) {
                            break;
                        }

                    }
                }
                modeloTabla.addRow(filas);
            }
        }

        jtResultadoConsulta.setModel(modeloTabla);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelResultado = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtResultadoConsulta = new javax.swing.JTable();
        jButtonRegresar = new javax.swing.JButton();

        setBackground(new java.awt.Color(224, 239, 255));

        panelResultado.setBackground(new java.awt.Color(224, 239, 255));
        panelResultado.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccione el Expediente"));

        jtResultadoConsulta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jtResultadoConsulta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtResultadoConsultaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtResultadoConsulta);

        javax.swing.GroupLayout panelResultadoLayout = new javax.swing.GroupLayout(panelResultado);
        panelResultado.setLayout(panelResultadoLayout);
        panelResultadoLayout.setHorizontalGroup(
            panelResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelResultadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 829, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelResultadoLayout.setVerticalGroup(
            panelResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelResultadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButtonRegresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Cancel.png"))); // NOI18N
        jButtonRegresar.setMnemonic('c');
        jButtonRegresar.setText("Cancelar");
        jButtonRegresar.setToolTipText("Cancelar");
        jButtonRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegresarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelResultado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(383, 383, 383)
                .addComponent(jButtonRegresar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelResultado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jButtonRegresar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Regresa al frame anterior
     *
     * @param evt
     */
    private void jButtonRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegresarActionPerformed

        BuscaExpediente be = new BuscaExpediente();
        Principal.desktop.add(be);
        dispose();
}//GEN-LAST:event_jButtonRegresarActionPerformed

    /**
     * Muestra el arbol con los tipos de documentos digitalizados y el valor de
     * los indices
     *
     * @param evt
     */
    private void jtResultadoConsultaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtResultadoConsultaMouseClicked

        String idExpediente;

        idExpediente = ((String) jtResultadoConsulta.getValueAt(jtResultadoConsulta.getSelectedRow(), 0));
        traza.trace("selecciono el expediente " + idExpediente, Level.INFO);
        expe.setIdExpediente(idExpediente);
        ManejoSesion.setExpediente(expe);

        CalidadDocumento documento = new CalidadDocumento(idExpediente);
        Principal.desktop.add(documento);
        this.dispose();

}//GEN-LAST:event_jtResultadoConsultaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRegresar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtResultadoConsulta;
    private javax.swing.JPanel panelResultado;
    // End of variables declaration//GEN-END:variables

    public void setConsultaDinamicasAprobar(List<com.develcom.calidad.ConsultaDinamica> consultaDinamicasAprobar) {
        ResultadoExpedienteCalidad.consultaDinamicasAprobar = consultaDinamicasAprobar;
    }

    public void setIdExpedienteCalidad(String idExpedienteCalidad) {
        this.idExpedienteCalidad = idExpedienteCalidad;
    }
}
