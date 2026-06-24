#Imagen base de Python 3.12 en su versión slim para reducir el tamaño de la imagen
FROM python:3.12-slim 

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 5000

CMD ["python", "app.py"]