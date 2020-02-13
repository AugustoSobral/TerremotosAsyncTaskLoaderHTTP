package com.example.android.terremotos;

import android.nfc.Tag;
import android.support.annotation.Nullable;
import android.content.AsyncTaskLoader;
import android.content.Context;

import java.net.URL;
import java.util.List;


/* Loaders entram em cena para trazer otimização e melhor performance no uso de memória, se você estiver usando um simples
   AsyncTask e girar a tela do celular por exemplo, serão criadas várias activities com várias tarefas em segundo plano.*/

public class TerremotoLoader extends AsyncTaskLoader<List<Terremoto>> {
    // Tag para mensagens de log
    private static final String LOG_TAG = TerremotoLoader.class.getName();

    // URL da busca
    private String mUrl;

    //Construtor
    public TerremotoLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Terremoto> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Realiza requisição de rede, decodifica a resposta, e extrai uma lista de earthquakes.
        List<Terremoto> terremotos = JSON_Resposta.extractEarthquakes(mUrl);
        return terremotos;
    }


}
