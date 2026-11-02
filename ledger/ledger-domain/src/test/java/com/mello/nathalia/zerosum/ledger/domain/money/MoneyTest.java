package com.mello.nathalia.zerosum.ledger.domain.money;

import com.mello.nathalia.zerosum.ledger.domain.enuns.Moeda;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class MoneyTest {

    static Stream<Arguments> cenariosDeRateio() {
        return Stream.of(
                arguments("100.00", new int[] {3, 1, 1},   List.of("60.00", "20.00", "20.00")),
                arguments("100.00", new int[] {50, 25, 25}, List.of("50.00", "25.00", "25.00")),
                arguments("100.00", new int[] {1, 1, 1},   List.of("33.34", "33.33", "33.33")),
                arguments("100.00", new int[] {3, 0, 1},   List.of("75.00", "0.00", "25.00")),
                arguments("0.05",   new int[] {70, 30},    List.of("0.04", "0.01")),
                arguments("0.05",   new int[] {0, 1, 1},   List.of("0.00", "0.03", "0.02")),
                arguments("-100.00", new int[] {3, 1, 1},  List.of("-60.00", "-20.00", "-20.00")),
                arguments("-100.00", new int[] {1, 1, 1},  List.of("-33.34", "-33.33", "-33.33"))
        );
    }

    @Test
    void novoDinheiroSempreTeraDuasCasasDecimais() {
        var money = Money.of("10", Moeda.BRL);
        assertThat(money.valor()).isEqualTo(new BigDecimal("10.00"));
    }

    @Test
    void rejeitaValorComMaisDeDuasCasasDecimais() {
        assertThatThrownBy(() -> Money.of("10.005", Moeda.BRL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Valor com mais de 2 casas decimais: 10.005");

    }

    @Test
    void dinheiroSemDefinirValorEhRejeitado() {
        assertThatThrownBy(() -> Money.of(null, Moeda.BRL))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Valor é obrigatório");
    }

    @Test
    void dinheiroSemDefinirMoedaEhRejeitado() {
        assertThatThrownBy(() -> Money.of("10", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Moeda é obrigatória");
    }

    @Test
    void valorNegativoEhAceitoComDuasCasas() {
        var money = Money.of("-50", Moeda.BRL);
        assertThat(money.valor()).isEqualTo(new BigDecimal("-50.00"));
    }

    @Test
    void valoresComEscalasDiferentesSaoIguais() {
        var money10_0 = Money.of("10.0", Moeda.BRL);
        var money10_00 = Money.of("10.00", Moeda.BRL);
        assertThat(money10_0).isEqualTo(money10_00);
        assertThat(money10_0).hasSameHashCodeAs(money10_00);
    }

    @Test
    void dinheiroComMoedasDiferentesSaoDiferentes() {
        var moneyBrl = Money.of("10", Moeda.BRL);
        var moneyUsd = Money.of("10", Moeda.USD);
        assertThat(moneyBrl).isNotEqualTo(moneyUsd);
    }

    @Test
    void somarDinheiroComMesmaMoeda() {
        var money10 = Money.of("10", Moeda.BRL);
        var money5_50 = Money.of("5.50", Moeda.BRL);
        var resultado = money10.somar(money5_50);
        assertThat(resultado.valor()).isEqualTo(new BigDecimal("15.50"));
        assertThat(resultado.moeda()).isEqualTo(Moeda.BRL);
    }

    @Test
    void subtrairDinheiroComMesmaMoeda() {
        var money10_0 = Money.of("10.0", Moeda.BRL);
        var money15_00 = Money.of("15.00", Moeda.BRL);
        var resultado = money10_0.subtrair(money15_00);
        assertThat(resultado.valor()).isEqualTo(new BigDecimal("-5.00"));
        assertThat(resultado.moeda()).isEqualTo(Moeda.BRL);
    }

    @Test
    void compararDinheiroDiferenteDaMesmaMoedaRertornaVerdadeiro() {
        var money10_00 = Money.of("10.00", Moeda.BRL);
        var money5_00 = Money.of("5.00", Moeda.BRL);

        assertThat(money10_00.isMaiorQue(money5_00)).isTrue();
        assertThat(money5_00.isMenorQue(money10_00)).isTrue();
    }

    @Test
    void compararDinheiroDiferenteDaMesmaMoedaRertornaFalso() {
        var money10_00 = Money.of("10.00", Moeda.BRL);
        var money5_00 = Money.of("5.00", Moeda.BRL);

        assertThat(money5_00.isMaiorQue(money10_00)).isFalse();
        assertThat(money10_00.isMenorQue(money5_00)).isFalse();
    }

    @Test
    void compararDinheiroIgualDaMesmaMoeda() {
        var money10_00 = Money.of("10.00", Moeda.BRL);
        var money10_00_2 = Money.of("10.00", Moeda.BRL);
        assertThat(money10_00.isMaiorQue(money10_00_2)).isFalse();
        assertThat(money10_00.isMenorQue(money10_00_2)).isFalse();
    }

    @Test
    void varificarValorPositivo() {
        var moneyPositivo = Money.of("10.00", Moeda.BRL);
        assertThat(moneyPositivo.isPositivo()).isTrue();
        var moneyNegativo = Money.of("-10.00", Moeda.BRL);
        assertThat(moneyNegativo.isPositivo()).isFalse();
    }

    @Test
    void verificarValorNegativo() {
        var moneyNegativo = Money.of("-10.00", Moeda.BRL);
        assertThat(moneyNegativo.isNegativo()).isTrue();
        var moneyPositivo = Money.of("10.00", Moeda.BRL);
        assertThat(moneyPositivo.isNegativo()).isFalse();
    }

    @Test
    void verificarValorZerado() {
        var moneyZero = Money.of("0.00", Moeda.BRL);
        assertThat(moneyZero.isZero()).isTrue();
        assertThat(moneyZero.isPositivo()).isFalse();
        assertThat(moneyZero.isNegativo()).isFalse();
    }

    @Test
    void rejeitarOperacaoDinheiroOutroDinheiroNulo() {
        var money10 = Money.of("10", Moeda.BRL);
        assertThatThrownBy(() -> money10.somar(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Outro valor é obrigatório");

        assertThatThrownBy(() -> money10.subtrair(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Outro valor é obrigatório");
    }

    @Test
    void
    rejeitarOperacaoComMoedasDiferentes() {
        var moneyBRL = Money.of("10.00", Moeda.BRL);
        var moneyUSD = Money.of("10.00", Moeda.USD);

        assertThatThrownBy(() -> moneyBRL.somar(moneyUSD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Moedas diferentes:");

        assertThatThrownBy(() -> moneyBRL.subtrair(moneyUSD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Moedas diferentes:");

    }

    @Test
    void rejeitarComparacaoComMoedasDiferentes() {
        var moneyBRL = Money.of("10.00", Moeda.BRL);
        var moneyUSD = Money.of("10.00", Moeda.USD);

        assertThatThrownBy(() -> moneyBRL.isMaiorQue(moneyUSD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Moedas diferentes:");

        assertThatThrownBy(() -> moneyBRL.isMenorQue(moneyUSD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Moedas diferentes:");
    }

    @Test
    void realizarOperacaoComDinheiroSempreGeraNovoDinheiro() {
        var money1 = Money.of("10.00", Moeda.BRL);
        var money2 = Money.of("10.00", Moeda.BRL);
        var money3 = money1.somar(money2);

        assertThat(money3).isNotSameAs(money1);
        assertThat(money3).isNotSameAs(money2);
        assertThat(money3).isNotEqualTo(money1);
        assertThat(money3).isNotEqualTo(money2);
        assertThat(money1.valor()).isEqualTo(new BigDecimal("10.00"));
        assertThat(money2.valor()).isEqualTo(new BigDecimal("10.00"));
    }

    @Test
    void alocar100PositivoCorretamentePara3Partes() {
        var money = Money.of("100.00", Moeda.BRL);
        var partes = money.alocar(3);

        assertThat(partes).containsExactly(
                Money.of("33.34", Moeda.BRL),
                Money.of("33.33", Moeda.BRL),
                Money.of("33.33", Moeda.BRL)
        );

    }

    @Test
    void alocar100NegativoCorretamentePara3Partes() {
        var money = Money.of("-100.00", Moeda.BRL);
        var partes = money.alocar(3);

        assertThat(partes).containsExactly(
                Money.of("-33.34", Moeda.BRL),
                Money.of("-33.33", Moeda.BRL),
                Money.of("-33.33", Moeda.BRL)
        );

    }

    @Test
    void alocar0CorretamentePara3Partes() {
        var money = Money.of("0", Moeda.BRL);
        var partes = money.alocar(3);

        assertThat(partes).containsExactly(
                Money.of("0.00", Moeda.BRL),
                Money.of("0.00", Moeda.BRL),
                Money.of("0.00", Moeda.BRL)
        );
    }

    @Test
    void alocar100CorretamentePara4Partes() {
        var money = Money.of("100.00", Moeda.BRL);
        var partes = money.alocar(4);

        assertThat(partes.size()).isEqualTo(4);
        assertThat(partes.get(0).valor()).isEqualTo(new BigDecimal("25.00"));
        assertThat(partes.get(1).valor()).isEqualTo(new BigDecimal("25.00"));
        assertThat(partes.get(2).valor()).isEqualTo(new BigDecimal("25.00"));
        assertThat(partes.get(3).valor()).isEqualTo(new BigDecimal("25.00"));
    }

    @Test
    void alocar1CentavoCorretamentePara3Partes() {
        var money = Money.of("0.01", Moeda.BRL);
        var partes = money.alocar(3);

        assertThat(partes.size()).isEqualTo(3);
        assertThat(partes.get(0).valor()).isEqualTo(new BigDecimal("0.01"));
        assertThat(partes.get(1).valor()).isEqualTo(new BigDecimal("0.00"));
        assertThat(partes.get(2).valor()).isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    void rejeitarAlocacaoDeDinheiroParaQuantidadeInvalida() {
        var money = Money.of("100.00", Moeda.BRL);
        assertThatThrownBy(() -> money.alocar(0))
            .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Número de partes deve ser maior que zero");

        assertThatThrownBy(() -> money.alocar(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Número de partes deve ser maior que zero");
    }

    @Test
    void somaDasPartesEhIgualAoOriginal() {
        var original = Money.of("100.00", Moeda.BRL);
        var soma = original.alocar(3).stream()
                .reduce(Money.of("0", Moeda.BRL), Money::somar);

        assertThat(soma).isEqualTo(original);
    }

    @Test
    void listaDePartesEhImutavel() {
        var original = Money.of("100.00", Moeda.BRL);
        var partes = original.alocar(3);
        assertThatThrownBy(() -> partes.add(Money.of("1.00", Moeda.BRL)))
                .isInstanceOf(UnsupportedOperationException.class);

        assertThatThrownBy(() -> partes.removeFirst())
                .isInstanceOf(UnsupportedOperationException.class);

        assertThatThrownBy(() -> partes.set(0, Money.of("50.00", Moeda.BRL)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @ParameterizedTest(name = "{0} com pesos {1} -> {2}")
    @MethodSource("cenariosDeRateio")
    void ratearComValoresEPesosDiferentes(String valor, int[] pesos, List<String> esperado) {
        var original = Money.of(valor, Moeda.BRL);
        var partes = original.ratear(pesos);

        assertThat(partes).containsExactlyElementsOf(
                esperado.stream()
                        .map(v -> Money.of(v, Moeda.BRL))
                        .toList());

        assertThat(partes.stream()
                .reduce(Money.of("0.00", Moeda.BRL), Money::somar))
                .isEqualTo(original);

    }

    @Test
    void rejeitarRateioComPesoNulo() {
        var original = Money.of("100.00", Moeda.BRL);
        assertThatThrownBy(() -> original.ratear(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Pesos não podem ser nulos");
    }

    @Test
    void rejeitarRateioComPesoVazio() {
        var original = Money.of("100.00", Moeda.BRL);
        assertThatThrownBy(() -> original.ratear())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Pesos não podem ser vazios");
    }

    @Test
    void rejeitarRateioComSomaDosPesosIgualZero() {
        var original = Money.of("100.00", Moeda.BRL);
        assertThatThrownBy(() -> original.ratear(0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Soma dos pesos deve ser maior que zero");
    }

    @Test
    void rejeitarRateioCompesoNegativo() {
        var original = Money.of("100.00", Moeda.BRL);
        assertThatThrownBy(() -> original.ratear(2, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Peso não pode ser negativo");
    }

}
