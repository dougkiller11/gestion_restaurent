package restau.app;

import javafx.application.Application;
import javafx.stage.Stage;
import restau.ui.LoginWindow;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        new LoginWindow().show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

