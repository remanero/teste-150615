package com.nutrichecker.br.nutrichecker;

/**
 * Created by renato on 15/06/2015.
 */
public class Fornecedor {

    private Integer id;
    private String nome;
    private String cnpj;
    private Usuario usuario;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getCnpj() { return cnpj; }

    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

}
