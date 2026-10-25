package com.fatec.vendas.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;


import org.junit.jupiter.api.Test;

public class TUProdutoTests {

        @Test
        void deveCriarProdutoComSucessoQuandoDadosValidos() {
                Produto produto = new Produto("COD001", "Teclado Mecanico", 250.00, 10);
                produto.setId(1L);

                assertEquals(1L, produto.getId());
                assertEquals("COD001", produto.getCodigo());
                assertEquals("Teclado Mecanico", produto.getDescricao());
                assertEquals(250.00, produto.getPreco());
                assertEquals(10, produto.getQuantidadeEstoque());
        }

        @Test
        void deveLancarExcecaoQuandoCodigoNuloOuVazio() {
                Produto produto = new Produto();

                IllegalArgumentException exNulo = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.setCodigo(null));
                assertEquals("Codigo do produto invalido", exNulo.getMessage());

                IllegalArgumentException exVazio = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.setCodigo("   "));
                assertEquals("Codigo do produto invalido", exVazio.getMessage());

                IllegalArgumentException exEspacoUnicode = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.setCodigo("\u2003"));
                assertEquals("Codigo do produto invalido", exEspacoUnicode.getMessage());
        }

        @Test
        void deveLancarExcecaoQuandoDescricaoNulaOuVazia() {
                Produto produto = new Produto();

                IllegalArgumentException exNulo = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.setDescricao(null));
                assertEquals("Descricao do produto invalida", exNulo.getMessage());

                IllegalArgumentException exVazio = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.setDescricao("   "));
                assertEquals("Descricao do produto invalida", exVazio.getMessage());

                // Rejeita descricao vazia
                IllegalArgumentException exNome = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.setDescricao(""));
                assertEquals("Descricao do produto invalida", exNome.getMessage());

                IllegalArgumentException exEspacoUnicode = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.setDescricao("\u2003"));
                assertEquals("Descricao do produto invalida", exEspacoUnicode.getMessage());
        }

        @Test
        void deveLancarExcecaoQuandoPrecoNaoForPositivoOuFinito() {
                Produto produto = new Produto();

                IllegalArgumentException exZero = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.setPreco(0));
                assertEquals("Preco do produto invalido", exZero.getMessage());

                IllegalArgumentException exNegativo = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.setPreco(-10.0));
                assertEquals("Preco do produto invalido", exNegativo.getMessage());

                IllegalArgumentException exNaN = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.setPreco(Double.NaN));
                assertEquals("Preco do produto invalido", exNaN.getMessage());

                IllegalArgumentException exInfinito = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.setPreco(Double.POSITIVE_INFINITY));
                assertEquals("Preco do produto invalido", exInfinito.getMessage());
        }

        @Test
        void deveLancarExcecaoQuandoQuantidadeEstoqueZeroOuNegativa() {
                Produto produto = new Produto();

                IllegalArgumentException exZero = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.setQuantidadeEstoque(0));
                assertEquals("Quantidade em estoque do produto invalida", exZero.getMessage());

                IllegalArgumentException exNegativo = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.setQuantidadeEstoque(-5));
                assertEquals("Quantidade em estoque do produto invalida", exNegativo.getMessage());
        }

        @Test
        void deveLancarExcecaoQuandoIdMenorOuIgualAZero() {
                Produto produto = new Produto();

                IllegalArgumentException exZero = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.setId(0L));
                assertEquals("Id do produto invalido", exZero.getMessage());

                IllegalArgumentException exNegativo = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.setId(-1L));
                assertEquals("Id do produto invalido", exNegativo.getMessage());
        }

        @Test
        void deveBaixarEstoqueComSucesso() {
                Produto produto = new Produto("COD001", "Mouse", 50.0, 10);
                produto.baixarEstoque(4);
                assertEquals(6, produto.getQuantidadeEstoque());
        }

        @Test
        void deveConsiderarProdutosComMesmoIdComoIguais() {
                Produto primeiro = new Produto("COD001", "Mouse", 50.0, 10);
                primeiro.setId(1L);
                Produto segundo = new Produto("COD002", "Teclado", 100.0, 5);
                segundo.setId(1L);

                assertEquals(primeiro, segundo);
                assertEquals(primeiro.hashCode(), segundo.hashCode());
        }


        @Test
        void deveLancarExcecaoAoBaixarEstoqueComQuantidadeInvalida() {
                Produto produto = new Produto("COD001", "Mouse", 50.0, 10);

                IllegalArgumentException exZero = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.baixarEstoque(0));
                assertEquals("Quantidade para baixa deve ser maior que zero", exZero.getMessage());

                IllegalArgumentException exInsuficiente = assertThrows(
                                IllegalArgumentException.class,
                                () -> produto.baixarEstoque(15));
                assertEquals("Estoque insuficiente para o produto: Mouse (Disponível: 10, Solicitado: 15)",
                                exInsuficiente.getMessage());
        }
}
