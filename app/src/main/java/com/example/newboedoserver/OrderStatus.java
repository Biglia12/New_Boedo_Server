package com.example.newboedoserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.newboedoserver.Common.Common;
import com.example.newboedoserver.Interface.ItemClickListener;
import com.example.newboedoserver.Model.Request;
import com.example.newboedoserver.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class OrderStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase db;
    DatabaseReference requests;

    MaterialSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Firebase
        db=FirebaseDatabase.getInstance();
        requests=db.getReference("Requests");

        recyclerView=findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();//cargar toda las ordenes

    }

    private void loadOrders() {
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(requests,Request.class).build();

        adapter=new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Request model) {
                holder.txtOrderId.setText(adapter.getRef(position).getKey());
                holder.txtOrderEstados.setText(Common.convertCodeToStatus(model.getEstados()));
                holder.txtOrderDireccion.setText(model.getDireccion());
                holder.txtOrderTelefono.setText(model.getTelefono());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
                Intent trackingOrder=new Intent(OrderStatus.this,TrackingOrder.class);
                Common.currentRequest=model;
                startActivity(trackingOrder);


            }



            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent,false);
                return new OrderViewHolder(view);
            }
        };
        //Adaptador
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
       if (item.getTitle().equals(Common.UPDATE))
           showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
       else if (item.getTitle().equals(Common.DELETE))
           deleteOrder(adapter.getRef(item.getOrder()).getKey());
        return super.onContextItemSelected(item);

    }

    private void deleteOrder(String key) {
        requests.child(key).removeValue();


    }

    private void showUpdateDialog(String key, final Request item) {
        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(OrderStatus.this);
        alertDialog.setTitle("Actulizar Orden");
        alertDialog.setMessage("Por favor elegir Estado");

        LayoutInflater inflater=this.getLayoutInflater();
        final View view=inflater.inflate(R.layout.update_order_layout,null);

        spinner=view.findViewById(R.id.statusSpinner);
        spinner.setItems("En lugar","En camino","Enviado");

        alertDialog.setView(view);

        final String localKey=key;
        alertDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setEstados(String.valueOf(spinner.getSelectedIndex()));

                requests.child(localKey).setValue(item);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }
}
