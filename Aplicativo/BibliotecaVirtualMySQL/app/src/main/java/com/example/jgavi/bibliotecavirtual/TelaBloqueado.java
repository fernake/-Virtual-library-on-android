package com.example.jgavi.bibliotecavirtual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class TelaBloqueado extends AppCompatActivity {
    Button continuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_bloqueado);

        continuar = (Button) findViewById(R.id.buttonBlockCont);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voltar = new Intent(TelaBloqueado.this, Login.class);
                startActivity(voltar);
            }
        });
    }

    //=========== Função para não voltar a tela de login ===========
    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
