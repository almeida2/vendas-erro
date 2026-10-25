package com.fatec.vendas.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TUValidaDescontoNaCompraDdTests {

    DescontoNaVenda descontoNaVenda = new DescontoNaVenda();

    @ParameterizedTest
    @CsvFileSource(resources = "/desconto_na_venda-pce.csv", numLinesToSkip = 1)
    void testRegraDeDesconto(int id, String primeiraCompra, String dataCompra, String valorCompra,
            String resultadoEsperado) {
        try {
            // Arrange & Act
            BigDecimal resultadoCalculado = descontoNaVenda.regraDeDesconto(primeiraCompra, dataCompra, valorCompra);

            // Assert
            assertEquals(new BigDecimal(resultadoEsperado), resultadoCalculado);

        } catch (IllegalArgumentException e) {
            assertEquals(resultadoEsperado, e.getMessage());
        }
    }
}
