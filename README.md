# Spring Cloud Stream

## Configurando Container Docker

### Criar arquivo docker-compose.yml:

```yml
version: '3.8'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    container_name: zookeeper
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - "2181:2181"

  kafka:
    image: confluentinc/cp-kafka:latest
    container_name: kafka
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```
#### Subir os containers:

```bash
docker-compose up -d
```

#### Verificar os containers:

```bash
docker ps
```

### Testar a Configuração:

#### Acessar o container Kafka:
```bash
docker exec -it kafka bash
```

#### Listar os tópicos:
```bash
kafka-topics --list --bootstrap-server localhost:9092
```

#### Ler mensagens (Consumer):
```bash
kafka-console-consumer --topic approvalRequest-out-0 --bootstrap-server localhost:9092 --from-beginning
```

## Comandos Gradle

```bash
./gradlew build
```
```bash
./gradlew test
```
```bash
./gradlew bootRun
```
```bash
./gradlew bootRun --args="--spring.integration.poller.fixed-delay=5000"
```

