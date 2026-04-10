package taller2_Hilos.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import taller2_Hilos.controller.CarController;

/**
 * Clase Database - Maneja la conexión la DB en Mongo y las operaciones
 * Se utiliza el driver MongoDB Java para conectar y guardar los datos en documntos
 */
public class Database {
    
    // Configuración de conexión MongoDB
    private static final String MONGO_URI = "mongodb+srv://MariaG:1234@cluster0.vqrcwld.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    private static final String DATABASE_NAME = "ThreadJava";
    private static final String COLLECTION_NAME = "collection-car";
    
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    
    /**
     * Constructor - Inicializa la conexión a MongoDB
     */
    public Database() {
        try {
            // Crear cliente de MongoDB con la URI de la conexión, los datos de la DB y la colección correspondiente.
            mongoClient = MongoClients.create(MONGO_URI);

            database = mongoClient.getDatabase(DATABASE_NAME);

            collection = database.getCollection(COLLECTION_NAME);
            
            System.out.println(" Conexión a MongoDB éxitoosa");
        } catch (Exception e) {
            System.err.println(" Error al conectar a MongoDB: " + e.getMessage());
            throw new RuntimeException("No se pudo conectar a la base de datos", e);
        }
    }
    
    /**
     * Metodo para guardar un vehículo en MongoDB
     * @param car Objeto CarController con los datos del vehículo
     * @return true si el guardado fue exitoso, false en caso de que falle.
     */
    public boolean saveCar(CarController car) {
        try {
            // Crear un documento BSON con los datos del carro
            Document documento = new Document()
                    .append("code", car.getCode())
                    .append("marca", car.getMarca())
                    .append("modelo", car.getModelo())
                    .append("precio", car.getPrecio());
            
            // Insertar el documento en la colección
            InsertOneResult resultado = collection.insertOne(documento);
            
            // Verificar si la inserción fue exitosa
            if (resultado.getInsertedId() != null) {
                System.out.println("Informacion del vehículo guardada exitosamente!");
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            System.err.println("Error al guardar la información del vehículo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Metodo para cerrar la conexión a MongoDB
     * Debe llamarse al terminar de usar la base de datos
     */
    public void cerrarConexion() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexión a MongoDB cerrada");
        }
    }
}
