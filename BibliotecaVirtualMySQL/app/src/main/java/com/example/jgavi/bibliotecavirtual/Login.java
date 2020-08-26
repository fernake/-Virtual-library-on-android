package com.example.jgavi.bibliotecavirtual;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;

public class Login extends AppCompatActivity {
    //=========== Objetos ===========
    private EditText editLogin, editSenhaLog;
    private Button buttonLogin;

    //=========== Para conexão do localhost ===========
    LocalHost host = new LocalHost();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //=========== findViewById ===========
        editLogin = (EditText) findViewById(R.id.editLogin);
        editSenhaLog = (EditText) findViewById(R.id.editSenhaLog);
        buttonLogin = (Button) findViewById(R.id.ButtonLogin);


        //=========== Ação para logar a conta ===========
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = editLogin.getText().toString();
                String senha = editSenhaLog.getText().toString();

                String URL = host.HOST +"/logar.php";

                if(codigo.isEmpty() || senha.isEmpty()){
                    Toast.makeText(Login.this, "Preenche os campos que estão em branco", Toast.LENGTH_LONG).show();
                } else{
                    Ion.with(Login.this)
                            .load(URL)
                            .setBodyParameter("email_app", codigo)
                            .setBodyParameter("senha_app", senha)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    try{
                                        Usuario user = new Usuario();
                                        user.setTipo(result.get("LOGIN").getAsInt());
                                        user.setEmail(result.get("email").getAsString());
                                        user.setNome(result.get("nome").getAsString());
                                        user.setId(result.get("id").getAsInt());
                                        String RETORNO = result.get("LOGIN").getAsString();

                                        if(RETORNO.equals("ERRO")){
                                            Toast.makeText(Login.this, "E-mail ou Senha incorretos", Toast.LENGTH_SHORT).show();
                                        } else if(RETORNO.equals("1")){
                                            //=========== ACESSO DO MENU DO FUNCIONÁRIO ===========
                                            Intent login = new Intent(Login.this, MenuFuncionario.class);
                                            startActivity(login);
                                        } else if (RETORNO.equals("2") || RETORNO.equals("3")) {
                                            //=========== ACESSO DO MENU DO ALUNO ===========
                                            Bundle args = new Bundle();
                                            args.putSerializable("login", user);
                                            Intent login = new Intent(Login.this, MenuEstudante.class);
                                            login.putExtra("conta", args);
                                            startActivity(login);
                                        } else if(RETORNO.equals("4")){
                                            Intent login = new Intent(Login.this, TelaBloqueado.class);
                                            startActivity(login);
                                        } else {
                                                Toast.makeText(Login.this, "Opss... Ocorreu um erro", Toast.LENGTH_SHORT).show();
                                        }
                                    }catch(Exception erro){
                                        Toast.makeText(Login.this, "E-mail ou Senha incorretos", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }


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
