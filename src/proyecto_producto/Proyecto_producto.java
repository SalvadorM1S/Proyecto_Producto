/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_producto;

import controlador.Controllerlogin;
import modelo.UsuarioJpaController;
import vista.ViewLogin;

/**
 *
 * @author Shalva
 */
public class Proyecto_producto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ManageFactory manager= new ManageFactory();
        ViewLogin vista = new ViewLogin();
        UsuarioJpaController modelo = new UsuarioJpaController(manager.getEntityManagerFactory());
        Controllerlogin controlador = new Controllerlogin(manager, vista, modelo);
    }
    
}
