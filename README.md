# Spring Cloud Stream

## Configurando Container Docker Kafka

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

#### Parando os containers:

```bash
docker-compose down
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

## Configurando Container Docker RabbitMQ

### Criar arquivo docker-compose.yml:

```yml
version: '3.8'

services:
  rabbitmq:
    image: rabbitmq:management
    container_name: rabbitmq
    ports:
      - "5672:5672" # Porta para conexões AMQP
      - "15672:15672" # Porta para o painel de gerenciamento web
    environment:
      RABBITMQ_DEFAULT_USER: guest # Usuário padrão
      RABBITMQ_DEFAULT_PASS: guest # Senha padrão
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq # Persistência de dados

volumes:
  rabbitmq_data:
```

#### Subir o container:
```bash
docker-compose up -d
```

#### Acessar o RabbitMQ
Acesse o painel de administração no navegador:
http://localhost:15672. Use o usuário e senha definidos nas variáveis de ambiente (ex.: admin/admin).

#### Entrar no container RabbitMQ
```bash
docker exec -it rabbitmq-container bash
```

#### Listar as filas
```bash
rabbitmqctl list_queues
```
#### Listar as filas com mais detalhes
```bash
rabbitmqctl list_queues name messages_ready messages_unacknowledged consumers
```


#### Parar o container
```bash
docker-compose down
```
#### Vincular uma fila ao Exchange
Execute o comando a seguir para criar uma fila chamada test-queue para extrair dados de transação
```bash
docker exec -it rabbitmq sh -c "rabbitmqadmin declare queue name=test-queue && rabbitmqadmin declare binding source=approvalRequest-out-0 destination=test-queue routing_key=#"
```


#### Verificar os containers
```bash
docker ps --format 'table {{.Names}}\t{{.ID}}\t{{.Image}}\t{{.Status}}'
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
```bash
./gradlew bootRun --args="--spring.integration.poller.fixed-delay=100"
```

```bash
docker exec -it rabbitmq sh -c "rabbitmqadmin declare queue name=test-queue && rabbitmqadmin declare binding source=approvalRequest-out-0 destination=test-queue routing_key=#"
```


