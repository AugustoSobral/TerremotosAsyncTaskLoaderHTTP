package com.example.android.terremotos;


import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TerremotoAdapter extends ArrayAdapter<Terremoto> {

    private static final String LOCATION_SEPARATOR = " of ";

    //Construtor
    public TerremotoAdapter (Context context, ArrayList<Terremoto> infoTerremotos){

        super(context, 0 , infoTerremotos);

    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_list_item, parent, false);
        }

        //Pegando as informações dos terremotos nas respectivas posições do array
        Terremoto atualTerremoto = getItem(position);

        //Formatando a magnitude para mostras apenas 2 dígitos com uma função criada

        double mag = atualTerremoto.getMagnitude();
        String magnitude = formatarMag(mag);

        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);
        magnitudeView.setText(magnitude);

        // Configure a cor de fundo apropriada no círculo de magnitude.
        // Busque o fundo do TextView, que é um GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Obtenha a cor de fundo apropriada, baseada na magnitude do terremoto atual
        int magnitudeColor = getMagnitudeColor(atualTerremoto.getMagnitude());

        // Configure a cor no círculo de magnitude
        magnitudeCircle.setColor(magnitudeColor);

        //
        String originalLocation = atualTerremoto.getLocation();
        String proximidade;
        String local;
        //Separando a String de localização em "proximidade" + "local" ou adicionando "próximo à" caso não aja proximidade exata.
        if (originalLocation.contains(LOCATION_SEPARATOR)){
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            proximidade = parts[0] + " de ";
            local = parts[1];
        } else {
            proximidade = getContext().getString(R.string.perto);
            local = originalLocation;
        }
        TextView locationView = (TextView) listItemView.findViewById(R.id.location);
        locationView.setText(local);
        TextView proximidadeView = (TextView) listItemView.findViewById(R.id.proximidade);
        proximidadeView.setText(proximidade);


        //Na parte de data e hora converteremos o tempo Unix (milisegundos) em um formato padrão
        Locale localSystem = new Locale("pt", "BR");
        Date dateObject = new Date(atualTerremoto.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", localSystem);
        SimpleDateFormat timeFormat = new SimpleDateFormat(" h:mm a", localSystem);

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        String dataFormatada = dateFormat.format(dateObject);
        dateView.setText(dataFormatada);

        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        String tempoFormatado = timeFormat.format(dateObject);
        timeView.setText(tempoFormatado);

        return listItemView;
    }


    private String formatarMag(double mag){
        DecimalFormat magFormat = new DecimalFormat(("0.0"));
        String magnitude = magFormat.format(mag);
        return magnitude;
    }

    private int getMagnitudeColor(double mag){
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(mag);   //Está passando o valor double para inteiro, descartando a parte decimal(floor)

        switch (magnitudeFloor){
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }


        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }


}


