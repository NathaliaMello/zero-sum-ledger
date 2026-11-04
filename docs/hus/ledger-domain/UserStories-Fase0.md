# Histórias de usuário — Fase 0

## Sumário

- [HU01 — Representar valores monetários](#hu01)
- [HU02 — Somar, subtrair e comparar valores](#hu02)
- [HU03 — Ratear um valor sem perder centavos](#hu03)
- [HU03.1 — Ratear um valor por pesos](#hu03.1)
- [HU04 — Aplicar percentual com regra de arredondamento](#hu04)
- [HU05 — Abrir conta](#hu05)
- [HU06 — Bloquear e desbloquear conta](#hu06)
- [HU07 — Registrar transação balanceada](#hu07)
- [HU08 — Impedir transações anômalas](#hu08)
- [HU09 — Calcular o saldo de uma conta](#hu09)
- [HU10 — Validar a transação contra o estado das contas](#hu10)
- [HU11 — Encerrar conta](#hu11)
- [HU12 — Estornar transação](#hu12)

<a id="hu01"></a>

## HU01 — Representar valores monetários

> **Como** analista contábil,
> **quero** que todo valor monetário tenha moeda e exatamente duas casas decimais,
> **para** que nenhum valor inválido entre nos livros.

### Regras

- **RDD01** O Dinheiro possui uma, e apenas uma, moeda obrigatória.
- **RDD02** O valor do Dinheiro possui sempre 2 casas decimais; um valor com menos casas é completado com zeros (10 → 10,00) e um valor com mais de 2 casas decimais não será aceito.
- **RDD03** O valor do Dinheiro pode ser negativo.
- **RDD07** Dois Dinheiros são iguais quando têm o mesmo valor e a mesma moeda (10,0 é igual a 10,00).

### Critérios de aceite

- [x] Dado o valor 10 em BRL, quando crio o Dinheiro, então o valor é 10,00.
- [x] Dado o valor 10,005 em BRL, quando crio o Dinheiro, então a criação é rejeitada.
- [x] Dado um valor sem moeda, quando crio o Dinheiro, então a criação é rejeitada.
- [x] Dado o valor −50 em BRL, quando crio o Dinheiro, então ele é aceito com valor −50,00.
- [x] Dado 10,0 BRL e 10,00 BRL, quando comparo os dois, então são iguais e têm o mesmo hash.
- [x] Dado 10,00 BRL e 10,00 USD, quando comparo os dois, então são diferentes.

> Entrgue em: `PR #1` 

<a id="hu02"></a>

## HU02 — Somar, subtrair e comparar valores

> **Como** analista contábil,
> **quero** somar, subtrair e comparar valores da mesma moeda,
> **para** que os cálculos nunca misturem moedas.

### Regras

- **RDD04** Operações (somar, subtrair, comparar) só são permitidas entre Dinheiros da mesma moeda.
- **RDD06** O Dinheiro é imutável: toda operação gera um novo Dinheiro.

### Critérios de aceite

- [x] Dado 10,00 BRL e 5,50 BRL, quando somo, então o resultado é 15,50 BRL.
- [x] Dado 10,00 BRL e 15,00 BRL, quando subtraio o segundo do primeiro, então o resultado é −5,00 BRL.
- [x] Dado 10,00 BRL e 5,00 BRL, quando comparo, então o primeiro é maior.
- [x] Dado 10,00 BRL e 5,00 USD, quando somo, subtraio ou comparo, então a operação é rejeitada.
- [x] Dado 10,00 BRL, quando somo 5,00 BRL a ele, então o Dinheiro original continua valendo 10,00.

> Entrgue em: `PR #2` 

<a id="hu03"></a>

## HU03 — Ratear um valor sem perder centavos

> **Como** operadora do backoffice,
> **quero** dividir um valor em partes,
> **para** que um rateio nunca perca nem crie centavos.

### Regras

- **RDD08** Ao dividir um Dinheiro em partes, a soma das partes é exatamente igual ao original, sem perder nem criar centavos.

### Critérios de aceite

- [x] Dado 100,00 BRL, quando divido em 3 partes, então recebo 33,34, 33,33 e 33,33, somando 100,00.
- [x] Dado 100,00 BRL, quando divido em 4 partes, então recebo quatro partes de 25,00.
- [x] Dado 0,01 BRL, quando divido em 3 partes, então recebo 0,01, 0,00 e 0,00.
- [x] Dado qualquer valor, quando divido em zero ou menos partes, então a operação é rejeitada.

> Entrgue em: `PR #3`

<a id="hu03.1"></a>

## HU03.1 — Ratear um valor por pesos

> **Como** operadora do backoffice,
> **quero** dividir um valor em partes proporcionais a pesos que eu informo,
> **para** que rateios desiguais também nunca percam nem criem centavos.

### Regras

- **RDD09** ao ratear um Dinheiro por pesos, cada parte é proporcional ao seu peso, e a soma das partes é exatamente igual ao original;
- **RDD10** os pesos são números inteiros livres: não precisam somar 100 (3/1/1 equivale a 60/20/20);
- **RDD11** é obrigatório informar pelo menos um peso, nenhum peso pode ser negativo e a soma dos pesos deve ser maior que zero;
- **RDD12** uma parte com peso zero recebe 0,00;
- **RDD13** quando a divisão não for exata, os centavos que sobram vão, um por parte, para as primeiras partes.

### Critérios de aceite

- [x] Dado 100,00 BRL e pesos 3/1/1, quando rateio, então recebo 60,00 · 20,00 · 20,00
- [x] Dado 100,00 BRL e pesos 50/25/25, quando rateio, então recebo 50,00 · 25,00 · 25,00
- [x] Dado 0,05 BRL e pesos 70/30, quando rateio, então recebo 0,04 · 0,01
- [x] Dado 100,00 BRL e pesos 1/1/1, quando rateio, então recebo 33,34 · 33,33 · 33,33, o mesmo resultado de dividir em 3 partes iguais
- [x] Dado −100,00 BRL e pesos 3/1/1, quando rateio, então recebo −60,00 · −20,00 · −20,00
- [x] Dado 100,00 BRL e pesos 3/0/1, quando rateio, então recebo 75,00 · 0,00 · 25,00
- [x] Dado nenhum peso, quando rateio, então a operação é rejeitada
- [x] Dado algum peso negativo, quando rateio, então a operação é rejeitada
- [x] Dado pesos 0/0/0, quando rateio, então a operação é rejeitada
- [x] Dado qualquer valor e quaisquer pesos válidos, quando rateio, então a soma das partes é igual ao original

> Entrgue em: `RP#5`

<a id="hu04"></a>

## HU04 — Aplicar percentual com regra de arredondamento

> **Como** analista contábil,
> **quero** informar a regra de arredondamento ao aplicar um percentual,
> **para** que cada cálculo siga a norma que o rege (como a REINF, que trunca).

### Regras

- **RDD05** Operações que podem gerar mais de 2 casas decimais (multiplicar, dividir, percentual) exigem que quem as solicita informe a regra de arredondamento.

### Critérios de aceite

- [x] Dado 10% de 10,05 BRL (1,005), quando aplico com truncamento, então o resultado é 1,00.
- [x] Dado 10% de 10,05 BRL, quando aplico com arredondamento meio para cima, então o resultado é 1,01.
- [x] Dado 10% de 10,05 BRL, quando aplico com arredondamento meio para o par, então o resultado é 1,00.
- [x] Dado qualquer percentual, quando não informo a regra de arredondamento, então a operação é rejeitada.

> Entrgue em: `PR#6`

<a id="hu05"></a>

## HU05 — Abrir conta

> **Como** operadora do backoffice,
> **quero** abrir uma conta informando descrição, tipo contábil, moeda e limite,
> **para** que ela possa ser movimentada.

### Regras

- **RDC01** Toda Conta possui um identificador único.
- **RDC02** Toda Conta possui uma moeda, definida na criação e que nunca muda.
- **RDC03** O limite nunca é negativo e está sempre na mesma moeda da Conta.
- **RDC04** Toda Conta nasce com status ABERTA.
- **RCD01** Toda Conta possui um tipo contábil, ATIVO ou PASSIVO, definido na criação e que nunca muda.

### Critérios de aceite

- [ ] Dada uma descrição, tipo PASSIVO, moeda BRL e limite 0,00 BRL, quando abro a conta, então ela recebe um identificador e nasce com status ABERTA.
- [ ] Dado um limite de −10,00 BRL, quando abro a conta, então a abertura é rejeitada.
- [ ] Dada uma conta em BRL e um limite em USD, quando abro a conta, então a abertura é rejeitada.
- [ ] Dada a ausência de tipo contábil ou de moeda, quando abro a conta, então a abertura é rejeitada.
- [ ] Dada uma conta aberta, quando procuro uma forma de alterar seu tipo ou sua moeda, então ela não existe.

<a id="hu06"></a>

## HU06 — Bloquear e desbloquear conta

> **Como** operadora do backoffice,
> **quero** bloquear uma conta e depois desbloqueá-la,
> **para** que eu possa cumprir uma restrição, como uma ordem judicial, sem encerrar a conta.

### Regras

- **RDC05** Uma Conta *ABERTA* pode ser *BLOQUEADA*, e uma Conta *BLOQUEADA* pode voltar a *ABERTA*.

### Critérios de aceite

- [ ] Dada uma conta ABERTA, quando bloqueio, então o status passa a BLOQUEADA.
- [ ] Dada uma conta BLOQUEADA, quando desbloqueio, então o status volta a ABERTA.
- [ ] Dada uma conta BLOQUEADA, quando tento bloquear de novo, então a operação é rejeitada.
- [ ] Dada uma conta ABERTA, quando tento desbloquear, então a operação é rejeitada.
- [ ] Dada uma conta ENCERRADA, quando tento bloquear ou desbloquear, então a operação é rejeitada.

<a id="hu07"></a>

## HU07 — Registrar transação balanceada

> **Como** operadora do backoffice,
> **quero** registrar uma transação com seus lançamentos,
> **para** que valores se movimentem entre contas sem que o livro jamais fique desbalanceado.

### Regras

- **RDL01** Todo Lançamento pertence a uma Transação e só existe dentro dela.
- **RDL02** Todo Lançamento referencia uma única Conta, pelo seu identificador.
- **RDL03** Todo Lançamento possui uma natureza: DÉBITO ou CRÉDITO.
- **RDL04** O valor do Lançamento é sempre maior que zero; quem define se o saldo da Conta sobe ou desce é a natureza combinada com o tipo contábil da Conta.
- **RDL05** Um Lançamento nunca é alterado nem excluído.
- **RDT01** Toda Transação possui um identificador único.
- **RDT02** Toda Transação possui um tipo: TRANSFERÊNCIA, DEPÓSITO, SAQUE ou ESTORNO.
- **RDT03** Toda Transação possui uma descrição.
- **RDT04** Toda Transação registra o momento em que ocorreu, informado por quem a cria.
- **RDT05** Toda Transação possui pelo menos dois Lançamentos.
- **RDT07** A soma dos débitos de uma Transação é sempre igual à soma dos créditos.
- **RDT08** Uma Transação nunca é alterada nem excluída; um erro é corrigido por um estorno.

### Critérios de aceite

- [ ] Dado um débito de 100,00 na conta A e um crédito de 100,00 na conta B, quando registro uma TRANSFERÊNCIA com descrição e momento, então ela é aceita e recebe um identificador.
- [ ] Dado um débito de 60,00 na conta do voucher, um débito de 40,00 na conta do cartão e um crédito de 100,00 na conta do mercado, quando registro a transação, então ela é aceita.
- [ ] Dado um débito de 100,00 e um crédito de 90,00, quando registro a transação, então ela é rejeitada por estar desbalanceada.
- [ ] Dado um único lançamento, quando registro a transação, então ela é rejeitada.
- [ ] Dado um lançamento com valor zero ou negativo, quando crio o lançamento, então ele é rejeitado.
- [ ] Dada a ausência de tipo, descrição ou momento, quando registro a transação, então ela é rejeitada.
- [ ] Dada uma transação registrada, quando procuro uma forma de alterar seus dados ou sua lista de lançamentos, então ela não existe.

<a id="hu08"></a>

## HU08 — Impedir transações anômalas

> **Como** analista contábil,
> **quero** que transações com moedas misturadas ou contas repetidas sejam recusadas,
> **para** que o extrato de cada conta nunca mostre linhas sem sentido de negócio.

### Regras

- **RDT06** Todos os Lançamentos de uma Transação estão na mesma moeda.
- **RDT12** Uma mesma Conta aparece no máximo uma vez em cada Transação.

### Critérios de aceite

- [ ] Dado um débito de 100,00 BRL e um crédito de 100,00 USD, quando registro a transação, então ela é rejeitada.
- [ ] Dado um débito e um crédito na mesma conta, quando registro a transação, então ela é rejeitada.
- [ ] Dado um débito de 100,00 no caixa e dois créditos de 60,00 e 40,00 na mesma conta de cliente, quando registro a transação, então ela é rejeitada.

<a id="hu09"></a>

## HU09 — Calcular o saldo de uma conta

> **Como** analista contábil,
> **quero** obter o saldo de uma conta a partir dos seus lançamentos,
> **para** que o saldo seja sempre consequência do histórico, respeitando o tipo contábil.

### Regras

- **RCC02** Uma Conta ATIVO representa o que o banco tem ou tem a receber (ex.: caixa da agência, reservas no Banco Central, empréstimos concedidos). Nela, um débito aumenta o saldo e um crédito diminui.
- **RCC03** Uma Conta PASSIVO representa o que o banco deve a terceiros (ex.: conta de cliente, conta de depósito judicial). Nela, um crédito aumenta o saldo e um débito diminui.
- **RCC04** O saldo de uma Conta ATIVO é a soma dos débitos menos a soma dos créditos; o de uma Conta PASSIVO, a soma dos créditos menos a soma dos débitos.

### Critérios de aceite

- [ ] Dada uma conta sem lançamentos, quando calculo o saldo, então ele é 0,00.
- [ ] Dada uma conta PASSIVO com crédito de 100,00 e débito de 30,00, quando calculo o saldo, então ele é 70,00.
- [ ] Dada uma conta ATIVO com débito de 100,00 e crédito de 30,00, quando calculo o saldo, então ele é 70,00.
- [ ] Dado um depósito em espécie de 100,00 (débito no caixa ATIVO, crédito na conta do cliente PASSIVO), quando calculo os dois saldos, então ambos aumentaram 100,00.
- [ ] Dada uma conta PASSIVO com crédito de 50,00 e débito de 80,00, quando calculo o saldo, então ele é −30,00.

<a id="hu10"></a>

## HU10 — Validar a transação contra o estado das contas

> **Como** operadora do backoffice,
> **quero** que uma transação só seja efetivada se todas as contas envolvidas puderem recebê-la,
> **para** que nenhum limite, bloqueio ou encerramento seja violado.

### Regras

- **RSC01** O saldo da Conta pode ficar negativo somente até o seu limite.
- **RSC02** Uma Conta com limite zero nunca fica negativa.
- **RSC04** Uma Conta ENCERRADA não recebe lançamentos.
- **RSC05** Uma Conta BLOQUEADA não recebe nenhum novo lançamento até que seja reaberta.
- **RSL01** A Conta referenciada pelo Lançamento deve existir.
- **RSL02** A moeda do Lançamento é igual à moeda da Conta que ele referencia.
- **RST01** Uma Transação só é registrada se todas as Regras de Sistema das Contas e dos Lançamentos envolvidos forem atendidas.

### Critérios de aceite

- [ ] Dada uma conta PASSIVO com limite 0,00 e saldo 50,00, quando aplico um débito de 80,00, então a transação é rejeitada.
- [ ] Dada uma conta PASSIVO com limite 100,00 e saldo 50,00, quando aplico um débito de 120,00, então a transação é aceita e o saldo fica −70,00.
- [ ] Dada a mesma conta, quando aplico um débito de 200,00, então a transação é rejeitada, porque o saldo ficaria em −150,00.
- [ ] Dada uma conta ENCERRADA, quando uma transação a envolve, então ela é rejeitada.
- [ ] Dada uma conta BLOQUEADA, quando uma transação a debita ou a credita, então ela é rejeitada nos dois casos.
- [ ] Dado um lançamento que referencia uma conta inexistente, quando aplico a transação, então ela é rejeitada.
- [ ] Dado um lançamento em BRL numa conta em USD, quando aplico a transação, então ela é rejeitada.
- [ ] Dada uma transação rejeitada por qualquer conta, quando consulto os saldos de todas as contas envolvidas, então nenhum mudou.

<a id="hu11"></a>

## HU11 — Encerrar conta

> **Como** operadora do backoffice,
> **quero** encerrar uma conta zerada,
> **para** que ela deixe de ser movimentada sem que nenhum valor fique sem dono.

### Regras

- **RDC06** Uma Conta ENCERRADA nunca reabre.
- **RSC03** Uma Conta só pode ser ENCERRADA com saldo zero.

### Critérios de aceite

- [ ] Dada uma conta ABERTA com saldo 0,00, quando encerro, então o status passa a ENCERRADA.
- [ ] Dada uma conta BLOQUEADA com saldo 0,00, quando encerro, então o status passa a ENCERRADA.
- [ ] Dada uma conta ABERTA ou BLOQUEADA com saldo diferente de zero (positivo ou negativo), quando encerro, então a operação é rejeitada.
- [ ] Dada uma conta ENCERRADA, quando tento encerrar, bloquear ou reabrir, então a operação é rejeitada.

<a id="hu12"></a>

## HU12 — Estornar transação

> **Como** operadora do backoffice,
> **quero** estornar uma transação registrada por engano,
> **para** que as contas voltem ao estado anterior ao erro sem apagar o histórico.

### Regras

**Referências:** Transação D9, D10, D11, S2 e S3.

- **RDT09** Um estorno é uma nova Transação, do tipo ESTORNO, que referencia a Transação original e contém os mesmos Lançamentos com as naturezas invertidas.
- **RDT10** Somente uma Transação do tipo ESTORNO referencia outra Transação.
- **RDT11** Uma Transação do tipo ESTORNO nunca pode ser estornada; para refazer a operação, cria-se uma nova Transação.
- **RST02** A Transação referenciada por um estorno deve existir.
- **RST03** Uma Transação só pode ser estornada uma vez.

### Critérios de aceite

- [ ] Dada uma TRANSFERÊNCIA com débito de 100,00 em A e crédito de 100,00 em B, quando estorno, então é criada uma transação ESTORNO com crédito de 100,00 em A, débito de 100,00 em B e referência à transação original.
- [ ] Dada uma transação estornada, quando calculo os saldos de A e B, então são iguais aos de antes da transação original.
- [ ] Dada uma transação estornada, quando consulto o histórico, então a original e o estorno aparecem, e nenhuma delas foi apagada.
- [ ] Dada uma transação do tipo ESTORNO, quando tento estorná-la, então a operação é rejeitada.
- [ ] Dada uma transação já estornada, quando tento estorná-la de novo, então a operação é rejeitada.
- [ ] Dada uma transação de tipo diferente de ESTORNO, quando tento criá-la referenciando outra transação, então a criação é rejeitada.
- [ ] Dado um estorno que referencia uma transação inexistente, quando aplico, então ele é rejeitado.
