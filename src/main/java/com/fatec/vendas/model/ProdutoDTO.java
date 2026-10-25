package com.fatec.vendas.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProdutoDTO(
        Long id,

        @NotBlank
        String codigo,

        @NotBlank
        String descricao,

        @NotNull
        @Positive
        Double preco,

        @NotNull
        @PositiveOrZero
        Integer quantidadeEstoque
) {
    public Produto toEntity() {
        Produto produto = new Produto();
        produto.setCodigo(codigo);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setQuantidadeEstoque(quantidadeEstoque);
        return produto;
    }

    public static ProdutoDTO fromEntity(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getCodigo(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidadeEstoque()
        );
    }
}
