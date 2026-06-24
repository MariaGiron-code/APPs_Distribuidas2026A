from flask import Flask, render_template
import sqlite3
import socket
import os

app = Flask(__name__)

DB_PATH = os.path.join(os.path.dirname(__file__), "provincias.db")

def obtener_provincias():
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row #Permite acceder a las columnas por nombre y no solo por índice
    cursor = conn.cursor()
    cursor.execute("SELECT nombre, capital, area_km2, poblacion, latitud, longitud FROM provincias")
    filas = cursor.fetchall()
    conn.close()
    return [dict(fila) for fila in filas] #Convertimos cada fila a un diccionario para facilitar su uso en la plantilla

@app.route("/")
def inicio():
    provincias = obtener_provincias()
    servidor = socket.gethostname()
    return render_template("index.html", provincias=provincias, servidor=servidor)

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=5000)