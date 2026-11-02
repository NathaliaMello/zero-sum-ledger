package com.mello.nathalia.zerosum.ledger.domain.money;

import com.mello.nathalia.zerosum.ledger.domain.enuns.Moeda;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Money(BigDecimal valor, Moeda moeda) {

    public Money {
        Objects.requireNonNull(valor, "Valor é obrigatório");
        Objects.requireNonNull(moeda, "Moeda é obrigatória");
        try {
            valor = valor.setScale(2);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException(
                    "Valor com mais de 2 casas decimais: " + valor, e);
        }
    }

    public static Money of(String valor, Moeda moeda) {
        Objects.requireNonNull(valor, "Valor é obrigatório");
        return new Money(new BigDecimal(valor), moeda);
    }

    public Money somar(Money outro) {
        exigirMesmaMoeda(outro);
        return new Money(valor.add(outro.valor), moeda);
    }

    public Money subtrair(Money outro) {
        exigirMesmaMoeda(outro);
        return new Money(valor.subtract(outro.valor), moeda);
    }

    public List<Money> alocar(int partes) {
        if (partes <= 0) {
            throw new IllegalArgumentException("Número de partes deve ser maior que zero");
        }

        long centavos = valor.unscaledValue().longValueExact();
        long sinal = Long.signum(centavos);
        long absoluto = Math.abs(centavos);
        long base = absoluto / partes;
        long resto = absoluto % partes;

        List<Money> resultado = new ArrayList<>();
        for (int i = 0; i < partes; i++) {
            long centavosDaParte = sinal * (base + (i < resto ? 1 : 0));
            resultado.add(new Money(BigDecimal.valueOf(centavosDaParte, 2), moeda));
        }

        return List.copyOf(resultado);
    }

    public boolean isMaiorQue(Money outro) {
        exigirMesmaMoeda(outro);
        return valor.compareTo(outro.valor) > 0;
    }

    public boolean isMenorQue(Money outro) {
        exigirMesmaMoeda(outro);
        return valor.compareTo(outro.valor) < 0;
    }

    public boolean isPositivo() {
        return valor.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isNegativo() {
        return valor.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isZero() {
        return valor.compareTo(BigDecimal.ZERO) == 0;
    }

    private void exigirMesmaMoeda(Money outro) {
        Objects.requireNonNull(outro, "Outro valor é obrigatório");
        if (moeda != outro.moeda) {
            throw new IllegalArgumentException("Moedas diferentes: " + moeda + " e " + outro.moeda);
        }
    }
}