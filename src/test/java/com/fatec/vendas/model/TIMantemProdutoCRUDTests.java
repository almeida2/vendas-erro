package com.fatec.vendas.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.fatec.vendas.servico.IProdutoRepository;

// A anotacao @DataJpaTest permite testar as operacoes de banco de dados
// configurando o contexto necessario para executar testes de repositorios JPA.
// evitando o custo de carregar a aplicação inteira
@DataJpaTest
class TIReqMantemProdutoCRUDTests {
    // O objetivo principal para validar a integração da classe Produto
    // com o Spring Data JPA Repository, é testar se o mapeamento ORM
    // (anotações de entidade, validações de coluna, IDs) e as consultas SQL
    // funcionam perfeitamente no banco de dados.
    @Autowired
    IProdutoRepository produtoRepository;   

    @Test
    void ct01_cadastrar_produto_com_sucesso() {
        // DADO que a informacoes de produto sao validas
        Produto produto = BuilderProduto.umProduto().build();
        // QUANDO o usuario cadastra o produto
        produtoRepository.save(produto);
        // ENTÃO o produto e cadastrado com sucesso e listado no repositorio
        assertEquals(1, produtoRepository.count());
        var produtoRetornado = produtoRepository.findById(produto.getId()).get();
        assertNotNull(produtoRetornado.getId());
        assertEquals("PROD001", produtoRetornado.getCodigo());
        assertEquals("Teclado Mecanico", produtoRetornado.getDescricao());
        assertEquals(250.00, produtoRetornado.getPreco());
        assertEquals(5, produtoRetornado.getQuantidadeEstoque());
    }
    @Test
    @DisplayName("Deve persistir e consultar um produto com sucesso no banco de dados de teste")
    void ct02_consultar_produto_com_sucesso_no_banco_de_dados() {
        // Dado que os dados sao validos
        Produto produtoParaPersistir = BuilderProduto.umProduto()
                .comId(null) // Garante que a chave primária será gerada pelo SGBD
                .comCodigo("PROD001")
                .comDescricao("Teclado Mecanico")
                .comPreco(250.00)
                .comQuantidadeEstoque(5)
                .build();

        Produto produtoSalvo = produtoRepository.save(produtoParaPersistir);

        // Quando consulta o produto no sgbd
        var resultadoConsulta = produtoRepository.findById(produtoSalvo.getId());

        // Então - Valida se o registro retornado coincide com os dados gravados
        assertTrue(resultadoConsulta.isPresent(), "O produto deveria ser encontrado no SGBD");

        Produto produtoRetornado = resultadoConsulta.get();
        assertAll("Validação dos campos persistidos no banco de dados",
            () -> assertEquals(produtoSalvo.getId(), produtoRetornado.getId()),
            () -> assertEquals("PROD001", produtoRetornado.getCodigo()),
            () -> assertEquals("Teclado Mecanico", produtoRetornado.getDescricao()),
            () -> assertEquals(250.00, produtoRetornado.getPreco()),
            () -> assertEquals(5, produtoRetornado.getQuantidadeEstoque())
        );
    }
       
    @Test
    @DisplayName("Deve retornar Optional vazio ao consultar produto por ID inexistente")
    void ct03_consultar_produto_inexistente_retorna_vazio() {
        // Arrange (Dado)
        Long idInexistente = 999L;

        // Act (Quando)
        Optional<Produto> resultado = produtoRepository.findById(idInexistente);

        // Assert (Então)
        assertTrue(resultado.isEmpty(), "O resultado da busca por ID inexistente deve ser vazio");
    }
     
    @Test
    @DisplayName("Deve atualizar os dados do produto com sucesso no banco de dados de teste")
    void ct04_atualizar_informacoes_do_produto_com_sucesso_no_sgbd() {
        // Arrange (Dado) - Persiste o produto original no banco
        Produto produtoOriginal = BuilderProduto.umProduto()
                .comId(null)
                .comCodigo("PROD001")
                .comDescricao("Teclado Mecanico")
                .comPreco(250.00)
                .comQuantidadeEstoque(5)
                .build();

        Produto produtoSalvo = produtoRepository.save(produtoOriginal);
        Long idExistente = produtoSalvo.getId();

        // Act (Quando) - Altera as informações e salva a atualização
        produtoSalvo.setDescricao("Teclado Mecanico RGB");
        produtoSalvo.setPreco(300.00);
        produtoSalvo.setQuantidadeEstoque(10);

        produtoRepository.save(produtoSalvo);

        // Reconsulta o registro no SGBD para garantir que a alteração foi persistida
        var produtoAtualizadoOpt = produtoRepository.findById(idExistente);

        // Assert (Então) - Valida se as alterações foram salvas corretamente
        assertTrue(produtoAtualizadoOpt.isPresent(), "O produto atualizado deve ser encontrado no SGBD");

        Produto produtoAtualizado = produtoAtualizadoOpt.get();
        assertAll("Validação das informações atualizadas no banco de dados",
            () -> assertEquals(idExistente, produtoAtualizado.getId(), "O ID não deve mudar após a atualização"),
            () -> assertEquals("PROD001", produtoAtualizado.getCodigo(), "O código original deve ser mantido"),
            () -> assertEquals("Teclado Mecanico RGB", produtoAtualizado.getDescricao(), "A descrição deve ser atualizada"),
            () -> assertEquals(300.00, produtoAtualizado.getPreco(), "O preço deve ser atualizado"),
            () -> assertEquals(10, produtoAtualizado.getQuantidadeEstoque(), "O estoque deve ser atualizado")
        );
    }
    @Test
    void ct05_deletar_produto_com_sucesso() {
        // DADO que o produto foi cadastrado com sucesso
        Produto produto = BuilderProduto.umProduto().build();
        produtoRepository.save(produto);
        assertEquals(1, produtoRepository.count());

        // QUANDO o usuario deleta o produto
        produtoRepository.delete(produto);

        // ENTÃO o produto e removido do repositorio
        assertEquals(0, produtoRepository.count());
    }
}
