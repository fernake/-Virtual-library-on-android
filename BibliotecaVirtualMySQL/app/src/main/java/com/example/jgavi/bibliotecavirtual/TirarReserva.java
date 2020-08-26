package com.example.jgavi.bibliotecavirtual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class TirarReserva extends AppCompatActivity {
    private TextView TxtTitulo, TxtEstudante, TxtEmail, TxtTelefone;
    private Button BtnVoltar, BtnReserva;
    LocalHost host = new LocalHost();
    Titulo title = new Titulo();
   // Usuario user = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tirar_reserva);

        // Pegando as info. do título reservado
        Bundle args = getIntent().getBundleExtra("reserva");
        final Titulo titulo = (Titulo) args.getSerializable("titulo");
        title.setDisponivel(titulo.getDisponivel());

        TxtTitulo = (TextView) findViewById(R.id.tituloFinal);
        TxtEstudante = (TextView) findViewById(R.id.nomeEstudanteFinal);
        BtnVoltar = (Button) findViewById(R.id.buttonVoltarMenuReserva);
        BtnReserva = (Button) findViewById(R.id.buttonReservaRetirada);
        TxtEmail = (TextView) findViewById(R.id.emailFinal);
        TxtTelefone = (TextView) findViewById(R.id.telefoneFinal);

     //   PegarUsuario();
        String idtitle = titulo.getDisponivel();
        String url = host.HOST +"searchiduser.php";

        Ion.with(TirarReserva.this)
                .load(url)
                .setBodyParameter("id_app", idtitle)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                            for(int i = 0; i < result.size(); i++){
                                JsonObject obj = result.get(i).getAsJsonObject();
                                Usuario user = new Usuario();

                                user.setNome(obj.get("nome").getAsString());
                                user.setEmail(obj.get("email").getAsString());
                                user.setTelefone(obj.get("telefone").getAsString());

                                TxtEstudante.setText(user.getNome());
                                TxtEmail.setText(user.getEmail());
                                TxtTelefone.setText(user.getTelefone());


                        }
                    }
                });


        TxtTitulo.setText(titulo.getTitulo());

        BtnReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = host.HOST +"/tirarReservaLivro.php";
                int id = titulo.getId();
                String reserva = "disp";
                Ion.with(TirarReserva.this)
                        .load(url)
                        .setBodyParameter("id_app", String.valueOf(id))
                        .setBodyParameter("disponivel_app", reserva)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                String RETORNO = result.get("RESERVA").getAsString();

                                if (RETORNO.equals("SUCESSO")) {
                                    Toast.makeText(TirarReserva.this, "Agora este título está novamente disponível para o empréstimo", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(TirarReserva.this, FuncProcurarLivro.class);
                                    startActivity(intent);

                                } else if (RETORNO.equals("ERRO")) {
                                    Toast.makeText(TirarReserva.this, "ERRO - tente mais tarde", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        BtnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TirarReserva.this, FuncProcurarLivro.class);
                startActivity(intent);
            }
        });

    }


    //=========== Função "pause" ===========
    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
