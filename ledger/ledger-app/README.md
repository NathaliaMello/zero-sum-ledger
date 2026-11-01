# ledger-app

A aplicação executável do ledger. Expõe a API, persiste os lançamentos e publica os eventos, sempre usando o `ledger-domain` como fonte das regras de negócio.

## Responsabilidades

- **Persistência** das contas e transações, com controle de concorrência (locks, nível de isolamento, ordenação de locks para evitar deadlock).
- **API HTTP** idempotente: toda requisição de escrita exige um `Idempotency-Key`, e retries do cliente são seguros.
- **Event store e projeções** (CQRS): o lado de escrita grava eventos, o de leitura mantém projeções e snapshots reconstruíveis.
- **Outbox transacional**: o evento é gravado na mesma transação do lançamento, e o Debezium o captura e publica no Kafka.
- **Participação na saga** com o `liquidacao-service`, incluindo os lançamentos de compensação.

## Fases

| Fase | Critério de pronto |
|------|--------------------|
| 1 · Concorrência | 100 threads cruzadas: zero deadlock, saldo total do sistema inalterado |
| 2 · API idempotente | Mesma requisição 10x em paralelo gera exatamente um lançamento |
| 3 · Event sourcing + CQRS | Projeção apagada e reconstruída fica idêntica à original |
| 4 · Outbox + Debezium | Kafka derrubado no meio da carga: nada perdido, nada duplicado |
| 5 · Saga entre serviços | Falha no último passo dispara compensações e o ledger termina consistente |

## Dependências

- `ledger-domain`
- Infra local (banco, Kafka, Debezium): ver [`/infra`](../../infra)
