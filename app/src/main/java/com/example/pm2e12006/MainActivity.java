package com.example.pm2e12006;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm2e12006.configuracion.SQLiteConexion;
import com.example.pm2e12006.transacciones.Contactos;
import com.example.pm2e12006.transacciones.Transacciones;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    EditText nombre;
    Spinner pais;
    EditText telefono,nota;
    Blob imagen;

    static final int REQUEST_IMAGE = 101;
    static final int PETICION_ACCESS_CAM = 201;

    Button btnagregar;
    Button btntakefoto;
    Button btnMostrar;


    ImageView imageView;

    String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre = (EditText) findViewById(R.id.txtnombre);
        String texto = nombre.getText().toString();
        if(texto.isEmpty()){
            nombre.setError("Escriba el Nombre, Este Campo es Obligatorio");
            nombre.requestFocus();
        }else{

        }

        pais = (Spinner) findViewById(R.id.pais);

        telefono = (EditText)  findViewById(R.id.txttelefono);
        String texto1 = telefono.getText().toString();
        if(texto.isEmpty()){
            telefono.setError("Digite el Numero Telefonico, Este Campo es Obligatorio");
            telefono.requestFocus();
        }else{

        }

        nota = (EditText)  findViewById(R.id.txtnota);
        String texto2 = nota.getText().toString();
        if(texto.isEmpty()){
            nota.setError("Escriba una Nota, Este Campo es Opcional");
            nota.requestFocus();
        }else{

        }

        imageView = (ImageView) findViewById(R.id.imageView);

        btntakefoto = (Button) findViewById(R.id.btnfoto);
        btnMostrar = (Button) findViewById(R.id.btnMostrar) ;
        btnagregar = (Button) findViewById(R.id.btnEnviar);



        nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert(MainActivity.this, "Debe Escribir el Nombre");
            }
        });

        telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert(MainActivity.this, "Debe Escribir el Telefono");
            }
        });

        nota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert(MainActivity.this, "Debe Escribir la Nota");
            }
        });



        btntakefoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgregarContacto();

            }
        });

        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), ActivityList.class);
                startActivity(intent);

            }
        });
    }



    private void AgregarContacto(){
        try{
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues valor = new ContentValues();

            Contactos contacto = new Contactos(nombre.getText().toString(), pais.getSelectedItem().toString(), telefono.getText().toString(), nota.getText().toString(), nota.getText().toString());

            valor.put("nombre", contacto.getNombre());
            valor.put("telefono",contacto.getTelefono());
            valor.put("nota", contacto.getNota());
            valor.put("pais", contacto.getPais());
            valor.put("imagen", contacto.getImagen());



            Long Resultado = db.insert(Transacciones.tablacontactos, "id", valor);
            Toast.makeText(this, Resultado.toString(), Toast.LENGTH_SHORT).show();

            ClearScreen();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "Error No se pudo insertar el dato", Toast.LENGTH_LONG).show();
        }
    }

    private void permisos()
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},PETICION_ACCESS_CAM);
        }
        else
        {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PETICION_ACCESS_CAM)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                dispatchTakePictureIntent();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Necesita Autorizacion de la Camara", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void TomarFoto()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager())!= null)
        {
            startActivityForResult(intent, REQUEST_IMAGE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE && resultCode == RESULT_OK)
        {
            try{
                File foto = new File(currentPhotoPath);
                imageView.setImageURI(Uri.fromFile(foto));
            }
            catch (Exception ex)
            {
                ex.toString();
            }
        }

    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.toString();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.pm2e146.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE);
            }
        }
    }

    private void ClearScreen(){
        nombre.setText(Transacciones.Empty);
        telefono.setText(Transacciones.Empty);
        nota.setText(Transacciones.Empty);
    }

    public void showAlert(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}