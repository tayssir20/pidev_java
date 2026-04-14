package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import tn.esprit.entities.Jeu;
import tn.esprit.services.ServiceJeu;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Panneau liste des jeux (cartes). CRUD actif sauf si {@link #setReadOnly(boolean)} est appelé avec {@code true}
 * (page catalogue avec navbar).
 */
public class JeuxPanelController implements Initializable {

    private static final double CARD_W = 286;
    private static final double IMG_H = 132;

    @FXML
    private FlowPane cardsContainer;
    @FXML
    private Label messageLabel;
    @FXML
    private Label totalJeuxLabel;
    @FXML
    private Button newGameButton;

    private final ServiceJeu serviceJeu = new ServiceJeu();
    private Image coverPlaceholder;
    private boolean readOnly;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        URL imgUrl = getClass().getResource("/images/game.png");
        if (imgUrl != null) {
            coverPlaceholder = new Image(imgUrl.toExternalForm(), CARD_W, IMG_H, false, true);
        }
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        if (newGameButton != null) {
            newGameButton.setVisible(!readOnly);
            newGameButton.setManaged(!readOnly);
        }
        loadJeux();
    }

    private void loadJeux() {
        try {
            List<Jeu> list = serviceJeu.getAll();
            totalJeuxLabel.setText(String.valueOf(list.size()));
            messageLabel.setText("");
            cardsContainer.getChildren().clear();
            for (Jeu j : list) {
                cardsContainer.getChildren().add(buildGameCard(j));
            }
        } catch (SQLException e) {
            showError("Impossible de charger les jeux : " + e.getMessage());
        }
    }

    private VBox buildGameCard(Jeu jeu) {
        VBox card = new VBox(0);
        card.setPrefWidth(CARD_W);
        card.setMaxWidth(CARD_W);
        card.setStyle(
                "-fx-background-color: #151f38;"
                        + "-fx-background-radius: 12;"
                        + "-fx-border-color: rgba(124,58,237,0.45);"
                        + "-fx-border-radius: 12;"
                        + "-fx-border-width: 1;"
                        + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 14, 0.2, 0, 4);"
        );

        StackPane imgStack = new StackPane();
        imgStack.setPrefSize(CARD_W, IMG_H);
        imgStack.setMaxSize(CARD_W, IMG_H);

        Rectangle imgClip = new Rectangle(CARD_W, IMG_H);
        imgClip.setArcWidth(22);
        imgClip.setArcHeight(22);
        imgStack.setClip(imgClip);

        boolean hasCover = coverPlaceholder != null && !coverPlaceholder.isError();
        if (!hasCover) {
            Rectangle grad = new Rectangle(CARD_W, IMG_H);
            grad.setArcWidth(22);
            grad.setArcHeight(22);
            grad.setFill(new LinearGradient(0, 0, 1, 1, true, null,
                    new Stop(0, Color.web("#5f56ff")),
                    new Stop(0.5, Color.web("#7c3aed")),
                    new Stop(1, Color.web("#3b1022"))));
            imgStack.getChildren().add(grad);
        } else {
            ImageView iv = new ImageView(coverPlaceholder);
            iv.setFitWidth(CARD_W);
            iv.setFitHeight(IMG_H);
            iv.setPreserveRatio(false);
            imgStack.getChildren().add(iv);
        }

        String ribbonText = ribbonLabelFor(jeu);
        Label ribbon = new Label(ribbonText);
        ribbon.setWrapText(true);
        ribbon.setMaxWidth(140);
        ribbon.setStyle(
                "-fx-background-color: #f2d061;"
                        + "-fx-text-fill: #0d1b3e;"
                        + "-fx-font-size: 9px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 5 10;"
                        + "-fx-background-radius: 2;"
        );
        ribbon.setRotate(-12);
        StackPane.setAlignment(ribbon, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(ribbon, new Insets(0, 8, 10, 0));
        imgStack.getChildren().add(ribbon);

        Label title = new Label(jeu.getNom() != null ? jeu.getNom() : "—");
        title.setWrapText(true);
        title.setMaxWidth(CARD_W - 24);
        title.setStyle(
                "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 14 14 12 14;"
        );
        VBox titleBox = new VBox(title);
        titleBox.setStyle("-fx-background-color: #0a1228;");

        String genre = emptyAsDash(jeu.getGenre());
        String plateforme = emptyAsDash(jeu.getPlateforme());
        String statut = emptyAsDash(jeu.getStatut());
        String desc = jeu.getDescription() != null ? jeu.getDescription() : "—";
        if (desc.length() > 48) {
            desc = desc.substring(0, 45) + "…";
        }

        VBox details = new VBox(0);
        details.getChildren().addAll(
                genrePlateformeRow(genre, plateforme),
                infoRow("Statut", statut, statutStyle(statut)),
                infoRow("Description", desc, "-fx-text-fill: #94a3b8; -fx-font-size: 11px;"),
                footerRow(jeu)
        );

        card.getChildren().addAll(imgStack, titleBox, details);
        return card;
    }

    private static String ribbonLabelFor(Jeu jeu) {
        if (jeu.getGenre() != null && !jeu.getGenre().isBlank()) {
            String g = jeu.getGenre().toUpperCase();
            return g.length() > 22 ? g.substring(0, 19) + "…" : g;
        }
        return "CATALOGUE";
    }

    private static String emptyAsDash(String s) {
        return (s == null || s.isBlank()) ? "—" : s;
    }

    private HBox genrePlateformeRow(String genre, String plateforme) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 14, 10, 14));
        row.setStyle("-fx-border-color: rgba(255,255,255,0.07); -fx-border-width: 0 0 1 0;");

        Label left = new Label("🎮  " + genre);
        left.setWrapText(true);
        left.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 12px;");
        left.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(left, Priority.ALWAYS);

        Label right = new Label(plateforme);
        right.setStyle("-fx-text-fill: #a78bfa; -fx-font-size: 12px; -fx-font-weight: bold;");
        row.getChildren().addAll(left, right);
        return row;
    }

    private HBox infoRow(String leftText, String rightText, String rightExtraStyle) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 14, 10, 14));
        row.setStyle("-fx-border-color: rgba(255,255,255,0.07); -fx-border-width: 0 0 1 0;");

        Label left = new Label(leftText);
        left.setWrapText(true);
        left.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 12px;");
        left.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(left, Priority.ALWAYS);

        if (rightText.isEmpty()) {
            row.getChildren().add(left);
            return row;
        }

        Label right = new Label(rightText);
        right.setWrapText(true);
        right.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;" + (rightExtraStyle.isEmpty() ? " -fx-text-fill: #a78bfa;" : " " + rightExtraStyle));
        row.getChildren().addAll(left, right);
        return row;
    }

    private static String statutStyle(String statut) {
        String s = statut.toLowerCase();
        if (s.contains("fin") || s.contains("termin")) {
            return "-fx-text-fill: #f59e0b;";
        }
        if (s.contains("ferm") || s.contains("indis")) {
            return "-fx-text-fill: #f87171;";
        }
        if (s.contains("actif") || s.contains("dispo") || s.contains("ouvert")) {
            return "-fx-text-fill: #4ade80;";
        }
        return "-fx-text-fill: #a78bfa;";
    }

    private HBox footerRow(Jeu jeu) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 14, 14, 14));
        row.setSpacing(10);

        Label idLab = new Label("ID · " + jeu.getId());
        idLab.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11px;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        row.getChildren().add(idLab);

        if (!readOnly) {
            Button editBtn = new Button("Modifier");
            editBtn.setStyle(
                    "-fx-background-color: #f59e0b; -fx-text-fill: white; -fx-font-size: 11px;"
                            + "-fx-padding: 6 12; -fx-cursor: hand; -fx-background-radius: 6;"
            );
            editBtn.setOnAction(e -> openEditWindow(jeu));

            Button deleteBtn = new Button("Supprimer");
            deleteBtn.setStyle(
                    "-fx-background-color: #dc2626; -fx-text-fill: white; -fx-font-size: 11px;"
                            + "-fx-padding: 6 12; -fx-cursor: hand; -fx-background-radius: 6;"
            );
            deleteBtn.setOnAction(e -> confirmDelete(jeu));

            row.getChildren().addAll(spacer, editBtn, deleteBtn);
        } else {
            HBox.setHgrow(idLab, Priority.NEVER);
        }

        return row;
    }

    @FXML
    private void handleNewJeu() {
        if (readOnly) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterJeu.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter un jeu");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadJeux();
        } catch (IOException e) {
            showError("Erreur d'ouverture du formulaire : " + e.getMessage());
        }
    }

    private void openEditWindow(Jeu j) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterJeu.fxml"));
            Parent root = loader.load();
            AjouterJeuController controller = loader.getController();
            controller.setJeuToEdit(j);
            Stage stage = new Stage();
            stage.setTitle("Modifier le jeu");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadJeux();
        } catch (IOException e) {
            showError("Erreur d'ouverture du formulaire : " + e.getMessage());
        }
    }

    private void confirmDelete(Jeu j) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText(null);
        confirm.setContentText("Supprimer le jeu « " + j.getNom() + " » ?");
        confirm.showAndWait().ifPresent(button -> {
            if (button == ButtonType.OK) {
                try {
                    serviceJeu.supprimer(j.getId());
                    messageLabel.setStyle("-fx-text-fill: #4ade80;");
                    messageLabel.setText("Jeu supprimé.");
                    loadJeux();
                } catch (SQLException ex) {
                    showError("Suppression impossible : " + ex.getMessage());
                }
            }
        });
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Erreur");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
