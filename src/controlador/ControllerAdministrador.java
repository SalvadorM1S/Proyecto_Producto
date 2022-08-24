/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;
import javax.swing.JOptionPane;
import modelo.PersonaJpaController;
import modelo.ProductoJpaController;
import modelo.UsuarioJpaController;
import vista.viewAdminstrador;
import proyecto_producto.ManageFactory;
import vista.interna.ViewPersona;
import vista.interna.ViewProductos;
import vista.interna.ViewUsuario;

/**
 *
 * @author Shalva
 */
public class ControllerAdministrador extends javax.swing.JFrame{
    viewAdminstrador vista;
    ManageFactory manage;

    public ControllerAdministrador(viewAdminstrador vista, ManageFactory manage) {
        this.vista = vista;
        this.manage = manage;
        
        this.vista.setExtendedState(MAXIMIZED_BOTH);
        controlEvento();
    }
    
    public void controlEvento(){
        vista.getjMenuPersonas().addActionListener(l->cargarvistaPersona());
        vista.getjMenuProducto().addActionListener(l->cargarvistaProducto());
        vista.getjMenuUsuario().addActionListener(l->cargarvistaUsuario());
    }
    
    public static ViewPersona vp;
    
    public void cargarvistaPersona(){
       new ControllerPersona(vp, manage, new PersonaJpaController(manage.getEntityManagerFactory()),this.vista.getEscritorio());
        System.out.println("mensaje");
       
    }
    public static ViewProductos vps;
    
    public void cargarvistaProducto(){
       new ControllerProducto(vps, manage, new ProductoJpaController(manage.getEntityManagerFactory()),this.vista.getEscritorio());
        System.out.println("mensaje");
       
    }
    public static ViewUsuario vpu;
    
    public void cargarvistaUsuario(){
       new ControllerUsuario(vpu, manage, new UsuarioJpaController(manage.getEntityManagerFactory()),this.vista.getEscritorio());
        System.out.println("mensaje");
       
    }
}
