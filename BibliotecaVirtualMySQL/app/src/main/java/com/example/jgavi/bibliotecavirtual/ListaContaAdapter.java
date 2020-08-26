package com.example.jgavi.bibliotecavirtual;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListaContaAdapter extends BaseAdapter {
    //=========== Objetos ===========
    private Context context;
    private ArrayList<Usuario> lista;
    String tipo_user;

    LayoutInflater inflater;


    public ListaContaAdapter(Context ctx, ArrayList<Usuario> lista2){
        context = ctx;
        lista = lista2;
    }

    @Override
    public int getCount(){
        return lista.size();
    }

    @Override
    public Usuario getItem(int position){
        return lista.get(position);
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = null;
        if (convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            v = inflater.inflate(R.layout.contato_lista, null);
        } else{
            v = convertView;
        }

        Usuario usuario = getItem(position);

        TextView itemNome = (TextView) v.findViewById(R.id.textNomeList);
        TextView itemEmail = (TextView) v.findViewById(R.id.textEmailList);
        TextView itemTelefone = (TextView) v.findViewById(R.id.textTelefoneList);
        TextView itemTipo = (TextView) v.findViewById(R.id.textTipoList);

        if (usuario.getTipo() == 1){
            tipo_user = "Funcionário";
        } else if(usuario.getTipo() == 2){
            tipo_user = "Estudante";
        } else if(usuario.getTipo() == 3){
            tipo_user = "Professor";
        } else if(usuario.getTipo() == 4){
            tipo_user = "Bloqueado";
            itemNome.setTextColor(Color.RED);
        }


        itemNome.setText(usuario.getNome());
        itemEmail.setText(usuario.getEmail());
        itemTelefone.setText(usuario.getTelefone());
        itemTipo.setText(tipo_user);

        return v;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        //this.lista = ProcurarUsuario.lista;
        this.lista = (ArrayList<Usuario>) ProcurarUsuario.lista.clone();
        ProcurarUsuario.lista.clear();
        if (charText.length() == 0){
            ProcurarUsuario.lista.addAll(this.lista);
        } else{
            for(Usuario user : this.lista){
                if(user.getNome().toLowerCase(Locale.getDefault()).contains(charText)){
                    ProcurarUsuario.lista.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }

}
