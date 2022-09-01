/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.Resouces;
import java.awt.Dimension;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelo.Persona;
import modelo.PersonaJpaController;
import modelo.Usuario;
import modelo.UsuarioJpaController;
import modelo.exceptions.NonexistentEntityException;
import proyecto_producto.ManageFactory;
import vista.interna.ViewUsuario;

/**
 *
 * @author Shalva
 */
public class ControllerUsuario {

    ViewUsuario vista;
    ManageFactory manage;
    UsuarioJpaController modeloUsuario;
    Usuario usuario;
    JDesktopPane panelEscritorio;
    ModeloTablaUsuario modelotabla;
    ListSelectionModel listaUsernaModel;

    public ControllerUsuario(ViewUsuario vista, ManageFactory manage, UsuarioJpaController modeloUsuario, JDesktopPane panelEscritorio) {
        this.manage = manage;
        this.modeloUsuario = modeloUsuario;
        this.panelEscritorio = panelEscritorio;
        this.modelotabla = new ModeloTablaUsuario();
        this.modelotabla.setFilas(this.modeloUsuario.findUsuarioEntities());

        if (ControllerAdministrador.vpu == null) {
            ControllerAdministrador.vpu = new ViewUsuario();
            this.vista = ControllerAdministrador.vpu;
            this.panelEscritorio.add(this.vista);
            this.vista.getTablausuario().setModel(modelotabla);

            this.vista.show();
            cargarCombobos();
            iniciarControl();

            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vista.getSize();
            this.vista.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);

        } else {
            ControllerAdministrador.vpu.show();

        }
    }

    public void iniciarControl() {
        this.vista.getBtnCrear().addActionListener(l -> guardarUsuario());
        this.vista.getBtnEditar().addActionListener(l -> editarUsuario());
        this.vista.getBtnEliminar().addActionListener(l -> eliminarUsuario());
        this.vista.getTablausuario().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaUsernaModel = this.vista.getTablausuario().getSelectionModel();
        listaUsernaModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    usuarioSeleccionado();
                }
            }

        });

        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnLimpiar1().addActionListener(l -> limpiar());
        this.vista.getBtnLimpiar().addActionListener(l -> limpiarbuscador());
        this.vista.getBtnBuscar().addActionListener(l -> buscarusuario());
        this.vista.getjCheckMostrar().addActionListener(l -> buscarusuario());
    }

    public void guardarUsuario() {
        usuario = new Usuario();
        usuario.setUsuario(this.vista.getTxtUsuario().getText());
        usuario.setClave(this.vista.getTxtpassword().getText());
        usuario.setIdpersona((Persona) this.vista.getJcmbNombreUsuario().getSelectedItem());

        modeloUsuario.create(usuario);
        modelotabla.agregar(usuario);
        Resouces.success("Atención!!", "USUARIO GUARDADA CORECTAMENTE");
        limpiar();
    }

    public void editarUsuario() {
        if (usuario != null) {
            usuario.setUsuario(this.vista.getTxtUsuario().getText());
            usuario.setClave(this.vista.getTxtpassword().getText());
            usuario.setIdpersona((Persona) this.vista.getJcmbNombreUsuario().getSelectedItem());
            Resouces.success("Atención!!", "USUARIO EDITADA CORECTAMENTE");
            try {
                modeloUsuario.edit(usuario);
                modelotabla.eliminar(usuario);
                modelotabla.actualizar(usuario);
                limpiar();
            } catch (Exception ex) {

                Logger.getLogger(ControllerUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void eliminarUsuario() {
        if (usuario != null) {
            try {
                modeloUsuario.destroy(usuario.getIdusuario());
                limpiar();
            } catch (Exception ex) {
                Logger.getLogger(ControllerUsuario.class.getName()).log(Level.SEVERE, null, ex);
                limpiar();
            }
            modelotabla.eliminar(usuario);
            Resouces.success("ALERTA!!", "USUARIO ELIMINADO CORECTAMENTE");
        }
    }

    public void limpiar() {
        this.vista.getTxtUsuario().setText("");
        this.vista.getTxtpassword().setText("");
        this.vista.getJcmbNombreUsuario().setSelectedItem(0);

        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnCrear().setEnabled(true);
        this.vista.getTablausuario().getSelectionModel().clearSelection();
    }

    public void usuarioSeleccionado() {
        if (this.vista.getTablausuario().getSelectedRow() != -1) {
            usuario = modelotabla.getFilas().get(this.vista.getTablausuario().getSelectedRow());
            this.vista.getTxtUsuario().setText(usuario.getUsuario());
            this.vista.getTxtpassword().setText(usuario.getClave());
            this.vista.getJcmbNombreUsuario().setSelectedItem(usuario.getIdpersona());
            //
            this.vista.getBtnEliminar().setEnabled(true);
            this.vista.getBtnEditar().setEnabled(true);
            this.vista.getBtnCrear().setEnabled(false);
        }
    }

    public void limpiarbuscador() {
        this.vista.getTxtBuscar().setText("");
        modelotabla.setFilas(modeloUsuario.findUsuarioEntities());
        modelotabla.fireTableDataChanged();
    }

    public void cargarCombobos() {
        try {
            Vector v = new Vector();
            v.addAll(new PersonaJpaController(manage.getEntityManagerFactory()).findPersonaEntities());
            this.vista.getJcmbNombreUsuario().setModel(new DefaultComboBoxModel(v));

        } catch (ArrayIndexOutOfBoundsException ex) {

            System.out.println("ERROR");
        }
    }

    public void buscarusuario() {
        if (this.vista.getjCheckMostrar().isSelected()) {
            modelotabla.setFilas(modeloUsuario.findUsuarioEntities());
            modelotabla.fireTableDataChanged();
            limpiarbuscador();
        } else {
            if (!this.vista.getTxtBuscar().getText().equals("")) {
                modelotabla.setFilas(modeloUsuario.buscarusuario(this.vista.getTxtBuscar().getText()));
                modelotabla.fireTableDataChanged();
            } else {

            }

        }

    }
}
