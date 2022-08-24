/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;
import modelo.Usuario;
import modelo.UsuarioJpaController;
import proyecto_producto.ManageFactory;
import vista.ViewLogin;
import vista.viewAdminstrador;

/**
 *
 * @author Shalva
 */
public class Controllerlogin {
    private ManageFactory manager;
    private ViewLogin vista;
    private UsuarioJpaController modelo;
    viewAdminstrador admin = new viewAdminstrador();


    public Controllerlogin(ManageFactory manager, ViewLogin vista, UsuarioJpaController modelo) {
        this.manager = manager;
        this.vista = vista;
        this.modelo = modelo;
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        iniciarControl();
    }
    
    public void iniciarControl(){
        vista.getBtnEntrar().addActionListener(l-> controlLogin());
        vista.getBtnsalir().addActionListener(l-> salir());
    }
    
    public void salir(){
        System.exit(0);
    }
    
    public void controlLogin(){
        String usuar = vista.getTxtUsuario().getText();
        String clave= new String(vista.getTxtPassword().getPassword());
        
        
        try{
            Usuario user = modelo.BuscarUsuario(usuar, clave);
            if(user !=null){
            admin.setVisible(true);
            new ControllerAdministrador(admin, manager);
            vista.setVisible(false);
            
        }else{
            JOptionPane.showMessageDialog(vista, "Usuario Incorrecto");
        }
        }catch(PersistenceException e){
            JOptionPane.showMessageDialog(vista, "No existe conexion con la base de datos");
        }
        
        
        
        
    }
    
}
