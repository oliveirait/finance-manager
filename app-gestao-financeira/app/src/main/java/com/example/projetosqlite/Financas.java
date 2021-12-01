package com.example.projetosqlite;

public class Financas {

    private int cod_tipo ;
    private String descricao ;
    private String data ;
    private String tipo ;
    private double valor ;
    //update para passar dados completos

    public Financas(int cod_tipo, String descricao, String data, String tipo,
                    double valor){
        this.cod_tipo=cod_tipo;
        this.descricao=descricao;
        this.data=data;
        this.tipo=tipo;
        this.valor=valor;
    }
    //para cadastro
    public Financas(String descricao,String data,String tipo, double valor){
    this.descricao=descricao;
    this.tipo=tipo;
    this.descricao=descricao;
    this.data=data;
    this.tipo=tipo;
    this.valor=valor;
    }

    //instância
    public Financas(){}
    public int getCod_tipo() {
        return cod_tipo;
    }
    public void setCod_tipo(int cod_tipo) {
        this.cod_tipo = cod_tipo;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }

}
