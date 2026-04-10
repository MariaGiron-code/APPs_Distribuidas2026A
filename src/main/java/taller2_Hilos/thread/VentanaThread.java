package taller2_Hilos.thread;

import taller2_Hilos.model.Modelo;
import taller2_Hilos.controller.CarController;
import taller2_Hilos.controller.CarFXMLController;

//Declarar la clase que extiende Thread
public class VentanaThread extends Thread {
    private CarController car;
    private Modelo modelo;
    private CarFXMLController controlador;

    // Constructor para recibir los datos del modelo, la clase car y el controlador
    public VentanaThread(Modelo modelo, CarController car, CarFXMLController controlador) {
        this.modelo = modelo;
        this.car = car;
        this.controlador = controlador;
    }


    @Override
    //Generamos un hilo separado para ejecutar la logica de guardado de los datos en Mongo
    public void run() {
        // Mensaje de inicio
        controlador.setMensaje("Iniciando guardado de: " + car.getMarca());
        
        //Primero llamamos al metodo saveCar
        boolean resultado = modelo.saveCar(car); //Capturamos el resultado true se guardo bien la información del vehículo o falso hubo algun error

        //Luego validamos el resultado
        if (resultado) {
            String msg = "Vehículo guardado exitosamente!";
            System.out.println(msg);
            controlador.setMensaje(msg);
        }else{
            String msg = "Error al guardar información del vehículo";
            System.out.println(msg);
            controlador.setMensaje(msg);
        }
    }

}






