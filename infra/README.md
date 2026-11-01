# infra

Ambiente local para subir e testar o sistema inteiro: banco, mensageria, CDC e injeção de falhas.

## Componentes

| Componente | Uso | Fase |
|------------|-----|------|
| Banco relacional | Persistência do ledger, event store e outbox | 1+ |
| Kafka | Transporte de eventos entre serviços | 4+ |
| Kafka Connect + Debezium | CDC da tabela de outbox para o Kafka | 4 |
| Toxiproxy | Injeção de latência, quedas e partições de rede | 6 |

## Uso

```bash
docker compose up -d      # sobe o ambiente
docker compose down -v    # derruba e apaga os volumes
```

## Segredos

As credenciais ficam em `.env`, que não é versionado. Copie `.env.example` para `.env` e ajuste os valores.
