package com.example.jgavi.bibliotecavirtual;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class ListaReservado extends AppCompatActivity {
    private EditText SearchTitulo;
    private EditText SearchCurso;
    private ImageButton SearchTituloButton;
    private Button atualizarTitulo;
    private ListView tituloLista;
    private Context context;
    private AlertDialog.Builder alert;
    private int itemClicado;

    ListaTituloAdapter listaTituloAdapter;
    public ArrayList<Titulo> lista = new ArrayList<Titulo>();

    //=========== Para conexão do localhost ===========
    LocalHost host = new LocalHost();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func_procurar_livro);

        //======================= PEGANDO AS INFO DO USUÁRIO ==========================
        Bundle args = getIntent().getBundleExtra("conta");
        final Usuario user = (Usuario) args.getSerializable("user");
        final int idUser = user.getId();

        SearchTitulo = (EditText) findViewById(R.id.TituloFuncTxt);
        SearchCurso = (EditText) findViewById(R.id.CursoFuncTxt);
        SearchTituloButton = (ImageButton) findViewById(R.id.SearchTituloButton);
        atualizarTitulo = (Button) findViewById(R.id.buttonAtualizar);
        tituloLista = (ListView) findViewById(R.id.ListViewTituloFunc);

        lista = new ArrayList<Titulo>();
        listaTituloAdapter = new ListaTituloAdapter(ListaReservado.this, lista);
        tituloLista.setAdapter(listaTituloAdapter);

        listaTitulo(String.valueOf(idUser));

        atualizarTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaTitulo(String.valueOf(idUser));
            }
        });

        SearchTituloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tituloPego = SearchTitulo.getText().toString();
                String cursoPego = SearchCurso.getText().toString();
                procurarTitulo(tituloPego, cursoPego);
            }
        });


    }

    //============= FUNÇÕES DA LISTVIEW ===============

    private void listaTitulo(String disp){
        String url = host.HOST +"/procurarreserva.php";
        lista.clear();

        Ion.with(getBaseContext())
                .load(url)
                .setBodyParameter("disponivel_app", disp)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        for(int i = 0; i < result.size(); i++){
                            JsonObject obj = result.get(i).getAsJsonObject();
                            Titulo titulo = new Titulo();

                            titulo.setId(obj.get("id").getAsInt());
                            titulo.setCodigo(obj.get("codigo").getAsString());
                            titulo.setTitulo(obj.get("titulo").getAsString());
                            titulo.setAutor(obj.get("autor").getAsString());
                            titulo.setEdicao(obj.get("edicao").getAsString());
                            titulo.setEditora(obj.get("editora").getAsString());
                            titulo.setAno(obj.get("ano").getAsString());
                            titulo.setLocal(obj.get("local").getAsString());
                            titulo.setCurso(obj.get("curso").getAsString());
                            titulo.setAssunto(obj.get("Assunto").getAsString());
                            titulo.setImg_url(obj.get("img_url").getAsString());
                            titulo.setDisponivel(obj.get("disponivel").getAsString());

                            lista.add(titulo);
                        }
                        listaTituloAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void procurarTitulo(String titulo, String curso){
        String url = host.HOST +"/procurartitulo.php";
        lista.clear();

        Ion.with(getBaseContext())
                .load(url)
                .setBodyParameter("titulo_app", titulo)
                .setBodyParameter("curso_app", curso)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    //JSONArray jsonArray = null;
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        for (int i = 0; i < result.size(); i++) {
                            JsonObject obj = result.get(i).getAsJsonObject();
                            Titulo titulo = new Titulo();

                            titulo.setId(obj.get("id").getAsInt());
                            titulo.setCodigo(obj.get("codigo").getAsString());
                            titulo.setTitulo(obj.get("titulo").getAsString());
                            titulo.setAutor(obj.get("autor").getAsString());
                            titulo.setEdicao(obj.get("edicao").getAsString());
                            titulo.setEditora(obj.get("editora").getAsString());
                            titulo.setAno(obj.get("ano").getAsString());
                            titulo.setLocal(obj.get("local").getAsString());
                            titulo.setCurso(obj.get("curso").getAsString());
                            titulo.setAssunto(obj.get("Assunto").getAsString());
                            titulo.setImg_url(obj.get("img_url").getAsString());
                            titulo.setDisponivel(obj.get("disponivel").getAsString());

                            lista.add(titulo);
                        }
                        listaTituloAdapter.notifyDataSetChanged();
                    }
                });

    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}

