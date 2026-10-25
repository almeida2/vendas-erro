package com.fatec.vendas.controller;


import com.fatec.vendas.model.Produto;
import com.fatec.vendas.servico.IProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.HttpHeaders;

import java.nio.file.Files;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TIProdutoCRUDTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IProdutoRepository produtoRepository;

    @Value("classpath:json/produto_cadastrar.json")
    private Resource jsonCadastrar;

    @Value("classpath:json/produto_atualizar.json")
    private Resource jsonAtualizar;

    @BeforeEach
    void setUp() {
        // Garante isolamento limpando a base real/fake injetada antes de cada teste
        if (produtoRepository.findAll() != null) {
            produtoRepository.findAll().forEach(p -> produtoRepository.deleteById(p.getId()));
        }
    }

    
@Test
@DisplayName("POST /api/v1/produtos - Deve cadastrar produto e retornar 201 Created")
void deveSalvarProdutoComSucesso() throws Exception {
    String json = carregarConteudoResource(jsonCadastrar);

    mockMvc.perform(post("/api/v1/produtos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(header().string(
            HttpHeaders.LOCATION,
            matchesPattern(".*/api/v1/produtos/\\d+")
        ))
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.codigo").value("PROD-1001"))
        .andExpect(jsonPath("$.descricao").value("Teclado Mecânico RGB Wireless"))
        .andExpect(jsonPath("$.preco").value(350.00))
        .andExpect(jsonPath("$.quantidadeEstoque").value(25));
}


    @Test
    @DisplayName("GET /api/v1/produtos - Deve listar todos os produtos cadastrados e retornar HTTP 200")
    void deveListarTodosOsProdutosComSucesso() throws Exception {
        produtoRepository.save(new Produto( "P01", "Mouse Optico", 80.0, 15));
        produtoRepository.save(new Produto( "P02", "Monitor 27", 1400.0, 8));

        mockMvc.perform(get("/api/v1/produtos")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].codigo").value("P01"))
                .andExpect(jsonPath("$[1].codigo").value("P02"));
    }

    @Test
    @DisplayName("GET /api/v1/produtos/{id} - Deve obter o produto por ID com retorno HTTP 200")
    void deveBuscarPorIdComSucesso() throws Exception {
        Produto produtoSalvo = produtoRepository.save(new Produto("PROD-99", "Cadeira Gamer", 900.0, 3));

        mockMvc.perform(get("/api/v1/produtos/{id}", produtoSalvo.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(produtoSalvo.getId()))
                .andExpect(jsonPath("$.codigo").value("PROD-99"))
                .andExpect(jsonPath("$.descricao").value("Cadeira Gamer"));
    }

    @Test
    @DisplayName("PUT /api/v1/produtos/{id} - Deve atualizar o produto cadastrado e retornar HTTP 200")
    void deveAtualizarProdutoComSucesso() throws Exception {
        Produto produtoExistente = produtoRepository.save(new Produto("PROD-1001", "Versao Antiga", 100.0, 2));
        String jsonUpdateBody = carregarConteudoResource(jsonAtualizar);

        mockMvc.perform(put("/api/v1/produtos/{id}", produtoExistente.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUpdateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(produtoExistente.getId()))
                .andExpect(jsonPath("$.codigo").value("PROD-1001-V2"))
                .andExpect(jsonPath("$.descricao").value("Teclado Mecânico RGB Wireless Switch Red"))
                .andExpect(jsonPath("$.preco").value(399.90))
                .andExpect(jsonPath("$.quantidadeEstoque").value(30));
    }

    @Test
    @DisplayName("DELETE /api/v1/produtos/{id} - Deve excluir produto existente, retornar HTTP 204 e remover do banco")
    void deveDeletarProdutoComSucesso() throws Exception {
        Produto produtoParaDeletar = produtoRepository.save(new Produto("P-DEL", "Item Temporario", 50.0, 1));

        mockMvc.perform(delete("/api/v1/produtos/{id}", produtoParaDeletar.getId()))
                .andExpect(status().isNoContent());

        // Confirma que a exclusão refletiu na camada de persistência real
        mockMvc.perform(get("/api/v1/produtos/{id}", produtoParaDeletar.getId()))
                .andExpect(status().isNotFound());
    }

    private String carregarConteudoResource(Resource resource) throws Exception {
        return Files.readString(resource.getFile().toPath());
    }
}