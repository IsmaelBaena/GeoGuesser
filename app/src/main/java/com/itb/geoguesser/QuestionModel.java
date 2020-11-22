package com.itb.geoguesser;

public class QuestionModel {
    private int answer, imagen, pregunta;
    private int[] textoBotones;
    private String hint;

    public QuestionModel(int answer, int imagen, int pregunta, int[] textoBotones, String hint) {
        this.answer = answer;
        this.imagen = imagen;
        this.pregunta = pregunta;
        this.textoBotones = textoBotones;
        this.hint = hint;
    }

    public int getAnswer() {return answer;}

    public boolean hasAcertado(int respuesta) {return respuesta == answer;}

    public int getPregunta() {return pregunta;}

    public String getHint() {return hint;}

    public int getImagen() {return imagen;}

    public int[] getTextoBotones() {return textoBotones;}
}
