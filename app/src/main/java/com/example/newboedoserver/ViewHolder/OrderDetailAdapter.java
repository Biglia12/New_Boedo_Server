package com.example.newboedoserver.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newboedoserver.Model.Order;
import com.example.newboedoserver.R;

import java.util.List;

class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView name,quantity,price,discount;

    public MyViewHolder (View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.product_name);
        quantity=itemView.findViewById(R.id.product_quantity);
        price=itemView.findViewById(R.id.product_price);
        discount=itemView.findViewById(R.id.product_discount);
    }
}

public class OrderDetailAdapter extends RecyclerView.Adapter<MyViewHolder> {

    List<Order> myorders;

    public OrderDetailAdapter(List<Order> myorders) {
        this.myorders = myorders;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_layout,parent,false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = myorders.get(position);
        holder.name.setText(String.format("Nombre : %s",order.getProductoNombre()));
        holder.quantity.setText(String.format("Cantidad : %s",order.getCantidad()));
        holder.price.setText(String.format("Precio : %s",order.getPrecio()));
        holder.discount.setText(String.format("Descuento : %s",order.getDescuento()));


    }

    @Override
    public int getItemCount() {
        return myorders.size();
    }
}
