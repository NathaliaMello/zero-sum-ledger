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
        var a = Money.of("10.0", Moeda.BRL);
        var b = Money.of("10.00", Moeda.BRL);
        assertThat(a).isEqualTo(b);
        assertThat(a).hasSameHashCodeAs(b);
    }

    @Test
    void dinheiroComMoedasDiferentesSaoDiferentes() {
        var money = Money.of("10", Moeda.BRL);
        var money2 = Money.of("10", Moeda.USD);
        assertThat(money).isNotEqualTo(money2);
    }

}
