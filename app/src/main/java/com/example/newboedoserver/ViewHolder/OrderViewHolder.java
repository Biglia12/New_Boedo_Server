package com.example.newboedoserver.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newboedoserver.Interface.ItemClickListener;
import com.example.newboedoserver.R;


public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener  {

    public TextView txtOrderId,txtOrderEstados,txtOrderTelefono,txtOrderDireccion;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        txtOrderDireccion = itemView.findViewById(R.id.order_direccion);
        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderEstados = itemView.findViewById(R.id.order_estados);
        txtOrderTelefono = itemView.findViewById(R.id.order_telefono);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);


    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Seleccionar la accion");

        contextMenu.add(0,0,getAdapterPosition(),"Actualizar");
        contextMenu.add(0,0,getAdapterPosition(),"Actualizar");

    }
}
