# docs

Documentação do projeto: as decisões, o roadmap e o registro de cada fase.

## Conteúdo

- **Roadmap**: as fases de 0 a 6 e seus critérios de pronto.
- **ADRs** (Architecture Decision Records): por que cada escolha foi feita (tipo de lock, nível de isolamento, estratégia de idempotência, saga orquestrada ou coreografada etc.).
- **Diagramas**: fluxo de uma transação, outbox/CDC e sequência da saga com compensações.
- **Contratos**: API HTTP e esquema dos eventos e tópicos Kafka.
- **Relatórios de caos**: resultados da Fase 6 (falhas injetadas, volume processado, resultado da reconciliação).

## Roadmap

| Fase | Tema | Critério de pronto |
|------|------|--------------------|
| 0 | Modelagem do domínio | Transação desbalanceada não pode existir, domínio testado sem banco |
| 1 | Concorrência | 100 threads cruzadas: zero deadlock, saldo total do sistema inalterado |
| 2 | API idempotente | Mesma requisição 10x em paralelo gera exatamente um lançamento |
| 3 | Event sourcing + CQRS | Projeção apagada e reconstruída fica idêntica à original |
| 4 | Outbox + Debezium | Kafka derrubado no meio da carga: nada perdido, nada duplicado |
| 5 | Saga entre serviços | Falha no último passo dispara compensações e o ledger termina consistente |
| 6 | Reconciliação e caos | 1 milhão de operações com falhas injetadas e a reconciliação fecha |
