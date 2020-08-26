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

public class ListaTitulo extends AppCompatActivity {
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
        listaTituloAdapter = new ListaTituloAdapter(ListaTitulo.this, lista);
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

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ListaTitulo.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_info_livro, null);
                TextView mTitulo = (TextView) mView.findViewById(R.id.textTituloInfo);
                TextView mAutor = (TextView) mView.findViewById(R.id.textAutorInfo);
                TextView mEdicao = (TextView) mView.findViewById(R.id.textEdicaoInfo);
                TextView mEditora = (TextView) mView.findViewById(R.id.textEditoraInfo);
                TextView mAno = (TextView) mView.findViewById(R.id.textAnoInfo);
                TextView mLocal = (TextView) mView.findViewById(R.id.textLocalInfo);
                TextView mAssunto = (TextView) mView.findViewById(R.id.textAssuntoInfo);
                Button mReserva = (Button) mView.findViewById(R.id.buttonReserva);
                ImageView iconeLivro = (ImageView) mView.findViewById(R.id.CapaLivroInfoView);

                mTitulo.setText(titulo.getTitulo());
                mAutor.setText(titulo.getAutor());
                mEdicao.setText(titulo.getEdicao());
                mEditora.setText(titulo.getEditora());
                mAno.setText(titulo.getAno());
                mLocal.setText(titulo.getLocal());
                mAssunto.setText(titulo.getAssunto());

                mReserva.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = host.HOST +"/tirarReservaLivro.php";
                        int id = titulo.getId();
                        String reserva = String.valueOf(idUser);

                        if (titulo.getDisponivel().equals("disp")){
                            Ion.with(ListaTitulo.this)
                                    .load(url)
                                    .setBodyParameter("id_app", String.valueOf(id))
                                    .setBodyParameter("disponivel_app", reserva)
                                    .asJsonObject()
                                    .setCallback(new FutureCallback<JsonObject>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonObject result) {
                                            String RETORNO = result.get("RESERVA").getAsString();

                                            if (RETORNO.equals("SUCESSO")) {
                                                Toast.makeText(ListaTitulo.this, "Reserva feita com sucesso", Toast.LENGTH_LONG).show();
                                            } else if (RETORNO.equals("ERRO")) {
                                                Toast.makeText(ListaTitulo.this, "ERRO - tente mais tarde", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(ListaTitulo.this, "Este titulo já está reservado ou está indisponível", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                String link = host.HOSTPDF + titulo.getImg_url();
                Ion.with(iconeLivro).placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder).load(link);



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

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
