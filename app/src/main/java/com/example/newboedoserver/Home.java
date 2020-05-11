package com.example.newboedoserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.newboedoserver.Common.Common;
import com.example.newboedoserver.Interface.ItemClickListener;
import com.example.newboedoserver.Model.Categoria;
import com.example.newboedoserver.Model.Token;
import com.example.newboedoserver.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.UUID;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

TextView txtFullName;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference categories;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseRecyclerAdapter <Categoria, MenuViewHolder>adapter;


    //View
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    //Agregar nuevo menu layout
    MaterialEditText edtName;
    Button btnUpload,btnSelect;

    Categoria newCategoria;

    Uri saveUri;

    DrawerLayout drawer;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //iniciar firebase
        database = FirebaseDatabase.getInstance();
        categories = database.getReference("Categoria");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showDialog());
        //abrir el hamburguer
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());

       /* if (txtFullName!=null )txtFullName.setText(Common.currentUser.getName());{
            else if (txtFullName== null)
                return null;*/


        //Init view
        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();
        updateToken(FirebaseInstanceId.getInstance().getToken());

        //updateToken(FirebaseInstanceId.getInstance().getInstanceId());

    }


   /* private void updateToken(Task<InstanceIdResult> instanceId) {
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token data = new Token(instanceId,true);
        tokens.child(Common.currentUser.getPhone()).setValue(data);
    }*/

   private void updateToken(String token) {
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token data = new Token(token,true);
        tokens.child(Common.currentUser.getPhone()).setValue(data);

    }

    private void showDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Agregar nueva categoria");
        alertDialog.setMessage("por favor complete la información completa");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout,null);

        edtName=add_menu_layout.findViewById(R.id.edtName);
        btnSelect=add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload=add_menu_layout.findViewById(R.id.btnUpload);

        //Evento para el boton
        btnSelect.setOnClickListener(v -> {
            chooseImage();//seleccion de imagen de de galeria y grabar uri de esta imagen
        });

        btnUpload.setOnClickListener(v -> uploadImage());

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //set Button
        alertDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();

                //aqui, creamos nueva categoria
                if (newCategoria!=null)
                {
                    categories.push().setValue(newCategoria);
                    Snackbar.make(drawer,"Nueva categoria"+newCategoria.getNombre()+"fue agregada",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void uploadImage() {
        if (saveUri!=null)
        {
            final ProgressDialog mDialog=new ProgressDialog(this);
            mDialog.setMessage("Subiendo...");
            mDialog.show();

            String imageName= UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("imagenes/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          mDialog.dismiss();
                            Toast.makeText(Home.this,"Subido!!!",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //establecer valor para nueva categoria si ka imgane esta subida y nsosotros podemos obtener el link
                                    newCategoria=new Categoria(edtName.getText().toString(),uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(e -> {
                        mDialog.dismiss();
                        Toast.makeText(Home.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                     .addOnProgressListener(taskSnapshot -> {
                         double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                         mDialog.setMessage("Subido"+progress +"%");
                     });
        }
    }

    //apretar control + O
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data !=null && data.getData()!=null)
        {
            saveUri=data.getData();
            btnSelect.setText("Imagen Seleccionada!");
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("Imagen/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleccionar Imagen"),Common.PICK_IMAGE_REQUEST);
    }

    //se cargara lo de firebase
    private void loadMenu() {
        FirebaseRecyclerOptions<Categoria>options=
                new FirebaseRecyclerOptions.Builder<Categoria>()
                        .setQuery(categories, Categoria.class)
                        .build();
        adapter=new FirebaseRecyclerAdapter<Categoria, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int i, @NonNull Categoria model) {
                holder.txtMenuNombre.setText(model.getNombre());
                Picasso.get().load(model.getImagen()).into(holder.imageView, new Callback() {

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //enviar categoria id y empezar nueva actividad
                        Intent foodList=new Intent(Home.this,FoodList.class);
                        foodList.putExtra("CategoriaId",adapter.getRef(position).getKey());
                        startActivity(foodList);

                    }
                });
            }


            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item, viewGroup, false);
                return new MenuViewHolder(view);
            }
        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recycler_menu.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recycler_menu.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();



        if (id == R.id.nav_menu) {


        } else if (id == R.id.nav_cart) {


        } else if (id == R.id.nav_orders) {

            Intent orders=new Intent(Home.this,OrderStatus.class);
            startActivity(orders);

        } else if (id == R.id.nav_sign_out) {



        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    //Actualizar/Eliminar
    //presionar ctrl+O

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

       if (item.getTitle().equals(Common.UPDATE))
       {
       showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
       }

       else  if (item.getTitle().equals(Common.DELETE))
       {
           deleteCategory(adapter.getRef(item.getOrder()).getKey());
       }

        return super.onContextItemSelected(item);

    }

    private void deleteCategory(String key) {

        DatabaseReference foods = database.getReference("Comidas");
        Query foodInCategory = foods.orderByChild("menuId").equalTo(key);
        foodInCategory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    postSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        categories.child(key).removeValue();
        Toast.makeText(this,"Item Eliminado!!!",Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialog(final String key, final Categoria item) {
        //solo copiar codigo de showdialog y modificar
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Actualizar categoria");
        alertDialog.setMessage("por favor complete la información completa");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout,null);

        edtName=add_menu_layout.findViewById(R.id.edtName);
        btnSelect=add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload=add_menu_layout.findViewById(R.id.btnUpload);

        edtName.setText(item.getNombre());

        //Evento para el boton
        btnSelect.setOnClickListener(v -> {
            chooseImage();//seleccion de imagen de de galeria y grabar uri de esta imagen
        });

        btnUpload.setOnClickListener(v -> changeImage(item));

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //set Button
        alertDialog.setPositiveButton("SI", (dialog, i) -> {
            dialog.dismiss();

           //Actulizar informacion
            item.setNombre(edtName.getText().toString());
            categories.child(key).setValue(item);
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }
    private void changeImage(final Categoria item) {
        if (saveUri!=null)
        {
            final ProgressDialog mDialog=new ProgressDialog(this);
            mDialog.setMessage("Subiendo...");
            mDialog.show();

            String imageName= UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("imagenes/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(Home.this,"Subido!!!",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //establecer valor para nueva categoria si ka imgane esta subida y nsosotros podemos obtener el link
                                    item.setImagen(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(Home.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Subido"+progress +"%");
                        }
                    });
        }
    }
}
