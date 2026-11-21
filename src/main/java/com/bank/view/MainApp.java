package com.bank.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Starts the application window and shows the login screen first. */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        LoginView login = new LoginView();

        // Placeholder behaviour: the real checks are added with the controllers in Step 3.
        login.setOnLogin((username, password) ->
                login.showMessage("Login pressed for '" + username + "'. Checking comes in Step 3."));
        login.setOnRegister(() ->
                login.showMessage("Registration comes in Step 3."));

        stage.setTitle("Banking System - Login");
        stage.setScene(new Scene(login.getRoot(), 420, 340));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
