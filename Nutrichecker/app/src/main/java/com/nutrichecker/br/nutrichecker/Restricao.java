package com.nutrichecker.br.nutrichecker;

/**
 * Created by renato on 15/06/2015.
 */
public class Restricao {

    private Integer id;
    private String descricao;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    @Override
    public String toString() {
        return this.descricao;
    }

}
