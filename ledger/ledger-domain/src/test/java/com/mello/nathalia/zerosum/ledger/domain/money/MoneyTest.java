package com.mello.nathalia.zerosum.ledger.domain.money;

import com.mello.nathalia.zerosum.ledger.domain.enuns.Moeda;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MoneyTest {

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
}
