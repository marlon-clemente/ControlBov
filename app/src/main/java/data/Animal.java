package data;

import java.io.Serializable;
import java.lang.String;

public class Animal implements Serializable {
    public String nome_registro;
    public String tipo_registro;
    public String sexo;
    public String tipo;
    public String peso;
    public String classificacao;
    public String fotografia;
    public int idade;

    public Animal() {

    }

    public Animal(String nome_registro, String tipo_registro, String sexo, String tipo,
                  String peso, String classificacao, String fotografia, int idade){
        this.nome_registro = nome_registro;
        this.tipo_registro = tipo_registro;
        this.sexo = sexo;
        this.tipo = tipo;
        this.peso = peso;
        this.classificacao = classificacao;
        this.fotografia = fotografia;
        this.idade = idade;

    }

    public String getNome_registro() {
        return nome_registro;
    }

    public void setNome_registro(String nome) {
        this.nome_registro = nome;
    }

    public String getTipo_registro() {
        return tipo_registro;
    }

    public void setTipo_registro(String registro) {
        this.tipo_registro = registro;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    @Override
    public String toString() {
        return  "nome='" + nome_registro + '\'' +
                ", registro='" + tipo_registro + '\'' +
                ", sexo='" + sexo + '\'' +
                ", tipo='" + tipo;
    }
}

