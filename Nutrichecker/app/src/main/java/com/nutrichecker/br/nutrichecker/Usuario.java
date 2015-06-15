package com.nutrichecker.br.nutrichecker;

/**
 * Created by renato on 14/06/2015.
 */
public class Usuario {

    Integer id;
    String nome, email, senha;

    public Usuario(){

    }

    public Usuario(String nome, String email, String senha){
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Usuario(String nome, String senha){
        this.nome = nome;
        this.email = "";
        this.senha = senha;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }

    public void setSenha(String senha) { this.senha = senha; }
}
