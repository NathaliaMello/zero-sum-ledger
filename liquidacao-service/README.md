# liquidacao-service

Serviço independente que liquida as operações registradas no ledger, ou seja, efetiva a movimentação junto a um sistema externo simulado. Existe para que o ledger precise coordenar uma transação distribuída entre serviços.

## Responsabilidades

- Executar o passo de liquidação de uma operação iniciada no ledger.
- Responder com sucesso ou falha. Uma falha aqui dispara as compensações da saga.
- Ser idempotente: o mesmo comando de liquidação recebido mais de uma vez tem um único efeito.
- Permitir a injeção de falhas (erro, timeout, lentidão) para os testes de saga e de caos.

## Fases

| Fase | Critério de pronto |
|------|--------------------|
| 5 · Saga entre serviços | Falha no último passo dispara compensações e o ledger termina consistente |
| 6 · Reconciliação e caos | 1 milhão de operações com falhas injetadas e a reconciliação fecha |

## Integração

- Recebe comandos e publica resultados via Kafka. Os tópicos serão documentados em [`/docs`](../docs).
- Não acessa o banco do ledger: a comunicação acontece só por mensagens.
