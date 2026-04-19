package Server;

public class Modelo {

    // Simula una base de datos con un Array de 5 preguntas
    private int contador = 0;
    
    // Array con las 5 preguntas
    private final String[] preguntas = {
        "1|¿Qué es UDP?|A) Un protocolo de comunicación|B) Un lenguaje de programación|C) Un tipo de base de datos|D) Un sistema operativo",
        "2|¿Cuál es la principal función de una API?|A) Almacenar datos|B) Proporcionar una interfaz de comunicación|C) Diseñar páginas web|D) Crear gráficos",
        "3|¿Qué es TCP?|A) Un protocolo de transporte|B) Un lenguaje de scripting|C) Un tipo de servidor|D) Una red social",
        "4|¿Cuántos bits tiene una dirección IPv4?|A) 32 bits|B) 16 bits|C) 64 bits|D) 128 bits",
        "5|¿Qué puerto usa HTTPS por defecto?|A) 80|B) 443|C) 8080|D) 21"
    };
    
    // Array con las respuestas correctas
    private final String[] respuestasCorrectas = {
        "A)",  // Pregunta 1: UDP es un protocolo de comunicación
        "B)",  // Pregunta 2: La función de una API es proporcionar interfaz de comunicación
        "A)",  // Pregunta 3: TCP es un protocolo de transporte
        "A)",  // Pregunta 4: IPv4 tiene 32 bits
        "B)"   // Pregunta 5: HTTPS usa el puerto 443
    };

    // Devuelve una pregunta en el formato: ID|Pregunta|A|B|C|D
    public String obtenerPregunta() {
        contador++;
        // Usamos % 5 para iterar entre las 5 preguntas
        int indice = (contador - 1) % 5;
        return preguntas[indice];
    }

    // Validación de la respuesta
    public String validarRespuesta(String idPregunta, String respuestaSeleccionada) {
        try {
            // Convertir el ID a índice (ID 1 = índice 0, ID 2 = índice 1, etc.)
            int indice = Integer.parseInt(idPregunta) - 1;
            
            // Verificar que el índice sea válido
            if (indice < 0 || indice >= 5) {
                return "Error! ID de pregunta inválido";
            }
            
            // Verificar si la respuesta es correcta
            String respuestaCorrecta = respuestasCorrectas[indice];
            
            if (respuestaSeleccionada.startsWith(respuestaCorrecta)) {
                return "RESPUESTA CORRECTA";
            } else {
                return " RESPUESTA INCORRECTA";
            }
            
        } catch (NumberFormatException e) {
            return "Error! ID de pregunta inválido";
        }
    }
}
