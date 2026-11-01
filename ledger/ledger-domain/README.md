# ledger-domain

O núcleo do ledger, em Java puro: sem Spring, sem banco e sem framework de mensageria. Aqui ficam as regras que garantem que nenhum centavo é perdido ou inventado.

## Responsabilidades

- **Money**: valor monetário exato (nada de `double`), com moeda e regras de arredondamento explícitas.
- **Conta**: identidade, tipo (ativo, passivo etc.) e saldo derivado dos lançamentos.
- **Transação**: conjunto de lançamentos que só pode ser criado se estiver balanceado (soma dos débitos = soma dos créditos).
- **Lançamento**: débito ou crédito de um valor em uma conta, imutável depois de criado.
- **Eventos de domínio** (Fase 3): fatos imutáveis que servem de base para o event sourcing.

## Fases

| Fase | Papel deste módulo |
|------|--------------------|
| 0 · Modelagem do domínio | Entrega principal |
| 3 · Event sourcing + CQRS | Define os eventos e a reconstrução do agregado a partir deles |
| 6 · Reconciliação e caos | Invariantes verificados com property-based testing (jqwik) |

## Critério de pronto (Fase 0)

> Transação desbalanceada não pode existir, domínio testado sem banco.

- É impossível instanciar uma `Transação` desbalanceada: o construtor ou a factory rejeita.
- Os testes rodam só com JUnit e AssertJ, sem nenhuma infraestrutura.
- Testes de arquitetura (ArchUnit) garantem que o módulo não depende de frameworks nem de infraestrutura.

## Como testar

```bash
mvn -pl ledger-domain test   # a partir de ledger/
```
