/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.Resouces;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelo.PersonaJpaController;
import proyecto_producto.ManageFactory;
import vista.interna.ViewPersona;
import modelo.Persona;
import modelo.exceptions.NonexistentEntityException;

/**
 *
 * @author Shalva
 */
public class ControllerPersona {

    ViewPersona vista;
    ManageFactory manage;
    PersonaJpaController modeloPersona;
    Persona persona;
    JDesktopPane panelEscritorio;
    ModeloTablaPersona ModeloTabla;
    ListSelectionModel listaPersonaModel;

    public ControllerPersona(ViewPersona vista, ManageFactory manage, PersonaJpaController modeloPersona, JDesktopPane panelEscritorio) {

        this.manage = manage;
        this.modeloPersona = modeloPersona;
        this.panelEscritorio = panelEscritorio;
        this.ModeloTabla = new ModeloTablaPersona();
        this.ModeloTabla.setFilas(modeloPersona.findPersonaEntities());

        if (ControllerAdministrador.vp == null) {
            ControllerAdministrador.vp = new ViewPersona();
            this.vista = ControllerAdministrador.vp;
            this.vista.getjTable1().setModel(ModeloTabla);
            this.panelEscritorio.add(this.vista);
            ControllerAdministrador.vp.show();
            //Para centar la vista en la ventana
            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vista.getSize();
            this.vista.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);

        } else {
            ControllerAdministrador.vp.show();
        }
        controlEvento();
    }

    public void controlEvento() {
//        this.vista.getBtnCrear().addActionListener(l -> guardarPersona());
//        this.vista.getBtnEditar().addActionListener(l -> editarPersona());
//        this.vista.getBtnEliminar().addActionListener(l -> eliminarPersona());
//        this.vista.getBtnLimpiar1().addActionListener(l -> limpiar());
//        this.vista.getBtnLimpiar().addActionListener(l -> limpiarbuscador());
//        this.vista.getBtnBuscar().addActionListener(l -> buscarpersona());
//        this.vista.getjCheckMostrar().addActionListener(l -> buscarpersona());
//        this.vista.getjTable1().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//
//        listaPersonaModel = this.vista.getjTable1().getSelectionModel();
//        listaPersonaModel.addListSelectionListener(new ListSelectionListener() {
//            public void valueChanged(ListSelectionEvent e) {
//                if (!e.getValueIsAdjusting()) {
//                    personaSeleccionada();
//                }
//            }
//
//        });
        this.vista.getBtnCrear().addActionListener(l -> guardarPersona());
        this.vista.getBtnEditar().addActionListener(l -> editarPersona());
        this.vista.getBtnEliminar().addActionListener(l -> eliminarPersona());
        this.vista.getBtnReportes().addActionListener(l -> reporteGeneral());
        this.vista.getBtnImprimir().addActionListener(l -> reporteIndividual());
        this.vista.getjTable1().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPersonaModel = this.vista.getjTable1().getSelectionModel();
        listaPersonaModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    personaSeleccionada();
                }
            }

        });
        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnLimpiar1().addActionListener(l -> limpiar());
        this.vista.getBtnLimpiar().addActionListener(l -> limpiarbuscador());
        this.vista.getBtnBuscar().addActionListener(l -> buscarpersona());
        this.vista.getjCheckMostrar().addActionListener(l -> buscarpersona());

//control botones
        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnEditar().setEnabled(false);
    }

//    public void cargarCombobox() {
//        try {
//            Vector v = new Vector();
//            v.addAll(new PersonaJpaController(manager.getEntityManagerFactory()).findPersonaEntities());
//            this.vista.getjComboPersonas().setModel(new DefaultComboBoxModel(v));
//        } catch (ArrayIndexOutOfBoundsException e) {
//            System.out.println("Capturando errores cargarCombobox()");
//        }
//    }
    public void guardarPersona() {
        persona = new Persona();
        persona.setNombre(this.vista.getTxtNombre().getText());
        persona.setApellido(this.vista.getTxtApellido().getText());
        persona.setCedula(this.vista.getTxtCedula().getText());
        persona.setCelular(this.vista.getTxtCelular().getText());
        persona.setCorreo(this.vista.getTxtCorreo().getText());
        persona.setDireccion(this.vista.getTxtDireccion().getText());
        modeloPersona.create(persona);
        ModeloTabla.agregar(persona);
        Resouces.success("Atención!!", "Persona creada Correctamente");
        limpiar();
    }

    public void editarPersona() {
        if (persona != null) {
            persona.setNombre(this.vista.getTxtNombre().getText());
            persona.setApellido(this.vista.getTxtApellido().getText());
            persona.setCelular(this.vista.getTxtCelular().getText());
            persona.setCorreo(this.vista.getTxtCorreo().getText());
            persona.setDireccion(this.vista.getTxtDireccion().getText());
            try {
                modeloPersona.edit(persona);
                ModeloTabla.eliminar(persona);
                ModeloTabla.actualizar(persona);
            } catch (Exception e) {
                java.util.logging.Logger.getLogger(ControllerPersona.class.getName()).log(Level.SEVERE, null, e);
            }

            Resouces.success("Atención!!", "Persona editar Correctamente");
            limpiar();
        }
    }

    public void eliminarPersona() {
        if (persona != null) {
            try {
                modeloPersona.destroy(persona.getIdpersona());
                limpiar();
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(ControllerPersona.class.getName()).log(Level.SEVERE, null, ex);
            }
            ModeloTabla.eliminar(persona);
//            Resouces.success("Persona eliminada correctamente");
            Resouces.success("Atención!!", "Persona Eliminada Correctamente");
            limpiar();
        }
    }

    public void limpiar() {
        this.vista.getTxtNombre().setText("");
        this.vista.getTxtApellido().setText("");
        this.vista.getTxtCedula().setText("");
        this.vista.getTxtCelular().setText("");
        this.vista.getTxtCorreo().setText("");
        this.vista.getTxtDireccion().setText("");

        this.vista.getjTable1().getSelectionModel().clearSelection();
        //control botones
        this.vista.getBtnCrear().setEnabled(true);
        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnEditar().setEnabled(false);
    }

    public void personaSeleccionada() {
        if (this.vista.getjTable1().getSelectedRow() != -1) {
            persona = ModeloTabla.getFilas().get(this.vista.getjTable1().getSelectedRow());
            this.vista.getTxtNombre().setText(persona.getNombre());
            this.vista.getTxtApellido().setText(persona.getApellido());
            this.vista.getTxtCedula().setText(persona.getCedula());
            this.vista.getTxtCelular().setText(persona.getCelular());
            this.vista.getTxtCorreo().setText(persona.getCorreo());
            this.vista.getTxtDireccion().setText(persona.getDireccion());

            //control botones
            this.vista.getBtnCrear().setEnabled(false);
            this.vista.getBtnEliminar().setEnabled(true);
            this.vista.getBtnEditar().setEnabled(true);
        }
    }

    public void buscarpersona() {
        if (this.vista.getjCheckMostrar().isSelected()) {
            ModeloTabla.setFilas(modeloPersona.findPersonaEntities());
            ModeloTabla.fireTableDataChanged();
        }
        if (!this.vista.getTxtBuscar().getText().equals("")) {
            ModeloTabla.setFilas(modeloPersona.buscarPersona(this.vista.getTxtBuscar().getText()));
            ModeloTabla.fireTableDataChanged();
        } else {
            limpiarbuscador();
        }
    }

    public void limpiarbuscador() {
        this.vista.getTxtBuscar().setText("");
        ModeloTabla.setFilas(modeloPersona.findPersonaEntities());
        ModeloTabla.fireTableDataChanged();

    }

    //llamar
    public void reporteGeneral() {
        Resouces.imprimirReeporte(ManageFactory.getConnection(manage.getEntityManagerFactory().createEntityManager()), "/reportes/Persona.jasper", new HashMap());
    }

    //reporte individual
    public void reporteIndividual() {
       //validar si existe un reporte de poersona
        if (persona!= null) {
            //Construir los parametros de envio al reporte
            Map parameters= new HashMap();
            //Asignar parametros al 
            parameters. put("id",persona.getIdpersona());
            //Llamamos al metodo del reporte
            Resouces.imprimirReeporte(ManageFactory.getConnection(manage.getEntityManagerFactory().createEntityManager()), "/reportes/Individual.jasper", parameters);
            
        }else{
            Resouces.warning("Atencion!!", "Debe selecionar una persona");
        }
    }

}
