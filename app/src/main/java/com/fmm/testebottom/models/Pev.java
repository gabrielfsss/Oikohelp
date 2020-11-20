package com.fmm.testebottom.models;

import java.util.ArrayList;

public class Pev {

    private String nome;
    private String contato;
    private String email;
    private Localization localizacao;
    private ArrayList<String> materiais;
    private int type;

    public Pev() {

    }

    public Pev(String nome, String contato, String email, Localization localizacao, ArrayList<String> materiais, int type) {
        this.nome = nome;
        this.contato = contato;
        this.email = email;
        this.localizacao = localizacao;
        this.materiais = materiais;
        this.type = type;
    }

    public ArrayList<String> getMateriais() {
        return materiais;
    }

    public void setMateriais(ArrayList<String> materiais) {
        this.materiais = materiais;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Localization getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localization localizacao) {
        this.localizacao = localizacao;
    }
}
