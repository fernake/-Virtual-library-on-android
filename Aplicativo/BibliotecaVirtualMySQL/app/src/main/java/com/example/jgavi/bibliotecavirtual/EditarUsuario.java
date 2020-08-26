package com.example.jgavi.bibliotecavirtual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class EditarUsuario extends AppCompatActivity {
    //=========== Objetos ===========
    EditText editEmailEdit, editNomeEdit, editTelefoneEdit, editSenhaEdit;
    Button buttonEditar, buttonRedefinir;

    //=========== Para conexão do localhost ===========
    LocalHost host = new LocalHost();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        //========== Bundle & args ==========
        Bundle args = getIntent().getBundleExtra("conta");
        final Usuario user = (Usuario) args.getSerializable("editar");
        final int id = user.getId();

        //=========== findViewById ===========
        editEmailEdit = (EditText) findViewById(R.id.editEmailEdit);
        editNomeEdit = (EditText) findViewById(R.id.editNomeEdit);
        editTelefoneEdit = (EditText) findViewById(R.id.editTelefoneEdit);
        editSenhaEdit = (EditText) findViewById(R.id.editSenhaEdit);
        buttonEditar = (Button) findViewById(R.id.buttonEditar);
        buttonRedefinir = (Button) findViewById(R.id.buttonRedefinir);

        editEmailEdit.setText(user.getEmail());
        editNomeEdit.setText(user.getNome());
        editTelefoneEdit.setText(user.getTelefone());
        editSenhaEdit.setText(user.getSenha());

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = editEmailEdit.getText().toString();
                String Nome = editNomeEdit.getText().toString();
                String Telefone = editTelefoneEdit.getText().toString();
                String Senha = editSenhaEdit.getText().toString();

                String URL = host.HOST +"/editarusuario.php";

                if(Email.isEmpty() || Nome.isEmpty() || Telefone.isEmpty()){
                    Toast.makeText(EditarUsuario.this, "Preenche os campos que estão em branco", Toast.LENGTH_LONG).show();
                } else {
                    Ion.with(EditarUsuario.this)
                            .load(URL)
                            .setBodyParameter("nome_app", Nome)
                            .setBodyParameter("email_app", Email)
                            .setBodyParameter("senha_app", Senha)
                            .setBodyParameter("telefone_app", Telefone)
                            .setBodyParameter("id_app", String.valueOf(id))
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    try{
                                        String RETORNO = result.get("EDITAR").getAsString();

                                        if(RETORNO.equals("ERRO")){
                                            Toast.makeText(EditarUsuario.this, "Ocorreu um erro, tente mais tarde", Toast.LENGTH_SHORT).show();
                                        } else if (RETORNO.equals("ID_ERRO")) {
                                            Toast.makeText(EditarUsuario.this, "Este e-mail já está cadastrado", Toast.LENGTH_SHORT).show();

                                        }else if (RETORNO.equals("SUCESSO")){
                                            Toast.makeText(EditarUsuario.this, "Registro Atualizado", Toast.LENGTH_LONG).show();
                                            Intent SucessEdit = new Intent(EditarUsuario.this, ProcurarUsuario.class);
                                            startActivity(SucessEdit);
                                        } else{
                                            Toast.makeText(EditarUsuario.this, "Opss... Ocorreu um erro, ", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch(Exception erro){
                                           // Toast.makeText(EditarUsuario.this, "Ocorreu um erro, tente novamente", Toast.LENGTH_SHORT).show();
                                            Intent SucessEdit = new Intent(EditarUsuario.this, ProcurarUsuario.class);
                                            startActivity(SucessEdit);
                                    }
                                }
                            });
                }

            }
        });

        buttonRedefinir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEmailEdit.setText(user.getEmail());
                editNomeEdit.setText(user.getNome());
                editTelefoneEdit.setText(user.getTelefone());
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
