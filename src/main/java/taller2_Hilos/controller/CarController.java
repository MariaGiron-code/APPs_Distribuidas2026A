package taller2_Hilos.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class CarController {
    // Patron de diseño builder
    // Atributos para la clase Car
    @Getter private int code;
    @Getter private String marca;
    @Getter private String modelo;
    @Getter @Setter private double precio;
}
