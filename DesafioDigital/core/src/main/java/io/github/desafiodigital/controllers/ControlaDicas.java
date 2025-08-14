package io.github.desafiodigital.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ControlaDicas {
    
    // Controladores de botões.
    private ControlaBotao controlaBotaoEsquerdo, controlaBotaoDireito, controlaBotaoLampada;

    // Variáveis de controle das dicas.
    private int qntDicas, whichDica, limiteDicas;

    public ControlaDicas(
            int limiteDicas, 
            ControlaBotao controlaBotaoEsquerdo, 
            ControlaBotao controlaBotaoDireito,
            ControlaBotao controlaBotaoLampada) {

        qntDicas = -1;
        whichDica = 0;
        this.limiteDicas = limiteDicas;

        this.controlaBotaoDireito = controlaBotaoDireito;
        this.controlaBotaoEsquerdo = controlaBotaoEsquerdo;
        this.controlaBotaoLampada = controlaBotaoLampada;

        controlaBotaoEsquerdo.setAtivo(false);
        controlaBotaoDireito.setAtivo(false);
    }

    public void dispose() {
        // Libera os assets.
        this.controlaBotaoDireito.dispose();
        this.controlaBotaoEsquerdo.dispose();
        this.controlaBotaoLampada.dispose();
    }

    public boolean somaDica() {
        if (qntDicas < (limiteDicas - 1)) {
            qntDicas++;
            setWhichDica(qntDicas);
            inicialBotoes();
            return true;
        }
        setWhichDica(qntDicas);
        inicialBotoes();
        return false;
    }

    public void passaDicaDireita() {
            if (whichDica < qntDicas) {
                whichDica++;
                controlaBotaoEsquerdo.setAtivo(true);
                if (whichDica == qntDicas) controlaBotaoDireito.setAtivo(false);
            }
            else controlaBotaoDireito.setAtivo(false);
    }

    public void passaDicaEsquerda() {
        if (whichDica > 0) {
            whichDica--;
            controlaBotaoDireito.setAtivo(true);
            if (whichDica == 0) controlaBotaoEsquerdo.setAtivo(false);
        }
        else controlaBotaoEsquerdo.setAtivo(false);
    }

    public void drawBotoes(SpriteBatch batch) {
        controlaBotaoDireito.draw(batch);
        controlaBotaoEsquerdo.draw(batch);
    }

    public void drawLampada(SpriteBatch batch) {
        controlaBotaoLampada.draw(batch);
    }

    public void inicialBotoes() {
        if (qntDicas > 0) {
            controlaBotaoEsquerdo.setAtivo(true);
            controlaBotaoDireito.setAtivo(false);
        }
        else {
            controlaBotaoEsquerdo.setAtivo(false);
            controlaBotaoDireito.setAtivo(false);
        }
    }

    public ControlaBotao getControlaBotaoEsquerdo() {
        return controlaBotaoEsquerdo;
    }
    
    public ControlaBotao getControlaBotaoDireito() {
        return controlaBotaoDireito;
    }
    
    public int getQntDicas() {
        return qntDicas;
    }
    
    public int getLimiteDicas() {
        return limiteDicas;
    }
    
    public int getWhichDica() {
        return whichDica;
    }
    
    public ControlaBotao getControlaBotaoLampada() {
        return controlaBotaoLampada;
    }

    public void setControlaBotaoEsquerdo(ControlaBotao controlaBotaoEsquerdo) {
        this.controlaBotaoEsquerdo = controlaBotaoEsquerdo;
    }

    public void setControlaBotaoDireito(ControlaBotao controlaBotaoDireito) {
        this.controlaBotaoDireito = controlaBotaoDireito;
    }

    public void setQntDicas(int qntDicas) {
        this.qntDicas = qntDicas;
    }

    public void setLimiteDicas(int limiteDicas) {
        this.limiteDicas = limiteDicas;
    }

    public void setControlaBotaoLampada(ControlaBotao controlaBotaoLampada) {
        this.controlaBotaoLampada = controlaBotaoLampada;
    }

    public void setWhichDica(int whichDica) {
        this.whichDica = whichDica;
    }
}
