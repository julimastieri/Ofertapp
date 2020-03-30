package com.example.mariapiagoicoechea.ofertas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.OfertaViewHolder> implements View.OnClickListener{


    private Context mCtx;
    private List<Oferta> ofertList;
    private View.OnClickListener listener;


    //Constructor
    public Adapter(Context mCtx, List<Oferta> ofertList ) {
        this.mCtx = mCtx;
        this.ofertList = ofertList;
    }



    @Override
    public OfertaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout .buscar_list_layout,null);
        view.setOnClickListener(this);
        return new OfertaViewHolder(view);
    }



    // Actualiza los text view con los datos de la posicion actual.
    @Override
    public void onBindViewHolder(OfertaViewHolder holder, int position) {
        Oferta oferta = ofertList.get(position);
        holder.textViewTipo.setText(oferta.getTipo());
        holder.textViewPrecio.setText("$"+String.valueOf(oferta.getPrecio())); //Convierto de double a String
    }



    @Override
    public int getItemCount() {
        return ofertList.size();
    }

    public void setOnClickListener (View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }


    public static class OfertaViewHolder extends RecyclerView.ViewHolder {

        //Declaro los TextView de la interfaz
        TextView textViewTipo, textViewPrecio;

        public OfertaViewHolder (View itemview){
            super(itemview);

            //Enlazo los controles java con los widgets de XML
            textViewTipo = itemview.findViewById(R.id.listar_txt_tipo);
            textViewPrecio = itemview.findViewById(R.id.listar_txt_precio);
        }
    }
}
