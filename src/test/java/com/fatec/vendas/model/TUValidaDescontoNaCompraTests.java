package com.fatec.vendas.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TUValidaDescontoNaCompraTests {

    private DescontoNaVenda descontoNaVenda;

    @BeforeEach
    void setUp() {
        this.descontoNaVenda = new DescontoNaVenda();
    }

    @Test
    void ct01_quando_data_nao_promocional_e_primeira_compra_true_deveAplicarDesconto() {
        // DADO que a compra não foi realizada em mes promocional, primeira compra true
        // e valor da compra<200
        String primeiraCompra = "true";
        String dataCompra = "15/01/2026";
        String valorCompra = "100.00";

        // QUANDO o usuario confirma a operação de compra
        BigDecimal valorDePagamentoObtido = descontoNaVenda.regraDeDesconto(primeiraCompra, dataCompra, valorCompra);
        BigDecimal valorDePagamentoEsperado = new BigDecimal("115.00").setScale(2, RoundingMode.HALF_UP);

        // ENTÃO o valor a pagar considera 5% de desconto => valor total a pagar = (100
        // + 20) - 5 = 115.00
        assertEquals(valorDePagamentoEsperado, valorDePagamentoObtido);

    }

    @Test
    void ct02_quando_data_nao_promocional_e_primeira_compra_false_nao_deve_aplicar_desconto() {
        // DADO que a compra não foi realizada em mes promocional, primeira compra false
        // e valor da compra<200
        String primeiraCompra = "false";
        String dataCompra = "15/01/2026";
        String valorCompra = "100.00";

        // QUANDO o usuario confirma a operação de compra
        BigDecimal valorDePagamentoObtido = descontoNaVenda.regraDeDesconto(primeiraCompra, dataCompra, valorCompra);
        BigDecimal valorDePagamentoEsperado = new BigDecimal("120.00").setScale(2, RoundingMode.HALF_UP);

        // ENTÃO o valor a pagar é sem desconto
        assertEquals(valorDePagamentoEsperado, valorDePagamentoObtido);

    }

    @Test
    void ct03_quando_data_nao_promocional_e_primeira_compra_true_deveAplicarDesconto() {
        // DADO que a compra não foi realizada em mes promocional, primeira compra true
        // e valor da compra>200
        String primeiraCompra = "true";
        String dataCompra = "15/01/2026";
        String valorCompra = "250.00";

        // QUANDO o usuario confirma a operação de compra
        BigDecimal valorDePagamentoObtido = descontoNaVenda.regraDeDesconto(primeiraCompra, dataCompra, valorCompra);
        BigDecimal valorDePagamentoEsperado = new BigDecimal("237.50").setScale(2, RoundingMode.HALF_UP);

        // ENTÃO o valor a pagar considera 5% de desconto => valor total a pagar = (250
        // + 0) - 12.5 = 237.50
        assertEquals(valorDePagamentoEsperado, valorDePagamentoObtido);

    }

    @Test
    void ct04_quando_primeira_compra_vazio_retorna_msg_de_erro() {
        // DADO que o atributo primeira compra esta vazio
        String primeiraCompra = "";
        String dataCompra = "15/01/2026";
        String valorCompra = "100.00";

        // QUANDO o usuario confirma a operação de compra
        try {
            descontoNaVenda.regraDeDesconto(primeiraCompra, dataCompra, valorCompra);
            fail("Deveria falhar com atributo invalido");
        } catch (IllegalArgumentException e) {
            // ENTÃO retorna mensagem de erro
            assertEquals("Primeira compra não pode estar em branco ou vazia.", e.getMessage());
        }
    }

    @Test
    void ct05_quando_primeira_compra_em_branco_retorna_msg_de_erro() {
        // DADO que a primeira compra esta em branco
        String primeiraCompra = " ";
        String dataCompra = "15/01/2026";
        String valorCompra = "100.00";

        // QUANDO o usuario confirma a operação de compra
        try {
            descontoNaVenda.regraDeDesconto(primeiraCompra, dataCompra, valorCompra);
            fail("Deveria falhar com atributo invalido");
        } catch (IllegalArgumentException e) {
            // ENTÃO retorna mensagem de erro
            assertEquals("Primeira compra não pode estar em branco ou vazia.", e.getMessage());
        }

    }

    @Test
    void ct06_quando_primeira_compra_valor_invalido_retorna_msg_de_erro() {
        // DADO que a primeira compra tem valor invalido
        String primeiraCompra = "aaa";
        String dataVenda = "15/01/2026";
        String valorCompra = "100.00";

        // QUANDO o usuario confirma a operação de compra
        try {
            descontoNaVenda.regraDeDesconto(primeiraCompra, dataVenda, valorCompra);
            fail("Deveria falhar com atributo invalido");
        } catch (IllegalArgumentException e) {

            // ENTÃO retorna mensagem de erro

            assertEquals("Primeira compra deve ser true ou false.", e.getMessage());
        }

    }

    @Test
    void ct07_quando_primeira_compra_null_retorna_msg_de_erro() {
        // DADO que a primeira compra é null
        String primeiraCompra = null;
        String dataVenda = "15/01/2026";
        String valorCompra = "100.00";

        // QUANDO o usuario confirma a operação de compra
        try {
            descontoNaVenda.regraDeDesconto(primeiraCompra, dataVenda, valorCompra);
            fail("Deveria falhar com atributo invalido");
        } catch (IllegalArgumentException e) {
            // ENTÃO retorna mensagem de erro
            assertEquals("Primeira compra não pode estar em branco ou vazia.", e.getMessage());
        }

    }

    @Test
    void ct08_quando_primeira_compra_True_retorna_msg_erro() {
        // DADO que a primeira compra é True
        String primeiraCompra = "True";
        String dataVenda = "15/01/2026";
        String valorCompra = "100.00";

        // QUANDO o usuario confirma a operação de compra
        try {
            descontoNaVenda.regraDeDesconto(primeiraCompra, dataVenda, valorCompra);
            fail("Deveria falhar com atributo invalido");
        } catch (IllegalArgumentException e) {

            // ENTÃO retorna mensagem de erro

            assertEquals("Primeira compra deve ser true ou false.", e.getMessage());
        }

    }

    @Test
    void ct09_quando_primeira_compra_true_e_data_promocional_aplica_10_por_cento_de_desconto() {
        // DADO que a primeira compra é True
        String primeiraCompra = "true";
        String dataVenda = "15/04/2026";
        String valorCompra = "100.00";

        // QUANDO o usuario confirma a operação de compra
        BigDecimal resultado = descontoNaVenda.regraDeDesconto(primeiraCompra, dataVenda, valorCompra);
        // ENTAO nao deve aplicar desconto
        assertEquals(new BigDecimal(110.00).setScale(2, RoundingMode.HALF_UP), resultado);

    }

    @Test
    void ct10_quando_data_nao_promocional_e_primeira_compra_true_deveAplicarDesconto() {
        // DADO que a compra não foi realizada em mes promocional, primeira compra true
        // e valor da compra<200
        String primeiraCompra = "true";
        String dataCompra = "15/01/2026";
        String valorCompra = "100.00";

        // QUANDO o usuario confirma a operação de compra
        BigDecimal valorObtido = descontoNaVenda.regraDeDesconto(primeiraCompra, dataCompra, valorCompra);
        BigDecimal valorEsperado = new BigDecimal("115.00").setScale(2, RoundingMode.HALF_UP);

        // ENTÃO o valor a pagar considera 5% de desconto => valor total a pagar = (100
        // + 20) - 5 = 115.00
        assertEquals(valorEsperado, valorObtido);
    }

}