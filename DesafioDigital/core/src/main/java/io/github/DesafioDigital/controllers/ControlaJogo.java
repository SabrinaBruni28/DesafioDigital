package io.github.DesafioDigital.controllers;

import io.github.DesafioDigital.views.TelaIntroducaoFase;
import io.github.DesafioDigital.models.ConfiguracaoFase;
import io.github.DesafioDigital.views.TelaFaseContexto;
import io.github.DesafioDigital.models.NivelContexto;
import io.github.DesafioDigital.views.TelaFaseCartas;
import io.github.DesafioDigital.models.NivelPartida;
import io.github.DesafioDigital.models.NivelCarta;
import io.github.DesafioDigital.views.TelaTitulo;
import io.github.DesafioDigital.DesafioDigital;
import io.github.DesafioDigital.utils.Arquivos;
import io.github.DesafioDigital.models.Nivel;
import io.github.DesafioDigital.models.Fase;
import com.badlogic.gdx.Screen;
import java.util.ArrayList;

/** Classe responsável por fazer o fluxo do entre as fases e níveis do jogo. */
public class ControlaJogo {
    private int qntFases;
    private int faseAtual;
    private int nivelAtual;
    private Fase fase;
    private ConfiguracaoFase configFase;

    public ControlaJogo(int qntFases) {
        this.qntFases = qntFases;
        iniciaJogo();
    }

    /** Calcula o próximo nível e retorna a próxima tela. */
    public Screen decideNextNivel(final DesafioDigital game) {

        if (nivelAtual < configFase.getQntNivel()) {
            nextNivel();
            iniciaNivelPartida(game);
            return witchTela(game);
        }
        else {
            if (faseAtual == qntFases ) {
                iniciaJogo();
                return new TelaTitulo(game);
            }
            nextFase();
            return new TelaIntroducaoFase(game);
        }
    }

    public void configuraFase() {
        configFase = Arquivos.readConfiguracaoFaseFromFile(Arquivos.arquivosFase(faseAtual));
    }

    public void nextFase() {
        faseAtual++;
        nivelAtual = 0;
        configuraFase();
        carregaFase();
    }

    public void nextNivel() {
        nivelAtual++;
        carregaNivel();
    }

    public void iniciaJogo(){
        resetJogo();
        configuraFase();
        carregaFase();
    }

    public void resetJogo(){
        faseAtual = 1;
        nivelAtual = 0;
    }

    public Screen witchTela(final DesafioDigital game){
        if (configFase.getTipoFase().equals("cartas")) return new TelaFaseCartas(game);
        else if (configFase.getTipoFase().equals("contexto")) return new TelaFaseContexto(game);
        else return null;
    }

    public void iniciaNivelPartida(final DesafioDigital game) {
        NivelPartida partida = game.getNivelPartida();
        partida.zerar();
        partida.setFase(faseAtual);
        partida.setNivel(nivelAtual);
    }

    public Fase obterFaseAtual() {
        return fase;
    }

    public Nivel obterNivelAtual() {
        int size = obterFaseAtual().getNiveis().size();
        int circularIndex = ((nivelAtual-1) % size + size) % size;
        if ((circularIndex >= 0) && (circularIndex < size)) return obterFaseAtual().obterNivel(circularIndex);
        System.err.println("Index out of range.");
        return null;
    }

    public NivelCarta obterNivelCarta() {
        return (NivelCarta) obterNivelAtual();
    }

    public NivelContexto obterNivelContexto() {
        return (NivelContexto) obterNivelAtual();
    }

    public void carregaFase() {
        fase = Arquivos.readFaseFromFile(Arquivos.arquivosFase(faseAtual));
        obterFaseAtual().setNiveis(new ArrayList<Nivel>());
    }
        
    public void carregaNivel() {
        ArrayList<Nivel> nivel = obterFaseAtual().getNiveis();

        nivel.add(
            Arquivos.readNivelFromFile(
                Arquivos.arquivosFaseNivel(faseAtual, nivelAtual),
                configFase.getTipoFase()
            )
        );
    }

    public int getFaseAtual() {
        return faseAtual;
    }

    public int getNivelAtual() {
        return nivelAtual;
    }

    public ConfiguracaoFase getConfigFase() {
        return configFase;
    }
}
