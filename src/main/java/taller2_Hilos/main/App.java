package taller2_Hilos.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * App - Punto de entrada de la aplicación JavaFX
 * Este archivo lanza la ventana definida en viewCar.fxml
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML
        Parent root = FXMLLoader.load(getClass().getResource("/taller2_Hilos/view/viewCar.fxml"));
        
        // Crear la escena
        Scene scene = new Scene(root);
        
        // Configurar la ventana
        primaryStage.setTitle("APP TSDScar ESFOT");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Lanzar la aplicación JavaFX
        launch(args);
    }
}
