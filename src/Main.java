import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("page0.fxml"));
        Parent root = loader.load();
        root.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        Controller controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setTitle("Strukturformel-Zeichner");
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(false);
        System.out.println(getClass().getResource("../data/Placeholder-Icon.png"));
        //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("Placeholder-Icon.png")));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
