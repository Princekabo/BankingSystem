package com.bank.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.function.BiConsumer;

/**
 * Boundary class for the login screen.
 *
 * It only draws the form, collects what the user types, and passes it to a listener.
 * It does not check passwords or touch the database: that is the controller's job (Step 3).
 */
public class LoginView {
    private final VBox root = new VBox(15);
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button loginButton = new Button("Login");
    private final Button registerButton = new Button("Create account");
    private final Label messageLabel = new Label();

    private BiConsumer<String, String> loginListener = (username, password) -> { };
    private Runnable registerListener = () -> { };

    public LoginView() {
        Label title = new Label("Welcome to the Bank");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(0, new Label("Username"), usernameField);
        form.addRow(1, new Label("Password"), passwordField);

        loginButton.setDefaultButton(true);
        loginButton.setOnAction(event ->
                loginListener.accept(usernameField.getText(), passwordField.getText()));
        registerButton.setOnAction(event -> registerListener.run());

        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-text-fill: #c0392b;");

        root.getChildren().addAll(title, form, loginButton, registerButton, messageLabel);
        root.setPadding(new Insets(25));
    }

    /** The top-level node that goes into a Scene. */
    public Parent getRoot() {
        return root;
    }

    /** Called with the username and password when the user presses Login. */
    public void setOnLogin(BiConsumer<String, String> listener) {
        this.loginListener = listener;
    }

    /** Called when the user presses Create account. */
    public void setOnRegister(Runnable listener) {
        this.registerListener = listener;
    }

    /** Shows a message under the form, for example "Invalid username or password". */
    public void showMessage(String text) {
        messageLabel.setText(text);
    }

    /** Clears the password box, for example after a failed login. */
    public void clearPassword() {
        passwordField.clear();
    }
}
