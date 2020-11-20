package com.fmm.testebottom.models;

public class User {

    private String id;
    private String nome;
    private String contato;
    private String email;
    private String senha;
    private int type;

    public User(){

    }

    public User(String nome, String contato, String email, String senha, int type) {
        this.nome = nome;
        this.contato = contato;
        this.email = email;
        this.senha = senha;
        this.type = type;
    }

    public User(String id, String nome, String cpf, String contato, String email, String senha, int type) {
        this.id = id;
        this.nome = nome;
        this.contato = contato;
        this.email = email;
        this.senha = senha;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
