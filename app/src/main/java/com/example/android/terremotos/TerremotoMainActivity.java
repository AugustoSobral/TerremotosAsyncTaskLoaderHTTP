package com.example.android.terremotos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TerremotoMainActivity extends AppCompatActivity implements LoaderCallbacks<List<Terremoto>> {

    public static final String LOG_TAG = TerremotoMainActivity.class.getName();
    private static final String STRING_URL_REQUEST = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    public TerremotoAdapter mAdapter;
    private static final int ID_LOADER = 1;
    private TextView mTextoListaVazia;
    private ProgressBar mProgressBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.config_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, ConfigActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
       mAdapter = new TerremotoAdapter(this, new ArrayList<Terremoto>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Achar o terremoto atual que foi clicado, Substituímos o método onItemClick(), de modo que quando um item de lista é clicado, encontramos o objeto Earthquake correspondente a partir do adapter
                Terremoto terremotoAtual= mAdapter.getItem(position);
                // Converte o URL String em um objeto URI (para passar no construtor de Intent)
                Uri url = Uri.parse(terremotoAtual.getUrl());
                // Cria um novo intent para visualizar a URI do earthquake
                Intent webSiteIntent = new Intent(Intent.ACTION_VIEW, url);

                startActivity(webSiteIntent);

            }
        });
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        mTextoListaVazia = (TextView) findViewById(R.id.textVazio);
        //Programando a listView pra mostrar uma view quando ela estiver vazia.
        earthquakeListView.setEmptyView(mTextoListaVazia);

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Obtém uma referência ao LoaderManager, a fim de interagir com loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Inicializa o loader. Passa um ID constante int definido acima e passa nulo para
            // o bundle. Passa esta activity para o parâmetro LoaderCallbacks (que é válido
            // porque esta activity implementa a interface LoaderCallbacks).
            loaderManager.initLoader(ID_LOADER, null, this);
        } else{

            mProgressBar =(ProgressBar) findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.GONE);

            mTextoListaVazia.setText(R.string.noConnection);
        }

    }

    @Override
    public Loader<List<Terremoto>> onCreateLoader(int id, Bundle args) {
        // Criar um novo loader para a dada URL

        //Pega a string digitada pelo usuário em Configurações de Terremoto - Magnitude Mínima usando sua key para identificação de qual preferência nos referimos
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        //Pega a opção de "ordenar por" escolhida pelo usuário em Configurações de Terremoto - Ordenar por
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));


        //Passa a string de url base pra uri(formato geral de url cujo java tem classe builder) e com o builder adicionamos os parâmetros à URI
        Uri baseUri = Uri.parse(STRING_URL_REQUEST);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new TerremotoLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<Terremoto>> loader, List<Terremoto> terremotos) {

// Esconde o indicador de carregamento porque os dados foram carregados
        mProgressBar =(ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

// Limpa o adapter dos antigos dados de earthquake
        mAdapter.clear();
        // Se há uma lista válida de {@link Earthquake}s, então adiciona-os ao data set do adapter.
        // Isso ativará a atualização da ListView.
        if (terremotos != null && !terremotos.isEmpty()) {
            mAdapter.addAll(terremotos);
        }
        //Quando a listView estiver vazia, mostrará o texto:
        mTextoListaVazia.setText(R.string.vazio);

    }

    @Override
    public void onLoaderReset(Loader<List<Terremoto>> loader) {
// Reseta o Loader, então podemos limpar nossos dados existentes.
        mAdapter.clear();
    }


}
