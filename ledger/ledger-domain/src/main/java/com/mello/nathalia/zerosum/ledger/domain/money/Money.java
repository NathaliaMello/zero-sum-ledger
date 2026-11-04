package com.mello.nathalia.zerosum.ledger.domain.money;

import com.mello.nathalia.zerosum.ledger.domain.enuns.Moeda;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
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

        int[] pesos = new int[partes];
        Arrays.fill(pesos, 1);
        return ratear(pesos);
    }

    public List<Money> ratear(int... pesos) {

        validarPesos(pesos);

        long centavos = valor.unscaledValue().longValueExact();
        long sinal = Long.signum(centavos);
        long absoluto = Math.abs(centavos);
        long somaPesos = Arrays.stream(pesos).asLongStream().sum();

        long[] fatias = new long[pesos.length];
        long distribuido = 0;

        for (int i = 0; i < pesos.length; i++) {
            fatias[i] = absoluto * pesos[i] / somaPesos;
            distribuido += fatias[i];
        }

        long sobra = absoluto - distribuido;
        for (int i = 0; i < pesos.length && sobra > 0; i++) {
            if (pesos[i] > 0) {
                fatias[i]++;
                sobra--;
            }
        }

        List<Money> resultado = new ArrayList<>();
        for (long fatia: fatias) {
            resultado.add(new Money(BigDecimal.valueOf(sinal * fatia, 2), moeda));
        }

        return List.copyOf(resultado);
    }

    public Money aplicarPercentual(BigDecimal percentual, RoundingMode modo) {
        Objects.requireNonNull(percentual, "Percentual é obrigatório");
        Objects.requireNonNull(modo, "Regra de arredondamento é obrigatória");

        BigDecimal bruto = valor.multiply(percentual).movePointLeft(2);
        return new Money(bruto.setScale(2, modo), moeda);
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

    private void validarPesos(int... pesos) {
        Objects.requireNonNull(pesos, "Pesos não podem ser nulos");

        if(pesos.length == 0) {
            throw new IllegalArgumentException("Pesos não podem ser vazios");
        }

        if(Arrays.stream(pesos).anyMatch(peso -> peso < 0)) {
            throw new IllegalArgumentException("Peso não pode ser negativo");
        }

        if (Arrays.stream(pesos).asLongStream().sum() == 0) {
            throw new IllegalArgumentException("Soma dos pesos deve ser maior que zero");
        }
    }
}