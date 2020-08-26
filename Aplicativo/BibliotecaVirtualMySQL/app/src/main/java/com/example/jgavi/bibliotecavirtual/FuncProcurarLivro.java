package com.example.jgavi.bibliotecavirtual;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FuncProcurarLivro extends AppCompatActivity {
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

        SearchTitulo = (EditText) findViewById(R.id.TituloFuncTxt);
        SearchCurso = (EditText) findViewById(R.id.CursoFuncTxt);
        SearchTituloButton = (ImageButton) findViewById(R.id.SearchTituloButton);
        atualizarTitulo = (Button) findViewById(R.id.buttonAtualizar);
        tituloLista = (ListView) findViewById(R.id.ListViewTituloFunc);

        lista = new ArrayList<Titulo>();
        listaTituloAdapter = new ListaTituloAdapter(FuncProcurarLivro.this, lista);
        tituloLista.setAdapter(listaTituloAdapter);

        listaTitulo();

        atualizarTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaTitulo();
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

        tituloLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


        tituloLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Titulo titulo = (Titulo) parent.getAdapter().getItem(position);

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(FuncProcurarLivro.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_func_livro, null);
                TextView mCodigo = (TextView) mView.findViewById(R.id.textCodInfo);
                TextView mTitulo = (TextView) mView.findViewById(R.id.textTituloInfo);
                TextView mAutor = (TextView) mView.findViewById(R.id.textAutorInfo);
                TextView mEdicao = (TextView) mView.findViewById(R.id.textEdicaoInfo);
                TextView mEditora = (TextView) mView.findViewById(R.id.textEditoraInfo);
                TextView mAno = (TextView) mView.findViewById(R.id.textAnoInfo);
                TextView mLocal = (TextView) mView.findViewById(R.id.textLocalInfo);
              //  TextView mCurso = (TextView) mView.findViewById(R.id.textCursoInfo);
                TextView mAssunto = (TextView) mView.findViewById(R.id.textAssuntoInfo);
                Button mExclui = (Button) mView.findViewById(R.id.ButtonExcluirLivro);
                Button mTirarReserva = (Button) mView.findViewById(R.id.buttonVerificarReserva);
                Button mIndisponivel = (Button) mView.findViewById(R.id.buttonEmprestimo);
                ImageView iconeLivro = (ImageView) mView.findViewById(R.id.CapaLivroInfoView);

                mCodigo.setText("código: "+titulo.getCodigo());
                mTitulo.setText(titulo.getTitulo());
                mAutor.setText(titulo.getAutor());
                mEdicao.setText(titulo.getEdicao());
                mEditora.setText(titulo.getEditora());
                mAno.setText(titulo.getAno());
                mLocal.setText(titulo.getLocal());
                //mCurso.setText(titulo.getCurso());
                mAssunto.setText(titulo.getAssunto());

            String link = host.HOSTPDF + titulo.getImg_url();
            Ion.with(iconeLivro).placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder).load(link);

            mExclui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = host.HOST +"/deletarlivro.php";
                    int id = titulo.getId();

                    Ion.with(FuncProcurarLivro.this)
                            .load(url)
                            .setBodyParameter("id_app", String.valueOf(id))
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    String RETORNO = result.get("DELETAR").getAsString();

                                    if(RETORNO.equals("SUCESSO")){
                                        Toast.makeText(FuncProcurarLivro.this, "Título Excluído", Toast.LENGTH_LONG).show();
                                    } else if(RETORNO.equals("ERRO")){
                                        Toast.makeText(FuncProcurarLivro.this, "ERRO - tente mais tarde", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            });

            mTirarReserva.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = host.HOST +"/tirarReservaLivro.php";
                    int id = titulo.getId();
                    String reserva = "disp";

                    if (titulo.getDisponivel().equals("disp")){
                        Toast.makeText(FuncProcurarLivro.this, "Este titulo já está sem reserva", Toast.LENGTH_LONG).show();
                    } else if(titulo.getDisponivel().equals("in")) {
                        Ion.with(FuncProcurarLivro.this)
                                .load(url)
                                .setBodyParameter("id_app", String.valueOf(id))
                                .setBodyParameter("disponivel_app", reserva)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        String RETORNO = result.get("RESERVA").getAsString();

                                        if (RETORNO.equals("SUCESSO")) {
                                            Toast.makeText(FuncProcurarLivro.this, "Agora este título está novamente disponível para o empréstimo", Toast.LENGTH_LONG).show();
                                        } else if (RETORNO.equals("ERRO")) {
                                            Toast.makeText(FuncProcurarLivro.this, "ERRO - tente mais tarde", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else {
                        Bundle args = new Bundle();
                        args.putSerializable("titulo", titulo);
                        Intent reservas = new Intent(FuncProcurarLivro.this, TirarReserva.class);
                        reservas.putExtra("reserva", args);
                        startActivity(reservas);
                    }
                }
            });

            mIndisponivel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = host.HOST +"/tirarReservaLivro.php";
                    int id = titulo.getId();
                    String reserva = "in";


                    if (titulo.getDisponivel().equals("in")){
                        Toast.makeText(FuncProcurarLivro.this, "Este titulo já está indisponível", Toast.LENGTH_LONG).show();
                    } else {
                        Ion.with(FuncProcurarLivro.this)
                                .load(url)
                                .setBodyParameter("id_app", String.valueOf(id))
                                .setBodyParameter("disponivel_app", reserva)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        String RETORNO = result.get("RESERVA").getAsString();

                                        if (RETORNO.equals("SUCESSO")) {
                                            Toast.makeText(FuncProcurarLivro.this, "Agora este livro está no momento indisponível", Toast.LENGTH_LONG).show();
                                        } else if (RETORNO.equals("ERRO")) {
                                            Toast.makeText(FuncProcurarLivro.this, "ERRO - tente mais tarde", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                }
            });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
                itemClicado = position;
            }
        });

    }

    //============= FUNÇÕES DA LISTVIEW ===============

    private void listaTitulo(){
        String url = host.HOST +"/listaTitulo.php";
        lista.clear();

        Ion.with(getBaseContext())
                .load(url)
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

    //=========== Função "pause" ===========
    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
