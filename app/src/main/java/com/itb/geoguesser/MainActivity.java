package com.itb.geoguesser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView contadorAcertadas, progressText, pregunta, titulo, contadorHints;
    Button hint, respuesta1, respuesta2, respuesta3, respuesta4;
    ProgressBar progressBar;
    ImageView preguntaImagen, estrella;
    int contadorQuestionModel = 0, hintsRemaining = 3, questionsAnswered = 0, contadorEstrella = 0;
    double puntuacion = 0;
    boolean yaHasRespondido = false, yaHasUsadoHint = false;
    MyCountDownTimer myCountDownTimer;

    static final QuestionModel[] questionModels = new QuestionModel[] {
            new QuestionModel(0, R.drawable.canguro, R.string.question_animal, new int[]{R.string.australia, R.string.japon, R.string.china, R.string.russia}, "The place with the deadliest animals"),
            new QuestionModel(2, R.drawable.panda_rojo, R.string.question_animal, new int[]{R.string.espana, R.string.francia, R.string.china, R.string.UK}, "The biggest country in the world"),
            new QuestionModel(3, R.drawable.lince, R.string.question_animal, new int[]{R.string.UK, R.string.australia, R.string.russia, R.string.espana}, "Flamenquito"),
            new QuestionModel(2, R.drawable.aguila, R.string.question_animal, new int[]{R.string.japon, R.string.china, R.string.EEUU, R.string.francia}, "Guns are allowed here"),
            new QuestionModel(1, R.drawable.demonio_tasmania, R.string.question_animal, new int[]{R.string.espana, R.string.australia, R.string.UK, R.string.espana}, "The spiders are so poisonous here"),
            new QuestionModel(0, R.drawable.bandera_russia, R.string.question_flag, new int[]{R.string.russia, R.string.china, R.string.EEUU, R.string.UK}, "I like Vodka"),
            new QuestionModel(0, R.drawable.bandera_japon, R.string.question_flag, new int[]{R.string.japon, R.string.francia, R.string.espana, R.string.russia}, "Otakulandia"),
            new QuestionModel(3, R.drawable.bandera_uk, R.string.question_flag, new int[]{R.string.australia, R.string.china, R.string.espana, R.string.UK}, "I like tea"),
            new QuestionModel(1, R.drawable.bandera_espana, R.string.question_flag, new int[]{R.string.UK, R.string.espana, R.string.russia, R.string.francia}, "We like paella and \"Bocadillos de calamares\""),
            new QuestionModel(2, R.drawable.bandera_francia, R.string.question_flag, new int[]{R.string.russia, R.string.china, R.string.francia, R.string.japon}, "Oui oui bagette")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Mezcla el quiestionModels
        List<QuestionModel> qmList = Arrays.asList(questionModels);
        Collections.shuffle(qmList);
        qmList.toArray(questionModels);

        //Se muestran en pantalla y no varian
        titulo = findViewById(R.id.titulo);
        estrella = findViewById(R.id.imagen_estrella);

        //Se muestran en pantalla y se cambian cada vez que cambia la pregunta
        preguntaImagen = findViewById(R.id.pregunta_imagen);
        pregunta = findViewById(R.id.pregunta_texto);
        respuesta1 = findViewById(R.id.answer1);
        respuesta2 = findViewById(R.id.answer2);
        respuesta3 = findViewById(R.id.answer3);
        respuesta4 = findViewById(R.id.answer4);

        //Se muestran en pantalla y se cambian cada vez que se cumple x condición.
        contadorHints = findViewById(R.id.contador_hints);
        contadorAcertadas = findViewById(R.id.contador_estrellas);
        progressText = findViewById(R.id.progress_text);
        hint = findViewById(R.id.button_hint);
        progressBar = findViewById(R.id.progress_bar);

        actualizarScreen();
        actualizarContadorAcertadas();
        actualizarContadorHints();
        actualizarProgressText();
        resetearColorBotones();

        myCountDownTimer = new MyCountDownTimer(10000, 500);
        myCountDownTimer.start();

        respuesta1.setOnClickListener(this);
        respuesta2.setOnClickListener(this);
        respuesta3.setOnClickListener(this);
        respuesta4.setOnClickListener(this);
        hint.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_hint:
                Toast.makeText(this, questionModels[contadorQuestionModel].getHint(), Toast.LENGTH_SHORT).show();
                if (!yaHasUsadoHint) {
                    hintsRemaining -= 1;
                    actualizarContadorHints();
                    yaHasUsadoHint = true;
                }
                break;
            case  R.id.answer1:
                responder(0, respuesta1);
                break;
            case  R.id.answer2:
                responder(1, respuesta2);
                break;
            case  R.id.answer3:
                responder(2, respuesta3);
                break;
            case  R.id.answer4:
                responder(3, respuesta4);
                break;
        }

    }

    public void actualizarScreen() {
        pregunta.setText(questionModels[contadorQuestionModel].getPregunta());
        preguntaImagen.setImageResource(questionModels[contadorQuestionModel].getImagen());
        int[] botones = questionModels[contadorQuestionModel].getTextoBotones();
        respuesta1.setText(botones[0]);
        respuesta2.setText(botones[1]);
        respuesta3.setText(botones[2]);
        respuesta4.setText(botones[3]);
    }

    public void responder(int respuesta, Button b) {
        if (!yaHasRespondido) {
            if (questionModels[contadorQuestionModel].hasAcertado(respuesta)) {
                setColorBotonTrue(b);
                if (!yaHasUsadoHint) puntuacion += 1;
                contadorEstrella++;
            } else {
                setColorBotonFalse(b);
                switch (questionModels[contadorQuestionModel].getAnswer()) {
                    case 0:
                        setColorBotonTrue(respuesta1); break;
                    case 1:
                        setColorBotonTrue(respuesta2); break;
                    case 2:
                        setColorBotonTrue(respuesta3); break;
                    case 3:
                        setColorBotonTrue(respuesta4); break;
                }
                puntuacion -= 0.5;
            }
            questionsAnswered++;
            contadorQuestionModel++;
            yaHasRespondido = true;
        }
    }

    @SuppressLint("SetTextI18n")
    public void actualizarContadorAcertadas() {contadorAcertadas.setText("x"+ contadorEstrella);}

    @SuppressLint("SetTextI18n")
    public void actualizarContadorHints() {contadorHints.setText("x"+hintsRemaining);}

    @SuppressLint("SetTextI18n")
    public void actualizarProgressText() {progressText.setText("Pregunta "+(questionsAnswered+1)+" de 10");}

    @SuppressLint("ResourceAsColor")
    public void setColorBotonTrue(Button b) {b.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.has_respondido_bien));}

    @SuppressLint("ResourceAsColor")
    public void setColorBotonFalse(Button b) {b.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.has_respondido_mal));}

    public void resetearColorBotones() {
        respuesta1.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.button_default));
        respuesta2.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.button_default));
        respuesta3.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.button_default));
        respuesta4.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.button_default));
    }


    public void alertDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Congratulations, you finished the quiz!");
        if (puntuacion < 0) puntuacion = 0;
        alertBuilder.setMessage("Score: "+(puntuacion*100)+"/100");
        alertBuilder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {finish();
            }});
        alertBuilder.setNegativeButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { reiniciar();
            }});
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public void reiniciar() {
        contadorQuestionModel = 0;
        hintsRemaining = 3;
        questionsAnswered = 0;
        contadorEstrella = 0;
        puntuacion = 0;
        yaHasRespondido = false;
        yaHasUsadoHint = false;
        resetearColorBotones();
        actualizarScreen();
        actualizarProgressText();
        myCountDownTimer.start();
    }


    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished/100);
            progressBar.setProgress(progress);
        }

        @Override
        public void onFinish() {
            if (contadorQuestionModel != questionModels.length - 1) {
                progressBar.setProgress(0);
                contadorQuestionModel++;
                if (yaHasRespondido) puntuacion -= 0.5;
                yaHasRespondido = false;
                yaHasUsadoHint = false;
                resetearColorBotones();
                actualizarScreen();
                actualizarProgressText();
                myCountDownTimer.start();
            } else {
                alertDialog();
            }
        }
    }
}