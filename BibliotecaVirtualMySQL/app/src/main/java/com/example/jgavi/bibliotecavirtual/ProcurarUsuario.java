package com.example.jgavi.bibliotecavirtual;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProcurarUsuario extends AppCompatActivity implements SearchView.OnQueryTextListener {
    //=========== Objetos ===========
    private SearchView search;
    private ImageButton VoiceButton;
    private ListView ListViewContatos;
    private int itemClicado;
    private Context context;
    private AlertDialog.Builder alert;
    private Button atualizarLista;
    String tipo_user;



    ListaContaAdapter listaContaAdapter;
    public static ArrayList<Usuario> lista = new ArrayList<Usuario>();

    //=========== Para conexão do localhost ===========
    LocalHost host = new LocalHost();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurar_usuario);

        //=========== findViewById ===========
        search = (SearchView) findViewById(R.id.editNomeSearch);
        ListViewContatos = (ListView) findViewById(R.id.ListViewContatos);
        atualizarLista = (Button) findViewById(R.id.atualizarListaUser);

        //========================== CÓDIGO PARA LISTA DE USUÁRIOS =================================
        lista = new ArrayList<Usuario>();
        listaContaAdapter = new ListaContaAdapter(ProcurarUsuario.this, lista);
        ListViewContatos.setAdapter(listaContaAdapter);

        search.setQueryHint("Informe o nome do Usuário");

        listaUsuario();

       search.setOnQueryTextListener(this);

       //comando para atualizar a lista de usuário
       atualizarLista.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               listaUsuario();
           }
       });


       // Comando de opções do usuário
        ListViewContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
              final Usuario user = (Usuario) adapterView.getAdapter().getItem(position);

                if (user.getTipo() == 1){
                    tipo_user = "Funcionário";
                } else if(user.getTipo() == 2){
                    tipo_user = "Estudante";
                } else if(user.getTipo() == 3){
                    tipo_user = "Professor";
                } else if(user.getTipo() == 4){
                    tipo_user = "Bloqueado";
                }

              AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProcurarUsuario.this);
              View mView = getLayoutInflater().inflate(R.layout.dialog_info_usuario, null);
              TextView mTitle = (TextView) mView.findViewById(R.id.textInfoTitle);
              TextView mTxtNome = (TextView) mView.findViewById(R.id.txtn);
              TextView mNome = (TextView) mView.findViewById(R.id.textNomeInfo);
              TextView mTxtEmail = (TextView) mView.findViewById(R.id.txtee);
              TextView mEmail = (TextView) mView.findViewById(R.id.textEmailInfo);
              TextView mTxtTel = (TextView) mView.findViewById(R.id.txttt);
              TextView mTelefone = (TextView) mView.findViewById(R.id.textTelefoneInfo);
              Button mEditar = (Button) mView.findViewById(R.id.buttonEditar);
              Button mBloquear = (Button) mView.findViewById(R.id.buttonBloquear);
              Button mDebloquear = (Button) mView.findViewById(R.id.buttonDesbloquear);

              mTitle.setText("Informações do "+tipo_user);
              mNome.setText(user.getNome());
              mEmail.setText(user.getEmail());
              mTelefone.setText(user.getTelefone());

              mEditar.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Bundle args = new Bundle();
                      args.putSerializable("editar", user);
                      Intent intent = new Intent(ProcurarUsuario.this, EditarUsuario.class);
                      intent.putExtra("conta", args);
                      startActivity(intent);
                  }
              });

              mBloquear.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      int i = user.getId();
                      int tipo = user.getTipo();
                      if(tipo == 4){
                          Toast.makeText(ProcurarUsuario.this, "Este usuário já está bloqueado", Toast.LENGTH_SHORT).show();
                      } else if(tipo == 1){
                          Toast.makeText(ProcurarUsuario.this, "Não é possível bloquear usuários do tipo Funcionários", Toast.LENGTH_SHORT).show();
                      }
                        else {
                          String url = host.HOST +"/desbloquearusuario.php";

                          Ion.with(getBaseContext())
                                  .load(url)
                                  .setBodyParameter("tipo_app", "4")
                                  .setBodyParameter("id_app", String.valueOf(i))
                                  .asJsonObject()
                                  .setCallback(new FutureCallback<JsonObject>() {
                                      @Override
                                      public void onCompleted(Exception e, JsonObject result) {
                                          try {
                                              String RETORNO = result.get("EDITAR").getAsString();

                                              if (RETORNO.equals("ERRO")) {
                                                  Toast.makeText(ProcurarUsuario.this, "Ocorreu um erro, tente mais tarde", Toast.LENGTH_SHORT).show();
                                              } else if (RETORNO.equals("SUCESSO")) {
                                                  Toast.makeText(ProcurarUsuario.this, "Usuário bloqueado", Toast.LENGTH_LONG).show();
                                              }
                                          } catch (Exception erro){
                                              Toast.makeText(ProcurarUsuario.this, "ERRO INESPERADO", Toast.LENGTH_LONG).show();
                                          }
                                      }
                                  });

                      }
                  }
              });

              mDebloquear.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      int tipo = user.getTipo();

                      if(tipo != 4){
                          Toast.makeText(ProcurarUsuario.this, "Este usuário já está desbloqueado", Toast.LENGTH_SHORT).show();
                      } else if(tipo == 4) {
                          Bundle args = new Bundle();
                          args.putSerializable("editar", user);
                          Intent intent = new Intent(ProcurarUsuario.this, EditarUsuario.class);
                          intent.putExtra("conta", args);
                          startActivity(intent);
                      }
                  }
              });

              mDebloquear.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Bundle args = new Bundle();
                      args.putSerializable("editar", user);
                      Intent intent = new Intent(ProcurarUsuario.this, OpcaoDesbloqueio.class);
                      intent.putExtra("conta", args);
                      startActivity(intent);
                  }
              });

              mBuilder.setView(mView);
              AlertDialog dialog = mBuilder.create();
              dialog.show();
              itemClicado = position;
            }
        });
    }

    //========================== FUNÇÕES =================================
    private void listaUsuario() {
        String url = host.HOST +"/listausuario.php";

        Ion.with(getBaseContext())
                .load(url)
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
                            user.setTipo(obj.get("tipo").getAsInt());
                            user.setId(obj.get("id").getAsInt());
                            user.setSenha(obj.get("senha").getAsString());

                            lista.add(user);
                        }

                        listaContaAdapter.notifyDataSetChanged();
                    }
                });

    }

    public boolean onQueryTextSubmit(String query) {

        return false;
    }

 //  @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        listaContaAdapter.filter(text);
        return false;
    }

    void EditUser(Usuario user){
        
    }

    //=========== Função "pause" ===========
    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
