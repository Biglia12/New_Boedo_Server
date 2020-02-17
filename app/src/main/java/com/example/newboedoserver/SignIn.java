package com.example.newboedoserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newboedoserver.Common.Common;
import com.example.newboedoserver.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    EditText edtPhone,edtPassword;
    Button btnSignIn;

    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);

        //iniciar firebase
        db=FirebaseDatabase.getInstance();
        users=db.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser(edtPhone.getText().toString(),edtPassword.getText().toString());
            }
        });

    }

    private void signInUser(final String phone, String password) {
        final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
        mDialog.setMessage("Por favor espere...");
        mDialog.show();

        final String localPhone = phone;
        final String localPassword = password;
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(localPhone).exists())
                {
                mDialog.dismiss();
                User user=dataSnapshot.child(localPhone).getValue(User.class);
                user.setPhone(localPhone);
                if (Boolean.parseBoolean(user.getIsStaff()))// si isstaff== true
                {
                if (user.getPassword().equals(localPassword))
                {
                    Intent login=new Intent(SignIn.this,Home.class);
                    Common.currentUser=user;
                    startActivity(login);
                    finish();
                }
                   else
                    Toast.makeText(SignIn.this,"Contraseña Incorrecta",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(SignIn.this,"Por favor inciar Sesion con una cuenta de la empresa",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(SignIn.this,"El Usuario no existe en la base de datos",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
