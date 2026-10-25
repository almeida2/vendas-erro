package com.fatec.vendas.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fatec.vendas.model.Produto;
import com.fatec.vendas.model.ProdutoDTO;
import com.fatec.vendas.servico.IProdutoService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutoController {

    private final IProdutoService produtoService;
    Logger logger = LogManager.getLogger(this.getClass());

    public ProdutoController(IProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        logger.info(">>>>>> controller listar todos os produtos iniciado...");
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
@PostMapping
public ResponseEntity<ProdutoDTO> salvar(@Valid @RequestBody ProdutoDTO produtoDTO) {

    Produto produto = produtoDTO.toEntity();
    Produto produtoSalvo = produtoService.cadastrarProduto(produto);

    //Construir a URI do recurso recém-criado e retorná-la no cabeçalho HTTP Location da resposta 201 Created.
    //documentada na RFC 9110

    URI localizacao = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(produtoSalvo.getId())
            .toUri();

    ProdutoDTO resposta = ProdutoDTO.fromEntity(produtoSalvo);

    return ResponseEntity.created(localizacao).body(resposta);
}


    /**
     * @param id
     * @return ResponseEntity com status HTTP apropriado:
     *         - HTTP 204 (No Content) se a exclusão for bem-suced
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
    try {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    } catch (IllegalArgumentException ex) {
        // Diferencia pelo conteúdo da mensagem ou validação da causa
        if (ex.getMessage() != null && ex.getMessage().contains("não encontrado")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()); // HTTP 404
        }
        return ResponseEntity.badRequest().body(ex.getMessage()); // HTTP 400
    }
}

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizar( @PathVariable Long id,
        @Valid @RequestBody ProdutoDTO produtoDTO) {

    logger.info("Atualizando produto com id={}", id);

    Produto produto = produtoDTO.toEntity();

    return produtoService.atualizar(id, produto)
            .map(ProdutoDTO::fromEntity)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
}
}