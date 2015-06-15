package com.nutrichecker.br.nutrichecker;

import java.util.List;

/**
 * Created by renato on 15/06/2015.
 */
public class Produto {

    private Integer id;
    private String descricao;
    private String codigoBarra;
    private List<Restricao> restricoes;
    private Fornecedor fornecedor;

    public Produto() {

    }

    public Produto(Integer id, String descricao, String codigoBarra) {
        this.id = id;
        this.descricao = descricao;
        this.codigoBarra = codigoBarra;
    }

    public Produto(String descricao, String codigoBarra) {
        this.descricao = descricao;
        this.codigoBarra = codigoBarra;
    }

    public Produto(Integer id,String descricao, String codigoBarra, List<Restricao> restricoes) {
        this.id = id;
        this.descricao = descricao;
        this.codigoBarra = codigoBarra;
        this.restricoes = restricoes;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCodigoBarra() { return codigoBarra; }

    public void setCodigoBarra(String codigoBarra) { this.codigoBarra = codigoBarra; }

    public Fornecedor getFornecedor() { return fornecedor; }

    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

    public List<Restricao> getRestricoes() { return restricoes; }

    public void setRestricoes(List<Restricao> restricoes) { this.restricoes = restricoes; }


}
