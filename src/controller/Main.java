package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Jesper Nylend on 10.02.2017.
 * s305070
 */
public class Main extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/gui.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Chess - Jesper|Mikkel");
        stage.setResizable(false);
        stage.show();
    }
}
