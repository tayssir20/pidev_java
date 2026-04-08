package tn.esprit.test;

import tn.esprit.services.ServicePersonne;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        ServicePersonne sp= new ServicePersonne();
        //Personne p= new Personne(1,"ons", "fadhel", 26);

        try {
            System.out.println(sp.getAll());
            //sp.modifier(p);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}