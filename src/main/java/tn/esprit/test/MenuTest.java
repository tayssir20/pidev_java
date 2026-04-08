package org.example.test;

import org.example.entities.Jeu;
import org.example.entities.Tournoi;
import org.example.services.ServiceJeu;
import org.example.services.ServiceTournoi;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MenuTest {
    private static Scanner scanner = new Scanner(System.in);
    private static ServiceJeu serviceJeu = new ServiceJeu();
    private static ServiceTournoi serviceTournoi = new ServiceTournoi();

    public static void main(String[] args) {
        int choix;
        do {
            afficherMenuPrincipal();
            choix = lireEntier();
            
            switch (choix) {
                case 1:
                    menuJeu();
                    break;
                case 2:
                    menuTournoi();
                    break;
                case 0:
                    System.out.println("\n=== Au revoir! ===");
                    break;
                default:
                    System.out.println("\nChoix invalide!");
            }
        } while (choix != 0);
        
        scanner.close();
    }

    private static void afficherMenuPrincipal() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       MENU PRINCIPAL - CRUD TEST       ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("  1. Gestion des Jeux");
        System.out.println("  2. Gestion des Tournois");
        System.out.println("  0. Quitter");
        System.out.println("─────────────────────────────────────────");
        System.out.print("Votre choix: ");
    }

    // ==================== MENU JEU ====================
    private static void menuJeu() {
        int choix;
        do {
            afficherMenuJeu();
            choix = lireEntier();
            
            switch (choix) {
                case 1:
                    ajouterJeu();
                    break;
                case 2:
                    afficherJeux();
                    break;
                case 3:
                    modifierJeu();
                    break;
                case 4:
                    supprimerJeu();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\nChoix invalide!");
            }
        } while (choix != 0);
    }

    private static void afficherMenuJeu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         GESTION DES JEUX               ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("  1. Ajouter un jeu");
        System.out.println("  2. Afficher tous les jeux");
        System.out.println("  3. Modifier un jeu");
        System.out.println("  4. Supprimer un jeu");
        System.out.println("  0. Retour");
        System.out.println("─────────────────────────────────────────");
        System.out.print("Votre choix: ");
    }

    private static void ajouterJeu() {
        System.out.println("\n=== AJOUTER UN JEU ===");
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();
        System.out.print("Plateforme: ");
        String plateforme = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Statut: ");
        String statut = scanner.nextLine();

        try {
            Jeu jeu = new Jeu(nom, genre, plateforme, description, statut);
            serviceJeu.ajouter(jeu);
            System.out.println("Jeu ajoute avec succes!");
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    private static void afficherJeux() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                            LISTE DES JEUX                                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        
        try {
            List<Jeu> jeux = serviceJeu.getAll();
            if (jeux.isEmpty()) {
                System.out.println("  Aucun jeu trouve.");
            } else {
                System.out.printf("%-5s %-20s %-15s %-15s %-15s%n", "ID", "Nom", "Genre", "Plateforme", "Statut");
                System.out.println("─────────────────────────────────────────────────────────────────────────────────");
                for (Jeu jeu : jeux) {
                    System.out.printf("%-5d %-20s %-15s %-15s %-15s%n", 
                        jeu.getId(), jeu.getNom(), jeu.getGenre(), jeu.getPlateforme(), jeu.getStatut());
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    private static void modifierJeu() {
        afficherJeux();
        System.out.print("\nID du jeu a modifier: ");
        int id = lireEntier();

        try {
            List<Jeu> jeux = serviceJeu.getAll();
            Jeu jeuAModifier = null;
            for (Jeu jeu : jeux) {
                if (jeu.getId() == id) {
                    jeuAModifier = jeu;
                    break;
                }
            }

            if (jeuAModifier == null) {
                System.out.println("Jeu introuvable!");
                return;
            }

            System.out.println("\n=== MODIFIER LE JEU ===");
            System.out.print("Nouveau nom [" + jeuAModifier.getNom() + "]: ");
            String nom = scanner.nextLine();
            if (!nom.isEmpty()) jeuAModifier.setNom(nom);

            System.out.print("Nouveau genre [" + jeuAModifier.getGenre() + "]: ");
            String genre = scanner.nextLine();
            if (!genre.isEmpty()) jeuAModifier.setGenre(genre);

            System.out.print("Nouvelle plateforme [" + jeuAModifier.getPlateforme() + "]: ");
            String plateforme = scanner.nextLine();
            if (!plateforme.isEmpty()) jeuAModifier.setPlateforme(plateforme);

            System.out.print("Nouvelle description [" + jeuAModifier.getDescription() + "]: ");
            String description = scanner.nextLine();
            if (!description.isEmpty()) jeuAModifier.setDescription(description);

            System.out.print("Nouveau statut [" + jeuAModifier.getStatut() + "]: ");
            String statut = scanner.nextLine();
            if (!statut.isEmpty()) jeuAModifier.setStatut(statut);

            serviceJeu.modifier(jeuAModifier);
            System.out.println("Jeu modifie avec succes!");
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    private static void supprimerJeu() {
        afficherJeux();
        System.out.print("\nID du jeu a supprimer: ");
        int id = lireEntier();

        System.out.print("Etes-vous sur? (o/n): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("o")) {
            try {
                serviceJeu.supprimer(id);
                System.out.println("Jeu supprime avec succes!");
            } catch (SQLException e) {
                System.err.println("Erreur: " + e.getMessage());
            }
        } else {
            System.out.println("Suppression annulee.");
        }
    }

    // ==================== MENU TOURNOI ====================
    private static void menuTournoi() {
        int choix;
        do {
            afficherMenuTournoi();
            choix = lireEntier();
            
            switch (choix) {
                case 1:
                    ajouterTournoi();
                    break;
                case 2:
                    afficherTournois();
                    break;
                case 3:
                    modifierTournoi();
                    break;
                case 4:
                    supprimerTournoi();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\nChoix invalide!");
            }
        } while (choix != 0);
    }

    private static void afficherMenuTournoi() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       GESTION DES TOURNOIS             ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("  1. Ajouter un tournoi");
        System.out.println("  2. Afficher tous les tournois");
        System.out.println("  3. Modifier un tournoi");
        System.out.println("  4. Supprimer un tournoi");
        System.out.println("  0. Retour");
        System.out.println("─────────────────────────────────────────");
        System.out.print("Votre choix: ");
    }

    private static void ajouterTournoi() {
        System.out.println("\n=== AJOUTER UN TOURNOI ===");
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Statut: ");
        String statut = scanner.nextLine();
        System.out.print("Type: ");
        String type = scanner.nextLine();
        System.out.print("Max participants: ");
        int maxParticipants = lireEntier();
        System.out.print("Cagnotte: ");
        double cagnotte = lireDouble();
        System.out.print("Frais inscription: ");
        double fraisInscription = lireDouble();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("ID du jeu: ");
        int jeuId = lireEntier();

        try {
            Date maintenant = new Date();
            Date dansSeptJours = new Date(maintenant.getTime() + (7 * 24 * 60 * 60 * 1000));
            
            Tournoi tournoi = new Tournoi(nom, maintenant, dansSeptJours, statut, type, 
                                          maxParticipants, cagnotte, maintenant, fraisInscription, description, jeuId);
            serviceTournoi.ajouter(tournoi);
            System.out.println("Tournoi ajoute avec succes!");
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    private static void afficherTournois() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                          LISTE DES TOURNOIS                                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        
        try {
            List<Tournoi> tournois = serviceTournoi.getAll();
            if (tournois.isEmpty()) {
                System.out.println("  Aucun tournoi trouve.");
            } else {
                System.out.printf("%-5s %-25s %-15s %-10s %-10s%n", "ID", "Nom", "Statut", "Type", "Cagnotte");
                System.out.println("─────────────────────────────────────────────────────────────────────────────────");
                for (Tournoi tournoi : tournois) {
                    System.out.printf("%-5d %-25s %-15s %-10s %-10.2f%n", 
                        tournoi.getId(), tournoi.getNom(), tournoi.getStatut(), 
                        tournoi.getType(), tournoi.getCagnotte());
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    private static void modifierTournoi() {
        afficherTournois();
        System.out.print("\nID du tournoi a modifier: ");
        int id = lireEntier();

        try {
            List<Tournoi> tournois = serviceTournoi.getAll();
            Tournoi tournoiAModifier = null;
            for (Tournoi tournoi : tournois) {
                if (tournoi.getId() == id) {
                    tournoiAModifier = tournoi;
                    break;
                }
            }

            if (tournoiAModifier == null) {
                System.out.println("Tournoi introuvable!");
                return;
            }

            System.out.println("\n=== MODIFIER LE TOURNOI ===");
            System.out.print("Nouveau nom [" + tournoiAModifier.getNom() + "]: ");
            String nom = scanner.nextLine();
            if (!nom.isEmpty()) tournoiAModifier.setNom(nom);

            System.out.print("Nouveau statut [" + tournoiAModifier.getStatut() + "]: ");
            String statut = scanner.nextLine();
            if (!statut.isEmpty()) tournoiAModifier.setStatut(statut);

            System.out.print("Nouvelle cagnotte [" + tournoiAModifier.getCagnotte() + "]: ");
            String cagnotteStr = scanner.nextLine();
            if (!cagnotteStr.isEmpty()) tournoiAModifier.setCagnotte(Double.parseDouble(cagnotteStr));

            serviceTournoi.modifier(tournoiAModifier);
            System.out.println("Tournoi modifie avec succes!");
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    private static void supprimerTournoi() {
        afficherTournois();
        System.out.print("\nID du tournoi a supprimer: ");
        int id = lireEntier();

        System.out.print("Etes-vous sur? (o/n): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("o")) {
            try {
                serviceTournoi.supprimer(id);
                System.out.println("Tournoi supprime avec succes!");
            } catch (SQLException e) {
                System.err.println("Erreur: " + e.getMessage());
            }
        } else {
            System.out.println("Suppression annulee.");
        }
    }

    // ==================== UTILITAIRES ====================
    private static int lireEntier() {
        while (true) {
            try {
                int valeur = Integer.parseInt(scanner.nextLine());
                return valeur;
            } catch (NumberFormatException e) {
                System.out.print("Veuillez entrer un nombre valide: ");
            }
        }
    }

    private static double lireDouble() {
        while (true) {
            try {
                double valeur = Double.parseDouble(scanner.nextLine());
                return valeur;
            } catch (NumberFormatException e) {
                System.out.print("Veuillez entrer un nombre valide: ");
            }
        }
    }
}
