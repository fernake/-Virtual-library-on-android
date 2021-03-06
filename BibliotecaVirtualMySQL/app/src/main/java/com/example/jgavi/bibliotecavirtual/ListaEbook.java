package com.example.jgavi.bibliotecavirtual;

import android.content.Context;
import android.content.Intent;
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

public class ListaEbook extends AppCompatActivity {
    private EditText SearchTitulo;
    private EditText SearchCurso;
    private ImageButton SearchTituloButton;
    private Button atualizarTitulo;
    private ListView tituloLista;
    private Context context;
    private AlertDialog.Builder alert;
    private int itemClicado;

    ListaEbookAdapter listaEbookAdapter;
    public ArrayList<Ebook> lista = new ArrayList<Ebook>();

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

        lista = new ArrayList<Ebook>();
        listaEbookAdapter = new ListaEbookAdapter(ListaEbook.this, lista);
        tituloLista.setAdapter(listaEbookAdapter);

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
                final Ebook titulo = (Ebook) parent.getAdapter().getItem(position);

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ListaEbook.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_info_ebook, null);
                TextView mTitulo = (TextView) mView.findViewById(R.id.textTituloInfo);
                TextView mAutor = (TextView) mView.findViewById(R.id.textAutorInfo);
                TextView mEdicao = (TextView) mView.findViewById(R.id.textEdicaoInfo);
                TextView mEditora = (TextView) mView.findViewById(R.id.textEditoraInfo);
                TextView mAno = (TextView) mView.findViewById(R.id.textAnoInfo);
                TextView mLocal = (TextView) mView.findViewById(R.id.textLocalInfo);
                TextView mAssunto = (TextView) mView.findViewById(R.id.textAssuntoInfo);
                Button mLer = (Button) mView.findViewById(R.id.buttonLerEbook);
                ImageView iconeLivro = (ImageView) mView.findViewById(R.id.CapaLivroInfoView);

                mTitulo.setText(titulo.getTitulo());
                mAutor.setText(titulo.getAutor());
                mEdicao.setText(titulo.getEdicao());
                mEditora.setText(titulo.getEditora());
                mAno.setText(titulo.getAno());
                mLocal.setText(titulo.getLocal());
                mAssunto.setText(titulo.getAssunto());

                String link = host.HOSTPDF + titulo.getPdf_icon();
                Ion.with(iconeLivro).placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder).load(link);

                mLer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ListaEbook.this, titulo.getTitulo(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ListaEbook.this, pdf.class);
                        i.putExtra("PATH",titulo.getPdf_url());
                        startActivity(i);
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
        String url = host.HOST +"/ebooklista.php";
        lista.clear();

        Ion.with(getBaseContext())
                .load(url)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        for(int i = 0; i < result.size(); i++){
                            JsonObject obj = result.get(i).getAsJsonObject();
                            Ebook titulo = new Ebook();

                            titulo.setId(obj.get("id").getAsInt());
                            titulo.setCodigo(obj.get("codigo").getAsString());
                            titulo.setTitulo(obj.get("titulo").getAsString());
                            titulo.setAutor(obj.get("autor").getAsString());
                            titulo.setEdicao(obj.get("edicao").getAsString());
                            titulo.setEditora(obj.get("editora").getAsString());
                            titulo.setAno(obj.get("ano").getAsString());
                            titulo.setLocal(obj.get("local").getAsString());
                            titulo.setCurso(obj.get("curso").getAsString());
                            titulo.setAssunto(obj.get("assunto").getAsString());
                            titulo.setPdf_url(host.HOSTPDF+obj.get("pdf_url").getAsString());
                            titulo.setPdf_icon(obj.get("pdf_icon").getAsString());

                            lista.add(titulo);
                        }
                        listaEbookAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void procurarTitulo(String titulo, String curso){
        String url = host.HOST +"/procurarebook.php";
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
                            Ebook titulo = new Ebook();

                            titulo.setId(obj.get("id").getAsInt());
                            titulo.setCodigo(obj.get("codigo").getAsString());
                            titulo.setTitulo(obj.get("titulo").getAsString());
                            titulo.setAutor(obj.get("autor").getAsString());
                            titulo.setEdicao(obj.get("edicao").getAsString());
                            titulo.setEditora(obj.get("editora").getAsString());
                            titulo.setAno(obj.get("ano").getAsString());
                            titulo.setLocal(obj.get("local").getAsString());
                            titulo.setCurso(obj.get("curso").getAsString());
                            titulo.setAssunto(obj.get("assunto").getAsString());
                            titulo.setPdf_url(host.HOSTPDF+obj.get("pdf_url").getAsString());
                            titulo.setPdf_icon(obj.get("pdf_icon").getAsString());

                            lista.add(titulo);
                        }
                        listaEbookAdapter.notifyDataSetChanged();
                    }
                });

    }
    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
