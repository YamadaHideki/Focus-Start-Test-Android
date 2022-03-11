package com.example.focusstarttest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class ValutesAdapter extends RecyclerView.Adapter<ValutesAdapter.ValuteViewHolder> {

    private static int viewHolderCount;
    private int numberItems;
    private Map<Integer, Map<String, String>> map;

    public ValutesAdapter(Map<Integer, Map<String, String>> map, int numberItems) {
        this.numberItems = numberItems;
        viewHolderCount = 0;
        this.map = map;
        Log.i("MAP", String.valueOf(map.size()));
    }

    @NonNull
    @Override
    public ValuteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.valute_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);

        ValuteViewHolder viewHolder = new ValuteViewHolder(view);
        viewHolder.valuteValue.setText("ViewHolder index: " + viewHolderCount);
        viewHolderCount++;

        return viewHolder;
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull ValuteViewHolder holder, int position) {
        //String tag = Objects.requireNonNull(map.get(position)).get(0);
        Map<String, String> dbMap = map.get(position);
        if (dbMap != null) {
            Log.i("MAP", dbMap.toString());
            holder.bind(dbMap.get(NotesCbr.NotesJson.VALUTE_NAME), dbMap.get(NotesCbr.NotesJson.VALUTE_NOMINAL) + " шт.",
                    String.format("%.2f р.", Double.parseDouble(dbMap.get(NotesCbr.NotesJson.VALUTE_VALUE))));
        }

    }

    @Override
    public int getItemCount() {
        //return numberItems;
        return map.size();
    }

    class ValuteViewHolder extends RecyclerView.ViewHolder {

        private TextView valuteTag;
        private TextView valuteNominal;
        private TextView valuteValue;


        public ValuteViewHolder(@NonNull View itemView) {
            super(itemView);
            valuteTag = itemView.findViewById(R.id.tv_valute_tag);
            valuteNominal = itemView.findViewById(R.id.tv_valute_nominal);
            valuteValue = itemView.findViewById(R.id.tv_valute_value);
        }

        public void bind(String name, String nominal, String value) {
            valuteTag.setText(name);
            valuteNominal.setText(nominal);
            valuteValue.setText(value);
        }
    }
}
