package com.example.pm2e12006;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityCall extends AppCompatActivity {

    Button btnRegresar ;
    AlertDialog.Builder  builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        btnRegresar = findViewById(R.id.Llamar);
        builder = new AlertDialog.Builder(this);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert2(ActivityCall.this, "Desea Regresar a la Pantalla Anterior");
            }
        });
    }

    public void showAlert2 (Context context, String message){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Regresar a la pantalla Anterior?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), ActivityList.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.show();
    }
}