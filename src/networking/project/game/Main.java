package networking.project.game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("menu/Scene.fxml"));
        primaryStage.setTitle("Battle Arena - Menu");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false); //Locks the stage so there aren't weird resolutions
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
