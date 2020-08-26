package com.example.jgavi.bibliotecavirtual;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuFuncionario extends AppCompatActivity {
    //=========== Objetos ===========
    private Button buttonCadUser, buttonSearchUser, buttonCadLivro, buttonCadEbook, buttonSearchTitulo, buttonSearchEbook;

    //=========== Para conexão do localhost ===========
    LocalHost host = new LocalHost();

    String url = host.EBOOK +"/cadastro%20de%20ebook.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_funcionario);

        //=========== findViewById ===========
        buttonCadUser = (Button) findViewById(R.id.buttonCadUser);
        buttonSearchUser = (Button) findViewById(R.id.buttonSearchUser);
        buttonCadLivro = (Button) findViewById(R.id.buttonCadLivro);
        buttonCadEbook = (Button) findViewById(R.id.buttonCadEbook);
        buttonSearchTitulo = (Button) findViewById(R.id.buttonSearchTitulo);
        buttonSearchEbook = (Button) findViewById(R.id.buttonSearchEbook);

        //=================================== OPÇÕES DO MENU =========================================

        //Botão de Registrar um Usuário
        buttonCadUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abreCad = new Intent(MenuFuncionario.this, Cadastro.class);
                startActivity(abreCad);

            }
        });

        //Botão de Procurar Usuário
        buttonSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abreProcura = new Intent(MenuFuncionario.this, ProcurarUsuario.class);
                startActivity(abreProcura);
            }
        });

        //Botão de Cadastrar Livro Físico
        buttonCadLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abreCadLivro = new Intent(MenuFuncionario.this, Cadastro_Livro_Fisico.class);
                startActivity(abreCadLivro);
            }
        });

        //Botão de cadastrar e-book
        buttonCadEbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        //Botão para procurar título Físico
        buttonSearchTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abreProcurar = new Intent(MenuFuncionario.this, FuncProcurarLivro.class);
                startActivity(abreProcurar);
            }
        });

        //Botão para procurar título Digital (e-book)
        buttonSearchEbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abreProcurar = new Intent(MenuFuncionario.this, ProcurarEbook.class);
                startActivity(abreProcurar);
            }
        });
    }
}
