package org.example.test;

import org.example.entities.User;
import org.example.services.ServiceUser;
import org.example.utils.MyDatabase;

public class MainUser {
    public static void main(String[] args) {
        MyDatabase.getInstance();

        ServiceUser serviceUser = new ServiceUser();
        int idToSupprimer = 25; // Remplacez par l'ID de l'utilisateur que vous souhaitez supprimer
        try {
            User user = new User(
                    "user2025@example.com",
                    "[\"ROLE_USER\"]",
                    "password123",
                    "User Test",
                    true,
                    null,
                    false,
                    null,
                    null,
                    null,
                    false
            );

            serviceUser.ajouter(user);

            if (!serviceUser.getAll().isEmpty()) {
                user.setNom("User Updated");
                serviceUser.modifier(user);
                System.out.println("\nListe avant suppression :");
                serviceUser.getAll().forEach(System.out::println);
              
                serviceUser.supprimer(idToSupprimer);
                System.out.println("Utilisateur supprime a " + idToSupprimer);
                System.out.println("\nListe apres suppression :");
                serviceUser.getAll().forEach(System.out::println);
                
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}