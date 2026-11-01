package com.mello.nathalia.zerosum.ledger.domain.money;

import com.mello.nathalia.zerosum.ledger.domain.enuns.Moeda;
import java.math.BigDecimal;
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

}