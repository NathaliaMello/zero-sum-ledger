# notificacao-consumer

Consumidor Kafka dos eventos publicados pelo ledger via outbox e Debezium. Envia notificações (simuladas) sobre os lançamentos e prova que o consumo é idempotente.

## Responsabilidades

- Consumir os eventos de lançamento publicados pelo CDC (Debezium) a partir da tabela de outbox.
- **Consumo idempotente**: deduplicar pelo ID do evento, para que reentregas, rebalanceamentos e reprocessamentos não gerem notificação duplicada.
- Tratar mensagens inválidas sem travar a partição (retry e dead-letter topic).

## Fases

| Fase | Critério de pronto |
|------|--------------------|
| 4 · Outbox + Debezium | Kafka derrubado no meio da carga: nada perdido, nada duplicado |
| 6 · Reconciliação e caos | Notificações conferem 1:1 com os lançamentos após falhas injetadas |

## Dependências

- Kafka e Debezium da infra local: ver [`/infra`](../infra)
