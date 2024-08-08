# Use a imagem oficial da OpenJDK 21
FROM maven:3.9.6-amazoncorretto-21-al2023
LABEL author="Rafael Teixeira"

# Diretório de trabalho dentro do container
WORKDIR /app

# Copie o JAR da aplicação para o container
COPY . /app
RUN mvn clean package -DskipTests=true -Dmaven.test.skip=true

RUN cp /app/target/*.jar /app.jar

## Exponha a porta que a aplicação Spring Boot está utilizando (por exemplo, 8080)
EXPOSE 8080
#
## Comando para iniciar a aplicação quando o contêiner for iniciado
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.datasource.url=jdbc:postgresql://database:5432/fiappay?createDatabaseIfNotExist=true "]