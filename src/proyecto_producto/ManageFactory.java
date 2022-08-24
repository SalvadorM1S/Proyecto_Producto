/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_producto;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Shalva
 */
public class ManageFactory {
    
    private EntityManagerFactory emf= null;
    public EntityManagerFactory getEntityManagerFactory(){
        return emf=Persistence.createEntityManagerFactory("proyecto_productoPU");
    }
    
}
