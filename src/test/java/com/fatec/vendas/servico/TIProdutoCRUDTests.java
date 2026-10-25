package com.fatec.vendas.servico;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.vendas.model.BuilderProduto;
import com.fatec.vendas.model.Produto;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
@SpringBootTest
public class TIProdutoCRUDTests {

    @Autowired 
    IProdutoService produtoService;

    @Test 
    @DisplayName ("Deve cadastrar um produto com sucesso quando os dados são válidos")
    void ct01_quando_cadastrar_um_produto_valido_retorna_sucesso() {
       // DADO que a informacoes de produto sao validas
        Produto produto = BuilderProduto.umProduto().build();
        // QUANDO o usuario cadastra o produto
        Produto produtoCadastrado = produtoService.cadastrarProduto(produto);
        // ENTAO o produto é cadastrado com sucesso
       
        assertEquals(produto, produtoCadastrado);
    }
     @Test 
     @DisplayName ("Deve lançar exceção ao tentar cadastrar um produto com código nulo")
    void ct02_quando_dados_invalidos_retorna_erro() {
       try{
        // DADO que a informacoes de produto sao invalidas
        Produto produto = BuilderProduto.umProduto().comCodigo(null).build();
        // QUANDO o usuario cadastra o produto
        Produto produtoCadastrado = produtoService.cadastrarProduto(produto);
        // ENTAO o produto é cadastrado com sucesso
        assertNull(produtoCadastrado);
       }
       catch(IllegalArgumentException e){
                assertTrue(e.getMessage().contains("Codigo do produto invalido"));
       }
    }
}

