package com.example.newboedoserver.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newboedoserver.Interface.ItemClickListener;
import com.example.newboedoserver.R;


public class OrderViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener

        ,View.OnCreateContextMenuListener*/ {

    public TextView txtOrderId,txtOrderEstados,txtOrderTelefono,txtOrderDireccion,txtEntrecalles,txtpisoydepartamento,txtlocalidad,txtOrderDate;

    public Button btnEdit,btnRemove,btnDetail,btnDirection;

   // private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        txtOrderDireccion = itemView.findViewById(R.id.order_direccion);
        txtEntrecalles=itemView.findViewById(R.id.order_entrecalles);
        txtlocalidad=itemView.findViewById(R.id.order_localidad);
        txtpisoydepartamento=itemView.findViewById(R.id.order_pisoydepartamento);
        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderEstados = itemView.findViewById(R.id.order_estados);
        txtOrderTelefono = itemView.findViewById(R.id.order_telefono);
        txtOrderDate = itemView.findViewById(R.id.order_date);


        btnEdit= itemView.findViewById(R.id.btnedit);
        btnDetail= itemView.findViewById(R.id.btnDetail);
        btnDirection= itemView.findViewById(R.id.btnDirection);
        btnRemove= itemView.findViewById(R.id.btndelete);





        /*itemView.setOnClickListener(this);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);*/


    }

   /* public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }*/

   /* @Override
    public void onClick(View view) { itemClickListener.onClick(view,getAdapterPosition(),false);
    }*/

   /* @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Seleccionar la accion");

        contextMenu.add(0,0,getAdapterPosition(),"Actualizar");
        contextMenu.add(0,0,getAdapterPosition(),"Eliminar");

    }*/

}
