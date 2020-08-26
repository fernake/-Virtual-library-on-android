package com.example.jgavi.bibliotecavirtual;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MenuAluno extends AppCompatActivity {
    //=========== Objetos ===========

    //=========== Para conexão do localhost ===========
    LocalHost host = new LocalHost();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_aluno);
    }
}
