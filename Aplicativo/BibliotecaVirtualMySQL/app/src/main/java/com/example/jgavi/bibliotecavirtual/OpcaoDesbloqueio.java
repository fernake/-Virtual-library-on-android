package com.example.jgavi.bibliotecavirtual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class OpcaoDesbloqueio extends AppCompatActivity {
    //=========== Objetos ===========
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button Confirmar;
    int tipo;

    //=========== Para conexão do localhost ===========
    LocalHost host = new LocalHost();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcao_desbloqueio);

        //========== Bundle & args ==========
        Bundle args = getIntent().getBundleExtra("conta");
        final Usuario user = (Usuario) args.getSerializable("editar");
        final int id = user.getId();

        //=========== findViewById ===========
       radioGroup = (RadioGroup) findViewById(R.id.RadioGroupDesbl);
       Confirmar = (Button) findViewById(R.id.buttonConfirmarDesb);

       Confirmar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int RadioId = radioGroup.getCheckedRadioButtonId();

               radioButton = findViewById(RadioId);

               if (radioButton.getText().equals("Professor")){
                   tipo = 3;

               } else if (radioButton.getText().equals("Estudante")){
                   tipo = 2;
               }

               String url = host.HOST +"/desbloquearusuario.php";

               Ion.with(getBaseContext())
                       .load(url)
                       .setBodyParameter("tipo_app", String.valueOf(tipo))
                       .setBodyParameter("id_app", String.valueOf(id))
                       .asJsonObject()
                       .setCallback(new FutureCallback<JsonObject>() {
                           @Override
                           public void onCompleted(Exception e, JsonObject result) {
                               try {
                                   String RETORNO = result.get("EDITAR").getAsString();

                                   if (RETORNO.equals("ERRO")) {
                                       Toast.makeText(OpcaoDesbloqueio.this, "Ocorreu um erro, tente mais tarde", Toast.LENGTH_SHORT).show();
                                   } else if (RETORNO.equals("SUCESSO")) {
                                       Toast.makeText(OpcaoDesbloqueio.this, "Usuário bloqueado", Toast.LENGTH_LONG).show();
                                       Intent SucessDesbloqueio = new Intent(OpcaoDesbloqueio.this, ProcurarUsuario.class);
                                       startActivity(SucessDesbloqueio);
                                   }
                               } catch (Exception erro){
                                   Toast.makeText(OpcaoDesbloqueio.this, "Erro Inesperado", Toast.LENGTH_LONG).show();
                               }
                           }
                       });
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
