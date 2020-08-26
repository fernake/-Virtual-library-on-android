package com.example.jgavi.bibliotecavirtual;

import android.media.Image;

public class WrapData {
    private String url;
    private String method;
    private String codigo;
    private String titulo;
    private String autor;
    private String edicao;
    private String editora;
    private String local;
    private String ano;
    private String curso;
    private String assunto;
    private Image image;


    public WrapData(){
      //  this.image = new Image();
    }

    public WrapData(String url, String method, String name, String codigo, String titulo, String autor, String edicao, String editora, String local, String ano, String curso, String assunto, Image image ){
        this.url = url;
        this.method = method;
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.edicao = edicao;
        this.editora = editora;
        this.local = local;
        this.ano = ano;
        this.curso = curso;
        this.assunto = assunto;
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEdicao() {
        return edicao;
    }

    public void setEdicao(String edicao) {
        this.edicao = edicao;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
