#JAVA 17 
En caso de tener java 17 cambiar en el pom la version de java a utilizar
#Ejecutar para iniciar mysql
1.-
docker build -t my-mysql-db .
2.-
docker run -d \
  --name mysql-container \
  -p 3306:3306 \
  -p 33060:33060 \
  my-mysql-db
#JAVA 21
En caso de tener java 21, asegúrese de que el entorno esté configurado correctamente.