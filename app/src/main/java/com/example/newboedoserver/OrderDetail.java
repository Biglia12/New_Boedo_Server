package com.example.newboedoserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.newboedoserver.Common.Common;
import com.example.newboedoserver.ViewHolder.OrderDetailAdapter;

public class OrderDetail extends AppCompatActivity {

    TextView order_id,order_phone,order_address,order_total,order_comment;
    String order_id_value="";
    RecyclerView IsFoods;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order_id=findViewById(R.id.order_id);
        order_phone=findViewById(R.id.order_telefono);
        order_address=findViewById(R.id.order_direccion);
        order_total=findViewById(R.id.order_total);
        order_comment=findViewById(R.id.order_comment);

        IsFoods = findViewById(R.id.IsFoods);
        IsFoods.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        IsFoods.setLayoutManager(layoutManager);

        if (getIntent()!=null)
            order_id_value=getIntent().getStringExtra("OrderId");

        order_id.setText(order_id_value);
        order_phone.setText(Common.currentRequest.getTelefono());
        order_total.setText(Common.currentRequest.getTotal());
        order_address.setText(Common.currentRequest.getDireccion());
        order_comment.setText(Common.currentRequest.getComment());

        OrderDetailAdapter adapter = new OrderDetailAdapter(Common.currentRequest.getComidas());
        adapter.notifyDataSetChanged();
        IsFoods.setAdapter(adapter);

    }
}
