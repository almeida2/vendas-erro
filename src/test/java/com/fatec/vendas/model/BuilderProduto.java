package com.fatec.vendas.model;


public class BuilderProduto {
    // 1. Valores padrao
    private Long id = null;
    private String codigo = "PROD001";
    private String descricao = "Teclado Mecanico";
    private double preco = 250.00;
    private int quantidadeEstoque = 5;

    // 2. Ponto de entrada estatico para o Builder
    public static BuilderProduto umProduto() {
        return new BuilderProduto();
    }

    public BuilderProduto comId(Long id) {
        this.id = id;
        return this;
    }

    public BuilderProduto comCodigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public BuilderProduto comDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public BuilderProduto comNome(String nome) {
        this.descricao = nome;
        return this;
    }

    public BuilderProduto comPreco(double preco) {
        this.preco = preco;
        return this;
    }

    public BuilderProduto comQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
        return this;
    }

    public Produto build() {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setCodigo(codigo);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setQuantidadeEstoque(quantidadeEstoque);
        return produto;
    }
}
