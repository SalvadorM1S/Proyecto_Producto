/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controller.Resouces;
import java.awt.Dimension;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelo.Persona;
import modelo.Producto;
import modelo.ProductoJpaController;
import modelo.exceptions.NonexistentEntityException;
import proyecto_producto.ManageFactory;
import vista.interna.ViewProductos;

/**
 *
 * @author Shalva
 */
public class ControllerProducto {

    ViewProductos vista;
    ManageFactory manage;
    ProductoJpaController modeloProducto;
    Producto producto;
    JDesktopPane panelEscritorio;
    ModeloTablaProducto modeloTabla;
    ListSelectionModel listaProductoModel;

    public ControllerProducto(ViewProductos vista, ManageFactory manage, ProductoJpaController modeloProducto, JDesktopPane panelEscritorio) {

        this.manage = manage;
        this.modeloProducto = modeloProducto;
        this.panelEscritorio = panelEscritorio;
        this.modeloTabla = new ModeloTablaProducto();
        this.modeloTabla.setFilas(this.modeloProducto.findProductoEntities());

        if (ControllerAdministrador.vps == null) {
            ControllerAdministrador.vps = new ViewProductos();

            this.vista = ControllerAdministrador.vps;
            this.panelEscritorio.add(this.vista);
            this.vista.getJtablaProductos().setModel(modeloTabla);
            this.vista.show();
            inciar();
            //Para centar la vista en la ventana
            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vista.getSize();
            this.vista.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);

        } else {
            ControllerAdministrador.vps.show();
        }

    }

    public void guardarProducto() {
        producto = new Producto();
        producto.setNombre(this.vista.getTxtNombre().getText());
        String precios = this.vista.getTxtPrecio().getText();
        BigInteger precio = new BigInteger(precios);
        producto.setPrecio(precio);
        Integer cantidad = Integer.parseInt(this.vista.getTxtCantidad().getText());
        producto.setCantidad(cantidad);

        modeloProducto.create(producto);
        modeloTabla.agregar(producto);
        Resouces.success("ALERTA!!", "PRODUCTO GUARDADA CORECTAMENTE");
        JOptionPane.showMessageDialog(panelEscritorio, "PRODUCTO CREADO CORRECTAMENTE");
        limpiar();
    }

    public void editarProducto() {
        if (producto != null) {
            producto.setNombre(this.vista.getTxtNombre().getText());
            String precios = this.vista.getTxtPrecio().getText();
            BigInteger precio = new BigInteger(precios);
            producto.setPrecio(precio);
            Integer cantidad = Integer.parseInt(this.vista.getTxtCantidad().getText());
            producto.setCantidad(cantidad);
            Resouces.success("ALERTA!!", "PERSONA ACTUALIZAD CORECTAMENTE");
            try {
                modeloProducto.edit(producto);
                modeloTabla.eliminar(producto);
                modeloTabla.actualizar(producto);
                limpiar();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(ControllerProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void eliminarProducto() {
        if (producto != null) {
            try {
                modeloProducto.destroy(producto.getIdproducto());
                limpiar();
            } catch (NonexistentEntityException ex) {
                java.util.logging.Logger.getLogger(ControllerProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
            modeloTabla.eliminar(producto);
            JOptionPane.showMessageDialog(panelEscritorio, "PERSONA ELIMINADA CORRECTAMENTE");
            Resouces.success("ALERTA!!", "PERSONA ELIMINADA CORECTAMENTE");
        }
    }

    public void inciar() {
        this.vista.getBtnCrear().addActionListener(l -> guardarProducto());
        this.vista.getBtnEditar().addActionListener(l -> editarProducto());
        this.vista.getBtnEliminar().addActionListener(l -> eliminarProducto());
        this.vista.getJtablaProductos().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaProductoModel = this.vista.getJtablaProductos().getSelectionModel();
        listaProductoModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    productoSeleccionada();
                }
            }

        });
        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnLimpiar1().addActionListener(l -> limpiar());
        this.vista.getBtnLimpiar().addActionListener(l -> limpiarbuscador());
        this.vista.getBtnBuscar().addActionListener(l -> buscarproducto());
        this.vista.getjCheckMostrar().addActionListener(l -> buscarproducto());
    }

    public void limpiar() {
        this.vista.getTxtNombre().setText("");
        this.vista.getTxtPrecio().setText("");
        this.vista.getTxtCantidad().setText("");

        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnCrear().setEnabled(true);
        this.vista.getJtablaProductos().getSelectionModel().clearSelection();
    }

    public void productoSeleccionada() {
        if (this.vista.getJtablaProductos().getSelectedRow() != -1) {
            producto = modeloTabla.getFilas().get(this.vista.getJtablaProductos().getSelectedRow());
            this.vista.getTxtNombre().setText(producto.getNombre());
            String precio = String.valueOf(producto.getPrecio());
            this.vista.getTxtPrecio().setText(precio);
            String cantidad = String.valueOf(producto.getCantidad());
            this.vista.getTxtCantidad().setText(cantidad);
            ////
            this.vista.getBtnEliminar().setEnabled(true);
            this.vista.getBtnEditar().setEnabled(true);
            this.vista.getBtnCrear().setEnabled(false);
        }

    }

    public void buscarproducto() {
        if (this.vista.getjCheckMostrar().isSelected()) {
            modeloTabla.setFilas(modeloProducto.findProductoEntities());
            modeloTabla.fireTableDataChanged();

        } else {
            if (!this.vista.getTxtBuscar().getText().equals("")) {
                modeloTabla.setFilas(modeloProducto.buscarProducto(this.vista.getTxtBuscar().getText()));
                modeloTabla.fireTableDataChanged();
            } else {
                limpiarbuscador();
            }

        }

    }

    public void limpiarbuscador() {
        this.vista.getTxtBuscar().setText("");
        modeloTabla.setFilas(modeloProducto.findProductoEntities());
        modeloTabla.fireTableDataChanged();
    }
}
