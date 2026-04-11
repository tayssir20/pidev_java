package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.ServiceUser;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {

    private final ServiceUser serviceUser = new ServiceUser();

    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;
    @FXML private Button registerButton;

    @FXML
    private void handleRegister() {
        String nom = nomField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (nom.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("Please fill in all fields.", true);
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showMessage("Please enter a valid email address.", true);
            return;
        }

        if (password.length() < 6) {
            showMessage("Password must contain at least 6 characters.", true);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match.", true);
            return;
        }

        try {
            if (serviceUser.emailExists(email)) {
                showMessage("This email is already used.", true);
                return;
            }

            User user = new User(
                    email,
                    "[\"ROLE_USER\"]",
                    password,
                    nom,
                    true,
                    null,
                    false,
                    null,
                    null,
                    null,
                    false
            );

            serviceUser.ajouter(user);
            showMessage("Account created successfully. You can now log in.", false);
            clearFields();
        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage(), true);
        }
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        try {
            loadScene(event, "/Login.fxml", "Login");
        } catch (IOException e) {
            showMessage("Unable to open login form.", true);
        }
    }

    private void loadScene(ActionEvent event, String resourcePath, String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(resourcePath));
        Stage stage = (Stage) ((Control) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }

    private void clearFields() {
        nomField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    private void showMessage(String msg, boolean isError) {
        messageLabel.setText(msg);
        messageLabel.setTextFill(isError ? Color.RED : Color.web("#5b4cdf"));
    }
}
