package JavaDBProject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.GetScreenDimension;

public class App extends Application{

    private final static int WIDTH = GetScreenDimension.center().width * 70/100;
    private final static int HEIGHT = GetScreenDimension.center().height * 70/100;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("scenes/MainScene.fxml"));
        primaryStage.setTitle("DataBase Cooperativa Sanitaria");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
