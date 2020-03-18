package com.example.newboedoserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.newboedoserver.Common.Common;
import com.example.newboedoserver.Interface.ItemClickListener;
import com.example.newboedoserver.Model.MyResponse;
import com.example.newboedoserver.Model.Notification;
import com.example.newboedoserver.Model.Request;
import com.example.newboedoserver.Model.Sender;
import com.example.newboedoserver.Model.Token;
import com.example.newboedoserver.Remote.APIService;
import com.example.newboedoserver.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    MaterialSpinner spinner;

    APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        mService = Common.getFCMSerivice();//

        //Firebase
        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");



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
                holder.txtOrderDate.setText(Common.getDate(Long.parseLong(adapter.getRef(position).getKey())));

                holder.btnEdit.setOnClickListener((View v) -> { //codigo lambda (se actualizo por el codigo anterior)

                        showUpdateDialog(adapter.getRef(position).getKey(),
                                adapter.getItem(position));
                });

                holder.btnRemove.setOnClickListener((View v) -> {

                        deleteOrder(adapter.getRef(position).getKey());
                });

                holder.btnDetail.setOnClickListener((View v)-> {

                        Intent orderDetail  =new Intent(OrderStatus.this,OrderDetail.class);
                        Common.currentRequest=model;
                        orderDetail.putExtra("OrderId",adapter.getRef(position).getKey());
                        startActivity(orderDetail);
                });
                holder.btnDirection.setOnClickListener((View v) -> {
                          Intent trackingOrder=new Intent(OrderStatus.this,TrackingOrder.class);
                          Common.currentRequest=model;
                          startActivity(trackingOrder);
                });


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

  /*  @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
       if (item.getTitle().equals(Common.UPDATE))
           showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
       else if (item.getTitle().equals(Common.DELETE))
           deleteOrder(adapter.getRef(item.getOrder()).getKey());
        return super.onContextItemSelected(item);

    }*/

    private void deleteOrder(String key) {
        requests.child(key).removeValue();
        adapter.notifyDataSetChanged();


    }

    private void showUpdateDialog(String key, final Request item) {
        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(OrderStatus.this);
        alertDialog.setTitle("Actulizar Orden");
        alertDialog.setMessage("Por favor elegir Estado");

        LayoutInflater inflater=this.getLayoutInflater();
        final View view=inflater.inflate(R.layout.update_order_layout,null);

        spinner=view.findViewById(R.id.statusSpinner);
        spinner.setItems("En lugar","Enviando","Enviado");

        alertDialog.setView(view);

        final String localKey=key;
        alertDialog.setPositiveButton("SI", (dialog, which) -> {
            dialog.dismiss();
            item.setEstados(String.valueOf(spinner.getSelectedIndex()));
            requests.child(localKey).setValue(item);

            sendOrderStatusToUser(localKey,item);
            adapter.notifyDataSetChanged();

        });

        alertDialog.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

        alertDialog.show();

    }

    private void sendOrderStatusToUser(final String key,Request item) {
        DatabaseReference tokens=database.getReference("Tokens");

        tokens.child(item.getTelefono())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Token token = dataSnapshot.getValue(Token.class);

                            Notification notification = new Notification("New Boedo","Su orden"+key+"fue actualizada");

                            Sender content = new Sender(token.getToken(),notification);

                            mService.sendNotification(content)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                            if (response.code() == 200) {
                                                if (response.body().success == 1) {

                                                    Toast.makeText(OrderStatus.this, "Orden fue actulizada!", Toast.LENGTH_SHORT).show();
                                                } else {

                                                    Log.e("notificacion","enRespuesta"+key+"fallo");
                                                    Toast.makeText(OrderStatus.this, "Orden fue actulizada pero fallo !", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Log.e("ERROR", "onFailure: "+t.getMessage() );

                                        }
                                    });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
