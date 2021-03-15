 package com.juegolenhead.juegounap.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.juegolenhead.juegounap.R;
import com.juegolenhead.juegounap.common.Constantes;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    Random  ivPoke1 = new Random();
    Integer[] imagenesID =
            {R.drawable.poke_01, R.drawable.poke_02, R.drawable.poke_03, R.drawable.poke_04, R.drawable.poke_05, R.drawable.poke_07 };

    TextView tvCounterPoke, tvTimer, tvNick;
    ImageView ivPoke;
    int counter = 0;
    int anchoPantalla;
    int altoPantalla;
    Random aleatorio;
    boolean gameOver = false;
    String id, nick;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        db = FirebaseFirestore.getInstance();

        initViewComponents();
        eventos();
        initPantalla();
        movePoke();
        initCuentaAtras();
    }

    private void initCuentaAtras() {
        new CountDownTimer( 30000, 1000 ) {

            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished / 1000;
                tvTimer.setText(segundosRestantes + "s");
            }

            public void onFinish() {
                tvTimer.setText("0s");
                gameOver = true;
                mostrarDialogoGameOverr();
                saveResultFirestore();
            }

        }.start();
    }

    private void saveResultFirestore() {

        db.collection("usuarios")
                .document(id)
                .update(
                        "pokemones", counter
                );


    }

    private void mostrarDialogoGameOverr() {

        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Atrapaste " + counter + " Pokemones")
                .setTitle("Fin del Juego");
        builder.setCancelable(false);

        builder.setPositiveButton("Reiniciar el Juego", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                counter = 0;
                tvCounterPoke.setText("0");
                gameOver= false;
                initCuentaAtras();
                movePoke();
            }
        });
        builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
                finish();
            }
        });

        // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
            AlertDialog dialog = builder.create();
        // 4 MOstrat el dialogo
            dialog.show();
    }

    private void initPantalla() {

        // 1. Obtener el tamaño de la pantalla del dispositivo
        // en el que estamos ejecutando la app

        Display display = getWindowManager().getDefaultDisplay();
        Point size  = new Point();
        display.getSize(size);

        anchoPantalla = size.x;
        altoPantalla = size.y;

        // 2. inicializamos el objeto para generar numeros aleatorios
        aleatorio = new Random();

    }

    private void initViewComponents() {
        tvCounterPoke = findViewById( R.id.textViewCounter);
        tvTimer = findViewById( R.id.textViewTimer );
        tvNick = findViewById( R.id.textViewNick);
        ivPoke = findViewById( R.id.imageViewPikachu );
        Integer q = imagenesID[ivPoke1.nextInt(imagenesID.length)];
        ivPoke = (ImageView) findViewById(R.id.imageViewPikachu);

        // Tengo el nick del usuario y el seteo
        Bundle extras  = getIntent().getExtras();
        nick = extras.getString(Constantes.EXTRA_NICK);
        id  = extras.getString(Constantes.EXTRA_ID);
        tvNick.setText(nick);

    }

    private void eventos() {
        ivPoke.setOnClickListener(v -> {
            if(!gameOver){

                counter++;
                tvCounterPoke.setText(String.valueOf(counter));

                int resources = imagenesID[ivPoke1.nextInt(imagenesID.length)];
                ivPoke.setImageResource( resources );

                ivPoke.setImageResource(R.drawable.pobeball);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivPoke.setImageResource( resources );
                        movePoke();
                    }
                }, 500);

            }

        });
    }

    private void movePoke() {

        int min = 0;
        int maximoX = anchoPantalla - ivPoke.getWidth();
        int maximoY = altoPantalla - ivPoke.getHeight();

        // Generamos 2 numeros aletorios, uno para la coordenada
        // x y otro para la coordenada Y.

        int randomX  = aleatorio.nextInt(((maximoX - min) + 1) + min);
        int randomY  = aleatorio.nextInt(((maximoY - min) + 1) + min);

        // Utizamos los numeros aleatorios para mover el pikachu en esa pocicion

        ivPoke.setX(randomX);
        ivPoke.setY(randomY);

    }








}