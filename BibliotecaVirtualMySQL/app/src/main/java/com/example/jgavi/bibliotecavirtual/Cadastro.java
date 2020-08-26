package com.example.jgavi.bibliotecavirtual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class Cadastro extends AppCompatActivity {
    //=========== Objetos ===========
    private EditText editSenha, editNome, editEmail, editTelefone;
    private Spinner spinnerTipo;
    private Button buttonCriarConta;

    //=========== Para conexão do localhost ===========
    LocalHost host = new LocalHost();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //=========== findViewById ===========
        editSenha = (EditText) findViewById(R.id.editSenha);
        editNome = (EditText) findViewById(R.id.editNome);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editTelefone = (EditText) findViewById(R.id.editTelefone);
        buttonCriarConta = (Button) findViewById(R.id.buttonCriarConta);
        spinnerTipo = (Spinner) findViewById(R.id.spinnerTipo);

        //====== Código para deixar mostrados as opções dos tipo da conta com um dropdown ======
        ArrayAdapter<CharSequence> adapter_tipo = ArrayAdapter.createFromResource(this,
                R.array.tipo_conta, android.R.layout.simple_spinner_item);

        adapter_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter_tipo);
        //======================================================================================

        //=========== Ação para Registrar a conta ===========
        buttonCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String senha = editSenha.getText().toString();
                String nome = editNome.getText().toString();
                String email = editEmail.getText().toString();
                String telefone = editTelefone.getText().toString();
                int tipo = spinnerTipo.getSelectedItemPosition();

                tipo++;

                String URL = host.HOST +"/cadastrar.php";

                if(senha.isEmpty() || nome.isEmpty() || email.isEmpty() || telefone.isEmpty()){
                    Toast.makeText(Cadastro.this, "Preenche os campos que estão em branco", Toast.LENGTH_LONG).show();
                } else{
                    Ion.with(Cadastro.this)
                            .load(URL)
                            .setBodyParameter("senha_app", senha)
                            .setBodyParameter("nome_app", nome)
                            .setBodyParameter("email_app", email)
                            .setBodyParameter("telefone_app", telefone)
                            .setBodyParameter("tipo_app", String.valueOf(tipo))
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    try{
                                        String RETORNO = result.get("CADASTRO").getAsString();

                                        if(RETORNO.equals("ID_ERRO")){
                                            Toast.makeText(Cadastro.this, "Este e-mail já está cadastrado", Toast.LENGTH_SHORT).show();
                                        } else if (RETORNO.equals("SUCESSO")){
                                            Toast.makeText(Cadastro.this, "Cadastrado com sucesso!!", Toast.LENGTH_LONG).show();
                                            Intent SucessCad = new Intent(Cadastro.this, MenuFuncionario.class);
                                            startActivity(SucessCad);
                                        } else{
                                            Toast.makeText(Cadastro.this, "Opss... Ocorreu um erro, ", Toast.LENGTH_SHORT).show();
                                        }
                                    }catch(Exception erro){
                                        Toast.makeText(Cadastro.this, "Opss... Ocorreu um erro. TENTE NOVAMENTE ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }


            }
        });
    }

    //=========== Função para não voltar a tela de Cadastro ===========
    protected void onPause() {
        super.onPause();

        finish();
    }
}
