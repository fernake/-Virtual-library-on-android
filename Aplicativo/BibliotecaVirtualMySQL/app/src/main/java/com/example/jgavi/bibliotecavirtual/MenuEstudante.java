package com.example.jgavi.bibliotecavirtual;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MenuEstudante extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        LocalHost localHost = new LocalHost();
        Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_estudante);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //======================= PEGANDO AS INFO DO USUÁRIO ==========================
        Bundle args = getIntent().getBundleExtra("conta");
        final Usuario user = (Usuario) args.getSerializable("login");
        String nomeUser = user.getNome();
        String emailUser = user.getEmail();
        int idUser = user.getId();

        usuario.setId(idUser);

        // ======================= MENU DO EBOOK ==============================
        final GridView myGridView= findViewById(R.id.myGridViewMENU);
        Button btnRetrieve= findViewById(R.id.downloadBtnMENU);
        final ProgressBar myProgressBar= findViewById(R.id.myProgressBarMENU);

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MenuEstudante.JSONDownloader(MenuEstudante.this).retrieve(myGridView,myProgressBar);
               //
            }
        });


        // ====================================================================



     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView nomeEstudante= (TextView) headerView.findViewById(R.id.NomeEstudante);
        TextView emailEstudante= (TextView) headerView.findViewById(R.id.EmailEstudante);

        nomeEstudante.setText(nomeUser);
        emailEstudante.setText(emailUser);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_estudante, menu);
        return true;
    } */


   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // nav_camera = nav_ebook
        if (id == R.id.nav_ebook) {
            Intent ebook = new Intent(MenuEstudante.this, ListaEbook.class);
            startActivity(ebook);

        } else if (id == R.id.nav_titulo) {
            Bundle args = new Bundle();
            args.putSerializable("user", usuario);
            Intent titulo = new Intent(MenuEstudante.this, ListaTitulo.class);
            titulo.putExtra("conta", args);
            startActivity(titulo);
        } else if (id == R.id.nav_reserva) {
            Bundle args = new Bundle();
            args.putSerializable("user", usuario);
            Intent titulo = new Intent(MenuEstudante.this, ListaReservado.class);
            titulo.putExtra("conta", args);
            startActivity(titulo);
        } //else if (id == R.id.nav_manage) {

        //} else if (id == R.id.nav_share) {

        //} else if (id == R.id.nav_send) {

        //}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //=========================================================================================
    //================================== EBOOK CONFIGURAÇÃO ===================================
    //=========================================================================================

    public class PDFDoc {
        private int id;
        private String name,category,pdfURL,pdfIconURL, author, codigo, edicao, editora, ano, local, assunto;
        public int getId() {return id;}
        public void setId(int id) {this.id = id;}
        public String getAssunto() {return assunto;}
        public void setAssunto(String assunto) {this.assunto = assunto;}
        public String getLocal() {return local;}
        public void setLocal(String local) {this.local = local;}
        public String getAno() {return ano;}
        public void setAno(String ano) {this.ano = ano;}
        public String getEditora() {return editora;}
        public void setEditora(String editora) {this.editora = editora;}
        public String getEdicao() {return edicao;}
        public void setEdicao(String edicao) {this.edicao = edicao;}
        public String getCodigo() {return codigo;}
        public void setCodigo(String codigo) {this.codigo = codigo;}
        public String getName() {return name;} // TITULO
        public void setName(String name) {this.name = name;}
        public String getAuthor() {return author;}
        public void setAuthor(String author) {this.author = author;}
        public String getCategory() {return category;}
        public void setCategory(String category) {this.category = category;}
        public String getPdfURL() {return pdfURL;}
        public void setPdfURL(String pdfURL) {this.pdfURL = pdfURL;}
        public String getPdfIconURL() {return pdfIconURL;}
        public void setPdfIconURL(String pdfIconURL) {this.pdfIconURL = pdfIconURL;}
    }

    public class GridViewAdapter extends BaseAdapter {
        Context c;
        ArrayList<MenuEstudante.PDFDoc> pdfDocuments;

        public GridViewAdapter(Context c, ArrayList<MenuEstudante.PDFDoc> pdfDocuments) {
            this.c = c;
            this.pdfDocuments = pdfDocuments;
        }

        @Override
        public int getCount() {return pdfDocuments.size();}
        @Override
        public Object getItem(int pos) {return pdfDocuments.get(pos);}
        @Override
        public long getItemId(int pos) {return pos;}
        @Override
        public View getView(int pos, View view, ViewGroup viewGroup){
            if(view==null)
            {
                view= LayoutInflater.from(c).inflate(R.layout.row_model,viewGroup,false);
            }

            TextView txtTitulo = view.findViewById(R.id.pdfTituloTxt);
            TextView txtAuthor = view.findViewById(R.id.authorTxt);
            ImageView pdfIcon = view.findViewById(R.id.AberturaView);

            final MenuEstudante.PDFDoc pdf= (MenuEstudante.PDFDoc) this.getItem(pos);

            txtTitulo.setText(pdf.getName());
            txtAuthor.setText(pdf.getCategory());

            if(pdf.getPdfURL() != null && pdf.getPdfURL().length()>0)
            {
                Picasso.get().load(pdf.getPdfIconURL()).placeholder(R.drawable.placeholder).into(pdfIcon);
            }else {
                Toast.makeText(c, "Empty Image URL", Toast.LENGTH_LONG).show();
                Picasso.get().load(R.drawable.pdf_icon).into(pdfIcon);
            }




            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Toast.makeText(c, pdf.getName(), Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(c, pdf.class);
                    i.putExtra("PATH",pdf.getPdfURL());
                    c.startActivity(i);
                }
            });

            return view;
        }

    }

    // HTTP CLIENT

    public class JSONDownloader{
        private static final String PDF_SITE_URL = "http://192.168.0.108/login/ebooklista.php";
        private final Context context;
        private MenuEstudante.GridViewAdapter adapter;





        public JSONDownloader(Context context) {
            this.context = context;
        }

        //PDF DOWNLOAD PELO MYSQL

        public void retrieve (final GridView gv, final ProgressBar myProgressBar){
            final ArrayList<MenuEstudante.PDFDoc> pdfDocuments = new ArrayList<>();

            myProgressBar.setIndeterminate(true);
            myProgressBar.setVisibility(View.VISIBLE);


            Ion.with(getBaseContext())
                    .load(PDF_SITE_URL)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject obj = result.get(i).getAsJsonObject();
                                MenuEstudante.PDFDoc p = new MenuEstudante.PDFDoc();

                                p.setId(obj.get("id").getAsInt());
                                p.setName(obj.get("titulo").getAsString());
                                p.setCategory(obj.get("curso").getAsString());
                                p.setPdfIconURL(localHost.HOSTPDF+obj.get("pdf_icon").getAsString());
                                p.setPdfURL(localHost.HOSTPDF+obj.get("pdf_url").getAsString());
                                p.setAuthor(obj.get("autor").getAsString());
                                p.setCodigo(obj.get("codigo").getAsString());
                                p.setEdicao(obj.get("edicao").getAsString());
                                p.setEditora(obj.get("editora").getAsString());
                                p.setAno(obj.get("ano").getAsString());
                                p.setLocal(obj.get("local").getAsString());
                                p.setAssunto(obj.get("assunto").getAsString());


                                pdfDocuments.add(p);

                                adapter = new MenuEstudante.GridViewAdapter(context, pdfDocuments);
                                gv.setAdapter(adapter);
                                myProgressBar.setVisibility(View.GONE);

                            }

                        }

                    });
        }
    }
}
