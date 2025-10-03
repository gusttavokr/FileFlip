package com.example.backend.model;

import jakarta.persistence;

@entity
public class usuario{
    private long id;
    private string nome;
    private string email;
    private string senha;

    public Usuario(long id, string nome, string email, string senha){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
}