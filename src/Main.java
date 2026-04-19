import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        // Cargar el archivo FXML
        Parent root = FXMLLoader.load(getClass().getResource("Client/View/VistaAppQ.fxml"));
        
        // Crear la escena
        Scene scene = new Scene(root);
        
        // Configurar el stage
        stage.setTitle("TSDS Question APP");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        // Lanzar la aplicación JavaFX
        launch(args);
    }
}