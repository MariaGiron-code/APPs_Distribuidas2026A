package taller2_Hilos.model;

import taller2_Hilos.controller.CarController;
import taller2_Hilos.util.Database;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Modelo {
    // Definición de los atributos de las clases Car y Database
    // para obtener las caracteristicas del vehiculo y la conexión a MongoDB
    private CarController car;
    private Database database;
    private boolean operacionExitosa;

    //Contructor para inizializar la conexión a la DB
    public Modelo(){
        database = new Database();
    }

    //Definición de los métodos para el manejo de datos del vehículo
    public boolean saveCar(CarController nuevoCarro) {
        // 1. Asignar el carro al modelo
        this.car = nuevoCarro;

        // 2. Llamar a Database para guardar
        boolean resultado = database.saveCar(car);

        // 3. Guardar el resultado
        this.operacionExitosa = resultado;
        return resultado;
    }

}
