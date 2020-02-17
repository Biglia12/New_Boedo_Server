package com.example.newboedoserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newboedoserver.Common.Common;
import com.example.newboedoserver.Interface.ItemClickListener;
import com.example.newboedoserver.Model.Categoria;
import com.example.newboedoserver.Model.Food;
import com.example.newboedoserver.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    RelativeLayout rootLayout;

    FloatingActionButton fab;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference foodList;
    FirebaseStorage storage;
    StorageReference storageReference;

    String categoriaId = "";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    //agregar nueva comida
    MaterialEditText edtName,edtDescription,edtPrice,edtDiscount;
    Button btnSelect,btnUpload;

    Food newFood;

    Uri saveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Firebase
        db = FirebaseDatabase.getInstance();
        foodList = db.getReference("Comidas");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //init
        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rootLayout=findViewById(R.id.rootLayout);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showAddFoodDialog();
                
            }
        });
        if (getIntent() != null)
            categoriaId = getIntent().getStringExtra("CategoriaId");
        if (!categoriaId.isEmpty())
            loadListFood(categoriaId);

    }

    private void showAddFoodDialog() {
    //copiar codigo de Home Activity
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("Agregar nueva Comida");
        alertDialog.setMessage("por favor complete la información completa");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_food_layout,null);

        edtName=add_menu_layout.findViewById(R.id.edtName);
        edtDescription=add_menu_layout.findViewById(R.id.edtDescription);
        edtDiscount=add_menu_layout.findViewById(R.id.edtDiscount);
        edtPrice=add_menu_layout.findViewById(R.id.edtPrice);

        btnSelect=add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload=add_menu_layout.findViewById(R.id.btnUpload);

        //Evento para el boton
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();//copiar de Home Activity
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();//copiar de Home Activity
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //set Button
        alertDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();


                {
                    foodList.push().setValue(newFood);
                    Snackbar.make(rootLayout,"Nueva categoria"+newFood.getNombre()+"fue agregada",Snackbar.LENGTH_SHORT).show();
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
        if (saveUri!=null) {
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
                            Toast.makeText(FoodList.this,"Subido!!!",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //establecer valor para nueva categoria si ka imgane esta subida y nsosotros podemos obtener el link
                                    newFood=new Food();
                                    newFood.setNombre(edtName.getText().toString());
                                    newFood.setDescripcion(edtDescription.getText().toString());
                                    newFood.setPrecio(edtPrice.getText().toString());
                                    newFood.setDescuento(edtDiscount.getText().toString());
                                    newFood.setMenuId(categoriaId);
                                    newFood.setImagen(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("Imagen/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleccionar Imagen"), Common.PICK_IMAGE_REQUEST);
    }

    private void loadListFood(String categoriaId) {

        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(foodList.orderByChild("menuId")
                        .equalTo(categoriaId), Food.class).build();
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.food_name.setText(model.getNombre());
                Picasso.get().load(model.getImagen()).into(holder.food_image);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {


                    }
                });

            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
                return new FoodViewHolder(view);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override //ctrl+o Onactivityresult
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data !=null && data.getData()!=null)
        {
            saveUri=data.getData();
            btnSelect.setText("Imagen Seleccionada!");

        }
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getTitle().equals(Common.UPDATE)){
            showUpdateFoodDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE)){
            deleteFood(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteFood(String key) {
        foodList.child(key).removeValue();
    }

    private void showUpdateFoodDialog(final String key, final Food item) {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("Editar Comida");
        alertDialog.setMessage("por favor complete la información completa");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_food_layout,null);

        edtName=add_menu_layout.findViewById(R.id.edtName);
        edtDescription=add_menu_layout.findViewById(R.id.edtDescription);
        edtDiscount=add_menu_layout.findViewById(R.id.edtDiscount);
        edtPrice=add_menu_layout.findViewById(R.id.edtPrice);

        edtName.setText(item.getNombre());
        edtDiscount.setText(item.getDescuento());
        edtDescription.setText(item.getDescripcion());
        edtPrice.setText(item.getPrecio());

        btnSelect=add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload=add_menu_layout.findViewById(R.id.btnUpload);

        //Evento para el boton
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();//copiar de Home Activity
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);//copiar de Home Activity
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //set Button
        alertDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();

                //aqui, creamos nueva categoria
                if (newFood!=null)
                {
                    //actualizar info
                    item.setNombre(edtName.getText().toString());
                    item.setPrecio(edtPrice.getText().toString());
                    item.setDescripcion(edtDescription.getText().toString());
                    item.setDescuento(edtDiscount.getText().toString());

                    foodList.child(key).setValue(item);
                    Snackbar.make(rootLayout,"Comida"+newFood.getNombre()+"fue editada",Snackbar.LENGTH_SHORT).show();
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

    private void changeImage(final Food item) {
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
                            Toast.makeText(FoodList.this,"Subido!!!",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(FoodList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
