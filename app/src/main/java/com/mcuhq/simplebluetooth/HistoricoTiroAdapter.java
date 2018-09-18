package com.mcuhq.simplebluetooth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by eduardo.dall on 13/08/2018.
 */

public class HistoricoTiroAdapter extends ArrayAdapter<TiroCorredor> {
    private List<TiroCorredor> tiros;
    SimpleDateFormat formatter;

    public HistoricoTiroAdapter(@NonNull Context context, int resource, @NonNull List<TiroCorredor> objects) {
        super(context, resource, objects);
        tiros = objects;

        formatter = new SimpleDateFormat("mm:ss");
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TiroCorredor tiro = getItem(i);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_historico_tiro, viewGroup, false);
        }
        // Lookup view for data population
        TextView textTempoDecorridoPlataforma = view.findViewById(R.id.textTempoDecorridoPlataforma);
        TextView textPrimeiraCorrida = view.findViewById(R.id.textPrimeiraCorrida);
        TextView textSegundaCorrida = view.findViewById(R.id.textSegundaCorrida);
        TextView textNomeCorredor = view.findViewById(R.id.textNomeCorredor);
        // Populate the data into the template view using the data object
        textPrimeiraCorrida.setText(tiro.getPrimeiraCorrida() == null ? "00:00" : formatter.format(tiro.getPrimeiraCorrida()));
        textTempoDecorridoPlataforma.setText(tiro.getTempoDecorridoPlataforma() == null ? "00:00" : formatter.format(tiro.getTempoDecorridoPlataforma()));
        textSegundaCorrida.setText(tiro.getSegundaCorrida() == null ? "00:00" : formatter.format(tiro.getSegundaCorrida()));
        textNomeCorredor.setText(tiro.getNome());
        // Return the completed view to render on screen
        return view;

    }
}
