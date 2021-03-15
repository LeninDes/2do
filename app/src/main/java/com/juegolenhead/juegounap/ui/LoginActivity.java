package com.juegolenhead.juegounap.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.juegolenhead.juegounap.R;
import com.juegolenhead.juegounap.common.Constantes;
import com.juegolenhead.juegounap.models.Usuario;

public class LoginActivity extends AppCompatActivity {

    EditText etNick;
    Button btnStart;
    String nick;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instanciar la conexion a Firestore
        db = FirebaseFirestore.getInstance();

        etNick = findViewById(R.id.editTextNick);
        btnStart = findViewById(R.id.buttonStart);

        // Evento Start sera lanzado cuando de haga clic
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nick = etNick.getText().toString();

                if( nick.isEmpty() ) {
                    etNick.setError("El nombre de usuario es obligatorio");
                } else if(  nick.length() < 3) {
                    etNick.setError("Debe de tener al menos tres carateres");
                }
                else {
                    addNickAndStart();
                }

            }
        });

    }

    private void addNickAndStart() {
        db.collection("usuarios").whereEqualTo("nick", nick)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if( queryDocumentSnapshots.size() > 0 ) {
                            etNick.setError("El nick no esta disponible");
                        } else {
                            addNickToFirestore();
                        }
                    }
                });


    }

    private void addNickToFirestore() {

        Usuario nuevoUsuario = new Usuario(nick, 0);

        db.collection( "usuarios" )
                .add(nuevoUsuario)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        etNick.setText("");
                        Intent i = new Intent( LoginActivity.this, GameActivity.class );
                        i.putExtra(Constantes.EXTRA_NICK, nick);
                        i.putExtra(Constantes.EXTRA_ID,documentReference.getId() );

                        startActivity( i );
                    }
                });


    }




}