import sqlite3

conn = sqlite3.connect("provincias.db")
cursor = conn.cursor()

cursor.execute("DROP TABLE IF EXISTS provincias")
cursor.execute("""
    CREATE TABLE provincias (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        nombre TEXT NOT NULL,
        capital TEXT NOT NULL,
        area_km2 REAL NOT NULL,
        poblacion INTEGER NOT NULL,
        latitud REAL NOT NULL,
        longitud REAL NOT NULL
    )
""")

provincias = [
    ("Azuay", "Cuenca", 8000, 881394, -2.9001, -79.0059),
    ("Bolívar", "Guaranda", 3254, 209933, -1.5905, -79.0011),
    ("Cañar", "Azogues", 3122, 206981, -2.7397, -78.8466),
    ("Carchi", "Tulcán", 3605, 172983, 0.8129, -77.7172),
    ("Chimborazo", "Riobamba", 6072, 524004, -1.6635, -78.6546),
    ("Cotopaxi", "Latacunga", 6569, 488716, -0.9332, -78.6155),
    ("El Oro", "Machala", 5850, 715751, -3.2581, -79.9554),
    ("Esmeraldas", "Esmeraldas", 15239, 643654, 0.9592, -79.6837),
    ("Galápagos", "Puerto Baquerizo Moreno", 8010, 33042, -0.9022, -89.6080),
    ("Guayas", "Guayaquil", 17139, 4387434, -2.1894, -79.8891),
    ("Imbabura", "Ibarra", 4599, 476257, 0.3517, -78.1223),
    ("Loja", "Loja", 11027, 521154, -3.9931, -79.2042),
    ("Los Ríos", "Babahoyo", 7175, 921763, -1.8021, -79.5346),
    ("Manabí", "Portoviejo", 18400, 1562079, -1.0546, -80.4546),
    ("Morona Santiago", "Macas", 25690, 190479, -2.3081, -78.1147),
    ("Napo", "Tena", 12476, 133705, -0.9938, -77.8131),
    ("Orellana", "Puerto Francisco de Orellana", 22000, 171159, -0.4654, -76.9836),
    ("Pastaza", "Puyo", 29520, 119184, -1.4924, -78.0022),
    ("Pichincha", "Quito", 9494, 3228233, -0.1807, -78.4678),
    ("Santa Elena", "Santa Elena", 3763, 401178, -2.2266, -80.8588),
    ("Santo Domingo de los Tsáchilas", "Santo Domingo", 3857, 536732, -0.2542, -79.1719),
    ("Sucumbíos", "Nueva Loja", 18612, 230503, 0.0867, -76.8889),
    ("Tungurahua", "Ambato", 3334, 590600, -1.2417, -78.6197),
    ("Zamora Chinchipe", "Zamora", 10584, 115412, -4.0677, -78.9558),
]

cursor.executemany(
    "INSERT INTO provincias (nombre, capital, area_km2, poblacion, latitud, longitud) VALUES (?, ?, ?, ?, ?, ?)",
    provincias
)

conn.commit()
conn.close()
print(f"Base de datos creada con {len(provincias)} provincias.")