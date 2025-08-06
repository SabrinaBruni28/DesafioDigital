package io.github.DesafioDigital.models;

public abstract class Nivel {
    private int pontuacaoInicial;
    private float tempoMinimo;
    private float tempoMaximo;
    private int bonusTempo;
    private int penalidadeTempo;
    private int erroMaximo;
    private int penalidadeErro;

    public Nivel(
            int pontuacaoInicial, 
            float tempoMinimo, float tempoMaximo, 
            int bonusTempo, int penalidadeTempo, 
            int erroMaximo, int penalidadeErro) {

        this.pontuacaoInicial = pontuacaoInicial;
        this.tempoMinimo = tempoMinimo;
        this.tempoMaximo = tempoMaximo;
        this.bonusTempo = bonusTempo;
        this.penalidadeTempo = penalidadeTempo;
        this.erroMaximo = erroMaximo;
        this.penalidadeErro = penalidadeErro;
    }

    public int calcularPontuacao(int qtdErros, float tempoGasto) {
        int penalidadePorErros = Math.min(qtdErros, erroMaximo) * penalidadeErro;
        int bonusOuPenalidadeTempo = 0;

        if (tempoGasto <= tempoMinimo) bonusOuPenalidadeTempo = bonusTempo;
        else if (tempoGasto > tempoMaximo) bonusOuPenalidadeTempo = penalidadeTempo;
        
        return (pontuacaoInicial + penalidadePorErros + bonusOuPenalidadeTempo);
    }

    public int getPontuacaoInicial() {
        return pontuacaoInicial;
    }

    public float getTempoMinimo() {
        return tempoMinimo;
    }

    public float getTempoMaximo() {
        return tempoMaximo;
    }

    public int getBonusTempo() {
        return bonusTempo;
    }

    public int getPenalidadeTempo() {
        return penalidadeTempo;
    }

    public int getErroMaximo() {
        return erroMaximo;
    }

    public int getPenalidadeErro() {
        return penalidadeErro;
    }

    public void setBonusTempo(int bonusTempo) {
        this.bonusTempo = bonusTempo;
    }

    public void setPenalidadeTempo(int penalidadeTempo) {
        this.penalidadeTempo = penalidadeTempo;
    }

    public void setPenalidadeErro(int penalidadeErro) {
        this.penalidadeErro = penalidadeErro;
    }
}

