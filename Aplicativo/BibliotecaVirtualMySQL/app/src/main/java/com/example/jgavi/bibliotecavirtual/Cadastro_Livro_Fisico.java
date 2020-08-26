package com.example.jgavi.bibliotecavirtual;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cadastro_Livro_Fisico extends AppCompatActivity {
    private WebView CadLivroFisico;
    private Button voltarButton;
    LocalHost localHost = new LocalHost();
    String url = localHost.HOST +"/cadastro%20de%20livro%20fisico.html";

    public static final int INPUT_FILE_REQUEST_CODE = 1;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String diretorioFotoImagem;
    private static final String TAG = Cadastro_Livro_Fisico.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__livro__fisico);

        CadLivroFisico =(WebView) findViewById(R.id.CadLivroFisico);
        voltarButton = (Button) findViewById(R.id.VoltarCad_button);

       // CadLivroFisico.loadUrl(url);

        // define as configurações basicas
        setWebview(CadLivroFisico);

        if (savedInstanceState != null) {
            // retaura o webview
            CadLivroFisico.restoreState(savedInstanceState);
        }

        CadLivroFisico.setWebChromeClient(new WebChromeClient() {
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                if(mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // cria local onde a foto deve ir
                    File photoFile = null;
                    try {
                        photoFile = armazenarImagemTemp();
                        takePictureIntent.putExtra("PhotoPath", diretorioFotoImagem);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e(TAG, "Unable to create Image File", ex);
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        diretorioFotoImagem = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent[] intentArray;
                if(takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

                return true;
            }
        });

        // carrega a URL
        if(CadLivroFisico.getUrl() == null) {
            CadLivroFisico.loadUrl(url);
        }



    //Método para voltar
        voltarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abreMenu = new Intent(Cadastro_Livro_Fisico.this, MenuFuncionario.class);
                startActivity(abreMenu);
            }
        });

    }

    //método para armazenar imagem temporariamente
    private File armazenarImagemTemp() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    // método para adaptar as configurações padrões do WebView com o nome setWebview:
    private void setWebview(WebView webView) {
        WebSettings set = webView.getSettings();

        // habilita javascript
        set.setJavaScriptEnabled(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // oculta zoom no sdk HONEYCOMB+
            set.setDisplayZoomControls(false);
        }

        // habilita debugging remoto via chrome {inspect}
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        CadLivroFisico.setWebViewClient(new WebViewClient());
    }

    //Método onActivityResult
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        Uri[] results = null;

        // verifica se a resposta esta ok com a variável estática {RESULT_OK} do Activity
        if(resultCode == Activity.RESULT_OK) {
            if(data == null) {
                // se não houver dados solicitará que tire uma foto
                if(diretorioFotoImagem != null) {
                    results = new Uri[]{Uri.parse(diretorioFotoImagem)};
                }
            } else {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }

        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
        return;
    }

    //=========== Função "pause" ===========
    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }

}