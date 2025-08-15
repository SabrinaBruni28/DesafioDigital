package io.github.desafiodigital.models;

import java.util.ArrayList;

public class NivelContexto extends Nivel {
    private ArrayList<Carta> opcoes;
    private Cenario cenario;
    private int idResposta;

    // Construtor
    public NivelContexto(
            int pontuacaoInicial, 
            float tempoMinimo, float tempoMaximo, 
            int bonusTempo, int penalidadeTempo, 
            int erroMaximo, int penalidadeErroCenario,
            Cenario cenario, ArrayList<Carta> opcoes, int idResposta) {
                
        super(pontuacaoInicial, tempoMinimo, tempoMaximo, bonusTempo, penalidadeTempo, 
            erroMaximo, penalidadeErroCenario);
        this.opcoes = opcoes;
        this.cenario = cenario;
        this.idResposta = idResposta;
    }

    // Validação da resposta
    public boolean validarResposta(int idRespostaEscolhida) {
        return idRespostaEscolhida == idResposta;
    }

    // Getters e Setters
    public Cenario getCenario() {
        return cenario;
    }

    public void setCenario(Cenario cenario) {
        this.cenario = cenario;
    }

    public ArrayList<Carta> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(ArrayList<Carta> opcoes) {
        this.opcoes = opcoes;
    }

    public int getIdResposta() {
        return idResposta;
    }

    public void setIdResposta(int idResposta) {
        this.idResposta = idResposta;
    }

    @Override
    public String toString() {
        return "NivelContexto [\nopcoes=" + opcoes.toString() + ", \ncenario=" + cenario + ", \nidResposta=" + idResposta
                + ", \ngetPontuacaoInicial()=" + getPontuacaoInicial() + ", \ngetTempoMinimo()="
                + getTempoMinimo() + ", getTempoMaximo()=" + getTempoMaximo() + ", getBonusTempo()=" + getBonusTempo()
                + ", \ngetPenalidadeTempo()=" + getPenalidadeTempo() + ", \ngetErroMaximo()=" + getErroMaximo()
                + ", \ngetPenalidadeErro()=" + getPenalidadeErro() + "]";
    }
}
