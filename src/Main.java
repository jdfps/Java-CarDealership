import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application
{
    public static void main(String[] args)
    {
        System.out.println("We are in main");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login_resources.fxml")));
        Scene scene = new Scene(parent);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Used Car Dealership Program");
        stage.show();
    }
}